#ifndef ALGORITHM_H__
#define ALGORITHM_H__


#include <map>
using std::map;
#include <algorithm>
using std::unary_function; 
#include <cstdlib> // rand;
#include <utility>
using std::pair;
#include <vector>
using std::vector;
#include <iostream>
using std::cout;
using std::endl;

#include "model/example.h"
#include "arffparser.h"

struct TreeNode;

struct TreeNode : public vector<pair<unsigned int, TreeNode*> > {	
public:
	unsigned int value;

	void print(ARFFParser::HeaderList& header, vector<string>& headerLookupList, unsigned int& classification_id) {
		print(0,header,headerLookupList, classification_id);
	}
private:
	void print(size_t depth,ARFFParser::HeaderList& header, vector<string>& headerLookupList, unsigned int& classification_id) {
		if(empty()) {
			cout << ": " << header[classification_id].right[value];
		}
		cout << endl;
		for(size_t i = depth; i != 0; --i) {
			cout << '\t';
		}
		for(vector<pair<unsigned int, TreeNode*> >::iterator i = begin(); i != end(); ++i){
			cout << headerLookupList[value] << " = " << header[value].right[i->first];
			i->second->print(depth+1, header, headerLookupList, classification_id);
		}
	}
};

class Algorithm {
	typedef vector<Example> Examples;
	typedef unsigned int Attribute;
	typedef unsigned int AttributeValue;
	typedef typename ARFFParser::BiMap BiMap;

	typedef Hash_Map<int, BiMap> Attributes;
	typedef TreeNode Tree;

public:
	static Tree* decisionTreeLearning(Examples& examples, Attributes& attributes, Examples& parent_examples) {
		Tree* realTree = new TreeNode;
		if(examples.empty()) {
			realTree->value = pluralityValue(parent_examples);
			return realTree;
		} else {
			{
				pair<bool, AttributeValue> hsc = hasSameClassification(examples);
				if(hsc.first) {
					realTree->value = hsc.second;
					return realTree;
				}
			}
			if(attributes.size() == 0) {
				realTree->value = pluralityValue(examples);
				return realTree;
			} else {
				vector<vector<pair<int, BiMap> > >::iterator bit = attributes.map.begin();

				vector<pair<int, BiMap> >::iterator it = bit->begin();
				while(it == bit->end()) {
					++bit;
					it = bit->begin();
				}

				pair<int, BiMap>* best = &(*it);
				unsigned int best_value(rand() % 10); // use importance here aswell

				for(; bit != attributes.map.end(); ++bit) {
					for(it = bit->begin();it != bit->end(); ++it) {

					    unsigned int value = rand() % 10; //TODO implement importance

					    if (value > best_value) {
					        best_value = value;
					        best = &(*it);
					    }
					}
				}

				BiMap mostImportant = best->second;
				realTree->value = best->first;

				for(vector<vector<pair<string, size_t> > >::iterator i = mostImportant.left.map.begin(); i != mostImportant.left.map.end(); ++i) {
					for(vector<pair<string, size_t> >::iterator j = i->begin(); j != i->end(); ++j) {
						vector<Example> exs;
						for(vector<Example>::iterator ei = examples.begin(); ei != examples.end(); ++ei) {
							if((*ei)[best->first] == j->second) {
								exs.push_back(*ei);
							}
						}
						Attributes na = attributes;
						na.erase(best->first);
						Example::printList(exs);
						TreeNode* subtree = decisionTreeLearning(exs,na,examples);
						realTree->push_back(pair<unsigned int, TreeNode*>(j->second, subtree));
					}
				}
				return realTree;
			}
		}
	}
	static unsigned int classification_id;
private:
	static AttributeValue pluralityValue(const Examples& parent_examples) {
		map<unsigned int, unsigned int> count;
		for(vector<Example>::const_iterator i = parent_examples.begin(); i != parent_examples.end(); ++i) {
			++count[(*i)[classification_id]];
		}
		map<unsigned int, unsigned int>::iterator i = count.begin();
		pair<unsigned int, unsigned int> best(*(i++));
		unsigned int best_value = best.second;
		for(; i != count.end(); ++i) {
			if(i->second > best_value) {
				best = *i;
				best_value = best.second;
			}
		}
		return best.first;
	}

	static pair<bool, AttributeValue> hasSameClassification(Examples& examples) {
		pair<bool, AttributeValue> rtn;
		Examples::iterator i = examples.begin();
		rtn.second = *((*i++).rbegin());
		rtn.first = false;
		for(; i != examples.end(); ++i) {
			if(rtn.second != *((*i).rbegin()) ) {
				return rtn;
			}
		}
		rtn.second = true;
		return rtn;
	}

};

#endif /* end of include guard: ALGORITHM_H__ */
