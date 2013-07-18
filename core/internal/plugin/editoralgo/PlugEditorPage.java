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

/**
 * è la classe che gestisce le indicazioni relativi alla page per la classe PlugEditor
 * @author michele
 * Charmy plug-in
 *	<extension
 *		  point="editor">
 *	   <page
 *			 name="fa qualcosa"
 *			 class="pippo.pluto.entrypoint"  //punto di ingresso del plugin
 *			 id="minni.topolino">
 *	   </page>
 *	</extension>
 *
 ***/
public class PlugEditorPage {
	
	/**
	 * stringa descrittiva del plug editor
	 */
	private String name;

	/**
	 * punto di entrata della classe di editing
	 */
	private String entryPoint;
	
	
	/**
	 * identificativo univoco a livello globale 
	 */
	private String id;
	


	/**
	 * punto di entrata del plug
	 * @return
	 */
	public String getEntryPoint() {
		return entryPoint;
	}

	/**
	 * id del plug
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * descrizione della classe
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * punto di entrata del plug
	 * @param string
	 */
	public void setEntryPoint(String string) {
		entryPoint = string;
	}

	/**
	 * id del plug
	 * @param string
	 */
	public void setId(String string) {
		id = string;
	}

	/**
	 * descrizione della classe
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

}
