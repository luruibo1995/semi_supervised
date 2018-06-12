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
	private semi_problem prob;		// set by read_problem
	private semi_model model;
	private String input_file_name;		// set by parse_command_line
	private int cross_validation;
	private int nr_fold;
	
	public static void main(String[] args) {
		ReadDataAndTrain readDataAndTrain = new ReadDataAndTrain();
		readDataAndTrain.run(args);
		
	}
	
	public void run(String[] args){
		parse_command_line(args);
		try {
			read_problem();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		param.k = 4;
		SemiSupervisedTrain semiSupervisedTrain = new SemiSupervisedTrain(prob, param);
		model = semiSupervisedTrain.train();
		List<Double> doubleList = new ArrayList<Double>();
		for (int i = prob.l ; i < prob.x.length ; i++ )
			doubleList.add(SemiSupervisedPredict.semi_predict(model, prob.x[i], semiSupervisedTrain.doubleEdgeNodeList, 
					semiSupervisedTrain.alphaRestrict, semiSupervisedTrain.alphaBoundless, prob));
//		svm_node[] svmPredictNode = new svm_node[] {
//				new svm_node(1,-1.20755205308) , new svm_node(2, 0.223784126706), new svm_node(3, -0.875596245277), new svm_node(4, -1.03824799273)
//		};
//		-2 1:-1.20755205308 2:0.223784126706 3:-0.875596245277 4:-1.03824799273 
		System.out.println(doubleList);
//		System.out.println(SemiSupervisedPredict.semi_predict(model, svmPredictNode, semiSupervisedTrain.doubleEdgeNodeList, semiSupervisedTrain.alphaRestrict, semiSupervisedTrain.alphaBoundless, prob));
		
	}
	
	private void parse_command_line(String... args) {
		param = new semi_parameter();
		// default values
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.RBF;
		param.degree = 3;
		param.gamma = 10;	// 1/num_features
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 5;
		param.C = 30;
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
	
	
	private void read_problem() throws IOException
	{
		BufferedReader fp = new BufferedReader(new FileReader(input_file_name));
		Vector<Double> vy = new Vector<Double>();
		Vector<svm_node[]> vx = new Vector<svm_node[]>();
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
	}
}
