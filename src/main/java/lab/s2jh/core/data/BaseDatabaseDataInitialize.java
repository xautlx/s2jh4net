package lab.s2jh.core.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import lab.s2jh.core.util.DateUtils;
import lab.s2jh.support.service.DynamicConfigService;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

/**
 * 数据库基础数据初始化处理器
 */
public abstract class BaseDatabaseDataInitialize {

    private final static Logger logger = LoggerFactory.getLogger(BaseDatabaseDataInitialize.class);

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private DynamicConfigService dynamicConfigService;

    private Date startTime;

    @Transactional
    public void initialize() {
        startTime = new Date();
        String hbm2ddl = dynamicConfigService.getString("hibernate_hbm2ddl_auto");
        if ("create-drop".equalsIgnoreCase(hbm2ddl)) {
            logger.debug("Invoke hibernate sessionFactory.close() to trigger drop tables as hbm2ddl={}.", hbm2ddl);
            SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
            sessionFactory.close();
            logger.debug("Hibernate hbm2ddl drop done, force System.exit()");
            System.exit(0);
        }

        final String className = this.getClass().getName();
        Thread logThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Date now = new Date();
                        logger.info("Passed time " + DateUtils.getHumanDisplayForTimediff(now.getTime() - startTime.getTime()) + " to run "
                                + className);
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
        logThread.start();

        initializeInternal();

        logThread.interrupt();
    }

    protected String getStringFromTextFile(String fileName) {
        InputStream is = this.getClass().getResourceAsStream(fileName);
        try {
            String text = StringUtils.join(IOUtils.readLines(is, "GBK"), "\n");
            return text;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * 提交当前事务并新起一个事务
     */
    protected void commitAndResumeTransaction() {

        entityManager.flush();
        entityManager.clear();

        Session session = entityManager.unwrap(org.hibernate.Session.class);

        //提交当前事务
        Transaction existingTransaction = session.getTransaction();
        existingTransaction.commit();
        Assert.isTrue(existingTransaction.wasCommitted(), "Transaction should have been committed.");

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
