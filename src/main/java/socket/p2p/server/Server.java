package socket.p2p.server;

import socket.p2p.constants.Constants;

import java.io.IOException;

/**
 * @author hum
 */
public class Server {
    public static void main(String[] args) {

        TcpServer tcpServer = new TcpServer(Constants.TCP_PORT_SERVER);
        boolean isSucceed = tcpServer.start();
        if (!isSucceed) {
            System.out.println("Tcp server start failed!");
            return;
        }

        ServerProvider.start(Constants.TCP_PORT_SERVER);
        try {

            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerProvider.stop();
        tcpServer.stop();
    }
}
