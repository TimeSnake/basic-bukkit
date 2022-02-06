package de.timesnake.basic.bukkit.core.user.scoreboard;

import java.util.Iterator;

public class Tab<E extends Tab.TabEntry<E>> implements Iterable<E> {

    private E head;
    private E tail;

    /**
     * Adds an entry to the Tab
     *
     * @param entry The entry to add
     * @return true if the entry was merged into another entry
     */
    public boolean addEntry(E entry) {

        if (this.head == null) {
            this.head = entry;
            this.tail = entry;
            entry.setPrevious(null);
            entry.setNext(null);
            return false;
        }

        TabIterator<E> it = ((TabIterator<E>) this.iterator());
        E current;
        do {
            current = it.next();
            int compare = current.getRank().compareToIgnoreCase(entry.getRank());
            if (compare > 0) {
                // first entry in list with lower rank -> insert before
                this.insertBefore(entry, current);
                return false;
            }
            // rank is equals -> merge entries
            else if (compare == 0) {
                current.merge(entry);
                return true;
            }
        } while (it.hasNext());

        this.insertLast(entry);

        return false;
    }

    public void insertBefore(E entry, E current) {
        if (current == this.head) {
            entry.setPrevious(null);
            entry.setNext(current);
            current.setPrevious(entry);
            this.head = entry;
        } else {
            current.getPrevious().setNext(entry);
            entry.setPrevious(current.getPrevious());
            current.setPrevious(entry);
            entry.setNext(current);

        }
    }

    public void insertLast(E entry) {
        this.tail.setNext(entry);
        entry.setPrevious(this.tail);
        entry.setNext(null);
        this.tail = entry;
    }

    public boolean removeEntry(E entry) {
        // current is head
        if (entry == this.head) {
            this.head = entry.getNext();
            if (this.head != null) {
                this.head.setPrevious(null);
            } else {
                // current is head and tail
                this.tail = null;
            }
        }
        // current is tail
        else if (entry == this.tail) {
            this.tail = entry.getPrevious();
            this.tail.setNext(null);
        }
        // current is middle
        else {
            // not a valid entry
            if (entry.getPrevious() == null || entry.getNext() == null) {
                return false;
            }
            entry.getPrevious().setNext(entry.getNext());
            entry.getNext().setPrevious(entry.getPrevious());
        }
        return true;
    }

    public E getHead() {
        return head;
    }

    public E getTail() {
        return tail;
    }

    public int size() {
        int size = 0;
        for (E ignored : this) {
            size++;
        }
        return size;
    }

    public abstract static class TabEntry<E> {

        protected E previous;
        protected E next;

        protected final String rank;

        public TabEntry(String rank) {
            this.rank = rank;
        }

        public String getRank() {
            return rank;
        }

        public E getNext() {
            return next;
        }

        public E getPrevious() {
            return previous;
        }

        public void setNext(E next) {
            this.next = next;
        }

        public void setPrevious(E previous) {
            this.previous = previous;
        }

        public abstract void merge(E entry);

    }

    @Override
    public Iterator<E> iterator() {
        return new TabIterator<>(this);
    }

    public static class TabIterator<E extends TabEntry<E>> implements Iterator<E> {

        private E current;

        public TabIterator(Tab<E> tab) {
            this.current = tab.getHead();
        }

        @Override
        public boolean hasNext() {
            return this.current != null;
        }

        @Override
        public E next() {
            if (this.hasNext()) {
                E tmp = this.current;
                this.current = this.current.getNext();
                return tmp;
            }
            return null;
        }

    }

}
