package ru.mail.technopolis.hw1;

import java.util.*;

public class LinkedList<E> implements java.util.List<E> {

    /*
    * invariant: size>=0, first==last==null || (first!=null && last!=null)
    * */
    private int size;
    private Element<E> first;
    private Element<E> last;

    /*
    * invariant: next!=prev,first.prev==null,last.next==null
    * */
    private class Element<V> {
        V value;
        Element<V> next;
        Element<V> prev;

        Element(V value, Element<V> prev, Element<V> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    private LinkedList() {
    }

    /*
    * result==(null || element)
    * */
    private Element<E> getElement(E e) {
        for (Element<E> element = first; element != null; element = element.next) {
            if (e == element.value)
                return element;
        }
        return null;
    }

    private void addFirst(E e) {
        Element<E> temp = first;
        Element<E> element = new Element<>(e, null, temp);
        first = element;
        if (temp == null)
            last = element;
        else
            temp.prev = element;
        size++;
    }

    private void addLast(E e) {
        Element<E> temp = last;
        Element<E> element = new Element<>(e, temp, null);
        last = element;
        if (temp == null)
            first = element;
        else
            temp.next = element;
        size++;
    }

    private void addMiddle(int index, E e) {
        Element<E> temp = getElement(get(index));
        Element<E> prev = temp != null ? temp.prev : null;
        Element<E> element = null;
        if (temp != null) {
            element = new Element<>(e, temp.prev, temp);
        }
        if (temp != null) {
            temp.prev = element;
        }
        if (prev == null)
            first = element;
        else
            prev.next = element;
        size++;
    }

    private void deleteFirst() {
        Element<E> temp = first.next;
        first.prev = first.next = null;
        first.value = null;
        first = temp;
        if (temp == null)
            last = null;
        else
            temp.prev = null;
        size--;
    }

    private void deleteLast() {
        Element<E> temp = last.prev;
        last.prev = last.next = null;
        last.value = null;
        last = temp;
        if (temp == null)
            first = null;
        else
            temp.next = null;
        size--;
    }

    private void deleteMiddle(Element<E> nextElement) {
        Element<E> prev = nextElement.prev;
        Element<E> next = nextElement.next;
        if (prev == null)
            first = null;
        else
            prev.next = next;
        if (next == null)
            last = null;
        else
            next.prev = prev;
        nextElement.prev = nextElement.next = null;
        nextElement.value = null;
        size--;
    }

    /**
     * post: result==String
     */
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("LinkedList=[");
        if (first != null) {
            stringBuilder.append(first.value);
            for (Element<E> element = first.next; element != null; element = element.next) {
                stringBuilder.append(", ").append(element.value);
            }
        }
        return stringBuilder.append("]").toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinkedList)) return false;

