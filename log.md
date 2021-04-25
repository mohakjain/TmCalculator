# Change Log

This week:

I wrote up a preliminary writeup. Todos include potentially more testing, and cleaning up the code.

Last Week:

Found some test cases; implemented a thing that switches which nearest-neighbor model MELTING uses to crunch these melting temps. Seems to work pretty well. More test cases would be nice though. 


Earlier: 

jaligner issue is fixed. We now have a totally working model. I'm going to look for feedback and test cases before wrapping this thing up.


This should in theory work, but I have no easy way of testing the jaligner functionality that the TmCalculator's previous version used. Once I have some idea of how to approach this, I can test my implementation further. As it stands, we use the MELTING 5.2.0 jar to calculate the melting temperature for the input DNA duplex.

This works out of the box for duplexes of the same length (even with mismatches), but when they are not of the same length, we encounter some trouble. Here I aim to use jaligner to achieve a sequence where we have correct alignment and such.

I'm looking for advice on how to integrate jaligner, since I can't figure out how to test what it even does.
