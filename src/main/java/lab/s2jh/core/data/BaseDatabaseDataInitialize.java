package lab.s2jh.core.data;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import lab.s2jh.core.util.DateUtils;
import lab.s2jh.support.service.DynamicConfigService;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 数据库基础数据初始化处理器
 */
public abstract class BaseDatabaseDataInitialize {

    private final static Logger logger = LoggerFactory.getLogger(BaseDatabaseDataInitialize.class);

    private EntityManager entityManager;

    public void initialize(EntityManager entityManager) {
        this.entityManager = entityManager;

        initializeInternal();

        if (DynamicConfigService.isDevMode()) {
            //重置恢复模拟数据设置的临时时间
            DateUtils.setCurrentDate(null);
        }
    }

    /**
     * 帮助类方法，从当前类的classpath路径下面读取文本内容为String字符串
     * @param fileName 文件名称
     * @return
     */
    protected String getStringFromTextFile(String fileName) {
        InputStream is = this.getClass().getResourceAsStream(fileName);
        try {
            String text = IOUtils.toString(is, "UTF-8");
            return text;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    protected int executeNativeSQL(String sql) {
        return entityManager.createNativeQuery(sql).executeUpdate();
    }

    /**
     * 查询整个数据对象表
     */
    @SuppressWarnings("unchecked")
    protected <X> List<X> findAll(Class<X> entity) {
        return entityManager.createQuery("from " + entity.getSimpleName()).getResultList();
    }

    /**
     * 获取表数据总记录数
     */
    protected int countTable(Class<?> entity) {
        Object count = entityManager.createQuery("select count(1) from " + entity.getSimpleName()).getSingleResult();
        return Integer.valueOf(String.valueOf(count));
    }

    /**
     * 判定实体对象对应表是否为空
     */
    protected boolean isEmptyTable(Class<?> entity) {
        Object count = entityManager.createQuery("select count(1) from " + entity.getSimpleName()).getSingleResult();
        if (count == null || String.valueOf(count).equals("0")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 提交当前事务并新起一个事务
     */
    protected void commitAndResumeTransaction() {
        Session session = entityManager.unwrap(org.hibernate.Session.class);

        //提交当前事务
        Transaction existingTransaction = session.getTransaction();
        existingTransaction.commit();
        Assert.isTrue(existingTransaction.wasCommitted(), "Transaction should have been committed.");
        entityManager.clear();

        // Cannot reuse existing Hibernate transaction, so start a new one.
        Transaction newTransaction = session.beginTransaction();

        // Now need to update Spring transaction infrastructure with new Hibernate transaction.
        HibernateEntityManagerFactory emFactory = (HibernateEntityManagerFactory) entityManager.getEntityManagerFactory();
        SessionFactory sessionFactory = emFactory.getSessionFactory();
        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
        if (sessionHolder == null) {
            sessionHolder = new SessionHolder(session);
            TransactionSynchronizationManager.bindResource(sessionFactory, sessionHolder);
        }
        sessionHolder.setTransaction(newTransaction);
    }

    /**
     * 读取Excel数据内容
     * 约定格式要求：第一行为标题行，之后为数据行
     * 返回结构为Map结构的List集合：每行的key=第一行的标题，value=单元格值，统一为字符串，根据需要自行转换数据类型
     * 
     * @param InputStream
     * @return Map 包含单元格数据内容的Map对象
     */
    protected List<Map<String, String>> readExcelContent(String excelName, String sheetName) {
        List<Map<String, String>> rows = Lists.newArrayList();
        InputStream is = null;
        try {
            is = this.getClass().getResourceAsStream(excelName);
            Workbook wb = new HSSFWorkbook(is);
            logger.debug("Excel: {}, Sheet: {}", excelName, sheetName);
            Sheet sheet = wb.getSheet(sheetName);
            Row row0 = sheet.getRow(0);
            // 标题总列数
            int colNum = 0;
            List<String> titleList = Lists.newArrayList();
            while (true) {
                Cell cell = row0.getCell(colNum);
                if (cell == null) {
                    break;
                }
                String title = getCellFormatValue(cell);
                if (StringUtils.isBlank(title)) {
                    break;
                }
                titleList.add(title);
                colNum++;
                logger.debug(" - Title : {} = {}", colNum, title);
            }
            logger.debug("Excel: {}, Sheet: {}, Column Num: {}", excelName, sheetName, colNum);
            String[] titles = titleList.toArray(new String[titleList.size()]);

            // 正文内容应该从第二行开始,第一行为表头的标题
            int rowNum = 1;
            while (rowNum > 0) {
                Row row = sheet.getRow(rowNum++);
                if (row == null) {
                    break;
                }
                Map<String, String> rowMap = Maps.newHashMap();
                rowMap.put("sheetName", sheetName);

                Cell firstCell = row.getCell(0);
                //假如第一列并且为空则终止行项数据处理
                if (firstCell == null) {
                    logger.info("End as firt cell is Null at row: {}", rowNum);
                    break;
                }

                int j = 0;
                while (j < colNum) {
                    Cell cell = row.getCell(j);
                    if (cell != null) {
                        String cellValue = getCellFormatValue(cell);
                        rowMap.put(titles[j], cellValue);
                    }
                    j++;
                }
                if (rowNum > 0) {
                    rows.add(rowMap);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        logger.debug("Row Map Data: {}", rows);
        return rows;
    }

    /**
     * 根据HSSFCell类型设置数据
     * 
     * @param cell
     * @return
     */
    private static String getCellFormatValue(Cell cell) {
        String cellvalue = null;
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
            // 如果当前Cell的Type为NUMERIC
            case Cell.CELL_TYPE_NUMERIC:
            case Cell.CELL_TYPE_FORMULA: {
                // 判断当前的cell是否为Date
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 如果是Date类型则，转化为Data格式

                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    //cellvalue = cell.getDateCellValue().toLocaleString();

                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    cellvalue = sdf.format(date);

                }
                // 如果是纯数字
                else {
                    // 取得当前Cell的数值
                    DecimalFormat df = new DecimalFormat("#.####");
                    cellvalue = df.format(cell.getNumericCellValue());
                }
                break;
            }
            // 如果当前Cell的Type为STRIN
            case Cell.CELL_TYPE_STRING:
                // 取得当前的Cell字符串
                cellvalue = cell.getRichStringCellValue().getString();
                break;
            }
        }
        if (cellvalue == null) {
            logger.warn("NULL cell value [{}, {}]", cell.getRowIndex(), cell.getColumnIndex());
        } else {
            cellvalue = cellvalue.trim();
        }
        return cellvalue;
    }

    public abstract void initializeInternal();
}
