package HashMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SlotDemo<K, V> {
	int size;
	Node<K, V> head, tail;

	public SlotDemo(int size) {
		this.head = new Node<>(null, null);
		this.tail = new Node<>(null, null);
		//Singly LinkedList
		head.next = tail;
		this.size = size;
	}

	/**
	 * Operations:
	 *  Get
	 *  Remove
	 *  put
	 *  containsKey
	 *  getNode
	 *  size
	 *  isEmpty()
	 *  keys()
	 *  entries()
	 */

	public V get(K key) {
		if (null == key) throw new NullPointerException("KEY CAN NOT BE NULL");
		Node<K, V> cur = head.next;
		while(cur != null) {
			if (cur.key.equals(key)) {
				return cur.getValue();
			}
			cur = cur.next;
		}
		return null;
	}

	//If exists, remove and return old value, otherwise, return null
	public V remove(K key) {
		if (null == key) throw new NullPointerException("KEY CAN NOT BE NULL");
		Node<K, V> prev = head;
		//since we have singly LinkedList only, we need to keep previous one
		while(prev != null && prev.next != null) {
			Node<K, V> next = prev.next;
			if (next.getKey().equals(key)) {
				prev.next = next.next;
				return next.value;
			}else{
				prev = prev.next;
			}
		}
		return null;
	}

	//If not existed, return new value, otherwise return old value
	public V put(K key, V value) {
		if (null == key || null == value) throw new NullPointerException("Key OR Value can not be null!");
		//check if key value already exist
		Node<K, V> existNode = getNode(key);
		//always add to head, cause if we add to tail, we need previous node information of tail, time complexity O(n)
		if (null != existNode) {
			return existNode.setValue(value);
		}else {
			Node<K, V> newNode = new Node<>(key, value);
			newNode.next = head.next;
			head.next = newNode;
			size++;
		}
		//TODO 曾经犯错，如果return value，api 层面 怎么知道return的value是新还是旧的？？？
		// 所以如果key不存在，要创建新node，直接返回null
		return null;
	}

	//注意这里返回list, 因为我们这个slot本来就是用linear (singly linkedList 解决 key collision 问题）
	public List<K> keys() {
		if (size == 0) return new LinkedList<K>();
		List<K> keys = new LinkedList<>();
		for(Node<K, V> cur = head.next; cur != tail; cur = cur.next) {
			keys.add(cur.getKey());
		}
		return keys;
	}

	public List<Node<K, V>> entries() {
		List<Node<K, V>> entryList = new LinkedList<>();
		Node<K, V> curNode = head.next;
		while(curNode != tail) {
			entryList.add(curNode);
			curNode = curNode.next;
		}
		return entryList;
	}

	public boolean containsKey(K key) {
		if (null == key) {
			throw new NullPointerException("Key can not be null!");
		}
		return null != getNode(key);
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public Node<K,V> getNode(K key) {
		for (Node<K, V> curNode = head; curNode != tail; curNode = curNode.next) {
			if (curNode.getKey().equals(key)) {
				return curNode;
			}
		}
		//can not find
		return null;
	}

	private static class Node<K, V> implements Map.Entry<K, V> {
		private K key;
		private V value;
		private Node<K, V> next;

		public Node(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			if (null == value) {
				throw new NullPointerException("Value given can not be null!");
			}
			V oldVal = this.value;
			this.value = value;
			return oldVal;
		}
	}
}
