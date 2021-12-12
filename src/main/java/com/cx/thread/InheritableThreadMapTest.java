package com.cx.thread;

/**
 * @author chenxiang
 * @create 2021-12-07 18:18
 */
public class InheritableThreadMapTest {
    private static final InheritableThreadLocal<Integer> t = new InheritableThreadLocal<>();

    private static  InheritableThreadLocal<Integer> t1 = new InheritableThreadLocal<>();


    public static void main(String[] args) {
        //
//[,"addAtTail","get","addAtHead","addAtIndex","addAtHead"]
//[[4],[4],[4],[5,0],[6]]
        //
        MyLinkedList list = new MyLinkedList();
        list.addAtHead(7);
        list.addAtHead(2);
        list.addAtHead(1);
        list.addAtIndex(3,0);
        list.deleteAtIndex(2);
        list.addAtHead(6);
        list.addAtTail(4);
        System.out.println(list.get(4));
        list.addAtHead(4);
        list.addAtIndex(5,0);
        list.addAtHead(6);
        System.out.println(list);


    }

    public static class MyLinkedList {
        private static final int NOT_EXISTS = -1;

        static class Node{
            int val;
            Node next;

            public Node(int val){
                this.val = val;
            }

            public Node(int val, Node next){
                this.val = val;
                this.next = next;
            }
        }

        private Node head;

        public MyLinkedList() {

        }

        public int get(int index) {
            Node node = getNode(index);
            return node == null ? NOT_EXISTS : node.val;
        }

        public void addAtHead(int val) {
            head = new Node(val, head);
        }

        public void addAtTail(int val) {
            Node node = new Node(val);
            if(head == null){
                head = node;
                return;
            }
            Node h = head;
            while(h.next != null){
                h = h.next;
            }
            h.next = node;
        }

        public void addAtIndex(int index, int val) {
            if(index == 0){
                addAtHead(val);
                return;
            }
            Node pre = getNode(index-1);
            if(pre != null){
                pre.next = new Node(val, pre.next);
            }
        }

        public void deleteAtIndex(int index) {
            if(index == 0){
                if(head != null){
                    Node h = head;
                    head = head.next;
                }
                return;
            }
            Node prev = getNode(index-1);
            if(prev != null && prev.next != null){
                prev.next = prev.next.next;
            }
        }

        private Node getNode(int index){
            if(index < 0){
                return null;
            }
            Node h = head;
            while(h != null && index > 0){
                index--;
                h = h.next;
            }
            return h;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            Node h = this.head;
            while (h != null){
                sb.append(h.val).append(",");
                h = h.next;
            }
            return sb.toString();
        }
    }



}
