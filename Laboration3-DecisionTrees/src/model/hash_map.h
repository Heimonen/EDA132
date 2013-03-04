#ifndef HASHMAP_H__
#define HASHMAP_H__

#include <vector>
using std::vector;

#include <utility>
using std::pair;
#include <algorithm>
using std::find_if;
#include <functional>
using std::hash;

template<typename Key, typename Value>
struct EraseComparator {
	EraseComparator(Key key) : toLookFor(key) {}

	bool operator()(const pair<Key, Value>& pair) {
		return pair.first == name;
	}

private:
	Key toLookFor;
};

template<typename Key, typename Value>
class Hash_Map {

typedef pair<Key, Value> Pair;
typedef vector<vector<Pair> > Buckets;


public:
	Hash_Map(int size) : map(size, vector<Pair>()) {}

	void insert(const Pair& val) {
		map[hashFunction(val.first) % map.size()].push_back(val);
	}

	bool erase(const Key& key) {
		vector<Pair>& toDelete = map[hashFunction(key) % map.size()];
		EraseComparator eraser(key);
		vector<Pair>::iterator it = find_if(toDelete.begin(), toDelete.end(), eraser);
		if(it != toDelete.end()) {
			toDelete.erase(it);
			return true;
		}
		return false;
	}

	const Value operator[] (const Key& key) const {
		const vector<Pair>& toFind = map[hashFunction(key) % map.size()];
		EraseComparator finder(key);
		vector<Pair>::const_iterator begin= toFind.begin();
		vector<Pair>::const_iterator end = toFind.end();
		vector<Pair>::const_iterator it = find_if(begin, end, finder);
		if(it != toFind.end()) {
			return it->second;
		} else {
			return Value;
		}
	}
private:
	Map map;

	size_t hashFunction(const Key& key) const {
		hash<Key> H;
		return H(key);
	}
};


#endif
