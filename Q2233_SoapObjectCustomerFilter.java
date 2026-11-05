import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

// SOAP 1.1 gửi tay bằng HTTP vì JDK mới không còn wsimport/JAX-WS
public class Q2233_SoapObjectCustomerFilter {
    private static final String URL = "http://36.50.135.242:2221/ObjectService";
    private static final String NS = "http://soap.server.network.judge.dblab.ptit/";
    private static final String SV = "B23DCAT280", Q = "XXXXXXXX";

    static Document call(String op, String body) throws Exception {
        String env = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Body>"
            + "<ns2:" + op + " xmlns:ns2=\"" + NS + "\">" + body + "</ns2:" + op + ">"
            + "</S:Body></S:Envelope>";
        HttpURLConnection c = (HttpURLConnection) new URL(URL).openConnection();
        c.setRequestMethod("POST");
        c.setDoOutput(true);
        c.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        c.setRequestProperty("SOAPAction", "\"\"");
        c.getOutputStream().write(env.getBytes(StandardCharsets.UTF_8));
        InputStream in = c.getResponseCode() < 400 ? c.getInputStream() : c.getErrorStream();
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        return f.newDocumentBuilder().parse(in);
    }

    static String text(Element e, String tag) {
        NodeList l = e.getElementsByTagName(tag);
        return l.getLength() == 0 ? "" : l.item(0).getTextContent();
    }

    static String esc(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    public static void main(String[] args) throws Exception {
        String head = "<studentCode>" + SV + "</studentCode><qCode>" + Q + "</qCode>";
        NodeList ls = call("requestListCustomer", head).getElementsByTagName("return");
        String data = "";
        for (int i = 0; i < ls.getLength(); i++) {
            Element e = (Element) ls.item(i);
            int cnt = Integer.parseInt(text(e, "purchaseCount").trim());
            float spent = Float.parseFloat(text(e, "totalSpent").trim());
            if (cnt >= 5 && spent > 5000) {
                data += "<data><customerId>" + esc(text(e, "customerId")) + "</customerId>"
                    + "<location>" + esc(text(e, "location")) + "</location>"
                    + "<purchaseCount>" + text(e, "purchaseCount").trim() + "</purchaseCount>"
                    + "<totalSpent>" + text(e, "totalSpent").trim() + "</totalSpent></data>";
            }
        }
        call("submitListCustomer", head + data);
    }
}
