import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class MarcaturaTemporale {
	final static int N = 8;

	public static void main(String[] args) throws IOException, InterruptedException {
		//istanzio la TSA - timestamping authority
		TSA tsa = new TSA();
		
		//inizializzo Super Hash Value SHV0 con un seed iniziale e una dimensione
		SuperHashValue shv = new SuperHashValue("0x00", 10);
		
		//definisco i file in input al sistema
		//esempio: 8 file + 1 nodi fittizio
		String[] filenames = {"0", "files2/file1.pdf", "files2/file2.pdf", "files2/file3.pdf", "files2/file4.pdf", "files2/file5.pdf", "files2/file6.pdf", "files2/file7.pdf", "files2/file8.pdf"};
		
		//secondo timeframe
		String[] filenames2 = {"0", "files3/file1.pdf", "files3/file2.pdf", "files3/file3.pdf", "files3/file4.pdf", "files3/file5.pdf", "files3/file6.pdf", "files3/file7.pdf", "files3/file8.pdf"};
		
		//costruisco il merkle tree associato al primo timeframe con algoritmo SHA-256
		MerkleTree mt = new MerkleTree(filenames);
		
		//il sistema restituisce le marche temporali dei relativi documenti
		Sig[] signatures = MarcaturaTemporale.getMarcheTemporali(mt, tsa, shv);	
		
		//costruisco il merkle tree associato al primo timeframe con algoritmo SHA-256
		MerkleTree mt2 = new MerkleTree(filenames2);
		
		//il sistema restituisce le marche temporali dei relativi documenti
		Sig[] signatures2 = MarcaturaTemporale.getMarcheTemporali(mt2, tsa, shv);

		//stampo hash delle foglie
		//mt.printHashLeaves();
		
		//visualizzo marche temporali prodotte nel primo timeframe e aggiungo IDn+1 alle marche temporale per completare il procedimento
		for(int j = 1; j < signatures.length; j++) {
			signatures[j] = new Sig(signatures[j], j+1);
			System.out.print("hash documento " + j + " " +mt.hashLeaves[j] + " " +signatures[j].toString() + "\n");
			System.out.println(signatures2[j].getTimestamp());
		}
		
		
		//visualizzo marche temporali prodotte nel primo timeframe e aggiungo IDn+1 alle marche temporale per completare il procedimento
		for(int j = 1; j < signatures2.length; j++) {
			signatures2[j] = new Sig(signatures2[j], j+1);
			System.out.print("hash documento " + j + " " +mt2.hashLeaves[j] + " " +signatures2[j].toString() + "\n");
			System.out.println(signatures2[j].getTimestamp());
		}
		
		//verifica marca temporale
		
	}
	
	public static Sig[] getMarcheTemporali(MerkleTree mt, TSA tsa, SuperHashValue shv) {
		//parametro di ritorno
		Sig[] signatures = new Sig[N+1];
		
		//inizializzo vettore linking information
		LinkingInformation[] liv = new LinkingInformation[N+1];
				
				//sistema di catena di marcature temporali
				for(int i=1; i < 9; i++) {
					if(i==1) {
						String timestampn_1 = "0";
						LinkingInformation l0 = new LinkingInformation(timestampn_1, 0, "0", 0); //L0 = (0, 0, 0; 0)
						liv[i] = l0;
						Sig s = tsa.signature(mt.hashLeaves[i], i, i, l0);
						signatures[i] = s;
					} else {
						
							LinkingInformation li = new LinkingInformation(signatures[i-1].getTimestamp(), i, (i-1), mt.hashLeaves[i-1], liv[i-1]);
							liv[i] = li;
							Sig s = tsa.signature(mt.hashLeaves[i], i, i, li);
							signatures[i] = s;
					}
				}
		
		//aggiorno oggetto super hash value
		shv.computeSHV(mt);
		
		return signatures;
	}
	
	
	

}
