package maimai.app.redis.detailOps;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
@Component
public class ListOps {
	/**
	 * 此处两个注入需要组合使用
	 */
	@Autowired
	protected RedisTemplate<String, String> template;
	// inject the template as SetOperations
	@Resource(name="redisTemplate")
    private ListOperations<String, String> listOps;
	
	public long leftPush(String listKey, String item){
		return listOps.leftPush(listKey, item);
	}
	public long rightPush(String listKey, String item){
		return listOps.rightPush(listKey, item);
	}
	public String leftPop(String listKey){
		return listOps.leftPop(listKey);
	}
	public String rightPop(String listKey){
		return listOps.rightPop(listKey);
	}
	public long listSize(String listKey){
		return listOps.size(listKey);
	}
	public String getByIndex(String listKey, long index){
		return listOps.index(listKey, index);
	}
}
