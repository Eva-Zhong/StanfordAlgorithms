/**
 * Created by zhongyifan on 28/10/2017.
 *
 *
 * In this programming problem and the next you'll code up the greedy algorithm from the lectures on Huffman coding.

 This file describes an instance of the problem. It has the following format:

 [number_of_symbols]

 [weight of symbol #1]

 [weight of symbol #2]

 ...

 For example, the third line of the file is "6852892," indicating that the weight of the second symbol of the alphabet is
 6852892. (We're using weights instead of frequencies, like in the "A More Complex Example" video.)

 Your task in this problem is to run the Huffman coding algorithm from lecture on this data set. What is the maximum length
 of a codeword in the resulting Huffman code?

 */

/*
The code is working except for 'Get depth'.
It shouldn't be a problem with the recursion, because that's what everyone's been using.
Double check my use of priority queue.

*/

import java.util.*;
import java.io.*;


public class Huffman {

    public static void main(String[] args) {
        File symbols = new File("Course3/Huffman/midtest.txt");

        try {
            Scanner sc = new Scanner(symbols);
            int size = sc.nextInt();

            BinaryNode[] nums = new BinaryNode[size];

            int count = 0;
            while (sc.hasNext()) {
                count++;
                int curWeight = sc.nextInt();
//                System.out.println(count + ": " + curWeight);
                BinaryNode cur = new BinaryNode(count, curWeight);
                nums[count-1] = cur;
            }

            Tree huffmanTree = generateTree(nums);

            // problem: didn't return the correct root;
            System.out.println("Left: " + huffmanTree.root.getLeft().weight);
            huffmanTree.printPreorder(huffmanTree.root);
            int depth = huffmanTree.getDepth(huffmanTree.root) + 1;
            System.out.println("Depth: " + depth);

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    public static Tree generateTree(BinaryNode[] nums) {
        Comparator<BinaryNode> comparator = new NodeComparator();
        PriorityQueue<BinaryNode> queue = new PriorityQueue<BinaryNode>(comparator);
        // Write comparator

        for (int i = 0; i < nums.length; i++) {
            BinaryNode item = nums[i];
//            System.out.println(item.label + ": " + item.weight);
            queue.add(item);
        }


        while (queue.size() > 1) {
            BinaryNode left = queue.poll();
            BinaryNode right = queue.poll();
            // The queue is working alright, each time returning the smallest one

            System.out.println("Current Left: " + left.weight);
            System.out.println("Current right " + right.weight);

            int totalWeight = left.weight + right.weight;

            BinaryNode curRoot = new BinaryNode(0, totalWeight);
            curRoot.setLeft(left);
            curRoot.setRight(right);

            queue.add(curRoot);
            System.out.println(curRoot.weight);
        }

        BinaryNode root = queue.poll();
//        BinaryNode right = queue.poll();
//        BinaryNode root = new BinaryNode(0, left.weight+right.weight);
//        root.setLeft(left);
//        root.setRight(right);
////        System.out.println("Root left weight: " + root.left.weight);
////        System.out.println("Root right weight: " + root.right.weight);

        System.out.println("Root weight: " + root.weight);

        Tree t = new Tree(root);
        return t;
    }

    static class Tree {
        BinaryNode root;
        BinaryNode left;
        BinaryNode right;

        public Tree(BinaryNode root) {
            this.root = root;
            this.left = root.left;
            this.right = root.right;
        }

        public static int getDepth(BinaryNode root) {
            if (root == null) {
                return 0;

            }
            System.out.println("In traverse: " + root.weight);
            return 1+Math.max(getDepth(root.left), getDepth(root.right));
//            else {
//
//                int leftDepth = getDepth(root.left);
//                int rightDepth = getDepth(root.right);
//                if (leftDepth >= rightDepth) {
////                    System.out.println(root.left.label);
//                    int maxDepth = leftDepth + 1;
//                    return maxDepth;
//                } else {
////                    System.out.println(root.right.label);
//                    int maxDepth = rightDepth + 1;
//                    return maxDepth;
//                }
//            }
        }

        void printPreorder(BinaryNode node)
        {
            if (node == null)
                return;

            /* then print the data of node */
            System.out.print(node.weight + " ");

            /* first recur on left child */
            printPreorder(node.left);

            /* now recur on right child */
            printPreorder(node.right);
        }
    }

     static class BinaryNode {
         int label;
         int weight;
         BinaryNode left;
         BinaryNode right;

         public BinaryNode() {
             this.left = null;
             this.right = null;
         }

         public BinaryNode(int label, int weight) {
             this.label = label;
             this.weight = weight;
             this.left = null;
             this.right = null;
         }

         public void setLeft(BinaryNode leftChild) {
             this.left = leftChild;
         }

         public BinaryNode getLeft() {
             return this.left;
         }

         public void setRight(BinaryNode rightChild) {
             this.right = rightChild;
         }

         public BinaryNode getRight() {
             return this.right;
         }

         public int getLabel() {
             return label;
         }

         public int getWeight() {
             return weight;
         }

//         public static int getDepth(BinaryNode root) {
//             if (root == null) {
//                 return 0;
//
//             } else {
//                 int leftDepth = getDepth(root.left);
//                 int rightDepth = getDepth(root.right);
//                 if (leftDepth > rightDepth) {
//                     System.out.println(root.left.label);
//                     int maxDepth = leftDepth + 1;
//                     return maxDepth;
//                 } else {
//                     System.out.println(root.right.label);
//                     int maxDepth = rightDepth + 1;
//                     return maxDepth;
//                 }
//             }
//         }
     }

    static class NodeComparator implements Comparator<BinaryNode> {
             @Override
             public int compare(BinaryNode n1, BinaryNode n2) {
                 if (n1.weight > n2.weight) {
                     return 1;

                 } else if (n1.weight < n2.weight) {
                     return -1;
                 } else {
                     return 0;
                 }
             }
    }

}
