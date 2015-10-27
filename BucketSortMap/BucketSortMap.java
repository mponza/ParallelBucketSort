package bucketSortMap;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import cl.niclabs.skandium.Skandium;
import cl.niclabs.skandium.Stream;
import cl.niclabs.skandium.skeletons.Map;
import cl.niclabs.skandium.skeletons.Skeleton;

/**
* Implementa la versione parallela del Bucket Sort
* 
* @author Marco Ponza
*/
public class BucketSortMap {
	
	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		int parDegree; // Grado di parallelismo
		int nBuckets; // Numero di bucket
		int n; // Lunghezza di ogni array dello stream
		int m; // Lunghezza dello stream
		int i, j;
		Double[] a;
		Task[] taskStream; // Array di task da mandare come input alla map
		long time; // Usato per conoscere il tempo di completamento della map in millisecondi
		FileInputStream fis;
		DataInputStream dis;
		FileOutputStream fos;
		DataOutputStream dos;
		Future<Double[]>[] futures;
		Double[] writeArray;
		Skeleton<Task, Double[]> map;
		Skandium skandium;
		Stream <Task, Double[]> stream;
		
		// Inizio controllo dei parametri di input
		if (args.length != 4) {
			System.err.println("Devi specificare il numero di bucket, il grado di parallelismo e i path dell'input e dell'output file.");
			return;
		}
		
		try {
			nBuckets = Integer.parseInt(args[0]);
			parDegree = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return;
		}	
		
		if (nBuckets <= 0) {
			System.err.println("Il numero di bucket deve essere > 0.");
			return;
		}
		
		if (parDegree <= 0) {
			System.err.println("Il grado di parallelismo deve essere > 0.");
			return;
		}
		
		try {
			fis = new FileInputStream(args[2]);
			dis = new DataInputStream(fis);
			fos = new FileOutputStream(args[3]);
			dos = new DataOutputStream(fos);
			m = dis.readInt();
			n = dis.readInt();
			// La scrittura di n ed m sul file di output e' utile se si vuole
			// poi verificare se gli array sono stati ordinati correttamente
			dos.writeInt(m); 
			dos.writeInt(n);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		if (nBuckets > n) { // Si e' scelto un numero di bucket troppo alto
			nBuckets = n;
		}
		// Fine controllo dei parametri di input
		
		// Istanzio lo skeleton
		map =  new Map<Task, Double[]>(new SplitBucket(), new SortProcessor(), new MergeArrays());
		skandium = new Skandium(parDegree);
		stream = skandium.newStream(map);
		
		// Creo lo stream di array da ordinare
		taskStream = new Task[m];
		for (i = 0; i < m; i++) {
			a = new Double[n];
			for (j = 0; j < n; j++) {
				a[j] = dis.readDouble();
			}
			taskStream[i] = new Task(a, nBuckets, parDegree);
		}
		
		time = System.currentTimeMillis();
		
		// Mando gli array da ordinare allo skeleton
		futures = stream.input(taskStream);
		
		// Rilevo la fine delle computazioni
		for (i = 0; i < m; i++) {
			futures[i].get();
		}
		
		System.out.println("[Buckets Number = " + nBuckets + " - Parallelism Degree = " + parDegree + "] Time Elapsed (BucketSortMap): " + (System.currentTimeMillis() - time));
		
		// Scrivo gli array ordinati sul file di output
		for (i = 0; i < m; i++) {
			writeArray = futures[i].get();
			
			for (j = 0; j < writeArray.length; j++) { 
				dos.writeDouble(writeArray[j]);
			}
		}
		dos.close();
		fos.close();
	
		System.exit(0);
	}
}