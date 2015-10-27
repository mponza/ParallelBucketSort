package randomDoubleArrays;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class RandomDoubleArrays {
	public static void main(String[] args) throws IOException {
		int n = Integer.parseInt(args[0]); // Dimensione di ogni array
		int m = Integer.parseInt(args[1]); // Numero di array
		FileOutputStream fos = new FileOutputStream(args[2]);
		DataOutputStream dos = new DataOutputStream(fos);
		
		dos.writeInt(m);
		dos.writeInt(n);
		
		Random r = new Random();
		for (int i = 0; i < m; i++) {
			
			// Ad ogni ogni iterazione genero un Double casuale con valore compreso
			// nell'intervallo [-100; 100] e lo scrivo su file
			for (int j = 0; j < n; j++) {
				Double random = (r.nextInt(2) == 0 ? -1 : 1) * ((r.nextDouble()) * 100);
				dos.writeDouble(random);
			}
		}
		dos.close();
		fos.close();
	}
}