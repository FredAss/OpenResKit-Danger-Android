package openreskit.danger.functions;

public class RiskGroupCalculator 
{
	public static int[][] CalculateRiskGroupe()
	{
		int[][] riskMatrix = new int[5][5];
		int[] matrixValues = new int[]{3, 2, 1, 1, 1, 3, 2, 1, 1, 1, 3, 2, 2, 1, 1, 3, 2, 2, 2, 1, 3, 3, 3, 2, 2};
		
		// Fill riskMatrix with the values from matrixValues in a 5 x 5 matrix
		for(int i = 0; i < matrixValues.length; i++)
		{
			// Fill row
			for(int j = 0; j < 5; j++)
			{
				// Fill column of row
				for(int k = 0; k < 5; k++)
				{
					riskMatrix[j][k] = matrixValues[i];
					i++;
				}
			}
		}
		
		return riskMatrix;	
	}
}
