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
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import plugin.topologydiagram.TopologyWindow;

import core.internal.plugin.file.FileManager;
import core.resources.ui.WithGraphEditor;
import core.resources.utility.JComboBoxColor;
import core.resources.utility.JComboBoxStep;


/** La classe realizza un pannello di dialogo tramite il quale l'utente
	pu� impostare le caratteristiche di un oggetto 'WithGraphEditor'.
	Le caratteristiche che si possono modificare sono: larghezza, altezza
	e colore di background. */

public class FinestraGraphEditor extends JDialog
{
    
	public FileManager fileManager;
	
    /** Permette di selezionare la larghezza dell'editor. */
    private JComboBoxStep controllolarghezza;

    /** Permette di selezionare l'altezza dell'editor. */
    private JComboBoxStep controlloaltezza;

    /** Pannello contenente i campi con le dimensioni dell'editor. */
    private JPanel dimensionirettangolopannello;
    
    /** Permette di selezionare il colore di sfondo dell'editor. */
    private JComboBoxColor controllosfondocolore;

    /** Pannello contenente la 'ComboBox' per selezionare il colore dello sfondo. */
    private JPanel sfondopannello;

    /** Pannello per tutte le propriet� dell'editor. */
    private JPanel nordpannello;

    // Pulsanti di interazione con l'utente.
    /** Pulsante per applicare le nuove impostazioni all'editor ed uscire. */
    private JButton okpulsante;

    /** Pulsante per uscire senza modificare le impostazione dell'editor. */
    private JButton annullapulsante;
    // Fine pulsanti di interazione con l'utente.

    /** Pannello contenente i pulsanti 'okpulsante' e 'annullapulsante'. */
    private JPanel sudpannello;

    /** Pannello principale. */
    private JSplitPane principalepannello;

    private WithGraphEditor cEditor;
    
    
    /**
    Costruttore_
        ce        : riferimento all'oggetto di cui si vogliono modificare le caratteristiche_
        lframe    : riferimento al frame "proprietario" della finestra di dialogo_
        titolo    : titolo della finestra di dialogo.
    */
    public FinestraGraphEditor(WithGraphEditor ce, Frame lframe, String titolo,FileManager fileManager)
    {
        // Finestra di dialogo di tipo modale.
        super(lframe,titolo,true);

        cEditor = ce;
        this.fileManager= fileManager;
        // Costruzione del pannello relativo alle caratteristiche del rettangolo.
        dimensionirettangolopannello = new JPanel();
        dimensionirettangolopannello.setLayout(new GridLayout(2,2));
        dimensionirettangolopannello.add(new JLabel("Width: "));
        controllolarghezza = new JComboBoxStep(100,WithGraphEditor.maxWidth,100,cEditor.getCEWidth());
        dimensionirettangolopannello.add(controllolarghezza);
        dimensionirettangolopannello.add(new JLabel("Height: "));
        controlloaltezza = new JComboBoxStep(100,WithGraphEditor.maxHeight,100,cEditor.getCEHeight());
        dimensionirettangolopannello.add(controlloaltezza);
        dimensionirettangolopannello.setBorder(BorderFactory.createTitledBorder("Size"));

        sfondopannello = new JPanel();
        sfondopannello.setLayout(new GridLayout(1,2));
        sfondopannello.add(new JLabel("Color: "));
        controllosfondocolore = new JComboBoxColor(cEditor.getCEColor());
        sfondopannello.add(controllosfondocolore);
        sfondopannello.setBorder(BorderFactory.createTitledBorder("BackGround"));


        // Costruzione dei pannelli principali.
        nordpannello = new JPanel();
        nordpannello.setLayout(new BorderLayout());
        nordpannello.add(dimensionirettangolopannello,BorderLayout.NORTH);
        nordpannello.add(sfondopannello,BorderLayout.SOUTH);

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
        public void actionPerformed(ActionEvent e)
        {
            try
            {
       	        Object target = e.getSource();

                if (target.equals(annullapulsante))
          		{
                    dispose();
	        	}
				else
				{
                    cEditor.setCEProperties(controllolarghezza.getSelectedStep(),controlloaltezza.getSelectedStep(),
                        controllosfondocolore.getSelectedColor());
//                  core.internal.plugin.file.FileManager.setModificata(true);
					
					fileManager.setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);
                   
                    dispose();
				}
            }
            catch (Exception ex)
            {
	    	}
        }
    }

}