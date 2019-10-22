package socket.p2p.server.handler;

/**
 * @author hum
 */
public interface CloseNotify {
    void onSelfClosed(ClientHandler handler);
}
