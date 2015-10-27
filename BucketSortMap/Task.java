package bucketSortMap;

/**
 * Implementa il parametro di input dello skeleton.
 * */
public class Task {
	private Double[] a; // Array da ordinare
	private int nBuckets;  // Numero di bucket in cui dovra' essere suddiviso l'array 	
	private int parDegree; // Grado di parallelismo richiesto
	
	public Task(Double[] a, int nBuckets, int parDegree) {
		this.a = a;
		this.nBuckets = nBuckets;
		this.parDegree = parDegree;
	}
	
	public Double[] getArray() {
		return a;
	}
	
	public int getNBuckets() {
		return nBuckets;
	}

	public int getParDegree() {
		return parDegree;
	}
}
