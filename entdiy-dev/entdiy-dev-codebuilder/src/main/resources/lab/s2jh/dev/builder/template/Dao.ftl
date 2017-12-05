package ${root_package}.dao;


import com.entdiy.core.dao.jpa.BaseDao;

import org.springframework.stereotype.Repository;

import ${root_package}.entity.${entity_name};

@Repository
public interface ${entity_name}Dao extends BaseDao<${entity_name}, ${id_type}> {

}