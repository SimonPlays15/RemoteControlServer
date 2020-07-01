package me.simonplays15.development.remoteconsoleserver.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import me.simonplays15.development.remoteconsoleserver.Core;

public class LogSystem extends Logger {

	public LogSystem() {
		super("RemoteControlServer", null);
	}
	
	public void info(String msg) {
		Core.getInstance().getLogger().log(Level.INFO, msg);
	}
	
	public void warn(String msg) {
		Core.getInstance().getLogger().log(Level.WARNING, msg);
	}
	
	public void severe(String msg) {
		Core.getInstance().getLogger().log(Level.SEVERE, msg);
	}
	
	public void info(String msg, Throwable throwable) {
		Core.getInstance().getLogger().log(Level.INFO, msg, throwable);
	}
	
	public void warn(String msg, Throwable throwable) {
		Core.getInstance().getLogger().log(Level.WARNING, msg, throwable);
	}
	
	public void severe(String msg, Throwable throwable) {
		Core.getInstance().getLogger().log(Level.SEVERE, msg, throwable);
	}
	
	@Override
	public void log(Level level, String msg) {
		Core.getInstance().getLogger().log(level, msg);
	}
	
	public void log(Level level, String msg, Throwable throwable) {
		Core.getInstance().getLogger().log(level, msg, throwable);
	}
	

}
