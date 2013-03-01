#include <algorithm>
using std::unary_function; 
#include <cstdlib> // rand;

#include "model/example.h"

struct Tree;
struct Attributes;
struct Attribute;

template<typename IteratorT, typename HeuristicFunctorT>
IteratorT argmax(IteratorT it, const IteratorT & end, const HeuristicFunctorT & functor) {
IteratorT best(it++);
typename HeuristicFunctorT::result_type best_value(functor(*best));

for(; it != end; ++it) {
    typename HeuristicFunctorT::result_type value(functor(*it));

    if (value > best_value) {
        best_value = value;
        best = it;
    }
}

struct Importance : public binary_function<Example,int> {
  int operator() (const Example) {return (rand()%10);}
};

return best;
}

class Algorithm {



public:
	Tree decisionTreeLearning(Example& examples, Attributes& attributes, const Example& parent_examples) {

	}
private:
};