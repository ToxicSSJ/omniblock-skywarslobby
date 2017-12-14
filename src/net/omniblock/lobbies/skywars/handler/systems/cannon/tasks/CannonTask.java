package net.omniblock.lobbies.skywars.handler.systems.cannon.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	int round = 0;
	int maxRound = 320;
	
	int cannon = -1;
	int maxCannon = 0;
	
	Map<Block, Block> towardsBlocks = new HashMap<Block, Block>();
	Map<Block, BlockFace> faceBlocks = new HashMap<Block, BlockFace>();
	
	public CannonTask(SkywarsLobby lobby){
		
		List<Location> locations = lobby.getLastScan().get("BOAT_CANNON");
		
		for(Location location : locations){
			
			Block block = location.getBlock();
			
			if(block.getRelative(BlockFace.EAST).getType() == Material.AIR){
				
				towardsBlocks.put(block.getRelative(BlockFace.UP), block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST));
				faceBlocks.put(block.getRelative(BlockFace.UP), BlockFace.EAST);
				continue;
				
			} else if(block.getRelative(BlockFace.WEST).getType() == Material.AIR){
				
				towardsBlocks.put(block.getRelative(BlockFace.UP), block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST));
				faceBlocks.put(block.getRelative(BlockFace.UP), BlockFace.WEST);
				continue;
				
			} else if(block.getRelative(BlockFace.NORTH).getType() == Material.AIR){
				
				towardsBlocks.put(block.getRelative(BlockFace.UP), block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH));
				faceBlocks.put(block.getRelative(BlockFace.UP), BlockFace.NORTH);
				continue;
				
			} else if(block.getRelative(BlockFace.SOUTH).getType() == Material.AIR){
				
				towardsBlocks.put(block.getRelative(BlockFace.UP), block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH));
				faceBlocks.put(block.getRelative(BlockFace.UP), BlockFace.SOUTH);
				continue;
				
			}
			
		}
		
		maxCannon = towardsBlocks.size();
		
	}

	@Override
	public void run() {
		
		round++;
		
		if(round >= maxRound){
			
			round = 0;
			cannon = 0;
			
		}
		
		if(cannon != -1){
			
			if(cannon >= maxCannon){ cannon = -1; return; }
			
			List<Block> keys = new ArrayList<Block>();
			
			towardsBlocks.keySet().stream().forEach(k -> keys.add(k));
			
			Block key = keys.get(cannon);
			BlockFace face = faceBlocks.get(key);
			
			Block value = towardsBlocks.get(key);
			Block newvalue = value;
			
			for(int i = 0; i >= 12; i++){
				
				newvalue = newvalue.getRelative(face);
				continue;
				
			}
			
			ExplodeEffect effect = new ExplodeEffect(GeneralHandler.getEffectLibManager());
			
			effect.setLocation(key.getLocation());
			effect.setTargetLocation(key.getLocation());
			
			effect.visibleRange = 200;
			effect.iterations = 2;
			effect.start();
			
			Slime slime = CannonSlime.craftCannonSlime(key.getLocation());
			CannonSlime.setSlimeLiveSeconds(slime, 35);
			
			moveSlimeBullet(slime, newvalue.getLocation());
			explodeWhenHit(slime);
			cannon++;
			
		}
		
	}
	
	public void explodeWhenHit(Slime bullet){
		
		new BukkitRunnable(){

			int round = 0;
			
			@Override
			public void run() {
				
				boolean explode = false;
				
				if(bullet.isDead()){ cancel(); return; }
				
				if(bullet.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) explode = true;
				if(round == 40) explode = true;
				
				if(explode){
					
					bullet.getWorld().playSound(bullet.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 20, -5);
					ParticleEffect.CLOUD.display(1, 1, 1, 1, 30, bullet.getLocation().add(.5, 0, .5), 200);
					
					bullet.remove();
					
				}
				
				round++;
				
			}
			
		}.runTaskTimer(OmniLobbies.getInstance(), 5L, 5L);
		
	}
	
	public void moveSlimeBullet(Slime bullet, Location to) {
		
		double multiply = NumberUtil.getRandomDouble(3.6, 5.2);
		
		Location loc = bullet.getLocation();
        double x = loc.getX() - to.getX();
        double y = loc.getY() - to.getY();
        double z = loc.getZ() - to.getZ();
        
        Vector v = new Vector(x, y, z).normalize().multiply(-multiply);
		bullet.setVelocity(v);
		
	}
	
}
