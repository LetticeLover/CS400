public class BinarySearchTree<T extends Comparable<T>> implements SortedCollection<T> {

    /**
     * The binary search tree's root node.
     */
    protected BSTNode<T> root;

    /**
     * Default constructor that initializes the root node to null.
     */
    public BinarySearchTree() { root = null; }

    /**
     * Performs the naive binary search tree insert algorithm to recursively
     * insert the provided newNode (which has already been initialized with a
     * data value) into the provided tree/subtree.  When the provided subtree
     * is null, this method does nothing.
     * @param newNode the node to insert into the BST as a leaf
     * @param subtree the current node we are searching from for insertion
     */
    protected void insertHelper(BSTNode<T> newNode, BSTNode<T> subtree) {
        if (subtree != null) {
            if (newNode.data.equals(subtree.data)) {
                if (subtree.left == null) {
                    newNode.setUp(subtree);
                    subtree.setLeft(newNode);
                } else {
                    insertHelper(newNode, subtree.left);
                }
            } else if (newNode.data.compareTo(subtree.data) < 0) {
                if (subtree.left == null) {
                    newNode.setUp(subtree);
                    subtree.setLeft(newNode);
                } else {
                    insertHelper(newNode, subtree.left);
                }
            } else {
                if (subtree.right == null) {
                    newNode.setUp(subtree);
                    subtree.setRight(newNode);
                } else {
                    insertHelper(newNode, subtree.right);
                }
            }
        }
    }

    /**
     * Search the BST for a value
     * @param data the value to check for in the collection
     * @return true if the value is found, false otherwise
     */
    @Override
    public boolean contains(Comparable<T> data) {
        return lookup(root, data);
    }

    /**
     * Recursively search the BST for the specified value.
     * @param node the current node we are comparing to data
     * @param data the value we are searching for
     * @return true if the value is within the BST, false otherwise
     */
    private boolean lookup(BSTNode<T> node, Comparable<T> data) {

        if (node == null) {
            return false;
        }

        if (node.data.equals(data)) {
            return true;
        }

        if (data.compareTo(node.data) < 0) {
            return lookup(node.left, data);
        } else {
            return lookup(node.right, data);
        }
    }

    /**
     * Inserts a new value as a leaf BSTNode of the BST.
     * @param data the new value being inserted
     * @throws NullPointerException if data argument is null, we do not
     * allow null values to be stored within a BST
     */
    @Override
    public void insert(T data) throws NullPointerException {
        if (data == null) {
            throw new NullPointerException();
        }

        BSTNode<T> newNode = new BSTNode<>(data);
        if (root == null) {
            root = newNode;
        } else {
            insertHelper(newNode, root);
        }
    }

    /**
     * Count the number of nodes in the BST including duplicates.
     * @return the number of nodes in the BST
     */
    @Override
    public int size() {
        if (root == null) {
            return 0;
        }
        return count(root);
    }

    /**
     * Recursively count the number of nodes in the BST.
     * @param node the BSTNode to count from this iteration
     * @return the number of nodes in the subtree with the node
     * parameter as its root
     */
    private int count(BSTNode<T> node) {
        int count = 1;
        if (node.left != null) {
            count += count(node.left);
        }
        if (node.right != null) {
            count += count(node.right);
        }
        return count;
    }

    /**
     * Checks whether the BST is empty.
     * @return true if the BST has no root node, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Removes all values from the collection by removing the
     * pointer to the root node.
     */
    @Override
    public void clear() {
        root = null;
    }

    /**
     * Test the BST class and log results to the console.
     * @param args
     */
    public static void main(String[] args) {
        BinarySearchTree<Integer> dummyTree = new BinarySearchTree<>();

        System.out.println("Testing insert(), size(), clear(), and isEmpty():");
        if (dummyTree.test1()) {
            System.out.println("Test 1 Passed.");
        } else {
            System.out.println("Test 1 Failed.");
        }

        System.out.println("Testing contains():");
        if (dummyTree.test2()) {
            System.out.println("Test 2 Passed.");
        } else {
            System.out.println("Test 2 Failed.");
        }

        System.out.println("Testing duplicates and negative numbers:");
        if (dummyTree.test3()) {
            System.out.println("Test 3 Passed.");
        } else {
            System.out.println("Test 3 Failed.");
        }

    }

    /**
     * Test inserting nodes, calculating the size of the BST,
     * clearing the BST, and checking whether it is empty.
     * @return true if all tests pass, false otherwise
     */
    private boolean test1() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.insert(10);
        tree.insert(20);
        tree.insert(5);
        tree.insert(15);

        if (tree.size() != 4) { return false; }

        tree.clear();
        if (!tree.isEmpty()) { return false; }

        tree.insert(25);
        tree.insert(30);
        tree.insert(40);
        tree.insert(2);
        tree.insert(7);
        tree.insert(10);

        if (tree.size() != 6) { return false; }

        if (tree.isEmpty()) { return false; }
        tree.clear();
        if (!tree.isEmpty()) { return false; }

        return true;
    }


    /**
     * Test finding values of the root,
     * left and right leaves, and interior nodes.
     * @return true if all tests pass, false otherwise
     */
    private boolean test2() {
        BinarySearchTree<String> tree = new BinarySearchTree<>();
        tree.insert("b");
        tree.insert("a");
        tree.insert("c");
        tree.insert("h");
        tree.insert("i");
        tree.insert("j");
        tree.insert("d");
        tree.insert("e");
        tree.insert("l");
        tree.insert("m");
        tree.insert("f");
        tree.insert("g");
        tree.insert("k");
        tree.insert("n");

        if (!tree.contains("a")) { return false; }
        if (!tree.contains("b")) { return false; }
        if (!tree.contains("c")) { return false; }
        if (!tree.contains("h")) { return false; }
        if (!tree.contains("m")) { return false; }
        if (!tree.contains("g")) { return false; }
        if (!tree.contains("k")) { return false; }
        if (!tree.contains("e")) { return false; }

        return true;
    }

    /**
     * Test inserting and finding negative and duplicate values.
     * @return true if all tests pass, false otherwise
     */
    private boolean test3() {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.insert(-8);
        tree.insert(4);
        tree.insert(3);
        tree.insert(-3);
        tree.insert(3);
        tree.insert(3);

        if (tree.size() != 6) { return false; }
        if (!tree.contains(-3)) { return false; }
        if (!tree.contains(3)) { return false; }

        return true;
    }


}