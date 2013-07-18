/*
 * Created on 26-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package plugin.charmyui.utility;

import java.util.Hashtable;

import javax.swing.AbstractButton;

import core.internal.ui.action.toolbar.edit.CopyGraphButtonAction;
import core.internal.ui.action.toolbar.edit.CutGraphButtonAction;
import core.internal.ui.action.toolbar.edit.DelGraphButtonAction;
import core.internal.ui.action.toolbar.edit.PasteGraphButtonAction;
import core.internal.ui.action.toolbar.edit.RedoGraphButtonAction;
import core.internal.ui.action.toolbar.edit.UndoGraphButtonAction;
import core.internal.ui.simpleinterface.EditGraphInterface;


import plugin.charmyui.extensionpoint.toolbar.ExtensionPointToolbar;
import plugin.charmyui.extensionpoint.editor.IHostEditor;

/**
 * @author ezio di nisio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EditToolbar extends TypeToolBar {

    private AbstractButton CutButton;
    private CutGraphButtonAction CutButtonListener;
    
    private AbstractButton CopyButton;
    private CopyGraphButtonAction CopyButtonListener;
    
    private AbstractButton PasteButton;
    private PasteGraphButtonAction PasteButtonListener;
    
    private AbstractButton UndoButton;
    private UndoGraphButtonAction UndoButtonListener;
    
    private AbstractButton RedoButton;
    private RedoGraphButtonAction RedoButtonListener;
    
    private AbstractButton DelButton;
    private DelGraphButtonAction DelButtonListener;
    
    /**
     * 
     */
    public EditToolbar(IHostEditor rifGraphWindow) {
        super();
        // TODO Auto-generated constructor stub
        
        //super("Edit Toolbar");
        
        CutButton = createToolbarButtonJar("cut.gif","Cut");
        CutButtonListener = new CutGraphButtonAction(null);
        CutButton.addActionListener(CutButtonListener);
        
        CopyButton = createToolbarButtonJar("copy.gif","Copy");
        CopyButtonListener = new CopyGraphButtonAction(null);
        CopyButton.addActionListener(CopyButtonListener);
                
		PasteButton = createToolbarButtonJar("paste.gif","Paste");
        PasteButtonListener = new PasteGraphButtonAction(null);
        PasteButton.addActionListener(PasteButtonListener); 	
		
		UndoButton = createToolbarButtonJar("undo.gif","Undo");
        UndoButtonListener = new UndoGraphButtonAction(null);
    	UndoButton.addActionListener(UndoButtonListener); 		
		
		RedoButton = createToolbarButtonJar("redo.gif","Redo");
        RedoButtonListener = new RedoGraphButtonAction(null);
    	RedoButton.addActionListener(RedoButtonListener); 	
		
		DelButton = createToolbarButtonJar("del.gif","Delete");
		DelButtonListener = new DelGraphButtonAction(null);
		DelButton.addActionListener(DelButtonListener);
        
		updateRifGraphWindow(rifGraphWindow);
        
        add(CutButton);
		add(CopyButton);
		add(PasteButton);
		add(DelButton);        
        add(UndoButton);
        add(RedoButton);
		
		
		
		TavolaPulsanti = new Hashtable();
		TavolaPulsanti.put("Cut",CutButton);
		TavolaPulsanti.put("Copy",CopyButton); 
		TavolaPulsanti.put("Paste",PasteButton); 
		TavolaPulsanti.put("Undo",UndoButton); 
		TavolaPulsanti.put("Redo",RedoButton); 
		TavolaPulsanti.put("Del",DelButton);
        
    }

    /**
	 * abilita  disabilita i pulsanti
	 * @param enable
	 */
	private void enableAll(boolean enable){
		CutButton.setEnabled(enable);
		CopyButton.setEnabled(enable);
		PasteButton.setEnabled(enable);
		UndoButton.setEnabled(enable);
		RedoButton.setEnabled(enable);
		DelButton.setEnabled(enable);
	}

	/** Permette di modificare l'oggetto sul quale agiscono
		i pulsanti della toolbar. */
    public void updateRifGraphWindow(IHostEditor rifGraphWindow)
    {
    	
    	/*CutButtonListener.updateRifGraphWindow(rifGraphWindow);
    	CopyButtonListener.updateRifGraphWindow(rifGraphWindow);
    	PasteButtonListener.updateRifGraphWindow(rifGraphWindow);
    	UndoButtonListener.updateRifGraphWindow(rifGraphWindow);
    	RedoButtonListener.updateRifGraphWindow(rifGraphWindow);  
    	DelButtonListener.updateRifGraphWindow(rifGraphWindow);*/
    	if(rifGraphWindow == null){
    		enableAll(false);
    	}
    	else{
    		enableAll(true);
    	}
    }
    
}
