import java.util.Random;

public class DecisionTree 
{
	private static int intcount = 0;
	
	/*
	 * This function is used to construct a decision tree
	 * */
	public static treeNode constructingDT(treeNode rootNode, int[][] featLength, int[] isDone, int features, int[] indexList, treeNode parent, int h) 
	{
		if (rootNode == null) {
			rootNode = new treeNode();
			if (indexList == null || indexList.length == 0) {
				rootNode.label = funcUtil.findMaxValue(rootNode, featLength, features);
				rootNode.isLeaf = true;
				return rootNode;
			}
			if (features == 1 || mathUtil.ifAttrProcessed(isDone)) {
				rootNode.label = funcUtil.findMaxValue(rootNode, featLength, features);
				rootNode.isLeaf = true;
				return rootNode;
			}
			if (mathUtil.areAllNegative(indexList, featLength, features)) {
				rootNode.label = 0;
				rootNode.isLeaf = true;
				return rootNode;
			}
			if (mathUtil.areAllPositive(indexList, featLength, features)) {
				rootNode.label = 1;
				rootNode.isLeaf = true;
				return rootNode;
			}
		}
		if(h==0){
		rootNode = treeUtil.findAttrToConstructNode(rootNode, features, indexList , featLength, isDone);
		}
		else{
			rootNode = treeUtil.rDecisionTreeConst(rootNode, features, indexList, featLength, isDone);
		}
		rootNode.parent = parent; 
		if (rootNode.targetAttribute != -1)
			isDone[rootNode.targetAttribute] = 1;
		int leftIsDone[] = new int[isDone.length];
		int rightIsDone[] = new int[isDone.length];
		for (int j = 0; j < isDone.length; j++) {
			leftIsDone[j] = isDone[j];
			rightIsDone[j] = isDone[j]; 
		}
		rootNode.right = constructingDT(rootNode.right, featLength, rightIsDone,features,rootNode.rightIndices, rootNode,h);
		rootNode.left = constructingDT(rootNode.left, featLength, leftIsDone,features, rootNode.leftIndices, rootNode,h);
		return rootNode;
	}

	
	/* This function will return the maxValue of the Target Attribute among 
	the the examples present at the node of decision tree.*/
	private static int findMaxValueAtGivenNode(treeNode rootNode, int[][] values, int features) {
		int noOfOnes = 0;
		int noOfZeroes = 0;
		if (rootNode.leftIndices != null) {
			for (int i : rootNode.leftIndices) {
				if (values[i][features] == 1) {
					noOfOnes++;
				} else {
					noOfZeroes++;
				}
			}
		}

		if (rootNode.rightIndices != null) {
			for (int i : rootNode.rightIndices) {
				if (values[i][features] == 1) {
					noOfOnes++;
				} else {
					noOfZeroes++;
				}
			}
		}
		return noOfZeroes > noOfOnes ? 0 : 1;
	}

	
	/* This function will create and return the leaf node with label as majority 
	 value of the target_attribute among the examples at that node. */
	private static treeNode createLeafNodeWithMajorityEle(treeNode node, int[][] values, int features) {
		node.isLeaf = true;
		node.label = findMaxValueAtGivenNode(node, values, features);
		node.left = null;
		node.right = null;
		return node;
	}

	/*
	 * This function is used to build the array
	 * */
	private static void buildArray(treeNode rootNode, treeNode[] nodeArray) {
		if (rootNode == null || rootNode.isLeaf) {
			return;
		}
		intcount++;
		nodeArray[intcount] = rootNode;
		if (rootNode.left != null) {
			buildArray(rootNode.left, nodeArray);
		}
		if (rootNode.right != null) {
			buildArray(rootNode.right, nodeArray);
		}
	}

	
	/* This function counts the number of non leaf nodes and returns it. */
	private static int findNumberNonLeafNodes(treeNode rootNode) {
		if (rootNode == null || rootNode.isLeaf)
			return 0;
		else
			return (1 + findNumberNonLeafNodes(rootNode.left) + findNumberNonLeafNodes(rootNode.right));
	}
	
	/* This function counts the number of leaf nodes and returns it. */
	private static int LeafNodes(treeNode rootNode) {
		if (rootNode == null)
			return 0;
		else if (rootNode.isLeaf==true)
			return 1;
		else
			return (LeafNodes(rootNode.left) + LeafNodes(rootNode.right));
	}
	
	/*This function is to find the depth of the tree */
	private static int depthsum(treeNode rootNode,int d) {
		if (rootNode == null)
			return 0;
		else if (rootNode.isLeaf==true)
			return d;
		else
			return (depthsum(rootNode.left,d+1) + depthsum(rootNode.right,d+1));
	}
	
	/*
	 * This function is used to print the tree
	 * */
	private static void printTree(treeNode rootNode, int printLines, String[] featureNames) {
		int printLinesForThisLoop = printLines;
		if (rootNode.isLeaf) {
			System.out.println(" " + rootNode.label);
			return;
		}
		for (int i = 0; i < printLinesForThisLoop; i++) {
			System.out.print("| ");
		}
		if (rootNode.left != null && rootNode.left.isLeaf && rootNode.targetAttribute !=-1)
			System.out.print(featureNames[rootNode.targetAttribute] + "= 0 :");
		else
			if(rootNode.targetAttribute !=-1)
			System.out.println(featureNames[rootNode.targetAttribute] + "= 0 :");

		printLines++;
		printTree(rootNode.left, printLines, featureNames);
		for (int i = 0; i < printLinesForThisLoop; i++) {
			System.out.print("| ");
		}
		if (rootNode.right != null && rootNode.right.isLeaf&& rootNode.targetAttribute !=-1)
			System.out.print(featureNames[rootNode.targetAttribute] + "= 1 :");
		else
			if(rootNode.targetAttribute !=-1)
			System.out.println(featureNames[rootNode.targetAttribute] + "= 1 :");
		printTree(rootNode.right, printLines, featureNames);
	}

