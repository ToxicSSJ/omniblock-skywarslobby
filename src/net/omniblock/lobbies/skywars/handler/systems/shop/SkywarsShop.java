package net.omniblock.lobbies.skywars.handler.systems.shop;

import net.omniblock.lobbies.skywars.handler.base.SkywarsBase;
import net.omniblock.network.handlers.base.bases.type.BankBase;
import net.omniblock.network.handlers.base.bases.type.RankBase;
import net.omniblock.network.library.helpers.ItemBuilder;
import net.omniblock.network.library.helpers.inventory.InventoryBuilder;
import net.omniblock.network.library.utils.InstantFireworkUtil;
import net.omniblock.network.library.utils.TextUtil;
import net.omniblock.network.systems.rank.type.RankType;
import net.omniblock.packets.object.external.Kind;
import net.omniblock.packets.object.external.ServerType;
import net.omniblock.shop.systems.GameShopHandler;
import net.omniblock.shop.systems.object.Element;
import net.omniblock.shop.systems.object.PackageElement;
import net.omniblock.shop.systems.shop.GameShop;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class SkywarsShop extends GameShop {

	public SkywarsShop() {
		super(ServerType.SKYWARS_LOBBY_SERVER);
	}

	@Override
	public ItemStack itemGUI() {
		return new ItemBuilder(Material.GOLD_INGOT).amount(1)
				.name(TextUtil.format("&aTienda &7(Click derecho)"))
				.lore(TextUtil.format(""))
				.lore(TextUtil.format("&8&m-&r &7En la tienda podrás comprar mejoras"))
				.lore(TextUtil.format("&7visuales para todas tus partidas de Skywars!"))
				.lore(TextUtil.format("&7El manejo de la tienda se basa en tus OmniCoins"))
				.lore(TextUtil.format("&7las cuales las ganas jugando partidas."))
				.hideAtributes()
				.build();
	}

	@Override
	public Listener getEvent() {
		return new Listener() {

			@EventHandler
			public void onJoin(PlayerJoinEvent e){ e.getPlayer().getInventory().setItem(4, itemGUI()); }

			@EventHandler
			public void onInteract(PlayerInteractEvent e){

				if(e.getItem() == null) return;
				if(!e.getItem().hasItemMeta()) return;
				if(!e.getItem().getItemMeta().hasDisplayName()) return;

				if(e.getItem().equals(itemGUI()))
					openShopGUI(e.getPlayer());
			}
		};
	}

	@Override
	public void buy(Player player, Element element) {

		boolean inuse = ((net.omniblock.shop.systems.object.Element) SkywarsBase.getSelectedItem(SkywarsBase.SelectedItemType.CAGE, SkywarsBase.getSelectedItems(player))).getCode().equalsIgnoreCase(element.getCode()) ? true : false;
		boolean hasMoney = BankBase.getMoney(player) >= element.getPrice();
		boolean hasElement = ArrayUtils.contains(SkywarsBase.getItems(player).split(";"), element.getCode());

		if(inuse) return;

		if(hasElement){

			SkywarsBase.setSelectedItems(player, SkywarsBase.setSelectedItem(setSelected(element.getUID()), SkywarsBase.getSelectedItems(player), element.getCode()));
			player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 2, -1);
			openShopCageGUI(player, element.getKind(), element.getUID());
			return;
		}

		if(hasMoney){

			BankBase.setMoney(player, BankBase.getMoney(player) - element.getPrice());
			InstantFireworkUtil.spawn(player.getLocation());
			SkywarsBase.addItem(player, element.getCode());

			player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
			openShopCageGUI(player, element.getKind(), element.getUID());
			return;
		}

		player.closeInventory();
		player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 2, 2);
		player.sendMessage(TextUtil.format("&7&lSistema &a:: &cNo tienes suficiente dinero."));

	}

	protected void openShopGUI(Player pl){

		InventoryBuilder ib = new InventoryBuilder(TextUtil.format("&a&lTienda"), 6 * 9, true);

		ib.fill(new ItemBuilder(Material.STAINED_GLASS_PANE).durability((short) 15).name(TextUtil.format("&7-"))
				.build(), InventoryBuilder.RowsIntegers.ROW_1);

		ib.fill(new ItemBuilder(Material.STAINED_GLASS_PANE).durability((short) 15).name(TextUtil.format("&7-"))
				.build(), InventoryBuilder.RowsIntegers.ROW_6);

		ib.addItem(new ItemBuilder(Material.STAINED_GLASS)

				.amount(1)
				.name(TextUtil.format("&eCápsulas"))
				.lore("")
				.lore(TextUtil.format("&8&m -&r &7Usa las diferentes cápsulas"))
				.lore(TextUtil.format("&7que tenemos para tí."))
				.lore(TextUtil.format("&7¡Comienza la partida de un modo"))
				.lore(TextUtil.format("&7único y muy personalizable!"))

				.build(), 19, (clickType, player) -> {

					openShopCageGUI(player);
					return;

				});

		ib.addItem(new ItemBuilder(Material.BOW)

				.amount(1)
				.name(TextUtil.format("&aKits"))
				.lore("")
				.lore(TextUtil.format("&8&m -&r &7Usa los diferentes kits"))
				.lore(TextUtil.format("&7que tenemos para tí."))

				.build(), 22, (clickType, player) -> {


					return;

				});

		ib.addItem(new ItemBuilder(Material.COAL_BLOCK)

				.amount(1)
				.name(TextUtil.format("&c&lPróximamente..."))

				.build(), 25, (clickType, player) -> {


					return;

				});

		ib.addItem(new ItemBuilder(Material.DOUBLE_PLANT)

				.amount(1)
				.name(TextUtil.format("&cSalir"))

				.build(), 49, (clickType, player) -> {

					player.closeInventory();
					return;

				});

		ib.open(pl);
	}

	protected void openShopCageGUI(Player pl){

		InventoryBuilder ib = new InventoryBuilder(TextUtil.format("&a&lTienda"), 4 * 9, true);

		ib.addItem(new ItemBuilder(Material.GLASS)

				.amount(1)
				.name(TextUtil.format("&dColores"))
				.lore("")
				.lore(TextUtil.format("&8&m -&r &7¡Para empezar la partida de"))
				.lore(TextUtil.format("&7la forma más colorida posible!"))
				.lore(TextUtil.format("&7Disponible para cualquier jugador."))

				.build(), 10, (clickType, player) -> {

					openShopCageGUI(player, Kind.DEFAULT, 1L);
					return;

				});

		ib.addItem(new ItemBuilder(Material.REDSTONE)

				.name(TextUtil.format("&6Temporada"))
				.lore("")
				.lore(TextUtil.format("&8&m -&r &7¡Estás cápsulas solo estarán"))
				.lore(TextUtil.format("&7disponible en fechas especiales."))
				.lore(TextUtil.format("&7Date prisa en adquirirlas."))

				.build(), 12, (clickType, player) -> {

					openShopCageGUI(player, Kind.SEASON, 1L);
					return;

				});

		ib.addItem(new ItemBuilder(Material.FIREBALL)

				.name(TextUtil.format("&dRaras"))
				.lore("")
				.lore(TextUtil.format("&8&m -&r &7¡Son únicas en su clase, "))
				.lore(TextUtil.format("&7no encontraras otra igual! "))

				.build(), 14, (clickType, player) -> {

					openShopCageGUI(player, Kind.RARE, 1L);
					return;

				});

		ib.addItem(new ItemBuilder(Material.DIAMOND)

				.name(TextUtil.format("&dRango"))
				.lore("")
				.lore(TextUtil.format("&8&m -&r &7únicamente accesibles para los"))
				.lore(TextUtil.format("&7jugadores con rango Golem y Titán."))
				.lore(TextUtil.format("&7Estas cápsulas cambian de forma"))
				.lore(TextUtil.format("&7al posser animaciones únicas."))
				.lore(TextUtil.format("&7Teniendo una variante diferente"))
				.lore(TextUtil.format("&7de la cápsula jugando solo o en equipo."))

				.build(), 16, (clickType, player) -> {

					openShopCageGUI(player, Kind.VIP, 1L);
					return;

				});

		ib.addItem(new ItemBuilder(Material.DOUBLE_PLANT)

				.amount(1)
				.name(TextUtil.format("&cVolver"))

				.build(), 31, (clickType, player) -> {

					openShopGUI(player);
					return;

				});

		ib.open(pl);

	}

	protected void openShopCageGUI(Player pl, Kind kind, Long UID){

		InventoryBuilder ib = new InventoryBuilder(TextUtil.format("&a&lTienda"), 6 * 9, true);
		RankType rank = RankBase.getRank(pl);

		if(GameShopHandler.getShop() == null) {

			pl.playSound(pl.getLocation(), Sound.ENTITY_VILLAGER_NO, 2, 2);
			pl.sendMessage(TextUtil.format("&7&lSistema &a:: &cNo hay tiendas disponibles."));
			pl.closeInventory();

			return;
		}

		if(kind.equals(Kind.VIP))
			if(rank.equals(RankType.USER)){

				pl.playSound(pl.getLocation(), Sound.ENTITY_VILLAGER_NO, 2, 2);
				pl.sendMessage(TextUtil.format("&7&lSistema &a:: &cDebes comprar el rango para acceder a la sección &6Vip. "));
				pl.closeInventory();

				return;

			}

		PackageElement packageElement = (PackageElement) GameShopHandler.getShop().getValue();

		int current = 0;
		int maxSlot = (6 * 9) - 1;

		for(Element element : packageElement.getElements()){

			if(element.getKind() != kind) continue;
			if(element.getUID() != UID) continue;
			if(current == maxSlot) break;

			ib.addItem(ArrayUtils.contains(SkywarsBase.getItems(pl).split(";"), element.getCode()) ?

					new ItemBuilder(element.getMaterial())

							.amount(1)
							.name(TextUtil.format("&a" + element.getName()))
							.durability(element.getDurability())
							.lore("")
							.lore(TextUtil.format("&a!Cápsula adquirida!"))
							.lore("")
							.lore(TextUtil.format("&8&m -&r &7Comienza a utilizar este diseño único."))
							.lore(TextUtil.format("&7Se la envidia de todos."))
							.lore("")
							.lore(TextUtil.format( ((String) SkywarsBase.getSelectedItem(SkywarsBase.SelectedItemType.CAGE, SkywarsBase.getSelectedItems(pl))).equalsIgnoreCase(element.getCode()) ?
									"       &a¡Usando!" :
									" &7(Click para usar)"))

							.build() :

					new ItemBuilder(Material.STAINED_GLASS)

							.amount(1)
							.durability((short) 7)
							.hideAtributes()
							.name(TextUtil.format("&a" + element.getName()))
							.lore("")
							.lore(TextUtil.format("&8&m -&r &7Compra ya está increíble"))
							.lore(TextUtil.format("&7cápsulas."))
							.lore("")
							.lore("  &7Precio: &a&l" + element.getPrice() + "$")

							.build(), current, (clickType, player) -> {

								buy(player, element);
								return;

							});

			current++;
		}

		ib.addItem(new ItemBuilder(Material.DOUBLE_PLANT)

				.amount(1)
				.name(TextUtil.format("&cVolver"))

				.build(), 49, (clickType, player) -> {

					openShopCageGUI(player);
					return;

				});

		ib.open(pl);

	}


	protected SkywarsBase.SelectedItemType setSelected(Long UID){

		if(UID.equals(1L))
			return SkywarsBase.SelectedItemType.CAGE;
		if (UID.equals(2L))
			return SkywarsBase.SelectedItemType.KIT;
		return null;
	}
}
