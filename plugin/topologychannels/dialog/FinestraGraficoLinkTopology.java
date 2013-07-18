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
    

package plugin.topologychannels.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import plugin.statediagram.controllo.ControlloNomeMessaggio;
import plugin.statediagram.utility.JComboBoxMessage;
import plugin.topologychannels.TopologyWindow;
import plugin.topologychannels.data.ElementoCanale;
import plugin.topologychannels.data.ElementoProcesso;
import plugin.topologychannels.resource.graph.GraficoCollegamento;
import plugin.topologychannels.resource.graph.GraficoLink;
import plugin.topologychannels.resource.graph.GraficoLoop;
import plugin.topologychannels.resource.utility.JComboBoxPattern;
import core.internal.runtime.data.PlugDataManager;
import core.internal.ui.resources.TypeToolBar;
import core.resources.utility.JComboBoxColor;
import core.resources.utility.JComboBoxFont;
import core.resources.utility.JComboBoxStep;
import core.resources.utility.JComboBoxStyle;

/** La classe realizza un pannello di dialogo tramite il quale l'utente pu�
    impostare le caratteristiche di un oggetto 'ElementoCanaleMessaggio' e 
    del grafico associato (istanza della classe 'GraficoLink')_ 
    Classe utilizzata nel Topology Diagram e negli State Diagram. */

public class FinestraGraficoLinkTopology extends JDialog
{

    /** Memorizza l'inversione del flusso */
    boolean ctrlInversione = false;
    
    /** Memorizza il riferimento all'oggetto grafico associato al canale. */
    private GraficoLink grafico;

    /** Campo per impostare il nome del canale. */
    private JTextArea controllotesto;
    
    /** Pannello contenente il campo per impostare il nome del canale. */
    private JPanel stringapannello;

    /** Permette di selezionare il tipo di messaggio. */
    private JComboBoxMessage controllomessaggio;
    
    /** Permette di selezionare il tipo di collegamento (dal punto di vista grafico). */
    private JComboBoxStep controllolinea;
    
    /** Pannello che contiene le 'ComboBox' per la selezione del tipo di messaggio
        e del tipo di collegamento. */
    private JPanel messagepannello;
    
    /** Pannello contenente i pannelli 'stringapannello' (a Nord)
        e 'messagepannello' (a Sud). */
    private JPanel strANDmespannello;
    
    /** Permette la visualizzazione del nome del processo da cui partono i dati. */
    private JTextField controlloprocessofrom;
    
    /** Permette la visualizzazione del nome del processo a cui arrivano i dati. */
    private JTextField controlloprocessoto;
    
    /** Pulsante usato per invertire il flusso di dati nel canale. */
    private JButton flussopulsante;
    
    /** Pannello contenente i campi per le informazioni sui processi che utilizzano
        il canale e sul verso del flusso dei dati. */
    private JPanel processipannello;
    
    /** Pannello contenente tutti i campi relativi alle propriet� generali del canale. */
    private JPanel canaleproprietapannello;
    
    /** Permette di selezionare il colore del collegamento. */
    private JComboBoxColor controllolineacolore;
    
    /** Permette di selezionare lo spessore della linea. */
    private JComboBoxStep controllolineaspessore;
    
    /** Pannello contenente i campi per il colore e lo spessore del collegamento. */
    private JPanel lineapannello;
    
    /** Permette di selezionare il tipo di tratteggio del collegamento. */
    private JComboBoxPattern controllotratteggio;
    
    /** Pannello contenente la 'ComboBox' per selezionare il tipo di tratteggio. */
    private JPanel tratteggiopannello;
    
    /** Pannello contenente i pannelli 'lineapannello' e 'tratteggiopannello'. */
    private JPanel linANDtrapannello;

    /** Permette di selezionare il tipo di font. */
    private JComboBoxFont controllotestofont;
    
    /** Permette di selezionare il colore del testo. */    
    private JComboBoxColor controllotestocolore;    

    /** Contiene i campi per il font ed il colore del testo. */    
    private JPanel testopannello;
    
    /** Permette di selezionare la dimensione del font. */    
    private JComboBoxStep controllofontdimensione;
    
    /** Permette di selezionare lo stile del font. */    
    private JComboBoxStyle controllofontstile;
    
    /** Contiene i campi per la dimensione e lo stile del font. */    
    private JPanel fontpannello;
    
    /** Pannello contenente i pannelli 'testopannello' e 'fontpannello'. */
    private JPanel tesANDfonpannello;

