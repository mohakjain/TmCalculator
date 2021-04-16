import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import jaligner.Alignment;
import jaligner.Sequence;
import jaligner.SmithWatermanGotoh;
import jaligner.matrix.Matrix;
import jaligner.matrix.MatrixLoader;
import java.util.ArrayList;

/**
 * author @mohakjain
 * 
 */


public class TmCalculator {

    public void initiate() throws Exception {
    
    }

    /** Run my temperature checker. 
     * We take in char[] inputs as the sequence and complementary sequence.
    */
    public double run(char[] S1, char[] S2) throws IOException, InterruptedException {
      
        // We specify the location of the folder containing the MELTING5 jar.
        String path_to_melting_executable = "./MELTING5.2.0/executable/";
        

        // I was unable to determine how to run the melting5 jar as a library. 
        // As such I decided to take this approach which calls the jar file using a java process.
        String[] meltingArgs = {"java", "-jar", "melting5.jar",
                                "-S",  String.valueOf(S1),
                                "-H", "dnadna",
                                "-P", "50e-8",
                                "-E", "Na=0.05", 
                                "-C", String.valueOf(S2)
                            };
        // Arguments above are all defaults. 500nm primer concentration, Na=0.05 looks like the default too.

        // Start a process to deal with MELTING5.
        ProcessBuilder pb = new ProcessBuilder(meltingArgs);
        pb.directory(new File(path_to_melting_executable));
        Process proc = pb.start();
        proc.waitFor();
        
        // Then retrieve the process output
        InputStream in = proc.getInputStream();
        InputStream err = proc.getErrorStream();
        
        // print out the outputs from MELTING5
        byte results[]=new byte[in.available()];
        in.read(results, 0, results.length);        
        System.out.println(new String(results));
        
        // print out any errors from MELTING5; Not sure if this is necessary.
        byte c[]=new byte[err.available()];
        err.read(c,0,c.length);
        System.out.println(new String(c));
        
        // Process MELTING5's output string:
        String Tm_str = new String(Arrays.copyOfRange(results,  results.length-22, results.length-13));
        while ( !(Character.isDigit(Tm_str.charAt(0)) || (Tm_str.charAt(0) == '-')) ) {
            Tm_str = Tm_str.substring(1, Tm_str.length());
          
        }


        // We may error if the 
        if (Tm_str.length() == 0){
            System.out.println("Encountered fatal error!!");
            return 0.0;
        }

        // Return as double the melting temperature.
        double Tm = Double.parseDouble(Tm_str);
        
        return Tm;
    }

    /** MELTING5, the library I employ for this task demands that the complement string is given as a complement.
     * This function takes in a char[] DNA sequence and returns the complement of it in char[] format. 
     * Any non-ATCG characters are preserved.
    */
    public static char[] complement(char[] S) {
        
        char[] S_comp = S.clone();
        for (int i = 0; i < S.length; i++) {
            if (S[i] == 'A') {
                S_comp[i] = 'T';
            } else 
            if (S[i] == 'T') {
                S_comp[i] = 'A';
            } else 
            if (S[i] == 'C') {
                S_comp[i] = 'G';
            } else 
            if (S[i] == 'G') {
                S_comp[i] = 'C';
            }
            else {
                S_comp[i] = S[i];
            }
        }

        return S_comp;
    }

    public static void main(String[] args) throws Exception {
        String s1 = "CCCCCCCCCCCC".toUpperCase();  
        String s2 = "CCCCCCCCCCCC".toUpperCase();
        
        // input dependent: if spaces appear in the sequence we should replace them with hyphens ("-")
        // s1 = s1.replaceAll("\\s", "-");
        // s2 = s2.replaceAll("\\s", "-");
        
        Sequence seq1 = new Sequence(s1);
        Sequence seq2 = new Sequence(s2);

        Alignment alignment = SmithWatermanGotoh.align(seq1, seq2, MatrixLoader.load("EDNAFULL_1"), 10f, 0.5f);
        
        char[] S1 = alignment.getSequence1();
        char[] S2 = alignment.getSequence2();

        // // comment out below when you can figure out the jaligner stuff:
        // char[] S1 = s1.toCharArray(); 
        // char[] S2 = s2.toCharArray();
        
        // Complement S2 because that's what MELTING likes:
        S2 = complement(S2);

        // Print what the alignments actually look like:
        System.out.println(S1);
        System.out.println(S2);
        
        TmCalculator tmCalculator = new TmCalculator();
        tmCalculator.initiate();
        double result = tmCalculator.run(S1, S2);
        System.out.println(result);
        
    }
}




// // OLD VER:

// /**
//  *
//  * @author yishe
//  */
// public class TmCalculator {


//     private double Adjustment = 60.0; 
    
//     public void initiate() throws Exception {
    
//     }
    
    
//     public double run(char[] S1, char[] S2) {
   
    
//     double percentGC = computesPercentGC(S1);
//     double percentMismatching = computesPercentMismatching(S1, S2);
//     double duplexLength = S1.length; 
//     double NaConcentration = 0.1;
    
//     //double Tm = 81.5 + 16.6 * Math.log10(NaConcentration / (1.0 + 0.7 * NaConcentration)) + 0.41 * percentGC - 500.0 / duplexLength - percentMismatching;
    
//     double Tm = Adjustment + 0.41 * percentGC - 500.0 / duplexLength - percentMismatching;
    
//     return Tm;
//     }
    

        
//         /**
// 	 * computes the percentage of GC base pairs in the duplex of NucleotidSequences.
// 	 * @return double percentage of GC base pairs in the duplex.
// 	 */
// 	private double computesPercentGC(char[] S1){
            
// 		double numberGC = 0.0;
		
// 		for (int i = 0; i < S1.length;i++){
//                     char Base = S1[i];
// 			if (Base == 'G' || Base == 'C'){
// 				numberGC++;
// 			}
// 		}
		
// 		return numberGC / (double)S1.length * 100.0;
// 	}
	
// 	/**
// 	 * Computes the percentage of mismatching base pairs in the duplex of NucleotidSequences.
// 	 * @return double percentage of mismatching base pairs in the duplex
// 	 */
// 	private double computesPercentMismatching(char[] S1, char[] S2){
// 		double numberMismatching = 0.0;
	
// 		for (int i = 0; i < S1.length;i++){
// 			char Base1 = S1[i];
//                         char Base2 = S2[i];
// 			if (Base1 != Base2){
// 				numberMismatching++;
// 			}
// 		}
// 		return numberMismatching / (double)S1.length * 100.0;
// 	}
        
    
    
// }    

