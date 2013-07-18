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
    

package plugin.sequencediagram.data;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import plugin.sequencediagram.graph.GraficoCollegamentoSeqLink;
import plugin.sequencediagram.graph.GraficoLoopSeqLink;
import plugin.topologydiagram.resource.data.ElementoCanaleMessaggio;
import plugin.topologydiagram.resource.data.ElementoProcessoStato;
import plugin.topologydiagram.resource.data.interfacce.IListaCanMessNotify;


/**
 *
 * @author  FLAMEL 2005
 */
/**
 * Questa classe implementa il link per lo scambio di messaggi
 */
public class ElementoSeqLink extends ElementoCanaleMessaggio implements Serializable {

        /** Costante che rappresenta un messaggio sincrono. */
	public static final int SYNCHRONOUS = 1;
	/** Costante che rappresenta un messaggio asincrono. */
	public static final int ASYNCHRONOUS = 2;
	/** Costante che rappresenta un messaggio Regular. */
	public static final int REGULAR = 3;
	/** Costante che rappresenta un messaggio Required. */
	public static final int REQUIRED = 4;
	/** Costante che rappresenta un messaggio Fail. */
	public static final int FAIL = 5;
	/** Costante che determina la dimensione di un loop. */
	public static final int rloop = 10;
	/** Memorizza il tipo di messaggio: Regular, Required, Fail */
	private int regReqFail;

	/**
	 * Riferimento all'istante temporale della
	 */
	private ElementoTime time_one;
	/**
	 * Posizione nell'istante temporale della
	 */
	private int pos_one;
	/**
	 * Riferimento all'istante temporale della
	 */
	private ElementoTime time_two;
	/**
	 * Posizione nell'istante temporale della
	 */
	private int pos_two;
	/** variabile Point che memorizza il punto dove verra' inserito il contraint */
	private Point pCon;
	/** variabile Point che memorizza il punto di partenza del link */
	private Point pStart;
	/** variabile Point che memorizza il punto di arrivo del link */
	private Point pEnd;
	/** Linea continua */
	public static final float[] NOT_DASHED = {10.0f, 0.0f, 10.0f, 0.0f};
        /** linea trattegiata**/
        public static final float[] DASHED = {5.0f, 3.0f, 5.0f, 3.0f};
	/** vera nel caso in cui questo link con il precedente e' strict */
	private boolean strict = false;
        
        /**
	 * Uguale a 'true' se il messaggio ? un loop,
	 */
	private boolean isLoop;
        
        /**Vera se fà parte di un operatore loop**/
        private boolean isLoop_op;
        private int min_Loop =0;
        private int max_Loop =0;
        private long id_loop;     //id del loop di cui il link fà parte 
        
        /** vera nel caso in cui questo messaggio  e' Complementato */
	private boolean isComplement = false;
        
         /**
	 * Uguale a 'true' se il messaggio ? un simultaneo,
	 */
	private boolean isSimultaneous = false;
        private long id_sim;
        
        /** vera nel caso in cui questo messaggio è parallelo */
	private boolean isParallel = false;
        private long id_par;
        private  Point width_max_start;
        private  Point width_max_end;        
                     
        
	/** Link precedente nella scala temporale */
	private ElementoSeqLink prec;
	/** Memorizza la Y dell'elemento processo */
	private int procY;
        
        /*Memorizza una lista di constraint associati al link*/
	private LinkedList con;
        
	/** Rappresentazione grafica della relazione strict con il link precedente. */
	transient private Line2D.Double strictGraph;
        
        /**Se il link fà parte di un operatore parallelo :
         true : indica che fa parte dell'operatore up
         false : bottom**/
        private boolean Operator;

