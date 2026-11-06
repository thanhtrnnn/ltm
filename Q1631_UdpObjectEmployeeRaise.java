import java.io.*;
import java.net.*;

import UDP.Employee;

public class Q1631_UdpObjectEmployeeRaise {
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
        Employee a = (Employee) new ObjectInputStream(ba).readObject();

        a.setName(titleCase(a.getName()));
        a.setSalary(Math.round(a.getSalary() * 108) / 100.0);

        ByteArrayOutputStream bb = new ByteArrayOutputStream();
        bb.write(id.getBytes());
        new ObjectOutputStream(bb).writeObject(a);
        s = bb.toByteArray();
        sp = new DatagramPacket(s, s.length, InetAddress.getByName(HOST), PORT);
        client.send(sp);
        client.close();
    }

    static String titleCase(String name) {
        String wn[] = name.trim().toLowerCase().split("\\s+");
        String res = "";
        for (String x : wn) res += x.substring(0, 1).toUpperCase() + x.substring(1) + " ";
        return res.trim();
    }
}
