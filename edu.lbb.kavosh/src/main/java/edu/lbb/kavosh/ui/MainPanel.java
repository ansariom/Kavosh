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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import cytoscape.Cytoscape;
import cytoscape.view.CytoscapeDesktop;
import cytoscape.view.cytopanels.CytoPanel;
import cytoscape.view.cytopanels.CytoPanelState;
import edu.lbb.kavosh.algorithm.data.Parameters;

/**
 * 
 * Main panel of the cytoplugin (inputs)
 * 
 * @author fsikora
 * 
 */

public class MainPanel {
	private final JPanel jPanel = new JPanel();
	private final DecimalFormat decFormat = new DecimalFormat();
	private final JFormattedTextField motifSizeTextField = new JFormattedTextField(
			decFormat);
	private final JFormattedTextField randSizeTextField = new JFormattedTextField(
			decFormat);
	private final JFormattedTextField freqLimitTextField = new JFormattedTextField(decFormat);
	private final JFormattedTextField zScoreLimitTextField = new JFormattedTextField(decFormat);

	public MainPanel() {
		decFormat.setParseIntegerOnly(true);

		// this.startPanel = startPanel;
		jPanel.setLayout(new BorderLayout());

		JPanel topPanel = createTopPanel();
		JPanel bottomPanel = createBottomPanel();

		jPanel.add(topPanel, BorderLayout.NORTH);
		jPanel.add(bottomPanel, BorderLayout.SOUTH);
	}

	public JPanel getJPanel() {
		return jPanel;
	}

	private JPanel createTopPanel() {
		JPanel topPanel = new JPanel();

		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

		// topPanel.add(createPatternPanel());
		// topPanel.add(createFastaPanel());
		// topPanel.add(createBlastPanel());
		topPanel.add(createOptionPanel());
		topPanel.add(createButtonsOptionPanel());

		return topPanel;
	}

	private Component createOptionPanel() {
		JPanel jPanel = new JPanel(null);

		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		jPanel.add(createDelInsTimeOutPanel());
		// jPanel.add(createFirstSolutionPanel());
		jPanel.setBorder(BorderFactory.createTitledBorder("Options"));

		return jPanel;
	}

