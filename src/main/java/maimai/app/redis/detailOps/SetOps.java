package maimai.app.redis.detailOps;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

@Component
public class SetOps{
	@Autowired
	protected RedisTemplate<String, String> template;
	
	@Resource(name="redisTemplate")
	private SetOperations<String, String> setOps;// inject the template as SetOperations
	
	public Boolean setAdd(String setKey, String item){
		return setOps.add(setKey, item);
	}
	public String setGet(String setKey){
		return setOps.pop(setKey);
	}
}
