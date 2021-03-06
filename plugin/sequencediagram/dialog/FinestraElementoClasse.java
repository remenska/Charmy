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
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import plugin.sequencediagram.data.ElementoClasse;
import plugin.topologydiagram.TopologyWindow;
import plugin.topologydiagram.resource.graph.ElementoBox;
import plugin.topologydiagram.resource.graph.ElementoBoxTesto;
import core.internal.plugin.file.FileManager;
import core.resources.ui.WithGraphEditor;
import core.resources.utility.JComboBoxColor;
import core.resources.utility.JComboBoxFont;
import core.resources.utility.JComboBoxStep;
import core.resources.utility.JComboBoxStyle;


/** La classe realizza un pannello di dialogo tramite il quale l'utente
	pu� impostare le caratteristiche di un oggetto 'ElementoClasse'. */

public class FinestraElementoClasse extends JDialog
{
	
	public FileManager fileManager;	//ezio 2006
    // Variabili relative al pannello con le propriet� del rettangolo.
    /** Permette di selezionare la larghezza del rettangolo. */
    private JComboBoxStep controllolarghezza;

    /** Permette di selezionare l'altezza del rettangolo. */
    private JComboBoxStep controlloaltezza;

    /** Pannello contenente i campi con le dimensioni del rettangolo. */
    private JPanel dimensionirettangolopannello;

    /** Permette di selezionare l'ascissa dell'estremo in alto a sinistra del rettangolo. */
    private JTextField controllorettangoloX;

    /** Permette di selezionare l'ordinata dell'estremo in alto a sinistra del rettangolo. */
    private JTextField controllorettangoloY;

    /** Pannello contenente i campi con la posizione del rettangolo. */
    private JPanel posizionerettangolopannello;

    /** Pannello contenente i pannelli 'dimensionirettangolopannello' (a sinistra)
    	e 'posizionerettangolopannello' (a destra). */
    private JPanel dimANDposrettangolopannello;

    /** Permette di selezionare il colore di sfondo del pannello. */
    private JComboBoxColor controllosfondocolore;

    /** Pannello contenente la 'ComboBox' per selezionare il colore dello sfondo. */
    private JPanel sfondopannello;

    /** Permette di selezionare il colore del bordo. */
    private JComboBoxColor controllolineacolore;

    /** Permette di selezionare lo spessore del bordo. */
    private JComboBoxStep controllolineaspessore;

    /** Pannello contenente i campi per selezionare le caratteristiche del bordo. */
    private JPanel lineapannello;

    /** Pannello contenente i pannelli 'sfondopannello' (a sinistra)
    	e 'lineapannello' (a destra). */
    private JPanel sfoANDlinpannello;

    /** Pannello contenente tutti i campi per impostare le caratteristiche del rettangolo. */
    private JPanel graficoproprietapannello;
    // Fine variabili relative al pannello con le propriet� del rettangolo.

    // Variabili relative al pannello con le propriet� del testo.
    /** Permette di visualizzare/impostare il testo della stringa del rettangolo.
    	Non utilizzato (non 'editabile' e non visibile) nella versione attuale. */
    private JTextField controllotesto;

    /** Pannello contenente il campo 'controllotesto'. */
    private JPanel stringapannello;

    /** Permette di selezionare il colore del testo. */
    private JComboBoxColor controllotestocolore;

    /** Permette di selezionare il font del testo. */
    private JComboBoxFont controllotestofont;

    /** Pannello contenente i campi per impostare le caratteristiche del testo. */
    private JPanel testopannello;

    /** Permette di selezionare la dimensione del font. */
    private JComboBoxStep controllofontdimensione;

    /** Permette di selezionare lo stile del font. */
    private JComboBoxStyle controllofontstile;

    /** Pannello contenente i campi per impostare le caratteristiche del font. */
    private JPanel fontpannello;

    /** Pannello contenente i pannelli 'testopannello' (a sinistra) e 'fontpannello' (destra). */
    private JPanel tesANDfonpannello;

    /** Pannello contenente i pannelli 'stringapannello' (in alto) e 'tesANDfonpannello' (in basso). */
    private JPanel testoproprietapannello;
    // Fine variabili relative al pannello con le propriet� del testo.

    /** Pannello per tutte le propriet� del processo. */
    private JTabbedPane nordpannello;

    // Pulsanti di interazione con l'utente.
    /** Pulsante per applicare le nuove impostazioni al processo ed uscire. */
    private JButton okpulsante;

    /** Pulsante per uscire senza modificare le impostazioni del processo. */
    private JButton annullapulsante;
    // Fine pulsanti di interazione con l'utente.

    /** Pannello contenente i pulsanti 'okpulsante' e 'annullapulsante'. */
    private JPanel sudpannello;

