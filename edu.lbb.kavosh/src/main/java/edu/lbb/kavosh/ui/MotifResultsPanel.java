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

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.View;

import cytoscape.CyEdge;
import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;
import cytoscape.actions.GinyUtils;
import cytoscape.actions.ZoomSelectedAction;
import cytoscape.view.CyNetworkView;
import cytoscape.view.CytoscapeDesktop;
import cytoscape.view.cytopanels.CytoPanel;
import cytoscape.view.cytopanels.CytoPanelState;
import edu.lbb.kavosh.algorithm.data.Parameters;
import edu.lbb.kavosh.data.common.Motif;
import edu.lbb.kavosh.data.common.SubGraph;

/**
 * Right panel, ouput panel, results
 * 
 * @author fsikora
 * 
 */

public class MotifResultsPanel {
	private final JPanel jPanel = new JPanel(null);
	RadioButtonUI ui = new RadioButtonUI();
	int pageSize = 10;
	Box box = Box.createHorizontalBox();
	private final CyNetwork network;
	private final CyNetworkView networkView;
	private int currentPageIndx = 1;
	// private final MyPlugin parent;
	private final JLabel imageCorrespondance = new JLabel();
	TableModel model;
	TableRowSorter sorter;

	public MotifResultsPanel(List<Motif> results, CyNetwork network) {
		this.network = network;
		this.networkView = Cytoscape.getNetworkView(network.getIdentifier());
		// this.parent = parent;

		jPanel.setLayout(new BorderLayout());

		JPanel drawResultPanel = createDrawResultPanel(results);
		JPanel correspondancePanel = createCorrespondancePanel();
		JPanel bottomPanel = createBottomPanel(results);

		correspondancePanel.setPreferredSize(new Dimension(100, 200));

		JSplitPane horizontalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				drawResultPanel, correspondancePanel);
		horizontalSplit.setDividerLocation(0.5);

		JPanel resultsImgName = new JPanel(null);
		resultsImgName
				.setLayout(new BoxLayout(resultsImgName, BoxLayout.Y_AXIS));

		resultsImgName.add(drawResultPanel);
		resultsImgName.add(correspondancePanel);

		jPanel.add(resultsImgName, BorderLayout.CENTER);

