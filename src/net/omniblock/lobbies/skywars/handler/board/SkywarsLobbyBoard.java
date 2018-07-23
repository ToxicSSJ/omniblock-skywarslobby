package net.omniblock.lobbies.skywars.handler.board;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.omniblock.lobbies.api.LobbyUtility;
import net.omniblock.lobbies.api.object.LobbyBoard;
import net.omniblock.lobbies.skywars.handler.base.SkywarsBase;
import net.omniblock.network.handlers.base.bases.type.BankBase;
import net.omniblock.network.library.helpers.Scroller;
import net.omniblock.network.library.helpers.scoreboard.ScoreboardUtil;
import net.omniblock.network.library.utils.TextUtil;

public class SkywarsLobbyBoard implements LobbyBoard {

	protected static String title = TextUtil.format("&9&l     SkyWars     ");
	
	protected static Scroller scroller = new Scroller("&7¡El modo &4Skywars Z&7 está activado!", 24, 5, '&');
	
	protected static int title_round = 0;
	protected static int title_pos = 0;
	
	public void sendPacket(boolean z){
		
		if(title_pos != 34){
			title_pos++;
			title = sbTitle(title_pos);
		} else {
			if(title_round != 490){
				title_round++;
			} else {
				title_pos = 0;
				title_round = 0;
			}
		}
		
		//String scroll_msg = scroller.next();
		
		for(Player player : Bukkit.getOnlinePlayers()){
			
			SkywarsBase.saveAccount(player);
			
			if(z) {
				
				ScoreboardUtil.unrankedSidebarDisplay(
						player, 
						new String[] { 
									   title,
									   //TextUtil.format(scroll_msg),
									   TextUtil.format(" "),
									   TextUtil.format("&7OmniCoins: &b" + BankBase.getMoney(player)),
									   TextUtil.format("&7Nivel: &b" + BankBase.getLevel(player)),
									   TextUtil.format("&7Premios: &b0/0"),
									   TextUtil.format("  "),
									   TextUtil.format("&7Cabezas: &a" + 0),
									   TextUtil.format("&7Servidores: &a" + 0),
									   TextUtil.format("   "),
									   TextUtil.format("&7Promedio: &9&l" + SkywarsBase.getAverage(SkywarsBase.SAVED_ACCOUNTS.get(player).getStats())),
									   TextUtil.format("&7Network Booster:"),
									   TextUtil.format("  "+LobbyUtility.getFixedBoosterStatus("skywarsnetworkbooster")),
									   TextUtil.format("    "),
									   TextUtil.format("&emc.omniblock.net")}, false);
				
				continue;
				
			}
			
			ScoreboardUtil.unrankedSidebarDisplay(
					player, 
					new String[] { 
								   title,
								   TextUtil.format(" "),
								   TextUtil.format("&7OmniCoins: &b" + BankBase.getMoney(player)),
								   TextUtil.format("&7Nivel: &b" + BankBase.getLevel(player)),
								   TextUtil.format("  "),
								   TextUtil.format("&7Cabezas: &a" + 0),
								   TextUtil.format("&7Servidores: &a" + 0),
								   TextUtil.format("   "),
								   TextUtil.format("&7Promedio: &9&l" + SkywarsBase.getAverage(SkywarsBase.SAVED_ACCOUNTS.get(player).getStats())),
								   TextUtil.format("&7Network Booster:"),
								   TextUtil.format("  "+LobbyUtility.getFixedBoosterStatus("skywarsnetworkbooster")),
								   TextUtil.format("    "),
								   TextUtil.format("&emc.omniblock.net")}, false);
			
		}
		
	}
	
	public static String sbTitle(int a){
		 switch(a){
			 case 1:
				 return TextUtil.format("&9&l     SkyWars    ");
			 case 2:
				 return TextUtil.format("&8&l     SkyWars     ");
			 case 3:
			 	 return TextUtil.format("&b&l     SkyWars     ");
			 case 4:
				 return TextUtil.format("&9&l     &b&lSkyWars     ");
			 case 5:
				 return TextUtil.format("&8&l     &9&lS&b&lkyWars     ");	 
			 case 6:
				 return TextUtil.format("&8&l     S&9&lk&b&lyWars     ");
			 case 7:
				 return TextUtil.format("&8&l     Sk&9&ly&b&lWars     ");
			 case 8:
				 return TextUtil.format("&8&l     Sky&9&lW&b&lars     ");
			 case 9:
				 return TextUtil.format("&8&l     SkyW&9&la&b&lrs     ");
			 case 10:
				 return TextUtil.format("&8&l     SkyWa&9&lr&b&ls     ");
			 case 11:
				 return TextUtil.format("&8&l     SkyWar&9&ls&b&l     ");
			 case 12:
				 return TextUtil.format("&8&l     SkyWars     ");
			 case 13:
				 return TextUtil.format("&8&l     SkyWars     ");
			 case 14:
				 return TextUtil.format("&8&l     SkyWars     ");
			 case 15:
				 return TextUtil.format("&8&l     SkyWars     ");
			 case 16:
				 return TextUtil.format("&8&l     SkyWars     ");
			 case 18:
				 return TextUtil.format("&8&l     SkyWars     ");
			 case 19:
				 return TextUtil.format("&8&l     SkyWars     ");
			 case 20:
				 return TextUtil.format("&8&l     SkyWars     ");
			 case 21:
				 return TextUtil.format("&9&l     &8&lSkyWars     ");
			 case 22:
				 return TextUtil.format("&b&l     &9&lS&8&lkyWars     ");
			 case 23:
				 return TextUtil.format("&b&l     S&9&lk&8&lyWars     ");
			 case 24:
				 return TextUtil.format("&b&l     Sk&9&ly&8&lWars     ");
			 case 25:
				 return TextUtil.format("&b&l     Sky&9&lW&8&lars     ");
			 case 26:
				 return TextUtil.format("&b&l     SkyW&9&la&8&lrs     ");
			 case 27:
				 return TextUtil.format("&b&l     SkyWa&9&lr&8&ls     ");
			 case 28:
				 return TextUtil.format("&b&l     SkyWar&9&ls     ");
			 case 29:
				 return TextUtil.format("&b&l     SkyWars     ");
			 case 30:
				 return TextUtil.format("&b&l     SkyWars     ");
			 case 31:
				 return TextUtil.format("&b&l     SkyWars     ");
			 case 32:
				 return TextUtil.format("&b&l     SkyWars     ");
			 case 33:
				 return TextUtil.format("&8&l     SkyWars     ");
			 case 34:
				 return TextUtil.format("&9&l     SkyWars     ");
				 
		 }
		 
		 return TextUtil.format("&9&l     SkyWars     ");
	}

	@Override
	public void sendPacket() {
		return;
	}
	
}
