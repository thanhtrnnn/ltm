import java.net.*;

public class JNP802_UdpDataMissingNumbers {
    static final String HOST = "36.50.135.242";
    static final Integer PORT = 2207;
    static final String CREDS = ";B23DCAT120;7SgBVwrd";

    public static String solve(String s) {
        String[] w = s.split("[;,]");
        int n = Integer.valueOf(w[1]);
        int[] a = new int[105];
        for (int i = 2; i < w.length; i++) a[Integer.valueOf(w[i].trim())]++;
        String res = w[0] + ";";
        for (int i = 1; i <= n; i++) {
            if(a[i] < 1) res += String.valueOf(i) + ",";
        }
        if (res.endsWith(",")) res = res.substring(0, res.length() - 1);
        return res;
    }

    public static void main(String[] args) throws Exception {
        DatagramSocket client = new DatagramSocket();
        client.setSoTimeout(5000);
        String data = null;
        try {
            byte[] s, r;
            DatagramPacket sp, rp;
            s = CREDS.getBytes();
            sp = new DatagramPacket(s, s.length, InetAddress.getByName(HOST), PORT);
            client.send(sp);
            System.out.println(">> " + CREDS);
            r = new byte[10001];
            rp = new DatagramPacket(r, r.length);
            client.receive(rp);
            data = new String(rp.getData(), 0, rp.getLength()).trim();
            System.out.println("<< " + data);
            String res = solve(data);
            sp = new DatagramPacket(res.getBytes(), res.getBytes().length, InetAddress.getByName(HOST), PORT);
            client.send(sp);
            System.out.println(">> " + res);
        } catch (SocketTimeoutException e) {
            System.err.println("[LỖI] Server không phản hồi sau 5s (sai HOST/PORT/qCode hoặc mạng chặn UDP)");
        } catch (Exception e) {
            System.err.println("[LỖI] " + e);
            if (data != null) System.err.println("[LỖI] Dữ liệu nhận được: " + data);
            e.printStackTrace();
        } finally {
            client.close();
        }
    }
}
