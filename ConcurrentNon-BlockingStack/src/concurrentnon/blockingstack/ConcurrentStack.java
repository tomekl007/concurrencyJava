/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package concurrentnon.blockingstack;

import java.util.concurrent.atomic.AtomicReference;


/**
 * ConcurrentStack
 * <p/>
 * Nonblocking stack using Treiber's algorithm
 *
 *Stacks are the simplest linked data structure: each element refers to only one other element
 * and each element is referred to by only one object reference. ConcurrentStack in Listing 15.
 * 6 shows how to construct a stack using atomic references. The stack is a linked list of
 * Node elements, rooted at top, each of which contains a value and a link to the next element. 
 * The push method prepares a new link node whose next field refers to the current top of 
 * the stack, and then uses CAS to try to install it on the top of the stack. If the same
 * node is still on the top of the stack as when we started, the CAS succeeds; if the top
 * node has changed (because another thread has added or removed elements since we started), 
 * the CAS fails and push updates the new node based on the current stack state and tries again. 
 * In either case, the stack is still in a consistent state after the CAS
 */
//@ThreadSafe
        public class ConcurrentStack <E> {
    AtomicReference<Node<E>> top = new AtomicReference<Node<E>>();

    public void push(E item) {
        Node<E> newHead = new Node<E>(item);
        Node<E> oldHead;
        do {
            oldHead = top.get();
            newHead.next = oldHead;
        } while (!top.compareAndSet(oldHead, newHead));
    }
    /*when we construct the Node representing the new element, we are hoping that
     * the value of the next reference will still be correct by the time it is installed
     * on the stack, but are prepared to retry in the event of contention*/

    public E pop() {
        Node<E> oldHead;
        Node<E> newHead;
        do {
            oldHead = top.get();
            if (oldHead == null)
                return null;
            newHead = oldHead.next;
        } while (!top.compareAndSet(oldHead, newHead));
        return oldHead.item;
    }/*When a thread examines the stack, it does so by calling get on the same AtomicReference,
     * which has the memory effects of a volatile read. So any changes made by one thread are
     * safely published to any other thread that examines the state of the list. And the list 
     * is modified with a compareAndSet that atomically either updates the top reference or fails
     * if it detects interference from another thread.*/

    private static class Node <E> {
        public final E item;
        public Node<E> next;

        public Node(E item) {
            this.item = item;
        }
    }
}