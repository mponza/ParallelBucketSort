package sequentialBucketSort;

import java.util.ArrayList;

/**
 * Sequenza di bucket che devono essere ordinati da un worker
 */
public class BucketSeq { 
	
	private ArrayList<Double[]> buckets; // bucket da ordinare
	
	public BucketSeq() {
		this.buckets = new ArrayList<Double[]>();
	}
	
	public void addBucket(Double[] bucket) {
		buckets.add(bucket);
	}
	
	public void addBucket(int i, Double[] bucket) {
		buckets.add(i, bucket);
	}
	
	public int size() {
		return buckets.size();
	}
	
	public Double[] getBucket (int i) {
		return buckets.get(i);
	}
	
	public Double[] getAndRemove(int i) {
		Double[] bucket;
		
		bucket = buckets.get(i);
		buckets.remove(i);
		
		return bucket;
	}
}