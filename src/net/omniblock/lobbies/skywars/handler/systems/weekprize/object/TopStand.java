package net.omniblock.lobbies.skywars.handler.systems.weekprize.object;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;

import net.omniblock.lobbies.skywars.handler.SkywarsLobby;

public class TopStand {

	public static enum TopType {
		
		TOP_1(1, 90, 0, "SkidzInc", Material.DIAMOND),
		TOP_2(2, 90, 0, "Trajan", Material.GOLD_INGOT),
		TOP_3(3, 90, 0, "TheSkidz", Material.IRON_INGOT),
		
		;
		
		private int top = 1, yaw = 0, pitch = 0;
		
		private String skinName = "SkidzInc";
		private Material material = Material.AIR;
		
		TopType(int top, int yaw, int pitch, String skinName, Material material){
			
			this.top = top;
			this.yaw = yaw;
			this.pitch = pitch;
			
			this.skinName = skinName;
			this.material = material;
			
		}
		
		public int getTop(){
			return top;
		}
		
		public int getYaw() {
			return yaw;
		}
		
		public int getPitch() {
			return pitch;
		}
		
		public String getSkinName() {
			return skinName;
		}
		
		public Material getMaterial() {
			return material;
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
