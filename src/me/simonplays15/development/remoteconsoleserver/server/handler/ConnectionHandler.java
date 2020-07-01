package me.simonplays15.development.remoteconsoleserver.server.handler;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import me.simonplays15.development.general.packet.Packet;
import me.simonplays15.development.general.packet.PacketInfo;
import me.simonplays15.development.general.utils.ClientType;
import me.simonplays15.development.general.utils.User;
import me.simonplays15.development.remoteconsoleserver.Core;

public class ConnectionHandler extends Thread {

	private boolean isRunning = true;
	public ConnectionHandler() {
		
	}
	
	@Override
	public void interrupt() {
		this.isRunning = false;
		this.checkAccess();
		Core.getInstance().getLogger().log(Level.INFO, "ConnectionHandler interrupted | Please restart the server to restart the RemoteServer");
		super.interrupt();
	}
	
	
	@Override
	public void run() {
		
		while(!(Core.getInstance().getRemoteServer().getRemoteServer().isClosed())) {
			if(!this.isRunning) {
				this.interrupt();
				return;
			}
			
			try {
				
				Socket socket = Core.getInstance().getRemoteServer().getRemoteServer().accept();
				
				Core.getInstance().getLogger().info("Incomming connection -> "+socket.getInetAddress().getHostAddress()+":"+socket.getPort());
				
				User informations = new User("UNKNOWN", ClientType.UNKNOWN, socket);
				
				informations.getObjectOutputStream().writeObject(new Packet(new PacketInfo("CONNECTED", new Object[0])));
				informations.getObjectOutputStream().flush();
				
				Core.getInstance().getRemoteServer().authClient(socket, informations);
				
				AsyncServerPacketHandler handler = new AsyncServerPacketHandler(socket, informations.getClientType());
//				
//				handler.setName("PacketHandler-"+socket.getInetAddress().getHostAddress().replaceAll(".", "/"));
//				
//				handler.start();
				
				Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), handler);
				
			} catch(IOException ex) {
				if(ex.getMessage() != null && ex.getMessage().contains("socket closed")) {
					Core.getInstance().getLogger().warning("Cannot connect to socket -> Thread closed?");
				} else {
					Core.getInstance().getLogger().log(Level.SEVERE, "ConnectionHandler error -> ", ex);
				}
			}
			
			
		}
		
	}
	
	
}
