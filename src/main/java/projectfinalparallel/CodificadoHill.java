package projectfinalparallel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

public class CodificadoHill {
    String key = "hill";

    ArrayList<String> palabrasSec=new ArrayList<String>();
    ArrayList<String> palabrasCon=new ArrayList<String>();
    CodificadorTexto cifrador = new CodificadorTexto(palabrasSec, palabrasCon);
    ContadorDePalabras contadorPalabras=new ContadorDePalabras();
    String nombreArchivo = "C:/Users/sears/OneDrive/Documentos/NetBeansProjects/projectFinalParallel/src/main/java/projectfinalparallel/";
    ContadorDePalabrasHilos contadorHilos=new ContadorDePalabrasHilos();
    int numCifrado=10000;
    
    int numHilos = 8;
    
    CodificadoRMIServidor server;
    
    public void contarPalabrasSecuencial(JTextArea log) throws IOException{
        long tiempoInicioSecuencial = System.currentTimeMillis();

        int totPalabras=contadorPalabras.contarPalabras(nombreArchivo, palabrasSec);
        log.append("\nNúmero de palabras (secuencial): " + totPalabras);
        
        long tiempoFinSecuencial = System.currentTimeMillis();
        long tiempoTotalSecuencial = tiempoFinSecuencial - tiempoInicioSecuencial;
        log.append("\nTiempo de ejecución (secuencial): " + tiempoTotalSecuencial + " ms");         
    }
    
 private class CifradoRunnable implements Runnable {
    private String[] texto;
    private String[] textosCifrados;
    private int startIndex;
    private int endIndex;

    public CifradoRunnable(String[] texto, String[] textosCifrados, int startIndex, int endIndex) {
        this.texto = texto;
        this.textosCifrados = textosCifrados;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public void run() {
        System.out.println("Running");
    }
}
  
 public void contarPalabrasParalelo(CodificadoRMIServidor server, JTextArea log) throws IOException {
    int totPalabras = contadorPalabras.contarPalabras(nombreArchivo, palabrasSec);
    System.out.println("palabrasSec: " + palabrasSec);
    String[] texto = new String[palabrasSec.size()];
    server.contarPalabrasClientes(texto, log);
}
 
  public String[] cifrarConHilosClientesFinal(String[] textoACifrar, int numPalabras) throws IOException {
    System.out.println("Número de palabras (Clientes): " + numPalabras);

    String[] texto = new String[numPalabras];

    String[] textosCifrados = new String[numPalabras];

    ExecutorService executorService = Executors.newFixedThreadPool(numHilos);

    int batchSize = numPalabras / numHilos;

    for (int i = 0; i < numHilos; i++) {
        int startIndex = i * batchSize;
        int endIndex = (i == numHilos - 1) ? numPalabras : (i + 1) * batchSize;

        executorService.submit(new CifradoRunnable(texto, textosCifrados, startIndex, endIndex));
    }

    executorService.shutdown();

    try {
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
      
      return textosCifrados;
  }
 
    public void contarPalabrasConcurrente(JTextArea log) throws IOException {
        try {
            long tiempoInicioConHilos = System.currentTimeMillis();

            int totPalabras = contadorHilos.contarPalabrasConcurrente(nombreArchivo, numHilos,  palabrasSec);
            log.append("\nNúmero de palabras (con hilos): " + totPalabras);

            long tiempoFinConHilos = System.currentTimeMillis();
            long tiempoTotalConHilos = tiempoFinConHilos - tiempoInicioConHilos;
            log.append("\nTiempo de ejecución (con hilos): " + tiempoTotalConHilos + " ms");
        } catch (InterruptedException ex) {
            Logger.getLogger(CodificadoHill.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}