import java.net.*;
import java.io.*;
public class MyClient extends Thread {

    public MyClient(String address, int port) throws IOException {
        try {
            Socket socket = new Socket(address, port);
            new Reader(socket, this).start();
            new Writer(socket, this).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class Writer extends Thread {
        private Socket socket;
        private MyClient client;
        private DataInputStream input;
        private DataInputStream inputFromServer;
        private DataOutputStream out;
        private PrintWriter writer;

        Writer(Socket socket, MyClient client) {
            this.socket = socket;
            this.client = client;
        }

        public void run() {
            try {
                String line = " ";
                inputFromServer = new DataInputStream(socket.getInputStream());
                writer = new PrintWriter(socket.getOutputStream(), true);
                input = new DataInputStream(System.in);
                out = new DataOutputStream(socket.getOutputStream());
                System.out.println("Let's play rock paper scissors");
                System.out.println("Enter rock, paper or scissors");
                System.out.println("Enter Username");
                while (!line.equalsIgnoreCase("Bye")) {
                    try {
                        line = input.readLine();
                        out.writeUTF(line);
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
                try {
                    input.close();
                    out.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    class Reader extends Thread{
        private Socket socket;
        private String lineFromServer = null;
        private BufferedReader reader;
        private PrintWriter outputToClient = null;
        private MyClient client;
        private DataInputStream inputFromServer = null;
        private DataOutputStream out;

        public Reader(Socket socket,MyClient client) {
            this.socket = socket;
            this.client = client;
        }
        public void run(){
            try {
                inputFromServer = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outputToClient = new PrintWriter(socket.getOutputStream(), true);
                while (true) {
                    try {
                        lineFromServer = inputFromServer.readLine();
                        System.out.println(lineFromServer);

                    } catch (IOException e) {
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        MyClient client = new MyClient("localhost",4000);
    }
}



