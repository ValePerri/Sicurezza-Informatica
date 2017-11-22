
public interface ClassicCipher {
	void setKey(String key); String getKey();
	String genKey();
	String Enc(String plainText);
	String Dec(String cipherText);
}
