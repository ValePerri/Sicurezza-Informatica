import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Timestamp {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Timestamp ts = new Timestamp();
		User pasquale = new User("pasquale", "xc5qgswls95");
		ts.recuperaChiaviUtente(pasquale);
		pasquale.addPsw("www.unisa.it", "lolo", "xc5qgswls95");
		pasquale.addPsw("www.libero.it", "xilofono", "xc5qgswls95");
		pasquale.addPsw("www.vodafone.it", "robot", "xc5qgswls95");
		pasquale.addPsw("www.uomomoda.it", "89493asf", "xc5qgswls95");
		
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
