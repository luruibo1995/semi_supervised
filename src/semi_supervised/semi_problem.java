package semi_supervised;


public class semi_problem extends svm_problem{

	public semi_problem() {
		super();
		for (int i = 0; i < this.y.length; i++) {
			if (y[i] == -2) {
				for (int j = y.length - 1; j > 0; j++) {
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
	
	protected int E; 
}
