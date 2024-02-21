package HashMap;

public class LinearHashMapDemo<K, V> {
	int size = 0;
	private static final int INIT_CAP = 4;
	SlotDemo<K, V>[] table;

	public LinearHashMapDemo() {
		this(INIT_CAP);
	}

	public LinearHashMapDemo(int capacity) {
		size = 0;
		table = (SlotDemo<K, V>[]) new SlotDemo[capacity];
		for (int i = 0; i < table.length; i++) {

		}
	}

	/**
	 * put
	 * remove
	 * get
	 * containsKey
	 * isEmpty
	 * size()
	 * isEmpty()
	 * private hash()
	 * private resize()
	 */

	public void put(K key, V value) {
		if (null == key) throw new NullPointerException("Key can not be null");

	}
}
