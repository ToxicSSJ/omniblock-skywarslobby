package net.omniblock.lobbies.skywars.handler.base;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.omniblock.lobbies.skywars.handler.systems.SWKits.SWKitsType;
import net.omniblock.network.handlers.base.sql.make.MakeAdvancedSQLQuery;
import net.omniblock.network.handlers.base.sql.make.MakeSQLQuery;
import net.omniblock.network.handlers.base.sql.make.MakeSQLUpdate;
import net.omniblock.network.handlers.base.sql.make.MakeSQLUpdate.TableOperation;
import net.omniblock.network.handlers.base.sql.type.TableType;
import net.omniblock.network.handlers.base.sql.util.Resolver;
import net.omniblock.network.handlers.base.sql.util.SQLResultSet;
import net.omniblock.network.handlers.base.sql.util.VariableUtils;
import net.omniblock.skywars.patch.managers.CageManager.CageType;
import net.omniblock.skywars.patch.managers.MapManager;

public class SkywarsBase {

	protected static TableType tabletype = TableType.SKYWARS_DATA;
	
	protected static String map_inserter_sql = "INSERT INTO top_maps_skywars (map_name, votes) SELECT * FROM (SELECT VAR_P_SKYWARS_MAP a, VAR_P_SKYWARS_MAP_INITIAL b) AS tmp WHERE NOT EXISTS (SELECT 1 FROM top_maps_skywars WHERE map_name = VAR_P_SKYWARS_MAP );";
	protected static String top_weekprize_pos_sql = "SELECT id, p_id, p_points, FIND_IN_SET( p_points, ( SELECT GROUP_CONCAT( p_points ORDER BY p_points DESC ) FROM skywars_weekprize ) ) AS rank FROM skywars_weekprize WHERE p_id =  'VAR_P_ID'";
	
	public static Map<Player, AccountInfo> SAVED_ACCOUNTS = new HashMap<Player, AccountInfo>();
	
	public static void addMapVote(boolean good){
		
		if(Bukkit.getPluginManager().isPluginEnabled("Skywars")){
			
			String name = MapManager.CURRENT_MAP != null ? MapManager.CURRENT_MAP.getName() : "Unknow";
			
			try {
				
				if(!isMapRegistered(name)){
					
					MakeSQLUpdate msu = new MakeSQLUpdate(TableType.TOP_MAPS_SKYWARS, TableOperation.INSERT);
					
					msu.rowOperation("map_name", name);
					msu.rowOperation("votes", good ? 1 : 0);
					
					msu.execute();
					return;
					
				}
				
			} catch (SQLException e) { e.printStackTrace(); }
			
			MakeSQLQuery msq = new MakeSQLQuery(TableType.TOP_MAPS_SKYWARS)
					.select("votes")
					.where("map_name", name);
			
			try {
				
				SQLResultSet result = msq.execute();
				
				if(result.next()){
					
					int votes = result.get("votes") != null ? result.get("votes") : 1;
					
					MakeSQLUpdate msu = new MakeSQLUpdate(TableType.TOP_MAPS_SKYWARS, TableOperation.UPDATE);
					
					msu.whereOperation("map_name", name);
					msu.rowOperation("votes", good ? votes++ : votes--);
					
					msu.execute();
					return;
					
				}
				
			} catch (SQLException e) { e.printStackTrace(); }
			
		}
		
	}
	
	public static boolean isMapRegistered(String name) throws SQLException{
		
		MakeSQLQuery msq = new MakeSQLQuery(TableType.TOP_MAPS_SKYWARS)
				.select("map_name")
				.where("map_name", name);
		
		if(!msq.execute().next()) return false;
		return true;
		
	}
	
