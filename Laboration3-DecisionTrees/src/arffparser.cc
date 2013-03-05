#include "arffparser.h"

ARFFParser::ARFFParser(const string& inFile) : headerList(75) {
	ifstream bookExample;
 	bookExample.open(inFile);
	vector<string> whiteSpaceSplit;
	string line;
	ExampleList currentList;
	if (bookExample.is_open()) {
		int attributeCounter = 0;
		while (getline(bookExample, line)) {
			whiteSpaceSplit = GoodString::splitByWhiteSpace(line);
			string toCompare = GoodString::toLowerCase(whiteSpaceSplit[0]);
			if(toCompare == "@attribute") {	
				BiMap biMap(25);
				string chars = "{}";
 				whiteSpaceSplit[2] = GoodString::removeChars(chars, whiteSpaceSplit[2]);
				vector<string> attributes = GoodString::split(whiteSpaceSplit[2], ',');
				int counter = 0;
				for(vector<string>::iterator it = attributes.begin(); it != attributes.end(); ++it) {
					typename BiMap::LeftPair pairToInsert(*it, counter);
					biMap.insert(pairToInsert);
					++counter;
				}
				pair<int, BiMap> newPair(attributeCounter, biMap);
				++attributeCounter;
				headerList.insert(newPair);
				headerLookupList.push_back(whiteSpaceSplit[1]);
			} else if(toCompare == "@relation" || toCompare == "@data" || toCompare == "null") {
				;//Do nothing
			} else {
				vector<string> data = GoodString::split(line, ',');
				Example toInsert(0);
				int c = 0;
				for(vector<string>::iterator iterator = data.begin(); iterator != data.end(); ++iterator) {
					toInsert.push_back(headerList[c].left[*iterator]);
					++c;
				}
				exampleList.push_back(toInsert);
			}
		}
	}
	//DEBUGGING
	cout << "Example list:" << endl;
	for(vector<Example>::iterator it = exampleList.begin(); it != exampleList.end(); ++it) {
		for(vector<unsigned int>::iterator ite = it->begin(); ite != it->end(); ++ite) {
			cout << *ite << " ";
		}
		cout << endl;
	}
	bookExample.close();	
}