    /** Pannello contenente i pannelli 'linANDtrapannello' e 'tesANDfonpannello'. */    
    private JPanel graficoproprietapannello;

    /** Pannello per tutte le propriet� del canale. */
    private JTabbedPane nordpannello;

    /** Pulsante per applicare le nuove impostazioni al canale ed uscire. */
    private JButton okpulsante;
    
    /** Pulsante per uscire senza modificare le impostazioni del canale. */    
    private JButton annullapulsante;

    /** Pannello contenente i pulsanti 'okpulsante' e 'annullapulsante'. */
    private JPanel sudpannello;

    /** Pannello principale. */    
    private JSplitPane principalepannello;

    /** Riferimento all'oggetto di cui si vogliono modificare le caratteristiche. */
    private ElementoCanale canale;

    /** Memorizza il nome del processo da cui parte il flusso di dati. */    
    private String processo_da;

    /** Memorizza il nome del processo a cui arriva il flusso di dati. */    
    private String processo_a;

    /** Variabile usata per modificare l'aspetto della finestra di dialogo
    	a seconda del tipo di link da impostare:
    	se (tipoLink = 0) ==> canale del Topology Diagram;
    	se (tipoLink = 1) ==> messaggio (no loop) dello State Diagram;
    	se (tipoLink = 2) ==> loop dello State Diagram. */
    private int tipoLink;
    
    /** JCheckBox per impostare la posizione di un loop. */
    private JCheckBox loopPosition;

	private PlugDataManager pm;
	
