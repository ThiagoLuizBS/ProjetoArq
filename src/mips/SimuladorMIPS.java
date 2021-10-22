/* 
 * Projeto MIPS
 * Equipe: André Felipe, Ricardo Alves e Thiago Luiz
 * 
 * Label Start será tratado na parte 3 do projeto.
 * 
 *	comentar
 */
package mips;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SimuladorMIPS{
	private String nome;
	private int valor;	
	
	public SimuladorMIPS() {
		this.nome = null;
		this.valor = 0;
	}
	
	public SimuladorMIPS(String nome) {
		this.nome = nome;
		this.valor = 0;
	}

	public String getNome() {
		return nome;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	//registradores
	public ArrayList<SimuladorMIPS> registradores(){
		ArrayList<SimuladorMIPS> r = new ArrayList<>();
		r.add(new SimuladorMIPS("$0"));
		r.add(new SimuladorMIPS("$1"));
		r.add(new SimuladorMIPS("$2"));
		r.add(new SimuladorMIPS("$3"));
		r.add(new SimuladorMIPS("$4"));
		r.add(new SimuladorMIPS("$5"));
		r.add(new SimuladorMIPS("$6"));
		r.add(new SimuladorMIPS("$7"));
		r.add(new SimuladorMIPS("$8"));
		r.add(new SimuladorMIPS("$9"));
		r.add(new SimuladorMIPS("$10"));
		r.add(new SimuladorMIPS("$11"));
		r.add(new SimuladorMIPS("$12"));
		r.add(new SimuladorMIPS("$13"));
		r.add(new SimuladorMIPS("$14"));
		r.add(new SimuladorMIPS("$15"));
		r.add(new SimuladorMIPS("$16"));
		r.add(new SimuladorMIPS("$17"));
		r.add(new SimuladorMIPS("$18"));
		r.add(new SimuladorMIPS("$19"));
		r.add(new SimuladorMIPS("$20"));
		r.add(new SimuladorMIPS("$21"));
		r.add(new SimuladorMIPS("$22"));
		r.add(new SimuladorMIPS("$23"));
		r.add(new SimuladorMIPS("$24"));
		r.add(new SimuladorMIPS("$25"));
		r.add(new SimuladorMIPS("$26"));
		r.add(new SimuladorMIPS("$27"));
		r.add(new SimuladorMIPS("$28"));
		r.add(new SimuladorMIPS("$29"));
		r.add(new SimuladorMIPS("$30"));
		r.add(new SimuladorMIPS("$31"));
		r.add(new SimuladorMIPS("hi"));
		r.add(new SimuladorMIPS("lo"));
		return r;
	}
	
	//transformar hexadecimal para binario
	public String hexToBin(String hex){
	    String bin = "";
	    String binFragment = "";
	    int iHex;
	    hex = hex.trim();
	    hex = hex.replaceFirst("0x", "");

	    for(int i = 0; i < hex.length(); i++){
	        iHex = Integer.parseInt(""+hex.charAt(i),16);
	        binFragment = Integer.toBinaryString(iHex);

	        while(binFragment.length() < 4){
	            binFragment = "0" + binFragment;
	        }
	        bin += binFragment;
	    }
	    return bin;
	} 
	
	//transformar binario para decimal
	private int binDecimal(String x) {
		boolean ehNegativo = false;
		if(x.substring(0, 1).equalsIgnoreCase("1") && x.length() >= 16) {
			x = this.complementoa2(x);
			ehNegativo = true;
		}
		
		int numero = Integer.parseInt(x, 2);
		if(ehNegativo == true) {
			numero = numero * (-1);
		}
		return numero;
	}
	
	private String decimalBin(int no) {
		boolean c2 = false;
		if(no < 0) {
			no = no * (-1);
			c2 = true;
		}
	    int i = 0, temp[] = new int[100];
	    String retorno = "";
	    while (no > 0) {
	        temp[i++] = no % 2;
	        no /= 2;
	    }
	    for (int j = i - 1; j >= 0; j--) {
	        retorno = retorno + temp[j];
	    }
	    
	    if(c2 == true) {
	    	retorno = this.complementoa2(retorno);
	    }
	    
	    if(retorno == "") {
	    	retorno = "0";
	    }

	    return retorno;
	}
	
	private String complementoa2(String bin) {
		int cont = bin.length();
		String aux = "";
		for(int i = 0; i < cont; i++) {
			if(bin.substring(i, i+1).equalsIgnoreCase("1")) {
				aux = aux + "0";
			} else {
				aux = aux + "1";
			}
		}
		
		bin = aux;
		aux = "";
		
		for(int i = cont; i > 0; i--) {
			if(bin.substring(i-1, i).equalsIgnoreCase("0")) {
				bin = bin.substring(0, cont-1) + "1";
				i = 0;
			} else {
				bin = bin.substring(0, cont-1) + "0";
			}
		}
		return bin;
	}
	
	/* Instrucoes tipo R 
	 * opcode 6 bits / rs e rt (operando) 5 bits cada/ rd (resultante) 5 bits / sh 5 bits / opcode funcao 6 bits
	 * 
	 *  Instrucoes tipo I 
	 *  opcode 6 bits / rs source or base 5 bits / rt destination or data 5 bits / operand ou endereço offset 16 bits
	 * 
	 *  Instrucoes tipo J
	 *  opcode 6 bits / memory word adress 26 bits
	 */	 
	
	//achar a operação pelo opcode: syscall, R, I ou J
	public String acharOp(String b, ArrayList<SimuladorMIPS> lista) {
		String res = "";
		
		if(b.equalsIgnoreCase("00000000000000000000000000001100")) {
			//tipo syscall
			res = syscall(res);
		} else if(b.substring(0,6).equalsIgnoreCase("000000")) {
			//tipo R
			res = tipoR(res,b, lista);
		} else if(b.substring(0,6).equalsIgnoreCase("000010") ||
				b.substring(0,6).equalsIgnoreCase("000011")) {
			//tipo J
			res = tipoJ(res,b, lista);
		} else {
			//tipo I
			res = tipoI(res,b, lista);
		}
		
		return res;
	}
	
	//comando syscall
	private String syscall(String res) {
		res = "syscall";
		return res;
	}
	
	//tipos de registradores R que possuem 3 argumentos finais
	private String registradorRTriplo(String res, String b) {
		if(res == "sllv" || res == "srlv" || res == "srav") {
			res = res + " ";
			res = res + "$" + binDecimal(b.substring(16, 21)) + ", ";
			res = res + "$" + binDecimal(b.substring(11, 16)) + ", ";
			res = res + "$" + binDecimal(b.substring(6, 11));
		} else {
			res = res + " ";		
			res = res + "$" + binDecimal(b.substring(16, 21)) + ", ";
			res = res + "$" + binDecimal(b.substring(6, 11)) + ", ";
			res = res + "$" + binDecimal(b.substring(11, 16));
		}
		return res;
	}
	
	//tipos de registradores R que possuem 2 argumentos finais
	private String registradorRDuplo(String res, String b) {
		if(res == "mult" ||	res == "multu" || res == "div" || res == "divu") {
			res = res + " ";
			res = res + "$" + binDecimal(b.substring(6, 11)) + ", ";
			res = res + "$" + binDecimal(b.substring(11, 16));
		} else {
			res = res + " ";
			res = res + "$" + binDecimal(b.substring(16, 21)) + ", ";
			res = res + "$" + binDecimal(b.substring(11, 16)) + ", ";
			res = res + binDecimal(b.substring(21, 26));
		}		
		return res;
	}
	
	//tipos de registradores R que possuem 1 único argumento final
	private String registradorRUnico(String res, String b) {
		if(res == "jr") {
			res = res + " ";		
			res = res + "$" + binDecimal(b.substring(6, 11));
		} else {
			res = res + " ";		
			res = res + "$" + binDecimal(b.substring(16, 21));
		}		
		return res;
	}
	
	//achar a função R comparando com os ultimos 6 bits
	private String tipoR(String res, String b, ArrayList<SimuladorMIPS> lista) {
		if(b.substring(26, 32).equalsIgnoreCase("100000")) {
			res = "add";
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			int id3 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));		
			lista.get(id1).setValor(lista.get(id2).getValor() + lista.get(id3).getValor());
			
		} else if(b.substring(26, 32).equalsIgnoreCase("100010")) {
			res = "sub";
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			int id3 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));	
			lista.get(id1).setValor(lista.get(id2).getValor() - lista.get(id3).getValor());
			
		} else if(b.substring(26, 32).equalsIgnoreCase("101010")) {
			res = "slt";
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			int id3 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));
			
			if(lista.get(id2).getValor() < lista.get(id3).getValor()) {
				lista.get(id1).setValor(1);
			} else lista.get(id1).setValor(0);
			
		} else if(b.substring(26, 32).equalsIgnoreCase("100100")) {
			res = "and";
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			int id3 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));
			String and = "";
			String rt = decimalBin(lista.get(id2).getValor()) + "";
			String rs = decimalBin(lista.get(id3).getValor()) + "";
			int tam = rt.length();
			int tam2 = rs.length();
			if(tam > tam2) {
				for(int i = tam2; i < tam; i++) {
					rs = "0" + rs;
				}
			} else if(tam < tam2) {
				for(int i = tam; i < tam2; i++) {
					rt = "0" + rt;
				}
			}
			
			int cont = rt.length();
			for(int i = 0; i < cont; i++) {		
				if(rt.substring(0+i,0+i+1).equalsIgnoreCase(rs.substring(0+i,0+i+1)) && rt.substring(0+i,0+i+1).equalsIgnoreCase("1")) {
					and = and + "1";
				} else and = and + "0";
			}

			lista.get(id1).setValor(binDecimal(and));
			
		} else if(b.substring(26, 32).equalsIgnoreCase("100101")) {
			res = "or";	
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			int id3 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));
			String or = "";
			String rt = decimalBin(lista.get(id2).getValor()) + "";
			String rs = decimalBin(lista.get(id3).getValor()) + "";
			
			int tam = rt.length();
			int tam2 = rs.length();
			if(tam > tam2) {
				for(int i = tam2; i < tam; i++) {
					rs = "0" + rs;
				}
			} else if(tam < tam2) {
				for(int i = tam; i < tam2; i++) {
					rt = "0" + rt;
				}
			}
			
			int cont = rt.length();
			for(int i = 0; i < cont; i++) {		
				if(rt.substring(0+i,0+i+1).equalsIgnoreCase(rs.substring(0+i,0+i+1)) && rt.substring(0+i,0+i+1).equalsIgnoreCase("0")) {
					or = or + "0";
				} else or = or + "1";
			}
			
			lista.get(id1).setValor(binDecimal(or));	
			
		} else if(b.substring(26, 32).equalsIgnoreCase("100110")) {
			res = "xor";
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			int id3 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));
			String xor = "";
			String rt = decimalBin(lista.get(id2).getValor()) + "";
			String rs = decimalBin(lista.get(id3).getValor()) + "";
			
			int tam = rt.length();
			int tam2 = rs.length();
			if(tam > tam2) {
				for(int i = tam2; i < tam; i++) {
					rs = "0" + rs;
				}
			} else if(tam < tam2) {
				for(int i = tam; i < tam2; i++) {
					rt = "0" + rt;
				}
			}
			
			int cont = rt.length();
			for(int i = 0; i < cont; i++) {		
				if(rt.substring(0+i,0+i+1).equalsIgnoreCase(rs.substring(0+i,0+i+1))) {
					xor = xor + "0";
				} else xor = xor + "1";
			}
			
			lista.get(id1).setValor(binDecimal(xor));
			
		} else if(b.substring(26, 32).equalsIgnoreCase("100111")) {
			res = "nor";
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			int id3 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));
			String nor = "";
			String rt = decimalBin(lista.get(id2).getValor()) + "";
			String rs = decimalBin(lista.get(id3).getValor()) + "";
			
			int tam = rt.length();
			int tam2 = rs.length();
			if(tam > tam2) {
				for(int i = tam2; i < tam; i++) {
					rs = "0" + rs;
				}
			} else if(tam < tam2) {
				for(int i = tam; i < tam2; i++) {
					rt = "0" + rt;
				}
			}
			
			int cont = rt.length();
			for(int i = 0; i < cont; i++) {		
				if(rt.substring(0+i,0+i+1).equalsIgnoreCase(rs.substring(0+i,0+i+1)) && rt.substring(0+i,0+i+1).equalsIgnoreCase("0")) {
					nor = nor + "1";
				} else nor = nor + "0";
			}
			
			lista.get(id1).setValor(binDecimal(nor));
		} else if(b.substring(26, 32).equalsIgnoreCase("010000")) {
			res = "mfhi";
			res = registradorRUnico(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(16, 21)));
			int hi = lista.get(this.acharRegs(lista, "hi")).getValor();
			lista.get(id1).setValor(hi);
			
		} else if(b.substring(26, 32).equalsIgnoreCase("010010")) {
			res = "mflo";
			res = registradorRUnico(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(16, 21)));
			int lo = lista.get(this.acharRegs(lista, "lo")).getValor();
			lista.get(id1).setValor(lo);
		} else if(b.substring(26, 32).equalsIgnoreCase("100001")) {
			res = "addu";	
			res = registradorRTriplo(res,b);
			//nao estamos verificando overflow ainda
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			int id3 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));		
			lista.get(id1).setValor(lista.get(id2).getValor() + lista.get(id3).getValor());
			
		} else if(b.substring(26, 32).equalsIgnoreCase("100011")) {
			res = "subu";	
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			int id3 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));	
			lista.get(id1).setValor(lista.get(id2).getValor() - lista.get(id3).getValor());
			
		} else if(b.substring(26, 32).equalsIgnoreCase("011000")) {
			res = "mult";	
			res = registradorRDuplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));
			//int hi = this.acharRegs(lista, "hi");
			int lo = this.acharRegs(lista, "lo");
			
			lista.get(lo).setValor(lista.get(id1).getValor() * lista.get(id2).getValor());
			
		} else if(b.substring(26, 32).equalsIgnoreCase("011001")) {
			res = "multu";	
			res = registradorRDuplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));
			//int hi = this.acharRegs(lista, "hi");
			int lo = this.acharRegs(lista, "lo");
			
			lista.get(lo).setValor(lista.get(id1).getValor() * lista.get(id2).getValor());
			
		} else if(b.substring(26, 32).equalsIgnoreCase("011010")) {
			res = "div";
			res = registradorRDuplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));
			int hi = this.acharRegs(lista, "hi");
			int lo = this.acharRegs(lista, "lo");
			
			if(lista.get(id1).getValor() != 0 && lista.get(id2).getValor() != 0) {
				lista.get(hi).setValor(lista.get(id1).getValor() % lista.get(id2).getValor());
				lista.get(lo).setValor(lista.get(id1).getValor() / lista.get(id2).getValor());
			} else {
				lista.get(hi).setValor(0);
				lista.get(lo).setValor(0);
			}			
		} else if(b.substring(26, 32).equalsIgnoreCase("011011")) {
			res = "divu";
			res = registradorRDuplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));
			int hi = this.acharRegs(lista, "hi");
			int lo = this.acharRegs(lista, "lo");
			
			if(lista.get(id1).getValor() != 0 && lista.get(id2).getValor() != 0) {
				lista.get(hi).setValor(lista.get(id1).getValor() % lista.get(id2).getValor());
				lista.get(lo).setValor(lista.get(id1).getValor() / lista.get(id2).getValor());
			} else {
				lista.get(hi).setValor(0);
				lista.get(lo).setValor(0);
			}	
			
		} else if(b.substring(26, 32).equalsIgnoreCase("000000")) {
			res = "sll";
			res = registradorRDuplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));
			String rt = decimalBin(lista.get(id2).getValor()) + "";			
			int shift = binDecimal(b.substring(21, 26));
			
			for(int i = 0; i < shift; i++) {
				rt = rt + "0";
			}
			
			lista.get(id1).setValor(binDecimal(rt));			
			
		} else if(b.substring(26, 32).equalsIgnoreCase("000010")) {
			res = "srl";
			res = registradorRDuplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));
			String rt = decimalBin(lista.get(id2).getValor()) + "";			
			int shift = binDecimal(b.substring(21, 26));
			int cont = rt.length();
			String aux = "";
			
			for(int i = 0; i < shift; i++) {
				rt = "0" + rt;
			}
			
			for(int i = 0; i < cont; i++) {
				aux = aux + rt.substring(i, i+1);
			}
			lista.get(id1).setValor(binDecimal(aux));
			
		} else if(b.substring(26, 32).equalsIgnoreCase("000011")) {
			res = "sra";
			res = registradorRDuplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));
			String rt = decimalBin(lista.get(id2).getValor()) + "";	
			int shift = binDecimal(b.substring(21, 26));
			int cont = rt.length();
			String aux = "";
			
			if(lista.get(id2).getValor() >= 0) {
				for(int i = 0; i < shift; i++) {
					rt = "0" + rt;
				}
				
				for(int i = 0; i < cont; i++) {
					aux = aux + rt.substring(i, i+1);
				}
			} else {				
				for(int i = 0; i < shift; i++) {
					rt = "1" + rt;
				}
				
				for(int i = 0; i < cont; i++) {
					aux = aux + rt.substring(i, i+1);
				}
			}
			lista.get(id1).setValor(binDecimal(aux));
			
		} else if(b.substring(26, 32).equalsIgnoreCase("000100")) {
			res = "sllv";	
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));
			int id3 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			String rt = decimalBin(lista.get(id2).getValor()) + "";			
			int shift = lista.get(id3).getValor();			
			
			for(int i = 0; i < shift; i++) {
				rt = rt + "0";
			}
			
			lista.get(id1).setValor(binDecimal(rt));
			
		} else if(b.substring(26, 32).equalsIgnoreCase("000110")) {
			res = "srlv";	
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));
			int id3 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			String rt = decimalBin(lista.get(id2).getValor()) + "";			
			int shift = lista.get(id3).getValor();
			int cont = rt.length();
			String aux = "";
			
			for(int i = 0; i < shift; i++) {
				rt = "0" + rt;
			}
			
			for(int i = 0; i < cont; i++) {
				aux = aux + rt.substring(i, i+1);
			}
			lista.get(id1).setValor(binDecimal(aux));
			
		} else if(b.substring(26, 32).equalsIgnoreCase("000111")) {
			res = "srav";
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));
			int id3 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			String rt = decimalBin(lista.get(id2).getValor()) + "";	
			int shift = lista.get(id3).getValor();
			int cont = rt.length();
			String aux = "";
			
			if(lista.get(id2).getValor() >= 0) {
				for(int i = 0; i < shift; i++) {
					rt = "0" + rt;
				}
				
				for(int i = 0; i < cont; i++) {
					aux = aux + rt.substring(i, i+1);
				}
			} else {				
				for(int i = 0; i < shift; i++) {
					rt = "1" + rt;
				}
				
				for(int i = 0; i < cont; i++) {
					aux = aux + rt.substring(i, i+1);
				}
			}
			lista.get(id1).setValor(binDecimal(aux));
			
		} else if(b.substring(26, 32).equalsIgnoreCase("001000")) {
			res = "jr";		
			res = registradorRUnico(res,b);
		} 		
		return res;		
	}
	
	//tipos de registradores I que possuem 3 argumentos finais
	private String registradorITriplo(String res, String b) {
		if(res == "addi" || res == "slti" || res == "andi" || res == "ori" || res == "xori" || res == "addiu") {
			res = res + " ";
			res = res + "$" + binDecimal(b.substring(11, 16)) + ", ";
			res = res + "$" + binDecimal(b.substring(6, 11)) + ", ";
			res = res + binDecimal(b.substring(16,32));
		} else if(res == "beq" || res == "bne") {
			res = res + " ";
			res = res + "$" + binDecimal(b.substring(6, 11)) + ", ";
			res = res + "$" + binDecimal(b.substring(11, 16)) + ", ";
			//o start não esta sendo tratado ainda, apenas retornando o start
			res = res + "start";
		}
		return res;
	}
	
	//tipos de registradores I que possuem 2 argumentos finais
	private String registradorIDuplo(String res, String b) {
		if(res == "lui") {
			res = res + " ";
			res = res + "$" + binDecimal(b.substring(11, 16)) + ", ";
			res = res + binDecimal(b.substring(16,32));
		} else if(res == "lw" || res == "sw" || res == "lb" || res == "lbu" || res == "sb") {
			res = res + " ";
			res = res + "$" + binDecimal(b.substring(11, 16)) + ", ";
			res = res + binDecimal(b.substring(16,32)) + "(";
			res = res + "$" + binDecimal(b.substring(6, 11)) + ")";
		} else if(res == "bltz") {
			res = res + " ";
			res = res + "$" + binDecimal(b.substring(6, 11)) + ", ";
			if(b.substring(16,32).equalsIgnoreCase("1111111111101110")){
				res = res + "start";
			}
		}
		return res;
	}
	
	//achar a função I comparando com os primeiros 6 bits
	private String tipoI(String res, String b, ArrayList<SimuladorMIPS> lista) {
		if(b.substring(0,6).equalsIgnoreCase("001111")) {
			res = "lui";
			res = registradorIDuplo(res,b);
		} else if(b.substring(0,6).equalsIgnoreCase("001000")) {
			res = "addi";
			res = registradorITriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));		
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			int num = binDecimal(b.substring(16, 32));
			
			lista.get(id1).setValor(lista.get(id2).getValor() + num);
			
		} else if(b.substring(0,6).equalsIgnoreCase("001010")) {
			res = "slti";	
			res = registradorITriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			int num = binDecimal(b.substring(16, 32));
			
			if(lista.get(id2).getValor() < num) {
				lista.get(id1).setValor(1);
			} else lista.get(id1).setValor(0);
			
		} else if(b.substring(0,6).equalsIgnoreCase("001100")) {
			res = "andi";
			res = registradorITriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			int num = binDecimal(b.substring(16, 32));
			
			String andi = "";
			String rt = decimalBin(lista.get(id2).getValor()) + "";
			String rs = decimalBin(num) + "";
			
			int tam = rt.length();
			int tam2 = rs.length();
			if(tam > tam2) {
				for(int i = tam2; i < tam; i++) {
					rs = "0" + rs;
				}
			} else if(tam < tam2) {
				for(int i = tam; i < tam2; i++) {
					rt = "0" + rt;
				}
			}
			
			int cont = rt.length();
			for(int i = 0; i < cont; i++) {		
				if(rt.substring(0+i,0+i+1).equalsIgnoreCase(rs.substring(0+i,0+i+1)) && rt.substring(0+i,0+i+1).equalsIgnoreCase("1")) {
					andi = andi + "1";
				} else andi = andi + "0";
			}
			
			lista.get(id1).setValor(binDecimal(andi));
			
		} else if(b.substring(0,6).equalsIgnoreCase("001101")) {
			res = "ori";
			res = registradorITriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			int num = binDecimal(b.substring(16, 32));
			String ori = "";
			String rt = decimalBin(lista.get(id2).getValor()) + "";
			String rs = decimalBin(num) + "";
			
			int tam = rt.length();
			int tam2 = rs.length();
			if(tam > tam2) {
				for(int i = tam2; i < tam; i++) {
					rs = "0" + rs;
				}
			} else if(tam < tam2) {
				for(int i = tam; i < tam2; i++) {
					rt = "0" + rt;
				}
			}
			
			int cont = rt.length();
			for(int i = 0; i < cont; i++) {		
				if(rt.substring(0+i,0+i+1).equalsIgnoreCase(rs.substring(0+i,0+i+1)) && rt.substring(0+i,0+i+1).equalsIgnoreCase("0")) {
					ori = ori + "0";
				} else ori = ori + "1";
			}
			
			lista.get(id1).setValor(binDecimal(ori));	
			
		} else if(b.substring(0,6).equalsIgnoreCase("001110")) {
			res = "xori";
			res = registradorITriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			int num = binDecimal(b.substring(16, 32));
			String xori = "";
			String rt = decimalBin(lista.get(id2).getValor()) + "";
			String rs = decimalBin(num) + "";
			
			int tam = rt.length();
			int tam2 = rs.length();
			if(tam > tam2) {
				for(int i = tam2; i < tam; i++) {
					rs = "0" + rs;
				}
			} else if(tam < tam2) {
				for(int i = tam; i < tam2; i++) {
					rt = "0" + rt;
				}
			}
			
			int cont = rt.length();
			for(int i = 0; i < cont; i++) {		
				if(rt.substring(0+i,0+i+1).equalsIgnoreCase(rs.substring(0+i,0+i+1))) {
					xori = xori + "0";
				} else xori = xori + "1";
			}
			
			lista.get(id1).setValor(binDecimal(xori));
			
		} else if(b.substring(0,6).equalsIgnoreCase("100011")) {
			res = "lw";	
			res = registradorIDuplo(res,b);
		} else if(b.substring(0,6).equalsIgnoreCase("101011")) {
			res = "sw";	
			res = registradorIDuplo(res,b);
		} else if(b.substring(0,6).equalsIgnoreCase("000001")) {
			res = "bltz";
			res = registradorIDuplo(res,b);
		} else if(b.substring(0,6).equalsIgnoreCase("000100")) {
			res = "beq";	
			res = registradorITriplo(res,b);
		} else if(b.substring(0,6).equalsIgnoreCase("000101")) {
			res = "bne";
			res = registradorITriplo(res,b);
		} else if(b.substring(0,6).equalsIgnoreCase("001001")) {
			res = "addiu";
			res = registradorITriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimal(b.substring(11, 16)));		
			int id2 = this.acharRegs(lista, "$" + binDecimal(b.substring(6, 11)));
			int num = binDecimal(b.substring(16, 32));
			
			lista.get(id1).setValor(lista.get(id2).getValor() + num);
			
		} else if(b.substring(0,6).equalsIgnoreCase("100000")) {
			res = "lb";	
			res = registradorIDuplo(res,b);
		} else if(b.substring(0,6).equalsIgnoreCase("100100")) {
			res = "lbu";
			res = registradorIDuplo(res,b);
		} else if(b.substring(0,6).equalsIgnoreCase("101000")) {
			res = "sb";			
			res = registradorIDuplo(res,b);
		}
		return res;
	}
	
	//achar a função do tipo J comparando os 6 primeiros bits
	private String tipoJ(String res, String b, ArrayList<SimuladorMIPS> lista) {
		if(b.substring(0,6).equalsIgnoreCase("000010")) {
			res = "j";
		} else {
			res = "jal";
		}
		res = res + " ";
		//o start não esta sendo tratado ainda, apenas retornando o start
		res = res + "start";
		return res;
	}
	
	//printar registrador	
	private String toStringRegs(ArrayList<SimuladorMIPS> lista) {
		String stringRegs = "REGS[";
		
		for(int i = 0; i < 32; i++) {
			stringRegs = stringRegs + lista.get(i).getNome() + "=" + lista.get(i).getValor();
			if(i < 31) {
				stringRegs = stringRegs + ";";
			}					
		}
		
		stringRegs = stringRegs + "]";
		
		return stringRegs;
	}
	
	//verificar qual e o registrador e fazer a alteração
	
	//achar qual é o resgistrador
	private int acharRegs(ArrayList<SimuladorMIPS> lista, String nome) {
		int indiceRegs = 0;
		for(int i = 0; i < lista.size(); i++) {
			if(lista.get(i).getNome().equalsIgnoreCase(nome)) {
				indiceRegs = i;
			}
		}
		
		return indiceRegs;
	}
	
	public static void main(String[] args) {
		SimuladorMIPS simulador = new SimuladorMIPS();
		ArrayList<SimuladorMIPS> lista = simulador.registradores();		
		
		String localDir = System.getProperty("user.dir");
		
		//colocar o caminho de entrada correto
		String caminhoEntrada = localDir + "\\entrada.txt";
		//colocar o caminho de saida correto
		String caminhoSaida = localDir + "\\saida.txt";
		ArrayList<String> entrada = new ArrayList<>();
		ArrayList<String> saida = new ArrayList<>();
		
		try {
		      FileReader arq = new FileReader(caminhoEntrada);
		      BufferedReader lerArq = new BufferedReader(arq);
		      String linha = lerArq.readLine(); // lê a primeira linha
		      
		      while (linha != null) {
		        entrada.add(linha);
		        linha = lerArq.readLine(); // lê da segunda até a última linha		        
		      }
		      arq.close();
		    } catch (IOException e) {
		        System.out.printf("Erro na abertura do arquivo: %s.\n", e.getMessage());
		}
		
		FileWriter arq2 = null;
		try {			
			arq2 = new FileWriter(caminhoSaida);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PrintWriter gravarArq = new PrintWriter(arq2);
		
		for(int i = 0; i < entrada.size(); i++) {
			if(entrada.get(i) != null && entrada.get(i).isEmpty() == false && entrada.get(i) != "\n") {
				//se a linha de comando não é nula nem vazia, vai para a função acharOp e retorna a string em comandos assembly
				String assembly = simulador.acharOp(simulador.hexToBin(entrada.get(i)),lista);
				saida.add(assembly);
				//System.out.println(assembly);
				
				if(assembly.contains("add") || assembly.contains("sub") || assembly.contains("mult") || assembly.contains("div") || assembly.contains("addu") || assembly.contains("subu") ||
						assembly.contains("multu") || assembly.contains("divu") || assembly.contains("addiu") || assembly.contains("and") || assembly.contains("or") || assembly.contains("xor") ||
						assembly.contains("nor") || assembly.contains("andi") || assembly.contains("ori") || assembly.contains("xori") || assembly.contains("addi") || assembly.contains("sll") ||
						assembly.contains("srl") || assembly.contains("sra") || assembly.contains("srlv") || assembly.contains("sllv") || assembly.contains("srav")) {
					saida.add(simulador.toStringRegs(lista));
					//System.out.println(simulador.toStringRegs(lista));
				}
			}
		}
		
		for(int i = 0; i < saida.size(); i++) {			
				gravarArq.printf(saida.get(i) + "\n");
		}
		
		try {
			arq2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
