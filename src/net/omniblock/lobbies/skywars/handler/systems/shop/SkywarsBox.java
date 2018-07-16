package net.omniblock.lobbies.skywars.handler.systems.shop;

import net.omniblock.shop.systems.box.MysteryBox;
import net.omniblock.shop.systems.object.Element;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SkywarsBox extends MysteryBox {


	public SkywarsBox(Location location) {
		super(location);
	}

	@Override
	public void craftBox() {

		this.getBase().setType(Material.ENDER_PORTAL_FRAME);
		this.getBox().setType(Material.CHEST);

	}

	@Override
	public void contentBox() {

	}

	@Override
	public Listener getEvent() {

		return new Listener() {

			@EventHandler
			public void onClick(PlayerInteractEvent e){

				if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.LEFT_CLICK_BLOCK))
					if(e.getClickedBlock().getLocation().equals(getBox().getLocation()))
						if(e.getClickedBlock().getType().equals(getBox().getType()))
							destroyBox();
			}
		};
	}

	@Override
	public void buy(Player player, Element element) {

	}
}
