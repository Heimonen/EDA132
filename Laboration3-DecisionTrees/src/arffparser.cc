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
			if(GoodString::toLowerCase(splitted[0]) == "@attribute") {	
				vector<string> newVector;
				string chars = "{}";
 				splitted[2] = GoodString::removeChars(chars, splitted[2]);
				vector<string> attributes = GoodString::split(splitted[2], ',');
				for(vector<string>::iterator it = attributes.begin(); it != attributes.end(); ++it) {
					newVector.push_back(*it);
				}
				HeaderPair newPair(splitted[1], newVector);
				headerList.push_back(newPair);
			} else {
				cout << "NO ATTRIBUTE: " << line << endl;
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
	bookExample.close();	
}

