package csc380;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


/*	
 * 				Easy:				Medium:						Hard:
//  	Max Size of Queue: 		16	Max Size of Queue:	117		Max Size of Queue:		41,231	
//	Nodes Explored:			22	Nodes Explored:		192		Nodes Explored:			104,760
//	# Steps in Solution:		5	# Steps in Solution:	9		# Steps in Solution:		failed
//	Cost of Solution:		17	Cost of Solution:	31		Cost of Solution:		failed
*/

// expands nodes by (FIFO)

public class BFS_EightPuzzle {
	
	PuzzleState initialState;												// PuzzleState (Root) representation of the starting state
	String goal = "";													// string representation of the goal state

	
	LinkedList <PuzzleState> 	stepsToSolve;										// stores the path from start to goal
	LinkedList <PuzzleState> 	unexploredMoves;									// nodes we can reach but have not yet explored
	Map <String, PuzzleState> 	exploredMoves;										// stores previously observed states so we do not explore them again <tilePos, PuzzleState>
	
	int nodesExplored	 	= 0;											// number of nodes popped off of the queue
	int lengthOfSolution		= 0;											// number of moves from start to goal
	int costOfSolution 		= 0;											// cost of solution path
	int maxSizeOfQueue 		= 0; 											// max space needed to store unexploredMoves
	
	
	
	BFS_EightPuzzle(PuzzleState initialState, String goal){									// constructor
		this.stepsToSolve = new LinkedList<PuzzleState>();
		this.unexploredMoves = new LinkedList<PuzzleState>();
		this.exploredMoves = new HashMap<String, PuzzleState>();
		this.initialState = initialState;
		this.goal = goal;
		addToUnexploredMoves(initialState);												
	}
	
	
	
