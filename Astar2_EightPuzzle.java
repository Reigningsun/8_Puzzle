package csc380;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

/* 	 works and is similar to performance of classmates
 * 						Easy:								Medium:									Hard:
		 		Max Size of Queue: 		9			Max Size of Queue:		  49 				Max Size of Queue:		23,622	 		
				Nodes Explored:			13			Nodes Explored:			  66	    		Nodes Explored:			176,406	
				# Steps in Solution:	5			# Steps in Solution:	  9		 			# Steps in Solution:	30	     
				Cost of Solution:		17			Cost of Solution:		  31				Cost of Solution:		128
*/

// expands nodes by (costOfPath + tilesOutofPos + manhattanDist)

public class Astar2_EightPuzzle {
	
	PuzzleState initialState;													// PuzzleState (Root) representation of the starting state
	String goal = "";															// string representation of the goal state

	
	LinkedList <PuzzleState> 	stepsToSolve;									// stores the path from start to goal
	PriorityQueue <PuzzleState> unexploredMoves;								// nodes we can reach but have not yet explored
	Map <String, PuzzleState>	explored;										// stores explored nodes for comparison 
	
	int nodesExplored	 	= 0;												// number of nodes popped off of the queue
	int lengthOfSolution	= 0;												// number of moves from start to goal
	int costOfSolution 		= 0;												// cost of solution path
	int maxSizeOfQueue 		= 0; 												// max space needed to store unexploredMoves
	
	
	
	Astar2_EightPuzzle(PuzzleState initialState, String goal){					// constructor
		this.stepsToSolve = new LinkedList<PuzzleState>();
		this.unexploredMoves = new PriorityQueue<PuzzleState>();
		this.explored = new HashMap<String, PuzzleState>();
		this.initialState = initialState;
		this.goal = goal;
		addToUnexploredMoves(initialState);												
	}
	
	
	
	void getSuccessors(){														// discovers legal moves and adds them to unexploredMoves if they are new
		long startTime = System.currentTimeMillis();							// get the time at start of search
		
		while (!unexploredMoves.isEmpty()){
			long elapsedTime = (new Date()).getTime() - startTime;				
			long duration = (elapsedTime / 1000);	 							// find out how long the search has been running in seconds
			if (duration >= 300){												// ends the search after 5 minutes and prints out statistics 
				System.out.println("Timed Out");
				printSolution();
				break;
			}
			
			PuzzleState current = unexploredMoves.poll();
			nodesExplored++;													// update number of nodes explored
															
			explored.put(current.tilePos, current);								// marks current PuzzleState as explored
			
			int blanksIndex = current.tilePos.indexOf("0");						// find the location of the blank tile
			String tiles = current.tilePos;
			int costOfPath = current.costOfPath; 
			
			if (isGoal(tiles)){													// if we find a goal there isn't any reason to expand further							
				costOfSolution = costOfPath;									// updates the costOfPath
				addStepsToSolve(current);										// creates a linked list of the solution
				printSolution();
				break;
			}
			
	
								
			// Move Left
			if (blanksIndex != 2 && blanksIndex != 5 && blanksIndex != 8){		// LEFT: move adjacent tile into blank space by sliding it LEFT
				String newTilePos = tiles.substring(0, blanksIndex)				// create the new configuration of the tiles after moving LEFT
						+ tiles.charAt(blanksIndex +1)
						+ tiles.charAt(blanksIndex)
						+ tiles.substring(blanksIndex +2);
				int cost = (Character.getNumericValue							// adds the number of the tile being moved to costOfPath
										(tiles.charAt(blanksIndex + 1))				
							+ costOfPath);	
				PuzzleState newState = new PuzzleState(newTilePos, current, 	// create PuzzleState representing the game state after taking a LEFT move
															cost, "Left");		
				if(iShouldAdd(newState)){
					addToUnexploredMoves(newState); 							// adds new state to unexplored queue									
																				// the case of duplicate entries, keep the lower cost option
				}
			}
				
			// Move Right
			if (!(blanksIndex % 3 == 0)){										// RIGHT: move adjacent tile into blank space by sliding it RIGHT
				String newTilePos = tiles.substring(0, blanksIndex -1)			// create the new configuration of the tiles after moving RIGHT
						+ tiles.charAt(blanksIndex)	
						+ tiles.charAt(blanksIndex - 1)
						+ tiles.substring(blanksIndex + 1); 
				int cost = (Character.getNumericValue							// adds the number of the tile being moved to costOfPath
										(tiles.charAt(blanksIndex - 1))				
							+ costOfPath);
				PuzzleState newState = new PuzzleState(newTilePos, current, 	// create PuzzleState representing the game state after taking a RIGHT move
															cost, "Right");
				if(iShouldAdd(newState)){
					addToUnexploredMoves(newState); 							// adds new state to unexplored queue									
																				// the case of duplicate entries, keep the lower cost option
				}
			}
				
			// Move Up
			if (!(blanksIndex > 5)){											// UP: move adjacent tile into blank space by sliding it UP
				String newTilePos = tiles.substring(0, blanksIndex)				// create the new configuration of the tiles after moving UP
						+ tiles.charAt(blanksIndex + 3)
						+ tiles.substring(blanksIndex + 1, blanksIndex + 3)
						+ tiles.charAt(blanksIndex)	
						+ tiles.substring(blanksIndex + 4); 
				int cost = (Character.getNumericValue							// adds the number of the tile being moved to costOfPath
										(tiles.charAt(blanksIndex + 3))				
							+ costOfPath);
				PuzzleState newState = new PuzzleState(newTilePos, current, 	// create PuzzleState representing the game state after taking a UP move
															cost, "Up");	
				if(iShouldAdd(newState)){
					addToUnexploredMoves(newState); 							// adds new state to unexplored queue									
																				// the case of duplicate entries, keep the lower cost option
				}
			}
				
			// Move Down
			if (!(blanksIndex < 3)){											// DOWN: move adjacent tile into blank space by sliding it DOWN
				String newTilePos = tiles.substring(0, blanksIndex -3)			// create the new configuration of the tiles after moving UP
						+ tiles.charAt(blanksIndex)
						+ tiles.substring(blanksIndex - 2, blanksIndex)
						+ tiles.charAt(blanksIndex - 3)
						+ tiles.substring(blanksIndex + 1);
				int cost = (Character.getNumericValue							// adds the number of the tile being moved to costOfPath
										(tiles.charAt(blanksIndex - 3))				
							+ costOfPath);
				PuzzleState newState = new PuzzleState(newTilePos, current, 	// create PuzzleState representing the game state after taking a DOWN move
															cost, "Down");	
				if(iShouldAdd(newState)){	
					addToUnexploredMoves(newState); 							// adds new state to unexplored queue									
																				// the case of duplicate entries, keep the lower cost option
				}
			}
		}
	}
	
	
	// Helpers
	
