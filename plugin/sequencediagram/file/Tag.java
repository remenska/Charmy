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
    

package plugin.sequencediagram.file;

/**
 * @author michele
 * dati da utilizzare per la costruzione del file di salvataggio
 *
 */
public interface Tag {

	public static String identificatori = "Identificatori";

	public static String idSequence = "sequence";

	public static String idTime = "time";
	public static String idConstraint = "constraint";
	public static String idSim = "sim";
	public static String idLoop = "loop";
	public static String idParallel = "parallel";

	
/* LISTADS +++++++++++++++++++++++++++++++++++++++++*/
//ListaDS
	public static String listaDS ="ListaDS";
	
	public static String sequenceElement ="SequenceElement";
	public static String att_SEE_nome ="nome";
	public static String att_SEE_id ="id";
	public static String att_SEE_pre_id = "SEE_";
	
	
	//ListaClasse
	public static String listaClasse ="ListaClasse";
	
	//ElementoClasse
	public static String elementoClasse ="ElementoClasse";
	public static String att_ECL_id ="id";	
	public static String att_ECL_pre_id = "ECL_";
	public static String att_ECA_pre_id = "ECA_";
	public static String att_ECL_nome ="nome";
	
	//ListaTime
	public static String listaTime ="ListaTime";
	//ElementoTime
	public static String elementoTime ="ElementoTime";
	public static String att_ETI_time ="time";	
	public static String att_ETI_id ="id";
	public static String att_ETI_pre_id ="ETI_";
	
	
//	ListaConstraint
	public static String listaConstraint ="ListaConstraint";
	//ElementoConstraint
	public static String elementoConstraint ="ElementoConstraint";
	public static String att_ECO_id ="id";	
	public static String att_ECO_pre_id = "ECO_";
	public static String att_ECO_nome ="nome";
	
	
	
	public static String att_ECO_eslFrom ="ElementoSeqLinkFrom";	
	
	public static String constraintExpression = "constraintExpression";
	public static String listaLinkConstraint = "listaLinkConstraint";
	public static String elementoLinkConstraint = "Link";
	public static String att_ELC_id ="idLink";	   
        //ListaParallel
	public static String listaParallel ="ListaParallel";
	//ElementoParallel
	public static String elementoParallel ="ElementoParallel";
	public static String att_EPA_id ="id";	
	public static String att_EPA_pre_id = "EPA_";
	public static String att_EPA_nome ="nome";	
	public static String att_EPA_point_middle ="point_start_middle";	
	
	public static String listaLinkParallel = "listaLinkParallel";
	public static String elementoLinkParallel = "Link";
	public static String att_ELP_id ="idLink";	
        
        //ListaSim
	public static String listaSim ="ListaSim";
	//ElementoSim
	public static String elementoSim ="ElementoSim";
	public static String att_ESI_id ="id";	
	public static String att_ESI_pre_id = "ESI_";
	public static String att_ESI_nome ="nome";	
	
	public static String listaLinkSim = "listaLinkSim";
	public static String elementoLinkSim = "Link";
	public static String att_ELS_id ="idLink";	
	
    //ListaLoop
	public static String listaLoop ="ListaLoop";
	//ElementoLoop
	public static String elementoLoop ="ElementoLoop";
	public static String att_ELO_id ="id";	
	public static String att_ELO_pre_id = "ELO_";
	public static String att_ELO_min ="min";
	public static String att_ELO_max ="max";
	
	public static String listaLinkLoop = "listaLinkLoop";
	public static String elementoLinkLoop = "Link";
	public static String att_ELL_id ="idLink";	
	
	
	
	
	//ListaSeqLink
	public static String listaSeqLink ="ListaSeqLink";
	//ElementoMessaggio
	public static String elementoSeqLink ="ElementoSeqLink";
	
	public static String att_ESL_id ="id";
	public static String att_ESL_pre_id = "ESL_";
	
	public static String att_ESL_strict ="strict";
	public static String att_ESL_rrf ="msgRRF";
	public static String att_ESL_flussodiretto ="flussodiretto";
	public static String att_ESL_fromPos ="fromPos";
	public static String att_ESL_toPos ="toPos";
	public static String att_ESL_loop ="loop";
	public static String att_ESL_nome ="nome";
	public static String att_ESL_tipo ="tipo";
	
	public static String elementoSeqLinkPrec ="ElementoSeqLinkPrec";
	public static String att_ESL_slprec ="idref";
	
	public static String elementoConstraintRef ="ElementoContraint";
	public static String att_ESL_ecref ="idref";
	
	public static String elementoClasseFrom ="ElementoClasseFrom";
	public static String att_ESL_ecFrom ="idref";
	
	public static String elementoClasseTo ="ElementoClasseTo";
	public static String att_ESL_ecTo ="idref";
	
	public static String elementoTimeFrom ="ElementoTimeFrom";
	public static String att_ESL_etFrom ="idref";
	
	public static String elementoTimeTo ="ElementoTimeTo";
	public static String att_ESL_etTo ="idref";
	
/* FINE LISTADP +++++++++++++++++++++++++++++++++++++ */
	
/* INIZIO GRAFICA ++++++++++++++++++++++++++++++++++++ */	

	
	public static String elementoBox ="ElementoBox";
	
	
	public static String att_EBO_idref= "idref";
	
	public static String att_EBO_TopX ="TopX";
	public static String att_EBO_TopY ="TopY";
	
	public static String att_EBO_Height ="Height";
	public static String att_EBO_Width  ="Width";
	

	
	public static String eBoxBkColor  ="BkColor";
	public static String att_eBoxBkColor_red  ="red";
	public static String att_eBoxBkColor_green  ="green";
	public static String att_eBoxBkColor_blue  ="blue";
	
	public static String font  ="Font";
	public static String att_font_nome  ="nome";
	public static String att_font_size  ="size";
	public static String att_font_style  ="style";
	
	public static String line  ="Line";
	public static String att_line_Theme  ="theme";
	public static String att_line_Weight  ="weight";
	
	//comune colore
	public static String color  ="Colore";
	public static String att_red  ="red";
	public static String att_green  ="green";
	public static String att_blue  ="blue";
	public static String value ="value";
	
	public static String graficoLink ="GraficoLink";
	public static String att_graficoLink_idref ="idref";
	
	//ElementoTime
//<ElementoTime lineNum="numero" stringVisible="true/false" maxY="numero" minY="numero" lineVisible="true/false" id="ETI_numero" />
	
	public static String att_ETI_gr_minY ="minY";
	public static String att_ETI_gr_maxY ="maxY";
	public static String att_ETI_gr_lineNum ="lineNum";
	public static String att_ETI_gr_stringVisible ="stringVisible";
	public static String att_ETI_gr_lineVisible ="lineVisible";
	
//default	
	public static String defaultConf ="Default";
	
	public static String processo_def ="Processo";
	public static String store_def ="Store";
	public static String start_def ="Start";
	public static String build_def ="Build";
	public static String class_def ="Class";

	public static String defValue ="DefBox";
	public static String att_DVA_Height ="Height";
	public static String att_DVA_Width  ="Width";
	
	public static String link_def ="Link";
	
	//elementografico constraint
	public static String att_ECO_tipo ="type";
	
	///elemento parallelo
	public static String att_EPA_PntStart ="Pnt_Start";
	
	

/* FINE GRAFICA +++++++++++++++++++++++++++++++++++++ */	

}
