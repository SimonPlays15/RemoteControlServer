package me.simonplays15.development.remoteconsoleserver;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.jar.JarFile;
import java.util.logging.Level;

import org.apache.logging.log4j.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.simonplays15.development.remoteconsoleserver.cryptography.StringUtils;
import me.simonplays15.development.remoteconsoleserver.cryptography.StringUtils.StringUtilsType;
import me.simonplays15.development.remoteconsoleserver.events.PlayerJoinHandler;
import me.simonplays15.development.remoteconsoleserver.handler.CloudNetHandler;
import me.simonplays15.development.remoteconsoleserver.handler.PermissionsExHandler;
import me.simonplays15.development.remoteconsoleserver.mysql.MySQL;
import me.simonplays15.development.remoteconsoleserver.mysql.MySQLHandler;
import me.simonplays15.development.remoteconsoleserver.server.RemoteServer;
import me.simonplays15.development.remoteconsoleserver.utils.Log4JAppender;
import me.simonplays15.development.remoteconsoleserver.utils.LogSystem;
import me.simonplays15.development.remoteconsoleserver.utils.player.CloudNetPermissionsHandler;
import me.simonplays15.development.remoteconsoleserver.utils.player.DefaultPermissionHandler;
import me.simonplays15.development.remoteconsoleserver.utils.player.PermissionHandler;
import me.simonplays15.development.remoteconsoleserver.utils.player.PermissionsExPermissionHandler;

public class Core extends JavaPlugin{
	
	/**
	 * public static fields
	 * @return the prefix
	 */
	public static String getPrefix() {
		return "§8[§cRemote§4Control§cServer§8] §r";
	}
	
	/**
	 * Private Static fields
	 * @return Core.class
	 */
	private static Core instance;
	
	
	/**
	 * private fields
	 */
	private LogSystem logger;
	private MySQL mysql;
	private MySQLHandler mysqlHandler;
	private PermissionsExHandler pexHandler;
	private CloudNetHandler cloudNetHandler;
	private RemoteServer remoteServer;
	private PermissionHandler permHandler;
	
