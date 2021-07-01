package edu.cs.satassignments.traversal;

class MyList<T> {

    MyNode<T> current;
    int size;

    public int size() {
        return size;
    }

    public MyList(T val) {
        this(new MyNode<>(val, null, null));
    }

    public MyList(MyNode<T> node) {

        node.next = node;
        node.prev = node;
        current = node;
        size = 1;
    }

    //adds new node after current
    public MyNode<T> add(T val) {
        size++;

        MyNode<T> newNode = new MyNode<>(val, current, current.next);
        current.next = newNode;
        newNode.next.prev = newNode;
        return newNode;
    }

    public void remove(MyNode<T> node) {
        if (current == node) {
            current = node.prev;
        }

        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
    }

    /*public void add(MyNode<T> newNode) {
        size++;
        newNode.prev = current;
        newNode.next = current.next;
        current.next = newNode;
        newNode.next.prev = newNode;
    }*/

    public T current() {
        return current.val;
    }

    public T next() {

        current = current.next;
        return current.val;
    }

    public void removeCurrent() {
        size--;
        if (size == 0) {
            current = null;
        } else {
            current.prev.next = current.next;
            current.next.prev = current.prev;
            current = current.prev;
        }
    }

    @Override
    public String toString() {
        String retval = "MyList{";
        MyNode<T> p = current;
        retval += p.val + ", ";
        while (p.next != current) {
            p = p.next;
            retval += p.val + ",";
        }
        return retval + "}";
    }
}
