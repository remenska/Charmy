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
    
	package plugin.sequencediagram;
	import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.LinkedList;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import plugin.sequencediagram.dialog.FinestraElementoClasse;
import plugin.statediagram.graph.ElementoMessaggio;
import plugin.topologydiagram.dialog.FinestraGraphEditor;
import plugin.sequencediagram.controllo.ControlloNomeSeqLink;
import plugin.sequencediagram.data.ElementoClasse;
import plugin.sequencediagram.data.ElementoConstraint;
import plugin.sequencediagram.data.ElementoLoop;
import plugin.sequencediagram.data.ElementoParallelo;
import plugin.sequencediagram.data.ElementoSeqLink;
import plugin.sequencediagram.data.ElementoSim;
import plugin.sequencediagram.data.ElementoTime;
import plugin.sequencediagram.data.ListaClasse;
import plugin.sequencediagram.data.ListaConstraint;
import plugin.sequencediagram.data.ListaLoop;
import plugin.sequencediagram.data.ListaParallel;
import plugin.sequencediagram.data.ListaSeqLink;
import plugin.sequencediagram.data.ListaSim;
import plugin.sequencediagram.data.ListaTime;
import plugin.sequencediagram.data.PlugData;
import plugin.sequencediagram.data.SequenceElement;
import plugin.sequencediagram.dialog.FinestraNuovoConstraint;
import plugin.sequencediagram.dialog.FinestraSequenceLink;
import plugin.sequencediagram.dialog.FinestraSequenceLoop;
import plugin.sequencediagram.general.undo.UndoRedoSeqElement;
import plugin.sequencediagram.toolbar.SequenceToolBar;
import plugin.sequencediagram.utility.JPopupMenuTimeLine;

import plugin.topologydiagram.TopologyWindow;