	void getSuccessors(){													// discovers legal moves from current tile position and adds them to unexploredMoves if they are new
		long startTime = System.currentTimeMillis();									// get the time at start of search
		
		while (!unexploredMoves.isEmpty()){
			long elapsedTime = (new Date()).getTime() - startTime;							// current time minus start time
			long duration = (elapsedTime / 1000);	 								// find out how long the search has been running in seconds
			if (duration >= 800){											// ends the search after 5 minutes and prints out statistics 300
				System.out.println("Timed Out");
				printSolution();
				break;
			}														 
			
			PuzzleState current = unexploredMoves.pop();
			nodesExplored++;											// update number of nodes explored
															
			exploredMoves.put(current.tilePos, current);								// marks current PuzzleState as explored
				
			if (isGoal(current.tilePos)){										// if we find a goal there isn't any reason to expand further
				endSearchOutputSolution(current);
				break;
			}
			else{
				int blanksIndex = current.tilePos.indexOf("0");							// find the location of the blank tile
				String tiles = current.tilePos;
				int costOfPath = current.costOfPath; 
	

				// Move Down
				if (!(blanksIndex < 3)){									// DOWN: move adjacent tile into blank space by sliding it DOWN
					String newTilePos = tiles.substring(0, blanksIndex -3)					// create the new configuration of the tiles after moving UP
							+ tiles.charAt(blanksIndex)
							+ tiles.substring(blanksIndex - 2, blanksIndex)
							+ tiles.charAt(blanksIndex - 3)
							+ tiles.substring(blanksIndex + 1);
					int cost = (Character.getNumericValue(tiles.charAt(blanksIndex - 3))+ costOfPath);	// adds the number of the tile being moved to costOfPath Integer.valueOf
															
									
					PuzzleState newState = new PuzzleState(newTilePos, current, cost, "Down"); 		// create PuzzleState representing the game state after taking a DOWN move
																
					if (isGoal(newState.tilePos)){								// if we find a goal there isn't any reason to expand further
						endSearchOutputSolution(newState);
						break;
					}
					if (iShouldAdd(newState)){								// adds new state to unexplored queue if it isn't already on there
						addToUnexploredMoves(newState);								
					}
				}
				
				// Move Left
				if (blanksIndex != 2 && blanksIndex != 5 && blanksIndex != 8){					// LEFT: move adjacent tile into blank space by sliding it LEFT
					String newTilePos = tiles.substring(0, blanksIndex)					// create the new configuration of the tiles after moving LEFT
										+ tiles.charAt(blanksIndex +1)
										+ tiles.charAt(blanksIndex)
										+ tiles.substring(blanksIndex +2);
					int cost = (Character.getNumericValue(tiles.charAt(blanksIndex + 1))+ costOfPath);	// adds the number of the tile being moved to costOfPath Integer.valueOf
															
								
					PuzzleState newState = new PuzzleState(newTilePos, current, cost, "Left");		// create PuzzleState representing the game state after taking a LEFT move
																	
					if (isGoal(newState.tilePos)){								// if we find a goal there isn't any reason to expand further
						endSearchOutputSolution(newState);
						break;
					}
					if (iShouldAdd(newState)){								// adds new state to unexplored queue if it isn't already on there
						addToUnexploredMoves(newState);								
					}
				}
				
				// Move Right
				if (!(blanksIndex % 3 == 0)){									// RIGHT: move adjacent tile into blank space by sliding it RIGHT
					String newTilePos = tiles.substring(0, blanksIndex -1)					// create the new configuration of the tiles after moving RIGHT
										+ tiles.charAt(blanksIndex)	
										+ tiles.charAt(blanksIndex - 1)
										+ tiles.substring(blanksIndex + 1); 
					int cost = (Character.getNumericValue(tiles.charAt(blanksIndex - 1))+ costOfPath);	// adds the number of the tile being moved to costOfPath
															
								
					PuzzleState newState = new PuzzleState(newTilePos, current, cost, "Right");		// create PuzzleState representing the game state after taking a RIGHT move
															
					if (isGoal(newState.tilePos)){								// if we find a goal there isn't any reason to expand further
						endSearchOutputSolution(newState);
						break;
					}
					if (iShouldAdd(newState)){								// adds new state to unexplored queue if it isn't already on there
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
					int cost = (Character.getNumericValue(tiles.charAt(blanksIndex + 3))+ costOfPath);	// adds the number of the tile being moved to costOfPath
															
								
					PuzzleState newState = new PuzzleState(newTilePos, current, cost, "Up");		// create PuzzleState representing the game state after taking a UP move
																
					if (isGoal(newState.tilePos)){								// if we find a goal there isn't any reason to expand further
						endSearchOutputSolution(newState);
						break;
					}
					if (iShouldAdd(newState)){								// adds new state to unexplored queue if it isn't already on there
						addToUnexploredMoves(newState);								
					}
				}
			}
		}
	}
	
	
	
	// Helpers
	
	boolean iShouldAdd(PuzzleState newState){										// determines if this state has already been seen
		if (!unexploredMoves.contains(newState)
				&& !exploredMoves.containsKey(newState.tilePos)){
			return true;								
		}
		return false;
	}
	
	
	
	void  addToUnexploredMoves(PuzzleState newState){									// adds PuzzleState node to unexploredMoves if it has not already been explored BFS
		unexploredMoves.addLast(newState);
		updateMaxSizeOfQueue();
	}
	
	
	
	void updateMaxSizeOfQueue(){												// keeps track of the maximum number of nodes on the unexploredMoves queue, updating if a new max occurs
		int newSize = unexploredMoves.size();
		if(newSize > maxSizeOfQueue){
			maxSizeOfQueue = newSize;
		}
	}
	
	
	
	boolean isGoal(String tilePos){												// determines if the given tile position is the goal state
		return Integer.parseInt(tilePos) == Integer.parseInt(goal);
	}
	
	
	
	void endSearchOutputSolution(PuzzleState current){									// update and print current solutionPath							
		costOfSolution = current.costOfPath;										// updates the costOfPath
		addStepsToSolve(current);											// creates a linked list of the solution
		printSolution();
	}
	
	
	
	void addStepsToSolve(PuzzleState goalState){										// adds each puzzleState from goal to initial state to a LinkedList
		PuzzleState current = goalState;
		while (!(current == null)){
			stepsToSolve.addFirst(current);
			current = current.parent;
		}
		lengthOfSolution = stepsToSolve.size();
	}
	
	
	
	void printSolution(){													// outputs solution step by step
		while (!stepsToSolve.isEmpty()){
			PuzzleState step = stepsToSolve.pop();
			System.out.println("---" + step.moveToState);
			System.out.println(step.tilePos);
			System.out.println();
		}
		System.out.println("Max Size of Queue: " + maxSizeOfQueue);
		System.out.println("Nodes Explored: " + nodesExplored);
		System.out.println("# of Steps to solution: "+ (lengthOfSolution - 1) ); 		
		System.out.println("Cost of Solution: "+ costOfSolution);
							
	}
	
	
	

	
	
	

	
	
	


}
