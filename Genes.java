
/**
 * Write a description of class findStopCodon here.
 * 
 * @author Zhanibek 
 * @version 1.0
 */
import edu.duke.StorageResource;
import edu.duke.FileResource;


public class findStopCodon {
    public int findStopCodon(String dnaStr, int startIndex, String stopCodon) {
        // Find the stop codon, start searching from start + 3 since start codon is 3 char's long
        int currIndex = dnaStr.indexOf(stopCodon, startIndex+3);
        while (currIndex != -1) {
            int diff = currIndex - startIndex;
            if (diff % 3 == 0) {
                // If it's divisible by 3, then it's a valid gene
                return currIndex;
            }
            else {
                // If it's not a valid gene, start looking for a new stopCodon, starting from + 1
                currIndex = dnaStr.indexOf(stopCodon, currIndex+1);
            }
        }
   
        return -1; // return negative one if nothing was found
    }
    
    public String findGene(String dna, int where) {
        int startIndex = dna.indexOf("ATG", where);
        while (true) {
            startIndex = dna.indexOf("ATG", where);
            if (startIndex == -1) {
                return ""; //return empty string if gene start not found
            }
            int taaIndex = findStopCodon(dna,startIndex,"TAA");
            int tagIndex = findStopCodon(dna,startIndex,"TAG");
            int tgaIndex = findStopCodon(dna,startIndex,"TGA");
            //int temp = Math.min(taaIndex, tagIndex);
            //int minIndex = Math.min(temp, tgaIndex);
            int minIndex = 0;
            if (taaIndex == -1 || 
                (tgaIndex != -1 && tgaIndex < taaIndex)) {
                minIndex = tgaIndex;
            }
            else {
                minIndex = taaIndex;
            }
            
            if (minIndex == -1 || 
                (tagIndex != -1 && tagIndex < minIndex)) {
                minIndex = tagIndex;
            }
            
            
            if (minIndex == -1) {
                where = where + 1; // To try new start  codon
            }
            
            return dna.substring(startIndex, minIndex+3); //+3 to include stop codon
        }
    }
    
    
    public void printAllGenes(String dna) {
        int startIndex = 0;
        int count = 0;
        while ( true ) {
            String currentGene = findGene(dna, startIndex);
            if (currentGene.isEmpty()) {
                System.out.println("Count of found genes: " + count);
                break;
            }
            count = count + 1;
            System.out.println(currentGene);
            startIndex = dna.indexOf(currentGene, startIndex) + currentGene.length();
        }
        
    
    }
    
   
    public StorageResource getAllGenes(String dna) {
        dna = dna.toUpperCase();
        StorageResource geneList = new StorageResource();
        
        int startIndex = 0;
        //int count = 0; For counting
        while ( true ) {
            String currentGene = findGene(dna, startIndex);
            if (currentGene.isEmpty()) {
                //System.out.println("Count of found genes: " + count); for counting
                break;
            }
            //count = count + 1; for counting
            geneList.add(currentGene);
            startIndex = dna.indexOf(currentGene, startIndex) + currentGene.length();
        }
        return geneList;
    }
    
    public float cgRatio(String dna) {
        int countC = 0;
        int countG = 0;
        int startIndex = 0;
        while (true) {
            int findIt = dna.indexOf("C", startIndex);
            if (findIt == -1) {
                break;
            }
            countC = countC + 1;
            startIndex = findIt + 1;
        }
        startIndex = 0;
        while (true) {
            int findIt = dna.indexOf("G", startIndex);
            if (findIt == -1) {
                break;               
            }
            countG = countG + 1;
            startIndex = findIt + 1;
        }
        float ratio = (float) countC/countG;
        return ratio;
    }
    
    
    public void processGenes(StorageResource sr) {
        int countLong = 0;
        System.out.println("Printing all strings that logner than 9: ");
        for (String g: sr.data()) {
            if (g.length() > 9) {
                countLong = countLong + 1;
                System.out.println(g);                
            }
        }
        System.out.println("Count of strings higher than 9: " + countLong);
        
        int countRatio = 0;
        float geneRatio = 0;
        System.out.println("Printling all strings with cgRatio higher than 0.35: ");
        for (String g: sr.data()) {
            geneRatio = cgRatio(g);
            if (geneRatio > 0.35) {
                countRatio = countRatio + 1;
                System.out.println(g);
            }
        }
        System.out.println("Count of strings with cgRatio higher than 0.35: " + countRatio);
        
        int longestGene = 0;
        for (String g: sr.data()) {
            if (g.length() > longestGene) {
                longestGene = g.length();
            }
        }
        System.out.println("The longest gene length is: " + longestGene);
    }
    
