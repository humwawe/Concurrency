package socket.tcp.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;

/**
 * @author hum
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.setSoTimeout(3000);
        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), 2000), 3000);

        System.out.println("client start");
        System.out.println("client info: address: " + socket.getLocalAddress() + " port: " + socket.getLocalPort());
        System.out.println("server info: address: " + socket.getInetAddress() + " port: " + socket.getPort());

        try {
            doSomething(socket);
        } catch (Exception e) {
            System.out.println("exception close!");
        }
        socket.close();
        System.out.println("client end");
    }

    private static void doSomething(Socket client) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        PrintStream socketOutput = new PrintStream(client.getOutputStream());
        BufferedReader socketInput = new BufferedReader(new InputStreamReader(client.getInputStream()));

        boolean flag = true;
        do {
            String sendStr = input.readLine();
            socketOutput.println(sendStr);
            String strFromServer = socketInput.readLine();

            if ("bye".equalsIgnoreCase(strFromServer)) {
                flag = false;
            } else {
                System.out.println(strFromServer);
            }
        } while (flag);

        socketOutput.close();
        socketInput.close();

    }
}
