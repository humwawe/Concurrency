package socket.udp.search.broadcast;

/**
 * @author hum
 */
public class MessageCreator {
    private static final String PORT_CODE = "this is code, response sn to [port]: ";
    private static final String SN_CODE = "receive code, [sn] is:";

    public static String buildWithPort(int port) {
        return PORT_CODE + port;
    }

    public static int parsePort(String data) {
        if (data.startsWith(PORT_CODE)) {
            return Integer.valueOf(data.substring(PORT_CODE.length()));
        }
        return -1;
    }

    public static String buildWithSn(String sn) {
        return SN_CODE + sn;
    }

    public static String parseSN(String data) {
        if (data.startsWith(SN_CODE)) {
            return data.substring(SN_CODE.length());
        }
        return null;
    }
}