import core.internal.runtime.data.IPlugData;
import core.internal.ui.PlugDataWin;
import core.internal.ui.statusbar.StatusBar;
import core.internal.ui.toolbar.EditToolBar;
import core.resources.jpeg.JpegImage;
import core.resources.ui.WithGraphEditor;

	
	/**	Classe per la creazione e gestione del pannello su cui verrï¿½ disegnato
		il Sequence Diagram_ La classe controlla gli eventi del mouse (clicked,
		released, dragged), esegue le operazioni di zoom, implementa le 
		operazioni associate ai pulsanti della SequenceToolBar, etc_ Non sono
		implementate, perchï¿½ considerate superflue, le operazioni di editing: 
		cut, copy e paste_  E' possibile invece eseguire la funzione del sui
		messaggi e le classiche operazioni di undo/redo. */
			
	public class SequenceEditor extends WithGraphEditor implements Serializable
	{
	    
	    /** Numero di Messaggi */
		private long numMessaggio;
		
		private int spiazzamentoX=0;
		private int spiazzamentoY=0;
		Point startPoint;
		
		private static int constraintInstanceNumber=1;
                private static int parallelInstanceNumber=1;
                private static int simInstanceNumber=1;
                private static int loopInstanceNumber=1;
		
	    /** Costante che delimita il margine superiore sotto il quale
	    	possono essere disegnati i messaggi scambiati tra le classi_
	    	Il lato superiore della prima fascia temporale coincide, 
	    	per l'appunto, con il margine superiore.  */
	    public static final int MARGINE_SUPERIORE = 140;
	    
	    /** Costante che definisce la minima distanza del rettangolo 
	    	rappresentante la classe dal lato superiore dell'editor_
	    	Usata anche come minima distanza (di default) tra il 
	    	suddetto rettangolo ed il lato sinistro dell'editor. */
	    public static final int MARGINE_CLASSE = 20;
	    
	    /** Valore di default della distanza tra il centro di una
	    	classe e quello della successiva (precedente). */
	    public static final int STEP_CLASSE = 200;
	    
	    /** Incremento della dimensione verticale dell'editor quando
	    	l'ultima fascia temporale non ï¿½ contenuta. */
	    public static final int STEP_EDITOR = 200;
	    
	    /** Costante per la condizione di attesa dell'input dell'utente. */    
	    public static final int ATTESA = 0;
	    
	    /** Costante per la situazione in cui l'utente sta  
	    	inserendo un nuovo messaggio tra classi. */
	    public static final int INSERIMENTO_MESSAGGIO = 1;
	    
	    /** Costante per la situazione in cui si sta tracciando una 
	    	una linea relativa ad un messaggio tra classi. */
	    public static final int DISEGNA_MESSAGGIO = 2;
	    
	    /** Costante per la condizione in cui l'utente sta 
	    	spostando una classe. */
	    public static final int SPOSTA_PROCESSO = 3;
	   /** Costante per la selezione dell-area di stampa. */      
	    public static final int PRINT_AREA = 4;
	    /** Costante per la multiselezione*/
	    public static final int MULTISELEZIONE = 5;
	    /** Costante per l-inserimento di constraints. */
	    public static final int CONSTRAINT = 6;
            /** Costante per l-inserimento di messaggi paralleli. */
	    public static final int PARALLEL = 7;
            /** Costante per l-inserimento di messaggi simultanei. */
	    public static final int SIM = 9;
            /** Costante per l-inserimento del'operatore loop. */
	    public static final int LOOP = 10;
            /** Costante per muovere il parallelo. */
	    public static final int MOV_PAR = 11;
            /** Costante per muovere il simultaneo. */
	    public static final int MOV_SIM = 12;
            /** Costante per muovere il loop. */
	    public static final int MOV_LOOP = 13;
            
	    /** Riferimento alla toolbar. */
	    transient private SequenceToolBar localSequenceToolBar;
	    
	    /** Riferimento alla toolbar. */
	    transient private EditToolBar localEditToolBar;
	    
	    /**
	     * variabile indicante per generare un unico invio del
	     * di messaggi per la modifica della classe 
	     *
	     */
	    boolean unicoInvio = false;
	    boolean primaVolta = true;
            
            /**
	     * variabile indicante per generare un unico invio del
	     * di messaggi per la modifica dell'operatore parallelo 
	     *
	     */
	    boolean unicoInvio_par = false;
	    boolean primaVolta_par = true;
            
            /**
	     * variabile indicante per generare un unico invio del
	     * di messaggi per la modifica dell'operatore simultaneo 
	     *
	     */
	    boolean unicoInvio_sim = false;
	    boolean primaVolta_sim = true;
            
            /**
	     * variabile indicante per generare un unico invio del
	     * di messaggi per la modifica dell'operatore loop 
	     *
	     */
	    boolean unicoInvio_loop = false;
	    boolean primaVolta_loop = true;
	    
	    /** Riferimento allo stato corrente.*/
	    transient private ElementoClasse ClasseCorrente;
	    
	    /** Classe di partenza nell'operazione di
	    	inserimento di un nuovo messaggio. */
	    transient private ElementoClasse ClasseFrom;
	    
	    /** Istante di partenza nell'operazione di 
	    	inserimento di un nuovo messaggio. */
	    transient private ElementoTime TimeFrom;
	    
	    /** Classe di arrivo nell'operazione di
	    	inserimento di un nuovo messaggio. */    
	    transient private ElementoClasse ClasseTo;
	    
	    /** Istante di arrivo nell'operazione di
	    	inserimento di un nuovo messaggio. */
	    transient private ElementoTime TimeTo;
	
            /** Variabile per memorizzare un riferimento 
                    alla fascia temporale sulla quale si preme 
                    il bottone destro del mouse. */
            transient private ElementoTime TimePopup;

            /** Riferimento al link corrente.*/	
            transient private ElementoSeqLink LinkCorrente;

            /** Riferimento al constraint */
            transient private ElementoConstraint ConstraintCorrente;

            /**Riferimento all'operatore parallelo**/
            transient private ElementoParallelo ParalleloCorrente;
            
            /**Riferimento all'operatore sim**/
            transient private ElementoSim SimCorrente;
            
            /**Riferimento all'operatore loop**/
            transient private ElementoLoop LoopCorrente;

            /** Messaggio a cui viene applicato il constraint. */
            transient private ElementoSeqLink Link_con;
		
		/** Memorizza lo stato dell'editor (ATTESA, INSERIMENTO_MESSAGGIO, 
			DISEGNA_MESSAGGIO, SPOSTA_PROCESSO,PARALLELO,SIMULTANEO,COREGION,LOOP etc.) */        
	    transient private int SequenceEditorStatus = 0;
	
	    /** Memorizza il tipo di link per la 
	    	successiva operazione di inserimento. */
	    transient private int TipoMessaggio = 0;
	    
	        /** Memorizza il tipo di link: Regular, Required, Fail per la 
	    	successiva operazione di inserimento. */
	    transient private int msgRRF = 3;
	    
	    /** Assume il valore true quando uno dei pulsanti di inserimento
	    	messaggio (SIMPLE, SYN, ASYN, LOOP), oppure inserimento Constraint risulta bloccato. */
	    transient private boolean BloccoPulsante = false;
	
		/** Finestra di dialogo per impostare le proprietï¿½ di una classe. */
	    transient private FinestraElementoClasse FinestraProprietaClasse;
	
		/** Finestra di dialogo per impostare le proprietï¿½ di un messaggio. */    
	    transient private FinestraSequenceLink FinestraProprietaMessaggio;
            
            /** Finestra di dialogo per impostare le proprietï¿½ di un loop. */    
	    transient private FinestraSequenceLoop FinestraProprietaLoop;
	
		/** Finestra di dialogo per impostare le proprietï¿½ dell'editor. */    
	    transient private FinestraGraphEditor FinestraProprietaEditor;
	    
	    /** Necessaria per l'implementazione. */
	    transient private Graphics2D g2;
	        
	    transient private Rectangle2D rectAreaStampa = null;
            
            /** Rettangolo visualizzato durante un'operazione di 
	    trascinamento del mouse nello stato MULTISELEZIONE. */
	   transient private Rectangle2D rectMultiSelection = null;
           
           /** Memorizza il pi? piccolo rettangolo contenente
		tutti i processi selezionati (multiselezione). */
	    transient private Rectangle2D externalRect = null;
            
            public int xImg = 0, yImg = 0, widthImg = rWidth, heightImg = rHeight;
            
            /** Utilizzata per memorizzare la lista degli stati nelle
		operazioni interessate da multiselezione_ Forse potrebbe 
		essere eliminata, riutilizzando tmpListaStati. */
            transient private LinkedList ListaStatisel = null;
	        
	    /** Riferimento all'editor dello Sequence Diagram, ovvero alla
	    	classe stessa_ E' usato all'interno delle classi nidificate 
	    	ClassEditorClickAdapter e ClassEditorMotionAdapter. */
	    private SequenceEditor rifEditor;
	    
	    /** Linea usata per visualizzare il margine superiore, ovvero
	    	il lato superiore della prima fascia temporale. */
	    transient private Line2D linea;
	
	    /** PopupMenu visualizzato quando si preme il bottone destro
	    	del mouse sopra una fascia temporale. */
	    transient private JPopupMenuTimeLine TimeLinePopupMenu;
	       
	    /**
	     * elemento rappresentante l'unione delle classi
	     * ListaClassi, ListaTime, ListaSeqLink
	     */
	    private SequenceElement seqEle;
	
	    /**
	     * contenitori dei dati
	     */
	    private PlugData plugData;  
	    
	    private PlugDataWin plugDataWin; 
	    
	
	    private UndoRedoSeqElement undoManager;
	    
	    private plugin.statediagram.data.PlugData pdThread;
	           
	    /** Costruttore_ Prende in ingresso il nome da dare all'editor, 
	    	un riferimento alla barra di stato e la lista delle classi
	    	utilizzate nel SequenceDiagram. */ 
	    public SequenceEditor(String nomeEditor,DefaultListModel listaClassi, boolean ctrlStringTime, boolean ctrlLineTime,IPlugData plugDt, PlugDataWin plugW)
	    {
	        this(new SequenceElement(nomeEditor, plugDt), plugDt,plugW);
	        
	        ///ezio 2006
	     //   pdThread=(plugin.statediagram.data.PlugData)plugDt.getPlugDataManager().getPlugData("charmy.plugin.state");
	////fine
	        for (int i=0; i<listaClassi.size(); i++){
	        	ClasseCorrente = new 
	        		ElementoClasse(i*STEP_CLASSE+MARGINE_CLASSE,
	        			MARGINE_CLASSE,
	        			(String)listaClassi.get(i), null);
	      	
	        		seqEle.getListaClasse().addElement(ClasseCorrente);
	        }
	
	        seqEle.getListaTime().addFirst(
	        		new ElementoTime(
	        				0,
	        				MARGINE_SUPERIORE + 2 * ElementoTime.hfascia,
							MARGINE_SUPERIORE,
							ctrlStringTime,
							ctrlLineTime, null));
	        seqEle.getListaTime().addLast(
	        		new ElementoTime(
	        				1,
	        				MARGINE_SUPERIORE + 4 * ElementoTime.hfascia,
							MARGINE_SUPERIORE + 2 * ElementoTime.hfascia,
							ctrlStringTime,
							ctrlLineTime,null));
	        seqEle.getListaTime().addLast(
	        		new ElementoTime(
	        				2,
	        				MARGINE_SUPERIORE + 6 * ElementoTime.hfascia,
							MARGINE_SUPERIORE + 4 * ElementoTime.hfascia,
							ctrlStringTime,
							ctrlLineTime,null));
	        // Inizializzazione memoria per le operazioni di undo/redo.      
	
	        plugData.getListaDS().add(seqEle);
	        repaint();
	        
	    } 
	    
	
	    /* Costruttore. */
	    public SequenceEditor(String nomeEditor,boolean ctrlStringTime,
				boolean ctrlLineTime, IPlugData plugDt, PlugDataWin plugW) {
	
	    	this(nomeEditor, null, ctrlStringTime, ctrlLineTime, plugDt, plugW);
	
	    }
	 
	    
	    /** 
	     * Costruttore. Con SequenceElement
	     */
	    public SequenceEditor(SequenceElement se,IPlugData plugDt,PlugDataWin plugW) {
	    	
	    	super(plugW.getFileManager());  ///ezio 2006
	    	  ///ezio 2006
		       pdThread=(plugin.statediagram.data.PlugData)plugDt.getPlugDataManager().getPlugData("charmy.plugin.state");
		////fine
	    	plugData =(PlugData)plugDt;
	    	plugDataWin = plugW;
	    	seqEle = se;
	    	setName(se.getName());
	    	
	    	//popup per la gestione delle linee temporali
	    	TimeLinePopupMenu = new JPopupMenuTimeLine(this);
	    	
	    	undoManager = new UndoRedoSeqElement();
	    	undoManager.setDati(null, plugData);
	    	seqEle.addListener(undoManager);
	    	
	        addMouseListener(new ClassEditorClickAdapter());
	        addMouseMotionListener(new ClassEditorMotionAdapter());


	    	//grafica
	    	linea = new Line2D.Double(20,MARGINE_SUPERIORE,1180,MARGINE_SUPERIORE);
	        rifEditor = this;
	    }
	    
	    
	    ////ezio 2006 - eliminare non servono, anzi incasinano...
		/*public static int getConstraintInstanceNumber(){
			return constraintInstanceNumber;
		}
		
		public static void incConstraintInstanceNumber(){
			constraintInstanceNumber++;
		}
                
                public static int getParallelInstanceNumber(){
			return parallelInstanceNumber;
		}
		
		public static void incParallelInstanceNumber(){
			parallelInstanceNumber++;
		}
                
                public static int getSimInstanceNumber(){
			return simInstanceNumber;
		}
		
		public static void incSimInstanceNumber(){
			simInstanceNumber++;
		}
                
                public static int getLoopInstanceNumber(){
			return loopInstanceNumber;
		}
		
		public static void incLoopInstanceNumber(){
			loopInstanceNumber++;
		}
		*/
		public long getNumMessaggio(){
			return numMessaggio;
		}
		
		public void incNumMessaggio(){
			numMessaggio = numMessaggio + 1;
		}	    
	    
	       
	    /** Imposta il riferimento alle toolbar. */
	    public void setToolBar(EditToolBar etbar,SequenceToolBar ctbar)
	    {
	        localSequenceToolBar = ctbar;
	        localEditToolBar = etbar;
	        localEditToolBar.setButtonEnabled("Copy",true);
	        localEditToolBar.setButtonEnabled("Paste",true);        
	        localEditToolBar.setButtonEnabled("Cut",true);
	        localEditToolBar.setButtonEnabled("Del",true);         
	        localEditToolBar.setButtonEnabled("Undo",true);
	        localEditToolBar.setButtonEnabled("Redo",true);                       
	    }
	
	
		/** Restituisce la condizione dell'editor. */
	    public int getEditorStatus(){
	        return SequenceEditorStatus;
	    }
	
	    
	    /** Imposta la situazione dell'editor_ Si osservi che solo
	    	alcune condizioni possono essere assegnate dall'esterno:
	    	ATTESA, INSERIMENTO_MESSAGGIO, DISEGNA_MESSAGGIO, CONSTRAINT. */
	    public void setEditorStatus(int j, int tipo, int msgType, boolean ctrlpulsante)
	    {
	        BloccoPulsante = ctrlpulsante;
	
	        switch(j){
	            case INSERIMENTO_MESSAGGIO:
	                SequenceEditorStatus = INSERIMENTO_MESSAGGIO;
	                TipoMessaggio = tipo;
	                msgRRF=msgType;
	                break;                
	            case DISEGNA_MESSAGGIO:
	                SequenceEditorStatus = DISEGNA_MESSAGGIO;
	                TipoMessaggio = tipo;
	                msgRRF=msgType;
	                break;
	            case CONSTRAINT:
	                SequenceEditorStatus = CONSTRAINT;
	                break;
                    case PARALLEL:
	                SequenceEditorStatus = PARALLEL;
	                break;      
                    case SIM:
	                SequenceEditorStatus = SIM;
	                break; 
                    case LOOP:
	                SequenceEditorStatus = LOOP;
	                break;                             
	            default:
	                SequenceEditorStatus = ATTESA;
	                break;
	        }
	    }
	    
	    
	    /** "Stampa" l'editor con classi e link. */
	    public void paintComponent(Graphics g)
	    {		
                super.paintComponent(g);
	        g2 = (Graphics2D)g;
	        g2.scale(scaleX,scaleY);
	        Stroke tmpstroke = g2.getStroke();
	        g2.setStroke(bstroke);
	        g2.draw(rettangolo);
	        g2.setStroke(tmpstroke);
	
	        Stroke tmpStroke = g2.getStroke();
	        Color tmpColor = g2.getColor();        
	        g2.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND,
	            BasicStroke.JOIN_ROUND,10.0f,ElementoTime.TIME_LINK_TYPE,0));
	        g2.setColor(ElementoTime.TIME_LINK_COLOR);        
	        g2.draw(linea);
	        g2.setColor(tmpColor);
	        g2.setStroke(tmpStroke); 
	                  
                if (rectAreaStampa != null) {
                    g2.draw(rectAreaStampa);
		}
	        seqEle.getListaClasse().paintLista(g2);
	        seqEle.getListaTime().paintLista(g2);
	        seqEle.getListaSeqLink().paintLista(g2);	        
	        seqEle.getListaConstraint().paintLista(g2);
                
                //new
                seqEle.getListaParallel().paintLista(g2);
                seqEle.getListaSim().paintLista(g2);
                seqEle.getListaLoop().paintLista(g2);
	    }
	
	
	    /** Classe per la gestione della pressione dei tasti del mouse. */
	    private final class ClassEditorClickAdapter extends MouseAdapter
	    {
	        public void mousePressed(MouseEvent e)
	        {
	           if(SequenceEditorStatus == CONSTRAINT){	                
	                Link_con=(ElementoSeqLink) seqEle.getListaSeqLink().getElementSelected(updateGetPoint(e.getPoint()));
	                if(Link_con!=null) 
                        {
                            ConstraintCorrente = new ElementoConstraint(Link_con,"","",false,0,12,null);
                            FinestraNuovoConstraint fnc = new FinestraNuovoConstraint(ConstraintCorrente,seqEle,Link_con,(Frame)getTopLevelAncestor(),"New constraint insert",true,g2,plugData.getPlugDataManager());
                            repaint(); 
                            if(ConstraintCorrente.getInserisciConstraint()){
                                seqEle.getListaConstraint().addElement(ConstraintCorrente);                   
                                plugDataWin.getStatusBar().setText("Constraint " + ConstraintCorrente.getLabel() + " inserted.");
                            }
                            else{
                                ConstraintCorrente=null;
                                plugDataWin.getStatusBar().setText("Constraint not inserted.");
                            }
                            Link_con.setSelected(false);

                            if (!BloccoPulsante){
                                SequenceEditorStatus = ATTESA;
                                localSequenceToolBar.setNoPressed();
                                    }
                            repaint();
                           //ezio 2006 core.internal.plugin.file.FileManager.setModificata(true);
                            plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true); // ezio 2006
                            
                        }
                        else{
                            localSequenceToolBar.setNoPressed();
                            SequenceEditorStatus = ATTESA;
                        }
	            }
	           ClasseCorrente = (ElementoClasse)(seqEle.getListaClasse().getElement(updateGetPoint(e.getPoint())));
	            if (ClasseCorrente != null){
	                // E' stata selezionata una classe.
	
	            	Point p = updateGetPoint(e.getPoint());
	                spiazzamentoX=ClasseCorrente.getTopX()-p.x;
	                spiazzamentoY=ClasseCorrente.getTopY()-p.y;
	            }
                   
                   
	            startPoint = updateGetPoint(e.getPoint());
	        }


	        /** Rilascio del mouse. */
	        public void mouseReleased(MouseEvent e)
	        {
	            switch (SequenceEditorStatus){	                
	               	case PARALLEL:                               
	                	Point endPoint = updateGetPoint(e.getPoint());
	                        if (endPoint!=null){
	                            if(endPoint.x>startPoint.x)
	                                if(endPoint.y>startPoint.y)
	                                    rectAreaStampa = new Rectangle2D.Double(startPoint.x,startPoint.y,
	                                            endPoint.x-startPoint.x,endPoint.y-startPoint.y);
	                                else 
	                                    rectAreaStampa = new Rectangle2D.Double(startPoint.x,endPoint.y,
	                                            endPoint.x-startPoint.x,startPoint.y-endPoint.y);
	                            else
	                                if(endPoint.y>startPoint.y)
	                                    rectAreaStampa = new Rectangle2D.Double(endPoint.x,startPoint.y,
	                                            startPoint.x-endPoint.x,endPoint.y-startPoint.y);
	                                else 
	                                    rectAreaStampa = new Rectangle2D.Double(endPoint.x,endPoint.y,
	                                            startPoint.x-endPoint.x,startPoint.y-endPoint.y);
                                    
                                seqEle.getListaSeqLink().setSelectedIfInRectangle(rectAreaStampa);
                                ListaStatisel = seqEle.getListaSeqLink().listSelectedChannel();                                     
                                rectAreaStampa = null;
                                if (!ListaStatisel.isEmpty()) {
                                    plugDataWin.getStatusBar().setText("Selection ok!!  Ready.");
                                    createParallel(ListaStatisel);                                    
                                } else
				// Non è stato selezionato alcuno stato.
				{
                                    SequenceEditorStatus = ATTESA;
                                    ParalleloCorrente=null;
                                    plugDataWin.getStatusBar().setText("Parallel operator not inserted. Ready");                              
                                }
	                            
                                repaint();
	                        }
                                if (!BloccoPulsante){
	                            SequenceEditorStatus = ATTESA;
	                            localSequenceToolBar.setNoPressed();                                    
	                   	}
                                seqEle.getListaSeqLink().noSelected();                    
                                seqEle.getListaClasse().noSelected();
	                	break;
                        case SIM: 
                            endPoint = updateGetPoint(e.getPoint());
	                        if (endPoint!=null){
	                            if(endPoint.x>startPoint.x)
	                                if(endPoint.y>startPoint.y)
	                                    rectAreaStampa = new Rectangle2D.Double(startPoint.x,startPoint.y,
	                                            endPoint.x-startPoint.x,endPoint.y-startPoint.y);
	                                else 
	                                    rectAreaStampa = new Rectangle2D.Double(startPoint.x,endPoint.y,
	                                            endPoint.x-startPoint.x,startPoint.y-endPoint.y);
	                            else
	                                if(endPoint.y>startPoint.y)
	                                    rectAreaStampa = new Rectangle2D.Double(endPoint.x,startPoint.y,
	                                            startPoint.x-endPoint.x,endPoint.y-startPoint.y);
	                                else 
	                                    rectAreaStampa = new Rectangle2D.Double(endPoint.x,endPoint.y,
	                                            startPoint.x-endPoint.x,startPoint.y-endPoint.y);
                                    
                                seqEle.getListaSeqLink().setSelectedIfInRectangle(rectAreaStampa);
                                ListaStatisel = seqEle.getListaSeqLink().listSelectedChannel();                                     
                                rectAreaStampa = null;
                                if (!ListaStatisel.isEmpty()) {
                                    plugDataWin.getStatusBar().setText("Selection ok!!  Ready.");
                                    createSim(ListaStatisel);                                    
                                } else
				// Non è stato selezionato alcuno stato.
				{
                                    SequenceEditorStatus = ATTESA;
                                    SimCorrente=null;
                                    plugDataWin.getStatusBar().setText("Sim operator not inserted. Ready");                              
                                }
	                            
                                repaint();
	                        }
                                if (!BloccoPulsante){
	                            SequenceEditorStatus = ATTESA;
	                            localSequenceToolBar.setNoPressed();                                    
	                   	}
                                seqEle.getListaSeqLink().noSelected();                    
                                seqEle.getListaClasse().noSelected();
	                	break;
                                
                        case LOOP: 
                            endPoint = updateGetPoint(e.getPoint());
	                        if (endPoint!=null){
	                            if(endPoint.x>startPoint.x)
	                                if(endPoint.y>startPoint.y)
	                                    rectAreaStampa = new Rectangle2D.Double(startPoint.x,startPoint.y,
	                                            endPoint.x-startPoint.x,endPoint.y-startPoint.y);
	                                else 
	                                    rectAreaStampa = new Rectangle2D.Double(startPoint.x,endPoint.y,
	                                            endPoint.x-startPoint.x,startPoint.y-endPoint.y);
	                            else
	                                if(endPoint.y>startPoint.y)
	                                    rectAreaStampa = new Rectangle2D.Double(endPoint.x,startPoint.y,
	                                            startPoint.x-endPoint.x,endPoint.y-startPoint.y);
	                                else 
	                                    rectAreaStampa = new Rectangle2D.Double(endPoint.x,endPoint.y,
	                                            startPoint.x-endPoint.x,startPoint.y-endPoint.y);
                                    
                                seqEle.getListaSeqLink().setSelectedIfInRectangle(rectAreaStampa);
                                ListaStatisel = seqEle.getListaSeqLink().listSelectedChannel();                                     
                                rectAreaStampa = null;
                                if (!ListaStatisel.isEmpty()) {
                                    plugDataWin.getStatusBar().setText("Selection ok!!  Ready.");
                                    createLoop(ListaStatisel);                                    
                                } else
				// Non è stato selezionato alcuno stato.
				{
                                    SequenceEditorStatus = ATTESA;
                                    LoopCorrente=null;
                                    plugDataWin.getStatusBar().setText("Loop operator not inserted. Ready");                              
                                }
	                            
                                repaint();
	                        }
                                if (!BloccoPulsante){
	                            SequenceEditorStatus = ATTESA;
	                            localSequenceToolBar.setNoPressed();                                    
	                   	}
                                seqEle.getListaSeqLink().noSelected();                    
                                seqEle.getListaClasse().noSelected();
	                	break;
                                
	                case INSERIMENTO_MESSAGGIO:
	                   	// Completa deselezione.
	                   	seqEle.getListaSeqLink().noSelected();                    
	                	seqEle.getListaClasse().noSelected();
	                   	ClasseFrom = (ElementoClasse)(seqEle.getListaClasse().getLineElement(updateGetPoint(e.getPoint())));
	                   	TimeFrom = (ElementoTime)(seqEle.getListaTime().getElement(updateGetPoint(e.getPoint())));
	                   	if ((ClasseFrom != null)&&(TimeFrom != null)){
	                   		plugDataWin.getStatusBar().setText("Class " + ClasseFrom.getName() + " selected.  Time: " + 
	                   			TimeFrom.getTime() + ".  Click over another class to insert a link.");                        	
	                   		// Selezionata la classe di partenza del nuovo link.                        	
	                       	ClasseFrom.setSelected(true);
	                       	SequenceEditorStatus = DISEGNA_MESSAGGIO;                       
	                   	}
	                   	repaint();
	                   	break;
	                
	                case DISEGNA_MESSAGGIO:    					                    
	                   	// Creazione ed inserimento di un nuovo link.                   	
	                   	ClasseTo = (ElementoClasse)(seqEle.getListaClasse().getLineElement(updateGetPoint(e.getPoint())));
	                   	TimeTo = (ElementoTime)(seqEle.getListaTime().getElement(updateGetPoint(e.getPoint())));	                   	
	                   	if ((ClasseFrom != null)&&(TimeTo != null)){                                             
                                    if ((ClasseTo != null)&&(!(ClasseTo.equals(ClasseFrom)))&&
	               			// Per i messaggi semplici e sincroni l'istante di invio
	               			// coincide necessariamente con quello di ricezione.
	               			((TimeFrom.getTime()==TimeTo.getTime())||
	               			// Per i messaggi asincroni l'istante di ricezione puï¿½
	               			// o coincidere o essere maggiore di quello di invio.
	               			((TimeFrom.getTime()<TimeTo.getTime())&&(TipoMessaggio==ElementoMessaggio.ASYNCHRONOUS))))
                                    {    
                                        LinkedList listaNomi = null;
                                        if(pdThread!=null)
                                            listaNomi =(pdThread.getListaDP()).getAllMessageNameForProcess(ClasseFrom.getName(),ClasseTo.getName());
                                        else
                                            listaNomi=new LinkedList();
                                        if(listaNomi.size()!=0){
                                            if (seqEle.getListaSeqLink().size()!=0)
                                            {
                                              // ezio 2006 if(((ElementoSeqLink)seqEle.getListaSeqLink().getLastElement()).getTimeFrom().getTime() + 1 == TimeFrom.getTime())
                                            	if((ElementoSeqLink)seqEle.getListaSeqLink().getLinkAtTime(TimeFrom.getTime()-1)!=null)
                                                {                                                
                                                    incNumMessaggio();
                                                    //ezio 2006
                                                    LinkCorrente = new ElementoSeqLink((new ControlloNomeSeqLink(plugData.getPlugDataManager())).getNameOK(),ClasseFrom,ClasseTo,TimeFrom,TimeTo,1,1,TipoMessaggio,msgRRF,(String)listaNomi.getFirst(), null);
                                                    ////
                                                    if(ParalleloCorrente!=null && ParalleloCorrente.isSelezionato(LinkCorrente.getPointStart()))
                                                          ParalleloCorrente.addElem(LinkCorrente);
                                                    if (seqEle.getListaSeqLink().size()!=0)
                                                            LinkCorrente.setPrec((ElementoSeqLink)seqEle.getListaSeqLink().getLastElement());                                            
                                                    seqEle.getListaSeqLink().addElement(LinkCorrente);
                                                    seqEle.setLinkgood_allpar();
                                                    seqEle.setLinkgood_allsim();
                                                    seqEle.setLinkgood_allloop();
                                                            // Aggiornamento della barra di stato.                     
                                                    plugDataWin.getStatusBar().setText("Link " + LinkCorrente.getName() + " inserted.");
                                                    //ezio 2006 core.internal.plugin.file.FileManager.setModificata(true);
                                                    plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true); // ezio 2006
                                                   }
                                                else
                                                  JOptionPane.showMessageDialog(null, "The message isn't consecutive ", "Error!",JOptionPane.ERROR_MESSAGE);
                                            }
                                            else
                                            {
                                                if(TimeFrom.getTime() == 0)
                                                {
                                                    incNumMessaggio();
                                                    ///ezio 2006
                                                    LinkCorrente = new ElementoSeqLink((new ControlloNomeSeqLink(plugData.getPlugDataManager())).getNameOK(),ClasseFrom,ClasseTo,TimeFrom,TimeTo,1,1,TipoMessaggio,msgRRF,(String)listaNomi.getFirst(), null);
                                                    ////////
                                                    
                                                    if(ParalleloCorrente!=null && ParalleloCorrente.isSelezionato(LinkCorrente.getPointStart()))
                                                          ParalleloCorrente.addElem(LinkCorrente);
                                                    if (seqEle.getListaSeqLink().size()!=0)
                                                            LinkCorrente.setPrec((ElementoSeqLink)seqEle.getListaSeqLink().getLastElement());                                            
                                                    seqEle.getListaSeqLink().addElement(LinkCorrente);
                                                    seqEle.setLinkgood_allpar();
                                                    seqEle.setLinkgood_allsim();
                                                    seqEle.setLinkgood_allloop();
                                                            // Aggiornamento della barra di stato.                     
                                                    plugDataWin.getStatusBar().setText("Link " + LinkCorrente.getName() + " inserted.");
                                                    //ezio 2006 core.internal.plugin.file.FileManager.setModificata(true);
                                                    plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true); // ezio 2006
                                                   }
                                                else
                                                    JOptionPane.showMessageDialog(null, "Non ci possono essere istanti di tempo senza messaggi", "Error!",JOptionPane.ERROR_MESSAGE);
                                            }
                                        }
                                        else{
                                                JOptionPane.showMessageDialog(null, "There are no messages exchanged between the pair of processes", "Error!",JOptionPane.ERROR_MESSAGE);
                                        }
                                    }
	                           else
	                               plugDataWin.getStatusBar().setText("Link not inserted.  Ready.");
                                    ClasseFrom.setSelected(false);
                                    SequenceEditorStatus = INSERIMENTO_MESSAGGIO;
		                    }
                                    else
                                        if ((ClasseTo != null)&&(TimeTo != null)){                               
                                            LinkedList listaNomi = null;
                                            if(pdThread!=null)
                                                listaNomi =(pdThread.getListaDP()).getAllTauMessageNameForProcess(ClasseTo.getName());
                                            else
                                                listaNomi = new LinkedList();
                                            if(listaNomi!=null)
                                                if(listaNomi.size()!=0){
                                                    if (seqEle.getListaSeqLink().size()!=0)
                                                    {
                                                      // ezio 2006  if(((ElementoSeqLink)seqEle.getListaSeqLink().getLastElement()).getTimeFrom().getTime() + 1 == TimeFrom.getTime())
                                                    	if((ElementoSeqLink)seqEle.getListaSeqLink().getLinkAtTime(TimeFrom.getTime()-1)!=null)
                                                        {                                                
                                                            incNumMessaggio();
                                                            LinkCorrente = new ElementoSeqLink((new ControlloNomeSeqLink(plugData.getPlugDataManager())).getNameOK(),ClasseFrom,ClasseTo,TimeFrom,TimeTo,1,1,TipoMessaggio,msgRRF,(String)listaNomi.getFirst(), null);	                                                       
                                                            if(ParalleloCorrente!=null && ParalleloCorrente.isSelezionato(LinkCorrente.getPointStart()))
                                                                  ParalleloCorrente.addElem(LinkCorrente);
                                                            if (seqEle.getListaSeqLink().size()!=0)
                                                                    LinkCorrente.setPrec((ElementoSeqLink)seqEle.getListaSeqLink().getLastElement());                                            
                                                            seqEle.getListaSeqLink().addElement(LinkCorrente);
                                                            seqEle.setLinkgood_allpar();
                                                            seqEle.setLinkgood_allsim();
                                                            seqEle.setLinkgood_allloop();
                                                                    // Aggiornamento della barra di stato.                     
                                                            plugDataWin.getStatusBar().setText("Link " + LinkCorrente.getName() + " inserted.");
                                                            //ezio 2006 core.internal.plugin.file.FileManager.setModificata(true);
                                                            plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true); // ezio 2006
                                                           }
                                                        else
                                                          JOptionPane.showMessageDialog(null, "The message isn't consecutive ", "Error!",JOptionPane.ERROR_MESSAGE);
                                                    }
                                                    else
                                                    {
                                                        if(TimeFrom.getTime() == 0)
                                                        {
                                                            incNumMessaggio();
                                                            ///ezio 2006
                                                            LinkCorrente = new ElementoSeqLink((new ControlloNomeSeqLink(plugData.getPlugDataManager())).getNameOK(),ClasseFrom,ClasseTo,TimeFrom,TimeTo,1,1,TipoMessaggio,msgRRF,(String)listaNomi.getFirst(), null);
                                                            ///
                                                            
                                                            if(ParalleloCorrente!=null && ParalleloCorrente.isSelezionato(LinkCorrente.getPointStart()))
                                                                  ParalleloCorrente.addElem(LinkCorrente);
                                                            if (seqEle.getListaSeqLink().size()!=0)
                                                                    LinkCorrente.setPrec((ElementoSeqLink)seqEle.getListaSeqLink().getLastElement());                                            
                                                            seqEle.getListaSeqLink().addElement(LinkCorrente);
                                                            seqEle.setLinkgood_allpar();
                                                            seqEle.setLinkgood_allsim();
                                                            seqEle.setLinkgood_allloop();
                                                                    // Aggiornamento della barra di stato.                     
                                                            plugDataWin.getStatusBar().setText("Link " + LinkCorrente.getName() + " inserted.");
                                                            //ezio 2006 core.internal.plugin.file.FileManager.setModificata(true);
                                                            plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true); // ezio 2006
                                                           }
                                                        else
                                                            JOptionPane.showMessageDialog(null, "Non ci possono essere istanti di tempo senza messaggi", "Error!",JOptionPane.ERROR_MESSAGE);
                                                } 
                                                }
                                                else{
                                                    JOptionPane.showMessageDialog(null, "There are no loop messages for the selected process", "Error!",JOptionPane.ERROR_MESSAGE);
                                                }
                                        }
	                   	ClasseTo = null;
	                   	ClasseFrom = null;                	                     
	                   	if (!BloccoPulsante){
	                            SequenceEditorStatus = ATTESA;
	                            localSequenceToolBar.setNoPressed();
	                   	}
	                   	repaint();  
	                   	break;                
	                
	                case SPOSTA_PROCESSO:
	                	if (ClasseCorrente != null){
	                		// E' stata selezionata una classe.
	                		ClasseCorrente.testAndReset(unicoInvio);
	                		primaVolta = true;
                                        plugDataWin.getStatusBar().setText("Class " + ClasseCorrente.getName() + " selected.  Ready.");
	                 	}
                                else{
                                    if(ParalleloCorrente != null && LinkCorrente==null ){                                       
                                            ParalleloCorrente.testAndReset(unicoInvio_par);
                                            primaVolta_par = true;
                                            plugDataWin.getStatusBar().setText("Parallel operator selected.  Ready.");                                        
                                    }                                   
                                    
                                }
	                    SequenceEditorStatus = ATTESA;
	                	
	                    break;
                        case MOV_PAR:
                                if(ParalleloCorrente != null && LinkCorrente==null ){                                       
                                    ParalleloCorrente.testAndReset(unicoInvio_par);
                                    primaVolta_par = true;
                                    ParalleloCorrente.CntrlElem();
                                    ParalleloCorrente.updateParallelPosizione();
                                 //ezio 2006   core.internal.plugin.file.FileManager.setModificata(true);
                                    plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true); // ezio 2006
                                    plugDataWin.getStatusBar().setText("Parallel selected.  Ready.");  
                                    SequenceEditorStatus = ATTESA;
                                }
                            
                            break;
                        case MOV_SIM:
                                if(SimCorrente != null && LinkCorrente==null ){                                       
                                    SimCorrente.testAndReset(unicoInvio_sim);
                                    primaVolta_sim = true;
                                    SimCorrente.CntrlElem();
                                    SimCorrente.updateSimPosizione();
                                    //ezio 2006   core.internal.plugin.file.FileManager.setModificata(true);
                                    plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true); // ezio 2006
                                 plugDataWin.getStatusBar().setText("Simultaneous selected.  Ready.");  
                                    SequenceEditorStatus = ATTESA;
                                }
                        case MOV_LOOP:
                                if(LoopCorrente != null && LinkCorrente==null ){                                       
                                    LoopCorrente.testAndReset(unicoInvio_loop);
                                    primaVolta_loop = true;
                                    LoopCorrente.CntrlElem();
                                    LoopCorrente.updateLoopPosizione();
                                    //ezio 2006   core.internal.plugin.file.FileManager.setModificata(true);
                                    plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true); // ezio 2006
                                   plugDataWin.getStatusBar().setText("Loop selected.  Ready.");  
                                    SequenceEditorStatus = ATTESA;
                                }
	                default:
	                    break;
	                    
	            }            
	        }
	
	
	        /** Gestione del click sul mouse. */
	        public void mouseClicked(MouseEvent e)
	        {
	            if (rettangolo.contains(updateGetPoint(e.getPoint()))){
	            	switch (SequenceEditorStatus){
                        case PRINT_AREA:
                            Point endPoint = updateGetPoint(e.getPoint());
                            rWidth=endPoint.x;
                            rHeight=endPoint.y;
                            rectAreaStampa = new Rectangle2D.Double(0,0,endPoint.x,endPoint.y);
                            SequenceEditorStatus = ATTESA;
                            //ezio 2006   core.internal.plugin.file.FileManager.setModificata(true);
                            plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true); // ezio 2006
                           repaint();
                            break;
                        case CONSTRAINT:
                            repaint();
                            break;
                        case ATTESA:
                            seqEle.getListaParallel().noSelected();
                            seqEle.getListaSim().noSelected();
                            seqEle.getListaLoop().noSelected();
                            seqEle.getListaSeqLink().noSelected();
                            seqEle.getListaClasse().noSelected();
                            seqEle.getListaConstraint().noSelected();
                            ParalleloCorrente =(ElementoParallelo)(seqEle.getListaParallel().getElementSelected(updateGetPoint(e.getPoint())));
                            LinkCorrente = (ElementoSeqLink)(seqEle.getListaSeqLink().getElementSelected(updateGetPoint(e.getPoint())));
                            SimCorrente =(ElementoSim)(seqEle.getListaSim().getElementSelected(updateGetPoint(e.getPoint())));
                            LoopCorrente =(ElementoLoop)(seqEle.getListaLoop().getElementSelected(updateGetPoint(e.getPoint())));
                            ConstraintCorrente = (ElementoConstraint) (seqEle.getListaConstraint().getElementSelected(updateGetPoint(e.getPoint())));
                            if(ParalleloCorrente!=null && LinkCorrente==null && ConstraintCorrente==null)
                            {       
                                if(ParalleloCorrente.isSelezionato_par(updateGetPoint(e.getPoint()))){
                                       ParalleloCorrente.Mod_par(updateGetPoint(e.getPoint()));
                                       plugDataWin.getStatusBar().setText("Parallel margin  selected.");
                                       repaint();
                                    }
                                    else{                              
                                        //e' stato selezionato un operatore parallelo                                
                                        ParalleloCorrente.setSelected(true);
                                        plugDataWin.getStatusBar().setText("Parallel  selected.");
                                        repaint(); 
                                    }
                            }
                            
                            else if(SimCorrente!=null && LinkCorrente==null && ConstraintCorrente==null)
                                {       
                                    if(SimCorrente.isSelezionato_sim(updateGetPoint(e.getPoint()))){
                                           SimCorrente.Mod_sim(updateGetPoint(e.getPoint()));
                                           plugDataWin.getStatusBar().setText("Simultaneous margin  selected.");
                                           repaint();
                                        }
                                        else{                              
                                            //e' stato selezionato un operatore parallelo                                
                                            SimCorrente.setSelected(true);
                                            plugDataWin.getStatusBar().setText("Simultaneous  selected.");
                                            repaint(); 
                                        }
                                }
                            else if(LoopCorrente!=null && LinkCorrente==null && ConstraintCorrente==null)
                                {   
                                if (e.getClickCount()>1){
                                    FinestraProprietaLoop = new FinestraSequenceLoop(LoopCorrente,null,"Loop Properties",plugData.getPlugDataManager());
                                }
                                    if(LoopCorrente.isSelezionato_Loop(updateGetPoint(e.getPoint()))){
                                           LoopCorrente.Mod_Loop(updateGetPoint(e.getPoint()));
                                           plugDataWin.getStatusBar().setText("Loop margin  selected.");
                                           repaint();
                                        }
                                        else{                              
                                                                          
                                            LoopCorrente.setSelected(true);
                                            plugDataWin.getStatusBar().setText("Loop  selected.");
                                            repaint(); 
                                        }
                                }
                            else{
                            //	Politica: ï¿½ possibile fare multiselezione sui messaggi ma non sulle classi!!
                                    if(ConstraintCorrente!=null)
                                    {                                        
                                        ConstraintCorrente.setSelected(true);
                                         plugDataWin.getStatusBar().setText("Constraint  selected.");   
                                        //e' stato selezionato un constraint

                                        if (e.getClickCount()>1){
                                             ElementoSeqLink Link =null;
                                             //Ricerca del link di cui il constr fa parte
                                            for(int i=0;i<seqEle.getListaSeqLink().size();i++)
                                            {
                                                ElementoSeqLink link =(ElementoSeqLink) seqEle.getListaSeqLink().getListLinkSequence().get(i);
                                                if(link.ConInLink(ConstraintCorrente))
                                                    Link=link;
                                            }
                                            FinestraNuovoConstraint fnc = new FinestraNuovoConstraint(ConstraintCorrente,seqEle,Link,(Frame)getTopLevelAncestor(),"Constraint properties",false,g2,plugData.getPlugDataManager());
                                            repaint();
                                        }
                                        repaint();
                                        validate();
                                        
                                    }
                            else
                                    {                                         
                                        seqEle.getListaClasse().noSelected();                                
                                        ClasseCorrente = (ElementoClasse)(seqEle.getListaClasse().getElement(updateGetPoint(e.getPoint())));
                                        if (ClasseCorrente != null){
                                            // E' stata selezionata una classe.
                                            Point p = e.getPoint();
                                            spiazzamentoX=ClasseCorrente.getTopX()-p.x;
                                            spiazzamentoY=ClasseCorrente.getTopY()-p.y;
                                            seqEle.getListaSeqLink().noSelected();
                                            ClasseCorrente.setSelected(true);
                                            plugDataWin.getStatusBar().setText("Class " + ClasseCorrente.getName() + " selected.");
                                            repaint();                            
                                            if (e.getClickCount()>1){
                                                // Gestione del doppio click su una classe.
                                                FinestraProprietaClasse = new FinestraElementoClasse(ClasseCorrente,g2,null,"Class Properties",plugDataWin.getFileManager());
                                                //localArraySequenceMemory.addSequenceMemory(ListaDelleClassi,ListaLink,ListaDeiTempi,ListaCon);
                                            }
                                        }
                                        else 
                                        {

                                            // Non ï¿½ stato selezionata alcuna classe.                        	
                                            if (!e.isShiftDown()){
                                                // Non ï¿½ stato premuto il tasto "MAIUSC", pertanto
                                                // devo eliminare qualunque precedente selezione.
                                                seqEle.getListaSeqLink().noSelected();
                                            }                        	
                                            
                                           LinkCorrente = (ElementoSeqLink)(seqEle.getListaSeqLink().getElementSelected(updateGetPoint(e.getPoint())));
                                            if (LinkCorrente != null){
                                                  // E' stato selezionato un link.

                                                 if (!e.isShiftDown()){                            	
                                                    LinkCorrente.setSelected(true);
                                                    plugDataWin.getStatusBar().setText("Link " + LinkCorrente.getName() + " selected.");
                                                }
                                                else{
                                                    // Avendo premuto il tasto "MAIUSC", devo selezionare (deselezionare)
                                                    // un link se deselezionato (selezionato).
                                                    LinkCorrente.invSelected();
                                                    plugDataWin.getStatusBar().setText("Clicked over " + LinkCorrente.getName() + ".");
                                                }
                                                repaint();
                                                if (e.getClickCount()>1){
                                                    // Gestione del doppio click su un link.
                                                    if ((LinkCorrente.getElementFrom()).equals(LinkCorrente.getElementTo())){	
                                                        // Si tratta di un loop.
                                                        FinestraProprietaMessaggio = new FinestraSequenceLink(LinkCorrente,null,"Link Properties",plugData.getPlugDataManager());
                                                    }
                                                    else{
                                                        FinestraProprietaMessaggio = new FinestraSequenceLink(LinkCorrente,null,"Link Properties",plugData.getPlugDataManager());
                                                    }
                                                }
                                            }
                                            else{
                                                // Non ï¿½ stato selezionato nï¿½ una classe nï¿½ un link.
                                                if (SwingUtilities.isRightMouseButton(e)){
                                                    TimePopup = (ElementoTime)(seqEle.getListaTime().getElement(updateGetPoint(e.getPoint())));
                                                    if (TimePopup != null){
                                                        TimeLinePopupMenu.show(rifEditor,e.getX(),TimePopup.getMinY());
                                                    }
                                                    repaint();
                                                }
                                                else{
                                                    if (e.getClickCount()>1){
                                                        // Gestione del doppio click sull'editor.
                                                        FinestraProprietaEditor = new FinestraGraphEditor(rifEditor,null,"Editor Properties");
                                                    }
                                                }
                                                plugDataWin.getStatusBar().setText("Ready.");
                                            }
                                       }
                                    }
                                }
                            repaint();
	                            
	                    	break;
	                
	                	case INSERIMENTO_MESSAGGIO:
	                    	break;
	                
	                	case DISEGNA_MESSAGGIO:    					                    
	                    	break;
	                	
	                	default:               	
	                		// Tornare nella condizione di attesa.
	                		seqEle.getListaClasse().noSelected();
                                        seqEle.getListaParallel().noSelected();
                                        seqEle.getListaSim().noSelected();
                                        seqEle.getListaLoop().noSelected();
	                		seqEle.getListaSeqLink().noSelected();
	                		SequenceEditorStatus = ATTESA;
	                		plugDataWin.getStatusBar().setText("Ready.");
	                		repaint(); 
	                    	break;
	                    
	            	}
	        	}
	        }
	    }
	
	    
	    /** Gestione del movimento del mouse. */
	    private final class ClassEditorMotionAdapter extends MouseMotionAdapter
	    {
	        /** Trascinamento del mouse. */
	        public void mouseDragged(MouseEvent e)
	        {
                    Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
                    ((JPanel)e.getSource()).scrollRectToVisible(r);
	            // Gestione degli eventi di trascinamento solo se
	            // interni all'area di editing (delimitata da rettangolo).
	            if (rettangolo.contains(updateGetPoint(e.getPoint()))){            
	            	switch (SequenceEditorStatus){
                        case PARALLEL:                		
                                Point endPoint = updateGetPoint(e.getPoint());
                		// Per selezionare gli oggetti il trascinamento del mouse deve 
                		// avvenire da sinistra verso destra e dall'alto verso il basso. 
	                         if(endPoint.x>startPoint.x)
	                            if(endPoint.y>startPoint.y)
	                                rectAreaStampa = new Rectangle2D.Double(startPoint.x,startPoint.y,
	                                    endPoint.x-startPoint.x,endPoint.y-startPoint.y);
	                            else 
	                                rectAreaStampa = new Rectangle2D.Double(startPoint.x,endPoint.y,
	                                    endPoint.x-startPoint.x,startPoint.y-endPoint.y);
	                         else
	                            if(endPoint.y>startPoint.y)
	                                rectAreaStampa = new Rectangle2D.Double(endPoint.x,startPoint.y,
	                                	startPoint.x-endPoint.x,endPoint.y-startPoint.y);
	                            else 
	                                rectAreaStampa = new Rectangle2D.Double(endPoint.x,endPoint.y,
	                                    startPoint.x-endPoint.x,startPoint.y-endPoint.y);
                                
                                repaint();                          
                		    break;
                        case SIM:                		
                		// Per selezionare gli oggetti il trascinamento del mouse deve 
                		// avvenire da sinistra verso destra e dall'alto verso il basso.
                                endPoint = updateGetPoint(e.getPoint());
	                         if(endPoint.x>startPoint.x)
	                            if(endPoint.y>startPoint.y)
	                                rectAreaStampa = new Rectangle2D.Double(startPoint.x,startPoint.y,
	                                    endPoint.x-startPoint.x,endPoint.y-startPoint.y);
	                            else 
	                                rectAreaStampa = new Rectangle2D.Double(startPoint.x,endPoint.y,
	                                    endPoint.x-startPoint.x,startPoint.y-endPoint.y);
	                         else
	                            if(endPoint.y>startPoint.y)
	                                rectAreaStampa = new Rectangle2D.Double(endPoint.x,startPoint.y,
	                                	startPoint.x-endPoint.x,endPoint.y-startPoint.y);
	                            else 
	                                rectAreaStampa = new Rectangle2D.Double(endPoint.x,endPoint.y,
	                                    startPoint.x-endPoint.x,startPoint.y-endPoint.y);
                                
                                repaint();                          
                		    break;  
                        case LOOP:                		
                		// Per selezionare gli oggetti il trascinamento del mouse deve 
                		// avvenire da sinistra verso destra e dall'alto verso il basso.
                                endPoint = updateGetPoint(e.getPoint());
	                         if(endPoint.x>startPoint.x)
	                            if(endPoint.y>startPoint.y)
	                                rectAreaStampa = new Rectangle2D.Double(startPoint.x,startPoint.y,
	                                    endPoint.x-startPoint.x,endPoint.y-startPoint.y);
	                            else 
	                                rectAreaStampa = new Rectangle2D.Double(startPoint.x,endPoint.y,
	                                    endPoint.x-startPoint.x,startPoint.y-endPoint.y);
	                         else
	                            if(endPoint.y>startPoint.y)
	                                rectAreaStampa = new Rectangle2D.Double(endPoint.x,startPoint.y,
	                                	startPoint.x-endPoint.x,endPoint.y-startPoint.y);
	                            else 
	                                rectAreaStampa = new Rectangle2D.Double(endPoint.x,endPoint.y,
	                                    startPoint.x-endPoint.x,startPoint.y-endPoint.y);
                                
                                repaint();                          
                		    break;             
                                    
                        case CONSTRAINT:
                            break;   
                        case MOV_PAR:
                            if(ParalleloCorrente !=null){
                                ParalleloCorrente.setPoint_mov(updateGetPoint(e.getPoint()));  
                                ParalleloCorrente.updateParallelPosizione_am();
                                plugDataWin.getStatusBar().setText("Parallel selected and moved.");
                                repaint();
                            }
                            //ezio 2006   core.internal.plugin.file.FileManager.setModificata(true);
                            plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true); // ezio 2006
                          break;
                        case MOV_SIM:
                            if(SimCorrente !=null){
                                SimCorrente.setPoint_mov(updateGetPoint(e.getPoint()));  
                                SimCorrente.updateSimPosizione_am();
                                plugDataWin.getStatusBar().setText("Simultaneous selected and moved.");
                                repaint();
                            }
                            //ezio 2006   core.internal.plugin.file.FileManager.setModificata(true);
                            plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true); // ezio 2006
                         break;   
                        case MOV_LOOP:
                            if(LoopCorrente !=null){
                                LoopCorrente.setPoint_mov(updateGetPoint(e.getPoint()));  
                                LoopCorrente.updateLoopPosizione_am();
                                plugDataWin.getStatusBar().setText("Loop selected and moved.");
                                repaint();
                            }
                            //ezio 2006   core.internal.plugin.file.FileManager.setModificata(true);
                            plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true); // ezio 2006
                          break;     
                        case ATTESA:                    
                            seqEle.getListaClasse().noSelected();
                            seqEle.getListaSeqLink().noSelected();
                            seqEle.getListaConstraint().noSelected();
                            seqEle.getListaParallel().noSelected();
                            seqEle.getListaSim().noSelected();
                            seqEle.getListaLoop().noSelected();
                            
                            ClasseCorrente = (ElementoClasse)(seqEle.getListaClasse().getElement(updateGetPoint(e.getPoint()))); 		
                            if (ClasseCorrente != null){
                            	if(primaVolta){
                            		unicoInvio = ClasseCorrente.testAndSet();
                            		primaVolta = false;
                            	}
                        	
                                // E' stato selezionata una classe.
                                ClasseCorrente.setSelected(true);
                                Point p = e.getPoint();
                                p.x = p.x+spiazzamentoX;
                                p.y = p.y+spiazzamentoY;
                                ClasseCorrente.setPoint(updateGetPoint(p));
                                plugDataWin.getStatusBar().setText("Class " + ClasseCorrente.getName() + " selected and moved.");
                                seqEle.getListaSeqLink().updateListaCanalePosizione(ClasseCorrente);
                                for (int i=0;i<seqEle.getListaConstraint().size();i++)
                                	seqEle.getListaConstraint().getElement(i).updateConstraintPosizione();
                                for (int i=0;i<seqEle.getListaParallel().size();i++)
                                	seqEle.getListaParallel().getElement(i).updateParallelPosizione();
                                for (int i=0;i<seqEle.getListaSim().size();i++)
                                	seqEle.getListaSim().getElement(i).updateSimPosizione();
                                for (int i=0;i<seqEle.getListaLoop().size();i++)
                                	seqEle.getListaLoop().getElement(i).updateLoopPosizione();
                                SequenceEditorStatus = SPOSTA_PROCESSO;                            
                                repaint();
                            }
                            else{
                                ParalleloCorrente =(ElementoParallelo)(seqEle.getListaParallel().getElementSelected(updateGetPoint(e.getPoint())));
                                SimCorrente =(ElementoSim)(seqEle.getListaSim().getElementSelected(updateGetPoint(e.getPoint())));
                                LoopCorrente =(ElementoLoop)(seqEle.getListaLoop().getElementSelected(updateGetPoint(e.getPoint())));
                                if(ParalleloCorrente != null){                                                                                                          
                                        if(primaVolta_par){
                                            unicoInvio_par = ParalleloCorrente.testAndSet();
                                            primaVolta_par = false;
                                        }  
                                       if(ParalleloCorrente.isSelezionato_par(updateGetPoint(e.getPoint()))){                                          
                                           ParalleloCorrente.Mod_par(updateGetPoint(e.getPoint()));
                                           ParalleloCorrente.setSelected(true);
                                           plugDataWin.getStatusBar().setText("Parallel margin  selected.");
                                           SequenceEditorStatus = MOV_PAR;
                                           repaint();
                                        }
                                        else{  

                                            // E' stato selezionata un parallelo.
                                            ParalleloCorrente.setSelected(true);                                  
                                            ParalleloCorrente.setPoint_lincen(updateGetPoint(e.getPoint()));
                                            ParalleloCorrente.updateParallelPosizione_ao();
                                            plugDataWin.getStatusBar().setText("Parallel operator selected and moved.");                                                                          
                                            SequenceEditorStatus = SPOSTA_PROCESSO;                            
                                            repaint();
                                        }
                                    
                                }
                                else if(SimCorrente != null){                                                                                                          
                                        if(primaVolta_sim){
                                            unicoInvio_sim = SimCorrente.testAndSet();
                                            primaVolta_sim = false;
                                        }  
                                       if(SimCorrente.isSelezionato_sim(updateGetPoint(e.getPoint()))){                                          
                                           SimCorrente.Mod_sim(updateGetPoint(e.getPoint()));
                                           SimCorrente.setSelected(true);
                                           plugDataWin.getStatusBar().setText("Simultaneous margin  selected.");
                                           SequenceEditorStatus = MOV_SIM;
                                           repaint();
                                        }
                                    
                                }
                                else if(LoopCorrente != null){                                                                                                          
                                        if(primaVolta_loop){
                                            unicoInvio_loop = LoopCorrente.testAndSet();
                                            primaVolta_loop = false;
                                        }  
                                       if(LoopCorrente.isSelezionato_Loop(updateGetPoint(e.getPoint()))){                                          
                                           LoopCorrente.Mod_Loop(updateGetPoint(e.getPoint()));
                                           LoopCorrente.setSelected(true);
                                           plugDataWin.getStatusBar().setText("Loop margin  selected.");
                                           SequenceEditorStatus = MOV_LOOP;
                                           repaint();
                                        }
                                    
                                }
                                else{
                                    // Nessuno elemento selezionato.
                                    SequenceEditorStatus = ATTESA;	
                                }
                            }
                            break;
	                
                            case INSERIMENTO_MESSAGGIO:
                                    break;

                            case DISEGNA_MESSAGGIO:
                                break;
                         
                            case SPOSTA_PROCESSO:
                                if(ClasseCorrente != null){
                                    // Aggiornamento della posizione del processo selezionato.
                                    Point p = e.getPoint();
                                    p.x = p.x+spiazzamentoX;
                                    p.y = p.y+spiazzamentoY;
                                    ClasseCorrente.setPoint(updateGetPoint(p));
                                    seqEle.getListaSeqLink().updateListaCanalePosizione(ClasseCorrente);
                                    for(int i=0;i<seqEle.getListaSeqLink().size();i++)
                                            seqEle.getListaConstraint().updateListaConstraintPosizione((ElementoSeqLink)seqEle.getListaSeqLink().getElement(i));
                                    for(int i=0;i<seqEle.getListaParallel().size();i++)
                                            seqEle.getListaParallel().getElement(i).updateParallelPosizione();
                                    for(int i=0;i<seqEle.getListaSim().size();i++)
                                            seqEle.getListaSim().getElement(i).updateSimPosizione();
                                    for(int i=0;i<seqEle.getListaLoop().size();i++)
                                            seqEle.getListaLoop().getElement(i).updateLoopPosizione();
                                    repaint();
                                }
                                else{
                                    if(ParalleloCorrente !=null){                                        
                                       // Aggiornamento della posizione della linea centrale dell'operatore selezionato.                                        
                                        ParalleloCorrente.setPoint_lincen(updateGetPoint(e.getPoint()));
                                        ParalleloCorrente.updateParallelPosizione_ao();
                                        repaint();                                                                                                                           
                                    }
                                }
                                                              
                                //ezio 2006   core.internal.plugin.file.FileManager.setModificata(true);
                                plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true); // ezio 2006
                            break;
                                
                            default:                               
	                    	localSequenceToolBar.setNoPressed();
	                    	SequenceEditorStatus = ATTESA;
	                    	plugDataWin.getStatusBar().setText("Ready.");                    
	                    	ClasseCorrente = (ElementoClasse)(seqEle.getListaClasse().getElement(updateGetPoint(e.getPoint())));
	                    	if (ClasseCorrente != null){
                                    ClasseCorrente.setSelected(true);
                                    plugDataWin.getStatusBar().setText("Class " + ClasseCorrente.getName() + " selected.");                        
                                    ClasseCorrente.setPoint(updateGetPoint(e.getPoint()));
                                    seqEle.getListaSeqLink().updateListaCanalePosizione(ClasseCorrente);
                                    for(int i=0;i<seqEle.getListaSeqLink().size();i++)
                                	seqEle.getListaConstraint().updateListaConstraintPosizione((ElementoSeqLink)seqEle.getListaSeqLink().getElement(i));
                                    for(int i=0;i<seqEle.getListaParallel().size();i++)
	                        	seqEle.getListaParallel().getElement(i).updateParallelPosizione();
                                    for(int i=0;i<seqEle.getListaSim().size();i++)
	                        	seqEle.getListaSim().getElement(i).updateSimPosizione();
                                    for(int i=0;i<seqEle.getListaLoop().size();i++)
	                        	seqEle.getListaLoop().getElement(i).updateLoopPosizione();
                                    SequenceEditorStatus = SPOSTA_PROCESSO;
                                    repaint();
	                    	} 
                                else{
                                    ParalleloCorrente =(ElementoParallelo)(seqEle.getListaParallel().getElementSelected(updateGetPoint(e.getPoint())));
                                    SimCorrente =(ElementoSim)(seqEle.getListaSim().getElementSelected(updateGetPoint(e.getPoint())));
                                    LoopCorrente =(ElementoLoop)(seqEle.getListaLoop().getElementSelected(updateGetPoint(e.getPoint()))); 
                                    if(ParalleloCorrente != null){                                      
                                            ParalleloCorrente.setSelected(true);
                                            
                                            if(ParalleloCorrente.isSelezionato_par(updateGetPoint(e.getPoint()))){
                                               ParalleloCorrente.setPoint_mov(updateGetPoint(e.getPoint()));
                                               ParalleloCorrente.updateParallelPosizione_am();
                                               plugDataWin.getStatusBar().setText("Parallel margin  selected.");
                                               SequenceEditorStatus = MOV_PAR;
                                               repaint();
                                            }
                                            else{
                                                plugDataWin.getStatusBar().setText("Parallel  selected.");                        
                                                ParalleloCorrente.setPoint_lincen(updateGetPoint(e.getPoint()));
                                                ParalleloCorrente.updateParallelPosizione_ao();
                                                SequenceEditorStatus = SPOSTA_PROCESSO;
                                                repaint(); 
                                            }
                                    }
                                    else if(SimCorrente != null){                                      
                                            SimCorrente.setSelected(true);
                                            
                                            if(SimCorrente.isSelezionato_sim(updateGetPoint(e.getPoint()))){
                                               SimCorrente.setPoint_mov(updateGetPoint(e.getPoint()));
                                               SimCorrente.updateSimPosizione_am();
                                               plugDataWin.getStatusBar().setText("Simultaneous margin  selected.");
                                               SequenceEditorStatus = MOV_SIM;
                                               repaint();
                                            }
                                    }
                                    else if(LoopCorrente != null){                                      
                                            LoopCorrente.setSelected(true);
                                            
                                            if(LoopCorrente.isSelezionato_Loop(updateGetPoint(e.getPoint()))){
                                               LoopCorrente.setPoint_mov(updateGetPoint(e.getPoint()));
                                               LoopCorrente.updateLoopPosizione_am();
                                               plugDataWin.getStatusBar().setText("Loop margin  selected.");
                                               SequenceEditorStatus = MOV_LOOP;
                                               repaint();
                                            }
                                    }
                                }
	                    	break;
	                    	
                            }
	        	}
	        }
                
                /** Trascinamento del mouse. */
	        public void mouseMoved(MouseEvent e)
	        {
                    Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
                    ((JPanel)e.getSource()).scrollRectToVisible(r);
	            // Gestione degli eventi di trascinamento solo se
	            // interni all'area di editing (delimitata da rettangolo).
	            if (rettangolo.contains(updateGetPoint(e.getPoint()))){
                        switch (SequenceEditorStatus){
                        case ATTESA:
                            ParalleloCorrente =(ElementoParallelo)(seqEle.getListaParallel().getElementSelect());
                            SimCorrente =(ElementoSim)(seqEle.getListaSim().getElementSelect());
                            LoopCorrente =(ElementoLoop)(seqEle.getListaLoop().getElementSelect());
                            if(ParalleloCorrente != null){                                                                  
                                if(ParalleloCorrente.isSelezionato_par(updateGetPoint(e.getPoint()))) 
                                {                               
                                   ParalleloCorrente.Mod_par(updateGetPoint(e.getPoint()));
                                   plugDataWin.getStatusBar().setText("Parallel margin selected.");
                                   repaint();
                                }
                                else{
                                    ParalleloCorrente.updateParallelPosizione();
                                    plugDataWin.getStatusBar().setText("Parallel  selected.");
                                    repaint();
                                }                                                               
                            }
                            else if(SimCorrente != null){                                                                  
                                if(SimCorrente.isSelezionato_sim(updateGetPoint(e.getPoint()))) 
                                {                               
                                   SimCorrente.Mod_sim(updateGetPoint(e.getPoint()));
                                   plugDataWin.getStatusBar().setText("Simultaneous margin selected.");
                                   repaint();
                                }
                                else{
                                    SimCorrente.updateSimPosizione();
                                    plugDataWin.getStatusBar().setText("Simultaneous  selected.");
                                    repaint();
                                }                                                               
                            }
                            else if(LoopCorrente != null){                                                                  
                                if(LoopCorrente.isSelezionato_Loop(updateGetPoint(e.getPoint()))) 
                                {                               
                                   LoopCorrente.Mod_Loop(updateGetPoint(e.getPoint()));
                                   plugDataWin.getStatusBar().setText("Loop margin selected.");
                                   repaint();
                                }
                                else{
                                    LoopCorrente.updateLoopPosizione();
                                    plugDataWin.getStatusBar().setText("Loop  selected.");
                                    repaint();
                                }                                                               
                            }
                        }
                    }
                }
            }	    
	    
	    /** Restituisce un punto le cui coordinate sono quelle del punto in ingresso
	    	aggiornate tenendo conto dei fattori di scala per l'asse X e per l'asse Y. */
	    private Point updateGetPoint(Point pnt)
	    {
	        Point pp = new Point(roundToInt(pnt.x/scaleX),roundToInt(pnt.y/scaleY));
	        return pp;
	    }
	    
	    
	    /** Operazione di copy_ Non implementata!! */
	    public void opCopy()
	    {       
	    }    
	
	
	    /** Operazione di paste_ Non implementata!! */
	    public void opPaste()
	    {
	    } 
	
	 	    
	    /** Operazione di cut_ Non implementata!! */
	    public void opCut()
	    {
	    }
	
	    
	    /** Operazione di redo. */
	    public void opRedo()
	    {	
	    	undoManager.redo(scaleX, scaleY);
	    	repaint();
	    }
	
	    
	    /** Operazione di undo. */
	    public void opUndo()
	    {
	    	undoManager.undo(scaleX, scaleY);
	    	repaint();
	    }
	
	    
	    /** Operazione di cancellazione di link. */
	    public void opDel()
	    {
                
	    	seqEle.getListaSeqLink().removeAllSelected();
	    	seqEle.getListaClasse().removeAllSelected();
                seqEle.getListaConstraint().removeAllSelected();
                seqEle.getListaParallel().removeParallelInLink();
                seqEle.getListaSim().removeSimInLink();
                seqEle.getListaLoop().removeLoopInLink();
                seqEle.removeAllSelected_Par();                
                seqEle.removeAllSelected_Sim();
                seqEle.removeAllSelected_Loop();
                seqEle.removeAllSelected_Constr();
                
                
	    	repaint();
	    }
	
	    
	    /** Creazione dell'immagine jpeg del Sequence Diagram. */
	    public void opImg()
	    {
	    	boolean ctrlImage;
	    	JpegImage Immagine;
	    	Graphics2D imgG2D;
	    	    	
	    	Immagine = new JpegImage(0,0,rWidth,rHeight,scaleX,scaleY,editorColor);
	    	imgG2D = Immagine.getImageGraphics2D();
	        if (imgG2D != null){
	        	imgG2D.draw(linea);
	        	seqEle.getListaTime().paintLista(imgG2D);        	
	        	seqEle.getListaClasse().paintLista(imgG2D);
	        	seqEle.getListaSeqLink().paintLista(imgG2D);
	        	seqEle.getListaConstraint().paintLista(imgG2D);
                        seqEle.getListaParallel().paintLista(imgG2D);
                        seqEle.getListaSim().paintLista(imgG2D);
                        seqEle.getListaLoop().paintLista(imgG2D); 
				plugDataWin.getStatusBar().setText("Select file and wait.");         	
	        	ctrlImage = Immagine.saveImageFile((Frame)getTopLevelAncestor());
				if (ctrlImage){
					plugDataWin.getStatusBar().setText("Image saved.  Ready.");
				}
				else{
					plugDataWin.getStatusBar().setText("Image not saved.  Ready.");
				}        	
	    	}       	                 	
	    }
	
	
	    /** Ripristina la scala del pannello al 100%. */
	    public void resetScale()
	    {
			super.resetScale();
			plugDataWin.getStatusBar().setText("<Zoom Reset> ok!!  Ready.");		
	    }  
	
	    
	    /** Operazione di zoom sull'asse X. */
	    public void incScaleX()
	    {
			super.incScaleX();
			plugDataWin.getStatusBar().setText("<Stretch Horizontal> ok!!  Ready.");       
	    }
	
	
	    /** Operazione di zoom negativo sull'asse X. */    
	    public void decScaleX()
	    {
			super.decScaleX();
			plugDataWin.getStatusBar().setText("<Compress Horizontal> ok!!  Ready."); 
	    }
	
	    
	    /** Operazione di zoom sull'asse Y. */    
	    public void incScaleY()
	    {
			super.incScaleY();
			plugDataWin.getStatusBar().setText("<Stretch Vertical> ok!!  Ready.");       
	    }
	    
	
	    /** Operazione di zoom negativo sull'asse Y. */    
	    public void decScaleY()
	    {
			super.decScaleY();
			plugDataWin.getStatusBar().setText("Compress Vertical> ok!!  Ready."); 
	    }
	
            public PlugDataWin getPlugDW(){
                return  plugDataWin;
            } 
            
	    /** Restituisce un riferimento alla lista dei messaggi. */
	    public ListaSeqLink getListaLink() {
	    	return seqEle.getListaSeqLink();
	    }
	
	    /** Restituisce un riferimento alla lista delle classi. */
	    public ListaClasse getListaClasse() {
	    	return seqEle.getListaClasse();
	    }
	
	    /** Restituisce un riferimento alla lista dei tempi. */
	    public ListaTime getListaDeiTempi() {
	    	return seqEle.getListaTime();
	    }
	    
	    /**
	     * ritorna l'elemento associato all'editor
	     * @return elemento associato all'editor
	     */
	    public SequenceElement getSequenceElement(){
	    	return this.seqEle;
	    }
            
           /** Restituisce un riferimento alla lista dei constraint. */
            public ListaConstraint getListaConstraint()
            {
                return seqEle.getListaConstraint();
            }
            
            /** Restituisce un riferimento alla lista degli operatori paralleli. */
            public ListaParallel getListaParallel()
	    {
	        return seqEle.getListaParallel();
	    }
            
            /** Restituisce un riferimento alla lista degli operatori simultanei. */
            public ListaSim getListaSim()
	    {
	        return seqEle.getListaSim();
	    }
            
            /** Restituisce un riferimento alla lista degli operatori loop. */
            public ListaLoop getListaLoop()
	    {
	        return seqEle.getListaLoop();
	    }

            /**
             * sets the Sequence element
             * @return void
             */
            public void setSequenceElement(SequenceElement se){
                    seqEle=se;
            }	    

            /** Aggiunga una fascia (in ultima posizione) al Sequence Diagram. */
            public void addTimeLine()
            {
			
                        ElementoTime et;
                        //ezio 2006   core.internal.plugin.file.FileManager.setModificata(true);
                        plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true); // ezio 2006
                    if (seqEle.getListaTime().isEmpty()){
				seqEle.getListaTime().addFirst();
			}
			else{
				seqEle.getListaTime().addLast();
				et = seqEle.getListaTime().getLast();
				// Aggiornamento delle dimensioni dell'editor per 
				// contenere la fascia aggiunta.
				if (et.getMaxY()> rHeight){
					rHeight = rHeight + STEP_EDITOR;
	        		rettangolo.setRect(0,0,rWidth,rHeight);				
					resetScale();
				}		
			}
			repaint();
		}
		
		
		/** Rimuove la fascia temporale sulla quale ï¿½ stato
			premuto il bottone destro del mouse. */
		public void removeTimeLine()
		{
			seqEle.getListaTime().removeElement(TimePopup);
			seqEle.getListaSeqLink().removeAllLink(TimePopup);
	       
			  //ezio 2006   core.internal.plugin.file.FileManager.setModificata(true);
            plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true); // ezio 2006
        repaint();
		}
		
	
		/** Aggiunge una fascia temporale prima di quella sulla 
			quale ï¿½ stato premuto il bottone destro del mouse. */
		public void addTimeLineFirst()
		{
			long tempo = TimePopup.getTime();
			  //ezio 2006   core.internal.plugin.file.FileManager.setModificata(true);
            plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true); // ezio 2006
        
			seqEle.getListaTime().updateElementTimeTwo(tempo + 1, TimePopup);
			seqEle.getListaTime().addElementTimeFirst(tempo, TimePopup);
			seqEle.getListaSeqLink().updateListaSeqLinkPosizione();
			repaint();
		}
			
		
		/** Abilita/disabilita la visualizzazione delle linee e del 'time'
			nella rappresentazione delle fasce temporali. */		
		public void setGraphOptions(boolean ctrlStringTime, boolean ctrlLineTime)
		{
			seqEle.getListaTime().setStringVisible(ctrlStringTime);
			seqEle.getListaTime().setLineVisible(ctrlLineTime);
		}
		
		
		/** Verifica se la visualizzazione del 'time' 
			nella fascia temporale ï¿½ abilitata. */
		public boolean isStringTimeVisible()
		{
			return (seqEle.getListaTime().isStringTimeVisible());
		}
		
		
		/** Verifica se la visualizzazione delle linee 
			nella fascia temporale ï¿½ abilitata. */
		public boolean isLineTimeVisible()
		{
			return (seqEle.getListaTime().isLineTimeVisible());
		}
		
	
	    /** Aggiorna le proprietï¿½ del Sequence. */
	    public void updateSequence(String nomeSequence, DefaultListModel listaClassi, boolean ctrlStringTime, boolean ctrlLineTime)
	    {
	    	String nomeClasse;
	    	ElementoClasse ClasseCorrente;
	    	int indice;
	    	
	    	this.setName(nomeSequence);
	    	seqEle.getListaTime().setStringVisible(ctrlStringTime);
			seqEle.getListaTime().setLineVisible(ctrlLineTime);
			
	        // Eliminazione classi dal Sequence Diagram.
	        boolean loop = true;
	        while (loop){
	        	loop=false;
	        	for (int i=0; i<seqEle.getListaClasse().size(); i++){
	        		ClasseCorrente = (ElementoClasse)(seqEle.getListaClasse().getElement(i));
	        		nomeClasse = ClasseCorrente.getName();
	        		if (!listaClassi.contains(nomeClasse)){
	        			seqEle.getListaClasse().removeElement(ClasseCorrente);
	        			// Eliminazione dei link collegati con la classe appena cancellata.
	        			seqEle.getListaSeqLink().removeAllLink(ClasseCorrente);
	        			loop=true;
	        		}
	        	}
	    	}
	    	
	        // Inserimento classi nel Sequence Diagram.        
	        for (int i=0; i<listaClassi.size(); i++){
	        	nomeClasse = (String)(listaClassi.get(i));
	        	if (!seqEle.getListaClasse().giaPresente(nomeClasse)){
	        		ClasseCorrente = new ElementoClasse(posizioneLibera()*STEP_CLASSE+MARGINE_CLASSE,MARGINE_CLASSE,
	        			(String)listaClassi.get(i),null);
	        		seqEle.getListaClasse().addElement(ClasseCorrente);        		
	        	}
	        }    	
			repaint();            	
	    }	
	
	    
	    /**
	     * trova una posizione libera per l'elemento da inserire
	     * @return numero di posizione
	     */
	    private int posizioneLibera()
	    {
	    	int i=-1;
	    	int extS;
	    	int extD;
	    	int posClasse;
	    	boolean libera = false;
	    	boolean occupata;
	    	
	    	while (!libera){
	    		i++;
	    		extS = SequenceEditor.MARGINE_CLASSE+i*SequenceEditor.STEP_CLASSE-5;
	    		extD = SequenceEditor.MARGINE_CLASSE+i*SequenceEditor.STEP_CLASSE+5;
	    		occupata = false;
	    		for (int j=0; j<seqEle.getListaClasse().size(); j++){
	    			posClasse = ((ElementoClasse)(seqEle.getListaClasse().getElement(j))).getTopX();
	    			if ((posClasse <= extD) && (posClasse >= extS)){
	    				occupata = true;
	    			}
	    		}
	    		libera = !occupata;
	    	}
	    	return i;
	    }   
	
	
		/** Metodo per ricostruire la struttura delle classi a partire
			dalle informazioni memorizzate sul file. */
		public void restoreFromFile(StatusBar sb, EditToolBar etb, SequenceToolBar stb)
		{
            super.restoreFromFile();
            seqEle.getListaClasse().restoreFromFile();
            seqEle.getListaSeqLink().restoreFromFile();
            seqEle.getListaClasse().noSelected();
            seqEle.getListaSeqLink().noSelected();
            seqEle.getListaTime().restoreFromFile();
            seqEle.getListaConstraint().restoreFromFile();    
            seqEle.getListaSim().restoreFromFile();
            seqEle.getListaLoop().restoreFromFile();
            seqEle.getListaParallel().restoreFromFile();
            seqEle.getListaConstraint().noSelected();
            seqEle.getListaParallel().noSelected();
            seqEle.getListaSim().noSelected();
            seqEle.getListaLoop().noSelected();
            
            SequenceEditorStatus = ATTESA;
            BloccoPulsante = false;
            localEditToolBar = etb;
            localSequenceToolBar = stb;
            linea = new Line2D.Double(20,MARGINE_SUPERIORE,1180,MARGINE_SUPERIORE);
            addMouseListener(new ClassEditorClickAdapter());
            addMouseMotionListener(new ClassEditorMotionAdapter());
		} 
	
	
		/** Restituisce una lista contenente, senza duplicati, i nomi
			di tutti i flussi (messaggi) definiti nel Sequence Diagram. */
		public LinkedList getAllMessageName()
		{
			if (seqEle.getListaSeqLink() != null) {
				return seqEle.getListaSeqLink().getAllMessageName();
			} 
			else {
				return (new LinkedList());
			}
		}
	
	
		public boolean isAsynchronous(String nomeC)
		{
			if (seqEle.getListaSeqLink() != null) {
				return (seqEle.getListaSeqLink().isAsynchronous(nomeC));
			} 
			else {
				return false;
			}		
		}
	
	
		public boolean isSynchronous(String nomeC)
		{
			if (seqEle.getListaSeqLink() != null) {
				return (seqEle.getListaSeqLink().isSynchronous(nomeC));
			} 
			else {
				return false;
			}
		}
		
		
	
		public LinkedList getListLinkSequence()
		{
			if (seqEle.getListaSeqLink() != null) {
				return (seqEle.getListaSeqLink().getListLinkSequence());
			} 
			else {
				return (new LinkedList());
			}
		}
		
		
		public LinkedList getAllClassName()
		{
			if (seqEle.getListaClasse() != null) {
				return (seqEle.getListaClasse().getListaAllClassName());
			} 
			else {
				return (new LinkedList());
			}
		}	
	
	
	    /** Imposta le proprietï¿½ del pannello: altezza, larghezza e colore dello sfondo. */
	    public void setCEProperties(int nWidth, int nHeight, Color nBGColor)
	    {
	        super.setCEProperties(nWidth,nHeight,nBGColor);
	        linea.setLine(20,MARGINE_SUPERIORE,(nWidth-20),MARGINE_SUPERIORE);
	        seqEle.getListaTime().setLine(nWidth-20);
	        seqEle.getListaClasse().setLine(nHeight-20);
	    } 
            /** Setta l'operatore parallelo in base ai link selezionati**/
            public void createParallel(LinkedList list){
                int level=0;
                for(int i=0;i<list.size();i++){
                    ElementoSeqLink link=(ElementoSeqLink) list.get(i); 
                    if(link.hasConstraint())
                        {
                            if(!(link.isConstraintPre()))
                            {
                                plugDataWin.getStatusBar().setText("Non sono ammessi open e closed constraints ");
                                JOptionPane.showMessageDialog(null, "It's not possible ", "Error!",JOptionPane.ERROR_MESSAGE);
                                seqEle.getListaSeqLink().noSelected();                    
                                seqEle.getListaClasse().noSelected();
                                return;
                            }   
                        }
                    if(link.isParallel()
                       ||link.isSimultaneous()
                       ||link.isStrict()){                                               
                        plugDataWin.getStatusBar().setText("Non è possibile applicare il concetto di parallelismo");
                        JOptionPane.showMessageDialog(null, "It's not possible ", "Error!",JOptionPane.ERROR_MESSAGE);
                        seqEle.getListaSeqLink().noSelected();                    
                        seqEle.getListaClasse().noSelected();
                        return;
                    }
                    else{
                        level++;
                        if(list.getLast().equals(link)){    
                            if(link.getTimeFrom().getTime()==0
                               ||link.getPrec()==null){
                                plugDataWin.getStatusBar().setText("Non è possibile applicare il concetto di parallelismo");
                                JOptionPane.showMessageDialog(null, "It's not possible ", "Error!",JOptionPane.ERROR_MESSAGE);
                                seqEle.getListaSeqLink().noSelected();                    
                                seqEle.getListaClasse().noSelected();
                                return;
                            }
                            else{    
                                if(level==1){
                                    plugDataWin.getStatusBar().setText("Non è possibile applicare il concetto di parallelismo");
                                    JOptionPane.showMessageDialog(null, "It's not possible ", "Error!",JOptionPane.ERROR_MESSAGE);
                                    seqEle.getListaSeqLink().noSelected();                    
                                    seqEle.getListaClasse().noSelected();
                                    return; 
                                }
                                else{
                                    ParalleloCorrente = new ElementoParallelo(ListaStatisel,seqEle,0,null,1,ListaStatisel.size()-1,false); 
                                    seqEle.getListaParallel().addElement(ParalleloCorrente);
                                    
                                    //ezio 2006
                                    this.plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);
                                 //
                                    
                                    for(int k=0;k<list.size();k++){
                                        ElementoSeqLink link1=(ElementoSeqLink) list.get(k);
                                        link1.setParallel(true,ParalleloCorrente.getId()); 
                                    }
                                    
                                    // Aggiornamento della barra di stato.                     
                                    plugDataWin.getStatusBar().setText("Parallel operator inserted.");
                                    //ezio 2006   core.internal.plugin.file.FileManager.setModificata(true);
                                    plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true); // ezio 2006
                                
                                   
                                }
                            }
                        }                        
                    }                    
                }
            }
            /** Setta l'operatore simultaneo in base ai link selezionati**/
            public void createSim(LinkedList list){
                int level=0;
                for(int i=0;i<list.size();i++){
                    ElementoSeqLink link=(ElementoSeqLink) list.get(i); 
                    if(link.hasConstraint())
                        {
                            if(link.isConstraintChCloFut() ||
                                    link.isConstraintChOpFut() ||
                                    link.isConstraintChCloPast() ||
                                    link.isConstraintChOpPast() ||
                                    link.isConstraintUnCloFut() ||
                                    link.isConstraintUnOpFut() ||
                                    link.isConstraintUnCloPast() ||
                                    link.isConstraintUnOpPast())
                            {
                                plugDataWin.getStatusBar().setText("I constraints con catene come attributi non sono ammessi");
                                JOptionPane.showMessageDialog(null, "It's not possible ", "Error!",JOptionPane.ERROR_MESSAGE);
                                seqEle.getListaSeqLink().noSelected();                    
                                seqEle.getListaClasse().noSelected();
                                return;
                            }   
                        }
                    if(link.isParallel()
                       ||link.isSimultaneous()
                       ||link.isStrict()
                       ){                        
                        plugDataWin.getStatusBar().setText("Non è possibile applicare il concetto di simultaneità");
                        JOptionPane.showMessageDialog(null, "It's not possible ", "Error!",JOptionPane.ERROR_MESSAGE);
                        seqEle.getListaSeqLink().noSelected();                    
                        seqEle.getListaClasse().noSelected();
                        return;
                    }
                    else{
                        level++;
                        if(list.getLast().equals(link)){    
                            if(link.getTimeFrom().getTime()==0
                               ||link.getPrec()==null){
                                plugDataWin.getStatusBar().setText("Non è possibile applicare il concetto di simultaneità");
                                JOptionPane.showMessageDialog(null, "It's not possible ", "Error!",JOptionPane.ERROR_MESSAGE);
                                seqEle.getListaSeqLink().noSelected();                    
                                seqEle.getListaClasse().noSelected();
                                return;
                            }
                            else{    
                                if(level==1){
                                    plugDataWin.getStatusBar().setText("Non è possibile applicare il concetto di simultaneità");
                                    JOptionPane.showMessageDialog(null, "It's not possible ", "Error!",JOptionPane.ERROR_MESSAGE);
                                    seqEle.getListaSeqLink().noSelected();                    
                                    seqEle.getListaClasse().noSelected();
                                    return; 
                                }
                                else{
                                    SimCorrente = new ElementoSim(ListaStatisel,seqEle,0,null,false); 
                                    seqEle.getListaSim().addElement(SimCorrente);
                                    //ezio 2006
                                    this.plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);
                                  ///
                                    
                                    for(int k=0;k<list.size();k++){
                                        ElementoSeqLink link1=(ElementoSeqLink) list.get(k);
                                        link1.setSimultaneous(true,SimCorrente.getId()); 
                                    }
                                    
                                    // Aggiornamento della barra di stato.                     
                                    plugDataWin.getStatusBar().setText("Simultaneous operator inserted.");
                                    //ezio 2006   core.internal.plugin.file.FileManager.setModificata(true);
                                    plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true); // ezio 2006
                                 }            
                            }
                        }                        
                    }                    
                }
            }
            
            /** Setta l'operatore loop in base ai link selezionati**/
            public void createLoop(LinkedList list){
                int level=0;
                for(int i=0;i<list.size();i++){
                    ElementoSeqLink link=(ElementoSeqLink) list.get(i); 
                    if(link.isParallel()
                       ||link.isSimultaneous()
                       ||link.isStrict()){     
                        plugDataWin.getStatusBar().setText("Non è possibile applicare il concetto di loop");
                        JOptionPane.showMessageDialog(null, "It's not possible ", "Error!",JOptionPane.ERROR_MESSAGE);
                        seqEle.getListaSeqLink().noSelected();                    
                        seqEle.getListaClasse().noSelected();
                        return;
                    }
                    else{
                        level++;
                        if(list.getLast().equals(link)){      
                            LoopCorrente = new ElementoLoop(ListaStatisel,seqEle,0,0,0,null,false); 
                            seqEle.getListaLoop().addElement(LoopCorrente);
                            
                            //ezio 2006
                            this.plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);
                          ////
                            
                            for(int k=0;k<list.size();k++){
                                ElementoSeqLink link1=(ElementoSeqLink) list.get(k);
                                link1.setLoop(true,0,0,LoopCorrente.getId()); 
                            }

                            // Aggiornamento della barra di stato.                     
                            plugDataWin.getStatusBar().setText("Loop operator inserted.");
                            //ezio 2006   core.internal.plugin.file.FileManager.setModificata(true);
                            plugDataWin.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true); // ezio 2006
                        
                            if(LoopCorrente.getfirst_link().hasConstraint())
                            {
                                if(LoopCorrente.getfirst_link().isConstraintPastClo() 
                                    || LoopCorrente.getfirst_link().isConstraintPastOp()
                                    || LoopCorrente.getfirst_link().isConstraintChCloPast()
                                    || LoopCorrente.getfirst_link().isConstraintChOpPast()
                                    || LoopCorrente.getfirst_link().isConstraintUnCloPast()
                                    || LoopCorrente.getfirst_link().isConstraintUnOpPast())
                                {
                                    plugDataWin.getStatusBar().setText("Non è possibile applicare il concetto di loop");
                                    JOptionPane.showMessageDialog(null, "It's not possible ", "Error!",JOptionPane.ERROR_MESSAGE);
                                    seqEle.getListaSeqLink().noSelected();                    
                                    seqEle.getListaClasse().noSelected();                                        
                                    seqEle.getListaLoop().removeElement(LoopCorrente);

                                }
                            }
                            if(LoopCorrente.getlast_link().hasConstraint())
                            {
                                if(LoopCorrente.getlast_link().isConstraintFutClo() 
                                    || LoopCorrente.getlast_link().isConstraintFutOp()
                                    || LoopCorrente.getlast_link().isConstraintChCloFut()
                                    || LoopCorrente.getlast_link().isConstraintChOpFut()
                                    || LoopCorrente.getlast_link().isConstraintUnCloFut()
                                    || LoopCorrente.getlast_link().isConstraintUnOpFut())
                                {
                                    plugDataWin.getStatusBar().setText("Non è possibile applicare il concetto di loop");
                                    JOptionPane.showMessageDialog(null, "It's not possible ", "Error!",JOptionPane.ERROR_MESSAGE);
                                    seqEle.getListaSeqLink().noSelected();                    
                                    seqEle.getListaClasse().noSelected();                                        
                                    seqEle.getListaLoop().removeElement(LoopCorrente);

                                }
                            }                            
                        }                        
                    }                    
                }
            }

            
	    				
}
        