/**
@project Lbb_Kavosh
@author Mitra Ansari
@date Jun 6, 2011
 **/
package edu.lbb.kavosh.data.common;

import giny.view.NodeView;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

import cytoscape.CyEdge;
import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;
import cytoscape.data.Semantics;
import cytoscape.ding.DingNetworkView;
import cytoscape.layout.algorithms.GridNodeLayout;
import ding.view.DGraphView;
import edu.lbb.kavosh.ui.KavoshTask;

public class SubGraph implements ISubGraph {
	private int[] nodes;
	private long id;
	private CyNetwork subNetwork;
	private ImageIcon image;
	private List<CyNode> cyNodes = new ArrayList<CyNode>();
	private List<CyEdge> cyEdges = new ArrayList<CyEdge>(); 

	public SubGraph(long id, int[] nodes) {
		this.id = id;
		this.nodes = nodes;
	}

	public void createNet(CyNetwork network) {
		List<CyNode> nList = new ArrayList<CyNode>();
		for (int i = 0; i < nodes.length; i++) {
			CyNode cyNode = Cytoscape.getCyNode(KavoshTask.inverseNodeLabelHash.get(nodes[i]));
			nList.add(cyNode);
		}
		cyNodes.addAll(nList);
//		List<CyEdge> edgeList = new ArrayList<CyEdge>();
//		for (int i = 0; i < nList.size(); i++) {
//			CyNode node0 = nList.get(i);
//			for (int j = 0; j < nList.size(); j++) {
//				CyNode node1 = nList.get(j);
//				if (network.edgeExists(node0.getRootGraphIndex(),
//						node1.getRootGraphIndex())) {
//					CyEdge edge = Cytoscape.getCyEdge(node0, node1,
//							Semantics.INTERACTION, "", true);
//					edgeList.add(edge);
//				}
//			}
//		}
//		cyEdges.addAll(edgeList);
//		subNetwork = Cytoscape.createNetwork(nList, edgeList, "Motif_Net",
//				network, false);
//		LaunchAction.loadedNetworks.add(subNetwork);
//		DGraphView view = createGraphView();
//		image = createImage(view);

	}
	
	

	public List<CyNode> getCyNodes() {
		return cyNodes;
	}

	public List<CyEdge> getCyEdges() {
		return cyEdges;
	}

	public int[] getNodes() {
		return nodes;
	}

	public ImageIcon getImage() {
		return image;
	}

	public void setNodes(int[] nodes) {
		this.nodes = nodes;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	private DGraphView createGraphView() {
		DingNetworkView view = new DingNetworkView(subNetwork, "tmp");

		final int[] nodes = subNetwork.getNodeIndicesArray();
		System.out.println(Arrays.toString(nodes));
		for (int i = 0; i < nodes.length; i++) {
			view.addNodeView(nodes[i]);
		}

		final int[] edges = subNetwork.getEdgeIndicesArray();
		for (int i = 0; i < edges.length; i++) {
			view.addEdgeView(edges[i]);
		}

		for (Iterator<NodeView> in = view.getNodeViewsIterator(); in.hasNext();) {
			NodeView nv = in.next();

			String label = nv.getNode().getIdentifier();
			nv.getLabel().setText(label);
			nv.setWidth(40);
			nv.setHeight(40);
			nv.setShape(NodeView.ELLIPSE);
			nv.setUnselectedPaint(Color.RED);
			nv.setBorderPaint(Color.BLACK);
		}
		GridNodeLayout layout = new GridNodeLayout();
		view.applyLayout(layout);

		return view;
	}

	public CyNetwork getSubNetwork() {
		return subNetwork;
	}

	private ImageIcon createImage(DGraphView view) {
		int width = 30;
		int height = 30;

		view.getCanvas().setSize(width, height);
		view.fitContent();
		Image i1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		final Graphics2D g = (Graphics2D) i1.getGraphics();
		view.getCanvas().paint(g);

		return new ImageIcon(view.getCanvas(DGraphView.Canvas.NETWORK_CANVAS)
				.getImage());
	}

	@Override
	public String toString() {
		String str = "[";
		for (int i = 0; i < nodes.length; i++) {
			str += " " + nodes[i];
		}
		str += "]";
		return str;
	}
}
