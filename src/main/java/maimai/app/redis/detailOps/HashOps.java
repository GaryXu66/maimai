package maimai.app.redis.detailOps;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
@Component
public class HashOps {
	/**
	 * 此处两个注入需要组合使用
	 */
	@Autowired
	protected RedisTemplate<String, String> template;
	// inject the template as HashOperations
	@Resource(name="redisTemplate")
    private HashOperations<String, String, String> hashOps;
	
	public void set(String hashKey, String itemKey, String itemVal){
		hashOps.put(hashKey, itemKey, itemVal);
	}
	
	public String get(String hashKey, String itemKey){
		return hashOps.get(hashKey, itemKey);
	}
	
	
}
