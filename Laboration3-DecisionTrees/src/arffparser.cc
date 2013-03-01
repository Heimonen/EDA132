#include "arffparser.h"



ARFFParser::ARFFParser(const string& inFile) {
	ifstream bookExample;
 	bookExample.open(inFile);
	vector<string> output;
	string line;
	if (bookExample.is_open()) {
		while (getline(bookExample, line)) {
			//if(toLowerCase(line) == "@DaTa") {
				output.push_back(line);
			//}
			cout << line << endl;
			
		}
	}
	bookExample.close();
}



string ARFFParser::toLowerCase(const std::string& in)
{
  string out;
  transform( in.begin(), in.end(), std::back_inserter( out ), ::tolower );
  return out;
}