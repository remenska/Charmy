
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
    

package plugin.topologychannels.utility.listaProcesso.toolbar;

import javax.swing.AbstractButton;

import core.resources.toolbar.TypeToolBarGroup;

import plugin.topologychannels.utility.listaProcesso.action.ListaProcessoAllButtonAction;
import plugin.topologychannels.utility.listaProcesso.action.ListaProcessoGlobalButtonAction;
import plugin.topologychannels.utility.listaProcesso.action.ListaProcessoInterface;
import plugin.topologychannels.utility.listaProcesso.action.ListaProcessoNoDummyButtonAction;
import plugin.topologychannels.utility.listaProcesso.action.ListaProcessoUserButtonAction;


/** La classe crea e gestisce la "ZoomToolBar", ovvero la toolbar
	contenente i pulsanti per tutte le operazioni di zoom. */
	
public class ListaProcessoToolBar 
	extends TypeToolBarGroup
{
	
    private AbstractButton AllButton;
    private ListaProcessoAllButtonAction AllButtonListener;
    
    
    
    private AbstractButton UserButton;
    private ListaProcessoUserButtonAction UserButtonListener;
    
    private AbstractButton GlobalButton;
    private ListaProcessoGlobalButtonAction GlobalButtonListener;
    
    private AbstractButton NoDummyButton;
    private ListaProcessoNoDummyButtonAction NoDummyButtonListener;
    
    

    /** Costruttore_ Prende in ingresso il riferimento all'oggetto 
    	sul quale devono agire i pulsanti della toolbar. */
    public ListaProcessoToolBar(ListaProcessoInterface rifGraphWindow)
    {
        super("Process Selection");
    	
		AllButton = createToolbarToogleButton("core/internal/ui/icon/stretchHorizontal.gif","Charmy.jar","All Process");
		AllButtonListener = new ListaProcessoAllButtonAction(rifGraphWindow);
        AllButton.addActionListener(AllButtonListener);
        
        UserButton = createToolbarToogleButton("core/internal/ui/icon/compressHorizontal.gif","Charmy.jar","User Process");
        UserButtonListener = new ListaProcessoUserButtonAction(rifGraphWindow);
        UserButton.addActionListener(UserButtonListener);
        
		GlobalButton = createToolbarToogleButton("core/internal/ui/icon/stretchVertical.gif","Charmy.jar","Global Process");
        GlobalButtonListener = new ListaProcessoGlobalButtonAction(rifGraphWindow);
        GlobalButton.addActionListener(GlobalButtonListener);
        
        NoDummyButton = createToolbarToogleButton("core/internal/ui/icon/compressVertical.gif","Charmy.jar","No Dummy Process");
        NoDummyButtonListener = new ListaProcessoNoDummyButtonAction(rifGraphWindow);
        NoDummyButton.addActionListener(NoDummyButtonListener);
        
       // this.setBorder(new LineBorder(new Color(0,0,0), 2));
        addGroupButton(AllButton);
        this.addSeparator();
        addGroupButton(UserButton);
		this.addSeparator();
		addGroupButton(GlobalButton);
		this.addSeparator();
		addGroupButton(NoDummyButton);
		NoDummyButton.setSelected(true);
        this.setFloatable(false);
        //add(ZoomResetButton);       
    }
    

	/** Permette di modificare l'oggetto sul quale agiscono
		i pulsanti della toolbar. */    
    public void updateRifGraphWindow(ListaProcessoInterface rifGraphWindow)
    {
    	AllButtonListener.updateRifGraphWindow(rifGraphWindow);
    	NoDummyButtonListener.updateRifGraphWindow(rifGraphWindow);
    	UserButtonListener.updateRifGraphWindow(rifGraphWindow);
    	GlobalButtonListener.updateRifGraphWindow(rifGraphWindow);
    	//ZoomResetButtonListener.updateRifGraphWindow(rifGraphWindow);    	    	    	    	
    }

}