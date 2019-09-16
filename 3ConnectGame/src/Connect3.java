 /* This class will provide an implementation of a variant of the famous Connect-4 game
 * in that it will only be required to collect three in a row to win the game.  This code
 * will take in a game state from a text file inputed through the command line
 * of connect3 and apply MiniMax to it to find out which player would win from that game 
 * state and what the next player's optimal move is.  
 */

//import statements
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.*;
import java.math.*;


public class Connect3 {

	//instance variable
	private static int rowVal = 4;
	private static int colVal = 5;
	private static int utilVal;
	private	static char empty = '.';
	private static char p1 = 'X';
	private static char p2 = 'O';
	private static int bestVal = 0;
	
	//main method where the program will be run from
	public static void main(String[] args) throws FileNotFoundException {
		//get the file -> args[0] will be the path name to the problem in .txt format
		File file = new File(args[0]);  //should be args[0]
		char[][] gameBoard = createGameBoard(file);
		
		//set the utility value at beginning to negative outside of range
		utilVal = 0;
		
		//run the miniMax algorithm on the gameState to get results for output
		//this variable will be the column # the next player should play their optimal move
		int optMove = miniMax(gameBoard);
		//after miniMax has run, get utility value of game board
		int utility = bestVal;
		//get current player
		int player = getCurrentPlayer(gameBoard);
		char playah = ' ';
		if(player == 1){
			//player is player 1 or 'X'
			playah = p1;
		}
		else if(player == -1){
			//player is p2 or 'O'
			playah = p2;
		}
		//print output
		System.out.println(utility + " " + playah + optMove);
	}
	
	//GOOD
	private static char[][] createGameBoard(File file) throws FileNotFoundException{
		//conditions to check for file integrity
		if(!file.exists()){
			//File doesn't exist
			System.out.println("ERROR: File does not exist.  Please input a proper text file"); //ERROR MSG #1
		} 
		if(!file.isFile()){
			//File is not a "regular" file.  May be a directory.
			System.out.println("ERROR: File is not a proper file format.  Please input a proper text file"); //ERROR MSG #2
		}
		if(!file.canRead()){
			//File can't be read
			System.out.println("ERROR: File cannot be read. Please input a proper text file"); //ERROR MSG #3
		}
		//use a queue to temporarily store the values
		Queue<Character> queue = new LinkedList<Character>();		
		//read in the file and store data 
		Scanner scanner = new Scanner(file);
		//go through each line of file
		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			//make counter for 
			//go through each spot in the line
			char [] numbersInLine = line.toCharArray();
			for(int i=0; i < colVal; i++){
				queue.add(numbersInLine[i]);
			}
		}
		//close scanner
		scanner.close();
		
		/*set up multi-dimensional array data struct. to hold game
		* state.  Need to read file before able to specify size due to varying values of row and col.
		*/
		char [][] gameBoard = new char[rowVal][colVal];
		
