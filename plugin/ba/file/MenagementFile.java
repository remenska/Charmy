package plugin.ba.file;

import org.xml.sax.Attributes;

import plugin.ba.BAWindow;
import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.plugin.file.SerializableCharmyFile;
import core.internal.plugin.file.xschema.SchemaEntry;
import core.internal.plugin.file.xschema.SimpleValue;

public class MenagementFile implements SerializableCharmyFile{

	private BAWindow plug;
	
	public MenagementFile() {
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
		// TODO Auto-generated method stub
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
		plug.resetForNewFile();
	}

	public void setPlugin(IMainTabPanel plugin) {
		this.plug=(BAWindow)plugin;
	}

	public void startElement(String elementName, Attributes attributes) {
		// TODO Auto-generated method stub
		
	}

}
