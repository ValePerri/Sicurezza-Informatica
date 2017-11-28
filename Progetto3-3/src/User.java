import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class User {
	private String nome;
	final public keyRing keyring;
	
	public User(String nome, String password) throws Exception{
		this.nome = nome;
		this.keyring = new keyRing(this, password);
	}
	
    public void addPsw(String sito, String passwordSito, String passwordKeyRing) throws Exception {
    		keyRing kr = this.keyring;
    		
    		//sblocco il keyring con la relativa password
    		kr.unlock(passwordKeyRing);
    		String decryptedFilename = kr.keyRing.getName() + "decrypted.txt";
    		
    		String linea = sito + " " + kr.get_SHA_512_SecurePassword(passwordSito);
    		System.out.println(linea);
    		
    		//append della linea nel file
    	try {
    	    Files.write(Paths.get(decryptedFilename), linea.getBytes(), StandardOpenOption.APPEND);
    	}catch (IOException e) {
    	    System.out.println("Eccezione input/output. Linea 49.");
    	}
    	
    		//blocco di nuovo il keyring
    		encryptFile.main(kr.keyRing, passwordKeyRing);
    }

	public String getNome() {
		return nome;
	}

	
	static class keyRing{
	    private File keyRing;
        
	    private keyRing(User user, String password) throws Exception{
	        
	        try {
	            //genero la coppia di chiavi asimmetriche dell'utente user
	            KeyPairGenerator keyPairGenerator = null;
	            try {
	                keyPairGenerator = KeyPairGenerator.getInstance("RSA");
	            } catch (NoSuchAlgorithmException ex) {
	                Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
	            }
	            keyPairGenerator.initialize(1024, new SecureRandom());
	            KeyPair userKey = keyPairGenerator.generateKeyPair();
	            
	            //mi vado a memorizzare quella privata e quella pubblica di cifratura
	            PrivateKey prk = userKey.getPrivate();
	            PublicKey puk = userKey.getPublic();
	            
	            	//genero la chiave simmetrica AES dello user
	            SecureRandom random = new SecureRandom();
	            		byte aesKey[] = new byte[16];
	            		random.nextBytes(aesKey);
	            		SecretKey secretAESKey = new SecretKeySpec(aesKey, 0, aesKey.length, "AES");
	            		
	            
	            //genero la firma per l'utente                       
	            KeyPairGenerator signKey = KeyPairGenerator.getInstance("DSA");
	            keyPairGenerator.initialize(1024, new SecureRandom());
	            KeyPair userSignKey = signKey.generateKeyPair();

	            PrivateKey userSignKeyPr = userSignKey.getPrivate();
	            PublicKey userSignKeyPub = userSignKey.getPublic();
	            
	            FileOutputStream kFile = null;
	            try {
	                String generatedFile = user.getNome() + " keyring";
	                kFile = new FileOutputStream(generatedFile+".txt"); 
	                
	                //mi salvo il file 
	                this.keyRing = new File(generatedFile+ ".txt");
	                
	                PrintStream scrivi = new PrintStream(kFile);
	                
	                //organizzo chiavi nel keyring - base64
	                byte[] arr = prk.getEncoded();
	                String encodedKey = Base64.getEncoder().encodeToString(arr);
	                scrivi.print(encodedKey);
	                           
	                byte[] arr2 = puk.getEncoded();
	                String encodedKey2 = Base64.getEncoder().encodeToString(arr2);
	                scrivi.print("," + encodedKey2);
	                
	                byte[] arr3 = userSignKeyPr.getEncoded();
	                String encodedsignKey3 = Base64.getEncoder().encodeToString(arr3);
	                scrivi.print("," + encodedsignKey3);
	                
	                byte[] arr4 = userSignKeyPub.getEncoded();
	                String encodedsignKey4 = Base64.getEncoder().encodeToString(arr4);
	                scrivi.print("," + encodedsignKey4);
	                
	                byte[] arr5 = secretAESKey.getEncoded();
	                String encodedAESKey = Base64.getEncoder().encodeToString(arr5);
	                scrivi.print("," + encodedAESKey);
	                
	                //cripta file
	                encryptFile.main(this.keyRing, password);
	               
	                
	            } catch (FileNotFoundException ex) {
	                Logger.getLogger(user.getClass().getName()).log(Level.SEVERE, null, ex);
	            } 
	        } catch (NoSuchAlgorithmException ex) {
	            Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
	        } 
	            
	    }
	    
	    public String unlock(String password) throws Exception {
	    		decryptFile.main(this.keyRing.getName(), password);
	    		String decryptedFilename = this.keyRing.getName() + "decrypted.txt";
	    		
	    		return decryptedFilename;
	    }
	    
	    public char[] getPrKey(String password) throws Exception{
	    		//apro il keyring con la password
	    		String decryptedFilename = this.unlock(password);

	        char[] arrPrKey = new char[5000];
	        int i=0;
	        
	        FileReader filein = null;
	        int next = 0;
	        try {
	            // apre il file decriptato con la password in lettura
	            filein = new FileReader(new File(decryptedFilename));
	            do {
	                try {
	                    next = filein.read(); // legge il prossimo carattere
	                } catch (IOException ex) {
	                    Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
	                }
	                
	                if (next == ',') { // se è un new line                    
	                    break; 
	                }
	                else{ // se non è un new line                    
	                    char nextc = (char) next;
	                    arrPrKey[i] = nextc;
	                    i++;
	                    //System.out.print(nextc); // stampa il carattere
	                }
	                
	            } while (next != -1);
	            try {
	                filein.close(); // chiude il file
	            } catch (IOException ex) {
	                Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        } catch (FileNotFoundException ex) {
	            Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
	        } finally {
	            try {
	                filein.close();
	            } catch (IOException ex) {
	                Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        return arrPrKey;
	        

	    }
	    
	    public char[] getPubKey(String password) throws Exception{
	    		String decryptedFilename = this.unlock(password);
	        char[] arrPubKey = new char[1000];
	        int i=1;
	       
	        FileReader filein = null;
	        int next = 0;
	        int idx = 0;
	        
	       try {
	            // apre il file in lettura
	            filein = new FileReader(decryptedFilename);
	            do {
	                try {
	                    next = filein.read(); // legge il prossimo carattere
	                } catch (IOException ex) {
	                    Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
	                }
	                if (next == ',') { // se è un new line 
	                    idx += 1;
	                }
	                if (idx==1){
	                    char nextc = (char) next;
	                    arrPubKey[i] = nextc;
	                    i++;
	                }
	                if (idx == 2){
	                    break;
	                }
	                                
	            } while (next != -1);
	            try {
	                filein.close(); // ch{iude il file
	            } catch (IOException ex) {
	                Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        } catch (FileNotFoundException ex) {
	            Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
	        } finally {
	            try {
	                filein.close();
	            } catch (IOException ex) {
	                Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        return arrPubKey;
	        
	    }
	    
	    public char[] getSignPrKey(String password) throws Exception{
	    		String decryptedFile = this.unlock(password);
	        char[] arrSignPrKey = new char[500];
	        int i=1;
	       
	        FileReader filein = null;
	        int next = 0;
	        int idx = 0;
	        
	       try {
	            // apre il file in lettura
	            filein = new FileReader(decryptedFile);
	            do {
	                try {
	                    next = filein.read(); // legge il prossimo carattere
	                } catch (IOException ex) {
	                    Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
	                }
	                if (next == ',') { // se è un new line 
	                    idx += 1;
	                }
	                if (idx==2){
	                    char nextc = (char) next;
	                    arrSignPrKey[i] = nextc;
	                    i++;
	                }
	                if (idx == 3){
	                    break;
	                }
	                                
	            } while (next != -1);
	            try {
	                filein.close(); // ch{iude il file
	            } catch (IOException ex) {
	                Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        } catch (FileNotFoundException ex) {
	            Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
	        } finally {
	            try {
	                filein.close();
	            } catch (IOException ex) {
	                Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        return arrSignPrKey;
	        
	    }
	    
	    //encrypt password with sha-512 and random salt
	    public String get_SHA_512_SecurePassword(String passwordToHash) throws UnsupportedEncodingException{
	    	
	    	//generazione del salt casuale da aggiungere alla password
	    	SecureRandom random = new SecureRandom();
	    	byte random_salt[] = new byte[16];
		random.nextBytes(random_salt);
		String salt = random_salt.toString();
		
	    	String generatedPassword = null;
	    	    try {
	    	         MessageDigest md = MessageDigest.getInstance("SHA-512");
	    	         md.update(salt.getBytes("UTF-8"));
	    	         byte[] bytes = md.digest(passwordToHash.getBytes("UTF-8"));
	    	         StringBuilder sb = new StringBuilder();
	    	         for(int i=0; i< bytes.length ;i++){
	    	            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	    	         }
	    	         generatedPassword = sb.toString();
	    	        } 
	    	       catch (NoSuchAlgorithmException e){
	    	        e.printStackTrace();
	    	       }
	    	    return generatedPassword;
	    	}
	    
	    public char[] getSignPubKey(String password) throws Exception{
	    		String decryptedFile = this.unlock(password);
	        char[] arrSignPubKey = new char[1000];
	        int i=1;
	       
	        FileReader filein = null;
	        int next = 0;
	        int idx = 0;
	        
	       try {
	            // apre il file in lettura
	            filein = new FileReader(decryptedFile);
	            do {
	                try {
	                    next = filein.read(); // legge il prossimo carattere
	                } catch (IOException ex) {
	                    Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
	                }
	                if (next == ',') { // se è un new line 
	                    idx += 1;
	                }
	                if (idx==3){
	                    char nextc = (char) next;
	                    arrSignPubKey[i] = nextc;
	                    i++;
	                }
	                if (idx == 4){
	                    break;
	                }
	                                
	            } while (next != -1);
	            try {
	                filein.close(); // ch{iude il file
	            } catch (IOException ex) {
	                Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        } catch (FileNotFoundException ex) {
	            Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
	        } finally {
	            try {
	                filein.close();
	            } catch (IOException ex) {
	                Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        return arrSignPubKey;
	        
	    }
	    
	    public char[] getSymmetricKey(String password) throws Exception{
    		String decryptedFile = this.unlock(password);
        char[] arrSymmetricKey = new char[1000];
        int i=1;
       
        FileReader filein = null;
        int next = 0;
        int idx = 0;
        
       try {
            // apre il file in lettura
            filein = new FileReader(decryptedFile);
            do {
                try {
                    next = filein.read(); // legge il prossimo carattere
                } catch (IOException ex) {
                    Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (next == ',') { // se è un new line 
                    idx += 1;
                }
                if (idx==4){
                    char nextc = (char) next;
                    arrSymmetricKey[i] = nextc;
                    i++;
                }
                if (idx == 5){
                    break;
                }
                                
            } while (next != -1);
            try {
                filein.close(); // ch{iude il file
            } catch (IOException ex) {
                Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                filein.close();
            } catch (IOException ex) {
                Logger.getLogger(keyRing.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return arrSymmetricKey;
        
    }
	    
		void recuperaChiaviUtente(User user) throws Exception {
			
			char[] privateKeyBase64encoded = user.keyring.getPrKey("xc5qgswls95");
			char[] pubKeyb64enc = user.keyring.getPubKey("xc5qgswls95");
			char[] prSignKeyb64enc = user.keyring.getSignPrKey("xc5qgswls95");
			char[] pubSignKeyb64enc = user.keyring.getSignPubKey("xc5qgswls95");
			char[] symmetricKeyb64enc = user.keyring.getSymmetricKey("xc5qgswls95");
			
			String p = new String(privateKeyBase64encoded);
			String p1 = new String(pubKeyb64enc);
			String p2 = new String(prSignKeyb64enc);
			String p3 = new String(pubSignKeyb64enc);
			String p4 = new String(symmetricKeyb64enc);
			//test recupero chiave privata RSA
			byte[] decodedprKey = Base64.getMimeDecoder().decode(p);
			PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decodedprKey));
			
			//test recupero chiave pubblica RSA
			byte[] decodedpubKey = Base64.getMimeDecoder().decode(p1);
			PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decodedpubKey));
			
			//test recupero chiave privata DSA
			byte[] decodedsignprKey = Base64.getMimeDecoder().decode(p2);
			PrivateKey privatesignKey = KeyFactory.getInstance("DSA").generatePrivate(new PKCS8EncodedKeySpec(decodedsignprKey));
			
			//test recupero chiave pubblica DSA
			byte[] decodedpubsignKey = Base64.getMimeDecoder().decode(p3);
			PublicKey publicsignKey = KeyFactory.getInstance("DSA").generatePublic(new X509EncodedKeySpec(decodedpubsignKey));
			
			//test recupero chiave simmetrica AES
			byte[] decodedsymmetricKeyb64enc = Base64.getMimeDecoder().decode(p4);
			SecretKey aesKey = new SecretKeySpec(decodedsymmetricKeyb64enc, "AES");
			
			
		}
	}
}
