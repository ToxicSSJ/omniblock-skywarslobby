package net.omniblock.lobbies.skywars.handler.signs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.omniblock.lobbies.OmniLobbies;
import net.omniblock.lobbies.apps.joinsigns.LobbySigns;
import net.omniblock.lobbies.apps.joinsigns.object.SignActioner;
import net.omniblock.network.library.helpers.Scan;
import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.packet.PlayerSendToGamePacket;
import net.omniblock.packets.network.structure.type.PacketSenderType;
import net.omniblock.packets.object.external.GamePreset;

public class SkywarsLobbySigns implements LobbySigns {

	protected Map<Block, GamePreset> skywarsSigns = new HashMap<Block, GamePreset>();
	
	protected World world;
	protected BukkitTask loop;
	protected SignActioner actioner;
	
	public SkywarsLobbySigns(World world){
		
		this.world = world;
		return;
		
	}
	
	@Override
	public void stop() {
		
		if(loop != null)
			loop.cancel();
		
		if(actioner != null)
			LobbySigns.unregisterActioner(actioner);
		
		skywarsSigns.clear();
		world = null;
		loop = null;
		return;
		
	}
	
	@Override
	public void register() {
		
		this.actioner = LobbySigns.registerActioner(new SignActioner(){
			
			@Override
			public void execute(Player player, String[] lines) {
				
				if(lines.length >= 4){
					
					if(lines[0].equalsIgnoreCase(TextUtil.format("&8&l(&aEntrar&8&l)"))){
						
						/*
						 *  - Categoria de modos de juego
						 *  para un solo jugador.
						 */
						if(lines[3].equalsIgnoreCase(TextUtil.format("&lSOLO"))){
							
							if(lines[1].equalsIgnoreCase(TextUtil.format("&bModo Normal"))){
								
								Packets.STREAMER.streamPacket(new PlayerSendToGamePacket()
										.setPlayername(player.getName())
										.setPreset(GamePreset.SKYWARS_SOLO_NORMAL)
										.useParty(true)
										.build().setReceiver(PacketSenderType.OMNICORE));
								return;
								
							} else if(lines[1].equalsIgnoreCase(TextUtil.format("&cModo Mejorado"))){
										
								Packets.STREAMER.streamPacket(new PlayerSendToGamePacket()
										.setPlayername(player.getName())
										.setPreset(GamePreset.SKYWARS_SOLO_INSANE)
										.useParty(true)
										.build().setReceiver(PacketSenderType.OMNICORE));
								return;
								
							} else if(lines[1].equalsIgnoreCase(TextUtil.format("&4&lModo Z"))){
								
								Packets.STREAMER.streamPacket(new PlayerSendToGamePacket()
										.setPlayername(player.getName())
										.setPreset(GamePreset.SKYWARS_SOLO_Z)
										.useParty(true)
										.build().setReceiver(PacketSenderType.OMNICORE));
								return;
								
							}

						}
						
						/*
						 *  - Categoria de modos de juego
						 *  para dos o más jugadores.
						 */
						if(lines[3].equalsIgnoreCase(TextUtil.format("&lEQUIPOS"))){
							
							if(lines[1].equalsIgnoreCase(TextUtil.format("&bModo Normal"))){
								
								Packets.STREAMER.streamPacket(new PlayerSendToGamePacket()
										.setPlayername(player.getName())
										.setPreset(GamePreset.SKYWARS_TEAM_NORMAL)
										.useParty(true)
										.build().setReceiver(PacketSenderType.OMNICORE));
								return;
								
							} else if(lines[1].equalsIgnoreCase(TextUtil.format("&cModo Mejorado"))){
										
								Packets.STREAMER.streamPacket(new PlayerSendToGamePacket()
										.setPlayername(player.getName())
										.setPreset(GamePreset.SKYWARS_TEAM_INSANE)
										.useParty(true)
										.build().setReceiver(PacketSenderType.OMNICORE));
								return;
								
							} else if(lines[1].equalsIgnoreCase(TextUtil.format("&4&lModo Z"))){
								
								Packets.STREAMER.streamPacket(new PlayerSendToGamePacket()
										.setPlayername(player.getName())
										.setPreset(GamePreset.SKYWARS_TEAM_Z)
										.useParty(true)
										.build().setReceiver(PacketSenderType.OMNICORE));
								return;
								
							}

						}
						
					}
				}
				
			}

			@Override
			public void create(Player player, Sign sign, SignChangeEvent event, String[] lines) {
				
				if(lines[0] != null) {
					if(lines[0].equalsIgnoreCase("skywars")) {
						
						if(lines[1] != null) {
							if(lines[1].equalsIgnoreCase("solo")) {
								
								if(lines[2] != null) {
									if(lines[2].equalsIgnoreCase("normal")) {
										
										event.setLine(0, TextUtil.format("&8&l(&aEntrar&8&l)"));
										event.setLine(1, TextUtil.format("&lSOLO"));
										event.setLine(2, "");
										event.setLine(3, TextUtil.format("&bModo Normal"));
										sign.update();
										
										return;
										
									} else if(lines[2].equalsIgnoreCase("mejorado")) {
										
										event.setLine(0, TextUtil.format("&8&l(&aEntrar&8&l)"));
										event.setLine(1, TextUtil.format("&lSOLO"));
										event.setLine(2, "");
										event.setLine(3, TextUtil.format("&cModo Mejorado"));
										sign.update();
										
										return;
										
									} else if(lines[2].equalsIgnoreCase("z")) {
										
										event.setLine(0, TextUtil.format("&8&l(&aEntrar&8&l)"));
										event.setLine(1, TextUtil.format("&lSOLO"));
										event.setLine(2, "");
										event.setLine(3, TextUtil.format("&4&lModo Z"));
										sign.update();
										
										return;
										
									}
								}
								
							} else if(lines[1].equalsIgnoreCase("team")) {
								
								if(lines[2] != null) {
									if(lines[2].equalsIgnoreCase("normal")) {
										
										event.setLine(0, TextUtil.format("&8&l(&aEntrar&8&l)"));
										event.setLine(1, TextUtil.format("&lEQUIPOS"));
										event.setLine(2, "");
										event.setLine(3, TextUtil.format("&bModo Normal"));
										sign.update();
										
										return;
										
									} else if(lines[2].equalsIgnoreCase("mejorado")) {
										
										event.setLine(0, TextUtil.format("&8&l(&aEntrar&8&l)"));
										event.setLine(1, TextUtil.format("&lEQUIPOS"));
										event.setLine(2, "");
										event.setLine(3, TextUtil.format("&cModo Mejorado"));
										sign.update();
										
										return;
										
									} else if(lines[2].equalsIgnoreCase("z")) {
										
										event.setLine(0, TextUtil.format("&8&l(&aEntrar&8&l)"));
										event.setLine(1, TextUtil.format("&lEQUIPOS"));
										event.setLine(2, "");
										event.setLine(3, TextUtil.format("&4&lModo Z"));
										sign.update();
										
										return;
										
									}
								}
								
							}
						}
						
						return;
						
					}
					
				}
			}
			
		});
		
	}

