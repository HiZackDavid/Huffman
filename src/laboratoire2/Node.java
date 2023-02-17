package laboratoire2;

public class Node {

  private char key;
  private int value;
  private Node leftChild;
  private Node rightChild;

  Node() {
    this.rightChild = null;
    this.leftChild = null;
  }

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

  public boolean hasChildren(){
    return this.getLeftChild()!=null && this.getRightChild() !=null;
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
    this.value += leftChild.getValue();
  }

  public Node getRightChild() {
    return this.rightChild;
  }

  public void setRightChild(Node rightChild) {
    this.rightChild = rightChild;
    this.value += rightChild.getValue();
  }
}
