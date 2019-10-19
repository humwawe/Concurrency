package socket.udp.search.broadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.UUID;

/**
 * @author hum
 */
public class Provider {
    public static void main(String[] args) throws IOException {
        String sn = UUID.randomUUID().toString();
        ProviderClient providerClient = new ProviderClient(sn);
        providerClient.start();

        // exit when press any key
        System.in.read();
        providerClient.exit();

    }

    private static class ProviderClient extends Thread {
        private final String sn;
        private boolean done = false;
        private DatagramSocket ds = null;

        public ProviderClient(String sn) {
            super();
            this.sn = sn;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("provider client start");
            try {
                ds = new DatagramSocket(20000);
                while (!done) {
                    final byte[] buf = new byte[512];
                    DatagramPacket receivePack = new DatagramPacket(buf, buf.length);

                    ds.receive(receivePack);
                    InetAddress address = receivePack.getAddress();
                    int port = receivePack.getPort();
                    int dataLen = receivePack.getLength();
                    String data = new String(receivePack.getData(), 0, dataLen);
                    System.out.println("receive from address: " + address + " port: " + port + " data: " + data);

                    int responsePort = MessageCreator.parsePort(data);
                    if (responsePort != -1) {
                        String responseData = MessageCreator.buildWithSn(sn);
                        byte[] responseDataBytes = responseData.getBytes();
                        DatagramPacket responsePacket = new DatagramPacket(responseDataBytes, responseDataBytes.length, address, responsePort);
                        ds.send(responsePacket);
                    }
                }
            } catch (Exception ignore) {
            } finally {
                close();
            }
            System.out.println("provider client end");
        }

        void exit() {
            done = true;
            close();
        }

        private void close() {
            if (ds != null) {
                ds.close();
                ds = null;
            }
        }
    }
}
