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

import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;

import cytoscape.Cytoscape;
import edu.lbb.kavosh.algorithm.data.Parameters;

public class AboutWindow {
	public AboutWindow(){
		
		JDialog dialog = new JDialog(Cytoscape.getDesktop(), "About", false);
		dialog.setResizable(false);
		
        JEditorPane editorPane = new JEditorPane();
        editorPane.setMargin(new Insets(10,10,10,10));
        editorPane.setEditable(false);
        editorPane.setEditorKit(new HTMLEditorKit());
        editorPane.addHyperlinkListener(new HyperlinkAction(editorPane));

        String logoCode = "";
       
        editorPane.setText(
                "<html><body>"+logoCode+"<P align=center><b>" + Parameters.PLUGIN_NAME + " " + Parameters.VERSION + "</b><BR>" +
                "A Cytoscape PlugIn<BR><BR>" +

                "<a href='http://lbb.ut.ac.ir/'>http://lbb.ut.ac.ir/</a><BR>" +
                "Supported by Labratory of System Biology and Bioinformatics- University Of Tehran." +
                "</body></html>");
        dialog.setContentPane(editorPane);
        
        dialog.pack();
        dialog.setVisible(true);
		
	}
	
	
	private class HyperlinkAction implements HyperlinkListener {
        JEditorPane pane;

        public HyperlinkAction(JEditorPane pane) {
            this.pane = pane;
        }

        public void hyperlinkUpdate(HyperlinkEvent event) {
            if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                cytoscape.util.OpenBrowser.openURL(event.getURL().toString());
            }
        }
    }
}
