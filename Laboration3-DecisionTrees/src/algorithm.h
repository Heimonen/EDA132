#include <algorithm>
using std::unary_function; 
#include <cstdlib> // rand;

#include "model/example.h"

struct Tree;
struct Attributes;
struct Attribute;
struct Examples

template<typename IteratorT, typename HeuristicFunctorT>
IteratorT argmax(IteratorT it, const IteratorT & end, const HeuristicFunctorT & functor, const Examples& examples) {
IteratorT best(it++);
typename HeuristicFunctorT::result_type best_value(functor(*best,examples));

for(; it != end; ++it) {
    typename HeuristicFunctorT::result_type value(functor(*it, examples));

    if (value > best_value) {
        best_value = value;
        best = it;
    }
}

struct Importance : public binary_function<const Attribute&, const Examples&, int> {
	int operator() (const Attribute& attr, const Examples& examples ) {return (rand()%10);}
};

return best;
}

class Algorithm {



public:
	Tree decisionTreeLearning(vector<Example>& examples, Attributes& attributes, const Example& parent_examples) {
		
	}
private:
};