	private JPanel createDelInsTimeOutPanel() {
		JPanel delInsPanel = new JPanel();
		delInsPanel.setLayout(new GridLayout(4, 1, 10, 0));

		JLabel motifSizeLabel = new JLabel("Motif Size");
		motifSizeTextField.setColumns(3);
		motifSizeTextField
		.setMaximumSize(motifSizeTextField.getPreferredSize());
		motifSizeTextField.addPropertyChangeListener("value",
				new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Number n = (Number) evt.getNewValue();
				if (n != null && n.intValue() > 0) {
					Parameters.getInstance().setMotifSize(n.intValue());
				} else {
					JOptionPane.showMessageDialog(Cytoscape.getDesktop(),
							"You Should enter valid number", "Try again", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		String nInsTip = "Size of motif that would be found.";
		motifSizeTextField.setToolTipText(nInsTip);
		motifSizeTextField.setText(Integer.toString(Parameters.getInstance()
				.getMotifSize()));
		
		JPanel nInsPanel = new JPanel(null);
		nInsPanel.setLayout(new BoxLayout(nInsPanel, BoxLayout.X_AXIS));
		nInsPanel.setToolTipText(nInsTip);
		nInsPanel.add(Box.createHorizontalGlue());
		nInsPanel.add(motifSizeLabel);
		nInsPanel.add(motifSizeTextField);

		
		JLabel randomSizeLabel = new JLabel("Random Graph Count");
		randSizeTextField.setColumns(3);
		randSizeTextField
		.setMaximumSize(randSizeTextField.getPreferredSize());
		randSizeTextField.addPropertyChangeListener("value",
				new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Number n = (Number) evt.getNewValue();
				if (n != null && n.intValue() > 0  && n.intValue() <= 1000) {
					Parameters.getInstance().setRandSize(n.intValue());
				} else {
					JOptionPane.showMessageDialog(Cytoscape.getDesktop(),
							"You Should enter valid number", "Try again", JOptionPane.ERROR_MESSAGE);
					JFormattedTextField source = (JFormattedTextField) evt.getSource();
					source.setText(String.valueOf(Parameters.getInstance().getRandSize()));
				}
			}
		});
		String rInsTip = "Size of random graph for calculate zscore.";
		randSizeTextField.setToolTipText(rInsTip);
		randSizeTextField.setText(Integer.toString(Parameters.getInstance()
				.getRandSize()));
		
		JPanel nDelPanel = new JPanel(null);
		nDelPanel.setLayout(new BoxLayout(nDelPanel, BoxLayout.X_AXIS));
		nDelPanel.setToolTipText(rInsTip);
		nDelPanel.add(Box.createHorizontalGlue());
		nDelPanel.add(randomSizeLabel);
		nDelPanel.add(randSizeTextField);
		
		JLabel freqLimitLabel = new JLabel("Minimum Frequency (%)");
		freqLimitTextField.setColumns(3);
		freqLimitTextField
		.setMaximumSize(freqLimitTextField.getPreferredSize());
		freqLimitTextField.addPropertyChangeListener("value",
				new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Number n = (Number) evt.getNewValue();
				if (n != null && n.intValue() >= 0  && n.intValue() <= 100) {
					Parameters.getInstance().setFreqLimit(n.intValue());
				} else {
					JOptionPane.showMessageDialog(Cytoscape.getDesktop(),
							"You Should enter valid number", "Try again", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		String fInsTip = "Minimum Frequency to filtering the results.";
		freqLimitTextField.setToolTipText(fInsTip);
		freqLimitTextField.setText(Integer.toString(Parameters.getInstance()
				.getFreqLimit()));
		
		JPanel fPanel = new JPanel(null);
		fPanel.setLayout(new BoxLayout(fPanel, BoxLayout.X_AXIS));
		fPanel.setToolTipText(fInsTip);
		fPanel.add(Box.createHorizontalGlue());
		fPanel.add(freqLimitLabel);
		fPanel.add(freqLimitTextField);
		
		JLabel zScoreLimitLabel = new JLabel("Minimum ZScore");
		zScoreLimitTextField.setColumns(3);
		zScoreLimitTextField
				.setMaximumSize(zScoreLimitTextField.getPreferredSize());
		zScoreLimitTextField.addPropertyChangeListener("value",
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent evt) {
						Number n = (Number) evt.getNewValue();
						if (n != null && n.intValue() >= -1000  && n.intValue() <= 1000) {
							Parameters.getInstance().setzScoreLimit(n.intValue());
						} else {
							JOptionPane.showMessageDialog(Cytoscape.getDesktop(),
									"You Should enter valid number", "Try again", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
		String zInsTip = "Minimum ZScore to filtering the results.";
		zScoreLimitTextField.setToolTipText(zInsTip);
		zScoreLimitTextField.setText(Integer.toString(Parameters.getInstance()
				.getzScoreLimit()));

		JPanel zPanel = new JPanel(null);
		zPanel.setLayout(new BoxLayout(zPanel, BoxLayout.X_AXIS));
		zPanel.setToolTipText(zInsTip);
		zPanel.add(Box.createHorizontalGlue());
		zPanel.add(zScoreLimitLabel);
		zPanel.add(zScoreLimitTextField);

		// big panel with grid
		delInsPanel.add(nInsPanel);
		delInsPanel.add(nDelPanel);
		delInsPanel.add(fPanel);
		delInsPanel.add(zPanel);

		return delInsPanel;
	}

	private Component createButtonsOptionPanel() {
		JPanel panel = new JPanel();

		JButton loadDefault = new JButton("Load Default");
		loadDefault.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// value load default
				Parameters.getInstance().setDefault();
				// graphic update
				updateOptionsValues();
			}
		});

		panel.add(loadDefault);

		return panel;
	}

	/**
	 * Update the graphic value of options from the parameters values
	 */
	private void updateOptionsValues() {
		motifSizeTextField.setText(Integer.toString(Parameters.getInstance()
				.getMotifSize()));
		randSizeTextField.setText(Integer.toString(Parameters.getInstance()
				.getRandSize()));
		freqLimitTextField.setText(Integer.toString(Parameters.getInstance()
				.getFreqLimit()));
		zScoreLimitTextField.setText(Integer.toString(Parameters.getInstance()
				.getzScoreLimit()));
	}

	private JPanel createBottomPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());

		// start GraphMotif
		JButton launchButton = new JButton("Submit");
		launchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Launching ....");
				new LaunchAction();
			}

		});
		// Close the tab
		JButton closeButton = new JButton("Close " + Parameters.PLUGIN_NAME);
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CytoscapeDesktop desktop = Cytoscape.getDesktop();
				CytoPanel cytoPanel = desktop.getCytoPanel(SwingConstants.EAST);
				cytoPanel.setState(CytoPanelState.HIDE);
				cytoPanel = desktop.getCytoPanel(SwingConstants.WEST);
				cytoPanel.remove(getJPanel());
				StartPanel.closePanel();
			}

		});

		panel.add(launchButton);
		panel.add(closeButton);

		return panel;
	}

}
