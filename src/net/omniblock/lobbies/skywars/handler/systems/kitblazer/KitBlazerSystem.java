package net.omniblock.lobbies.skywars.handler.systems.kitblazer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.omniblock.lobbies.OmniLobbies;
import net.omniblock.lobbies.api.Lobby;
import net.omniblock.lobbies.api.object.LobbySystem;
import net.omniblock.lobbies.apps.clicknpc.LobbyNPC;
import net.omniblock.lobbies.apps.clicknpc.object.NPCActioner;
import net.omniblock.lobbies.apps.general.GeneralHandler;
import net.omniblock.lobbies.skywars.handler.SkywarsLobby;
import net.omniblock.lobbies.skywars.handler.base.SkywarsBase;
import net.omniblock.lobbies.skywars.handler.systems.SWKits.KitKind;
import net.omniblock.lobbies.skywars.handler.systems.SWKits.SWKitsType;
import net.omniblock.lobbies.skywars.handler.systems.kitblazer.type.BoxInventoryItem;
import net.omniblock.lobbies.skywars.handler.systems.kitblazer.type.SlotIntegers;
import net.omniblock.network.handlers.base.bases.type.BankBase;
import net.omniblock.network.library.helpers.ItemBuilder;
import net.omniblock.network.library.helpers.RandomFirework;
import net.omniblock.network.library.helpers.effectlib.EffectManager;
import net.omniblock.network.library.helpers.effectlib.effect.HelixEffect;
import net.omniblock.network.library.helpers.effectlib.effect.LineEffect;
import net.omniblock.network.library.helpers.effectlib.effect.TextEffect;
import net.omniblock.network.library.helpers.effectlib.util.ParticleEffect;
import net.omniblock.network.library.helpers.inventory.InventoryBuilder;
import net.omniblock.network.library.utils.NumberUtil;
import net.omniblock.network.library.utils.TextUtil;

@SuppressWarnings("unused")
public class KitBlazerSystem implements LobbySystem {

	private Block block;
	private Block elevator;

	private Block beambasea;
	private Block beambaseb;

	private NPC blaze;
	private NPC hud;

	private NPC beama;
	private NPC beamb;

	private Listener listener;

	private BukkitTask coanimtask;
	private BukkitTask task;

	protected SkywarsLobby lobby;

	private boolean use = false, destroy = false;

	public String[] textAnimation = new String[] {

			"&9&l>&b&l>> KIT BLAZER <<&9&l<", "&9&l>>&b&l> KIT BLAZER <&9&l<<", "&9&l>>>&b&l KIT BLAZER &9&l<<<",
			"&b&l>>>&9&l KIT BLAZER &b&l<<<", "&9&l>>>&b&l KIT BLAZER &9&l<<<", "&b&l>>>&9&l KIT BLAZER &b&l<<<",
			"&9&l>>>&b&l KIT BLAZER &9&l<<<", "&9&l>>&b&l> KIT BLAZER <&9&l<<", "&9&l>&b&l>> KIT BLAZER <<&9&l<",
			"&b&l>>> KIT BLAZER <<<",

	};

	public String[] textAnimationWithDeath = new String[] {

			"&b&l 1%", "&b&l 5%", "&b&l 8%", "&b&l 12%", "&b&l 15%", "&b&l 18%", "&b&l 20%", "&b&l 22%", "&b&l 26%",
			"&b&l 30%", "&b&l 34%", "&b&l 38%", "&b&l 41%", "&b&l 45%", "&b&l 49%", "&b&l 54%", "&b&l 57%", "&b&l 65%",
			"&b&l 67%", "&b&l 69%", "&b&l 73%", "&b&l 83%", "&b&l 86%", "&b&l 89%", "&b&l 96%", "&b&l 98%", "&b&l 99%",
			"&4&l 100%" };

