import java.rmi.registry.*;

import RMI.ObjectService;
import RMI.ProductX;

public class JNP1002_RmiObjectProductX {
    private static final String HOST = "36.50.135.242";
    private static final String SV = "B23DCAT280", Q = "XXXXXXXX";
    public static void main(String[] args) throws Exception {
        Registry rg = LocateRegistry.getRegistry(HOST, 1099);
        ObjectService sv = (ObjectService) rg.lookup("RMIObjectService");
        ProductX p = (ProductX) sv.requestObject(SV, Q);
        int sum = 0;
        for (char c : p.getDiscountCode().toCharArray()) {
            if (Character.isDigit(c)) sum += c - '0';
        }
        p.setDiscount(sum);
        sv.submitObject(SV, Q, p);
    }
}
