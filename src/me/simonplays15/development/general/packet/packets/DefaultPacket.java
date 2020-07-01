package me.simonplays15.development.general.packet.packets;

import me.simonplays15.development.general.packet.Packet;
import me.simonplays15.development.general.packet.PacketInfo;

public class DefaultPacket extends Packet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7958504100635588663L;

	public DefaultPacket(PacketInfo packetInfo) {
		super(new PacketInfo(DefaultPacket.class, new Object[] {}));
	}

}
