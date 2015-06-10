/**
@project Lbb_Kavosh
@author Mitra Ansari
@date Jun 12, 2011
 **/
package edu.lbb.kavosh.ui;

import java.util.HashMap;
import java.util.List;

import cytoscape.CyEdge;
import cytoscape.CyNetwork;
import cytoscape.logger.CyLogger;
import cytoscape.task.Task;
import cytoscape.task.TaskMonitor;
import edu.lbb.kavosh.algorithm.JKavosh;
import edu.lbb.kavosh.algorithm.data.Graph;
import edu.lbb.kavosh.algorithm.data.Parameters;
import edu.lbb.kavosh.data.common.Motif;

public class KavoshTask implements Task {

	protected static CyLogger logger = CyLogger.getLogger(KavoshTask.class);
	private TaskMonitor taskMonitor;
	private long programStartTime;
	private CyNetwork network;
	private List<Motif> results;
	private HashMap<String, Integer> nodeLabelHash = new HashMap<String, Integer>(); 
	public static HashMap<Integer, String> inverseNodeLabelHash = new HashMap<Integer, String>(); 

	public List<Motif> getResults() {
		return results;
	}

	public KavoshTask(CyNetwork network) {
		this.network = network;
	}

	@Override
	public String getTitle() {
		return "Running " + Parameters.PLUGIN_NAME;
	}

	@Override
	public void halt() {
		Parameters.getInstance().interrupt(true);
	}

	@Override
	public void run() {
		System.out.println("Running Kavosh");
		taskMonitor.setStatus("Initialization..");
		taskMonitor.setPercentCompleted(-1);
		JKavosh jKavosh = new JKavosh(buildGraph());
		jKavosh.startKavosh();
//		Kavosh kavosh = new Kavosh(buildGraph());
		// kavosh.startKavosh();
		results = jKavosh.getMotifList();
		taskMonitor.setPercentCompleted(100);
	}

	private Graph buildGraph() {
		Graph graph = null;
		int count = 1 , source, target;
		try {
			graph = new Graph(network.getNodeCount(), Parameters.getInstance()
					.getMotifSize(), false);
			List<CyEdge> edges = network.edgesList();
			for (CyEdge edge : edges) {
				String src = edge.getSource().getIdentifier();
				String tgt = edge.getTarget().getIdentifier();
				if (src.equalsIgnoreCase(tgt))
					continue;
				Integer s = nodeLabelHash.get(src);
				if (s == null) {
					source = count++;
					nodeLabelHash.put(src, source);
					inverseNodeLabelHash.put(source, src);
				} else
					source = s.intValue();
				Integer t = nodeLabelHash.get(tgt);
				if (t == null) {
					target = count++;
					nodeLabelHash.put(tgt, target);
					inverseNodeLabelHash.put(target, tgt);
				} else
					target = t.intValue();
				graph.addEdge(source, target);
			}
			graph.finalizeG();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return graph;
	}

	@Override
	public void setTaskMonitor(TaskMonitor taskMonitor)
			throws IllegalThreadStateException {
		this.taskMonitor = taskMonitor;
	}

	public void updatePercent(float percent) {
		// when batch..
		if (taskMonitor != null) {
			taskMonitor.setPercentCompleted((int) percent);
		}
	}

	public void setStartTime() {
		programStartTime = System.currentTimeMillis();
	}

	public long getStartTime() {
		return programStartTime;
	}

}
