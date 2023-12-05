package projectfinalparallel;

import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.swing.JTextArea;

public interface CodificadoRMI extends Remote {
    void contarPalabrasClientes(String[] texto, JTextArea log) throws RemoteException;
    void registerClient(InteraccionCliente client) throws RemoteException;    
    void sendResult(String[] message, int identifier) throws RemoteException;
}
