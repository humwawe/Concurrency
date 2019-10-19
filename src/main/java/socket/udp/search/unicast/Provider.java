package socket.udp.search.unicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author hum
 */
public class Provider {
    public static void main(String[] args) throws IOException {
        System.out.println("provider start");

        DatagramSocket ds = new DatagramSocket(20000);

        final byte[] buf = new byte[512];
        DatagramPacket receivePack = new DatagramPacket(buf, buf.length);

        ds.receive(receivePack);
        InetAddress address = receivePack.getAddress();
        int port = receivePack.getPort();
        int dataLen = receivePack.getLength();
        String data = new String(receivePack.getData(), 0, dataLen);
        System.out.println("receive from address: " + address + " port: " + port + " data: " + data);

        String responseData = "provider receive data with len: " + dataLen;
        byte[] responseDataBytes = responseData.getBytes();
        DatagramPacket responsePacket = new DatagramPacket(responseDataBytes, responseDataBytes.length, address, port);

        ds.send(responsePacket);

        System.out.println("provider end");
        ds.close();
    }
}
