package me.simonplays15.development.remoteconsoleserver.server.handler;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import me.simonplays15.development.general.packet.packets.messages.MessagePacket;
import me.simonplays15.development.remoteconsoleserver.Core;
import me.simonplays15.development.remoteconsoleserver.server.utils.DateUtil;

public class AsyncFailedLoginTimer implements Runnable {

	private List<Socket> timeout = new ArrayList<Socket>();
	
	private Socket socket;
	 public AsyncFailedLoginTimer(Socket socket) {
		if(timeout.contains(socket))
			return;
		timeout.add(socket);
		
		this.run();
	}
	
	@Override
	public void run() {
		
		AsyncFailedLoginThread forSocket = new AsyncFailedLoginThread(socket);
		
		Bukkit.getScheduler().runTask(Core.getInstance(), forSocket);
		
	}
	
	
	class AsyncFailedLoginThread implements Runnable{
		
		private Socket socket;
		private BukkitTask taskId;
		private long timeout = System.currentTimeMillis() + 1000*60*5;
		public AsyncFailedLoginThread(Socket socket) {
			this.socket = socket;
			
		}
		public Socket getSocket() {
			return socket;
		}
		
		@Override
		public void run() {
			
			taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(Core.getInstance(), () -> {
				
				String timestamp = DateUtil.formatDateDiff(timeout);
				
				if(System.currentTimeMillis() >= timeout) {
					Core.getInstance().getRemoteServer().getFailedToAuth().remove(socket);
					taskId.cancel();
				} else {
					Core.getInstance().getRemoteServer().sendPacket(getSocket(), new MessagePacket(JOptionPane.INFORMATION_MESSAGE, "WARNING", "You must wait "+timestamp+""));
					System.out.println(timestamp);
				}
				
			}, 0L, 20);
			
		}
		
	}

}
