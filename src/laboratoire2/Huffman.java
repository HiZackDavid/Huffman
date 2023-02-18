package laboratoire2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Huffman {

  public static final int END_OF_FILE = -1;

  public static FileOutputStream encodeOutputStream;

  public static BitOutputStream bos;

  public LinkedHashMap<Character, String> correspondanceTable = new LinkedHashMap<Character, String>();
  private Node root;

  public void Compresser(String nomFichierEntre, String nomFichierSortie) {
    try {
      FileInputStream reader = new FileInputStream(nomFichierEntre);
      BitOutputStream bos = new BitOutputStream(nomFichierSortie);
      BufferedInputStream bis = new BufferedInputStream(reader);

      LinkedHashMap<Character, Integer> frequencyTable =
        this.createFrequencyTable(nomFichierEntre);
      this.root = this.createHuffmanTree(frequencyTable);
      this.createcorrespondanceTable(this.root, "");

      int singleCharInt;
      char singleChar;

      /* ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream(new File(nomFichierSortie))
      );
      oos.writeObject(root);
      oos.close(); */

      System.out.println(frequencyTable);
      System.out.println(correspondanceTable);
      while ((singleCharInt = bis.read()) != END_OF_FILE) {
        singleChar = (char) singleCharInt;
        String path = correspondanceTable.get(singleChar);
        for (char direction : path.toCharArray()) {
          if (direction == '0') {
            bos.writeBit(0);
          } else {
            bos.writeBit(1);
          }
        }
      }

      bis.close();
      bos.close();
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void Decompresser(String nomFichierEntre, String nomFichierSortie) {
    try {
      BitInputStream bis = new BitInputStream(nomFichierEntre);
      FileOutputStream fos = new FileOutputStream(nomFichierSortie);
      BufferedOutputStream bos = new BufferedOutputStream(fos);

      int direction;
      int count = 0;

      while ((direction = bis.readBit()) != END_OF_FILE) {
        //System.out.print(direction);
      }

      bos.close();
      fos.close();
      bis.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
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
  public LinkedHashMap<Character, Integer> createFrequencyTable(
    String filePath
  ) throws FileNotFoundException {
    LinkedHashMap<Character, Integer> frequency = new LinkedHashMap<>();
    File file = new File(filePath);
    FileInputStream reader = new FileInputStream(file);
    BufferedInputStream bis = null;
    //int nbBytes = 0;
    try {
      int singleCharInt;
      char singleChar;
      bis = new BufferedInputStream(reader);

      while ((singleCharInt = bis.read()) != END_OF_FILE) {
        singleChar = (char) singleCharInt;
        //System.out.println(String.format("0x%X %c", singleCharInt, singleChar));
        //nbBytes++;
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
    //System.out.println("number of bytes read in binary mode : " + nbBytes);

    return sortFrequencyTable(frequency);
  }

  /**
   * <p>
   * Sorts a frequency table in ascending order of it's value.
   * </p>
   *
   * @param frequencyTable The frequency table to sort.
   * @return A sorted frequency table.
   */
  private LinkedHashMap<Character, Integer> sortFrequencyTable(
    LinkedHashMap<Character, Integer> frequencyTable
  ) {
    LinkedHashMap<Character, Integer> sortedFrequencyTable = new LinkedHashMap<>();
    List<Map.Entry<Character, Integer>> entries = new ArrayList<>(
      frequencyTable.entrySet()
    );

    Collections.sort(entries, new FrequencyComparator());

    for (Map.Entry<Character, Integer> entry : entries) {
      sortedFrequencyTable.put(entry.getKey(), entry.getValue());
    }

    return sortedFrequencyTable;
  }

  /**
   * Creates a Huffman using a given frequency table.
   *
   * @param frequencyTable Frequency table.
   * @return A single node as a binary tree.
   */
  public Node createHuffmanTree(Map<Character, Integer> frequencyTable) {
    Node[] nodes = new Node[frequencyTable.size()];
    int index = 0;
    for (Map.Entry<Character, Integer> entry : frequencyTable.entrySet()) {
      nodes[index] = new Node(entry.getKey(), entry.getValue());
      index++;
    }

    return combine(nodes, 0);
  }

  /**
   * <p>Combines nodes in a list of nodes until only one is left.</p>
   *
   * @param nodes List of nodes.
   * @param pointer Index of the item to combine.
   * @return A single node containing all the nodes from the list of nodes. It is arranged as a binary tree.
   */
  public Node combine(Node[] nodes, int pointer) {
    if (nodes[nodes.length - 1] == null || nodes.length == 1) { // Last node has been combined
      /* Clear null elements in array */
      nodes =
        Arrays
          .stream(nodes)
          .filter(node -> (node != null))
          .toArray(Node[]::new);
      return nodes[0];
    }

    Node combined = new Node();
    combined.setLeftChild(nodes[0]);
    combined.setRightChild(nodes[pointer + 1]);

    nodes[0] = combined;
    nodes[pointer + 1] = null;
    pointer++;
    return combine(nodes, pointer);
  }

  public void createcorrespondanceTable(Node node, String path)
    throws IOException {
    Node childNodeLeft = node.getLeftChild();
    Node childNodeRight = node.getRightChild();
    // No children
    if (childNodeLeft == null && childNodeRight == null) {
      // encode(path,node);
      correspondanceTable.put(node.getKey(), path);
    } else {
      // left child
      if (childNodeLeft != null) {
        createcorrespondanceTable(childNodeLeft, path + "1");
        // right child
      }
      if (childNodeRight != null) {
        createcorrespondanceTable(childNodeRight, path + "0");
      }
    }
  }

  public static void encode(String path) throws IOException {
    if (path == "") {
      encodeOutputStream.write(0);
    } else {
      encodeOutputStream.write((path + "\n").getBytes());
    }
  }

  public static void testBinaryFile() throws IOException {
    File file = new File("src/laboratoire2/Temp_2.txt");
  }

  public static void main(String[] args) {
    String initial = "src/laboratoire2/Temp.txt";
    String compressed = "src/laboratoire2/TempC.bin";
    String decompressed = "src/laboratoire2/TempD.bin";

    Huffman huff = new Huffman();
    huff.Compresser(initial, compressed);
    huff.Decompresser(compressed, decompressed);
    File initialFile = new File(initial);
    File compressedFile = new File(compressed);

    System.out.println("Initial file size " + initialFile.length() + " bytes");
    System.out.println(
      "Compressed file size " + compressedFile.length() + " bytes"
    );
    /*try {
      LinkedHashMap<Character, Integer> frequency = huff.createFrequencyTable(
        "src/laboratoire2/Temp.txt"
      );
      Node tree = huff.createHuffmanTree(frequency);
      try {
        File file = new File("src/laboratoire2/Temp_2.txt");
        file.delete();
        encodeOutputStream =
          new FileOutputStream(new File("src/laboratoire2/Temp_2.txt"), true);
        bos = new BitOutputStream("src/laboratoire2/Temp_2.txt");
        encodeOutputStream.write((frequency + "\n").getBytes());
        huff.createcorrespondanceTable(tree, "");
        //huff.writeInFile(correspondanceTable);
        correspondanceTable.forEach((key, val) -> {
          System.out.println("key " + key);
          System.out.println("value " + val);
        });
        testBinaryFile();
        encodeOutputStream.flush();
      } catch (Exception e) {
        System.out.println(e);
      }
      // Formatting
      /* frequency.forEach((key, val) -> {
        String str = ("" + key + " : " + val);
        str = str.replace(System.lineSeparator(), "HEY");
        System.out.println(str);
      }); */
    /*} catch (FileNotFoundException e) {
      e.printStackTrace();
    }*/
  }
}
