//    This file is part of GraMoFoNe.
//
//    Copyright (C) 2009 Guillaume BLIN Florian SIKORA Stephane VIALETTE 
//    http://igm.univ-mlv.fr/AlgoB/gramofone/
//
//    GraMoFoNe is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    GraMoFoNe is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    The complete GNU General Public Licence Notice can be found as the
//    `COPYING' file in the root directory.

package edu.lbb.kavosh.ui;

import java.util.HashSet;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.task.ui.JTaskConfig;
import cytoscape.task.util.TaskManager;
import cytoscape.view.CytoscapeDesktop;
import cytoscape.view.cytopanels.CytoPanel;
import cytoscape.view.cytopanels.CytoPanelState;
import edu.lbb.kavosh.algorithm.data.Parameters;
import edu.lbb.kavosh.data.common.Motif;

public class LaunchAction {

	/**
	 * Launch action of the plugin
	 */

	public static HashSet<CyNetwork> loadedNetworks = new HashSet<CyNetwork>();
	public static ApplyVizMapper vizMapper = new ApplyVizMapper();

	/**
	 * Launch the plugin with the LPB solver
	 * 
	 * @param pattern
	 */
	public LaunchAction() {
		// verifications..
		// destroy all the networks loaded, except the network loaded here..
		for (CyNetwork network : Cytoscape.getNetworkSet()) {
			if (loadedNetworks.contains(network)) {
				Cytoscape.destroyNetwork(network);
				Cytoscape.destroyNetworkView(network);
			}
		}
		loadedNetworks.clear();
		if (Cytoscape.getNetworkSet().isEmpty()) {
			JOptionPane.showMessageDialog(Cytoscape.getDesktop(),
					"You must have a network loaded\nto run this plugin.",
					"Analysis Interrupted", JOptionPane.WARNING_MESSAGE);
			return;
		}
		CyNetwork network = Cytoscape.getNetworkSet().iterator().next();
		System.out.println("network : " + network.getIdentifier());
		if (network == null) {
			JOptionPane.showMessageDialog(Cytoscape.getDesktop(),
					"You must have a network loaded\nto run this plugin.",
					"Analysis Interrupted", JOptionPane.WARNING_MESSAGE);
			return;
		}
		// needs a network of at least 1 node
		if (network.getNodeCount() < 1) {
			JOptionPane.showMessageDialog(Cytoscape.getDesktop(),
					"You must have a network loaded\nto run this plugin.",
					"Analysis Interrupted", JOptionPane.WARNING_MESSAGE);
			return;
		}

		vizMapper.apply(Cytoscape.getNetworkView(network.getIdentifier()),
				network);

		KavoshTask myTask = new KavoshTask(network);
		JTaskConfig jTaskConfig = new JTaskConfig();
		jTaskConfig.setOwner(Cytoscape.getDesktop());
		jTaskConfig.displayCloseButton(true);

		jTaskConfig.displayCancelButton(true);

		jTaskConfig.displayStatus(true);
		jTaskConfig.setAutoDispose(true);

		// Execute Task in New Thread; pops open JTask Dialog Box.
		TaskManager.executeTask(myTask, jTaskConfig);

		// ArrayList<int[]> results = myTask.getResults();
		List<Motif> results = myTask.getResults();

		if (results.size() > 0) {
			CytoscapeDesktop desktop = Cytoscape.getDesktop();
			CytoPanel cytoPanel = desktop.getCytoPanel(SwingConstants.EAST);
			MotifResultsPanel resultPanel = new MotifResultsPanel(results,
					network);
			cytoPanel.add(
					Parameters.PLUGIN_NAME + " results < " + results.size()
							+ " > ", resultPanel.getJPanel());

			JPanel test = new JPanel();
			test.add(new JTextArea("Results"));
			cytoPanel.add(test);

			// focus the result panel
			int index = cytoPanel.indexOfComponent(resultPanel.getJPanel());
			cytoPanel.setSelectedIndex(index);
			cytoPanel.setState(CytoPanelState.DOCK);

		}

	}

}