    /** Costruttore_
        cnl         : riferimento al canale di cui si vogliono modificare le caratteristiche_
        lframe      : riferimento al frame 'proprietario' della finestra di dialogo_
        titolo      : titolo della finestra di dialogo. */
    public FinestraGraficoLinkTopology(ElementoCanale cnl, Frame lframe, String titolo, int type,PlugDataManager pm)
    {
        
        // Finestra di dialogo di tipo modale.
        super(lframe,titolo,true);
        this.pm=pm;
       
        tipoLink = type;
        canale = cnl;
        grafico = canale.getGrafico();
        if (canale.getFlussoDiretto()){
            processo_da = canale.getElement_one().getName();
            processo_a = canale.getElement_two().getName();
            
			stringapannello = new JPanel();
			stringapannello.setLayout(new FlowLayout(FlowLayout.LEFT));
			stringapannello.add(new JLabel("Name:    "));
			//controllotesto = new JTextField(grafico.getText(),20);
			controllotesto = new JTextArea(((ElementoProcesso)canale.getElement_one()).getConnectedProcess(
					canale.getElement_two().getName()),5,20);
			controllotesto.setAutoscrolls(true);
			controllotesto.setEditable(false);
			stringapannello.add(controllotesto);
        }
        else{
            processo_da = canale.getElement_two().getName();
            processo_a = canale.getElement_one().getName();
            
			stringapannello = new JPanel();
			stringapannello.setLayout(new FlowLayout(FlowLayout.LEFT));
			stringapannello.add(new JLabel("Name:    "));
			//controllotesto = new JTextField(grafico.getText(),20);
			controllotesto = new JTextArea(((ElementoProcesso)canale.getElement_two()).getConnectedProcess(
					canale.getElement_one().getName()),5,20);
			controllotesto.setAutoscrolls(true);
			controllotesto.setEditable(false);
			stringapannello.add(controllotesto);
        }        
        //if (tipoLink==0){	//caso di Topology diagram

		//}
        messagepannello = new JPanel();
        messagepannello.setLayout(new GridLayout(2,3,10,0));
        
        switch (tipoLink){
        	case 0:
        		messagepannello.add(new JLabel(""));
        		messagepannello.add(new JLabel(""));
        		messagepannello.add(new JLabel("Line Type: ")); 
        		controllomessaggio = new JComboBoxMessage(0);
        		controllomessaggio.setEnabled(false);
        		controllomessaggio.setVisible(false);
        		messagepannello.add(controllomessaggio);
        		messagepannello.add(new JLabel(""));
        		break;
        	case 1:
        		messagepannello.add(new JLabel("Message: "));
        		messagepannello.add(new JLabel(""));
        		messagepannello.add(new JLabel("Line Type: "));
        		messagepannello.add(controllomessaggio);
        		messagepannello.add(new JLabel(""));
        		break;
        	case 2:
        		messagepannello.add(new JLabel("Message: "));
        		messagepannello.add(new JLabel("Position Check: "));
        		messagepannello.add(new JLabel("Line Type: "));
        		messagepannello.add(controllomessaggio);
        		loopPosition = new JCheckBox();
        		loopPosition.setSelected(((GraficoLoop)grafico).getRotazione());
        		messagepannello.add(loopPosition);        		
        		break;
        	default:
        		break;
        }
        
        controllolinea = new JComboBoxStep(-5,5,1,0);
        controllolinea.setEnabled(false);
        messagepannello.add(controllolinea);
        messagepannello.setBorder(BorderFactory.createTitledBorder("Message"));

        strANDmespannello = new JPanel();
        strANDmespannello.setLayout(new BorderLayout());
        strANDmespannello.add(stringapannello,BorderLayout.NORTH);
        strANDmespannello.add(messagepannello,BorderLayout.SOUTH);
        
        processipannello = new JPanel();
        processipannello.setLayout(new GridLayout(2,3,10,0));
        
        String str;
        switch (tipoLink){
        	case 0:
        		str = new String("Process");
        		break;
        	case 1:
        		str = new String("State");
        		break;
        	case 2:
        		str = new String("State");
        		break;
        	default:
        		str = new String("");
        		break;
        }
        
        processipannello.add(new JLabel(str+" From:"));
        //flussopulsante = new JButton("Exchange Flux");       
        //processipannello.add(flussopulsante);       
        processipannello.add(new JLabel(str+" To:"));        	
        
        //flussopulsante.addActionListener(new pulsantiListener());    	
        controlloprocessofrom = new JTextField(processo_da,10);
        controlloprocessofrom.setEditable(false);
        processipannello.add(controlloprocessofrom);
        ImageIcon freccia = new ImageIcon(TypeToolBar.PathGif + "right.gif");
        processipannello.add(new JLabel(freccia));        
        controlloprocessoto = new JTextField(processo_a,10);
        processipannello.add(controlloprocessoto);
        controlloprocessoto.setEditable(false);      
        processipannello.setBorder(BorderFactory.createTitledBorder(str));
        canaleproprietapannello = new JPanel();
        canaleproprietapannello.setLayout(new BorderLayout());
        canaleproprietapannello.add(strANDmespannello,BorderLayout.NORTH);
        canaleproprietapannello.add(processipannello,BorderLayout.SOUTH);
                
        lineapannello = new JPanel();
        lineapannello.setLayout(new GridLayout(2,2));
        lineapannello.add(new JLabel("Color: "));
        controllolineacolore = new JComboBoxColor(grafico.getLineColor());
        lineapannello.add(controllolineacolore);
        lineapannello.add(new JLabel("Weight: "));
        controllolineaspessore = new JComboBoxStep(GraficoLink.mincollegamentospessore,GraficoLink.maxcollegamentospessore,
        GraficoLink.stepcollegamentospessore,grafico.getLineWeight());
        lineapannello.add(controllolineaspessore);
        
        tratteggiopannello = new JPanel();
        tratteggiopannello.setLayout(new GridLayout(2,2));
        tratteggiopannello.add(new JLabel("Theme: "));
        controllotratteggio = new JComboBoxPattern(grafico.getLineTheme());
        tratteggiopannello.add(controllotratteggio);        
        
        linANDtrapannello = new JPanel();
        linANDtrapannello.setLayout(new BorderLayout());
        linANDtrapannello.add(lineapannello,BorderLayout.WEST);
        linANDtrapannello.add(tratteggiopannello,BorderLayout.EAST);        
        linANDtrapannello.setBorder(BorderFactory.createTitledBorder("Line"));
        
        testopannello = new JPanel();
        testopannello.setLayout(new GridLayout(4,1));
        testopannello.add(new JLabel("Font: "));
        controllotestofont = new JComboBoxFont(grafico.getTextFont());
        testopannello.add(controllotestofont);
        JLabel FontColor = new JLabel("Color: ");
        testopannello.add(FontColor);
        controllotestocolore = new JComboBoxColor(grafico.getTextColor());
        testopannello.add(controllotestocolore);
        testopannello.setBorder(BorderFactory.createTitledBorder("Text"));

        fontpannello = new JPanel();
        fontpannello.setLayout(new GridLayout(4,1));
        fontpannello.add(new JLabel("Size: "));
        controllofontdimensione = new JComboBoxStep(GraficoLink.minlinkfontdimensione,GraficoLink.maxlinkfontdimensione,
        GraficoLink.steplinkfontdimensione,grafico.getFontSize());
        fontpannello.add(controllofontdimensione);
        fontpannello.add(new JLabel("Style: "));
        controllofontstile = new JComboBoxStyle(grafico.getFontStyle());
        fontpannello.add(controllofontstile);
        fontpannello.setBorder(BorderFactory.createTitledBorder("Font"));

        tesANDfonpannello = new JPanel();
        tesANDfonpannello.setLayout(new BorderLayout());
        tesANDfonpannello.add(testopannello,BorderLayout.WEST);
        tesANDfonpannello.add(fontpannello,BorderLayout.EAST);

        graficoproprietapannello = new JPanel();
        graficoproprietapannello.setLayout(new BorderLayout());
        graficoproprietapannello.add(linANDtrapannello,BorderLayout.NORTH);
        graficoproprietapannello.add(tesANDfonpannello,BorderLayout.SOUTH);

        nordpannello = new JTabbedPane();
        nordpannello.addTab(" General ",canaleproprietapannello);
        nordpannello.addTab(" Graphic ",graficoproprietapannello);

        //okpulsante = new JButton("OK");
		//okpulsante.addActionListener(new pulsantiListener());
        annullapulsante = new JButton("Ok");
        annullapulsante.addActionListener(new pulsantiListener());
        sudpannello = new JPanel();
        sudpannello.setLayout(new FlowLayout());
        sudpannello.add(annullapulsante);
        //sudpannello.add(okpulsante);
        getRootPane().setDefaultButton(okpulsante);
        
        principalepannello = new JSplitPane();
        principalepannello.setOrientation(JSplitPane.VERTICAL_SPLIT);
        principalepannello.setTopComponent(nordpannello);
        principalepannello.setBottomComponent(sudpannello);
        getContentPane().add (principalepannello);
        pack();

		Toolkit SystemKit = Toolkit.getDefaultToolkit();
		Dimension ScreenSize = SystemKit.getScreenSize();
        Dimension DialogSize = getSize();
        setLocation((ScreenSize.width-DialogSize.width)/2,(ScreenSize.height-DialogSize.height)/2);
        
        setVisible(true);
    }

    
    /** Implementa le azioni relative alla pressione dei pulsanti OK
        oppure CANCEL, con la chiusura della finestra di dialogo. */
    private final class pulsantiListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e){
            try{
       	        Object target = e.getSource();
                if (target.equals(flussopulsante)){
                    String tmp = controlloprocessofrom.getText();
                    controlloprocessofrom.setText(controlloprocessoto.getText());
                    controlloprocessoto.setText(tmp);
                    ctrlInversione = !ctrlInversione;
                }
                else 
                    if (target.equals(annullapulsante)){
                    	dispose();
	        		}		
                    else{                    
                        if (ctrlInversione){ 
                        	canale.invFlussoDiretto();
                        }
	                    if (tipoLink == 0){	// Canale in S_A_ Topology Diagram.
	                    	canale.setName(controllotesto.getText());
	                    	((GraficoCollegamento)grafico).updateFreccia(controllomessaggio.getSelectedMessage(),canale.getFlussoDiretto());	
	                    	grafico.updateTestoProprieta(/*controllotesto.getText(),*/controllotestocolore.getSelectedColor(),
	                        	controllotestofont.getSelectedFont(),controllofontstile.getSelectedStile(),
	                        	controllofontdimensione.getSelectedStep());                   
	                    	grafico.updateLineaProprieta(controllolineacolore.getSelectedColor(),
	                        	controllolineaspessore.getSelectedStep(),controllotratteggio.getSelectedPattern());
	                    	
							//core.internal.plugin.file.FileManager.setModificata(true);
						    pm.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);
	                        dispose();                    
	                    } 
	                    else 
		                    if ((new ControlloNomeMessaggio(pm)).checkOK(controllotesto.getText())){
		                        grafico.updateLineaProprieta(controllolineacolore.getSelectedColor(),
		                        controllolineaspessore.getSelectedStep(),controllotratteggio.getSelectedPattern());
//		                      core.internal.plugin.file.FileManager.setModificata(true);
							    pm.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);
		                         dispose();                		
		                    }
		                    else{
	                            String str;
	                            str =  	"Necessario modificare il nome del messaggio. \n" + 
	                                            "Il nome di un messaggio deve iniziare con '?' o con '!'.";	
	                            JOptionPane.showMessageDialog(null,str,"Warning",JOptionPane.INFORMATION_MESSAGE);
		                    }                    
						dispose();		
                    }
                
            }
            catch (Exception ex)
            {
                System.out.println("Errore: " + ex);
	    	}
        }
    }

}