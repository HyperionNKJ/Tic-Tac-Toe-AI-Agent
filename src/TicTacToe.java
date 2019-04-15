import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// #### denotes configurations
public class TicTacToe {
    private static int[][] state = new int[3][3];

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        init(state);
        print(state);
        System.out.println("Game begins. Choose your move...");
        while (!terminalTest(state)) {
            String rawMove = s.nextLine();
            Move move;
            try {
                move = new Move(rawMove, state);
            } catch (Exception e) {
                System.out.println("Invalid move. Choose your move...");
                continue;
            }
            state = result(state, move, 0); // Human move = 0
            print(state);
            if (!terminalTest(state)) {
//                Move bestMove = minimaxDecision(state);         // #### NORMAL MINIMAX
                Move bestMove = hMinimaxDecision(state, 5); // #### DEPTH LIMITED HEURISTIC MINIMAX
                System.out.println("AI chooses...");
                state = result(state, bestMove, 1); // AI move = 1
                print(state);
            }
        }
        switch (determineWinner(state)) {
        case 1: System.out.println("Unfortunately. You lost to Kaijun's AI!"); break;
        case -1: System.out.println("It is a draw."); break;
        case 0: System.out.println("Congratulations! You have beat the AI"); break;
        }
    }

//======================================= Heuristic Minimax ==============================================//

    private static Move hMinimaxDecision(int[][] state, int depth) {
        List<Move> validMoves = getValidMoves(state);
        Move bestMove = validMoves.get(0); // assume first valid move to be best move
        for (Move move : validMoves) {
            move.setUtility(hMinValue(result(state, move, 1), 0, depth)); // AI starts next
            if (move.getUtility() > bestMove.getUtility()) {
                bestMove = move;
            }
        }
        return bestMove;
    }

    private static int hMaxValue(int[][] state, int depth, int limit) {
        if (depth == limit) {
            return evaluateHeuristic(state);
        }
        int utility = -2;
        for (Move move : getValidMoves(state)) {
            utility = Math.max(utility, hMinValue(result(state, move, 1), depth + 1, limit));
        }
        return utility;
    }

    private static int hMinValue(int[][] state, int depth, int limit) {
        if (depth == limit) {
            return evaluateHeuristic(state);
        }
        int utility = 2;
        for (Move move : getValidMoves(state)) {
            utility = Math.min(utility, hMaxValue(result(state, move, 0), depth + 1, limit));
        }
        return utility;
    }

    private static int evaluateHeuristic(int[][] state) {
//        return numOfWinningLines(state, 0) - numOfWinningLines(state, 1);
        return computeScore(state);   // #### UNCOMMENT TO CHANGE HEURISTIC
    }

//======================================= Score Heuristics ==============================================//

    private static int computeScore(int[][] state) {
        int score = 0;
        score += evaluateLine(state, 0,0, 0,1, 0,2);
        score += evaluateLine(state, 1,0, 1,1, 1,2);
        score += evaluateLine(state, 2,0, 2,1, 2,2);
        score += evaluateLine(state, 0,0, 1,0, 2,0);
        score += evaluateLine(state, 0,1, 1,1, 2,1);
        score += evaluateLine(state, 0,2, 1,2, 2,2);
        score += evaluateLine(state, 0,0, 1,1, 2,2);
        score += evaluateLine(state, 0,2, 1,1, 2,0);
        return score;
    }

    private static int evaluateLine(int[][] state, int i, int i1, int i2, int i3, int i4, int i5) {
        if (state[i][i1] == 1 && state[i2][i3] == 1 && state[i4][i5] == 1) {
            return 100;
        } else if (state[i][i1] == 1 && state[i2][i3] == 1 && state[i4][i5] == -1) {
            return 10;
        } else if (state[i][i1] == 1 && state[i2][i3] == -1 && state[i4][i5] == 1) {
            return 10;
        } else if (state[i][i1] == -1 && state[i2][i3] == 1 && state[i4][i5] == 1) {
            return 10;
        } else if (state[i][i1] == 1 && state[i2][i3] == -1 && state[i4][i5] == -1) {
            return 1;
        } else if (state[i][i1] == -1 && state[i2][i3] == 1 && state[i4][i5] == -1) {
            return 1;
        } else if (state[i][i1] == -1 && state[i2][i3] == -1 && state[i4][i5] == 1) {
            return 1;
        } else {
            return 0;
        }
    }

