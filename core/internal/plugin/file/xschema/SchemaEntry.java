package core.internal.plugin.file.xschema;

import java.util.Vector;

import com.sun.org.apache.xerces.internal.impl.xs.XSParticleDecl;
import com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.XSElementDeclaration;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;

import core.internal.plugin.file.SerializableCharmyFile;

public class SchemaEntry {

	private XSElementDeclaration elementSource=null;
	private SchemaEntry[] child = null;
	private SchemaEntry parent = null;
	private Object instanceElement=null;
	
	private SerializableCharmyFile plugin;
	
	private Vector infoExtensionAttribute = null;
	
	
	
	public SchemaEntry(XSElementDeclaration elementSource) {


		this.elementSource=elementSource;
			
	}

	public SchemaEntry[] getChild() {
		return child;
	}

	public void setChild(SchemaEntry[] child) {
		this.child = child;
	}
	
	
	
	public void appendChild(SchemaEntry childElement) {
		
		SchemaEntry[] list;

		if (this.child == null) {
			list = new SchemaEntry[1];
		} else {
			list = new SchemaEntry[this.child.length + 1];
			System.arraycopy(child, 0, list, 0, child.length);
		}

		list[list.length - 1] = childElement;
		this.child = list;
		
	}

	
	public SchemaEntry getParent() {
		return parent;
	}

	public void setParent(SchemaEntry parent) {
		this.parent = parent;
	}

	public XSElementDeclaration getElementSource() {
		return elementSource;
	}

	public SerializableCharmyFile getPlugin() {
		return plugin;
	}

	public void setPlugin(SerializableCharmyFile plugin) {
		this.plugin = plugin;
	}

	public String getElementName(){
		
		return this.getElementSource().getName();
	}	
	
	public String getElementParentName(){
		
		if (this.getParent()!=null)
			return (this.getParent().getElementName());
		
		return  null;
	}
	
	public void setElementSource(XSElementDeclaration elementSource) {
		this.elementSource = elementSource;
	}

	public boolean isList(){
		
		/*if (this.elementSource.getTypeDefinition().getTypeCategory()==XSTypeDefinition.COMPLEX_TYPE){
			
			XSComplexTypeDefinition complexTypeElement = (XSComplexTypeDefinition)elementSource.getTypeDefinition();
			
			if ((complexTypeElement.getContentType()==XSComplexTypeDefinition.CONTENTTYPE_ELEMENT) ||
					(complexTypeElement.getContentType()==XSComplexTypeDefinition.CONTENTTYPE_MIXED)){
				//non sono sicuro se per schema complessi questo controllo funziona.
				return true;
				//XSParticleDecl particella = (XSParticleDecl)complexTypeElement.getParticle();
			}
		}*/
		
		XSComplexTypeDefinition complexType = this.elementSource.getEnclosingCTDefinition();
		
		
		//tipo lista
		if (complexType!=null){
			
			XSParticleDecl particle = (XSParticleDecl)complexType.getParticle();
			
			
			///il codice successivo non funziona, forse per la presenza di errori del sorgente delle API -
			/*if (particle.fMaxOccurs == SchemaSymbols.OCCURRENCE_UNBOUNDED)
				return true;
			
			if (complexType.getParticle().getMaxOccursUnbounded())
				return true;
			if (complexType.getParticle().getMaxOccurs()>1)
				return true;*/
			
			// ottengo le maxoccurs e min occurs dalla stringa di descrizione
			String description = particle.toString();
			if (description.contains("{")){
				
				String minOccurs = description.substring(description.indexOf("{")+1,description.indexOf("-"));
				String maxOccurs = description.substring(description.indexOf("-")+1,description.indexOf("}"));
				if (maxOccurs.compareTo("UNBOUNDED")==0)
					return true;
				Integer max = new Integer(maxOccurs);
				if (max.intValue()>1)
					return true;				
			}			
			
		}
		
		
		
		return false;
	}
	
	
	public boolean isSequence(){
		
//		/tipo record
		/*XSComplexTypeDefinition complexTypeElement = (XSComplexTypeDefinition)this.elementSource.getTypeDefinition();
		
		if ((complexTypeElement.getContentType()==XSComplexTypeDefinition.CONTENTTYPE_ELEMENT) ||
				(complexTypeElement.getContentType()==XSComplexTypeDefinition.CONTENTTYPE_MIXED)){
			
			return true;
		}*/
		
		if ((this.child!=null)&&(this.child.length>=1))
		return true;
		
		return false;
		
	}
	
	public boolean isAnyTypeDefinition(){
		
		
		if(this.elementSource.getTypeDefinition().getBaseType().getName().compareTo("anyType")==0)
			return true;
		return false;
	}
	
	public String getTypeDefinitionName(){				
		
		return this.elementSource.getTypeDefinition().getName();
			
	}
	
	public boolean isTypeDefinition(){
		
		String t=this.elementSource.getTypeDefinition().getBaseType().getName();
		int y=0;
		if(this.elementSource.getTypeDefinition().getBaseType().getName().compareTo("anyType")!=0)
			return true;
		return false;
	}
	
	public Object getInstanceElement() {
		return instanceElement;
	}

	public void setInstanceElement(Object instanceElement) {
		this.instanceElement = instanceElement;
	}
	
	public void addAttributesExtension(SerializableCharmyFile plugin,XSObjectList attributeUses){
		
		if (this.infoExtensionAttribute==null)
			this.infoExtensionAttribute = new Vector();
		
		Vector info = new Vector(2);
		info.add(0,plugin);		
		info.add(1,attributeUses);
		this.infoExtensionAttribute.add(info);
		
	}

	public Vector getInfoExtensionAttribute() {
		return infoExtensionAttribute;
	}
    
}
