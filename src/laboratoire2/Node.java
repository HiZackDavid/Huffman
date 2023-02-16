package laboratoire2;

public class Node {

  private char key;
  private int value;
  private Node leftChild;
  private Node rightChild;

  Node(int value) {
    this.value = value;
    this.rightChild = null;
    this.leftChild = null;
  }

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

  public void add(int value) {}
}
