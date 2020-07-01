package me.simonplays15.development.remoteconsoleserver.server.handler;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.json.JSONObject;

import me.simonplays15.development.general.utils.ClientType;
import me.simonplays15.development.general.utils.User;
import me.simonplays15.development.remoteconsoleserver.Core;

public class AsyncConnectionHandler implements Runnable{
	
	private List<AsyncServerPacketHandler> packetHandler = new ArrayList<AsyncServerPacketHandler>();
	private boolean isRunning = true;
	private BukkitTask bukkitTask;
	public AsyncConnectionHandler() {
		
	}
	
	public void interrupt() {
		Core.getInstance().getLogger().log(Level.WARNING,"AsyncConnectionHandler("+bukkitTask.getTaskId()+") terminated at "+new SimpleDateFormat("dd.MM.yyyy / HH:mm:ss EE").format(new Date()));
		this.isRunning = false;
		for(AsyncServerPacketHandler asph : packetHandler) {
			asph.interrupt();
		}
		if(bukkitTask != null)
			bukkitTask.cancel();
		
	}

	@Override
	public void run() {	
		
		bukkitTask = Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), () -> {
			
			
			while(Core.getInstance() != null && Core.getInstance().getRemoteServer() != null && Core.getInstance().getRemoteServer().getRemoteServer() != null && !(Core.getInstance().getRemoteServer().getRemoteServer().isClosed())) {
				if(!this.isRunning) {
					this.interrupt();
					return;
				}
				
				try {
					
					Socket socket = Core.getInstance().getRemoteServer().getRemoteServer().accept();
					
					Core.getInstance().getLogger().info("Incomming connection -> "+socket.getInetAddress().getHostAddress()+":"+socket.getPort());
					
					User informations = new User("UNKNOWN", ClientType.UNKNOWN, socket);
					
					JSONObject theTestObject = new JSONObject();
					
					theTestObject.put("GetClientType", "sendMeYourTypeOfClient");
					
					informations.getObjectOutputStream().writeObject(theTestObject);
					informations.getObjectOutputStream().flush();
					
//					Core.getInstance().getRemoteServer().authClient(socket, informations);
					
					AsyncServerPacketHandler handler = new AsyncServerPacketHandler(socket, informations.getClientType());
					packetHandler.add(handler);
					
					Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), handler);
					
				} catch(IOException ex) {
					if(ex.getMessage() != null && ex.getMessage().contains("socket closed")) {
						Core.getInstance().getLogger().warning("Cannot connect to socket -> Thread closed?");
					} else {
						Core.getInstance().getLogger().log(Level.SEVERE, "ConnectionHandler error -> ", ex);
					}
				}
				
				
			}
			
		});
		
	}

}
