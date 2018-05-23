package semi_supervised;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class semi_edge {
	public int k = 0;
	public int l = 0;
	public double[] y;
	public byte[] y_fake = new byte[]{1,1,1,1};
	public semi_edge_node[][] edge_node;
	private ArrayList<Double> arrayList;
	private Map<Double, Integer> map;
	public SVC_Q QMatrix;
	public static semi_edge_node[][] edgeNodeDict;
	public List<semi_edge_double_node> doubleEdgeNodesList;
	public semi_edge(semi_parameter semi_parma, semi_problem semi_prob){
		this.k = semi_parma.k;
		this.l = semi_prob.l;
		this.y = semi_prob.y;
		map = new HashMap<Double, Integer>();
		arrayList = new ArrayList<Double>();
		QMatrix = new SVC_Q(semi_prob, semi_parma, y_fake);
		this.selectEdge(k, semi_parma, semi_prob);
		doubleEdgeNodesList = new ArrayList<semi_edge_double_node>();
		for (int row =0; row < edgeNodeDict.length; row++){
			for (int col =0; col < edgeNodeDict[row].length; col++){
				doubleEdgeNodesList.add(new semi_edge_double_node(row,col));
			}		
		}		
		 HashSet<semi_edge_double_node> h = new HashSet<semi_edge_double_node>(doubleEdgeNodesList);   
		 doubleEdgeNodesList.clear();   
		 doubleEdgeNodesList.addAll(h); 
		 semi_prob.E = doubleEdgeNodesList.size();
	}
	
	public void selectEdge(int k, semi_parameter semi_parma, semi_problem semi_prob) {
		for (int i = 0; i < l; i++) {
			if (y[i] != -2) {
				for (int j = 0; j < l; j++) {
					if (i != j && y[j] == -2) {
						double e_ij = QMatrix.kernel_function(i, j);
						map.put(e_ij, j);
						arrayList.add(e_ij);
					}
				}
				Collections.sort(arrayList, new descendComparator());
				for (int iter = 0;iter < k; iter++) {
					double upToNode = arrayList.get(iter);
					edgeNodeDict[i][iter] = new semi_edge_node(map.get(upToNode), upToNode);
				}
			} else {
				for (int j = 0; j < l; j++) {
					if (i != j) {
						double e_ij = QMatrix.kernel_function(i, j);
						map.put(e_ij, j);
						arrayList.add(e_ij);
					}
				}
				Collections.sort(arrayList,Collections.reverseOrder());
				for (int iter = 0;iter < k; iter++) {
					double upToNode = arrayList.get(iter);
					edgeNodeDict[i][iter] = new semi_edge_node(map.get(upToNode), upToNode);
				}
			}
			map.clear();
			arrayList.clear();
		}
	}
	
	
}

class descendComparator implements Comparator<Double>{

	@Override
	public int compare(Double o1, Double o2) {
		// TODO Auto-generated method stub
		
		return new Double(o2 - o1).intValue();
	}
	

	
}