		jPanel.add(bottomPanel, BorderLayout.SOUTH);
	}

	public JPanel getJPanel() {
		return jPanel;
	}

	/**
	 * Draw the panel with all results
	 * 
	 * @param results
	 * @return
	 */
	private JPanel createDrawResultPanel(final List<Motif> results) {
		// JPanel drawResultPanel = new JPanel(new GridLayout(2, 1));
		JPanel drawResultPanel = new JPanel(new BorderLayout());

		// table model
		model = createResultTableModel(results);
		sorter = new TableRowSorter<TableModel>(model);
		JTable jTable = new JTable(model);
		// jTable.setAutoCreateRowSorter(true);
		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Renderer of cells
		jTable.setDefaultRenderer(StringBuilder.class,
				new DetailsResultRenderer());

		// listener when click on rows..
		jTable.addMouseListener(createResultTableMouseListener(jTable, results));

		jTable.setRowSorter(sorter);
		showPages(10, 1);

		JScrollPane tableScrollPane = new JScrollPane(jTable);

		drawResultPanel.add(tableScrollPane, BorderLayout.CENTER);
		drawResultPanel.add(box, BorderLayout.SOUTH);

		return drawResultPanel;
	}

	private MouseListener createResultTableMouseListener(final JTable jTable,
			final List<Motif> results) {
		return new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				// left clic simulation
				if (SwingUtilities.isRightMouseButton(e)) {
					try {
						Robot r = new Robot();
						r.mousePress(InputEvent.BUTTON1_MASK);
						r.mouseRelease(InputEvent.BUTTON1_MASK);
					} catch (AWTException e1) {
						e1.printStackTrace();
					}
				}

				// table cell
				int row = jTable.getSelectedRow();
				if (row >= 0) {
					final Motif subGraph = results.get(jTable.convertRowIndexToModel(row));
					subGraph.createNet(network);
					LaunchAction.vizMapper.apply(Cytoscape.getNetworkView(subGraph.getSubNetwork().getIdentifier()), network);
					imageCorrespondance.setIcon(subGraph.getImage());
					System.out.println(subGraph.getSubGraphs().size());
					highlightMotifInNetwork(subGraph.getSubGraphs());
					// zoom to the selected nodes !
					new ZoomSelectedAction().actionPerformed(null);

					// left clic
					if (e.getButton() == MouseEvent.BUTTON1) {
					}
					// right click
					else if (e.getButton() == MouseEvent.BUTTON3) {
						JPopupMenu popupMenu = new JPopupMenu("Export");

						// export subnetwork
						JMenuItem exportItem = new JMenuItem(
								"Export in a new network");
						exportItem.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								subGraph.showView();
							}
						});

						popupMenu.add(exportItem);
						popupMenu.show(e.getComponent(), e.getX(), e.getY());
					} else {
						System.err.println("Unkown button..");
					}
				}
			}
		};
	}

	/**
	 * Highlight the solutions nodes in the network
	 * 
	 * @param subGraph
	 */
	private void highlightMotifInNetwork(List<SubGraph> subGraphList) {
		GinyUtils.deselectAllNodes(networkView);
		if (networkView != null) {
			List<CyNode> nodeList = new ArrayList<CyNode>();
//			List<CyEdge> edgeList = new ArrayList<CyEdge>();
			for (SubGraph sg : subGraphList) {
				sg.createNet(network);
				nodeList.addAll(sg.getCyNodes());
//				edgeList.addAll(sg.getCyEdges());
			}
			network.setSelectedNodeState(nodeList,
					true);

		} else {
			// Warn user that nothing will happen in this case because there is
			// no view to select nodes with
			JOptionPane.showMessageDialog(Cytoscape.getDesktop(),
					"You must have a network view\ncreated to select nodes.",
					"No Network View", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Renderer for list of results
	 * 
	 * @author florian
	 * 
	 */
	private class DetailsResultRenderer extends JTextArea implements
			TableCellRenderer {
		private static final long serialVersionUID = -6544298129581550411L;

		private final int minHeight = 0;

		public DetailsResultRenderer() {
			setEditable(false);
			setLineWrap(true);
			setWrapStyleWord(true);
			this.setFont(new Font(this.getFont().getFontName(), Font.PLAIN, 11));
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			setText(value.toString());

			if (isSelected) {
				this.setBackground(table.getSelectionBackground());
				this.setForeground(table.getSelectionForeground());
			} else {
				this.setBackground(table.getBackground());
				this.setForeground(table.getForeground());
			}

			// row height calculations
			int currentRowHeight = table.getRowHeight(row);
			int rowMargin = table.getRowMargin();
			this.setSize(table.getColumnModel().getColumn(column).getWidth(),
					currentRowHeight - (2 * rowMargin));
			int textAreaPreferredHeight = (int) this.getPreferredSize()
					.getHeight();
			// JTextArea can grow and shrink here
			if (currentRowHeight != Math.max(textAreaPreferredHeight
					+ (2 * rowMargin), minHeight + (2 * rowMargin))) {
				table.setRowHeight(row, Math.max(textAreaPreferredHeight
						+ (2 * rowMargin), minHeight + (2 * rowMargin)));
			}

			return this;
		}

	}

	/**
	 * Table model for results
	 * 
	 * @param results
	 * @return
	 */
	private TableModel createResultTableModel(final List<Motif> results) {
		return new AbstractTableModel() {
			private static final long serialVersionUID = 539481070513608075L;
			
			@Override
			public int getColumnCount() {
				return 4;
			}

			@Override
			public int getRowCount() {
				return results.size();
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				if (columnIndex == 0) {
					return results.get(rowIndex).getNumber();
				} else if (columnIndex == 1) {
					return results.get(rowIndex).getCount();
				} else if (columnIndex == 2) {
					return results.get(rowIndex).getFrequency();
				} else if (columnIndex == 3) {
					return results.get(rowIndex).getzScore();
				} else {
					throw new IllegalArgumentException("Column index "
							+ columnIndex);
				}
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return getValueAt(0, columnIndex).getClass();
			}

			@Override
			public String getColumnName(int column) {
				switch (column) {
				case 0:
					return "Id";
				case 1:
					return "Subgraph_Count";
				case 2:
					return "Frequency";
				case 3:
					return "ZScore";
				default:
					return super.getColumnName(column);
				}
			}
		};
	}

	/**
	 * Panel for correspondances
	 * 
	 * @return
	 */
	private JPanel createCorrespondancePanel() {
		JScrollPane scroll = new JScrollPane(imageCorrespondance);
		JPanel panel = new JPanel(new GridLayout(1, 1));
		// JPanel panel = new JPanel();
		panel.add(scroll);
		return panel;
	}

	/**
	 * Bottom panel
	 * 
	 * @param results
	 * @return
	 */
	private JPanel createBottomPanel(final List<Motif> results) {
		JPanel bottomPanel = new JPanel();

		// close button
		JButton closeButton = new JButton("Discard Result");
		JButton saveButton = new JButton("Save Result");
		// close action
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CytoscapeDesktop desktop = Cytoscape.getDesktop();
				CytoPanel cytoPanel = desktop.getCytoPanel(SwingConstants.EAST);
				// Must make sure the user wants to close this results panel
				String message = "You are about to dispose of "
						+ Parameters.PLUGIN_NAME
						+ " results.\nDo you wish to continue?";
				int userResult = JOptionPane.showOptionDialog(
						Cytoscape.getDesktop(), new Object[] { message },
						"Confirm", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (userResult == JOptionPane.YES_OPTION) {
					GinyUtils.deselectAllNodes(networkView);
					cytoPanel.remove(getJPanel());

					// must destroy the networks created for the results !
					for (Motif result : results) {
						System.out.println(result.getSubNetwork());
						Cytoscape.destroyNetwork(result.getSubNetwork());
						// Cytoscape.destroyNetwork(result.getNetworkWithNeighbor());
					}

				}
				// If there are no more tabs in the cytopanel then we hide it
				if (cytoPanel.getCytoPanelComponentCount() == 0) {
					cytoPanel.setState(CytoPanelState.HIDE);

				}
			}
		});

		bottomPanel.add(closeButton);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				CytoscapeDesktop desktop = Cytoscape.getDesktop();
				CytoPanel cytoPanel = desktop.getCytoPanel(SwingConstants.EAST);
				int returnVal = fc.showSaveDialog(getJPanel());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					// This is where a real application would save the file.
					System.out.println(file.getName());
				}

			}
		});

		//bottomPanel.add(saveButton);

		return bottomPanel;
	}

	private void showPages(final int itemsPerPage, final int currentPageIndex) {
		sorter.setRowFilter(filter(itemsPerPage, currentPageIndex - 1));
		ArrayList<JRadioButton> l = new ArrayList<JRadioButton>();

		int startPageIndex = currentPageIndex - pageSize;
		if (startPageIndex <= 0)
			startPageIndex = 1;
		int maxPageIndex = (model.getRowCount() / itemsPerPage) + 1;
		int endPageIndex = currentPageIndex + pageSize - 1;
		if (endPageIndex > maxPageIndex)
			endPageIndex = maxPageIndex;
		currentPageIndx = currentPageIndex;
		if (currentPageIndex > 1)
			l.add(createRadioButtons(itemsPerPage, currentPageIndex - 1, "Prev"));
		for (int i = startPageIndex; i <= endPageIndex; i++)
			l.add(createLinks(itemsPerPage, currentPageIndex, i - 1));
		if (currentPageIndex < maxPageIndex)
			l.add(createRadioButtons(itemsPerPage, currentPageIndex + 1, "Next"));

		box.removeAll();
		ButtonGroup bg = new ButtonGroup();
		box.add(Box.createHorizontalGlue());
		for (JRadioButton r : l) {
			box.add(r);
			bg.add(r);
		}
		box.add(Box.createHorizontalGlue());
		box.add(Box.createHorizontalGlue());
		box.revalidate();
		box.repaint();
		l.clear();
	}

	private JRadioButton createLinks(final int itemsPerPage, final int current,
			final int target) {
		JRadioButton radio = new JRadioButton("" + (target + 1)) {
			protected void fireStateChanged() {
				ButtonModel model = getModel();
				if (!model.isEnabled()) {
					setForeground(Color.GRAY);
				} else if (model.isPressed() && model.isArmed()) {
					setForeground(Color.GREEN);
				} else if (model.isSelected()) {
					setForeground(Color.RED);
				}
				super.fireStateChanged();
			}
		};
		radio.setForeground(Color.BLUE);
		radio.setUI(ui);
		if (target + 1 == current) {
			radio.setSelected(true);
		}
		radio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPages(itemsPerPage, target + 1);
			}
		});
		return radio;
	}

	private JRadioButton createRadioButtons(final int itemsPerPage,
			final int target, String title) {
		JRadioButton radio = new JRadioButton(title);
		radio.setForeground(Color.BLUE);
		radio.setUI(ui);
		radio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showPages(itemsPerPage, target);
			}
		});
		return radio;
	}

	private RowFilter filter(final int itemsPerPage, final int target) {
		return new RowFilter() {
			public boolean include(Entry entry) {
				int ei = (Integer) entry.getIdentifier();
				return (target * itemsPerPage <= ei && ei < target
						* itemsPerPage + itemsPerPage);
			}
		};
	}

}

