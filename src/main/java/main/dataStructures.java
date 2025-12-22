package main;

import java.io.Serializable;
import java.util.Iterator;

public class dataStructures {

    public static class Node<T> implements Serializable {
        private static final long serialVersionUID = 1L;
        T data;
        Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }
    }

    public static class Queue<T> implements Serializable, Iterable<T> {
        private static final long serialVersionUID = 1L;
        private Node<T> front;
        private Node<T> rear;
        private int size;

        public Queue() {
            this.front = null;
            this.rear = null;
            this.size = 0;
        }

        public void offer(T item) {
            Node<T> newNode = new Node<>(item);
            if (rear == null) {
                front = rear = newNode;
            } else {
                rear.setNext(newNode);
                rear = newNode;
            }
            size++;
        }

        public T poll() {
            if (front == null) {
                return null;
            }
            T data = front.getData();
            front = front.getNext();
            if (front == null) {
                rear = null;
            }
            size--;
            return data;
        }

        public T peek() {
            if (front == null) {
                return null;
            }
            return front.getData();
        }

        public boolean isEmpty() {
            return front == null;
        }

        public int size() {
            return size;
        }

        public void clear() {
            front = null;
            rear = null;
            size = 0;
        }

        @Override
        public Iterator<T> iterator() {
            return new QueueIterator();
        }

        private class QueueIterator implements Iterator<T> {
            private Node<T> current = front;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    return null;
                }
                T data = current.getData();
                current = current.getNext();
                return data;
            }
        }

        public ArrayList<T> toArrayList() {
            ArrayList<T> list = new ArrayList<>();
            Node<T> current = front;
            while (current != null) {
                list.add(current.getData());
                current = current.getNext();
            }
            return list;
        }
        
        public ArrayList<T> toReversedArrayList() {
            ArrayList<T> list = new ArrayList<>();
            Node<T> current = front;
            while (current != null) {
                list.add(0, current.getData());
                current = current.getNext();
            }
            return list;
        }
    }

    public static class ArrayList<T> implements Serializable, Iterable<T> {
        private static final long serialVersionUID = 1L;
        private static final int DEFAULT_CAPACITY = 10;
        private Object[] elements;
        private int size;

        public ArrayList() {
            this.elements = new Object[DEFAULT_CAPACITY];
            this.size = 0;
        }

        public ArrayList(int initialCapacity) {
            this.elements = new Object[initialCapacity];
            this.size = 0;
        }

        public void add(T element) {
            if (size >= elements.length) {
                resize();
            }
            elements[size++] = element;
        }

        public void add(int index, T element) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            if (size >= elements.length) {
                resize();
            }
            for (int i = size; i > index; i--) {
                elements[i] = elements[i - 1];
            }
            elements[index] = element;
            size++;
        }

        @SuppressWarnings("unchecked")
        public T get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            return (T) elements[index];
        }

        @SuppressWarnings("unchecked")
        public T set(int index, T element) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            T oldValue = (T) elements[index];
            elements[index] = element;
            return oldValue;
        }

        public boolean remove(T element) {
            for (int i = 0; i < size; i++) {
                if (elements[i].equals(element)) {
                    removeAt(i);
                    return true;
                }
            }
            return false;
        }

        @SuppressWarnings("unchecked")
        public T removeAt(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            T removed = (T) elements[index];
            for (int i = index; i < size - 1; i++) {
                elements[i] = elements[i + 1];
            }
            elements[--size] = null;
            return removed;
        }

        public boolean removeIf(java.util.function.Predicate<T> predicate) {
            boolean removed = false;
            for (int i = size - 1; i >= 0; i--) {
                @SuppressWarnings("unchecked")
                T element = (T) elements[i];
                if (predicate.test(element)) {
                    removeAt(i);
                    removed = true;
                }
            }
            return removed;
        }

        public boolean contains(T element) {
            for (int i = 0; i < size; i++) {
                if (elements[i].equals(element)) {
                    return true;
                }
            }
            return false;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int size() {
            return size;
        }

        public void clear() {
            for (int i = 0; i < size; i++) {
                elements[i] = null;
            }
            size = 0;
        }

        private void resize() {
            int newCapacity = elements.length * 2;
            Object[] newElements = new Object[newCapacity];
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }

        @Override
        public Iterator<T> iterator() {
            return new ArrayListIterator();
        }

        private class ArrayListIterator implements Iterator<T> {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public T next() {
                if (!hasNext()) {
                    return null;
                }
                return (T) elements[currentIndex++];
            }
        }

        public static <T> ArrayList<T> fromQueue(Queue<T> queue) {
            ArrayList<T> list = new ArrayList<>();
            for (T item : queue) {
                list.add(item);
            }
            return list;
        }
    }

    public static class LinkedList<T> implements Serializable, Iterable<T> {
        private static final long serialVersionUID = 1L;
        private Node<T> head;
        private Node<T> tail;
        private int size;

        public LinkedList() {
            this.head = null;
            this.tail = null;
            this.size = 0;
        }

        public void addFirst(T data) {
            Node<T> newNode = new Node<>(data);
            if (head == null) {
                head = tail = newNode;
            } else {
                newNode.setNext(head);
                head = newNode;
            }
            size++;
        }

        public void addLast(T data) {
            Node<T> newNode = new Node<>(data);
            if (tail == null) {
                head = tail = newNode;
            } else {
                tail.setNext(newNode);
                tail = newNode;
            }
            size++;
        }

        public void add(T data) {
            addLast(data);
        }

        public void add(int index, T data) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            if (index == 0) {
                addFirst(data);
                return;
            }
            if (index == size) {
                addLast(data);
                return;
            }
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.getNext();
            }
            Node<T> newNode = new Node<>(data);
            newNode.setNext(current.getNext());
            current.setNext(newNode);
            size++;
        }

        public T get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
            Node<T> current = head;
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
            return current.getData();
        }

        public T removeFirst() {
            if (head == null) {
                return null;
            }
            T data = head.getData();
            head = head.getNext();
            if (head == null) {
                tail = null;
            }
            size--;
            return data;
        }

        public boolean remove(T data) {
            if (head == null) {
                return false;
            }
            if (head.getData().equals(data)) {
                removeFirst();
                return true;
            }
            Node<T> current = head;
            while (current.getNext() != null) {
                if (current.getNext().getData().equals(data)) {
                    current.setNext(current.getNext().getNext());
                    if (current.getNext() == null) {
                        tail = current;
                    }
                    size--;
                    return true;
                }
                current = current.getNext();
            }
            return false;
        }

        public boolean removeIf(java.util.function.Predicate<T> predicate) {
            boolean removed = false;
            while (head != null && predicate.test(head.getData())) {
                removeFirst();
                removed = true;
            }
            if (head == null) {
                tail = null;
                return removed;
            }
            Node<T> current = head;
            while (current.getNext() != null) {
                if (predicate.test(current.getNext().getData())) {
                    current.setNext(current.getNext().getNext());
                    if (current.getNext() == null) {
                        tail = current;
                    }
                    size--;
                    removed = true;
                } else {
                    current = current.getNext();
                }
            }
            return removed;
        }

        public boolean contains(T data) {
            Node<T> current = head;
            while (current != null) {
                if (current.getData().equals(data)) {
                    return true;
                }
                current = current.getNext();
            }
            return false;
        }

        public boolean isEmpty() {
            return head == null;
        }

        public int size() {
            return size;
        }

        public void clear() {
            head = null;
            tail = null;
            size = 0;
        }

        @Override
        public Iterator<T> iterator() {
            return new LinkedListIterator();
        }

        private class LinkedListIterator implements Iterator<T> {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    return null;
                }
                T data = current.getData();
                current = current.getNext();
                return data;
            }
        }

        public ArrayList<T> toArrayList() {
            ArrayList<T> list = new ArrayList<>();
            Node<T> current = head;
            while (current != null) {
                list.add(current.getData());
                current = current.getNext();
            }
            return list;
        }
    }
}

