package me.simonplays15.development.general.packet;

import java.io.Serializable;
import java.util.logging.Level;

import me.simonplays15.development.remoteconsoleserver.Core;
import me.simonplays15.development.remoteconsoleserver.cryptography.ArrayCryptography;

public class PacketInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8402248834407454903L;
	
	private String from;
	private Object[] data;
	private String packetName;
	private String packetId;
	private boolean isEncrypted;
	private boolean isDecrypted;
	private boolean doDecrypt;
	
	public PacketInfo(Class<? extends Packet> packetClass, Object...packetData) {
		this.data = packetData;
		this.packetName = packetClass.getSimpleName();
		this.packetId = "0x" + (packetName.length() + packetName.hashCode());
		this.doDecrypt = false;
		this.isEncrypted = false;
		if (packetData.length != 0) {
			encrypt();
		}

	}
	
	public PacketInfo(String packetClass, Object...packetData) {
		this.data = packetData;
		this.packetName = packetClass;
		this.packetId = "0x" + packetName.hashCode();
		this.doDecrypt = false;
		this.isEncrypted = false;
		if (packetData.length != 0) {
			encrypt();
		}

	}
	
	public PacketInfo(Class<? extends Packet> packetClass, boolean doDecrypt, Object... packetData) {
		this.data = packetData;
		this.packetName = packetClass.getName();
		this.packetId = "0x" + packetName.hashCode();
		this.doDecrypt = doDecrypt;
		this.isEncrypted = false;
		if (packetData.length != 0) {
			if (doDecrypt) {
				decrypt();
			} else {
				encrypt();
			}
		}

	}
	
	public PacketInfo(String packetClass, boolean doDecrypt, Object... packetData) {
		this.data = packetData;
		this.packetName = packetClass;
		this.packetId = "0x" + packetName.hashCode();
		this.doDecrypt = doDecrypt;
		this.isEncrypted = false;
		if (packetData.length != 0) {
			if (doDecrypt) {
				decrypt();
			} else {
				encrypt();
			}
		}

	}

	public void encrypt(){
		if(isEncrypted){
			Core.getInstance().getLogger().log(Level.SEVERE, "Object data is already encrypted");
			return;
		}
		if(data.length != 0 ){
			try {
				this.data = ArrayCryptography.encrypt(data);
				this.isEncrypted = true;
				this.isDecrypted = false;
			} catch (Exception e) {
				this.isEncrypted = false;
				this.isDecrypted = true;
				Core.getInstance().getLogger().log(Level.SEVERE, "Cannot encrypt data", e);
			}
		} else {
			Core.getInstance().getLogger().log(Level.SEVERE, "Cannot encrypt 'null' of data");
		}
	}

	public void decrypt(){
		if(isDecrypted){
			Core.getInstance().getLogger().log(Level.SEVERE, "Object data is already encrypted");
			return;
		}
		if(data.length != 0 ){
			try {
				this.data = ArrayCryptography.decrypt(data);
				this.isEncrypted = false;
				this.isDecrypted = true;
			} catch (Exception e) {
				this.isDecrypted = false;
				Core.getInstance().getLogger().log(Level.SEVERE, "Cannot decrypt data", e);
			}
		} else {
			Core.getInstance().getLogger().log(Level.SEVERE, "Cannot encrypt data type of 'null'");
		}
	}

	public boolean isDecrypted() {
		return isDecrypted;
	}

	public boolean doDecrypt() {
		return doDecrypt;
	}

	public boolean isEncrypted() {
		return this.isEncrypted;
	}

	public Object[] getPacketData() {
		return data;
	}

	public String getPacketName() {
		return packetName;
	}

	public String getPacketId() {
		return packetId;
	}
	
	public String getFrom() {
		return from;
	}
	
	
}
