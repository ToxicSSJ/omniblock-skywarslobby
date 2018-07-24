package net.omniblock.lobbies.skywars;

import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.packet.PlayerSendToServerPacket;
import net.omniblock.packets.network.structure.type.PacketSenderType;
import net.omniblock.packets.object.external.ServerType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkywarsExecutor implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(cmd.getName().equalsIgnoreCase("hub") ||
				cmd.getName().equalsIgnoreCase("lobby") ||
				cmd.getName().equalsIgnoreCase("salir") ||
				cmd.getName().equalsIgnoreCase("abandonar")){

			Player player = (Player) sender;

			Packets.STREAMER.streamPacket(new PlayerSendToServerPacket()
					.setPlayername(player.getName())
					.setServertype(ServerType.MAIN_LOBBY_SERVER)
					.setParty(false)
					.build().setReceiver(PacketSenderType.OMNICORE));

			return true;

		}

		return false;
		
	}
}
