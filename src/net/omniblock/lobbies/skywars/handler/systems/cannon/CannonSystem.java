package net.omniblock.lobbies.skywars.handler.systems.cannon;

import net.omniblock.lobbies.OmniLobbies;
import net.omniblock.lobbies.api.Lobby;
import net.omniblock.lobbies.api.object.LobbySystem;
import net.omniblock.lobbies.skywars.handler.SkywarsLobby;
import net.omniblock.lobbies.skywars.handler.systems.cannon.tasks.CannonTask;

public class CannonSystem implements LobbySystem {

	protected SkywarsLobby lobby;
	
	protected CannonTask task;
	
	@Override
	public void setup(Lobby lobby) {
		
		if(lobby instanceof SkywarsLobby) {
			
			this.lobby = (SkywarsLobby) lobby;
			
			if(this.lobby.getSide() == SkywarsLobby.SKYWARS_Z_SIDE)
				this.task = new CannonTask(this.lobby);
			
			return;
			
		}
		
	}

	@Override
	public void start() {
		
		if(this.task != null)
			this.task.runTaskTimer(OmniLobbies.getInstance(), 10L, 10L);
		
		return;
		
	}

	@Override
	public void destroy() {
		
		if(this.task != null)
			this.task.cancel();
		
		this.task = null;
		
		return;
		
	}

}
