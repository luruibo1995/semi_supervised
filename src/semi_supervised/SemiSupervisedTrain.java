package semi_supervised;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.xml.transform.Templates;

import semi_supervised.Solver.SolutionInfo;
import semi_supervised.svm.decision_function;


public class SemiSupervisedTrain {
	
	private semi_problem prob ;
	private semi_parameter param ;
	private int l ;
	private int E ;
	svm_node[] supportVec;
	public double[] alphaRestrict ;
	public double[] alphaBoundless ;
	
	byte[] y_fake ;
	SVC_Q  RestrictMatrix ;
	
	semi_edge semiEdge ;
	List<semi_edge_double_node> doubleEdgeNodeList ;
	semi_edge_node[][] edgeNodeDict;
 	
	public  semi_model train() {
		solve_c_svc(this.prob, this.param, this.alphaRestrict, this.param.C, this.param.C);
		
		this.startSolving();
		double[] alphaRestrict_ = this.alphaRestrict.clone();
		for (int i = 0; i < alphaRestrict_.length ; i++) {
			if (Math.abs(alphaRestrict_[i]) > 0 ) {
				this.supportVec = this.prob.x[i];
				break;
			}
		}
		
		return semi_train(this.prob, this.param);
		
		
	}
	
	public semi_model semi_train(semi_problem prob, semi_parameter param) {
		semi_model model = new semi_model();
		model.param = param;
		
		model.nr_class = 2;
		model.label = null;
		model.nSV = null;
		model.probA = null;
		model.probB = null;
		model.sv_coef = new double[1][];
//		decision_function f = svm.svm_train_one(prob, param, 0, 0);
//		decision_function f = new decision_function();
//		f.alpha = this.alphaRestrict;
		int nSv = 0;
		for (int index = 0; index < prob.l; index++) {
			if (Math.abs(this.alphaRestrict[index]) > 0) {
				nSv++;
			}
		}
		model.oneSupportSV = supportVec;
		model.l = nSv;
//		System.out.println(nSv);
		model.E = this.E;
		model.SV = new svm_node[nSv][];
		int j = 0;
		for (int i = 0;i < prob.l; i ++) {
			if (Math.abs(this.alphaRestrict[i]) > 0) {
				model.SV[j++] = prob.x[i];
			}
		}
		return model;
	}
	
	private void startSolving() {
		double[][]  coefficient = this.getCoefficient(this.RestrictMatrix, this.doubleEdgeNodeList, this.edgeNodeDict);
//		System.out.println(coefficient[2][4]);
		double[] value = this.getValue(this.alphaRestrict, this.prob, this.RestrictMatrix, this.doubleEdgeNodeList);
		try {
			this.alphaBoundless = new CalculationEquations(coefficient, value).getResult();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("linear equatation error");
		}
		finally {
//			System.out.println(Arrays.toString(alphaRestrict));
			System.out.println(Arrays.toString(alphaBoundless));
		}
		
		
		
	}
	public SemiSupervisedTrain(semi_problem prob,semi_parameter param) {
		this.prob = prob;
		this.param = param;
		this.l = prob.l;
		
		this.alphaRestrict = new double[l];
		
		
		this.y_fake = new byte[l];
		this.RestrictMatrix = new SVC_Q(prob, param, y_fake);
		
		this.semiEdge = new semi_edge(param, prob);
		this.doubleEdgeNodeList = semiEdge.doubleEdgeNodesList;
		this.edgeNodeDict = semiEdge.edgeNodeDict;
		this.E = prob.getE();
		this.alphaBoundless =  new double[E];
	}
	

	
	private double[][] getCoefficient(SVC_Q RestrictMatrix, List<semi_edge_double_node> doubleEdgeNodeList , semi_edge_node[][] edgeNodeDict) {
			double[][] coefficient = new double[E][E];
			double tmp = 0;
			for (int i = 0; i < coefficient.length; i++) {
				int iforeNode = doubleEdgeNodeList.get(i).forehead;
				int ibackNode = doubleEdgeNodeList.get(i).backhead;
				for (int j = 0; j < coefficient[i].length; j++) {
					int jforeNode = doubleEdgeNodeList.get(j).forehead;
					int jbackNode = doubleEdgeNodeList.get(j).backhead;
					coefficient[i][j] = RestrictMatrix.kernel_function(iforeNode, jforeNode) - RestrictMatrix.kernel_function(iforeNode, jbackNode)
							-RestrictMatrix.kernel_function(ibackNode, jforeNode) + RestrictMatrix.kernel_function(ibackNode, jbackNode);
	//				System.out.println("+++++++++++++++++:"+coefficient[i][j]);
					if (i == j) {
						tmp = returnEij(iforeNode, ibackNode, edgeNodeDict);
						if (tmp == 0) {
							System.out.println("Not found Node!");
						}
						coefficient[i][j] += 0.25 / tmp; 
						
					}
	//				System.out.println(coefficient[i][j]);
				}
			}
			return coefficient;
		}

