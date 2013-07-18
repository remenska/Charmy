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
    

package core.internal.datistatici;



/**
 * 
 * @author michele
 * questa interfaccia fornisce i dati per il parser 
 * del files plug-in.xml per le specifiche plug-in
 * ricavato da eclipse plug-in 
 */
public interface IModel {

	public static final int INDENT = 2;
	public static final int RADIX = 36;

	public static final String TRUE = "true"; //$NON-NLS-1$
	public static final String FALSE = "false"; //$NON-NLS-1$

	public static final String REGISTRY = "plugin-registry"; //$NON-NLS-1$
	public static final String REGISTRY_PATH = "path"; //$NON-NLS-1$


	public static final String PLUGIN = "plugin"; //$NON-NLS-1$
	public static final String PLUGIN_ID = "id"; //$NON-NLS-1$
	public static final String PLUGIN_NAME = "name"; //$NON-NLS-1$
	public static final String PLUGIN_VENDOR = "vendor-name"; //$NON-NLS-1$
//	public static final String PLUGIN_PROVIDER = "provider-name"; //$NON-NLS-1$
	public static final String PLUGIN_VERSION = "version"; //$NON-NLS-1$
	public static final String PLUGIN_CLASS = "class"; //$NON-NLS-1$

	public static final String PLUGIN_KEY_VERSION_SEPARATOR = "_"; //$NON-NLS-1$


	public static final String RUNTIME = "runtime"; //$NON-NLS-1$
	public static final String LIBRARY = "library"; //$NON-NLS-1$
	public static final String LIBRARY_NAME = "name"; //$NON-NLS-1$


	public static final String EXTENSION = "extension"; //$NON-NLS-1$
	public static final String EXTENSION_NAME = "name"; //$NON-NLS-1$
	public static final String EXTENSION_ID = "id"; //$NON-NLS-1$
	public static final String EXTENSION_CLASS = "class"; //$NON-NLS-1$
	public static final String EXTENSION_TARGET = "point"; //$NON-NLS-1$

	public static final String EXTENSION_TARGET_FILE = "file"; //$NON-NLS-1$
	public static final String EXTENSION_TARGET_EDIT = "editor"; //$NON-NLS-1$
	public static final String HOST_COMPONENT = "host"; //$NON-NLS-1$
	public static final String EXTENDER_COMPONENT = "extender"; //$NON-NLS-1$
	public static final String HOST_REQUIRED = "host-required"; //$NON-NLS-1$


	public static final String PAGE = "page"; //$NON-NLS-1$
	public static final String PAGE_NAME = "name"; //$NON-NLS-1$
	public static final String PAGE_ID = "id"; //$NON-NLS-1$
	public static final String PAGE_CLASS = "class"; //$NON-NLS-1$

	public static final String FILE = "file"; //$NON-NLS-1$
	public static final String XSCHEMA = "xschema"; //$NON-NLS-1$
	public static final String CLASS_MENAGEMENT_FILE = "class"; //$NON-NLS-1$
	public static final String ID_REF_XMLMODEL = "idref"; //$NON-NLS-1$
	public static final String XSCHEMA_FILE = "xschema_file"; //$NON-NLS-1$
	public static final String XML_MODEL = "xml_model"; //$NON-NLS-1$
	public static final String ID_XMLMODEL = "id"; //$NON-NLS-1$
	public static final String NAME_XMLMODEL = "name"; //$NON-NLS-1$
	
	
	public static final String OBJCONT = "objectContribution"; //$NON-NLS-1$
	public static final String OBJCONT_ID = "id"; //$NON-NLS-1$		
	public static final String OBJCONT_CLASS = "objectClass"; //$NON-NLS-1$	
	public static final String OBJCONT_FILTER = "nameFilter"; //$NON-NLS-1$

	public static final String ACTION = "action"; //$NON-NLS-1$
	public static final String ACTION_LABEL = "label"; //$NON-NLS-1$
	public static final String ACTION_SAVE = "class_save"; //$NON-NLS-1$
	public static final String ACTION_OPEN = "class_open"; //$NON-NLS-1$
	public static final String ACTION_CLASSPLUG = "class"; //$NON-NLS-1$
	public static final String TAG_ROOT_XML = "tag_root_xml"; //$NON-NLS-1$
	
	
	
	public static final String EXTENSION_POINT = "extension-point"; //$NON-NLS-1$
	public static final String EXTENSION_POINT_NAME = "name"; //$NON-NLS-1$
	public static final String EXTENSION_POINT_ID = "id"; //$NON-NLS-1$
	public static final String EXTENSION_POINT_MULTIPLICITY = "multiplicity"; //$NON-NLS-1$
	public static final String EXTENSION_POINT_SCHEMA = "schema"; //$NON-NLS-1$ // non utilizzato
	//nuovi , da ezio - non uso lo schema
	public static final String EXTENSION_POINT_CLASS = "class"; //$NON-NLS-1$

	public static final String EXTENSIONPOINT_DEPENDENCES = "dependences"; //$NON-NLS-1$
	public static final String EXTENSIONPOINT_DEPENDENCY = "extensionpoint-required"; //$NON-NLS-1$
	public static final String EXTENSIONPOINT_ID = "id"; //$NON-NLS-1$
	

	public static final String ELEMENT = "element"; //$NON-NLS-1$
	public static final String ELEMENT_NAME = "name"; //$NON-NLS-1$
	public static final String ELEMENT_VALUE = "value"; //$NON-NLS-1$

	public static final String PROPERTY = "property"; //$NON-NLS-1$
	public static final String PROPERTY_NAME = "name"; //$NON-NLS-1$
	public static final String PROPERTY_VALUE = "value"; //$NON-NLS-1$
	
	
	public static final String PLUGIN_REQUIRES = "requires"; //$NON-NLS-1$
	public static final String PLUGIN_REQUIRES_PLATFORM = "platform-version"; //$NON-NLS-1$
	public static final String PLUGIN_REQUIRES_PLUGIN = "plugin"; //$NON-NLS-1$
	public static final String PLUGIN_REQUIRES_PLUGIN_VERSION = "version"; //$NON-NLS-1$
	public static final String PLUGIN_REQUIRES_OPTIONAL = "optional"; //$NON-NLS-1$
	public static final String PLUGIN_REQUIRES_IMPORT = "import"; //$NON-NLS-1$
	public static final String PLUGIN_REQUIRES_EXPORT = "export"; //$NON-NLS-1$
	public static final String PLUGIN_REQUIRES_MATCH = "match"; //$NON-NLS-1$
	public static final String PLUGIN_REQUIRES_MATCH_EXACT = "exact"; //$NON-NLS-1$
	public static final String PLUGIN_REQUIRES_MATCH_PERFECT = "perfect"; //$NON-NLS-1$
	public static final String PLUGIN_REQUIRES_MATCH_EQUIVALENT = "equivalent"; //$NON-NLS-1$
	public static final String PLUGIN_REQUIRES_MATCH_COMPATIBLE = "compatible"; //$NON-NLS-1$
	public static final String PLUGIN_REQUIRES_MATCH_GREATER_OR_EQUAL = "greaterOrEqual"; //$NON-NLS-1$
	
}
