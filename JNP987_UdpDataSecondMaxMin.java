import java.util.*;
import java.net.*;

public class JNP987_UdpDataSecondMaxMin {
    private static final String HOST = "36.50.135.242";
    private static final int PORT = 2207;
    private static final String CREDS = ";B23DCAT280;XXXXXXXX";

    // requestId;z1,...,z50  ->  requestId;secondMax,secondMin (giá trị phân biệt)
    public static String solve(String s) {
        String[] w = s.split(";");
        TreeSet<Integer> a = new TreeSet<>();
        for (String x : w[1].split(",")) a.add(Integer.parseInt(x.trim()));
        int secondMax = a.lower(a.last());
        int secondMin = a.higher(a.first());
        return w[0] + ";" + secondMax + "," + secondMin;
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
