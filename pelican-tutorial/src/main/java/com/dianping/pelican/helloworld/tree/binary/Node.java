package com.dianping.pelican.helloworld.tree.binary;

/**
 * Created by liming_liu on 15/7/27.
 */
public class Node {

    public int value;

    public Node(int value) {
        this.value = value;
    }

    public Node leftChild;
    public Node rightChild;

    public void add(int value) {
        if (value <= this.value) {
            if (leftChild == null) {
                leftChild = new Node(value);
            } else {
                leftChild.add(value);
            }
        } else {
            if (rightChild == null) {
                rightChild = new Node(value);
            } else {
                rightChild.add(value);
            }
        }
    }

    public void dlr() {
        System.out.print(value + "\t");
        if (leftChild != null) {
            leftChild.dlr();
        }
        if (rightChild != null) {
            rightChild.dlr();
        }
    }

    public void ldr() {
        if (leftChild != null) {
            leftChild.ldr();
        }
        System.out.print(value + "\t");
        if (rightChild != null) {
            rightChild.ldr();
        }
    }

    public void lrd() {
        if (leftChild != null) {
            leftChild.lrd();
        }
        if (rightChild != null) {
            rightChild.lrd();
        }
        System.out.print(value + "\t");
    }

    public static void main(String[] args) {
        Integer[] nodeList = {5,2,3,6,8,10,7,1};
        Node root = new Node(nodeList[0]);
        for (int i=1; i<nodeList.length; ++i) {
            root.add(nodeList[i]);
        }

        root.dlr();
        System.out.println();
        root.ldr();
        System.out.println();
        root.lrd();
    }
}
