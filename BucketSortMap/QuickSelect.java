package bucketSortMap;

import java.util.Random;

public class QuickSelect {
	
	/**
	 * Scambia a[i] con a[j].
	 */
	private static void swap(Double[] a, int i, int j) {
		Double tmp;
		tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}
	
	/**
	 * Partiziona l'array a mettendo a sinistra di a[p] tutti gli elementi <= a[p]
	 * e a destra tutti gli elementi >= a[p].
	 */
	private static int partition(Double[] a, int l, int p, int r) {
		if (p != r) { // Metto a[p] in fondo alla porzione di array da partizionare
			swap(a, p, r);
		}
		
		Double x = a[r];
		int i = l - 1; // i conterra' l'indice piu' alto della porzione di array contenente elementi <= x
		for (int j = l; j < r; j++) {
			if (a[j] <= x) { // scambio a[i + 1] con a[j]
				i++;
				swap(a, i, j);
			}
		}
		swap (a, i + 1, r);
		
		return i + 1;
	}
	
	/**
	 * Trova l'f-esimo elemento piu' piccolo in un array disordinato in tempo medio O(n).
	 * Al termine della procedura, l'array conterra', a sinsitra di a[f], tutti gli
	 * elementi <= a[f] e alla sua destra tutti gli elementi >= a[f].
	 * 
	 * @param a array in cui cercare l'f-esimo elemento piu' piccolo
	 * @param l limite sinistro della porzione di array a in cui cercare l'f-esimo elemento piu' piccolo
	 * @param f indice dell'elemento da trovare
	 * @param r limite destro della porzione di array a in cui cercare l'f-esimo elemento piu' piccolo
	 * @return  il valore dell'f-esimo elemento piu' piccolo
	 */
	public static Double select(Double[] a, int l, int f, int  r) {
		if (l == r) {
			return a[l];
		}
		int pivot = l + (new Random()).nextInt(r - l); // Seleziona casualmente il perno
		
		// Partiziona l'array mettendo a sinsitra gli elementi minori o uguali di a[pivot]
		// e alla sua destra quelli maggiori o uguali di a[pivot] 
		pivot = partition(a, l, pivot, r); 
		
		if (f == pivot) { // Ho trovato l'elemento che stavo cercando
			return a[pivot];
		}
		if (f < pivot) { // Sto cercando un elemento che sta prima della pivot-eima posizione
			return select(a, l, f, pivot - 1);
		} else { // Sto cercando un elemento che sta dopo la pivot-esima posizione
			return select(a, pivot + 1, f, r);
		}
	}
	
}
