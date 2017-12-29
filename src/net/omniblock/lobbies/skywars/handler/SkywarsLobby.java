package net.omniblock.lobbies.skywars.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.citizensnpcs.api.CitizensAPI;
import net.omniblock.lobbies.OmniLobbies;
import net.omniblock.lobbies.api.object.LobbyBoard;
import net.omniblock.lobbies.api.object.LobbyScan;
import net.omniblock.lobbies.api.object.LobbySide;
import net.omniblock.lobbies.api.object.LobbySystem;
import net.omniblock.lobbies.api.type.SidedLobby;
import net.omniblock.lobbies.apps.attributes.type.AttributeType;
import net.omniblock.lobbies.apps.clicknpc.LobbyNPC;
import net.omniblock.lobbies.apps.general.type.GeneralLobbyItem;
import net.omniblock.lobbies.apps.joinsigns.LobbySigns;
import net.omniblock.lobbies.skywars.handler.board.SkywarsLobbyBoard;
import net.omniblock.lobbies.skywars.handler.events.SkywarsLobbyListener;
import net.omniblock.lobbies.skywars.handler.systems.cannon.CannonSystem;
import net.omniblock.lobbies.skywars.handler.systems.kitblazer.KitBlazerSystem;
import net.omniblock.lobbies.skywars.handler.systems.swjoin.SkywarsJoinSystem;
import net.omniblock.lobbies.skywars.handler.systems.weekprize.WeekPrizeSystem;
import net.omniblock.lobbies.skywars.handler.type.SkywarsLobbyItem;
import net.omniblock.lobbies.utils.PlayerUtils;
import net.omniblock.network.library.addons.resourceaddon.ResourceHandler;
import net.omniblock.network.library.addons.resourceaddon.type.ResourceType;
import net.omniblock.network.library.helpers.ItemBuilder;
import net.omniblock.network.library.helpers.geometric.Cuboid;
import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.network.systems.InformationCenterPatcher;
import net.omniblock.network.systems.InformationCenterPatcher.Information;
import net.omniblock.network.systems.InformationCenterPatcher.InformationType;

public class SkywarsLobby extends SidedLobby {
	
	public static final LobbySide SKYWARS_NORMAL_SIDE = new LobbySide("Skywars", "SKWLAMAP");
	public static final LobbySide SKYWARS_Z_SIDE = new LobbySide("SkywarsZ", "SKWLBMAP");
	
	protected Map<String, List<Location>> scan;
	protected List<LobbySystem> systems;
	
	protected SkywarsLobby instance;
	protected SkywarsLobbyBoard board;
	
	protected BossBar bar;
	
	public SkywarsLobby() {
		super(new LobbySide[] {
				SKYWARS_NORMAL_SIDE,
				SKYWARS_Z_SIDE
		});
	}
	
