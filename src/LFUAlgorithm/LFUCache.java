package src.LFUAlgorithm;

/**
 * Reference 参考
 * https://appktavsiei5995.pc.xiaoe-tech.com/p/t_pc/course_pc_detail/image_text/i_627ceadae4b01c509aaf8afa?product_id=p_627cea48e4b01c509aaf8a98&content_app_id=&type=6
 */

import java.util.HashMap;
import java.util.LinkedHashSet;

/**
 *              思路分析
 *
 *
 * 一定先从最简单的开始，根据 LFU 算法的逻辑，我们先列举出算法执行过程中的几个显而易见的事实：
 *
 * 1、调用 get(key) 方法时，要返回该 key 对应的 val。
 *
 * 2、只要用 get 或者 put 方法访问一次某个 key，该 key 的 freq 就要加一。
 *
 * 3、如果在容量满了的时候进行插入，则需要将 freq 最小的 key 删除，如果最小的 freq 对应多个 key，则删除其中最旧的那一个。
 *
 * 好的，我们希望能够在 O(1) 的时间内解决这些需求，可以使用基本数据结构来逐个击破：
 *
 * 1、使用一个 HashMap 存储 key 到 val 的映射，就可以快速计算 get(key)。
 *
 * HashMap<Integer, Integer> keyToVal;
 * 2、使用一个 HashMap 存储 key 到 freq 的映射，就可以快速操作 key 对应的 freq。
 *
 * HashMap<Integer, Integer> keyToFreq;
 * 3、这个需求应该是 LFU 算法的核心，所以我们分开说：
 *
 * 3.1、首先，肯定是需要 freq 到 key 的映射，用来找到 freq 最小的 key。
 *
 * 3.2、将 freq 最小的 key 删除，那你就得快速得到当前所有 key 最小的 freq 是多少。想要时间复杂度 O(1) 的话，肯定不能遍历一遍去找，那就用一个变量 minFreq 来记录当前最小的 freq 吧。
 *
 * 3.3、可能有多个 key 拥有相同的 freq，所以 freq 对 key 是一对多的关系，即一个 freq 对应一个 key 的列表。
 *
 * 3.4、希望 freq 对应的 key 的列表是存在时序的，便于快速查找并删除最旧的 key。
 *
 * 3.5、希望能够快速删除 key 列表中的任何一个 key，因为如果频次为 freq 的某个 key 被访问，那么它的频次就会变成 freq+1，就应该从 freq 对应的 key 列表中删除，加到 freq+1 对应的 key 的列表中。
 *
 * HashMap<Integer, LinkedHashSet<Integer>> freqToKeys;
 * int minFreq = 0;
 * 介绍一下这个 LinkedHashSet，它满足我们 3.3，3.4，3.5 这几个要求。你会发现普通的链表 LinkedList 能够满足 3.3，3.4 这两个要求，但是由于普通链表不能快速访问链表中的某一个节点，所以无法满足 3.5 的要求。
 *
 * LinkedHashSet 顾名思义，是链表和哈希集合的结合体。链表不能快速访问链表节点，但是插入元素具有时序；哈希集合中的元素无序，但是可以对元素进行快速的访问和删除。
 *
 * 那么，它俩结合起来就兼具了哈希集合和链表的特性，既可以在 O(1) 时间内访问或删除其中的元素，又可以保持插入的时序，高效实现 3.5 这个需求。
 *
 * 综上，我们可以写出 LFU 算法的基本数据结构：
 */
public class LFUCache {
	private int capacity;
	private HashMap<Integer, Integer> keyToVal;
	//map for key-freq
	private HashMap<Integer, Integer> keyToFreq;
	/**
	 * 3.3、可能有多个 key 拥有相同的 freq，所以 freq 对 key 是一对多的关系，即一个 freq 对应一个 key 的列表。
	 *
	 * 3.4、希望 freq 对应的 key 的列表是存在时序的，便于快速查找并删除最旧的 key。
	 *
	 * 3.5、希望能够快速删除 key 列表中的任何一个 key，因为如果频次为 freq 的某个 key 被访问，那么它的频次就会变成 freq+1，
	 *      就应该从 freq 对应的 key 列表中删除，加到 freq+1 对应的 key 的列表中。
	 */
	private HashMap<Integer, LinkedHashSet<Integer>> freqToKeys;
	/**
	 * 3.2、将 freq 最小的 key 删除，那你就得快速得到当前所有 key 最小的 freq 是多少。想要时间复杂度 O(1) 的话，肯定不能遍历一遍去找，
	 * 那就用一个变量 minFreq 来记录当前最小的 freq 吧。
	 */
	int minFreq;
	// 记录 LFU 缓存的最大容量
	int cap;

	public LFUCache(int capacity) {
		this.capacity = capacity;
		keyToVal = new HashMap<>();
		keyToFreq = new HashMap<>();
		freqToKeys = new HashMap<>();
		minFreq = 0;
		cap = capacity;
	}
	/**
	 * LFUCache Operations
	 */