		//store the numbers from the queue into the gameBoard 2-D array
		for(int j = 0; j < rowVal; j++){
			for(int k = 0; k < colVal; k++){
				gameBoard[j][k] =  queue.remove();
			}
		}
		return gameBoard;
	}
	
	//GOOD
	//method to get the possible states from a current game state
	private static ArrayList<Integer> getPossibleMoves(char[][] gameBoard){
		//set up ArrayList of potential moves
		ArrayList<Integer> moves = new ArrayList<Integer>();
		//check each column to see if not filled
		for(int i = 0; i < colVal; i++){
			//check by looking at top row of each column for "."
			if(gameBoard[0][i] == empty){
				//column has at least one empty spot. viable move so add to list
				moves.add(i);
			}
		}
		return moves;
	}
	
	//GOOD
	//method to create new game state with desired move
	private static char[][] doMove(int col, char[][] gameState){  
		//create deep copy of original game state
		char[][] gameBoard = new char[rowVal][colVal]; //gameState.clone();
		for(int i = 0; i < rowVal; i++){
			for(int j = 0; j < colVal; j++){
				gameBoard[i][j] = gameState[i][j];
			}
		}
		//on the given column, loop up rows until empty spot found
		for(int i = 3; i >= 0; i--){
			//check for empty spot
			if(gameBoard[i][col] == empty){
				//spot is empty, place move here and return changed board
				//check for whose move it is
				//check if player 1 move
				if(getCurrentPlayer(gameBoard) == 1){
					//place an 'X'
					gameBoard[i][col] = p1 ;
					//return changed board
					return gameBoard;
				} //or check for player 2 move
				else if(getCurrentPlayer(gameBoard) == -1){
					//place an 'O'
					gameBoard[i][col] = p2;
					//return changed board
					return gameBoard;
				}
			}
			//not empty, go up one row 
		}
		//if reaches here column full, can't make move there
		//return original state on fail
		return gameBoard;
	}
	
	//method for min value used as part of MiniMax algorithm. returns min utility value.
	private static int minValue(char[][] gameBoard){
		//check if the state is game solution or game is over
		if(isTerminalState(gameBoard)){
			return getUtility();
		}
		//set up smallest possible max value to start
		int v = Integer.MAX_VALUE;
		//get the successor states
		ArrayList<Integer> possibleOptMoves = getPossibleMoves(gameBoard);
		//loop through possible states and run minValue
		for(int i = 0; i < possibleOptMoves.size(); i++){
			char[][] stateToCheck = doMove(possibleOptMoves.get(i), gameBoard);
			//check for if move did nothing
			if(stateToCheck != gameBoard){
				v = Math.min(v, maxValue(stateToCheck));
			}	
		}
		//return smallest value
		return v;
	}
		
	//method for max value used as part of MiniMax algorithm. returns max utility value.
	private static int maxValue(char[][]gameBoard){
		//check if the state is game solution or game is over
		if(isTerminalState(gameBoard)){
			return getUtility();
		}
		//set up smallest possible max value to start
		int v = Integer.MIN_VALUE;
		//get the successor states
		ArrayList<Integer> possibleOptMoves = getPossibleMoves(gameBoard);
		//loop through possible states and run minValue
		for(int i = 0; i < possibleOptMoves.size(); i++){
			char[][] stateToCheck = doMove(possibleOptMoves.get(i),gameBoard);
			//check for if move did nothing, skip if does so as to not recheck same state
			if(stateToCheck != gameBoard){
				v = Math.max(v, minValue(stateToCheck)); 
			}
		}
		//return highest value
		return v;
	}
		
	/* Pre: the current game state represented by the game board
	 * Post: utility value var is updated, return a int of column # of optimal move for next player
	 * This method is an implementation of the MiniMax algorithm for game theory.
	 */
	private static int miniMax(char[][] gameBoard){
		//get the possible moves from current game state
		ArrayList<Integer> possibleOptMoves = getPossibleMoves(gameBoard);
		//variable to store best move
		int bestMove = -1;
		//return -1 for draw if possibleMoves is empty
		//loop through the possible moves and choose one with maximum value (w/ respect to Player 1/"X")
		for(int i = 0; i < possibleOptMoves.size(); i++){		
			int cp = getCurrentPlayer(gameBoard);
			char[][] stateToCheck = doMove(possibleOptMoves.get(i),gameBoard);
			if(cp == 1){
				int maxVal = minValue(stateToCheck);
				if(maxVal > bestVal){
					bestMove = possibleOptMoves.get(i);  
					bestVal = maxVal;
				}
			}
			else if(cp == -1){
				int minVal = maxValue(stateToCheck);
				if(minVal < bestVal){
					bestMove = possibleOptMoves.get(i);  
					bestVal = minVal;
				}
			}
		}
		return bestMove;
	}
	
	//GOOD
	//this method is used to test if the game state is a terminal state or game is draw
	private static boolean isTerminalState(char[][] gameBoard){
		//check rows first //***BETTER implementation go from bottom row to top 
		for(int i = 0; i < rowVal; i++){
			//int variable to count number of consecutive matching pieces in a row
			int count = 0;
			//char variable to hold recently checked piece
			char val = empty;  
			for(int j = 0; j <= colVal; j++){
				//check for 3 in a row or game over
				if(count == 3){
					//set utility value depending on who won
					//check if player 1
					if(val == p1){
						//p1 won
						utilVal = 1;
					} 
					else if(val == p2){
						//p2 won
						utilVal = -1;
					}
					//return success for terminal state found
					return true;
				}
				
				//System.out.println(gameBoard[i][j]);
				
				if(j !=colVal){
					//check if space is filled
					if(gameBoard[i][j] != empty){
					
						//System.out.println(count);
					
						//check if same as prev. piece
						if(gameBoard[i][j] == val){
						//increment counter
						count++;
						} else {
							//get the new player move in that spot and store in val
							val = gameBoard[i][j];
							//reset count
							count = 1;
						}
					}
					else{
						//set prev checked piece to empty
						val = empty;
						//reset count
						count = 0;
					}
				}
			}
		}
			
		//check columns next
		for(int i = 0; i < colVal; i++){
			//int variable to count number of consecutive matching pieces in a col
			int count = 0;
			//char variable to hold recently checked piece
			char val = empty;  
			for(int j = 0; j <= rowVal; j++){
				//check for 3 in a row or game over
				if(count == 3){
					//set utility value depending on who won
					//check if player 1
					if(val == p1){
						//p1 won
						utilVal = 1;
					} 
					else if(val == p2){
						//p2 won
						utilVal = -1;
					}
					//return success for terminal state found
					return true;
				}
				if(j != rowVal){
					//check if space is filled
					if(gameBoard[j][i] != empty){
						//check if same as prev. piece
						if(gameBoard[j][i] == val){
							//increment counter
							count++;
						} else {
							//get the new player move in that spot and store in val
							val = gameBoard[j][i];
							//reset count
							count = 1;
						}
					} else{
						//set prev checked piece to empty
						val = empty;
						//reset count
						count = 0;
					}
				}
			}
		}
			
		//check for right diagonals - only 6 so check case-by-case
		if((gameBoard[2][0] != empty) && (gameBoard[2][0] == gameBoard[1][1]) && (gameBoard[2][0] == gameBoard[0][2])){
			//solution found
			if(gameBoard[2][0] == p1){
				utilVal = 1;
			} else{
				utilVal = -1; 
			} 
			return true;
		}
		if((gameBoard[3][0] != empty) && (gameBoard[3][0] == gameBoard[2][1]) && (gameBoard[3][0] == gameBoard[1][2])){
			//solution found
			if(gameBoard[3][0] == p1){
				utilVal = 1;
			} else{
				utilVal = -1;
			} 
			return true;
		}
		if((gameBoard[2][1] != empty) && (gameBoard[2][1] == gameBoard[1][2]) && (gameBoard[2][1] == gameBoard[0][3])){
			//solution found
			if(gameBoard[3][0] == p1){
				utilVal = 1;
			} else{
			utilVal = -1; 
			} 
			return true;
		}
		if((gameBoard[3][1] != empty) && (gameBoard[3][1] == gameBoard[2][2]) && (gameBoard[3][1] == gameBoard[1][3])){
			//solution found
			if(gameBoard[3][1] == p1){
				utilVal = 1;
			} else{
				utilVal = -1; 
			} 
			return true;
		}
		if((gameBoard[2][2] != empty) && (gameBoard[2][2] == gameBoard[1][3]) && (gameBoard[2][2] == gameBoard[0][4])){
			//solution found
			if(gameBoard[3][0] == p1){
				utilVal = 1;
			} else{
				utilVal = -1; 
			} 
			return true;
		}
		if((gameBoard[3][2] != empty) && (gameBoard[3][2] == gameBoard[2][3]) && (gameBoard[3][2] == gameBoard[1][4])){
			//solution found
			if(gameBoard[3][0] == p1){
				utilVal = 1;
			} else{
				utilVal = -1; 
			} 
			return true;
		}
			
		//check for left diagonals - again only 6 so case-by-case //BETTER IMPLEMENTATION WITH LOOPS
		if((gameBoard[1][0] != empty) && (gameBoard[1][0] == gameBoard[2][1]) && (gameBoard[1][0] == gameBoard[3][2])){
			//solution found
			if(gameBoard[1][0] == p1){
				utilVal = 1;
			} else{
				utilVal = -1;
			} 
			return true;
		}
		if((gameBoard[0][0] != empty) && (gameBoard[0][0] == gameBoard[1][1]) && (gameBoard[0][0] == gameBoard[2][2])){
			//solution found
			if(gameBoard[1][0] == p1){
				utilVal = 1;
			} else{
				utilVal = -1; 
			} 
			return true;
		}
		if((gameBoard[1][1] != empty) && (gameBoard[1][1] == gameBoard[2][2]) && (gameBoard[1][1] == gameBoard[3][3])){
			//solution found
			if(gameBoard[1][0] == p1){
				utilVal = 1;
			} else{
				utilVal = -1; 
			} 
			return true;
		}
		if((gameBoard[0][1] != empty) && (gameBoard[0][1] == gameBoard[1][2]) && (gameBoard[0][1] == gameBoard[2][3])){
			//solution found
			if(gameBoard[1][0] == p1){
				utilVal = 1;
			} else{
				utilVal = -1;
			} 
			return true;
		}
		if((gameBoard[1][2] != empty) && (gameBoard[1][2] == gameBoard[2][3]) && (gameBoard[1][2] == gameBoard[3][4])){
			//solution found
			if(gameBoard[1][0] == p1){
				utilVal = 1;
			} else{
				utilVal = -1; 
			} 
			return true;
		}
		if((gameBoard[0][2] != empty) && (gameBoard[0][2] == gameBoard[1][3]) && (gameBoard[0][2] == gameBoard[2][4])){
			//solution found
			if(gameBoard[1][0] == p1){
				utilVal = 1;
			} else{
				utilVal = -1;  
			} 
			return true;
		}
		//check all possible spots for three in a row yet didn't find anything so not a terminal state
		return false;
	}
	
	//method used to retrieve the utility value of a certain game state
	private static int getUtility(){
		return utilVal; 
	}
	
	//GOOD
	//method for getting the current player. returns 1 for player one move and -1 for player two move
	private static int getCurrentPlayer(char[][] gameBoard){
		//int variable to track total moves done
		int count = 0;
		//loop through game board tracking moves done so far
		for(int i = 0; i < rowVal; i++){
			for(int j = 0; j < colVal; j++){
				if(gameBoard[i][j] != empty){
					count++;
				}
			}
		}
		//figure out whose turn it is by dividing total turns done by 2 and applying % operator
		int res = count % 2;
		//if res is 0 then player 2 went last so player1 is current player
		if(res == 0){
			//current player = P1
			return 1;
		}  //if res is 1 player 1 went last so player2 is current player
		else if(res == 1){
			//current player = P2
			return -1;
		}
		//never will/should reach here
		return 0;
	}	
	
	//GOOD
	//used for testing purposes only!!!!!!!!!!!!!!!!!!!!
	//method to print out the game State
	public static void printState(char [][] gameState){  ///WHY ARE METHODS STATIC
		//loop through each row
		for(int i =0; i < rowVal; ++i){
			//loop through columns
			for(int j =0; j < colVal; ++j){
				//print out element
				System.out.print(gameState[i][j]);
			}
			System.out.println();
		}
	}
	
}
