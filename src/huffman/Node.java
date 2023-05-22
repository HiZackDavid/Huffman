package huffman;

public class Node implements Comparable<Node> {

  /**
   * Represents an empty character.
   */
  public static final char EMPTY_KEY = Character.MIN_VALUE;

  private char key;
  private int value;
  private Node leftChild;
  private Node rightChild;

  Node(char key, int value) {
    this.key = key;
    this.value = value;
    this.rightChild = null;
    this.leftChild = null;
  }

  public char getKey() {
    return this.key;
  }

  public int getValue() {
    return this.value;
  }

  public boolean hasChildren() {
    return this.getLeftChild() != null && this.getRightChild() != null;
  }

  public void setKey(char key) {
    this.key = key;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public Node getLeftChild() {
    return this.leftChild;
  }

  public void setLeftChild(Node leftChild) {
    this.leftChild = leftChild;
  }

  public Node getRightChild() {
    return this.rightChild;
  }

  public void setRightChild(Node rightChild) {
    this.rightChild = rightChild;
  }

  public boolean isLeaf() {
    return (this.leftChild == null && this.rightChild == null);
  }

  @Override
  public int compareTo(Node node) {
    if (this.getValue() > node.getValue()) {
      return 1;
    } else if (this.getValue() < node.getValue()) {
      return -1;
    } else {
      return 0;
    }
  }

  @Override
  public String toString() {
    return "{" + this.key + ", " + this.value + "}";
  }
}
