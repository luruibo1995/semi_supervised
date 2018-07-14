package semi_supervised;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formattable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import semi_supervised.svm_node;
import semi_supervised.semi_parameter;
import semi_supervised.semi_problem;

public class ReadDataAndTrain {

	private semi_parameter param;		// set by parse_command_line
	public semi_problem prob;		// set by read_problem
	
	private semi_model model;
	private String input_file_name;		// set by parse_command_line
	private int cross_validation;
	private int nr_fold;
	private semi_problem probTest;
	private semi_problem probTestPositive;
	
	public static void main(String[] args) {
		ReadDataAndTrain readDataAndTrain = new ReadDataAndTrain();
		readDataAndTrain.run(args);
		
	}
	
	public void run(String[] args){
		parse_command_line(args);
		try {
			this.prob = read_problem(this.input_file_name);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		param.k = 4;
		SemiSupervisedTrain semiSupervisedTrain = new SemiSupervisedTrain(prob, param);
		model = semiSupervisedTrain.train();
		List<Double> doubleList = new ArrayList<Double>();
//		for (int i = 0 ; i < prob.x.length ; i++ )
//			doubleList.add(SemiSupervisedPredict.semi_predict(model, prob.x[i], semiSupervisedTrain.doubleEdgeNodeList, 
//					semiSupervisedTrain.alphaRestrict, semiSupervisedTrain.alphaBoundless, prob));
		
		doubleList = SemiSupervisedPredict.semi_predict(model, prob.x, semiSupervisedTrain.doubleEdgeNodeList, 
					semiSupervisedTrain.alphaRestrict, semiSupervisedTrain.alphaBoundless, prob);
//		svm_node[] svmPredictNode = new svm_node[] {
//				new svm_node(1,-1.20755205308) , new svm_node(2, 0.223784126706), new svm_node(3, -0.875596245277), new svm_node(4, -1.03824799273)
//		};
//		-2 1:-1.20755205308 2:0.223784126706 3:-0.875596245277 4:-1.03824799273 
		System.out.println(doubleList);
		System.out.println(doubleList.size());
		int numNormalPlusOne = 0;
		int numCrackleNegativeOne = 0;
		int numWheezeNegativeOne = 0;
		for (int i = 0; i < doubleList.size(); i++) {
			if (i < 800) {
				if (doubleList.get(i) == +1.0) 
					numNormalPlusOne++;
			} else if (i < 900) {
				if (doubleList.get(i) == -1.0) 
					numCrackleNegativeOne++;
			} else if (i < 1000) {
				if (doubleList.get(i) == -1.0) {
					numWheezeNegativeOne++;
				}
			}
			
		}
		System.out.println(numNormalPlusOne);
		System.out.println("numNormalPlusOne: " + numNormalPlusOne/(double)(800));
		System.out.println(numCrackleNegativeOne);
		System.out.println("nunCrackleNegativeOne: " + numCrackleNegativeOne/(double)(100));
		System.out.println(numWheezeNegativeOne);
		System.out.println("numWheezeNegativeOne: " + numWheezeNegativeOne/(double)(100));
		
		
		
		
//		System.out.println(SemiSupervisedPredict.semi_predict(model, svmPredictNode, semiSupervisedTrain.doubleEdgeNodeList, semiSupervisedTrain.alphaRestrict, semiSupervisedTrain.alphaBoundless, prob));
		List<Double> doublePredicteDoublesNegative = new ArrayList<Double>();
		String testFileString = "E:\\JavaProject\\semi_supervised\\data\\lung_sound_v4\\foo8_test.txt";
		try {
			this.probTest = read_problem(testFileString);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
//		for (int i = probTest.l ; i < probTest.x.length ; i++ )
//			doublePredicteDoublesNegative.add(SemiSupervisedPredict.semi_predict(model, probTest.x[i], semiSupervisedTrain.doubleEdgeNodeList, 
//					semiSupervisedTrain.alphaRestrict, semiSupervisedTrain.alphaBoundless, prob));
		
		doublePredicteDoublesNegative = SemiSupervisedPredict.semi_predict(model, probTest.x, semiSupervisedTrain.doubleEdgeNodeList, 
				semiSupervisedTrain.alphaRestrict, semiSupervisedTrain.alphaBoundless, prob);
		System.out.println(doublePredicteDoublesNegative);
		System.out.println(doublePredicteDoublesNegative.size());
		int crackleNegativeOne = 0;
		int wheezeNegativeOne = 0;
		int noiseNegativeOne = 0;
		int normalPlusOne = 0;
		for (int i = 0; i < doublePredicteDoublesNegative.size(); i++) {
			if (i < 155) {
				if (doublePredicteDoublesNegative.get(i) == -1.0) {
					crackleNegativeOne++;
				}
			} else if (i < 633) {
				if (doublePredicteDoublesNegative.get(i) == -1.0) {
					wheezeNegativeOne++;
				}
			} else if (i < 645) {
				if (doublePredicteDoublesNegative.get(i) == -1.0) {
					noiseNegativeOne++;
				}
			} else if (i < 1003) {
				if (doublePredicteDoublesNegative.get(i) == +1.0) {
					normalPlusOne++;
				}
			}
				
		}
		
		System.out.println(crackleNegativeOne);
		System.out.println("crackleNegativeOne: " + crackleNegativeOne/155.0);
		System.out.println(wheezeNegativeOne);
		System.out.println("wheezeNegativeOne: " + wheezeNegativeOne/478.0);
		System.out.println(noiseNegativeOne);
		System.out.println("noiseNegativeOne: " + noiseNegativeOne/12.0);
		System.out.println(normalPlusOne);
		System.out.println("normalPlusOne: " + normalPlusOne/358.0);
		
		
		
		
//		List<Double> doublePredicteDoublesPositive = new ArrayList<Double>();
//		String testPositiveFileString = "E:\\JavaProject\\semi_supervised\\data\\Iris\\iris_test_positive.txt";
//		try {
//			this.probTestPositive = read_problem(testPositiveFileString);
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		for (int i = probTestPositive.l ; i < probTestPositive.x.length ; i++ )
//			doublePredicteDoublesPositive.add(SemiSupervisedPredict.semi_predict(model, probTestPositive.x[i], semiSupervisedTrain.doubleEdgeNodeList, 
//					semiSupervisedTrain.alphaRestrict, semiSupervisedTrain.alphaBoundless, prob));
//		System.out.println(doublePredicteDoublesPositive);
//		System.out.println(doublePredicteDoublesPositive.size());
	}
	
	private void parse_command_line(String... args) {
		param = new semi_parameter();
		// default values
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.RBF;
		param.degree = 3;
		param.gamma = 0.00313541880178     ;	// 1/num_features
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 5;
		param.C = 0.813000990239;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1; //½øÐÐshringking
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];
		cross_validation = 0;
		input_file_name = args[0];
	}
	private static double atof(String s)
	{
		double d = Double.valueOf(s).doubleValue();
		if (Double.isNaN(d) || Double.isInfinite(d))
		{
			System.err.print("NaN or Infinity in input\n");
			System.exit(1);
		}
		return(d);
	}

	private static int atoi(String s)
	{
		return Integer.parseInt(s);
	}
	
	
	private semi_problem read_problem(String input_file_name) throws IOException
	{
		BufferedReader fp = new BufferedReader(new FileReader(input_file_name));
		Vector<Double> vy = new Vector<Double>();
		Vector<svm_node[]> vx = new Vector<svm_node[]>();
		semi_problem prob = null;
		int max_index = 0;

		while(true)
		{
			String line = fp.readLine();
			if(line == null) break;

			StringTokenizer st = new StringTokenizer(line," \t\n\r\f:");

			vy.addElement(atof(st.nextToken()));
			int m = st.countTokens()/2;
			svm_node[] x = new svm_node[m];
			for(int j=0;j<m;j++)
			{
				x[j] = new svm_node();
				x[j].index = atoi(st.nextToken());
				x[j].value = atof(st.nextToken());
			}
			if(m>0) max_index = Math.max(max_index, x[m-1].index);
			vx.addElement(x);
		}
		prob = new semi_problem(vx, vy);
		int times = 0;
		for (Iterator iterator = vy.iterator(); iterator.hasNext();) {
			Double double1 = (Double) iterator.next();
			if (double1 != -2) {
				times++;
			}
			
		}
		prob.l = times;
		

		if(param.gamma == 0 && max_index > 0)
			param.gamma = 1.0/max_index;
//			param.gamma = 2.0;


		fp.close();
		return prob;
	}
}
