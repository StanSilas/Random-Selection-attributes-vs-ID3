import java.util.Random;

public class treeUtil {

	/* This function gives the construction of a Random Decision Tree 
	 * This is a bonus question for 2nd assignment */
	public static treeNode rDecisionTreeConst(treeNode rootNode, int features, int[] indexList, int[][] leftRightvalues, int[] boolCheck) 
	{
		int maxLeftIndex[] = null;
		int maxRightIndex[] = null;

		Random r = new Random();
		int i = 0 + r.nextInt(19);
		while(boolCheck[i] ==1)
		{
			i = 0 + r.nextInt(19);
		}	
		if (boolCheck[i] == 0) 
		{
				double left = 0;
				double right = 0;
				int[] leftIndex = new int[leftRightvalues.length];
				int[] rightIndex = new int[leftRightvalues.length];
				for (int k = 0; k < indexList.length; k++) 
				{
					if (leftRightvalues[indexList[k]][i] == 1) 
					{
						rightIndex[(int) right++] = indexList[k];
					} else 
					{
						leftIndex[(int) left++] = indexList[k];
					}

				}
	
				int leftTempArray[] = new int[(int) left];
				for (int index = 0; index < left; index++) {
					leftTempArray[index] = leftIndex[index];
				}
				int rightTempArray[] = new int[(int) right];
				for (int index = 0; index < right; index++) {
					rightTempArray[index] = rightIndex[index];
				}
				maxLeftIndex = leftTempArray;
				maxRightIndex = rightTempArray;	
		}
		rootNode.targetAttribute = i;	
		rootNode.leftIndices = maxLeftIndex;
		rootNode.rightIndices = maxRightIndex;
		return rootNode;
	}

	/*This is the implementation of ID3 algo 
	 * 
	 * inputs: tree Node, features, index list, vaules, boolcheck
	 * 
	 * */
	protected static treeNode findAttrToConstructNode(treeNode rootNode, int features, int[] indexList , int[][] leftRightvalues, int[] boolCheck)
	{
		double maxIG = 0;	//IG = Information Gain
		int maxLeftIndex[] = null;
		int maxIndex = -1;		
		int maxRightIndex[] = null;
		for (int i=0 ; i < features; i++) {
			if (boolCheck[i] == 0) {
				double _negs = 0;
				double _pos = 0;
				double left = 0;
				double right = 0;
				double entropy = 0;
				double rightPositives = 0;
				double infoGain = 0;
				double REntropy = 0;
				double LEntropy = 0;
				int[] leftIndex = new int[leftRightvalues.length];
				int[] rightIndex = new int[leftRightvalues.length];
				double rightNegatives = 0, leftPositives = 0, leftNegatives = 0;
				for (int k = 0; k < indexList.length; k++) {
					if (leftRightvalues[indexList[k]][features] == 1) {
						_pos++;
					} else {
						_negs++;
					}
					if (leftRightvalues[indexList[k]][i] == 1) {
						rightIndex[(int) right++] = indexList[k];
						if (leftRightvalues[indexList[k]][features] == 1) {
							rightPositives++;
						} else {
							rightNegatives++;
						}
					} else {
						leftIndex[(int) left++] = indexList[k];
						if (leftRightvalues[indexList[k]][features] == 1) {
							leftPositives++;
						} else {
							leftNegatives++;
						}
					}
				}
				mathUtil mu = new mathUtil();	//object of mathUtil class
				entropy = (-1 * mathUtil.getLogarithm(_pos / indexList.length) * ((_pos / indexList.length))) + (-1 * mathUtil.getLogarithm(_negs / indexList.length) * (_negs / indexList.length));
				LEntropy = (-1 * mathUtil.getLogarithm(leftPositives / (leftPositives + leftNegatives)) * (leftPositives / (leftPositives + leftNegatives))) + (-1 * mathUtil.getLogarithm(leftNegatives / (leftPositives + leftNegatives)) * (leftNegatives / (leftPositives + leftNegatives)));
				REntropy = (-1 * mathUtil.getLogarithm(rightPositives / (rightPositives + rightNegatives)) * (rightPositives / (rightPositives + rightNegatives))) + (-1 * mathUtil.getLogarithm(rightNegatives / (rightPositives + rightNegatives)) * (rightNegatives / (rightPositives + rightNegatives)));

				if (Double.compare(Double.NaN, LEntropy) == 0) {
					LEntropy = 0;
				}
				if (Double.compare(Double.NaN, entropy) == 0) {
					entropy = 0;
				}
				if (Double.compare(Double.NaN, REntropy) == 0) {
					REntropy = 0;
				}

				infoGain = entropy
						- ((left / (left + right) * LEntropy) + (right / (left + right) * REntropy));
				if (infoGain >= maxIG) {
					maxIG = infoGain;
					maxIndex = i;
					int leftTempArray[] = new int[(int) left];
					for (int index = 0; index < left; index++) {
						leftTempArray[index] = leftIndex[index];
					}
					int rightTempArray[] = new int[(int) right];
					for (int index = 0; index < right; index++) {
						rightTempArray[index] = rightIndex[index];
					}
					maxLeftIndex = leftTempArray;
					maxRightIndex = rightTempArray;

				}
			}
		}
		
		rootNode.leftIndices = maxLeftIndex;
		rootNode.rightIndices = maxRightIndex;
		rootNode.targetAttribute = maxIndex;
		return rootNode;
	}
	
	/* This function's main purpose is to create a copy for the given tree.
	 * The return is on the root node of the tree..
	 * */
	public static treeNode copyTree(treeNode rootNode) 
	{
		if (rootNode == null)
			return rootNode;

		treeNode tempNode = new treeNode();
		tempNode.label = rootNode.label;
		tempNode.leftIndices = rootNode.leftIndices;
		tempNode.targetAttribute = rootNode.targetAttribute;
		tempNode.parent = rootNode.parent;
		tempNode.left = copyTree(rootNode.left); 		
		tempNode.isLeaf = rootNode.isLeaf;
		tempNode.rightIndices = rootNode.rightIndices;
		tempNode.right = copyTree(rootNode.right); 
		return tempNode;
	}


/*
	public static void printCurrentTree(treeNode treenode) {
		if (treenode != null) {
			System.out.println("tree.targetAttribute " + tree.targetAttribute);
			System.out.println("tree.label " + tree.label);
			System.out.println("tree.isLeaf " + tree.isLeaf);
			if (tree.leftIndices != null) {
				System.out.println("tree.leftIndices ");
				for (int i : tree.leftIndices) {
					System.out.print(i + " ");
				}
			}
			if (tree.rightIndices != null) {
				System.out.println("\ntree.rightIndices ");
				for (int i : tree.rightIndices) {
					System.out.print(i + " ");
				}
			}
			System.out.println();
			printTree(tree.left);
			printTree(tree.right);
		}
	}
*/
}
