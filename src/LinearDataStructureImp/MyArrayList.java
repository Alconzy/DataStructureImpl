package LinearDataStructureImp;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class MyArrayList<E> implements Iterable<E>{
    private E[] data;
    private int size;

    private int capacity;
    private static final int DEFAULT_CAPACITY = 10;

    public MyArrayList() {
        this(DEFAULT_CAPACITY);
    }
    public MyArrayList(int capacity) {
        this.capacity = capacity;
        //TODO 曾经犯错 new E[]; --- compile error
        data = (E[]) new Object[capacity];
        size = 0;
    }
    /**
     * Operations
     *  1. add
     *  2. delete
     *  3. search (find)
     *  4. update
     *  5. size() resize() checkElementIndex() checkPositionIndex() iterator() display()
     */
    public void addLast(E element) {
        if (null == element) {
            throw new NullPointerException("Element can not be null!");
        }
        //check size and capacity
        if (size == capacity) {
            resize();
        }
        add(size, element);
    }

    @Override
    public Iterator<E> iterator() {
        Iterator<E> iter = new Iterator<>() {
            private int curIdx = 0;
            @Override
            public boolean hasNext() {
                return curIdx < size;
            }
            @Override
            public E next() {
                return data[curIdx++];
            }
        };

        return iter;
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<E> spliterator() {
        return Iterable.super.spliterator();
    }

    public void resize() {
        /**
         * labuladong implementation
         * if (size > newCap) {
         *             return;
         *         }  not sure why????
         */
        if (size < capacity) return;
        //TODO 曾经犯错 data = Arrays.copyOf(data, 2 * capacity);
        // copyOf is shallow copy, only primitive value element would work, eg, int[], if element itself is
        // an object, new array will have all elements which are only ref copy of the previous one

        // TODO maybe can use System.arraycopy(data, 0, temp, 0, size);
        E[] newData = (E[]) new Object[capacity * 2];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
        //TODO dont forget to update the new capacity
        this.capacity = newData.length;
    }

    public E get(int index) {
        //检查索引越界
        checkElementIndex(index);
        return data[index];
    }

    public E set(int index, E element) {
        //TODO 理解了set 做的是更新，必须是现有element的更新，跟add 方法不能重合
        checkElementIndex(index);
        E oldElement = data[index];
        data[index] = element;
        return oldElement;
    }
    public void add(int index, E element) {
        checkPositionIndex(index);

        if (size == capacity) {
            resize();
        }
        //shift
        System.arraycopy(data, index, data, index + 1, size - index);
        //add (insert)
        data[index] = element;
        size++;
    }

    public void addFirst(E element) {
        add(0, element);
    }

    public E removeFirst() {
        return remove(0);
    }

    /**
     * Remove the element at index
     * @param index index of the element to be removed
     * @return the removed element, return null if invalid index
     */
    public E remove(int index) {
        //check if index valid or not
        checkElementIndex(index);

        //some optimization idea
        // 可以缩容，节约空间
//        if (size == capacity / 4) {
//            resize(capacity / 2);
//        }

        E removedElement = data[index];
        //remove the element and shift
        //TODO another way to do this, use API :
        // 搬移数据 data[index+1..] -> data[index..]
//        System.arraycopy(data, index + 1,
//                data, index,
//                size - index - 1);
        for(int i = index; i < size - 1; i++) {
            data[i]= data[i + 1];
        }

        //TODO 曾经犯错forgot need to manually removed the 'previous' tail element
        data[size - 1] = null;

        //TODO dont forget to update size
        size--;

        //TODO 曾经犯错 return data[index];
        // after shifting, cur index element is no longer that one!!!
        return removedElement;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void display() {
        System.out.println("array has capacity: " + capacity + "and size: " + size);
        System.out.println("All elements: " + Arrays.toString(data));
    }

    public void checkPositionIndex(int index) {
        //== size is valid position
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("Index is not a valid position!");
        }
    }
    public void checkElementIndex(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("THE INDEX IS NOT VALID");
    }

    /**
     * Test Method
     */
    public static void main(String[] args) {
        MyArrayList<Integer> arr = new MyArrayList();
        for(int i = 0; i <= 5; i++) {
            arr.addLast(i);
        }
        arr.remove(3);
        arr.add(1, 9);
        arr.addFirst(100);

        for (int i = 0; i < arr.size(); i++) {
            System.out.println(arr.get(i));
        }

        Iterator<Integer> iter = arr.iterator();
        while(iter.hasNext()) {
            System.out.println(iter.next());
        }
        iter.next();
        iter.next();
        iter.next();
        //iter.next(); expect exception

        arr.display();
    }

}
