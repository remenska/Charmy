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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import plugin.sequencediagram.data.ElementoConstraint;
import plugin.sequencediagram.data.SequenceElement;
import plugin.sequencediagram.graph.GraficoLoop;
import plugin.topologydiagram.TopologyWindow;
import core.internal.plugin.file.FileManager;

/**
 *
 * @author  FLAMEL 2005
 */


/** La classe realizza un pannello di dialogo tramite il quale l'utente
	pu� impostare il sender ed il receiver  del nuovo Constraint. */


public class FinestraSequenceConstClass extends JDialog  {
	
	private FileManager fileManager;//ezio 2006
	
    /** Memorizza il riferimento all'oggetto grafico associato al canale. */
    private GraficoLoop grafico;
    
    /** Pannello contenente il campo per impostare il nome del canale. */
    private JPanel nordpannello;
    
     /** Pannello contenente il campo per impostare il nome del canale. */
    private JPanel estpannello;
    
     /** Pannello contenente il campo per impostare il nome del canale. */
    private JPanel westpannello;
    
     /** Pannello contenente il campo per impostare il nome del canale. */
    private JPanel pannelloclassi;
    
    /** Pannello contenente il campo per impostare il nome del canale. */
    private JPanel sudpannello;

    /** Lista di tutti i canali utilizzabili in un constraint. */
    private LinkedList listaClassi;
    
    /** Pulsante per applicare le nuove impostazioni all'opertore. */
    private JButton okpulsante;
    
    /** Pulsante per uscire senza modificare le impostazioni all'opertore. */
    private JButton annullapulsante;
    
    /** Pannello principale. */
    private JSplitPane principalepannello;
    
    /** Riferimento all'oggetto di cui si vogliono modificare le caratteristiche. */
    private ElementoConstraint Con;
    
    private SequenceElement seq;
    
    /** Modello per la lista dei canali disponibili all'uso. */
    private Vector listModelSender;

    /** Lista degli stati disponibili all'uso. */
    private JComboBox Sender;
    
    /** Lista degli stati disponibili all'uso. */
    private JComboBox Receiver;
    
    /** Modello per la lista degli stati . */
    private Vector listModelReceiver;
    
    private JTextArea areatesto;
    
    private String nomelink;
    
    /**riferimento al pulsante agg. della finestra constraint**/
    private JButton plsAggiungi;
    
    /** Gestione dei pulsanti per aggiungere o rimuovere 
            uno o pi� processi dal corrente Sequence Diagram. */
    private GestioneComandi plsGestore;
    

    
    
    /** Costruttore_
     * cnl         : riferimento al constr di cui si vogliono modificare le caratteristiche_
     * lframe      : riferimento al frame 'proprietario' della finestra di dialogo_
     * titolo      : titolo della finestra di dialogo.
     * elemSel     : il nome del link selezionato 
     * txt         : riferimento all'area di testo su cui verra scritta la formula finale
     * agg         : riferimento al pulsante aggiungi della finestra constraints*/
    public FinestraSequenceConstClass(ElementoConstraint con,SequenceElement seqEl, String titolo, Frame lframe,String elemSel,JTextArea text,JButton agg,
    		FileManager fileManager) {//ezio 2006 - serve file manager
        
        // Finestra di dialogo di tipo modale.
        super(lframe,titolo,true);
        this.fileManager=fileManager;//ezio 2006
        Con = con;
        seq = seqEl;
        areatesto = text;
        nomelink = elemSel;
        plsAggiungi= agg;
        plsGestore = new GestioneComandi();
        listModelSender = new Vector();
        listModelReceiver = new Vector();
        for(int i=0;i<seqEl.getListaClasse().size();i++)
        {
            listModelSender.add(seqEl.getListaClasse().getElement(i).getName());
            listModelReceiver.add(seqEl.getListaClasse().getElement(i).getName());
        }
        
        Receiver = new JComboBox(listModelReceiver);        
        estpannello = new JPanel();
        estpannello.add(Receiver);
        estpannello.setBorder(BorderFactory.createTitledBorder("Receiver:"));  
        estpannello.setPreferredSize(new Dimension(75,100));
        
        Sender = new JComboBox(listModelSender);
        westpannello = new JPanel();
        westpannello.add(Sender);
        westpannello.setBorder(BorderFactory.createTitledBorder("Sender:"));
        westpannello.setPreferredSize(new Dimension(75,100));
        
        Sender.setSelectedIndex(0);
        Receiver.setSelectedIndex(1);
     
        pannelloclassi = new JPanel();
        pannelloclassi.setLayout(new BorderLayout());
        pannelloclassi.add(westpannello,BorderLayout.WEST);
        pannelloclassi.add(estpannello,BorderLayout.EAST);
        pannelloclassi.setBorder(BorderFactory.createTitledBorder(""));
        
        
        okpulsante = new JButton("OK");
        okpulsante.addActionListener(new pulsantiListener());
        annullapulsante = new JButton("Cancel");
        annullapulsante.addActionListener(new pulsantiListener());
    
        sudpannello = new JPanel();
        sudpannello.setLayout(new FlowLayout());
        sudpannello.add(annullapulsante);
        sudpannello.add(okpulsante);
        getRootPane().setDefaultButton(okpulsante);
        
        Sender.addActionListener(plsGestore);
        Receiver.addActionListener(plsGestore);        
        
        principalepannello = new JSplitPane();
        principalepannello.setOrientation(JSplitPane.VERTICAL_SPLIT);
        principalepannello.setTopComponent(pannelloclassi);
        principalepannello.setBottomComponent(sudpannello);
        principalepannello.setPreferredSize(new Dimension(250,130));
        getContentPane().add(principalepannello);
        pack();
        
        
        Toolkit SystemKit = Toolkit.getDefaultToolkit();
        Dimension ScreenSize = SystemKit.getScreenSize();
        Dimension DialogSize = getSize();
        setLocation((ScreenSize.width-DialogSize.width)/2,(ScreenSize.height-DialogSize.height)/2);
        
        setVisible(true);
    }
    
    /** Classe interna per la gestione della selezione degli stati. */
	private final class GestioneComandi extends AbstractAction{

		GestioneComandi(){ 
			super();
		}
	
		public void actionPerformed(ActionEvent e){
                    Object target = e.getSource();    
 
                        Object index = Receiver.getSelectedItem();
                        Object index1 = Sender.getSelectedItem();
                        if(index.equals(index1))
                            okpulsante.setEnabled(false);
                        else
                            okpulsante.setEnabled(true);

                   repaint();   
                }
        }
        
        
    /** Implementa le azioni relative alla pressione dei pulsanti OK
     * oppure CANCEL, con la chiusura della finestra di dialogo. */
    private final class pulsantiListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                Object target = e.getSource();
                if (target.equals(annullapulsante)){

                    dispose();
                } 
                else{
                    
                    String sender =(String) Sender.getSelectedItem();
                    String receiver =(String) Receiver.getSelectedItem();
                    String out = sender+"."+nomelink+"."+receiver;
                    areatesto.append(out);
                    plsAggiungi.setEnabled(false);
                    // ezio 2006 core.internal.plugin.file.FileManager.setModificata(true);
                    fileManager.setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);// ezio 2006
                    dispose();
                    
                }
                
            }
            catch (Exception ex){
                System.out.println("Errore: " + ex);
            }
        }
    }
    
    

}
