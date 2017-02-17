package maimai.app.dao;

import java.util.Map;

public interface BaseDAO<T>{
	
	<T> T getEntityById(Map<String, Object> map);
}
