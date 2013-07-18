/*   Charmy (CHecking Architectural Model consistencY)
 *   Copyright (C) 2004 Patrizio Pelliccione <pellicci@di.univaq.it>,
 *   Henry Muccini <muccini@di.univaq.it>, Paola Inverardi <inverard@di.univaq.it>. 
 *   Computer Science Department, University of L'Aquila. SEA Group. 
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
    

package core.internal.ui;


import java.io.Serializable;

import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import core.internal.plugin.PlugManager;
import core.internal.plugin.algoritmo.AlgoManager;
import core.internal.plugin.file.FileManager;
import core.internal.ui.menu.BuildMenu;
import core.internal.ui.menu.EditMenu;
import core.internal.ui.menu.FileMenu;
import core.internal.ui.menu.HelpMenu;
import core.internal.ui.statusbar.StatusBar;
import core.internal.ui.toolbar.EditToolBar;
import core.internal.ui.toolbar.GeneralToolBar;
import core.internal.ui.toolbar.ZoomToolBar;



/**
 * @author michele
 * Charmy plug-in
 * Dati da inviare al plug-in per quanto riguarda 
 * le finestre dell'applicazione
 **/
public class PlugDataWin implements Serializable{
	
	
	/**
	 * riferimento alla tab principale
	 */
	private JTabbedPane mainPanel = null;

	/**
	 * riferimento al pannello delle toolbar
	 */
	private JPanel toolBarPanel = null; 
	
	/**
	 * riferimento al menu principale
	 */
	private JMenuBar mainMenuBar = null;
		
	/**
	 * menu relativo all'editing
	 */
	private EditMenu istanzaEditMenu;
	
	/**
	 * istanza della toolbar per i files
	 */
	private GeneralToolBar generalToolBar;
	
	/**
	 * menu relativo al file management
	 */
	private FileManager fileManager;
	
	/**
	 * Menu degli algoritmi (verra trasformato nel menu dei plug-in)
	 */
	private AlgoManager istanzaAlgoManager;
    
    
    /**
     * istanza del file menu
     */	
	private FileMenu fileMenu;
	
	
	/**
	 * menu di plug-in
	 */
	private BuildMenu plugMenu;
	

	private HelpMenu helpMenu;
	
	/**
	 * riferimento alla barra zoom
	 */
    private ZoomToolBar istanzaZoomToolBar = null;

	/**
	 * riferimento alla barra dell'editor 
	 */
	
	private EditToolBar istanzaEditToolBar = null;

	/**
	 * istanza della status bar
	 */
	private StatusBar statusBar;
	
	
	/**
	 * istanza del plug-manager
	 */
	private PlugManager plugManager;

	/**
	 * costruttore 
	 * 
	 *
	 */
	public PlugDataWin(){
		setMainMenuBar( new JMenuBar());
		setMainPanel(new JTabbedPane());
		setToolBarPanel(new JPanel());
		setEditToolBar(new EditToolBar(null)); 
		setZoomToolBar(new ZoomToolBar(null));
		setStatusBar(new StatusBar(""));	
		setGeneralToolBar(new GeneralToolBar(null));
		createMenu();
	}


	/**
	 * crea i menu dell'interfaccia
	 *
	 */
	private void createMenu(){
		
		
		//this.newFileMenu = new NewFileMenu("New",getFileManager());
		fileMenu = new FileMenu(getFileManager());	
		mainMenuBar.add(fileMenu);
		
		setEditMenu(new EditMenu(null));
		mainMenuBar.add(getEditMenu());
		
		setPlugMenu(new BuildMenu(getAlgoManager()));
		mainMenuBar.add(getPlugMenu());
			
		setHelpMenu(new HelpMenu());
		mainMenuBar.add(getHelpMenu());		
	}

	/**
	 * @return riferimento al menu
	 */
	public JMenuBar getMainMenuBar() {
		return mainMenuBar;
	}

	/**
	 * @return riferimento al MainPanel
	 */
	public JTabbedPane getMainPanel() {
		return mainPanel;
	}

	/**
	 * @return riferimento alla toolBarPanel
	 */
	public JPanel getToolBarPanel() {
		return toolBarPanel;
	}