	//if not found, return -1;
	public int get(int key) {
		if (!keyToFreq.containsKey(key)) return -1;
		// 增加 key 对应的 freq
		increaseFreq(key);
		return keyToVal.get(key);
	}

	/**
	 * If capacity is reached, remove the least used element (tell by used frequency), if multiple
	 * elements found with same frequency, remove the one added earliest
	 * @param key
	 * @param val
	 */
	public void put(int key, int val) {
		if (this.cap <= 0) return;

		/* 若 key 已存在，修改对应的 val 即可 */
		if (keyToVal.containsKey(key)) {
			keyToVal.put(key, val);
			// key 对应的 freq 加一
			increaseFreq(key);
			return;
		}

		/* key 不存在，需要插入 */
		/* 容量已满的话需要淘汰一个 freq 最小的 key */
		if (this.cap <= keyToVal.size()) {
			removeMinFreqKey();
		}

		/* 插入 key 和 val，对应的 freq 为 1 */
		// 插入 KV 表
		keyToVal.put(key, val);
		// 插入 KF 表
		keyToFreq.put(key, 1);
		// 插入 FK 表
		freqToKeys.putIfAbsent(1, new LinkedHashSet<>());
		freqToKeys.get(1).add(key);
		// 插入新 key 后最小的 freq 肯定是 1
		this.minFreq = 1;
	}


	/**
	 *
	 *  LFU 算法的核心
	 *  1. increaseFreq()
	 *  2. removeMinFreqKey()
	 *
	 */


	/**
	 * 补充说明：
	 *
	 * 删除某个键 key 肯定是要同时修改三个映射表的，借助 minFreq 参数可以从 FK 表中找到 freq 最小的 keyList，
	 * 根据时序，其中第一个元素就是要被淘汰的 deletedKey，操作三个映射表删除这个 key 即可。
	 *
	 * 但是有个细节问题，如果 keyList 中只有一个元素，那么删除之后 minFreq 对应的 key 列表就为空了，
	 * 也就是 minFreq 变量需要被更新。如何计算当前的 minFreq 是多少呢？
	 *
	 * 实际上没办法快速计算 minFreq，只能线性遍历 FK 表或者 KF 表来计算，这样肯定不能保证 O(1) 的时间复杂度。
	 *
	 * 但是，其实这里没必要更新 minFreq 变量，因为你想想 removeMinFreqKey 这个函数是在什么时候调用？
	 * 在 put 方法中插入新 key 时可能调用。而你回头看 put 的代码，插入新 key 时一定会把 minFreq 更新成 1，所以说即便这里 minFreq 变了，我们也不需要管它
	 */
	private void removeMinFreqKey() {
		// freq 最小的 key 列表
		LinkedHashSet<Integer> keyList = freqToKeys.get(this.minFreq);
		// 其中最先被插入的那个 key 就是该被淘汰的 key
		int deletedKey = keyList.iterator().next();
		/* 更新 FK 表 */
		keyList.remove(deletedKey);
		if (keyList.isEmpty()) {
			freqToKeys.remove(this.minFreq);
			// 问：这里需要更新 minFreq 的值吗？
		}
		/* 更新 KV 表 */
		keyToVal.remove(deletedKey);
		/* 更新 KF 表 */
		keyToFreq.remove(deletedKey);
	}

	/**
	 * 更新某个 key 的 freq 肯定会涉及 FK 表和 KF 表，所以我们分别更新这两个表就行了。
	 *
	 * 和之前类似，当 FK 表中 freq 对应的列表被删空后，需要删除 FK 表中 freq 这个映射。如果这个 freq 恰好是 minFreq，说明 minFreq 变量需要更新。
	 *
	 * 能不能快速找到当前的 minFreq 呢？这里是可以的，因为之前修改的那个 key 依然是目前出现频率最小的 key，所以 minFreq 也加 1 就行了。
	 * @param key
	 */
	private void increaseFreq(int key) {
		int freq = keyToFreq.get(key);
		/* 更新 KF 表 */
		keyToFreq.put(key, freq + 1);
		/* 更新 FK 表 */
		// 将 key 从 freq 对应的列表中删除
		freqToKeys.get(freq).remove(key);
		// 将 key 加入 freq + 1 对应的列表中
		freqToKeys.putIfAbsent(freq + 1, new LinkedHashSet<>());
		freqToKeys.get(freq + 1).add(key);
		// 如果 freq 对应的列表空了，移除这个 freq
		if (freqToKeys.get(freq).isEmpty()) {
			freqToKeys.remove(freq);
			// 如果这个 freq 恰好是 minFreq，更新 minFreq
			if (freq == this.minFreq) {
				this.minFreq++;
			}
		}
	}

	public static void main(String[] args) {
	}
}
