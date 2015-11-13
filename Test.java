

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		BSTreeNode<String> root = new BSTreeNode<String>("15");
//		String[] nodes = {"10", "30", "5", "13", "17", "50", "16", "26", "40"};
//		for (String node : nodes) {
//			root.insert(node);
//		}
//		root.delete("15");
//		root.delete("30");
//		System.out.println(root);
		
		BPlusTree<Integer, String> tree = new BPlusTree<Integer, String>(3);
		int[] keys = {2,3,5,7,11,17,19,23,29,31,9,10,8};
		for (int key : keys) {
			tree.insert(key, "" + key);
		}

	}

}
