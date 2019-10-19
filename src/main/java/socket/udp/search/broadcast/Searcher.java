package socket.udp.search.broadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author hum
 */
public class Searcher {
    private static final int LISTEN_PORT = 30000;

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("searcher start");

        Listener listener = listen();
        sendBroadcast();

        // exit when press any key
        System.in.read();
        List<Client> clients = listener.getClientsAndClose();
        System.out.println("clients: " + clients);

        System.out.println("searcher end");
    }

    private static Listener listen() throws InterruptedException {
        System.out.println("searcher listener start");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Listener listener = new Listener(LISTEN_PORT, countDownLatch);
        listener.start();
        countDownLatch.await();
        return listener;
    }

    private static void sendBroadcast() throws IOException {
        System.out.println("searcher send broadcast start");
        DatagramSocket ds = new DatagramSocket();

        // tell provider client response to 30000
        String requestData = MessageCreator.buildWithPort(LISTEN_PORT);
        byte[] requestDataBytes = requestData.getBytes();
        DatagramPacket requestPacket = new DatagramPacket(requestDataBytes, requestDataBytes.length);

        // broadcast message to 20000
        requestPacket.setAddress(InetAddress.getByName("255.255.255.255"));
        requestPacket.setPort(20000);

        ds.send(requestPacket);
        ds.close();
        System.out.println("searcher send broadcast end");
    }

    private static class Listener extends Thread {
        private final int listenPort;
        private final CountDownLatch countDownLatch;
        private final List<Client> clients = new ArrayList<>();
        private boolean done = false;
        private DatagramSocket ds = null;

        public Listener(int listenPort, CountDownLatch countDownLatch) {
            super();
            this.listenPort = listenPort;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            super.run();
            countDownLatch.countDown();
            try {
                ds = new DatagramSocket(listenPort);
                while (!done) {
                    final byte[] buf = new byte[512];
                    DatagramPacket receivePack = new DatagramPacket(buf, buf.length);
                    ds.receive(receivePack);

                    // use ip: address.getHostAddress()
                    InetAddress address = receivePack.getAddress();
                    int port = receivePack.getPort();
                    int dataLen = receivePack.getLength();
                    String data = new String(receivePack.getData(), 0, dataLen);
                    System.out.println("searcher receive from address: " + address + " port: " + port + " data: " + data);

                    String sn = MessageCreator.parseSN(data);
                    if (sn != null) {
                        Client client = new Client(port, address, sn);
                        clients.add(client);
                    }
                }
            } catch (Exception ignore) {

            } finally {
                close();
            }
            System.out.println("searcher listener end");
        }

        public List<Client> getClientsAndClose() {
            done = true;
            close();
            return clients;
        }

        private void close() {
            if (ds != null) {
                ds.close();
                ds = null;
            }
        }
    }

    private static class Client {
        final int port;

        final InetAddress address;
        final String sn;

        public Client(int port, InetAddress address, String sn) {
            this.port = port;
            this.address = address;
            this.sn = sn;
        }

        @Override
        public String toString() {
            return "Client{" +
                    "port=" + port +
                    ", address='" + address + '\'' +
                    ", sn='" + sn + '\'' +
                    '}';
        }
    }


}
