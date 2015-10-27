package bucketSortMap;

import cl.niclabs.skandium.muscles.Merge;

public class MergeArrays implements Merge<BucketSeq, Double[]>{
	
	@Override
	public Double[] merge(BucketSeq[] bucketSetArray) throws Exception {
		int i, j, k, length;
		Double[] bucket;
		Double[] sortedArray;
		
		// Calcolo la dimensione dell'lunghezza dell'array
		// come somma delle lunghezze di tutti i bucket
		// che verranno poi giustapposti
		length = 0;
		for (i = 0; i < bucketSetArray.length; i++) {
			for (j = 0; j < bucketSetArray[i].size(); j++) {
				bucket = bucketSetArray[i].getBucket(j);
				length += bucket.length;
			}
		}
		
		// Istanzio l'array con la dimensione appena calcolata
		sortedArray = new Double[length];
		
		// Giustappongo i bucket in modo da ottenere l'array
		// complessivamente ordinato
		k = 0;
		for (i = 0; i < bucketSetArray.length; i++) {
			for (j = 0; j < bucketSetArray[i].size(); j++) {
				bucket = bucketSetArray[i].getBucket(j);
				System.arraycopy(bucket, 0, sortedArray, k, bucket.length);
				k += bucket.length;
			}
		}
		
		return sortedArray;
	}
}