package maimai.app.redis.detailOps;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class ValueOps{
	@Autowired
	protected RedisTemplate<String, String> template;
	
	@Resource(name="redisTemplate")
    private ValueOperations<String, String> valueOps; // inject the template as SetOperations
	
	public void set(String key, String val){
		valueOps.set(key, val);
	}
	public Boolean setNX(String key, String value){
		return valueOps.setIfAbsent(key, value);
	}
	public void setExpire(String key, String val, long sec_time){
		valueOps.set(key, key, sec_time);
	}
	public String get(String key){
		return valueOps.get(key);
	}
}
