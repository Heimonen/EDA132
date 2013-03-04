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
	IteratorT best(it++);
	typename HeuristicFunctorT::result_type best_value(functor(*best));

	for(; it != end; ++it) {
	    typename HeuristicFunctorT::result_type value(functor(*it));

	    if (value > best_value) {
	        best_value = value;
	        best = it;
	    }
	}

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
	typedef vector<AttributeValue> Attributes;
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
			if(attributes.empty()) {
				subTree.value = pluralityValue(examples);
				return subTree;
			} else {
				Importance<Attribute, Examples> functor;
				functor.meta = examples;
				Attribute mostImportant = *argmax(attributes.begin(), attributes.end(), functor, examples);
				//for()
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
