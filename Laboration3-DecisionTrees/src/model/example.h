#ifndef EXAMPLE_H__
#define EXAMPLE_H__

#include <vector>
using std::vector;
#include <string> // for size_t

#include <iostream>
using std::cout;
using std::endl;

class Example : public vector<unsigned int> {

public:
	Example(size_t numElements) : vector(numElements) {}
	static void printList(const vector<Example>& exampleList) {
		cout << "Example list:" << endl;
		for(vector<Example>::const_iterator it = exampleList.begin(); it != exampleList.end(); ++it) {
			for(vector<unsigned int>::const_iterator ite = it->begin(); ite != it->end(); ++ite) {
				cout << *ite << " ";
			}
			cout << endl;
		}
	}
};

#endif /* end of include guard: EXAMPLE_H__ */
