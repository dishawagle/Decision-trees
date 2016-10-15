package decisiontree;

import java.util.*;
public class dTreeNode {
	//This is the decision attrbute associated with this decision node, as explained below.
	int att;
	//Each Decision Node may point to two (binary) or more (multiway) other decision nodes or leaf nodes.
	//To decide which Node to traverse next, we look at the value of the attribute in the current decision Node.
	//Depending on this value, we traverse to the next node.
	//Thus, we create a hashmap out of the values associated with the next node to traverse.
	HashMap<Double,dTreeNode> children=new HashMap<Double,dTreeNode>();
	public double threshold=0;
	boolean isLeaf;
	public dTreeNode(int d, boolean b){
		att=d;
		isLeaf=b;
	}
	public void displaymultiway(int tab,double val){
		System.out.println();
		for(int i=0;i<tab;i++){
			System.out.print("    ");
		}
		if(val!=0)
			System.out.print(val);
		System.out.print("-->");
		System.out.print(att);
		if(children.isEmpty()){
			System.out.print("leaf");
		}
		else{
			for(Map.Entry<Double,dTreeNode> child : children.entrySet()){
				child.getValue().displaymultiway(tab+1,child.getKey());
			}
		}
	}
	
	public void display(int tab,double val, double k){
		System.out.println();
		for(int i=0;i<tab;i++){
			System.out.print("    ");
		}
		if(val==1)
			System.out.print("<"+k);
		if(val==2)
			System.out.print(">"+k);
		System.out.print("-->");
		System.out.print(att);
		if(children.isEmpty()){
			System.out.print("leaf");
		}
		else{
			for(Map.Entry<Double,dTreeNode> child : children.entrySet()){
				child.getValue().display(tab+1,child.getKey(),threshold);
			}
		}
	}
}
