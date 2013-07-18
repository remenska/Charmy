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

package core.internal.plugin;

import java.io.File;
import java.util.Stack;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import core.internal.datistatici.IModel;
import core.internal.extensionpoint.ExtensionPointDescriptor;
import core.internal.extensionpoint.HostDescriptor;
import core.internal.plugin.editoralgo.PlugEditor;
import core.internal.plugin.file.PlugFile;


/**
 * piccolo parser per plugin.xml relativo ad ogni plug
 * 
 * @author michele 
 */

public class XMLParsePlug extends DefaultHandler implements IModel {

	private InternalError internalError;
	
	private PluginRegistry pluginRegistry;
	
	private boolean fatalErrorOccured = false;

	private Stack elementStack = new Stack();

	/**
	 * PlugInDescriptor ricavato dalla lettura del files
	 * xml
	 */
	private PlugInDescriptor plugInDescriptor;
	private ExtensionPointDescriptor currentExtensionPoint = null;
	private HostDescriptor currentHostComponent = null;

	//Locator locator = null;

	boolean runtime = false;
	boolean editor = false;
	boolean boolfile = false;
	private PlugEditor tmpEditor;
	private PlugFile tmpFile;
	boolean extension = false;
	boolean extensionGeneric = false;
	boolean extensionpoint = false;
	boolean extensionpointRequires = false;
	boolean parsingHost = false;
	boolean xSchemaFile = false;
	/**
	 * id dell'extension-point corrente su cui ci agganciamo dal tag extension - verrà riportato a null alla chiusura del tag extension
	 */
	String currentExtension = null;
	boolean pluginRequires = false;

	private File file = null;

	public XMLParsePlug(InternalError internalError,PluginRegistry pluginRegistry) {

		super();
		this.pluginRegistry=pluginRegistry;
		this.internalError = internalError;
	}

