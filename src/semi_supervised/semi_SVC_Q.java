package semi_supervised;


public class semi_SVC_Q extends SVC_Q {
		
	public semi_SVC_Q(semi_problem prob, semi_parameter param, byte[] y_) {
		super(prob, param, y_);
				
		// TODO Auto-generated constructor stub
	}
	
	

	public semi_SVC_Q(semi_problem prob, semi_parameter param, double[] y) {
		// TODO Auto-generated constructor stub
		
		this(prob, param,tranferDouble2Byte(y));
	}
	
	private static byte[] tranferDouble2Byte(double[] y) {
		byte[] y_ = null;
		for (int i = 0 , j = 0; i < y.length; i++) {
			if (y[i] == -1) {
				y_[j++] = -1;
				
			} else if (y[i] == 1) {
				y_[j++] = 1;
			}
		}
		return y_;
	}

}
