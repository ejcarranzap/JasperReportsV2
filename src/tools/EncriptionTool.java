package tools;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.olap4j.impl.Base64;

public class EncriptionTool {
	public static String ENCRYPT_KEY = "clave-compartida-no-reveloar-nun";
	
	public static String encript(String text) throws Exception {
		Key aesKey = new SecretKeySpec(ENCRYPT_KEY.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, aesKey);
		byte[] encrypted = cipher.doFinal(text.getBytes());
		return Base64.encodeBytes(encrypted);
	}
	
	public static String decrypt(String encrypted) throws Exception {
		byte[] encryptedBytes = Base64.decode(encrypted.replace("\n", ""));
		Key aesKey = new SecretKeySpec(ENCRYPT_KEY.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, aesKey);
		String decrypted = new String(cipher.doFinal(encryptedBytes));
		return decrypted;
	}

}
