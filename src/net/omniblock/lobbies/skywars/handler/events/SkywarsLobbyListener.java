package net.omniblock.lobbies.skywars.handler.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerInteractEvent;
import net.omniblock.lobbies.apps.general.GeneralHandler;
import net.omniblock.lobbies.skywars.handler.base.SkywarsBase;
import net.omniblock.lobbies.skywars.handler.type.SkywarsLobbyItem;
import net.omniblock.network.handlers.base.bases.type.BankBase;
import net.omniblock.network.library.helpers.ItemBuilder;
import net.omniblock.network.library.helpers.inventory.InventoryBuilder;
import net.omniblock.network.library.helpers.inventory.InventoryBuilder.Action;
import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.network.systems.account.AccountManager;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.packet.ResposePlayerGameLobbiesPacket;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.data.PacketStructure.DataType;
import net.omniblock.packets.network.structure.packet.RequestPlayerGameLobbyServersPacket;
import net.omniblock.packets.network.structure.type.PacketSenderType;
import net.omniblock.packets.network.tool.object.PacketResponder;
import net.omniblock.packets.object.external.ServerType;

public class SkywarsLobbyListener implements Listener {

	@EventHandler
    public void onBlockIgnite(BlockIgniteEvent event){
		
		if(event.getCause() == IgniteCause.LAVA)
			event.setCancelled(true);
		
    }
	
	@EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
	
		Material material = event.getBlock().getType();
    
		if(material == Material.WATER || material == Material.STATIONARY_WATER
				|| material == Material.LAVA || material == Material.STATIONARY_LAVA) {
    
			event.setCancelled(true);
    
		}
      
    }
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		
		if(e.getItem() == null) return;
		if(!e.getItem().hasItemMeta()) return;
		if(!e.getItem().getItemMeta().hasDisplayName()) return;
		
		if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(
				SkywarsLobbyItem.PLAYER_PROFILE.getBuilder().getDisplayname())) {
			
			InventoryBuilder ib = new InventoryBuilder(TextUtil.format("&9&lEstadisticas"), 6 * 9, false);
			
			ib.addItem(new ItemBuilder(Material.BOOK).amount(1)
					.name(TextUtil.format("&eLogros"))
					.lore("")
					.lore(TextUtil.format("&8&m-&r &7¡Explora todos los logros"))
					.lore(TextUtil.format("&7disponibles de los diferentes"))
					.lore(TextUtil.format("&7juegos de OmniBlock."))
					.lore(TextUtil.format("&7¡Manos a la obra!")).build(), 11, new Action() {

						@Override
						public void click(ClickType click, Player player) {
							
							openAchievements(player);
							return;
							
						}
						
			});
			
			ib.addItem(new ItemBuilder(Material.IRON_SWORD).amount(1)
					.name(TextUtil.format("&bEstadísticas"))
					.lore("")
					.lore(TextUtil.format("&8&m-&r &7Tus marcas y estadísticas"))
					.lore(TextUtil.format("&7más recientes son:"))
					.lore("")
					.lore(TextUtil.format(" &8&m+&r &7Kills: &c" + SkywarsBase.getKills(SkywarsBase.getStats(e.getPlayer()))))
					.lore(TextUtil.format(" &8&m+&r &7Asistencias: &b" + SkywarsBase.getAssistences(SkywarsBase.getStats(e.getPlayer()))))
					.lore(TextUtil.format(" &8&m+&r &7Victorias: &a" + SkywarsBase.getWinnedGames(SkywarsBase.getStats(e.getPlayer()))))
					.lore(TextUtil.format(" &8&m+&r &7Partidas: &2" + SkywarsBase.getPlayedGames(SkywarsBase.getStats(e.getPlayer()))))
					.lore(TextUtil.format(" &8&m+&r &7Promedio: &9&l" + SkywarsBase.getAverage(SkywarsBase.getStats(e.getPlayer())))).build(), 13);
			
			ib.addItem(new ItemBuilder(Material.EMPTY_MAP).amount(1)
					.name(TextUtil.format("&8&lRanking semanal"))
					.lore("")
					.lore(TextUtil.format("&8&m-&r &7Mira tu posición en el"))
					.lore(TextUtil.format("&7premio semanal de Skywars."))
					.lore(TextUtil.format("&7Tu posición actual es: &a&l#" + SkywarsBase.getWeekPrizePosition(e.getPlayer()))).build(), 15);
			
			ib.addItem(new ItemBuilder(Material.SKULL_ITEM).amount(1)
					.durability((short) 3)
					.setSkullOwner("Seska_Rotan")
					.name(TextUtil.format("&9Idiomas &c&l(PRÓXIMAMENTE)"))
					.lore("")
					.lore(TextUtil.format("&8&m-&r &7Cambia el idioma de todos los"))
					.lore(TextUtil.format("&7textos dirigidos al jugador como tál.")).build(), 31);
			
			ib.addItem(new ItemBuilder(Material.SKULL_ITEM).amount(1)
					.durability((short) 3)
					.setSkullOwner(e.getPlayer().getName())
					.name(TextUtil.format("&aEstadísticas generales"))
					.lore("")
					.lore(TextUtil.format("&8IGN: &7" + e.getPlayer().getName()))
					.lore(TextUtil.format("&8OmniCoins: &7" + BankBase.getMoney(e.getPlayer())))
					.lore(TextUtil.format("&8Experiencia: &7" + BankBase.getExp(e.getPlayer())))
					.lore(TextUtil.format("&8Puntos: &7" + BankBase.getPoints(e.getPlayer())))
					.lore("").build(), 33);
			
			ib.addItem(new ItemBuilder(Material.BLAZE_POWDER).amount(1)
					.name(TextUtil.format("&6Configuraciones"))
					.lore("")
					.lore(TextUtil.format("&8&m-&r &7Personaliza la configuración de"))
					.lore(TextUtil.format("&7tu cuenta. Activa y desactiva"))
					.lore(TextUtil.format("&7características de tu cuenta según tu"))
					.lore(TextUtil.format("&7apetencia o necesidad.")).build(), 29, new Action(){

						@Override
						public void click(ClickType click, Player player) {
					
							AccountManager.displayAccountGUI(player, ib);
							return;
					
						}
				
					});
			
			ib.open(e.getPlayer());
			
			return;
			
		} else if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(
				SkywarsLobbyItem.LOBBY_SELECTOR.getBuilder().getDisplayname())) {
			
			Packets.STREAMER.streamPacketAndRespose(new RequestPlayerGameLobbyServersPacket()
					.setPlayername(e.getPlayer().getName())
					.setServername(Bukkit.getServerName())
					.setServertype(ServerType.SKYWARS_LOBBY_SERVER)
					.build().setReceiver(PacketSenderType.OMNICORE), new PacketResponder<ResposePlayerGameLobbiesPacket>(){

						@Override
						public void readRespose(PacketSocketData<ResposePlayerGameLobbiesPacket> packetsocketdata) {
							
							if(!e.getPlayer().isOnline()) return;
							
							GeneralHandler.displayLobbiesGUI(e.getPlayer(), Bukkit.getServerName(), packetsocketdata.getStructure().get(DataType.STRINGS, "servers"));
							return;
							
						}
				
			});
			return;
			
		} else if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(
				SkywarsLobbyItem.LOOT.getBuilder().getDisplayname())) {
			
			AccountManager.displayLootGUI(e.getPlayer());
			return;
			
		}
		
	}

	private void openAchievements(Player player) {

	}
	
}
