package ${root_package}.service.test;

import java.util.List;

import ${root_package}.entity.${entity_name};
import ${root_package}.service.${entity_name}Service;
import com.entdiy.core.test.SpringTransactionalTestCase;
import com.entdiy.core.util.MockEntityUtils;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.google.common.collect.Sets;

public class ${entity_name}ServiceTest extends SpringTransactionalTestCase {

	@Autowired
	private ${entity_name}Service ${entity_name_uncapitalize}Service;

    @Test
    public void findByPage() {
        //Insert mock entity
        ${entity_name} entity = MockEntityUtils.buildMockObject(${entity_name}.class);
        ${entity_name_uncapitalize}Service.save(entity);
        Assert.assertTrue(entity.getId() != null);

        //JPA/Hibernate query validation
        List<${entity_name}> items = ${entity_name_uncapitalize}Service.findAll(entity.getId());
        Assert.assertTrue(items.size() >= 1);
    }
}