	/**
	 * Costruttore con update
	 * 
	 * @param nomeLink
	 * @param FromProcess
	 * @param ToProcess
	 * @param FromTime
	 * @param ToTime
	 * @param label
	 * @param updateE
	 */
	public ElementoSeqLink(String nomeLink, ElementoProcessoStato FromProcess,ElementoProcessoStato ToProcess, ElementoTime FromTime,
			       ElementoTime ToTime, String label, IListaCanMessNotify updateE) {
		this(nomeLink, FromProcess, ToProcess, FromTime, ToTime, SYNCHRONOUS,label, updateE);
	}
	/**
	 * @param nomeLink
	 * @param FromProcess
	 * @param ToProcess
	 * @param FromTime
	 * @param ToTime
	 * @param msgType
	 * @param label
	 */
	public ElementoSeqLink(String nomeLink, ElementoProcessoStato FromProcess,
			ElementoProcessoStato ToProcess, ElementoTime FromTime,
			ElementoTime ToTime, int msgType, String label,
			IListaCanMessNotify updateE) {
		this(nomeLink, FromProcess, ToProcess, FromTime, ToTime, 1, 1, msgType,
				REGULAR, label, updateE);

	}
	/**
	 * @param nomeLink
	 * @param FromProcess
	 * @param ToProcess
	 * @param FromTime
	 * @param ToTime
	 * @param FromPos
	 * @param ToPos
	 * @param msgType
	 * @param label
	 * @param updateE
	 */
	public ElementoSeqLink(String nomeLink, ElementoProcessoStato FromProcess,
			ElementoProcessoStato ToProcess, ElementoTime FromTime,
			ElementoTime ToTime, int FromPos, int ToPos, int msgType,
			int msgRRF, String label, IListaCanMessNotify updateE) {
		this(nomeLink, FromProcess, ToProcess, FromTime, ToTime, FromPos,
				ToPos, msgType, msgRRF, label, false, 0, updateE);

	}
	/**
	 * @param nomeLink
	 * @param FromProcess
	 * @param ToProcess
	 * @param FromTime
	 * @param ToTime
	 * @param FromPos
	 * @param ToPos
	 * @param msgType
	 * @param label
	 * @param forClone
	 * @param idSeq
	 * @param updateE
	 */
	public ElementoSeqLink(String nomeLink, ElementoProcessoStato FromProcess,
			ElementoProcessoStato ToProcess, ElementoTime FromTime,
			ElementoTime ToTime, int FromPos, int ToPos, int msgType,
			int msgRRF, String label, boolean forClone, long idSeq,
			IListaCanMessNotify updateE) {
		super(forClone, idSeq, updateE);
		procY = FromProcess.getTopY() + FromProcess.getHeight();

		setTipo(msgType);
		setName(label);
		if (FromProcess.equals(ToProcess)) {
			isLoop = true;
			element_one = FromProcess;
			element_two = FromProcess;
			time_one = FromTime;
			time_two = FromTime;
			if ((FromPos > 0) && (FromPos < time_one.getLineNumber())) {
				pos_one = FromPos;
				pos_two = FromPos;
			} else {
				pos_one = 1;
				pos_two = 1;
			}
			flussodiretto = true;
		} else {
			isLoop = false;
			// La variabile 'element_one' conterr? un riferimento
			// al processo con 'idProcesso' pi? piccolo.
			if (FromProcess.getId() < ToProcess.getId()) {
				element_one = FromProcess;
				element_two = ToProcess;
				time_one = FromTime;
				time_two = ToTime;
				if ((FromPos > 0) && (FromPos <= time_one.getLineNumber())) {
					pos_one = FromPos;
				} else {
					pos_one = 1;
				}
				if ((ToPos > 0) && (ToPos <= time_two.getLineNumber())) {
					pos_two = ToPos;
				} else {
					pos_two = 1;
				}
				flussodiretto = true;
			} else {
				
				//ezio 2006
				element_one = ToProcess;
				element_two = FromProcess;
				time_one = ToTime;
				time_two = FromTime;
				/*element_one = FromProcess;
				element_two = ToProcess;
				time_one = FromTime;
				time_two = ToTime;*/
				
				if ((ToPos > 0) && (ToPos <= time_one.getLineNumber())) {
					pos_one = ToPos;
				} else {
					pos_one = 1;
				}
				if ((FromPos > 0) && (FromPos <= time_two.getLineNumber())) {
					pos_two = FromPos;
				} else {
					pos_two = 1;
				}
				flussodiretto = false;
			}
		}
		regReqFail = msgRRF;
		pStart = new Point();
		pEnd = new Point();
		pStart.x = (FromProcess.getPointMiddle()).x;
		pEnd.x = (ToProcess.getPointMiddle()).x;
		pStart.y = time_one.getMinY() + pos_one * ElementoTime.hfascia;
		pEnd.y = time_two.getMinY() + pos_two * ElementoTime.hfascia;
		grafico = null;
                con = new LinkedList();
	}
        
        /**Verifica se il constraint è una future close chain**/
        public boolean isConstraintChCloFut()
        {
            if(con.isEmpty())
                return false;
            for(int i=0;i<con.size();i++)
            {
                ElementoConstraint constr =(ElementoConstraint) con.get(i);
                if(constr.isChCloFut())
                    return true;
            }
            return false;
        }
        
        /**Verifica se il constraint è una future open chain**/
         public boolean isConstraintChOpFut()
        {
             if(con.isEmpty())
                return false;
            for(int i=0;i<con.size();i++)
            {
                ElementoConstraint constr =(ElementoConstraint) con.get(i);
                if(constr.isChOpFut())
                    return true;
            }
            return false;
        }
         
