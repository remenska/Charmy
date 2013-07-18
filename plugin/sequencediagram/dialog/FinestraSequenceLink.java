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


package plugin.sequencediagram.dialog;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import plugin.sequencediagram.controllo.ControlloComboBoxSeqLink;
import plugin.sequencediagram.data.ElementoSeqLink;
import plugin.topologydiagram.TopologyWindow;
import plugin.topologydiagram.resource.graph.GraficoLink;
import plugin.topologydiagram.resource.utility.JComboBoxPattern;
import core.internal.runtime.data.PlugDataManager;
import core.resources.utility.JComboBoxColor;
import core.resources.utility.JComboBoxFont;
import core.resources.utility.JComboBoxStep;
import core.resources.utility.JComboBoxStyle;

/** La classe realizza un pannello di dialogo tramite il quale l'utente puï¿½
 * impostare il nome di un oggetto 'ElementoSeqLink' (utilizzato come link
 * nel Sequence Diagram) e del grafico associato (istanza della classe
 * 'GraficoLink'). */

public class FinestraSequenceLink extends JDialog {

	public PlugDataManager pdm;//ezio 2006
	
    /** Memorizza il riferimento all'oggetto grafico associato al canale. */
    private GraficoLink grafico;
    
    /** Campo per impostare il nome del canale. */
    private JTextField controllotesto;
    private JComboBox controllotestobis;
    
    /** Pannello contenente il campo per impostare il nome del canale. */
    private JPanel stringapannello;
    
    /** Pannello usato per selezionare se un evento e' regular, required o fail */
    private JPanel events;
    
    /** Permette di selezionare il tipo di event: regular, required o fail */
    private JTextField eventType;
    private JComboBox eventTypebis;
    
    /** Pannello che contiene le 'CheckBox' per selezionare se il link strict. */
    private JPanel strictPanel;
    
    /** Permette di selezionare se il link e' strict. */
    private JCheckBox strict;
    
    ///////////////////////////////NEW Flamel///////////////////////////////////
    
    /** Pannello che contiene le 'CheckBox' per selezionare il complemento del messaggio. */
    private JPanel notPanel;
    
    /** Permette di selezionare se il messaggio è complementato. */
    private JCheckBox not;   
        
    /////////////////////////////////////////////////////////////////////////////
    
    /** Permette di selezionare il tipo di collegamento (dal punto di vista grafico). */
    private JComboBoxStep controllolinea;
    
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
    
    /** Pannello contente informazioni generali sul link*/
    private JPanel linkGeneralProperties;
    
    /** Pannello per tutte le proprietï¿½ del canale. */
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
    private ElementoSeqLink canale;
    
    /** Memorizza il nome del processo da cui parte il flusso di dati. */
    private String processo_da;
    
