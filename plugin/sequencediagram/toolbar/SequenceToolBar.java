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


package plugin.sequencediagram.toolbar;
import javax.swing.AbstractButton;
import javax.swing.border.Border;

import plugin.sequencediagram.Config;
import plugin.sequencediagram.SequenceWindow;
import plugin.sequencediagram.action.AsynMessageButtonAction;
import plugin.sequencediagram.action.ConstraintsButtonAction;
import plugin.sequencediagram.action.CopySequenceButtonAction;
import plugin.sequencediagram.action.DelSequenceButtonAction;
import plugin.sequencediagram.action.LoopButtonAction;
import plugin.sequencediagram.action.LoopMessageButtonAction;
import plugin.sequencediagram.action.NewSequenceButtonAction;
import plugin.sequencediagram.action.ParallelButtonAction;
import plugin.sequencediagram.action.ReadyButtonAction;
import plugin.sequencediagram.action.RenameSequenceButtonAction;
import plugin.sequencediagram.action.SimButtonAction;
import plugin.sequencediagram.action.SynMessageButtonAction;
import plugin.sequencediagram.action.TimeLineButtonAction;
import plugin.sequencediagram.action.menuhelp.SequenceItemAction;
import core.internal.ui.resources.TypeToolBar;
import core.resources.action.ImgButtonAction;
import core.resources.action.image.ImageItemAction;

/** La classe crea e gestisce la "SequenceToolBar", ovvero la toolbar
 contenente i pulsanti per le operazioni relative al Sequence Diagram. */

public class SequenceToolBar extends TypeToolBar
{
	
	private Border DefaultBorder;
	
	private AbstractButton NewSequenceButton;
	private NewSequenceButtonAction NewSequenceListener;
	
	private AbstractButton DelSequenceButton;
	private DelSequenceButtonAction DelSequenceListener;
	
	private AbstractButton CopySequenceButton;
	private CopySequenceButtonAction CopySequenceListener;
	
//	private AbstractButton SimpleSequenceButton;
//	private SimpleMessageButtonAction SimpleMessageListener;
	
	private AbstractButton ReadySequenceButton;
	private ReadyButtonAction ReadyListener;
	
	private AbstractButton SynSequenceButton;
	private SynMessageButtonAction SynMessageListener;
	
	private AbstractButton AsynSequenceButton;
	private AsynMessageButtonAction AsynMessageListener;
	
	private AbstractButton LoopSequenceButton;
	private LoopMessageButtonAction LoopMessageListener;
	
	private AbstractButton TimeSequenceButton;
	private TimeLineButtonAction TimeLineListener;
	
	private AbstractButton ConstraintsButton;
	private ConstraintsButtonAction ConstraintsButtonListener;
	
	private AbstractButton PropertiesSequenceButton;
	private RenameSequenceButtonAction RenameSequenceListener;
	
	private ImgButtonAction ImgSequenceButtonListener;
	private AbstractButton ImgSequenceButton;    
	
//	private UpdateLTLButtonAction UpdateLTLButtonListener;
//	private AbstractButton UpdateLTLButton;     
	
	private AbstractButton HelpSequenceButton;
	////NEW Flamel
	private AbstractButton ParallelButton;
	private ParallelButtonAction ParallelButtonListener;
	
	private AbstractButton SimButton;
	private SimButtonAction SimButtonListener;
	
	private AbstractButton LoopButton;
	private LoopButtonAction LoopButtonListener;
	
	
	
