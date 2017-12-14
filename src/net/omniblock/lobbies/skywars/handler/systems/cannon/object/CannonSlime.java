package net.omniblock.lobbies.skywars.handler.systems.cannon.object;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;

import net.omniblock.network.library.utils.TextUtil;

public abstract interface CannonSlime {
	
	public static Slime craftCannonSlime(Location location){
		
		Slime ball = getSlime(location);
		ball.teleport(location);
		
		return ball;
		
	}
	
	public static Slime getSlime(Location location){
		
		Slime cannon_slime = (Slime) location.getWorld().spawnEntity(location, EntityType.SLIME);
		
		cannon_slime.setSize(2);
		cannon_slime.setInvulnerable(true);
		
		cannon_slime.setCustomName(TextUtil.format("&4&l¡Bala de Cañon!"));
		return cannon_slime;
		
	}
	
	public static void setSlimeLiveSeconds(Slime slime, int seconds){
		
		slime.setTicksLived(seconds * 20);
		return;
		
	}
	
}
