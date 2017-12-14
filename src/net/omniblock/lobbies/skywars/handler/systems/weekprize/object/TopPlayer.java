package net.omniblock.lobbies.skywars.handler.systems.weekprize.object;

import java.util.UUID;

public class TopPlayer {

	private String name;
	private UUID uuid;
	
	private Integer points;
	
	public TopPlayer(String name, UUID uuid, Integer points){
		
		this.name = name;
		this.uuid = uuid;
		
		this.points = points;
		
	}

	public String getName() {
		return name;
	}

	public Integer getPoints() {
		return points;
	}

	public UUID getUuid() {
		return uuid;
	}
	
}
