package net.omniblock.lobbies.skywars;

import org.bukkit.plugin.java.JavaPlugin;

import net.omniblock.lobbies.api.LobbyHandler;
import net.omniblock.lobbies.skywars.handler.SkywarsLobby;
import net.omniblock.network.handlers.Handlers;
import net.omniblock.network.handlers.network.NetworkManager;
import net.omniblock.packets.object.external.ServerType;

public class SkywarsLobbyPlugin extends JavaPlugin {

	private static SkywarsLobbyPlugin instance;
	private static SkywarsLobby lobby;
	
	@Override
	public void onEnable() {
		
		instance = this;
		lobby = new SkywarsLobby();
		
		if(NetworkManager.getServertype() != ServerType.SKYWARS_LOBBY_SERVER) {
			
			Handlers.LOGGER.sendModuleInfo("&7Se ha registrado SkywarsLobby v" + this.getDescription().getVersion() + "!");
			Handlers.LOGGER.sendModuleMessage("OmniLobbies", "Se ha inicializado SkywarsLobby en modo API!");
			return;
			
		}
		
		Handlers.LOGGER.sendModuleInfo("&7Se ha registrado SkywarsLobby v" + this.getDescription().getVersion() + "!");
		Handlers.LOGGER.sendModuleMessage("OmniLobbies", "Se ha inicializado este lobby como un SkywarsLobby!");
		
		LobbyHandler.startLobby(lobby);
		
	}
	
	public static SkywarsLobbyPlugin getInstance() {
		return instance;
	}
	
}