	@Override
	public void setup(Lobby lobby) {

		if (lobby instanceof SkywarsLobby) {

			this.lobby = (SkywarsLobby) lobby;
			KitBlazerHandler.renewSystem(this);
			block = this.lobby.getLastScan().get("MYSTERY_BOX").get(0).getBlock();

			elevator = block.getRelative(BlockFace.UP);

			beambasea = block.getLocation().clone().add(-2, 0, 7).getBlock();
			beambaseb = block.getLocation().clone().add(-7, 0, 2).getBlock();

			blaze = CitizensAPI.getNPCRegistry().createNPC(EntityType.BLAZE, TextUtil.format("&b&l>>> KIT BLAZER <<<"));
			hud = CitizensAPI.getNPCRegistry().createNPC(EntityType.ARMOR_STAND, TextUtil.format(" "));

			beama = CitizensAPI.getNPCRegistry().createNPC(EntityType.GUARDIAN,
					TextUtil.format("&cMaterializador &7(#1)"));
			beamb = CitizensAPI.getNPCRegistry().createNPC(EntityType.GUARDIAN,
					TextUtil.format("&cMaterializador &7(#2)"));

			block.setType(Material.ENDER_PORTAL_FRAME);

			beambasea.setType(Material.COAL_BLOCK);
			beambaseb.setType(Material.COAL_BLOCK);

			hud.spawn(block.getLocation().clone().add(0.5, 1.3, 0.5));

			blaze.spawn(block.getLocation().clone().add(0.5, 1, 0.5));

			beama.spawn(beambasea.getLocation().clone().add(0.5, 1.3, 0.5));
			beamb.spawn(beambaseb.getLocation().clone().add(0.5, 1.3, 0.5));

			ArmorStand stand = (ArmorStand) hud.getEntity();
			stand.setCustomNameVisible(true);
			stand.setVisible(false);
			stand.setCanPickupItems(false);
			stand.setGravity(false);
			stand.setBasePlate(false);

		}

	}

	@Override
	public void start() {

		makeIA();
		onClickNPC();

	}

	@Override
	public void destroy() {

		this.destroy = true;

		if (block != null)
			block.setType(Material.AIR);

		if (elevator != null)
			elevator.setType(Material.AIR);

		if (beambasea != null)
			beambasea.setType(Material.AIR);

		if (beambaseb != null)
			beambaseb.setType(Material.AIR);

		if (blaze != null)
			if (blaze.isSpawned())
				blaze.destroy();

		if (hud != null)
			if (hud.isSpawned())
				hud.destroy();

		if (beama != null)
			if (beama.isSpawned())
				beama.destroy();

		if (beamb != null)
			if (beamb.isSpawned())
				beamb.destroy();

		if (task != null)
			task.cancel();

	}

	private void makeIA() {

		task = new BukkitRunnable() {

			int round = 0;
			int maxRound = textAnimation.length - 1;

			@Override
			public void run() {

				if (destroy) {
					this.cancel();
					return;
				}

				if (round == maxRound)
					round = 0;

				blaze.setName(TextUtil.format(textAnimation[round]));
				round++;
				return;

			}

		}.runTaskTimer(OmniLobbies.getInstance(), 0L, 20L);

	}

