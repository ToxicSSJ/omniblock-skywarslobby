package net.omniblock.lobbies.skywars.handler.systems.cannon.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.omniblock.lobbies.skywars.SkywarsLobbyPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Slime;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.omniblock.lobbies.OmniLobbies;
import net.omniblock.lobbies.apps.general.GeneralHandler;
import net.omniblock.lobbies.skywars.handler.SkywarsLobby;
import net.omniblock.lobbies.skywars.handler.systems.cannon.object.CannonSlime;
import net.omniblock.network.library.helpers.effectlib.effect.ExplodeEffect;
import net.omniblock.network.library.helpers.effectlib.util.ParticleEffect;
import net.omniblock.network.library.utils.NumberUtil;

public class CannonTask extends BukkitRunnable {

	private int logicIteration = 0;
	private int maxLogicIteration = 60;
	private int currentFiringCannon = 0;
	private boolean coolingCannons = false;
	private List<CannonInfo> cannons = new ArrayList<>();
	
	public CannonTask(SkywarsLobby lobby){

		for(Location cannonLocation : lobby.getLastScan().get("BOAT_CANNON")){
			Block cannonBlock = cannonLocation.getBlock();
			Location offsetLocation = null;
			
			if(cannonBlock.getRelative(BlockFace.EAST).getType() == Material.AIR){
				offsetLocation = cannonBlock.getRelative(BlockFace.EAST).getLocation();
			} else if(cannonBlock.getRelative(BlockFace.WEST).getType() == Material.AIR){
				offsetLocation = cannonBlock.getRelative(BlockFace.WEST).getLocation();
			} else if(cannonBlock.getRelative(BlockFace.NORTH).getType() == Material.AIR){
				offsetLocation = cannonBlock.getRelative(BlockFace.NORTH).getLocation();
			} else if(cannonBlock.getRelative(BlockFace.SOUTH).getType() == Material.AIR){
				offsetLocation = cannonBlock.getRelative(BlockFace.SOUTH).getLocation();
			}

			if(offsetLocation == null) {
				Logger log = SkywarsLobbyPlugin.getInstance().getLogger();
				log.warning("Un cañon parece no tener un bloque proximo libre hacia donde disparar, este cañon se omitira de la logica de disparo.");
				log.warning("Localizacion del cañon: " + cannonLocation);
				return;
			}

			cannons.add(new CannonInfo(cannonBlock.getLocation(), offsetLocation));
		}
	}

	@Override
	public void run() {
		logicIteration++;
		
		if(logicIteration >= maxLogicIteration){
			logicIteration = 0;
			currentFiringCannon = 0;
			coolingCannons = false;
		}

		if(currentFiringCannon == cannons.size()) {
			coolingCannons = true;
		}

		if(coolingCannons) {
			return;
		}

		CannonInfo cannonInfo = cannons.get(currentFiringCannon);
		Location cannonLocation = cannonInfo.location.clone();

		ExplodeEffect effect = new ExplodeEffect(GeneralHandler.getEffectLibManager());
		effect.setLocation(cannonLocation);
		effect.setTargetLocation(cannonLocation);
		effect.visibleRange = 200;
		effect.iterations = 2;
		effect.start();

		Slime slime = CannonSlime.craftCannonSlime(cannonInfo.offsetLocation.clone());
		moveCannonBullet(slime, cannonInfo);
		aplyCannonBulletLogic(slime);

		cannonLocation.getWorld().playSound(cannonLocation, Sound.ENTITY_GENERIC_EXPLODE, 5, -4);
		ParticleEffect.CLOUD.display(1, 1, 1, 1, 30, cannonLocation.clone().add(.5, 0, .5), 200);

		currentFiringCannon++;
	}
	
	public void aplyCannonBulletLogic(Slime entity){
		
		new BukkitRunnable(){
			@Override
			public void run() {
				boolean explode = false;
				
				if(entity.isDead()){
					cancel();
					return;
				}

				//if(entity.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) explode = true;

				if(entity.getTicksLived() >= 200){
					entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_ITEM_PICKUP, 5, -3);
					ParticleEffect.VILLAGER_HAPPY.display(1, 1, 1, 1, 10, entity.getLocation().add(.5, 0, .5), 200);
					entity.remove();
				}
			}
		}.runTaskTimer(OmniLobbies.getInstance(), 5L, 5L);
	}
	
	public void moveCannonBullet(Slime entity, CannonInfo cannonInfo) {

		double multiply = NumberUtil.getRandomDouble(2, 4);
		
		Location entityLocation = entity.getLocation();

		Vector velocity = cannonInfo.offsetLocation.clone().toVector().subtract(cannonInfo.location.clone().toVector());
		entity.setVelocity(velocity.normalize().multiply(multiply));
	}

	private class CannonInfo {
		private Location location;
		private Location offsetLocation;

		private CannonInfo(Location location, Location offsetLocation) {
			this.location = location;
			this.offsetLocation = offsetLocation;
		}
	}
	
}
