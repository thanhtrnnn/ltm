import java.rmi.registry.*;

import RMI.CharacterService;

public class Q1732_RmiCharLogLevels {
    private static final String HOST = "36.50.135.242";
    private static final String SV = "B23DCAT280", Q = "XXXXXXXX";
    public static void main(String[] args) throws Exception {
        Registry rg = LocateRegistry.getRegistry(HOST, 1099);
        CharacterService sv = (CharacterService) rg.lookup("RMICharacterService");
        String s = sv.requestCharacter(SV, Q);
        int e = 0, i = 0, w = 0;
        for (String line : s.split("\\|\\|")) {
            line = line.trim();
            if (line.startsWith("ERROR")) e++;
            else if (line.startsWith("INFO")) i++;
            else if (line.startsWith("WARN")) w++;
        }
        sv.submitCharacter(SV, Q, "ERROR=" + e + ";INFO=" + i + ";WARN=" + w);
    }
}
