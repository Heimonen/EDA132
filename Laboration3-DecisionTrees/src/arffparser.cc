#include "arffparser.h"

ARFFParser::ARFFParser(const string& inFile) {
	ifstream bookExample;
 	bookExample.open(inFile);
	vector<string> output;
	string line;
	if (bookExample.is_open()) {
		while (getline(bookExample, line)) {
			if(line == "@data")
			output.push_back(line);
			cout << line << endl;

		}
	}
	bookExample.close();
}