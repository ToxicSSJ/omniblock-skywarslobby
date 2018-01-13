package net.omniblock.lobbies.skywars.handler.systems.kitblazer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

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
import net.omniblock.lobbies.skywars.handler.systems.kitblazer.object.Animation;
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
public class KitBlazerSystem extends Animation implements LobbySystem {

	private Block block;
	
	private Listener listener;

	private NPC blaze;
	private NPC guardianA;
	private NPC guardianB;

	private Block beambasea;
	private Block beambaseb;

	private Location blazeLoc;
	private Location beambaseaLoc;
	private Location beambasebLoc;

	private Hologram hologram;
	private TextLine line;
	
	private boolean destroy = false;

	private BukkitTask coanimtask;
	protected BukkitTask animationTask;
	private BukkitTask task;

	protected SkywarsLobby lobby;

	public String[] textAnimation = new String[] {

			"&9&l>&b&l>> KIT BLAZER <<&9&l<", 
			"&9&l>>&b&l> KIT BLAZER <&9&l<<", 
			"&9&l>>>&b&l KIT BLAZER &9&l<<<",
			"&b&l>>>&9&l KIT BLAZER &b&l<<<", 
			"&9&l>>>&b&l KIT BLAZER &9&l<<<", 
			"&b&l>>>&9&l KIT BLAZER &b&l<<<",
			"&9&l>>>&b&l KIT BLAZER &9&l<<<", 
			"&9&l>>&b&l> KIT BLAZER <&9&l<<", 
			"&9&l>&b&l>> KIT BLAZER <<&9&l<",
			"&b&l>>> KIT BLAZER <<<",

	};

	@Override
	public void setup(Lobby lobby) {

		if (lobby instanceof SkywarsLobby) {

			this.lobby = (SkywarsLobby) lobby;
			KitBlazerHandler.renewSystem(this);
			block = this.lobby.getLastScan().get("MYSTERY_BOX").get(0).getBlock();

			beambasea = block.getLocation().clone().add(-2, 0, 7).getBlock();
			beambaseb = block.getLocation().clone().add(-7, 0, 2).getBlock();

			blaze = CitizensAPI.getNPCRegistry().createNPC(EntityType.BLAZE, TextUtil.format("&b&l>>> KIT BLAZER <<<"));

			guardianA = CitizensAPI.getNPCRegistry().createNPC(EntityType.GUARDIAN, TextUtil.format("&cMaterializador &7(#1)"));
			guardianB = CitizensAPI.getNPCRegistry().createNPC(EntityType.GUARDIAN, TextUtil.format("&cMaterializador &7(#2)"));

			block.setType(Material.ENDER_PORTAL_FRAME);

			beambasea.setType(Material.COAL_BLOCK);
			beambaseb.setType(Material.COAL_BLOCK);

			hologram = HologramsAPI.createHologram(OmniLobbies.getInstance(), block.getLocation().clone().add(0.5, 3.3, 0.5));
			line = hologram.appendTextLine(TextUtil.format(" "));

			spawnNPCs();

		}

	}

	@Override
	public void start() {

		makeIA();
		onClickNPC();

	}

	protected void spawnNPCs() {

		blazeLoc = block.getLocation().clone().add(0.5, 1, 0.5);

		blazeLoc.setYaw((float) 47.3);
		blazeLoc.setPitch((float) 18.2);

		blaze.spawn(blazeLoc);

		beambaseaLoc = beambasea.getLocation().clone().add(0.5, 1.3, 0.5);
		beambaseaLoc.setYaw((float) -165.3);
		beambaseaLoc.setPitch((float) 3.0);

		beambasebLoc = beambaseb.getLocation().clone().add(0.5, 1.3, 0.5);
		beambasebLoc.setYaw((float) -107.4);
		beambasebLoc.setPitch((float) 2.1);

		guardianA.spawn(beambaseaLoc);
		guardianB.spawn(beambasebLoc);

		line.setText(" ");

	}

