package net.omniblock.lobbies.skywars.handler.systems.swjoin.type;

import java.util.AbstractMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.omniblock.lobbies.OmniLobbies;
import net.omniblock.lobbies.apps.clicknpc.LobbyNPC;
import net.omniblock.lobbies.apps.clicknpc.object.NPCActioner;
import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.packet.PlayerSendToGamePacket;
import net.omniblock.packets.network.structure.type.PacketSenderType;
import net.omniblock.packets.object.external.GamePreset;

@SuppressWarnings("deprecation")
public enum SkywarsJoinType {

	SKYWARS_SOLO_NORMAL("&6&lSOLO", "&aModo Normal", "Grass_Block", Material.GRASS, GamePreset.SKYWARS_SOLO_NORMAL, 90, -1),
	SKYWARS_SOLO_INSANE("&6&lSOLO", "&bModo Mejorado", "Grass_Block", Material.DIAMOND_ORE, GamePreset.SKYWARS_SOLO_INSANE, 90, -1),
	SKYWARS_SOLO_Z("&6&lSOLO", "&4Modo Z", "Fire", Material.getMaterial(2256), GamePreset.SKYWARS_SOLO_Z, 90, -1),
	
	SKYWARS_TEAM_NORMAL("&e&lTEAM", "&aModo Normal", "Grass_Block", Material.GRASS, GamePreset.SKYWARS_TEAM_NORMAL, 90, -1),
	SKYWARS_TEAM_INSANE("&e&lTEAM", "&bModo Mejorado", "Grass_Block", Material.DIAMOND_ORE, GamePreset.SKYWARS_TEAM_INSANE, 90, -1),
	SKYWARS_TEAM_Z("&e&lTEAM", "&4Modo Z", "Fire", Material.getMaterial(2256), GamePreset.SKYWARS_TEAM_Z, 90, -1),
	
	;
	
	private String modeKindName;
	private String modeTypeName;
	
	private String modeSkin;
	private Material material;
	private GamePreset preset;
	
	private float yaw, pitch;
	
	SkywarsJoinType(String modeKindName, String modeTypeName, String modeSkin, Material material, GamePreset preset, float yaw, float pitch) {
		
		this.modeKindName = modeKindName;
		this.modeTypeName = modeTypeName;
		
		this.material = material;
		this.modeSkin = modeSkin;
		this.preset = preset;
		
		this.yaw = yaw;
		this.pitch = pitch;
		
	}
	
	public Entry<SkywarsJoinType, Entry<NPC, Hologram>> spawnJoin(Location loc){
		
		NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, modeSkin);
		Hologram hologram = HologramsAPI.createHologram(OmniLobbies.getInstance(), loc.clone().add(0, 3.3, 0));
		Location faceLoc = loc.clone();
		
		faceLoc.setYaw(yaw);
		faceLoc.setPitch(pitch);
		
		hologram.appendItemLine(new ItemStack(material));
		hologram.appendTextLine(TextUtil.format(modeKindName));
		
		npc.spawn(faceLoc);
		npc.setName(TextUtil.format(modeTypeName));
		npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, modeSkin);
		
		LobbyNPC.registerActioner(npc, new NPCActioner() {

			@Override
			public void execute(NPC npc, Player player) {
				
				player.sendMessage(TextUtil.format("&bConectandote a Skywars..."));
				
				Packets.STREAMER.streamPacket(new PlayerSendToGamePacket()
						.setPlayername(player.getName())
						.setPreset(preset)
						.useParty(true)
						.build().setReceiver(PacketSenderType.OMNICORE));
				return;
				
			}
			
		});
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				
				if(!npc.isSpawned()) {
					
					this.cancel();
					return;
					
				}
				
				npc.data().set(NPC.PLAYER_SKIN_UUID_METADATA, modeSkin);
				return;
				
			}
			
		}.runTaskTimer(OmniLobbies.getInstance(), 80L, 20 * 120);
		
		return new AbstractMap.SimpleEntry<SkywarsJoinType, Entry<NPC, Hologram>>(this, new AbstractMap.SimpleEntry<NPC, Hologram>(npc, hologram));
		
	}
	
}
