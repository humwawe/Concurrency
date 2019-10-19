package socket.tcp.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author hum
 */
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2000);
        System.out.println("start server");
        System.out.println("server info: address: " + serverSocket.getInetAddress() + " port: " + serverSocket.getLocalPort());

        while (true) {
            Socket client = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(client);
            clientHandler.start();
        }

    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private boolean flag = true;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("client info: address: " + socket.getInetAddress() + " port: " + socket.getPort());
            try {
                PrintStream socketOutput = new PrintStream(socket.getOutputStream());
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                do {
                    String strFromClient = socketInput.readLine();
                    if ("bye".equalsIgnoreCase(strFromClient)) {
                        flag = false;
                        socketOutput.println("bye");
                    } else {
                        System.out.println(strFromClient);
                        socketOutput.println("echo: " + strFromClient.length());
                    }
                } while (flag);

                socketOutput.close();
                socketInput.close();

            } catch (Exception e) {
                System.out.println("exception close!");
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("client exit: address: " + socket.getInetAddress() + " port: " + socket.getPort());
        }
    }
}
