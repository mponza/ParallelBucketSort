ParallelBucketSort
==================

ParallelBucketSort is a final project of the Parallel and Distributed Computing course, whose goal was to design and implement a parallel and high-scalable version of the [Bucket sort] (https://en.wikipedia.org/wiki/Bucket_sort) sorting algorithm by using [Skandium](http://skandium.niclabs.cl/).

Additionally, in the current repository you can find the laboratory report (italian only).


Setting up
------------

The project can be compiled by typing:

	ant

Input File Generation
------------------------

In order to generate the input files (random floating-points array), you have to type:

	ant RandomDoubleArrays -Dn=nValue -Dm=mValue

where `nValue` and `mValue` are the size of each array and the number of arrays you want to generate, respectively.

Running
---------
The generated array can be sorted by typing:

	ant BucketSortAlgorithm -Dnb=nBuckets -Dpg=parDegree

where `BucketSortAlgorithm` can be:

- `SequentialBucketSort`, for running the sequential version of the BucketSort algorithm;
- `BucketSortMap`, for running the parallel version of the BucketSort algorithm;

and `nBuckets` is the number of buckets, `parDegree` is the parallel degree.

Sorting Checking
--------------------
In order to check the correct sorting of the input arrays, you can type:

	ant AreSorted
