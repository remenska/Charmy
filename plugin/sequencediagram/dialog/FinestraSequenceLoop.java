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
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.SpinnerNumberModel;

import plugin.sequencediagram.data.ElementoLoop;
import plugin.sequencediagram.graph.GraficoLoop;
import plugin.topologydiagram.TopologyWindow;
import core.internal.plugin.file.FileManager;
import core.internal.runtime.data.PlugDataManager;

/** La classe realizza un pannello di dialogo tramite il quale l'utente pu�
 * impostare il nome di un oggetto 'ElementoSeqLink' (utilizzato come link
 * nel Sequence Diagram) e del grafico associato (istanza della classe
 * 'GraficoLink'). */

public class FinestraSequenceLoop extends JDialog {
	
	public PlugDataManager pdm;//ezio 2006
    /** Memorizza il riferimento all'oggetto grafico associato al canale. */
    private GraficoLoop grafico;
    
    /** Pannello contenente il campo per impostare il nome del canale. */
    private JPanel nordpannello;
    
    /** Pannello contenente il campo per impostare il nome del canale. */
    private JPanel sudpannello;
    
    /** Permette di selezionare il max. */
    private JSpinner max_value;
    
    /** Permette di selezionare il min. */
    private JSpinner min_value;
    
    private SpinnerNumberModel model1;
    private SpinnerNumberModel model2;
    
    /** Pulsante per applicare le nuove impostazioni all'opertore. */
    private JButton okpulsante;
    
    /** Pulsante per uscire senza modificare le impostazioni all'opertore. */
    private JButton annullapulsante;
    
    /** Pannello principale. */
    private JSplitPane principalepannello;
    
    /** Riferimento all'oggetto di cui si vogliono modificare le caratteristiche. */
    private ElementoLoop Loop;
    
    
    /** Costruttore_
     * cnl         : riferimento al loop di cui si vogliono modificare le caratteristiche_
     * lframe      : riferimento al frame 'proprietario' della finestra di dialogo_
     * titolo      : titolo della finestra di dialogo. */
    public FinestraSequenceLoop(ElementoLoop cnl, Frame lframe, String titolo, PlugDataManager pd) {
        
        // Finestra di dialogo di tipo modale.
        super(lframe,titolo,true);
        Loop = cnl;
        
        this.pdm=pd;//ezio 2006
        
        nordpannello = new JPanel();
        nordpannello.setLayout(new GridLayout(1,4));
        nordpannello.add(new JLabel("Min Value:    "));
        
        model1 = new SpinnerNumberModel(0,0,100,1);
        model2 = new SpinnerNumberModel(0,0,100,1);
    
        max_value = new JSpinner(model1);
        min_value = new JSpinner(model2);
        
        max_value.setValue(new Integer (Loop.getMax()));
        min_value.setValue(new Integer(Loop.getMin()));

        
        nordpannello.add(min_value);
        nordpannello.add(new JLabel("Max Value:    "));
        nordpannello.add(max_value);
        nordpannello.setBorder(BorderFactory.createTitledBorder("Loop value"));
        
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
                    Loop.setSelected(false);
                    dispose();
                } 
                else{
                    boolean bo = Loop.testAndSet();
                    
                    int value_max = model1.getNumber().intValue();
                    int value_min = model2.getNumber().intValue();
                    if(value_min < value_max){
                        Loop.setMax(value_max);
                        Loop.setMin(value_min);                        
                    }
                        
                    
                    Loop.setSelected(false);
                    Loop.testAndReset(bo);
                    // ezio 2006 core.internal.plugin.file.FileManager.setModificata(true);
                    pdm.getFileManager().setChangeWorkset(TopologyWindow.idPluginFileCharmy,true);// ezio 2006
                    dispose();
                    
                }
                
            }
            catch (Exception ex){
                System.out.println("Errore: " + ex);
            }
        }
    }
}  
    


