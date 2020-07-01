package me.simonplays15.development.remoteconsoleserver.cryptography;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;

public class StringUtils {

	private static final char[] hexCode;

	static {
		hexCode = "0123456789ABCDEF".toCharArray();
	}

	public static boolean containsIgnoreCase(String arg0, String arg1) {
		String a = arg0.toLowerCase();
		String b = arg1.toLowerCase();

		return a.contains(b);
	}

	public static boolean startsWithIgnoreCase(String arg0, String arg1) {
		String a = arg0.toLowerCase();
		String b = arg1.toLowerCase();

		return a.startsWith(b);
	}

	public static String getRandomString(int length) {
		StringBuilder builder = new StringBuilder();
		String s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for (int i = 0; i < length; i++) {
			double index = Math.random() * s.length();
			builder.append(s.charAt((int) index));
		}
		return builder.toString();
	}
	
	public static String getRandomStringWithSymbols(int length) {
		StringBuilder builder = new StringBuilder();
		String s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!§$%&/()=?\\}][{€@~+*#'´`^°-.,:;_\"";
		for (int i = 0; i < length; i++) {
			double index = Math.random() * s.length();
			builder.append(s.charAt((int) index));
		}
		return builder.toString();
	}
	
	public static Integer getRandomInt(int length) {
		StringBuilder builder = new StringBuilder();
		String s = "1234567890";
		for (int i = 0; i < length; i++) {
			double index = Math.random() * s.length();
			builder.append(s.charAt((int) index));
		}
		return Integer.valueOf(builder.toString());
	}

	public static enum StringUtilsType {
		Base64, HexBinary;
	}

	public static String encodePerType(String input, StringUtilsType type) {
		if (type == StringUtilsType.HexBinary) {
			String string = new String(input.getBytes(), Charset.forName("UTF-8"));
			byte[] inputB = string.getBytes();
			String hexCode = HexBinaryConverter.printHexBinary(inputB);
			return hexCode.toLowerCase();
		} else {
			byte[] data = Base64.encodeBase64(input.getBytes());
			return new String(data);
		}
	}

	public static String decodePerType(String input, StringUtilsType type) {
		if (type == StringUtilsType.HexBinary) {
			byte[] decodeByte = HexBinaryConverter.parseHexBinary(input);
			String decoded;
			try {
				decoded = new String(decodeByte, Charset.forName("UTF-8"));
			} catch (Exception ex) {
				decoded = "ERROR";
				ex.printStackTrace(System.err);
			}

			return decoded;
		} else {

			byte[] data = Base64.decodeBase64(input.getBytes());

			return new String(data);
		}
	}

	public static String encryptAES(String in, SecretKeySpec key) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] data = cipher.doFinal(in.getBytes());

		return Base64.encodeBase64String(data);
	}

	public static String decryptAES(String in, SecretKeySpec key) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

		byte[] data = Base64.decodeBase64(in.getBytes(Charsets.UTF_8));
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] outPutData = cipher.doFinal(data);

		return new String(outPutData);
	}

	public static SecretKeySpec getKey(String key) throws NoSuchAlgorithmException {
		byte[] data = key.getBytes(Charsets.UTF_8);
		MessageDigest md = MessageDigest.getInstance("SHA-256");

		data = md.digest(data);
		data = Arrays.copyOf(data, 16);
		return new SecretKeySpec(data, "AES");
	}

	public static class HexBinaryConverter {

		private static int hexToBin(char c) {
			if (('0' <= c) && (c <= '9')) {
				return c - '0';
			}
			if (('A' <= c) && (c <= 'F')) {
				return c - 'A' + 10;
			}
			if (('a' <= c) && (c <= 'f')) {
				return c - 'a' + 10;
			}
			return -1;
		}

		public static byte[] parseHexBinary(String string) {
			int i = string.length();
			if (i % 2 != 0) {
				throw new IllegalArgumentException("HexCodes needs to be even-length: " + string);
			}

			byte[] arrayOfByte = new byte[i / 2];
			for (int j = 0; j < i; j += 2) {
				int k = hexToBin(string.charAt(j));
				int m = hexToBin(string.charAt(j + 1));

				if ((k == -1) || (m == -1)) {
					throw new IllegalArgumentException("contains illegal character for HexBinary: " + string);
				}

				arrayOfByte[(j / 2)] = ((byte) (k * 16 + m));

			}
			return arrayOfByte;
		}

		public static String printHexBinary(byte[] Byte) {
			StringBuilder builder = new StringBuilder(Byte.length * 2);

			byte[] arrayOfByte = Byte;
			int j = Byte.length;
			for (int i = 0; i < j; i++) {
				int k = arrayOfByte[i];
				builder.append(StringUtils.hexCode[(k >> 4 & 0xF)]);
				builder.append(StringUtils.hexCode[(k & 0xF)]);
			}
			return builder.toString();
		}

	}

}