        /**Verifica se il constraint è una past close chain**/ 
        public boolean isConstraintChCloPast()
        {
            if(con.isEmpty())
                return false;
            for(int i=0;i<con.size();i++)
            {
                ElementoConstraint constr =(ElementoConstraint) con.get(i);
                if(constr.isChCloPast())
                    return true;
            }
            return false;
        }
        
        /**Verifica se il constraint è una past open chain**/
        public boolean isConstraintChOpPast()
        {
            if(con.isEmpty())
                return false;
            for(int i=0;i<con.size();i++)
            {
                ElementoConstraint constr =(ElementoConstraint) con.get(i);
                if(constr.isChOpPast())
                    return true;
            }
            return false;
        }
        
        /**Verifica se il constraint è una future close unwanted chain**/
        public boolean isConstraintUnCloFut()
        {
            if(con.isEmpty())
                return false;
            for(int i=0;i<con.size();i++)
            {
                ElementoConstraint constr =(ElementoConstraint) con.get(i);
                if(constr.isUnCloFut())
                    return true;
            }
            return false;
        }
        
        /**Verifica se il constraint è una future open unwanted chain**/
        public boolean isConstraintUnOpFut()
        {
            if(con.isEmpty())
                return false;
            for(int i=0;i<con.size();i++)
            {
                ElementoConstraint constr =(ElementoConstraint) con.get(i);
                if(constr.isUnOpFut())
                    return true;
            }
            return false;
        }
        
        /**Verifica se il constraint è una past close unwanted chain**/
        public boolean isConstraintUnCloPast()
        {
            if(con.isEmpty())
                return false;
            for(int i=0;i<con.size();i++)
            {
                ElementoConstraint constr =(ElementoConstraint) con.get(i);
                if(constr.isUnCloPast())
                    return true;
            }
            return false;
        }
        
        /**Verifica se il constraint è una past open unwanted chain**/
        public boolean isConstraintUnOpPast()
        {
            if(con.isEmpty())
                return false;
            for(int i=0;i<con.size();i++)
            {
                ElementoConstraint constr =(ElementoConstraint) con.get(i);
                if(constr.isUnOpPast())
                    return true;
            }
            return false;
        }
        
        /**Verifica se il constraint è un present **/
        public boolean isConstraintPre()
        {
            if(con.isEmpty())
                return false;
            
            for(int i=0;i<con.size();i++)
            {
                ElementoConstraint constr =(ElementoConstraint) con.get(i);
                if(constr.isPre())
                    return true;
            }
            return false;
        }
        
        /**Verifica se il constraint è un past close boolean formula **/
        public boolean isConstraintPastClo()
        {
            if(con.isEmpty())
                return false;
            for(int i=0;i<con.size();i++)
            {
                ElementoConstraint constr =(ElementoConstraint) con.get(i);
                if(constr.isPastClo())
                    return true;
            }
            return false;
        }
        
        /**Verifica se il constraint è un past open boolean formula **/
        public boolean isConstraintPastOp()
        {
            if(con.isEmpty())
                return false;
            for(int i=0;i<con.size();i++)
            {
                ElementoConstraint constr =(ElementoConstraint) con.get(i);
                if(constr.isPastOp())
                    return true;
            }
            return false;
        }
        
        /**Verifica se il constraint è un future close boolean formula **/
        public boolean isConstraintFutClo() 
        {
            if(con.isEmpty())
                return false;
            for(int i=0;i<con.size();i++)
            {
                ElementoConstraint constr =(ElementoConstraint) con.get(i);
                if(constr.isFutClo())
                    return true;
            }
            return false;
        }
        
        /**Verifica se il constraint è un future open boolean formula **/
        public boolean isConstraintFutOp() 
        {
            if(con.isEmpty())
                return false;
            for(int i=0;i<con.size();i++)
            {
                ElementoConstraint constr =(ElementoConstraint) con.get(i);
                if(constr.isFutOp())
                    return true;
            }
            return false;
        }
        
        /**setta il tipo di consraint **/
        public void setConstraintType(int i)
        {
            if(con.isEmpty())
                return ;
            for(int j=0;j<con.size();j++)
            {
                ElementoConstraint constr =(ElementoConstraint) con.get(j);
                constr.setType(i);
            }
        }
        
        /**aggiorna la posizione di tutti i constraint**/
        public void updateAllConstraintPosizione()
        {
            if(con.isEmpty())
                return ;
            for(int j=0;j<con.size();j++)
            {
                ElementoConstraint constr =(ElementoConstraint) con.get(j);
                constr.updateConstraintPosizione();
            }
        }
        
