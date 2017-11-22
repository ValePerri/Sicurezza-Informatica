import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Hill implements ClassicCipher{
	private int[] intKey = new int[4];
	private String stringKey;
	private BigInteger[][] matrixKey = new BigInteger[2][2];
    public static final Map<Character, Integer> map;
    public static final Map<Integer, Character> inverseMap;
    static
    {	
        map = new HashMap<Character, Integer>();
        inverseMap = new HashMap<Integer, Character>();
        int i;
        String alphabet = "0abcdefghijklmnopqrstuvwxyz";
        
        //caratteri alfabetici
        for(i=1; i<27;i++) {
        		map.put(alphabet.charAt(i),i);
        		inverseMap.put(i, alphabet.charAt(i));        		
        }
        
        //caratteri speciali
        map.put(' ', 0);
        map.put(',', 27);
        map.put('\'', 28);
        inverseMap.put(0,' ');
        inverseMap.put(27, ',');
        inverseMap.put(28, '\'');
    }
    
	public static void main(String[] args) {
		String plaintext = "aaaa";
		Hill hill = new Hill();
		String generatedKey = hill.genKey();
		
		try{
			hill.setKey(generatedKey);
		} catch(IllegalArgumentException e){
			System.out.println("Chiave illegale. Cambiare chiave e rilanciare il programma.");
			return;
		}
		
		String s3 = hill.Enc(plaintext);
		System.out.println(s3);
        

        System.out.println("CHIAVE: " + generatedKey);

		
}
		
	@Override
	public void setKey(String key) throws IllegalArgumentException{
		// TODO Auto-generated method stub
		int i;
		char c;
		
		if(this.checkKey(key)) {
			this.stringKey = key;
			
			for(i=0; i< this.stringKey.length(); i++) {
				c = key.charAt(i);
				this.intKey[i]= Hill.map.get(c);
			}
			
			//carico la chiave nella matrice
			this.matrixKey[0][0] = new BigInteger(""+this.intKey[0]+"");
			this.matrixKey[0][1] = new BigInteger(""+this.intKey[2]+"");
			this.matrixKey[1][0] = new BigInteger(""+this.intKey[1]+"");
			this.matrixKey[1][1] = new BigInteger(""+this.intKey[3]+"");
			return;
			}
		else {
				throw new IllegalArgumentException();
		}
	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return this.stringKey;
	}

	@Override
	public String genKey() {
		// Generatore di chiavi VALIDE
		int min = 0;
		int max = 27;
		Character res = null;
		String key = "";
		
		for(int i=0;i<4;i++) {
			int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
			res = this.inverseMap.get(randomNum);
			key = key + res.toString();	
		}
		
		if(!this.checkKey(key))
			return null;
		else
			return key;
	}

	@Override
	public String Enc(String plainText) {
		// TODO Auto-generated method stub
		ModMatrix matrixKey = new ModMatrix(this.matrixKey);
		char nc1,nc2,cod1,cod2;
		int i,p1,p2,c1,c2;
		String ret = "";
	
		//padding stringa dispari
		if(plainText.length() % 2 != 0) {
			plainText  = plainText.concat(" ");
		}
		
		//converto la stringa in array di char
		char[] app = plainText.toCharArray();
		
		for(i=0;i<plainText.length();i=i+2) {
				nc1 = app[i];
				nc2 = app[i+1];
				p1=map.get(nc1);
				p2=map.get(nc2);
				c1 = ((p1*matrixKey.getValueAt(0, 0).intValue()) + (p2*matrixKey.getValueAt(0, 1).intValue())) % 29;
				c2 = (p1*matrixKey.getValueAt(1, 0).intValue() + p2*matrixKey.getValueAt(1, 1).intValue()) % 29;
				cod1 = Hill.inverseMap.get(c1);
				cod2 = Hill.inverseMap.get(c2);
				ret = ret + cod1;
				ret = ret + cod2;
	}
		
		return ret;
	}
	@Override
	public String Dec(String cipherText) {
		// TODO Auto-generated method stub
		
		//genero la matrice inversa modulo 29
        ModMatrix mm = new ModMatrix(this.matrixKey);
        ModMatrix inversemod29MatrixKey= mm.inverse(mm);
        
        //inizializzo delle variabili
        String ret = "";
		char nc1,nc2,s1,s2;
		int i,cod1,cod2;
		int p1,p2;
		
		//padding stringa dispari
		if(cipherText.length() % 2 != 0) {
			cipherText  = cipherText.concat(" ");
		}
		
		//-a mod m, m-a mod m
		//converto la stringa in array di char
		char[] app = cipherText.toCharArray();
		
		for(i=0;i<cipherText.length();i=i+2) {
				nc1 = app[i];
				nc2 = app[i+1];
				cod1 = Hill.map.get(nc1);
				cod2 = Hill.map.get(nc2);
				p1 = ((inversemod29MatrixKey.getValueAt(0, 0).intValue()*cod1) + (inversemod29MatrixKey.getValueAt(0, 1).intValue()*cod2)) % 29;
				p2 = ((inversemod29MatrixKey.getValueAt(1, 0).intValue()*cod1) + (inversemod29MatrixKey.getValueAt(1, 1).intValue()*cod2)) % 29;
				s1 = Hill.inverseMap.get(p1);
				s2 = Hill.inverseMap.get(p2);
				ret += s1;
				ret += s2;
		}
		
		return ret;
	}
	
	public boolean checkKey(String s) throws IllegalArgumentException{
		int i, res;
		char c;
		int[] det = new int[4];
		String illegal = "/*!@#$%^&*()\"{}_[]|\\?/<>.";
		
		if(s.length() != 4) {
			System.out.println("Chiave-stringa lunghezza non valida.");
			return false;
		}
		
	    String str2[]=s.split("");

	    for (int j=0;j<str2.length;j++)
	    {
	    if (illegal.contains(str2[j])){
	    			throw new IllegalArgumentException();
	    		}
	    }
		
		for(i=0; i<4; i++) {
			c = s.charAt(i);
			det[i]= Hill.map.get(c);
		}
		
		//matrice non invertibile oppure con determinante multiplo 29
		res = ((det[0]*det[3]) - (det[1]*det[2]));
		if(res == 0 || (res % 29 == 0))
			return false;
		return true;
	}
}
