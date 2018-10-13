
import java.util.*;

public class Minimax {


    public static int totalDepth;
    public static int lowerThreshold = 5;

    public static int [] scores_O;
    public static int [] scores_X;

    public static int[][] diagonal_index = {{0, 5, 10, 15}, {3, 6, 9, 12}};
    public static int[][] rows_index = {{0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}};
    public static int[][] colum_index = {{0, 4, 8, 12}, {1, 5, 9, 13}, {2, 6, 10, 14}, {3, 7, 11, 15}};



    public static void update_scores_OsAndXs(int scoreRow, int player){

        if ((player == 2 )&& (scoreRow < 5 ))
            scores_O[scoreRow]++;

        if ((player == 1 )&& (scoreRow < 5 ))
            scores_X[scoreRow]++;
    }

    public static void diags(final GameState gameState, int player)
    {
        int diagScore = 0;
        int other = getOtherPlayer(player);
        //  Scores for Diagonals
        for(int d = 0; d < diagonal_index.length; d++){
            for(int index = 0; index < diagonal_index[0].length; index++){
                if(gameState.at(diagonal_index[d][index]) == other){
                    diagScore = 0;
                    break;
                }
                if(gameState.at(diagonal_index[d][index]) == player){
                    diagScore++;
                }
            }
            update_scores_OsAndXs(diagScore, player);
        }
    }

    public static void rows(final GameState gameState, int player)
    {
        // Scores for rows
        int rowScore = 0;
        int other = getOtherPlayer(player);
        for(int r = 0; r < rows_index.length; r++){
            for(int index = 0; index < rows_index[0].length; index++){
                if(gameState.at(rows_index[r][index]) == other){
                    rowScore = 0;
                    break;
                }
                if(gameState.at(rows_index[r][index]) == player){
                    rowScore++;
                }
            }

            update_scores_OsAndXs(rowScore, player);
        }

    }

    public static void cols(final GameState gameState, int player)
    {
        // Scores for columns 
        int colScore = 0;
        int other = getOtherPlayer(player);
        for(int r = 0; r < colum_index.length; r++){
            for(int index = 0; index < colum_index[0].length; index++){
                if(gameState.at(colum_index[r][index]) == other){
                    colScore = 0;
                    break;
                }
                if(gameState.at(colum_index[r][index]) == player){
                    colScore++;
                }
            }

            update_scores_OsAndXs(colScore, player);
        }
    }

    private static int calculate_depth(final GameState gameState){
        int depth = 0;
        for(int index = 0; index < gameState.CELL_COUNT; index++){
            if(gameState.at(index) == 0)
                depth++;
        }
        return depth;
    }


    public static int getOtherPlayer(int player)
    {
        // Alternate the player roll for each run
        // of the game MIN get MAX
        // MAX get MIN

        int otherplayer = (player == 1? 2 : 1);

        return otherplayer;

    }

    public static void reinitialize_utility_functions()
    {
        // Renitialize score for each player
        scores_O = new int[5];
        scores_X = new int[5];
        for(int i = 1; i < scores_X.length; i++)
        {
            scores_X[i] = 0;
            scores_O[i] = 0;
        }
    }

    public static int eval(final GameState gameState, int depth)
    {

        // Reinitialize score for each player
        reinitialize_utility_functions();

        diags(gameState, Constants.CELL_X);
        rows(gameState, Constants.CELL_X);
        cols(gameState, Constants.CELL_X);

        diags(gameState, Constants.CELL_O);
        rows(gameState, Constants.CELL_O);
        cols(gameState, Constants.CELL_O);

        //int score = (100 * x4 + 10 * x3 + 3 * x2 + x1) - (100 * o4 + 10 * o3 + 3 * o2 + o1);
        int X_scores = 100 * scores_X[4] + 10 * scores_X[3] + 3 * scores_X[2] + scores_X[1];
        int O_scores = 100 * scores_O[4] + 10 * scores_O[3] + 3 * scores_O[2] + scores_O[1];

        int difference = X_scores - O_scores;


        difference += depth;

        return difference;
    }


    /* Initialization of alpha-beta search */
    public static GameState alphabeta(GameState gameState)
    {
        // Create a new vector of type Gamestate
        Vector<GameState> nextStates = new Vector<GameState>();
        // Store all possible moves in the Gamestate object
        gameState.findPossibleMoves(nextStates);

        int depth = calculate_depth(gameState);
        totalDepth = depth;
        int maxdepth = (depth <= lowerThreshold ? 0 : (int) ((double)depth * 0.75));

        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        int player = gameState.getNextPlayer();
        int bestVal = Integer.MIN_VALUE;
        int current = Integer.MIN_VALUE;

        int bestGameState = -1;
        for(int i = 0; i < nextStates.size(); i++){
            current = alphabeta(nextStates.elementAt(i), depth-1, alpha, beta, getOtherPlayer(player), maxdepth);

            if(current > bestVal){
                bestVal = current;
                bestGameState = i;
            }
        }

        return nextStates.elementAt(bestGameState);

    }

    /* MINIMAX with optimization Alpha-beta pruning */
    public static int alphabeta(GameState gameState, int depth, int alpha, int beta, int player, int maxdepth){


        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);

        int v;

        if(depth == maxdepth || nextStates.size() == 0)
        {
            // No more possible move available or
            // max_depth is reached
            v = eval(gameState, depth);

        }
        else if(player == Constants.CELL_X)
        {
            // Evaluate Max's player position
            v = Integer.MIN_VALUE;
            for(int i = 0; i < nextStates.size(); i++){
                v = Math.max(v, alphabeta(nextStates.elementAt(i), depth-1, alpha, beta, Constants.CELL_O, (depth <= lowerThreshold? 0 : maxdepth)));
                alpha = Math.max(v, alpha);
                if(alpha >= beta){
                    // Alpha pruning
                    break;
                }
            }
        }
        else
        {
            // Evaluate Min's player position
            v = Integer.MAX_VALUE;
            for(int i = 0; i < nextStates.size(); i++)
            {
                v = Math.min(v, alphabeta(nextStates.elementAt(i), depth-1, alpha, beta, Constants.CELL_X, (depth <= lowerThreshold? 0 : maxdepth)));
                beta = Math.min(v, beta);
                if(alpha >= beta)
                {
                    // Beta pruning
                    break;
                }
            }
        }

        return v;
    }


}