	private double returnEij(int foreNode, int backNode, semi_edge_node[][] edgeNodeDict) {
		double tmp = 0;
		for (int x = 0; x < edgeNodeDict[foreNode].length; x++) {
			if (edgeNodeDict[foreNode][x].index == backNode) {
				tmp = edgeNodeDict[foreNode][x].e_ij;
				break;
			}
		}
		if (tmp == 0) {
			for (int x = 0; x < edgeNodeDict[backNode].length; x++) {
				if (edgeNodeDict[backNode][x].index == foreNode) {
					tmp = edgeNodeDict[backNode][x].e_ij;
					break;
				}
			}
		}
		return tmp;
	}
	
	
	
	private double[] getValue(double[] alphaRestrict, semi_problem prob, SVC_Q RestrictMatrix, List<semi_edge_double_node> doubleEdgeNodeList) {
//		double[] y_ = prob.y.clone();
		double[] value = new double[this.E];
		
		for (int row = 0; row < value.length; row++) {
			int rowforeNode = doubleEdgeNodeList.get(row).forehead;
			int rowbackNode = doubleEdgeNodeList.get(row).backhead;
			for (int col = 0; col < this.l; col++) {
				double littleVal = ((RestrictMatrix.kernel_function(col, rowforeNode) - RestrictMatrix.kernel_function(col, rowbackNode)) * alphaRestrict[col]);
				value[row] -= littleVal;
//				System.out.println("value:"+value[col]);
			}
		}
		
		return value;
		
	}
	

	private static void solve_c_svc(semi_problem prob, svm_parameter param, double[] alpha, 
			double Cp, double Cn) {
		int l = prob.l;
		double[] p_restrict = new double[l];
		byte[] y = new byte[l];
		Random rand = new Random();
		int i;
		double mSum = 0;
//		int n = (int) (param.nu * prob.l);
//		for (i = 0; i < n; i++) {
//			alpha[i] = 1;
//		}
//		if (n < prob.l) {
//			alpha[n] = param.nu * l - n;
//		}
//		for (i = n+1;  i< l; i++) {
//			alpha[i] = 0;
//		}
		for (i = 0; i < l; i++) {
			if (prob.y[i] > 0)
				y[i] = +1;
			else
				y[i] = -1;
			p_restrict[i] = -y[i];
//			alpha[i] = 1;
//		}
//		alpha[39] = 0;
		
			if (i < l / 2) {
				alpha[i] = rand.nextDouble() * Cp;
			} else {
				alpha[i] = rand.nextDouble()* 0.5 * Cp;
			}

////			alpha[i] = 1.0;
////			alpha[i] = 1.0;
			mSum += alpha[i] * y[i];
		}
		
		for ( i = 0; i < l; i++) {
			alpha[i] /= mSum;
		}
		double sum = 0;
		for (int j = 0; j < l; j++) {
			sum += y[j] * alpha[j];
		}
		System.out.println(sum);
		SolutionInfo sInfo = new SolutionInfo();
		Solver s = new Solver();
		s.Solve(l, new SVC_Q(prob, param, y), p_restrict, y, alpha, Cp, Cn, param.eps, sInfo, param.shrinking);

		double sum_alpha = 0;
		for (i = 0; i < l; i++)
			sum_alpha += alpha[i];
		
		
		if (Cp == Cn)
			svm.info("nu = " + sum_alpha / (Cp * prob.l) + "\n");

		for (i = 0; i < l; i++) {
			System.out.println("alpha["+i+"]: "+alpha[i]);
			alpha[i] *= y[i];
	
		}
		System.out.println("sumn of all a is " + sum_alpha);
		
	}
	
	
}
