#ifndef ARFF_PARSER_H__
#define ARFF_PARSER_H__

#include "model/example.h"
#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <algorithm>
#include <utility>

using namespace std;

class ARFFParser {
	typedef vector<Example> ExampleList;
	typedef pair<string, vector<string>> HeaderPair;
	typedef vector<HeaderPair> headerList;
public:
	ARFFParser(const string& inFile);
private:
	ExampleList exampleList;
	string toLowerCase(const std::string& in);
};

#endif