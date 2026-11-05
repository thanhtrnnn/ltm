import java.io.*;
import java.net.*;

import UDP.Employee;

public class JNP1001_UdpObjectEmployee {
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

        String wn[] = a.getName().trim().toLowerCase().split("\\s+");
        String resname = "";
        for (int i = 0; i < wn.length; i++) {
            resname += wn[i].substring(0, 1).toUpperCase() + wn[i].substring(1) + " ";
        }
        String wd[] = a.getHireDate().split("-");
        int x = 0;
        for (char c : wd[0].toCharArray()) x += c - '0';
        a.setName(resname.trim());
        a.setSalary(Math.round(a.getSalary() * (100 + x)) / 100.0);
        a.setHireDate(wd[2] + "/" + wd[1] + "/" + wd[0]);
        
        ByteArrayOutputStream bb = new ByteArrayOutputStream();
        bb.write(id.getBytes());
        new ObjectOutputStream(bb).writeObject(a);
        s = bb.toByteArray();
        sp = new DatagramPacket(s, s.length, InetAddress.getByName(HOST), PORT);
        client.send(sp);
        client.close();
    }    
}
