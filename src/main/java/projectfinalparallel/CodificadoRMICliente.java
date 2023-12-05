package projectfinalparallel;

import java.rmi.Naming;
import javax.swing.JTextArea;


public class CodificadoRMICliente {
    

    public void connect(String ip, CodificadoRMI service, JTextArea log) {
        try {
            service = (CodificadoRMI) Naming.lookup("rmi://" + ip + ":9000/CifradoRMI");

            InteraccionClienteImpl clientCallback = new InteraccionClienteImpl();
            service.registerClient(clientCallback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

