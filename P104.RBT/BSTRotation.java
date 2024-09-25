import java.util.Objects;

public class BSTRotation<T extends Comparable<T>> extends BinarySearchTree<T> {


    public static void main(String[] args) {
        BSTRotation<String> dummyTree = new BSTRotation<>();
        System.out.println("Testing rotation involving the root (right rotation):");
        if (dummyTree.test1()) {
            System.out.println("Test Passed!");
        } else {
            System.out.println("Test Failed.");
        }
        System.out.println("Testing input validation:");
        if (dummyTree.test2()) {
            System.out.println("Test Passed!");
        } else {
            System.out.println("Test Failed.");
        }
        System.out.println("Testing rotation involving non-root nodes (left rotation):");
        if (dummyTree.test3()) {
            System.out.println("Test Passed!");
        } else {
            System.out.println("Test Failed.");
        }
    }

    /**
     * Performs the rotation operation on the provided nodes within this tree.
     * When the provided child is a left child of the provided parent, this
     * method will perform a right rotation. When the provided child is a right
     * child of the provided parent, this method will perform a left rotation.
     * When the provided nodes are not related in one of these ways, this
     * method will either throw a NullPointerException: when either reference is
     * null, or otherwise will throw an IllegalArgumentException.
     *
     * @param child is the node being rotated from child to parent position
     * @param parent is the node being rotated from parent to child position
     * @throws NullPointerException when either passed argument is null
     * @throws IllegalArgumentException when the provided child and parent
     *     nodes are not initially (pre-rotation) related that way
     */
    protected void rotate(BSTNode<T> child, BSTNode<T> parent)
            throws NullPointerException, IllegalArgumentException {

        // Cannot rotate a null node.
        if (child == null || parent == null) {
            throw new NullPointerException("One of the nodes is a null reference that cannot be rotated.");
        }
        // If child is not a right or left child of parent then the two nodes cannot be rotated.
        if (parent.right != child && parent.left != child) {
            throw new IllegalArgumentException("These nodes do not have a parent-child relationship and cannot be rotated.");
        }

        // Perform a left rotation if it is a right child
        if (child.isRightChild()) {
            if (child.left != null) {
                parent.setRight(child.left);
            } else {
                parent.setRight(null);
            }
            child.setLeft(parent);
        } else { // Else it is a left child so perform a right rotation
            if (child.right != null) {
                parent.setLeft(child.right);
            } else {
                parent.setLeft(null);
            }
            child.setRight(parent);
        }

        // Update the grandparent node's child reference
        if (parent.up != null) {
            if (parent.isRightChild()) {
                parent.up.setRight(child);
            } else {
                parent.up.setLeft(child);
            }
        }

        // Set the child's parent to what was its grandparent
        child.setUp(parent.up);
        // Set the parent's parent to the child node
        parent.setUp(child);

        // If the parent was the root of the tree, the child is the new root so set it as such
        if (root == parent) {
            root = child;
        }

    }

    public boolean test1() {
        BSTRotation<String> tree = new BSTRotation<>();
        BSTNode<String> child = tree.root.left;
        tree.rotate(tree.root.left, tree.root);
        // The parent and child did not get rotated correctly.
        if (!Objects.equals(tree.root.data, "B") || !Objects.equals(tree.root.right.data, "E")) {
            return false;
        }
        // The right child of the child node did not get rotated/moved correctly.
        if (!Objects.equals(tree.root.right.left.data, "D")) {
            return false;
        }
        // The tree did not correctly update the root node.
        if (tree.root != child) {
            return false;
        }

        return true;
    }

    public boolean test2() {
        BSTRotation<String> tree = new BSTRotation<>();
        try {
            tree.rotate(tree.root.left, tree.root.right);
        } catch (NullPointerException e) {
            // Rotate should not be throwing a NullPointerException in this case.
            System.out.println("NullPointer");
            return false;
        } catch (IllegalArgumentException e) {
            // The method successfully threw the exception.
            System.out.println("IllegalArgument");
            return true;
        }

        return false;
    }

    public boolean test3() {
        BSTRotation<String> tree = new BSTRotation<>();

        tree.rotate(tree.root.right.right, tree.root.right);
        // The parent and child nodes did not rotate correctly.
        if (!tree.root.right.data.equals("J") || !tree.root.right.left.data.equals("H")) {
            return false;
        }
        // The left child of the child node did not rotate correctly.
        if (!tree.root.right.left.right.data.equals("I")) {
            return false;
        }

        return true;
    }

}
