import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class funcUtil {
	
	/*
	 * This function is used to measure the accuracy over validation set..
	 * 
	 * */
	protected static double measureAccuracyOverValidationSet(String inputPathName, treeNode newRootNode) {
		int[][] _validate_d_set = buildValidationSet(inputPathName);
		double d_count = 0;
		for (int i = 1; i < _validate_d_set.length; i++) {
			d_count += checkIfClassifiedCorrectly(_validate_d_set[i], newRootNode);
		}
		return d_count / _validate_d_set.length;
	}

	
	/*
	 * This method will construct and return the validation set from the file path
	 * specified.
	 */
	protected static int[][] buildValidationSet(String inputPathName) {
		int[] _getfeaturesLength = getLengthNotherFeatures(inputPathName);
		String _inputFile = inputPathName;	//this is the input csv Files
		int[][] _validate_d_set = new int[_getfeaturesLength[1]][_getfeaturesLength[0]];
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		try {
			br = new BufferedReader(new FileReader(_inputFile));
			int i = 0;
			int count = 0;
			while ((line = br.readLine()) != null) {
				String[] lineParameters = line.split(cvsSplitBy);
				int j = 0;
				if (count == 0) {
					count++;
					continue;
				} else {
					for (String lineParameter : lineParameters) {
						_validate_d_set[i][j++] = Integer.parseInt(lineParameter);
					}
				}
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return _validate_d_set;
	}

	/*
	 * This function will verify if the given Example is correctly classified
	 * as per the Constructed Tree.
	 */
	protected static int checkIfClassifiedCorrectly(int[] inputsetValues, treeNode newRootNode) {
		int getindex = newRootNode.targetAttribute;
		int boolCoorectClassified = 0;
		treeNode testNode = newRootNode;
		while (testNode.label == -1) {
			if (inputsetValues[getindex] == 1) {
				testNode = testNode.right;
			} else {
				testNode = testNode.left;
			}
			if (testNode.label == 1 || testNode.label == 0) {
				if (inputsetValues[inputsetValues.length - 1] == testNode.label) {
					boolCoorectClassified = 1;
					break;
				} else {
					break;
				}
			}
			getindex = testNode.targetAttribute;
		}
		return boolCoorectClassified;
	}

	/*
	 * This function is used to get the length and other features from the training data..
	 * 
	 * */
	protected static int[] getLengthNotherFeatures(String inputFile) {
		BufferedReader br = null;
		int count = 0;
		int[] featuresAndLength = new int[2];
		String line = "";
		String cvsSplitBy = ",";
		try {

			br = new BufferedReader(new FileReader(inputFile));
			while ((line =  br.readLine()) != null) {
				if (count == 0) {
					String[] country = line.split(cvsSplitBy);
					featuresAndLength[0] = country.length;
				}
				count++;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		featuresAndLength[1] = count;
		return featuresAndLength;
	}

	/*
	 * This function is to check the accuracy over the test data
	 * */
	protected static double calAccuracyOfTestData(String path, treeNode rootNode) {
		double boolaccuracy = 0;
		int[][] testData = loadTestingData(path);
		for (int i = 0; i < testData.length; i++) {
			boolaccuracy += funcUtil.checkIfClassifiedCorrectly(testData[i], rootNode);
		}
		return boolaccuracy / testData.length;
	}

	
	private static int[][] loadTestingData(String pathName) {
		int[] featuresAndLength = funcUtil.getLengthNotherFeatures(pathName);
		String csvFile = pathName;
		int[][] validationSet = new int[featuresAndLength[1]][featuresAndLength[0]];
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		try {
			br = new BufferedReader(new FileReader(csvFile));
			int i = 0;
			int count = 0;
			while ((line = br.readLine()) != null) {
				String[] lineParameters = line.split(cvsSplitBy);
				int j = 0;
				if (count == 0) {
					count++;
					continue;
				}
				else {
					for (String lineParameter : lineParameters) {
						validationSet[i][j++] = Integer.parseInt(lineParameter);
					}
				}
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return validationSet;
	}
	
	protected static void loadActualValues(String pathName, int[][] featuresvalues, String[] featureNames, int[] isDone,
			int[] indexList, int features) {
		String csvFile = pathName;
		BufferedReader bufferedReader = null;
		String line = "";
		String cvsSplitBy = ",";
		for (int k = 0; k < features; k++) {
			isDone[k] = 0;
		}
		int k = 0;
		for (k = 0; k < featuresvalues.length; k++) {
			indexList[k] = k;
		}
		try {

			bufferedReader = new BufferedReader(new FileReader(csvFile));
			int i = 0;
			while ((line = bufferedReader.readLine()) != null) {
				String[] lineParameters = line.split(cvsSplitBy);
				int j = 0;
				if (i == 0) {
					for (String lineParameter : lineParameters) {
						featureNames[j++] = lineParameter;
					}
				}

				else {

					for (String lineParameter : lineParameters) {
						featuresvalues[i][j++] = Integer.parseInt(lineParameter);
					}
				}
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	/* This function will return max value of Target_Attribute among the examples available at the given splitting attribute */
	public static int findMaxValue(treeNode rootNode, int[][] values, int features) {
		int noOfOnes = 0;
		int noOfZeroes = 0;
		if (rootNode.parent == null) {
			int i = 0;
			for (i = 0; i < values.length; i++) {
				if (values[i][features] == 1) {
					noOfOnes++;
				} else {
					noOfZeroes++;
				}
			}
		} else {
			for (int i : rootNode.parent.leftIndices) {
				if (values[i][features] == 1) {
					noOfOnes++;
				} else {
					noOfZeroes++;
				}
			}

			for (int i : rootNode.parent.rightIndices) {
				if (values[i][features] == 1) {
					noOfOnes++;
				} else {
					noOfZeroes++;
				}
			}
		}
		return noOfZeroes > noOfOnes ? 0 : 1;

	}

}

