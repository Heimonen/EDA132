
#include "arffparser.h"
#include "model/example.h"
#include "algorithm.h"


int main(int argc, char* argv[]) {
	ARFFParser parser("../bookExample.arff");
	Algorithm algorithm;
	vector<Example> examples;
	Algorithm::decisionTreeLearning(parser.exampleList, parser.headerList, examples);
}
