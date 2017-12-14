package net.omniblock.lobbies.skywars.handler.systems.swjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import net.citizensnpcs.api.npc.NPC;
import net.omniblock.lobbies.api.Lobby;
import net.omniblock.lobbies.api.object.LobbySystem;
import net.omniblock.lobbies.skywars.handler.SkywarsLobby;
import net.omniblock.lobbies.skywars.handler.systems.swjoin.type.SkywarsJoinType;
import net.omniblock.packets.util.Lists;

public class SkywarsJoinSystem implements LobbySystem {

	public static List<Entry<SkywarsJoinType, Entry<NPC, Hologram>>> displayedEntries = new ArrayList<Entry<SkywarsJoinType, Entry<NPC, Hologram>>>();
	protected SkywarsLobby lobby;
	
	@Override
	public void setup(Lobby lobby) {
		
		if(lobby instanceof SkywarsLobby) {
			
			this.lobby = (SkywarsLobby) lobby;
			
		}
		
	}

	@Override
	public void start() {
		
		if(this.lobby == null)
			return;
		
		if(this.lobby.getSide() != null) {
			
			List<Entry<SkywarsJoinType, Entry<NPC, Hologram>>> cache = Lists.newArrayList();
			
			if(displayedEntries.isEmpty() || displayedEntries.size() == 0) {
				
				for(SkywarsJoinType type : SkywarsJoinType.values()) {
					
					if(type != SkywarsJoinType.SKYWARS_SOLO_Z && type != SkywarsJoinType.SKYWARS_TEAM_Z) {
						displayedEntries.add(type.spawnJoin(this.lobby.getLastScan().get(type.name()).get(0)));
					}
					
					if(this.lobby.getSide() == SkywarsLobby.SKYWARS_Z_SIDE &&
							(type == SkywarsJoinType.SKYWARS_SOLO_Z || type == SkywarsJoinType.SKYWARS_TEAM_Z))
						displayedEntries.add(type.spawnJoin(this.lobby.getLastScan().get(type.name()).get(0)));
					
					continue;
					
				}
				
				return;
				
			}
			
			for(Entry<SkywarsJoinType, Entry<NPC, Hologram>> entry : displayedEntries) {
				
				if(entry.getKey() != SkywarsJoinType.SKYWARS_SOLO_Z && entry.getKey() != SkywarsJoinType.SKYWARS_TEAM_Z) {
					
					if(entry.getValue().getKey().isSpawned()) {
						
						Location location = this.lobby.getLastScan().get(entry.getKey().name()).get(0);
						
						entry.getValue().getKey().teleport(location, TeleportCause.PLUGIN);
						entry.getValue().getValue().teleport(location.clone().add(0, 3.3, 0));
						continue;
						
					}
					
					if(!entry.getValue().getValue().isDeleted())
						entry.getValue().getValue().delete();
					
					cache.add(entry.getKey().spawnJoin(this.lobby.getLastScan().get(entry.getKey().name()).get(0)));
					continue;
					
				}
				
				if(this.lobby.getSide() == SkywarsLobby.SKYWARS_Z_SIDE &&
						(entry.getKey() == SkywarsJoinType.SKYWARS_SOLO_Z || entry.getKey() == SkywarsJoinType.SKYWARS_TEAM_Z)) {
					
					if(entry.getValue().getKey().isSpawned()) {
						
						Location location = this.lobby.getLastScan().get(entry.getKey().name()).get(0);
						
						entry.getValue().getKey().teleport(location, TeleportCause.PLUGIN);
						entry.getValue().getValue().teleport(location.clone().add(0, 3.3, 0));
						continue;
						
					}
					
					if(!entry.getValue().getValue().isDeleted())
						entry.getValue().getValue().delete();
					
					cache.add(entry.getKey().spawnJoin(this.lobby.getLastScan().get(entry.getKey().name()).get(0)));
					continue;
					
				}
				
				continue;
				
			}
			
			for(SkywarsJoinType type : SkywarsJoinType.values()) {
				
				Entry<SkywarsJoinType, Entry<NPC, Hologram>> result = displayedEntries.stream().filter(entry -> entry.getKey() == type).findAny().orElse(null);
				SkywarsJoinType temporalType = result == null ? null : result.getKey();
				
				if(temporalType == null) {
					
					if(type != SkywarsJoinType.SKYWARS_SOLO_Z && type != SkywarsJoinType.SKYWARS_TEAM_Z) {
						displayedEntries.add(type.spawnJoin(this.lobby.getLastScan().get(type.name()).get(0)));
					}
					
					if(this.lobby.getSide() == SkywarsLobby.SKYWARS_Z_SIDE &&
							(type == SkywarsJoinType.SKYWARS_SOLO_Z || type == SkywarsJoinType.SKYWARS_TEAM_Z))
						displayedEntries.add(type.spawnJoin(this.lobby.getLastScan().get(type.name()).get(0)));
					
					continue;
					
				}
				
			}
			
			if(cache.size() > 0)
				cache.forEach(data -> displayedEntries.add(data));
			
		}
		
	}

	@Override
	public void destroy() { return; }
	
}
