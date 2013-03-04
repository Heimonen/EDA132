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
	bool value;
	bool isLeaf() {
		return vector<pair<Attribute, AttributeValue> >::empty();
	}
};

template<typename IteratorT, typename HeuristicFunctorT, typename Metadata>
IteratorT argmax(IteratorT it, const IteratorT& end, const HeuristicFunctorT& functor, const Metadata& meta) {
	IteratorT best(it++);
	typename HeuristicFunctorT::result_type best_value(functor(*best, meta));

	for(; it != end; ++it) {
	    typename HeuristicFunctorT::result_type value(functor(*it, meta));

	    if (value > best_value) {
	        best_value = value;
	        best = it;
	    }
	}

	return best;
}

template<typename Attribute, typename Examples>
struct Importance : public binary_function<const Attribute&, const Examples&, int> {
	int operator() (const Attribute& attr, const Examples& examples ) {
		return (rand()%10); // implement proper evaluation!
	}
};

class Algorithm {
	typedef vector<Example> Examples;
	typedef unsigned int Attribute;
	typedef unsigned int AttributeValue;
	typedef vector<AttributeValue> Attributes;
	typedef TreeNode<Attribute, AttributeValue> Tree;

public:
	Tree decisionTreeLearning(Examples& examples, Attributes& attributes, const Examples& parent_examples) {
		
	}
private:
};

#endif /* end of include guard: ALGORITHM_H__ */
