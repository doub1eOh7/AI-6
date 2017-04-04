import java.util.Random;
public class RandomForest extends SupervisedLearner{
	
	DecisionTree[] trees;
	int size = 0;
	
	RandomForest(int size)
	{
		this.size = size;
	}
	
	String name()
	{
		return "RandomForest";
	}
	
	void train(Matrix features, Matrix labels)
	{
		trees = new DecisionTree[size];
		Random r = new Random(1);
		for(int i = 0; i < size; i++)
		{
			Matrix sampleFeatures = new Matrix();
			Matrix sampleLabels = new Matrix();
			sampleFeatures.copyMetaData(features);
			sampleLabels.copyMetaData(labels);

			for(int j = 0; j < features.rows(); j++)
			{
				Vec.copy(sampleFeatures.newRow(), features.row(r.nextInt(features.rows())));
				Vec.copy(sampleLabels.newRow(), labels.row(r.nextInt(labels.rows())));
			}
			addTreeToForest(i, sampleFeatures, sampleLabels);
		}
	}
	
	void addTreeToForest(int index, Matrix features, Matrix labels)
	{
		DecisionTree tree = new DecisionTree();
		tree.train(features, labels);
		trees[index] = tree;
	}
	
	void predict(double[] in, double[] out)
	{
		double[][] votes = new double[trees.length][1];
		for(int i = 0; i < trees.length; i++)
		{
			trees[i].predict(in, votes[i]);
		}
		for(int j = 0; j < votes[0].length; j++)
		{
			double eachVote[] = new double[size];
			for(int i = 0; i < size; i++)
			{
				eachVote[i] = votes[i][j];
			}
			out[j] = getPopularElement(eachVote);
		}
		
	}
	
	double getPopularElement(double[] a)
	{
	  int count = 1, tempCount;
	  double popular = a[0];
	  double temp = 0;
	  for (int i = 0; i < (a.length - 1); i++)
	  {
	    temp = a[i];
	    tempCount = 0;
	    for (int j = 1; j < a.length; j++)
	    {
	      if (temp == a[j])
	        tempCount++;
	    }
	    if (tempCount > count)
	    {
	      popular = temp;
	      count = tempCount;
	    }
	  }
	  return popular;
	}
	
}
