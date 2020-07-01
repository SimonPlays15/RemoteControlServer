package me.simonplays15.development.remoteconsoleserver.server.threads;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import me.simonplays15.development.general.packet.Packet;
import me.simonplays15.development.remoteconsoleserver.Core;

public abstract class AsyncSendPacketThread implements Runnable{
	
	private Packet packet;
	private BukkitTask bukkitTask;
	boolean isRunning = true;
	
	public AsyncSendPacketThread(Packet packet) {
		this.packet = packet;
		this.isRunning = true;
	}
	
	public Packet getPacket() {
		return this.packet;
	}
	
	public void terminate() {
		Core.getInstance().getLogger().log(Level.INFO,"AsyncSendPacketThread("+bukkitTask.getTaskId()+") terminated at "+new SimpleDateFormat("dd.MM.yyyy / HH:mm:ss EE").format(new Date()));
		this.isRunning = false;
	}
	
	@Override
	public void run() {
		bukkitTask = Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), () -> {
			
			Core.getInstance().getLogger().info("AsnySendPacketThread("+bukkitTask.getTaskId()+") -> "+this.getPacket().getPacketName());
			sendProcess();
			terminate();
			
		});
	}
	
	public abstract void sendProcess();

}
