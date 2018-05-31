package semi_supervised;


import java.text.DecimalFormat;  


public class CalculationEquations {  
    static DecimalFormat df = new DecimalFormat("0.##");  
    
    private double[][]  coefficient;
    private double[] value;
    private int n;
    /*** 
     * ���������ͳ����б仯���㷨 
     *  
     * @param value 
     *            ��Ҫ���������� 
     * @return ����Ľ�� 
     */  
    public static double[][] mathDeterminantCalculation(double[][] value)  
            throws Exception {  
        // ���������������2ʱ  
        for (int i = 0; i < value.length; i++) {  
            // �������Խ���λ�õ���ֵ�Ƿ���0�����������Ը�������е��������ҵ�һ�в�Ϊ0�Ľ��е���  
            if (value[i][i] == 0) {  
                value = changeDeterminantNoZero(value, i, i);  
            }  
            for (int j = 0; j < i; j++) {  
                // �ÿ�ʼ������е���λΪ0����Ϊ������ʽ  
                // ���Ҫ�������Ϊ0����Լ�����һ��λ�ã�������ʡȥ�˼���  
                if (value[i][j] == 0) {  
                    continue;  
                }  
                // ���Ҫ��Ҫ���������0��������һ�н��е���  
                if (value[j][j] == 0) {  
                    double[] temp = value[i];  
                    value[i] = value[i - 1];  
                    value[i - 1] = temp;  
                    continue;  
                }  
                double ratio = -(value[i][j] / value[j][j]);  
                value[i] = addValue(value[i], value[j], ratio);  
            }  
        }  
        return value;  
    }  
  
    /*** 
     * ��i��֮ǰ��ÿһ�г���һ��ϵ����ʹ�ô�i�еĵ�i��֮ǰ�������û�Ϊ0 
     *  
     * @param currentRow 
     *            ��ǰҪ������� 
     * @param frontRow 
     *            i��֮ǰ�ı������� 
     * @param ratio 
     *            Ҫ���Ե�ϵ�� 
     * @return ��i��i��֮ǰ�����û�Ϊ0����µ��� 
     */  
    public static double[] addValue(double[] currentRow, double[] frontRow,  
            double ratio) throws Exception {  
        for (int i = 0; i < currentRow.length; i++) {  
            currentRow[i] += frontRow[i] * ratio;  
            currentRow[i] = Double.parseDouble(df.format(currentRow[i]));  
        }  
        return currentRow;  
    }  
  
    /** 
     * ָ���е�λ���Ƿ�Ϊ0�����ҵ�һ����Ϊ0��λ�õ��н���λ�õ��������û���򷵻�ԭ����ֵ 
     *  
     * @param determinant 
     *            ��Ҫ���������ʽ 
     * @param line 
     *            Ҫ�������� 
     * @param row 
     *            Ҫ�жϵ��� 
     */  
    public static double[][] changeDeterminantNoZero(double[][] determinant,  
            int line, int row) throws Exception {  
        for (int j = line; j < determinant.length; j++) {  
            // �����е���  
            if (determinant[j][row] != 0) {  
                double[] temp = determinant[line];  
                determinant[line] = determinant[j];  
                determinant[j] = temp;  
                return determinant;  
            }  
        }  
        return determinant;  
    }  
  
    /** 
     * ��ϵ������ͷ���ֵ�ľ�����кϲ���������� 
     *  
     * @param coefficient 
     *            ϵ������ 
     * @param value 
     *            ����ֵ 
     * @return ������� 
     */  
    public static double[][] transferMatrix(double[][] coefficient,  
            double[] value) {  
        double[][] temp = new double[coefficient.length][coefficient[0].length + 1];  
        if (coefficient.length != value.length) {  
            return temp;  
        }  
        // ������ֵ��ӵ�ϵ��������  
        for (int i = 0; i < coefficient.length; i++) {  
            for (int j = 0; j < coefficient[0].length; j++) {  
                temp[i][j] = coefficient[i][j];  
            }  
        }  
        for (int i = 0; i < value.length; i++) {  
            temp[i][temp[i].length - 1] = value[i];  
        }  
        return temp;  
    }  
  
    /** 
     * �����Ч���������������еĸ��� 
     *  
     * @param value 
     *            ��Ҫ�������� 
     * @return �����еĸ��� 
     */  
    public static int effectiveMatrix(double[][] value) {  
        for (int i = value.length - 1; i > -1; i--) {  
            for (int j = 0; j < value[i].length; j++) {  
                if (value[i][j] != 0) {  
                    return i + 1;  
                }  
            }  
        }  
        return 0;  
    }  
  
    /** 
     * ���������н��ʱ����㷽����Ľ� 
     *  
     * @param mathMatrix 
     *            �������������� 
     * @return ������Ľ� 
     */  
    private static double[] calculationResult(double[][] mathMatrix) {  
        // �н�ʱ������ĸ������ڷ������δ֪��  
        double[] result = new double[mathMatrix.length];  
        for (int i = mathMatrix.length - 1; i > -1; i--) {  
            double temp = 0;  
            for (int j = mathMatrix[i].length; j > 0; j--) {  
                // ��һ��Ϊ���̵Ľ⣬��Ҫ���⸳ֵ����ʱ����  
                if (mathMatrix[i][j - 1] != 0) {  
                    if (j == mathMatrix[i].length) {  
                        temp = mathMatrix[i][j - 1];  
                    } else if (j - 1 > -1 && result[j - 1] != 0) {  
                        temp -= mathMatrix[i][j - 1] * result[j - 1];  
                    } else {  
                        result[i] = temp / mathMatrix[i][j - 1];  
                        continue;  
                    }  
                }  
            }  
        }  
        return result;  
    }  
  
    public CalculationEquations (double[][] coefficient, double[] value) {
    	this.coefficient = coefficient;
    	this.value = value;
    	this.n = value.length;
    }
    
    public double[][] getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(double[][] coefficient) {
		this.coefficient = coefficient;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public void setValue(double[] value) {
		this.value = value;
	}

	public double[] getResult() throws Exception {
    	double[][] mathMatrix = mathDeterminantCalculation(transferMatrix(this.coefficient, this.value));
    	int checkMatrixRow = effectiveMatrix(mathMatrix);
    	if (n != checkMatrixRow) {
			System.out.println("error equatation!");
			System.exit(1);
			return null;
		} else {
			double[] result = calculationResult(mathMatrix);
			return result;
		}
    	
		
    }
    
  
}  