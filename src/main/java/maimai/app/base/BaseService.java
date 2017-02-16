package maimai.app.base;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseService<T> extends BaseLogger {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	public T getEntityById(long id, T t){
		String simpleName = t.getClass().getSimpleName();
		String statement = "select * from t_"+simpleName.toLowerCase() + " where id="+id;
		Object result = sqlSessionTemplate.selectOne(statement);
		return (T) result;
	}
}
