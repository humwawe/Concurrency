package socket.udp.search.unicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author hum
 */
public class Searcher {
    public static void main(String[] args) throws IOException {
        System.out.println("searcher start");

        DatagramSocket ds = new DatagramSocket();

        String requestData = "hello";
        byte[] requestDataBytes = requestData.getBytes();
        DatagramPacket requestPacket = new DatagramPacket(requestDataBytes, requestDataBytes.length);
        requestPacket.setAddress(InetAddress.getLocalHost());
        requestPacket.setPort(20000);

        ds.send(requestPacket);

        final byte[] buf = new byte[512];
        DatagramPacket receivePack = new DatagramPacket(buf, buf.length);

        ds.receive(receivePack);
        InetAddress address = receivePack.getAddress();
        int port = receivePack.getPort();
        int dataLen = receivePack.getLength();
        String data = new String(receivePack.getData(), 0, dataLen);
        System.out.println("searcher receive from address: " + address + " port: " + port + " data: " + data);


        System.out.println("searcher end");
        ds.close();
    }
}
