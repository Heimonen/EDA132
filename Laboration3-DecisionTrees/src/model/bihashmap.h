template<typename Key1, typename Key2>
class BiHashMap {
	typedef pair<Key1, Key2> LeftPair;
	typedef hash_map<Key1, Key2> LeftMap;

	typedef pair<Key2, Key1> RightPair;
	typedef hash_map<Key2, Key1> RightMap;

	LeftMap left;
	RightMap right;

	bool empty() {
		return left.empty() && right.empty();
	}

	void insert(const LeftPair& pair) {
		RightPair rightPair;
		rightPair.first = pair.second;
		rightPair.second =  pair.first;
		left.insert(pair);
		right.insert(rightPair);
	}

	
}