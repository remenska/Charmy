package core.internal.plugin.file;

import org.xml.sax.Attributes;

import core.internal.plugin.editoralgo.IMainTabPanel;
import core.internal.plugin.file.xschema.SchemaEntry;
import core.internal.plugin.file.xschema.SimpleValue;

public interface SerializableCharmyFile {

	////METODI PER IL SALVATAGGIO : SI INTERROGA IL PLUGIN
	public SimpleValue getAttributeValue(SchemaEntry schemaEntry,String attributeName);
	
	public Object getObject(SchemaEntry schemaEntry);
	
	public Object[] getList (SchemaEntry schemaEntry);
	
	public void setPlugin(IMainTabPanel plugin);
	
	/**
	 * se un elemento dell'xschema è definito come un tipo specifico, questo metodo deve restituire l'istanza del tipo
	 * es: il contenuto di una sezione CDATA se l'elemento è di tipo string 
	 * @param schemaEntry
	 * @return
	 */
	public Object getElement(SchemaEntry schemaEntry);
	
	/////METODI PER L'APERTURA : MANO A MANO CHE IL PARSER DI SISTEMA PARSA L'XML VENGONO PASSATE LE INFORMAZIONI
	
	//public void setAttributesValue(String elementName, Attributes attributes);
	
	public void startElement(String elementName, Attributes attributes);
	
	public void endElement(String elementName);
	
	public void characters(char buf[], int offset, int len); 
	
	public void resetForNewFile();
}
