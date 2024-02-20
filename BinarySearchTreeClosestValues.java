package Question4;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {}

    TreeNode(int val) {
        this.val = val;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

public class BinarySearchTreeClosestValues {

    // Method to find the k closest values to the target in a binary search tree
    public List<Integer> findClosestValues(TreeNode root, double target, int k) {
        List<Integer> result = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();
        inorderTraversal(root, stack);

        // Pop the k closest values from the stack
        while (k-- > 0 && !stack.isEmpty()) {
            result.add(stack.pop());
        }

        return result;
    }

    // Helper method for inorder traversal of the binary search tree
    private void inorderTraversal(TreeNode root, Stack<Integer> stack) {
        if (root == null) return;

        inorderTraversal(root.left, stack);
        stack.push(root.val);
        inorderTraversal(root.right, stack);
    }

    public static void main(String[] args) {
        // Example tree creation
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(5);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);

        double target = 3.8;
        int k = 2;

        // Instantiate the BinarySearchTreeClosestValues class
        BinarySearchTreeClosestValues searcher = new BinarySearchTreeClosestValues();

        // Find the closest values to the target in the binary search tree
        List<Integer> result = searcher.findClosestValues(root, target, k);

        // Print the closest values
        System.out.println("Closest values to " + target + " are: " + result);
    }
}
