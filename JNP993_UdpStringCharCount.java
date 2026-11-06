import java.util.*;
import java.net.*;

public class JNP993_UdpStringCharCount {
    private static final String HOST = "36.50.135.242";
    private static final int PORT = 2208;
    private static final String CREDS = ";B23DCAT280;XXXXXXXX";

    // requestId;aaazbbbccdaaz  ->  requestId;5a2z3b2c1d (thứ tự xuất hiện đầu tiên)
    public static String solve(String s) {
        int k = s.indexOf(';');
        String data = s.substring(k + 1);
        Map<Character, Integer> mp = new LinkedHashMap<>();
        for (char c : data.toCharArray()) mp.put(c, mp.getOrDefault(c, 0) + 1);
        String res = "";
        for (Map.Entry<Character, Integer> e : mp.entrySet()) res += e.getValue() + "" + e.getKey();
        return s.substring(0, k) + ";" + res;
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
