import java.net.*;

public class JNP932_UdpStringTitleCase {
    private static final String HOST = "36.50.135.242";
    private static final int PORT = 2207;
    private static final String CREDS = ";B23DCAT120;ECUuSPkC"; 
    public static String solve(String s) {
        String[] w = s.split(";");
        s = w[1];
        String[] w1 = s.split("\\s+");
        String ans = "";
        for (int i = 0; i < w1.length; i++) {
            ans += w1[i].substring(0, 1).toUpperCase() + w1[i].substring(1).toLowerCase() + " ";
        }
        String res = w[0] + ";" + ans.trim();
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
