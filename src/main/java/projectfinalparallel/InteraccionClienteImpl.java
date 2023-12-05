package projectfinalparallel;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InteraccionClienteImpl extends UnicastRemoteObject implements InteraccionCliente {


    public InteraccionClienteImpl() throws RemoteException {

    }

    @Override
    public void receiveMessage(String[] message, CodificadoRMI servidor, int identifier) throws RemoteException {
        CodificadoHill cifrador=new CodificadoHill();
        System.out.println("Texto recibido: " + Arrays.toString(message));
        try {
            String[] textoCifrado=cifrador.cifrarConHilosClientesFinal(message, message.length);
                    System.out.println(Arrays.toString(textoCifrado));
                    servidor.sendResult(textoCifrado, identifier);
                    
        } catch (IOException ex) {
            Logger.getLogger(InteraccionClienteImpl.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
}
