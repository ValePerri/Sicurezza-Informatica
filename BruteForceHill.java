import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class BruteForceHill {

	public static void main(String[] args) {
		BruteForceHill bfh = new BruteForceHill();
		Hill hill = new Hill();
		String ciphertext1 = "kgeprrm ,gilzhpn,fhcaposvv,rqrp'pwwdj vb,gkgklweshwmqrosvvzolwilrfxpgoezfnkldiqs";
		String ciphertext2 = "gbgbemumlcdvbb,izn qxpmwatoehldvmg qqumnivlw jmwpoeiyxyhnemwu w,u mnjidqo,fddqdvcvswumlcdvcvswumoe";
		String ciphertext3 = "hv ymkne,dxupzmqojqjtmqjrvlqtw,dtvrvphkcqjpzgzzole ham'bsbcujqbjxppzgzbef'xykrvrml'sampzgzjrrvokmbzobyb,qbpzgzjr'be,d bcgwleeknvwffqbjrqhvtrgoydrgnzj'tm yqfzmzo'bwzyqvr";
		String ciphertext4 = "x'hi,qtsikgaphpsuowd'dozyuaysefyburrlwk'ekeekcybx'hi,qtsikgaphpsuowd'dozyuaysewdr'mfthyybzir";
		String ciphertext5 = "ushssoyvxiywkbb hsdmyhyee blhgg,,z, ,znznqywgggvhv'qkberjy";
		String ciphertext6 = "m bqhigabqkmawahofsbhx'frc'zavfqbntgxpo'r ckudiqrqrvexj,jtesllffo'i vttytwofhjgohtbays'i";
		//bfh.bruteForce(hill, ciphertext1);
		bfh.bruteForce(hill, ciphertext2);
		//bfh.bruteForce(hill, ciphertext3);
		//bfh.bruteForce(hill, ciphertext4);
		//bfh.bruteForce(hill, ciphertext5);
		//bfh.bruteForce(hill, ciphertext6);
	}
	
	public boolean isValid(char c)
	{
	    if(c=='a' || c=='e' || c=='i' || c=='o' || c=='u' || c==' ' || c==',' || c=='\'')
	    {    
	        return true;
	    }    
	    else
	    {
	        return false;
	    }    
	}
	
	public boolean tooMuchConsonants(String string) {
		int i, count = 0;
		char[] app = string.toCharArray();
		
		for(i=0; i < app.length; i++){
			if(!this.isValid(app[i])) {
				count++;
			} else {
				count = 0;
			}
			//System.out.println(count + " " + app[i]);
			if(count > 3)
				return true;
		}
		return false;
	}
		
	public void bruteForce(Hill hill, String ciphertext) {
        String currentString = "";       
        int n = 0;
        char[] charset = "abcdefghijklmnopqrstuvwxyz,' ".toCharArray();
        
        for (int i = 0; i < charset.length; i++) {
           char currentChar = charset[i];

            for (int j = 0; j < charset.length; j++) {

                char c = charset[j];
                            
                for (int k = 0; k < charset.length; k++) {
                    char c2 = charset[k];
                 
                    for (int m = 0; m < charset.length; m++) {

                        char c3 = charset[m];
                        currentString =  "" +currentChar + c +c2 +c3;
                        
                        if(hill.checkKey(currentString)){
                        		hill.setKey(currentString);
                        		String res = hill.Dec(ciphertext);
                        		if(this.tooMuchConsonants(res)) {
                        			
            
                        		}else {
                        			n++;
                        		System.out.println("CHIAVE: "+n+" "+currentString + " " + res);
                        		}
                        }
                    }
                }
            }
        }
	}	
}