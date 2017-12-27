package net.omniblock.lobbies.skywars.handler.systems.weekprize;

import java.io.File;
import java.time.LocalTime;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dispenser;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import com.xxmicloxx.NoteBlockAPI.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.omniblock.lobbies.OmniLobbies;
import net.omniblock.lobbies.api.Lobby;
import net.omniblock.lobbies.api.object.LobbySystem;
import net.omniblock.lobbies.apps.general.GeneralHandler;
import net.omniblock.lobbies.skywars.SkywarsLobbyPlugin;
import net.omniblock.lobbies.skywars.handler.SkywarsLobby;
import net.omniblock.lobbies.skywars.handler.systems.weekprize.object.TopPlayer;
import net.omniblock.lobbies.skywars.handler.systems.weekprize.object.TopStand;
import net.omniblock.lobbies.skywars.handler.systems.weekprize.object.TopStand.TopType;
import net.omniblock.lobbies.skywars.handler.systems.weekprize.packets.WeekPrizePackets;
import net.omniblock.lobbies.skywars.handler.systems.weekprize.packets.WeekPrizePackets.WeekPrizeStatus;
import net.omniblock.network.handlers.Handlers;
import net.omniblock.network.handlers.base.bases.type.BankBase;
import net.omniblock.network.handlers.base.sql.make.MakeAdvancedSQLQuery;
import net.omniblock.network.handlers.base.sql.type.TableType;
import net.omniblock.network.handlers.base.sql.util.Resolver;
import net.omniblock.network.handlers.base.sql.util.SQLResultSet;
import net.omniblock.network.library.helpers.ItemBuilder;
import net.omniblock.network.library.helpers.RandomFirework;
import net.omniblock.network.library.helpers.effectlib.EffectManager;
import net.omniblock.network.library.helpers.effectlib.effect.HelixEffect;
import net.omniblock.network.library.helpers.effectlib.effect.LineEffect;
import net.omniblock.network.library.helpers.effectlib.effect.TextEffect;
import net.omniblock.network.library.helpers.effectlib.util.ParticleEffect;
import net.omniblock.network.library.utils.FileRefactorUtil;
import net.omniblock.network.library.utils.NumberUtil;
import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.packets.util.Lists;

@SuppressWarnings("deprecation")
public class WeekPrizeSystem implements LobbySystem, Listener {

	protected static final String TOP_QUERY_SQL = "SELECT * FROM skywars_weekprize ORDER BY p_points DESC LIMIT 3";
	
	protected BukkitTask task;
	protected BukkitTask textTask;
	protected BukkitTask updaterTask;
	
	protected Location topTextLocation;
	protected SkywarsLobby lobby;
	
	protected boolean destroy = false, animation = false;
	
	protected Map<TopType, TopPlayer> topPlayers = new HashMap<TopType, TopPlayer>();
	protected LinkedList<Entry<NPC, TextLine>> topNPCs = new LinkedList<Entry<NPC, TextLine>>();
	
