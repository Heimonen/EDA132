#ifndef ARFF_PARSER_H__
#define ARFF_PARSER_H__

#include "model/example.h"
#include <vector>
#include <string>
#include <iostream>
#include <fstream>

using namespace std;

class ARFFParser {
	typedef vector<Example> ExampleList;
public:
	ARFFParser(const string& inFile);
private:
	ExampleList exampleList;
};

#endif