import java.io.*;
import java.net.*;

import UDP.PricedProduct;

public class Q1892_UdpObjectPricedProductFee {
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
            PricedProduct a = (PricedProduct) new ObjectInputStream(ba).readObject();

            double fee = a.getBasePrice() >= 1000 ? 25 : 0;
            double p = a.getBasePrice() * (1 + a.getTaxRate() / 100) * (1 - a.getDiscountRate() / 100) + fee;
            a.setFinalPrice(Math.round(p * 100) / 100.0);

            ByteArrayOutputStream bb = new ByteArrayOutputStream();
            bb.write(id.getBytes());
            new ObjectOutputStream(bb).writeObject(a);
            s = bb.toByteArray();
            sp = new DatagramPacket(s, s.length, InetAddress.getByName(HOST), PORT);
            client.send(sp);
            System.out.println(">> requestId=" + id + ", " + s.length + " byte");
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

}
