package net.omniblock.lobbies.skywars.handler.type;

import org.bukkit.Material;

import net.omniblock.network.library.helpers.ItemBuilder;
import net.omniblock.network.library.utils.TextUtil;

public enum SkywarsLobbyItem {

	PLAYER_PROFILE(new ItemBuilder(Material.SKULL_ITEM).amount(1).durability((short) 3)
			.name(TextUtil.format("&6Mi Perfil &7(Click derecho)")).lore(TextUtil.format(""))
			.lore(TextUtil.format("&8&m-&r &7Revisa tu perfil y tus configuraciones"))
			.lore(TextUtil.format("&7como usuario. Tambien podrás revisar tus estadisticas"))
			.lore(TextUtil.format("&7en la modalidad de Skywars, El promedio que es el stat"))
			.lore(TextUtil.format("&7que define tu efectividad como jugador solo se"))
			.lore(TextUtil.format("&7habilitará despues de las 50 partidas."))),

	LOOT(new ItemBuilder(Material.FLOWER_POT_ITEM).amount(1).name(TextUtil.format("&4Botín &7(Click derecho)"))
			.lore(TextUtil.format(""))
			.lore(TextUtil.format("&8&m-&r &7Abre tu botín y revisa tus gadgets, particulas"))
			.lore(TextUtil.format("&7rangos, mascotas, boosters o incluso revisa los regalos"))
			.lore(TextUtil.format("&7que te han hecho los demás. Si deseas adquirir más items"))
			.lore(TextUtil.format("&7para tu botín, Visita nuestra tienda: &btienda.omniblock.net"))),

	SHOP(new ItemBuilder(Material.GOLD_INGOT).amount(1).name(TextUtil.format("&aTienda &7(Click derecho)"))
			.lore(TextUtil.format("")).lore(TextUtil.format("&8&m-&r &7En la tienda podrás comprar mejoras"))
			.lore(TextUtil.format("&7visuales para todas tus partidas de Skywars!"))
			.lore(TextUtil.format("&7El manejo de la tienda se basa en tus OmniCoins"))
			.lore(TextUtil.format("&7las cuales las ganas jugando partidas."))),

	SEE_PLAYERS(new ItemBuilder(Material.GLOWSTONE_DUST).amount(1)
			.name(TextUtil.format("&7Ocultar Jugadores &7(Click derecho)")).lore(TextUtil.format(""))
			.lore(TextUtil.format("&8&m-&r &7Remueve de tu visión a todos los"))
			.lore(TextUtil.format("&7jugadores que estan en la lobby!"))),

	@SuppressWarnings("deprecation")
	HIDE_PLAYERS(new ItemBuilder(Material.getMaterial(289)).amount(1)
			.name(TextUtil.format("&8Ver Jugadores &7(Click derecho)")).lore(TextUtil.format(""))
			.lore(TextUtil.format("&8&m-&r &7Muestra a todos los jugadores que están"))
			.lore(TextUtil.format("&7en la lobby!"))),

	LOBBY_SELECTOR(new ItemBuilder(Material.HOPPER_MINECART).amount(1)
			.name(TextUtil.format("&bLobbies de Skywars &7(Click derecho)")).lore(TextUtil.format(""))
			.lore(TextUtil.format("&8&m-&r &7Revisa todas las lobbies disponibles"))
			.lore(TextUtil.format("&7de la modalidad Skywars, Podrás eligir la que"))
			.lore(TextUtil.format("&7desees, Esto generalmente sirve para ir a"))
			.lore(TextUtil.format("&7lobbies más despejadas."))),

	;

	private ItemBuilder builder;

	SkywarsLobbyItem(ItemBuilder builder) {
		this.builder = builder;
	}

	public ItemBuilder getBuilder() {
		return new ItemBuilder(builder.build());
	}

}
