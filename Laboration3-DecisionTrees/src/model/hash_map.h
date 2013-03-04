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
		return pair.first == toLookFor;
	}

private:
	Key toLookFor;
};

template<typename Key, typename Value>
class Hash_Map {

typedef pair<Key, Value> Pair;
typedef vector<vector<Pair> > Buckets;
typedef EraseComparator<Key, Value> Eraser;

public:
	Hash_Map(int size) : map(size, vector<Pair>()), elemCount(0) {}

	void insert(const Pair& val) {
		map[hashFunction(val.first) % map.size()].push_back(val);
		++elemCount;
	}

	bool erase(const Key& key) {
		vector<Pair>& toDelete = map[hashFunction(key) % map.size()];
		Eraser eraser(key);
		typename vector<Pair>::iterator it = find_if(toDelete.begin(), toDelete.end(), eraser);
		if(it != toDelete.end()) {
			toDelete.erase(it);
			--elemCount;
			return true;
		}
		return false;
	}

	const Value operator[] (const Key& key) const {
		const vector<Pair>& toFind = map[hashFunction(key) % map.size()];
		Eraser finder(key);
		typename vector<Pair>::const_iterator begin= toFind.begin();
		typename vector<Pair>::const_iterator end = toFind.end();
		typename vector<Pair>::const_iterator it = find_if(begin, end, finder);
		if(it != toFind.end()) {
			return it->second;
		} else {
			return Value();
		}
	}

	size_t size () {
		return elemCount;
	}

private:
	Buckets map;
	size_t elemCount;

	size_t hashFunction(const Key& key) const {
		hash<Key> H;
		return H(key);
	}
};

#endif