        LinkedList<?> list = (LinkedList<?>) o;
        Element<E> element = first;
        if (size != list.size) return false;
        for (Iterator iterator = list.iterator(); iterator.hasNext(); element = element.next) {
            if (element.value != iterator.next()) {
                return false;
            }
        }
        return true;
    }

    /**
     * post: result==hashcode
     */
    public int hashCode() {
        int code = 0;
        for (Element<E> element = first; element != null; element = element.next) {
            code += element.hashCode();
        }
        return code;
    }

    /**
     * post: result==size;
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * post: result==size==0
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * post: size++, first!=null && last!=null, result==true
     */
    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    /**
     * pre: 0 <= index <= size
     * post: size++, first!=null && last!=null, result==true
     */
    @Override
    public void add(int index, E element) {
        if (index >= 0 && index <= size) {
            if (index == 0) {
                addFirst(element);
                return;
            }
            if (index == size) {
                addLast(element);
                return;
            }
            addMiddle(index, element);
        }
    }

    /**
     * pre: c!=null
     * post: size=size+c.size(), result==(true||false),
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        return addAll(size, c);
    }

    /**
     * pre: c!=null, 0 <= index <= size
     * post: size=size+c.size(), result==(true||false)
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (!(index >= 0 && index <= size) || c.size() < 1)
            return false;
        Iterator iterator = c.iterator();
        Element<E> prevElement, nextElement;

        while (iterator.hasNext()) {

            Element<E> element = getElement(get(index));

            if (index == size) {
                prevElement = last;
                nextElement = null;
            } else {
                prevElement = element != null ? element.prev : null;
                nextElement = element;
            }

            Element<E> newElement = new Element<>((E) iterator.next(), prevElement, nextElement);

            if (prevElement == null)
                first = newElement;
            else
                prevElement.next = newElement;

            if (nextElement == null)
                last = newElement;
            else
                nextElement.prev = newElement;

            index++;
            size++;

        }

        return true;
    }

    /**
     * pre: 0 <= index < size
     * post: size--, result==element(index).value||null
     */
    @Override
    public E remove(int index) {
        E deleteValue = get(index);
        if (index < size) {
            if (index == 0) {
                deleteFirst();
                return deleteValue;
            }
            deleteMiddle(getElement(deleteValue));
            return deleteValue;

        }
        return null;
    }

    /**
     * pre: size > 0
     * post: size--; result==true||false
     */
    @Override
    public boolean remove(Object o) {
        int i = 0;
        if (o == null)
            for (Element<E> element = first; element != null; element = element.next, i++) {
                if (element.value == null) {
                    remove(i);
                    return true;
                }
            }
        else {
            for (Element<E> element = first; element != null; element = element.next, i++) {
                if (element.value == null)
                    continue;
                if (element.value.equals(o)) {
                    remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * pre: c!=null
     * post: size=size-c.size(), result==true||false
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        if (c.size() > 0) {
            for (Object aC : c) {
                E value = (E) aC;
                if (contains(value))
                    remove(indexOf(value));
            }
            return true;
        } else return false;
    }

    /**
     * pre: c!=null
     * post: size=size-c.size(), result==true||false
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.size() > 0) {
            for (Element<E> element = first; element != null; element = element.next) {
                if (!c.contains(element.value))
                    deleteMiddle(element);
            }
            return true;
        } else return false;
    }

    /**
     * pre: size!=0
     * post: size==0, first==last==null
     */
    @Override
    public void clear() {
        if (size != 0) {
            for (Element<E> element = first; element != null; element = element.next) {
                if (element.prev != null)
                    element.prev.next = null;
                element.prev = null;
                element.value = null;
            }
            first = null;
            last = null;
            size = 0;
        }
    }


    /**
     * pre: 0 <= index < size
     * post: result==element(index).value||null
     */
    @Override
    public E get(int index) {
        if (index >= 0 && index < size) {
            Element<E> element;
            if (index > (size >> 1)) {
                element = last;
                for (int i = size - 1; i > index; i--) {
                    element = element.prev;
                }
            } else {
                element = first;
                for (int i = 0; i < index; i++) {
                    element = element.next;
                }
            }
            return element.value;
        }
        return null;
    }

    /**
     * pre: 0 <= index < size
     * post: result==oldElement.value||null oldElement.value==element
     */
    @Override
    public E set(int index, E element) {
        if (index >= 0 && index < size) {
            Element<E> newElement = getElement(get(index));
            E e = newElement != null ? newElement.value : null;
            if (newElement != null) {
                newElement.value = element;
            }
            return e;
        } else
            return null;
    }

    /**
     * post: result==(i || -1)
     */
    @Override
    public int indexOf(Object o) {
        int i = 0;
        if (o == null)
            for (Element<E> element = first; element != null; element = element.next, i++) {
                if (element.value == null)
                    return i;
            }
        else
            for (Element<E> element = first; element != null; element = element.next, i++) {
                if (o.equals(element.value))
                    return i;
            }
        return -1;
    }

    /**
     * post: result==(i || -1)
     */
    @Override
    public int lastIndexOf(Object o) {
        int i = size - 1;
        if (o == null)
            for (Element<E> element = last; element != null; element = element.prev, i--) {
                if (get(i) == null)
                    return i;
            }
        else
            for (Element<E> element = last; element != null; element = element.prev, i--) {
                if (o.equals(get(i)))
                    return i;
            }
        return -1;
    }

    /**
     * post: result==(true && index>0) || (false && index<0)
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) > -1;
    }

    /**
     * pre: c!=null
     * post: result==(true && index>0) || (false && index<0)
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object aC : c) {
            if (!contains(aC))
                return false;
        }
        return true;
    }


    /**
     * post: result==(new Object[size])
     */
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int i = 0;
        for (Element<E> element = first; element != null; element = element.next, i++) {
            array[i] = element.value;
        }
        return array;
    }

    /**
     * pre: a!=null
     * post: result==(a||(T[]) new Object[size])
     */
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) new Object[size];
        }
        int i = 0;
        for (Element<E> element = first; element != null; element = element.next, i++) {
            a[i] = (T) element.value;
        }
        while (i < a.length) {
            a[i++] = null;
        }
        return a;
    }

    /**
     * pre: 0 <= fromIndex,toIndex < size, fromIndex<=toIndex
     * post: result==(new LinkedList<V>())||null
     */
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        List<E> list = new LinkedList<>();
        if (fromIndex <= toIndex) {


            for (Element<E> element = getElement(get(fromIndex)); fromIndex <= toIndex; fromIndex++, element = element != null ? element.next : null) {
                if (element != null) {
                    list.add(element.value);
                }
            }
        }
        return list;
    }

    /**
     * post: result==(new Iterator<V>())
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            // invariant:
            private Element<E> element = first;

            /**
             * post: result==(true && element!=null) || (false && element==null)
             */
            @Override
            public boolean hasNext() {
                return element != null;
            }

            /**
             * pre: element!=null
             * post: result== element.value, element=element.next
             */
            @Override
            public E next() {
                E e = element.value;
                element = element.next;
                return e;
            }
        };
    }


    @Override
    public ListIterator<E> listIterator() {
        return new ListIterator<E>() {

            private Element<E> element = first;

            private void set(Element<E> element) {
                this.element = element;
            }

            /**
             * post: result==(true && element!=null) || (false && element==null)
             */
            @Override
            public boolean hasNext() {
                return element != null;
            }

            /**
             * pre: element!=null
             * post: result== element.value, element=element.next
             */
            @Override
            public E next() {
                E e = element.value;
                element = element.next;
                return e;
            }

            /**
             * post: result==(true && element!=null) || (false && element==null)
             */
            @Override
            public boolean hasPrevious() {
                return element != null;
            }

            /**
             * pre: element!=null
             * post: result== element.value, element=element.prev
             */
            @Override
            public E previous() {
                E e = element.value;
                element = element.prev;
                return e;
            }

            /**
             * post: result==i||size
             */
            @Override
            public int nextIndex() {
                int i = indexOf(element.next);
                return i < size ? i : size;
            }

            /**
             * post: result==i||-1
             */
            @Override
            public int previousIndex() {
                int i = indexOf(element.prev);
                return i < 0 ? i : -1;
            }

            /**
             * pre: size>0, element!=null
             * post: size--
             */
            @Override
            public void remove() {
                if (size > 0)
                    LinkedList.this.remove(element);
            }

            /**
             * pre: element!=null
             */
            @Override
            public void set(E e) {
                LinkedList.this.set(indexOf(element), e);
            }

            /**
             * post:size++
             */
            @Override
            public void add(E e) {
                LinkedList.this.add(indexOf(element), e);
            }
        };
    }

    /**
     * pre: 0<= index < size
     * post: result==(new ListIterator<V>(index))||null
     */
    @Override
    public ListIterator<E> listIterator(int index) {
        ListIterator<E> listIterator = listIterator();
        if (index <= 0 && index < size) {
            listIterator.set(get(index));
        }
        return listIterator;
    }

    public LinkedList<E> reverse() {
        if (size < 2) {
            return this;
        }
        LinkedList<E> newList = new LinkedList<>();
        /*Element<V> temp, firstElement = first, lastElement = last;
        for (int index = 0; index != size / 2; index++, firstElement = firstElement.next, lastElement = lastElement.next) {
            temp = firstElement.next;
            firstElement.next = firstElement.prev;
            firstElement.prev = firstElement.next;
        }
        return this;*/

        for (Element<E> element = last; element != null; element = element.prev) {
            newList.addLast(element.value);
        }
        return newList;
    }

    private LinkedList<E> anotherReverse() {
        if (size < 2) {
            return this;
        }
        Element<E> temp, firstElement = first, lastElement = last;
        for (int index = 0; index != size >> 1; index++, firstElement = lastElement.next, lastElement = firstElement.prev) {
            temp = new Element<>(null, firstElement.prev, firstElement.next);
            firstElement.next = lastElement.next;
            firstElement.prev = lastElement.prev;
            firstElement.prev.next = firstElement;
            lastElement.next = temp.next;
            lastElement.prev = temp.prev;
            lastElement.next.prev = lastElement;
        }
        return this;
    }


    public static void main(String[] args) {
        LinkedList<Integer> linkedList = new LinkedList<>();

        /*linkedList.add(1);
        System.out.println("add & get: " + (linkedList.get(0) == 1));

        linkedList.set(0, 2);
        System.out.println("set: " + (linkedList.get(0) == 2));

        linkedList.add(0, 3);
        System.out.println("add first: " + (linkedList.get(0) == 3));

        linkedList.remove(0);
        System.out.println("remove first & size: " + (linkedList.size() == 1 && linkedList.get(0) == 2));

        linkedList.add(4);
        linkedList.add(1, 3);
        System.out.println("add middle: " + (linkedList.get(1) == 3));

        linkedList.remove(1);
        System.out.println("remove middle: " + (linkedList.size() == 2 && linkedList.get(1) == 4));

        linkedList.add(linkedList.size(), 5);
        System.out.println("add last: " + (linkedList.get(linkedList.size() - 1) == 5));

        linkedList.remove(0);
        System.out.println("remove last: " + (linkedList.size() == 2 && linkedList.get(linkedList.size() - 1) == 5));

        linkedList.remove(new Integer(4));
        System.out.println("remove(Object): " + (linkedList.size() == 1 && linkedList.get(0) == 5));

        LinkedList<Integer> list = new LinkedList<>();
        list.add(1);
        linkedList.addAll(0, list);
        System.out.println("addAll in front: " + (linkedList.get(0) == 1));

        linkedList.addAll(1, list);
        System.out.println("addAll in middle: " + (linkedList.get(1) == 1));

        list.add(7);
        linkedList.addAll(linkedList.size(), list);
        System.out.println("addAll in back: " + (linkedList.get(linkedList.size() - 1) == 7));

        Object[] array = linkedList.toArray();
        int i = 0;
        for (Iterator iterator = linkedList.iterator(); iterator.hasNext(); i++)
            if (array[i] == iterator.next())
                continue;
            else System.out.println("toArray & iterator: false");
        System.out.println("toArray & iterator: " + (array.length == linkedList.size()));

        Integer[] ar = new Integer[7];
        linkedList.toArray(ar);
        i = 0;
        for (Iterator iterator = linkedList.iterator(); iterator.hasNext(); i++)
            if (ar[i] == iterator.next())
                continue;
            else System.out.println("toArray(array): false");
        System.out.println("toArray(array): true");

        LinkedList<Integer> linkedList1 = (LinkedList<Integer>) linkedList.subList(0, 0);
        System.out.println("subList: " + (linkedList1.get(0) == linkedList.get(0) && linkedList1.size() == 1));

        System.out.println("indexOF: " + (linkedList.indexOf(1) == 0));

        System.out.println("lastIndexOF: " + (linkedList.lastIndexOf(1) == 3));

        System.out.println("contains: " + ((linkedList.contains(91829) == false) && ((linkedList.contains(7)) == true)));

        list.add(666);
        System.out.println("containsAll: " + ((linkedList.containsAll(linkedList) == true) && (linkedList.containsAll(list) == false)));

        linkedList.retainAll(list);
        System.out.println("retainAll: " + (linkedList.size() == 4));

        System.out.println("removeAll & isEmpty: " + (linkedList.removeAll(linkedList) == linkedList.isEmpty()));

        linkedList.add(1);
        linkedList.add(2);
        linkedList.add(3);
        linkedList.clear();
        System.out.println("clear: " + (linkedList.size() == 0));*/

        linkedList.add(1);
        linkedList.add(2);
        linkedList.add(3);
        linkedList.add(4);
        linkedList.add(5);
        linkedList = linkedList.anotherReverse();
        System.out.println(linkedList.toString());


    }

}