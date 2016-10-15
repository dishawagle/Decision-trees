package decisiontree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

public class DecisionTree {

	public static ArrayList<TreeSet<Double>> attributeValues = new ArrayList<TreeSet<Double>>();//Treeset has been used for storing the values only because the values can be directly stored in a sorted manner and is not used in the building of the decision tree.

	private class Gain {
		public double gain = 0;
		public double threshold = 0;

		public Gain(double g, double t) {
			gain = g;
			threshold = t;
		}
	}

	/**
	 * H(S): This function calculates the entropy H(S) of set S
	 * 
	 * @param vectors
	 *            : S
	 * @return : H(S)
	 */
	private double entropy(ArrayList<ArrayList<Double>> vectors) {
		double ones = 0;
		double zeros = 0;
		double pZero = 0;
		double pOne = 0;
		double logpZero = 0;
		double logpOne = 0;

		for (ArrayList<Double> vector : vectors) {
			if (vector.get(0) == 0)
				zeros++;
			else
				ones++;
		}
		if(ones==0||zeros==0)
			return 0;
		pZero = zeros / (ones + zeros);
		pOne = ones / (ones + zeros);
		if (pZero != 0)
			logpZero = Math.log(pZero) / Math.log(2);
		if (pOne != 0)
			logpOne = Math.log(pOne) / Math.log(2);

		double entropy = -((pOne * logpOne) + (pZero * logpZero));
		return entropy;
	}

	/**
	 * This creates a sorted set out of all the distinct values of all
	 * attributes for a given set. Sorted set is saved in variable
	 * attributeValues
	 * 
	 * @param Vectors
	 *            : given set
	 */
	private void setValues(ArrayList<ArrayList<Double>> Vectors) {
		for (int i = 0; i < Vectors.get(0).size(); i++) {
			TreeSet<Double> temp1 = new TreeSet<Double>();
			for (int j = 0; j < Vectors.size(); j++) {
				double h = Vectors.get(j).get(i);
				temp1.add(h);

			}
			attributeValues.add(temp1);
		}
	}

	/**
	 * This function calculates information gain G(S,A) for attribute A in set
	 * S. This function also selects the threshold value of an attribute.
	 * Threshold value with lowest entropy is selected and that threshold is
	 * used to calculate the gain. Returns Gain object which contains gain
	 * along with threshold value.
	 * 
	 * @param attribute:
	 *            Attribute A
	 * @param vectors:
	 *            Set S
	 * @return: Gain
	 */
	private Gain getGain(int attribute, ArrayList<ArrayList<Double>> vectors) {
		TreeSet<Double> values = attributeValues.get(attribute);
		for (ArrayList<Double> vector : vectors) {
			values.add(vector.get(attribute));
		}
		double min = Double.MAX_VALUE;
		double threshold = 0;
		double largeEntropy = 0;
		double smallEntropy = 0;
		double largeSize = 0;
		double smallSize = 0;
		while (values.size() > 1) {
			double a = values.pollFirst(); // take and remove first
			double b = values.first(); // take second
			double avg = (a + b) / 2; // take average
			ArrayList<ArrayList<Double>> largeSet = new ArrayList<ArrayList<Double>>();
			ArrayList<ArrayList<Double>> smallSet = new ArrayList<ArrayList<Double>>();
			for (ArrayList<Double> vector : vectors) { // partition around mid
														// or avg
				if (vector.get(attribute) > avg)
					largeSet.add(vector);
				else
					smallSet.add(vector);
			}
			double midpointEntropy = entropy(largeSet) + entropy(smallSet); // to find mid or avg with minimum entropy
																			
			if (midpointEntropy < min) {
				min = midpointEntropy;
				threshold = avg;
				largeEntropy = entropy(largeSet);
				smallEntropy = entropy(smallSet);
				largeSize = largeSet.size();
				smallSize = smallSet.size();
			}
		}

		// got minentropy threshold
		double totalEntropy = entropy(vectors);// H(s)
		double totalSize = vectors.size();// |S|
		double attributeEntropy = ((largeSize / totalSize) * largeEntropy) + ((smallSize / totalSize) * smallEntropy); // summation
																														// term
		double gain = totalEntropy - attributeEntropy;// get gain from
														// minentropy threshold
		
		Gain gainResult = new Gain(gain, threshold);
		return gainResult;
	}

