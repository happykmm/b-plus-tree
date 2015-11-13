

import java.util.ArrayList;


public class BPlusTree<Key extends Comparable<? super Key>, Value>
{
    /** Pointer to the root node. It may be a leaf or an inner node, but it is never null. */
    private Node root;
    /** the maximum number of keys in the leaf node, M must be > 0 */
    private final int M;
    /** the maximum number of keys in inner node, the number of pointer is N+1, N must be > 2 */
    private final int N;

    /** Create a new empty tree. */
    public BPlusTree(int n) {
    	this(n, n);
    }

    public BPlusTree(int m, int n) {
        M = m;
        N = n;
        root = new LNode();
    }

    public void insert(Key key, Value value) {
    	System.out.println("insert key=" + key);
    	Split result = root.insert(key, value);
        if (result != null) {
	    // The old root was split into two parts.
	    // We have to create a new root pointing to them
            INode _root = new INode();
            _root.number = 1;
            _root.keys.add(0, result.key);
            _root.children.add(0, result.left);
            _root.children.add(1, result.right);
            root = _root;
        }
         
        this.dump();
    }

    public Value find(Key key) {
        Node node = root;
        while (node instanceof BPlusTree.INode) { // need to traverse down to the leaf
        	INode inner = (INode) node;
            int index = inner.getLocation(key);
            node = inner.children.get(index);
        }
        //We are @ leaf after while loop
        LNode leaf = (LNode) node;
        int index = leaf.getLocation(key);
        if (index < leaf.number && leaf.keys.get(index).equals(key)) {
        	return leaf.values.get(index);
        } else {
        	return null;
        }
    }

    public void dump() {
    	root.dump();
    }

    
    
    abstract class Node {
		protected int number; 
		protected ArrayList<Key> keys;
	
		public int getLocation(Key key) {
			int i;
			for (i = 0; i < number; i++) {
				if (keys.get(i).compareTo(key) >= 0)
					break;
		    }
		    return i;
		}
		
		// returns null if no split, otherwise returns split info
		abstract public Split insert(Key key, Value value);
		abstract public void dump();
    }

    
    
    class LNode extends Node {
		ArrayList<Value> values = new ArrayList<Value>();
		{ keys = new ArrayList<Key>(); }
		
		public Split insert(Key key, Value value) {
		    int index = getLocation(key);
		    if (index < number && keys.get(index).equals(key)) {
		    	values.set(index, value);
		    	return null;
		    }
		    keys.add(index, key);
			values.add(index, value);
			number++;
		    if (number <= M)  return null;
		    /* number > M, we need to split it */
	    	int middle = (number + 1) / 2;
			LNode sibling = new LNode();
			sibling.number = this.number - middle;
			sibling.keys = new ArrayList<Key>(this.keys.subList(middle, number));
			sibling.values = new ArrayList<Value>(this.values.subList(middle, number));
			this.keys = new ArrayList<Key>(this.keys.subList(0, middle));
			this.values = new ArrayList<Value>(this.values.subList(0, middle));
			this.number = middle;
			Split result = new Split(sibling.keys.get(0), this, sibling);
			return result;
		}
	
		public void dump() {
		    System.out.print("lNode");
		    for (int i = 0; i < number; i++){
		    	System.out.print(" "+keys.get(i));
		    }
		    System.out.println();
		}
    }

    
    
    class INode extends Node {
		ArrayList<Node> children = new ArrayList<Node>();
		{ keys = new ArrayList<Key>(); }
	
		public Split insert(Key key, Value value) {
			int index = getLocation(key);
		    Split result = children.get(index).insert(key, value);
		    if (result == null)  return null;
		    index = getLocation(result.key);
		    keys.add(index, result.key);
		    children.set(index, result.left);
		    children.add(index+1, result.right);
		    number++;
		    if (number <= N)  return null;
		    /* number > N, we need to split it */
    		int middle = number / 2;
    		INode sibling = new INode();
    		sibling.number = this.number - middle -1;
    		sibling.keys = new ArrayList<Key>(this.keys.subList(middle+1, number)) ;
    		sibling.children = new ArrayList<Node>(this.children.subList(middle+1, number+1)) ;
    		Split split = new Split(keys.get(middle), this, sibling);
    		this.keys = new ArrayList<Key>(this.keys.subList(0, middle)) ;
    		this.children = new ArrayList<Node>(this.children.subList(0, middle+1)) ;
    		this.number = middle;
    		return split;
		}
		
		public void dump() {
		    System.out.print("iNode");
		    for (int i=0; i<number; i++) {
		    	System.out.print(" "+keys.get(i));
		    }
		    System.out.println();
		    
		    for (int i = 0; i <=number; i++) {
				children.get(i).dump();
		    }

		    System.out.print("iNode");
		    for (int i=0; i<number; i++) {
		    	System.out.print(" "+keys.get(i));
		    }
		    System.out.println();
		}
    }

    class Split {
		public final Key key;
		public final Node left;
		public final Node right;
	
		public Split(Key k, Node l, Node r) {
		    key = k; left = l; right = r;
		}
    }
}