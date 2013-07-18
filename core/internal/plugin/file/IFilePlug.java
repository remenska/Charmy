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
    

package core.internal.plugin.file;



import core.internal.runtime.data.PlugDataManager;
import core.internal.plugin.PlugInDescriptor;
import core.internal.ui.PlugDataWin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author michele
 * Charmy plug-in
 * Interfaccia per i Componenti che debbono manipolare 
 * i files per salvare o leggere in altri formati
 **/
public interface IFilePlug {

	/**
	 * Setta il riferimento del PlugDataWin e del PlugDataManager.
	 * @param plugDataWin 
	 * @param PlugDataManager
	 * @see PlugDataWin
	 * @see PlugDataManager
	 */
	public void setDati(PlugDataWin plugDtW, PlugDataManager pd);
	
	/**
	 * restituisce il riferimento al plugWin
	 * @return PlugDataWin
	 */
	public PlugDataWin getPlugDataWin();
     
	/**
	 * restituisce il riferimento al plugData
	 * @return riferiemnto al plugData 
	 * @see PlugData
	 */
	public PlugDataManager getPlugData();
   
	
	public void setPlugins(SerializableCharmyFile[] plugins);

	/**
	 * usata nella fase di salvataggio : il plugin file appende al tag root 
	 * informazioni supplementari, il sistema invoca questo metodo per ogni xml contenuto nel file zip
	 */
	public void appendInfoFile(String idXmlModel, Document doc,Element elementRoot) throws Exception;
	
	public void resetForNewFile();
	
	
}
