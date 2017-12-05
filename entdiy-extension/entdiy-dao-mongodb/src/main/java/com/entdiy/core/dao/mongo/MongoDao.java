package com.entdiy.core.dao.mongo;

import com.entdiy.core.pagination.GroupPropertyFilter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mongodb.DB;
import com.mongodb.DBObject;

public interface MongoDao {

    DB getDB();

    Page<DBObject> findPage(String collectionName, GroupPropertyFilter groupPropertyFilter, Pageable pageable, DBObject fields);

    boolean save(String collectionName, DBObject queryData, DBObject saveData);
}