	@Override
	public void destroy() {

		this.destroy = true;

		if (block != null)
			block.setType(Material.AIR);

		if (beambasea != null)
			beambasea.setType(Material.AIR);

		if (beambaseb != null)
			beambaseb.setType(Material.AIR);

		if (blaze != null)
			if (blaze.isSpawned())
				blaze.destroy();

		if (guardianA != null)
			if (guardianA.isSpawned())
				guardianA.destroy();

		if (guardianB != null)
			if (guardianB.isSpawned())
				guardianB.destroy();

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

				InventoryBuilder ib = new InventoryBuilder(TextUtil.format("&4&l¡Kit Blazer!"), 5 * 9, true);

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

						fill(ib, new ItemBuilder(Material.STAINED_GLASS_PANE).amount(1).name("&8-").durability((short) subid).build(), SlotIntegers.Slots_1);
						fill(ib, new ItemBuilder(Material.STAINED_GLASS_PANE).amount(1).name("&8-").durability((short) subid).build(), SlotIntegers.Slots_2);
						fill(ib, new ItemBuilder(Material.STAINED_GLASS_PANE).amount(1).name("&8-").durability((short) subid).build(), SlotIntegers.Slots_3);
						fill(ib, new ItemBuilder(Material.STAINED_GLASS_PANE).amount(1).name("&8-").durability((short) subid).build(), SlotIntegers.Slots_4);
						fill(ib, new ItemBuilder(Material.STAINED_GLASS_PANE).amount(1).name("&8-").durability((short) subid).build(), SlotIntegers.Slots_5);

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

	public void makeKitAnimation(KitKind kk, Player p) {

		player = p;
		kitKind = kk;
		animationLobby = lobby;
		animationBlock = block;
		
		if (animationRunning()) return;
		
		preparePlayerKit(Animation.ANIMATION_KIT_FREE);
		
		if(playerHasAllKit()) return;
		
		kitType = getKit();
		task.cancel();
		blaze.setName(TextUtil.format("&c&lPREPARANDO KIT"));

		animationTask = new BukkitRunnable() {

			int phase = 1;
			int round = 0;

			@Override
			public void run() {

				round++;

				if (phase == 1) {

					guardianShoot(guardianA.getEntity(), blaze.getEntity());
					guardianShoot(guardianB.getEntity(), blaze.getEntity());

					Item item = block.getWorld().dropItem(block.getLocation(), new ItemBuilder(Material.BLAZE_ROD).amount(1).name(UUID.randomUUID().toString().substring(0, 5)).build());
					bounceEntity(item);

					if (round >= 3) {
						phase = 2;
						round = 0;
					}
				}

				if (round >= 1 && phase == 2) {

					phase = 3;
					round = 0;

					block.getLocation().getWorld().playSound(block.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, 10);

					LineEffect ef = new LineEffect(GeneralHandler.getEffectLibManager());
					ef.visibleRange = 300;
					ef.particle = ParticleEffect.FIREWORKS_SPARK;
					ef.iterations = 3;
					ef.speed = 2;
					ef.setLocation(block.getLocation());
					ef.setTargetLocation(blaze.getEntity().getLocation().clone().add(0, 4, 0));
					ef.start();

					guardianA.despawn();
					guardianB.despawn();
					blaze.getEntity().setVelocity(new Vector(0, 0.5, 0));

					SkywarsBase.addItem(player, kitType.getCode());

				}

				if (round >= 1 && phase == 3) {

					phase = 4;
					round = 0;

					Location l = block.getLocation().clone().add(0, 7, 0);

					l.getBlock().setType(Material.ANVIL);
					block.getLocation().getWorld().playSound(block.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, -1);

					HelixEffect ef = new HelixEffect(GeneralHandler.getEffectLibManager());
					ef.visibleRange = 300;
					ef.particle = ParticleEffect.LAVA;
					ef.iterations = 1;
					ef.speed = 1;
					ef.setLocation(block.getLocation());
					ef.start();
					
					blaze.despawn();
					block.getLocation().getWorld().playSound(block.getLocation(), Sound.BLOCK_ANVIL_USE, 2, -2);

				}

				if (round >= 1 && phase == 4) {

					phase = 5;
					round = 0;
					removeDropItems();
					textAnimation();
				}

				if (round >= 2 && phase == 5) {

					animationTask.cancel();
					animationDeath();
				}
			}
		}.runTaskTimer(OmniLobbies.getInstance(), 0L, 20L);
	}

	public void makeSurpriseBoxAnimation(int surpriseBoxPrice, Player player) {

	}
	
	
	protected void animationDeath() {
		
		Location cacheloc = lobby.getLastScan().get("MYSTERY_BOX").get(0);
		Location location = new Location(cacheloc.getWorld(), cacheloc.getX(),cacheloc.getY() + 10, cacheloc.getZ(), (float) -142.2,(float) -12.9);
		
		new BukkitRunnable() {

			int round = 0;
			int maxRound = textAnimationWithDeath.length - 1;

			@Override
			public void run() {

				if (destroy) {
					this.cancel();
					return;
				}

				line.setText(TextUtil.format(textAnimationWithDeath[round]));
				player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, 2, 2);

				round++;

				if (round == maxRound) {

					cancel();
					spawnNPCs();
					makeIA();

					block.getWorld().strikeLightningEffect(block.getLocation());

					TextEffect effe = new TextEffect(manager);
					effe.setLocation(location);
					effe.text = "¡ KIT BLAZER !";
					effe.particle = ParticleEffect.FLAME;
					effe.color = Color.RED;
					effe.visibleRange = 500;
					effe.autoOrient = true;
					effe.period = 15;
					effe.iterations = 1;
					effe.start();

					animationRunning = false;
					
					return;
				}

			}
		}.runTaskTimer(OmniLobbies.getInstance(), 0L, 5L);
		
	}

	private void fill(InventoryBuilder ib, ItemStack stack, SlotIntegers rowintegers) {
		for (int i : rowintegers.getIntegers()) {
			ib.addItem(stack, i);
		}
	}

}
