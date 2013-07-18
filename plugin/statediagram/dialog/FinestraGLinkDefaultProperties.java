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
import plugin.topologydiagram.resource.graph.GraficoLink;
import plugin.topologydiagram.resource.utility.JComboBoxPattern;

import core.internal.plugin.file.FileManager;
import core.resources.utility.JComboBoxColor;
import core.resources.utility.JComboBoxFont;
import core.resources.utility.JComboBoxStep;
import core.resources.utility.JComboBoxStyle;


/** La classe realizza un pannello di dialogo tramite il quale l'utente pu�
    impostare le caratteristiche di un oggetto 'ElementoCanaleMessaggio' e 
    del grafico associato (istanza della classe 'GraficoLink'). */

public class FinestraGLinkDefaultProperties extends JDialog
{
	public FileManager fileManager;
	
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

    /** Pulsante per applicare le nuove impostazioni al canale ed uscire. */
    private JButton okpulsante;
    
    /** Pulsante per uscire senza modificare le impostazioni del canale. */    
    private JButton annullapulsante;

    /** Pannello contenente i pulsanti 'okpulsante' e 'annullapulsante'. */
    private JPanel sudpannello;

    /** Pannello principale. */    
    private JSplitPane principalepannello;
    
    
    /** Costruttore_
        lframe      : riferimento al frame 'proprietario' della finestra di dialogo_
        titolo      : titolo della finestra di dialogo. */
    public FinestraGLinkDefaultProperties(Frame lframe, String titolo,FileManager fileManager)
    {
        
        // Finestra di dialogo di tipo modale.
        super(lframe,titolo,true);
        this.fileManager=fileManager; 
        lineapannello = new JPanel();
        lineapannello.setLayout(new GridLayout(2,2));
        lineapannello.add(new JLabel("Color: "));
        controllolineacolore = new JComboBoxColor(GraficoLink.getDEFAULT_LINK_COLOR());
        lineapannello.add(controllolineacolore);
        lineapannello.add(new JLabel("Weight: "));
        controllolineaspessore = new JComboBoxStep(GraficoLink.mincollegamentospessore,GraficoLink.maxcollegamentospessore,
            GraficoLink.stepcollegamentospessore,GraficoLink.getDEFAULT_LINK_WEIGHT());
        lineapannello.add(controllolineaspessore);
        
        tratteggiopannello = new JPanel();
        tratteggiopannello.setLayout(new GridLayout(2,2));
        tratteggiopannello.add(new JLabel("Theme: "));
        controllotratteggio = new JComboBoxPattern(GraficoLink.getDEFAULT_LINK_PATTERN());
        tratteggiopannello.add(controllotratteggio);        
        
        linANDtrapannello = new JPanel();
        linANDtrapannello.setLayout(new BorderLayout());
        linANDtrapannello.add(lineapannello,BorderLayout.WEST);
        linANDtrapannello.add(tratteggiopannello,BorderLayout.EAST);        
        linANDtrapannello.setBorder(BorderFactory.createTitledBorder("Line"));
        
        testopannello = new JPanel();
        testopannello.setLayout(new GridLayout(4,1));
        testopannello.add(new JLabel("Font: "));
        controllotestofont = new JComboBoxFont(GraficoLink.getDEFAULT_TEXT_FONT());
        testopannello.add(controllotestofont);
        JLabel FontColor = new JLabel("Color: ");
        testopannello.add(FontColor);
        controllotestocolore = new JComboBoxColor(GraficoLink.getDEFAULT_TEXT_COLOR());
        testopannello.add(controllotestocolore);
        testopannello.setBorder(BorderFactory.createTitledBorder("Text"));

        fontpannello = new JPanel();
        fontpannello.setLayout(new GridLayout(4,1));
        fontpannello.add(new JLabel("Size: "));
        controllofontdimensione = new JComboBoxStep(GraficoLink.minlinkfontdimensione,GraficoLink.maxlinkfontdimensione,
            GraficoLink.steplinkfontdimensione,GraficoLink.getDEFAULT_FONT_SIZE());
        fontpannello.add(controllofontdimensione);
        fontpannello.add(new JLabel("Style: "));
        controllofontstile = new JComboBoxStyle(GraficoLink.getDEFAULT_FONT_STYLE());
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
        principalepannello.setTopComponent(graficoproprietapannello);
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
            try{
       	        Object target = e.getSource();

                if (target.equals(annullapulsante)){
                    dispose();
	        	}
				else{
                    GraficoLink.setDEFAULT_PROPERTIES(controllolineacolore.getSelectedColor(),
                    	controllolineaspessore.getSelectedStep(),controllotratteggio.getSelectedPattern(),
                    	controllotestofont.getSelectedFont(),controllotestocolore.getSelectedColor(),
                    	controllofontdimensione.getSelectedStep(),controllofontstile.getSelectedStile());
//                  core.internal.plugin.file.FileManager.setModificata(true);
					
					fileManager.setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);
                    dispose();
				}
            }
            catch (Exception ex){
                System.out.println("Error: " + ex);
	    	}
        }
    }

}