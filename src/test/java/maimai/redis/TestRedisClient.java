package maimai.redis;

import javax.annotation.Resource;

import org.junit.Test;

import maimai.app.redis.JedisClient;
import maimai.app.redis.RedisClient;
import maimai.app.redis.detailOps.ListOps;
import maimai.app.redis.detailOps.SetOps;
import maimai.app.redis.detailOps.ValueOps;
import maimai.base.BaseTest;

public class TestRedisClient extends BaseTest{
	@Resource
	private RedisClient redisClient;
	@Resource
	private JedisClient jedisClient;
	
	@Resource
	private ValueOps valueOps;
	@Resource
	private ListOps listOps;
	@Resource
	private SetOps setOps;
	
	
	@Test
	public void add(){
		Boolean effect = redisClient.set("xuheng1", "徐衡");
		System.out.println("添加结果:"+effect);
	}
	
	@Test
	public void setIfNX(){
		Boolean result = valueOps.setNX("xuheng2", "gary");
		System.out.println("执行结果:"+result);
	}
	
	@Test
	public void get(){
		String result = valueOps.get("xuheng2");
		System.out.println("执行结果:"+result);
	}
	
	@Test
	public void testGetALl(){
		long size = listOps.listSize("mylist4");
		StringBuffer sb = new StringBuffer(); 
		for(long i =0; i<size;i++){
			if(sb.length()>0){
				sb.append(",");
			}
			sb.append(listOps.getByIndex("mylist4", i));
		}
		System.out.println(sb);
	}
	
	@Test
	public void testPopAndPush(){
		String listKey = "testxuheng2";
		listOps.leftPush(listKey, "111");
		listOps.leftPush(listKey, "222");
		listOps.leftPush(listKey, "333");
		listOps.leftPush(listKey, "444");
		listOps.leftPush(listKey, "555");
		System.out.println("size:--"+listOps.listSize(listKey));
		listOps.leftPop(listKey);
		System.out.println("size-1:--"+listOps.listSize(listKey));
		listOps.leftPop(listKey);
		listOps.leftPop(listKey);
		System.out.println("size-2:--"+listOps.listSize(listKey));
	}
	
	@Test
	public void testSetByJedis(){
		String result = jedisClient.set("hahah", "xuheng hah");
		System.out.println("执行结果:"+result);
	}
	
	@Test
	public void getByJedis(){
		String result = jedisClient.get("hahah");
		System.out.println("执行结果:"+result);
	}
}
