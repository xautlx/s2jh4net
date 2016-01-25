package lab.s2jh.core.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.persistence.EntityManager;

import lab.s2jh.core.util.DateUtils;
import lab.s2jh.support.service.DynamicConfigService;

import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

/**
 * 数据库基础数据初始化处理器
 */
public abstract class BaseDatabaseDataInitialize {

    private final static Logger logger = LoggerFactory.getLogger(BaseDatabaseDataInitialize.class);

    private EntityManager entityManager;

    public void initialize(EntityManager entityManager) {
        this.entityManager = entityManager;

        logger.debug("Invoking data process for {}", this);
        initializeInternal();

        //确保最后提交事务
        commitAndResumeTransaction();

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

    public abstract void initializeInternal();
}
