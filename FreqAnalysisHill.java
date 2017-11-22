import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class FreqAnalysisHill {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filenameCiphertext = "src/ciphertext.txt";
		String filenameFreqtable = "src/Jones2004_Bigram.txt";
		HashMap<String,Integer> digrams, sortedDigrams, encodedDigrams, sortedEncodedDigrams;
		KnownPlainTextHill kpth = new KnownPlainTextHill();
		Hill hill = new Hill();
		int i, m;
		int dim=5;
		FreqAnalysisHill fah = new FreqAnalysisHill();
		digrams = fah.loadMap(filenameFreqtable);
		sortedDigrams = MapUtil.sortByValue(digrams,dim);
		System.out.println(sortedDigrams.keySet() + " " + sortedDigrams.values());
		encodedDigrams = fah.loadCipher(filenameCiphertext);
		sortedEncodedDigrams = MapUtil.sortByValue(encodedDigrams, dim);
		System.out.println(sortedEncodedDigrams.keySet() + " " + sortedEncodedDigrams.values());
		String[] plaintext = fah.extractStrings(sortedDigrams, dim);
		String[] ciphertext = fah.extractStrings(sortedEncodedDigrams, dim);
		String[] array = fah.knownPlainTextFreq(hill, kpth, plaintext, ciphertext);
		String allText = fah.allText(filenameCiphertext);
		
		fah.result(allText, hill, array);
	}
	
	public void result(String text, Hill hill, String[] array){
		for(String key: array) {
			if(hill.checkKey(key)) {
				hill.setKey(key);
			String decodedText = hill.Dec(text);
			if(decodedText.contains("the ") && decodedText.contains("be ") && decodedText.contains("of ") && decodedText.contains("to ") ) {
				System.out.println("CHIAVE: " +  key + " " +decodedText);
				break;
			}
			}
		}
	}
	
	public String[] knownPlainTextFreq(Hill hill, KnownPlainTextHill kpth, String[] p, String[] c) {
		int k=0;
		String[] key = new String[p.length * c.length];
		
		for(String string1: p) {
			for(String string2: c) {
				key[k] = kpth.knownPlainText(hill, string1, string2);
				k++;
			}
		}
		
		return key;
	}
	
	public String[] extractStrings(HashMap<String,Integer> digrams, int dim) {
		String[] results = new String[dim*(dim-1)];
		int i=0;
		
		for(String key1 : digrams.keySet()) {
			for(String key2 : digrams.keySet()) {
				if (key1!=key2) {
					results[i] = key1 + key2;
					i++;
				}
			}
		}
		
		return results;
	}
	
	public String allText(String filename) {
		File fileCiphertext = new File(filename);
        
        FileReader fr = null;
		try {
			fr = new FileReader(fileCiphertext);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        BufferedReader lines = new BufferedReader(fr);
        String cipher = new String();
        String line=null;
       
        
        while(true){
        		try {
					line = lines.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		if (line== null) {
        			break;
        		}
        		cipher = cipher.concat(line);				//cipher contiene tutto il testo su una sola linea
        }
        
        if(cipher.length() %2 !=0 ) {
        		cipher = cipher.concat(" ");				//se la lunghezza di cipher è dispari aggiungo un carattere
        }
        return cipher;
	}
	
	public HashMap<String,Integer> loadCipher(String filename){
		HashMap<String, Integer> ciphertext = new HashMap<String, Integer>();
		File fileCiphertext = new File(filename);
        
        FileReader fr = null;
		try {
			fr = new FileReader(fileCiphertext);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        BufferedReader lines = new BufferedReader(fr);
        int i;
        String cipher = new String();
        Character prec,succ;
        String line=null;
       
        
        while(true){
        		try {
					line = lines.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		if (line== null) {
        			break;
        		}
        		cipher = cipher.concat(line);				//cipher contiene tutto il testo su una sola linea
        }
        
        if(cipher.length() %2 !=0 ) {
        		cipher = cipher.concat(" ");				//se la lunghezza di cipher è dispari aggiungo un carattere
        }
        
        for(i= 0; i<cipher.length()-1; i++) {
        		prec = cipher.charAt(i);
	        succ = cipher.charAt(i+1);
	        String digram = prec.toString() + succ.toString();
	        if (ciphertext.containsKey(digram)) {
	        		int value = ciphertext.get(digram);
	        		ciphertext.put(digram, value+1);
	        }else {
	        		ciphertext.put(digram, 1);
	        }        	
      }
		return ciphertext;
}
       
	//escludiamo la lettura delle colonne che non servono
	public HashMap<String, Integer> loadMap(String filename) {
		String alphabet = "abcdefghijklmnopqrstuvwxyz ',";
		HashMap<String, Integer> mostFrequentsel = new HashMap<String, Integer>();  
		
        //Creo un nuovo file con Jones2004_Bigram.txt
        File frequencies = new File("src/Jones2004_Bigram.txt");
        if ( !frequencies.exists() ){ 
            System.out.println("Jones2004_Bigram.txt non esiste!");
        }
        FileReader fr = null;
		try {
			fr = new FileReader(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        BufferedReader lines = new BufferedReader(fr);
        
        String prec,succ,web;
        Character carattere;
        int i;
        String []v = new String[128];
                        
        while(true){
        		i = 0;
            String line = null;
			try {
				line = lines.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}          
            if (line==null){
                break;
            }            
            v = line.split("\\s+");
            if(filename.contains("Bigram")) {
	            if(alphabet.contains(v[i]) && alphabet.contains(v[i+1])){
	            		prec = v[i];
	            		succ = v[i+1];
	            		String digram = prec + succ;
	            		web = v[i+6];
	            		int freqInt = Integer.parseInt(web);
	            		mostFrequentsel.put(digram, freqInt);
	            		}
	            }
        }
        return mostFrequentsel;
	}
}
