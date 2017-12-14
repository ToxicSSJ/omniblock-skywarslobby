package net.omniblock.lobbies.skywars.handler.systems.weekprize;

import java.time.LocalTime;
import java.util.AbstractMap;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.omniblock.lobbies.OmniLobbies;
import net.omniblock.lobbies.api.Lobby;
import net.omniblock.lobbies.api.object.LobbySystem;
import net.omniblock.lobbies.skywars.handler.SkywarsLobby;
import net.omniblock.lobbies.skywars.handler.systems.weekprize.object.TopPlayer;
import net.omniblock.lobbies.skywars.handler.systems.weekprize.object.TopStand;
import net.omniblock.lobbies.skywars.handler.systems.weekprize.object.TopStand.TopType;
import net.omniblock.lobbies.skywars.handler.systems.weekprize.packets.WeekPrizePackets;
import net.omniblock.lobbies.skywars.handler.systems.weekprize.packets.WeekPrizePackets.WeekPrizeStatus;
import net.omniblock.network.handlers.base.sql.make.MakeAdvancedSQLQuery;
import net.omniblock.network.handlers.base.sql.type.TableType;
import net.omniblock.network.handlers.base.sql.util.Resolver;
import net.omniblock.network.handlers.base.sql.util.SQLResultSet;
import net.omniblock.network.library.helpers.ItemBuilder;
import net.omniblock.network.library.helpers.RandomFirework;
import net.omniblock.network.library.helpers.effectlib.EffectManager;
import net.omniblock.network.library.helpers.effectlib.effect.TextEffect;
import net.omniblock.network.library.helpers.effectlib.util.ParticleEffect;
import net.omniblock.network.library.utils.NumberUtil;
import net.omniblock.network.library.utils.TextUtil;

public class WeekPrizeSystem implements LobbySystem, Listener {

	protected static final String TOP_QUERY_SQL = "SELECT p_id FROM skywars_weekprize ORDER BY p_points DESC LIMIT 3";
	
	protected BukkitTask task;
	protected BukkitTask textTask;
	protected BukkitTask updaterTask;
	
	protected Location topTextLocation;
	protected SkywarsLobby lobby;
	
	protected boolean destroy = false;
	
	protected Map<Integer, TopPlayer> topPlayers = new HashMap<Integer, TopPlayer>();
	protected LinkedList<Entry<NPC, NPC>> topNPCs = new LinkedList<Entry<NPC, NPC>>();
	
	@Override
	public void setup(Lobby lobby) {
		
		if(lobby instanceof SkywarsLobby) {
			
			this.lobby = (SkywarsLobby) lobby;
			
			OmniLobbies.getInstance().getServer().getPluginManager().registerEvents(this, OmniLobbies.getInstance());
			return;
			
		}
		
	}

	@Override
	public void start() {
		
		WeekPrizePackets.startQuery();
		
		updateTop();
		displayCountdown();
		displayTop();
		
	}

	private void updateTop(){
		
		updaterTask = new BukkitRunnable(){
			
			@Override
			public void run(){
				
				try {
					
					if(destroy) {
						this.cancel();
						return;
					}
					
					topPlayers.clear();
					
					SQLResultSet query = new MakeAdvancedSQLQuery(TableType.TOP_STATS_WEEKPRIZE_SKYWARS)
							.append(TOP_QUERY_SQL)
							.execute();
					
					int index = 1;
					
					while(query.next()){
						
						TopPlayer player = new TopPlayer(
								Resolver.getLastNameByNetworkID(query.get("p_id")),
								Resolver.isPremium((String) query.get("p_id")) ?
										Resolver.getOfflineUUIDByName(query.get("p_id")) :
										Resolver.getOfflineUUIDByNetworkID(query.get("p_id")),
								0);
						
						topPlayers.put(index, player);
						
						index++;
						
					}
					
				} catch (Exception e) { e.printStackTrace(); }
				
			}
			
		}.runTaskTimer(OmniLobbies.getInstance(), 0, 20 * 30);
		
	}
	
