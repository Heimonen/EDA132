#include <cstdlib>
#include <iostream>
using std::cout;
using std::endl;

inline char getNext(const unsigned int& phase){
	switch(phase) {
	case 0:
		return 's';
	break;
	case 1:
		return 'a';
	break;
	case 2:
		return 'n';
	break;
	case 3:
		return 'e';
	break;
	}	
}

int main () {
	unsigned int count = 0;
	unsigned int phase = 0;
	while(phase < 3) {
		srand(++count);
		if(getNext(phase) == rand()) {
			++phase;
			continue;
		}
	}
	cout << count << endl;
}