/**
@project Lbb_Kavosh
@author Mitra Ansari
@date Jun 20, 2011
**/
package edu.lbb.kavosh.data.common;

import javax.swing.ImageIcon;

import cytoscape.CyNetwork;

public interface ISubGraph {

	public long getId();
	public ImageIcon getImage();
	public CyNetwork getSubNetwork();
	
}
