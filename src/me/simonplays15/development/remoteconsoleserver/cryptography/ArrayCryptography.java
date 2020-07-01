package me.simonplays15.development.remoteconsoleserver.cryptography;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.Charsets;

import me.simonplays15.development.remoteconsoleserver.cryptography.StringUtils.StringUtilsType;
public class ArrayCryptography {

	private static final String ALGO = "AES";

	private static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream h = new ObjectOutputStream(out);

		h.writeObject(obj);

		return out.toByteArray();

	}

	private static Object deserialize(byte[] array) throws IOException, ClassNotFoundException {

		ByteArrayInputStream in = new ByteArrayInputStream(array);
		ObjectInputStream objectIn = new ObjectInputStream(in);
		return objectIn.readObject();

	}

	public static Object[] encrypt(Object[] data) throws Exception {
		Key key = getKey(StringUtils.decodePerType("MTQ2OHNkNGY2czRmNTY0ZjgyNDR0MTZ3MTQ2Z2E4NDY5cTI4NGc4ZTRnYWU0cjE4NGc4ZGY0Zzk4M3FlNDhxOTN3NGhlYTQ4ZTlhNGc=", StringUtilsType.Base64));

		Object[] res = new Object[data.length];

		Cipher c = Cipher.getInstance(ALGO);

		c.init(Cipher.ENCRYPT_MODE, key);

		for (int i = 0; i < data.length; i++) {
			byte[] g = c.doFinal(serialize(data[i]));
			res[i] = Base64.getEncoder().encodeToString(g);
		}

		return res;
	}

	public static Object[] decrypt(Object[] data) throws Exception {
		Key key = getKey(StringUtils.decodePerType("MTQ2OHNkNGY2czRmNTY0ZjgyNDR0MTZ3MTQ2Z2E4NDY5cTI4NGc4ZTRnYWU0cjE4NGc4ZGY0Zzk4M3FlNDhxOTN3NGhlYTQ4ZTlhNGc=", StringUtilsType.Base64));

		Object[] res = new Object[data.length];

		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, key);

		for (int i = 0; i < data.length; i++) {
			byte[] g = Base64.getDecoder().decode(((String) data[i]).getBytes());
			byte[] j = c.doFinal(g);
			res[i] = deserialize(j);
		}

		return res;

	}

	public static SecretKeySpec getKey(String key) throws Exception {
		byte[] data = key.getBytes(Charsets.UTF_8);
		MessageDigest md = MessageDigest.getInstance("SHA-256");

		data = md.digest(data);
		data = Arrays.copyOf(data, 16);
		return new SecretKeySpec(data, "AES");
	}
	
}
