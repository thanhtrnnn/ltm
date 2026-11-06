import java.net.*;

public class JNP934_UdpDataCaesarDecode {
    private static final String HOST = "36.50.135.242";
    private static final int PORT = 2207;
    private static final String CREDS = ";B23DCAT280;XXXXXXXX"; 
    public static String solve(String s) {
        String[] w = s.split(";");
        int k = Integer.parseInt(w[2].trim()) % 26;
        String ans = "";
        for (char c : w[1].toCharArray()) {
            if (Character.isUpperCase(c)) ans += (char) ((c - 'A' - k + 26) % 26 + 'A');
            else if (Character.isLowerCase(c)) ans += (char) ((c - 'a' - k + 26) % 26 + 'a');
            else ans += c;
        }
        return w[0] + ";" + ans;
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
