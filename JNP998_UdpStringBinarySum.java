import java.math.BigInteger;
import java.net.*;

public class JNP998_UdpStringBinarySum {
    private static final String HOST = "36.50.135.242";
    private static final int PORT = 2208;
    private static final String CREDS = ";B23DCAT280;XXXXXXXX"; 
    public static String solve(String s) {
        String[] w = s.split("[;,]");
        BigInteger sum = new BigInteger(w[1].trim(), 2).add(new BigInteger(w[2].trim(), 2));
        return w[0] + ";" + sum;
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
