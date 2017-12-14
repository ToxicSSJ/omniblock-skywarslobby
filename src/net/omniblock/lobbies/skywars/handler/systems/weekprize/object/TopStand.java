package net.omniblock.lobbies.skywars.handler.systems.weekprize.object;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;

import net.omniblock.lobbies.skywars.handler.SkywarsLobby;

public class TopStand {

	public static enum TopType {
		
		TOP_1(1),
		TOP_2(2),
		TOP_3(3),
		
		;
		
		private int top = 1;
		
		TopType(int top){
			this.top = top;
		}
		
		public int getTop(){
			return top;
		}
		
		public static TopType parseInt(int top){
			
			for(TopType type : TopType.values()){
				if(type.getTop() == top){
					return type;
				}
			}
			
			return TopType.TOP_1;
			
		}
		
		public TopStand getStand(SkywarsLobby lobby){
			
			Map<String, List<Location>> scan = lobby.getLastScan();
			
			switch(this){
			
			case TOP_1: return new TopStand(this, scan.get("WEEK_PRIZE_TOP1").get(0));
			case TOP_2: return new TopStand(this, scan.get("WEEK_PRIZE_TOP2").get(0));        
			case TOP_3: return new TopStand(this, scan.get("WEEK_PRIZE_TOP3").get(0));
			
			default:
				
			}
			
			return null;
			
		}
		
	}
	
	private TopType top;
	private Location loc;
	
	public TopStand(TopType top, Location loc){
		
		this.top = top;
		this.loc = loc;
		
	}

	public TopType getTop() {
		return top;
	}

	public Location getLocation() {
		return loc;
	}
	
	public void changeLocation(Location newloc){
		
		this.loc = newloc;
		return;
		
	}
	
}
