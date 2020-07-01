package me.simonplays15.development.general.packet.packets.actions;

import javax.swing.JOptionPane;

import org.bukkit.Bukkit;

import me.simonplays15.development.general.packet.Packet;
import me.simonplays15.development.general.packet.PacketInfo;
import me.simonplays15.development.general.packet.packets.messages.MessagePacket;
import me.simonplays15.development.general.utils.RemoteConsoleCommandSender;
import me.simonplays15.development.remoteconsoleserver.Core;

public class CommandPacket extends Packet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3677691869140917481L;
	private String command, sender;
	private String[] args;
	
	public CommandPacket(String command, String sender, String[] args){
		super(new PacketInfo(CommandPacket.class, command, sender, args));
		this.command = command;
		this.sender = sender;
		this.args = args;
	}
	
	@Override
	public void execute(Object... params) {
		if(params.length > 3 || params.length < 3) {
			Core.getInstance().getLogger().severe("Invalid params length in"+this.getPacketName()+" [CommandPacket.java:22]");
			Core.getInstance().getRemoteServer().sendPacket(sender, new MessagePacket(JOptionPane.WARNING_MESSAGE, "Invalid param length", "Invalid param length in packet class -> try again -> Still seeing this message? Inform the developer instead"));
			return;
		}
		Core.getInstance().getLogger().info("Performing command '"+command+"' via RemoteControlServer -> User: "+sender);
		Bukkit.dispatchCommand(new RemoteConsoleCommandSender(sender), command);
		
	}

	/**
	 * @return the command
	 */
	public final String getCommand() {
		return command;
	}

	/**
	 * @return the sender
	 */
	public final String getSender() {
		return sender;
	}

	/**
	 * @return the args
	 */
	public final String[] getArgs() {
		return args;
	}

}
