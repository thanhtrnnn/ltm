import java.io.*;
import java.net.*;

import UDP.Student;

public class JNP933_UdpObjectStudent {
    private static final String HOST = "36.50.135.242";
    private static final int PORT = 2209;
    private static final String CREDS = ";B23DCAT120;QhdUvdJS"; 
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
            Student a = (Student) new ObjectInputStream(ba).readObject();

            String wn[] = a.getName().toLowerCase().split("\\s+");
            String resname = "", resmail = "";
            for (int i = 0; i < wn.length; i++) {
                if (i == wn.length - 1) {
                    resmail = wn[i] + resmail + "@ptit.edu.vn";
                } else resmail += wn[i].substring(0, 1);
                resname += wn[i].substring(0, 1).toUpperCase() + wn[i].substring(1) + " ";
            }
            a.setName(resname.trim());
            a.setEmail(resmail);

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