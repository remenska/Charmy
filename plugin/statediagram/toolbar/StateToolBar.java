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
    

package plugin.statediagram.toolbar;
import java.awt.Color;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

import core.internal.runtime.data.IPlugData;
import core.internal.ui.resources.TypeToolBar;
import core.resources.action.ImgButtonAction;
import core.resources.action.image.ImageItemAction;

import plugin.statediagram.Config;
import plugin.statediagram.StateWindow;
import plugin.statediagram.action.AsynMessageButtonAction;
import plugin.statediagram.action.BuildStateButtonAction;
import plugin.statediagram.action.CheckButtonAction;
import plugin.statediagram.action.CopyThreadButtonAction;
import plugin.statediagram.action.DelThreadButtonAction;
import plugin.statediagram.action.LoopMessageButtonAction;
import plugin.statediagram.action.NewThreadButtonAction;
import plugin.statediagram.action.ReadyButtonAction;
import plugin.statediagram.action.RenameThreadButtonAction;
import plugin.statediagram.action.StartStateButtonAction;
import plugin.statediagram.action.SynMessageButtonAction;
import plugin.statediagram.action.menuhelp.StateItemAction;

/** La classe crea e gestisce la "StateToolBar", ovvero la toolbar
	contenente i pulsanti per le operazioni relative allo State Diagram. */
	
public class StateToolBar extends TypeToolBar
{

    private Border DefaultBorder;
    
	private AbstractButton CheckButton;
	private CheckButtonAction CheckListener;    
    
    private AbstractButton NewThreadButton;
    private NewThreadButtonAction NewThreadListener;
    
    private AbstractButton CopyThreadButton;
    private CopyThreadButtonAction CopyThreadListener;
    
    private AbstractButton DelThreadButton;
    private DelThreadButtonAction DelThreadListener;
       
    private AbstractButton RenameThreadButton;
    private RenameThreadButtonAction RenameThreadListener;
    
    private AbstractButton ReadySequenceButton;
    private ReadyButtonAction ReadyListener;
        
    private AbstractButton StartStateButton;
    private StartStateButtonAction StartStateListener;
    
    private AbstractButton BuildStateButton;
    private BuildStateButtonAction BuildStateListener;
        
    private AbstractButton SynMessageButton;
    private SynMessageButtonAction SynMessageListener;
        
    private AbstractButton AsynMessageButton;
    private AsynMessageButtonAction AsynMessageListener;
        
    private AbstractButton LoopMessageButton;
    private LoopMessageButtonAction LoopMessageListener;

    private ImgButtonAction ImgStateButtonListener;
    private AbstractButton ImgStateButton;
          
    private AbstractButton HelpStateButton; 
    
    private IPlugData plugData; 
  
