import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class MerkleTree {
	final int N = 9;
	public String[] hashLeaves = new String[N];

	
	public MerkleTree(String[] args) throws IOException{
		for(int i = 1; i < N; i++) {
			File f = new File(args[i]);
			byte[] arr = FileUtils.readFileToByteArray(f);
			String s = org.apache.commons.codec.digest.DigestUtils.sha256Hex(arr);
			this.hashLeaves[i] = s;
		}
	}
	
	public void printHashLeaves() {
		for(int i = 1; i < N; i++) {
			System.out.println("Foglia " + i + " " + this.hashLeaves[i]);
		}
	}
	
	public String rootHashValue() {
		String h12 = this.hashLeaves[1] + this.hashLeaves[2];
		String h34 = this.hashLeaves[3] + this.hashLeaves[4];
		String h56 = this.hashLeaves[5] + this.hashLeaves[6];
		String h78 = this.hashLeaves[7] + this.hashLeaves[8];
		String h14 = h12 + h34;
		String h58 = h56 + h78;
		String h18 = h14 + h58;
		return h18;
	}
}
