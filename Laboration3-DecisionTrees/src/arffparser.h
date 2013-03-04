#ifndef ARFF_PARSER_H__
#define ARFF_PARSER_H__

#include "model/example.h"
#include "model/goodstring.h"
#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <algorithm>
#include <utility>
#include <iterator>


#include <sstream>

using namespace std;

class ARFFParser {
	typedef vector<Example> ExampleList;
	typedef pair<string, vector<string>> HeaderPair;
	typedef vector<HeaderPair> HeaderList;

public:
	ARFFParser(const string& inFile);
private:
	ExampleList exampleList;
	HeaderList headerList;
};

#endif