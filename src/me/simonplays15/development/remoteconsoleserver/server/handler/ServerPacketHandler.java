package me.simonplays15.development.remoteconsoleserver.server.handler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import me.simonplays15.development.general.packet.Packet;
import me.simonplays15.development.remoteconsoleserver.Core;

public class ServerPacketHandler extends Thread{
	
	private boolean isRunning = true;
	private Socket socket;
	private ObjectInputStream inputStream;
	
	public ServerPacketHandler(Socket socket) {
		this.isRunning = true;
		this.socket = socket;

		this.inputStream = Core.getInstance().getRemoteServer().getClientMap().get(socket).getObjectInputStream();
	}
	
	@Override
	public void interrupt() {
		this.isRunning = false;
		this.checkAccess();
		Core.getInstance().getLogger().log(Level.WARNING,"ConnectionHandler terminated at "+new SimpleDateFormat("dd.MM.yyyy / HH:mm:ss EE").format(new Date()));
		super.interrupt();
	}
	
	@Override
	public void run() {
		try {
			
			while(true) {
				
				if(!this.isRunning) {
					return;
				}
				
				Object in;
				
				while((in = inputStream.readObject()) != null && socket.isConnected()) {
					
					if(in instanceof Packet) {
						Packet packet = (Packet)in;
						
						if(packet.getPacketInfo().isEncrypted()) {
							packet.getPacketInfo().decrypt();
						}
						
						Core.getInstance().getLogger().info("Packet \""+packet.getPacketName()+"\" received from: "+this.socket.getInetAddress().getHostAddress());		
						
						
					} else {
						Core.getInstance().getLogger().log(Level.SEVERE, "Unknown data sended from socket: "+socket.getInetAddress().getHostAddress()+":"+socket.getPort());
						
						try{
							socket.close();
						}catch(IOException ex){
							Core.getInstance().getLogger().log(Level.SEVERE, "Socket closed - IOException - Unknown data send", ex);
						}
						
						this.interrupt();
					}
				}
			}
			
		}catch(IOException ex) {
			if(ex.getMessage() != null) {
				if(ex.getMessage().contains("Connection reset")) {
					Core.getInstance().getLogger().info("Client "+this.socket.getInetAddress().getHostAddress()+":"+this.socket.getPort()+" disconnected ["+ex.getMessage()+"]");
					Core.getInstance().getRemoteServer().deauthClient(socket);
				}
			} else {
				Core.getInstance().getLogger().info("An unknown connection error occurred. Is a client disconnected?");
				Core.getInstance().getLogger().info("Client "+this.socket.getInetAddress().getHostAddress()+":"+this.socket.getPort()+" disconnected ["+ex.getMessage()+"]");
				Core.getInstance().getRemoteServer().deauthClient(socket);
			}
		} catch(ClassNotFoundException ex) {
			Core.getInstance().getLogger().log(Level.WARNING, "An unknown class called. ", ex);
		}
	}
	
}
