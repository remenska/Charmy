/*
 * Created on 22-ott-2004
 */
package plugin.TeStor.interfaccia;

import javax.swing.AbstractButton;

import core.internal.ui.resources.TypeToolBar;
import plugin.TeStor.*;
import core.internal.ui.action.toolbar.zoom.ZoomInXGraphButtonAction;
import core.internal.ui.action.toolbar.zoom.ZoomInYGraphButtonAction;
import core.internal.ui.action.toolbar.zoom.ZoomOutXGraphButtonAction;
import core.internal.ui.action.toolbar.zoom.ZoomOutYGraphButtonAction;
//import action.zoom.ZoomResetGraphButtonAction;
import plugin.sequencediagram.SequenceEditor;
import core.internal.plugin.editoralgo.IMainTabPanel;
/**
 * La barra di zoom inclusa nel TeStor.
 * La classe replica la ZoomToolBar fornita ma modificandola in modo da adattarla
 * alle esigenze del TeStor: gli oggetti su cui agisce sono SequenceEditor
 * piuttosto che WithGraphWindow
 * 
 * @author Fabrizio Facchini
 * @see toolbar.ZoomToolBar
 */
public class BarraZoom extends TypeToolBar{
	
	// i pulsanti di zoom...
	private AbstractButton ZoomInXButton;
	private ZoomInXGraphButtonAction ZoomInXButtonListener;
    
	private AbstractButton ZoomOutXButton;
	private ZoomOutXGraphButtonAction ZoomOutXButtonListener;
    
	private AbstractButton ZoomInYButton;
	private ZoomInYGraphButtonAction ZoomInYButtonListener;
    
	private AbstractButton ZoomOutYButton;
	private ZoomOutYGraphButtonAction ZoomOutYButtonListener;
    
	//private AbstractButton ZoomResetButton;
	//private ZoomResetGraphButtonAction ZoomResetButtonListener;


	/** Costruisce un nuovo oggetto BarraZoom sul SD specificato
	 * 
	 * @param se il SequenceEditor che visualizza il SD su cui deve agire la barra di zoom
	 * @see toolbar.ZoomToolBar#ZoomToolBar(WithGraphWindow)
	 */
	public BarraZoom(SequenceEditor se,IMainTabPanel plugin)
	{
		super("Barra di Zoom",plugin);
		Config.init();
    	
    	// crea i pulsanti di zoom e i rispettivi listener..
		ZoomInXButton = createToolbarButtonJar(Config.PathGif+Config.strHorizIMG,Config.strHoriz);
		ZoomInXButtonListener = new ZoomInXGraphButtonAction(se);
		ZoomInXButton.addActionListener(ZoomInXButtonListener);
        
		ZoomOutXButton = createToolbarButtonJar(Config.PathGif+Config.compHorizIMG,Config.compHoriz);
		ZoomOutXButtonListener = new ZoomOutXGraphButtonAction(se);
		ZoomOutXButton.addActionListener(ZoomOutXButtonListener);
        
		ZoomInYButton = createToolbarButtonJar(Config.PathGif+Config.strVertIMG,Config.strVert);
		ZoomInYButtonListener = new ZoomInYGraphButtonAction(se);
		ZoomInYButton.addActionListener(ZoomInYButtonListener);
        
		ZoomOutYButton = createToolbarButtonJar(Config.PathGif+Config.compVertIMG,Config.compVert);
		ZoomOutYButtonListener = new ZoomOutYGraphButtonAction(se);
		ZoomOutYButton.addActionListener(ZoomOutYButtonListener);
        
		/*ZoomResetButton = createToolbarButton("zoomreset.gif","Zoom Reset");
		ZoomResetButtonListener = new ZoomResetGraphButtonAction(se);
		ZoomResetButton.addActionListener(ZoomResetButtonListener);*/
        
        // li aggiunge alla barra...
		add(ZoomInXButton);
		add(ZoomOutXButton);
		add(ZoomInYButton);
		add(ZoomOutYButton);
		/*add(ZoomResetButton);*/
	}
    

	/**
	 * Cambia il SD sul quale deve agire la barra di zoom
	 * 
	 * @param se il nuovo SequenceEditor che visualizza il SD
	 * @see toolbar.ZoomToolBar#updateRifGraphWindow(ZoomGraphInterface)
	 */    
	public void aggiornaRifSeqEd(SequenceEditor se){
		// aggiorna i riferimenti al nuovo SequenceEditor...
		ZoomInXButtonListener.updateRifGraphWindow(se);
		ZoomOutXButtonListener.updateRifGraphWindow(se);
		ZoomInYButtonListener.updateRifGraphWindow(se);
		ZoomOutYButtonListener.updateRifGraphWindow(se);
		//ZoomResetButtonListener.updateRifGraphWindow(se);    	    	    	    	
	}
	
	/**
	 * Abilita/disabilita contemporaneamente tutti i pulsanti della barra di zoom
	 * 
	 * @param en se <code>true</code> abilita, altrimenti disabilita i pulsanti
	 * @see toolbar.ZoomToolBar#setEnabledButtons(boolean)
	 */
	public void setEnabledButtons(boolean en){

		ZoomInXButton.setEnabled(en);
		ZoomOutXButton.setEnabled(en);
		ZoomInYButton.setEnabled(en);
		ZoomOutYButton.setEnabled(en);
		//ZoomResetButton.setEnabled(en);
	}

}
