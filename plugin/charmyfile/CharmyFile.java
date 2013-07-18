package plugin.charmyfile;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import plugin.charmyfile.info.Informazioni;

import core.internal.plugin.PlugInDescriptor;
import core.internal.plugin.file.IFilePlug;
import core.internal.plugin.file.SerializableCharmyFile;
import core.internal.runtime.data.PlugDataManager;
import core.internal.ui.PlugDataWin;

public class CharmyFile implements IFilePlug{

	PlugDataManager plugDataManager;
	PlugDataWin plugDataWin;
	SerializableCharmyFile[] plugins;
	
	
	public CharmyFile() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void appendInfoFile(String idXmlModel, Document doc, Element elementRoot) throws Exception {
		
	if (idXmlModel.compareTo("charmy.plugin.salvastd.semantica")==0){
			
			elementRoot.appendChild((new Informazioni()).creaInformazioni(doc));
			
		}
		
		if (idXmlModel.compareTo("charmy.plugin.salvastd.grafica")==0){
			
			elementRoot.appendChild((new Informazioni()).creaInformazioni(doc));				
			
		}
		
	}

	public PlugDataManager getPlugData() {
		
		return plugDataManager;
	}

	public PlugDataWin getPlugDataWin() {
		
		return plugDataWin;
	}

	public void resetForNewFile() {
		
		
		
	}

	public void setDati(PlugDataWin plugDtW, PlugDataManager pd) {

		this.plugDataManager=pd;
		this.plugDataWin = plugDtW;
		
	}

	public void setPlugins(SerializableCharmyFile[] plugins) {
		this.plugins=plugins;
		
	}

	

}
