package maimai.app.service.impl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.plexus.util.StringUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import maimai.app.base.BaseLogger;
import maimai.app.service.IBaseService;

@Service
public class BaseServiceImpl<T> extends BaseLogger implements IBaseService{
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public <T> T getEntityById(long id, Class t){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tableName", classNameToTableName(t.getSimpleName()));
		params.put("id", id);
		Map<String, Object> result = sqlSessionTemplate.selectOne("maimai.app.dao.BaseDAO.getEntityById", params);
		Set<Entry<String, Object>> entrySet = result.entrySet();
		Field[] entityFiles = t.getDeclaredFields();
		Object entity;
		try {
			entity = Class.forName(t.getName()).newInstance();
		} catch (Exception e1) {
			e1.printStackTrace();
			return null;
		}
		for(Field field : entityFiles){
			String fieldName = field.getName();
			Object val = getValFromEntrySet(entrySet, fieldName);
			if(null == val){
				continue;
			}
			try {
				field.setAccessible(true);
				field.set(entity, val);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return (T) entity;
	}
	
	private Object getValFromEntrySet(Set<Entry<String, Object>> entrySet, String key){
		for(Entry<String, Object> entry : entrySet){
			if(StringUtils.isNotBlank(key) && key.equals(entry.getKey())){
				return entry.getValue();
			}
		}
		return null;
	}
	
	private String classNameToTableName(String className){
		Pattern pattern = Pattern.compile("[A-Z]");
		StringBuffer sb = new StringBuffer((className.length())*2+1);
		sb.append("t_");
		for(int i=0; i<className.length();i++){
			String ele = String.valueOf(className.charAt(i));
			Matcher matcher = pattern.matcher(ele);
			if(matcher.matches()){
				if(sb.length() == 2){
					sb.append(ele.toLowerCase());
				}else{
					sb.append("_").append(ele.toLowerCase());
				}
			}else{
				sb.append(ele);
			}
		}
		return sb.toString();
	}
}
