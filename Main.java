package csc380;

import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		// easy: 134862705 medium: 281043765 hard: 567408321
		
		String goalPos = "123804765";		
		Scanner scanner = new Scanner (System.in);
		boolean again = true;
		
		while (again){
			System.out.println("Which algorithm? (BFS, DFS, UC, GBF, A*, A*2): ");
			String alg = scanner.next();
			System.out.println("Difficulty (E for easy, M for medium, H for hard): ");
			String diff = scanner.next();
			String initialPos = "";
			
			if (diff.equalsIgnoreCase("E")){
				initialPos = "134862705";
			}
			if (diff.equalsIgnoreCase("M")){
				initialPos = "281043765";	
			}
			if (diff.equalsIgnoreCase("H")){
				initialPos = "567408321";
			}
			
			PuzzleState root = new PuzzleState(initialPos, null, (short) 0, "Start");
			
			if (alg.equalsIgnoreCase("BFS")){
				BFS_EightPuzzle BFS_currentEightPuzzle= new BFS_EightPuzzle(root, goalPos);
				BFS_currentEightPuzzle.getSuccessors();
			}
			
			if (alg.equalsIgnoreCase("DFS")){
				DFS_EightPuzzle DFS_currentEightPuzzle= new DFS_EightPuzzle(root, goalPos);
				DFS_currentEightPuzzle.getSuccessors();
			}
			
			if (alg.equalsIgnoreCase("UC")){
				UniformCost_EightPuzzle UniformCost_currentEightPuzzle= new UniformCost_EightPuzzle(root, goalPos);
				UniformCost_currentEightPuzzle.getSuccessors();
			}
			
			if (alg.equalsIgnoreCase("GBF")){
				BestFirst_EightPuzzle BestFirst_currentEightPuzzle = new BestFirst_EightPuzzle(root, goalPos);
				BestFirst_currentEightPuzzle.getSuccessors();
			}
			
			if (alg.equalsIgnoreCase("A*")){
				Astar_EightPuzzle Astar_currentEightPuzzle = new Astar_EightPuzzle(root, goalPos);
				Astar_currentEightPuzzle.getSuccessors();
			}
			
			if (alg.equalsIgnoreCase("A*2")){
				Astar2_EightPuzzle Astar2_currentEightPuzzle = new Astar2_EightPuzzle(root, goalPos);
				Astar2_currentEightPuzzle.getSuccessors();
			}
			
			System.out.println("Would you like to run another test?(Y/N): ");
			String cont = scanner.next();
			if (cont.equalsIgnoreCase("N")){
				again = false;
			}
		}
	}
}
