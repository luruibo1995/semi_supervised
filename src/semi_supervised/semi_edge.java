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
	public int allNode = 0;
	public int l = 0;
	public double[] y;
	public byte[] y_fake = new byte[]{1,1,1,1};
	public semi_edge_node[][] edge_node;
	private ArrayList<Double> arrayList;
	private Map<Double, Integer> map;
	public SVC_Q QMatrix;
	public  semi_edge_node[][] edgeNodeDict;
	public List<semi_edge_double_node> doubleEdgeNodesList;
	
	
	public semi_edge(semi_parameter semi_parma, semi_problem semi_prob){
		this.k = semi_parma.k;
		this.l = semi_prob.l;
		this.allNode = semi_prob.getallNode();
		this.y = semi_prob.y;
		map = new HashMap<Double, Integer>();
		arrayList = new ArrayList<Double>();
		QMatrix = new SVC_Q(semi_prob, semi_parma, y_fake);
		edgeNodeDict = new semi_edge_node[this.allNode][this.k];
		this.selectEdge(k, semi_parma, semi_prob);
		doubleEdgeNodesList = new ArrayList<semi_edge_double_node>();
		for (int row =0; row < edgeNodeDict.length; row++){
			for (int col =0; col < edgeNodeDict[row].length; col++){
				doubleEdgeNodesList.add(new semi_edge_double_node(row,edgeNodeDict[row][col].index));
			}		
		}		
		 HashSet<semi_edge_double_node> h = new HashSet<semi_edge_double_node>(doubleEdgeNodesList); 
//		 for (semi_edge_double_node node : h) {
//			for (semi_edge_double_node node2 : h) {
//				if (node.forehead == node2.backhead && node.backhead == node2.forehead) {
//					System.out.println(""+node.forehead + ""+node.backhead);
//				}
//			}
//		}
		 doubleEdgeNodesList.clear();   
		 doubleEdgeNodesList.addAll(h); 
		 System.out.println("doubleEdgeNodeList.size():"+doubleEdgeNodesList.size());
		 semi_prob.setE(doubleEdgeNodesList.size());
	}
	
	public void selectEdge(int k, semi_parameter semi_parma, semi_problem semi_prob) {
		for (int i = 0; i < this.allNode; i++) {
			if (y[i] != -2) {
				for (int j = l; j < this.allNode; j++) {
					if (i != j && y[j] == -2) {
						double e_ij = QMatrix.kernel_function(i, j);
						map.put(e_ij, j);
						arrayList.add(e_ij);
					}
				}
//				System.out.println(map);
				Collections.sort(arrayList, new descendComparator());
				for (int iter = 0;iter < k; iter++) {
					double upToNode = arrayList.get(iter);
//					System.out.println(map.get(upToNode));
					edgeNodeDict[i][iter] = new semi_edge_node(map.get(upToNode), upToNode);
				}
			} else {
				for (int j = 0; j < this.allNode; j++) {
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
//		System.out.println(o1.doubleValue());
		return (o1.doubleValue() - o2.doubleValue() < 0)? 1 : -1;
	}
	

	
}