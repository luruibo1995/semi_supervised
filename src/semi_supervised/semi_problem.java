package semi_supervised;

import java.util.Vector;

public class semi_problem extends svm_problem{

	public semi_problem(Vector<svm_node[]> vx, Vector<Double> vy) {
		super();
		
		this.allNode = vy.size();
		
		this.x = new svm_node[this.allNode][];
		for(int i=0;i<this.allNode;i++)
			this.x[i] = vx.elementAt(i);
		this.y = new double[this.allNode];
		for(int i=0;i<this.allNode;i++) {
			this.y[i] = vy.elementAt(i);
		}
		
		for (int i = 0; i < this.y.length; i++) {
			if (y[i] == -2) {
				for (int j = y.length - 1; j > 0; j--) {
					if (y[j] != -2 && i < j - 1) {
						double tmp = y[i];
						y[i] = y[j];
						y[j] = tmp;
						svm_node[] tmpNodes = x[i];
						x[i] = x[j];
						x[i] = tmpNodes;
					}
					if (i >= j - 1) {
						break;
					}
				}
			}
		}
		// TODO Auto-generated constructor stub
	}
	
	private  int E = 0;
	private int allNode = 0;
	public int getE() {
		return E;
	}

	public void setE(int e) {
		E = e;
	} 
	
	public int getallNode() {
		return allNode;
	}
	
	public void setallNode(int allNode) {
		this.allNode = allNode;
	}
	
	
}
