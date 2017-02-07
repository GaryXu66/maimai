package maimai.app.util.lock;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 鏂板浜嗗垎甯冨紡閿佸姛鑳�
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
	 * 榛樿鎸佺画1鍒嗛挓锛屽鏋滆閿佽秴杩�1鍒嗛挓杩樻湭閲婃斁锛岀郴缁熷皢璇ラ攣浠庡垪琛ㄤ腑鍒犻櫎
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
	 * 鑾峰彇閿佹柟娉�
	 * 璋冪敤璇ユ柟娉曞悗锛屼絾涓氬姟鎵ц瀹屾垚锛岄渶瑕佽皟鐢ㄨВ閿佹柟娉曡В閿�
	 * @param key 鏍囪瘑閿佺殑鍞竴瀛楁
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
	 * @param waitTime 濡傛灉宸茬粡琚玪ock锛屽皾璇曠瓑寰厀aitTime绉掞紝鐪嬫槸鍚﹀彲浠ヨ幏寰楅攣锛屽鏋渨aitTime绉掑悗浠嶇劧鏃犳硶鑾峰緱閿佸垯杩斿洖false
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
	 * 杩囨湡澶辨晥鐨勯攣娓呴櫎锛屽苟閲婃斁閿�
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
	 * 瑙ｉ攣鏂规硶
	 * @param key  鏍囪瘑閿佺殑鍞竴瀛楁
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
			//鑾峰彇閿�
			LockUtil.getInstance().lock("abc");
			/*
			 * 涓氬姟浠ｇ爜鐗囨
			 */
		} catch (Exception e) {
		}finally{
			//鏈�缁堥噴鏀鹃攣
			LockUtil.getInstance().unlock("abc");
		}
		
//		//鎴栬��
//		try {
//			//鍒ゆ柇褰撳墠閿佺姸鎬侊紝濡傛灉鍙敤鍒欒繑鍥瀟rue锛屽鏋滀笉鍙敤绛夊緟5S锛屽啀鍒ゆ柇閿佺姸鎬侊紝濡傛灉杩樻槸涓嶅彲鐢ㄥ垯杩斿洖false
//			if(LockUtil.getInstance().tryLock("abc", 5)){
//				/*
//				 * 涓氬姟浠ｇ爜鐗囨
//				 */
//			}
//		} catch (Exception e) {
//		}finally{
//			//鏈�缁堥噴鏀鹃攣
//			LockUtil.getInstance().unlock("abc");
//		}
	}
	
	/** --------------------浠ヤ笅浠ｇ爜鏄娇鐢ㄦ暟鎹簱绾︽潫鏂瑰紡鎺у埗閿� --------------------**/
	
	
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
				//杩欓噷涓烘柟渚垮拰涓荤▼搴忓尯鍒嗭紝灏嗛攣缂撳瓨鏁版嵁鎿嶄綔鏂瑰紡鐩存帴浣跨敤JDBC鏂瑰紡鎿嶄綔锛屽彲璁块棶鍏朵粬搴擄紝杩欐牱鍜屼富绋嬪簭鐨勪簨鍔℃病鏈夊啿绐�
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
	 * 鍒涘缓閿佽〃
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
