#ifndef EXAMPLE_H__
#define EXAMPLE_H__

#include <vector>
using std::vector;
#include <string> // for size_t

class Example : private vector<unsigned int> {
	using vector::begin;
	using vector::end;
	using vector::operator[];
	using vector::push_back;

public:
	Example(size_t numElements) : vector(numElements) {}
};

#endif /* end of include guard: EXAMPLE_H__ */
