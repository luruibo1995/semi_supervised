package semi_supervised;
public class svm_node implements java.io.Serializable
{
	public int index;
	public double value;
	public svm_node(int index, double value) {
		super();
		this.index = index;
		this.value = value;
	}
	public svm_node() {
		this.index = 0;
		this.value = 0;
	}
	
}
