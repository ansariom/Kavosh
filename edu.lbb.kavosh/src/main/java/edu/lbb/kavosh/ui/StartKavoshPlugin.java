/**
@project Lbb_Sordaria DataBase
@author Mitra Ansari
@date May 3, 2011
 **/
package edu.lbb.kavosh.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import cytoscape.Cytoscape;
import cytoscape.logger.CyLogger;
import cytoscape.plugin.CytoscapePlugin;
import edu.lbb.kavosh.algorithm.data.Parameters;

public class StartKavoshPlugin extends CytoscapePlugin {

	protected static CyLogger logger = CyLogger
			.getLogger(StartKavoshPlugin.class);

	public StartKavoshPlugin() {
//		KavoshPluginMenuAction menuAction = new KavoshPluginMenuAction(this);
		JMenu menu = Cytoscape.getDesktop().getCyMenus().getOperationsMenu();
		addInPluginMenu(menu);
	}
	
	private void addInPluginMenu(JMenu menu) {
		JMenuItem item;
        JMenu submenu = new JMenu(Parameters.PLUGIN_NAME);

        item = new JMenuItem("Start " + Parameters.PLUGIN_NAME);
        item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				extractSo();
				StartPanel.getInstance();
				
			}

			private void extractSo() {
				String currentDir = new File(".").getAbsolutePath() + "/plugins/";
				try {
					JarFile jarFile = new JarFile(currentDir + "kavosh-1.0.jar");
					Enumeration<JarEntry> enumJar = jarFile.entries();
					while (enumJar.hasMoreElements()) {
						JarEntry file = (JarEntry) enumJar.nextElement();
						java.io.File f = new java.io.File("/home/Mitra/" + java.io.File.separator + file.getName());
						if (file.isDirectory()) { // if its a directory, create it
							f.mkdir();
							continue;
						}
						java.io.InputStream is = jarFile.getInputStream(file); // get the input stream
						java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
						while (is.available() > 0) {  // write contents of 'is' to 'fos'
							fos.write(is.read());
						}
						fos.close();
						is.close();

					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        });
        submenu.add(item);

        //About box
        item = new JMenuItem("About");
        item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new AboutWindow();
			}
        });
        submenu.add(item);
        
        menu.add(submenu);
	}


}
