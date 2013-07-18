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

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import core.internal.plugin.file.FileManager;
import core.internal.ui.action.menu.file.OpenFileItemAction;
import core.internal.ui.action.menu.file.PrintAreaItemAction;
import core.internal.ui.action.menu.file.PrintItemAction;
import core.internal.ui.action.menu.file.QuitItemAction;
import core.internal.ui.action.menu.file.SaveAsFileItemAction;
import core.internal.ui.action.menu.file.SaveFileItemAction;
import core.internal.ui.resources.TypeJMenu;

/** La classe crea il menu File. */
	
public class FileMenu extends TypeJMenu
{
	private FileManager fileManager;

	private NewFileMenu newFileMenu;
	
    /** Item per l'operazione di inizio di una nuova architettura. */
    //private JMenuItem NewMenuFile;

    /** Item per l'operazione di apertura di un'architettura. */    
    private JMenuItem OpenMenuFile;

    /** Item per selezionare l'area di stampa. */
    private JMenuItem PrintAreaMenuFile;
    
    /** Item per l'operazione di salvataggio su file dell'architettura. */    
    private JMenuItem SaveMenuFile;

    /** Item per l'operazione di salvataggio su un nuovo file. */    
    private JMenuItem SaveAsMenuFile;

    /** Item per l'operazione di print. */    
    private JMenuItem PrintMenuFile;

    /** Item per l'operazione di uscita dal programma. */    
    private JMenuItem QuitMenuFile;
    
  //  private NewFileItemAction newFileItemAction;
    
    private OpenFileItemAction openFileItemAction;
	private QuitItemAction quitItemAction;
	private PrintItemAction printItemAction;
	private SaveAsFileItemAction saveAsFileItemAction;
	private SaveFileItemAction saveFileItemAction;
	private PrintAreaItemAction printAreaItemAction; 
	
    /** Costruttore. */
    public FileMenu(FileManager istanzaFM)
    {
		super("File");
		
		this.fileManager = istanzaFM;
		
		//NewMenuFile = createMenuItem("New",new ImageIcon(PathGif + "new.gif"));
		
		this.newFileMenu= new NewFileMenu("New",istanzaFM);
        
        
		openFileItemAction = new OpenFileItemAction(istanzaFM);
        OpenMenuFile = createMenuItem("Open",new ImageIcon(PathGif + "open.gif"));
        OpenMenuFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_MASK));
        OpenMenuFile.addActionListener(openFileItemAction);	


		printAreaItemAction = new PrintAreaItemAction(istanzaFM);
        PrintAreaMenuFile = createMenuItem("Printing Area",new ImageIcon(PathGif + "area.gif"));
        PrintAreaMenuFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,InputEvent.CTRL_MASK));
        PrintAreaMenuFile.addActionListener(printAreaItemAction);
 
 
		saveFileItemAction = new SaveFileItemAction(istanzaFM);
        SaveMenuFile = createMenuItem("Save",new ImageIcon(PathGif + "save.gif"));
        SaveMenuFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK));
        SaveMenuFile.addActionListener(saveFileItemAction);		
 
		saveAsFileItemAction = new SaveAsFileItemAction(istanzaFM);
 
        SaveAsMenuFile = createMenuItem("Save As",new ImageIcon(PathGif + "save.gif"));
        SaveAsMenuFile.addActionListener(saveAsFileItemAction);        
 
		printItemAction = new PrintItemAction(istanzaFM);
        PrintMenuFile = createMenuItem("Print",new ImageIcon(PathGif + "print.gif"));
        PrintMenuFile.addActionListener(printItemAction);		

		quitItemAction = new QuitItemAction(istanzaFM);
		QuitMenuFile = createMenuItem("Quit",new ImageIcon(PathGif + "quit.gif"));
        QuitMenuFile.addActionListener(quitItemAction);		   	
		enableAll(false);

       // add(NewMenuFile);
		add(this.newFileMenu);
		add(OpenMenuFile);
        add(PrintAreaMenuFile);
		add(SaveMenuFile);
		add(SaveAsMenuFile);
		addSeparator();
        add(PrintMenuFile);
        //PrintMenuFile.setEnabled(false);
        addSeparator();
		add(QuitMenuFile);    
    }
    
    
	/**
	 * abilita / disabilita tutti i menu di edit
	 * @param enable true abilita tutti, false disabilita
	 */
	private void enableAll(boolean enable){
		//NewMenuFile.setEnabled(enable);
		OpenMenuFile.setEnabled(enable);
		PrintAreaMenuFile.setEnabled(enable);
		SaveMenuFile.setEnabled(enable);
		SaveAsMenuFile.setEnabled(enable);
		PrintMenuFile.setEnabled(enable);	
		QuitMenuFile.setEnabled(enable);	
	}

	/**
	 * installa un nuovo FileMAnager
	 * @param FileManager di riferimento
	 */
	public void updateRifGraphWindow(FileManager fm)

	{
    	
		newFileMenu.updateFileManagerWindow(fm);
		openFileItemAction.updateFileManagerWindow(fm);
		quitItemAction.updateFileManagerWindow(fm);
		printItemAction.updateFileManagerWindow(fm);
		saveAsFileItemAction.updateFileManagerWindow(fm);
		saveFileItemAction.updateFileManagerWindow(fm);
		printAreaItemAction.updateFileManagerWindow(fm); 
		
		
		if(fm == null){
			enableAll(false);
		}
		else{
			enableAll(true); 
		}    	  	    	    	    	
	}
      
	
	
	/**
	 * @return Returns the newFileMenu.
	 */
	public NewFileMenu getNewFileMenu() {
		return newFileMenu;
	}
}