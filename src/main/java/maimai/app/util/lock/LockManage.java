package maimai.app.util.lock;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 生成一个单列的map类，用以管理锁对象
 * @author angus
 *
 */
public class LockManage extends HashMap<String, Object>{
	
		private static final long serialVersionUID = 1L;
		private volatile static LockManage cacheManage = null;
		
		private LockManage(){
		}
		
		public static synchronized LockManage getInstance() {  
	         if (cacheManage == null) { 
	        	 synchronized(LockManage.class){
	        		 if (cacheManage == null) { 
	        			 cacheManage = new LockManage();  
	        		 }
	        	 }
	         }    
	        return cacheManage;  
	    }  
		
		@Override
		public void clear() {
			super.clear();
		}
		@Override
		public Object clone() {
			return super.clone();
		}
		@Override
		public boolean containsKey(Object key) {
			return super.containsKey(key);
		}
		@Override
		public boolean containsValue(Object value) {
			return super.containsValue(value);
		}
		@Override
		public Set<java.util.Map.Entry<String, Object>> entrySet() {
			return super.entrySet();
		}
		@Override
		public Object get(Object key) {
			return super.get(key);
		}
		@Override
		public boolean isEmpty() {
			return super.isEmpty();
		}
		@Override
		public Set<String> keySet() {
			return super.keySet();
		}
		@Override
		public Object put(String key, Object value) {
			return super.put(key, value);
		}
		@Override
		public void putAll(Map<? extends String, ? extends Object> m) {
			super.putAll(m);
		}
		@Override
		public Object remove(Object key) {
			return super.remove(key);
		}
		@Override
		public int size() {
			return super.size();
		}
		@Override
		public Collection<Object> values() {
			return super.values();
		}
		
		
	}