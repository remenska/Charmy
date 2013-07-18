package plugin.EsempioPlugin1.file;

import org.xml.sax.Attributes;

import plugin.EsempioPlugin1.EsempioPlugin1;
import plugin.EsempioPlugin1.data.PlugData;
import plugin.topologydiagram.file.Tag;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.plugin.file.SerializableCharmyFile;
import core.internal.plugin.file.xschema.SchemaEntry;
import core.internal.plugin.file.xschema.SimpleValue;


public class MenagementFileDati implements  SerializableCharmyFile{

	
	private PlugData plugData;
	private EsempioPlugin1 plug;
	
	public MenagementFileDati() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void characters(char[] buf, int offset, int len) {
		// TODO Auto-generated method stub
		
	}

	public void endElement(String elementName) {
		// TODO Auto-generated method stub
		
	}

	public SimpleValue getAttributeValue(SchemaEntry schemaEntry, String attributeName) {
		
		String elementName=schemaEntry.getElementName();
		Object instanceElement = schemaEntry.getInstanceElement();
		
		
//		semantica 
		if ((elementName.compareTo(Tag.listaProcesso)==0)&& (attributeName.compareTo("attributoEsempio")==0))
		  return new SimpleValue(plugData.getInformazione1());
		
		
		///nuovo formato di esempio
		if ((elementName.compareTo("dato1")==0)&& (attributeName.compareTo("valore")==0))
			  return new SimpleValue(plugData.getInformazione2());
			
		return null;
	}

	public Object getElement(SchemaEntry schemaEntry) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object[] getList(SchemaEntry schemaEntry) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getObject(SchemaEntry schemaEntry) {
		// TODO Auto-generated method stub
		return null;
	}

	public void resetForNewFile() {
		this.plug.resetForNewFile();
	}

	
	public void startElement(String elementName, Attributes attributes) {
		
		
		
		
	}

	public void setPlugin(IMainTabPanel plugin) {
		this.plugData = (PlugData)plugin.getPlugData();
		
		this.plug=(EsempioPlugin1)plugin;
	}

}
