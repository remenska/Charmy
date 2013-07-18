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
    
package core.internal.plugin.editoralgo;

import core.internal.plugin.PlugExtension;
import core.internal.plugin.PlugInDescriptor;


/**
 * descrizione del plugin per la parte dell'editor/algoritmo
 * ogni plug in puo averne al piu 1
 * @author michele
 * Charmy plug-in
 **/
public class PlugEditor extends PlugExtension{
	
	PlugInDescriptor pluginDescriptor;
	
	/**
	 * descrittore dell'editor
	 */
	private PlugEditorPage plugEditorPage;
	
	/**
	 * costruttore
	 * @param pep plug editor pare
	 */
	public PlugEditor(PlugEditorPage pep){
		super("editor");
		plugEditorPage = pep;
	}
	
	public PlugEditor(){
			this(new PlugEditorPage());
	}


	/**
	 * @return
	 */
	public PlugEditorPage getPlugEditorPage() {
		return plugEditorPage;
	}

	/**
	 * @param page
	 */
	public void setPlugEditorPage(PlugEditorPage page) {
		plugEditorPage = page;
	}

	/**
	 * funzione entry point per l'editor
	 * @return
	 */
	public String getEntryPoint() {
		return plugEditorPage.getEntryPoint();
	}

	/**
	 * @param string
	 */
	public void setId(String string) {
		plugEditorPage.setId(string);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return plugEditorPage.hashCode();
	}

	/**
	 * @return
	 */
	public String getId() {
		return plugEditorPage.getId();
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		plugEditorPage.setName(string);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return plugEditorPage.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return plugEditorPage.equals(obj);
	}

	/**
	 * @return
	 */
	public String getName() {
		return plugEditorPage.getName();
	}

	/**
	 * @param string
	 */
	public void setEntryPoint(String string) {
		plugEditorPage.setEntryPoint(string);
	}

	public PlugInDescriptor getPluginDescriptor() {
		return pluginDescriptor;
	}

	public void setPluginDescriptor(PlugInDescriptor pluginDescriptor) {
		this.pluginDescriptor = pluginDescriptor;
	}

}
