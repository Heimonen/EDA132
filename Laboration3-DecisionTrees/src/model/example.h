#include <vector>
using std::vector;

class Example : private vector {
using vector::begin;
using vector::end;
using vector::operator[];
using vector::push_back;

public:
	Example(size_t numElements) : vector(numElements) {}
};