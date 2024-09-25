import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RedBlackTree<T extends Comparable<T>> extends BSTRotation<T> {

    /**
     * Checks if a new red node in the RedBlackTree causes a red property violation
     * by having a red parent. If this is not the case, the method terminates without
     * making any changes to the tree. If a red property violation is detected, then
     * the method repairs this violation and any additional red property violations
     * that are generated as a result of the applied repair operation.
     * @param newRedNode a newly inserted red node, or a node turned red by previous repair
     */
    protected void ensureRedProperty(RBTNode<T> newRedNode) {
        // If the newRedNode has no parent then it is the root of the tree, and we
        // update the tree to reflect that as well as set the root's color to black.
        if (newRedNode.getUp() == null) {
            if (newRedNode.isRed()) {
                newRedNode.flipColor();
            }
            root = newRedNode;
            return;
        }

        RBTNode<T> parent = newRedNode.getUp();
        // Parent being red means there is a red property violation.
        // If the parent is not red there are no violations and we do nothing.
        if (parent.isRed()) {
            RBTNode<T> grandparent = parent.getUp();
            RBTNode<T> aunt;
            // Determine which child of the grandparent is the aunt.
            if (parent.isRightChild()) {
                aunt = grandparent.getLeft();
            } else {
                aunt = grandparent.getRight();
            }
            // aunt is red so we can just flip colors of parent, aunt, and grandparent (Red Aunt case).
            if (aunt != null && aunt.isRed()) {
                parent.flipColor();
                aunt.flipColor();
                grandparent.flipColor();
                // Have to ensure that flipping the grandparent's color didn't introduce another violation.
                ensureRedProperty(grandparent);
            } else {

                // newRedNode and parent are aligned we can just do the normal algorithm (Black-Line case).
                if (parent.isRightChild() == newRedNode.isRightChild()) {
                    rotate(parent, grandparent);
                    parent.flipColor();
                    grandparent.flipColor();
                    // Don't need to call ensureRedProperty recursively here because parent will be black
                    // and thus won't cause any new red property violations.
                } else {    // newRedNode and parent need to be rotated before we carry on with algorithm (Black-Zig case).
                    rotate(newRedNode, parent);
                    rotate(newRedNode, grandparent);
                    newRedNode.flipColor();
                    grandparent.flipColor();
                    // Same as above, flipping newRedNode's color makes it black so
                    // it won't cause any new red property violations.
                }
            }
        }
    }

    @Override
    public void insert(T data) throws NullPointerException {
        // If there is no data to insert throw an error.
        if (data == null) {
            throw new NullPointerException();
        }
        // Instantiate a new node created from the data.
        RBTNode<T> newNode = new RBTNode<>(data);
        // If there is no root, then set this new node as the root.
        if (root == null) {
            // Set the new root's color to black.
            newNode.flipColor();
            root = newNode;
        } else {
            // Basic BST insertion.
            insertHelper(newNode, root);
            // RBT checking for red property violations.
            ensureRedProperty(newNode);
        }
    }

    /**
     * Tests insertion of a new node resulting in a Red Aunt case.
     * Tests the specific case of quiz question 2.
     */
    @Test
    public void testRBT1() {
        // Instantiate a new RedBlackTree.
        RedBlackTree<String> redBlackTree = new RedBlackTree<>();
        // Insert nodes into the new tree to replicate quiz question 2.
        redBlackTree.insert("N");
        redBlackTree.insert("F");
        redBlackTree.insert("S");
        redBlackTree.insert("B");
        redBlackTree.insert("J");
        redBlackTree.insert("P");
        redBlackTree.insert("U");
        redBlackTree.insert("O");
        redBlackTree.insert("R");
        redBlackTree.insert("M");
        // Assert that the tree is ordered correctly by comparing the expected and actual level-order strings.
        Assertions.assertEquals("[ N(b), F(r), S(r), B(b), J(b), P(b), U(b), M(r), O(r), R(r) ]", redBlackTree.root.toLevelOrderString());
    }

    /**
     * Tests insertion of a new node resulting in a Black-Line case.
     */
    @Test
    public void testRBT2() {
        // Instantiate a new RedBlackTree.
        RedBlackTree<Integer> redBlackTree = new RedBlackTree<>();
        // Insert nodes into the tree.
        redBlackTree.insert(10);
        redBlackTree.insert(5);
        redBlackTree.insert(9);
        redBlackTree.insert(11);
        redBlackTree.insert(7);
        redBlackTree.insert(3);
        redBlackTree.insert(20);
        redBlackTree.insert(16);
        // Insertion of 2 then 1 results in a Black-Line case.
        redBlackTree.insert(2);
        redBlackTree.insert(1);
        // Assert that the tree is ordered correctly.
        Assertions.assertEquals("[ 9(b), 5(r), 11(r), 2(b), 7(b), 10(b), 20(b), 1(r), 3(r), 16(r) ]", redBlackTree.root.toLevelOrderString());
    }

    /**
     * Tests insertion of a new node resulting in a Black-Zig case.
     */
    @Test
    public void testRBT3() {
        // Instantiate a new RedBlackTree.
        RedBlackTree<Integer> redBlackTree = new RedBlackTree<>();
        // Insert nodes into the tree.
        redBlackTree.insert(10);
        redBlackTree.insert(5);
        redBlackTree.insert(9);
        redBlackTree.insert(11);
        redBlackTree.insert(7);
        redBlackTree.insert(3);
        redBlackTree.insert(20);
        redBlackTree.insert(16);
        // Insertion of 1 then 2 results in a Black-Zig case.
        redBlackTree.insert(1);
        redBlackTree.insert(2);
        // Assert that the tree is ordered correctly.
        Assertions.assertEquals("[ 9(b), 5(r), 11(r), 2(b), 7(b), 10(b), 20(b), 1(r), 3(r), 16(r) ]", redBlackTree.root.toLevelOrderString());
    }

}