    /** Pannello principale. */
    private JSplitPane principalepannello;

    /** Riferimento al processo di cui si vogliono modificare le caratteristiche. */
    private ElementoClasse processo;

    /** Riferimento al grafico del processo di cui si vogliono modificare le caratteristiche. */
    private ElementoBoxTesto processografico;

    /** Necessaria per l'implementazione. */
    private Graphics2D grafica2D;
    

    /**
    Costruttore_
        pg        : riferimento all'oggetto di cui si vogliono modificare le caratteristiche_
        g2d       : necessaria per l'implementazione_
        lframe    : riferimento al frame "proprietario" della finestra di dialogo_
        titolo    : titolo della finestra di dialogo.
    */
    public FinestraElementoClasse(ElementoClasse ep, Graphics2D g2d, Frame lframe, String titolo,FileManager fileManager)//ezio 2006 - serve il fileManager
    {
        // Finestra di dialogo di tipo modale.
        super(lframe,titolo,true);

        this.fileManager=fileManager;
        // ctrlClient = Cliente;
        processo = ep;
        processografico = (ElementoBoxTesto)processo.getGrafico();
        grafica2D = g2d;
		
        // Costruzione del pannello relativo alle caratteristiche del rettangolo.
        dimensionirettangolopannello = new JPanel();
        dimensionirettangolopannello.setLayout(new GridLayout(2,2));
        dimensionirettangolopannello.add(new JLabel("Width: "));
        controllolarghezza = new JComboBoxStep(ElementoBoxTesto.minlarghezza,ElementoBoxTesto.maxlarghezza,
            ElementoBoxTesto.steplarghezza,processografico.getWidth());
        dimensionirettangolopannello.add(controllolarghezza);
        dimensionirettangolopannello.add(new JLabel("Height: "));
        controlloaltezza = new JComboBoxStep(ElementoBoxTesto.minaltezza,ElementoBoxTesto.maxaltezza,
            ElementoBoxTesto.stepaltezza,processografico.getHeight());
        dimensionirettangolopannello.add(controlloaltezza);

        posizionerettangolopannello = new JPanel();
        posizionerettangolopannello.setLayout(new GridLayout(2,2));
        posizionerettangolopannello.add(new JLabel("X: "));
        controllorettangoloX = new JTextField(processografico.getXAsString(),4);
        posizionerettangolopannello.add(controllorettangoloX);
        posizionerettangolopannello.add(new JLabel("Y: "));
        controllorettangoloY = new JTextField(processografico.getYAsString(),4);
        posizionerettangolopannello.add(controllorettangoloY);
        controllorettangoloY.setEnabled(false);

        dimANDposrettangolopannello = new JPanel();
        dimANDposrettangolopannello.setLayout(new BorderLayout());
        dimANDposrettangolopannello.add(dimensionirettangolopannello,BorderLayout.WEST);
        dimANDposrettangolopannello.add(posizionerettangolopannello,BorderLayout.EAST);
        dimANDposrettangolopannello.setBorder(BorderFactory.createTitledBorder("Shape"));

        sfondopannello = new JPanel();
        sfondopannello.setLayout(new GridLayout(2,2));
        sfondopannello.add(new JLabel("Color: "));
        controllosfondocolore = new JComboBoxColor(processografico.getBackgroundColor());
        sfondopannello.add(controllosfondocolore);
        sfondopannello.setBorder(BorderFactory.createTitledBorder("BackGround"));

        lineapannello = new JPanel();
        lineapannello.setLayout(new GridLayout(2,2));
        lineapannello.add(new JLabel("Color: "));
        controllolineacolore = new JComboBoxColor(processografico.getLineColor());
        lineapannello.add(controllolineacolore);
        lineapannello.add(new JLabel("Weight: "));
        controllolineaspessore = new JComboBoxStep(ElementoBox.minspessorelinea,ElementoBox.maxspessorelinea,
            ElementoBox.stepspessorelinea,processografico.getLineWeight());
        lineapannello.add(controllolineaspessore);
        lineapannello.setBorder(BorderFactory.createTitledBorder("Border"));

        sfoANDlinpannello = new JPanel();
        sfoANDlinpannello.setLayout(new BorderLayout());
        sfoANDlinpannello.add(sfondopannello,BorderLayout.WEST);
        sfoANDlinpannello.add(lineapannello,BorderLayout.EAST);

        graficoproprietapannello = new JPanel();
        graficoproprietapannello.setLayout(new BorderLayout());
        graficoproprietapannello.add(dimANDposrettangolopannello,BorderLayout.NORTH);
        graficoproprietapannello.add(sfoANDlinpannello,BorderLayout.SOUTH);

        // Costruzione del pannello relativo alle caratteristiche del testo.
        stringapannello = new JPanel();
        stringapannello.setLayout(new FlowLayout(FlowLayout.LEFT));
        stringapannello.add(new JLabel("Text: "));
        controllotesto = new JTextField(processo.getName(),20);
        controllotesto.setEditable(false);
        stringapannello.add(controllotesto);
        stringapannello.setVisible(false);

        testopannello = new JPanel();
        testopannello.setLayout(new GridLayout(4,1));
        testopannello.add(new JLabel("Font: "));
        controllotestofont = new JComboBoxFont(processografico.getTextFont());
        testopannello.add(controllotestofont);
        JLabel FontColor = new JLabel("Color: ");
        testopannello.add(FontColor);
        controllotestocolore = new JComboBoxColor(processografico.getTextColor());
        testopannello.add(controllotestocolore);
        testopannello.setBorder(BorderFactory.createTitledBorder("Text"));

        fontpannello = new JPanel();
        fontpannello.setLayout(new GridLayout(4,1));
        fontpannello.add(new JLabel("Size: "));
        controllofontdimensione = new JComboBoxStep(ElementoBoxTesto.minfontdimensione,ElementoBoxTesto.maxfontdimensione,
            ElementoBoxTesto.stepfontdimensione,processografico.getFontSize());
        fontpannello.add(controllofontdimensione);
        fontpannello.add(new JLabel("Style: "));
        controllofontstile = new JComboBoxStyle(processografico.getFontStyle());
        fontpannello.add(controllofontstile);
        fontpannello.setBorder(BorderFactory.createTitledBorder("Font"));

        tesANDfonpannello = new JPanel();
        tesANDfonpannello.setLayout(new BorderLayout());
        tesANDfonpannello.add(testopannello,BorderLayout.WEST);
        tesANDfonpannello.add(fontpannello,BorderLayout.EAST);

        testoproprietapannello = new JPanel();
        testoproprietapannello.setLayout(new BorderLayout());
        testoproprietapannello.add(stringapannello,BorderLayout.NORTH);
        testoproprietapannello.add(tesANDfonpannello,BorderLayout.SOUTH);

        // Costruzione dei pannelli principali.
        nordpannello = new JTabbedPane();
        nordpannello.addTab(" Graphic ",graficoproprietapannello);
        nordpannello.addTab(" Text ",testoproprietapannello);

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
            boolean check = true;
            String str;
            
            try
            {
       	        Object target = e.getSource();

                if (target.equals(annullapulsante))
          		{
                    dispose();
	        	}
		else 				{	
                    if ((Integer.parseInt(controllorettangoloX.getText())<0)||(Integer.parseInt(controllorettangoloY.getText())<0)
                    	||(Integer.parseInt(controllorettangoloX.getText())>(WithGraphEditor.maxWidth-1))
                    	||(Integer.parseInt(controllorettangoloY.getText())>(WithGraphEditor.maxHeight-1)))
                    {
                    	check = false;
                    	str = "Il numero immesso nella casella X deve \nessere compreso tra 0 e " + (WithGraphEditor.maxWidth-1) + ". \nIl numero immesso nella casella Y deve \nessere compreso tra 0 e " + (WithGraphEditor.maxHeight-1) + ".";
                    	JOptionPane.showMessageDialog(null,str,"Warning",JOptionPane.INFORMATION_MESSAGE);
                    }
                    if (check)
                    {
                    	processografico.setAllParameters(controllolarghezza.getSelectedStep(),
                        	controlloaltezza.getSelectedStep(),Integer.parseInt(controllorettangoloX.getText()),
                        	Integer.parseInt(controllorettangoloY.getText()),controllolineacolore.getSelectedColor(),
                        	controllolineaspessore.getSelectedStep(),controllosfondocolore.getSelectedColor(),
                        	/*processo.getName(),*/controllotestocolore.getSelectedColor(),
                       		controllotestofont.getSelectedFont(),controllofontdimensione.getSelectedStep(),
                        	controllofontstile.getSelectedStile());
                    	processografico.paintElementoGrafico(grafica2D);
						//core.internal.plugin.file.FileManager.setModificata(true);
                    	//ezio 2006
                    	fileManager.setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);
                        dispose();
                    }
				}
            
            }
            catch (NumberFormatException nfe)
            {
            	str = "Nelle caselle X e Y bisogna immettere numeri interi \nnon negativi. Controllare il formato!!";
            	JOptionPane.showMessageDialog(null,str,"Warning",JOptionPane.INFORMATION_MESSAGE);
            }            
            catch (Exception ex)
            {
	    	}
        }
    }

}