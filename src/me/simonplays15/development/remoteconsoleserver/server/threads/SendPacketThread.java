package me.simonplays15.development.remoteconsoleserver.server.threads;

import me.simonplays15.development.general.packet.Packet;
import me.simonplays15.development.remoteconsoleserver.Core;
import me.simonplays15.development.remoteconsoleserver.cryptography.StringUtils;

public abstract class SendPacketThread extends Thread{

	
	Packet packet;
	
	boolean isRunning = true;
	
	public SendPacketThread(Packet packet) {
		this.packet = packet;
		this.isRunning = true;
		this.setName("PacketSendThread-"+packet.getPacketName()+"-"+StringUtils.getRandomInt(6));
	}
	
	public Packet getPacket() {
		return this.packet;
	}
	
	public void terminate() {
		//Core.getInstance().getLogger().log(Level.WARNING, "Interrupted Thread ("+this.getName()+") - "+packet.getPacketName()+" at "+new SimpleDateFormat("dd.MM.yyyy | HH:mm:ss EE").format(new Date()));
		this.isRunning = false;
		this.checkAccess();
		this.interrupt();
	}
	
	@Override
	public void run() {
		Core.getInstance().getLogger().info("SendPacketThread -> "+this.getPacket().getPacketName());
		process();
		terminate();
	}
	
	public abstract void process();
	
}