	/*
	 *  This function is to Post Pruning algorithm on the constructed Tree.
	 *  */		
	public static treeNode postPruneTheAlgorithm(String pathName,int K, treeNode rootNode, int[][] values, int features) 
	{
		treeNode postPrunedTree = new treeNode();
		int i = 0;int j = 0;int temp =0;
		postPrunedTree = rootNode;
		double maxAccuracy = funcUtil.measureAccuracyOverValidationSet(pathName, rootNode);
		treeNode newRoot = treeUtil.copyTree(rootNode);
		Random randomNumbers = new Random();
		int[] M = new int[K]; 
		int L = findNumberNonLeafNodes(newRoot);
		for (i = 0; i < K; i++) 
		{			
			M[i] = 4 + randomNumbers.nextInt(L-4+1);
			/*
			 * // As per the instructors suggestions ...
			 * pruning is done from 3rd level, 
			 * so the minimum node that can be pruned is 4.
			 * 
			 * */
		}
		for (i = 0; i < K; i++) 
		{
			for (j = 0; j < i; j++) 
			{
				if(M[i]>M[j])
				{
						temp=M[i];
						M[i]=M[j];
						M[j]=temp;
				}
			}
	 	}
		int noOfNonLeafNodes = findNumberNonLeafNodes(newRoot);
		treeNode nodeArray[] = new treeNode[noOfNonLeafNodes+1];
		intcount =0;
		buildArray(newRoot, nodeArray);
		for (i = 0; i < K; i++) 
		{
			if (noOfNonLeafNodes == 0)
				break;	
			if(M[i]<=L)
			{
				nodeArray[M[i]] = createLeafNodeWithMajorityEle(nodeArray[M[i]], values, features);
			}
			else
			{
				System.out.println("Number of nodes to prune are greater than the nodes present!");
				System.exit(0);
			}
		}

		double accuracy = funcUtil.measureAccuracyOverValidationSet(pathName, newRoot);
		if (accuracy > maxAccuracy) {
				postPrunedTree = newRoot;
				maxAccuracy = accuracy;
		}
		return postPrunedTree;
	}

	/*
	 * This is the main function to execute the file
	 * */
	public static void main(String[] args) {
		if (args.length != 5) {
			System.out.println("Please check the command line arguments .. should be 5");
			return;
		}
		int h=0;
		treeNode rootNode=null;
		treeNode root1=null; 
		int K = Integer.parseInt(args[0]);
		int[] featuresAndLength = funcUtil.getLengthNotherFeatures(args[1]);
		int[][] values = new int[featuresAndLength[1]][featuresAndLength[0]];
		String[] featureNames = new String[featuresAndLength[0]];
		int[] isDone = new int[featuresAndLength[0]];
		int[] indexList = new int[values.length];
		funcUtil.loadActualValues(args[1], values, featureNames, isDone, indexList, featuresAndLength[0]);
		
		if(h==0){
				rootNode = constructingDT(null, values, isDone, featuresAndLength[0] - 1, indexList, null,h);
				h++;
		}
		funcUtil.loadActualValues(args[1], values, featureNames, isDone, indexList, featuresAndLength[0]);
		if(h==1)
		{
				root1 = constructingDT(null, values, isDone, featuresAndLength[0] - 1, indexList, null,h);
		}
		int p=LeafNodes(rootNode);
		int min=depthsum(rootNode,0);
		int nonLeafNodes=findNumberNonLeafNodes(rootNode);
		System.out.println("The Average depth of Decision tree using ID3: "+min/p);
		System.out.println("Hence the total Nodes are :"+(nonLeafNodes+p));
		
		int p1=LeafNodes(root1);
		int min1=depthsum(root1,0);
		int nonLeafNodes1=findNumberNonLeafNodes(root1);
		System.out.println("The Average depth of Random Decision tree :"+min1/p1);
		System.out.println("Hence the total Nodes are :"+(nonLeafNodes1+p1));
		
		treeNode pruneTree = postPruneTheAlgorithm(args[2], K, rootNode, values, featuresAndLength[0] - 1);
		treeNode pruneTree1 = postPruneTheAlgorithm(args[2], K, root1, values, featuresAndLength[0] - 1);
		System.out.println("** ID3 DECISION TREE **");
		System.out.println("The Accuracy over Testing data for DT " + funcUtil.calAccuracyOfTestData(args[3], rootNode)*100);
		System.out.println("The Accuracy over Testing data for Pruned " + funcUtil.calAccuracyOfTestData(args[3], pruneTree)*100);
		System.out.println("** RANDOM DECISION TREE **");
		System.out.println("The Accuracy over Testing data for random DT " + funcUtil.calAccuracyOfTestData(args[3], root1)*100);
		System.out.println("The Accuracy over Testing data for Pruned random Tree " + funcUtil.calAccuracyOfTestData(args[3], pruneTree1)*100);
		if (args[4].equalsIgnoreCase("yes"))
		{
			System.out.println("** DECISION TREE USING ID3 ***");
			printTree(pruneTree, 0, featureNames);
			System.out.println("** RANDOM DECISION TREE ***");
			printTree(pruneTree1, 0, featureNames);
		}
	}
}
