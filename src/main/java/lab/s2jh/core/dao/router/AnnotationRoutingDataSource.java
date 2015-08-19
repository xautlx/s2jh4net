package lab.s2jh.core.dao.router;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

/**
 * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
 */
public class AnnotationRoutingDataSource extends AbstractDataSource implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationRoutingDataSource.class);

    private final static RandomDataGenerator randomDataGenerator = new RandomDataGenerator();

    private Map<Object, Object> targetDataSources;

    private Object defaultTargetDataSource;

    private Map<Object, List<DataSource>> resolvedDataSources;

    private DataSource resolvedDefaultDataSource;

    /**
     * Specify the map of target DataSources, with the lookup key as key.
     * The mapped value can either be a corresponding {@link javax.sql.DataSource}
     * instance or a data source name String (to be resolved via a
     * {@link #setDataSourceLookup DataSourceLookup}).
     * <p>The key can be of arbitrary type; this class implements the
     * generic lookup process only. The concrete key representation will
     * be handled by {@link #resolveSpecifiedLookupKey(Object)} and
     * {@link #determineCurrentLookupKey()}.
     */
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        this.targetDataSources = targetDataSources;
    }

    /**
     * Specify the default target DataSource, if any.
     * <p>The mapped value can either be a corresponding {@link javax.sql.DataSource}
     * instance or a data source name String (to be resolved via a
     * {@link #setDataSourceLookup DataSourceLookup}).
     * <p>This DataSource will be used as target if none of the keyed
     * {@link #setTargetDataSources targetDataSources} match the
     * {@link #determineCurrentLookupKey()} current lookup key.
     */
    public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
        this.defaultTargetDataSource = defaultTargetDataSource;
    }

    /**
     * Retrieve the current target DataSource. Determines the
     * {@link #determineCurrentLookupKey() current lookup key}, performs
     * a lookup in the {@link #setTargetDataSources targetDataSources} map,
     * falls back to the specified
     * {@link #setDefaultTargetDataSource default target DataSource} if necessary.
     * @see #determineCurrentLookupKey()
     */
    protected DataSource determineTargetDataSource() {
        Assert.notNull(this.resolvedDataSources, "DataSource router not initialized");
        Object lookupKey = determineCurrentLookupKey();
        DataSource dataSource = resolvedDefaultDataSource;
        if (lookupKey != null) {
            List<DataSource> dataSources = this.resolvedDataSources.get(lookupKey);
            int index = 0;
            if (dataSources.size() > 1) {
                //如果对应多个数据源，则随机挑选一个
                index = randomDataGenerator.nextInt(0, dataSources.size() - 1);
            }
            dataSource = dataSources.get(index);
        }
        //logger.debug("Using lookupKey: {}, datasource: {}", lookupKey, dataSource);
        if (dataSource == null) {
            throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
        }
        return dataSource;
    }

    protected Object determineCurrentLookupKey() {
        return RoutingDataSourceAdvice.getDatasource();
    }

    @Override
    public Connection getConnection() throws SQLException {
        DataSource dataSource = determineTargetDataSource();
        Connection connection = dataSource.getConnection();
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return determineTargetDataSource().getConnection(username, password);
    }

    /**
     * Resolve the given lookup key object, as specified in the
     * {@link #setTargetDataSources targetDataSources} map, into
     * the actual lookup key to be used for matching with the
     * {@link #determineCurrentLookupKey() current lookup key}.
     * <p>The default implementation simply returns the given key as-is.
     * @param lookupKey the lookup key object as specified by the user
     * @return the lookup key as needed for matching
     */
    protected Object resolveSpecifiedLookupKey(Object lookupKey) {
        return lookupKey;
    }

    /**
     * Resolve the specified data source object into a DataSource instance.
     * <p>The default implementation handles DataSource instances and data source
     * names (to be resolved via a {@link #setDataSourceLookup DataSourceLookup}).
     * @param dataSource the data source value object as specified in the
     * {@link #setTargetDataSources targetDataSources} map
     * @return the resolved DataSource (never {@code null})
     * @throws IllegalArgumentException in case of an unsupported value type
     */
    @SuppressWarnings("unchecked")
    protected List<DataSource> resolveSpecifiedDataSource(Object dataSource) throws IllegalArgumentException {
        if (dataSource instanceof DataSource) {
            return Lists.newArrayList((DataSource) dataSource);
        } else if (dataSource instanceof List) {
            return (List<DataSource>) dataSource;
        } else {
            throw new IllegalArgumentException("Illegal data source value - only [javax.sql.DataSource] and String supported: " + dataSource);
        }
    }

    @Override
    public void afterPropertiesSet() {
        if (this.targetDataSources == null) {
            throw new IllegalArgumentException("Property 'targetDataSources' is required");
        }
        this.resolvedDataSources = new HashMap<Object, List<DataSource>>(this.targetDataSources.size());
        for (Map.Entry<Object, Object> entry : this.targetDataSources.entrySet()) {
            Object lookupKey = resolveSpecifiedLookupKey(entry.getKey());
            List<DataSource> dataSource = resolveSpecifiedDataSource(entry.getValue());
            this.resolvedDataSources.put(lookupKey, dataSource);
        }
        if (this.defaultTargetDataSource != null) {
            this.resolvedDefaultDataSource = resolveSpecifiedDataSource(this.defaultTargetDataSource).get(0);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("defaultTargetDataSource is " + resolvedDefaultDataSource);
            for (Map.Entry<Object, List<DataSource>> me : resolvedDataSources.entrySet()) {
                logger.debug("datasource list of key: " + me.getKey());
                for (DataSource ds : me.getValue()) {
                    logger.debug(" - " + ds);
                }
            }
        }
    }

}
