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


package plugin.statediagram.controllo;

import java.util.LinkedList;

import core.internal.runtime.data.IPlugData;


import plugin.topologydiagram.data.ListaProcesso;
import plugin.statediagram.data.ElementoStato;
import plugin.statediagram.data.ListaDP;
import plugin.statediagram.data.ListaMessaggio;
import plugin.statediagram.data.ListaStato;
import plugin.statediagram.data.ListaThread;
import plugin.statediagram.data.PlugData;
import plugin.statediagram.data.ThreadElement;
import plugin.statediagram.graph.ElementoMessaggio;



/**
 * @author Patrizio
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ThreadCheck {
	
	private PlugData plugData;
	
	private VariablesList glVariables;
	
	private String threadWrongGuard="";
	
	/**
	 * The constructor contains the setting of the variable plugData
	 * @param plugData
	 */
	public ThreadCheck(IPlugData plugData){
		this.plugData=(plugin.statediagram.data.PlugData)plugData.getPlugDataManager().getPlugData("charmy.plugin.state");	
	}
	
	/**
	 * This function check if there are messages in the system that are 
	 * sent and not received and viceversa 
	 * @return an instance of the class Error that in the case of error
	 * conteins the message error.
	 */
	public Error messagesCheck(){
		boolean found=false;
		LinkedList messages=plugData.getListaDP().getAllMessages();
		LinkedList wrongMessages=new LinkedList();
		LinkedList wrongMessagesString=new LinkedList();
		boolean [] msgFound= new boolean[messages.size()];
		ElementoMessaggio em;
		ElementoMessaggio em2;
		for(int i=0;i<messages.size();i++){
			if(msgFound[i]==false){
				em = (ElementoMessaggio)messages.get(i);
				if(!em.getName().equals("")){
					if(em.getSendReceive()!=ElementoMessaggio.TAU){		
						for(int j=i;j<messages.size();j++){			
							em2=(ElementoMessaggio)messages.get(j);
							if(em.getName().equals(em2.getName())){
								if((em.getSendReceive()!=em2.getSendReceive())
										&&(em2.getSendReceive()!=ElementoMessaggio.TAU)){
									found=true;
									msgFound[j]=true;
								}						
							}
						}
						if(!found){
							wrongMessages.add(em);
							wrongMessagesString.add("Message \""+em.getName()+"\" of the thread \"" +									em.getThreadName()+"\"");
						}else{
							found=false;					
						}
					}else{
						for(int j=i;j<messages.size();j++){			
							em2=(ElementoMessaggio)messages.get(j);
							if(em.getName().equals(em2.getName())){
								if(em2.getSendReceive()!=ElementoMessaggio.TAU){
									wrongMessages.add(em);
									wrongMessagesString.add("Message \""+em.getName()+"\" of the thread \"" +
											em.getThreadName()+"\"");
									found=true;				
								}	
							}
						}
						if(!found){
							for(int p=0;p<wrongMessages.size();p++){
								if(((ElementoMessaggio)wrongMessages.get(p)).getName().equals(em.getName())){
									wrongMessages.add(em);
									wrongMessagesString.add("Message \""+em.getName()+"\" of the thread \"" +
											em.getThreadName()+"\"");
									p=wrongMessages.size();
								}
							}
							found=false;
						}
					}
				}
				
			}
		}
		if(wrongMessages.size()==0){
			String msg="******************************************\n";
			msg+="Messages Check:\n" +			"(Messages sent and not received and viceversa)\n";
			msg+="There are no errors!\n";
			return new Error(false,msg);
		}
		else{
			String msg="******************************************\n";
			msg+="Messages Check:\n" +			"(Messages sent and not received and viceversa)\n";
			for(int k=0;k<wrongMessagesString.size();k++)
				msg+=wrongMessagesString.get(k)+"\n";
			msg+="******************************************\n";	
			return new Error(true,msg);
		}
	}
	
	/**
	 * @return
	 */
	public Error threadMessagesCheck(){
		boolean error=false;
		LinkedList procMessages;
		LinkedList wrongMessages=new LinkedList();
		String procName;
		ListaProcesso procs;
		plugin.topologydiagram.data.PlugData pdTop=(plugin.topologydiagram.data.PlugData)plugData.getPlugDataManager().getPlugData("charmy.plugin.topology");	
		if(pdTop!=null)
			procs= pdTop.getListaProcesso();
		else
			procs=new ListaProcesso(null);	 
		String msg="******************************************\n" +		"Thread Messages Check:\n" +		"(Messages sent and received in the same process)\n";
		for(int i=0;i<procs.size();i++){
			procMessages=plugData.getListaDP().getDinamicaProcesso(i).getAllMessages();
			procName=procs.getElement(i).getName();
			msg+="============\nProcess: "+procName+"\n";
			for(int j=0;j<procMessages.size();j++){
				ElementoMessaggio em=(ElementoMessaggio)procMessages.get(j);
				if(!em.getName().equals("")){
					ElementoMessaggio em2;
					for(int k=j;k<procMessages.size();k++){
						em2=(ElementoMessaggio)procMessages.get(k);
						if(em.getName().equals(em2.getName()))
							if(em.getSendReceive()!=em2.getSendReceive()){
								msg+=em.getName()+"\n";
								error=true;
							}
					}
				}
			}		
		}
		msg+="******************************************";
		if(error)
			return new Error(true,msg);
		else
			return new Error(false,msg);
	}
	
	/**
	 * @return
	 */
	public Error checkNumPar(){
		boolean value=false;		
		LinkedList listaMessaggi = new LinkedList();	
		ListaDP lDP=plugData.getListaDP();
		listaMessaggi = lDP.getAllMessagesDoppioni();
		LinkedList canaliErrati=new LinkedList();
		for(int i=0;i<listaMessaggi.size();i++){
			for(int j=i;j<listaMessaggi.size();j++){
				
				if(!((ElementoMessaggio)listaMessaggi.get(i)).getName().equals(""))
					if(						
							(((ElementoMessaggio)listaMessaggi.get(i)).getName().equals(((ElementoMessaggio)listaMessaggi.get(j)).getName()))&&
							(((ElementoMessaggio)listaMessaggi.get(i)).getParameters().size()!=((ElementoMessaggio)listaMessaggi.get(j)).getParameters().size())){
						value=true;
						canaliErrati.add("Message \""+((ElementoMessaggio)listaMessaggi.get(i)).getName()+"\""+
								" of the thread: \""+((ElementoMessaggio)listaMessaggi.get(i)).getThreadName()+"\", states: \""+((ElementoMessaggio)listaMessaggi.get(i)).getElementFrom().getName()+
								" -> "+((ElementoMessaggio)listaMessaggi.get(i)).getElementTo().getName()+"\"\n"+
								"\tNumber of parameters: "+((ElementoMessaggio)listaMessaggi.get(i)).getParameters().size() 
						);
						canaliErrati.add("Message \""+((ElementoMessaggio)listaMessaggi.get(j)).getName()+"\""+
								" of the thread: \""+((ElementoMessaggio)listaMessaggi.get(j)).getThreadName()+"\", states: \""+((ElementoMessaggio)listaMessaggi.get(j)).getElementFrom().getName()+
								" -> "+((ElementoMessaggio)listaMessaggi.get(j)).getElementTo().getName()+"\"\n"+
								"\tNumber of parameters: "+((ElementoMessaggio)listaMessaggi.get(j)).getParameters().size() 
						);
						
					}
			}
		}
		if(value){
			String msg="******************************************\n";
			msg+="Number Parameters Check:\n" +			"(Messages with same name and different number of parameters)";
			
			for(int i=0;i<canaliErrati.size();i++){
				msg+="\t"+canaliErrati.get(i)+"\n";
			}
			msg+="******************************************\n";
			return new Error(true,msg);
		}
		else{
			String msg = "******************************************\n" +			"Number Parameters Check:\n" +			"(Messages with same name and different number of parameters)\n" +
			"There are no errors!\n" +						"******************************************\n";
			return new Error(false,msg);
		}
	}
	
	/**
	 * @return
	 */
	public Error startState(){
		
		ThreadElement te;	
		ListaThread tl;
		boolean check=false;
		String msg="******************************************\n" +		"Start state Check:\n" +		"\tthe followings threads have not start state:\n\n";
		for(int i=0;i<plugData.getListaDP().size();i++){
			tl = plugData.getListaDP().getDinamicaProcesso(i);
			for(int j=0;j<tl.size();j++){
				te=tl.get(j);
				if(te.getListaStato().getStartState()==null){
					msg+="thread: "+te.getNomeThread()+"\n";
					check=true;
				}				
			}
		}
		msg+="******************************************\n";
		if(check){
			return new Error(true,msg);
		}else{
			return new Error(false,msg);
		}
	}
	
	/**
	 * @return
	 */
	public Error checkSendReceive(){
		Error er;
		String msg="******************************************\n" +
		"Send Receive Check:\n";
		String msgR="\treceive of a message into an integer value \n" +
		"in the following messages:\n";
		String msgS="\tsend of an empty value _ in the following messages:\n";
		String msgRC="";
		String msgSC="";
		for(int i=0;i<plugData.getListaDP().getAllMessages().size();i++){
			ElementoMessaggio em=(ElementoMessaggio)plugData.getListaDP().getAllMessages().get(i);
			if(em.getSendReceive()==ElementoMessaggio.RECEIVE){
				for(int j=0;j<em.getParameters().size();j++){
					try{
						Integer.parseInt((String) em.getParameters().get(j));
						msgRC+=em.getName()+" in the thread: "+em.getThreadName()+"\n";
					}catch(NumberFormatException e){
					}
				}			
			}
			if(em.getSendReceive()==ElementoMessaggio.SEND){
				for(int j=0;j<em.getParameters().size();j++){
					if(((String) em.getParameters().get(j)).equals("_"))
						msgSC+=em.getName()+" in the thread: "+em.getThreadName()+"\n";
				}			
			}
		}
		boolean error=false;
		if(!msgRC.equals("")){
			msg+=msgR+msgRC;
			error=true;
		}
		if(!msgSC.equals("")){
			msg+=msgS+msgSC;
			error=true;
		}		
		msg+="******************************************\n";
		if(error)
			return new Error(true,msg);
		else
			return new Error(false,msg);	
	}
	
	/**
	 * @param operVar
	 * @param receivePar
	 * @param sendPar
	 * @param guard
	 * @param entryCode
	 * @return
	 */
	
	private Error checkEachOne(VariablesList operVar,
			VariablesList receivePar,
			VariablesList sendPar,
			VariablesList guard,	
			VariablesList entryCode,VariablesList one){
		Error er;
		String msg="******************************************\n" +
		"Variables Check:\n" +
		"\tplease check the following variables:\n\nVariable ";			
		String msgEr=" with is used as a simple variable and as an array\n" +
		"******************************************\n";	
		if(one!=null){
			for(int i=0;i<one.size();i++){
				if(one.get(i).getName().indexOf("$")!=-1)
					one.get(i).setName(one.get(i).getName().substring(0,one.get(i).getName().indexOf("$")));
				
				if(operVar==null)
					return new Error(true,"******************************************\n" +
							"Operation Check:\n" +
							"Thread " +threadWrongGuard+"\n"+
							"You have used a bidimensional array" +
							" without the use of the symbol '.'\n" +
					"******************************************\n");
				else{
					for(int j=0;j<operVar.size();j++){
						if(one.get(i).getName().equals(operVar.get(j).getName())){
							if(one.get(i).getDimension()!=operVar.get(j).getDimension()){
								er=new Error(true,msg+one.get(i).getName()+msgEr);
								return er;						
							}
						}
					}
				}
				if(guard==null)
					return new Error(true,"******************************************\n" +
							"Guard Check:\n" +							"Thread " +threadWrongGuard+"\n"+
							"You have used a bidimensional array" +
							" without the use of the symbol '.'\n" +					"******************************************\n");
				else{
					for(int j=0;j<guard.size();j++){
						if(one.get(i).getName().equals(guard.get(j).getName())){
							if(one.get(i).getDimension()!=guard.get(j).getDimension()){
								er=new Error(true,msg+one.get(i).getName()+msgEr);
								return er;						
							}
						}
					}
				}
				if(receivePar==null)
					return new Error(true,"******************************************\n" +
							"Receive Parameters Check:\n" +
							"Thread " +threadWrongGuard+"\n"+
							"You have used a bidimensional array" +
							" without the use of the symbol '.'\n" +
					"******************************************\n");
				else{			
					for(int j=0;j<receivePar.size();j++){
						if(one.get(i).getName().equals(receivePar.get(j).getName())){
							if(one.get(i).getDimension()!=receivePar.get(j).getDimension()){
								er=new Error(true,msg+one.get(i).getName()+msgEr);
								return er;						
							}
						}
					}
				}
				if(sendPar==null)
					return new Error(true,"******************************************\n" +
							"Send Parameters Check:\n" +
							"Thread " +threadWrongGuard+"\n"+
							"You have used a bidimensional array" +
							" without the use of the symbol '.'\n" +
					"******************************************\n");
				else{									
					for(int j=0;j<sendPar.size();j++){
						if(one.get(i).getName().equals(sendPar.get(j).getName())){
							if(one.get(i).getDimension()!=sendPar.get(j).getDimension()){
								er=new Error(true,msg+one.get(i).getName()+msgEr);
								return er;						
							}
						}
					}
				}
				if(entryCode==null)
					return new Error(true,"******************************************\n" +
							"Entry Code Check:\n" +
							"Thread " +threadWrongGuard+"\n"+
							"You have used a bidimensional array" +
							" without the use of the symbol '.'\n" +
					"******************************************\n");
				else{									
					for(int j=0;j<entryCode.size();j++){
						if(one.get(i).getName().equals(entryCode.get(j).getName())){
							if(one.get(i).getDimension()!=entryCode.get(j).getDimension()){
								er=new Error(true,msg+one.get(i).getName()+msgEr);
								return er;						
							}
						}
					}
				}
			}
		}
		er=new Error(false,msg+"NO ERROR!!!!\n" +
		"******************************************\n");
		return er;			
	}
	
	
	public Error checkDimension(VariablesList operVar,
			VariablesList receivePar,
			VariablesList sendPar,
			VariablesList guard,	
			VariablesList entryCode){
		Error er;
		
		er=checkEachOne(operVar,
				receivePar,
				sendPar,
				guard,	
				entryCode,
				operVar);	
		if(er.isError())
			return er;	
		er=checkEachOne(operVar,
				receivePar,
				sendPar,
				guard,	
				entryCode,
				receivePar);	
		if(er.isError())
			return er;	
		er=checkEachOne(operVar,
				receivePar,
				sendPar,
				guard,	
				entryCode,
				sendPar);	
		if(er.isError())
			return er;	
		er=checkEachOne(operVar,
				receivePar,
				sendPar,
				guard,	
				entryCode,
				guard);	
		if(er.isError())
			return er;	
		er=checkEachOne(operVar,
				receivePar,
				sendPar,
				guard,	
				entryCode,
				entryCode);	
		return er;	
	}
	
	private void extractArgs(String s,VariablesList vl,String threadName,int position){	
		String []aS= s.split("\\[");
		if(aS.length==3){
			try{
				Integer.parseInt(aS[1].substring(0,aS[1].indexOf("]")));
			}catch(NumberFormatException e){
				vl.add(vl.new Variable(aS[1].substring(0,aS[1].indexOf("]")),
						false,
						position,
						0,
						threadName));
			}
			try{
				Integer.parseInt(aS[2].substring(0,aS[2].indexOf("]")));
			}catch(NumberFormatException e){
				vl.add(vl.new Variable(aS[2].substring(0,aS[2].indexOf("]")),
						false,
						position,
						0,
						threadName));
			}						
		}else if(aS.length==2){
			try{
				Integer.parseInt(aS[1].substring(0,aS[1].indexOf("]")));
			}catch(NumberFormatException e){
				vl.add(vl.new Variable(aS[1].substring(0,aS[1].indexOf("]")),
						false,
						position,
						0,
						threadName));
			}
		}
		
	}
	
	
	private VariablesList extractOperVar(ListaMessaggio messages){
		VariablesList vl=new VariablesList();
		for(int i=0;i<messages.getAllMessages().size();i++){
			ElementoMessaggio em=(ElementoMessaggio)messages.getAllMessages().get(i);
			String operations=em.getOperations();
			if(!operations.equals("")){
				String [] op=operations.split(";");
				for(int j=0;j<op.length;j++){
					if(!op[j].equals("")){
						String oper=op[j].split("=")[0];
						if(oper.split("\\[").length>2){
							if(oper.indexOf(".")!=-1){
								vl.add(vl.new Variable(oper.substring(0,oper.indexOf("["))+"$"+
										oper.substring(oper.indexOf("."),oper.lastIndexOf("[")),
										false,VariablesList.Variable.OPERATION,2,em.getThreadName()));
								extractArgs(oper,vl,em.getThreadName(),VariablesList.Variable.OPERATION);
							}else{
								threadWrongGuard=em.getThreadName();			
								return null;
							}
						}else{
							if(oper.split("\\[").length==2){	
								vl.add(vl.new Variable(oper.substring(0,oper.indexOf("[")),
										false,VariablesList.Variable.OPERATION,1,em.getThreadName()));
								extractArgs(oper,vl,em.getThreadName(),VariablesList.Variable.OPERATION);
							}else{
								vl.add(vl.new Variable(oper,
										false,VariablesList.Variable.OPERATION,0,em.getThreadName()));
							}
						}
					}
				}
			}
		}
		return vl;
	}
	
	private VariablesList extractEntryCode(ListaStato states, String threadName){
		VariablesList vl=new VariablesList();
		for(int i=0;i<states.size();i++){
			ElementoStato es=(ElementoStato)states.getElement(i);
			String entryCode=es.getOnEntryCode();
			if(entryCode!=null)
				if(!entryCode.equals("")){
					String [] op=entryCode.split(";");
					for(int j=0;j<op.length;j++){
						if(!op[j].equals("")){
							String oper=op[j].split("=")[0];
							if(oper.split("\\[").length>2){
								if(oper.indexOf(".")!=-1){
									vl.add(vl.new Variable(oper.substring(0,oper.indexOf("["))+"$"+
											oper.substring(oper.indexOf("."),oper.lastIndexOf("[")),
											false,VariablesList.Variable.ENTRYCODE,2,""));
									extractArgs(oper,vl,"",VariablesList.Variable.ENTRYCODE);		
								}else{
									threadWrongGuard=threadName;
									return null;	
								}
							}else{
								if(oper.split("\\[").length==2){	
									vl.add(vl.new Variable(oper.substring(0,oper.indexOf("[")),
											false,VariablesList.Variable.ENTRYCODE,1,""));
									extractArgs(oper,vl,"",VariablesList.Variable.ENTRYCODE);
								}else{
									if(!oper.trim().equals(""))
										vl.add(vl.new Variable(oper,
												false,VariablesList.Variable.ENTRYCODE,0,""));		
								}
							}
						}
					}			
				}
		}
		return vl;
	}
	
	private VariablesList extractGuard(ListaMessaggio messages){
		//LinkedList result=new LinkedList();
		VariablesList vl=new VariablesList();
		for(int i=0;i<messages.getAllMessages().size();i++){
			ElementoMessaggio em=(ElementoMessaggio)messages.getAllMessages().get(i);
			String guard=em.getGuard().trim();
			//guard=guard.replace('(',' ');
			//guard=guard.replace(')',' ').trim();
			guard=guard.replaceAll("\\(","");
			guard=guard.replaceAll("\\)","").trim();
			LinkedList guardList=new LinkedList();				
			if(guard.indexOf("&&")!=-1){
				if(guard.indexOf("||")!=-1){
					String []g1=guard.split("&&");
					for(int l=0;l<g1.length;l++){
						String []g2=guard.split("||");
						for(int l2=0;l2<g2.length;l2++)
							guardList.add(g2[l2]);
					}
				}else{
					String []g1=guard.split("&&");
					for(int l=0;l<g1.length;l++){
						guardList.add(g1[l]);
					}				
				}
			}else
				if(guard.indexOf("||")!=-1){
					String []g2=guard.split("||");
					for(int l2=0;l2<g2.length;l2++)
						guardList.add(g2[l2]);					
				}else{
					guardList.add(guard);			
				}
			String gua="";	
			for(int j=0;j<guardList.size();j++){
				if(((String) guardList.get(j)).indexOf("==")!=-1){	
					gua=((String) guardList.get(j)).split("==")[0];
				}else
					if(((String) guardList.get(j)).indexOf(">=")!=-1){	
						gua=((String) guardList.get(j)).split(">=")[0];
					}else
						if(((String) guardList.get(j)).indexOf(">")!=-1){	
							gua=((String) guardList.get(j)).split(">")[0];
						}else
							if(((String) guardList.get(j)).indexOf("<=")!=-1){	
								gua=((String) guardList.get(j)).split("<=")[0];
							}else
								if(((String) guardList.get(j)).indexOf("<")!=-1){	
									gua=((String) guardList.get(j)).split("<")[0];
								}else
									if(((String) guardList.get(j)).indexOf("!=")!=-1){
										gua=((String) guardList.get(j)).split("!=")[0];
									}else{
										if(((String) guardList.get(j)).indexOf("!")!=-1){
											gua=((String) guardList.get(j)).replace('!',' ');
										}
									}
				if(gua.startsWith("["))
					gua=gua.substring(1,gua.length());
				gua=gua.trim();
				if(!gua.equals("")){
					if(gua.split("\\[").length>2){
						if(gua.indexOf(".")!=-1){
							vl.add(vl.new Variable(gua.substring(0,gua.indexOf("["))+"$"+
									gua.substring(gua.indexOf("."),gua.lastIndexOf("[")),
									false,VariablesList.Variable.GUARD,2,em.getThreadName()));
							extractArgs(gua,vl,em.getThreadName(),VariablesList.Variable.GUARD);
						}else{			
							threadWrongGuard=em.getThreadName();			
							return null;	
						}
					}else{
						if(gua.split("\\[").length==2){	
							vl.add(vl.new Variable(gua.substring(0,gua.indexOf("[")),
									false,VariablesList.Variable.GUARD,1,em.getThreadName()));
							extractArgs(gua,vl,em.getThreadName(),VariablesList.Variable.GUARD);
						}
						else{
							vl.add(vl.new Variable(gua,
									false,VariablesList.Variable.GUARD,0,em.getThreadName()));
						}
					}				
				}
			}
			
		}
		return vl;
		//return result;
	}
	
	private VariablesList extractSendPar(ListaMessaggio messages){
		LinkedList par=null;
		VariablesList vl=new VariablesList();
		for(int i=0;i<messages.getAllMessages().size();i++){
			ElementoMessaggio em=(ElementoMessaggio)messages.getAllMessages().get(i);
			if(em.getSendReceive()==ElementoMessaggio.SEND){
				par=em.getParameters();
				for(int j=0;j<par.size();j++){
					try{
						Integer.parseInt((String)par.get(j));
						if(em.getSendReceive()==ElementoMessaggio.RECEIVE){
						}
					}catch(NumberFormatException e){
						if(!par.get(j).equals("_")){
							if(((String)par.get(j)).split("\\[").length>2){
								if(((String)par.get(j)).indexOf(".")!=-1){
									vl.add(vl.new Variable(((String)par.get(j)).substring(0,((String)par.get(j)).indexOf("["))+"$"+
											((String)par.get(j)).substring(((String)par.get(j)).indexOf("."),((String)par.get(j)).lastIndexOf("[")),
											false,VariablesList.Variable.PARAMETER,2,em.getThreadName()));
									extractArgs((String)par.get(j),vl,em.getThreadName(),VariablesList.Variable.PARAMETER);
								}else{
									threadWrongGuard=em.getThreadName();			
									return null;
								}
							}
							else{
								if(((String)par.get(j)).split("\\[").length==2){	
									vl.add(vl.new Variable(((String)par.get(j)).substring(0,((String)par.get(j)).indexOf("[")),
											false,VariablesList.Variable.PARAMETER,1,em.getThreadName()));					
									extractArgs((String)par.get(j),vl,em.getThreadName(),VariablesList.Variable.PARAMETER);
								}else{
									vl.add(vl.new Variable((String)par.get(j),
											false,VariablesList.Variable.PARAMETER,0,em.getThreadName()));					
								}
							}
							
						}else{
							if(em.getSendReceive()==ElementoMessaggio.SEND){
							}						
						}
					}
				}
			}
		}
		return vl;
	}
	
	private VariablesList extractReceivePar(ListaMessaggio messages){
		LinkedList par=null;
		VariablesList vl=new VariablesList();
		for(int i=0;i<messages.getAllMessages().size();i++){
			ElementoMessaggio em=(ElementoMessaggio)messages.getAllMessages().get(i);
			if(em.getSendReceive()==ElementoMessaggio.RECEIVE){
				par=em.getParameters();
				for(int j=0;j<par.size();j++){
					try{
						Integer.parseInt((String)par.get(j));
						if(em.getSendReceive()==ElementoMessaggio.RECEIVE){
						}
					}catch(NumberFormatException e){
						if(!par.get(j).equals("_")){
							if(((String)par.get(j)).split("\\[").length>2){
								if(((String)par.get(j)).indexOf(".")!=-1){
									vl.add(vl.new Variable(((String)par.get(j)).substring(0,((String)par.get(j)).indexOf("["))+"$"+
											((String)par.get(j)).substring(((String)par.get(j)).indexOf("."),((String)par.get(j)).lastIndexOf("[")),
											false,VariablesList.Variable.PARAMETER,2,em.getThreadName()));
									extractArgs((String)par.get(j),vl,em.getThreadName(),VariablesList.Variable.PARAMETER);
								}else{
									threadWrongGuard=em.getThreadName();			
									return null;	
								}
							}
							else{
								if(((String)par.get(j)).split("\\[").length==2){	
									vl.add(vl.new Variable(((String)par.get(j)).substring(0,((String)par.get(j)).indexOf("[")),
											false,VariablesList.Variable.PARAMETER,1,em.getThreadName()));					
									extractArgs((String)par.get(j),vl,em.getThreadName(),VariablesList.Variable.PARAMETER);
								}else{
									vl.add(vl.new Variable((String)par.get(j),
											false,VariablesList.Variable.PARAMETER,0,em.getThreadName()));					
								}
							}
							
						}else{
							if(em.getSendReceive()==ElementoMessaggio.SEND){
							}						
						}
					}
				}
			}
		}
		return vl;
	}
	
	
	
	public VariablesList extractGlobalVariables(){
		VariablesList globalVariables=new VariablesList();
		VariablesList operVar=new VariablesList();
		VariablesList parSend=new VariablesList();
		VariablesList parReceive=new VariablesList();
		VariablesList guard=new VariablesList();
		VariablesList entryCode=new VariablesList();
		ListaMessaggio par=new ListaMessaggio(plugData);
		
		for(int j=0;j<plugData.getListaDP().size();j++){
			entryCode.clearAll();
			operVar.clearAll();
			parSend.clearAll();
			parReceive.clearAll();
			guard.clearAll();
			for(int h=0;h<plugData.getListaDP().getDinamicaProcesso(j).size();h++){
				entryCode.clearAll();
				operVar.clearAll();
				parSend.clearAll();
				parReceive.clearAll();
				guard.clearAll();
				entryCode.add(extractEntryCode(plugData.getListaDP().getDinamicaProcesso(j).get(h).getListaStato(),
						plugData.getListaDP().getDinamicaProcesso(j).get(h).getNomeThread()));
				operVar.add(extractOperVar(plugData.getListaDP().getDinamicaProcesso(j).get(h).getListaMessaggio()));
				parSend.add(extractSendPar(plugData.getListaDP().getDinamicaProcesso(j).get(h).getListaMessaggio()));
				parReceive.add(extractReceivePar(plugData.getListaDP().getDinamicaProcesso(j).get(h).getListaMessaggio()));
				guard.add(extractGuard(plugData.getListaDP().getDinamicaProcesso(j).get(h).getListaMessaggio()));
				
				extractGlobalVariablesGuard("",operVar,guard,entryCode,parReceive,globalVariables);	
				extractGlobalVariablesOper("",operVar,parSend,guard,entryCode,globalVariables);
			}
		}
		glVariables=globalVariables;
		return globalVariables;	
	}
	
	
	public LocalVariablesList extractLocalVariables(){
		LocalVariablesList localVariables=new LocalVariablesList(glVariables);
		VariablesList operVar=new VariablesList();
		VariablesList parSend=new VariablesList();
		VariablesList parReceive=new VariablesList();
		VariablesList guard=new VariablesList();
		VariablesList entryCode=new VariablesList();
		ListaMessaggio par=new ListaMessaggio(plugData);
		
		for(int j=0;j<plugData.getListaDP().size();j++){
			entryCode.clearAll();
			operVar.clearAll();
			parSend.clearAll();
			parReceive.clearAll();
			guard.clearAll();
			LocalVariablesList.LocalVariableThreadList localThreadVar;
			for(int h=0;h<plugData.getListaDP().getDinamicaProcesso(j).size();h++){
				entryCode.clearAll();
				operVar.clearAll();
				parSend.clearAll();
				parReceive.clearAll();
				guard.clearAll();
				localThreadVar=localVariables.new LocalVariableThreadList(plugData.getListaDP().getDinamicaProcesso(j).get(h).getNomeThread(),
						plugData.getListaDP().getDinamicaProcesso(j).getNameProcesso()); 
				entryCode.add(extractEntryCode(plugData.getListaDP().getDinamicaProcesso(j).get(h).getListaStato(),
						plugData.getListaDP().getDinamicaProcesso(j).get(h).getNomeThread()));
				operVar.add(extractOperVar(plugData.getListaDP().getDinamicaProcesso(j).get(h).getListaMessaggio()));
				parSend.add(extractSendPar(plugData.getListaDP().getDinamicaProcesso(j).get(h).getListaMessaggio()));
				parReceive.add(extractReceivePar(plugData.getListaDP().getDinamicaProcesso(j).get(h).getListaMessaggio()));
				guard.add(extractGuard(plugData.getListaDP().getDinamicaProcesso(j).get(h).getListaMessaggio()));
				localThreadVar.add(entryCode);
				localThreadVar.add(operVar);
				localThreadVar.add(parSend);
				localThreadVar.add(parReceive);
				localThreadVar.add(guard);
				localVariables.add(localThreadVar);
			}
		}
		return localVariables;	
	}
	
	
	
	/**
	 * Method that check if in a thread there are variables used in the operations
	 * and in the entryCode but never used in other parts
	 * @param threadName
	 * @param operVar
	 * @param par
	 * @param guard
	 * @param entryCode
	 * @return
	 */
	private void extractGlobalVariablesOper(String threadName,
			VariablesList operVar,
			VariablesList sendPar,
			VariablesList guard,	
			VariablesList entryCode,
			VariablesList globalVariables){
		boolean found=false;
		for(int i=0;i<operVar.size();i++){
			for(int j=0;j<guard.size();j++){
				if(operVar.get(i).getName().equals(guard.get(j).getName())){
					found=true;
					j=guard.size();
				}
			}
			if(!found)
				for(int j=0;j<sendPar.size();j++){
					if(operVar.get(i).getName().equals(sendPar.get(j).getName())){
						found=true;
						j=sendPar.size();
					}
				}
			if(!found){
				globalVariables.add(operVar);
			}
		}
		for(int i=0;i<entryCode.size();i++){
			for(int j=0;j<guard.size();j++){
				if(entryCode.get(i).getName().equals(guard.get(j).getName())){
					found=true;
					j=guard.size();
				}
			}
			if(!found)
				for(int j=0;j<sendPar.size();j++){
					if(entryCode.get(i).getName().equals(sendPar.get(j).getName())){
						found=true;
						j=sendPar.size();
					}
				}
			if(!found){
				globalVariables.add(operVar);
			}
		}
	}
	
	/**
	 * Method that check if in a thread there are variables defined in the guard
	 * and never used in other parts
	 * @param threadName
	 * @param operVar
	 * @param par
	 * @param guard
	 * @param entryCode
	 * @return Error string
	 */
	private void extractGlobalVariablesGuard(String threadName,
			VariablesList operVar,
			VariablesList guard,	
			VariablesList entryCode,
			VariablesList receivePar,
			VariablesList globalVariables){
		boolean found=false;
		for(int i=0;i<guard.size();i++){
			for(int j=0;j<operVar.size();j++){
				if(guard.get(i).getName().equals(operVar.get(j).getName())){
					found=true;
					j=operVar.size();
				}
			}
			if(!found)
				for(int j=0;j<entryCode.size();j++){
					if(guard.get(i).getName().equals(entryCode.get(j).getName())){
						found=true;
						j=entryCode.size();
					}
				}
			if(!found)
				for(int j=0;j<receivePar.size();j++){
					if(guard.get(i).getName().equals(receivePar.get(j).getName())){
						found=true;
						j=receivePar.size();
					}
				}
			if(!found){
				//System.out.println("Global variable from Guard: "+guard.get(i).getName());
				globalVariables.add(guard.get(i));
			}
		}
	}
	
	
	public String check(){
		boolean result=false;
		String msg="";
		ThreadCheck tc = new ThreadCheck(plugData);
		//Check if each message is sent and received
		ThreadCheck.Error er=tc.messagesCheck();
		if(er.isError()){
			result=true;
			msg=er.printError();
		}
		//Check if there exists a pair of messages with same name 
		//and different number of parameters
		er=tc.checkNumPar();
		if(er.isError()){
			result=true;
			msg+=er.printError();
		}
		//Check if a thread send and receive the same message
		er=tc.threadMessagesCheck();
		if(er.isError()){
			result=true;
			msg+=er.printError();
		}
		//Check if each thread have a start state
		er=tc.startState();
		if(er.isError()){
			result=true;
			msg+=er.printError();
		}	
		
		VariablesList operVar=null;
		VariablesList parSend=null;
		VariablesList parReceive=null;
		VariablesList guard=null;	
		VariablesList entryCode=null;	
		ListaMessaggio par=new ListaMessaggio(plugData);
		for(int i=0;i<plugData.getListaDP().getAllMessages().size();i++)
			par.add((ElementoMessaggio)plugData.getListaDP().getAllMessages().get(i));
		operVar=extractOperVar(par);
		parSend=extractSendPar(par);
		parReceive=extractReceivePar(par);
		guard=extractGuard(par);
		entryCode=new VariablesList();
		for(int j=0;j<plugData.getListaDP().size();j++){
			for(int h=0;h<plugData.getListaDP().getDinamicaProcesso(j).size();h++)
				entryCode.add(extractEntryCode(plugData.getListaDP().getDinamicaProcesso(j).get(h).getListaStato(),
						plugData.getListaDP().getDinamicaProcesso(j).get(h).getNomeThread()));
		}
		//Check if a variable is used as simple variable, an array or a bidimensional array
		er=checkDimension(operVar,parReceive,parSend,guard,entryCode);
		if(er.isError()){
			result=true;
			msg+=er.printError();
		}	
		//Check if is sent an empty value and if is received a value into a constant	
		er=checkSendReceive();
		if(er.isError()){
			result=true;
			msg+=er.printError();
		}	
		if(result)
			return msg;
		return "";
	}
	
	
	
	public class LocalVariablesList{
		private LinkedList vl;
		private VariablesList globalVariables;
		
		public LocalVariablesList(VariablesList globalVariables){
			vl= new LinkedList();
			this.globalVariables=globalVariables;
		}
		
		public void add(LocalVariableThreadList lvl){
			vl.add(lvl);
		}
		
		public LocalVariableThreadList get(int i){
			return (LocalVariableThreadList)vl.get(i);
		}
		
		
		public LocalVariableThreadList get(String processName, String threadName){
			for(int i=0;i<size();i++){
				if(get(i).processName.equals(processName)&&
						get(i).threadName.equals(threadName)){
					return get(i);
				}
			}
			return null;
		}
		
		public int size(){
			return vl.size();
		}
		
		public class LocalVariableThreadList{
			private String threadName;
			private String processName;
			private LinkedList vl;
			
			public LocalVariableThreadList(String threadName,String processName){
				vl=new LinkedList();
				this.processName=processName;
				this.threadName=threadName;
			}
			
			public void add(VariablesList.Variable v){
				boolean found=false;
				for(int i=0;i<vl.size();i++){
					if(get(i).getName().equals(v.getName())){
						found=true;
						i=vl.size();
					}
				}
				
				if(!found){					
					for(int i=0;i<globalVariables.size();i++){
						if(globalVariables.get(i).getName().equals(v.getName())){
							found=true;
							i=globalVariables.size();
						}
					}
					if(!found)
						vl.add(v);
				}
			}
			
			public void add(VariablesList varList){
				boolean found;
				for(int i=0;i<varList.size();i++){
					found=false;
					for(int j=0;j<size();j++){
						if(get(j).getName().equals(varList.get(i).getName())){
							found=true;
							j=size();
						}
					}
					if(!found){					
						for(int k=0;k<globalVariables.size();k++){
							if(globalVariables.get(k).getName().equals(varList.get(i).getName())){
								found=true;
								k=globalVariables.size();
							}
						}
						if(!found)
							add(varList.get(i));
					}
				}
			}
			
			public int size(){
				return vl.size();
			}
			
			public String getThreadName(){
				return threadName;
			}
			
			public String getProcessName(){
				return processName;
			}
			
			public VariablesList.Variable get(int i){
				return (VariablesList.Variable)vl.get(i);
			}
			
			public LinkedList getList(){
				return vl;
			}
		}
	}
	
	/**
	 * @author Patrizio
	 *
	 * To change the template for this generated type comment go to
	 * Window>Preferences>Java>Code Generation>Code and Comments
	 */
	public class VariablesList{			
		
		private LinkedList vl;
		
		public VariablesList(){
			vl=new LinkedList();
		}
		
		public void clearAll(){
			vl.clear();
		}
		
		public void add(Variable v){
			boolean found=false;
			for(int i=0;i<vl.size();i++)
				if(((Variable)vl.get(i)).getName().equals(v.getName()))
					found=true;
			if(!found)
				vl.add(v);
		}
		
		public void add(VariablesList varList){
			if(varList!=null)
				for(int i=0;i<varList.size();i++){
					add(varList.get(i));
				}
		}
		
		public Variable get(int i){
			return (Variable)vl.get(i);
		}
		
		
		public int size(){
			return vl.size();
		}
		
		
		/**
		 * @author Patrizio
		 *
		 * To change the template for this generated type comment go to
		 * Window>Preferences>Java>Code Generation>Code and Comments
		 */
		public class Variable{
			public static final int OPERATION=0;
			public static final int GUARD=1;
			public static final int PARAMETER=2;
			public static final int ENTRYCODE=3;
			private String name;
			private boolean global;
			private int position;
			private int dimension;
			private String threadName;
			
			/**
			 * 
			 */
			public Variable(String name, boolean global, int position, int dimension, String threadName) {
				this.name=name;
				this.global=global;
				this.position=position;
				this.dimension=dimension;
				this.threadName=threadName;
			}
			
			/**
			 * @return
			 */
			public boolean isGlobal() {
				return global;
			}
			
			/**
			 * @return
			 */
			public String getName() {
				return name;
			}
			
			/**
			 * @return
			 */
			public int getPosition() {
				return position;
			}
			
			/**
			 * @param b
			 */
			public void setGlobal(boolean b) {
				global = b;
			}
			
			/**
			 * @param string
			 */
			public void setName(String string) {
				name = string;
			}
			
			/**
			 * @param i
			 */
			public void setPosition(int i) {
				position = i;
			}
			
			/**
			 * @return
			 */
			public int getDimension() {
				return dimension;
			}
			
			/**
			 * @param i
			 */
			public void setDimension(int i) {
				dimension = i;
			}
			
			/**
			 * @return
			 */
			public String getThreadName() {
				return threadName;
			}
			
			/**
			 * @param string
			 */
			public void setThreadName(String string) {
				threadName = string;
			}
			
		}
	}
	
	
	public class Error{
		private boolean berror;
		private String errorReport;
		
		public Error(boolean b, String s){
			berror=b;
			errorReport=s;
		}
		
		public boolean isError(){
			return berror;
		}
		
		public String printError(){ 
			return errorReport;
		}
	}
	
	
}


