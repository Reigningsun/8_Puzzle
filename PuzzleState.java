package csc380;


public class PuzzleState implements Comparable<PuzzleState> {

	String tilePos;																			// order of the tiles
	PuzzleState parent;																		// previous PuzzleState
	int costOfPath;																			// cumulative cost of path leading to this node
	String moveToState;																		// move that lead to this state
	int depth;																				// depth in tree
	short tilesOutPos;																		// stores total number of tiles not in goal position
	short manhattanDist;																	// stores total Manhattan distances of tiles
	short sortStyle;																		// 1: BestFirst, 2: A*, 3: A*2
	
	
	PuzzleState(String tilePos, PuzzleState parent, int costOfPath, String moveToState){	// constructor
		this.tilePos 		= tilePos;
		this.parent 		= parent;
		this.costOfPath 	= costOfPath;
		this.moveToState 	= moveToState;
		
		if (parent == null){
			this.depth = 0;
		}
		else{
			this.depth		= parent.depth +1;
		}
		
		sortStyle 			= 0;
		tilesOutPos 		= 0;
		this.manhattanDist 	= 0;
	}


		@Override
		public int compareTo(PuzzleState puzzle) {
			if (sortStyle == 3){															// Astar2: comparable by cost of path + Manhattan distances + tilesOutOfPos
				return ((this.costOfPath + this.manhattanDist + this.tilesOutPos)
						- (puzzle.costOfPath + puzzle.manhattanDist + puzzle.tilesOutPos));
				
			}
			if (sortStyle == 2){															// Astar: comparable by cost of path + tiles out of position
				return ((this.costOfPath + this.tilesOutPos)
						- (puzzle.costOfPath + puzzle.tilesOutPos));
			}
			
			if (sortStyle == 1){															// BestFirst: comparable by tilesOutOfPos
				return (this.tilesOutPos - puzzle.tilesOutPos);
			}
			
			return (this.costOfPath - puzzle.costOfPath);									// Default: comparable by cost of path
		}
	}
