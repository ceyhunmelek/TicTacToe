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
        ServerSocket server = new ServerSocket(this.PORT);
        System.out.println("Waiting for an opponent...");
        Socket client = server.accept();
        System.out.println("Opponent has joined.");
        PrintWriter toClient = new PrintWriter(client.getOutputStream(), true);
        BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
        while (true) {
            System.out.println(printBoard());
            if(isOver()){
                System.out.println( isHostTurn ? "You Lose" : "You Won");
                break;
            }
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
        Socket socket = new Socket(hostIp, this.PORT);
        System.out.println("Successfully connected.");
        PrintWriter toHost = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader fromHost = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while (true) {
            System.out.println(printBoard());
            if(isOver()){
                System.out.println( isHostTurn ? "You Won" : "You Lose");
                break;
            }
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
            if(validMoveMatcher.matches()){
                if(this.board[move.charAt(0) -49][move.charAt(2)-49] == '\u0000'){
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

    public boolean checkRows(){
        for(int row = 0; row< 3; row++){
            if( this.board[row][0] == this.board[row][1] && this.board[row][1] == this.board[row][2] && this.board[row][0] != '\u0000' ){
                return true;
            }
        }
        return false;
    }

    public boolean checkCols(){
        for(int col = 0; col< 3; col++){
            if( this.board[0][col] == this.board[1][col] && this.board[1][col] == this.board[2][col] && this.board[0][col] != '\u0000' ){
                return true;
            }
        }
        return false;
    }

    public boolean checkDiagonal(){
        if( this.board[0][0] == this.board[1][1] && this.board[1][1] == this.board[2][2] && this.board[0][0] != '\u0000'){
            return true;
        }
        if( this.board[2][0] == this.board[1][1] && this.board[1][1] == this.board[0][2] && this.board[2][0] != '\u0000'){
            return true;
        }
        return false;
    }

    public boolean isOver(){
        return checkCols() || checkCols() || checkDiagonal();
    }

}
