package me.simonplays15.development.general.packet.packets.authentication;

import me.simonplays15.development.general.packet.Packet;
import me.simonplays15.development.general.packet.PacketInfo;

public class FailedToAuthenticatePacket extends Packet {
	
	/*
	 * 
	 */
	private static final long serialVersionUID = -3476320005499079958L;
	
	public FailedToAuthenticatePacket() {
		super(new PacketInfo(FailedToAuthenticatePacket.class));
	}

}
