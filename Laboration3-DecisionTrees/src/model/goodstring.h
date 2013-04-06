#ifndef GOOD_STRING_H__
#define GOOD_STRING_H__

#include "example.h"
#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <algorithm>
#include <utility>
#include <iterator>
#include <sstream>

using namespace std;

class GoodString {

public:

GoodString() {}

~GoodString(){}

static string toLowerCase(const std::string& in)
{
  string out;
  transform( in.begin(), in.end(), std::back_inserter( out ), ::tolower );
  return out;
}

static vector<string> splitByWhiteSpace(string toSplit) {
  vector<string> tokens;
  if(toSplit == "") {
    //decide on this
    tokens.push_back("NULL");
    return tokens;
  }
  istringstream iss(toSplit); 
  copy(istream_iterator<string>(iss),
         istream_iterator<string>(),
         std::back_inserter<vector<string> >(tokens));
  return tokens;
}

static std::vector<std::string> &split(const std::string &s, char delim, std::vector<std::string> &elems) {
    std::stringstream ss(s);
    std::string item;
    while(std::getline(ss, item, delim)) {
        elems.push_back(item);
    }
    return elems;
}


static std::vector<std::string> split(const std::string &s, char delim) {
    std::vector<std::string> elems;
    return GoodString::split(s, delim, elems);
}

static string &removeChars(string toRemove, string &target) {
  for(std::string::iterator it = toRemove.begin(); it != toRemove.end(); ++it) 
    {
      target.erase (std::remove(target.begin(), target.end(), *it), target.end());
    }
    return target;
} 



};

#endif