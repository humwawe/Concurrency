package socket.p2p.client;

import socket.p2p.bean.ServerInfo;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author hum
 */
public class TcpClient {
    public static void linkWith(ServerInfo info) throws IOException {
        Socket socket = new Socket();
        socket.setSoTimeout(3000);

        socket.connect(new InetSocketAddress(Inet4Address.getByName(info.getAddress()), info.getPort()), 3000);

        System.out.println("connect start");
        System.out.println("client info：" + socket.getLocalAddress() + " port:" + socket.getLocalPort());
        System.out.println("server info：" + socket.getInetAddress() + " port:" + socket.getPort());

        try {
            doSomething(socket);
        } catch (Exception e) {
            System.out.println("exception close!");
        }

        socket.close();
        System.out.println("client exit");

    }

    private static void doSomething(Socket client) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        PrintStream socketPrintStream = new PrintStream(client.getOutputStream());
        BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

        boolean flag = true;
        do {
            String str = input.readLine();
            socketPrintStream.println(str);
            String echo = socketBufferedReader.readLine();
            if ("Bye".equalsIgnoreCase(echo)) {
                flag = false;
            } else {
                System.out.println(echo);
            }
        } while (flag);

        socketPrintStream.close();
        socketBufferedReader.close();
    }
}