	private void displayCountdown(){
		
		textTask = new BukkitRunnable(){
			
			EffectManager manager = new EffectManager(OmniLobbies.getInstance());
			TextEffect effect = new TextEffect(manager);
			
			Location cacheloc = lobby.getLastScan().get("WEEK_PRIZE_TEXT").get(0);
			Location location = new Location(cacheloc.getWorld(), cacheloc.getX(), cacheloc.getY() + 2, cacheloc.getZ(), -88, (float) 4.2);
			
			int animround = 0;
			
			boolean animation = false, animationp = false;
		
			@Override
			public void run(){
				
				if(destroy) {
					this.cancel();
					return;
				}
				
				Calendar until = Calendar.getInstance();
				Calendar today = Calendar.getInstance();
				
				if(WeekPrizePackets.getStatus() == WeekPrizeStatus.WAITING) return;
				if(WeekPrizePackets.getDate() != null) until.setTime(WeekPrizePackets.getDate());
				
				int countdown = (int) (WeekPrizePackets.getDate() == null ? 86405 : (until.getTimeInMillis() - today.getTimeInMillis()) / 1000);
				
				if(effect != null)
					if(!effect.isDone())
						effect.cancel(false);
				
				effect = new TextEffect(manager);
				effect.setLocation(location);

				effect.text = countdown >= 86400 ? "+24:00:00" : countdown <= 0 ? "" : String.valueOf(LocalTime.ofSecondOfDay(countdown));
				effect.particle = ParticleEffect.FLAME;
				effect.color = Color.RED;
				effect.visibleRange = 500;
				effect.autoOrient = true;
				effect.period = 15;
				effect.iterations = 1;
				
				if(animround > 0 && animation){
					
					animround++;
					
					if(animround > 0 && animround < 6)
						for(int i = 0; i < 5; i++)
							RandomFirework.spawnRandomFirework(location.clone().add(NumberUtil.getRandomInt(-5, 5), NumberUtil.getRandomInt(-5, 5), NumberUtil.getRandomInt(-5, 5)), 3);
					
					if(animround > 5 && animround < 11){
						
						if(!animationp){
							
							animationp = true;
							
							new BukkitRunnable(){
								
								int soundpitch = 15;
								int round = -1;
								
								@SuppressWarnings("deprecation")
								@Override
								public void run(){
									
									round++;
									
									if(round == 130){
										
										animround = 0;
										
										animation = false;
										animationp = false;
										
										topNPCs.clear();
										
										topNPCs.add(spawnNPC(TopType.TOP_1.getStand(lobby), CitizensAPI.getNPCRegistry()));
										topNPCs.add(spawnNPC(TopType.TOP_2.getStand(lobby), CitizensAPI.getNPCRegistry()));
										topNPCs.add(spawnNPC(TopType.TOP_3.getStand(lobby), CitizensAPI.getNPCRegistry()));
										
										topNPCs.stream().forEach(entry -> entry.getKey().getEntity().getWorld().strikeLightningEffect(entry.getKey().getEntity().getLocation()));
										
										cancel();
										return;
										
									}
									
									if(round == 70){
										
										location.getWorld().playSound(location, Sound.ENTITY_ENDERDRAGON_DEATH, 10, 10);
										
										topNPCs.stream().forEach(entry -> {
											
											location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 10, 10);
											
											ParticleEffect.ENCHANTMENT_TABLE.display(cacheloc.getBlockX(), cacheloc.getBlockY(), cacheloc.getBlockZ(), 5, 50, location, 100);
											
											ParticleEffect.EXPLOSION_LARGE.display(entry.getValue().getEntity().getLocation().add(.5, 1, .5), 100);
											ParticleEffect.LAVA.display(entry.getValue().getEntity().getLocation().add(.5, 1, .5), 100);
											
											entry.getValue().despawn();
											
										});
										
										return;
										
									}
									
									if(round == 51){
										
										soundpitch = 20;
										
									}
									
									if(round > 51 && round < 70){
										
										location.getWorld().playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10, soundpitch--);
										location.getWorld().playSound(location, Sound.BLOCK_NOTE_BASS, 10, 10);
										
										topNPCs.stream().forEach(entry -> {
											
											entry.getValue().teleport(new Location(
													location.getWorld(), 
													entry.getValue().getEntity().getLocation().getX(),
													entry.getValue().getEntity().getLocation().getY(),
													entry.getValue().getEntity().getLocation().getZ(),
													entry.getValue().getEntity().getLocation().getPitch(),
													entry.getValue().getEntity().getLocation().getYaw()), TeleportCause.PLUGIN);
											
											ParticleEffect.EXPLOSION_NORMAL.display(entry.getValue().getEntity().getLocation().add(.5, 1, .5), 100);
											
										});
										
									}
									
									if(round < 51){
										
										location.getWorld().playSound(location, Sound.BLOCK_NOTE_PLING, 10, soundpitch--);
										
										topNPCs.stream().forEach(entry -> {
											
											entry.getValue().teleport(new Location(
													location.getWorld(), 
													entry.getValue().getEntity().getLocation().getX(),
													entry.getValue().getEntity().getLocation().getY() + 0.2,
													entry.getValue().getEntity().getLocation().getZ(),
													entry.getValue().getEntity().getLocation().getPitch(),
													entry.getValue().getEntity().getLocation().getYaw()), TeleportCause.PLUGIN);
											
											ParticleEffect.CLOUD.display(entry.getValue().getEntity().getLocation(), 100);
											
										});
										
									}
									
								}
								
							}.runTaskTimer(OmniLobbies.getInstance(), 20L, 3L);
							
						}
						
					}
					
				} else {
					
					countdown--;
					
					if(countdown < 5 && !animation){
						
						animround = 1;
						animation = true;
						
						topNPCs.stream().forEach(entry -> {
							
							ArmorStand asnpc = (ArmorStand) entry.getValue().getEntity();
							asnpc.getEquipment().setHelmet(
									new ItemBuilder(Material.SKULL_ITEM)
									.setSkullOwner(entry.getKey().getName())
									.durability((short) 3)
									.amount(1)
									.build());
							
							asnpc.setCustomNameVisible(false);
							
							entry.getKey().despawn();
							
						});
						return;
						
					}
					
					effect.start();
					
				}
				
			}
			
		}.runTaskTimer(OmniLobbies.getInstance(), 10L, 10L);
		
	}
	
	private void displayTop(){
		
		new BukkitRunnable(){

			@Override
			public void run() {
				
				if(destroy) {
					this.cancel();
					return;
				}
				
				NPCRegistry registry = CitizensAPI.getNPCRegistry();
				
				topNPCs.add(spawnNPC(TopType.TOP_1.getStand(lobby), registry));
				topNPCs.add(spawnNPC(TopType.TOP_2.getStand(lobby), registry));
				topNPCs.add(spawnNPC(TopType.TOP_3.getStand(lobby), registry));
				
				task = new BukkitRunnable(){

					@Override
					public void run() {
						
						if(topPlayers.size() >= 3){
							
							TopPlayer[] tops = new TopPlayer[]{
									
									topPlayers.get(1),
									topPlayers.get(2),
									topPlayers.get(3)
									
									};
							
							if(!topNPCs.get(0).getKey().isSpawned() ||
									!topNPCs.get(1).getKey().isSpawned() ||
									!topNPCs.get(2).getKey().isSpawned()) return;
							
							if(topNPCs.get(0).getKey().getName() != 
									tops[0].getName()){
								
								topNPCs.get(0).getKey().setName(tops[0].getName());
								topNPCs.get(0).getKey().data().set(NPC.PLAYER_SKIN_UUID_METADATA, tops[0].getName());
								
							}
							if(topNPCs.get(1).getKey().getName() != 
									tops[1].getName()){
								
								topNPCs.get(1).getKey().setName(tops[1].getName());
								topNPCs.get(1).getKey().data().set(NPC.PLAYER_SKIN_UUID_METADATA, tops[1].getName());
								
							}
							if(topNPCs.get(2).getKey().getName() != 
									tops[2].getName()){
								
								topNPCs.get(2).getKey().setName(tops[2].getName());
								topNPCs.get(2).getKey().data().set(NPC.PLAYER_SKIN_UUID_METADATA, tops[2].getName());
										
							}
							
						}
						
					}
					
				}.runTaskTimer(OmniLobbies.getInstance(), 5L, 5L);
				
			}
			
		}.runTaskLater(OmniLobbies.getInstance(), 20L);
		
	}
	
	private Entry<NPC, NPC> spawnNPC(TopStand stand, NPCRegistry registry){
		
		NPC npc = registry.createNPC(EntityType.PLAYER, "Desconocido");
		npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA,
				stand.getTop() == TopType.TOP_1 ? "SkidzInc" :
				stand.getTop() == TopType.TOP_2 ? "Trajan" : "TheSkidz");
		
		Location location = stand.getLocation();
		
		location = new Location(
				location.getWorld(),
				location.getX(),
				location.getY(),
				location.getZ(), 
				stand.getTop() == TopType.TOP_1 ? 89 :
					stand.getTop() == TopType.TOP_2 ? 97 : 78,
							stand.getTop() == TopType.TOP_1 ? 18 :
								stand.getTop() == TopType.TOP_2 ? 10 : 9);
		
		npc.spawn(location);
		npc.addTrait(Equipment.class);
		npc.getEntity().setMetadata("NPC", new FixedMetadataValue(OmniLobbies.getInstance(), "NPC"));
		
		NPC hud = registry.createNPC(EntityType.ARMOR_STAND, TextUtil.format("&aTOP #" + stand.getTop().getTop() + " &8- &7" + 0));
		hud.spawn(location);
		
		ArmorStand entity = (ArmorStand) hud.getEntity();
		
		entity.setVisible(false);
		entity.setCustomNameVisible(true);
		entity.setGravity(false);
		entity.setCanPickupItems(false);
		entity.setBasePlate(false);
		
		return new AbstractMap.SimpleEntry<NPC, NPC>(npc, hud);
		
	}
	
	@Override
	public void destroy() {
		
		this.destroy = true;
		
		if(task != null)
			task.cancel();
		
		if(textTask != null)
			textTask.cancel();
		
		if(updaterTask != null)
			updaterTask.cancel();
		
		if(topTextLocation != null)
			topTextLocation = null;
		
		if(lobby != null)
			lobby = null;
		
		topNPCs.forEach(entry -> {
			entry.getValue().destroy();
			entry.getKey().destroy();
		});
		
	}

}