class RadioButtonUI extends BasicRadioButtonUI {
	public Icon getDefaultIcon() {
		return null;
	}

	private static Dimension size = new Dimension();
	private static Rectangle rec1 = new Rectangle();
	private static Rectangle rec2 = new Rectangle();
	private static Rectangle rec3 = new Rectangle();

	public synchronized void paint(Graphics g, JComponent c) {
		AbstractButton b = (AbstractButton) c;
		ButtonModel model = b.getModel();
		Font f = c.getFont();
		g.setFont(f);
		FontMetrics fm = c.getFontMetrics(f);

		Insets i = c.getInsets();
		size = b.getSize(size);
		rec1.x = i.left;
		rec1.y = i.top;
		rec1.width = size.width - (i.right + rec1.x);
		rec1.height = size.height - (i.bottom + rec1.y);
		rec2.x = rec2.y = rec2.width = rec2.height = 0;
		rec3.x = rec3.y = rec3.width = rec3.height = 0;

		String text = SwingUtilities.layoutCompoundLabel(c, fm, b.getText(),
				null, b.getVerticalAlignment(), b.getHorizontalAlignment(),
				b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
				rec1, rec2, rec3, 0);

		if (c.isOpaque()) {
			g.setColor(b.getBackground());
			g.fillRect(0, 0, size.width, size.height);
		}
		if (text == null)
			return;
		g.setColor(b.getForeground());
		if (!model.isSelected() && !model.isPressed() && !model.isArmed()
				&& b.isRolloverEnabled() && model.isRollover()) {
			g.drawLine(rec1.x, rec1.y + rec1.height, rec1.x + rec1.width,
					rec1.y + rec1.height);
		}
		View v = (View) c.getClientProperty(BasicHTML.propertyKey);
		if (v != null) {
			v.paint(g, rec3);
		} else {
			paintText(g, b, rec3, text);
		}
	}
}
