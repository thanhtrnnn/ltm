import java.net.*;

public class JNP992_UdpDataFirstPrimes {
    private static final String HOST = "36.50.135.242";
    private static final int PORT = 2207;
    private static final String CREDS = ";B23DCAT280;XXXXXXXX";

    // requestId;n  ->  requestId;p1,p2,...,pn (n số nguyên tố đầu tiên)
    static boolean isPrime(int x) {
        if (x < 2) return false;
        for (int i = 2; i * i <= x; i++) if (x % i == 0) return false;
        return true;
    }

    public static String solve(String s) {
        String[] w = s.split(";");
        int n = Integer.parseInt(w[1].trim());
        String res = "";
        for (int x = 2, cnt = 0; cnt < n; x++) {
            if (isPrime(x)) {
                res += x + ",";
                cnt++;
            }
        }
        if (res.endsWith(",")) res = res.substring(0, res.length() - 1);
        return w[0] + ";" + res;
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
