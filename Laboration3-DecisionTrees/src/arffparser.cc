#include "arffparser.h"


ARFFParser::ARFFParser(const string& inFile) {
	ifstream bookExample;
 	bookExample.open(inFile);
	vector<string> output;
	vector<string> splitted;
	string line;
	ExampleList currentList;
	if (bookExample.is_open()) {
		while (getline(bookExample, line)) {
			splitted = GoodString::splitByWhiteSpace(line);
			string toCompare = GoodString::toLowerCase(splitted[0]);
			if(toCompare == "@attribute") {	
				vector<string> newVector;
				string chars = "{}";
 				splitted[2] = GoodString::removeChars(chars, splitted[2]);
				vector<string> attributes = GoodString::split(splitted[2], ',');
				for(vector<string>::iterator it = attributes.begin(); it != attributes.end(); ++it) {
					newVector.push_back(*it);
				}
				HeaderPair newPair(splitted[1], newVector);
				headerList.push_back(newPair);
			} else if(toCompare == "@relation" || toCompare == "@data" || toCompare == "NULL") {
				//Do nothing
			//Data
			} else {
				vector<string> data = GoodString::split(line, ',');
				Example toInsert(headerList.size());
				for(vector<string>::iterator iterator = data.begin(); iterator != data.end(); ++iterator) {
					//toInsert.push_back(*iterator);
				}
				exampleList.push_back(toInsert);
			}
		}
	}
	//DEBUGGING
	cout << headerList.size() << endl;
	for(int i = 0; i < headerList.size(); ++i) {
		cout << headerList[i].first << ": " << endl;
		for(int j = 0; j < headerList[i].second.size(); ++j) {
			cout << "   " << headerList[i].second[j] << endl;
		}				
	}
	/*for(int k = 0; k < exampleList.size(); ++k) {
		for(int z = 0; z < exampleList[k].size(); ++z) {
			//cout << exampleList[k][z];
		}
		cout << endl;
	}*/
	bookExample.close();	
}

