package maimai.app.util.lock;

import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 新增了分布式锁功能
 * @author Administrator
 *
 */
public class LockUtil {
	private static LockManage lockManage = LockManage.getInstance();
	private ReentrantLock value;
	private Date effectiveTime;
	private volatile static LockUtil lock = null;
//	private Lock(){}
	
	public static synchronized LockUtil getInstance() {  
        if (lock == null) { 
       	 synchronized(LockUtil.class){
       		 if (lock == null) { 
       			lock = new LockUtil();  
       		 }
       	 }
        }    
       return lock;  
   }
	
	/*
	 * 默认持续1分钟，如果该锁超过1分钟还未释放，系统将该锁从列表中删除
	 */
	private final static long defaultTime = 60L;

	private Date getEffectiveTime() {
		return effectiveTime;
	}

	public ReentrantLock getValue() {
		return value;
	}

	public void setValue(ReentrantLock value) {
		this.value = value;
	}

	private void setEffectiveTime(Date effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	/**
	 * 获取锁方法
	 * 调用该方法后，但业务执行完成，需要调用解锁方法解锁
	 * @param key 标识锁的唯一字段
	 */
	public void lock(String key){
//		overdueAll();
//		ReentrantLock reentrantLock = getLock(key,defaultTime);
//		reentrantLock.lock();
		setLock(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param waitTime 如果已经被lock，尝试等待waitTime秒，看是否可以获得锁，如果waitTime秒后仍然无法获得锁则返回false
	 * @return
	 */
	public boolean tryLock(String key,long waitTime){
		overdueAll();
		ReentrantLock reentrantLock = getLock(key,defaultTime);
		boolean isOk = false;
		try {
			isOk = reentrantLock.tryLock(waitTime, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return isOk;
	}
	
	private static void setLockCache(String key,ReentrantLock reentrantLock,long timer){
		LockUtil lock = new LockUtil();
		lock.setValue(reentrantLock);
		long nowTimes = new Date().getTime()+timer*1000;
		lock.setEffectiveTime(new Date(nowTimes));
		lockManage.put(key, lock);
	}
	
	private static synchronized ReentrantLock getLock(String key,long timer){
		ReentrantLock reentrantLock = null;
		LockUtil lock = (LockUtil)lockManage.get(key);
		if(lock==null||lock.getValue()==null){
			reentrantLock = new ReentrantLock();
			setLockCache(key, reentrantLock,timer);
		}else{
			reentrantLock = lock.getValue();
		}
		return reentrantLock;
	}
	
	/**
	 * 过期失效的锁清除，并释放锁
	 */
	private static void overdueAll() {
		if (lockManage != null && lockManage.size() > 0){
			Iterator<String> keys = lockManage.keySet().iterator();
			while(keys.hasNext()){
				String key = keys.next();
				LockUtil lock = lockManage.get(key) == null ? null: (LockUtil) lockManage.get(key);
				if (lock == null) {
					continue;
				}
				Date effectiveTime = lock.getEffectiveTime();
				if (effectiveTime != null && effectiveTime.getTime() > 0) {
					if (new Date().getTime() >= effectiveTime.getTime()) {
						keys.remove();
					}
				}
			}
		}
	} 
	
	
	/**
	 * 解锁方法
	 * @param key  标识锁的唯一字段
	 */
	public void unlock(String key){
		updateLockStatusForUnLock(key);
//		ReentrantLock reentrantLock = null;
//		Lock lock = (Lock)lockManage.get(key);
//		if(lock==null){
//		}else{
//			reentrantLock = lock.getValue();
//			if(reentrantLock!=null){
//				try {
//					reentrantLock.unlock();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			lockManage.remove(key);
//			reentrantLock = null;
//		}
	}
	
	public static void main(String[] args) {
		try {
			//获取锁
			LockUtil.getInstance().lock("abc");
			/*
			 * 业务代码片段
			 */
		} catch (Exception e) {
		}finally{
			//最终释放锁
			LockUtil.getInstance().unlock("abc");
		}
		
//		//或者
//		try {
//			//判断当前锁状态，如果可用则返回true，如果不可用等待5S，再判断锁状态，如果还是不可用则返回false
//			if(LockUtil.getInstance().tryLock("abc", 5)){
//				/*
//				 * 业务代码片段
//				 */
//			}
//		} catch (Exception e) {
//		}finally{
//			//最终释放锁
//			LockUtil.getInstance().unlock("abc");
//		}
	}
	
	/** --------------------以下代码是使用数据库约束方式控制锁 --------------------**/
	
	
	private LockDao lockDao;
	
	
	protected LockDao getLockDao() {
		return lockDao;
	}

	protected void setLockDao(LockDao lockDao) {
		this.lockDao = lockDao;
	}

	private LockUtil(){
		if(lockDao==null)
		lockDao = new LockDao();
	}
	
	private synchronized void setLock(String key){
		long timer = defaultTime;
		while(!setLockData(key)&&timer>0){
			try {
				Thread.sleep(1000);
				timer -= 1;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(timer<=0){
			unlock(key);
		}
	}
	
	private boolean setLockData(String key){
		int result = -1;
		if(checkLockIsExist(key)){
			return updateLockStatus(1, key);
		}else{
			result = insertLock(key);
		}
		if(result>0)
			return true;
		else
			return false;
	}
	
	
	private boolean checkLockIsExist(String key){
		String sql = "select id from t_lock where `key`='"+key+"'";
		Map<String,Object> map = lockDao.getMap(sql);
		if(map==null||map.size()<1)return false;
		return true;
	}
	
	
	private boolean updateLockStatus(int status,String key){
		String sql = "update t_lock set status ="+status+" where `key`='"+key+"' and status<>"+status;
		int result = lockDao.update(sql);
		if(result>0){
			return true;
		}else{
			return false;
		}
	}
	
	private boolean updateLockStatusForUnLock(String key){
		String sql = "update t_lock set status =0 where `key`='"+key+"' ";
		int result = lockDao.update(sql);
		if(result>0){
			return true;
		}else{
			return false;
		}
	}
	
	private int insertLock(String key){
		String sql = "insert into t_lock(`key`,`status`) values('"+key+"',1)";
		return lockDao.update(sql);
	}
	
	
	private class LockDao extends JdbcDaoSupport {
		
		protected LockDao(){
			Properties properties = new Properties();
			try {
//				InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
//				properties.load(in);
//				String url = properties.getProperty("Lock_datasource_url");
//				String username = properties.getProperty("Lock_datasource_username");
//				String password = properties.getProperty("Lock_datasource_password");
//				String dataname = properties.getProperty("Lock_datasource_dataname");
				String url = "127.0.0.1";
				String username = "root";
				String password = "";
				String dataname = "maimai";
				//这里为方便和主程序区分，将锁缓存数据操作方式直接使用JDBC方式操作，可访问其他库，这样和主程序的事务没有冲突
//				BasicDataSource dataSource = new BasicDataSource();
				DruidDataSource dataSource = new DruidDataSource();
				dataSource.setPassword(password);
				dataSource.setUrl("jdbc:mysql://"+url+":3306/"+dataname);
				dataSource.setUsername(username);
				dataSource.setDriverClassName("com.mysql.jdbc.Driver");
				dataSource.setInitialSize(2);
				dataSource.setMinIdle(2);
				dataSource.setMaxActive(10);
				dataSource.setMaxWait(60000);
				dataSource.setTimeBetweenEvictionRunsMillis(5000);
				dataSource.setMinEvictableIdleTimeMillis(120000);
				super.setDataSource(dataSource);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		
		protected Map<String,Object> getMap(String sql){
			Map<String,Object> map = null;
			try {
				map = super.getJdbcTemplate().queryForMap(sql);
			} catch (Exception e) {
				map = null;
			}
			return map;
		}
		
		protected int update(String sql){
			try {
				return super.getJdbcTemplate().update(sql);
			} catch (Exception e) {
			}
			return -1;
		}
	}
	
	/**
	 * 创建锁表
	 DROP TABLE IF EXISTS `t_lock`;
	 CREATE TABLE `t_lock` (
	   `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	   `key` VARCHAR(100) DEFAULT NULL,
	   `status` INT(1) DEFAULT '1',
	   PRIMARY KEY (`id`),
	   UNIQUE KEY `key` (`key`)
	 ) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
	 */
	}
