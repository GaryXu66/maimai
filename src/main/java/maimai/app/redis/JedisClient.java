package maimai.app.redis;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 采用此方式需要手动管理连接的获取和释放（从连接池获取，归还给连接池）
 * @author Administrator
 *
 */
@Component
public class JedisClient {

	@Resource
	private JedisPool jedisPool;
	
	public String set(String key, String val){
		Jedis jedis = null;
		try{
			jedis = getResource();
			return jedis.set(key, val);
		}catch(Exception e ){
			
		}finally{
			returnResource(jedis);
		}
		return "set error";
	}
	
	public String get(String key){
		Jedis jedis = null;
		try{
			jedis = getResource();
			return jedis.get(key);
		}catch(Exception e ){
			
		}finally{
			returnResource(jedis);
		}
		return null;
	
	}
	
	/**
	 * 从连接池获取连接
	 * @return
	 */
	private Jedis getResource(){
		Jedis jedis = null;
		try{
			jedis = jedisPool.getResource();
		}catch(Exception e){
			returnResource(jedis);
			throw e;
		}
		return jedis;
	}
	
	/**
	 * 归还连接
	 * @param jedis
	 */
	private void returnResource(Jedis jedis){
		if(null != jedis){
			jedisPool.returnResource(jedis);
		}
	}
}
