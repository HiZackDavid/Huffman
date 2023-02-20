package laboratoire2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Huffman {

  public static final int END_OF_FILE = -1;

  public void Compresser(String nomFichierEntre, String nomFichierSortie) {
    Node huffmanTree = this.createHuffmanTree(nomFichierEntre);
    Map<Character, String> correspondance =
      this.createCorrespondanceTable(huffmanTree);
    try {
      FileInputStream reader = new FileInputStream(nomFichierEntre);
      BitOutputStream bos = new BitOutputStream(nomFichierSortie);
      BufferedInputStream bis = new BufferedInputStream(reader);

      // Write the compressed HuffmanTree
      this.writeTree(huffmanTree, bos);

      int count = this.countCompressedFileCharacters(nomFichierEntre);
      System.out.println("Count : " + count);
      String countInBinary = Integer.toBinaryString(count);
      if (countInBinary.length() < 32) {
        int missingBits = 32 - countInBinary.length();
        for (int i = 0; i < missingBits; i++) {
          countInBinary = '0' + countInBinary;
        }
      }
      for (int i = 0; i < countInBinary.length(); i++) {
        bos.writeBit(Integer.parseInt(countInBinary.charAt(i) + ""));
      }

      // Write the compressed output
      int singleCharInt;
      while ((singleCharInt = bis.read()) != END_OF_FILE) {
        this.writeToFile((char) singleCharInt, correspondance, bos);
      }

      bis.close();
      bos.close();
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * <p>Writes a compressed version of the huffman tree into a file.</p>
   * @param huffmanTree The huffman tree
   * @throws IOException
   * @throws FileNotFoundException
   */
  private void writeTree(Node huffmanTree, BitOutputStream bos)
    throws IOException {
    int count = this.countHuffTreeBits(huffmanTree);
    count += 8 - count % 8;

    String countInBinary = Integer.toBinaryString(count);
    if (countInBinary.length() < 32) {
      int missingBits = 32 - countInBinary.length();
      for (int i = 0; i < missingBits; i++) {
        countInBinary = '0' + countInBinary;
      }
    }
    for (int i = 0; i < countInBinary.length(); i++) {
      bos.writeBit(Integer.parseInt(countInBinary.charAt(i) + ""));
    }
    this.writeTreeInFile(huffmanTree, bos);
  }

  /**
   * Counts the amount of bits neccessary to write the huffman tree in a compressed file.
   * @return The amount of bits necessary to write the huffman tree.
   */
  private int countHuffTreeBits(Node huffmanTree) {
    int count = 0;
    // Is leaf
    if (huffmanTree.isLeaf()) {
      count++;
      String charToByte = Integer.toBinaryString(huffmanTree.getKey());
      if (charToByte.length() < 8) {
        int missingBits = 8 - charToByte.length();
        for (int i = 0; i < missingBits; i++) {
          charToByte = '0' + charToByte;
        }
      }
      for (int i = 0; i < charToByte.length(); i++) {
        count++;
      }
    } else {
      count++;
      count += countHuffTreeBits(huffmanTree.getLeftChild());
      count += countHuffTreeBits(huffmanTree.getRightChild());
    }

    return count;
  }

  /**
   * <p>Writes the huffman tree on a binary file in a compressed way.</p>
   * @param huffmanTree The huffman tree.
   * @throws IOException
   */
  private void writeTreeInFile(Node huffmanTree, BitOutputStream bos)
    throws IOException {
    // Is leaf
    if (huffmanTree.isLeaf()) {
      bos.writeBit(1);
      System.out.println(
        huffmanTree.getKey() +
        " to binary =" +
        Integer.toBinaryString(huffmanTree.getKey())
      );
      String charToByte = Integer.toBinaryString(huffmanTree.getKey());
      if (charToByte.length() < 8) {
        int missingBits = 8 - charToByte.length();
        for (int i = 0; i < missingBits; i++) {
          charToByte = '0' + charToByte;
        }
      }
      for (int i = 0; i < charToByte.length(); i++) {
        bos.writeBit(Integer.parseInt(charToByte.charAt(i) + ""));
      }
    } else {
      bos.writeBit(0);
      writeTreeInFile(huffmanTree.getLeftChild(), bos);
      writeTreeInFile(huffmanTree.getRightChild(), bos);
    }
  }

  /**
   * <p>Writes a single character to the binary file using the correspondance table.</p>
   *
   * @param singleChar The character to be written.
   * @param correspondance The correspondance table.
   * @param bos The bit writer.
   */
  private void writeToFile(
    char singleChar,
    Map<Character, String> correspondance,
    BitOutputStream bos
  ) {
    String path = correspondance.get(singleChar);
    for (char direction : path.toCharArray()) {
      if (direction == '0') {
        bos.writeBit(0);
      } else {
        bos.writeBit(1);
      }
    }
  }

  private int countCompressedFileCharacters(String filePath) {
    int count = 0;
    try {
      FileInputStream fis = new FileInputStream(filePath);
      BufferedInputStream bis = new BufferedInputStream(fis);
      while (bis.read() != END_OF_FILE) {
        count++;
      }
      bis.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return count;
  }

  public void Decompresser(String nomFichierEntre, String nomFichierSortie) {
    try {
      BitInputStream bis = new BitInputStream(nomFichierEntre);
      FileOutputStream fos = new FileOutputStream(nomFichierSortie);
      BufferedOutputStream bos = new BufferedOutputStream(fos);

      String huffmanTreePrefix = "";
      for (int i = 0; i < 32; i++) {
        huffmanTreePrefix += bis.readBit();
      }
      int limit = Integer.parseInt(huffmanTreePrefix, 2);

      Node huffmanTreeRoot = this.readTree(bis, limit);

      String compressedFilePrefix = "";
      for (int i = 0; i < 32; i++) {
        compressedFilePrefix += bis.readBit();
      }
      limit = Integer.parseInt(compressedFilePrefix, 2);

      int direction;
      Node tree = huffmanTreeRoot;
      while (limit != 0 && (direction = bis.readBit()) != END_OF_FILE) {
        if (direction == 0) {
          tree = tree.getLeftChild();
          if (tree.isLeaf()) {
            bos.write(tree.getKey());
            tree = huffmanTreeRoot;
            limit--;
          }
        } else {
          tree = tree.getRightChild();
          if (tree.isLeaf()) {
            bos.write(tree.getKey());
            tree = huffmanTreeRoot;
            limit--;
          }
        }
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
   * <p>Reads the huffman tree in binary form that is found in a compressed file <br>
   * and returns a single node as a huffman tree (with the frequency all set to 0).</p>
   *
   * @param bis The bit reader (Simply pass {@code new BitInputStream(filePath)})
   * @return A single node as a huffman tree (with the frequency all set to 0).
   */
  private Node readTree(BitInputStream bis, int limit) {
    int value;
    while (limit != 0 && (value = bis.readBit()) != END_OF_FILE) {
      limit--;
      if (value == 1) {
        String binary = "";
        for (int i = 0; i < 8; i++) {
          binary += bis.readBit();
          limit--;
        }
        return new Node((char) Integer.parseInt(binary, 2), 0);
      } else {
        Node internalNode = new Node(Node.EMPTY_KEY, 0);
        internalNode.setLeftChild(readTree(bis, limit));
        internalNode.setRightChild(readTree(bis, limit));
        return internalNode;
      }
    }
    return null;
  }

  /**
   * <p>
   * Creates an unsorted frequency table.
   * </p>
   * <p>
   * It will read a file and create a table containing the number of <br>
   * occurrences of each character in it.
   * </p>
   *
   * @param filePath The location of the file.
   * @return A map of characters and integers.
   */
  public Map<Character, Integer> createFrequencyTable(String filePath) {
    HashMap<Character, Integer> frequency = new HashMap<>();
    //int nbBytes = 0;
    try {
      File file = new File(filePath);
      FileInputStream reader = new FileInputStream(file);
      BufferedInputStream bis = new BufferedInputStream(reader);
      int singleCharInt;
      char singleChar;

      while ((singleCharInt = bis.read()) != END_OF_FILE) {
        singleChar = (char) singleCharInt;
        if (!frequency.containsKey(singleChar)) {
          frequency.put(singleChar, 1);
        } else {
          frequency.put(singleChar, frequency.get(singleChar) + 1);
        }
      }

      bis.close();
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return frequency;
  }

  /**
   * Creates a Huffman using a given frequency table.
   *
   * @param frequency Frequency table.
   * @return A single node as a binary tree.
   */
  public Node createHuffmanTree(String filePath) {
    Map<Character, Integer> frequency = this.createFrequencyTable(filePath);

    PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
    for (Map.Entry<Character, Integer> entry : frequency.entrySet()) {
      priorityQueue.offer(new Node(entry.getKey(), entry.getValue()));
    }

    return combine(priorityQueue);
  }

  /**
   * <p>Combines a priority queue of nodes to form a binary tree.</p>
   *
   * @param priorityQueue The priority queue of nodes.
   * @return A single node in the form of a binary tree.
   */
  private Node combine(PriorityQueue<Node> priorityQueue) {
    if (priorityQueue.size() == 1) {
      return priorityQueue.poll();
    }

    Node firstMin = priorityQueue.poll();
    Node secondMin = priorityQueue.poll();
    Node combined = new Node(
      Node.EMPTY_KEY,
      firstMin.getValue() + secondMin.getValue()
    );
    combined.setLeftChild(firstMin);
    combined.setRightChild(secondMin);

    priorityQueue.offer(combined);

    return combine(priorityQueue);
  }

  /**
   * <p>Creates a correspondance table using a given node.</p>
   * <p>It assumes that the nodes are arranged as a Huffman tree.</p>
   *
   * @param node
   * @return
   */
  private Map<Character, String> createCorrespondanceTable(Node node) {
    return this.correspondanceTableCreator(
        node,
        new StringBuilder(),
        new HashMap<>()
      );
  }

  /**
   * <p>Creates a correspondance table for a given node assuming its <br>
   * child nodes are arranged as a Huffman tree.</p>
   * @param node The root node.
   * @param path The path of the node (Simply pass {@code new StringBuilder()})
   * @param correspondance The correspondance table (Simply pass {@code new HashMap<Character, String>()})
   * @return
   */
  private Map<Character, String> correspondanceTableCreator(
    Node node,
    StringBuilder path,
    Map<Character, String> correspondance
  ) {
    // Is leaf
    if (node.isLeaf()) {
      correspondance.put(node.getKey(), path.toString());
    } else {
      // left child
      correspondanceTableCreator(
        node.getLeftChild(),
        path.append('0'),
        correspondance
      );
      path.deleteCharAt(path.length() - 1); // Flush

      correspondanceTableCreator(
        node.getRightChild(),
        path.append('1'),
        correspondance
      );
      path.deleteCharAt(path.length() - 1); // Flush
    }

    return correspondance;
  }

  public static void main(String[] args) {
    String initial = "src/laboratoire2/exemple.txt";
    String compressed = "src/laboratoire2/Temp.bin";
    String decompressed = "src/laboratoire2/exemple2.txt";

    Huffman huff = new Huffman();
    huff.Compresser(initial, compressed);
    huff.Decompresser(compressed, decompressed);

    File initialFile = new File(initial);
    File compressedFile = new File(compressed);

    System.out.println("Initial size: " + initialFile.length() + " bytes");
    System.out.println(
      "Compressed size: " + compressedFile.length() + " bytes"
    );
    System.out.println(
      "Compression ratio: " +
      String.format(
        "%.2f",
        (double) initialFile.length() / (double) compressedFile.length()
      )
    );
  }
}
