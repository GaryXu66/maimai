package maimai.app.util.secure;

import java.security.MessageDigest;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import maimai.app.util.secure.enums.EnumDigestAlgorithm;



/**
 * 消息摘要工具类
 * @author ZGL
 *
 */
public class MessageDigestUtil {
	public static byte[] encode(EnumDigestAlgorithm digestAlgorithm, byte[] byteData) throws Exception{
		Security.addProvider(new BouncyCastleProvider());
		return MessageDigest.getInstance(digestAlgorithm.name()).digest(byteData);
	}
}