	@Override
	public void scan() {
		
		@SuppressWarnings("serial")
		List<BlockState> signs_locs = new ArrayList<BlockState>(){
			{
				
				Scan.getTileEntities(world).forEach(state -> {
					
					if(state instanceof Sign) {
						add(state);
					}
					
				});
				
			}	
			
		};
		
		signs_locs.stream().forEach(state -> {
			
			Sign sign = (Sign) state;
			
			if(sign.getLines()[0].equalsIgnoreCase(TextUtil.format("&8&l(&aEntrar&8&l)"))){
				
				if(sign.getLines()[1].equalsIgnoreCase(TextUtil.format("&bModo Normal"))){
					
					System.out.println("equals -> " +sign.getLines()[3].equalsIgnoreCase(TextUtil.format("&lEQUIPOS")));
					System.out.println("line -> " + sign.getLines()[3]);
					System.out.println("text -> " + TextUtil.format("&lEQUIPOS"));
					
					if(sign.getLines()[3].equalsIgnoreCase(TextUtil.format("&lEQUIPOS")))
						skywarsSigns.put(state.getBlock(), GamePreset.SKYWARS_TEAM_NORMAL);
					else
						skywarsSigns.put(state.getBlock(), GamePreset.SKYWARS_SOLO_NORMAL);
					
				} else if(sign.getLines()[1].equalsIgnoreCase(TextUtil.format("&cModo Mejorado"))){
					
					if(sign.getLines()[3].equalsIgnoreCase(TextUtil.format("&lEQUIPOS")))
						skywarsSigns.put(state.getBlock(), GamePreset.SKYWARS_TEAM_INSANE);
					else
						skywarsSigns.put(state.getBlock(), GamePreset.SKYWARS_SOLO_INSANE);
					
				} else if(sign.getLines()[1].equalsIgnoreCase(TextUtil.format("&4&lModo Z"))){
					
					if(sign.getLines()[3].equalsIgnoreCase(TextUtil.format("&lEQUIPOS")))
						skywarsSigns.put(state.getBlock(), GamePreset.SKYWARS_TEAM_Z);
					else
						skywarsSigns.put(state.getBlock(), GamePreset.SKYWARS_SOLO_Z);
					
				}
				
			}
			
		});
		
		return;
		
	}

	@Override
	public void loop() {
		
		this.loop = new BukkitRunnable(){
			
			@Override
			public void run(){
				
				for(Block block : skywarsSigns.keySet()){
					
					if(		block.getType() == Material.SIGN ||
							block.getType() == Material.SIGN_POST ||
							block.getType() == Material.WALL_SIGN){
						
						
						@SuppressWarnings("unused")
						int count = 0;
						
						Sign sign = (Sign) block.getState();
						sign.setLine(2, TextUtil.format(""));
						
					} else {
						
						skywarsSigns.remove(block);
						continue;
						
					}
					
				}
				
			}
			
		}.runTaskTimer(OmniLobbies.getInstance(), 20 * 1, 20 * 1);
		
	}
}
