# TmCalculator

Currently: 

This should in theory work, but I have no easy way of testing the jaligner functionality that the TmCalculator's previous version used. Once I have some idea of how to approach this, I can test my implementation further. As it stands, we use the MELTING 5.2.0 jar to calculate the melting temperature for the input DNA duplex.

This works out of the box for duplexes of the same length (even with mismatches), but when they are not of the same length, we encounter some trouble. Here I aim to use jaligner to achieve a sequence where we have correct alignment and such.

I'm looking for advice on how to integrate jaligner, since I can't figure out how to test what it even does.
