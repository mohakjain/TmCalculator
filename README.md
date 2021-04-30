# TmCalculator

## Write-up

The ask for this project was relatively straightforward. I was tasked with updating a function used in ConstructionFileSimulator that caluclates the melting temperature of an oligo duplex given as a char[]. I accomplished this by keeping the jaligner framework that was implemented previously to find an appropriate oligo duplex. Jaligner returns DNA sequences with "-" where there are gaps and removes overhang regions. This means we can easily handle duplexes where both oligos are not identical.

The previous implementation to find the melting temperature used a function of GC content, mismatch percentage between the duplex, salt concentration, and oligo length. While this implementation does make sense (and from some review of online resources appears to be in line with what most Tm calculators do anyway), it was often found to be inaccurate. I orginally prototyped some code along these same lines, but found myself dissatisfied with the result and went back to looking online.

To improve on this model I scoured online for libraries that had fully implemented melting temperature calculators already, and encountered MELTING5. This model is the basis for Biopython's melting temperature calculator too, and appears to be a rather standardized tool. I implemented MELTING5 by having the function call the jar file for it as a separate process. The way to directly implement MELTING5 as a normal java library is not clear to me. This could be a further direction for this project, but I chose not to worry about it because I was constrained on time and this implementation works just fine for now.

MELTING5 takes in a DNA duplex, salt concentration (set to a "Na=0.05" as appears to be the default for all Tm calculators I could find), a primer concentration (I used 500nM, which is again pretty standard). The melting temperature is then returned by the run method I implemented.

From here I moved into testing. MELTING5 uses a nearest-neighbor model for calculating melting temperature, with its default parameters coming from a model named all97, coming from Allawi and Santalucia's 1997 paper. But there are an additional set of models with tweaked paramaters also implemented. Namely MELTING5 has support for bre86 (from Breslauer et al. 1986), san04 (from Hicks and Santalucia 2004), san96 (from Santalucia et al. 1996), sug96 (from Sugimoto et al 1996), tan04 (from Tanaka et al. 2004), in addition to all97 (from Allawi and Santalucia 1997).

I did read these papers to find information on which model would be approrpriate in each situation, but practically getting this information just from reading proved to be an unfruitful approach. I turned instead to testing all the models on a set of primers from a standard UPenn Medicine database. These tests were the same as used by the previous author of this function. I evaluated each model's performance based on the GC content and length of these oligos, and wrote a choose_model function that takes these characteristics into account to decide which model MELTING5 should use. At this point we pass all but one of the tests I chose to use (the one test not passed is an extremely short and unusual primer. It's a case I don't feel we would ever encounter in normal biological research use). Still, implementation details for oligos in the 20-40nt range is limited, as this script currently just defaults to the all97 model.

Another place where testing needs to be done is in how well MELTING5 handles calculations when there are mismatches and gaps. In general, I feel that the answers it returns are reasonable, but it's difficult to convinced of its accuracy without some tests to compare to. I did look for these sorts of tests but was unable to encounter anything.

Still, I'm very happy with the place this function is in right now. The code itself is clean and well-documented. It accurately determines which Tm calcuation model to use and then rapidly returns the melting temperature for an oligo duplex. Mission complete!

## Sources

- Allawi, H. T., & SantaLucia, J., Jr (1997). Thermodynamics and NMR of internal G.T mismatches in DNA. Biochemistry, 36(34), 10581–10594. https://doi.org/10.1021/bi962590c

- Breslauer, K. J., Frank, R., Blöcker, H., & Marky, L. A. (1986). Predicting DNA duplex stability from the base sequence. Proceedings of the National Academy of Sciences of the United States of America, 83(11), 3746–3750. https://doi.org/10.1073/pnas.83.11.3746

- JAligner: http://jaligner.sourceforge.net/

- MELTING: https://www.ebi.ac.uk/biomodels-static/tools/melting/

- SantaLucia, J., Jr, & Hicks, D. (2004). The thermodynamics of DNA structural motifs. Annual review of biophysics and biomolecular structure, 33, 415–440. https://doi.org/10.1146/annurev.biophys.32.110601.141800

- SantaLucia, J., Jr, Allawi, H. T., & Seneviratne, P. A. (1996). Improved nearest-neighbor parameters for predicting DNA duplex stability. Biochemistry, 35(11), 3555–3562. https://doi.org/10.1021/bi951907q

- Sugimoto, N., Nakano, S., Yoneyama, M., & Honda, K. (1996). Improved thermodynamic parameters and helix initiation factor to predict stability of DNA duplexes. Nucleic acids research, 24(22), 4501–4505. https://doi.org/10.1093/nar/24.22.4501

- Tanaka, F., Kameda, A., Yamamoto, M., & Ohuchi, A. (2004). Thermodynamic parameters based on a nearest-neighbor model for DNA sequences with a single-bulge loop. Biochemistry, 43(22), 7143–7150. https://doi.org/10.1021/bi036188r

- UPenn Medicine's Standard Primers: https://www.med.upenn.edu/naf/services/standard.html