  	/** Costruttore_ Prende in ingresso il riferimento all'oggetto
  		che gestisce la creazione di uno State Diagram. */  
    public StateToolBar(StateWindow StatePanel, IPlugData plugData)
    {       
        super("State Toolbar",StatePanel);
        this.plugData=plugData;	
        Config.init();	
        
		//CheckButton = createToolbarButton(Config.PathGif+Config.CheckIMG,Config.Check,Config.CheckMN);
		CheckButton = createToolbarButtonJar(Config.PathGif+Config.CheckIMG,Config.Check,Config.CheckMN);		
		CheckListener = new CheckButtonAction(StatePanel,plugData);
		CheckButton.addActionListener(CheckListener);
		
		//NewThreadButton = createToolbarButton(Config.PathGif+Config.NewThreadIMG,Config.NewThread);
		NewThreadButton = createToolbarButtonJar(Config.PathGif+Config.NewThreadIMG,Config.NewThread);
		NewThreadListener = new NewThreadButtonAction(StatePanel);
		NewThreadButton.addActionListener(NewThreadListener);

        DefaultBorder = NewThreadButton.getBorder();

		//CopyThreadButton = createToolbarButton(Config.PathGif+Config.CopyThreadIMG,Config.CopyThread);                
		CopyThreadButton = createToolbarButtonJar(Config.PathGif+Config.CopyThreadIMG,Config.CopyThread);
		CopyThreadListener = new CopyThreadButtonAction(StatePanel);
		CopyThreadButton.addActionListener(CopyThreadListener);
		
		//DelThreadButton = createToolbarButton(Config.PathGif+Config.DelThreadIMG,Config.DelThread);
		DelThreadButton = createToolbarButtonJar(Config.PathGif+Config.DelThreadIMG,Config.DelThread);
        DelThreadListener = new DelThreadButtonAction(StatePanel);
        DelThreadButton.addActionListener(DelThreadListener);
		
		//RenameThreadButton = createToolbarButton(Config.PathGif+Config.RenameThreadIMG,Config.RenameThread);
		RenameThreadButton = createToolbarButtonJar(Config.PathGif+Config.RenameThreadIMG,Config.RenameThread);
		RenameThreadListener = new RenameThreadButtonAction(StatePanel);
		RenameThreadButton.addActionListener(RenameThreadListener);  

		//ReadySequenceButton = createToolbarButton(Config.PathGif+Config.ReadySequenceIMG,Config.ReadySequence);                
		ReadySequenceButton = createToolbarButtonJar(Config.PathGif+Config.ReadySequenceIMG,Config.ReadySequence);
        ReadyListener = new ReadyButtonAction(StatePanel,this);
        ReadySequenceButton.addActionListener(ReadyListener);
		
		//StartStateButton = createToolbarButton(Config.PathGif+Config.StartStateIMG,Config.StartState);                
		StartStateButton = createToolbarButtonJar(Config.PathGif+Config.StartStateIMG,Config.StartState);
        StartStateListener = new StartStateButtonAction(StatePanel,this,StartStateButton);
                
		//BuildStateButton = createToolbarButton(Config.PathGif+Config.BuildStateIMG,Config.BuildState);
		BuildStateButton = createToolbarButtonJar(Config.PathGif+Config.BuildStateIMG,Config.BuildState);
        BuildStateListener = new BuildStateButtonAction(StatePanel,this,BuildStateButton);
        BuildStateButton.addMouseListener(BuildStateListener);
        
		//SynMessageButton = createToolbarButton(Config.PathGif+Config.SynMessageIMG,Config.SynMessage);
		SynMessageButton = createToolbarButtonJar(Config.PathGif+Config.SynMessageIMG,Config.SynMessage);
        SynMessageListener = new SynMessageButtonAction(StatePanel,this,SynMessageButton);
        SynMessageButton.addMouseListener(SynMessageListener);		
        
		//AsynMessageButton = createToolbarButton(Config.PathGif+Config.AsynMessageIMG,Config.AsynMessage);                
		AsynMessageButton = createToolbarButtonJar(Config.PathGif+Config.AsynMessageIMG,Config.AsynMessage);
        AsynMessageListener = new AsynMessageButtonAction(StatePanel,this,AsynMessageButton);
        AsynMessageButton.addMouseListener(AsynMessageListener);
		
		//LoopMessageButton = createToolbarButton(Config.PathGif+Config.LoopMessageIMG,Config.LoopMessage);                
		LoopMessageButton = createToolbarButtonJar(Config.PathGif+Config.LoopMessageIMG,Config.LoopMessage);
        LoopMessageListener = new LoopMessageButtonAction(StatePanel,this,LoopMessageButton);
        LoopMessageButton.addMouseListener(LoopMessageListener);
		
		//ImgStateButton = createToolbarButton(Config.PathGif+Config.ImgStateIMG,Config.ImgState);                
		ImgStateButton = createToolbarButtonJar(Config.PathGif+Config.ImgStateIMG,Config.ImgState);
		ImgStateButtonListener = new ImageItemAction(StatePanel);
		ImgStateButton.addActionListener(ImgStateButtonListener);
				
		//HelpStateButton = createToolbarButton(Config.PathGif+Config.HelpStateIMG,Config.HelpState);                
		HelpStateButton = createToolbarButtonJar(Config.PathGif+Config.HelpStateIMG,Config.HelpState);
		HelpStateButton.addActionListener(new StateItemAction());       
        
		add(NewThreadButton);
		add(CheckButton);        
        add(CopyThreadButton);
		add(DelThreadButton);
        add(RenameThreadButton);
        add(ReadySequenceButton);
        add(StartStateButton);
		add(BuildStateButton);
        add(SynMessageButton);
		add(AsynMessageButton);
		add(LoopMessageButton);
		add(ImgStateButton);
        add(HelpStateButton);
        setNoPressed();
        
        setEnableThreadButtons(false);
        setEnableStateButtons(false);
    }
    
    
    /** Riporta nella posizione di riposo tutti i pulsanti della toolbar. */    
    public void setNoPressed()
    {      
        StartStateButton.setBorder(DefaultBorder);
        BuildStateButton.setBorder(DefaultBorder);
		SynMessageButton.setBorder(DefaultBorder);
    	AsynMessageButton.setBorder(DefaultBorder);
    	LoopMessageButton.setBorder(DefaultBorder);     
    } 
    
    public void setPressed(int index)
    {
        switch (index)
        {
            case 1:
                BuildStateButton.setBorder(BorderFactory.createBevelBorder(1,Color.LIGHT_GRAY,Color.GRAY));
                break;
            case 2:
                SynMessageButton.setBorder(BorderFactory.createBevelBorder(1,Color.LIGHT_GRAY,Color.GRAY));
                break;
            case 3:
                AsynMessageButton.setBorder(BorderFactory.createBevelBorder(1,Color.LIGHT_GRAY,Color.GRAY));
                break;
            case 4:
                LoopMessageButton.setBorder(BorderFactory.createBevelBorder(1,Color.LIGHT_GRAY,Color.GRAY));
                break;
        }
    }
    
    public void setEnableThreadButtons(boolean en){
    	if(en){
			CopyThreadButton.setEnabled(true);
			DelThreadButton.setEnabled(true);
			RenameThreadButton.setEnabled(true);
			ReadySequenceButton.setEnabled(true);			
			ImgStateButton.setEnabled(true);
    	}
    	else{
			CopyThreadButton.setEnabled(false);
			DelThreadButton.setEnabled(false);
			RenameThreadButton.setEnabled(false);
			ReadySequenceButton.setEnabled(false);			
			ImgStateButton.setEnabled(false);
    	}
    }
    
    
    public void setEnableStateButtons(boolean en){
		if(en){
			CheckButton.setEnabled(true);
			StartStateButton.setEnabled(true);
			BuildStateButton.setEnabled(true);
			SynMessageButton.setEnabled(true);
			AsynMessageButton.setEnabled(true);
			LoopMessageButton.setEnabled(true);
		}
		else{
			CheckButton.setEnabled(false);
			StartStateButton.setEnabled(false);
			BuildStateButton.setEnabled(false);
			SynMessageButton.setEnabled(false);
			AsynMessageButton.setEnabled(false);
			LoopMessageButton.setEnabled(false);
		}	
    }

	public void setEnabledStartButton(boolean en){
		if(en){
			StartStateButton.setEnabled(en);
			StartStateButton.addMouseListener(StartStateListener);
		}else{
			StartStateButton.setEnabled(en);
			StartStateButton.removeMouseListener(StartStateListener);
		}
	}
	

}