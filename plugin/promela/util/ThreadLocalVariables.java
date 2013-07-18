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
    

package plugin.promela.util;

import java.util.LinkedList;
import plugin.statediagram.controllo.ThreadCheck.VariablesList;

/**
 * @author Patrizio
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ThreadLocalVariables {
	private String thread;
	private String localVariables;
	private String process;
	
	public ThreadLocalVariables(String te, String lv,String process){
		if((te==null)||(lv==null))return;
		thread=te;
		localVariables=lv;
		this.process=process;
	}
	
	public ThreadLocalVariables(String te, LinkedList lv,String process){
		if((te==null)||(lv==null))return;
		thread=te;
		this.process=process;
		String varDecl="";
		for(int i=0;i<lv.size();i++){
			if(((VariablesList.Variable)lv.get(i)).getDimension()==2){
				varDecl+="typedef type"+i+" {byte "+((VariablesList.Variable)lv.get(i)).getName().split("\\$.")[1]
					+"[10]};\ntype"+i+" "+((VariablesList.Variable)lv.get(i)).getName().split("\\$.")[0]+					"[10];\n";
			}else{				
				if(((VariablesList.Variable)lv.get(i)).getDimension()==1){
					varDecl+="byte "+((VariablesList.Variable)lv.get(i)).getName()+"[10];\n";
				}else{
					varDecl+="byte "+((VariablesList.Variable)lv.get(i)).getName()+";\n";
				}
			}
		}
		setLocalVariables(varDecl);
		
	}
	
	
	public String getThread(){
		return thread;		
	}
	
	public String getLocalVariables(){
		return localVariables;
	}
	
	public void setLocalVariables(String locVar){
		localVariables=locVar;
	}
	
	/**
	 * @return
	 */
	public String getProcess() {
		return process;
	}

	/**
	 * @param string
	 */
	public void setProcess(String string) {
		process = string;
	}

}
