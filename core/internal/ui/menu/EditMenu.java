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
    

package core.internal.ui.menu;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import core.internal.ui.action.menu.edit.CopyItemAction;
import core.internal.ui.action.menu.edit.CutItemAction;
import core.internal.ui.action.menu.edit.DelItemAction; 
import core.internal.ui.action.menu.edit.PasteItemAction;
import core.internal.ui.action.menu.edit.RedoItemAction;
import core.internal.ui.action.menu.edit.UndoItemAction;
import core.internal.ui.resources.TypeJMenu;
import core.internal.ui.simpleinterface.EditGraphInterface;


/** La classe crea il menu Edit. */
	
public class EditMenu extends TypeJMenu
{

    /** Item per l'operazione di cut. */    
    private JMenuItem CutMenuEdit;    
    private CutItemAction CutItemListener;

    /** Item per l'operazione di copy. */    
    private JMenuItem CopyMenuEdit; 
    private CopyItemAction CopyItemListener;

    /** Item per l'operazione di paste. */    
    private JMenuItem PasteMenuEdit;   
    private PasteItemAction PasteItemListener;    

    /** Item per l'operazione di delete. */    
    private JMenuItem DeleteMenuEdit;   
    private DelItemAction DeleteItemListener;    

    /** Item per l'operazione di undo. */    
    private JMenuItem UndoMenuEdit;   
    private UndoItemAction UndoItemListener;    

    /** Item per l'operazione di redo. */    
    private JMenuItem RedoMenuEdit;   
    private RedoItemAction RedoItemListener;    
    
    
    /** Costruttore. */
    public EditMenu(EditGraphInterface rifGraphWindow)
    {
		this();
		//updateRifGraphWindow(null);
		updateRifGraphWindow(rifGraphWindow);
    }

	/** Costruttore. 
	 * per default tutti i pulsanti sono disabilitati, vengono abilitati solo
	 * quando vi è un gestore valido (vedere updateRifGraphWindow)
	 * */
	public EditMenu()
	{
		super("Edit"); 
		CutMenuEdit = createMenuItem("Cut",new ImageIcon(PathGif + "cut.gif"));
		CutMenuEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,InputEvent.CTRL_MASK));
		CutItemListener = new CutItemAction(null);
		CutMenuEdit.addActionListener(CutItemListener);		


		CopyMenuEdit = createMenuItem("Copy",new ImageIcon(PathGif + "copy.gif"));
		CopyMenuEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,InputEvent.CTRL_MASK));
		CopyItemListener = new CopyItemAction(null);
		CopyMenuEdit.addActionListener(CopyItemListener);			

		PasteMenuEdit = createMenuItem("Paste",new ImageIcon(PathGif + "paste.gif"));
		PasteMenuEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,InputEvent.CTRL_MASK));
		PasteItemListener = new PasteItemAction(null);
		PasteMenuEdit.addActionListener(PasteItemListener);	        

		DeleteMenuEdit = createMenuItem("Delete");
		DeleteMenuEdit.setAccelerator(KeyStroke.getKeyStroke("DELETE"));        
		DeleteItemListener = new DelItemAction(null); 
		DeleteMenuEdit.addActionListener(DeleteItemListener);		

		UndoMenuEdit = createMenuItem("Undo",new ImageIcon(PathGif + "undo.gif"));
		UndoMenuEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,InputEvent.CTRL_MASK));
		UndoItemListener = new UndoItemAction(null);
		UndoMenuEdit.addActionListener(UndoItemListener);		

		RedoMenuEdit = createMenuItem("Redo", new ImageIcon(PathGif + "redo.gif"));
		RedoMenuEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,InputEvent.CTRL_MASK));
		RedoItemListener = new RedoItemAction(null); 
		RedoMenuEdit.addActionListener(RedoItemListener);    	
		enableAll(false);

		add(CutMenuEdit);
		add(CopyMenuEdit);
		add(PasteMenuEdit);
		addSeparator();
		add(DeleteMenuEdit);
		addSeparator();
		add(UndoMenuEdit);
		add(RedoMenuEdit);

		TavolaItems = new Hashtable();
		TavolaItems.put("Cut",CutMenuEdit);
		TavolaItems.put("Copy",CopyMenuEdit); 
		TavolaItems.put("Paste",PasteMenuEdit); 
		TavolaItems.put("Undo",UndoMenuEdit); 
		TavolaItems.put("Redo",RedoMenuEdit); 
		TavolaItems.put("Del",DeleteMenuEdit);		    
	}


	/**
	 * abilita / disabilita tutti i menu di edit
	 * @param enable true abilita tutti, false disabilita
	 */
	private void enableAll(boolean enable){
		CutMenuEdit.setEnabled(enable);
		CopyMenuEdit.setEnabled(enable);
		PasteMenuEdit.setEnabled(enable);
		DeleteMenuEdit.setEnabled(enable);
		UndoMenuEdit.setEnabled(enable);
		RedoMenuEdit.setEnabled(enable);
	}


    /** Aggiorna il riferimento alla finestra corrente:
    	TopologyWindow, StateWindow, SequenceWindow o PromelaWindow. */ 
//    public void updateRifGraphWindow(WithGraphWindow rifGraphWindow)
	public void updateRifGraphWindow(EditGraphInterface rifGraphWindow)

    {
    	
    	CutItemListener.updateRifGraphWindow(rifGraphWindow);
    	CopyItemListener.updateRifGraphWindow(rifGraphWindow);
    	PasteItemListener.updateRifGraphWindow(rifGraphWindow);
    	UndoItemListener.updateRifGraphWindow(rifGraphWindow);
    	RedoItemListener.updateRifGraphWindow(rifGraphWindow);  
    	DeleteItemListener.updateRifGraphWindow(rifGraphWindow); 
		if(rifGraphWindow == null){
			enableAll(false);
		}
		else{
			enableAll(true);
		}    	  	    	    	    	
    }
        
}