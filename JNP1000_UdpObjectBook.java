import java.io.*;
import java.net.*;

import UDP.Book;

public class JNP1000_UdpObjectBook {
    private static final String HOST = "36.50.135.242";
    private static final int PORT = 2209;
    private static final String CREDS = ";B23DCAT280;XXXXXXXX";

    public static void main(String[] args) throws Exception {
        DatagramSocket client = new DatagramSocket();
        client.setSoTimeout(5000);
        String raw = null;
        try {
            DatagramPacket sp, rp;
            byte[] s, r, ix;
            s = CREDS.getBytes();
            sp = new DatagramPacket(s, s.length, InetAddress.getByName(HOST), PORT);
            client.send(sp);
            System.out.println(">> " + CREDS);

            r = new byte[10001];
            rp = new DatagramPacket(r, r.length);
            client.receive(rp);
            raw = new String(rp.getData(), 0, rp.getLength());

            ByteArrayInputStream ba = new ByteArrayInputStream(rp.getData(), 0, rp.getLength());
            ix = new byte[8];
            ba.read(ix, 0, 8);
            String id = new String(ix);
            System.out.println("<< requestId=" + id + ", " + rp.getLength() + " byte");
            Book a = (Book) new ObjectInputStream(ba).readObject();
            System.out.println("<< " + a);

            a.setTitle(titleCase(a.getTitle()));
            // author -> "HỌ, Tên Đệm"
            String[] wa = a.getAuthor().trim().split("\\s+");
            String rest = "";
            for (int i = 1; i < wa.length; i++) rest += wa[i] + " ";
            a.setAuthor(wa[0].toUpperCase() + ", " + titleCase(rest));
            // ISBN -> 978-3-16-148410-0
            String d = a.getIsbn().replaceAll("[^0-9Xx]", "");
            a.setIsbn(d.substring(0, 3) + "-" + d.substring(3, 4) + "-" + d.substring(4, 6) + "-" + d.substring(6, 12) + "-" + d.substring(12));
            // yyyy-mm-dd -> mm/yyyy
            String[] wd = a.getPublishDate().trim().split("-");
            a.setPublishDate(wd[1] + "/" + wd[0]);

            ByteArrayOutputStream bb = new ByteArrayOutputStream();
            bb.write(id.getBytes());
            new ObjectOutputStream(bb).writeObject(a);
            s = bb.toByteArray();
            sp = new DatagramPacket(s, s.length, InetAddress.getByName(HOST), PORT);
            client.send(sp);
            System.out.println(">> requestId=" + id + ", " + s.length + " byte");
            System.out.println(">> " + a);
        } catch (SocketTimeoutException e) {
            System.err.println("[LỖI] Server không phản hồi sau 5s (sai HOST/PORT/qCode hoặc mạng chặn UDP)");
        } catch (StreamCorruptedException | ClassNotFoundException | InvalidClassException e) {
            System.err.println("[LỖI] Không đọc được object (sai package/tên lớp/serialVersionUID/thuộc tính?): " + e);
            if (raw != null) System.err.println("[LỖI] Dữ liệu nhận được: " + raw);
        } catch (Exception e) {
            System.err.println("[LỖI] " + e);
            if (raw != null) System.err.println("[LỖI] Dữ liệu nhận được: " + raw);
            e.printStackTrace();
        } finally {
            client.close();
        }
    }

    static String titleCase(String name) {
        String wn[] = name.trim().toLowerCase().split("\\s+");
        String res = "";
        for (String x : wn) res += x.substring(0, 1).toUpperCase() + x.substring(1) + " ";
        return res.trim();
    }
}
