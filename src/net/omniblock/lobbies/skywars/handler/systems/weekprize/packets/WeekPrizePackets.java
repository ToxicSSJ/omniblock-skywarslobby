package net.omniblock.lobbies.skywars.handler.systems.weekprize.packets;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.omniblock.lobbies.OmniLobbies;
import net.omniblock.packets.network.Packets;
import net.omniblock.packets.network.structure.data.PacketSocketData;
import net.omniblock.packets.network.structure.data.PacketStructure.DataType;
import net.omniblock.packets.network.structure.packet.RequestInformationPacket;
import net.omniblock.packets.network.structure.packet.ResposeInformationPacket;
import net.omniblock.packets.network.structure.type.PacketSenderType;
import net.omniblock.packets.network.tool.object.PacketResponder;

public class WeekPrizePackets {

	protected static Date date;
	protected static WeekPrizeStatus status;
	
	protected static String format = "dd/MM/yyyy HH:mm:ss";
	
	public static enum WeekPrizeStatus {
		
		WAITING,
		ENABLED
		
		;
		
	}
	
	public static BukkitTask startQuery(){
		
		return new BukkitRunnable(){
			
			@Override
			public void run(){
				
				Packets.STREAMER.streamPacketAndRespose(
						new RequestInformationPacket()
						.setServername(Bukkit.getServerName())
						.setInformationKey("WeekPrize")
						.setInformationValue("Skywars")
						.build().setReceiver(PacketSenderType.OMNICORE),
						new PacketResponder<ResposeInformationPacket>(){

							@Override
							public void readRespose(PacketSocketData<ResposeInformationPacket> packetsocketdata) {
								
								try {
									
									String value = packetsocketdata.getStructure().get(DataType.STRINGS, "infovalue");
									
									if(!value.equalsIgnoreCase("WAITING")){
										status = WeekPrizeStatus.ENABLED;
										date = new SimpleDateFormat(format).parse(value);
									} else {
										status = WeekPrizeStatus.WAITING;
										date = null;
									}
									
								} catch (ParseException e) { e.printStackTrace(); }
								
								return;
								
							}
							
						}
				);
				
			}
			
		}.runTaskTimer(OmniLobbies.getInstance(), 50L, 50L);
		
	}
	
	public static WeekPrizeStatus getStatus(){
		return status;
	}
	
	public static Date getDate(){
		return date;
	}
	
}