	/**
	 * @param bar
	 */
	public void setMainMenuBar(JMenuBar bar) {
		mainMenuBar = bar;
	}

	/**
	 * @param pane
	 */
	public void setMainPanel(JTabbedPane pane) {
		mainPanel = pane;
	}

	/**
	 * @param panel
	 */
	public void setToolBarPanel(JPanel panel) {
		toolBarPanel = panel;
	}




	/**
	 * @return
	 */
	public EditToolBar getEditToolBar() {
		return istanzaEditToolBar;
	}

	/**
	 * @return
	 */
	public ZoomToolBar getZoomToolBar() {
		return istanzaZoomToolBar;
	}

	/**
	 * @param bar
	 */
	public void setEditToolBar(EditToolBar bar) {
		istanzaEditToolBar = bar;
	}

	/**
	 * @param bar
	 */
	public void setZoomToolBar(ZoomToolBar bar) {
		istanzaZoomToolBar = bar;
	}

	/**
	 * @return
	 */
	public StatusBar getStatusBar() {
		return statusBar;
	}

	/**
	 * @param bar
	 */
	public void setStatusBar(StatusBar bar) {
		statusBar = bar;
	}
	
	
	
	
	

	/**
	 * ritorna l'istanza del menu di editing
	 * @return
	 */
	public EditMenu getEditMenu() {
		return istanzaEditMenu;
	}

	/**
	 * setta il nuovo menu di editing
	 * @param istanzaEditMenu
	 */
	public void setEditMenu(EditMenu istanzaEditMenu) {
		this.istanzaEditMenu = istanzaEditMenu;
	}

	/**
	 * menu del file menager
	 * @return riferimento al menu del file manager
	 */
	public FileManager getFileManager() {
		return fileManager;
	}

	/**
	 * setta il riferimento al menu di file manager
	 * @param istanzaFileManager
	 */
	public void setFileManager(FileManager istanzaFileManager) {
		this.fileManager = istanzaFileManager;
		fileMenu.updateRifGraphWindow(istanzaFileManager);
		generalToolBar.updateRifGraphWindow(fileManager);
	}

	/**
	 * Menu degli algoritmi, verra trasformato in 
	 * menu dei plug-in
	 * @return AlgoManager menu
	 */
	public AlgoManager getAlgoManager() {
		return istanzaAlgoManager;
	}

	/**
	 * setta il menu degli algoritmi
	 * @param istanzaAlgoManager 
	 */
	public void setAlgoManager(AlgoManager istanzaAlgoManager) {
		this.istanzaAlgoManager = istanzaAlgoManager;
	}

	/**
	 * Menu files
	 * @return il riferimento al menu File
	 */
	public FileMenu getFileMenu() {
		return fileMenu;
	}

	/**
	 * setta il menu file
	 * @param fileMenu
	 */
	public void setFileMenu(FileMenu fileMenu) {
		this.fileMenu = fileMenu;
	}

	/**
	 * ritorna un riferimento al menu dei plug-in
	 * @return
	 */
	public BuildMenu getPlugMenu() {
		return plugMenu;
	}

	/**
	 * setta il menu dei plug-in (solo variabile)
	 * @param plugMenu
	 */
	public void setPlugMenu(BuildMenu plugMenu) {
		this.plugMenu = plugMenu;
	}

	/**
	 * @return
	 */
	public HelpMenu getHelpMenu() {
		return helpMenu;
	}

	/**
	 * @param helpMenu
	 */
	public void setHelpMenu(HelpMenu helpMenu) {
		this.helpMenu = helpMenu;
	}

	/**
	 * @return
	 */
	public GeneralToolBar getGeneralToolBar() {
		return generalToolBar;
	}

	/**
	 * @param istanzaGeneralToolBar
	 */
	public void setGeneralToolBar(GeneralToolBar istanzaGeneralToolBar) {
		this.generalToolBar = istanzaGeneralToolBar;
		if(fileManager != null){
			generalToolBar.updateRifGraphWindow(fileManager);
		}
	}

	/**
	 * @return Returns the plugManager.
	 */
	public PlugManager getPlugManager() {
		return plugManager;
	}

	/**
	 * @param plugManager The plugManager to set.
	 */
	public void setPlugManager(PlugManager plugManager) {
		this.plugManager = plugManager;
	}

}
