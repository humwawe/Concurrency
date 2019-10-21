package socket.p2p.client;

import socket.p2p.bean.ServerInfo;

import java.io.IOException;

/**
 * @author hum
 */
public class Client {
    public static void main(String[] args) {
        ServerInfo info = ClientSearcher.searchServer(10000);
        System.out.println("Server:" + info);

        if (info != null) {
            try {
                TcpClient.linkWith(info);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