	private void onClickNPC() {

		LobbyNPC.registerActioner(blaze, new NPCActioner() {

			@Override
			public void execute(NPC npc, Player player) {

				InventoryBuilder ib = new InventoryBuilder(TextUtil.format("&4&lKit Blazer!"), 5 * 9, true);

				new BukkitRunnable() {

					int current = 1;
					int max = 15;

					@Override
					public void run() {

						if (destroy) {
							this.cancel();
							return;
						}

						if (current >= max) {
							current = 1;
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

						fill(ib, new ItemBuilder(Material.STAINED_GLASS_PANE).amount(1).name("&8-")
								.durability((short) subid).build(), SlotIntegers.Slots_1);
						fill(ib, new ItemBuilder(Material.STAINED_GLASS_PANE).amount(1).name("&8-")
								.durability((short) subid).build(), SlotIntegers.Slots_2);
						fill(ib, new ItemBuilder(Material.STAINED_GLASS_PANE).amount(1).name("&8-")
								.durability((short) subid).build(), SlotIntegers.Slots_3);
						fill(ib, new ItemBuilder(Material.STAINED_GLASS_PANE).amount(1).name("&8-")
								.durability((short) subid).build(), SlotIntegers.Slots_4);
						fill(ib, new ItemBuilder(Material.STAINED_GLASS_PANE).amount(1).name("&8-")
								.durability((short) subid).build(), SlotIntegers.Slots_5);

						current++;

					}

				}.runTaskTimer(OmniLobbies.getInstance(), 0L, 10L);

				ib.addItem(BoxInventoryItem.KITS_FREE.getStack(), 20, BoxInventoryItem.KITS_FREE.getAction());

				ib.addItem(BoxInventoryItem.SURPRISE_BOX.getStack(), 22, BoxInventoryItem.SURPRISE_BOX.getAction());

				ib.addItem(BoxInventoryItem.KIT_SEASONS.getStack(), 24, BoxInventoryItem.KIT_SEASONS.getAction());

				ib.addItem(BoxInventoryItem.ARROW.getStack(), 40, BoxInventoryItem.ARROW.getAction());

				ib.open(player);

			}

		});

	}

	public void makeKitAnimation(KitKind kk, Player player) {

		if (use) {

			player.closeInventory();
			player.sendMessage(TextUtil.format(" &e(!) &4Kit Blazer &7ya esta preparando un kit."));
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, -1);

			System.out.println("ANIMACION STOP");

			return;

		}

		use = true;

		List<SWKitsType> getKits = new ArrayList<SWKitsType>();

		for (SWKitsType kt : SWKitsType.values()) {

			boolean haskits = ArrayUtils.contains(SkywarsBase.getItems(player).split(";"), kt.getCode());

			if (kt.getKind() != kk) continue;
			if (haskits)
			continue;
			if (!haskits) getKits.add(kt);

		}

		if (!player.isOnline())
			return;

		if (getKits.size() <= 0) {
			
			player.sendMessage(TextUtil.format(" &e(!) &7Lo sentimos, ya no quedan Kits disponibles. &a¡Ya los tienes todos!"));
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, -1);
		
			use = false;
		
			return;
		
		}

		task.cancel();

