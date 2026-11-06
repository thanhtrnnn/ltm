import java.io.*;
import java.net.*;

import UDP.PricedProduct;

public class Q1632_UdpObjectPricedProduct {
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
        PricedProduct a = (PricedProduct) new ObjectInputStream(ba).readObject();

        double p = a.getBasePrice() * (1 + a.getTaxRate() / 100) * (1 - a.getDiscountRate() / 100);
        a.setFinalPrice(Math.round(p * 100) / 100.0);

        ByteArrayOutputStream bb = new ByteArrayOutputStream();
        bb.write(id.getBytes());
        new ObjectOutputStream(bb).writeObject(a);
        s = bb.toByteArray();
        sp = new DatagramPacket(s, s.length, InetAddress.getByName(HOST), PORT);
        client.send(sp);
        client.close();
    }

}
