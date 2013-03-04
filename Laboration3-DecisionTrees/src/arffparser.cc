#include "arffparser.h"
#include <utility>
using std::pair;

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

			} else if(toCompare == "@relation" || toCompare == "@data" || toCompare == "NULL") {
				//Do nothing
			//Data
			} else {
				vector<string> data = GoodString::split(line, ',');
				Example toInsert(attributeCounter);
				int c = 0;
				for(vector<string>::iterator iterator = data.begin(); iterator != data.end(); ++iterator) {
					toInsert.push_back(headerList[c].left[*iterator]);
					++c;
				}
				exampleList.push_back(toInsert); 
			}
		}
	}
	cout << headerList[0].right[0] << endl; //Y
	cout << headerList[5].right[2] << endl; //$$$
	cout << headerList[10].right[1] << endl; //N
	cout << headerList[9].right[3] << endl; //>60
	for(int i = 0; i < exampleList.size(); ++i) {
		for(int j = 0; exampleList[i].size(); ++j ) {
			cout << exampleList[i][j] << " ";
		}
		cout << endl;
	}
	//for(vector<Example> it = exampleList.begin; it != exampleList.end(); ++it) {
		
	//}
	//DEBUGGING
	//cout << headerList.size() << endl;
/*	for(int i = 0; i < headerList.size(); ++i) {
		cout << headerList[i].first << ": " << endl;
		for(int j = 0; j < headerList[i].second.size(); ++j) {
			cout << "   " << headerList[i].second[j] << endl;
		}				
	} */
	/*for(int k = 0; k < exampleList.size(); ++k) {
		for(int z = 0; z < exampleList[k].size(); ++z) {
			//cout << exampleList[k][z];
		}
		cout << endl;
	}*/
	bookExample.close();	
}

