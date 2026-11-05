import java.rmi.registry.*;
import java.util.*;

import RMI.DataService;

public class JNP1326_RmiDataPerfectSquares {
    private static final String HOST = "36.50.135.242";
    private static final String SV = "B23DCAT280", Q = "XXXXXXXX";
    public static void main(String[] args) throws Exception {
        Registry rg = LocateRegistry.getRegistry(HOST, 1099);
        DataService sv = (DataService) rg.lookup("RMIDataService");
        Object o = sv.requestData(SV, Q);
        long n = o instanceof Number ? ((Number) o).longValue() : Long.parseLong(o.toString().trim());
        List<Integer> res = new ArrayList<>();
        for (int i = 1; (long) i * i < n; i++) res.add(i * i);
        sv.submitData(SV, Q, res);
    }
}
