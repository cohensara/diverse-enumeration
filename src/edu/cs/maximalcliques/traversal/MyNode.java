package edu.cs.maximalcliques.traversal;

class MyNode<T> {
    public final T val;
    public MyNode next = null;
    public MyNode prev = null;

    MyNode(T val) {
        this.val = val;
    }

    MyNode(T stack, MyNode prev, MyNode next) {
        this.val = stack;
        this.next = next;
        this.prev = prev;
    }

    @Override
    public String toString() {
        return "MyNode{" +
                "val=" + val +
                ", next=" + next +
                ", prev=" + prev +
                '}';
    }
}

