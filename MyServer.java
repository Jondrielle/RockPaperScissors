import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class MyServer {
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream reader = null;
    public static ArrayList<ClientHandler> clients = new ArrayList<>();
    public static ArrayList<String> clientSign = new ArrayList<>(2);
    int maxPlayers = 0;



    public MyServer(int port) {
        try {
            server = new ServerSocket(port);
            while (maxPlayers < 2) {
                socket = server.accept();
                ClientHandler handler = new ClientHandler(socket,null);
                clients.add(handler);
                maxPlayers++;
                handler.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ClientHandler extends Thread {
        private static Socket socket;
        private BufferedReader reader;
        private DataOutputStream output;
        private PrintWriter out;
        private String userName;
        private DataInputStream input;
        private int index;


        public ClientHandler(Socket socket,String userName) {
            this.socket = socket;
            this.userName = null;
        }

        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                output = new DataOutputStream(socket.getOutputStream());
                input = new DataInputStream(socket.getInputStream());
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line = "";
                int counter = 0;
                while (!line.equals("Bye")) {
                    line = input.readUTF();
                    if (userName == null) {
                        setUserName(line);
                        userName = line;
                        String greeting = ("Welcome: " + userName);
                        System.out.println(greeting);
                        broadcastMessage(greeting);
                    }
                    else {
                            while (counter != 1) {
                                String toAllMessage = userName + ": " + line;
                                clientSign.add(toAllMessage);
                                System.out.println(toAllMessage);
                                counter++;
                            }
                            try {
                                winner(clientSign);
                            } catch (Exception e) {
                                System.out.println();
                            }

                    }
                }
                System.out.println(userName + " disconnected with a Bye message");
                broadcastMessage(userName + " disconnected with a Bye message");
                System.out.println("Server: Goodbye " + userName);
                broadcastMessage("Server: Goodbye " + userName);
                clients.remove(this);
                socket.close();
                input.close();
            } catch (IOException e) {
                System.out.println(e.getStackTrace());
            }

        }

        private void winner(ArrayList<String> clientSign) throws Exception{
            String player1 = clientSign.get(0);
            String player2 = clientSign.get(1);
            String message = "";
            System.out.println(player1);
            System.out.println(player2);

            if(player1.contains("rock")) {
                if(player2.contains("paper")) {
                    message = "Player " + clients.get(1).getUserName() + " wins!!";
                    broadcastMessage(player1 + "\n" + player2);
                    broadcastMessage(message);
                }
                else if (player2.contains("scissors")){
                    message = "Player "  + clients.get(0).getUserName() + " wins!!";
                    broadcastMessage(player1 + "\n" + player2);
                    broadcastMessage(message);
                }
                else{
                    message = "Draw";
                    broadcastMessage(player1 + "\n" + player2);
                    broadcastMessage(message);
                }
            }
            if(player1.contains("paper")){
                if(player2.contains("rock")) {
                    message = "Player " + clients.get(0).getUserName() + " wins!!";
                    broadcastMessage(player1 + "\n" + player2);
                    broadcastMessage(message);
                }
                else if (player2.contains("scissors")){
                    message = "Player "  + clients.get(1).getUserName() + " wins!!";
                    broadcastMessage(player1 + "\n" + player2);
                    broadcastMessage(message);
                }
                else{
                    broadcastMessage(player1 + "\n" + player2);
                    message = "Draw";
                    broadcastMessage(message);
                }
            }
            if (player1.contains("scissors")){
                if(player2.contains("paper")) {
                    message = "Player " + clients.get(0).getUserName() + " wins!!";
                    broadcastMessage(player1 + "\n" + player2);
                    broadcastMessage(message);
                }
                else if (player2.contains("rock")){
                    message = "Player " + clients.get(1).getUserName() + " wins!!";
                    broadcastMessage(player1 + "\n" + player2);
                    broadcastMessage(message);
                }
                else{
                    broadcastMessage(player1 + "\n" + player2);
                    message = "Draw";
                    broadcastMessage(message);
                }
            }
        }

        private void broadcastMessage(String toAllMessage) throws IOException {
            for (int i = 0; i < clients.size(); i++) {
                (clients.get(i)).out.println(toAllMessage);
            }
        }

        public void printThreadList() throws IOException {
            for (int i = 0; i < clients.size(); i++) {
                int j = i + 1;
                String clientThreadName = j + ") " + (clients.get(i)).getUserName();
                System.out.println(clientThreadName);
                broadcastMessage(clientThreadName);
            }
        }


        public void setUserName(String clientUserName){
            this.userName = clientUserName;
        }
        public String getUserName(){
            return userName;
        }
    }

    public static void main(String[] args) throws IOException {
        MyServer server = new MyServer(4000);
    }
}




