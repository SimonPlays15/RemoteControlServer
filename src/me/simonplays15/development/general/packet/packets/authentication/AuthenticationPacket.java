package me.simonplays15.development.general.packet.packets.authentication;

import javax.swing.JOptionPane;

import me.simonplays15.development.general.packet.Packet;
import me.simonplays15.development.general.packet.PacketInfo;
import me.simonplays15.development.remoteconsoleserver.Core;

public class AuthenticationPacket extends Packet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6416749353037510223L;
	private String username;
	private String password;
	
	public AuthenticationPacket(String username, String password) {
		super(new PacketInfo(AuthenticationPacket.class, new Object[] {username, password}));
		this.username = username;
		this.password = password;
	}
	
	@Override
	public void execute(Object... params) {
		if(params.length < 2) { 
			//Core.getInstance().getRemoteServer().sendPacket(socket, new MessagePacket(JOptionPane.WARNING_MESSAGE, "Failed to login", "");
			Core.getInstance().getLogger().warning("Failed to authenticate last connection -> param length is lower than 2");
			return;
		}
		//TODO: Authenticate user if database entry exists in player_permissions
		//		if yes -> Authenticate user
		//		if no -> add to list -> User failed to authenticate
		
		
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

}
