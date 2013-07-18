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
    


package plugin.statediagram.dialog;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import core.internal.plugin.file.FileManager;

import plugin.statediagram.StateWindow;
import plugin.statediagram.ThreadEditor;
import plugin.statediagram.controllo.ControlloNomeThread;
import plugin.topologydiagram.TopologyWindow;

/** Questa classe costruisce una finestra di dialogo che permette all'utente
	di scegliere un nome da assegnare ad un nuovo thread_ Essa prende in ingresso
	il frame su cui collocare la finestra_ Tale classe possiede due attributi: 
	campoNome, per permettere all'utente di scegliere il nome del thread; nomeThread,
	per memorizzare il nome scelto in una variabile, in modo da recuperarlo con il
	metodo getName(). */
	
public class FinestraNuovoThread extends JDialog 
{
    
		
	public FileManager fileManager;
	
	/** Riferimento alla "State Diagram" (finestra per la creazione
		e modifica degli State Diagrams). */
	private StateWindow localStateWindow;
  
	private String oldThreadName;
	
	private String oldProcessName;


	/** Campo di testo per accogliere il nome del thread scelto dall'utente. */
	private JTextField campoNome;

	/** Variabile usata per memorizzare il nome del thread scelto dall'utente. */
	private String nomeThread; 
	
	/** Variabile usata per gestire due diversi tipi di operazione_
		ctrlOP = true: aggiunta di un nuovo thread_
		ctrlOP = false: modifica proprietà del thread corrente. */
	private boolean ctrlOP; 
	 
	/** Memorizza l'indice del processo selezionato. */
	int processIndex; 
	
