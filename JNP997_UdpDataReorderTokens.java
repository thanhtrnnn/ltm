import java.util.*;
import java.net.*;

public class JNP997_UdpDataReorderTokens {
    private static final String HOST = "36.50.135.242";
    private static final int PORT = 2207;
    private static final String CREDS = ";B23DCAT280;XXXXXXXX";

    // requestId;tok:idx,tok:idx,...  ->  requestId;tok1,tok2,... (sắp theo idx)
    public static String solve(String s) {
        int k = s.indexOf(';');
        TreeMap<Integer, String> mp = new TreeMap<>();
        for (String x : s.substring(k + 1).split(",")) {
            int c = x.lastIndexOf(':');
            mp.put(Integer.parseInt(x.substring(c + 1).trim()), x.substring(0, c).trim());
        }
        return s.substring(0, k) + ";" + String.join(",", mp.values());
    }

    public static void main(String[] args) throws Exception {
        DatagramSocket client = new DatagramSocket();
        byte[] s, r;
        DatagramPacket sp, rp;
        s = CREDS.getBytes();
        sp = new DatagramPacket(s, s.length, InetAddress.getByName(HOST), PORT);
        client.send(sp);
        r = new byte[10001];
        rp = new DatagramPacket(r, r.length);
        client.receive(rp);
        String res = solve(new String(rp.getData(), 0, rp.getLength()).trim());
        sp = new DatagramPacket(res.getBytes(), res.getBytes().length, InetAddress.getByName(HOST), PORT);
        client.send(sp);
        client.close();
    }
}
