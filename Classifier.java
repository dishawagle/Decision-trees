package decisiontree;

import java.util.ArrayList;

public class Classifier {

	public void classify(ArrayList<ArrayList<Double>> testVectors, dTreeNode root) {
		int truePos = 0;
		int trueNeg = 0;
		int falsePos = 0;
		int falseNeg = 0;
		
		ArrayList<Double> labels = new ArrayList<Double>();
		double error = 0;
		for (ArrayList<Double> vector : testVectors) {
			dTreeNode p = root;
			while (!p.isLeaf) {//while this node does not become leaf
				int att = p.att;//get the splitting attribute 
				double val = vector.get(att);//get the value for that attribute for current test vector
				double threshold=p.threshold;
				if(val>threshold)
					p = p.children.get(2.0);//traverse the tree according to the given value till leaf is reached
				else
					p=p.children.get(1.0);
			}
			//p.att represents label for leaf node (p is leaf now), if tree label is not equal to test label, error ++
			if (p.att != vector.get(0))
				error++;
			labels.add((double)p.att); //add label to labels of test vectors.
			
			//Confusion Matrix
			int A = p.att;
			double B = vector.get(0);
			if(A==1&&B==1)
				truePos++;
			else if(A==0&&B==0)
				trueNeg++;
			else if(A==0&&B==1)
				falseNeg++;
			else if(A==1&&B==0)
				falsePos++;
		}
		System.out.println("\n\nConfusion Matrix\n\n");
		System.out.println("\tPredicted YES \t Predicted NO\n");
		System.out.println("Actual YES\t"+truePos+"\t"+falseNeg+"\n");
		System.out.println("Actual NO\t"+falsePos+"\t"+trueNeg+"\n");
		
		
		double total = testVectors.size();
		double percent = (total-error) / total;
		percent *= 100;
		double pe=error/total;
		pe *=100;
		System.out.println("Accuracy = " + percent + "%");
		System.out.println("Error = " + pe + "%");
		
		
	}
	
	public void classifyMultiway(ArrayList<ArrayList<Double>> testVectors, dTreeNode root) {
		int truePos = 0;
		int trueNeg = 0;
		int falsePos = 0;
		int falseNeg = 0;
		
		ArrayList<Double> labels = new ArrayList<Double>();
		double error = 0;
		for (ArrayList<Double> vector : testVectors) {
			dTreeNode p = root;
			while (!p.isLeaf) {//while this node does not become leaf
				int att = p.att;//get the splitting attribute 
				double val = vector.get(att);//get the value for that attribute for current test vector
				p = p.children.get(val);//traverse the tree according to the given value till leaf is reached
			}
			//p.att represents label for leaf node (p is leaf now), if tree label is not equal to test label, error ++
			if (p.att != vector.get(0))
				error++;
			labels.add((double)p.att); //add label to labels of test vectors.
			
			//Confusion Matrix
			int A = p.att;
			double B = vector.get(0);
			if(A==1&&B==1)
				truePos++;
			else if(A==0&&B==0)
				trueNeg++;
			else if(A==0&&B==1)
				falseNeg++;
			else if(A==1&&B==0)
				falsePos++;
		}
		System.out.println("\n\nConfusion Matrix\n\n");
		System.out.println("\tPredicted YES \t Predicted NO\n");
		System.out.println("Actual YES\t"+truePos+"\t"+falseNeg+"\n");
		System.out.println("Actual NO\t"+falsePos+"\t"+trueNeg+"\n");
		double total = testVectors.size();
		double percent = (total-error) / total;
		percent *= 100;
		double pe=error/total;
		pe *=100;
		System.out.println("Accuracy = " + percent + "%");
		System.out.println("Error = " + pe + "%");
		
	}
}