        /**Restituisce una lista di id dei constraint**/
        public LinkedList getConstraintId()
        {
            if(con.isEmpty())
                return null;
           LinkedList listId = new LinkedList();
           for(int j=0;j<con.size();j++)
            {
                ElementoConstraint constr =(ElementoConstraint) con.get(j);
                listId.add(new Long(constr.getId()));
            }
           return listId;
        }
        
        /**controla se ha constr**/
        public boolean hasConstraint()
        {
            if(con==null)
                return false;
            if(con.isEmpty())
                return false;
            else
                return true;
        }
        
        /**vede se il constr ha una formula boleana oppure una catena
         vera : è un boolean
         false : è un chain**/
        public boolean hasConBoll()
        {
            for(int i=0;i<con.size();i++)
            {
               ElementoConstraint constr =(ElementoConstraint) con.get(i); 
               if(constr.isFutClo() || constr.isFutOp() || constr.isPastClo() || constr.isPre() || constr.isPastOp())
                   return true;
               
            }
            return false;
        }

	/**
	 * setta l'elementoConstraint
	 * 
	 * @param con
	 */
	public void addConstraint(ElementoConstraint constr) {
		boolean bo = this.testAndSet();
		this.con.add(constr);
		this.testAndReset(bo);
	}
        
        /**
	 * setta l'elementoConstraint
	 * 
	 * @param con
	 */
	public void removeConstraint(ElementoConstraint constr) {
                if(con.isEmpty())
                        return;
		boolean bo = this.testAndSet();
                int i=con.indexOf(constr);
		this.con.remove(i);
		this.testAndReset(bo);
	}
        
        /**Restituisce la formula booleana **/
        public String getExpConPre()
        {
            String exp="";
                for(int i=0;i<con.size();i++)
                {
                    ElementoConstraint constr = (ElementoConstraint) con.get(i);
                    if(constr.isPre())
                        exp = constr.getConstraintExpression();
                }
            return exp;
        }
        
        /**Restituisce la formula booleana **/
        public String getExpConPastClo()
        {
            String exp="";
                for(int i=0;i<con.size();i++)
                {
                    ElementoConstraint constr = (ElementoConstraint) con.get(i);
                    if(constr.isPastClo())
                        exp = constr.getConstraintExpression();
                }
            return exp;
        }
        
        /**Restituisce la formula booleana **/
        public String getExpConPastOp()
        {
            String exp="";
                for(int i=0;i<con.size();i++)
                {
                    ElementoConstraint constr = (ElementoConstraint) con.get(i);
                    if(constr.isPastOp())
                        exp = constr.getConstraintExpression();
                }
            return exp;
        }
        
        /**Restituisce la formula booleana **/
        public String getExpConFutClo()
        {
            String exp="";
                for(int i=0;i<con.size();i++)
                {
                    ElementoConstraint constr = (ElementoConstraint) con.get(i);
                    if(constr.isFutClo())
                        exp = constr.getConstraintExpression();
                }
            return exp;
        }
        
        /**Restituisce la formula booleana **/
        public String getExpConFutOp()
        {
            String exp="";
                for(int i=0;i<con.size();i++)
                {
                    ElementoConstraint constr = (ElementoConstraint) con.get(i);
                    if(constr.isFutOp())
                        exp = constr.getConstraintExpression();
                }
            return exp;
        }
        
        /**Restituisce la catena  **/
        public LinkedList getExpConChPastClo()
        {
            LinkedList exp = new LinkedList();
                for(int i=0;i<con.size();i++)
                {
                    ElementoConstraint constr = (ElementoConstraint) con.get(i);
                    if(constr.isChCloPast())
                        exp = constr.getMsgForChain();
                }
            return exp;
        }
        
        /**Restituisce la catena  **/
        public LinkedList getExpConChFutClo()
        {
            LinkedList exp = new LinkedList();
                for(int i=0;i<con.size();i++)
                {
                    ElementoConstraint constr = (ElementoConstraint) con.get(i);
                    if(constr.isChCloFut())
                        exp = constr.getMsgForChain();
                }
            return exp;
        }
        
        /**Restituisce la catena  **/
        public LinkedList getExpConChPastOp()
        {
            LinkedList exp = new LinkedList();
                for(int i=0;i<con.size();i++)
                {
                    ElementoConstraint constr = (ElementoConstraint) con.get(i);
                    if(constr.isChOpPast())
                        exp = constr.getMsgForChain();
                }
            return exp;
        }
        
