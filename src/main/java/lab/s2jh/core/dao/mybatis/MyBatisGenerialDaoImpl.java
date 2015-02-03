package lab.s2jh.core.dao.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.github.loafer.mybatis.pagination.PaginationInterceptor;

@Component
public class MyBatisGenerialDaoImpl implements MyBatisDao {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public <T> T findOne(String namespace, String statementId, Object parameter) {
        String statement = namespace + "." + statementId;
        return sqlSession.selectOne(statement, parameter);
    }

    @Override
    public <E> List<E> findList(String namespace, String statementId, Object parameters) {
        String statement = namespace + "." + statementId;
        return sqlSession.selectList(statement, parameters);
    }

    @Override
    public <E> Page<E> findPage(String namespace, String statementId, Map<String, Object> parameters, Pageable pageable) {
        String statement = namespace + "." + statementId;
        RowBounds rowBounds = new RowBounds(pageable.getOffset(), pageable.getPageSize());
        PaginationInterceptor.setPaginationOrderby(pageable.getSort());
        List<E> rows = sqlSession.selectList(statement, parameters, rowBounds);
        int total = PaginationInterceptor.getPaginationTotal();
        Page<E> page = new PageImpl<E>(rows, pageable, total);
        return page;
    }

    @Override
    public <K, V> Map<K, V> findMap(String namespace, String statementId, Object parameter, String mapKey) {
        String statement = namespace + "." + statementId;
        return sqlSession.selectMap(statement, parameter, mapKey);
    }

    @Override
    public int execute(String namespace, String statementId, Object parameter) {
        String statement = namespace + "." + statementId;
        return sqlSession.update(statement, parameter);
    }
}
