package src.HashMap;

public class Test {
	public static void main(String[] args) {
		LinearHashMap<String, String> hashMap = new LinearHashMap(99);
		hashMap.put("Alan", "Java");
		hashMap.put("Code", "Skill");
		hashMap.put("Rich", "Yes");

		System.out.println(hashMap.get("Alan"));
		hashMap.put("Rich", "Of Course");
		System.out.println(hashMap.get("Rich"));
		hashMap.remove("Code");
		System.out.println(hashMap.get("Code"));
		System.out.println(hashMap.get("Rich"));
		System.out.println(hashMap.get("Alan"));

		//duplicate key test
		/**
		 * String --- hashCode
		 * AaAaAa---1952508096
		 * AaAaBB---1952508096
		 * AaBBAa---1952508096
		 * AaBBBB---1952508096
		 * BBAaAa---1952508096
		 * BBAaBB---1952508096
		 * BBBBAa---1952508096
		 * BBBBBB---1952508096
		 * TODO expect to have <AaAaAa, collision1> and  <AaAaBB, collision2> both saved in the hashmap
		 * (notice that 'AaAaAa' and 'AaAaBB' have same hashcode)
		 */
		hashMap.put("AaAaAa", "collision1");
		hashMap.put("AaAaBB", "collision2");
		System.out.println(hashMap.get("AaAaAa"));
		System.out.println(hashMap.get("AaAaBB"));
	}
}