	protected List<TNTPrimed> tntPrimed = new ArrayList<TNTPrimed>();
	protected List<Item> omniCoins = new ArrayList<Item>();
	
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

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPickup(PlayerPickupItemEvent e) {
		
		if(omniCoins.contains(e.getItem())) {
			
			e.setCancelled(true);
			
			e.getPlayer().sendMessage(TextUtil.format(
					e.getItem().getItemStack().getType() == Material.EMERALD ?
					"&b¡Has encontrado una OmniCoin! &a&l+&a1 ⛃" : "&b¡Has encontrado un bloque de OmniCoins! &a&l+&a10 ⛃"));
			e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
			
			BankBase.addMoney(e.getPlayer(), e.getItem().getItemStack().getType() == Material.EMERALD ? 1 : 10);
			BankBase.addExp(e.getPlayer(), 10);
			
			omniCoins.remove(e.getItem());
			e.getItem().remove();
			return;
			
		}
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onExplosion(ExplosionPrimeEvent e) {

		if (e.getEntity() instanceof TNTPrimed) {

			e.setCancelled(true);

			TNTPrimed tnt = (TNTPrimed) e.getEntity();
			
			tnt.getWorld().playSound(tnt.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 15, 10);
			ParticleEffect.CLOUD.display(1, 1, 1, 1, 30, tnt.getLocation().add(.5, 0, .5), 200);

			for(int i = 0; i < 2; i++) {
				
				Item item = tnt.getWorld().dropItem(tnt.getLocation(), new ItemBuilder(Material.EMERALD_BLOCK).amount(1).name(UUID.randomUUID().toString().substring(0, 5)).build());
				bounceEntity(item);
				
				omniCoins.add(item);
				continue;
				
			}
			
		}

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
					
					SQLResultSet query = new MakeAdvancedSQLQuery(TableType.TOP_STATS_WEEKPRIZE_SKYWARS)
							.append(TOP_QUERY_SQL)
							.execute();
					
					int index = 0;
					
					while(query.next()){
						
						index++;
						
						TopPlayer player = new TopPlayer(
								Resolver.getLastNameByNetworkID(query.get("p_id")),
								query.get("p_points"));
						
						topPlayers.put(TopType.parseInt(index), player);
						
					}
					
				} catch (Exception e) { e.printStackTrace(); }
				
			}
			
		}.runTaskTimer(OmniLobbies.getInstance(), 0, 20 * 30);
		
	}
	
	private void displayCountdown(){
		
		File file = new File(SkywarsLobbyPlugin.getInstance().getDataFolder(), "/songs/weekprize_song.nbs");
		
		if (!file.exists()) {
			
			try {

				file.getParentFile().mkdirs();
				FileRefactorUtil.copyFile(SkywarsLobbyPlugin.getInstance().getResource("/songs/weekprize_song.nbs"), file);

			} catch (Exception e) {
				Handlers.LOGGER.sendError("Un error fatal al crear un archivo de texto!");
				e.printStackTrace();
			}
			
		}
		
		textTask = new BukkitRunnable(){
			
			EffectManager manager = new EffectManager(OmniLobbies.getInstance());
			TextEffect effect = new TextEffect(manager);
			
			Location cacheloc = lobby.getLastScan().get("WEEK_PRIZE_TEXT").get(0);
			Location location = new Location(cacheloc.getWorld(), cacheloc.getX(), cacheloc.getY() + 2, cacheloc.getZ(), -88, (float) 4.2);
			
			Song song = NBSDecoder.parse(file);
			SongPlayer player = new RadioSongPlayer(song);
			
			@Override
			public void run(){
				
				if(destroy) {
					this.cancel();
					return;
				}
				
				if(animation)
					return;
				
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

				if(countdown <= 5 && !animation) {
					
					animation = true;
					
					List<Block> dispensers = Lists.newArrayList();
					List<Location> locs = Lists.newArrayList();
					List<Firework> firework = Lists.newArrayList();
					List<ArmorStand> stands = Lists.newArrayList();
					
					Bukkit.getOnlinePlayers().forEach(p -> player.addPlayer(p));
					
					player.setAutoDestroy(true);
					player.setPlaying(true);
					
					new BukkitRunnable() {
						
						int phase = 1;
						int cacheRound = 0;
						
						@Override
						public void run() {
							
							cacheRound++;
							
							if(cacheRound == 1 && phase == 1) {
								
								phase = 2;
								
								if(topNPCs.size() >= 3){
									
									topNPCs.forEach(entry -> {
										
										NPC npc = entry.getKey();
										Hologram hologram = entry.getValue().getParent();
										
										locs.add(npc.getEntity().getLocation());
										
										hologram.delete();
										RandomFirework.spawnRandomFirework(npc.getEntity().getLocation()).detonate();
										
										firework.add(RandomFirework.spawnRandomFirework(npc.getEntity().getLocation()));
										npc.getEntity().setVelocity(new Vector(0, 1, 0));
										
									});
									
								}
								
							}
							
							if(cacheRound == 2 && phase == 2) {
								
								phase = 3;
								
								if(topNPCs.size() >= 3){
									
									for(int i = 0; i < 3; i++) {
										
										NPC npc = topNPCs.get(i).getKey();
										
										npc.getEntity().getLocation().getBlock().setType(Material.ANVIL);
										
										if(firework.get(i) != null)
											if(!firework.get(i).isDead())
												firework.get(i).setPassenger(npc.getEntity()); firework.get(i).setVelocity(new Vector(0, 1, 0));
										
									}
									
								}
								
							}
							
							if(cacheRound == 3 && phase == 3) {
								
								phase = 4;
								
								if(topNPCs.size() >= 3){
									
									for(int i = 0; i < 3; i++) {
										
										NPC npc = topNPCs.get(i).getKey();
										
										if(firework.get(i) != null)
											if(!firework.get(i).isDead())
												firework.get(i).detonate(); RandomFirework.spawnRandomFirework(npc.getEntity().getLocation()).detonate();
										
										npc.despawn();
										
									}
									
								}
								
							}
							
							if(cacheRound == 4 && phase == 4) {
								
								phase = 5;
								
								for(Location l : locs)
									if(l.getBlock().getType() != Material.ANVIL)
										l.getBlock().setType(Material.ANVIL);
								
								new BukkitRunnable() {
									
									int randomCounter = 0;
									int randomFirework = 0;
									
									@Override
									public void run() {
										
										randomCounter++;
										randomFirework++;
										
										if(randomCounter <= 80) {
											
											Location lastloc = null;
											
											for(Location l : locs) {
												
												if(randomFirework % 40 == 0)
													RandomFirework.spawnRandomFirework(l);
												
												lastloc = l;
												
												l.getBlock().getRelative(BlockFace.UP).setType(Material.WOOL);
												l.getBlock().getRelative(BlockFace.UP).setData((byte) NumberUtil.getRandomInt(0, 15));
												
											}	
											
											if(lastloc != null) {
												
												if(randomFirework % 16 == 0)
													lastloc.getWorld().playSound(lastloc, Sound.UI_BUTTON_CLICK, 1, 10);
												
											}
												
											
										} else { this.cancel(); }
										
									}
									
								}.runTaskTimer(OmniLobbies.getInstance(), 0L, 1L);
								
							}
							
							if(cacheRound == 8 && phase == 5) {
								
								phase = 6;
								
								new BukkitRunnable() {
									
									int randomCounter = 0;
									
									@Override
									public void run() {
										
										randomCounter++;
										
										if(randomCounter == 1) {
											
											Location lastloc = null;
											
											for(Location l : locs) {
												
												lastloc = l;
												
												l.getWorld().playSound(l, Sound.BLOCK_DISPENSER_DISPENSE, 5, -2);
												l.getWorld().playSound(l, Sound.BLOCK_LAVA_POP, 5, -2);
												
												l.getBlock().getRelative(BlockFace.UP).setType(Material.DISPENSER);
												l.getBlock().getRelative(BlockFace.UP).setData((byte) 4);
												
												Dispenser dispenser = (Dispenser) l.getBlock().getRelative(BlockFace.UP).getState().getData();
												dispenser.setFacingDirection(BlockFace.WEST);
												
												l.getBlock().getRelative(BlockFace.UP).getState().setData(dispenser);
												l.getBlock().getRelative(BlockFace.UP).getState().update(true);
												
												dispensers.add(l.getBlock().getRelative(BlockFace.UP).getState().getBlock());
												
											}
											
											if(lastloc != null)
												lastloc.getWorld().playSound(lastloc, Sound.BLOCK_ANVIL_USE, 1, -1);
											
										}
										
										if(randomCounter >= 3 && randomCounter <= 12) {
											
											for(Block block : dispensers) {
												
												block.getWorld().playSound(block.getLocation(), Sound.ENTITY_CHICKEN_EGG, 5, 4);
												
												Item item = block.getWorld().dropItem(
														block.getRelative(BlockFace.WEST).getLocation(),
														new ItemBuilder(Material.EMERALD)
															.name(TextUtil.format(UUID.randomUUID().toString().substring(1, 5)))
															.build());
												
												omniCoins.add(item);
												
												moveOmniCoinBullet(item, block.getRelative(BlockFace.WEST).getLocation());
												
											}	
											
										}
										
										if(randomCounter >= 13)
											this.cancel();
										
									}
									
								}.runTaskTimer(OmniLobbies.getInstance(), 5L, 5L);
								
							}
							
							if(cacheRound == 14) {
								
								for(Location l : locs) {
									
									Location secondLoc = l.clone().add(0, 10, 0);
									
									LineEffect ef = new LineEffect(GeneralHandler.getEffectLibManager());
									ef.visibleRange = 300;
									ef.particle = ParticleEffect.FIREWORKS_SPARK;
									ef.iterations = 1;
									ef.speed = 2;
									ef.setLocation(l);
									ef.setTargetLocation(secondLoc);
									ef.start();
									
								}
								
							}
							
							if(cacheRound == 16) {
								
								effect.particle = ParticleEffect.FLAME;
								effect.text = "¡PREMIOS REPARTIDOS!";
								effect.start();
								
								HelixEffect ef = new HelixEffect(GeneralHandler.getEffectLibManager());
								ef.visibleRange = 300;
								ef.particle = ParticleEffect.SPELL_WITCH;
								ef.iterations = 1;
								ef.speed = 3;
								ef.setLocation(location.clone().add(-4, -10, 0));
								ef.start();
								
							}
							
							if(cacheRound == 18) {
								
								for(Block dispenser : dispensers) {
									
									Block blocktnt = dispenser.getRelative(BlockFace.WEST);
									TNTPrimed tnt = (TNTPrimed) blocktnt.getWorld().spawnEntity(blocktnt.getLocation(), EntityType.PRIMED_TNT);
									
									blocktnt.getWorld().playSound(blocktnt.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 5, -5);
									blocktnt.getWorld().playSound(blocktnt.getLocation(), Sound.ENTITY_TNT_PRIMED, 5, -5);
									
									tnt.setFireTicks(20 * 3);
									
								}
								
							}
							
							if(cacheRound == 24) {
								
								for(Location loc : locs)
									loc.getBlock().setType(Material.AIR);
								
								for(Block dispenser : dispensers)
									dispenser.setType(Material.AIR);
								
								for(Location loc : locs)
									stands.add((ArmorStand) effect.getLocation().getWorld().spawnEntity(loc, EntityType.ARMOR_STAND));
								
								for(ArmorStand stand : stands) {
									
									Location newloc = stand.getLocation();
									newloc.setYaw(90);
									newloc.setPitch(1);
									
									stand.teleport(newloc);
									
									newloc.getWorld().playSound(newloc, Sound.ENTITY_HORSE_SADDLE, 10, -5);
									ParticleEffect.DRIP_LAVA.display(1, 1, 1, 1, 10, newloc.clone(), 200);
									
								}
								
							}
							
							if(cacheRound == 26)
								for(ArmorStand stand : stands)
									stand.getEquipment().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).amount(1).color(Color.BLACK).build());
							
							if(cacheRound == 27)
								for(ArmorStand stand : stands)
									stand.getEquipment().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).amount(1).color(Color.BLACK).build());
							
							if(cacheRound == 28)
								for(ArmorStand stand : stands)
									stand.getEquipment().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).amount(1).color(Color.BLACK).build());
							
							if(cacheRound == 29)
								effect.getLocation().getWorld().playSound(effect.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 15, -2);
							
							if(cacheRound == 30) {
								
								ArmorStand stand = stands.get(2);
								Location loc = stand.getLocation();
								
								stand.getEquipment().setHelmet(new ItemBuilder(Material.STAINED_CLAY).durability((short) 9).amount(1).build());
								
								loc.getWorld().playSound(loc, Sound.ITEM_ARMOR_EQUIP_DIAMOND, 10, 10);
								loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_PLING, 10, 10);
								ParticleEffect.LAVA.display(1, 1, 1, 1, 15, stand.getEyeLocation().clone(), 200);
								
							}
							
							if(cacheRound == 31) {
								
								ArmorStand stand = stands.get(1);
								Location loc = stand.getLocation();
								
								stand.getEquipment().setHelmet(new ItemBuilder(Material.STAINED_CLAY).durability((short) 5).amount(1).build());
								
								loc.getWorld().playSound(loc, Sound.ITEM_ARMOR_EQUIP_DIAMOND, 10, 5);
								loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_PLING, 10, 5);
								ParticleEffect.WATER_DROP.display(1, 1, 1, 1, 15, stand.getEyeLocation().clone(), 200);
								
							}
							
							if(cacheRound == 32) {
								
								ArmorStand stand = stands.get(0);
								Location loc = stand.getLocation();
								
								stand.getEquipment().setHelmet(new ItemBuilder(Material.STAINED_CLAY).durability((short) 11).amount(1).build());
								
								loc.getWorld().playSound(loc, Sound.ITEM_ARMOR_EQUIP_DIAMOND, 10, -5);
								loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_PLING, 10, -5);
								ParticleEffect.SPELL_WITCH.display(1, 1, 1, 1, 15, stand.getEyeLocation().clone(), 200);
								
							}
							
							if(cacheRound == 34)
								for(ArmorStand stand : stands)
									RandomFirework.spawnRandomFirework(stand.getLocation());
							
							if(cacheRound == 35 && phase == 6) {
								
								this.cancel();
								
								phase = 7;
								
								for(Block b : dispensers) {
									b.getRelative(BlockFace.DOWN).setType(Material.AIR);
									b.setType(Material.AIR);
									b.getWorld().strikeLightningEffect(b.getLocation());
								}
								
								for(ArmorStand stand : stands) {
									stand.remove();
								}
								
								effect.getLocation().getWorld().playSound(effect.getLocation(), Sound.ENTITY_LIGHTNING_IMPACT, 5, 5);
								effect.getLocation().getWorld().playSound(effect.getLocation(), Sound.BLOCK_ANVIL_BREAK, 5, 5);
								
								animation = false;
								player.destroy();
								
								player = new RadioSongPlayer(song);
								
								topNPCs.clear();
								displayTop();
								
							}
							
						}
						
					}.runTaskTimer(OmniLobbies.getInstance(), 0L, 20L);
					
					
					
				} else { effect.start(); }
				
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

					int topRound = 0;
					int updateRound = 300;
					
					@Override
					public void run() {
						
						if(animation) {
							this.cancel();
							return;
						}
						
						topRound++;
						updateRound++;
						
						if(topRound >= 30) {
							
							topRound = 0;
							
							if(topPlayers.size() >= 3){
								
								for(int i = 0; i < 3; i++) {
									
									TopType top = TopType.parseInt(i + 1);
									TopPlayer player = topPlayers.get(top);
									
									NPC npc = topNPCs.get(i).getKey();
									TextLine line = topNPCs.get(i).getValue();
									
									if(!npc.isSpawned()) 
										return;
									
									line.setText(TextUtil.format("&aTOP &l#" + top.getTop() + " &8- &7" + player.getPoints()));
									
									if(updateRound >= 300 || !npc.getName().equalsIgnoreCase(player.getName())){
										
										if(i == 2)
											updateRound = 0;
										
										npc.setName(player.getName());
										npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, player.getName());
										
									}
									
								}
								
							}
							
						}
						
					}
					
				}.runTaskTimer(OmniLobbies.getInstance(), 5L, 5L);
				
			}
			
		}.runTaskLater(OmniLobbies.getInstance(), 20L);
		
	}
	
	private Entry<NPC, TextLine> spawnNPC(TopStand stand, NPCRegistry registry){
		
		NPC npc = registry.createNPC(EntityType.PLAYER, "Desconocido");
		npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, stand.getTop().getSkinName());
		
		Location location = stand.getLocation();
		
		location = new Location(
				location.getWorld(),
				location.getX(),
				location.getY(),
				location.getZ());
		
		location.setYaw(stand.getTop().getYaw());
		location.setPitch(stand.getTop().getPitch());
		
		npc.spawn(location);
		npc.addTrait(Equipment.class);
		npc.getEntity().setMetadata("NPC", new FixedMetadataValue(OmniLobbies.getInstance(), "NPC"));
		
		Hologram hud = HologramsAPI.createHologram(OmniLobbies.getInstance(), location.clone().add(0, 3.3, 0));
		hud.appendItemLine(new ItemStack(stand.getTop().getMaterial(), 1));
		
		return new AbstractMap.SimpleEntry<NPC, TextLine>(npc, hud.appendTextLine(TextUtil.format("&aTOP &l#" + stand.getTop().getTop() + " &8- &7" + 0)));
		
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
			entry.getValue().getParent().delete();
			entry.getKey().destroy();
		});
		
	}

	public void moveOmniCoinBullet(Item bullet, Location to) {
		
		double multiply = 0.2;
		
		Location loc = bullet.getLocation();
        double x = loc.getX() - to.getX();
        double y = loc.getY() - to.getY();
        double z = loc.getZ() - to.getZ();
        
        Vector v = new Vector(x, y, z).normalize().multiply(-multiply);
    	
    	if(		NumberConversions.isFinite(v.getX()) &&
    			NumberConversions.isFinite(v.getY()) &&
    			NumberConversions.isFinite(v.getZ()))
    		bullet.setVelocity(v);
		
	}
	
	public void bounceEntity(Entity entity) {

	    float x = (float) - 1 + (float)(Math.random() * ((1 - -1) + 1));
	    float y = (float) 0.5;
	    float z = (float) - 0.3 + (float)(Math.random() * ((0.3 - -0.3) + 1));

	    entity.setVelocity(new Vector(x, y, z));
	    
	}
	
}
