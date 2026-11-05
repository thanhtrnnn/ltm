import java.net.*;

public class Q1622_UdpStringMaskSensitive {
    private static final String HOST = "36.50.135.242";
    private static final int PORT = 2208;
    private static final String CREDS = ";B23DCAT280;XXXXXXXX"; 
    public static String solve(String s) {
        int p = s.indexOf(";");
        String d = s.substring(p + 1);
        d = d.replaceAll("token=\\S+", "token=[TOKEN]");
        d = d.replaceAll("[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+", "[EMAIL]");
        d = d.replaceAll("(\\+84|0)\\d{9,10}\\b", "[PHONE]");
        return s.substring(0, p) + ";" + d;
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
