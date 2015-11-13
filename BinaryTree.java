
public class BinaryTree<T extends Comparable<T>> {
	public T value;
	private BinaryTree<T> leftChild, rightChild;
	
	public BinaryTree(T value) {
		this.value = value;
	}
	
	public BinaryTree<T> leftChild() {
		if (this.leftChild != null && this.leftChild.value == null) {
			this.leftChild = null;
		}
		return this.leftChild;
	}
	
	public void leftChild(BinaryTree<T> leftChild) {
		this.leftChild = leftChild;
	}
	
	public BinaryTree<T> rightChild() {
		if (this.rightChild != null && this.rightChild.value == null) {
			this.rightChild = null;
		}
		return this.rightChild;
	}
	
	public void rightChild(BinaryTree<T> rightChild) {
		this.rightChild = rightChild;
	}
	
	public BinaryTree<T> find(T target) {
		int cmp = target.compareTo(this.value);
		if (cmp == 0) return this;
		if (cmp < 0) 
			if (this.leftChild() != null)
				return this.leftChild().find(target);
		if (cmp > 0)
			if (this.rightChild() != null)
				return this.rightChild().find(target);
		return null;
	}
	
	public void insert(T target) {
		int cmp = target.compareTo(this.value);
		if (cmp == 0)  return;
		if (cmp < 0) {
			if (this.leftChild() != null)
				this.leftChild().insert(target);
			else
				this.leftChild(new BinaryTree<T>(target));
		} else {
			if (this.rightChild() != null)
				this.rightChild().insert(target);
			else
				this.rightChild(new BinaryTree<T>(target));
		}
	}
	
	public BinaryTree<T> next() {
		BinaryTree<T> next = this.rightChild();
		if (next == null)  return null;
		while (next.leftChild() != null)
			next = next.leftChild();
		return next;
	}
	
	public void delete(T target) {
		BinaryTree<T> node = this.find(target);
		if (node == null)  return;
		BinaryTree<T> next = node.next();
		if (next == null) {
			node = node.leftChild(); 
		} else {
			node.value = next.value;
			next.value = null;
		}
	}
	
	public String toString() {
		String left = null, right = null;
		if (this.leftChild() != null)
			left = this.leftChild().toString();
		if (this.rightChild() != null)
			right = this.rightChild().toString();
		return "{" +
				"value: " + this.value.toString() + ", \n" +
				"leftChild: " + left + ", \n" +
				"rightChild: " + right + "}\n";
		
	}
}
