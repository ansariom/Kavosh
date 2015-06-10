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

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import cytoscape.Cytoscape;
import cytoscape.view.CytoscapeDesktop;
import cytoscape.view.cytopanels.CytoPanel;
import cytoscape.view.cytopanels.CytoPanelState;
import edu.lbb.kavosh.algorithm.data.Parameters;


public class StartPanel {
	private static StartPanel instance = null;
	private final MainPanel mainPanel;


	public static StartPanel getInstance() {
		if(instance == null) instance = new StartPanel();
		else JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "The " + Parameters.PLUGIN_NAME + " Plugin is already running.");
		return instance;
	}


	protected static void closePanel() {
		instance = null;
	}

	private StartPanel() {		
		mainPanel = new MainPanel();

		CytoscapeDesktop desktop = Cytoscape.getDesktop();
		CytoPanel cytoPanel = desktop.getCytoPanel(SwingConstants.WEST);

		cytoPanel.add(Parameters.PLUGIN_NAME, mainPanel.getJPanel());

		int index = cytoPanel.indexOfComponent(mainPanel.getJPanel());
		cytoPanel.setSelectedIndex(index);
		cytoPanel.setState(CytoPanelState.DOCK);
	}
}