//======================================= NumOfWinningLines Heuristics ==============================================//

    private static int numOfWinningLines(int[][] state, int opponent) {
        int numOfWinningLines = 0;
        if (state[0][0] != opponent && state[0][1] != opponent && state[0][2] != opponent) {
            numOfWinningLines++;
        }
        if (state[1][0] != opponent && state[1][1] != opponent && state[1][2] != opponent) {
            numOfWinningLines++;
        }
        if (state[2][0] != opponent && state[2][1] != opponent && state[2][2] != opponent) {
            numOfWinningLines++;
        }
        if (state[0][0] != opponent && state[1][0] != opponent && state[2][0] != opponent) {
            numOfWinningLines++;
        }
        if (state[0][1] != opponent && state[1][1] != opponent && state[2][1] != opponent) {
            numOfWinningLines++;
        }
        if (state[0][2] != opponent && state[1][2] != opponent && state[2][2] != opponent) {
            numOfWinningLines++;
        }
        if (state[0][2] != opponent && state[1][1] != opponent && state[2][0] != opponent) {
            numOfWinningLines++;
        }
        if (state[0][0] != opponent && state[1][1] != opponent && state[2][2] != opponent) {
            numOfWinningLines++;
        }
        return numOfWinningLines;
    }

//======================================= Actual Minimax ==============================================//

    private static Move minimaxDecision(int[][] state) {
        List<Move> validMoves = getValidMoves(state);
        Move bestMove = validMoves.get(0); // assume first valid move to be best move
        for (Move move : validMoves) {
            move.setUtility(minValue(result(state, move, 1))); // AI starts next
            if (move.getUtility() > bestMove.getUtility()) {
                bestMove = move;
            }
        }
        return bestMove;
    }

    private static int maxValue(int[][] state) {
        if (terminalTest(state)) {
            return determineWinner(state);
        }
        int utility = -2;
        for (Move move : getValidMoves(state)) {
            utility = Math.max(utility, minValue(result(state, move, 1)));
        }
        return utility;
    }

    private static int minValue(int[][] state) {
        if (terminalTest(state)) {
            return determineWinner(state);
        }
        int utility = 2;
        for (Move move : getValidMoves(state)) {
            utility = Math.min(utility, maxValue(result(state, move, 0)));
        }
        return utility;
    }

//======================================= Helper methods ==============================================//

    private static List<Move> getValidMoves(int[][] state) {
        List<Move> validMoves = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == -1) {
                    validMoves.add(new Move(i, j));
                }
            }
        }
        return validMoves;
    }

    private static int[][] result(int[][] state, Move move, int player) {
        int[][] newState = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newState[i][j] = state[i][j];
            }
        }
        newState[move.getRow()][move.getColumn()] = player;
        return newState;
    }

    private static void init(int[][] state) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                state[i][j] = -1;
            }
        }
    }

    private static boolean terminalTest(int[][] state) {
        int winner = determineWinner(state);
        return winner != -1 || boardIsFull(state);
    }

    private static boolean boardIsFull(int[][] state) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == -1) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int determineWinner(int[][] state) {
        if (state[0][0] != -1 && state[0][0] == state[0][1] && state[0][1] == state[0][2]) {
            return state[0][0];
        } else if (state[1][0] != -1 && state[1][0] == state[1][1] && state[1][1] == state[1][2]) {
            return state[1][0];
        } else if (state[2][0] != -1 && state[2][0] == state[2][1] && state[2][1] == state[2][2]) {
            return state[2][0];
        } else if (state[0][0] != -1 && state[0][0] == state[1][0] && state[1][0] == state[2][0]) {
            return state[0][0];
        } else if (state[0][1] != -1 && state[0][1] == state[1][1] && state[1][1] == state[2][1]) {
            return state[0][1];
        } else if (state[0][2] != -1 && state[0][2] == state[1][2] && state[1][2] == state[2][2]) {
            return state[0][2];
        } else if (state[0][0] != -1 && state[0][0] == state[1][1] && state[1][1] == state[2][2]) {
            return state[0][0];
        } else if (state[0][2] != -1 && state[0][2] == state[1][1] && state[1][1] == state[2][0]) {
            return state[0][2];
        } else {
            return -1; // -1 means no winner, 1 means AI won, 0 means Human won
        }
    }

    // method to print state
    private static void print(int[][] state) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int value = state[i][j];
                if (value == -1) {
                    System.out.print("_");
                } else {
                    System.out.print(value);
                }
                System.out.print(" ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

class Move {
    private int row;
    private int column;
    private int utility;

    Move(String move, int[][] state) {
        String[] s = move.split(",");
        int s_row = Integer.parseInt(s[0]);
        int s_column = Integer.parseInt(s[1]);
        if (s_row > 2 || s_row < 0 || s_column > 2 || s_column < 0 || state[s_row][s_column] != -1) {
            throw new IllegalArgumentException();
        }
        row = s_row;
        column = s_column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getUtility() {
        return utility;
    }

    public void setUtility(int utility) {
        this.utility = utility;
    }

    Move(int row, int column) {
        this.row = row;
        this.column = column;
    }
}