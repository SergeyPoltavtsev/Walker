package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import client.Heuristic.AStar;
import client.Heuristic.HeuristicPathFunction;
import client.Search.BestFirstSearch;
import client.Search.PathNode;
import client.Search.SearchNode;

public class IntentionDecomposer {
	
	public static ArrayList<SubIntention> decomposeIntention(Intention intention, World world, int agentId){
		ArrayList<SubIntention> subIntentions = new ArrayList<SubIntention>();
		Point agentPosition = world.getAgent(agentId).getPosition();
		Point boxPosition = intention.getBox().getPosition();
		Point goalPosition = intention.getGoal().getPosition();
		Queue<Point> pathFromAgentToBox = findPath(world, agentPosition, boxPosition);
		World clearPathToBoxWorld = moveBoxesOnPathToSafePlaces(world, pathFromAgentToBox, subIntentions);
		
		//SubIntention to clear path from Box to Goal.
		Queue<Point> pathFromBoxToGoal = findPath(clearPathToBoxWorld, boxPosition, goalPosition);
		moveBoxesOnPathToSafePlaces(clearPathToBoxWorld, pathFromBoxToGoal, subIntentions);

		//SubIntention for moving box to goal.
		subIntentions.add(new SubIntention(intention.getBox(), goalPosition));
		
		System.err.println("----------- Intention Decomposer START----------");
		for (SubIntention subIntention : subIntentions) {
			System.err.println("" + subIntention.getBox() + " -> " + subIntention.getEndPosition());
		}
		System.err.println("----------- Intention Decomposer END----------");
		return subIntentions;
	}

	private static World moveBoxesOnPathToSafePlaces(World world, Queue<Point> path, ArrayList<SubIntention> subIntentions) {
		World newWorld = world;
		
		for(Point point : path) {
			Box box = world.getBoxAt(point);
			if (box != null) {
				List<Point> safeSpots = SafeSpotDetector.detectSafeSpots(newWorld);
				Point savePosition = safeSpots.get(0);
				subIntentions.add(new SubIntention(box, savePosition));
				newWorld = new World(newWorld);
				newWorld.getBoxById(box.getId()).setPosition(savePosition);
			}
		}
		return newWorld;
	};

	private static Queue<Point> findPath(World world, Point sourcePosition, Point targetPosition) {
		HeuristicPathFunction pathFunction = new HeuristicPathFunction(targetPosition);
		AStar heuristic = new AStar(pathFunction);
		BestFirstSearch search = new BestFirstSearch(heuristic);

		search.addToFrontier(new PathNode(world, sourcePosition, targetPosition, true));
		while (true) {
			if (search.frontierIsEmpty()) {
				throw new RuntimeException("Unable to reach the target position");
			}

			PathNode leafNode = (PathNode) search.getAndRemoveLeaf();

			if (leafNode.getPosition().equals(targetPosition)) {
				return leafNode.extractListOfPossitions();
			}

			search.addToExplored(leafNode);
			for (SearchNode n : leafNode.getExpandedNodes()) {
				if (!search.isExplored(n) && !search.inFrontier(n)) {
					search.addToFrontier(n);
				}
			}
		}
	}
}
