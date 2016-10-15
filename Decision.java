//Assignment Partners: avrsriva@iu.edu dmwagle@iu.edu

package decisiontree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Decision {
	public static boolean skip=false;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Training set filepath");
		String trainfileName = sc.nextLine();
		System.out.println("Enter Testing set filepath");
		String testfileName = sc.nextLine();
		System.out.println("Enter Depth of Tree");
		int depth = Integer.parseInt(sc.nextLine());
		
		//default options, method=2(binary split), method=1(multiway split)
		int method = 2;
		int display = 0;
		String seperator = " ";
		
		if(args.length>0){
				for(String arg : args){
				if(arg.equalsIgnoreCase("comma"))
					seperator=",";
				if(arg.equalsIgnoreCase("tab"))
					seperator="\t";
				if(arg.equalsIgnoreCase("display"))
					display=1;
				if(arg.equalsIgnoreCase("multiway"))
					method=1;
				if(arg.equalsIgnoreCase("skipcolumn"))
					skip=true;
			}
		}
		
		
		
		// Get training set

		ArrayList<ArrayList<Double>> trainingSet = getData(trainfileName,seperator);

		// Get test set

		ArrayList<ArrayList<Double>> testSet = getData(testfileName, seperator);
		if (method == 2) {
			DecisionTree tree = new DecisionTree();
			Classifier classifier = new Classifier();
			// Get Tree from Training Vectors
			dTreeNode root = tree.generateTree(trainingSet, depth);
			if (display == 1)
				root.display(0, 0, 0);
			// Classify Test Vectors
			classifier.classify(testSet, root);
		} else if (method == 1) {
			MultiwayTree tree = new MultiwayTree();
			Classifier classifier = new Classifier();

			// Get Tree from Training Vectors
			dTreeNode root = tree.generateTree(trainingSet, depth);
			if (display == 1)
				root.displaymultiway(0, 0);
			// Classify Test Vectors
			classifier.classifyMultiway(testSet, root);
		}sc.close();
	}

	private static ArrayList<ArrayList<Double>> getData(String fileName, String seperator) {
		ArrayList<ArrayList<Double>> Vectors = new ArrayList<ArrayList<Double>>();
		String line = null;
		FileReader fReader = null;
		try {
			fReader = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader bufferedReader = new BufferedReader(fReader);
		try {
			while ((line = bufferedReader.readLine()) != null) {
				
				ArrayList<Double> temp = new ArrayList<Double>();
				String[] numbers = line.split(seperator);//by default separator is space
				int sz=numbers.length;
				if(skip) 
					sz-=1;
				for (int j = 0; j < sz; j++)
					if (numbers[j] != null && !numbers[j].isEmpty()) {
						temp.add(Double.parseDouble(numbers[j]));
					}
				Vectors.add(temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Vectors;//The data is stored in Vectors
	}
	
}
