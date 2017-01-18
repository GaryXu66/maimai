package maimai.app.redis;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class RedisClient implements ApplicationListener<ContextRefreshedEvent> {
	@Resource
	private RedisTemplate<String, String> redisTemplate;

	private static RedisSerializer<String> stringRedisSerializer;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		stringRedisSerializer = redisTemplate.getStringSerializer();
	}

	private byte[] getByte(String chars) {
		return stringRedisSerializer.serialize(chars);
	}

	private String byteToString(byte[] bData) {
		return stringRedisSerializer.deserialize(bData);
	}

	/**
	 * 设置单个值,覆盖旧的数值
	 * 
	 * @param key
	 * @param value
	 * @return
	 * @return
	 */
	public Boolean set(final String key, final String value) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				connection.set(getByte(key), getByte(value));
				return true;
			}
		});
	}

	/**
	 * 获取单个值
	 * 
	 * @param key
	 * @return
	 */
	public String get(final String key) {
		return redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				return byteToString(connection.get(getByte(key)));
			}
		});
	}

	/**
	 * 删除键
	 * 
	 * @param key
	 * @return
	 */
	public Long del(final String key) {
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.del(getByte(key));
			}
		});
	}

	/**
	 * 检查Key是否存在
	 * 
	 * @param key
	 * @return
	 */
	public Boolean exists(final String key) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.exists(getByte(key));
			}
		});
	}

	/**
	 * 在某段时间后失效
	 * 
	 * @param key
	 * @param seconds
	 * @return
	 */
	public Boolean expire(final String key, final int seconds) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.expire(getByte(key), seconds);
			}
		});
	}

	/**
	 * 在某个时间点失效
	 * 
	 * @param key
	 * @param unixTime
	 * @return
	 */
	public Boolean expireAt(final String key, final long unixTime) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.expireAt(getByte(key), unixTime);
			}
		});
	}

	/**
	 * 同步设置
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Boolean setNx(final String key, final String value) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.setNX(getByte(key), getByte(value));
			}
		});
	}

	/**
	 * 同步设置缓存并设定过期时间
	 * 
	 * @param key
	 * @param seconds
	 * @param value
	 * @return
	 */
	public Boolean setNx(final String key, final long seconds, final String value) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				boolean result = connection.setNX(getByte(key), getByte(value));
				if (result) {
					connection.expire(getByte(key), seconds);
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 设置值和过期时间
	 * 
	 * @param key
	 * @param seconds
	 * @param value
	 * @return
	 */
	public Boolean setEx(final String key, final long seconds, final String value) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				connection.setEx(getByte(key), seconds, getByte(value));
				return true;
			}
		});
	}
}
