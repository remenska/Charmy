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

//import core.internal.runtime.data.PlugDataManager;

import core.internal.extensionpoint.DeclaredHost;
import core.internal.runtime.data.IPlugData;
import core.internal.runtime.data.PlugDataManager;
import core.internal.ui.PlugDataWin;
import core.internal.ui.simpleinterface.EditGraphInterface;
import core.internal.ui.simpleinterface.ZoomGraphInterface;

/**
 * 
 * Charmy plug-in
 * Interfaccia che deve implementare un plugin di tipo editor/algo per 
 * essere agganciato al framework.
 **/
public interface IMainTabPanel {


	/**
	 * Setta il riferimento del contenitore unico dei dati relativi allo General User Interface.
	 * Viene invocato dal sistema in seguito alla creazione di tutti i plugin. 
	 * @param plugDataWin 
	 * @see PlugDataWin
	 */
	public void setDati(PlugDataWin plugDtW);
	
	/**
	 * In questo metodo i plugin devono creare il proprio contenitore unico dei dati. 
	 * @param reference al PlugDataManager
	 * @return Reference al contenitore unico dei dati.
	 */
	public IPlugData newPlugData(PlugDataManager pm);
	
	/**
	 * Metodo invocato dal sistema per permettere ai plugin di inizializzare
	 * la propria struttura dati ed il proprio User Interface.
	 * La chiamata avviene successivamente a SetDati e newPlugData
	 *@see setDati(PlugDataWin)
	 *@see newPlugData(PlugDataManager)
	 */
	public void init();

	/**
	 * Sperimentale.
	 * In questo metodo i plugin possono istanziare a runtime le proprie componenti host agganciate 
	 * agli extension point.
	 * @return   
	 */
	public DeclaredHost[] initHost();
	
	
	/**
	 * Restituisce il riferimento al PlugDataWin.
	 * @return PlugDataWin
	 */
	public PlugDataWin getPlugDataWin();
     
	/**
	 * Restituisce il riferimento al contenitore unico dei dati del Plugin.
	 * @return riferiemento al PlugData 
	 * @see IPlugData
	 */
	public IPlugData getPlugData();
     
     
     
     /**
      * Restituisce una implementazione di
      * EditGraphInterface per la gestione delle operazioni base di editor, 
      * null se il plugin non gestisce queste operazioni.
      * @see EditGraphInterface
      */
     public  EditGraphInterface getEditMenu();
     
	/**
	 * Restituisce una implementazione di
      * ZoomGraphInterface per la gestione delle operazioni di zoom, 
      * null se il plugin non gestisce queste operazioni.
      * @see ZoomGraphInterface
	 */
	public  ZoomGraphInterface getZoomAction();
      
      
	/**
	 *
	 *
	 */
   //ezio  public void resetForNewFile();
     
    
    /**
     * Evento di cambio Pannello. Viene invocato solo in occasione dell'attivazione del pannello principale del plugin.
     */
	public abstract void stateActive();


	 /**
     * Evento di cambio Pannello. Viene invocato solo in occasione della disattivazione del pannello principale del plugin.
     */
	public abstract void stateInactive();
	
	
	/**
	 * permette di prelevare un array di oggetti contenenti dati
	 * da mettere a disposizione di altri plug-in
	 * Ogni plug-in deve definire una propria rappresentazione dei 
	 * dati
	 * @return Array Di oggetti oppure null se non mando nulla all'esterno
	 */
	public abstract Object[] getDati();
	
	

}
