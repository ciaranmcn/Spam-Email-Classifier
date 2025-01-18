import java.util.*;
import java.io.*;

/**
 * ClassificationTree is a binary decision tree used to classify data points as "Spam" or "Ham".
 * It extends the abstract class Classifier, implementing methods to classify data, check if it can
 * classify a given input, and save its structure to a file.
 */
public class ClassificationTree extends Classifier {
    private ClassificationNode root;

    /**
     * Constructs a ClassificationTree using the provided data and corresponding labels.
     *
     * @param data   The list of data points to be classified.
     * @param labels The list of labels corresponding to the data points.
     * @throws IllegalArgumentException If the sizes of data and labels do not match or if they are empty.
     */
    public ClassificationTree(List<Classifiable> data, List<String> labels) {
        if (data == null || labels == null) {
            throw new IllegalArgumentException("Data and labels cannot be null.");
        }
        if (data.size() != labels.size()) {
            throw new IllegalArgumentException("The size of data and labels must be the same.");
        }
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Data and labels must not be empty.");
        }

        for (int i = 0; i < data.size(); i++) {
            root = insert(root, data.get(i), labels.get(i), data.get(i));
        }
    }

    /**
     * Recursively inserts a new node into the classification tree.
     *
     * @param node       The current node in the tree.
     * @param data       The data point to insert.
     * @param label      The label associated with the data point.
     * @param parentData The data point of the parent node.
     * @return The updated ClassificationNode after insertion.
     */
    private ClassificationNode insert(ClassificationNode node, Classifiable data, String label, Classifiable parentData) {
        if (node == null) {
            return new ClassificationNode(label, data);
        }

        if (node.isLeaf()) {
            if (node.label.equals(label)) {
                return node;
            }

            Split split = parentData.partition(data);
            ClassificationNode newNode = new ClassificationNode(split);

            if (split.evaluate(data)) {
                newNode.left = new ClassificationNode(label, data);
                newNode.right = node;
            } else {
                newNode.right = new ClassificationNode(label, data);
                newNode.left = node;
            }

            return newNode;
        }

        if (node.split.evaluate(data)) {
            node.left = insert(node.left, data, label, node.left == null ? data : parentData);
        } else {
            node.right = insert(node.right, data, label, node.right == null ? data : parentData);
        }

        return node;
    }

    /**
     * Constructs a ClassificationTree from a file using a Scanner.
     *
     * @param sc The Scanner to read the tree structure from.
     * @throws IllegalStateException If the tree is empty after being loaded from the file.
     */
    public ClassificationTree(Scanner sc) {
        if (sc == null) {
            throw new IllegalArgumentException("Scanner cannot be null.");
        }
        this.root = treeBuilder(sc);
        if (this.root == null) {
            throw new IllegalStateException("The tree is empty after reading from the file.");
        }
    }

    /**
     * Recursively builds the classification tree from the file.
     *
     * @param sc The Scanner reading the tree structure.
     * @return The constructed ClassificationNode.
     */
    private ClassificationNode treeBuilder(Scanner sc) {
        if (!sc.hasNextLine()) return null;

        String line = sc.nextLine().trim();
        if (line.startsWith("Feature:")) {
            String feature = line.substring(9).trim();
            double threshold = Double.parseDouble(sc.nextLine().substring(11).trim());
            ClassificationNode curr = new ClassificationNode(new Split(feature, threshold));
            curr.left = treeBuilder(sc);
            curr.right = treeBuilder(sc);
            return curr;
        } else {
            return new ClassificationNode(line, null);
        }
    }

    /**
     * Determines whether the tree can classify the given input.
     *
     * @param input The data point to check.
     * @return True if the tree can classify the input; false otherwise.
     * @throws IllegalArgumentException If the input is null.
     */
    @Override
    public boolean canClassify(Classifiable input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null.");
        }
        return canClassify(input, root);
    }

    /**
     * Recursively checks if the tree can classify the given input.
     *
     * @param input The data point to check.
     * @param curr  The current node in the tree.
     * @return True if the tree can classify the input; false otherwise.
     */
    private boolean canClassify(Classifiable input, ClassificationNode curr) {
        if (curr == null) return false;
        if (curr.isLeaf()) return true;
        if (!input.getFeatures().contains(curr.split.getFeature())) return false;
        return canClassify(input, curr.split.evaluate(input) ? curr.left : curr.right);
    }

    /**
     * Classifies the given input data.
     *
     * @param input The data point to classify.
     * @return The predicted label for the input.
     * @throws IllegalArgumentException If the input cannot be classified by this tree.
     */
    @Override
    public String classify(Classifiable input) {
        if (!canClassify(input)) {
            throw new IllegalArgumentException("Input cannot be classified by this tree.");
        }
        return classify(input, root);
    }

    /**
     * Recursively classifies the given input data.
     *
     * @param input The data point to classify.
     * @param curr  The current node in the tree.
     * @return The predicted label for the input.
     */
    private String classify(Classifiable input, ClassificationNode curr) {
        if (curr.isLeaf()) return curr.label;
        if (curr.split.evaluate(input)) return classify(input, curr.left);
        else return classify(input, curr.right);
    }

    /**
     * Saves the current tree structure to a PrintStream.
     *
     * @param ps The PrintStream to save the tree structure to.
     * @throws IllegalArgumentException If the PrintStream is null.
     */
    @Override
    public void save(PrintStream ps) {
        if (ps == null) {
            throw new IllegalArgumentException("PrintStream cannot be null.");
        }
        save(ps, root);
    }

    /**
     * Recursively saves the tree structure to a PrintStream.
     *
     * @param ps   The PrintStream to save the tree structure to.
     * @param curr The current node in the tree.
     */
    private void save(PrintStream ps, ClassificationNode curr) {
        if (curr != null) {
            if (curr.isLeaf()) {
                ps.println(curr.label);
            } else {
                ps.println("Feature: " + curr.split.getFeature() + "~" + curr.split.getThreshold()); // Add tilde here
                save(ps, curr.left);
                save(ps, curr.right);
            }
        }
    }

    /**
     * Inner class to represent nodes in the classification tree.
     * A node can either be a leaf node containing a label or an intermediary node containing a split.
     */
    private static class ClassificationNode {
        final String label;
        final Split split;
        final Classifiable data;
        ClassificationNode left;
        ClassificationNode right;

        /**
         * Constructor for leaf nodes.
         *
         * @param label The label for this leaf node.
         * @param data  The Classifiable data point used to create this leaf node.
         */
        ClassificationNode(String label, Classifiable data) {
            this.label = label;
            this.split = null;
            this.data = data;
            this.left = null;
            this.right = null;
        }

        /**
         * Constructor for intermediary nodes.
         *
         * @param split The split criterion for this node.
         */
        ClassificationNode(Split split) {
            this.label = null;
            this.split = split;
            this.data = null;
            this.left = null;
            this.right = null;
        }

        /**
         * Checks if this node is a leaf.
         *
         * @return True if this node is a leaf, false otherwise.
         */
        boolean isLeaf() {
            return label != null;
        }
    }
}