	/** Lista dei processi. */
	JComboBox ComboListaProcessi;

	  
	/** Il costruttore prende in ingresso il frame su cui collocare la finestra
		ed un riferimento all'oggetto che gestisce la creazione del nuovo thread_
		Se ctrlOperation = true viene aggiunto un nuovo thread, altrimenti
		vengono modificate le proprietà del thread corrente. */
    public FinestraNuovoThread(JFrame f, StateWindow sW, boolean ctrlOperation) 
    {
    	super(f, "Thread Properties", true);
    	this.fileManager=sW.getPlugDataManager().getFileManager();
		nomeThread = "";
		ctrlOP = ctrlOperation;
		localStateWindow = sW;
		getContentPane().setLayout(new BorderLayout());
		JPanel pannello = new JPanel();
		pannello.setLayout(new GridLayout(1, 2, 0, 0));	
		pannello.add(new JLabel("Thread name:   ", JLabel.LEFT));	
		campoNome = new JTextField(15);
		campoNome.setColumns(9);
		if (ctrlOP){
			campoNome.setText("thread" + (ThreadEditor.getNumIstanze()+1));
		}
		else{
			oldThreadName = localStateWindow.getNameCurrentThreadEditor();
			campoNome.setText(oldThreadName);
		}
 		pannello.add(campoNome);
		processIndex = localStateWindow.getSelectedProcessIndex();
		JPanel pannellobis = new JPanel();
		pannellobis.setLayout(new GridLayout(2,1,0,0));
		ComboListaProcessi = new JComboBox(localStateWindow.getProcessStringArray());
		if (processIndex >= 0){
			ComboListaProcessi.setSelectedIndex(processIndex);
			oldProcessName = (String)(ComboListaProcessi.getItemAt(processIndex));
		}
		pannellobis.add(new JLabel("Process: "));
		pannellobis.add(ComboListaProcessi);
		JPanel pannellotris = new JPanel();
		pannellotris.setLayout(new BorderLayout());
		pannellotris.add("North",pannello);
		pannellotris.add("South",pannellobis);
		pannellotris.setBorder(BorderFactory.createTitledBorder(""));
	
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout (new FlowLayout(FlowLayout.RIGHT));
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() 
		{
	        public void actionPerformed(ActionEvent e) 
	        {
				CancelPressed();       
			}
		});
    	cancel.setMargin(new Insets(1,1,1,1));
		buttonPanel.add( cancel );
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() 
		{
	    	public void actionPerformed(ActionEvent e) 
	    	{
				OKPressed();
			}
		});
		ok.setMargin(new Insets(1,14,1,14));			   
		buttonPanel.add( ok );
		getRootPane().setDefaultButton(ok);
    
		// Aggiunta al pannello principale.
		getContentPane().add("Center", pannellotris);		
    	getContentPane().add("South", buttonPanel);
	
		pack();
		Toolkit SystemKit = Toolkit.getDefaultToolkit();
		Dimension ScreenSize = SystemKit.getScreenSize();
        Dimension DialogSize = getSize();
        setLocation((ScreenSize.width-DialogSize.width)/2,(ScreenSize.height-DialogSize.height)/2);
        setVisible(true);        
    }

 
	
	  
 	/** Metodo che permette di recuperare il nome scelto dall'utente. */
 	public String getName()
  	{
   		return nomeThread;
  	};


 	/** Metodo per gestire il pulsante "Cancel" */ 			
 	private void CancelPressed() 
 	{
    	// Imposto un nome di thread nullo.
        nomeThread = "";
        // Chiudo la finestra. 
		this.setVisible(false);
		this.dispose();
	}


	/** Metodo per gestire il pulsante "OK" */
	private void OKPressed() 
	{
		String str;
		if (campoNome.getText().equals("")){
		    String temp = "Insert the thread name!";
            JOptionPane.showMessageDialog(this,temp," Get the name",JOptionPane.INFORMATION_MESSAGE);
		    return;
		}
		else if (ctrlOP){
		    nomeThread = campoNome.getText();
			//TODO v2.0 modificato 18 settembre nome thread con spazi
			nomeThread = nomeThread.replace(' ','_');
		    String nomeProcesso = (String)(ComboListaProcessi.getSelectedItem());
			ControlloNomeThread cnT=new ControlloNomeThread(localStateWindow.getPlugDataManager());
		    if (cnT.checkOkforNew(nomeProcesso,nomeThread))
		    {
		    	if (ComboListaProcessi.getSelectedIndex() != processIndex)
		    	{
		    		localStateWindow.setSelectedProcess(ComboListaProcessi.getSelectedIndex());
		    	}
				//TODO modificato bug daniela 21 luglio 2005
				//if(localStateWindow.getCurrentThreadEditor()!=null)
					//localStateWindow.getCurrentThreadEditor().getThreadElement().setNomeThread(nomeThread);	
            	localStateWindow.addThread(nomeThread);
				// Chiudo la finestra.		
 		   		this.setVisible(false);
// 		   	core.internal.plugin.file.FileManager.setModificata(true);
				
				fileManager.setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);
               	(localStateWindow.getStateToolBar()).setNoPressed();
				(localStateWindow.getStateToolBar()).setEnableThreadButtons(true);
				(localStateWindow.getStateToolBar()).setEnableStateButtons(true);
                this.dispose();
 		   	}
 		   	else
 		   	{
				str =  	"You must change the thread name. \n\n" + 
						"Two threads of the same process cannot have the same name\n";	
                JOptionPane.showMessageDialog(null,str,"Warning",JOptionPane.INFORMATION_MESSAGE); 		   		
 		   	}			
		}
		else
		{
			// Imposto il nome scelto per il thread.
		    nomeThread = campoNome.getText();
//			TODO v2.0 modificato 18 settembre nome thread con spazi
			nomeThread = nomeThread.replace(' ','_');
			String nomeProcesso = (String)(ComboListaProcessi.getSelectedItem());
			if((new ControlloNomeThread(localStateWindow.getPlugDataManager())).checkOkforProperties(oldProcessName,nomeProcesso,oldThreadName,nomeThread))
			{		    
		    	if (ComboListaProcessi.getSelectedIndex() != processIndex)
		    	{
		    		localStateWindow.moveThread(ComboListaProcessi.getSelectedIndex(),nomeThread);
				}
				else
				{
            		localStateWindow.setNameCurrentThreadEditor(nomeThread);
        		}
				// Chiudo la finestra.		
 		   		this.setVisible(false);
// 		   	core.internal.plugin.file.FileManager.setModificata(true);
				
				fileManager.setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);
               (localStateWindow.getStateToolBar()).setNoPressed();
				(localStateWindow.getStateToolBar()).setEnableThreadButtons(true);
				(localStateWindow.getStateToolBar()).setEnableStateButtons(true);
				localStateWindow.getCurrentThreadEditor().getThreadElement().setNomeThread(nomeThread);

                 this.dispose();
 		   	}
 		   	else
 		   	{
            	str =  	"You must change the thread name. \n\n" + 
            			"Two threads of the same process cannot have the same name\n";	
                JOptionPane.showMessageDialog(null,str,"Warning",JOptionPane.INFORMATION_MESSAGE); 		   	 		   		
 		   	}			
		} 
	}
  
}