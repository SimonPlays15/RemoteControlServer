package me.simonplays15.development.remoteconsoleserver.cryptography;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;

public class StreamCrypter implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 3599718843771213777L;

		public static OutputStream encode(OutputStream out) {
			return new Base64OutputStream(out, true);
		}
		
		public static OutputStream decode(OutputStream out) {
			return new Base64OutputStream(out, false);
		}
		
		public static InputStream encode(InputStream in) {
			return new Base64InputStream(in, true);
		}
		
		public static InputStream decode(InputStream in) {
			return new Base64InputStream(in, false);
		}
}
