package client;

import java.util.PriorityQueue;
import java.util.Queue;

import client.Client.Agent;

public class SafeSpotDetector {
	public static PriorityQueue<SafePoint> detectSafeSpots(World world, int agentId) {
		PriorityQueue<SafePoint> safeSpots = new PriorityQueue<SafePoint>();
		for(Point point : world.getRechableCells(agentId)) {
			if(world.isGoalAt(point) || world.isBoxAt(point)) {
				continue;
			}
			SafePoint spoint = new SafePoint(point);
			//Logger.logLine("------ reachable point " + spoint + "------");
			if(Pattern.isSafePoint(spoint, world)) {
				safeSpots.add(spoint);
			}
		}
		return safeSpots;
	}
	
	public static Point getSafeSpotForAgent(World world, int agentId, Queue<Point> path) {
		PriorityQueue<SafePoint> safeSpots = detectSafeSpots(world, agentId);

		//Logger.logLine("-----------  Safe spots ----------");
		//for (SafePoint safespot : safeSpots) {
		//	Logger.logLine("" + safespot);
		//}
		
		Point safePosition = null;
		//Find a safepoint not on the path.
		for (SafePoint safespot : safeSpots) {
			if(!path.contains(safespot) && world.isFreeCell(safespot)) {
				safePosition = safespot;
				break;
			}
		}
		return safePosition;
	}
	
	public static Point getSafeSpotForBox(World world, Box box) {
		Agent agent = world.getAgentToMoveBox(box);
		PriorityQueue<SafePoint> safeSpots = detectSafeSpots(world, agent.getId());

		//Logger.logLine("-----------  Safe spots ----------");
		//for (SafePoint safespot : safeSpots) {
		//	Logger.logLine("" + safespot);
		//}
		
		return safeSpots.poll();
	}
}
