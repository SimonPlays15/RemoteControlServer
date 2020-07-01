package me.simonplays15.development.general.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import me.simonplays15.development.remoteconsoleserver.Core;
import me.simonplays15.development.remoteconsoleserver.cryptography.StringUtils;

public class User {
	
	private String username;
	private ClientType clientType;
	private String uuid;
	private Socket client;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	
	public User(String username, ClientType type, Socket socket) {
		if(username == null) username = "Default_"+StringUtils.getRandomString(4)+StringUtils.getRandomInt(4);
		this.username = username;
		this.client = socket;
		this.uuid = Core.getInstance().getMySQLHandler().getUUIDFromUsername(username);
		this.clientType = type;
		try {
			this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			this.objectInputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public ClientType getClientType() {
		return clientType;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public String getUsername() {
		return username;
	}
	
	public Socket getClient() {
		return client;
	}
	
	public ObjectOutputStream getObjectOutputStream() {
		return objectOutputStream;
	}
	
	public ObjectInputStream getObjectInputStream() {
		return objectInputStream;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	

}
