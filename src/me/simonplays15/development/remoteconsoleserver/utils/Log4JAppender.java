package me.simonplays15.development.remoteconsoleserver.utils;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.bukkit.ChatColor;

import me.simonplays15.development.general.packet.packets.messages.LogMessagePacket;
import me.simonplays15.development.remoteconsoleserver.Core;

@Plugin(name = "Log4JAppender", category = "Core", elementType = "appender", printObject = true)
public class Log4JAppender extends AbstractAppender{

	public Log4JAppender() {
		super("Log4JAppender", null, PatternLayout.createLayout("[%d{HH:mm:ss} %level]: %message", null, null, "UTF-8", null), true);
	
		this.start();
	}
	
	@Override
	public boolean isStarted() {
		return true;
	}
	
	@Override
	public void append(LogEvent event) {
		String message = "["+new SimpleDateFormat("HH:mm:ss").format(new Date(event.getMillis()))+" "+event.getLevel().name().toUpperCase()+"] " + ChatColor.stripColor(event.getMessage().getFormattedMessage().replaceAll("§", "&"));
		message.trim();
		
		if(Core.getInstance().getMySQLConnection() != null)
			if(Core.getInstance().getMySQLConnection().isConnected())
				if(Core.getInstance().getMySQLHandler() != null)
					try {
						if(!Core.getInstance().getMySQLHandler().getMySQL().getCon().isClosed())
							Core.getInstance().getMySQLHandler().addLogEntry("["+event.getLevel().name().toUpperCase()+"] "+ChatColor.stripColor(event.getMessage().getFormattedMessage().replaceAll("§", "&")));
					} catch (SQLException e) {
						Core.getInstance().getLogger().logp(Level.SEVERE, Log4JAppender.class.getName(), "append(LogEvent)", "Failed to add LogEntry to the MySQL Database", e);
					}
		
		if(Core.getInstance().getRemoteServer() != null)
			if(Core.getInstance().getRemoteServer().getRemoteServer() != null)
				if(Core.getInstance().getRemoteServer().getRemoteServer().isClosed() == false) 
					Core.getInstance().getRemoteServer().sendPacket(new LogMessagePacket(message));
	}
	
}