        /**Restituisce la catena  **/
        public LinkedList getExpConChFutOp()
        {
            LinkedList exp = new LinkedList();
                for(int i=0;i<con.size();i++)
                {
                    ElementoConstraint constr = (ElementoConstraint) con.get(i);
                    if(constr.isChOpFut())
                        exp = constr.getMsgForChain();
                }
            return exp;
        }
        
        /**Restituisce la catena  **/
        public LinkedList getExpConUnPastClo()
        {
            LinkedList exp = new LinkedList();
                for(int i=0;i<con.size();i++)
                {
                    ElementoConstraint constr = (ElementoConstraint) con.get(i);
                    if(constr.isUnCloPast())
                        exp = constr.getMsgForChain();
                }
            return exp;
        }
        
        /**Restituisce la catena  **/
        public LinkedList getExpConUnFutClo()
        {
            LinkedList exp = new LinkedList();
                for(int i=0;i<con.size();i++)
                {
                    ElementoConstraint constr = (ElementoConstraint) con.get(i);
                    if(constr.isUnCloFut())
                        exp = constr.getMsgForChain();
                }
            return exp;
        }
        
        /**Restituisce la catena  **/
        public LinkedList getExpConUnPastOp()
        {
            LinkedList exp = new LinkedList();
                for(int i=0;i<con.size();i++)
                {
                    ElementoConstraint constr = (ElementoConstraint) con.get(i);
                    if(constr.isUnOpPast())
                        exp = constr.getMsgForChain();
                }
            return exp;
        }
        
        /**Restituisce la catena  **/
        public LinkedList getExpConUnFutOp()
        {
            LinkedList exp = new LinkedList();
                for(int i=0;i<con.size();i++)
                {
                    ElementoConstraint constr = (ElementoConstraint) con.get(i);
                    if(constr.isUnOpFut())
                        exp = constr.getMsgForChain();
                }
            return exp;
        }
        
        /**restituisce l'espressione totale **/
        public String getExpConTot()
        {
            String txt="";
                for(int i=0;i<con.size();i++)
                {
                    ElementoConstraint constr = (ElementoConstraint) con.get(i);
                    txt = txt+"&"+constr.getConstraintExpression();
                }
            return txt;
        }
        

        

	/**
	 * preleva l'eleemnto Constraint Collegato
	 * 
	 * @return
	 */
	public LinkedList getListConstraint() {
            if(con.isEmpty())
                return null;
            else
                return con;
	}
        
        /**Controlla se il const passato appartiene al link**/
        public boolean ConInLink(ElementoConstraint constr)
        {
           return con.contains(constr);
        }
        
        
        /**
	 * preleva l'elemento Constraint Collegato
	 * 
	 * @return
	 */
	public ElementoConstraint getConstraint(ElementoConstraint constr) {
            int i;
            if(con==null)
                return null;
            if(con.isEmpty())
                return null;
            try
            { 
                i = con.indexOf(constr);
            } 
            catch (IndexOutOfBoundsException e)
            {
                String s = "Indice fuori dai limiti ammessi \ndentro la classe ListaCnale$removeChannel.\n" + e.toString();
                JOptionPane.showMessageDialog(null,s,"Condizione di errore!",JOptionPane.WARNING_MESSAGE);
                return null;
            }
		
                return (ElementoConstraint)con.get(i);
	}
        
        /**
	 * preleva l'id del loop di cui fa parte
	 * 
	 * @return
	 */
	public long getId_loop() {
		return id_loop;
	}
        
        /**
	 * preleva l'id del parallelo di cui fa parte
	 * 
	 * @return
	 */
	public long getId_par() {
		return id_par;
	}
        
        /**
	 * preleva l'id del simultaneo di cui fa parte
	 * 
	 * @return
	 */
	public long getId_sim() {
		return id_sim;
	}

	public int getProcY() {
		return procY;
	}

	/** ritorna il punto di inizio del link */
	public Point getPointStart() {
		return pStart;
	}

	public void setPointStart(Point p) {
		boolean bo = this.testAndSet();
		pStart.x = p.x;
		pStart.y = p.y;
		this.testAndReset(bo);
	}

	/** ritorna il punto di fine del link */
	public Point getPointEnd() {
		return pEnd;
	}

	public void setPointEnd(Point p) {
		boolean bo = this.testAndSet();
		pEnd.x = p.x;
		pEnd.y = p.y;
		this.testAndReset(bo);
	}

	/** ritorna il valore di strict */
	public boolean isStrict() {
		return strict;
	}
        