	private dTreeNode partition(ArrayList<ArrayList<Double>> vectors, HashSet<Integer> parent, int depth) {
		dTreeNode currentNode = new dTreeNode(0, false);
		double totalEntropy = entropy(vectors);
		if (totalEntropy == 0) { //if totalentropy=0, node attribute value is given the value of the label and isLeaf=true 
			if (vectors.get(0).get(0) == 1) {
				currentNode.att = 1;
				currentNode.isLeaf = true;
				return currentNode;
			} else {
				currentNode.att = 0;
				currentNode.isLeaf = true;
				return currentNode;
			}
		} else if (depth == 0) {//if depth=0, majority label value becomes the value of the leaf
			int zero = 0;
			int one = 0;
			for (ArrayList<Double> vector : vectors) {
				if (vector.get(0) == 0)
					zero++;
				else
					one++;
			}
			if (zero > one) {
				dTreeNode d = new dTreeNode(0, true);
				return d;
			} else {
				dTreeNode d = new dTreeNode(1, true);
				return d;
			}
		} else {
			Gain[] gainAttributes = new Gain[vectors.get(0).size()];
			
			double max = 0;
			int splittingAttribute = 0;
			for (int i = 1; i < vectors.get(0).size(); i++) {
				if (parent.contains(i))//if parent contains the attribute i, i.e the attribute has already been used, continue, do not use it again
					continue;
				gainAttributes[i] = getGain(i, vectors);
				if (gainAttributes[i].gain > max) {//attribute with maximum gain is selected as the splitting attribute  
					max = gainAttributes[i].gain;
					splittingAttribute = i;
				}
			}
			if (splittingAttribute == 0) {
				int zero = 0;
				int one = 0;
				for (ArrayList<Double> vector : vectors) {
					if (vector.get(0) == 0)
						zero++;
					else
						one++;
				}
				if (zero > one) {
					dTreeNode d = new dTreeNode(0, true);
					return d;
				} else {
					dTreeNode d = new dTreeNode(1, true);
					return d;
				}
			} else {
				currentNode.att = splittingAttribute;
				double threshold = gainAttributes[splittingAttribute].threshold; 
				//Divided into two subsets in order to partition
				ArrayList<ArrayList<Double>> LargeSubset = new ArrayList<ArrayList<Double>>();
				ArrayList<ArrayList<Double>> SmallSubset = new ArrayList<ArrayList<Double>>();
				for (ArrayList<Double> vector : vectors) {
					if (vector.get(splittingAttribute) > threshold) {
						LargeSubset.add(vector);
					} else {
						SmallSubset.add(vector);
					}
				}
				HashSet<Integer> newSet = new HashSet<Integer>();
				newSet = (HashSet<Integer>) parent.clone();
				newSet.add(splittingAttribute);//once the attribute has been used,it is added to parent, so that it wont be used for that subset again
				dTreeNode rightChild = partition(LargeSubset, newSet, depth-1);
				dTreeNode leftChild = partition(SmallSubset, newSet, depth-1);
				currentNode.children.put(1.0, leftChild);
				currentNode.children.put(2.0, rightChild);
				currentNode.threshold = threshold;
				return currentNode;
			}
		}
	}

	public dTreeNode generateTree(ArrayList<ArrayList<Double>> trainingSet, int depth) {
		setValues(trainingSet);
		HashSet<Integer> parent = new HashSet<Integer>();
		dTreeNode root = partition(trainingSet, parent, depth);
		return root;
	}

}
