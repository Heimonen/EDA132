#ifndef ALGORITHM_H__
#define ALGORITHM_H__

#include <cmath>
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
		for(vector<pair<unsigned int, TreeNode*> >::iterator i = begin(); i != end(); ++i){
			indent(depth);
			cout << headerLookupList[value] << " = " << header[value].right[i->first];
			if(i->second->empty()) {
				cout << ": " << header[classification_id].right[i->second->value] << endl;
			} else {
				cout << endl;
				i->second->print(depth+1, header, headerLookupList, classification_id);
			}
		}
	}
	void indent(size_t depth) {
		for(size_t i = depth; i != 0; --i) {
			cout << ' ';
		}
	}
};

class Algorithm {
	typedef vector<Example> Examples;
	typedef unsigned int Attribute;
	typedef unsigned int AttributeValue;
	typedef typename ARFFParser::BiMap BiMap;

	typedef Hash_Map<unsigned int, BiMap> Attributes;
	typedef TreeNode Tree;

public:
	static void test(Examples& examples, Attributes& attributes, vector<string>& headerLookupList) {
		cout << "B(0.5)=" << B(0.5) << endl;
		cout << "B(0)=" << B(0) << endl;
		cout << "B(1)=" << B(1) << endl;
		for(unsigned int i = 0; i < 10; ++i ) {
			cout << headerLookupList[i] << " = " << Gain(i,examples,attributes) << endl;
		}

	}

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
			if(attributes.size() == 1) {
				realTree->value = pluralityValue(examples);
				return realTree;
			} else {
				vector<vector<pair<unsigned int, BiMap> > >::iterator bit = attributes.map.begin();

				vector<pair<unsigned int, BiMap> >::iterator it = bit->begin();
				while(it == bit->end() || it->first == classification_id) {
					++bit;
					it = bit->begin();
				}

				pair<unsigned int, BiMap>* best = &(*it);
				double best_value(Gain(best->first,examples,attributes)); // use importance here aswell

				for(; bit != attributes.map.end(); ++bit) {
					for(it = bit->begin();it != bit->end(); ++it) {
						if(classification_id == it->first) {
							continue;
						}

					    double value = Gain(it->first,examples,attributes); //TODO implement importance

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
		rtn.first = true;
		return rtn;
	}

	static double B(double q) {
		if(q == 0 || q == 1){
			return 0;
		}
		return -(q * log2(q) + (1-q) * log2(1-q));
	}

	static double Reminder(unsigned int& attribute, Examples& e, ARFFParser::HeaderList& header) {
		size_t index = 0;
		string empty;
		ARFFParser::BiMap attrValues = header[attribute];
		double sum = 0;
		string get;
		while((get = attrValues.right[index]) != empty) {
			double pk 	= getPk(attribute,index,e);
			double nk 	= getNk(attribute,index,e);
			double p 	= getP(attribute,e);
			double n 	= getN(attribute, e);
			double b 	= B(pk/(pk + nk));
			#if DEBUG
			cout << "Reminder = pk:" << pk << " nk:" << nk << " p:" << p << " n:" << n << " B:" << b << endl; 
			#endif
			sum += ((pk+nk)/( p + n )) * b;
			++index;
		}
		return sum;
	}

	static double Gain(unsigned int& attribute, Examples& e, ARFFParser::HeaderList& header){
		double p = getP(attribute,e);
		double n = getN(attribute,e);
		double reminder  = Reminder(attribute,e,header);
		double b = B(p/(p+n));
		#if DEBUG
		cout << "Gain = p:" << p << " n:" << n << " Reminder:" << reminder << " B:" << b << endl;
		#endif
		return b - reminder;
	}

	static double getPk(unsigned int& attribute, size_t& attrVal, Examples e) {
		size_t count = 0;
		for(Examples::iterator i = e.begin(); i != e.end(); ++i) {
			if( (*i)[classification_id] == 0 && (*i)[attribute] == attrVal){
				++count;
			}
		}
		return  count;
	}

	static double getNk(unsigned int& attribute, size_t& attrVal, Examples e) {
		size_t count = 0;
		for(Examples::iterator i = e.begin(); i != e.end(); ++i) {
			if((*i)[classification_id] == 1 &&(*i)[attribute] == attrVal){
				++count;
			}
		}
		return  count;
	}

	static double getP(unsigned int attr, Examples& e) {
		size_t count = 0;
		for(Examples::iterator i = e.begin(); i != e.end(); ++i) {
			if((*i)[classification_id] == 0){ // Y
				++count;
			}
		}
		return  count;
	}

	static double getN(unsigned int attr, Examples& e) {
		size_t count = 0;
		for(Examples::iterator i = e.begin(); i != e.end(); ++i) {
			if((*i)[classification_id] == 1){ // N
				++count;
			}
		}
		return  count;
	}


};

#endif /* end of include guard: ALGORITHM_H__ */
