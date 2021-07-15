package edu.cs.enumalgorithms.traversal;

class MyNode<T> {
    public final T val;
    public MyNode<T> next = null;
    public MyNode<T> prev = null;

    MyNode(T val) {
        this.val = val;
    }

    MyNode(T stack, MyNode<T> prev, MyNode<T> next) {
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

