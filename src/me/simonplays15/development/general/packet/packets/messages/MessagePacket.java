package me.simonplays15.development.general.packet.packets.messages;

import me.simonplays15.development.general.packet.Packet;
import me.simonplays15.development.general.packet.PacketInfo;

public class MessagePacket extends Packet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6790367653782305090L;
	private String title, message;
	private int type;
	
	public MessagePacket(int type, String title, String message) {
		super(new PacketInfo(MessagePacket.class, type, title, message));
		this.type = type;
		this.title = title;
		this.message = message;
	}

	/**
	 * @return the type
	 */
	public final Integer getType() {
		return type;
	}

	/**
	 * @return the title
	 */
	public final String getTitle() {
		return title;
	}

	/**
	 * @return the message
	 */
	public final String getMessage() {
		return message;
	}
	

}
