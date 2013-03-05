#ifndef ARFF_PARSER_H__
#define ARFF_PARSER_H__

#include "model/example.h"
#include "model/goodstring.h"
#include "model/bihashmap.h"
#include "model/hash_map.h"
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
	typedef BiHashMap<string, size_t> BiMap;
	typedef Hash_Map<int, BiMap > HeaderList;
public:
	ARFFParser(const string& inFile);
	ExampleList exampleList;
	HeaderList headerList;
	vector<string> headerLookupList;
};

#endif