	public PlugInDescriptor parseFile(File file) {
		// Use an instance of ourselves as the SAX event handler
		this.file = file;
		// Use the default (non-validating) parser
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			// Set up output stream

			// Parse the input
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(file, this);
			if (this.fatalErrorOccured)
				return null;
			else
				return plugInDescriptor;

		} catch (Throwable t) {
			//t.printStackTrace();
			return null;
		}
	}

	//static private Writer  out;

	//===========================================================
	// SAX DocumentHandler methods
	//===========================================================

	// Questa routine viene chiamata quando si identifica l'inizio del documento
	// la definizione dello schema
	public void startDocument() throws SAXException {
		plugInDescriptor = new PlugInDescriptor(file,this.pluginRegistry);

		//this.objectStack.push(plugInDescriptor);
	}

	public void endDocument() throws SAXException {
	}

	/** 
	 * prelevo tutti gli elementi iniziali quindi mi definisco un case che
	 * a seconda dell'elemento mi fa l'azione corrispondente
	 * In definitiva altero lo stato delle mie strutture per
	 * permettere la lettura dei contenuti characters !
	 */
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

		//fase1: In questa fase mi riconosco i tag degli elementi 
		//fase 2: (con i boolean)-successivamente agisco di conseguenza alterando l'ambiente di lavoro

		/*
		 * plugin
		 */
		if (eName.equals(PLUGIN)) {
			//controllo gli attributi - devono esistere tutti altrimenti il descriptor non viene inserito nel registro dei plug

			String a = attrs.getValue(PLUGIN_ID);
			String b = attrs.getValue(PLUGIN_NAME);
			String c = attrs.getValue(PLUGIN_VENDOR);
			String d = attrs.getValue(PLUGIN_VERSION);
			String e = attrs.getValue(PLUGIN_CLASS);

			if ((a != null) && (d != null)) {

				plugInDescriptor.setIdentifier(a);
				plugInDescriptor.setName(b);
				plugInDescriptor.setVendor(c);
				plugInDescriptor.setVersion(d);
				plugInDescriptor.setClassStartup(e);
			} else {
				this.internalError.addError(
					Error.FATAL_ERROR,
					a,d,
					"principal attribute in plugin "
						+ file.getPath()
						+ " missed",
					null);
				this.fatalErrorOccured = true;
			}

		}

		/*
		 * inizio
		 */
		if (eName.equals(RUNTIME)) {
			runtime = true;
		}

		/*
		 * runtime attivo
		 */
		if (runtime) {
			if (eName.equals(LIBRARY)) {

				String libraryName = attrs.getValue(LIBRARY_NAME);
				if (libraryName != null)
					////
					plugInDescriptor.getLibrary().add(libraryName);
				else {
					this.internalError.addError(
						Error.FATAL_ERROR,
						plugInDescriptor.getIdentifier(),plugInDescriptor.getVersion(),
						"Library (.jar) of plugin "
							+ file.getPath()
							+ " missed",
						null);
					this.fatalErrorOccured = true;
				}
			}
		}

		/*
		 * ezio: gestione dei plugin required
		 */

		if (eName.equals(PLUGIN_REQUIRES)) {

			pluginRequires = true;

		}

		if (pluginRequires) {
			if (eName.equals(PLUGIN_REQUIRES_IMPORT)) {

				// attributo  plugin tag import necessario, pena il descriptor non viene inserito nel registro.
				//				attributo versione opzionale, il sistema considera l'ultima versione
				String pluginRequireId = attrs.getValue(PLUGIN_REQUIRES_PLUGIN);
				String pluginRequireVersion =attrs.getValue(PLUGIN_REQUIRES_PLUGIN_VERSION);
				if ((pluginRequireId != null)&&(pluginRequireVersion!=null))
					plugInDescriptor.addPluginRequired(
							pluginRequireId,pluginRequireVersion
						);
				else {
					this.internalError.addError(
						Error.FATAL_ERROR,
						plugInDescriptor.getIdentifier(),plugInDescriptor.getVersion(),
						"attribute plugin missed in element import, in  manifest file "
							+ file.getPath(),
						null);
					this.fatalErrorOccured = true;
				}
			}

		}

		/*
		 * estensioni varie
		 */
		if (eName.equals(EXTENSION)) {

			if (attrs.getValue(EXTENSION_TARGET) != null) {

				extension = true;
			}

		}

		/*
		 * extension-point
		 */
		if (eName.equals(EXTENSION_POINT)) {
			//controllo gli attributi indispensabili
			if ((attrs.getValue(EXTENSION_POINT_ID) != null)
				&& (attrs.getValue(EXTENSION_POINT_NAME) != null)
				&& (attrs.getValue(EXTENSION_POINT_CLASS) != null)
				&& (attrs.getValue(EXTENSION_POINT_MULTIPLICITY) != null)) {

				this.currentExtensionPoint = new ExtensionPointDescriptor();
				currentExtensionPoint.setIdentifier(attrs.getValue(EXTENSION_POINT_ID));
				currentExtensionPoint.setName(
					attrs.getValue(EXTENSION_POINT_NAME));
				currentExtensionPoint.setClassPath(
					attrs.getValue(EXTENSION_POINT_CLASS));
				currentExtensionPoint.setMultiplicity(
					attrs.getValue(EXTENSION_POINT_MULTIPLICITY));

				extensionpoint = true;
			}

		}

		/*
		 * caso in cui si sta parsando il tag extension-point
		 * Le aggiunte di tag di sistema relative alle dichiarazioni sugli ext-Point vanno gestite qui 
		 */
		if (extensionpoint) {

			if (eName.equals(EXTENSIONPOINT_DEPENDENCES)) {
				extensionpointRequires = true;
			}
		}

		if (extensionpointRequires) {
			if (eName.equals(EXTENSIONPOINT_DEPENDENCY)) {

				if (attrs.getValue(EXTENSIONPOINT_ID) != null)
					this.currentExtensionPoint.addExtensionPointRequired(
						attrs.getValue(EXTENSIONPOINT_ID));

			}
		}

		/*
		 *  caso di un generico extension point
		 */
		if ((extension) && (!editor) && (!boolfile) && (!extensionGeneric)) {

			if (attrs
				.getValue(EXTENSION_TARGET)
				.equals(EXTENSION_TARGET_FILE)) {
				tmpFile = new PlugFile();
				tmpFile.setPluginDescriptor(this.plugInDescriptor);
				boolfile = true;
			} else if (
				attrs.getValue(EXTENSION_TARGET).equals(
					EXTENSION_TARGET_EDIT)) {
				tmpEditor = new PlugEditor();
				tmpEditor.setPluginDescriptor(plugInDescriptor);
				editor = true;
			} else {
				extensionGeneric = true;
				currentExtension = attrs.getValue(EXTENSION_TARGET);
				// id dell'extension-point su cui ci agganciamoverrà - azzerato alla chiusura del tag extension
			}
		}

		
		if (editor) { //plugin editor , aggancio al sistema in modalità base editor
			if (eName.equals(PAGE)) {

				tmpEditor.setName(attrs.getValue(PAGE_NAME));
				tmpEditor.setEntryPoint(attrs.getValue(PAGE_CLASS));
				tmpEditor.setId(attrs.getValue(PAGE_ID));
				plugInDescriptor.setEditor(tmpEditor);
			}
			
						
			if (eName.equals(FILE)) {				
				this.plugInDescriptor.setIdPlugFileRequest(attrs.getValue(PLUGIN));
				//this.plugInDescriptor.setXSchemaFile(attrs.getValue(XSCHEMA));
				this.xSchemaFile=true;
			}
		}
		
		if (this.xSchemaFile){
			
			if (eName.equals(XSCHEMA)) {
				this.plugInDescriptor.addXSchemaInfo(attrs.getValue(ID_REF_XMLMODEL),attrs.getValue(XSCHEMA_FILE),attrs.getValue(CLASS_MENAGEMENT_FILE));
			}
			
		}

		if (boolfile) { //plugin file - aggancio al sistema in modalità plug file. Plug di salvataggio-apertura file charmy
			if (eName.equals(OBJCONT)) {
				tmpFile.setId(attrs.getValue(OBJCONT_ID));
				tmpFile.setObjectClass(attrs.getValue(OBJCONT_CLASS));
				tmpFile.setNameFilter(attrs.getValue(OBJCONT_FILTER));
			}

			if (eName.equals(ACTION)) {
				tmpFile.setLabel(attrs.getValue(ACTION_LABEL));
				tmpFile.setClassFile(attrs.getValue(ACTION_CLASSPLUG));
				//tmpFile.setClassOpen(attrs.getValue(ACTION_OPEN));
				plugInDescriptor.setPlugFile(tmpFile);
				plugInDescriptor.setTagRootXMLFile(attrs.getValue(TAG_ROOT_XML));
			}
			
			if (eName.equals(XML_MODEL)) {				
				plugInDescriptor.addXmlModel(attrs.getValue(ID_XMLMODEL),attrs.getValue(NAME_XMLMODEL));
			}

		}

		/////
		//registriamo tutti i sub elementi a partire dal tag <host>
		// ad eccezzione dell'elemento host-required che è un tag di sistema
		if (this.parsingHost & !eName.equals(HOST_REQUIRED)) {

			///////SPOSTARE IN UN METODO

			ConfigurationElement currentElement = new ConfigurationElement();
			currentElement.setName(eName);

			this.parseElementAttributes(currentElement, attrs);

			//ConfigurationElement precElement = (ConfigurationElement) this.elementStack.peek();

			if (elementStack.empty()) {
				currentElement.setParent(this.currentHostComponent);
				this.currentHostComponent.addSubElement(currentElement);
			} else {
				ConfigurationElement precElement =
					(ConfigurationElement) this.elementStack.peek();
				currentElement.setParent(precElement);
				precElement.addChildren(currentElement);

			}

			this.elementStack.push(currentElement);

		}

		/////////////////////////

		//stiamo parsando il tag extension, e ci troviamo nel caso generico 
		//di aggancio di una o più componenti host o extender
		if (extensionGeneric) {

			if (eName.equals(HOST_COMPONENT) & (!this.parsingHost)) {

				//		controllo gli attributi indispensabili dell'host
				if ((attrs.getValue(EXTENSION_ID) != null)
					&& (attrs.getValue(EXTENSION_NAME) != null)
					&& (attrs.getValue(EXTENSION_CLASS) != null)) {

					this.currentHostComponent = new HostDescriptor();
					currentHostComponent.setId(attrs.getValue(EXTENSION_ID));
					this.currentHostComponent.setName(
						attrs.getValue(EXTENSION_NAME));
					this.currentHostComponent.setClassPath(
						attrs.getValue(EXTENSION_CLASS));
					this.currentHostComponent.setExtensionPoint(
						currentExtension);
					this.currentHostComponent.setParent(this.plugInDescriptor);

					this.plugInDescriptor.addHost(currentHostComponent);

					parsingHost = true;

				}

			}

			if (eName.equals(HOST_REQUIRED)) {
				if (attrs.getValue(EXTENSION_ID) != null) {

					this.currentHostComponent.addHostRequired(
						attrs.getValue(EXTENSION_ID));
				}
			}

			///>>>>>EXTENDER NON ANCORA GESTITI DAL SISTEMA		    
			if (eName.equals(EXTENDER_COMPONENT)) {
				//controllo gli attributi indispensabili dell'extender component
				if ((attrs.getValue(EXTENSION_ID) != null)
					|| (attrs.getValue(EXTENSION_NAME) != null)
					|| (attrs.getValue(EXTENSION_CLASS) != null)) {

					/*plugInDescriptor.addExtenderComponent(attrs.getValue(EXTENSION_ID),
					        attrs.getValue(EXTENSION_NAME),
					        attrs.getValue(EXTENSION_CLASS),currentExtension);*/

				}

			}

			//PARSIAMO GLI ELEMENI GENERICI DI UN HOST

		}
	}

	/**
	 * tag di chiusura
	 */
	public void endElement(String namespaceURI, String sName,
	// simple name
	String qName // qualified name
	) throws SAXException {

		if (qName.equals(PLUGIN_REQUIRES)) {

			pluginRequires = false;

		}

		if (qName.equals(RUNTIME)) {
			runtime = false;
		}

		if (qName.equals(EXTENSION)) {
			editor = false;
			boolfile = false;
			extensionGeneric = false;
			currentExtension = null;
			extension = false;
		}

		if (qName.equals(EXTENSION_POINT)) {

			if (this.currentExtensionPoint != null) {

				this.plugInDescriptor.addExtensionPoint(
					this.currentExtensionPoint);
				this.currentExtensionPoint = null;
			}

			extensionpoint = false;

		}

		if (qName.equals(EXTENSIONPOINT_DEPENDENCES)) {
			extensionpointRequires = false;

		}

		if (qName.equals(HOST_COMPONENT)) {
			parsingHost = false;
			this.currentHostComponent = null;
		}

		if (this.parsingHost & !qName.equals(HOST_REQUIRED)) {

			this.elementStack.pop();

		}
		
		if (qName.equals(FILE)) {
			this.xSchemaFile=false;
		}

	}

	/*
	 *  (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char buf[], int offset, int len) {
	}

	public void caller() {
	}

	/**
	 * preleva il plugingenerato - >> deprecated <<<
	 * @return pluginDescriptor generato
	 */
	public PlugInDescriptor getPlugInDescriptor() {
		return plugInDescriptor;
	}

	public void handleExtensionPoint(
		String elementName,
		Attributes attributes) {

		this.currentExtensionPoint = new ExtensionPointDescriptor();

	}

	public void parseElementAttributes(
		ConfigurationElement element,
		Attributes attributes) {

		Vector propVector = null;

		int len = (attributes != null) ? attributes.getLength() : 0;
		if (len == 0)
			return;

		for (int i = 0; i < len; i++) {

			String attrName = attributes.getQName(i);
			String attrValue = attributes.getValue(i);

			ConfigurationProperty currentConfigurationProperty =
				new ConfigurationProperty();
			currentConfigurationProperty.setName(attrName);
			currentConfigurationProperty.setValue(attrValue);

			element.addConfigurationProperty(currentConfigurationProperty);
		}
	}

	/* (non-Javadoc)
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	/*public void setDocumentLocator(Locator arg0) {
		
		this.locator = arg0;
	}*/
}
