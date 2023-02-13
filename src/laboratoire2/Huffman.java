package laboratoire2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Huffman {
	public static final int END_OF_FILE = -1;

	public void Compresser(String nomFichierEntre, String nomFichierSortie) {
	}

	public void Decompresser(String nomFichierEntre, String nomFichierSortie) {
	}

	/**
	 * <p>
	 * Creates a frequency table.
	 * </p>
	 * <p>
	 * It will read a file and create a table containing the number of <br>
	 * occurrences of each character in it.
	 * </p>
	 * 
	 * @param filePath The location of the file.
	 * @throws FileNotFoundException Thrown if the file is not found.
	 */
	public Map<Character, Integer> createFrequencyTable(String filePath) throws FileNotFoundException {
		LinkedHashMap<Character, Integer> frequency = new LinkedHashMap<>();
		File file = new File(filePath);
		FileInputStream reader = new FileInputStream(file);
		BufferedInputStream bis = null;
		int nbBytes = 0;

		try {
			int singleCharInt;
			char singleChar;
			bis = new BufferedInputStream(reader);

			while ((singleCharInt = bis.read()) != END_OF_FILE) {
				singleChar = (char) singleCharInt;
				System.out.println(String.format("0x%X %c", singleCharInt, singleChar));
				nbBytes++;
				if (!frequency.containsKey(singleChar)) {
					frequency.put(singleChar, 1);
				} else {
					frequency.put(singleChar, frequency.get(singleChar) + 1);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("number of bytes read in binary mode : " + nbBytes);
		System.out.println(frequency.entrySet());

		return frequency;
	}

	public static void main(String[] args) {
		Huffman huff = new Huffman();
		try {
			Map<Character, Integer> frequency = huff.createFrequencyTable("src/laboratoire2/Temp.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
