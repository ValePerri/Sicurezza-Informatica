import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class MarcaturaTemporale {
	final static int N = 8;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//definisco i file in input al sistema
		//esempio: 8 file + 1 nodi fittizio
		String[] filenames = {"0", "files2/file1.pdf", "files2/file2.pdf", "files2/file3.pdf", "files2/file4.pdf", "files2/file5.pdf", "files2/file6.pdf", "files2/file7.pdf", "files2/file8.pdf"};
		
		//costruisco il merkle tree associato con algoritmo SHA-256
		MerkleTree mt = new MerkleTree(filenames);
		
		//stampo hash delle foglie
		//mt.printHashLeaves();
		
		//istanzio la TSA - timestamping authority
		TSA tsa = new TSA();
		
		//inizializzo vettore di Sig
		Sig signatures[] = new Sig[N+1];
		
		//inizializzo vettore linking information
		LinkingInformation[] liv = new LinkingInformation[N+1];
		
		//sistema di catena di marcature temporali
		for(int i=1; i < 9; i++) {
			if(i==1) {
				String timestamp = "0";
				LinkingInformation l0 = new LinkingInformation(timestamp, 0, "0", 0); //L0 = (0, 0, 0; 0)
				liv[i] = l0;
				Sig s = tsa.signature(mt.hashLeaves[i], i, i, l0);
				signatures[i] = s;
			} else {
				if(i==2) {
					LinkingInformation li = new LinkingInformation(signatures[i-1].getTimestamp(), i, (i-1), mt.hashLeaves[i-1], liv[i-1]);
					liv[i] = li;
					Sig s = tsa.signature(mt.hashLeaves[i], i, i, li);
					signatures[i] = s;
					
				} else {
					LinkingInformation li = new LinkingInformation(signatures[i-1].getTimestamp(), i, i-1, mt.hashLeaves[i-1], liv[i-1]);
					liv[i] = li;
					Sig s = tsa.signature(mt.hashLeaves[i], i, i, li);
					signatures[i] = s;
				}
			}
		}
		
		//visualizzo marche temporali prodotte nel primo timeframe
		for(int j = 1; j < signatures.length; j++)
			System.out.print("MARCA: " + j + " " + signatures[j] + "\n");
		
		
	}

}
