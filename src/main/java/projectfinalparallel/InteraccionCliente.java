package projectfinalparallel;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InteraccionCliente extends Remote {
    void receiveMessage(String[] message, CodificadoRMI servidor, int identifier) throws RemoteException;
}