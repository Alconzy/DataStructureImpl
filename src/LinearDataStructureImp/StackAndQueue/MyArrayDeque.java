package LinearDataStructureImp.StackAndQueue;

import java.util.NoSuchElementException;

/**
 * use array to implement arrayDeque
 *
 * 一些优化思路
 * 1. for example, API addFirst(), addLast(), it needs array copy if size greater
 * than capacity, which is O(n), therefore we can not use
 * MyArrayList class, then what?
 * We can use head and tail pointers, therefore, we use MyLinkedList
 * to implement
 */
public class MyArrayDeque<E> {
    private int size;
    private E[] data;
    private final static int INIT_CAP = 2;

    // data 的索引区间 [first, last) 存储着添加的元素
    //⚠️左闭右开 容易处理 不容易出错 在最初初始化元素为0的时候也更合理 // 都初始化为 0，区间 [0,0) 是空集
    private int first, last;

    public MyArrayDeque(int initCap) {
        size = 0;
        data = (E[]) new Object[initCap];
        // 都初始化为 0，区间 [0,0) 是空集
        first = last = 0;
    }

    public MyArrayDeque() {
        // 不传参数，默认大小为 INIT_CAP
        this(INIT_CAP);
    }

    /**
     * add (first and last)
     * remove(first, and last)
     * get(first, last)
     */

    public void addFirst(E element) {
        if (data.length == size) {
            resize(data.length * 2);
        }
        if (first == 0) {
            first = data.length - 1;
        }else {
            first--;
        }
        //[first, last)
        data[first] = element;
        size++;
    }

    public void addLast(E element) {
        if (data.length == size) {
            resize(size * 2);
        }
        data[last] = element;
        last++;
        //todo 没搞明白为什么
        if (last == data.length) {
            last = 0;
        }
        size++;
    }

    public E getFirst() {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("can not remove from empty");
        }
        return data[first];
    }

    //TODO 常犯错！！！
    public E getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        if (last == 0) return data[data.length - 1];
        return data[last - 1];
    }

    /**
     * remove and return the value
     * @return removed old value
     */
    public E removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("can not remove from empty");
        }
        //尽可能节省空间
        if (size == data.length / 4) {
            resize(data.length / 2);
        }
        E removed = data[first];
        data[first++] = null;
        if (first == data.length) {
            first = 0;
        }
        size--;
        return removed;
    }

    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        //尽可能节省空间
        if (size == data.length / 4) {
            resize(data.length / 2);
        }

        //todo 这里经常犯错
        // 情况一：first----last
        // 情况二：---last  first---

        //todo 左移 last，当 last == 0 的时候是特殊情况
        // 左闭右开 所以先删后 last移动
        // E removed = data[last]; 也得在移动之后！！！！ 一开始没想明白

        if (last == 0) {
            //todo 这里经常犯错  一定要过一遍脑子 想清楚last物理意义 和过程
            last = data.length - 1;
        }else {
            last--;
        }
        //todo 这里经常犯错， 先后顺序 先上面update了last 再。。。
        E removed = data[last];
        data[last] = null;
        size--;
        return removed;
    }


    /***** 工具函数 *****/

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void resize(int newCap) {
        E[] temp = (E[]) new Object[newCap];
        for(int i = 0; i < size; i++) {
            temp[i] = data[(first + i) % data.length];
        }
        first = 0;
        last = size;
        data = temp;
    }

    public static void main(String[] args) {
        MyArrayDeque<String> deque = new MyArrayDeque<>();
        deque.addFirst("world");
        System.out.println(deque.getFirst());
        System.out.println(deque.getLast());
        deque.addFirst("Hello");
        System.out.println(deque.getFirst());
        System.out.println(deque.getLast());
        deque.addLast("Alan");
        System.out.println(deque.getFirst());
        System.out.println(deque.getLast());
        deque.addLast("Cozy");
        System.out.println(deque.getFirst());
        System.out.println(deque.getLast());
        deque.removeLast();
        System.out.println(deque.getFirst());
        System.out.println(deque.getLast());
        deque.removeLast();
        System.out.println(deque.getFirst());
        System.out.println(deque.getLast());

        System.out.println(deque.isEmpty());
    }
}