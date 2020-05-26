import java.io.*;
import java.net.*;
import java.util.regex.*;

public class TicTacToe {
    private final int PORT = 9999;
    private Console console = System.console();
    Pattern movePattern = Pattern.compile("^\\[move\\]([123])x([123])"); //find [move]nxm
    Pattern isValidMove = Pattern.compile("^[123]x[123]$"); //find nxm

    public static void main(String[] args) throws IOException {
        TicTacToe game = new TicTacToe();
        String userName = game.console.readLine("Enter your username : ");
        System.out.println("( 1 ) Host a new game");
        System.out.println("( 2 ) Join an existing game");
        System.out.println("Type anything else to exit.");
        String choice = game.console.readLine("Enter your choice : ");
        if (choice.equals("1")) {
            game.createGame();
        } else if (choice.equals("2")) {
            game.joinGame();
        } else {
            System.out.println("See you!");
        }
    }

    public void createGame() throws IOException {
        char[][] board = new char[3][3];
        boolean turn = true;
        String message = "[hostTurn]";
        String currentMove;
        ServerSocket server = new ServerSocket(this.PORT);
        System.out.println("Waiting for an opponent...");
        Socket client = server.accept();
        System.out.println("Opponent has joined.");
        PrintWriter toClient = new PrintWriter(client.getOutputStream(), true);
        BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
        while (true) {
        }
    }

    public void joinGame() throws IOException {
        String hostIp = console.readLine("What is the ip address of host player?");
        String currentMove;
        Socket socket = new Socket(hostIp, this.PORT);
        System.out.println("Successfully connected.");
        PrintWriter toHost = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader fromHost = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while (true) {
        }
    }

    public String printBoard(char[][] board) {
        String boardOutput = "   -------------\n";
        boardOutput += "   | 1 | 2 | 3 |\n";
        for (int row = 0; row < 3; row++) {
            boardOutput += "----------------\n " + (row + 1) + " |";
            for (int col = 0; col < 3; col++) {
                if(board[row][col] == (char) 0){
                    boardOutput += "   |";
                }else{
                    boardOutput += " " + board[row][col] + " |";
                }
            }
            boardOutput += "\n";
        }
        boardOutput += "----------------\n";
        return boardOutput;
    }

    public String getMove(){
        String move = console.readLine("Type your move with rowxcol format (example: 3x1)");
        Matcher validMoveMatcher = isValidMove.matcher(move);
        while (!validMoveMatcher.matches()){
            move = console.readLine("Wrong format! Type your move with rowxcol format (example: 3x1)");
            validMoveMatcher = isValidMove.matcher(move);
        }
        return move;
    }

    public void move(int row,int col, String player){

    }
}
