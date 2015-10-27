package bucketSortMap;

import cl.niclabs.skandium.muscles.Split;

import java.util.ArrayList;
import java.util.Arrays;

public class SplitBucket implements Split<Task, BucketSeq> {
	
	@Override
	public BucketSeq[] split(Task t) throws Exception {
		int from, ki, to = 0;
		int i, j;
		int nBuckets = t.getNBuckets();
		int parDegree = t.getParDegree();
		Double[] a = t.getArray(); // Array da ordinare
		BucketSeq[] buckets = new BucketSeq[nBuckets]; // Ogni suo elemento sara' un BucketSeq con un solo bucket
		BucketSeq[] mergedBuckets; // Usato per passare ad un worker piu' bucket nel caso in cui nBuckets > parDegree.
		ArrayList<Double[]> balance; // Usato per il bilanciamento del numero di bucket in ogni
						// insieme di bucket nel caso in cui nBuckets > parDegree
						// (Questa struttura verra' usata come una coda)
		
		// Istanzio l'array di BucketSeq
		for (i = 0; i < nBuckets; i++) {
			buckets[i] = new BucketSeq();
		}
		
		// Ad ogni iterazione viene selezionato il ki-esimo elemento piu' piccolo dell'array, mettendo
		// gli elementi piu' piccoli alla sua sinistra e i piu' grandi alla sua destra utilizzando
		// l'algoritmo quickselect. Dopo cio', viene effettuata una copia della porzione dell'array
		// dal ki + 1-esimo elemento (tranne nel caso in cui i == 0 (in tal caso ki + 1 == 0)) della
		// precedente iterazione (valore della variabile from) fino al ki-esimo elemento dell'iterazione
		// corrente. La porzione di array copiata, che costituisce il bucket, viene inserita in buckets[i].
		for (i  = 0; i < nBuckets; i++) {
			
			// Limite inferiore della porzione di array in cui cercare il ki-esimo elemento piu' piccolo
			from = a.length/nBuckets * i;
			// ki-esimo elemento piu' piccolo da cercare
			ki = a.length/nBuckets * (i + 1) - 1;
			
			if (i == nBuckets - 1) { // L'ultimo bucket potra' essere piu' grande degli altri
				ki = a.length - 1;
			} else {
				// Partiziono l'array in modo che in a[ki] ci sia il ki-esimo elemento piu' piccolo.
				// L'algoritmo quickselect ci garantisce che, al termine della sua esecuzione, a
				// sinistra di ki ci saranno tutti gli elementi <= a[ki] e alla sua destra tutti
				// gli elementi >= a[ki].
				QuickSelect.select(a, from, ki, a.length - 1);			
			}
			buckets[i].addBucket(Arrays.copyOfRange(a, from, ki + 1));
		}
		
		// Ci sara' almeno un worker che dovra' ordinare piu' bucket
		if (nBuckets > parDegree) { 
			
			// Istanzio il nuovo array di BucketSeq
			mergedBuckets = new BucketSeq[parDegree];
			for (i = 0; i < mergedBuckets.length; i++) {
				mergedBuckets[i] = new BucketSeq();
			}
			
			// Inserisco uno o piu' bucket in mergedBuckets[i], cercando di bilanciare il numero di bucket
			// presenti in ogni mergedBuckets[i]. Tutti i mergedBuckets[i] conterranno lo stesso numero di
			// bucket, escluso al piu' l'ultimo, che ne potra' contenerne piu' degli altri.
			for (i = 0; i < parDegree; i++) {
				
				// Posizione del primo bucket da inserire in mergedBuckets[i]
				from = nBuckets/parDegree * i;
				// Posizione dell'ultimo bucket da inserire in mergedBuckets[i]
				to = nBuckets/parDegree * (i + 1) - 1;
				
				if (i == parDegree - 1) { // L'ultimo BucketSeq potra' contenere piu' bucket rispetto agli altri
					to = buckets.length - 1;
				}
				
				// Inserisco in mergedBuckets[i] tutti i bucket dall'indice from all'indice to
				// presenti nell'array buckets.
				for (j = from; j <= to; j++) {
					mergedBuckets[i].addBucket(buckets[j].getBucket(0));
				}
			}
			
			// Aggiusto il balancing dei bucket in mergedBuckets visto che l'ultimo BucketSeq puo'
			// contenere un numero maggiore di bucket rispetto agli altri.
			if (parDegree != 1) {

				// Numero di bucket da togliere all'ultimo BucketSeq
				int load = mergedBuckets[parDegree - 1].size() - mergedBuckets[parDegree - 2].size();
					
				// Se la differenza tra il numero di bucket presenti nei BucketSeq (tranne l'ultimo) e
				// l'ultimo BucketSeq e' maggiore della meta' del numero di bucket presenti nei BucketSeq (tranne l'ultimo)
				//
				// (Tutti i BucketSeq fino a parDegree - 2 contengono lo stesso numero di bucket)
				if (load >= mergedBuckets[parDegree - 2].size()/2) {
					
					// Conterra' i bucket che potranno essere assegnati ai vari BucketSeq (tranne l'ultimo)
					balance = new ArrayList<Double[]>();
					
					// Rimuovo dall'ultimo bucket i load bucket con i valori piu' piccoli aggiungendoli a balance
					for (i = 0; i < load; i++) {
						balance.add(mergedBuckets[parDegree - 1].getAndRemove(0));
					}
					
					// Aggiungo ai load BucketSeq precedenti all'ultimo un bucket a testa
					// in modo da bilanciare meglio il carico di lavoro dei worker
					for (i = parDegree - 2; i >= 0; i--) {
						
						// Quanti bucket potra' al massimo contenere l'i-esimo BucketSeq
						// con il nuovo bilanciamento
						int maxSize = mergedBuckets[i].size() + 1;
						
						// Si rimuovono tutti i bucket dall'i-esimo BucketSeq, aggiungendoli
						// in testa a balance. L'ordine di rimozione/aggiunta dei bucket e'
						// dall'ultimo al primo bucket contenuto nel i-esimo BucketSeq.
						// In questo modo in balance sara' rispettata la relativa proprieta'
						// del Bucket Sort.
						
						// Rimuovo i bucket e li aggiungo in testa a balance
						int actualSize = mergedBuckets[i].size();
						for (j = 0; j < actualSize; j++) {
							balance.add(0, mergedBuckets[i].getAndRemove(mergedBuckets[i].size() - 1));
						}
						
						// Aggiungo i bucket all'i-esimo BucketSeq e, una volta aggiunti, li rimuovo da balance
						j = 0;
						while (j < maxSize && balance.size() != 0) {
							mergedBuckets[i].addBucket(0, balance.get(balance.size() - 1));
							balance.remove(balance.size() - 1);
							j++;
						}
					}
				}
			}
			
			return mergedBuckets;
		}
		
		return buckets;
	}
}