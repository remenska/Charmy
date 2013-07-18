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
    

package plugin.promela.dialog;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.FocusListener;

import plugin.promela.PromelaSpecified;
import plugin.promela.RecordDueCampi;
import plugin.promela.util.ThreadLocalVariables;


/**
 *
 * @author  Patrizio
 */
public class FinestraDefineDataStructuresPromela extends JDialog
{
    /** Variabile per definire il testo da aggiungere */
    private JTextArea testoDefinizioni;
    
    private PromelaSpecified promela;
    private LinkedList code;
    private RecordDueCampi processVariables = new RecordDueCampi();
    private LinkedList processVariablesList = new LinkedList();
    
    /** Pannello contenente il campo con il nome del processo. */
    private JPanel definizioni;

    // Pulsanti di interazione con l'utente.
    /** Pulsante per applicare le nuove impostazioni al processo ed uscire. */
    private JButton okPulsante;

    /** Pulsante per uscire senza modificare le impostazioni del processo. */
   // private JButton annullaPulsante;
    // Fine pulsanti di interazione con l'utente.
   
    /** Pannello contenente i pulsanti 'okpulsante' e 'annullapulsante'. */
    private JPanel sudPannello;
    
    /** Pannello contenente il pannello definizioni */
    private JPanel definizioniPannello;
    
    private JPanel globalVarPanel;
    
    private JPanel localVarPanel;
    
    private JList threadList;
        
    private JScrollPane threadListScroll;
    
    private JTextArea threadVariables;
    
    private JScrollPane threadVariablesScroll;
    
	private String[] threadLocalVariables;
	
	private LinkedList listLocalVariables;
    
        /** Pannello principale. */
    private JSplitPane principalePannello;	
      
    private JScrollPane definizioniScroller;
    
    private int precIndex=-1;
    
    private class SelListener implements ListSelectionListener{
		public void valueChanged(ListSelectionEvent e) {
			if (!threadVariables.isFocusOwner())
				threadVariables.setText(threadLocalVariables[threadList.getSelectedIndex()]);
		}
		
    }

	private class FocListener implements FocusListener{

		public void focusGained(FocusEvent e) {
			precIndex=threadList.getSelectedIndex();
		}

		public void focusLost(FocusEvent e) {
			threadLocalVariables[precIndex]=threadVariables.getText();
		}
	}
	
    public FinestraDefineDataStructuresPromela(LinkedList codice,LinkedList threadLocalVar,PromelaSpecified pro,Frame lframe, String titolo){
        // Finestra di dialogo di tipo modale.
        super(lframe,titolo,true);
        listLocalVariables=threadLocalVar;
        int precIndex;
		String[] threadVar=new String[threadLocalVar.size()];
		threadLocalVariables=new String[threadLocalVar.size()];
		for(int r=0;r<threadLocalVar.size();r++){			
			threadVar[r]=((ThreadLocalVariables)threadLocalVar.get(r)).getThread();
			threadLocalVariables[r]=((ThreadLocalVariables)threadLocalVar.get(r)).getLocalVariables();
		}
        promela=pro;
        code=new LinkedList();
        // Costruzione del pannello relativo alle caratteristiche generali del processo.
        definizioni = new JPanel();
        definizioni.setLayout(new FlowLayout(FlowLayout.LEFT));
        definizioni.add(new JLabel("Definitions:    "));
        String testo="";
        for (int i=0;i<codice.size();i++)
            testo=testo+codice.get(i);
        testoDefinizioni = new JTextArea(testo,7,30);
        definizioniScroller = new JScrollPane(testoDefinizioni);
               
        definizioniPannello = new JPanel();
        
        //globalVar
		globalVarPanel = new JPanel();
		globalVarPanel.setLayout(new BorderLayout());
		globalVarPanel.add(definizioni,BorderLayout.NORTH);
		globalVarPanel.add(definizioniScroller);
		globalVarPanel.setBorder(BorderFactory.createTitledBorder("Global variables definition"));                
        
        //localVar
		localVarPanel = new JPanel();        
        localVarPanel.setLayout(new BorderLayout());
        threadList= new JList(threadVar);
        threadList.addListSelectionListener(new SelListener());
        threadListScroll = new JScrollPane(threadList);
		threadListScroll.setPreferredSize(new Dimension(80,20));
		threadListScroll.setMaximumSize(new Dimension(80,20));
		localVarPanel.add(threadListScroll,BorderLayout.WEST);
		        
		threadVariables = new JTextArea();
		threadVariables.addFocusListener(new FocListener());
		threadVariablesScroll =new JScrollPane(threadVariables);
		threadVariablesScroll.setPreferredSize(new Dimension(250,80));
		localVarPanel.add(threadVariablesScroll,BorderLayout.EAST);
        
       	localVarPanel.setBorder(BorderFactory.createTitledBorder("Local variables definition"));			
		

		definizioniPannello.setLayout(new BorderLayout());
		definizioniPannello.add(globalVarPanel,BorderLayout.NORTH);
		definizioniPannello.add(localVarPanel);
		definizioniPannello.setBorder(BorderFactory.createTitledBorder("Data structures definition"));


        okPulsante = new JButton("OK");
		okPulsante.addActionListener(new pulsantiListener());
        sudPannello = new JPanel();
        sudPannello.setLayout(new FlowLayout());
        sudPannello.add(okPulsante);
        getRootPane().setDefaultButton(okPulsante);
        principalePannello = new JSplitPane();
        principalePannello.setOrientation(JSplitPane.VERTICAL_SPLIT);
        principalePannello.setTopComponent(definizioniPannello);
        principalePannello.setBottomComponent(sudPannello);
        getContentPane().add (principalePannello);
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
        public void actionPerformed(ActionEvent e)
        {
            try{
       	        Object target = e.getSource();
				for(int r=0;r<listLocalVariables.size();r++){			
					((ThreadLocalVariables)listLocalVariables.get(r)).setLocalVariables(threadLocalVariables[r]);
				}
                code.add(testoDefinizioni.getText());
                promela.setEvents(code);
                dispose();                        
            }
            catch (Exception ex){
                System.out.println("FinestraDefineDataStructuresPromela.actionPerformed: "+ex);
	    	}
        }
    }

}