	/** Costruttore_ Prende in ingresso il riferimento all'oggetto
	 che gestisce la creazione di un Sequence Diagram. */
	public SequenceToolBar(SequenceWindow SequencePanel)
	{    
		super("Sequence Toolbar",SequencePanel);
		Config.init();
		//SequencePanel.getPlugDataWin().getPlugManager().getPlugEditDescriptor()
		
		//NewSequenceButton = createToolbarButton(Config.PathGif+Config.NewSequenceIMG,Config.NewSequence);
	
		NewSequenceButton = createToolbarButtonJar(Config.PathGif+Config.NewSequenceIMG,Config.NewSequence);
		NewSequenceListener = new NewSequenceButtonAction(SequencePanel);
		NewSequenceButton.addActionListener(NewSequenceListener);
		  
		DefaultBorder = NewSequenceButton.getBorder();		
		
		//DelSequenceButton = createToolbarButton(Config.PathGif+Config.DelSequenceIMG,Config.DelSequence);
		
		//ezio 2006 correggere createToolbarButtonJar() su tutto il costruttore.
		
		DelSequenceButton =createToolbarButtonJar(Config.PathGif+Config.DelSequenceIMG,Config.DelSequence);
		DelSequenceListener = new DelSequenceButtonAction(SequencePanel);
		DelSequenceButton.addActionListener(DelSequenceListener);
		
		//CopySequenceButton = createToolbarButton(Config.PathGif+Config.CopySequenceIMG,Config.CopySequence);
		CopySequenceButton = createToolbarButtonJar(Config.PathGif+Config.CopySequenceIMG,Config.CopySequence);
		CopySequenceListener = new CopySequenceButtonAction(SequencePanel);
		CopySequenceButton.addActionListener(CopySequenceListener);        
		
		//ReadySequenceButton = createToolbarButton(Config.PathGif+Config.ReadySequenceIMG,Config.ReadySequence);
		ReadySequenceButton = createToolbarButtonJar(Config.PathGif+Config.ReadySequenceIMG,Config.ReadySequence);
		
		ReadyListener = new ReadyButtonAction(SequencePanel,this);
		ReadySequenceButton.addActionListener(ReadyListener);
		
		//SynSequenceButton = createToolbarButton(Config.PathGif+Config.SynSequenceIMG,Config.SynSequence);
		SynSequenceButton = createToolbarButtonJar(Config.PathGif+Config.SynSequenceIMG,Config.SynSequence);		
		SynMessageListener = new SynMessageButtonAction(SequencePanel,this,SynSequenceButton);
		SynSequenceButton.addMouseListener(SynMessageListener);		
		
		//AsynSequenceButton = createToolbarButton(Config.PathGif+Config.AsynSequenceIMG,Config.AsynSequence);
		AsynSequenceButton = createToolbarButtonJar(Config.PathGif+Config.AsynSequenceIMG,Config.AsynSequence);		
		AsynMessageListener = new AsynMessageButtonAction(SequencePanel,this,AsynSequenceButton);
		AsynSequenceButton.addMouseListener(AsynMessageListener);
		
		//LoopSequenceButton = createToolbarButton(Config.PathGif+Config.LoopSequenceIMG,Config.LoopSequence);
		LoopSequenceButton = createToolbarButtonJar(Config.PathGif+Config.LoopSequenceIMG,Config.LoopSequence);		
		LoopMessageListener = new LoopMessageButtonAction(SequencePanel,this,LoopSequenceButton);
		LoopSequenceButton.addMouseListener(LoopMessageListener);
		
		//ConstraintsButton = createToolbarButton(Config.PathGif+Config.ConstraintIMG,Config.Constraint);		
		ConstraintsButton = createToolbarButtonJar(Config.PathGif+Config.ConstraintIMG,Config.Constraint);
		ConstraintsButtonListener = new ConstraintsButtonAction(SequencePanel,this,ConstraintsButton);
		ConstraintsButton.addMouseListener(ConstraintsButtonListener); 
		
		//TimeSequenceButton = createToolbarButton(Config.PathGif+Config.TimeIMG,Config.Time);
		TimeSequenceButton = createToolbarButtonJar(Config.PathGif+Config.TimeIMG,Config.Time);		
		TimeLineListener = new TimeLineButtonAction(SequencePanel);
		TimeSequenceButton.addActionListener(TimeLineListener);
		
		//PropertiesSequenceButton = createToolbarButton(Config.PathGif+Config.PropSequenceIMG,Config.PropSequence);
		PropertiesSequenceButton = createToolbarButtonJar(Config.PathGif+Config.PropSequenceIMG,Config.PropSequence);		
		RenameSequenceListener = new RenameSequenceButtonAction(SequencePanel);
		PropertiesSequenceButton.addActionListener(RenameSequenceListener);
		
		//ImgSequenceButton = createToolbarButton(Config.PathGif+Config.ImgSequenceIMG,Config.ImgSequence);		
		ImgSequenceButton = createToolbarButtonJar(Config.PathGif+Config.ImgSequenceIMG,Config.ImgSequence);
		ImgSequenceButtonListener = new ImageItemAction(SequencePanel);
		ImgSequenceButton.addActionListener(ImgSequenceButtonListener);
		
		//HelpSequenceButton = createToolbarButton(Config.PathGif+Config.HelpSequenceIMG,Config.HelpSequence);
		HelpSequenceButton = createToolbarButtonJar(Config.PathGif+Config.HelpSequenceIMG,Config.HelpSequence);		
		HelpSequenceButton.addActionListener(new SequenceItemAction());      
		
////		/New
		ParallelButton = createToolbarButtonJar(Config.PathGif+Config.ParallelIMG,Config.Parallel);		
		ParallelButtonListener = new ParallelButtonAction(SequencePanel,this,ParallelButton);
		ParallelButton.addMouseListener(ParallelButtonListener);                
		
		SimButton = createToolbarButtonJar(Config.PathGif+Config.SimIMG,Config.Sim);		
		SimButtonListener = new SimButtonAction(SequencePanel,this,SimButton);
		SimButton.addMouseListener(SimButtonListener);
		
		LoopButton = createToolbarButtonJar(Config.PathGif+Config.LoopIMG,Config.Loop);		
		LoopButtonListener = new LoopButtonAction(SequencePanel,this,LoopButton);
		LoopButton.addMouseListener(LoopButtonListener);
		
		
		add(NewSequenceButton);
		add(CopySequenceButton);
		add(DelSequenceButton);
		add(PropertiesSequenceButton);		
		add(ReadySequenceButton);
		add(SynSequenceButton);
		add(AsynSequenceButton);
		add(LoopSequenceButton);
		add(ConstraintsButton);
		add(ParallelButton);
		add(SimButton);
		add(LoopButton);
		
		add(TimeSequenceButton);
		add(ImgSequenceButton);
		add(HelpSequenceButton);
		
		setNoPressed();
	}
	
	
	  /** Riporta nella posizione di riposo tutti i pulsanti della toolbar. */    
    public void setNoPressed()
    { 
        SynSequenceButton.setBorder(DefaultBorder);
        AsynSequenceButton.setBorder(DefaultBorder);
        LoopSequenceButton.setBorder(DefaultBorder);
        ConstraintsButton.setBorder(DefaultBorder);
        ParallelButton.setBorder(DefaultBorder);
        SimButton.setBorder(DefaultBorder);
        LoopButton.setBorder(DefaultBorder);

    }      

	public void setEnabledSequenceButtons(boolean en){
		CopySequenceButton.setEnabled(en);
		DelSequenceButton.setEnabled(en);
		PropertiesSequenceButton.setEnabled(en);					
	}

	public void setEnabledMessageButtons(boolean en){
		ReadySequenceButton.setEnabled(en);
		SynSequenceButton.setEnabled(en);
		AsynSequenceButton.setEnabled(en);
		LoopSequenceButton.setEnabled(en);					
		ConstraintsButton.setEnabled(en);					
		TimeSequenceButton.setEnabled(en);
		ImgSequenceButton.setEnabled(en);
                ParallelButton.setEnabled(en);
                SimButton.setEnabled(en);
                LoopButton.setEnabled(en);

	}

	
}