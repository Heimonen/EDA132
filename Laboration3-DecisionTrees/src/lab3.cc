#include <algorithm>

#include "arffparser.h"
#include "model/example.h"
#include "algorithm.h"

#include <iostream>




int main(int argc, char* argv[]) {
	ARFFParser parser("../bookExample.arff");
	Algorithm::classification_id = find(parser.headerLookupList.begin(), parser.headerLookupList.end(),"class") - parser.headerLookupList.begin();
	std::cout << Algorithm::classification_id << std::endl;
	typename Algorithm::Examples ex;
	Algorithm::decisionTreeLearning(parser.exampleList, parser.headerList, ex)->print(parser.headerList, parser.headerLookupList, Algorithm::classification_id);

}
