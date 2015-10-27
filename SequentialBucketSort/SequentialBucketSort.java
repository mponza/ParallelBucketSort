package sequentialBucketSort;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import sequentialBucketSort.BucketSeq;
import sequentialBucketSort.QuickSelect;

public class SequentialBucketSort {
	
	private static void selectionSort(Double [] a) {
		 int minIdx;
		 Double tmp;
		 
		 for (int i = 0; i < a.length; i++) {
			 minIdx = i;
			 for (int j = i + 1; j  < a.length; j++) {
				 if (a[j] < a[minIdx]) {
					 minIdx = j;
				 }
			 }
			 tmp = a[minIdx];
			 a[minIdx] = a[i];
			 a[i] = tmp;
		 }
	 }
	
	public static void main(String[] args) throws IOException {
		int i, j, k, m, n, from, ki, nBuckets;
		long time;
		FileInputStream fis;
		DataInputStream dis;
		FileOutputStream fos;
		DataOutputStream dos;
		Double[][] arrayStream;
		Double[] a;
		BucketSeq[] buckets;
		Double[] bucket;
		Double[][] sortedArrays;
		Double[] writeArray;
		
		// Inizio controllo dei parametri
		if (args.length != 3) {
			System.err.println("You must specify the \"buckets number\" and the path of the input and output file.");
			return;
		}
		try {
			nBuckets = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return;
		}
		try {
			fis = new FileInputStream(args[1]);
			dis = new DataInputStream(fis);
			fos = new FileOutputStream(args[2]);
			dos = new DataOutputStream(fos);
			m = dis.readInt();
			n = dis.readInt();
			dos.writeInt(m); 
			dos.writeInt(n);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		if (nBuckets > n) { // Si e' scelto un numero di bucket troppo alto
			nBuckets = n;
		}
		// Fine controllo dei parametri
		
		
		// Creo lo "stream" di array da ordinare
		arrayStream = new Double[m][n];
		for (i = 0; i < m; i++) {
			for (j = 0; j < n; j++) {
				arrayStream[i][j] = dis.readDouble();
			}
		}
		
		time = System.currentTimeMillis();
		
		// Ogni array dello stream viene suddiviso in bucket e, successivamente,
		// ogni bucket viene ordinato sequenzialmente.
		buckets = new BucketSeq[nBuckets];
		sortedArrays = new Double[m][n];
		for (i = 0; i < m; i++) {
			a = arrayStream[i];
			
			// Istanzio l'array di BucketSeq
			for (j = 0; j < nBuckets; j++) {
				buckets[j] = new BucketSeq();
			}
			
			// Ad ogni iterazione viene selezionato il ki-esimo elemento piu' piccolo dell'array, mettendo
			// gli elementi piu' piccoli alla sua sinistra e i piu' grandi alla sua destra, utilizzando
			// l'algoritmo quickselect. Dopo cio', viene effettuata una copia della porzione dell'array
			// dal ki + 1-esimo elemento (tranne nel caso in cui i == 0 (in tal caso ki + 1 == 0)) della
			// precedente iterazione (valore della variabile from) fino al ki-esimo elemento. La porzione
			//	di array copiata, che costituisce il bucket, viene inserita in buckets[i].
			for (j = 0; j < nBuckets; j++) {
				
				// Limite inferiore della porzione di array in cui cercare il ki-esimo elemento piu' piccolo
				from = a.length/nBuckets * j;
				// ki-esimo elemento piu' piccolo da cercare
				ki = a.length/nBuckets * (j + 1) - 1;
				
				if (j == nBuckets - 1) {
					ki = a.length - 1;
				} else {
					QuickSelect.select(a, from, ki, a.length - 1);
				}
				buckets[j].addBucket(Arrays.copyOfRange(a, from, ki + 1));
				
			}
			
			// Ordino sequenzialmente i bucket
			for (j = 0; j < nBuckets; j++) {
				selectionSort(buckets[j].getBucket(0));
			}
			
			// Giustappongo i bucket ordinati in modo da ottenere l'array
			// complessivamente ordinato
			k = 0;
			for (j = 0; j < nBuckets; j++) {
				bucket = buckets[j].getBucket(0);
				System.arraycopy(bucket, 0, sortedArrays[i], k, bucket.length);
				k += bucket.length;
			}
		}
		
		System.out.println("[Buckets Number = " + nBuckets + "] Time Elapsed (SequentialBucketSort): " + (System.currentTimeMillis() - time));
		
		// Scrivo i bucket ordinati su file
		for (i = 0; i < m; i++) {
			writeArray = sortedArrays[i];
			for (j = 0; j < writeArray.length; j++) {
				dos.writeDouble(writeArray[j]);
			}
		}
		
		dos.close();
		fos.close();
	}
}
