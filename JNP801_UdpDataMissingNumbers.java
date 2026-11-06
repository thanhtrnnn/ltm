import java.net.*;

public class JNP801_UdpDataMissingNumbers {
    private static final String HOST = "36.50.135.242";
    private static final int PORT = 2207;
    private static final String CREDS = ";B23DCAT280;XXXXXXXX";

    // requestId; n; A1,A2,...Am  ->  requestId;B1,B2,...
    public static String solve(String s) {
        String[] w = s.split(";");
        int n = Integer.parseInt(w[1].trim());
        boolean[] has = new boolean[n + 2];
        if (w.length > 2) {
            for (String x : w[2].split(",")) {
                if (x.trim().isEmpty()) continue;
                int v = Integer.parseInt(x.trim());
                if (v >= 1 && v <= n) has[v] = true;
            }
        }
        String res = "";
        for (int i = 1; i <= n; i++) if (!has[i]) res += i + ",";
        if (res.endsWith(",")) res = res.substring(0, res.length() - 1);
        return w[0].trim() + ";" + res;
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