    /** Memorizza il nome del processo a cui arriva il flusso di dati. */
    private String processo_a;
    
    
    /** Costruttore_
     * cnl         : riferimento al canale di cui si vogliono modificare le caratteristiche_
     * lframe      : riferimento al frame 'proprietario' della finestra di dialogo_
     * titolo      : titolo della finestra di dialogo. */
    public FinestraSequenceLink(ElementoSeqLink cnl, Frame lframe, String titolo, PlugDataManager pd) {
        
        // Finestra di dialogo di tipo modale.
        super(lframe,titolo,true);
        canale = cnl;
        this.pdm=pd;//ezio 2006
        grafico = canale.getGrafico();
        if (canale.getFlussoDiretto()){
            processo_da = canale.getElement_one().getName();
            processo_a = canale.getElement_two().getName();
        } else{
            processo_da = canale.getElement_two().getName();
            processo_a = canale.getElement_one().getName();
        }
        
        stringapannello = new JPanel();
        stringapannello.setLayout(new BorderLayout());
        stringapannello.add(new JLabel("Name:    "));
        if(processo_da.equals(processo_a)){
            controllotestobis = new JComboBox((new ControlloComboBoxSeqLink(pd)).getComboBoxListTau(processo_a));
            
        } else{
            controllotestobis = new JComboBox((new ControlloComboBoxSeqLink(pd)).getComboBoxList(processo_da,processo_a));
        }
        controllotestobis.setSelectedItem(canale.getName());
        controllotesto = new JTextField(grafico.getText(),20);
        stringapannello.add(controllotestobis);
        stringapannello.setBorder(BorderFactory.createTitledBorder("Name"));
        
        events = new JPanel();
        events.setLayout(new BorderLayout());
        events.add(new JLabel("Event types:    "));
        {Vector eventsVector=new Vector();
         eventsVector.add("Regular");
         eventsVector.add("Required");
         if(!(canale.isConstraintFutClo()
           || canale.isConstraintChCloFut()
           || canale.isConstraintChOpFut()
           || canale.isConstraintFutOp()
           || canale.isConstraintUnCloFut()
           || canale.isConstraintUnOpFut()))
            eventsVector.add("Fail");
    
         eventTypebis = new JComboBox(eventsVector);}
        int RRF=canale.getRegReqFail();
        int RRF_pre;
        
        switch(RRF){
            case 3:
                eventTypebis.setSelectedItem("Regular");
                break;
            case 4:
                eventTypebis.setSelectedItem("Required");
                break;
            case 5:
                eventTypebis.setSelectedItem("Fail");
                break;
        }
        
        eventType = new JTextField(grafico.getText(),20);
        events.add(eventTypebis);
        events.setBorder(BorderFactory.createTitledBorder("EventsType"));
        
        strictPanel=new JPanel();
        strictPanel.setLayout(new GridLayout());
        strictPanel.add(new JLabel("Is strict: "));
        strict = new JCheckBox("",canale.isStrict());
        strictPanel.add(strict);
        strictPanel.setBorder(BorderFactory.createTitledBorder("Strict or Loose"));
        if (canale.isParallel()
            ||canale.isLoop_op()
            ||canale.isSimultaneous())
            strict.setEnabled(false);
        else
        {
            if(canale.hasConstraint())
                if(canale.isConstraintPre())
                    strict.setEnabled(true);
                else
                     strict.setEnabled(false);
        }
           
        
    
        notPanel=new JPanel();
        notPanel.setLayout(new GridLayout());
        notPanel.add(new JLabel("Complement: "));
        not = new JCheckBox("",canale.isComplement());
        notPanel.add(not);
        notPanel.setBorder(BorderFactory.createTitledBorder("Complement"));                 
        

        
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
        
        linkGeneralProperties = new JPanel();
        linkGeneralProperties.setLayout(new GridLayout(4,2));
        linkGeneralProperties.add(stringapannello);
        linkGeneralProperties.add(events);
        linkGeneralProperties.add(strictPanel);
        linkGeneralProperties.add(notPanel);
      
        
        nordpannello = new JTabbedPane();
        nordpannello.addTab(" General ",linkGeneralProperties);
        nordpannello.addTab(" Graphic ",graficoproprietapannello);
        
        okpulsante = new JButton("OK");
        okpulsante.addActionListener(new pulsantiListener());
        annullapulsante = new JButton("Cancel");
        annullapulsante.addActionListener(new pulsantiListener());
        sudpannello = new JPanel();
        sudpannello.setLayout(new FlowLayout());
        sudpannello.add(annullapulsante);
        sudpannello.add(okpulsante);
        getRootPane().setDefaultButton(okpulsante);
        
        principalepannello = new JSplitPane();
        principalepannello.setOrientation(JSplitPane.VERTICAL_SPLIT);
        principalepannello.setTopComponent(nordpannello);
        principalepannello.setBottomComponent(sudpannello);
        getContentPane().add(principalepannello);
        pack();
        
        Toolkit SystemKit = Toolkit.getDefaultToolkit();
        Dimension ScreenSize = SystemKit.getScreenSize();
        Dimension DialogSize = getSize();
        setLocation((ScreenSize.width-DialogSize.width)/2,(ScreenSize.height-DialogSize.height)/2);
        
        setVisible(true);
    }   
    
    /** Implementa le azioni relative alla pressione dei pulsanti OK
     * oppure CANCEL, con la chiusura della finestra di dialogo. */
    private final class pulsantiListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Object target = e.getSource();
                if (target.equals(annullapulsante)){
                    canale.setSelected(false);
                    dispose();
                } else{
                    boolean bo = canale.testAndSet();
                    String msgRRF = (String)(eventTypebis.getSelectedItem());
                    if(msgRRF=="Regular"){
                        msgRRF = "e: ";
                        canale.setRegReqFail(3);
                    } else
                        if (msgRRF=="Required"){
                            msgRRF = "r: ";
                            canale.setRegReqFail(4);
                            
                        } else 
                            if(msgRRF=="Fail"){
                                msgRRF = "f: ";
                                canale.setRegReqFail(5); 
                            } 
                            else {
                                msgRRF = "e: ";
                                canale.setRegReqFail(3);
                            }
                    
                    
                    
                    if(strict.isSelected()){
                        canale.setStrict(true);
                    } else
                        canale.setStrict(false);
                    
                    canale.setName(((String)(controllotestobis.getSelectedItem())));
                    ///////////////////////////////////////NEW Flamel///////////////////////////
                    
                    if(not.isSelected())
                        canale.setComplement(true);
                    else
                        canale.setComplement(false);                    
                    
                    
                    /////////////////////////////////////////////////////////////////
                    
                    grafico.updateTestoProprieta(controllotestocolore.getSelectedColor(),
                            controllotestofont.getSelectedFont(),controllofontstile.getSelectedStile(),
                            controllofontdimensione.getSelectedStep());
                    grafico.updateLineaProprieta(controllolineacolore.getSelectedColor(),
                            controllolineaspessore.getSelectedStep(),controllotratteggio.getSelectedPattern());
                    
                    canale.setSelected(false);
                    canale.testAndReset(bo);
                    // ezio 2006 core.internal.plugin.file.FileManager.setModificata(true);
                    pdm.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);// ezio 2006
                    dispose();
                }
            } catch (Exception ex){
                System.out.println("Errore: " + ex);
            }
        }
    }
    
}