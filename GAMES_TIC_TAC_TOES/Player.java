import java.util.*;

public class Player {

	 public static void printGameState(final GameState gameState){

	  	for(int row = 0; row < gameState.BOARD_SIZE; row++){
		  	for(int col = 0; col < gameState.BOARD_SIZE; col++){
		  		System.err.print(gameState.at(row, col) + " ");
	  		}
			System.err.println();
		} 
	 	
	 	System.err.println();
	 
	 }

    
    /**
     * Performs a move
     *
     * @param gameState
     *            the current state of the board
     * @param deadline
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */    
    public GameState play(final GameState gameState, final Deadline deadline) {
    
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);

//	Your assignment consists of writing a program that plays tic-tac-toe as player X. --> this function returns
// some approximation of the best move for _player x_


			
        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
         	return new GameState(gameState, new Move());
        } else {
        		return Minimax.alphabeta(gameState);
        }

       
//	     Due to time constraints, one cannot analyse the com-
//plete game tree. Therefore the student should investigate the influence of the maximum search
//depth and come up with a suitable evaluation function.
	     
       
    }
}