		new BukkitRunnable() {

			int start = 0;

			@Override
			public void run() {

				if (destroy) {
					this.cancel();
					return;
				}

				guardianShoot(beama.getEntity(), blaze.getEntity());
				guardianShoot(beamb.getEntity(), blaze.getEntity());

				blaze.despawn();
				blaze.spawn(block.getLocation().clone().add(0.5, 1, 0.5));
				blaze.setName(TextUtil.format("&c&lPREPARANDO KIT"));

				start++;

				if (start >= 4) {

					cancel();

					block.getLocation().getWorld().playSound(block.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, 10);

					LineEffect ef = new LineEffect(GeneralHandler.getEffectLibManager());
					ef.visibleRange = 300;
					ef.particle = ParticleEffect.FIREWORKS_SPARK;
					ef.iterations = 3;
					ef.speed = 2;
					ef.setLocation(block.getLocation());
					ef.setTargetLocation(blaze.getEntity().getLocation().clone().add(0, 4, 0));
					ef.start();

					beama.despawn();
					beamb.despawn();
					blaze.despawn();

					SWKitsType kit = getKits.get(NumberUtil.getRandomInt(0, getKits.size() - 1));

					SkywarsBase.addItem(player, kit.getCode());

					block.getWorld().dropItemNaturally(
							block.getLocation().clone().add(NumberUtil.getRandomInt(-5, 5), 6,
									NumberUtil.getRandomInt(-5, 5)),
							new ItemBuilder(Material.BLAZE_POWDER).amount(1).build());
					block.getWorld().dropItemNaturally(
							block.getLocation().clone().add(NumberUtil.getRandomInt(-5, 5), 6,
									NumberUtil.getRandomInt(-5, 5)),
							new ItemBuilder(Material.BLAZE_POWDER).amount(1).build());
					block.getWorld().dropItemNaturally(
							block.getLocation().clone().add(NumberUtil.getRandomInt(-5, 5), 6,
									NumberUtil.getRandomInt(-5, 5)),
							new ItemBuilder(Material.BLAZE_POWDER).amount(1).build());
					block.getWorld().dropItemNaturally(
							block.getLocation().clone().add(NumberUtil.getRandomInt(-5, 5), 6,
									NumberUtil.getRandomInt(-5, 5)),
							new ItemBuilder(Material.BLAZE_POWDER).amount(1).build());
					block.getWorld().dropItemNaturally(
							block.getLocation().clone().add(NumberUtil.getRandomInt(-5, 5), 6,
									NumberUtil.getRandomInt(-5, 5)),
							new ItemBuilder(Material.BLAZE_POWDER).amount(1).build());
					block.getWorld().dropItemNaturally(
							block.getLocation().clone().add(NumberUtil.getRandomInt(-5, 5), 6,
									NumberUtil.getRandomInt(-5, 5)),
							new ItemBuilder(Material.BLAZE_ROD).amount(1).build());
					block.getWorld().dropItemNaturally(
							block.getLocation().clone().add(NumberUtil.getRandomInt(-5, 5), 6,
									NumberUtil.getRandomInt(-5, 5)),
							new ItemBuilder(Material.BLAZE_ROD).amount(1).build());
					block.getWorld().dropItemNaturally(
							block.getLocation().clone().add(NumberUtil.getRandomInt(-5, 5), 6,
									NumberUtil.getRandomInt(-5, 5)),
							new ItemBuilder(Material.BLAZE_ROD).amount(1).build());
					block.getWorld().dropItemNaturally(
							block.getLocation().clone().add(NumberUtil.getRandomInt(-5, 5), 6,
									NumberUtil.getRandomInt(-5, 5)),
							new ItemBuilder(Material.BLAZE_ROD).amount(1).build());
					block.getWorld().dropItemNaturally(
							block.getLocation().clone().add(NumberUtil.getRandomInt(-5, 5), 6,
									NumberUtil.getRandomInt(-5, 5)),
							new ItemBuilder(Material.BLAZE_ROD).amount(1).build());

					new BukkitRunnable() {

						Location l = block.getLocation().clone().add(0, 5, 0);

						@Override
						public void run() {

							if (l.getBlock().getType() != Material.ANVIL) {

								l.getBlock().setType(Material.ANVIL);
								block.getLocation().getWorld().playSound(block.getLocation(), Sound.BLOCK_ANVIL_PLACE,
										1, -1);

								new BukkitRunnable() {

									@Override
									public void run() {

										HelixEffect ef = new HelixEffect(GeneralHandler.getEffectLibManager());
										ef.visibleRange = 300;
										ef.particle = ParticleEffect.LAVA;
										ef.iterations = 1;
										ef.speed = 1;
										ef.setLocation(block.getLocation());
										ef.start();

										block.getLocation().getWorld().playSound(block.getLocation(),
												Sound.BLOCK_ANVIL_USE, 1, -1);

										new BukkitRunnable() {

											Location cacheloc = lobby.getLastScan().get("MYSTERY_BOX").get(0);

											Location location = new Location(cacheloc.getWorld(), cacheloc.getX(),
													cacheloc.getY() + 10, cacheloc.getZ(), (float) -142.2,
													(float) -12.9);

											Location seconlocation = new Location(cacheloc.getWorld(), cacheloc.getX(),
													cacheloc.getY() + 7, cacheloc.getZ(), (float) -142.2,
													(float) -12.9);

											EffectManager manager = new EffectManager(OmniLobbies.getInstance());
											TextEffect effect = new TextEffect(manager);

											@Override
											public void run() {

												effect.setLocation(location);

												effect.text = "KIT";
												effect.particle = ParticleEffect.FLAME;
												effect.color = Color.RED;
												effect.visibleRange = 500;
												effect.autoOrient = true;
												effect.period = 15;
												effect.iterations = 1;

												effect.start();

												new BukkitRunnable() {

													TextEffect eff = new TextEffect(manager);

													@Override
													public void run() {

														eff.setLocation(seconlocation);

														eff.particle = ParticleEffect.FIREWORKS_SPARK;
														eff.text = "" + kit.getName() + "!";
														eff.visibleRange = 500;
														effect.autoOrient = true;
														eff.period = 5;
														eff.iterations = 1;
														eff.start();

														RandomFirework.spawnRandomFirework(block.getLocation())
																.detonate();

														player.sendMessage("");
														player.sendMessage(TextUtil.format(
																"&r&b&l&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
														player.sendMessage("");
														player.sendMessage(
																TextUtil.getCenteredMessage("&r&7&l¡Increíble!", true));
														player.sendMessage("");
														player.sendMessage(TextUtil.getCenteredMessage(
																"&r&7&lConseguiste el Kit " + "&e" + kit.getName(),
																true));
														player.sendMessage("");
														player.sendMessage(TextUtil.format(
																"&r&b&l&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
														player.sendMessage("");

														new BukkitRunnable() {

															TextEffect effe = new TextEffect(manager);

															int round = 0;
															int maxRound = textAnimationWithDeath.length - 1;

															@Override
															public void run() {

																if (destroy) {
																	this.cancel();
																	return;
																}

																hud.setName(
																		TextUtil.format(textAnimationWithDeath[round]));
																player.playSound(player.getLocation(),
																		Sound.BLOCK_LAVA_POP, 2, 2);

																round++;

																if (round == maxRound) {

																	cancel();

																	beama.spawn(beambasea.getLocation().clone().add(0.5,
																			1.3, 0.5));
																	beamb.spawn(beambaseb.getLocation().clone().add(0.5,
																			1.3, 0.5));
																	blaze.spawn(block.getLocation().clone().add(0.5, 1,
																			0.5));
																	hud.setName(" ");

																	block.getWorld()
																			.strikeLightningEffect(block.getLocation());

																	block.getLocation().getWorld().playSound(
																			block.getLocation(),
																			Sound.ENTITY_ENDERDRAGON_HURT, 1, -1);

																	effe.setLocation(seconlocation);
																	effe.text = "¡ KIT BLAZER !";
																	effe.particle = ParticleEffect.FLAME;
																	effe.color = Color.RED;
																	effe.visibleRange = 500;
																	effe.autoOrient = true;
																	effe.period = 15;
																	effe.iterations = 1;

																	effe.start();

																	use = false;

																	makeIA();

																	return;
																}

															}
														}.runTaskTimer(OmniLobbies.getInstance(), 0L, 5L);

													}

												}.runTaskLater(OmniLobbies.getInstance(), 10L);
											}

										}.runTaskLater(OmniLobbies.getInstance(), 15L);

									}

								}.runTaskLater(OmniLobbies.getInstance(), 10L);

							}
						}

					}.runTaskLater(OmniLobbies.getInstance(), 20L);
				}
			}

		}.runTaskTimer(OmniLobbies.getInstance(), 0L, 30L);

	}

	public void makeSurpriseBoxAnimation(int surpriseBoxPrice, Player player) {

		if (use) {

			player.closeInventory();
			player.sendMessage(TextUtil.format(" &e(!) &4Kit Blazer &7ya esta preparando un kit."));
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, -1);

			return;

		}

		use = true;

		List<SWKitsType> getAllKits = new ArrayList<SWKitsType>();

		for (SWKitsType kt : SWKitsType.values()) {

			boolean haskits = ArrayUtils.contains(SkywarsBase.getItems(player).split(";"), kt.getCode());

			if (kt == SWKitsType.NONE)
				continue;
			if (haskits)
				continue;
			if (!haskits)
				getAllKits.add(kt);

		}

		if (!player.isOnline())
			return;

		if (getAllKits.size() <= 0) {

			player.sendMessage(
					TextUtil.format(" &e(!) &7Lo sentimos, ya no quedan Kits disponibles. &a¡Ya los tienes todos!"));
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BASS, 1, -1);

			use = false;

			return;

		}

		SWKitsType kit = getAllKits.get(NumberUtil.getRandomInt(0, getAllKits.size() - 1));
		BankBase.setMoney(player, BankBase.getMoney(player) - surpriseBoxPrice);
		SkywarsBase.addItem(player, kit.getCode());

		block.getWorld().dropItemNaturally(
				block.getLocation().clone().add(NumberUtil.getRandomInt(-5, 5), 6, NumberUtil.getRandomInt(-5, 5)),
				new ItemBuilder(Material.BLAZE_POWDER).amount(1).build());
		block.getWorld().dropItemNaturally(
				block.getLocation().clone().add(NumberUtil.getRandomInt(-5, 5), 6, NumberUtil.getRandomInt(-5, 5)),
				new ItemBuilder(Material.BLAZE_POWDER).amount(1).build());
		block.getWorld().dropItemNaturally(
				block.getLocation().clone().add(NumberUtil.getRandomInt(-5, 5), 6, NumberUtil.getRandomInt(-5, 5)),
				new ItemBuilder(Material.BLAZE_POWDER).amount(1).build());
		block.getWorld().dropItemNaturally(
				block.getLocation().clone().add(NumberUtil.getRandomInt(-5, 5), 6, NumberUtil.getRandomInt(-5, 5)),
				new ItemBuilder(Material.BLAZE_ROD).amount(1).build());
		block.getWorld().dropItemNaturally(
				block.getLocation().clone().add(NumberUtil.getRandomInt(-5, 5), 6, NumberUtil.getRandomInt(-5, 5)),
				new ItemBuilder(Material.BLAZE_ROD).amount(1).build());
		block.getWorld().dropItemNaturally(
				block.getLocation().clone().add(NumberUtil.getRandomInt(-5, 5), 6, NumberUtil.getRandomInt(-5, 5)),
				new ItemBuilder(Material.BLAZE_ROD).amount(1).build());
		block.getWorld().dropItemNaturally(
				block.getLocation().clone().add(NumberUtil.getRandomInt(-5, 5), 6, NumberUtil.getRandomInt(-5, 5)),
				new ItemBuilder(kit.getMaterial()).amount(1).build());
		block.getWorld().dropItemNaturally(
				block.getLocation().clone().add(NumberUtil.getRandomInt(-5, 5), 6, NumberUtil.getRandomInt(-5, 5)),
				new ItemBuilder(kit.getMaterial()).amount(1).build());
		block.getWorld().dropItemNaturally(
				block.getLocation().clone().add(NumberUtil.getRandomInt(-5, 5), 6, NumberUtil.getRandomInt(-5, 5)),
				new ItemBuilder(kit.getMaterial()).amount(1).build());

		player.sendMessage("");
		player.sendMessage(TextUtil.format("&r&b&l&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
		player.sendMessage("");
		player.sendMessage(TextUtil.getCenteredMessage("&r&7&l¡Increíble!", true));
		player.sendMessage("");
		player.sendMessage(TextUtil.getCenteredMessage("&r&7&lConseguiste el Kit " + "&e" + kit.getName(), true));
		player.sendMessage("");
		player.sendMessage(TextUtil.format("&r&b&l&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
		player.sendMessage("");

		use = false;

	}

	private void guardianShoot(Entity entity, Entity toshoot) {

		if (toshoot.getType() == EntityType.BLAZE) {

			toshoot.getWorld().playSound(toshoot.getLocation(), Sound.ENTITY_ENDERDRAGON_FLAP, 5, -2);

			Blaze affected = (Blaze) toshoot;
			Guardian guardian = (Guardian) entity;

			LineEffect ef = new LineEffect(GeneralHandler.getEffectLibManager());
			ef.particle = ParticleEffect.REDSTONE;
			ef.color = Color.ORANGE;
			ef.autoOrient = true;
			ef.visibleRange = 300;
			ef.setLocation(guardian.getLocation());
			ef.setTargetLocation(affected.getEyeLocation());
			ef.start();

			affected.setVelocity(entity.getLocation().getDirection().multiply(0.4));
		}

	}

	public static void fill(InventoryBuilder ib, ItemStack stack, SlotIntegers rowintegers) {
		for (int i : rowintegers.getIntegers()) {
			ib.addItem(stack, i);
		}
	}
}
