package lab.s2jh.core.dao.mongo;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import lab.s2jh.core.exception.ServiceException;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * @see http://mongodb.github.io/mongo-java-driver/2.13/getting-started/quick-tour/
 */
public class MongoGenerialDaoImpl implements MongoDao {

    @Value("${mongo_host}")
    private String mongoHost;

    @Value("${mongo_port:27017}")
    private String mongoPort;

    @Value("${mongo_dbname}")
    private String mongoDB;

    private MongoClient mongoClient;

    public void setMongoHost(String mongoHost) {
        this.mongoHost = mongoHost;
    }

    public void setMongoPort(String mongoPort) {
        this.mongoPort = mongoPort;
    }

    public void setMongoDB(String mongoDB) {
        this.mongoDB = mongoDB;
    }

    @PostConstruct
    public void init() {
        try {
            //MongoCredential credential = MongoCredential.createCredential(userName, database, password);
            //MongoClient mongoClient = new MongoClient(new ServerAddress(), Arrays.asList(credential));
            mongoClient = new MongoClient(mongoHost, Integer.valueOf(mongoPort));
        } catch (Exception e) {
            throw new ServiceException("Build MongoClient exception", e);
        }
    }

    @PreDestroy
    public void destroy() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    @Override
    public Page<DBObject> findPage(String collectionName, GroupPropertyFilter groupPropertyFilter, Pageable pageable, DBObject fields) {

        DB db = mongoClient.getDB(mongoDB);
        DBCollection dbColl = db.getCollection(collectionName);

        BasicDBObject query = new BasicDBObject();
        List<BasicDBObject> andQueries = Lists.newArrayList();
        List<PropertyFilter> filters = groupPropertyFilter.convertToPropertyFilters();
        if (CollectionUtils.isNotEmpty(filters)) {
            //Query and Projection Operators: https://docs.mongodb.org/manual/reference/operator/query/
            for (PropertyFilter filter : filters) {
                Object matchValue = filter.getMatchValue();
                if (matchValue == null) {
                    continue;
                }

                String[] propertyNames = filter.getConvertedPropertyNames();
                List<BasicDBObject> orQueries = Lists.newArrayList();
                for (String propertyName : propertyNames) {
                    BasicDBObject queryItem = new BasicDBObject();
                    orQueries.add(queryItem);
                    switch (filter.getMatchType()) {
                    case EQ:
                        queryItem.put(propertyName, matchValue);
                        break;
                    case NE:
                        queryItem.put(propertyName, new BasicDBObject("$ne", matchValue));
                        break;
                    case BK:
                        List<BasicDBObject> orList = Lists.newArrayList();
                        orList.add(new BasicDBObject(propertyName, 0));
                        orList.add(new BasicDBObject(propertyName, ""));
                        queryItem.put("$or", orList);
                        break;
                    case NB:
                        queryItem.put(propertyName, new BasicDBObject("$regex", ".{1,}"));
                        break;
                    case NU:
                        queryItem.put(propertyName, 0);
                        break;
                    case NN:
                        queryItem.put(propertyName, 1);
                        break;
                    case CN:
                        queryItem.put(propertyName, new BasicDBObject("$regex", ".*" + matchValue + ".*").append("$options", "i"));
                        break;
                    case NC:
                        queryItem.put(propertyName,
                                new BasicDBObject("$not", new BasicDBObject("$regex", ".*" + matchValue + ".*").append("$options", "i")));
                        break;
                    case BW:
                        queryItem.put(propertyName, new BasicDBObject("$regex", "^" + matchValue).append("$options", "i"));
                        break;
                    case BN:
                        queryItem.put(propertyName, new BasicDBObject("$not", new BasicDBObject("$regex", "^" + matchValue).append("$options", "i")));
                        break;
                    case EW:
                        queryItem.put(propertyName, new BasicDBObject("$regex", matchValue + "$").append("$options", "i"));
                        break;
                    case EN:
                        queryItem.put(propertyName, new BasicDBObject("$not", new BasicDBObject("$regex", matchValue + "$").append("$options", "i")));
                        break;
                    case BT:
                        Assert.isTrue(matchValue.getClass().isArray(), "Match value must be array");
                        Object[] matchValues = (Object[]) matchValue;
                        Assert.isTrue(matchValues.length == 2, "Match value must have two value");
                        List<BasicDBObject> andList = Lists.newArrayList();
                        andList.add(new BasicDBObject(propertyName, new BasicDBObject("$gte", matchValue)));
                        andList.add(new BasicDBObject(propertyName, new BasicDBObject("$gle", matchValue)));
                        queryItem.put("$and", andList);
                        break;
                    case GT:
                        queryItem.put(propertyName, new BasicDBObject("$gt", matchValue));
                        break;
                    case GE:
                        queryItem.put(propertyName, new BasicDBObject("$ge", matchValue));
                        break;
                    case LT:
                        queryItem.put(propertyName, new BasicDBObject("$lt", matchValue));
                        break;
                    case LE:
                        queryItem.put(propertyName, new BasicDBObject("$le", matchValue));
                        break;
                    case IN:
                        queryItem.put(propertyName, new BasicDBObject("$in", matchValue));
                        break;
                    default:
                        throw new UnsupportedOperationException("Undefined PropertyFilter MatchType: " + filter.getMatchType());
                    }
                }
                if (orQueries.size() > 1) {
                    andQueries.add(new BasicDBObject("$or", orQueries));
                } else {
                    andQueries.add(orQueries.get(0));
                }
            }
            query = new BasicDBObject("$and", andQueries);
        }

        BasicDBObject sort = new BasicDBObject();
        Sort pageSort = pageable.getSort();
        if (pageSort != null) {
            Iterator<Order> orders = pageSort.iterator();
            while (orders.hasNext()) {
                Order order = orders.next();
                String prop = order.getProperty();
                if (order.isAscending()) {
                    sort.put(prop, 1);
                } else {
                    sort.put(prop, -1);
                }
            }
        }

        DBCursor cur = dbColl.find(query, fields).sort(sort).skip(pageable.getOffset()).limit(pageable.getPageSize());
        List<DBObject> rows = Lists.newArrayList();
        while (cur.hasNext()) {
            DBObject obj = cur.next();
            rows.add(obj);
        }
        Page<DBObject> page = new PageImpl<DBObject>(rows, pageable, dbColl.count(query));
        return page;

    }

    @Override
    public boolean save(String collectionName, DBObject queryData, DBObject saveData) {
        DB db = mongoClient.getDB(mongoDB);
        DBCollection coll = db.getCollection(collectionName);
        coll.update(queryData, saveData, true, false);
        return false;
    }

    @Override
    public DB getDB() {
        return mongoClient.getDB(mongoDB);
    }

}