    public void testProcessGenes() {
        /*
        FileResource fr = new FileResource("brca1line.fa");
        String dna = fr.asString();
        StorageResource file = getAllGenes(dna);
        processGenes(file);
        */
        String dna = "oneAtGMyGeneOneAATGGGGTAATGATAGAACCCGGYGGGGTAGGGCTGCCCATGendOneTAAnonCodingDnaTAGTGAZZZtaaTwoATGMyGeneTwoCATGGGGTAATGATAGCCatgCCCFalseStartTAATGATGendTwoTAGnonCodingDNATAACCCThreeATGMyGeneThreeATGGGGTAATGATAGATGccendThreeTAAnonCodingDNAccTAAfalsecccFourATGMyGeneFourATGGGGTAATGATAGCendFourTAGnonCodingdnaFiveAtgMyGeneFiveATGGGGTAATGATAGCendFiveTGAnonCodingdnaSixATGmyGeneSixATATGGGGTAATGATAGAendSixTAAnoncodingdnaSevenATGMyGeneSevenCcATGGGGTAATGATAGendSeventaAnoncodingdnaEightATGmyGeneEightATGGGGTAATGATAGGGendEighttaAnoncodingdnaCcccWrongtgaCtaaCtagCCcgNineATgmyGeneNineATGGGGTAATGATAGTaaAendNineTAAnonCodingDnaCcccTenATGmyGeneTenGATGGGGTAATGATAGCCHasFakeATGFAKEatgcendTentaanonCodingDnaCtagCtganonCodingDnaxxxElevenATGmyGeneElevenCATGGGGTAATGATAGTAAxxGeneATGTAACATGTAAATGCendElevenTAAnonCodingDnaxTAGxtgaTwelveATGmyGeneTwelveCATGGGGTAATGATAGCTheLastGeneIsATGTAGendTwelvetgaATGTAG";
        String dna2 = "CTGONEAtGONEcCCGGGAAAXXXYYYGGGGTAGYYCTGCCCATGENDZZZTAAONEXXXYYYZZZTAAXXxXXTWOATGTWOYYYZZZCCCATGATGENDZZZTAGTWOXXTHREEATGATGTAATHREESTOPTAGAGGGCCCCCFOURATGTAGGXXXFIVEAtgYYYFIVZZZAAAXXXFIVENDZZZTGAFIVESTOPSIXATGSIXCGGGCCGGGATCAAASIXENDTAASEVATGSIXCGGGCCGGGATCAAASEVENDENDtaAEIGSTOPTAGAGLASTONEATgtAACTG";
        //StorageResource file = getAllGenes(dna);
        dna = dna.toUpperCase();
        printAllGenes(dna2);
    }
    
    
    public void testCgRatio() {
        System.out.println("Testing cgRatio with in ATGAACGAATTGAATC");
        System.out.println(cgRatio("ATGAACGAATTGAATC"));
        
        System.out.println("Testing cgRatio with ATGCCATAG");
        System.out.println(cgRatio("ATGCCATAG"));
    }
    
    
    public void testOn(String dna) {
        System.out.println("Testing printAllGenes on "+ dna);
        StorageResource genes = getAllGenes(dna);
        for (String g: genes.data()) {
            System.out.println(g);
        }
    }
    
    public void test() {
        //      ATG   TAA   ATG         TGA
        testOn("ATGATCTAATTTATGCTGCAACGGTGAAGA");
        //      012345678901234567890123456789
        testOn("");
        //      ATG            TAA         ATGTAA
        testOn("ATGATCATAAGAAGATAATAGAGGGCCATGTAA");
    }
    
    public void testFindStop() {
        //            01234567890123456789012345
        String dna = "xxxyyyzzzTAAxxxyyyzzzTAAxx";
        int dex = findStopCodon(dna, 0, "TAA");
        if (dex != 9) System.out.println("error on 9");
        
        dex = findStopCodon(dna, 9, "TAA");
        if (dex != 21) System.out.println("error on 21");
        
        dex = findStopCodon(dna, 1, "TAA");
        if (dex != -1) System.out.println("error on 26");
        
        dex = findStopCodon(dna, 0, "TAG");
        if (dex != -1) System.out.println("error on 26 TAG");
        
        System.out.println("tests finished");
    }
    
    public void testFindGene() {
        //            012345678901234567
        String dna = "ATGCCCGGGAAATAACCC";
        String gene = findGene(dna, 0);
        if (! gene.equals("ATGCCCGGGAAATAA")) {
            System.out.println("error");
        }
        System.out.println("tests finished");
    }
}
