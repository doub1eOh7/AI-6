import java.util.Random;

class Node {
	
}

class InternalNode extends Node {
	
}

class LeafNode extends InternalNode {
	
}

public class DecisionTree extends SupervisedLearner {

	Node root;
	
	String name()
	{
		return "DecisionTree";
	}
	
	Node buildTree(Matrix features, Matrix labels)
	{
		Random rand = new Random();
		int splitcol = rand.nextInt(features.cols());
		int randrow = rand.nextInt(features.rows());
		double splitval = features.row(randrow)[splitcol];
		
		while()
		
		return new Node()
	}
	
	void train(Matrix features, Matrix labels)
	{
		root = buildTree(features, labels);
	}

	void predict(double[] in, double[] out)
	{
		
	}
	
	
	
}
