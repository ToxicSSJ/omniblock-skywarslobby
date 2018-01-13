package net.omniblock.lobbies.skywars.handler.systems.kitblazer.object;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.omniblock.lobbies.OmniLobbies;
import net.omniblock.lobbies.apps.general.GeneralHandler;
import net.omniblock.lobbies.skywars.handler.SkywarsLobby;
import net.omniblock.lobbies.skywars.handler.base.SkywarsBase;
import net.omniblock.lobbies.skywars.handler.systems.SWKits.KitKind;
import net.omniblock.lobbies.skywars.handler.systems.SWKits.SWKitsType;
import net.omniblock.network.library.helpers.effectlib.EffectManager;
import net.omniblock.network.library.helpers.effectlib.effect.LineEffect;
import net.omniblock.network.library.helpers.effectlib.effect.TextEffect;
import net.omniblock.network.library.helpers.effectlib.util.ParticleEffect;
import net.omniblock.network.library.utils.NumberUtil;
import net.omniblock.network.library.utils.TextUtil;

public class Animation {

	protected SkywarsLobby animationLobby;

	protected static KitKind kitKind;

	protected static Player player;
	
	protected Block animationBlock;
	
	protected static SWKitsType kitType;
	protected static List<SWKitsType> KitInList = new ArrayList<SWKitsType>();

	protected static final String ANIMATION_KIT_FREE = "KIT_FREE";
	protected static final String ANIMATION_KIT_SURPRISE = "KIT_SURPRISE";

	protected EffectManager manager = new EffectManager(OmniLobbies.getInstance());

	protected boolean animationRunning = false;

	protected String[] textAnimationWithDeath = new String[] {

			"&b&l 1%", "&b&l 5%", 
			"&b&l 8%", "&b&l 12%", 
			"&b&l 15%", "&b&l 18%", 
			"&b&l 20%", "&b&l 22%", 
			"&b&l 26%","&b&l 30%", 
			"&b&l 34%", "&b&l 38%", 
			"&b&l 41%", "&b&l 45%",
			"&b&l 49%","&b&l 54%",
			"&b&l 57%", "&b&l 65%",
			"&b&l 67%", "&b&l 69%", 
			"&b&l 73%", "&b&l 83%", 
			"&b&l 86%", "&b&l 89%",
			"&b&l 96%", "&b&l 98%", 
			"&b&l 99%","&4&l 100%" 
			
	};
	

	protected boolean animationRunning() {
		
		if (!player.isOnline())
			return true;
		
		if (animationRunning) {

			player.closeInventory();
			player.sendMessage(TextUtil.format("&4Kit Blazer &7ya esta preparando un kit."));
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, -1);
			return true;

		}
		
		animationRunning = true;
		return false;
	}
	
	protected static void preparePlayerKit(String typeKit) {
		
		if(typeKit == ANIMATION_KIT_FREE) {
			
			for (SWKitsType kt : SWKitsType.values()) {

				boolean haskits = ArrayUtils.contains(SkywarsBase.getItems(player).split(";"), kt.getCode());

				if (kt.getKind() != kitKind) continue;
				if (haskits) continue;
				if (!haskits) KitInList.add(kt);
			}
			
			return;
		}
		
		if(typeKit == ANIMATION_KIT_SURPRISE) {
			
			for (SWKitsType kt : SWKitsType.values()) {

				boolean haskits = ArrayUtils.contains(SkywarsBase.getItems(player).split(";"), kt.getCode());

				if (haskits) continue;
				if (!haskits) KitInList.add(kt);
			}
		}
		return;
	}
	
	protected boolean playerHasAllKit() {
		
		if (KitInList.size() <= 0) {

			player.sendMessage(TextUtil.format("&7Lo sentimos, ya no quedan Kits disponibles. &a¡Ya los tienes todos!"));
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, -1);
			animationRunning = false;
			return true;
		}
		
		return false;
	}
	
	protected void textAnimation() {

		Location cacheloc = animationLobby.getLastScan().get("MYSTERY_BOX").get(0);
		Location location = new Location(cacheloc.getWorld(), cacheloc.getX(),cacheloc.getY() + 10, cacheloc.getZ(), (float) -142.2,(float) -12.9);
		Location seconlocation = new Location(cacheloc.getWorld(), cacheloc.getX(), cacheloc.getY() + 7, cacheloc.getZ(), (float) -142.2, (float) -12.9);
		
		TextEffect effect = new TextEffect(manager);
		effect.setLocation(location);
		effect.text = "KIT";
		effect.particle = ParticleEffect.FLAME;
		effect.color = Color.RED;
		effect.visibleRange = 500;
		effect.autoOrient = true;
		effect.period = 15;
		effect.iterations = 1;
		effect.start();
				
		TextEffect eff = new TextEffect(manager);
		eff.setLocation(seconlocation);
		eff.particle = ParticleEffect.FIREWORKS_SPARK;
		eff.text = "¡ " + kitType.getName() + " !";
		eff.visibleRange = 500;
		eff.autoOrient = true;
		eff.period = 5;
		eff.iterations = 1;
		eff.start();

		player.sendMessage("");
		player.sendMessage(TextUtil.format("&r&b&l&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
		player.sendMessage("");
		player.sendMessage(TextUtil.getCenteredMessage("&r&7&l¡Increíble!", true));
		player.sendMessage("");
		player.sendMessage(TextUtil.getCenteredMessage("&r&7&lConseguiste el Kit " + "&e" + kitType.getName(), true));
		player.sendMessage("");
		player.sendMessage(TextUtil.format("&r&b&l&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
		player.sendMessage("");
					
	}
	
	protected void guardianShoot(Entity entity, Entity toshoot) {

		if (toshoot.getType() == EntityType.BLAZE) {

			toshoot.getWorld().playSound(toshoot.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 5, -2);

			Blaze affected = (Blaze) toshoot;
			Guardian guardian = (Guardian) entity;

			LineEffect ef = new LineEffect(GeneralHandler.getEffectLibManager());
			ef.particle = ParticleEffect.REDSTONE;
			ef.color = Color.ORANGE;
			ef.autoOrient = true;
			ef.visibleRange = 200;
			ef.setLocation(guardian.getLocation());
			ef.setTargetLocation(affected.getEyeLocation());
			ef.start();
		}
	}
	
	protected SWKitsType getKit() {
		
		SWKitsType kit = KitInList.get(NumberUtil.getRandomInt(0, KitInList.size() - 1));
		return kit;
	}
	
	protected void removeDropItems() {

		List<Entity> entList = animationBlock.getWorld().getEntities();

		for (Entity current : entList) {
			if (current instanceof Item) {
				current.remove();
			}
		}
	}

	protected void bounceEntity(Entity entity) {

		float x = (float) -1 + (float) (Math.random() * ((1 - -1) + 1));
		float y = (float) 0.5;
		float z = (float) -0.3 + (float) (Math.random() * ((0.3 - -0.3) + 1));

		entity.setVelocity(new Vector(x, y, z));
	}
}
