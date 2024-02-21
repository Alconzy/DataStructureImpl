package HashMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Slot<K, V> {
	// 头尾节点占位符
	private final Node<K, V> head, tail;
	private int size;

	// 构造函数初始化头尾节点
	public Slot() {
		// 初始化头尾节点
		this.head = new Node<>(null, null);
		this.tail = new Node<>(null, null);

		// 将头结点和尾结点相连
		head.next = tail;
		this.size = 0;
	}

	// 单链表节点
	private static class Node<K, V> implements Map.Entry<K, V> {
		K key;
		V val;
		Node<K, V> next;

		Node(K key, V val) {
			this.key = key;
			this.val = val;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return val;
		}

		@Override
		public V setValue(V val) {
			V oldVal = this.val;
			this.val = val;
			return oldVal;
		}
	}

	/***** 增/改 *****/

	// 在链表中添加 key -> val 键值对，如果键 key 已存在，则将值修改为 val
	public V put(K key, V val) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}

		Node<K, V> p = getNode(key);
		// key 已存在
		if (p != null) {
			V oldVal = p.val;
			// 修改已有节点
			p.val = val;
			return oldVal;
		}else {
			// 将新节点插入到链表开头
			Node<K, V> x = new Node<>(key, val);
			x.next = head.next;
			head.next = x;
			size++;
		}

		return null;
	}

	/***** 删 *****/

	// 在链表中删除 key 和对应的 val，并返回 val，若 key 不存在，则返回 null
	public V remove(K key) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}

		Node<K, V> prev = head;
		for (Node<K, V> p = head.next; p != tail; p = p.next) {
			if (key.equals(p.key)) {
				prev.next = p.next;
				size--;
				return p.val;
			}
			prev = p;
		}
		return null;
	}

	/***** 查 *****/

	// 返回 key 对应的 val，如果 key 不存在，则返回 null
	public V get(K key) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}

		Node<K, V> p = getNode(key);

		if (p == null) {
			return null;
		}
		return p.val;
	}

	// 判断 key 是否存在 Map 中
	public boolean containsKey(K key) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}

		return getNode(key) != null;
	}

	// 找到 key 对应的单链表节点
	private Node<K, V> getNode(K key) {
		for (Node<K, V> p = head.next; p != tail; p = p.next) {
			if (key.equals(p.key)) {
				return p;
			}
		}

		return null;
	}

	/***** 其他工具方法 *****/

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public List<K> keys() {
		LinkedList<K> keyList = new LinkedList<>();

		Node<K, V> p = head.next;
		while (p != tail) {
			keyList.addLast(p.key);
			p = p.next;
		}

		return keyList;
	}

	public List<Map.Entry<K, V>> entries() {
		LinkedList<Map.Entry<K, V>> entryList = new LinkedList<>();
		Node<K, V> p = head.next;
		while (p != tail) {
			entryList.addLast(p);
			p = p.next;
		}
		return entryList;
	}
}