        /** setta la variabile strict */
	public void setStrict(boolean value) {
		boolean bo = this.testAndSet();
		strict = value;
		this.testAndReset(bo);
	}
        
        /** ritorna il valore di isComplement */
	public boolean isComplement() {
		return isComplement;
	}
        
        /** setta la variabile isComplement */
	public void setComplement(boolean value) {
		boolean bo = this.testAndSet();
		isComplement = value;
		this.testAndReset(bo);
	}
        
        /** ritorna il valore di simultaneous */
	public boolean isSimultaneous() {
		return isSimultaneous;
	}
        
        /** setta la variabile simultaneous */
	public void setSimultaneous(boolean value,long id) {
		boolean bo = this.testAndSet();
		isSimultaneous = value;
                id_sim = id;
		this.testAndReset(bo);
	}
        
      
       /** setta la variabile loop_operator **/
	public void setLoop(boolean value, int min, int max,long id) {
		boolean bo = this.testAndSet();
		isLoop_op = value;		
                max_Loop = max;
                min_Loop = min;
                this.id_loop = id;
                this.testAndReset(bo);
	}
        
        
        public void setIdLoop(long i)
        {
            this.id_loop = i;
        }
        
        public void setIdPar(long i)
        {
            this.id_par = i;
        }
        
        public void setIdSim(long i)
        {
            this.id_sim = i;
        }
        
       /** ritorna il valore del loop_operator true false*/
	public boolean isLoop() {
		return isLoop;
	} 
        
        public int getMinLoop()
        {
            return this.min_Loop;
        }
        
        public void setMinLoop(int i)
        {
            this.min_Loop = i;
        }
        
        public int getMaxLoop()
        {
            return this.max_Loop;
        }
        
        public void setMaxLoop(int i)
        {
            this.max_Loop = i;
        }
        
        /** ritorna il valore del loop_operator true false*/
	public boolean isLoop_op() {
		return isLoop_op;
	}
        
        /** ritorna il valore di parallel */
	public boolean isParallel() {
		return isParallel;
	}
        
        /** setta la variabile parallel */
	public void setParallel(boolean value,long id) {
            boolean bo = this.testAndSet();
            isParallel = value;
            id_par = id;
            this.testAndReset(bo);
	}               

	/** Settaggio dell'elemento precedente nella scala temporale */
	public void setPrec(ElementoSeqLink prec) {
		boolean bo = this.testAndSet();
		this.prec = prec;
		this.testAndReset(bo);
	}
        
      
        
	/** recupero dell'elemento precedente nella scala temporale */
	public ElementoSeqLink getPrec() {
		return prec;
	}

	/** Restituisce l'elemento temporale (Time's Line) da cui parte il link. */
	public ElementoTime getTimeFrom() {
		if (flussodiretto) {
			return time_one;
		} 
		else {
			return time_two;
		}
	}

	/** Restituisce l'elemento temporale (Time's Line) a cui arriva il link. */
	public ElementoTime getTimeTo() {
		if (flussodiretto) {
			return time_two;
		} 
		else {
			return time_one;
		}
	}
        
        /**indica da che parte è il link nell'operatore parallelo**/
        public boolean getOperatorPar()
        {
            return this.Operator;
        }
        
        /**setta il valore**/
        public void setOpPar(boolean value)
        {
            this.Operator = value;
        }

	/** Restituisce il primo elemento temporale (Time's Line) del link. */
	public ElementoTime getTime_one() {
		return time_one;
	}

	/** Restituisce il secondo elemento temporale (Time's Line) del link. */
	public ElementoTime getTime_two() {
		return time_two;
	}

	/**
	 * Restituisce la posizione all'interno della fascia
	 */
	public int getPosFrom() {
		if (flussodiretto) {
			return pos_one;
		} 
		else {
			return pos_two;
		}
	}
        
        /**
	 * Rimuove la lita dei constr 
	 */
        public void removeAllCon()
        {
            if(this.con.isEmpty())
                return;
            if(this.con==null)
                return;
            this.con = new LinkedList();
        }

	/**
	 * Restituisce la posizione all'interno della fascia
	 */
	public int getPosTo() {
		if (flussodiretto) {
			return pos_two;
		} 
		else {
			return pos_one;
		}
	}

