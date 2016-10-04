
public class mathUtil {

	/* Mathematical function for log*/
	protected static double getLogarithm(double inputFraction) 
	{
		return Math.log10(inputFraction) / Math.log10(2);
	}

	/* This function is to check if all the examples have Target_Attribute as 1*/
	public static boolean areAllPositive(int[] indxList, int[][] indexValues, int inputFeat) {
		boolean returncheck = true;
		for (int i : indxList) {
			if (indexValues[i][inputFeat] == 0)
				returncheck = false;
		}
		return returncheck;
	}
	
	/* This function is to check if all the examples have Target_Attribute as 0 */
	public static boolean areAllNegative(int[] indexList, int[][] indexValues, int feats) {
		boolean iszero = true;
		for (int i : indexList) {
			if (indexValues[i][feats] == 1)
				iszero = false;
		}
		return iszero;

	}

	/* This function [ifAttrProccesed] will check if all the Attributes are processed or not */
	public static boolean ifAttrProcessed(int[] input) {
		boolean returnIfDone = true;
		for (int i : input) {
			if (i == 0)
				returnIfDone = false;
		}
		return returnIfDone;
	}
	
	
}
