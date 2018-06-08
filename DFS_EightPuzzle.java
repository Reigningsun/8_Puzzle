package csc380;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


/* 	This works 
 * 		Easy:					Medium:					Hard:
	Max Size of Queue: 	42  		Max Size of Queue:	41		Max Size of Queue:	41				
	Nodes Explored:		4,478		Nodes Explored:		25,957		Nodes Explored:		31,494
	# Steps in Solution:	37		# Steps in Solution: 	47		# Steps in Solution:	46
	Cost of Solution:	179   		Cost of Solution:       255		Cost of Solution:	192	
*/

// expands nodes by (LIFO)

public class DFS_EightPuzzle {
	
	PuzzleState initialState;											// PuzzleState (Root) representation of the starting state
	String goal = "";												// string representation of the goal state

	
	LinkedList <PuzzleState> 	stepsToSolve;									// stores the path from start to goal
	LinkedList <PuzzleState> 	unexploredMoves;								// nodes we can reach but have not yet explored
	Map <String, PuzzleState> 	exploredMoves;									// stores previously observed states so we do not explore them again
	
	int nodesExplored	 	= 0;										// number of nodes popped off of the queue
	int lengthOfSolution		= 0;										// number of moves from start to goal
	int costOfSolution 		= 0;										// cost of solution path
	int maxSizeOfQueue 		= 0; 										// max space needed to store unexploredMoves
	
	
	
	DFS_EightPuzzle(PuzzleState initialState, String goal){								// constructor
		this.stepsToSolve = new LinkedList<PuzzleState>();
		this.unexploredMoves = new LinkedList<PuzzleState>();
		this.exploredMoves = new HashMap<String, PuzzleState>();
		this.initialState = initialState;
		this.goal = goal;
		addToUnexploredMoves(initialState);												
	}
	
	
	