	boolean iShouldAdd(PuzzleState newState){									// determines if adding a puzzleState is appropriate
		if (!explored.containsKey(newState.tilePos)								// if we haven't explored it we need to look at it
				&& !unexploredMoves.contains(newState)){							
			return true;
		}
		return false;															// if we have seen it before and it was cheaper we won't search this
	}
	
	
	
	void  addToUnexploredMoves(PuzzleState newState){							// adds PuzzleState node to unexploredMoves if it has the lowest combined 
		newState.tilesOutPos 	= countTilesOutPos(newState.tilePos);			
		newState.manhattanDist 	= calcManhattanDist(newState.tilePos); 			 
		newState.sortStyle 		= 2;
		unexploredMoves.add(newState);
		explored.put(newState.tilePos, newState);
		updateMaxSizeOfQueue();
	}
	
	
	
	void updateMaxSizeOfQueue(){												// keeps track of the maximum number of nodes on the unexploredMoves queue, 
		int newSize = unexploredMoves.size();									// updating if a new max occurs
		if(newSize > maxSizeOfQueue){
			maxSizeOfQueue = newSize;
		}
	}	
	
	
	
	boolean isGoal(String tilePos){												// determines if the given tile position is the goal state
		return Integer.parseInt(tilePos) == Integer.parseInt(goal);
	}
	
	
	
	void endSearchOutputSolution(PuzzleState current){							// update and print current solutionPath							
		costOfSolution = current.costOfPath;									
		addStepsToSolve(current);												
		printSolution();
	}
	
	
	
	void addStepsToSolve(PuzzleState goalState){								// adds each puzzleState from goal to initial state to a LinkedList
		PuzzleState current = goalState;
		while (!(current == null)){
			stepsToSolve.addFirst(current);
			current = current.parent;
		}
		lengthOfSolution = stepsToSolve.size();
	}
	
	
	
	void printSolution(){														// outputs solution step by step
		while (!stepsToSolve.isEmpty()){
			PuzzleState step = stepsToSolve.pop();
			System.out.println("---" + step.moveToState);
			System.out.println(step.tilePos);
			System.out.println();
		}
		System.out.println("Max Size of Queue: " + maxSizeOfQueue);
		System.out.println("Nodes Explored: " + nodesExplored);
		System.out.println("# of Steps to solution: " 
							+ (lengthOfSolution - 1));
		System.out.println("Cost of Solution: "
				+ costOfSolution);
	}
	
	
	
	short countTilesOutPos (String tilePos){									// counts the number of tiles that are not in the goal position 
		short count = 0;
		for (int i = 0; i < tilePos.length(); i++){
			if (tilePos.charAt(i) != goal.charAt(i)){
				count++;
			}
		}
		return count;
	}
	
	
	short calcManhattanDist (String tilePos){									// calculates Manhattan distance of each tile between current tile set and goal set
		short count = 0;
		for (int i = 0; i < tilePos.length(); i++){								// horizontal distance	(abs(curr % 3 - goal % 3))
			String character = Integer.toString(i);
			count += Math.abs(tilePos.indexOf(character) % 3
								- goal.indexOf(character) % 3); 							
		}
		
		for (int i = 0; i < tilePos.length(); i++){								// vertical distance (integer division of index by 3)
			String character = Integer.toString(i);
			count += Math.abs((tilePos.indexOf(character) / 3)
								- (goal.indexOf(character) / 3));
			
		}
		return count;
	}
	 
}

