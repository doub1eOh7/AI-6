import java.util.Random;

abstract class Node {
	Matrix x; 
	Matrix y;
	Node a;
	Node b;
	double splitval;
	int splitcol;
	boolean isCat;
	abstract boolean isInteriorNode();
	
}

class InteriorNode extends Node {
	
	boolean isInteriorNode()
	{
		return true;
	}
}

class LeafNode extends Node {
	double[] label;
	
	LeafNode(Matrix a, Matrix b)
	{
		this.x = a;
		this.y = b;
	}
	
	boolean isInteriorNode()
	{
		return false;
	}
	
}

public class DecisionTree extends SupervisedLearner {

	Node root;
	
	String name()
	{
		return "DecisionTree";
	}
	
	Node buildTree(Matrix features, Matrix labels)
	{
		int seed = 30000;
		Random rand = new Random(seed);
		int splitcol = rand.nextInt(features.cols());
		int randrow = rand.nextInt(features.rows());
		double splitval = features.row(randrow)[splitcol];
		Matrix af = new Matrix();
		Matrix bf = new Matrix();
		Matrix al = new Matrix();
		Matrix bl = new Matrix();

		for(int attempts = 0; attempts < 30; attempts++)
		{
			splitcol = rand.nextInt(features.cols());
			randrow = rand.nextInt(features.rows());
			splitval = features.row(randrow)[splitcol];
			af.copyMetaData(features);
			bf.copyMetaData(features);
			al.copyMetaData(labels);
			bl.copyMetaData(labels);
			
			for(int i = 0; i < features.rows(); i++)
			{
				if(features.valueCount(splitcol) == 0)
				{
					if(features.row(i)[splitcol] < splitval)
					{
						Vec.copy(af.newRow(), features.row(i));
						Vec.copy(al.newRow(), labels.row(i));
					}
					else
					{
						Vec.copy(bf.newRow(), features.row(i));
						Vec.copy(bl.newRow(), labels.row(i));
					}
				}
				else
				{
					if(features.row(i)[splitcol] == splitval)
					{
						Vec.copy(af.newRow(), features.row(i));
						Vec.copy(al.newRow(), labels.row(i));
					}
					else
					{
						Vec.copy(bf.newRow(), features.row(i));
						Vec.copy(bl.newRow(), labels.row(i));
					}
				}
			}
			if(af.rows() > 0 && bf.rows() > 0)
				break;
		}		
		if(af.rows() < 1)
		{
			LeafNode l = new LeafNode(bf, bl);
			l.label = bl.row(0);
			//System.out.println(l.toString());
			return l;
		}
		if(bf.rows() < 1)
		{
			LeafNode l = new LeafNode(af, al);
			l.label = al.row(0);
			//System.out.println(l.toString());
			return l;
		}
		
		InteriorNode n = new InteriorNode();
		n.splitval = splitval;
		n.splitcol = splitcol;
		n.x = af;
		n.y = bf;
		n.a = buildTree(af, al);
		n.b = buildTree(bf, bl);

		
		//System.out.println(n.toString());
		return n;
	}
	
	void train(Matrix features, Matrix labels)
	{
		root = buildTree(features, labels);
	}

	void predict(double[] in, double[] out)
	{
		Node n = (InteriorNode)root;
		while(n.isInteriorNode())
		{
			if(n.x.valueCount(n.splitcol) == 0)
			{
				if(in[n.splitcol] < n.splitval)
					n = n.a;
				else
					n = n.b;
			}
			else
			{
				if(in[n.splitcol] == n.splitval)
					n = n.a;
				else
					n = n.b;
			}
		}
		
		LeafNode leafnode = (LeafNode)n;
		//System.out.println(out.length);
		//System.out.println(leafnode.label.length);
		Vec.copy(out, leafnode.label);		
	}	
	
	
}
