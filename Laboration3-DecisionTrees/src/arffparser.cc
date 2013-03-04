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
			splitted = splitByWhiteSpace(line);
			if(toLowerCase(splitted[0]) == "@attribute") {	
				vector<string> newVector;
				string chars = "{}";
 				splitted[2] = removeChars(chars, splitted[2]);
				vector<string> attributes = split(splitted[2], ',');
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
	cout << headerList.size() << endl;
	for(int i = 0; i < headerList.size(); ++i) {
		cout << headerList[i].first << ": " << endl;
		for(int j = 0; j < headerList[i].second.size(); ++j) {
			cout << "   " << headerList[i].second[j] << endl;
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

vector<string> ARFFParser::splitByWhiteSpace(string toSplit) {
	vector<string> tokens;
	if(toSplit == "") {
		//decide on this
		tokens.push_back(" ");
		return tokens;
	}
	istringstream iss(toSplit);	
	copy(istream_iterator<string>(iss),
         istream_iterator<string>(),
         std::back_inserter<vector<string> >(tokens));
	return tokens;
}

std::vector<std::string> &ARFFParser::split(const std::string &s, char delim, std::vector<std::string> &elems) {
    std::stringstream ss(s);
    std::string item;
    while(std::getline(ss, item, delim)) {
        elems.push_back(item);
    }
    return elems;
}


std::vector<std::string> ARFFParser::split(const std::string &s, char delim) {
    std::vector<std::string> elems;
    return split(s, delim, elems);
}

string &ARFFParser::removeChars(string toRemove, string &target) {
	for(std::string::iterator it = toRemove.begin(); it != toRemove.end(); ++it) 
    {
    	target.erase (std::remove(target.begin(), target.end(), *it), target.end());
    }
    return target;
} 

