public class BinarySearchTree<T extends Comparable<T>> implements SortedCollection<T> {

    protected BSTNode<T> root;

    /**
     * Performs the naive binary search tree insert algorithm to recursively
     * insert the provided newNode (which has already been initialized with a
     * data value) into the provided tree/subtree.  When the provided subtree
     * is null, this method does nothing.
     */
    protected void insertHelper(BSTNode<T> newNode, BSTNode<T> subtree) {
        // TODO: define and make use of this method in BinarySearchTree class
    }

    @Override
    public boolean contains(Comparable<T> data) {
        return false;
    }

    @Override
    public void insert(T data) throws NullPointerException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void clear() {

    }
}
