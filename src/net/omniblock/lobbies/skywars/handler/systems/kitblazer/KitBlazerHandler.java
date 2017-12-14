package net.omniblock.lobbies.skywars.handler.systems.kitblazer;

public class KitBlazerHandler {

	protected static KitBlazerSystem nowSystem;
	
	public static void renewSystem(KitBlazerSystem newSystem) {
		
		if(nowSystem != null)
			nowSystem.destroy();
		
		nowSystem = newSystem;
		return;
		
	}
	
	public static KitBlazerSystem getSystem() {
		return nowSystem;
	}
	
}
