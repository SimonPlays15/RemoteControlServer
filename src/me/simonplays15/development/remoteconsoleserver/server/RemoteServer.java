package me.simonplays15.development.remoteconsoleserver.server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;

import me.simonplays15.development.general.packet.Packet;
import me.simonplays15.development.general.packet.PacketInfo;
import me.simonplays15.development.general.utils.User;
import me.simonplays15.development.remoteconsoleserver.Core;
import me.simonplays15.development.remoteconsoleserver.server.handler.AsyncConnectionHandler;
import me.simonplays15.development.remoteconsoleserver.server.threads.AsyncSendPacketThread;

public class RemoteServer {
	
	private List<Socket> socketList = new ArrayList<Socket>();
	private Map<Socket, Integer> failedToAuth = new HashMap<Socket, Integer>();
	private Map<Socket, User> clientMap = new HashMap<Socket, User>();
	private AsyncConnectionHandler connectionHandler;
	private ServerSocket remoteServer;
	private int port;
	
	public RemoteServer(int port) {
		if(port < 1 || port >65535) {
			throw new IllegalArgumentException("Port is out of range");
		}
		
		this.port = port;
	}
	
	public Logger getLogger() {
		return Core.getInstance().getLogger();
	}
	
	public void start() {
		if(remoteServer != null) {
			getLogger().severe("RemoteServer already initialized and is already running on port "+port);
			return;
		}
		
		try {
			
			remoteServer = new ServerSocket(port);
			
			connectionHandler = new AsyncConnectionHandler();
			
			Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), connectionHandler);
			
			getLogger().info("RemoteServer successfully started -> Listening on port "+port);
			
		} catch(BindException ex) {
			getLogger().severe("# CANNOT BIND TO PORT "+port+" # Port is already in use");
				port = new Random().nextInt(65534);
				getLogger().warning("# Try with "+port);
				start();
		} catch(Exception ex) {
			getLogger().log(Level.SEVERE, "An error occurred while starting the remote server... ", ex);
		}
		
	}
	
	public void stop() {
		if(remoteServer == null) {
			getLogger().info("RemoteServer is already stopped...");
			return;
		}
		
		if(this.remoteServer != null && !this.remoteServer.isClosed()) {
			
			sendPacket(new Packet(new PacketInfo("DEAUTH", new Object[] {"KILL_CONNECTION"})));
			
			for(Socket socket : this.socketList) {
				this.clientMap.remove(socket);
			}
			
			for(Socket socket : socketList) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			this.socketList.clear();
			
			try {
				this.remoteServer.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				this.remoteServer = null;
			}
			
			connectionHandler.interrupt();
			
		}
	}
	
	public void sendPacket(Socket socket, Packet packet) {

		if (socket == null) {
			getLogger().severe("Client is null -> How can I send a packet to him?");
			return;
		}

		if (packet == null) {
			getLogger().severe("Packet is null -> How can I send a packet that is null to him?");
			return;
		}

		if (socket.isClosed() || !socket.isConnected()) {
			getLogger().severe("Client is offline -> How can I send a packet to him?");
			return;
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new AsyncSendPacketThread(packet) {
			
			@Override
			public void sendProcess() {
				try {
					clientMap.get(socket).getObjectOutputStream().writeObject(packet);
					clientMap.get(socket).getObjectOutputStream().flush();
				} catch (IOException e) {
					getLogger().log(Level.SEVERE ,"Failed to send "+packet.getPacketName()+" to "+getClientMap().get(socket).getUsername()+"("+getClientMap().get(socket).getUuid()+")", e);
				}
			}
		});
	}
	
	public void sendPacket(String username, Packet packet) {
		if(this.socketList.isEmpty())
			return;
		for(Socket socket : this.socketList)
		{
			if(clientMap.get(socket).getUsername().equals(username)) {
				sendPacket(socket, packet);
			}
		}
	}
	
	public void sendPacket(Packet packet) {
		if(this.socketList.isEmpty())
			return;
		for(Socket socket : this.socketList) {
			sendPacket(socket, packet);
		}
	}
	
	
	public boolean authClient(Socket socket, User user) {		
		
		if(socket == null)
			return false;
		if(user == null)
			return false;
		
		if(!this.socketList.contains(socket))
			this.socketList.add(socket);
		
		if(!this.clientMap.containsKey(socket))
			this.clientMap.put(socket, user);
		
		getLogger().info("Client "+socket.getInetAddress().getHostAddress()+" authed with "+user.getUsername()+"("+user.getUuid()+") [ClientType:"+user.getClientType()+"]");
		
		return true;
	}
	
	public void deauthClient(Socket socket) {
		
		if(socket == null)
			return;
		
		getLogger().info("Client "+socket.getInetAddress().getHostAddress()+" authed with "+this.clientMap.get(socket).getUsername()+"("+this.clientMap.get(socket).getUuid()+")");
		
		if(this.socketList.contains(socket))
			this.socketList.remove(socket);
		
		if(this.clientMap.containsKey(socket))
			this.clientMap.remove(socket);
		
		if(!socket.isClosed())
			try {
				socket.close();
			} catch (IOException e) {
				getLogger().warning("Failed to close client -> Client is already closed");
			}
	}
	

	/**
	 * @return the socketList
	 */
	public final List<Socket> getSocketList() {
		return socketList;
	}

	/**
	 * @return the clientMap
	 */
	public final Map<Socket, User> getClientMap() {
		return clientMap;
	}

	/**
	 * @return the remoteServer
	 */
	public final ServerSocket getRemoteServer() {
		return remoteServer;
	}

	/**
	 * @return the port
	 */
	public final int getPort() {
		return port;
	}

	/**
	 * @return the failedToAuth
	 */
	public final Map<Socket, Integer> getFailedToAuth() {
		return failedToAuth;
	}

	/**
	 * @return the connectionHandler
	 */
	public final AsyncConnectionHandler getConnectionHandler() {
		return connectionHandler;
	}

}