	void getSuccessors(){												// discovers legal moves and adds them to unexploredMoves if they are new
		long startTime = System.currentTimeMillis();								// get the time at start of search
		
		while (!unexploredMoves.isEmpty()){
			long elapsedTime = (new Date()).getTime() - startTime;				
			long duration = (elapsedTime / 1000);	 							// find out how long the search has been running in seconds
			if (duration >= 300){										// ends the search after 5 minutes and prints out statistics 
				System.out.println("Timed Out");
				printSolution();
				break;
			}
			
			PuzzleState current = unexploredMoves.pop();
			nodesExplored++;										// update number of nodes explored
			
			if (exploredMoves.containsKey(current.tilePos)){						// avoid duplicating work. Won't expand nodes that have the same 
				continue;										// tile set up as others already seen
			}
												
			exploredMoves.put(current.tilePos, current);							// marks current PuzzleState as explored
				
			if (isGoal(current.tilePos)){									// if we find a goal there isn't any reason to expand further							
				costOfSolution = current.costOfPath;							// updates the costOfPath
				addStepsToSolve(current);								// creates a linked list of the solution
				printSolution();
				break;
			}
			
			int blanksIndex = current.tilePos.indexOf("0");							// find the location of the blank tile
			String tiles = current.tilePos;
			int costOfPath = current.costOfPath; 
	
								
			// Move Left
			if (blanksIndex != 2 && blanksIndex != 5 && blanksIndex != 8){					// LEFT: move adjacent tile into blank space by sliding it LEFT
				String newTilePos = tiles.substring(0, blanksIndex)					// create the new configuration of the tiles after moving LEFT
						+ tiles.charAt(blanksIndex +1)
						+ tiles.charAt(blanksIndex)
						+ tiles.substring(blanksIndex +2);
				int cost = (Character.getNumericValue(tiles.charAt(blanksIndex + 1)) + costOfPath);	// adds the number of the tile being moved to costOfPath
																		
								
				PuzzleState newState = new PuzzleState(newTilePos, current, cost, "Left"); 		// create PuzzleState representing the game state after taking a LEFT move
																	
				if (iShouldAdd(newState)){ 								// adds new state to unexplored queue if it isn't already on there
					addToUnexploredMoves(newState);								
				}
			}
				
			// Move Right
			if (!(blanksIndex % 3 == 0)){									// RIGHT: move adjacent tile into blank space by sliding it RIGHT
				String newTilePos = tiles.substring(0, blanksIndex -1)					// create the new configuration of the tiles after moving RIGHT
						+ tiles.charAt(blanksIndex)	
						+ tiles.charAt(blanksIndex - 1)
						+ tiles.substring(blanksIndex + 1); 
				int cost = (Character.getNumericValue(tiles.charAt(blanksIndex - 1)) + costOfPath);	// adds the number of the tile being moved to costOfPath
														
							
				PuzzleState newState = new PuzzleState(newTilePos, current, cost, "Right");		// create PuzzleState representing the game state after taking a RIGHT move
															
				if (iShouldAdd(newState)){ 								// adds new state to unexplored queue if it isn't already on there
					addToUnexploredMoves(newState);								
				}
			}
				
			// Move Up
			if (!(blanksIndex > 5)){									// UP: move adjacent tile into blank space by sliding it UP
				String newTilePos = tiles.substring(0, blanksIndex)					// create the new configuration of the tiles after moving UP
						+ tiles.charAt(blanksIndex + 3)
						+ tiles.substring(blanksIndex + 1, blanksIndex + 3)
						+ tiles.charAt(blanksIndex)	
						+ tiles.substring(blanksIndex + 4); 
				int cost = (Character.getNumericValue(tiles.charAt(blanksIndex + 3)) + costOfPath);	// adds the number of the tile being moved to costOfPath
														
							
				PuzzleState newState = new PuzzleState(newTilePos, current, cost, "Up"); 		// create PuzzleState representing the game state after taking a UP move
																
				if (iShouldAdd(newState)){ 								// adds new state to unexplored queue if it isn't already on there
					addToUnexploredMoves(newState);								
				}
			}
				
			// Move Down
			if (!(blanksIndex < 3)){									// DOWN: move adjacent tile into blank space by sliding it DOWN
				String newTilePos = tiles.substring(0, blanksIndex -3)					// create the new configuration of the tiles after moving UP
						+ tiles.charAt(blanksIndex)
						+ tiles.substring(blanksIndex - 2, blanksIndex)
						+ tiles.charAt(blanksIndex - 3)
						+ tiles.substring(blanksIndex + 1);
				int cost = (Character.getNumericValue(tiles.charAt(blanksIndex - 3)) + costOfPath);	// adds the number of the tile being moved to costOfPath
													
							
				PuzzleState newState = new PuzzleState(newTilePos, current, cost, "Down");		// create PuzzleState representing the game state after taking a DOWN move
																
				if (iShouldAdd(newState)){ 								// adds new state to unexplored queue if it isn't already on there
					addToUnexploredMoves(newState);								
				}
			}
		}
	}
	
	
	
	// Helpers

	boolean iShouldAdd(PuzzleState newState){									// determines whether a PuzzleState should be added
		if (!unexploredMoves.contains(newState) 
				&& !exploredMoves.containsKey(newState.tilePos)
				&& newState.depth < 48){								// depth limit 
			return true;
		}
		return false;
	}
	
	
	void  addToUnexploredMoves(PuzzleState newState){								// adds PuzzleState node to unexploredMoves if it has not already 
		unexploredMoves.addFirst(newState);									// been explored	
		updateMaxSizeOfQueue();																
	}
	
	
	
	void updateMaxSizeOfQueue(){											// keeps track of the maximum number of nodes on the unexploredMoves queue, 
		int newSize = unexploredMoves.size();									// updating if a new max occurs
		if(newSize > maxSizeOfQueue){
			maxSizeOfQueue = newSize;
		}
	}	
	
	
	boolean isGoal(String tilePos){											// determines if the given tile position is the goal state
		return Integer.parseInt(tilePos) == Integer.parseInt(goal);
	}
	
	
	
	void endSearchOutputSolution(PuzzleState current){								// update and print current solutionPath							
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
	
	
	
	void printSolution(){												// outputs solution step by step
		while (!stepsToSolve.isEmpty()){
			PuzzleState step = stepsToSolve.pop();
			System.out.println("---" + step.moveToState);
			System.out.println(step.tilePos);
			System.out.println();
		}
		System.out.println("Max Size of Queue: " + maxSizeOfQueue);
		System.out.println("Nodes Explored: " + nodesExplored);
		System.out.println("# of Steps to solution: "+ (lengthOfSolution - 1)); 	
		System.out.println("Cost of Solution: "+ costOfSolution);
				
	}	
}

