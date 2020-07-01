package me.simonplays15.development.general.packet;

public interface IPacketExecutor extends Runnable{

	boolean isRunning = false;
	
	boolean isRunning();
	
	void terminate();
	
	void execute();
	
}
