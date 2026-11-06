import java.io.*;
import java.net.*;

import UDP.Product;

public class JNP937_UdpObjectProduct {
    private static final String HOST = "36.50.135.242";
    private static final int PORT = 2209;
    private static final String CREDS = ";B23DCAT280;XXXXXXXX";

    public static void main(String[] args) throws Exception {
        DatagramSocket client = new DatagramSocket();
        DatagramPacket sp, rp;
        byte[] s, r, ix;
        s = CREDS.getBytes();
        sp = new DatagramPacket(s, s.length, InetAddress.getByName(HOST), PORT);
        client.send(sp);

        r = new byte[10001];
        rp = new DatagramPacket(r, r.length);
        client.receive(rp);

        ByteArrayInputStream ba = new ByteArrayInputStream(rp.getData(), 0, rp.getLength());
        ix = new byte[8];
        ba.read(ix, 0, 8);
        String id = new String(ix);
        Product a = (Product) new ObjectInputStream(ba).readObject();

        // Đổi chỗ từ đầu và từ cuối của name
        String[] wn = a.getName().trim().split("\\s+");
        String t = wn[0];
        wn[0] = wn[wn.length - 1];
        wn[wn.length - 1] = t;
        a.setName(String.join(" ", wn));
        // Đảo ngược quantity: 1899 -> 9981
        a.setQuantity(Integer.parseInt(new StringBuilder(String.valueOf(a.getQuantity())).reverse().toString()));

        ByteArrayOutputStream bb = new ByteArrayOutputStream();
        bb.write(id.getBytes());
        new ObjectOutputStream(bb).writeObject(a);
        s = bb.toByteArray();
        sp = new DatagramPacket(s, s.length, InetAddress.getByName(HOST), PORT);
        client.send(sp);
        client.close();
    }

}
