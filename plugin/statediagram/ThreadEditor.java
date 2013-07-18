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
    
package plugin.statediagram;

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
import java.io.Serializable;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import core.internal.runtime.data.IPlugData;
import core.internal.ui.statusbar.StatusBar;
import core.internal.ui.toolbar.EditToolBar;
import core.resources.jpeg.JpegImage;
import core.resources.ui.WithGraphEditor;

import plugin.statediagram.data.*;
import plugin.statediagram.dialog.FinestraGraphEditor;
import plugin.statediagram.dialog.FinestraElementoStato;
import plugin.statediagram.dialog.FinestraGraficoLink;
import plugin.statediagram.general.undo.UndoRedoThreadElement;
import plugin.statediagram.graph.ElementoMessaggio;
import plugin.statediagram.toolbar.StateToolBar;
import plugin.topologydiagram.resource.utility.ListMovedObjects;

/**	Classe per la creazione e gestione del pannello su cui verr? disegnato
	lo State Diagram_ La classe controlla gli eventi del mouse (clicked,
	released, dragged), esegue le classiche operazioni di editing (del, copy,
	paste, undo) e di zoom, implementa le operazioni associate ai pulsanti
	della StateToolBar, etc. */

public class ThreadEditor 
	extends WithGraphEditor 
	implements Serializable {

	/** */
	private static long numIstanze = 0;

	private int spiazzamentoX = 0, spiazzamentoY = 0;
	
	
	/** */

	/**
	 * variabile utilizzata dalla gestione degli eventi del mouse
	 * per generare un solo messaggio di update(sia stato che altro)
	 */
	private boolean bo = false;

	static public long getNumIstanze() {
		return numIstanze;
	}

	/** */
	static public void setNumIstanze(long n) {
		numIstanze = n;
	}

	/** Numero Messaggi*/
	private long numMessaggio;

	public long getNumMessaggio() {
		return numMessaggio;
	}

	public void incNumMessaggio() {
		numMessaggio = numMessaggio + 1;
	}

	public void decNumMessaggio() {
		numMessaggio = numMessaggio - 1;
	}

	/** Costante per la condizione di attesa dell'input dell'utente. */
	public static final int ATTESA = 0;

	/** Costante per la situazione in cui l'utente sta 
		introducendo un nuovo stato. */
	public static final int DISEGNA_STATO = 1;

	/** Costante per la situazione in cui l'utente sta inserendo 
		un nuovo link di comunicazione tra stati. */
	public static final int INSERIMENTO_MESSAGGIO = 2;

	/** Costante per la situazione in cui si sta tracciando una 
		una linea relativa ad un link tra stati. */
	public static final int DISEGNA_MESSAGGIO = 3;

	/** Costante per la condizione in cui l'utente sta 
		spostando uno stato. */
	public static final int SPOSTA_STATO = 4;

	/** Costante per la condizione in cui l'utente seleziona pi? 
		oggetti tramite trascinamento del mouse. */
	public static final int MULTISELEZIONE = 5;

	/** Costante per la situazione in cui l'utente sposta pi? oggetti_
		Ci sono tre modi per attivare questa condizione:
		1) Rilascio mouse nella condizione MULTISELEZIONE;
		2) Trascinamento del mouse con pressione del tasto 
		   "MAIUSC" nello stato ATTESA;
		3) Operazione di paste. */
	public static final int SPOSTA_MULTISELEZIONE = 6;

	/** Costante per la situazione in cui l'utente ha gi? iniziato l'operazione di 
		spostamento di pi? oggetti_ Questa condizione viene attivata solo tramite 
		trascinamento del mouse nella situazione SPOSTA_MULTISELEZIONE. */
	public static final int SPOSTA_MULTISELEZIONE_FASE2 = 7;

	public static final int TEMPATTESA = 8;

	public static final int PRINT_AREA = 9;

	/** Riferimento alla barra di stato. */
	transient private StatusBar LocalClassStatus;

	public void setLocalClassStatus(String msg) {
		LocalClassStatus.setText(msg);
	}
	/** Riferimento alla toolbar. */
	transient private StateToolBar localStateToolBar;

	/** Riferimento alla toolbar. */
	transient private EditToolBar localEditToolBar;

	/** Lista degli stati disegnati. */
	private ListaStato ListaDegliStati;

	/** Lista dei link disegnati. */
	private ListaMessaggio ListaDeiMessaggi;

	/** Riferimento allo stato corrente.*/
	transient private ElementoStato StatoCorrente;

	/** Stato di partenza nell'operazione di
		inserimento di un nuovo link. */
	transient private ElementoStato StateFrom;

	/** Stato di arrivo nell'operazione di
		inserimento di un nuovo link. */
	transient private ElementoStato StateTo;

	/** Riferimento al link corrente.*/
	transient private ElementoMessaggio MessaggioCorrente;

	/** Memorizza lo stato dell'editor (ATTESA, DISEGNA_STATO,
		INSERIMENTO_MESSAGGIO, DISEGNA_MESSAGGIO, SPOSTA_STATO, etc.) */
	transient private int ThreadEditorStatus = 0;

	/** Memorizza il tipo di stato per una 
		successiva operazione di inserimento. */
	transient private int TipoStato = 0;

	/** Memorizza il tipo di link per una 
		successiva operazione di inserimento. */
	transient private int TipoMessaggio = 0;

	/** Assume il valore 'true' quando uno dei pulsanti di 
		inserimento stato (START, BUILD, END) o di inserimento 
		link (SIMPLE, SYN, ASYN, LOOP) risulta bloccato. */
	transient private boolean BloccoPulsante = false;

	/** Finestra di dialogo per impostare le propriet? di uno stato. */
	transient private FinestraElementoStato FinestraProprietaStato;

	/** Finestra di dialogo per impostare le propriet? di un link. */
	transient private FinestraGraficoLink FinestraProprietaMessaggio;

	/** Finestra di dialogo per impostare le propriet? dell'editor. */
	transient private FinestraGraphEditor FinestraProprietaEditor;

	/** Necessaria per l'implementazione. */
	transient private Graphics2D g2;

	/** Utilizzata per le operazioni di copy e paste sugli stati. */
	transient private LinkedList tmpListaStati = null;

	/** Utilizzata per le operazioni di copy e paste sui link. */
	transient private LinkedList tmpListaMessaggi = null;

	/** Utilizzata per memorizzare la lista degli stati nelle
		operazioni interessate da multiselezione_ Forse potrebbe 
		essere eliminata, riutilizzando tmpListaStati. */
	transient private ListMovedObjects spostaListaStati = null;

	/** Riferimento all'editor dello State Diagram, ovvero alla
		classe stessa_ E' usato all'interno delle classi nidificate 
		ClassEditorClickAdapter e ClassEditorMotionAdapter. */
	private ThreadEditor rifEditor;

	/** Punto di partenza in un'operazione di multiselezione
		tramite trascinamento del mouse. */
	transient private Point startPoint;

	/** Punto di arrivo in un'operazione di multiselezione
		tramite trascinamento del mouse. */
	transient private Point endPoint;

	/** Punto di posizionamento del mouse nello 
		spostamento contemporaneo di pi? oggetti. */
	transient private Point trackPoint;

	/** Punto di riferimento per lo spostamento 
		contemporaneo di pi? oggetti. */
	transient private Point rifPoint;

	/** Rettangolo visualizzato durante un'operazione di 
		trascinamento del mouse nello stato MULTISELEZIONE. */
	transient private Rectangle2D rectMultiSelection = null;

	/** Memorizza il pi? piccolo rettangolo contenente
		tutti i processi selezionati (multiselezione). */
	transient private Rectangle2D externalRect = null;


	public int xImg = 0, yImg = 0, widthImg = rWidth, heightImg = rHeight;

	/**
	 * elemento collegato al threadEditor
	 */

	private ThreadElement threadElement;

	/**
	 * riferimento al contenitore dei dati
	 */
	private IPlugData plugData;

	/**
	 * undo manager
	 */
	private UndoRedoThreadElement urThreadElement;


	/**
	 * 
	 * @param nomeEditor
	 * @param cs barra di stato
	 * @param lt lista thread interessata dall'elemento
	 * @param pl riferimento al contenitore dei dati
	 */

	/** Costruttore_ 
	 *	Prende in ingresso un riferimento alla barra di stato. 
	 *  La ListaThread di riferimento	
	 * se lt ? diverso da null, viene aggiunto alla lista Thread il ThreadElement relativo
	 */
	public ThreadEditor(
		String nomeEditor,
		StatusBar cs,
		ListaThread lt,
		IPlugData pl) {
		this( cs, lt, pl,new ThreadElement(nomeEditor, lt, pl)	);
	}

	/**
	 * costruttore con threadElement gia attivato
	 * @param nomeEditor
	 * @param cs
	 * @param lt
	 * @param pl
	 * @param te
	 */	
	public ThreadEditor(
			StatusBar cs,
			ListaThread lt,
			IPlugData pl, 
			ThreadElement te) {
		super(pl.getPlugDataManager().getFileManager());
		threadElement = te;
		numMessaggio = 0;
		numIstanze++;
		setName(te.getNomeThread());
		addMouseListener(new ClassEditorClickAdapter());
		addMouseMotionListener(new ClassEditorMotionAdapter());
		LocalClassStatus = cs;

		plugData = pl;
		startUndoRedo();
		
		if (lt != null) {
			lt.add(threadElement);
		}

		rifEditor = this;
	}
	
	public void startUndoRedo(){
		urThreadElement = new UndoRedoThreadElement();
		urThreadElement.setDati(null, plugData);
		threadElement.addListener(urThreadElement);
	}

	public StateToolBar getStateToolBar(){
		return localStateToolBar;
	}

	public void setStateToolBar(StateToolBar ctbar){
		localStateToolBar = ctbar;
	}
	
	/** Imposta il riferimento alle toolbar. */
	public void setToolBar(EditToolBar etbar, StateToolBar ctbar) {
		localStateToolBar = ctbar;
		localEditToolBar = etbar;
		localEditToolBar.setButtonEnabled("Copy", true);
		localEditToolBar.setButtonEnabled("Paste", true);
		localEditToolBar.setButtonEnabled("Del", true);
		localEditToolBar.setButtonEnabled("Cut", true);
		localEditToolBar.setButtonEnabled("Undo", true);
		localEditToolBar.setButtonEnabled("Redo", true);
	}

	/** Restituisce la condizione dell'editor. */
	public int getEditorStatus() {
		return ThreadEditorStatus;
	}

	/** Imposta la situazione dell'editor_ Si osservi che solo
		alcune condizioni possono essere assegnati dall'esterno:
		DISEGNA_STATO, INSERIMENTO_MESSAGGIO, DISEGNA_MESSAGGIO. */
	public void setEditorStatus(int j, int tipo, boolean ctrlpulsante) {
		BloccoPulsante = ctrlpulsante;
		switch (j) {
			case DISEGNA_STATO :
				ThreadEditorStatus = DISEGNA_STATO;
				TipoStato = tipo;
				break;
			case INSERIMENTO_MESSAGGIO :
				ThreadEditorStatus = INSERIMENTO_MESSAGGIO;
				TipoMessaggio = tipo;
				break;
			case DISEGNA_MESSAGGIO :
				ThreadEditorStatus = DISEGNA_MESSAGGIO;
				TipoMessaggio = tipo;
				break;
			case PRINT_AREA :
				ThreadEditorStatus = PRINT_AREA;
				break;
			default :
				ThreadEditorStatus = ATTESA;
				break;
		}
	}

	/** "Stampa" l'editor con stati e link. */
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
		threadElement.getListaMessaggio().paintLista(g2);
		threadElement.getListaStato().paintLista(g2);
	}

	/** Classe per la gestione della pressione dei tasti del mouse. */
	private final class ClassEditorClickAdapter extends MouseAdapter {
		
		/** Gestione del click sul mouse. 
		 * Dispatcher
		 * */
		public void mousePressed(MouseEvent e) {
			if (rettangolo.contains(updateGetPoint(e.getPoint()))) {

				switch (ThreadEditorStatus) {

					case TEMPATTESA :
						ThreadEditorStatus = ATTESA;
						break;
					case PRINT_AREA :  //disabilitato nella nuova verisione
						casePresPrintArea(e);
						repaint();
						break;
					case ATTESA :
						casePresAttesa(e);
						repaint();
						break;

					case DISEGNA_STATO :
						casePresDisegnaStato(e);
						break;

					case INSERIMENTO_MESSAGGIO :
						casePresInserimentoMsg(e);
						break;

					case DISEGNA_MESSAGGIO :
						casePresDisegnaMessaggio(e);
						break;

					default :
						casePresDefault(e);
						break;
				}
			}
		}
		
		
	/** Rilascio del mouse. */
	public void mouseReleased(MouseEvent e) {
		if (rettangolo.contains(updateGetPoint(e.getPoint()))) {
			switch (ThreadEditorStatus) {

				case DISEGNA_STATO :
					caseRelDisegnaStato(e);
					break;

				case INSERIMENTO_MESSAGGIO :
					caseRelInserimentoMsg(e);
					break;

				case DISEGNA_MESSAGGIO :
					caseRelDisegnaMessaggio(e);
					break;

				case SPOSTA_STATO :
					caseRelSpostaStato(e);
					break;

				case MULTISELEZIONE :
					caseRelMultiSelezione(e);
					break;

				case SPOSTA_MULTISELEZIONE_FASE2 :
					caseRelSpostaMultiSelezione2(e);
					break;

				default :
					ThreadEditorStatus = ATTESA;
					break;
			}
		}
	}

		
		
		/**
		 * gestione PrintArea con pressione pulsante mouse
		 */
		private void caseRelPrintArea(MouseEvent e) {
		}

		/**
		 * caso Attesa
		 */
		private void caseRelAttesa(MouseEvent e) {
		}

		/**
		 * stato disegnaStato
		 * @param evento del mouse
		 */
		private void caseRelDisegnaStato(MouseEvent e) {
			threadElement.getListaStato().noSelected();
			threadElement.getListaMessaggio().noSelected();
			// Creazione ed inserimento di un nuovo stato. 
			if ((threadElement.getListaStato().startExist()) && (TipoStato == 0)) {
				JOptionPane.showMessageDialog(
					null,
					"Start state already exists");
				ThreadEditorStatus = ATTESA;
			} else {
				threadElement.incNumStato();
				//incNumStato();
				if(threadElement.getListaStato().giaPresente("S"+threadElement.getNumStato()))
				{
					StatoCorrente =
						new ElementoStato(
							updateGetPoint(e.getPoint()),
							TipoStato,
							"S" + threadElement.getNumStato()+threadElement.getNumStato(),
							null);
				}else{
					StatoCorrente =
						new ElementoStato(
							updateGetPoint(e.getPoint()),
							TipoStato,
							"S" + threadElement.getNumStato(),
							null);
				}
				// Aggiornamento della barra di stato.                     
				LocalClassStatus.setText(
					"State "
						+ StatoCorrente.getName()
						+ " inserted.");
				if(StatoCorrente.getTipo()==0)
					localStateToolBar.setEnabledStartButton(false);
				if (!BloccoPulsante) {
					ThreadEditorStatus = TEMPATTESA;
					localStateToolBar.setNoPressed();
				}
				threadElement.getListaStato().addElement(
					StatoCorrente);
				repaint();
			}

		}
		/**
		 * Inserimento messaggio
		 * @param e evento del mouse
		 */
		private void caseRelInserimentoMsg(MouseEvent e) {
			// Selezionato solo lo stato di partenza del nuovo link.
			threadElement.getListaMessaggio().noSelected();
			threadElement.getListaStato().noSelected();
			StateFrom =
				(ElementoStato) (threadElement
					.getListaStato()
					.getElement(updateGetPoint(e.getPoint())));
			if (StateFrom != null) {
				LocalClassStatus.setText(
					"State "
						+ StateFrom.getName()
						+ " selected."
						+ "  Click over another state to insert a link.");
				StateFrom.setSelected(true);
				ThreadEditorStatus = DISEGNA_MESSAGGIO;
			} else if (!BloccoPulsante)
				ThreadEditorStatus = ATTESA;
			repaint();
		}

		
		/**
		 * DisegnaMessaggio
		 * @param e
		 */
		private void caseRelDisegnaMessaggio(MouseEvent e) {
			// Creazione ed inserimento di un nuovo link.
			StateTo =
				(ElementoStato) (threadElement
					.getListaStato()
					.getElement(updateGetPoint(e.getPoint())));
			if (StateFrom != null) {
				if ((StateTo != null)
					&& (!(StateTo.equals(StateFrom)))) {
					incNumMessaggio();
					MessaggioCorrente =
						new ElementoMessaggio(
							threadElement,
							StateFrom,
							StateTo,
							TipoMessaggio,
							"link_" + getNumMessaggio(),
							null);
					threadElement.getListaMessaggio().addElement(
						MessaggioCorrente);
					// Aggiornamento della barra di stato.                     
					LocalClassStatus.setText(
						"Link "
							+ MessaggioCorrente.getName()
							+ " inserted.");						
				} else {
					LocalClassStatus.setText(
						"Link not inserted.  Ready.");
				}
				StateFrom.setSelected(false);
				ThreadEditorStatus = INSERIMENTO_MESSAGGIO;
			} else {
				if (StateTo != null) {
					incNumMessaggio();
					MessaggioCorrente =
						new ElementoMessaggio(
							threadElement,
							StateTo,
							StateTo,
							TipoMessaggio,
							"link_" + getNumMessaggio(),
							null);
					threadElement.getListaMessaggio().addElement(
						MessaggioCorrente);
					// Aggiornamento della barra di stato.                     
					LocalClassStatus.setText(
						"Link "
							+ MessaggioCorrente.getName()
							+ " inserted.");
				}
			}
			StateTo = null;
			StateFrom = null;
			if (!BloccoPulsante) {
				ThreadEditorStatus = TEMPATTESA;
				localStateToolBar.setNoPressed();
			}
			repaint();
		}

		
		
		/**
		 * spostamento stato,rilascio pulsante
		 * @param e vento mouse
		 */
		private void caseRelSpostaStato(MouseEvent e){
			ThreadEditorStatus = ATTESA;
			/*
			 * genero un evento di post update
			 */
			localStateToolBar.setNoPressed();
			StatoCorrente.testAndReset(bo);
			bo = false;

			LocalClassStatus.setText(
				"State "
					+ StatoCorrente.getName()
					+ " selected.  Ready.");
		}
		

		/**
		 * rilascio multiselezione fase 1
		 * @param e
		 */
		private void caseRelMultiSelezione(MouseEvent e){
			endPoint = updateGetPoint(e.getPoint());
			if (endPoint.x > startPoint.x)
				if (endPoint.y > startPoint.y)
					rectMultiSelection =
						new Rectangle2D.Double(
							startPoint.x,
							startPoint.y,
							endPoint.x - startPoint.x,
							endPoint.y - startPoint.y);
				else
					rectMultiSelection =
						new Rectangle2D.Double(
							startPoint.x,
							endPoint.y,
							endPoint.x - startPoint.x,
							startPoint.y - endPoint.y);
			else if (endPoint.y > startPoint.y)
				rectMultiSelection =
					new Rectangle2D.Double(
						endPoint.x,
						startPoint.y,
						startPoint.x - endPoint.x,
						endPoint.y - startPoint.y);
			else
				rectMultiSelection =
					new Rectangle2D.Double(
						endPoint.x,
						endPoint.y,
						startPoint.x - endPoint.x,
						startPoint.y - endPoint.y);
			threadElement.getListaStato().setSelectedIfInRectangle(rectMultiSelection);
			threadElement.getListaMessaggio().setSelectedIfInRectangle();
			rectMultiSelection = null;
			spostaListaStati = threadElement.getListaStato().listSelectedProcess();
			if (!spostaListaStati.isEmpty()) {
				ThreadEditorStatus = SPOSTA_MULTISELEZIONE;
				LocalClassStatus.setText("Selection ok!!  Ready.");
				externalRect = spostaListaStati.getExternalBounds();
			} else
				// Non è stato selezionato alcuno stato.
				{
				ThreadEditorStatus = ATTESA;
				LocalClassStatus.setText("Ready.");
			}
			repaint();
		}
		
		/**
		 * rilascio multiselezione fase2
		 * @param e
		 */
		private void caseRelSpostaMultiSelezione2(MouseEvent e){
			// Con queste istruzioni, per spostare ancora gli oggetti
			// selezionati, l'utente ? obbligato a passare con il
			// trascinamento del mouse all'interno della selezione.
			ThreadEditorStatus = SPOSTA_MULTISELEZIONE;
			externalRect = spostaListaStati.getExternalBounds();
			localStateToolBar.setNoPressed();
		}
		
		

	/**
	 * gestione PrintArea con pressione pulsante mouse
	 */
	private void casePresPrintArea(MouseEvent e) {
		endPoint = updateGetPoint(e.getPoint());
		widthImg = endPoint.x;
		heightImg = endPoint.y;
		rectMultiSelection = new Rectangle2D.Double(0, 0, endPoint.x, endPoint.y);
		ThreadEditorStatus = ATTESA;
	}

		/**
		 * caso Attesa
		 */
		private void casePresAttesa(MouseEvent e) {
			if (!e.isShiftDown()) {
				// Non ? stato premuto il tasto "MAIUSC", pertanto
				// devo eliminare qualunque precedente selezione.

				threadElement.getListaMessaggio().noSelected();
				threadElement.getListaStato().noSelected();
			}
			StatoCorrente =
				(ElementoStato) (threadElement
					.getListaStato()
					.getElement(updateGetPoint(e.getPoint())));
			if (StatoCorrente != null) {
				// E' stato selezionato uno stato.
				if (!e.isShiftDown()) {
					Point p = e.getPoint();
					spiazzamentoX = StatoCorrente.getTopX() - p.x;
					spiazzamentoY = StatoCorrente.getTopY() - p.y;
					StatoCorrente.setSelected(true);
					LocalClassStatus.setText("State " + StatoCorrente.getName() + " selected.");
				} else {
					// Avendo premuto il tasto "MAIUSC", devo selezionare (deselezionare)
					// uno stato se deselezionato (selezionato).
					StatoCorrente.invSelected();
					LocalClassStatus.setText(
						"Clicked over " + StatoCorrente.getName() + ".");
				}
				repaint();
				if (e.getClickCount() > 1) {
					// Gestione del doppio click su uno stato.                               
					FinestraProprietaStato = 
					new FinestraElementoStato(threadElement.getListaStato(),StatoCorrente,g2,null,"State Properties",rifEditor,plugData.getPlugDataManager());
					
					threadElement
						.getListaMessaggio()
						.updateListaCanalePosizione(
						StatoCorrente);
				}
			} else {
				// Non ? stato selezionato alcuno stato.
				MessaggioCorrente =
					(ElementoMessaggio) (threadElement
						.getListaMessaggio()
						.getElementSelected(updateGetPoint(e.getPoint())));
				if (MessaggioCorrente != null) {
					// E' stato selezionato un link.
					if (!e.isShiftDown()) {
						MessaggioCorrente.setSelected(true);
						LocalClassStatus.setText(
							"Link "
								+ MessaggioCorrente.getName()
								+ " selected.");
					} else {
						// Avendo premuto il tasto "MAIUSC", devo selezionare (deselezionare)
						// un link se deselezionato (selezionato).
						MessaggioCorrente.invSelected();
						LocalClassStatus.setText(
							"Clicked over "
								+ MessaggioCorrente.getName()
								+ ".");
					}
					repaint();
					if (e.getClickCount() > 1) {
						// Gestione del doppio click su un link.
						if ((MessaggioCorrente.getElementFrom())
							.equals(MessaggioCorrente.getElementTo())) {
							FinestraProprietaMessaggio = 
							    new FinestraGraficoLink(
							    		MessaggioCorrente,
							    		null,
										"Link Properties",
										2,
										ListaDeiMessaggi,plugData.getPlugDataManager());
						} else {
							FinestraProprietaMessaggio = 
								new FinestraGraficoLink(
										MessaggioCorrente,
										null,
										"Link Properties",
										1,
										ListaDeiMessaggi,plugData.getPlugDataManager());
						}
					}
				} else {
					// Non ? stato selezionato n? uno stato n? un link.
					if (e.getClickCount() > 1) {
						// Gestione del doppio click sull'editor.
						FinestraProprietaEditor =
							new FinestraGraphEditor(
								rifEditor,
								null,
								"Editor Properties",plugData.getPlugDataManager().getFileManager());
					}
					LocalClassStatus.setText("Ready.");
				}
			}
		}


		/**
		 * stato disegnaStato
		 * @param evento del mouse
		 */
		private void casePresDisegnaStato(MouseEvent e) {
		}
		/**
		 * Inserimento messaggio
		 * @param e evento del mouse
		 */
		private void casePresInserimentoMsg(MouseEvent e) {
		}

		/**
		 * DisegnaMessaggio
		 * @param e
		 */
		private void casePresDisegnaMessaggio(MouseEvent e) {
		}

		private void casePresDefault(MouseEvent e) {
			// Si entra qui con la condizione SPOSTA_STATO, MULTISELEZIONE,
			// SPOSTA_MULTISELEZIONE, SPOSTA_MULTISELEZIONE_FASE2 oppure,
			// al limite, con una condizione non prevista.                	
			// Tornare nella condizione di attesa.
			threadElement.getListaStato().noSelected();
			threadElement.getListaMessaggio().noSelected();
			spostaListaStati = null;
			ThreadEditorStatus = ATTESA;
			LocalClassStatus.setText("Ready.");
			repaint();
		}


	}

	/** Gestione del movimento del mouse. */
	private final class ClassEditorMotionAdapter extends MouseMotionAdapter {

		/** Trascinamento del mouse.
		 * dispatcher per gli eventi gestiti
		 *  */
		public void mouseDragged(MouseEvent e) {
			Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
			((JPanel)e.getSource()).scrollRectToVisible(r);
			// Gestione degli eventi di trascinamento solo se
			// interni all'area di editing (delimitata da rettangolo).
			if (rettangolo.contains(updateGetPoint(e.getPoint()))) {
				switch (ThreadEditorStatus) {

					case ATTESA :
						caseAttesa(e);
						break;

					case DISEGNA_MESSAGGIO :
						break;

					case SPOSTA_STATO :
						caseSpostaStato(e);
						break;

					case MULTISELEZIONE :
						caseMultiSelezione(e);
						break;

					case SPOSTA_MULTISELEZIONE :
						caseSpostaMultiSelezione(e);
						break;

					case SPOSTA_MULTISELEZIONE_FASE2 :
						caseSpostaMultiSelezione2(e);
						break;

					default :
						break;
				}
				repaint();
			}
		}

		/**
		 * funzioni relative al trascinamento del muose
		 */

		/**
		 * gestione del caso ATTESA di un qualche movimento
		 */
		private void caseAttesa(MouseEvent e) {
			if (!e.isShiftDown()) {
				// Non ? stato premuto il tasto "MAIUSC".
				threadElement.getListaMessaggio().noSelected();
				threadElement.getListaStato().noSelected();
				StatoCorrente =
					(ElementoStato) (threadElement
						.getListaStato()
						.getElement(updateGetPoint(e.getPoint())));
				if (StatoCorrente != null) {
					Point p = e.getPoint();
					spiazzamentoX = StatoCorrente.getTopX() - p.x;
					spiazzamentoY = StatoCorrente.getTopY() - p.y;
					p.x = p.x + spiazzamentoX;
					p.y = p.y + spiazzamentoY;

					/*
					 *disattivazione messaggi, permette di generare un solo messaggio 
					 *di update
					 */
					if (!bo) {
						bo = StatoCorrente.testAndSet();
					}
					StatoCorrente.setSelected(true);
					LocalClassStatus.setText(
						"State "
							+ StatoCorrente.getName()
							+ " selected and moved.");
					threadElement
						.getListaMessaggio()
						.updateListaCanalePosizione(
						StatoCorrente);
					ThreadEditorStatus = SPOSTA_STATO;
				} else {
					// Nessuno stato selezionato.
					startPoint = updateGetPoint(e.getPoint());
					ThreadEditorStatus = MULTISELEZIONE;
				}
			} else {
				// E' stato premuto il tasto "MAIUSC".
				// Creazione della lista dei processi selezionati.
				spostaListaStati =
					threadElement.getListaStato().listSelectedProcess();
				if (!spostaListaStati.isEmpty()) {
					// E' stato selezionato almento un processo.
					ThreadEditorStatus = SPOSTA_MULTISELEZIONE;
					externalRect = spostaListaStati.getExternalBounds();
				} else {
					ThreadEditorStatus = ATTESA;
				}
			}

		}

		/**
		 * gestione Caso PrintAREA
		 */
		private void casePrintAREA(MouseEvent e) {
			endPoint = updateGetPoint(e.getPoint());
			if (endPoint.x > startPoint.x)
				if (endPoint.y > startPoint.y)
					rectMultiSelection =
						new Rectangle2D.Double(
							startPoint.x,
							startPoint.y,
							endPoint.x - startPoint.x,
							endPoint.y - startPoint.y);
				else
					rectMultiSelection =
						new Rectangle2D.Double(
							startPoint.x,
							endPoint.y,
							endPoint.x - startPoint.x,
							startPoint.y - endPoint.y);
			else if (endPoint.y > startPoint.y)
				rectMultiSelection =
					new Rectangle2D.Double(
						endPoint.x,
						startPoint.y,
						startPoint.x - endPoint.x,
						endPoint.y - startPoint.y);
			else
				rectMultiSelection =
					new Rectangle2D.Double(
						endPoint.x,
						endPoint.y,
						startPoint.x - endPoint.x,
						startPoint.y - endPoint.y);
			;
		}

		/**
		 * gestione Spostamento Stato
		 */
		private void caseSpostaStato(MouseEvent e) {
			// Aggiornamento della posizione del processo selezionato.
			Point p = e.getPoint();
			p.x = p.x + spiazzamentoX;
			p.y = p.y + spiazzamentoY;
			StatoCorrente.setPoint(updateGetPoint(p));
			threadElement.getListaMessaggio().updateListaCanalePosizione(StatoCorrente);
		}

		/**
		 * gestione multiselezione
		 */
		private void caseMultiSelezione(MouseEvent e) {
			endPoint = updateGetPoint(e.getPoint());
			// Per selezionare gli oggetti il trascinamento del mouse deve 
			// avvenire da sinistra verso destra e dall'alto verso il basso. 
			if (endPoint.x > startPoint.x)
				if (endPoint.y > startPoint.y)
					rectMultiSelection =
						new Rectangle2D.Double(
							startPoint.x,
							startPoint.y,
							endPoint.x - startPoint.x,
							endPoint.y - startPoint.y);
				else
					rectMultiSelection =
						new Rectangle2D.Double(
							startPoint.x,
							endPoint.y,
							endPoint.x - startPoint.x,
							startPoint.y - endPoint.y);
			else if (endPoint.y > startPoint.y)
				rectMultiSelection =
					new Rectangle2D.Double(
						endPoint.x,
						startPoint.y,
						startPoint.x - endPoint.x,
						endPoint.y - startPoint.y);
			else
				rectMultiSelection =
					new Rectangle2D.Double(
						endPoint.x,
						endPoint.y,
						startPoint.x - endPoint.x,
						startPoint.y - endPoint.y);
		}

		/**
		 * Sposta Multiselezione fase 1
		 */
		private void caseSpostaMultiSelezione(MouseEvent e) {
			// Inizia lo spostamento contemporaneo di pi? oggetti.
			// Durante il trascinamento la condizione SPOSTA_MULTISELEZIONE
			// viene attivata una sola volta.
			trackPoint = updateGetPoint(e.getPoint());
			if (externalRect.contains(trackPoint)) {
				// Punto di riferimento rispetto al quale calcolare lo spostamento.
				rifPoint = spostaListaStati.getRifPoint();
				ThreadEditorStatus = SPOSTA_MULTISELEZIONE_FASE2;
			} else {
				StatoCorrente =
					(ElementoStato) (threadElement
						.getListaStato()
						.getElement(updateGetPoint(e.getPoint())));
				if (StatoCorrente != null) {
					// E' stato selezionato uno stato esterno alla selezione, pertanto
					// si suppone che l'utente voglia muovere solo quello stato.
					threadElement.getListaMessaggio().noSelected();
					threadElement.getListaStato().noSelected();
					StatoCorrente.setSelected(true);
					StatoCorrente.setPoint(updateGetPoint(e.getPoint()));
					LocalClassStatus.setText(
						"State "
							+ StatoCorrente.getName()
							+ " selected and moved.");
					threadElement
						.getListaMessaggio()
						.updateListaCanalePosizione(
						StatoCorrente);
					ThreadEditorStatus = SPOSTA_STATO;
				}
			}

		}

		/**
		 * Sposta Multiselezione fase 2
		 */
		private void caseSpostaMultiSelezione2(MouseEvent e) {
			// Spostamento contemporaneo di pi? oggetti.
			// Durante il trascinamento rimane attivato la condizione
			// SPOSTA_MULTISELEZIONE_FASE2; ad ogni passaggio si aggiorna
			// la posizione di tutti gli oggetti interessati dallo spostamento.
			trackPoint = updateGetPoint(e.getPoint());
			spostaListaStati.updatePosition(
				rifPoint,
				trackPoint,
				threadElement.getListaMessaggio());
			rifPoint = trackPoint;
			LocalClassStatus.setText("Selection moved.");
		}

		/**
		 * gestione default
		 */
		private void caseDefault(MouseEvent e) {
			// Si entra qui con la condizione DISEGNA_STATO o INSERIMENTO_MESSAGGIO
			// oppure, al limite, con una condizione non previsto.
			localStateToolBar.setNoPressed();
			ThreadEditorStatus = ATTESA;
			LocalClassStatus.setText("Ready.");
			StatoCorrente =
				(ElementoStato) (ListaDegliStati
					.getElement(updateGetPoint(e.getPoint())));
			if (StatoCorrente != null) {
				StatoCorrente.setSelected(true);
				LocalClassStatus.setText(
					"State " + StatoCorrente.getName() + " selected.");
				StatoCorrente.setPoint(updateGetPoint(e.getPoint()));
				ListaDeiMessaggi.updateListaCanalePosizione(StatoCorrente);
				ThreadEditorStatus = SPOSTA_STATO;
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

	/** Operazione di copy su stati e link. */
	public void opCopy() {
		tmpListaStati = threadElement.getListaStato().listSelectedProcess();
		tmpListaMessaggi =
			threadElement.getListaMessaggio().listSelectedChannel();
		LocalClassStatus.setText("<Copy> ok!!  Ready.");

	}

	/** Operazione di paste su stati e link. */
	public void opPaste() {

		//Controllare anche numero di messaggi e di stati
		int j;
		boolean tmpboolean;
		boolean listevuote = true;
		String NomeStato;
		String NomeMessaggio;
		ElementoStato PasteStato;
		ElementoMessaggio PasteMessaggio;

		threadElement.getListaStato().noSelected();
		threadElement.getListaMessaggio().noSelected();
		// Operazione di paste sugli stati.
		j = 0;
		if (tmpListaStati != null) {
			if (!tmpListaStati.isEmpty()) {
				listevuote = false;
			}
			while (j < tmpListaStati.size()) {
				PasteStato = (ElementoStato) tmpListaStati.get(j);
				tmpboolean =
					threadElement.getListaStato().addPasteElement(PasteStato);
				j++;
			}
		}
		repaint();
	}

	/** Operazione di cut. */
	public void opCut() {
		LinkedList delListaMessaggi = null;
		LinkedList delListaStati = null;
		ListaMessaggio delextraListaMessaggi = null;

		// Lista dei link da eliminare.
		delListaMessaggi =
			threadElement.getListaMessaggio().listSelectedChannel();
		if ((delListaMessaggi != null) && (!delListaMessaggi.isEmpty())) {
			// Eliminazione canali.
			threadElement.getListaMessaggio().removeListeSelected(
				delListaMessaggi);
			for (int num = 0; num < delListaMessaggi.size(); num++)
				decNumMessaggio();
		}
		// Lista degli stati da eliminare.
		delListaStati = threadElement.getListaStato().listSelectedProcess();
		if ((delListaStati != null) && (!delListaStati.isEmpty())) {
			// Eliminazione processi e creazione della lista
			// dei link da cancellare a causa dell'eliminazione 
			// dello stato di partenza e/o di arrivo.
			delextraListaMessaggi =
				threadElement.getListaStato().removeListSelected(
					delListaStati,
					threadElement.getListaMessaggio());
			ElementoStato elementoStato;
			for (int num = 0; num < delListaStati.size(); num++) {
				//decNumStato();
				threadElement.decNumStato();
				elementoStato = (ElementoStato) delListaStati.get(num);
			}
		}
		if ((delextraListaMessaggi != null)
			&& (!delextraListaMessaggi.isEmpty())) {
			// Eliminazione di canali per la precedente eliminazione di processi.
			threadElement.getListaMessaggio().removeListeSelected(
				delextraListaMessaggi);
			for (int num = 0; num < delextraListaMessaggi.size(); num++)
				decNumMessaggio();
		}
		ThreadEditorStatus = ATTESA;
		tmpListaStati = delListaStati;
		tmpListaMessaggi = delListaMessaggi;
		LocalClassStatus.setText("<Cut> ok!!  Ready.");		
		repaint();
	}

	/** Operazione di redo. */
	public void opRedo() {
		//Controllare numeri messaggi e stati
			urThreadElement.redo(this.scaleX, this.scaleY);
			ThreadEditorStatus = ATTESA;
			LocalClassStatus.setText("<Redo> ok!!  Ready.");
			repaint();
	}

	/** Operazione di undo sull'ultima operazione di del (per processi e canali). */
	public void opUndo() {
		urThreadElement.undo(this.scaleX, this.scaleY);
		ThreadEditorStatus = ATTESA;
		LocalClassStatus.setText("<Undo> ok!!  Ready.");
		repaint();
	}

	/** Operazione di cancellazione (del) di stati e/o link. */
	public void opDel() {
		threadElement.getListaStato().removeAllSelected();
		if(threadElement.getListaStato().startExist())
			localStateToolBar.setEnabledStartButton(false);
		else
			localStateToolBar.setEnabledStartButton(true);
		repaint();
		threadElement.getListaMessaggio().removeAllSelected();
	}

			

	/** Creazione dell'immagine jpeg del S_A_ Topology Diagram. */
	public void opImg() {
		boolean ctrlImage;
		JpegImage Immagine;
		Graphics2D imgG2D;

		Immagine =
			new JpegImage(
				0,
				0,
				widthImg,
				heightImg,
				scaleX,
				scaleY,
				editorColor);
		imgG2D = Immagine.getImageGraphics2D();

		if (imgG2D != null) {
			threadElement.getListaMessaggio().paintLista(imgG2D);
			threadElement.getListaStato().paintLista(imgG2D);
			LocalClassStatus.setText("Select file and wait.");
			ctrlImage = Immagine.saveImageFile((Frame) getTopLevelAncestor());
			if (ctrlImage) {
				LocalClassStatus.setText("Image saved.  Ready.");
			} else {
				LocalClassStatus.setText("Image not saved.  Ready.");
			}
		}
	}

	/** Ripristina la scala del pannello al 100%. */
	public void resetScale() {
		super.resetScale();
		LocalClassStatus.setText("<Zoom Reset> ok!!  Ready.");
	}

	/** Operazione di zoom sull'asse X. */
	public void incScaleX() {
		super.incScaleX();
		LocalClassStatus.setText("<Stretch Horizontal> ok!!  Ready.");
	}

	/** Operazione di zoom negativo sull'asse X. */
	public void decScaleX() {
		super.decScaleX();
		LocalClassStatus.setText("<Compress Horizontal> ok!!  Ready.");
	}

	/** Operazione di zoom sull'asse Y. */
	public void incScaleY() {
		super.incScaleY();
		LocalClassStatus.setText("<Stretch Vertical> ok!!  Ready.");
	}

	/** Operazione di zoom negativo sull'asse Y. */
	public void decScaleY() {
		super.decScaleY();
		LocalClassStatus.setText("<Compress Vertical> ok!!  Ready.");
	}

	/** Restituisce un riferimento alla lista dei messaggi. */
	public ListaMessaggio getListaMessaggio() {
		return threadElement.getListaMessaggio();
	}

	/** Restituisce un riferimento alla lista degli stati. */
	public ListaStato getListaStato() {
		return threadElement.getListaStato();
	}

	/** Imposta la lista dei messaggi. */
	public void setListaMessaggio(ListaMessaggio LM) {
		ListaDeiMessaggi = LM;
	}

	/** Imposta la lista degli stati. */
	public synchronized void setListaStato(ListaStato LS) {
		threadElement.setListaStato(LS);
	}

	public int getTipoStato()
	{
		return TipoStato;
	}
	
	public int getTipoMessaggio()
	{
		return TipoMessaggio;
	}
	
	/** Restituisce la lista di tutti i messaggi collegati 
		allo stato passato in ingresso. */
	public LinkedList getListaMessaggio(ElementoStato stato) {
		return threadElement.getListaMessaggio().getListaMessaggio(stato);
	}

	
	/** Restituisce la lista dei nomi di tutti  
	 i messaggi usati nel thread. */
	public LinkedList getAllMessageName()
	{
		return (threadElement.getListaMessaggio().getAllMessageName());
	}	

	/** Restituisce la lista di tutti  
	 i messaggi usati nel thread. */
	public LinkedList getAllMessages()
	{
		return (threadElement.getListaMessaggio().getAllMessages());
	}        
	
	
	/**
	 * Ritorna il ThreadElement associato all'editor
	 * @return
	 */
	public ThreadElement getThreadElement() {
		return threadElement;
	}

	/**
	 * Setta il ThreadElement associato all'editor
	 * @param element
	 */
	public void setThreadElement(ThreadElement element) {
		threadElement = element;
	}

	public void setDeselectedAll(){
		threadElement.getListaStato().setUnselected();
		threadElement.getListaMessaggio().setUnselected();
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
}