package socket.p2p.client;

import socket.p2p.bean.ServerInfo;
import socket.p2p.util.CloseUtils;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

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
            ReadHandler readHandler = new ReadHandler(socket.getInputStream());
            readHandler.start();

            write(socket);

            readHandler.exit();
        } catch (Exception e) {
            System.out.println("exception close!");
        }

        socket.close();
        System.out.println("client exit");

    }

    private static void write(Socket client) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        PrintStream socketPrintStream = new PrintStream(client.getOutputStream());

        do {
            String str = input.readLine();
            socketPrintStream.println(str);
            if ("00bye00".equalsIgnoreCase(str)) {
                break;
            }
        } while (true);

        socketPrintStream.close();
    }

    static class ReadHandler extends Thread {
        private boolean done = false;
        private final InputStream inputStream;

        ReadHandler(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            super.run();
            try {
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(inputStream));
                do {
                    String str;
                    try {
                        str = socketInput.readLine();
                    } catch (SocketTimeoutException e) {
                        continue;
                    }
                    if (str == null) {
                        System.out.println("can't read message!");
                        break;
                    }
                    System.out.println(str);
                } while (!done);
            } catch (Exception e) {
                if (!done) {
                    System.out.println("exception close!" + e.getMessage());
                }
            } finally {
                CloseUtils.close(inputStream);
            }
        }

        void exit() {
            done = true;
            CloseUtils.close(inputStream);
        }
    }
}
