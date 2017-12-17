package net.omniblock.lobbies.skywars.handler.systems.weekprize.object;

public class TopPlayer {

	private String name;
	
	private Integer points;
	
	public TopPlayer(String name, Integer points){
		
		this.name = name;
		this.points = points;
		
	}

	public String getName() {
		return name;
	}

	public Integer getPoints() {
		return points;
	}
	
}
