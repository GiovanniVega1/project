package projectfinalparallel;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTextArea;

public class CodificadoRMIServidor extends UnicastRemoteObject implements CodificadoRMI {
    public Map<Integer, String[]> resultadosClientes = new HashMap<>();
    public Map<Integer, InteraccionCliente> clientes;
    private CodificadoHill hill;
    private int contadorClientes;

    public CodificadoRMIServidor() throws RemoteException {
        clientes = new HashMap<>();
        hill = new CodificadoHill();
        contadorClientes = 0;
    }

    @Override
    public void contarPalabrasClientes(String[] nombreArchivo, JTextArea log) throws RemoteException {
        System.out.println("Contar palabras por hilos");

        int numClientes = clientes.size();

        int palabrasPorCliente = nombreArchivo.length / numClientes;
        int palabrasRestantes = nombreArchivo.length % numClientes;
        int inicio = 0;

        System.out.println("Num clientes: " + numClientes);
        System.out.println("Palabras por cliente: " + palabrasPorCliente);
        System.out.println("Palabras restantes: " + palabrasRestantes);

        long tiempoInicioConHilos = System.currentTimeMillis();
        for (int i = 0; i < numClientes; i++) {
                int fin = inicio + palabrasPorCliente + (i < palabrasRestantes ? 1 : 0);
                String[] parteTexto = Arrays.copyOfRange(nombreArchivo, inicio, fin);

                clientes.get(i+1).receiveMessage(parteTexto, this, i);

                inicio = fin;
            }
        long tiempoFinConHilos = System.currentTimeMillis();
        long tiempoTotalConHilos = tiempoFinConHilos - tiempoInicioConHilos;
        log.append("\nTiempo de ejecución (RMI): " + tiempoTotalConHilos + " ms\n");
    }
    
    public CodificadoRMI connect(String Ip, CodificadoRMIServidor server, JTextArea log) {
        try {
            LocateRegistry.createRegistry(9000);

            CodificadoRMI service = server;


            java.rmi.Naming.rebind("rmi://" + Ip + ":9000/CifradoRMI", service);

            log.append("\nServidor de RMI listo.\n");
            return service;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
        public void registerClient(InteraccionCliente cliente) throws RemoteException {
        contadorClientes++;
        clientes.put(contadorClientes, cliente);

        // Imprimir el array de clientes
        System.out.println("Clientes registrados: " + Arrays.toString(clientes.keySet().toArray()));

        System.out.println("Cliente registrado: " + cliente.toString() + " - ID: " + contadorClientes);

    }
        
    @Override
    public void sendResult(String[] resultado, int clientId) {
        resultadosClientes.put(clientId, resultado);

        // Verifica si todos los clientes han enviado sus resultados
        if (resultadosClientes.size() == clientes.size()) {
            ArrayList<String> finalTextList = new ArrayList<>();

            // Procesa los resultados en el orden correcto
            for (int i = 0; i <= clientes.size()-1; i++) {
                String[] resultadoCliente = resultadosClientes.get(i);
                // Agrega los elementos del resultado del cliente a la lista
                Collections.addAll(finalTextList, resultadoCliente);
                // Procesa el resultado como desees
                //System.out.println("Resultado del Cliente " + i + ": " + Arrays.toString(resultadoCliente));
            }
        }
    }
}