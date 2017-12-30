package net.omniblock.lobbies.skywars.handler.systems.kitblazer.type;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.omniblock.lobbies.OmniLobbies;
import net.omniblock.lobbies.skywars.handler.systems.SWKits.KitKind;
import net.omniblock.lobbies.skywars.handler.systems.kitblazer.KitBlazerHandler;
import net.omniblock.network.library.helpers.ItemBuilder;
import net.omniblock.network.library.helpers.inventory.InventoryBuilder;
import net.omniblock.network.library.helpers.inventory.InventoryBuilder.Action;
import net.omniblock.network.library.utils.TextUtil;

public enum BoxInventoryItem {

	GLASS_PANE_BLACK(new ItemBuilder(Material.STAINED_GLASS_PANE).amount(1).durability((short) 15)
			.name(TextUtil.format("&8-")).build(), new Action() {

				@Override
				public void click(ClickType click, Player player) {
					return;
				}

			}),

	ARROW(new ItemBuilder(Material.ARROW).amount(1).name(TextUtil.format("&7Volver")).build(), new Action() {

		@Override
		public void click(ClickType click, Player player) {

			player.closeInventory();

		}
	}),

	KITS_FREE(
			new ItemBuilder(Material.BLAZE_ROD).amount(1).durability((short) 0).name(TextUtil.format("&eGratuitos"))
				.lore("")	
				.lore(TextUtil.format("&8&m-&r &7Consigue cualquiera de"))
				.lore(TextUtil.format("&7estos kits de forma gratuita"))
				.lore(TextUtil.format("&7y descubre las nuevas"))
				.lore(TextUtil.format("&7formas de pvp."))
				.build(),

			new Action() {

				@Override
				public void click(ClickType click, Player player) {

					final String[] textAnimation = new String[] {

							"&9&l>&b&l>> 1% <<&9&l<", "&9&l>&b&l>> 5% <<&9&l<", "&9&l>>&b&l> 10% <&9&l<<",
							"&9&l>>&b&l> 15% <&9&l<<", "&9&l>>>&b&l 20% &9&l<<<", "&9&l>>>&b&l 25% &9&l<<<",
							"&9&l>>>&b&l 30% &9&l<<<", "&b&l>>>&9&l 35% &b&l<<<", "&b&l>>>&9&l 40% &b&l<<<",
							"&9&l>>>&b&l 45% &9&l<<<", "&9&l>>>&b&l 50% &9&l<<<", "&b&l>>>&9&l 60% &b&l<<<",
							"&b&l>>>&9&l 65% &b&l<<<", "&b&l>>>&9&l 70% &b&l<<<", "&9&l>>>&b&l 75% &9&l<<<",
							"&9&l>>>&b&l 80% &9&l<<<", "&9&l>>&b&l> 90% <&9&l<<", "&9&l>>&b&l> 92% <&9&l<<",
							"&9&l>>&b&l> 95% <&9&l<<", "&9&l>&b&l>> 96% <<&9&l<", "&9&l>&b&l>> 98% <<&9&l<",
							"&9&l>&b&l>> 99% <<&9&l<", "&b&l>>> 100% <<<",

			};

					final int[] slots = new int[] {

							39, 38, 37, 36, 27, 18, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 17, 26, 35, 44, 43, 42, 41

			};

					InventoryBuilder ib = new InventoryBuilder(TextUtil.format("&4&l¡Kit Blazer!"), 5 * 9, true);

					ib.addItem(new ItemBuilder(Material.BARRIER).name(TextUtil.format("&4Cancelar")).build(), 40,
							new Action() {

								@Override
								public void click(ClickType click, Player player) {

									player.closeInventory();
									player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
									player.sendMessage(TextUtil.format("&7Cancelaste la preparación del &cKit Blazer&7, puedes intentarlo de nuevo"));

								}
							});

					fill(ib, BoxInventoryItem.GLASS_PANE_BLACK.getStack(), SlotIntegers.Slots_1);
					fill(ib, BoxInventoryItem.GLASS_PANE_BLACK.getStack(), SlotIntegers.Slots_2);
					fill(ib, BoxInventoryItem.GLASS_PANE_BLACK.getStack(), SlotIntegers.Slots_3);
					fill(ib, BoxInventoryItem.GLASS_PANE_BLACK.getStack(), SlotIntegers.Slots_4);
					fill(ib, BoxInventoryItem.GLASS_PANE_BLACK.getStack(), SlotIntegers.Slots_5);

					new BukkitRunnable() {

						int current = 0;
						int craftKit = 0;
						final int maxCurrent = 15;

						@Override
						public void run() {

							if (current >= maxCurrent) {
								current = 0;
							}
							if (ib.getBukkitInventory() == null) {
								cancel();
								return;
							}
							if (!ib.getBukkitInventory().getViewers().contains(player)) {
								cancel();
								return;
							}

							int subid = current;

							ib.addItem(new ItemBuilder(Material.STAINED_CLAY).amount(1).durability((short) subid)
									.name(TextUtil.format(textAnimation[craftKit])).build(), 22);

							ib.addItem(new ItemBuilder(Material.STAINED_GLASS_PANE).amount(1).name("&8-")
									.durability((short) subid).build(), slots[craftKit]);

							player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, 5, 5);

							if (craftKit >= slots.length - 1) {

								cancel();

								craftKit = 0;

								player.closeInventory();
								player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 5, 5);

								KitBlazerHandler.getSystem().makeKitAnimation(KitKind.FREE, player);

								return;

							}

							craftKit++;
							current++;

						}

					}.runTaskTimer(OmniLobbies.getInstance(), 5L, 5L);

					ib.open(player);

				}
			}),

	KIT_SEASONS(new ItemBuilder(Material.BLAZE_POWDER).amount(1).name(TextUtil.format("&eTemporadas"))
			.lore("")
			.lore(TextUtil.format("&8&m-&r &7Consigue tu kit por"))
			.lore(TextUtil.format("&7temporada, recuerda que"))
			.lore(TextUtil.format("&7solo habrá uno de ese tipo."))
			.lore("")
			.lore(TextUtil.format("&6&l(Proximamente)")).build(),
			
			new Action() {

				@Override
				public void click(ClickType click, Player player) {
					return;

				}
			}),

	SURPRISE_BOX(new ItemBuilder(Material.FIREBALL).amount(1).name(TextUtil.format("&6Caja sorpresa"))
			.lore(TextUtil.format(""))
			.lore(TextUtil.format("&8&m-&r &7Consigue cualquier Kit"))
			.lore(TextUtil.format("&7de forma aleatoria."))
			.lore(TextUtil.format(""))
			.lore(TextUtil.format("&aPrecio: &7500"))
			.lore(TextUtil.format("")).build(),
			
			new Action() {

				@Override
				public void click(ClickType click, Player player) {

					InventoryBuilder ib = new InventoryBuilder(TextUtil.format("&4&lKit Blazer"), 5 * 9, true);

					ib.addItem(new ItemBuilder(Material.BARRIER).name(TextUtil.format("&4Cancelar")).build(), 40,
							new Action() {

								@Override
								public void click(ClickType click, Player player) {

									player.closeInventory();
									player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
									player.sendMessage(TextUtil.format("&7Cancelaste la preparación del &4Kit Blazer&7, ¡puedes intentarlo de nuevo!"));

								}
							});

					fill(ib, BoxInventoryItem.GLASS_PANE_BLACK.getStack(), SlotIntegers.Slots_1);
					fill(ib, BoxInventoryItem.GLASS_PANE_BLACK.getStack(), SlotIntegers.Slots_2);
					fill(ib, BoxInventoryItem.GLASS_PANE_BLACK.getStack(), SlotIntegers.Slots_3);
					fill(ib, BoxInventoryItem.GLASS_PANE_BLACK.getStack(), SlotIntegers.Slots_4);
					fill(ib, BoxInventoryItem.GLASS_PANE_BLACK.getStack(), SlotIntegers.Slots_5);

					ib.addItem(new ItemBuilder(Material.BLAZE_ROD).amount(1).name(TextUtil.format("&6Caja sorpresa"))
							.build(), 22);

					ib.open(player);
					
					player.closeInventory();
					player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
					player.sendMessage(TextUtil.format("&cEsta opción no se encuentra disponible, por el momento."));
				}
			}

	),

	;

	private ItemStack stack;
	private Action action;

	BoxInventoryItem(ItemStack stack, Action action) {
		this.stack = stack;
		this.action = action;
	}

	public ItemStack getStack() {
		return stack;
	}

	public Action getAction() {
		return action;
	}

	public static void fill(InventoryBuilder ib, ItemStack stack, SlotIntegers rowintegers) {
		for (int i : rowintegers.getIntegers()) {
			ib.addItem(stack, i);
		}
	}

}
