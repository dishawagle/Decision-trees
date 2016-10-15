package decisiontree;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

public class MultiwayTree {
	
	public static ArrayList<TreeSet<Double>> DiscreteValuesforAttribute = new ArrayList<TreeSet<Double>>();//Treeset has been used for storing the values only because the values can be directly stored in a sorted manner and is not used in the building of the decision tree.
	
	/**
	 * Calculates the gain for an attribute
	 * G(A,S)
	 * 
	 * @param attribute
	 * @param vectors
	 * @return
	 */
	private double gain(int attribute, ArrayList<ArrayList<Double>> vectors) {
		TreeSet<Double> values = DiscreteValuesforAttribute.get(attribute);
		double attributeEntropy = 0;
		double totalEntropy = 0;
		double gain = 0;
		double totalSize = (double) vectors.size();
		for (double value : values) {
			ArrayList<ArrayList<Double>> subset = new ArrayList<ArrayList<Double>>();

			for (ArrayList<Double> vector : vectors) {
				if (vector.get(attribute) == value)
					subset.add(vector);
			}
			double subsetSize = (double) subset.size();
			attributeEntropy += (subsetSize / totalSize) * (entropy(subset));
		}
		totalEntropy = entropy(vectors);
		gain = totalEntropy - attributeEntropy;
		return gain;
	}

	/**
	 * H(S)
	 * 
	 * @param vectors
	 * @return
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
 * Sets the discrete values for the attributes
 * @param Vectors
 */
	private void setValues(ArrayList<ArrayList<Double>> Vectors) {
		for (int i = 0; i < Vectors.get(0).size(); i++) {
			TreeSet<Double> temp1 = new TreeSet<Double>();
			for (int j = 0; j < Vectors.size(); j++) {
				double h = Vectors.get(j).get(i);
				temp1.add(h);

			}
			DiscreteValuesforAttribute.add(temp1);
		}
	}

	private dTreeNode partition(ArrayList<ArrayList<Double>> vectors, HashSet<Integer> parent, int depth) {
		dTreeNode currentNode = new dTreeNode(0, false);
		double totalEntropy = entropy(vectors);
		if (totalEntropy == 0) {
			if (vectors.get(0).get(0) == 1) {
				currentNode.att = 1;
				currentNode.isLeaf = true;
				return currentNode;
			} else {
				currentNode.att = 0;
				currentNode.isLeaf = true;
				return currentNode;
			}
		} else if (depth == 0) {
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
			double[] gainAttributes = new double[vectors.get(0).size()];
			double max = 0;
			int splittingAttribute = 0;
			for (int i = 1; i < vectors.get(0).size(); i++) {
				if (parent.contains(i))
					continue;
				gainAttributes[i] = gain(i, vectors); //determine attribute with maximum gain as splitting attribute
				if (gainAttributes[i] > max) {
					max = gainAttributes[i];
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
				for (double disvalue : DiscreteValuesforAttribute.get(splittingAttribute)) {
					ArrayList<ArrayList<Double>> subset = new ArrayList<ArrayList<Double>>();
					for (ArrayList<Double> vector : vectors) {
						if (vector.get(splittingAttribute) == disvalue) {
							subset.add(vector);
						}
					}
					HashSet<Integer> newSet = new HashSet<Integer>();
					newSet = (HashSet<Integer>) parent.clone();
					newSet.add(splittingAttribute);//if parent contains the attribute i, i.e the attribute has already been used, continue, do not use it again
					dTreeNode d = partition(subset, newSet, depth-1);
					currentNode.children.put(disvalue, d);
				}
				return currentNode;
			}
		}
	}

	public dTreeNode generateTree(ArrayList<ArrayList<Double>> trainingSet, int depth) {
		setValues(trainingSet);
		HashSet<Integer> set = new HashSet<Integer>();
		dTreeNode root = partition(trainingSet, set, depth);
		return root;
	}

}
