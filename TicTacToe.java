import java.io.*;
import java.net.*;
import java.util.regex.*;

public class TicTacToe {
    private final int PORT = 9999;
    private Console console = System.console();
    private String userName;
    private boolean isHostTurn = true;
    char[][] board = {
            {'\u0000', '\u0000', '\u0000'},
            {'\u0000', '\u0000', '\u0000'},
            {'\u0000', '\u0000', '\u0000'}
    };

    private Pattern isValidMove = Pattern.compile("^[123]x[123]$"); //find nxm

    public static void main(String[] args) throws IOException {
        TicTacToe game = new TicTacToe();
        game.userName = game.console.readLine("Enter your username : ");
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
            System.out.println(printBoard());
            if (isHostTurn) {
                getMove();
                toClient.println(this.cBoardtoString());
            } else {
                System.out.println("Waiting opponent to move");
                this.board = stringToCboard(fromClient.readLine());
            }
            isHostTurn = !isHostTurn;
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
            System.out.println(printBoard());
            if (!isHostTurn) {
                getMove();
                toHost.println(this.cBoardtoString());
            } else {
                System.out.println("Waiting opponent to move");
                this.board = stringToCboard(fromHost.readLine());
            }
            isHostTurn = !isHostTurn;
        }
    }

    public String printBoard() {
        String boardOutput = "   -------------\n";
        boardOutput += "   | 1 | 2 | 3 |\n";
        for (int row = 0; row < 3; row++) {
            boardOutput += "----------------\n " + (row + 1) + " |";
            for (int col = 0; col < 3; col++) {
                if (board[row][col] == '\u0000') {
                    boardOutput += "   |";
                } else {
                    boardOutput += " " + String.valueOf(board[row][col]) + " |";
                }
            }
            boardOutput += "\n";
        }
        boardOutput += "----------------\n";
        return boardOutput;
    }

    public void getMove() {
        String move = console.readLine("Type your move with rowXcol format (example: 3x1)");
        Matcher validMoveMatcher = this.isValidMove.matcher(move);
        while( true ){
            if(this.board[move.charAt(0) -49][move.charAt(2)-49] == '\u0000'){
                if(validMoveMatcher.matches()){
                    break;
                }
            }
            move = console.readLine("Wrong format or field is already occupied. (example: 3x1)");
            validMoveMatcher = isValidMove.matcher(move);
        };
        this.move(move.charAt(0) - 48,move.charAt(2) - 48);
    }

    public void move(int row, int col) {
        board[row - 1][col - 1] = isHostTurn ? 'X' : 'O';
    }

    public String cBoardtoString() {
        return new StringBuilder().append(this.board[0]).append(this.board[1]).append(this.board[2]).toString();
    }

    public char[][] stringToCboard(String text) {
        return new char[][]{
                {text.charAt(0), text.charAt(1), text.charAt(2)},
                {text.charAt(3), text.charAt(4), text.charAt(5)},
                {text.charAt(6), text.charAt(7), text.charAt(8)}
        };
    }
}
