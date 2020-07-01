package me.simonplays15.development.general.packet.packets.messages;

import me.simonplays15.development.general.packet.Packet;
import me.simonplays15.development.general.packet.PacketInfo;

public class LogMessagePacket extends Packet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1297937449467725194L;
	private String logMessage;
	
	public LogMessagePacket(String logMessage) {
		super(new PacketInfo(LogMessagePacket.class, new Object[] {logMessage}));
		this.logMessage = logMessage;
	}
	
	public String getLogMessage() {
		return logMessage;
	}

}
