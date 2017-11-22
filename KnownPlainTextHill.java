import java.math.*;
import java.math.BigInteger;

public class KnownPlainTextHill {

	   public static void main(String[] args) {
	      Hill hill = new Hill();
	      KnownPlainTextHill kpth = new KnownPlainTextHill();
	      String plaintext1 = "attacco all'alba";
	      String plaintext2 = "sicurezza informatica";
	      String plaintext3 = "farc, fuerzas armadas revolucionarias de colombia";
	      String plaintext4 = "postuv korespondencni problem";
	      String ciphertext1 = "axiarpjuzhvizhz'";
	      String ciphertext2 = "xrjddqf,jgimyexebfyojg";
	      String ciphertext3 = "dylqxjpwchylduv'gs'fduskfsbvjpn'v'zjdumwytgeybvuqx";
	      String ciphertext4 = "i, hcjtxxk yi,phlvj,iuywpm bmk";
	      String ciphertext5 = "cgiu";
	      String plaintext5 ="farc";
	      
	      String key1, key2, key3, key4, key5, key6;
	      key1 = kpth.knownPlainText(hill, plaintext1, ciphertext1);
	      key2 = kpth.knownPlainText(hill, plaintext2, ciphertext2);
	      key3 = kpth.knownPlainText(hill, plaintext3, ciphertext3);
	      key4 = kpth.knownPlainText(hill, plaintext4, ciphertext4);
	      key5 = kpth.knownPlainText(hill, plaintext5, ciphertext5);

	      System.out.println("CHIAVE 1: " + key1);
	      System.out.println("CHIAVE 2: " + key2);
	      System.out.println("CHIAVE 3: " + key3); //questo dà problemi
	      System.out.println("CHIAVE 4: " + key4);
	      System.out.println("CHIAVE 5: " + key5);

	   }

	   public String knownPlainText(Hill cipher, String plaintext, String ciphertext) {
		      BigInteger[][] p = new BigInteger[2][2];
		      BigInteger[][] c = new BigInteger[2][2];
		      int[][] k = new int[2][2];
		      int[] app = new int[plaintext.length()];
		      int i,j;
		      char[] ptext = plaintext.toCharArray();
		      char[] ctext = ciphertext.toCharArray();
		      String res = "";
		      
		      for(i=0; i<ptext.length;i=i+4) {
		    	  	app[i] = cipher.map.get(ptext[i]);
		    	  	app[i+1] = cipher.map.get(ptext[i+1]);
		    	  	app[i+2] = cipher.map.get(ptext[i+2]);
		    	  	app[i+3] = cipher.map.get(ptext[i+3]);

		    	  	if((app[i]*app[i+3] - app[i+1]*app[i+2]) == 0 || (app[i]*app[i+3] - app[i+1]*app[i+2]) % 29 == 0) {
		    	  		//prova con i prossimi 4 caratteri
			  		      p[0][0] = new BigInteger(""+cipher.map.get(ptext[i+2])+"");
					      p[0][1] = new BigInteger(""+cipher.map.get(ptext[i+1])+"");
					      p[1][0] = new BigInteger(""+cipher.map.get(ptext[i])+"");
					      p[1][1] = new BigInteger(""+cipher.map.get(ptext[i+3])+"");
						     //riempio la matrice C
					      c[0][0] = new BigInteger(""+cipher.map.get(ctext[i+2])+"");
					      c[0][1] = new BigInteger(""+cipher.map.get(ctext[i+1])+"");
					      c[1][0] = new BigInteger(""+cipher.map.get(ctext[i])+"");
					      c[1][1] = new BigInteger(""+cipher.map.get(ctext[i+3])+"");
					      break;
					      
		    	  	} else {
			  		      p[0][0] = new BigInteger(""+cipher.map.get(ptext[i])+"");
					      p[0][1] = new BigInteger(""+cipher.map.get(ptext[i+1])+"");
					      p[1][0] = new BigInteger(""+cipher.map.get(ptext[i+2])+"");
					      p[1][1] = new BigInteger(""+cipher.map.get(ptext[i+3])+"");
						     //riempio la matrice C
					      c[0][0] = new BigInteger(""+cipher.map.get(ctext[i])+"");
					      c[0][1] = new BigInteger(""+cipher.map.get(ctext[i+1])+"");
					      c[1][0] = new BigInteger(""+cipher.map.get(ctext[i+2])+"");
					      c[1][1] = new BigInteger(""+cipher.map.get(ctext[i+3])+"");
					      break;
		    	  	}
		      }
		      


		     
		      //inversa modulo 29
		      ModMatrix mp = new ModMatrix(p);
		      ModMatrix mpInverse = mp.inverse(mp);
		     
		      //costruisco la matrice chiave - prodotto riga per colonna
		      k[0][0] = ((mpInverse.getValueAt(0, 0).intValue()*c[0][0].intValue()) + (mpInverse.getValueAt(0, 1).intValue()*c[1][0].intValue())) % 29;
		      k[0][1] = ((mpInverse.getValueAt(0, 0).intValue()*c[0][1].intValue()) + (mpInverse.getValueAt(0, 1).intValue()*c[1][1].intValue())) % 29;
		      k[1][0] = ((mpInverse.getValueAt(1, 0).intValue()*c[0][0].intValue()) + (mpInverse.getValueAt(1, 1).intValue()*c[1][0].intValue())) % 29;
		      k[1][1] = ((mpInverse.getValueAt(1, 0).intValue()*c[0][1].intValue()) + (mpInverse.getValueAt(1, 1).intValue()*c[1][1].intValue())) % 29;
		      	      
		      //costruisco la stringa-chiave
		      res = res + cipher.inverseMap.get(k[0][0]);
		      res = res + cipher.inverseMap.get( k[0][1]);
		      res = res + cipher.inverseMap.get( k[1][0]);
		      res = res + cipher.inverseMap.get( k[1][1]);
		      
		   return res;
	   }
}