	public Point getPointCon() {
		return pCon;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.ElementoBase#getViewName()
	 */
	public String getViewName() {
		switch (regReqFail) {
			case 3 : if(this.isComplement()){
                            return "e: " +"!"+ getName();
                        }
				return "e: " + getName();
			case 4 :if(this.isComplement()){
                            return "r: " +"!"+ getName();
                        }
				return "r: " + getName();
			case 5 :if(this.isComplement()){
                            return "f: " +"!"+ getName();
                        }
				return "f: " + getName();
			default :if(this.isComplement()){
                            return "e: " +"!"+ getName();
                        }
				return "e: " + getName();
		}
	}

	/**
	 * crea il grafico strict
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param g2D
	 */
	public void creaGraficoStrict(int x1, int y1, int x2, int y2, Graphics2D g2D) {
		Stroke tmpStroke;
		Paint tmpPaint;
		AffineTransform tmpTransform;
		tmpStroke = g2D.getStroke();
		tmpPaint = g2D.getPaint();
		tmpTransform = g2D.getTransform();
		g2D.setColor(Color.black);
		strictGraph = new Line2D.Double(x1, y1, x2, y2);
		g2D.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND, 10.0f, NOT_DASHED, 0));
		g2D.draw(strictGraph);
		g2D.setStroke(tmpStroke);
		g2D.setPaint(tmpPaint);
		g2D.setTransform(tmpTransform);
	}                          
        
	/**
	 * Crea l'oggetto grafico associato al canale_ Il parametro graphicsType
	 * non ? utilizzato, ma necessario perch? il metodo ? dichiarato
	 */
	public void setPosizione(int graphicsType) {
		int x_one = (element_one.getPointMiddle()).x;
		int x_two = (element_two.getPointMiddle()).x;
		int y_one = time_one.getMinY() + pos_one * ElementoTime.hfascia;
		int y_two = time_two.getMinY() + pos_two * ElementoTime.hfascia;
		pCon = new Point(x_one + (x_two - x_one) / 8, y_one);
		if (isLoop) {
			grafico = new GraficoLoopSeqLink(
					element_one.getGrafico(),
					time_one,0,
					flussodiretto,
					this);
		} else {
			grafico = new GraficoCollegamentoSeqLink(
					element_one.getGrafico(),
					element_two.getGrafico(),
					time_one,
					time_two,
					0,
					flussodiretto,
					this);
		}
	}

	/**
	 * Aggiorna la posizione del link in funzione della classe di arrivo e di
	 * partenza e degli istanti di
	 */
	public void updateCanalePosizione() {
		
		
		int x_one = (element_one.getPointMiddle()).x;
		int x_two = (element_two.getPointMiddle()).x;
		int y_one = time_one.getMinY() + pos_one * ElementoTime.hfascia;
		int y_two = time_two.getMinY() + pos_two * ElementoTime.hfascia;
		
		////ezio 2006   - bug fixed
		/*if (getPointStart().x > getPointEnd().x)
			if (x_one > x_two) {
				setPointStart(new Point(x_one, y_one));
				setPointEnd(new Point(x_two, y_two));
			} else {
				setPointStart(new Point(x_two, y_two));
				setPointEnd(new Point(x_one, y_one));
			}
		else if (x_one > x_two) {
			setPointStart(new Point(x_two, y_two));
			setPointEnd(new Point(x_one, y_one));
		} else {
			setPointStart(new Point(x_one, y_one));
			setPointEnd(new Point(x_two, y_two));
		}*/
		
		if(flussodiretto){ ///ezio 2006 
			setPointStart(new Point(x_one, y_one));
			setPointEnd(new Point(x_two, y_two));
		}
		else{
			setPointStart(new Point(x_two, y_two));
			setPointEnd(new Point(x_one, y_one));
		}
		
		///// - fine
		
		
		pCon.x = x_one + (x_two - x_one) / 8;
		pCon.y = y_one;
		if (isLoop) {
			
			((GraficoLoopSeqLink) grafico).updateLoopPosizione(
					this.getPosizione(), 
					flussodiretto);

		} else {
			((GraficoCollegamentoSeqLink) grafico).updateCollegamentoPosizione(
					getPosizione(),
					flussodiretto);
		}
	}

	/**
	 * setta i valori dell'elemento Link
	 * 
	 * @param em
	 *            elementoSeqLink da cui prendere i dati
	 * @param forClone
	 *            true indica che deve essere copiato anche l'identificatore di
	 *            em
	 */
	public void setValue(ElementoSeqLink em, boolean forClone) {
		boolean bo = this.testAndSet();
		if (forClone) {
			setId(em.getId());
		}
		setFlussoDiretto(em.getFlussoDiretto());
		setPosizione(em.getPosizione());
		setName(em.getName());
		this.setPointEnd(em.getPointEnd());
		this.setPointStart(em.getPointStart());
		this.setPrec(em.getPrec());
		(grafico).updateLineaProprieta(em.getGrafico().getLineColor(), em
				.getGrafico().getLineWeight(), em.getGrafico().getLineTheme());
		(grafico).updateTestoProprieta(/* em.getGrafico().getText(), */
		em.getGrafico().getTextColor(), em.getGrafico().getTextFont(), em
				.getGrafico().getFontStyle(), em.getGrafico().getFontSize());
		isLoop = em.isLoop;
		if (isLoop) {
			((GraficoLoopSeqLink) (grafico)).setRotazione(((GraficoLoopSeqLink) em
					.getGrafico()).getRotazione());
		}
		this.testAndReset(bo);
	}

	/** Imposta il tipo del messaggio. */
	public boolean setTipo(int j) {
		boolean ret = false;
		boolean bo = testAndSet(); //controllo messaggi
		switch (j) {
			case SYNCHRONOUS :
				ret = super.setTipo(SYNCHRONOUS);
				break;
			case ASYNCHRONOUS :
				ret = super.setTipo(ASYNCHRONOUS);
				break;
			default :
				ret = super.setTipo(SYNCHRONOUS);
		}
		testAndReset(bo);
		return ret;
	}

	/** Imposta il tipo del messaggio. */
	public void setRegReqFail(int j) {
		boolean bo = testAndSet();
		switch (j) {
			case REGULAR :
				regReqFail = REGULAR;
				break;
			case REQUIRED :
				regReqFail = REQUIRED;
				break;
			case FAIL :
				regReqFail = FAIL;
				break;
			default :
				regReqFail = REGULAR;
		}
		testAndReset(bo);
	}

	/** Restituisce il tipo del messaggio. */
	public int getRegReqFail() {
		return regReqFail;
	}

	/** Verifica se si tratta di un loop. */
	public boolean ctrlIfLoop() {
		return isLoop;
	}

	/**
	 * Clonazione dell'oggetto. 
	 */
	public ElementoSeqLink cloneSeqLink(ListaClasse lc, ListaTime lt) {
		ElementoProcessoStato ClasseDa = getElementFrom();
		ElementoProcessoStato ClasseA = getElementTo();
		String NomeClasseDa = ClasseDa.getName();
		String NomeClasseA = ClasseA.getName();
		ElementoTime TimeDa = getTimeFrom();
		ElementoTime TimeA = getTimeTo();
		long TempoTimeDa = TimeDa.getTime();
		long TempoTimeA = TimeA.getTime();
		ElementoSeqLink cloned;
		ClasseDa = lc.getElement(NomeClasseDa);
		ClasseA = lc.getElement(NomeClasseA);
		TimeDa = lt.getElement(TempoTimeDa);
		TimeA = lt.getElement(TempoTimeA);
		cloned = new ElementoSeqLink(getName(), ClasseDa, ClasseA, TimeDa,
				TimeA, getPosFrom(), getPosTo(), getTipo(), this.regReqFail,
				"link_", true, this.getId(), null);
		cloned.setValue(this, true);

		return cloned;
	}

	/**
	 * crea un clone perfetto dell'oggetto
	 * 
	 * @return il clone dell'oggetto
	 */
	public ElementoSeqLink cloneSeqLink() {
		ElementoSeqLink cloned;
		ElementoClasse ClasseA = null;
		ElementoClasse ClasseDa = ((ElementoClasse) getElementFrom())
				.cloneClasse();
		if (isLoop) {
			ClasseA = ClasseDa;
		} else {
			ClasseA = ((ElementoClasse) getElementTo()).cloneClasse();
		}

		ElementoTime TimeDa = getTimeFrom();
		ElementoTime TimeA = getTimeTo();
		if (TimeDa == TimeA) {
			TimeA = TimeDa.cloneTime();
			TimeDa = TimeA; //stessi cloni
		} else {
			TimeDa = TimeDa.cloneTime();
			TimeA = TimeA.cloneTime();
		}

		cloned = new ElementoSeqLink(getName(), ClasseDa, ClasseA, TimeDa,
				TimeA, getPosFrom(), getPosTo(), getTipo(), this.regReqFail,
				"link_", true, this.getId(), null);
		cloned.setValue(this, true);

		return cloned;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.ImpUpdate#informPreUpdate()
	 */
	public void informPreUpdate() {
		if (sendMsg) { //posso inviare il messaggio
			if (updateEp != null) {
				updateEp.informaPreUpdate(this.cloneSeqLink());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.ImpUpdate#informPostUpdate()
	 */
	public void informPostUpdate() {
		if (sendMsg) { //posso inviare il messaggio
			if (updateEp != null) {
				updateEp.informaPostUpdate(this.cloneSeqLink());
			}
		}
	}
}