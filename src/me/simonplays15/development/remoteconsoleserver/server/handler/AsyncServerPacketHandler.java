package me.simonplays15.development.remoteconsoleserver.server.handler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import me.simonplays15.development.general.packet.Packet;
import me.simonplays15.development.general.utils.ClientType;
import me.simonplays15.development.remoteconsoleserver.Core;

public class AsyncServerPacketHandler implements Runnable{
	
	private boolean isRunning = true;
	private Socket socket;
	private ObjectInputStream inputStream;
	private BukkitTask bukkitTask;
	private ClientType type;
	public AsyncServerPacketHandler(Socket socket, ClientType type) {
		this.isRunning = true;
		this.type = type;
		this.socket = socket;
		this.inputStream = Core.getInstance().getRemoteServer().getClientMap().get(socket).getObjectInputStream();
	}
	
	public void interrupt() {
		Core.getInstance().getLogger().log(Level.WARNING,"AsyncServerPacketHandler("+bukkitTask.getTaskId()+" terminated at "+new SimpleDateFormat("dd.MM.yyyy / HH:mm:ss EE").format(new Date()));
		this.isRunning = false;
		if(bukkitTask != null)
			bukkitTask.cancel();
		
	}
	
	@Override
	public void run() {
		bukkitTask = Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), () -> {
			try {
				
				while(true) {
					
					if(!this.isRunning) {
						this.interrupt();
						return;
					}
					
					Object in;
					
					while((in = inputStream.readObject()) != null && socket.isConnected()) {
						
						if(type == ClientType.JAVA_CLIENT) {
						
							if(in instanceof Packet) {
								Packet packet = (Packet)in;
								
								if(packet.getPacketInfo().isEncrypted()) {
									packet.getPacketInfo().decrypt();
								}
								
								if(Core.getInstance().getRemoteServer().getFailedToAuth().containsKey(socket)) {
									if(Core.getInstance().getRemoteServer().getFailedToAuth().get(socket) >= 3) {
										new AsyncFailedLoginTimer(socket);
										return;
									}
								}
								
								Core.getInstance().getLogger().info("Packet \""+packet.getPacketName()+"\" received from: "+this.socket.getInetAddress().getHostAddress());		
								
								
							} else {
								Core.getInstance().getLogger().log(Level.SEVERE, "Unknown data sended from socket: "+socket.getInetAddress().getHostAddress()+":"+socket.getPort());
								
								try{
									socket.close();
								}catch(IOException ex){
									Core.getInstance().getLogger().log(Level.SEVERE, "Client failed to close -> Already closed");
								}
								
								this.interrupt();
							}
						} else if(type == ClientType.TCP_CLIENT) {
							
							//TODO: Deserialize data from C# Client -> Send Packets over JSON -> Create C# Json Decompiler to access Packets / do Actions
							
						} else {
							Core.getInstance().getLogger().log(Level.SEVERE, "Unknown socket client type: "+socket.getInetAddress().getHostAddress()+":"+socket.getPort());
							
							try{
								socket.close();
							}catch(IOException ex){
								Core.getInstance().getLogger().log(Level.SEVERE, "Client failed to close -> Already closed");
							}
							
							this.interrupt();
						}
					}
				}
				
			}catch(IOException ex) {
				Core.getInstance().getLogger().info("Client "+this.socket.getInetAddress().getHostAddress()+":"+this.socket.getPort()+" disconnected ["+ex.getMessage()+"]");
				Core.getInstance().getRemoteServer().deauthClient(socket);
			} catch(ClassNotFoundException ex) {
				Core.getInstance().getLogger().log(Level.WARNING, "Unknown packet requested", ex);
			} catch(Exception ex) {
				Core.getInstance().getLogger().warning("Unknown exception caused an deauthentication of a client -> "+ex.getMessage());
				Core.getInstance().getLogger().info("Client "+this.socket.getInetAddress().getHostAddress()+":"+this.socket.getPort()+" disconnected ["+ex.getMessage()+"]");
				Core.getInstance().getRemoteServer().deauthClient(socket);
			}
		});
	}

}
