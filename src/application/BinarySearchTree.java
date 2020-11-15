package application;

import java.util.ArrayList;
import java.util.List;

// If there's time, feel free to implement a self-balancing binary tree (red-black, preferably).
// Chronologically sorted binary tree w/ O(N) full traversal, O(log(N)) delete, O(log(N)) add. 
// If given DateTime is the same for two nodes/entries, they are sorted in order of insertion.
public class BinarySearchTree {

	// Node used for BST.
	private class Node {
		public Entry key; // Key == entry. We sort by its DateTime.
		public Node left;
		public Node right;
		public Node parent; // TODO: Implement parent assigning w/in insert once we need to implement delete.

		public Node(Entry key) {
			this.key = key;
			this.left = null;
			this.right = null;
			this.parent = null;
		}
	}

	public Node root;

	public BinarySearchTree() {
		this.root = null;
	}
	
	/**
	 * Search for a node using the key's key. 
	 * (I would prefer to search using id, but such is difficult since BST is traversed using DateTime.)
	 * 
	 * @param id The key of the node being searched for.
	 * @return The node corresponding to the given key. Returns null if not found.
	 */
	public Node search(Entry key) {
		Node node = null;
		node = searchRecursive(root, key);
		return node;
	}

	// Recursive search implementation.
	private Node searchRecursive(Node root, Entry key) {

		// Base case: leaf has been reached, w/o success.
		if (root == null) {
			return root;
		}

		// Node with input key has been found.
		else if (key.getId() == root.key.getId()) {
			return root;
		}

		// Recurse on right child if desired key is greater than current node's id.
		else if (key.getDateTime().compareTo(root.key.getDateTime()) >= 0) {
			return searchRecursive(root.right, key);
		}

		// Recurse on left child if desired key is less than current node's id.
		else if (key.getDateTime().compareTo(root.key.getDateTime()) < 0) {
			return searchRecursive(root.left, key);
		}

		// Node with desired key not present in BST.
		return null;
	}

	/**
	 * Insert a new Node with a given key into BST.
	 * 
	 * @param key The key of the inserted node.
	 */
	public void insert(Entry key) {
		root = insertRecursive(root, key);
	}

	// Recursive insert implementation.
	private Node insertRecursive(Node root, Entry key) {

		// Base case: given root is the leaf. Insert node /w desired key.
		if (root == null) {
			root = new Node(key);
			return root;
		}	

		// Recurse down the tree until a leaf is reached.
		if (MyDateTime.compareDateTime(key.getDateTime(), root.key.getDateTime()) >= 0) {
			root.right = insertRecursive(root.right, key);
		} else if (MyDateTime.compareDateTime(key.getDateTime(), root.key.getDateTime()) < 0) {
			root.left = insertRecursive(root.left, key);
		}

		// Given root's key is the desired key. Do nothing and return it as is.
		return root;
	}

	/**
	 * Recursively does an in-order traversal of the BST (and stores the ordered
	 * keys into an array list).
	 */
	public List<Entry> inorderTraversal() {
		List<Entry> entries = new ArrayList<>();
		entries = inorderRecursive(this.root, entries);
		return entries;
	}

	// Recursive in-order traversal implementation.
	private List<Entry> inorderRecursive(Node root, List<Entry> entryList) {
		if (root != null) {
			entryList = inorderRecursive(root.left, entryList);
			System.out.println(root.key.toString());
			entryList.add(root.key);
			entryList = inorderRecursive(root.right, entryList);
		}
		return entryList;
	}

	/**
	 * Returns the bottom left most node, which will be the minimum.
	 * 
	 * @param node The root node of the BST.
	 * @return Returns the node with the minimum key.
	 */
	public Node minimum(Node node) {
		while (node.left != null) {
			node = node.left;
		}
		return node;
	}
}
