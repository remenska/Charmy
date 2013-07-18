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
    

package plugin.topologychannels; 



import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JPanel;

import plugin.charmyui.extensionpoint.editor.GenericHostEditor;
import plugin.topologychannels.data.ElementoCanale;
import plugin.topologychannels.data.ElementoProcesso;
import plugin.topologychannels.data.ListaCanale;
import plugin.topologychannels.data.ListaProcesso;
import plugin.topologychannels.data.PlugData;
import plugin.topologychannels.dialog.FinestraElementoBoxTesto;
import plugin.topologychannels.dialog.FinestraGraficoLinkTopology;
import plugin.topologychannels.dialog.FinestraGraphEditor;
import plugin.topologychannels.general.edcp.CopyCutPasteTopology;
import plugin.topologychannels.general.undo.UndoRedoTopology;
import plugin.topologychannels.resource.utility.ListMovedObjects;
import core.internal.plugin.file.FileManager;
import core.internal.ui.PlugDataWin;
import core.resources.jpeg.JpegImage;

/**	Classe per la creazione e gestione del pannello su cui verrà disegnato il
	S_A_ Topology Diagram_ La classe controlla gli eventi del mouse (clicked,
	released, dragged), esegue le classiche operazioni di editing (del, copy,
	paste, undo) e di zoom, implementa le operazioni associate ai pulsanti
	della TopologyToolBar, etc. */

public class TopologyEditor extends GenericHostEditor{
	
	
   public FileManager fileManager;
	
	/** Numero di Processi. */
	private long numProcessi; 
	
	private int spiazzamentoX = 0, spiazzamentoY = 0;

	/** Costante per lo stato di attesa dell'input dell'utente. */
	public static final int ATTESA = 0;

	/** Costante per lo stato in cui l'utente sta 
		introducendo un nuovo processo. */
	public static final int DISEGNA_PROCESSO = 1;

	/** Costante per lo stato in cui l'utente sta inserendo 
		un nuovo canale di comunicazione tra processi. */
	public static final int INSERIMENTO_CANALE = 2;

	/** Costante per lo stato in cui si sta tracciando una 
		linea relativa ad un canale di collegamento. */
	public static final int DISEGNA_CANALE = 3;

	/** Costante per lo stato in cui l'utente sta 
		spostando un processo. */
	public static final int SPOSTA_PROCESSO = 4;

	/** Costante per lo stato in cui l'utente seleziona più 
		oggetti tramite trascinamento del mouse. */
	public static final int MULTISELEZIONE = 5;

	/** Costante per lo stato in cui l'utente sposta più oggetti_
		Ci sono tre modi per attivare questo stato:
		1) Rilascio mouse nello stato MULTISELEZIONE;
		2) Trascinamento del mouse con pressione del tasto 
		   "MAIUSC" nello stato ATTESA;
		3) Operazione di paste. */
	public static final int SPOSTA_MULTISELEZIONE = 6;

	/** Costante per lo stato in cui l'utente ha già iniziato l'operazione di 
		spostamento di più oggetti_ Questo stato viene attivato solo tramite 
		trascinamento del mouse nello stato SPOSTA_MULTISELEZIONE. */
	public static final int SPOSTA_MULTISELEZIONE_FASE2 = 7;

	public static final int TEMPATTESA = 8;

	/** Riferimento alla toolbar. */
	//private TopologyToolBar localTopologyToolBar;

	/** Riferimento alla toolbar. */
	//private EditToolBar localEditToolBar;

	/** Lista dei processi disegnati. */
	private ListaProcesso ListaDeiProcessi;

	/** Lista dei canali disegnati. */
	private ListaCanale ListaDeiCanali;

	/** Riferimento al processo corrente.*/
	private ElementoProcesso ProcessoCorrente;

	/** Processo di partenza nell'operazione di
		inserimento di un nuovo canale. */
	private ElementoProcesso ProcessFrom;

	/** Processo di arrivo nell'operazione di
		inserimento di un nuovo canale. */
	private ElementoProcesso ProcessTo;

	/** Riferimento al canale corrente.*/
	private ElementoCanale CanaleCorrente;

	/** Memorizza lo stato dell'editor (ATTESA, DISEGNA_PROCESSO,
		INSERIMENTO_CANALE, DISEGNA_CANALE, SPOSTA_PROCESSO, etc.) */
	private int ClassEditorStatus = ATTESA;

