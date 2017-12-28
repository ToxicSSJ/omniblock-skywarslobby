package net.omniblock.lobbies.skywars.handler.events;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Lists;

import net.omniblock.lobbies.OmniLobbies;
import net.omniblock.lobbies.apps.general.GeneralHandler;
import net.omniblock.lobbies.skywars.handler.base.SkywarsBase;
import net.omniblock.lobbies.skywars.handler.base.SkywarsBase.SelectedItemType;
import net.omniblock.lobbies.skywars.handler.systems.SWKits.KitKind;
import net.omniblock.lobbies.skywars.handler.systems.SWKits.SWKitsType;
import net.omniblock.lobbies.skywars.handler.type.SkywarsLobbyItem;
import net.omniblock.network.handlers.base.bases.type.BankBase;
import net.omniblock.network.library.helpers.ItemBuilder;
import net.omniblock.network.library.helpers.inventory.InventoryBuilder;
import net.omniblock.network.library.helpers.inventory.InventoryBuilder.Action;
import net.omniblock.network.library.utils.InstantFireworkUtil;
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
import net.omniblock.skywars.patch.managers.CageManager.CageKind;
import net.omniblock.skywars.patch.managers.CageManager.CageType;

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
					.lore(TextUtil.format("&7juegos de Omniblock,"))
					.lore(TextUtil.format("&7Manos a la obra!")).build(), 11, new Action() {

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
					.name(TextUtil.format("&8&lPremio Semanal (Posición)"))
					.lore("")
					.lore(TextUtil.format("&8&m-&r &7Mira tu posición en el"))
					.lore(TextUtil.format("&7premio semanal de Skywars."))
					.lore(TextUtil.format("&7Tu posición actual es: &a&l#" + SkywarsBase.getWeekPrizePosition(e.getPlayer()))).build(), 15);
			
			ib.addItem(new ItemBuilder(Material.SKULL_ITEM).amount(1)
					.durability((short) 3)
					.setSkullOwner("Seska_Rotan")
					.name(TextUtil.format("&9Idiomas &c&l(Proximamente)"))
					.lore("")
					.lore(TextUtil.format("&8&m-&r &7Cambia el idioma de todos los"))
					.lore(TextUtil.format("&7textos dirigidos al jugador como tál.")).build(), 31);
			
			ib.addItem(new ItemBuilder(Material.SKULL_ITEM).amount(1)
					.durability((short) 3)
					.setSkullOwner(e.getPlayer().getName())
					.name(TextUtil.format("&aEstadísticas Generales"))
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
				SkywarsLobbyItem.SHOP.getBuilder().getDisplayname())) {
			
			openShop(e.getPlayer());
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
	
	@SuppressWarnings("deprecation")
	private void openShop(Player player){
		
		InventoryBuilder ib = new InventoryBuilder(TextUtil.format("&a&lTienda"), 6 * 9, true);
		
		new BukkitRunnable() {
			
			int current = 0;
			List<CageType> types = Lists.newArrayList();
			
			{
				
				for(CageType type : CageType.values()) {
					
					if(type.getKind() == CageKind.COLOR) {
						
						types.add(type);
						
					}
					
				}
				
			}
			
			@Override
			public void run() {
				
				if(current >= types.size()) { current = 0; }
				if(ib.getBukkitInventory() == null) { cancel(); return; } 
				if(!ib.getBukkitInventory().getViewers().contains(player)) { cancel(); return; }
				
				Material m = types.get(current).getIcon().getType();
				short subid = types.get(current).getIcon().getDurability();
				
				ib.addItem(new ItemBuilder(m)
						.durability(subid)
						.amount(1)
						.name(TextUtil.format("&eJaulas"))
						.lore("")
						.lore(TextUtil.format("&8&m-&r &7Revisa, compra y usa las"))
						.lore(TextUtil.format("&7increíbles jaulas que tenemos"))
						.lore(TextUtil.format("&7preparadas para tí!")).build(), 10, new Action() {

							@Override
							public void click(ClickType click, Player player) {
								
								openCagesShop(player);
								return;
								
							}
							
				});
				
				current++;
				
			}
			
		}.runTaskTimer(OmniLobbies.getInstance(), 0L, 10L);
		
		ib.addItem(new ItemBuilder(Material.BONE).amount(1)
				.name(TextUtil.format("&8Animación de Muerte"))
				.lore("")
				.lore(TextUtil.format("&8&m-&r &7Elige animaciones de"))
				.lore(TextUtil.format("&7muerte que serán reproducidas"))
				.lore(TextUtil.format("&7cada vez que mueras!"))
				.lore("")
				.lore(TextUtil.format("   &6&l(Proximamente) ")).build(), 13);
		
		ib.addItem(new ItemBuilder(Material.BOW).amount(1)
				.name(TextUtil.format("&8Efectos de Arco"))
				.lore("")
				.lore(TextUtil.format("&8&m-&r &7Al lanzar flechas, estas"))
				.lore(TextUtil.format("&7generarán un rastro de"))
				.lore(TextUtil.format("&7partículas en el aire!"))
				.lore("")
				.lore(TextUtil.format("   &6&l(Proximamente) ")).build(), 16);
		
		ib.addItem(new ItemBuilder(Material.getMaterial(439)).amount(1)
				.name(TextUtil.format("&6Kits"))
				.lore("")
				.lore(TextUtil.format("&8&m-&r &7Compra los increíbles Kits"))
				.lore(TextUtil.format("&7que más se adapten a tu"))
				.lore(TextUtil.format("&7estilo de juego!")).build(), 28,
				
				new Action() {

					@Override
					public void click(ClickType click, Player player) {
						
						openKitShop(player);
						
					}
			
		});
		
		ib.addItem(new ItemBuilder(Material.COAL_BLOCK).amount(1)
				.name(TextUtil.format("&c&lPróximamente...")).build(), 31);
		
		ib.addItem(new ItemBuilder(Material.COAL_BLOCK).amount(1)
				.name(TextUtil.format("&c&lPróximamente...")).build(), 34);
		
		ib.open(player);
		
		return;
		
	}
	
	@SuppressWarnings("deprecation")
	private void openCagesShop(Player player) {
		
		InventoryBuilder ib = new InventoryBuilder(TextUtil.format("&a&lTienda"), 4 * 9, true);
		
		new BukkitRunnable() {
			
			int current = 0;
			List<CageType> types = Lists.newArrayList();
			
			{
				
				for(CageType type : CageType.values()) {
					
					if(type.getKind() == CageKind.COLOR) {
						
						types.add(type);
						
					}
					
				}
				
			}
			
			@Override
			public void run() {
				
				if(current >= types.size()) current = 0;
				if(ib.getBukkitInventory() == null) { cancel(); return; } 
				if(!ib.getBukkitInventory().getViewers().contains(player)) { cancel(); return; }
				
				Material m = types.get(current).getIcon().getType();
				short subid = types.get(current).getIcon().getDurability();
				
				ib.addItem(new ItemBuilder(m)
						.durability(subid)
						.amount(1)
						.name(TextUtil.format("&dJaulas de Colores"))
						.lore("")
						.lore(TextUtil.format("&8&m-&r &7Las jaulas mas coloridas"))
						.lore(TextUtil.format("&7de todo OmniNetwork disponibles"))
						.lore(TextUtil.format("&7para cualquier jugador!")).build(), 10, new Action() {

							@Override
							public void click(ClickType click, Player player) {
								
								openCageShop(CageKind.COLOR, player);
								return;
								
							}
							
				});
				
				current++;
				
			}
			
		}.runTaskTimer(OmniLobbies.getInstance(), 0L, 10L);
		
		ib.addItem(new ItemBuilder(Material.REDSTONE).amount(1)
				.name(TextUtil.format("&6Jaulas de Temporadas"))
				.lore("")
				.lore(TextUtil.format("&8&m-&r &7Estas jaulas solo estarán"))
				.lore(TextUtil.format("&7a la venta en temporadas especiales"))
				.lore(TextUtil.format("&7a precios muy distintos y pueden"))
				.lore(TextUtil.format("&7ser adquiridas por cualquier jugador!")).build(), 12, new Action(){

					@Override
					public void click(ClickType click, Player player) {
		
						openCageShop(CageKind.SEASONAL, player);
						return;
		
					}

		});
		
		ib.addItem(new ItemBuilder(Material.getMaterial(385)).amount(1)
				.name(TextUtil.format("&4Jaulas Customizadas"))
				.lore("")
				.lore(TextUtil.format("&8&m-&r &7Las jaulas customizadas"))
				.lore(TextUtil.format("&7son desarrolladas para ocasiones"))
				.lore(TextUtil.format("&7especieles y solo pueden ser"))
				.lore(TextUtil.format("&7ganadas por medio de eventos o"))
				.lore(TextUtil.format("&7el premio semanal!")).build(), 14, new Action(){

					@Override
					public void click(ClickType click, Player player) {
				
						openCageShop(CageKind.CUSTOM, player);
						return;
				
					}
	
		});
		
		ib.addItem(new ItemBuilder(Material.DIAMOND).amount(1)
				.name(TextUtil.format("&eJaulas VIP"))
				.lore("")
				.lore(TextUtil.format("&8&m-&r &7¡Las jaulas vips son jaulas"))
				.lore(TextUtil.format("&7unicamente accesibles por los jugadores"))
				.lore(TextUtil.format("&7con un rango &6&lVIP &7y poseen"))
				.lore(TextUtil.format("&7animaciones unicas y muy visuales!")).build(), 16, new Action(){

					@Override
					public void click(ClickType click, Player player) {
						
						openCageShop(CageKind.VIP, player);
						return;
						
					}
			
		});
		
		ib.addItem(new ItemBuilder(Material.ARROW).amount(1)
				.name(TextUtil.format("&7Volver")).build(), 31, new Action(){

					@Override
					public void click(ClickType click, Player player) {
						
						openShop(player);
						return;
						
					}
			
		});
		
		ib.open(player);
		
		return;
		
	}
	
	private void openCageShop(CageKind ck, Player player) {
		
		InventoryBuilder ib = new InventoryBuilder(TextUtil.format(ck.getInventoryName()), 6 * 9, true);
		
		int CURRENT_SLOT = 0;
		int MAX_SLOT = (6 * 9) - 1;
		
		for(CageType ct : CageType.values()){
			
			if(ct.getKind() != ck) continue;
			if(CURRENT_SLOT == MAX_SLOT) break;
			
			ib.addItem(
					
					ArrayUtils.contains(SkywarsBase.getItems(player).split(";"), ct.getCode()) ?
							
							new ItemBuilder(ct.getIcon().getType())
							.amount(1)
							.durability(ct.getIcon().getDurability())
							.hideAtributes()
							.name(TextUtil.format(ct.getName()))
							.lore("")
							.lore(ct.getLore())
							.lore("")
							.lore(TextUtil.format(" &a¡Jaula Adquirida!"))
							.lore(TextUtil.format(((CageType) SkywarsBase.getSelectedItem(SelectedItemType.CAGE, SkywarsBase.getSelectedItems(player))).getCode().equalsIgnoreCase(ct.getCode()) ?
									"       &a¡Usando!" :
									" &7(Click para usar)"
									)).build() :
								
							new ItemBuilder(Material.STAINED_GLASS)
							.amount(1)
							.durability((short) 7)
							.hideAtributes()
							.name(TextUtil.format(" &c&l&o? "))
							.lore("")
							.lore(ct.getLore())
							.lore("")
							.lore(TextUtil.format(" &aPrecio: &7" + ct.getPrice()))
							.lore(TextUtil.format(" &6Animación: &7" + (ct.hasAnimation() ? "Si" : "No"))).build(),
							
					CURRENT_SLOT, new Action(){

									boolean inuse = (((CageType) SkywarsBase.getSelectedItem(SelectedItemType.CAGE, SkywarsBase.getSelectedItems(player))).getCode().equalsIgnoreCase(ct.getCode())) ? true : false;
									
									boolean hasmoney = BankBase.getMoney(player) >= ct.getPrice();
									boolean hascage = ArrayUtils.contains(SkywarsBase.getItems(player).split(";"), ct.getCode());
								
									@Override
									public void click(ClickType click, Player player) {
										
										if(inuse) return;
										
										if(hascage){
											
											SkywarsBase.setSelectedItems(player, SkywarsBase.setSelectedItem(SelectedItemType.CAGE, SkywarsBase.getSelectedItems(player), ct.getCode()));
											
											player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 2, -1);
											openCageShop(ck, player);
											return;
											
										}
										
										if(hasmoney){
											
											BankBase.setMoney(player, BankBase.getMoney(player) - ct.getPrice());
											
											InstantFireworkUtil.spawn(player.getLocation());
											SkywarsBase.addItem(player, ct.getCode());
											
											player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
											openCageShop(ck, player);
											return;
											
										}
										
										player.closeInventory();
										
										player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, -1);
										player.sendMessage(TextUtil.format("&cNo tienes dinero suficiente para comprar esta jaula."));
										return;
										
									}
								
							});
			
			CURRENT_SLOT++;
			
		}
		
		ib.addItem(new ItemBuilder(Material.ARROW).amount(1)
				.name(TextUtil.format("&7Volver")).build(), 49, new Action(){

					@Override
					public void click(ClickType click, Player player) {
						
						openCagesShop(player);
						return;
						
					}
			
		});
		
		ib.open(player);
		return;
		
	}
	
	
	private void openKitShop(Player player) {
		
		
		InventoryBuilder ib = new InventoryBuilder(TextUtil.format("&a&lTienda"), 4 * 9, true);
		
		
		ib.addItem(new ItemBuilder(Material.ENCHANTED_BOOK).name(TextUtil.format("&aKits Generales"))
				.lore("")
				.lore(TextUtil.format("&8&m-&r &7Compra los kits más divertidos"))
				.lore(TextUtil.format("&7de manera que se adapten a tu"))
				.lore(TextUtil.format("&7estilo de juego y descubre las"))
				.lore(TextUtil.format("&7diferentes mecánicas que te ofrecen,"))
				.lore(TextUtil.format("&7decide estratégicamente!"))
				.build(), 11, new Action() {

					@Override
					public void click(ClickType click, Player player) {
						
						openKitShop(KitKind.GENERAL, player);
						return;
						
					}

		});
		
		
		ib.addItem(new ItemBuilder(Material.EMPTY_MAP).name(TextUtil.format("&6Gratuitos!"))
				.lore("")
				.lore(TextUtil.format("&8&m-&r &7Obtén los increíbles kits Gratuitos"))
				.lore(TextUtil.format("&7con nuestro sistema; &c&lKit Blazer!"))
				.lore(TextUtil.format("&7Disfruta al máximo las oportunidades"))
				.lore(TextUtil.format("&7que te brindan los kits"))
				.lore(TextUtil.format("&7gratuitos en el combate.")).build(), 15);
		
		
		ib.addItem(new ItemBuilder(Material.ARROW).amount(1)
				.name(TextUtil.format("&7Volver")).build(), 31, new Action(){

					@Override
					public void click(ClickType click, Player player) {
						
						openShop(player);
						return;
						
					}

		});
		
		ib.open(player);
		return;
		
	}
	
	private void openKitShop(KitKind kk, Player player) {
		
		InventoryBuilder ib = new InventoryBuilder(TextUtil.format(kk.getInventoryName()), 6 * 9, true);
		
		String colorString = " ";
		
		if(kk == KitKind.FREE) colorString = "&e";
		if(kk == KitKind.GENERAL) colorString = "&6";
		
		int CURRENT_SLOT = 0;
		int MAX_SLOT = (6 * 9) - 1;
		
		for(SWKitsType kt : SWKitsType.values()) {
				
			if(kt.getKind() != kk) continue;
			if(CURRENT_SLOT == MAX_SLOT) break;
			
		ib.addItem(
					
					ArrayUtils.contains(SkywarsBase.getItems(player).split(";"), kt.getCode()) ?
							
							new ItemBuilder(kt.getMaterial())
							.amount(1)
							.durability((short) kt.getData())
							.hideAtributes()
							.name(TextUtil.format(colorString + kt.getName() + " " + kt.getRarityString()))
							.lore("")
							.lore(kt.getLore())
							.lore("")
							.lore("&6Rareza: &7" + kt.getRarity())
							.lore("")
							.lore(TextUtil.format(" &a¡Kit Adquirido!"))
							.lore(TextUtil.format(( (SWKitsType) SkywarsBase.getSelectedItem(SelectedItemType.KIT, SkywarsBase.getSelectedItems(player))).getCode().equalsIgnoreCase(kt.getCode()) ?
									"       &a¡Usando!" :
									" &7(Click para usar)"
									)).build()	:
						
							new ItemBuilder(Material.STAINED_GLASS)
							.amount(1)
							.durability((short) 7)
							.hideAtributes()
							.name(TextUtil.format(" &c&l&o? "))
							.lore("")
							.lore(kt.getLore())
							.lore("")
							.lore(TextUtil.format(" &aPrecio: &7" + kt.getPrice()))
							.lore(TextUtil.format(" &6Rareza: &7" + kt.getRarity())).build(),
							
					CURRENT_SLOT, new Action(){

									boolean inuse = (((SWKitsType) SkywarsBase.getSelectedItem(SelectedItemType.KIT, SkywarsBase.getSelectedItems(player))).getCode().equalsIgnoreCase(kt.getCode())) ? true : false;
									
									boolean hasmoney = BankBase.getMoney(player) >= kt.getPrice();
									boolean haskits = ArrayUtils.contains(SkywarsBase.getItems(player).split(";"), kt.getCode());
								
									@Override
									public void click(ClickType click, Player player) {
										
										if(inuse) return;
										
										if(haskits){
											
											SkywarsBase.setSelectedItems(player, SkywarsBase.setSelectedItem(SelectedItemType.KIT, SkywarsBase.getSelectedItems(player), kt.getCode()));
											
											player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 2, -1);
											openKitShop(kk, player);
											return;
											
										}
										
										if(hasmoney){
											
											BankBase.setMoney(player, BankBase.getMoney(player) - kt.getPrice());
											
											InstantFireworkUtil.spawn(player.getLocation());
											SkywarsBase.addItem(player, kt.getCode());
											
											player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
											openKitShop(kk, player);
											return;
											
										}
										
										player.closeInventory();
										
										player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, -1);
										player.sendMessage(TextUtil.format("&cNo tienes dinero suficiente para comprar este kit"));
										return;
										
									}
								
							});
			
			CURRENT_SLOT++;
							
		}
		
		ib.addItem(new ItemBuilder(Material.ARROW).amount(1)
				.name(TextUtil.format("&7Volver")).build(), 49, new Action(){

					@Override
					public void click(ClickType click, Player player) {
						
						openKitShop(player);
						return;
						
					}
			
		});
		
		ib.open(player);
		return;

	}
	
	private void openAchievements(Player player) {

	}
	
}
