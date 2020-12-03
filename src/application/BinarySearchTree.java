package application;

import java.util.ArrayList;
import java.util.List;

// If there's time, feel free to implement a self-balancing binary tree (red-black, preferably).
// Chronologically sorted binary tree w/ O(N) full traversal, O(log(N)) delete, O(log(N)) add. 
// If given DateTime is the same for two nodes/entries, they are sorted in order of insertion.
public class BinarySearchTree {
	
	// Immediately initializes the one and only database instance at the start of the JVM.
	private static BinarySearchTree myBST = new BinarySearchTree();

	public Node root;
	
	/** 
	 * Private default constructor to prevent more than one BST instantiation.
	 */
	private BinarySearchTree() {
		this.root = null;
	}
	
	/**
	 * Retrieves an instance of the singleton, already initialized BST.
	 * @return Returns the instance of the BST.
	 */
	public static BinarySearchTree getBSTInstance() {
		return myBST;
	}

	// Node used for BST.
	private class Node {
		public Entry key; // Key == entry. We sort by its DateTime.
		public Node left;
		public Node right;

		public Node(Entry key) {
			this.key = key;
			this.left = null;
			this.right = null;
		}
	}
	
	/**
	 * Initialize the BST from database.
	 * @param db The database to get data from.
	 */
	public static void initializeBSTFromDB(Database db) {
		// Query into SQLite database and get ALL rows.
		List<String[]> allEntries = db.queryAll("entries", null);
		
		// Add the rows to BST.
		if (allEntries != null && !allEntries.isEmpty()) {
			for (String[] row : allEntries) {
				myBST.insert(new Entry(row));
			}
		}
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
	 * Delete a Node with a key containing given ID from the BST.
	 * @param id The id of the deleted node.
	 */
	public void delete(Entry key) {
		root = deleteRecursive(root, key);
	}
	
	// Recursive delete implementation.
	private Node deleteRecursive(Node root, Entry key) {
		// Base case: tree is empty; nothing to be deleted.
		if (root == null) {
			return root;
		}
		
		// Check if current Node is the one to delete.
		if (root.key.getId() == key.getId()) {
			// Case 1: Node has 1 or 0 children.
			// If 0 children, the node becomes null.
			// If 1 child, the node is replaced by its child.
			if (root.left == null) {
				root = root.right;
				return root;
			}
			else if (root.right == null) {
				root = root.left;
				return root;
			}
			// Case 2: Node has 2 children.
			else {
				// Replace the deleted root with the minimum value of the tree.
				root.key = minValue(root.right);
				// Get rid of the previous version of the minimum value.
				root.right = deleteRecursive(root.right, root.key);
			}	
		}
		
		// Recurse down the tree until a Node with given ID is reached.
		else if (MyDateTime.compareDateTime(key.getDateTime(), root.key.getDateTime()) < 0)
            root.left = deleteRecursive(root.left, key);
        else if (MyDateTime.compareDateTime(key.getDateTime(), root.key.getDateTime()) >= 0)
            root.right = deleteRecursive(root.right, key);
		
		return root;
	}
	
	// Gets the minimum value of the tree.
    private Entry minValue(Node root)
    {
        Entry minVal = root.key;
        while (root.left != null) 
        {
            minVal = root.left.key;
            root = root.left;
        }
        return minVal;
    }

	/**
	 * Recursively does an in-order traversal of the BST (and stores the ordered
	 * keys into an array list).
	 */
	public List<Entry> inorderTraversal() {
		List<Entry> entries = new ArrayList<>();
		System.out.println("======in-order START======");
		entries = inorderRecursive(this.root, entries);
		System.out.println("======in-order DONE======");
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
