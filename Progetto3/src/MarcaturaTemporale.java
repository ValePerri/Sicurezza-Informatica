import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class MarcaturaTemporale {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//definisco i file in input al sistema
		//esempio: 6 file + 2 nodi fittizi
		String[] filenames = {"files2/file1.pdf", "files2/file2.pdf", "files2/file3.pdf", "files2/file4.pdf", "files2/file5.pdf", "files2/file6.pdf", "files2/file7.pdf", "files2/file8.pdf"};
		
		//costruisco il merkle tree associato con algoritmo SHA-256
		MerkleTree mt = new MerkleTree(filenames);
		
		//stampo hash delle foglie
		mt.printHashLeaves();
		
		//
		System.out.println("roothash value: " + mt.rootHashValue());
		

		
		
		
	}

}