	/** Memorizza il tipo di processo per una 
		successiva operazione di inserimento. */
	private int TipoProcesso = 0;

	/** Memorizza se il processo è di tipo dummy per 
		una successiva operazione di inserimento. */
	private boolean ctrlDummy;

	/** Assume il valore 'true' quando uno dei pulsanti
		di inserimento processo (PROCESS, STORE, DUMMY)
		o quello di inserimento canale risulta bloccato. */
	private boolean BloccoPulsante = false;

	/** Finestra di dialogo per impostare le proprietà di un processo. */
	private FinestraElementoBoxTesto FinestraProprietaProcesso;

	/** Finestra di dialogo per impostare le proprietà di un canale. */
	private FinestraGraficoLinkTopology FinestraProprietaCanale;

	/** Finestra di dialogo per impostare le proprietà dell'editor. */
	private FinestraGraphEditor FinestraProprietaEditor;

	/** Necessaria per l'implementazione. */
	private Graphics2D g2;

	/** Utilizzata per le operazioni di copy e paste su processi. */
	private LinkedList tmpListaProcessi = null;

	/** Utilizzata per le operazioni di copy e paste su canali. */
	private LinkedList tmpListaCanali = null;

	/** Utilizzata per memorizzare la lista dei processi nelle
		operazioni interessate da multiselezione_ Forse potrebbe 
		essere eliminata, riutilizzando tmpListaProcessi. */
	private ListMovedObjects spostaListaProcessi = null;

	/** Riferimento all'editor del S_A_ Topology Diagram, ovvero alla
		classe stessa_ E' usato all'interno delle classi nidificate 
		ClassEditorClickAdapter e ClassEditorMotionAdapter. */
	private TopologyEditor rifEditor;

	/** Punto di partenza in un'operazione di multiselezione
		tramite trascinamento del mouse. */
	private Point startPoint;

	/** Punto di arrivo in un'operazione di multiselezione
		tramite trascinamento del mouse. */
	private Point endPoint;

	/** Punto di posizionamento del mouse nello 
		spostamento contemporaneo di più oggetti. */
	private Point trackPoint;

	/** Punto di riferimento per lo spostamento 
		contemporaneo di più oggetti. */
	private Point rifPoint;

	/** Rettangolo visualizzato durante un'operazione di 
		trascinamento del mouse nello stato MULTISELEZIONE. */
	private Rectangle2D rectMultiSelection = null;

	/** Memorizza il più piccolo rettangolo contenente
		tutti i processi selezionati (multiselezione). */
	private Rectangle2D externalRect = null;

	/**
	 * riferimenti alle finestre principali del programme
	 */
	private PlugDataWin plugDataWin;

	/**
	 * riferimento alla struttura dati del programma
	 */
	private PlugData plugData;

	/**
	 *  riferimento alla TopologyWindow 
	 */
	private TopologyWindow topologyWindow;

	/**
	 * riferimento al sistema di undo redo
	 */
	private UndoRedoTopology undoRedoManager;

	private CopyCutPasteTopology ccpTopology;
	

	/** Costruttore_ 
		Prende in ingresso un riferimento alla barra di stato. */
	public TopologyEditor() {
		super(null);
		
	/*	plugDataWin = pdw;
		plugData = (PlugData)pd;
		topologyWindow = tw;
		addMouseListener(new ClassEditorClickAdapter());
		addMouseMotionListener(new ClassEditorMotionAdapter());
		ListaDeiProcessi = plugData.getListaProcesso();
		ListaDeiCanali = plugData.getListaCanale();

		undoRedoManager = new UndoRedoTopology();
		undoRedoManager.setDati(plugDataWin, plugData);
		rifEditor = this;
		ccpTopology = new CopyCutPasteTopology(plugData);*/
		
	}

	public void setDati(TopologyWindow tw, PlugDataWin pdw, PlugData pd){
		
		this.fileManager=pdw.getFileManager();
		
		plugDataWin = pdw;
		plugData = (PlugData)pd;
		topologyWindow = tw;
		addMouseListener(new ClassEditorClickAdapter());
		addMouseMotionListener(new ClassEditorMotionAdapter());
		ListaDeiProcessi = plugData.getListaProcesso();
		ListaDeiCanali = plugData.getListaCanale();

		undoRedoManager = new UndoRedoTopology();
		undoRedoManager.setDati(plugDataWin, plugData);
		rifEditor = this;
		ccpTopology = new CopyCutPasteTopology(plugData);
		
	}
	
