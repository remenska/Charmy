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
    
package core.internal.runtime.data;

import java.io.Serializable;
import java.util.LinkedList;

import core.internal.plugin.PlugManager;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.plugin.file.FileManager;



/**
 * @author michele
 * @version 2.1
 * Charmy plug-in
 * 
 * Classe per lo scambio dei dati con il plug-in, contiene tutti i dati
 * che si possono scambiare con il plug-in
 *  **/
public class PlugDataManager  implements Serializable{

	/**
	 * variabile indicante una transazione, ovvero un'insieme di operazioni
	 * sui dati che riguardano la stessa sessione
	 */
	private boolean inTransaction = false;
	
	/**
	 * identificativo di sessione della transazione, ogni movimento
	 * di transazione avr� il medesimo id
	 */
	private long idSessione = 0;

	private LinkedList plugins;

	/**
	 * dati riguardanti la lista canali (S_A_Topology Diagram)
	 */
//	private ListaCanale listaCanale = null;
	
	/**
	 * lista dei thread relativi agli State Diagram
	 * 
	 */
//	private ListaDP listaDP = null;
	
	/**
	 * lista delle dinamiche di sistema
	 */
//	private ListaDS listaDS = null;
	
//	private ListaClasse listaClasse =null;


	/** lista dei processi definiti nel S_A_ Topology Diagram. */
//	private ListaProcesso listaProcesso = null;
	
	private PlugManager pm;
	
	private FileManager fileManager;
	
	/**
	 * costruttore, per default costruisce tutte le liste vuote
	 */

	public LinkedList getPluginsList(){
		return plugins;
	}
	
	public PlugDataManager(){
		plugins=new LinkedList();
		
		
		
		
//			listaProcesso = new ListaProcesso(this);
//			listaCanale = new ListaCanale(this);
			
			/* utilizzati principalmente in StateWindow */
//			listaDP = new ListaDP(this);
//			listaDS = new ListaDS(this);
			/*  */
			/**
			 * registrazioni listener
			 */
//			listaProcesso.addListener(listaDP);
			//listaProcessiUtente.addListener(listaDP);
//			listaProcesso.addListener(listaCanale);
			
			
			
	}
	
	public void setPlugManager(PlugManager pm){
		this.pm=pm;		
		this.fileManager=pm.getPlugDataWin().getFileManager();
	}

	public IPlugData getPlugData(String plugID){
		try {
			IMainTabPanel editor=pm.getPlugEdit(plugID);
			if(editor!=null)
				return editor.getPlugData();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public void addPlugin(IPlugData plug){
		
		for (int i = 0; i < plugins.size(); i++) {
			
			if(plugins.get(i).equals(plug))
				return;
			
		}
		
		plugins.add(plug);
	}
		
	
	/**
	 * pulisce tutti i dati contenuti nelle liste
	 * la cancellazzione dei dati viene vista come 
	 * un'unica sessione
	 *
	 */
	
 
	
	public  synchronized void  clearAll(){
		boolean bo = this.startSessione();
		for(int i=0;i<plugins.size();i++){
			((IPlugData)plugins.get(i)).clearAll();
		}
		this.stopSessione(bo);
	}
	
	public  synchronized void  clearAll(IPlugData[] plugsData){
		boolean bo = this.startSessione();
		for(int i=0;i<plugsData.length;i++){
			plugsData[i].clearAll();
		}
		this.stopSessione(bo);
	}
	
	
/*
 * getter e setter
 */



//	/**
//	 * Ritorna la lista Canali
//	 * @return
//	 */
//	public ListaCanale getListaCanale() {
//		return listaCanale;
//	}
//	
//
//	/**
//	 * restituisce la lista delle ListaThread 
//	 * relativi alle dinamiche dei processi
//	 * @return
//	 */
//	public ListaDP getListaDP() {
//		return listaDP;
//	}
//
//	/**
//	 * ritorna la lista relativa alla dinamica di sistema(ListaDS)
//	 * 
//	 * @return
//	 */
//	public ListaDS getListaDS() {
//		return listaDS;
//	}
//
//
//	/**
//	 * ritorna la lista dei processi
//	 * @return
//	 */
//	public ListaProcesso getListaProcesso() {
//		return listaProcesso;
//	}
	
	/**
	 * ritorna l'idSessione corrente
	 * @return
	 */
	public  synchronized  long getIdSessione() {
		return idSessione;
	}



	/**
	 * ritorna la modalità di transazione
	 * @return true, sono in uno stato di medesima transizione, false nessuna transizione pendente
	 */
	public synchronized boolean isInTransaction() {
		return inTransaction;
	}


	/**
	 * attiva una transazione e genera un nuovo id Di sessione
	 * @return nuovo id di sessione
	 */
	private synchronized  long startTransaction() {
		if(!inTransaction){
			idSessione++;
			inTransaction = true;
		}
		return idSessione;
	}

	/**
	 * controlla se la sessione ha una transazione attiva, se la transizione non era attiva 
	 * la attiva, e restituisce false, altrimenti true, il numero di sessione va recuperato mediante 
	 * <code>plugData.getIdSessione()</code>
	 * @param plugData
	 * @return true se la transazione era attiva, false attiva una transazione e ritorna
	 */
	public synchronized boolean startSessione() {	
		boolean bo = isInTransaction();
		   startTransaction();
		return bo; 
	}
	
	
	/**
	 * conclude una transazione 
	 */
	public synchronized  void stopSessione(boolean trans) {
		inTransaction = trans;
	}

	public FileManager getFileManager() {
		return fileManager;
	}


}
