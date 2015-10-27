package areSorted;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class AreSorted {
	public static void main(String[] args) throws IOException {
		int n, m;
		boolean sorted = true; // Flag che mi dice se un array dello stream e' o meno ordinato
		boolean allSorted = true; // Flag che mi dice se tutti gli array dello stream sono o meno ordinati
		Double[] dA;// Conterra' un array dell'input che verra' poi ordinato con la funzione Arrays.sort
		
		// Inizio controllo dei parametri di input
		if (args.length != 2) {
			System.err.println("Devi specificare il path dell'input file e del file generato dal Bucket Sort.");
			return;
		}
		
		FileInputStream fisArrays = new FileInputStream(args[0]);
		FileInputStream fisArraysSorted = new FileInputStream(args[1]);
		DataInputStream disArrays = new DataInputStream(fisArrays);
		DataInputStream disArraysSorted = new DataInputStream(fisArraysSorted);
		m = disArrays.readInt();
		if (m != disArraysSorted.readInt()) {
			System.err.println("File con diversi valori di m.");
			return;
		}
		n = disArrays.readInt();
		if (n != disArraysSorted.readInt()) {
			System.err.println("File con diversi valori di n.");
			return;
		}
		// Fine controllo dei parametri di input
		
		dA = new Double[n];
		for (int i = 0 ; i < m; i++) {
			// Suppongo che l'array del file generato dal Bucket Sort sia ordinato
			sorted = true;
			
			// Leggo il primo array dell'input file
			for (int j = 0; j < n; j++) {
				dA[j] = disArrays.readDouble();
			}
			
			// Ordino l'array
			Arrays.sort(dA);
			
			// Controllo se l'array ordinato e' equivalente al corrispondente array
			// presente nel file generato dal Bucket Sort
			
			for (int j = 0; j < n; j++) {
				if (dA[j] != disArraysSorted.readDouble()) { // L'array non e' ordinato
					sorted = false;
					allSorted=false;
				}
			}
			if (!sorted) {
				System.out.println("Array non ordinato.");
			}
		}
		fisArrays.close();
		disArrays.close();
		fisArraysSorted.close();
		disArraysSorted.close();
		
		// allSorted non e' mai stata modificata, quindi tutti gli array
		// ordinati dal Bucket Sort sono stati ordinati correttamente.
		if (allSorted) {
			System.out.println("Tutti gli array sono ordinati.");
		}
	}
}
