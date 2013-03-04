#include "hash_map.h"

template<typename Key1, typename Key2>
class BiHashMap {
public:
	typedef pair<Key1, Key2> LeftPair;
	typedef Hash_Map<Key1, Key2> LeftMap;

	typedef pair<Key2, Key1> RightPair;
	typedef Hash_Map<Key2, Key1> RightMap;
	LeftMap left;
	RightMap right;

	BiHashMap(unsigned int size) : left(size), right(size) {}

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
	
};