	public PlugData getPlugData(){
		return plugData;
	}
	
	/** Imposta il riferimento alle toolbar. */
	/*public void setToolBar(TopologyToolBar ctbar) {
		localTopologyToolBar = ctbar;
		localEditToolBar = plugDataWin.getEditToolBar();
		localEditToolBar.setButtonEnabled("Copy", true);
		localEditToolBar.setButtonEnabled("Paste", true);
		localEditToolBar.setButtonEnabled("Del", true);
		localEditToolBar.setButtonEnabled("Cut", true);
		localEditToolBar.setButtonEnabled("Undo", true);
		localEditToolBar.setButtonEnabled("Redo", true);
	}*/

	/** Restituisce lo stato dell'editor. */
	public int getEditorStatus() {
		return ClassEditorStatus;
	}

	/** Imposta lo stato dell'editor_ Si osservi che solo
		alcuni stati possono essere assegnati dall'esterno:
		DISEGNA_PROCESSO, INSERIMENTO_CANALE, DISEGNA_CANALE. */
	public void setEditorStatus(
		int j,
		int tipoprc,
		boolean isDummy,
		boolean ctrlpulsante) {
		BloccoPulsante = ctrlpulsante;
		TipoProcesso = tipoprc;
		ctrlDummy = isDummy;
		switch (j) {
			case DISEGNA_PROCESSO :
				ClassEditorStatus = DISEGNA_PROCESSO;
				break;
			case INSERIMENTO_CANALE :
				ClassEditorStatus = INSERIMENTO_CANALE;
				break;
			case DISEGNA_CANALE :
				ClassEditorStatus = DISEGNA_CANALE;
				break;
			default :
				ClassEditorStatus = ATTESA;
				break;
		}
	}

