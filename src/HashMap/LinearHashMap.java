package HashMap;

import java.util.Map;

// 拉链法解决哈希冲突
public class LinearHashMap<K, V> {
	// 哈希表中存入的键值对个数
	private int size;
	// 底层存储链表的数组
	private Slot<K, V>[] table;
	// 底层数组的初始容量
	private static final int INIT_CAP = 4;

	public LinearHashMap() {
		this(INIT_CAP);
	}

	public LinearHashMap(int initCapacity) {
		size = 0;
		// 初始化哈希表
		table = (Slot<K, V>[]) new Slot[initCapacity];
		for (int i = 0; i < table.length; i++) {
			table[i] = new Slot<>();
		}
	}

	/***** 增/改 *****/

	// 添加 key -> val 键值对
	// 如果键 key 已存在，则将值修改为 val
	public V put(K key, V val) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}
		if (size >= table.length * 0.75) {
			resize(table.length * 2);
		}
		Slot<K, V> slot = table[hash(key)];
		// 如果 key 之前不存在，则插入，size 增加
		if (!slot.containsKey(key)) {
			size++;
		}
		return slot.put(key, val);
	}

	/***** 删 *****/

	// 删除 key 和对应的 val，并返回 val
	// 若 key 不存在，则返回 null
	public V remove(K key) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}
		Slot<K, V> slot = table[hash(key)];
		// 如果 key 之前存在，则删除，size 减少
		if (slot.containsKey(key)) {
			size--;
			return slot.remove(key);
		}
		return null;
	}

	/***** 查 *****/

	// 返回 key 对应的 val
	// 如果 key 不存在，则返回 null
	public V get(K key) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}
		Slot<K, V> slot = table[hash(key)];
		return slot.get(key);
	}

	// 判断 key 是否存在 Map 中
	public boolean containsKey(K key) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}
		Slot<K, V> slot = table[hash(key)];
		return slot.containsKey(key);
	}

	/***** 其他工具函数 *****/

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	// 哈希函数，将键映射到 table 的索引
	private int hash(K key) {
		//return (key.hashCode() & 0x7fffffff) % table.length;
		//TODO use below fragile hash function which could highly possible causing collision
		// key.hashCode() % 2;
		// System.out.println("got hash code: " + key.hashCode() % 2);
		return (key.hashCode() & 0x7fffffff) % table.length;
	}

	private void resize(int newCap) {
		// 构造一个更大容量的 HashMap
		LinearHashMap<K, V> newMap = new LinearHashMap<>(newCap);
		// 穷举当前 HashMap 中的所有键值对
		for (Slot<K, V> slot : table) {
			for (Map.Entry<K, V> entry : slot.entries()) {
				K key = entry.getKey();
				V val = entry.getValue();
				// 将键值对转移到新的 HashMap 中
				newMap.put(key, val);
			}
		}
		// 将当前 HashMap 的底层 table 换掉
		this.table = newMap.table;
	}
}