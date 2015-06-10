/**
@project Lbb_Kavosh
@author Mitra Ansari
@date Jun 6, 2011
 **/
package edu.lbb.kavosh.data.common;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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
import edu.lbb.kavosh.ui.LaunchAction;
import giny.view.NodeView;

public class Motif implements ISubGraph {
	private char[] nodes;
	private List<SubGraph> subGraphs = new ArrayList<SubGraph>();
	private long id;
	private CyNetwork subNetwork;
	private ImageIcon image;
	private double zScore;
	private double frequency;
	private long count;
	private long totalMotifCount;
	private int motifSize;
	private long number;

	public Motif(long id, char[] nodes, long count) {
		this.id = id;
		this.nodes = nodes;
		this.count = count;
		motifSize = (int) Math.sqrt(nodes.length);
	}
	
	public StringBuilder getInformations(int rowIndex) {
		//computeNbDel(parentMyGraph, constraints, model);

		StringBuilder sb = new StringBuilder();
		sb.append("Subgraph_Count").append(getCount());
		sb.append("\nFrequency = ").append(getFrequency());
		sb.append("\nZ-score = ").append(getzScore());

		return sb;
	}

	public List<SubGraph> getSubGraphs() {
		return subGraphs;
	}

	public void setTotalMotifCount(long totalMotifCount) {
		this.totalMotifCount = totalMotifCount;
		frequency = (count / this.totalMotifCount) * 100;
	}
	
	public long getNumber() {
		return number;
	}

	public void setNumber(long number) {
		this.number = number;
	}
	
	public void showView() {
		Cytoscape.createNetworkView(subNetwork);
	}

	public void createNet(CyNetwork network) {
		int motifSize = (int) Math.sqrt(nodes.length);
		List<CyNode> nList = new ArrayList<CyNode>();
		for (int i = 1; i <= motifSize; i++) {
			CyNode cyNode = Cytoscape.getCyNode("M" + i, true);
			nList.add(cyNode);
		}
		List<CyEdge> edgeList = new ArrayList<CyEdge>();
		for (int i = 0; i < motifSize; i++) {
			CyNode node0 = nList.get(i);
			for (int j = 0; j < motifSize; j++) {
				if (nodes[(i * motifSize) + j] == '1') {
					CyNode node1 = nList.get(j);
					CyEdge edge = Cytoscape.getCyEdge(node0, node1,
							Semantics.INTERACTION, "pp",true, true);
					edgeList.add(edge);
				}
			}
		}
		subNetwork = Cytoscape.createNetwork(nList, edgeList, "Motif_" + id,
				network, false);
		LaunchAction.loadedNetworks.add(subNetwork);
		DGraphView view = createGraphView();
		image = createImage(view);

	}
	

	public double getzScore() {
		return zScore;
	}

	public void setzScore(double zScore) {
		this.zScore = zScore;
	}

	public double getFrequency() {
		
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public ImageIcon getImage() {
		return image;
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	private DGraphView createGraphView() {
		DingNetworkView view = new DingNetworkView(subNetwork, "tmp");

		final int[] nodes = subNetwork.getNodeIndicesArray();
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
		int width = 70;
		int height = 70;
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
//		String str = "|";
//		
//		for (int i = 0; i < motifSize; i++) {
//			for (int j = 0; j < motifSize; j++) {
//				str += nodes[(i * motifSize) + j] + " ";
//			}
//			str = str.substring(0, str.length() -1 );
//			str += "|\n|";
//		}
//		str = str.substring(0, str.length() -1 );
		String str = "";
		
		for (int i = 0; i < motifSize; i++) {
			for (int j = 0; j < motifSize; j++) {
				if (i == j)
					continue;
				str += nodes[(i * motifSize) + j] + " ";
			}
		}
		str = str.substring(0, str.length() -1 );
		return str;
	}
}
