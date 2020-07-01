package me.simonplays15.development.general.packet;

import me.simonplays15.development.remoteconsoleserver.cryptography.StringUtils;

public abstract class PacketExecutor extends Thread implements IPacketExecutor{
	
	private Packet packet;
	private boolean isRunning = false;
	public PacketExecutor(Packet handle) {
		this.isRunning = true;
		this.packet = handle;
		this.setName("PacketExecutor-"+packet.getPacketName()+"-"+StringUtils.getRandomInt(7));
	}
	
	public Packet getHandle() {
		return packet;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void terminate() {
		this.isRunning = false;
		this.checkAccess();
		this.interrupt();
		if(this.isInterrupted()) {
			try {
				this.stop();
			} catch(Exception ex) {
				
			}
		}
	}
	
	@Override
	public void run() {
		execute();
	}
	
	@Override
	public boolean isRunning() {
		return this.isRunning;
	}
	
	@Override
	public void execute() {}
}