	public static long getWeekPrizePosition(Player player) {
		
		try {
			
			SQLResultSet query = new MakeAdvancedSQLQuery(TableType.TOP_STATS_WEEKPRIZE_SKYWARS)
					.append(top_weekprize_pos_sql.replaceAll("VAR_P_ID", Resolver.getNetworkID(player)))
					.execute();
			
			if(query.next()) {
				
				return query.get("rank");
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
		
	}
	
	public static void saveAccount(Player player) {
		
		AccountInfo ai = new AccountInfo(getStats(player),
										 getItems(player),
									     getSelectedItems(player),
										 getAverages(player));
		
		SAVED_ACCOUNTS.put(player, ai);
		
	}
	
	public static String getStats(Player player) {
		
		MakeSQLQuery msq = new MakeSQLQuery(TableType.TOP_STATS_SKYWARS)
				.select("kills")
				.select("assists")
				.select("games")
				.select("wins")
				.select("average")
				.where("p_id", Resolver.getNetworkID(player));
		
		try {
			
			SQLResultSet sqr = msq.execute();
			
			if(sqr.next()) {
				
				return StringUtils.join(new Object[] { sqr.get("kills"),
													   sqr.get("assists"),
													   sqr.get("games"),
													   sqr.get("wins"),
													   (double) sqr.get("average") == 0.0 ?
													   "NEW" : sqr.get("average")
													 },
									   ";");
				
			}
			
		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}
		
		return "0;0;0;0;NEW";
		
	}
	
	public static void setStats(Player player, String stats) {
		
		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.TOP_STATS_SKYWARS, TableOperation.UPDATE);
		
		msu.rowOperation("p_stats", stats);
		msu.whereOperation("p_id", Resolver.getNetworkID(player));
		
		try {
			
			msu.execute();
			return;
			
		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}
		
		return;
		
	}
	
	public static int getKills(String stats) {
		
		if(stats.contains(";")) {
			
			try {
				
				String[] data_array = stats.split(";");
				String KILLS_STR = data_array[0];
				
				int kills = Integer.valueOf(KILLS_STR);
				return kills;
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return 0;
	}
	
	public static int getAssistences(String stats) {
		
		if(stats.contains(";")) {
			
			try {
				
				String[] data_array = stats.split(";");
				String ASISTENCES_STR = data_array[1];
				
				int assistences = Integer.valueOf(ASISTENCES_STR);
				return assistences;
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return 0;
	}
	
	public static int getPlayedGames(String stats) {
		
		if(stats.contains(";")) {
			
			try {
				
				String[] data_array = stats.split(";");
				String PLAYED_GAMES_STR = data_array[2];
				
				int played_games = Integer.valueOf(PLAYED_GAMES_STR);
				return played_games;
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return 0;
	}
	
	public static int getWinnedGames(String stats) {
		
		if(stats.contains(";")) {
			
			try {
				
				String[] data_array = stats.split(";");
				String WINNED_GAMES_STR = data_array[3];
				
				int winned_games = Integer.valueOf(WINNED_GAMES_STR);
				return winned_games;
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return 0;
	}
	
	public static String getAverage(String stats) {
		
		if(stats.contains(";")) {
			
			try {
				
				String[] data_array = stats.split(";");
				String AVERAGE_STR = data_array[4];
				
				return AVERAGE_STR;
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		return "NEW";
	}
	
	public static void setAverage(Player player, String average) {
		 
		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.SKYWARS_DATA, TableOperation.UPDATE);
		
		msu.rowOperation("p_last_games_average", average);
		msu.whereOperation("p_id", Resolver.getNetworkID(player));
		
		try {
			
			msu.execute();
			return;
			
		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}
		
		return;
		
	}
	
	public static Object getSelectedItem(SelectedItemType sit, String data) {
		
		if(!data.contains(";")) return "";
		
		String[] data_array = data.split(";");
		
		switch(sit) {
			case CAGE:
				
				String CAGE_CODE = data_array[0];
				
				for(CageType ct : CageType.values()) {
					if(ct.getCode().equalsIgnoreCase(CAGE_CODE)) {
						return ct;
					}
				}
				
				break;
				
			case EXTRA_INFO:
				
				String INFO_CODE = data_array[3];
				return INFO_CODE;
				
			case BOW_EFFECT:
				
				String BOW_CODE = data_array[2];
				return BOW_CODE;
				
			case KIT:
				
				String KIT_CODE = data_array[1];
				
				for(SWKitsType kt : SWKitsType.values()) {
					
					if(kt.getCode().equalsIgnoreCase(KIT_CODE)) {
						return kt;
					}
				}
				
			default:
				break;
				
		}
		
		return null;
		
	}
	
	public static String setSelectedItem(SelectedItemType sit, String data, String value) {
		
		if(!data.contains(";")) return data;
		
		String[] data_array = data.split(";");
		
		switch(sit) {
			case CAGE:
				
				data_array[0] = value;
				return StringUtils.join(data_array, ";");
				
			case EXTRA_INFO:
				
				data_array[3] = value;
				return StringUtils.join(data_array, ";");
				
			case BOW_EFFECT:
				
				data_array[2] = value;
				return StringUtils.join(data_array, ";");
				
			case KIT:
				
				data_array[1] = value;
				return StringUtils.join(data_array, ";");
				
			default:
				break;
				
		}
		
		return data;
		
	}
	
	public static String getSelectedItems(Player player) {
		
		MakeSQLQuery msq = new MakeSQLQuery(TableType.SKYWARS_DATA)
								.select("p_selected")
								.where("p_id", Resolver.getNetworkID(player));
		
		try {
			
			SQLResultSet sqr = msq.execute();
			
			if(sqr.next()) {
				return sqr.get("p_selected");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return VariableUtils.SKYWARS_INITIAL_SELECTED;
	}
	
	public static void setSelectedItems(Player player, String items) {
		
		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.SKYWARS_DATA, TableOperation.UPDATE);
		
		msu.rowOperation("p_selected", items);
		msu.whereOperation("p_id", Resolver.getNetworkID(player));
		
		try {
			
			msu.execute();
			return;
			
		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}
		
		return;
	}
	
	public static String getItems(Player player) {
		
		MakeSQLQuery msq = new MakeSQLQuery(TableType.SKYWARS_DATA)
				.select("p_items")
				.where("p_id", Resolver.getNetworkID(player));
		
		String items = "{}";
		
		try {
			SQLResultSet sqr = msq.execute();
			if(sqr.next()) {
				items = sqr.get("p_items");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return items;
	}
	
	public static void addItem(Player player, String item) {
		
		
		List<String> items = new ArrayList<String>();
		List<String> olditems = Arrays.asList(getItems(player));
		
		for(String k : olditems){
			
			if(k.equalsIgnoreCase(item)) return;
			else items.add(k);
			
		}
		
		items.add(item);
		
		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.SKYWARS_DATA, TableOperation.UPDATE);
		
		msu.rowOperation("p_items", StringUtils.join(items, ";"));
		msu.whereOperation("p_id", Resolver.getNetworkID(player));
		
		try {
			
			msu.execute();
			return;
			
		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}
		
		return;
		
	}
	
	public static double[] getAverages(Player player) {
		
		MakeSQLQuery msq = new MakeSQLQuery(TableType.SKYWARS_DATA)
				.select("p_last_games_average")
				.where("p_id", Resolver.getNetworkID(player));
		
		String averages = "{}";
		
		try {
			SQLResultSet sqr = msq.execute();
			if(sqr.next()) {
				averages = sqr.get("p_last_games_average");
			}
			
		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}
		
		if(averages != "{}") {
			if(averages.contains(";")) {
				
				String[] av_array = averages.split(";");
				double[] co_array = new double[] { };
				
				if(av_array.length >= 50) {
					
					int pos_x = 0;
					for(String k : av_array) {
						
						try {
							
							double av = Double.valueOf(k);
							co_array[pos_x] = av;
							pos_x++;
							
						}catch(Exception e) {
							return new double[] { 0.0 };
						}
						
					}
					
				}
				
				if(co_array.length >= 50) {
					return co_array;
				}
				
			}
		}
		
		return new double[] { 0.0 };
	}
	
	public static void setAverages(Player player, Double[] averages) {
		 
		setStats(player, StringUtils.join(averages, ";"));
		return;
		
	}
	
	public static int getExtraPoints(Player player) {
		
		MakeSQLQuery msq = new MakeSQLQuery(TableType.SKYWARS_DATA)
					.select("p_extra_points")
					.where("p_id", Resolver.getNetworkID(player));
		
		try {
			SQLResultSet sqr = msq.execute();
			if(sqr.next()) {
				return sqr.get("p_extra_points");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return VariableUtils.SKYWARS_INITIAL_EXTRA_POINTS;
	}
	
	public static void setExtraPoints(Player player, int quantity) {
		 
		MakeSQLUpdate msu = new MakeSQLUpdate(TableType.SKYWARS_DATA, TableOperation.UPDATE);
		
		msu.rowOperation("p_extra_points", quantity);
		msu.whereOperation("p_id", Resolver.getNetworkID(player));
		
		try {
			
			msu.execute();
			return;
			
		} catch (IllegalArgumentException | SQLException e) {
			e.printStackTrace();
		}
		
		return;
		
	}
	
	public enum SelectedItemType {
		
		CAGE,
		KIT,
		EXTRA_INFO,
		BOW_EFFECT,
		DEATH_EFFECT,
		
		;
		
	}
	
	public static class AccountInfo {
		
		private String items = VariableUtils.SKYWARS_INITIAL_ITEMS;
		private String stats = "0;0;0;0;NEW";
		private String selected = VariableUtils.SKYWARS_INITIAL_SELECTED;
		
		private double[] averages = new double[] { 0.0 };
		
		public AccountInfo(String stats, String items, String selected, double[] averages) {
			
			this.items = items;
			
			this.stats = stats;
			this.selected = selected;
			
			this.averages = averages;
			
		}

		public String getStats() {
			return stats;
		}

		public void setStats(String stats) {
			this.stats = stats;
		}

		public double[] getAverages() {
			return averages;
		}

		public void setAverages(double[] averages) {
			this.averages = averages;
		}

		public String getSelected() {
			return selected;
		}

		public void setSelected(String selected) {
			this.selected = selected;
		}

		public String getItems() {
			return items;
		}

		public void setItems(String items) {
			this.items = items;
		}
		
	}
	
}
