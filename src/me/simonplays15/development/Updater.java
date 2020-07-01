package me.simonplays15.development;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.simonplays15.development.remoteconsoleserver.Core;
import me.simonplays15.development.remoteconsoleserver.cryptography.StringUtils;
import me.simonplays15.development.remoteconsoleserver.utils.PlayerUtils;

public class Updater {
	
	private URL checkUrl;
	private String version;
	private boolean hasUpdate;
	private boolean isUpdated;
	
	private static enum VersionType{
		RELEASE, BETA, ALPHA, OPENBETA, OPENALPHA;
	}
	
	private VersionType versionType;

	/**
	 * @return the checkUrl
	 */
	public final URL getCheckUrl() {
		return checkUrl;
	}

	/**
	 * @return the version
	 */
	public final String getVersion() {
		return version;
	}

	/**
	 * @return the hasUpdate
	 */
	public final boolean isHasUpdate() {
		return hasUpdate;
	}

	/**
	 * @return the isUpdated
	 */
	public final boolean isUpdated() {
		return isUpdated;
	}

	/**
	 * @return the versionType
	 */
	public final VersionType getVersionType() {
		return versionType;
	}
	
	public Updater() {
		this.hasUpdate = false;
		this.isUpdated = true;
		this.version = Core.getInstance().getDescription().getVersion().replace("v", "");
	}
	
	public void checkForUpdates() {
		try {
			
			this.checkUrl = new URL("URL DELETED");
			this.checkUrl.openConnection().connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(this.checkUrl.openStream()));
			String var;
			
			while((var = reader.readLine()) != null) {
				if(!this.version.equalsIgnoreCase(var)) {
					this.versionType = VersionType.RELEASE;
					if (StringUtils.containsIgnoreCase(var, "beta")) {
						this.versionType = VersionType.BETA;
					}
					if (StringUtils.containsIgnoreCase(var, "alpha")) {
						this.versionType = VersionType.ALPHA;
					}
					this.hasUpdate = true;
					this.isUpdated = false;
					
					Core.getInstance().getLogger().info("Found a new Update for RemoteConsoleServer (v"+var+")");
					Core.getInstance().getLogger().info("Download the new version here: http://simonplays15.bplaced.net/projects/LionMC.de/updater/RemoteControlServer/RemoteControlServer.jar");
					
					for (Player players : Bukkit.getOnlinePlayers()) {
						if (PlayerUtils.hasPermission(players, "remotecontrolserver.updater.notify")) {
							players.sendMessage(Core.getPrefix()+"Found a new update for RemoteConsoleServer (v"+var+")");
							players.sendMessage(Core.getPrefix()+"Download the new Version here: §6http://simonplays15.bplaced.net/projects/LionMC.de/updater/RemoteControlServer/RemoteControlServer.jar");
						}	
					}
					
				}
			}
			
			reader.close();
			
		} catch(Exception ex) {
			Core.getInstance().getLogger().log(Level.SEVERE, "Cannot check for updates", new UpdaterException());
		}
	}
	
	public void download() {
		try {
			
			BufferedInputStream in = new BufferedInputStream(new URL("URL DELETED").openStream());
			FileOutputStream fout = new FileOutputStream(new File("plugins"+File.separator+"RemoteControlServer.jar"));
			int count;
			byte[] kb = new byte[1024];
			Core.getInstance().getLogger().info("Downloading update for RemoteControlServer...");
			while((count = in.read(kb, 0, 1024)) != -1) {
				fout.write(kb, 0, count);
			}
			in.close();
			fout.close();
			
			this.isUpdated = true;
			Core.getInstance().getLogger().info("Update for RemoteControlServer finished... Restart the server to take the update effect");
			
		} catch(Exception ex) {
			Core.getInstance().getLogger().log(Level.SEVERE, "Failed to download the update for RemoteConsoleServer", new UpdaterException());
		}
	}
	
	private class UpdaterException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3387464609962941800L;

		public UpdaterException() {
			super();
		}

	}

	
}
