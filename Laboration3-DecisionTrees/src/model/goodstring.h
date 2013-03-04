#ifndef GOOD_STRING_H__
#define GOOD_STRING_H__

#include <string>

class GoodString {

public:

GoodString(string s) {
	goodString = s;
}

string ARFFParser::toLowerCase()
{
  string out;
  transform( goodString.begin(), goodString.end(), std::back_inserter( out ), ::tolower );
  goodString = out;
}

vector<string> ARFFParser::splitByWhiteSpace() {
	vector<string> tokens;
	if(goodString == "") {
		//decide on this
		tokens.push_back(" ");
		return tokens;
	}
	istringstream iss(goodString);	
	copy(istream_iterator<string>(iss),
         istream_iterator<string>(),
         std::back_inserter<vector<string> >(tokens));
	return tokens;
}

std::vector<std::string> ARFFParser::split(, char delim) {
    std::vector<std::string> elems;
    return split(goodString, delim, elems);
}

void &ARFFParser::removeChars(string toRemove) {
	for(std::string::iterator it = toRemove.begin(); it != toRemove.end(); ++it) 
    {
    	goodString.erase (std::remove(goodString.begin(), goodString.end(), *it), goodString.end());
    }
} 

private:

string goodString;

std::vector<std::string> &ARFFParser::split(const std::string &s, char delim, std::vector<std::string> &elems) {
    std::stringstream ss(s);
    std::string item;
    while(std::getline(ss, item, delim)) {
        elems.push_back(item);
    }
    return elems;
}



};

#endif