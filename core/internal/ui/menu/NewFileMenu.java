/*
 * Created on 27-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package core.internal.ui.menu;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import core.internal.plugin.PlugInDescriptor;
import core.internal.plugin.file.FileManager;
import core.internal.ui.action.menu.file.NewFileItemAction;
import core.internal.ui.resources.TypeJMenu;

/**
 * @author Ezio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NewFileMenu extends TypeJMenu {

	
	private FileManager fileManager;
	private NewFileItemAction newFileItemAction;
	/**
	 * @param titolo
	 */
	public NewFileMenu(String titolo, FileManager fileManager) {
		super(titolo);
		this.fileManager=fileManager;
		// TODO Auto-generated constructor stub
	}
public void addItem (){
	
}
public void addNewFileItem (PlugInDescriptor plugFileDescriptor){
	
	//newFileItemAction.updateFileManagerWindow(fm);
	newFileItemAction = new NewFileItemAction(this.fileManager,plugFileDescriptor);
	//String titolo = plugFileDescriptor.getName();
	//modificare: prendere per il titolo un campo dall'xml - 
	JMenuItem NewMenuFile2 = createMenuItem(plugFileDescriptor.getPlugFile().getLabel(),new ImageIcon(PathGif + "new.gif"));
    NewMenuFile2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_MASK));     
    NewMenuFile2.addActionListener(newFileItemAction);
	
    add(NewMenuFile2);
	
}
public void updateFileManagerWindow(FileManager fm){
	this.fileManager=fm;
	
}

}