	/** "Stampa" l'editor con processi e canali. */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2 = (Graphics2D) g;
		g2.scale(scaleX, scaleY);
		Stroke tmpstroke = g2.getStroke();
		g2.setStroke(bstroke);
		g2.draw(rettangolo);
		g2.setStroke(tmpstroke);
		if (rectMultiSelection != null) {
			g2.draw(rectMultiSelection);
		}
		ListaDeiCanali.paintLista(g2);
		ListaDeiProcessi.paintLista(g2);
		
	}

	/** Classe per la gestione della pressione dei tasti del mouse. */
	private final class ClassEditorClickAdapter extends MouseAdapter {		
		/** Rilascio del mouse. */
		public void mouseReleased(MouseEvent e) {
			if (rettangolo.contains(updateGetPoint(e.getPoint()))) {
				switch (ClassEditorStatus) {

					case DISEGNA_PROCESSO :
						ListaDeiProcessi.noSelected();
						ListaDeiCanali.noSelected();
						ProcessoCorrente =
							new ElementoProcesso(
								updateGetPoint(e.getPoint()),
								ElementoProcesso.GLOBALE,
								TipoProcesso,
								ctrlDummy,
								"process" + (ElementoProcesso.getNumIstanze() + 1));
						// Aggiornamento della barra di stato.                     
						plugDataWin.getStatusBar().setText(
							"Process "
								+ ProcessoCorrente.getName()
								+ " inserted.");
						if (!BloccoPulsante) {
							ClassEditorStatus = TEMPATTESA;
							rifEditor.setAllButtonNoPressed();
						}
						ListaDeiProcessi.addElement(ProcessoCorrente);
						repaint();
						//plugDataWin.getFileManager().setModificata(true);
						//core.internal.plugin.file.FileManager.setModificata(true);
						fileManager.setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);
						
						break;

					case INSERIMENTO_CANALE :
						// Selezionato solo il processo di partenza del nuovo canale.
						ListaDeiCanali.noSelected();
						ListaDeiProcessi.noSelected();
						ProcessFrom =
							(ElementoProcesso) (ListaDeiProcessi
								.getElement(updateGetPoint(e.getPoint())));
						if (ProcessFrom != null) {
							plugDataWin.getStatusBar().setText(
								"Process "
									+ ProcessFrom.getName()
									+ " selected."
									+ " Click over another process to insert a channel.");
							ProcessFrom.setSelected(true);
							ClassEditorStatus = DISEGNA_CANALE;
						}
						repaint();
						break;

					case DISEGNA_CANALE :
						// Creazione ed inserimento di un nuovo canale.
						ProcessTo =
							(ElementoProcesso) (ListaDeiProcessi
								.getElement(updateGetPoint(e.getPoint())));
						if ((ProcessTo != null)
							&& (!(ProcessTo.equals(ProcessFrom)))) {
							ElementoCanale.incNumCanale();
							CanaleCorrente =
								new ElementoCanale(ProcessFrom, ProcessTo, null);
							ListaDeiCanali.addElement(CanaleCorrente);
							// Aggiornamento della barra di stato.                     
							plugDataWin.getStatusBar().setText(
								"Channel "
									+ CanaleCorrente.getName()
									+ " inserted.");
//							core.internal.plugin.file.FileManager.setModificata(true);
							fileManager.setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);
							
						} else {
							plugDataWin.getStatusBar().setText(
								"Channel not inserted.  Topology ready.");
						}
						ProcessTo = null;
						ProcessFrom.setSelected(false);
						ProcessFrom = null;
						ClassEditorStatus = INSERIMENTO_CANALE;
						if (!BloccoPulsante) {
							ClassEditorStatus = TEMPATTESA;
							rifEditor.setAllButtonNoPressed();
						}
						repaint();
						break;

					case SPOSTA_PROCESSO :
						
						ClassEditorStatus = ATTESA;

						//riabilita la lista dei il send dei messaggi
						//il sistema scritto qui potrebbe essere eliminato
						//gestendo l'evento ad un livello più basso.
						if(ProcessoCorrente!=null){
							ProcessoCorrente.enabled();
							ProcessoCorrente.informPostUpdate();
						}
						plugDataWin.getStatusBar().setText(
							"Process "
								+ ProcessoCorrente.getName()
								+ " selected.  Topology ready.");
						break;

					case MULTISELEZIONE :
						endPoint = updateGetPoint(e.getPoint());
						// Per selezionare gli oggetti il trascinamento del mouse deve 
						// avvenire da sinistra verso destra e dall'alto verso il basso.                	
						if ((endPoint.x > startPoint.x)
							&& (endPoint.y > startPoint.y)) {
							// Selezione degli oggetti contenuti nel rettangolo rectMultiSelection.                		
							rectMultiSelection =
								new Rectangle2D.Double(
									startPoint.x,
									startPoint.y,
									endPoint.x - startPoint.x,
									endPoint.y - startPoint.y);
							ListaDeiProcessi.setSelectedIfInRectangle(
								rectMultiSelection);
							ListaDeiCanali.setSelectedIfInRectangle();
							rectMultiSelection = null;
							spostaListaProcessi =
								ListaDeiProcessi.listSelectedProcess();
							if (!spostaListaProcessi.isEmpty()) {
								ClassEditorStatus = SPOSTA_MULTISELEZIONE;
								plugDataWin.getStatusBar().setText(
									"Selection ok.");
								externalRect =
									spostaListaProcessi.getExternalBounds();
							} else
								// Non è stato selezionato alcun processo.
								{
								ClassEditorStatus = ATTESA;
								plugDataWin.getStatusBar().setText(
									"Topology ready.");
							}
							repaint();
						} else {
							ClassEditorStatus = ATTESA;
							plugDataWin.getStatusBar().setText(
								"Topology ready.");
						}
						break;

					case SPOSTA_MULTISELEZIONE_FASE2 :
						// Con queste istruzioni, per spostare ancora gli oggetti
						// selezionati, l'utente è obbligato a passare con il
						// trascinamento del mouse all'interno della selezione.
						ClassEditorStatus = SPOSTA_MULTISELEZIONE;
						externalRect = spostaListaProcessi.getExternalBounds();
						//plugDataWin.getFileManager().setModificata(true);
//						core.internal.plugin.file.FileManager.setModificata(true);
						fileManager.setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);
						break;

					default :
						ClassEditorStatus = ATTESA;
						break;
				}
			}
		}

		/** Gestione del click sul mouse. */
		public void mouseClicked(MouseEvent e) {
			if (rettangolo.contains(updateGetPoint(e.getPoint()))){
				switch (ClassEditorStatus){
					
					case TEMPATTESA:
						ClassEditorStatus = ATTESA;
						break;
						
					case ATTESA:
						if (!e.isShiftDown()){
							// Non è stato premuto il tasto "MAIUSC", pertanto
							// devo eliminare qualunque precedente selezione.
							ListaDeiCanali.noSelected();
							ListaDeiProcessi.noSelected();
						}
						ProcessoCorrente = (ElementoProcesso)(ListaDeiProcessi.getElement(updateGetPoint(e.getPoint())));
						if (ProcessoCorrente != null){
							// E' stato selezionato un processo.
							if (!e.isShiftDown()){
								Point p = e.getPoint();
								spiazzamentoX=ProcessoCorrente.getTopX()-p.x;
								spiazzamentoY=ProcessoCorrente.getTopY()-p.y;
								ProcessoCorrente.setSelected(true);
								plugDataWin.getStatusBar().setText("Process " + ProcessoCorrente.getName() + " selected.");
							}
							else{
								// Avendo premuto il tasto "MAIUSC", devo selezionare (deselezionare)
								// un processo se deselezionato (selezionato).
								ProcessoCorrente.invSelected();
							}
							repaint();                            
							if (!ProcessoCorrente.isDummy()){
								if (e.getClickCount()>1){
									// Gestione del doppio click su un processo non dummy.
									FinestraProprietaProcesso = new FinestraElementoBoxTesto(ProcessoCorrente,g2,null,"Process Properties",0,plugData.getPlugDataManager());
								}
							}
						}
						else {
							// Non è stato selezionato alcun processo.
							CanaleCorrente = (ElementoCanale)(ListaDeiCanali.getElementSelected(updateGetPoint(e.getPoint())));
							if (CanaleCorrente != null){
								// E' stato selezionato un canale.
								if (!e.isShiftDown()){                            	
									CanaleCorrente.setSelected(true);
									plugDataWin.getStatusBar().setText("Channel " + CanaleCorrente.getName() + " selected.");
								}
								else{
									// Avendo premuto il tasto "MAIUSC", devo selezionare (deselezionare)
									// un canale se deselezionato (selezionato).
									CanaleCorrente.invSelected();
									plugDataWin.getStatusBar().setText("Clicked over " + CanaleCorrente.getName() + ".");
								}
								repaint();
								if (e.getClickCount()>1){
									// Gestione del doppio click su un canale.                                   
									FinestraProprietaCanale = new FinestraGraficoLinkTopology(CanaleCorrente,null,"Channel Properties",0,plugData.getPlugDataManager());
								}
							}
							else{
								// Non è stato selezionato né un processo né un canale.
								if (e.getClickCount()>1){
									// Gestione del doppio click sull'editor.
									FinestraProprietaEditor = new FinestraGraphEditor(rifEditor,null,"Editor Properties");
								}
							}
						}
						repaint();
						break;
						
					case DISEGNA_PROCESSO:
						break;
						
					case INSERIMENTO_CANALE:
						break;
						
					case DISEGNA_CANALE:
						break;
						
					default:
						// Si entra qui con lo stato SPOSTA_PROCESSO, MULTISELEZIONE,
						// SPOSTA_MULTISELEZIONE, SPOSTA_MULTISELEZIONE_FASE2 oppure,
						// al limite, con uno stato non previsto.                	
						// Tornare nello stato di attesa.
						ListaDeiProcessi.noSelected();
						ListaDeiCanali.noSelected();
						spostaListaProcessi = null;
						ClassEditorStatus = ATTESA;
						plugDataWin.getStatusBar().setText("Topology ready.");
						repaint(); 
						break;
						
				}
			}
		}
	}


	/** Gestione del movimento del mouse. */
	private final class ClassEditorMotionAdapter extends MouseMotionAdapter {
		/** Trascinamento del mouse. */
		public void mouseDragged(MouseEvent e) {
			Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
			((JPanel)e.getSource()).scrollRectToVisible(r);
			// Gestione degli eventi di trascinamento solo se
			// interni all'area di editing (delimitata da rettangolo).
			if (rettangolo.contains(updateGetPoint(e.getPoint()))) {
				switch (ClassEditorStatus) {

					case ATTESA :
						if (!e.isShiftDown()) {
							// Non è stato premuto il tasto "MAIUSC".
							ListaDeiCanali.noSelected();
							ListaDeiProcessi.noSelected();
							ProcessoCorrente =
								(ElementoProcesso) (ListaDeiProcessi
									.getElement(updateGetPoint(e.getPoint())));
							if (ProcessoCorrente != null) {
								Point p = e.getPoint();
								spiazzamentoX = ProcessoCorrente.getTopX() - p.x;
								spiazzamentoY = ProcessoCorrente.getTopY() - p.y;
								ProcessoCorrente.setSelected(true);
								plugDataWin.getStatusBar().setText(
									"Process "
										+ ProcessoCorrente.getName()
										+ " selected and moved.");
								ListaDeiCanali.updateListaCanalePosizione(ProcessoCorrente);
								ClassEditorStatus = SPOSTA_PROCESSO;
								//plugDataWin.getFileManager().setModificata(true);
//								core.internal.plugin.file.FileManager.setModificata(true);
								fileManager.setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);
								repaint();
							} 
							else {
								// Nessun processo selezionato.
								startPoint = updateGetPoint(e.getPoint());
								ClassEditorStatus = MULTISELEZIONE;
							}
						} 
						else {
							// E' stato premuto il tasto "MAIUSC".
							// Creazione della lista dei processi selezionati.
							spostaListaProcessi =
								ListaDeiProcessi.listSelectedProcess();
							if (!spostaListaProcessi.isEmpty()) {
								// E' stato selezionato almento un processo.
								ClassEditorStatus = SPOSTA_MULTISELEZIONE;
								externalRect = spostaListaProcessi.getExternalBounds();
								//plugDataWin.getFileManager().setModificata(true);
//								core.internal.plugin.file.FileManager.setModificata(true);
								fileManager.setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);
								} 
							else {
								ClassEditorStatus = ATTESA;
							}
							repaint();
						}
						break;

					case DISEGNA_CANALE :
						break;

					case SPOSTA_PROCESSO :
						// Aggiornamento della posizione del processo selezionato.
						Point p = e.getPoint();
						p.x = p.x + spiazzamentoX;
						p.y = p.y + spiazzamentoY;

						//unisco gli eventi di modifica
						if (ProcessoCorrente.getStato()) {
							ProcessoCorrente.informPreUpdate();
						}
						ProcessoCorrente.disable();
						ProcessoCorrente.setPoint(updateGetPoint(p));

						ListaDeiCanali.updateListaCanalePosizione(
							ProcessoCorrente);

						repaint();
						break;

					case MULTISELEZIONE :
						endPoint = updateGetPoint(e.getPoint());
						// Per selezionare gli oggetti il trascinamento del mouse deve 
						// avvenire da sinistra verso destra e dall'alto verso il basso.              			
						if ((endPoint.x > startPoint.x)
							&& (endPoint.y > startPoint.y)) {
							rectMultiSelection =
								new Rectangle2D.Double(
									startPoint.x,
									startPoint.y,
									endPoint.x - startPoint.x,
									endPoint.y - startPoint.y);
							repaint();
						}
						break;

					case SPOSTA_MULTISELEZIONE :
						// Inizia lo spostamento contemporaneo di più oggetti.
						// Durante il trascinamento lo stato SPOSTA_MULTISELEZIONE
						// viene attivato una sola volta.
						trackPoint = updateGetPoint(e.getPoint());
						if (externalRect.contains(trackPoint)) {
							// Punto di riferimento rispetto al quale calcolare lo spostamento.
							rifPoint = spostaListaProcessi.getRifPoint();
							ClassEditorStatus = SPOSTA_MULTISELEZIONE_FASE2;
						} 
						else {
							ProcessoCorrente =
								(ElementoProcesso) (ListaDeiProcessi
									.getElement(updateGetPoint(e.getPoint())));
							if (ProcessoCorrente != null) {
								// E' stato selezionato un processo esterno alla selezione, pertanto
								// si suppone che l'utente voglia muovere solo quel processo.
								ListaDeiCanali.noSelected();
								ListaDeiProcessi.noSelected();
								ProcessoCorrente.setSelected(true);
								ProcessoCorrente.setPoint(updateGetPoint(e.getPoint()));
								plugDataWin.getStatusBar().setText(
									"Process "
										+ ProcessoCorrente.getName()
										+ " selected and moved.");
								ListaDeiCanali.updateListaCanalePosizione(ProcessoCorrente);
								ClassEditorStatus = SPOSTA_PROCESSO;
								//plugDataWin.getFileManager().setModificata(true);
//								core.internal.plugin.file.FileManager.setModificata(true);
								fileManager.setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);
								repaint();
							}
						}
						break;

					case SPOSTA_MULTISELEZIONE_FASE2 :
						// Spostamento contemporaneo di più oggetti.
						// Durante il trascinamento rimane attivato lo stato
						// SPOSTA_MULTISELEZIONE_FASE2; ad ogni passaggio si aggiorna
						// la posizione di tutti gli oggetti interessati dallo spostamento.
						trackPoint = updateGetPoint(e.getPoint());
						spostaListaProcessi.updatePosition(
							rifPoint,
							trackPoint,
							ListaDeiCanali);
						rifPoint = trackPoint;
						plugDataWin.getStatusBar().setText("Selection moved.");
						//plugDataWin.getFileManager().setModificata(true);
//						core.internal.plugin.file.FileManager.setModificata(true);
						fileManager.setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);
						repaint();
						break;

					default :
						// Si entra qui con lo stato DISEGNA_PROCESSO o INSERIMENTO_CANALE
						// oppure, al limite, con uno stato non previsto.
						break;

				}
			}
		}
	}

	/** Restituisce un punto le cui coordinate sono quelle del punto in ingresso
		aggiornate tenendo conto dei fattori di scala per l'asse X e per l'asse Y. */
	private Point updateGetPoint(Point pnt) {
		Point pp =
			new Point(roundToInt(pnt.x / scaleX), roundToInt(pnt.y / scaleY));
		return pp;
	}

	/** Operazione di copy su processi e canali. */
	public void opCopy() {
//		ccpTopology.copy();
//		plugDataWin.getStatusBar().setText("<Copy> ok!!  Topology ready.");
	}

	/** Operazione di paste su processi e canali. */
	public void opPaste() {
//		int j;
//		boolean tmpboolean;
//		boolean listevuote = true;
//		String NomeProcesso;
//		String NomeCanale;
//		ElementoProcesso PasteProcesso;
//		ElementoCanale PasteCanale;
//		ccpTopology.paste();
//		repaint();
				
	}

	/** Operazione di cut. */
	public void opCut() {
//		LinkedList delListaCanali = null;
//		LinkedList delListaProcessi = null;
//		ListaCanale delextraListaCanali = null;
//		// Lista dei canali da eliminare.
//		delListaCanali = ListaDeiCanali.listSelectedChannel();
//		if ((delListaCanali != null) && (!delListaCanali.isEmpty())) {
//			// Eliminazione canali.
//			ListaDeiCanali.removeListeSelected(delListaCanali);
//		}
//		// Lista dei processi da eliminare.
//		delListaProcessi = ListaDeiProcessi.listSelectedProcess();
//		if ((delextraListaCanali != null)
//			&& (!delextraListaCanali.isEmpty())) {
//			// Eliminazione di canali per la precedente eliminazione di processi.
//			ListaDeiCanali.removeListeSelected(delextraListaCanali);
//		}
//		ClassEditorStatus = ATTESA;
//		tmpListaProcessi = delListaProcessi;
//		tmpListaCanali = delListaCanali;
//		plugDataWin.getStatusBar().setText("<Cut> ok!!  Topology ready.");
//		repaint();
	}


	/** Operazione di redo. */
	public void opRedo() {
		undoRedoManager.redo(scaleX, scaleY);
		ClassEditorStatus = ATTESA;
		rifEditor.setAllButtonNoPressed();
		plugDataWin.getStatusBar().setText("<Redo> ok!!  Ready.");
		repaint();
	}

	/** Operazione di undo sull'ultima operazione di del (per processi e canali). */
	public void opUndo() {
		undoRedoManager.undo(scaleX, scaleY);
		ClassEditorStatus = ATTESA;
		rifEditor.setAllButtonNoPressed();
		plugDataWin.getStatusBar().setText("<Undo> ok!!  Ready.");		
		repaint();
	}

	/** Operazione di cancellazione (del) di processi e/o canali. */
	public void opDel() {
		//plugData.getListaProcesso().removeAllSelected();
		//plugData.getListaCanale().removeAllSelected();
		//ClassEditorStatus = ATTESA;
		//plugDataWin.getStatusBar().setText("<Delete> ok!!  Topology ready.");
		//repaint();
	}


	/** Creazione dell'immagine jpeg del S_A_ Topology Diagram. */
	public void opImg() {
		boolean ctrlImage;
		JpegImage Immagine;
		Graphics2D imgG2D;
		Immagine =
			new JpegImage(0, 0, rWidth, rHeight, scaleX, scaleY, editorColor);
		imgG2D = Immagine.getImageGraphics2D();
		if (imgG2D != null) {
			ListaDeiCanali.paintLista(imgG2D);
			ListaDeiProcessi.paintLista(imgG2D);
			plugDataWin.getStatusBar().setText("Select file and wait.");
			ctrlImage = Immagine.saveImageFile((Frame) getTopLevelAncestor());
			if (ctrlImage) {
				plugDataWin.getStatusBar().setText(
					"Image saved.  Topology ready.");
			} 
			else {
				plugDataWin.getStatusBar().setText(
					"Image not saved.  Topology ready.");
			}
		}
	}

	/** Restituisce il vettore contenente i nomi di tutti i processi definiti_  
	    Nel vettore non sono inclusi i nomi dei processi 'dummy'. */
	public Vector getAllProcessName() {
		return ListaDeiProcessi.getAllProcessName();
	}

	/** Restituisce la lista di tutti i processi
		definiti nel S_A_ Topology Diagram. */
	public LinkedList getListaProcessi() {
		return ListaDeiProcessi.getListProcessNoDummy();
	}

	/** Restituisce la lista dei processi privata
		di quelli di tipo dummy. */
	public ListaProcesso getListaProcessoSenzaDummy() {
		return ListaDeiProcessi.getListaProcessoSenzaDummy();
	}

	/** Ripristina la scala del pannello al 100%. */
	public void resetScale() {
		super.resetScale();
		plugDataWin.getStatusBar().setText(
			"<Zoom Reset> ok!!  Topology ready.");
	}

	/** Operazione di zoom sull'asse X. */
	public void incScaleX() {
		super.incScaleX();
		plugDataWin.getStatusBar().setText(
			"<Stretch Horizontal> ok!!  Topology ready.");
	}

	/** Operazione di zoom negativo sull'asse X. */
	public void decScaleX() {
		super.decScaleX();
		plugDataWin.getStatusBar().setText(
			"<Compress Horizontal> ok!!  Topology ready.");
	}

	/** Operazione di zoom sull'asse Y. */
	public void incScaleY() {
		super.incScaleY();
		plugDataWin.getStatusBar().setText(
			"<Stretch Vertical> ok!!.  Topology ready.");
	}

	/** Operazione di zoom negativo sull'asse Y. */
	public void decScaleY() {
		super.decScaleY();
		plugDataWin.getStatusBar().setText(
			"<Compress Vertical> ok!!.  Topology ready.");
	}

	/** Restituisce la lista dei processi inseriti nel Topology Diagram. */
	public ListaProcesso getListaDeiProcessi() {
		return ListaDeiProcessi;
	}

	/** Imposta la lista dei processi del Topology Diagram. */
	public void setListaDeiProcessi(ListaProcesso lp) {
		ListaDeiProcessi = lp;
	}

	/** Restituisce la lista dei canali inseriti nel Topology Diagram. */
	public ListaCanale getListaDeiCanali() {
		return ListaDeiCanali;
	}

	/** Imposta la lista dei canali del Topology Diagram. */
	public void setListaDeiCanali(ListaCanale lc) {
		ListaDeiCanali = lc;
	}

	/** Metodo per svuotare la lista dei processi e dei canali_
		E' usato, ad esempio, dalle operazioni relative all'item 
		"New" del menu "File". */
	public void resetForNewFile() {
		ListaDeiProcessi.removeAll();
		ListaDeiCanali.removeAll();
		ClassEditorStatus = ATTESA;
		CanaleCorrente = null;
		ProcessoCorrente = null;
		ProcessFrom = null;
		ProcessTo = null;
		BloccoPulsante = false;
		//rifEditor.setAllButtonNoPressed();
		repaint();
	}

	public void restoreFromFile() {
		super.restoreFromFile();
	}

	/** Restituisce il nome di un canale se ne esiste almeno uno, altrimenti null. */
	public String getAnyNameChannel() {
		if (ListaDeiCanali == null)
			return null;
		if (ListaDeiCanali.isEmpty())
			return null;
		return ((ElementoCanale) (ListaDeiCanali.getElement(0))).getName();
	}

	public void setDeselectedAll(){
		rifEditor.getListaDeiProcessi().setUnselected();
		rifEditor.getListaDeiCanali().setUnselected();
		repaint();
	}


	/* (non-Javadoc)
	 * @see plugin.charmyui.extensionpoint.editor.IHostEditor#editorActive()
	 */
	public void editorActive() {
		// TODO Auto-generated method stub

	}
	/* (non-Javadoc)
	 * @see plugin.charmyui.extensionpoint.editor.IHostEditor#editorInactive()
	 */
	public void editorInactive() {
		// TODO Auto-generated method stub

	}


	public void notifyMessage(Object callerObject, int status, String message) {
		// TODO Auto-generated method stub
		
	}


	
}