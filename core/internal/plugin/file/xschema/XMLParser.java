package core.internal.plugin.file.xschema;



import java.util.Stack;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import com.sun.org.apache.xerces.internal.impl.xs.XSElementDecl;
import com.sun.org.apache.xerces.internal.xs.XSAttributeUse;
import com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;

import core.internal.plugin.file.SerializableCharmyFile;
import core.internal.runtime.data.PlugDataManager;


public class XMLParser extends DefaultHandler {
	
	private PlugDataManager plugData;
	
	private Stack schemaElementStack = new Stack();
	
	private SchemaEntry schemaEntryRoot;
	
	//private SchemaEntry currentEntrySchema = null;
	
	public XMLParser(PlugDataManager plugData,SchemaEntry schemaEntryRoot) {
		
		this.plugData = plugData;
		
		this.schemaEntryRoot=schemaEntryRoot;
	}
	
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}
	
	public void endElement(String namespaceURI, String sName,
			// simple name
			String qName // qualified name
	) throws SAXException {
		
		SchemaEntry currentSchemaEntry = (SchemaEntry)schemaElementStack.peek();
		if (currentSchemaEntry!=null){
			if (currentSchemaEntry.getElementName().compareTo(qName)==0){
				
				this.delegateEndElement(currentSchemaEntry);
				this.schemaElementStack.pop();
				
			}
			
		}
		
	}
	
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}
	
	public void startElement(String namespaceURI, String lName, // local name
			String qName, // qualified name
			Attributes attrs) throws SAXException {
		
		String eName = lName; // element name
		if ("".equals(eName)) 
			eName = qName; // namespaceAware = false
		
		// Gestisco gli attributi (Se ce ne sono)
		if (attrs != null) {
			for (int i = 0; i < attrs.getLength(); i++) {
				String aName = attrs.getLocalName(i); // Attr name
				if ("".equals(aName))
					aName = attrs.getQName(i);
			}
		}
		
		
		////memorizare il livello dalla radice, albero binario
		////anche su end element.
		///se a quel livello nell'xschema c'è un tag con lo stesso nome allora abbiamo trovato la entry nello schema.
		
		//se l'elemento corrente è uguale a quello definito di root (unico) allora siamo al livello 0 - )
		if (this.schemaEntryRoot.getElementName().compareTo(eName)==0){
			this.schemaElementStack.push(schemaEntryRoot);
			this.delegateStartElement(schemaEntryRoot,attrs);
			return;
		}
		
		//test
		/*if (eName.compareTo("Parametro")==0){
			int y=0;
		}
		if (eName.compareTo("Guard")==0){
			int y=0;
		}	
		
		if (eName.compareTo("Operation")==0){
			int y=0;
		}	*/
		
		SchemaEntry currentSchemaEntry = (SchemaEntry)schemaElementStack.peek();
		if (currentSchemaEntry!=null){
			
	
			if ((currentSchemaEntry.isList())&& (currentSchemaEntry.getElementName().compareTo(eName)==0)){
				
				this.delegateStartElement(currentSchemaEntry,attrs);
				this.schemaElementStack.push(currentSchemaEntry);
				return;
			}
			
			SchemaEntry[]  child = currentSchemaEntry.getChild();
			
			if(child!=null)
				for (int i = 0; i < child.length; i++) {
					
					if (child[i].getElementName().compareTo(eName)==0){
						
						this.delegateStartElement(child[i],attrs);
						
						/*if (child[i].isTypeDefinition()){
							
							int y=0;
						}*/
						
						this.schemaElementStack.push(child[i]);						
						return;
					}
				}
						
		}				
		
	}
	
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		
		//soluzione alla buona, forse possibili bug...		
		if (arg0[0]!='\n'){
			
			SchemaEntry currentSchemaEntry = (SchemaEntry)schemaElementStack.peek();
			if (currentSchemaEntry!=null){
				
				currentSchemaEntry.getPlugin().characters(arg0,arg1,arg2);
			}
			
		}
		
	}

	private void delegateStartElement(SchemaEntry schemaEntry,Attributes attrs){
		
		
		String elementName = schemaEntry.getElementName();
		AttributesImpl attributes = new AttributesImpl();
		
		XSTypeDefinition xsType = ((XSElementDecl)schemaEntry.getElementSource()).fType;
		if (xsType.getTypeCategory()==XSTypeDefinition.COMPLEX_TYPE){
			
			XSComplexTypeDefinition complexType= (XSComplexTypeDefinition)xsType;
			XSObjectList attributeUses = complexType.getAttributeUses();
			
			for (int i = 0; i < attributeUses.getLength(); i++) { 
				
				XSAttributeUse xsAttributeUse=(XSAttributeUse)attributeUses.item(i);
				String currentAttributeSchema = xsAttributeUse.getAttrDeclaration().getName();
				
				if (attrs != null) {
					for (int j = 0; j < attrs.getLength(); j++) {
						String attrLocalName = attrs.getLocalName(j); // Attr name
						if ("".equals(attrLocalName))
							attrLocalName = attrs.getQName(j);
						
						if (attrLocalName.compareTo(currentAttributeSchema)==0){
							
							attributes.addAttribute(attrs.getURI(j),attrs.getLocalName(j),attrs.getQName(j),attrs.getType(j),attrs.getValue(j));
							break;
						}
						
					}
					
				}
			}
		}
				
		//if (attributes.getLength()>0){
			
			schemaEntry.getPlugin().startElement(elementName,attributes);
		//}
		
		attributes.clear();
		
		Vector infoExtensionAttributes =  schemaEntry.getInfoExtensionAttribute();
		if (infoExtensionAttributes!=null)
			for (int i = 0; i < infoExtensionAttributes.size(); i++) {
				
				SerializableCharmyFile plugin = (SerializableCharmyFile)((Vector)infoExtensionAttributes.get(i)).get(0);
				XSObjectList attributeUses = (XSObjectList)((Vector)infoExtensionAttributes.get(i)).get(1);
				
				for (int j = 0; j < attributeUses.getLength(); j++) { 
					
					XSAttributeUse xsAttributeUse=(XSAttributeUse)attributeUses.item(j);
					String currentAttributeSchema = xsAttributeUse.getAttrDeclaration().getName();
					
					if (attrs != null) {
						for (int z = 0; z < attrs.getLength(); z++) {
							String attrLocalName = attrs.getLocalName(z); // Attr name
							if ("".equals(attrLocalName))
								attrLocalName = attrs.getQName(z);
							
							if (attrLocalName.compareTo(currentAttributeSchema)==0){
								
								attributes.addAttribute(attrs.getURI(z),attrs.getLocalName(z),attrs.getQName(z),attrs.getType(z),attrs.getValue(z));
								break;
							}
							
						}
						
					}
				}
				
			//	if (attributes.getLength()>0){					
					plugin.startElement(elementName,attributes);
					attributes.clear();
				//}				
				
			}
	}
	
	private void delegateEndElement(SchemaEntry schemaEntry){
		
		schemaEntry.getPlugin().endElement(schemaEntry.getElementName());
		
		Vector infoExtensionAttributes =  schemaEntry.getInfoExtensionAttribute();
		if (infoExtensionAttributes!=null)
			for (int i = 0; i < infoExtensionAttributes.size(); i++) {
				
				SerializableCharmyFile plugin = (SerializableCharmyFile)((Vector)infoExtensionAttributes.get(i)).get(0);
				plugin.endElement(schemaEntry.getElementName());
				
			}
	}

	public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
		// TODO Auto-generated method stub
		super.ignorableWhitespace(arg0, arg1, arg2);
	}
}
