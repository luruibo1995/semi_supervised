package semi_supervised;

import java.util.ArrayList;
import java.util.List;

public class SemiSupervisedPredict {
	
	public static double semi_predict(semi_model model, svm_node[] x, List<semi_edge_double_node> doubleEdgeNodeList, 
			double[] alphaRestrict, double[] alphaBoundless, semi_problem prob) {
		double sum = 0;
		List<Double> alphaRerstrictNotZeroList = new ArrayList<Double>();
		for (int i = 0; i < alphaRestrict.length; i++) {
			if (Math.abs(alphaRestrict[i]) > 0) {
				alphaRerstrictNotZeroList.add(alphaRestrict[i]);
			}
		}
//		System.out.println(alphaRerstrictNotZeroList.size());
//		System.out.println(model.l);
		for (int i = 0; i < alphaRerstrictNotZeroList.size(); i++) {
			sum += 2 * alphaRerstrictNotZeroList.get(i) * Kernel.k_function(x, model.SV[i], model.param);
			sum -= 2 * alphaRerstrictNotZeroList.get(i) * Kernel.k_function(model.oneSupportSV, model.SV[i], model.param);
		}
//		System.out.println(model.E);
		svm_node[][] probNodes = prob.x;
		for (int line = 0; line < model.E ; line ++) {
			int lineForeNode = doubleEdgeNodeList.get(line).forehead;
			int lineBackNode = doubleEdgeNodeList.get(line).backhead;
			sum += 2 * alphaBoundless[line] * Kernel.k_function(probNodes[lineForeNode], x, model.param);
			sum -= 2 * alphaBoundless[line] * Kernel.k_function(probNodes[lineForeNode], model.oneSupportSV, model.param);
			sum -= 2 * alphaBoundless[line] * Kernel.k_function(probNodes[lineBackNode], x, model.param);
			sum += 2 * alphaBoundless[line] * Kernel.k_function(probNodes[lineBackNode], model.oneSupportSV, model.param);
		}
		
//		System.out.println("sum:" + sum) ;
		
		return (sum > 0) ? 1: -1;
	}
	
	
	
}