	/**
	 * onLoad()
	 */
	public void onLoad() {
		instance = this;
		logger = new LogSystem();
		logger.info("Do onLoad() threads...");
	}
	
	
	/*
	 * onEnable()
	 */
	public void onEnable() {
		long timestamp = System.currentTimeMillis();
		logger.info("Setting up LogAppender "+Log4JAppender.class.getName());
		((org.apache.logging.log4j.core.Logger)LogManager.getRootLogger()).addAppender(new Log4JAppender());
		
		saveDefaultConfig();
		
		Bukkit.getConsoleSender().sendMessage("§7-=--=--=--=--=--=--=--=--=--=-");
		Bukkit.getConsoleSender().sendMessage("§ePlugIn Owner: §cSimonPlays15 (simonplays15.development@gmail.com)");
		Bukkit.getConsoleSender().sendMessage("§cPlugIn Version: §e"+this.getDescription().getVersion());
		
		/*
		 * Bukkit source
		 */
		
		Bukkit.getPluginManager().registerEvents(new PlayerJoinHandler(), this);
		
		/**
		 * End Bukkit source
		 */
		
		if(this.getConfig().getString("mysql.password").trim().length() > 0) {
			this.getConfig().set("mysql.password", StringUtils.encodePerType(this.getConfig().getString("mysql.password"), StringUtilsType.Base64));
		}
		
		this.mysql = new MySQL(this.getConfig().getString("mysql.hostname"), this.getConfig().getString("mysql.username"), StringUtils.decodePerType(this.getConfig().getString("mysql.password"), StringUtilsType.Base64));
		Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
			mysql.connect();
		});
		if(mysql.isConnected()) {
			this.mysqlHandler = new MySQLHandler(mysql);
		}
			
		if(!mysql.isConnected()) {
			Bukkit.getPluginManager().disablePlugin(this);
			throw new RuntimeException("MySQL connection is not established -> You must have an MySQL connection");
		}
		
		this.saveResource("license.txt", true);
		
		try {
			@SuppressWarnings("resource")
			JarFile jarFile = new JarFile(new File("plugins/RemoteControlServer.jar"));
			
			if(new Date(new File(this.getDataFolder(), "config.yml").lastModified()).getTime() < new Date(jarFile.getJarEntry("config.yml").getLastModifiedTime().toMillis()).getTime()) {
				this.saveResource("config.yml", true);
				new File(this.getDataFolder(), "config.yml").setLastModified(System.currentTimeMillis());
			} else {
				this.saveResource("config.yml", false);
			}
			
		} catch(IOException ex) {
			logger.severe("An error occurred... ", ex);
		}
		
		try {
			Class.forName("ru.tehkode.permissions.bukkit.PermissionsEx");
			pexHandler = new PermissionsExHandler();
			getLogSystem().log(Level.INFO, "PermissionsEx found in resources -> Successfully initialized PermissionsExHandler");
		} catch (ClassNotFoundException e) {
			pexHandler = null;
			getLogSystem().log(Level.WARNING, "Failed to load PermissionsEx from resource -> PermissionsEx is not installed");
		}
		
		try {
			Class.forName("de.dytanic.cloudnet.api.CloudAPI");
			cloudNetHandler = new CloudNetHandler();
		} catch(ClassNotFoundException ex) {
			Core.getInstance().getLogSystem().log(Level.WARNING, "Failed to load CloudAPI from resource -> CloudAPI is not installed");
			cloudNetHandler = null;
		}
		
		try {
			Class.forName("de.dytanic.cloudnet.bridge.CloudServer");
			cloudNetHandler = new CloudNetHandler();
		} catch(ClassNotFoundException ex) {
			cloudNetHandler = null;
			Core.getInstance().getLogSystem().log(Level.WARNING, "Failed to load CloudServer from resource -> CloudServer is not installed");
		}
		
		remoteServer = new RemoteServer(this.getConfig().getInt("remoteserver-port"));
		
		remoteServer.start();
		
		Bukkit.getConsoleSender().sendMessage("§aPlugIn successfully loaded in "+(System.currentTimeMillis() - timestamp)+"ms");
		Bukkit.getConsoleSender().sendMessage("§7-=--=--=--=--=--=--=--=--=--=-");
		
	}
	
	public void onDisable() {
		long timestamp = System.currentTimeMillis();
		Bukkit.getConsoleSender().sendMessage("§7-=--=--=--=--=--=--=--=--=--=-");
		Bukkit.getConsoleSender().sendMessage("§ePlugIn Owner: §cSimonPlays15 (simonplays15.development@gmail.com)");
		Bukkit.getConsoleSender().sendMessage("§cPlugIn Version: §e"+this.getDescription().getVersion());
		
		this.mysql.disconnect();
		
		this.remoteServer.stop();
		
		
		Bukkit.getConsoleSender().sendMessage("§aPlugIn successfully unloaded in "+(System.currentTimeMillis() - timestamp)+"ms");
		Bukkit.getConsoleSender().sendMessage("§7-=--=--=--=--=--=--=--=--=--=-");
	}
	
	public static Core getInstance() {
		return instance;
	}
	
	public LogSystem getLogSystem() {
		return logger;
	}
	
	public MySQL getMySQLConnection() {
		return mysql;
	}
	
	public MySQLHandler getMySQLHandler() {
		return mysqlHandler;
	}
	
	public PermissionsExHandler getPermissionHandler() {
		return this.pexHandler;
	}
	
	public CloudNetHandler getCloudNetHandler(){
		return cloudNetHandler;
	}
	
	public PermissionHandler getPermissionHandler(UUID uuid) {
		String permSys = this.getConfig().getString("permissionssystem");
		if(permSys.equalsIgnoreCase("cloudnet")) {
			this.permHandler = new CloudNetPermissionsHandler(uuid);
		} else if(permSys.equalsIgnoreCase("permissionsex")) {
			this.permHandler = new PermissionsExPermissionHandler(uuid);
		} else {
			this.permHandler = new DefaultPermissionHandler(uuid);
		}
		
		return this.permHandler;
	}
	
	public RemoteServer getRemoteServer() {
		return remoteServer;
	}
	
}
