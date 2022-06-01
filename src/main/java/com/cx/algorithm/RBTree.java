package com.cx.algorithm;

import com.sun.org.apache.regexp.internal.RE;

/**
 * @author chenxiang
 * @create 2022-05-31 20:47
 * 红黑树
 */
public class RBTree<K extends Comparable<K>, V> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    private RBNode root;

    public void put(K key, V value) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (root == null) {
            root = new RBNode(key, value);
            return;
        }
        RBNode t = root;
        int cmp;
        RBNode parent;
        do {
            parent = t;
            cmp = key.compareTo((K) t.key);
            if (cmp > 0) {
                t = t.right;
            } else if (cmp < 0) {
                t = t.left;
            } else {
                t.setValue(value);
                return;
            }
        } while (t != null);
        RBNode newNode = new RBNode(key, value, parent);
        if (cmp > 0) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        fixAfterPut(newNode);
    }

    private void fixAfterPut(RBNode x) {
        while (x != null && x != root && x.parent.color == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                RBNode y = rightOf(parentOf(parentOf(x)));
                if(colorOf(y) == RED){

                }
            } else {

            }
        }
        root.color = BLACK;
    }

    public V remove(K key) {


        return null;
    }

    private RBNode successor(RBNode t) {
        if (t == null) {
            return null;
        } else if (t.right != null) {
            RBNode p = t.right;
            while (p.left != null) {
                p = p.left;
            }
            return p;
        } else {
            RBNode p = t.parent;
            RBNode ch = t;
            while (p != null && ch == p.right) {
                ch = p;
                p = p.parent;
            }
            return p;
        }
    }

    /**
     * @param p 左旋
     *          p 和 pr都存在
     */
    private void leftRotate(RBNode p) {
        if (p != null) {
            RBNode r = p.right;
            p.right = r.left;
            if (r.left != null) {
                r.left.parent = p;
            }
            r.parent = p.parent;
            if (p.parent == null) {
                this.root = r;
            } else if (p.parent.left == p) {
                p.parent.left = r;
            } else {
                p.parent.right = r;
            }
            r.left = p;
            p.parent = r;
        }
    }


    /**
     * 右旋
     *
     * @param p p pl
     *          plr
     */
    private void rightRotate(RBNode p) {
        if (p != null) {
            RBNode l = p.left;
            p.left = l.right;
            if (l.right != null) {
                l.right.parent = p;
            }
            if (p.parent == null) {
                this.root = l;
            } else if (p.parent.left == p) {
                p.parent.left = l;
            } else {
                p.parent.right = l;
            }
            l.right = p;
            p.parent = l;
        }
    }


    private boolean colorOf(RBNode node) {
        return node == null ? BLACK : node.color;
    }

    private RBNode leftOf(RBNode node) {
        return node == null ? null : node.left;
    }

    private RBNode rightOf(RBNode node) {
        return node == null ? null : node.right;
    }

    private RBNode parentOf(RBNode node) {
        return node == null ? null : node.parent;
    }

    static class RBNode<K, V> {
        private K key;
        private V value;
        private RBNode parent;
        private RBNode right;
        private RBNode left;
        private boolean color;

        public RBNode() {

        }

        public RBNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.color = BLACK;
        }

        public RBNode(K key, V value, RBNode parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.color = RED;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public RBNode getParent() {
            return parent;
        }

        public void setParent(RBNode parent) {
            this.parent = parent;
        }

        public RBNode getRight() {
            return right;
        }

        public void setRight(RBNode right) {
            this.right = right;
        }

        public RBNode getLeft() {
            return left;
        }

        public void setLeft(RBNode left) {
            this.left = left;
        }

        public boolean isColor() {
            return color;
        }

        public void setColor(boolean color) {
            this.color = color;
        }
    }

}
