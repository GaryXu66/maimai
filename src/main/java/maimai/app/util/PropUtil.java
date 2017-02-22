package maimai.app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 资源文件读取util
 * @author Administrator
 *
 */
public class PropUtil {
	
	private static Logger logger = LoggerFactory.getLogger(PropUtil.class);
	
	private Properties props = new Properties();
	
	private List<File> fileList=new ArrayList<>();
	
	private Pattern pattern=Pattern.compile(".*.properties$");
	
	public static final String FILE_CHARSET = "UTF-8";
	
	private static PropUtil propUtil = new PropUtil();
	
	private PropUtil(){
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static  PropUtil getInstance(){
		return propUtil;
	}
	
	public void init() throws Exception{
		String s=this.getClass().getResource("/").getPath();
		File temp=new File(URLDecoder.decode(s,FILE_CHARSET));
		String path=URLDecoder.decode(temp.getAbsolutePath(),FILE_CHARSET);
		logger.info("加载properties配置文件的请求路径为：["+path+"]");
		File file=new File(path);
		if(!file.exists()){
			throw new FileNotFoundException("文件根目录设置错误！");
		}
		
		scanPropFiles(file);
		
		for(File f:fileList){
			InputStream inStream = new FileInputStream(f);
			props.load(inStream);
		}
	}
	
	private void scanPropFiles(File file) {
		if(file.isDirectory()){
			File[] props = file.listFiles();
			if(null != props && props.length>0){
				for(File p : props){
					if(p.isDirectory()){
						scanPropFiles(p);
					}else{
						Matcher matcher = pattern.matcher(p.getName());
						if(matcher.matches()){
							fileList.add(p);
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		String dn = PropUtil.getInstance().getValue("db_name");
		System.out.println(dn);
	}

	public Properties getProperties() {
		return props;
	}

	/**
	 * 取出Property。
	 */
	private String getValue(String key) {
		String systemProperty = System.getProperty(key);
		if (systemProperty != null) {
			return systemProperty;
		}
		return props.getProperty(key);
	}

	/**
	 * 取出String类型的Property,如果都為Null则抛出异常.
	 */
	public String getProperty(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return value;
	}

	/**
	 * 取出String类型的Property.如果都為Null則返回Default值.
	 */
	public String getProperty(String key, String defaultValue) {
		String value = getValue(key);
		return value != null ? value : defaultValue;
	}

	/**
	 * 取出Integer类型的Property.如果都為Null或内容错误则抛出异常.
	 */
	public Integer getInteger(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Integer.valueOf(value);
	}

	/**
	 * 取出Integer类型的Property.如果都為Null則返回Default值，如果内容错误则抛出异常
	 */
	public Integer getInteger(String key, Integer defaultValue) {
		String value = getValue(key);
		return value != null ? Integer.valueOf(value) : defaultValue;
	}

	/**
	 * 取出Double类型的Property.如果都為Null或内容错误则抛出异常.
	 */
	public Double getDouble(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Double.valueOf(value);
	}

	/**
	 * 取出Double类型的Property.如果都為Null則返回Default值，如果内容错误则抛出异常
	 */
	public Double getDouble(String key, Integer defaultValue) {
		String value = getValue(key);
		return value != null ? Double.valueOf(value) : defaultValue;
	}

	/**
	 * 取出Boolean类型的Property.如果都為Null抛出异常,如果内容不是true/false则返回false.
	 */
	public Boolean getBoolean(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Boolean.valueOf(value);
	}

	/**
	 * 取出Boolean类型的Propert.如果都為Null則返回Default值,如果内容不为true/false则返回false.
	 */
	public Boolean getBoolean(String key, boolean defaultValue) {
		String value = getValue(key);
		return value != null ? Boolean.valueOf(value) : defaultValue;
	}

}
