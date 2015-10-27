package bucketSortMap;

import cl.niclabs.skandium.muscles.Execute;

public class SortProcessor implements Execute<BucketSeq, BucketSeq> {
	
	private void selectionSort(Double [] a) {
		int minIdx;
		Double tmp;
		 
		for (int i = 0; i < a.length; i++) {
			minIdx = i;
			for (int j = i + 1; j  < a.length; j++) {
				if (a[minIdx] > a[j]) {
						minIdx = j;
				}
			}
			tmp = a[minIdx];
			a[minIdx] = a[i];
			a[i] = tmp;
		}
	}
	 
	@Override
	public BucketSeq execute(BucketSeq bucketSeq) throws Exception {
		
		// Ordina sequenzialmente i bucket presenti in bucketSeq
		for (int i = 0; i < bucketSeq.size(); i++) {
			selectionSort(bucketSeq.getBucket(i));
		}
		
		return bucketSeq;
	}
}