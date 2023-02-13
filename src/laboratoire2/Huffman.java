package laboratoire2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
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
	public LinkedHashMap<Character, Integer> createFrequencyTable(String filePath) throws FileNotFoundException {
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
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("number of bytes read in binary mode : " + nbBytes);
		System.out.println(frequency.entrySet());

		return sortFrequencyTable(frequency);
	}

	/**
	 * <p>
	 * Sorts a frequency table in descending order of it's value.
	 * </p>
	 * 
	 * @param frequencyTable The frequency table to sort.
	 * @return A sorted frequency table.
	 */
	private LinkedHashMap<Character, Integer> sortFrequencyTable(LinkedHashMap<Character, Integer> frequencyTable) {
		LinkedHashMap<Character, Integer> sortedFrequencyTable = new LinkedHashMap<>();
		List<Map.Entry<Character, Integer>> entries = new ArrayList<>(frequencyTable.entrySet());
		
		Collections.sort(entries, new FrequencyComparator());

		for (Map.Entry<Character, Integer> entry : entries) {
			sortedFrequencyTable.put(entry.getKey(), entry.getValue());
		}

		return sortedFrequencyTable;
	}

	public static void main(String[] args) {
		Huffman huff = new Huffman();
		try {
			LinkedHashMap<Character, Integer> frequency = huff.createFrequencyTable("src/laboratoire2/Temp.txt");
			System.out.println(frequency.entrySet());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