	@Override
	public void setup() {
		
		bar = Bukkit.createBossBar(TextUtil.format("&6&lOmniblock Network &8« &7Estamos en fase &c&lBETA &8»"), BarColor.RED, BarStyle.SOLID, BarFlag.DARKEN_SKY);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				if(isZActivated()) {
					
					start(SKYWARS_Z_SIDE);
					return;
					
				}
				
				start(SKYWARS_NORMAL_SIDE);
				
			}
			
		}.runTaskLater(OmniLobbies.getInstance(), 10L);
		
		this.instance = this;
		
		LobbySigns.setup();
		LobbyNPC.setup();
		
		InformationCenterPatcher.registerAutoInformation(
				new Information(InformationType.SKYWARS_Z, "skywarszchecker"),
				new String[] { "none" },
				0,
				20 * 10);
		
	}
	
	@Override
	public void onScanCompleted(LobbySide side, Map<String, List<Location>> scan) {
		
		this.scan = scan;
		return;
		
	}

	@Override
	public void onLobbyUnloaded(LobbySide side) {
		
	}

	@Override
	public Listener onEventsPerform(LobbySide side) {
		
		return new Listener() {
			
			@SuppressWarnings("deprecation")
			@EventHandler
			public void onJoin(PlayerJoinEvent e){
				
				PlayerUtils.forcePlayerGameMode(e.getPlayer(), GameMode.ADVENTURE);
				PlayerUtils.clearPlayerInventory(e.getPlayer());
				PlayerUtils.clearPlayerPotions(e.getPlayer());
				
				if(!bar.getPlayers().contains(e.getPlayer()))
					bar.addPlayer(e.getPlayer());
				
				e.getPlayer().setAllowFlight(false);
				e.getPlayer().setFlying(false);
				
				e.getPlayer().setCanPickupItems(false);
				e.getPlayer().setFireTicks(0);
				
				e.getPlayer().resetMaxHealth();
				e.getPlayer().resetTitle();
				e.getPlayer().resetPlayerWeather();
				e.getPlayer().resetPlayerTime();
				
				e.getPlayer().setExp(0);
				e.getPlayer().setLevel(0);
				
				ResourceHandler.sendResourcePack(e.getPlayer(), side.equals(SKYWARS_NORMAL_SIDE) ? ResourceType.OMNIBLOCK_DEFAULT : ResourceType.SKYWARS_Z_PACK);
				
				giveItems(e.getPlayer());
				teleportPlayer(e.getPlayer());
				
			}
			
		};
	}

	@Override
	public List<BukkitTask> onTasksPerform(LobbySide side) {
		
		if(side == SKYWARS_Z_SIDE) {
			
			return Arrays.asList(
					
					new BukkitRunnable(){
						
						@Override
						public void run() {
							
							Cuboid cuboid = new Cuboid(
									scan.get("MAIN_LOBBY_PORTAL_POS1").get(0),
									scan.get("MAIN_LOBBY_PORTAL_POS2").get(0)
									);
							
							List<Block> blocks = new ArrayList<Block>();
							cuboid.iterator().forEachRemaining(blocks::add);
							
							blocks.stream().forEach(block -> {
								if(block.getType() == Material.AIR) {
									if(block.getLocation().getBlockY() != scan.get("MAIN_LOBBY_PORTAL_POS2").get(0).getBlockY())
										block.setType(Material.STATIONARY_LAVA);
								}
							});
							return;
							
						}
						
					}.runTaskLater(OmniLobbies.getInstance(), 15L),
					
					new BukkitRunnable(){

						int x = 0;
						
						ItemStack lastMat = new ItemStack(Material.REDSTONE_BLOCK, 1, (byte) 15);
						ItemStack[] materials = new ItemStack[] { 
								new ItemBuilder(Material.STAINED_GLASS, 1).durability((short) 15).build(),
								new ItemBuilder(Material.STAINED_GLASS, 1).durability((short) 7).build(),
								new ItemBuilder(Material.STAINED_GLASS, 1).durability((short) 8).build(),
								new ItemBuilder(Material.STAINED_GLASS, 1).durability((short) 4).build(),
								new ItemBuilder(Material.STAINED_GLASS, 1).durability((short) 1).build(),
								new ItemBuilder(Material.STAINED_GLASS, 1).durability((short) 14).build()
								};
						
						Cuboid cuboid;
						
						@SuppressWarnings("deprecation")
						@Override
						public void run() {
							
							if(cuboid == null)
								cuboid = new Cuboid(scan.get("LETTER_Z_POS1").get(0), scan.get("LETTER_Z_POS2").get(0));
							
							List<Block> blocks = new ArrayList<Block>();
							cuboid.iterator().forEachRemaining(blocks::add);
							
							blocks.stream().forEach(block -> {
								if(block.getType() == lastMat.getType()){
									block.setType(materials[x].getType());
									block.setData((byte) materials[x].getDurability());
								}
							}); lastMat = materials[x]; x++; if(x >= materials.length) x = 0;
							return;
							
						}
						
					}.runTaskTimer(OmniLobbies.getInstance(), 15L, 15L),
					
					new BukkitRunnable() {

						@Override
						public void run() {
							
							for(LobbySystem system : getSystems()) {
								
								system.setup(instance);
								system.start();
								continue;
								
							}
							
						}
						
					}.runTaskLater(OmniLobbies.getInstance(), 25L),
					
					new BukkitRunnable() {
						
						@Override
						public void run() {
							
							CitizensAPI.getNPCRegistries().forEach(registry -> {
								
								registry.deregisterAll();
								
							});
							
						}
						
					}.runTaskLater(OmniLobbies.getInstance(), 20L));
			
		}
		
		return Arrays.asList(
				new BukkitRunnable(){
					
					@Override
					public void run() {
						
						Cuboid cuboid = new Cuboid(
								scan.get("MAIN_LOBBY_PORTAL_POS1").get(0),
								scan.get("MAIN_LOBBY_PORTAL_POS2").get(0)
								);
						
						List<Block> blocks = new ArrayList<Block>();
						cuboid.iterator().forEachRemaining(blocks::add);
						
						blocks.stream().forEach(block -> {
							if(block.getType() == Material.AIR) {
								if(block.getLocation().getBlockY() != scan.get("MAIN_LOBBY_PORTAL_POS2").get(0).getBlockY())
									block.setType(Material.STATIONARY_WATER);
							}
						});
						return;
						
					}
					
				}.runTaskLater(OmniLobbies.getInstance(), 15L),
				
				new BukkitRunnable() {

					@Override
					public void run() {
						
						for(LobbySystem system : getSystems()) {
							
							system.setup(instance);
							system.start();
							continue;
							
						}
						
					}
					
				}.runTaskLater(OmniLobbies.getInstance(), 25L),
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						
						CitizensAPI.getNPCRegistries().forEach(registry -> {
							
							registry.deregisterAll();
							
						});
						
					}
					
				}.runTaskLater(OmniLobbies.getInstance(), 20L));
		
	}

	@Override
	public void onStartBeingExecute() {
		
		if(systems != null) {
			
			systems.forEach(system -> system.destroy());
			systems = null;
			
		}
		
		this.setSpawnPoint(new Location(this.getWorld().getBukkitWorld(), 0.5, 50, 0.5, -90, (float) 0 ));
		return;
		
	}

	@Override
	public void onStopBeingExecute() {
		
		for(LobbySystem system : getSystems()) {
			
			system.destroy();
			continue;
			
		}
		
		systems = null;
		return;
		
	}

	@Override
	public LobbyScan getScan() {
		return new LobbyScan() {
			@SuppressWarnings("serial")
			@Override
			public Map<String, Material> getKeys() {
				return new HashMap<String, Material>() {{
					
				    put("BOAT_CANNON", Material.SLIME_BLOCK);
				    put("MAIN_LOBBY_PORTAL_POS1", Material.DIAMOND);
				    put("MAIN_LOBBY_PORTAL_POS2", Material.EMERALD);
				    put("LETTER_Z_POS1", Material.LAPIS_BLOCK);
				    put("LETTER_Z_POS2", Material.WOOL);
				    put("NPC_Z_PIRATE", Material.JACK_O_LANTERN);
				    put("NPC_Z_PIRATE", Material.PUMPKIN);
				    put("WEEK_PRIZE_TEXT", Material.BLAZE_POWDER);
				    put("WEEK_PRIZE_TOP1", Material.GOLD_BLOCK);
				    put("WEEK_PRIZE_TOP2", Material.IRON_BLOCK);
				    put("WEEK_PRIZE_TOP3", Material.STONE);
				    put("MYSTERY_BOX", Material.IRON_INGOT);
				    put("SKYWARS_SOLO_Z", Material.NETHERRACK);
				    put("SKYWARS_TEAM_Z", Material.SOUL_SAND);
				    put("SKYWARS_SOLO_NORMAL", Material.GRASS);
				    put("SKYWARS_TEAM_NORMAL", Material.SNOW_BLOCK);
				    put("SKYWARS_SOLO_INSANE", Material.DIAMOND_ORE);
				    put("SKYWARS_TEAM_INSANE", Material.REDSTONE_ORE);
				    
				}};
			}

			@Override
			public String getScanName() {
				return "SKYWARS_SCAN";
			}
			
		};
	}

	public Map<String, List<Location>> getLastScan() {
		return scan;
	}
	
	@Override
	public String getLobbyName() {
		return "SkywarsLobby";
	}

	public boolean isZActivated() {
		return InformationCenterPatcher.getLastInformation(
				new Information(InformationType.SKYWARS_Z, "skywarszchecker"))
				.equalsIgnoreCase("enabled")
				? true : false;
	}
	
	@Override
	public LobbyBoard getBoard() {
		
		if(board != null)
			return board;
		
		return new SkywarsLobbyBoard();
	}
	
	@Override
	public List<LobbySystem> getSystems() {
		
		if(systems != null)
			return systems;
		
		return Arrays.asList(new WeekPrizeSystem(), new KitBlazerSystem(), new CannonSystem(), new SkywarsJoinSystem());
	}
	
	@Override
	public Listener getEvents() {
		return new SkywarsLobbyListener();
	}

	@Override
	public List<BukkitTask> getTasks() {
		return Arrays.asList(
				
				new BukkitRunnable(){

					@Override
					public void run() {
						
						getBoard().sendPacket(isZActivated());
						return;
						
					}
					
				}.runTaskTimer(OmniLobbies.getInstance(), 0L, 5L),
				
				new BukkitRunnable(){
			
					@Override
					public void run() {
				
						for(World w : Bukkit.getWorlds()){
					
							w.setTime(side == SKYWARS_NORMAL_SIDE ? 1800 : 18000);
					
							w.setStorm(false);
							w.setThundering(false);
					
						}
				
					}
			
				}.runTaskTimer(OmniLobbies.getInstance(), 0L, 10L),
				
				new BukkitRunnable(){
					
					@Override
					public void run() {
						
						for(Player player : Bukkit.getOnlinePlayers())
							if(player.getWorld() == null || !player.getWorld().getName().equalsIgnoreCase(world.getWorldName()))
								teleportPlayer(player);
						
					}
					
				}.runTaskTimer(OmniLobbies.getInstance(), 25L, 25L),
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						
						if(side == SKYWARS_NORMAL_SIDE)
							if(isZActivated()) {
								switchSide(SKYWARS_Z_SIDE);
							}
								
						
						if(side == SKYWARS_Z_SIDE)
							if(!isZActivated()) {
								switchSide(SKYWARS_NORMAL_SIDE);
							}
						
					}
					
				}.runTaskTimer(OmniLobbies.getInstance(), 20 * 8, 20 * 8));
				
	}
	
	@Override
	public void giveItems(Player player) {
		
		for(GeneralLobbyItem item : GeneralLobbyItem.values())
			item.addItem(player);
		
		player.getInventory().setItem(1, SkywarsLobbyItem.PLAYER_PROFILE.getBuilder()
				.setSkullOwner(player.getName())
				.hideAtributes()
				.build());
		
		player.getInventory().setItem(2, SkywarsLobbyItem.LOOT.getBuilder()
				.hideAtributes()
				.build());
		
		player.getInventory().setItem(4, SkywarsLobbyItem.SHOP.getBuilder()
				.hideAtributes()
				.build());
		
		player.getInventory().setItem(8, SkywarsLobbyItem.LOBBY_SELECTOR.getBuilder()
				.hideAtributes()
				.build());
		
		player.updateInventory();
		
	}
	
	@Override
	public List<AttributeType> getAttributes() {
		return Arrays.asList(
				AttributeType.VOID_TELEPORTER,
				AttributeType.GAMEMODE_ADVENTURE,
				AttributeType.NO_DAMAGE,
				AttributeType.NO_HUNGER,
				AttributeType.NOT_COLLIDE,
				AttributeType.RANK_FLY_ENABLED,
				AttributeType.RANK_JOIN_MSG
				);
	}
	
}
