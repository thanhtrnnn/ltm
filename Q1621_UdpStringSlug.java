import java.net.*;

public class Q1621_UdpStringSlug {
    private static final String HOST = "36.50.135.242";
    private static final int PORT = 2208;
    private static final String CREDS = ";B23DCAT280;XXXXXXXX";

    // requestId;data  ->  requestId;lower-case-slug
    public static String solve(String s) {
        int k = s.indexOf(';');
        String data = s.substring(k + 1).toLowerCase().replaceAll("\\p{Punct}", "");
        String res = data.trim().replaceAll("\\s+", "-");
        return s.substring(0, k) + ";" + res;
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
