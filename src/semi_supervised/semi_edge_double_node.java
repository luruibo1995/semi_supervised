package semi_supervised;

public class semi_edge_double_node {
	int forehead;
	int backhead;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + backhead + forehead;
		result = prime * result + forehead + backhead;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		semi_edge_double_node other = (semi_edge_double_node) obj;
//		if (backhead != other.backhead)
//			return false;
//		if (forehead != other.forehead)
//			return false;
		
		if (backhead == other.backhead && forehead == other.forehead) {
			return true;
		}
		
		if (forehead == other.backhead && backhead == other.forehead) {
			return true;
		}
		
		return false;
	}
	public semi_edge_double_node(int forehead, int backhead) {
		super();
		this.forehead = forehead;
		this.backhead = backhead;
	}
	
	
		
	
}
