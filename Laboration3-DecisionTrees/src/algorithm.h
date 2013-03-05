#ifndef ALGORITHM_H__
#define ALGORITHM_H__

#include <algorithm>
using std::unary_function; 
#include <cstdlib> // rand;
#include <utility>
using std::pair;
#include <vector>
using std::vector;

#include "model/example.h"

template<typename Attribute, typename AttributeValue>
struct TreeNode : public vector<pair<Attribute, AttributeValue> > {	
public:
	AttributeValue value;
	bool isLeaf() {
		return vector<pair<Attribute, AttributeValue> >::empty();
	}
};

template<typename IteratorT, typename HeuristicFunctorT, typename Metadata>
IteratorT argmax(IteratorT it, const IteratorT& end, const HeuristicFunctorT& functor, const Metadata& meta) {
	

	return best;
}

template<typename Attribute, typename Examples>
struct Importance : public unary_function<const Attribute&, int> {
public:
	Examples meta;
	int operator() (const Attribute& attr ) const {
		return (rand()%10); // implement proper evaluation!
	}
};

//template<typename Attribute

class Algorithm {
	typedef vector<Example> Examples;
	typedef unsigned int Attribute;
	typedef unsigned int AttributeValue;
	typedef BiHashMap<size_t, string> AttributeValues;
	typedef Hash_Map<int, AttributeValues> Attributes;
	typedef TreeNode<Attribute, AttributeValue> Tree;

public:
	static Tree decisionTreeLearning(Examples& examples, Attributes& attributes, Examples& parent_examples) {
		Tree subTree;
		if(examples.empty()) {
			subTree.value = pluralityValue(parent_examples);
			return subTree;
		} else {
			{
				pair<bool, AttributeValue> hsc = hasSameClassification(parent_examples);
				if(hsc.first) {
					subTree.value = hsc.second;
					return subTree;
				}
			}
			if(attributes.size() == 0) {
				subTree.value = pluralityValue(examples);
				return subTree;
			} else {
				Importance<Attribute, Examples> functor;
				functor.meta = examples;
				
				
				AttributeValues
				IteratorT best(it++);
				unsigned int best_value(functor(*best));

				for(vector<vector<; it != end; ++it) {
				    unsigned value(functor(*it));

				    if (value > best_value) {
				        best_value = value;
				        best = it;
				    }
				}

				
				for(AttributeValues::iterator i = mostImportant.begin(); i != mostImportant.end(); ++i) {

				}
			}
		}
	}
private:
	static AttributeValue pluralityValue(const Examples& parent_examples) {

	}

	static pair<bool, AttributeValue> hasSameClassification(Examples& examples) {
		pair<bool, AttributeValue> rtn;
		Examples::iterator i = examples.begin();
		rtn.second = *((*i++).rbegin());
		rtn.first = false;
		for(; i != examples.end(); ++i) {
			if(rtn.second != *((*i).rbegin()) ) {
				return rtn;
			}
		}
		rtn.second = true;
		return rtn;
	}

};

#endif /* end of include guard: ALGORITHM_H__ */
