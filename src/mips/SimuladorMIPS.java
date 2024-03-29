/* 
 * Projeto MIPS - Parte 3
 * Equipe: Andr� Felipe, Ricardo Alves e Thiago Luiz
 * 
 *	PC (Program Counter) adicionado, ele recebe o endere�o inicial de 4194304 (0x00400000)
 *
 *  Memoria criada, com 1024 entradas de 8 bits
 *  Todo dado salvo na mem�ria em hexadecimal (at� 2 casas por entrada) e nos registradores em decimal
 *  
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
	private String valorHexa;
	
	public SimuladorMIPS() {
		this.nome = null;
		this.valor = 0;
		this.valorHexa = null;
	}
	
	public SimuladorMIPS(String nome) {
		this.nome = nome;
		this.valor = 0;
		this.valorHexa = null;
	}
	
	public SimuladorMIPS(String nome, int endereco) {
		this.nome = nome;
		this.valor = endereco;
		this.valorHexa = null;
	}

	public SimuladorMIPS(String nome, String hexa) {
		this.nome = nome;
		this.valor = 0;
		this.valorHexa = hexa;
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
	
	public String getValorHexa() {
		return valorHexa;
	}

	public void setValorHexa(String valor) {
		this.valorHexa = valor;
	}

	//Banco de Registradores inicializados com zero
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
		r.add(new SimuladorMIPS("pc", 4194304));
		r.add(new SimuladorMIPS("hi"));
		r.add(new SimuladorMIPS("lo"));
		return r;
	}
	
	public ArrayList<SimuladorMIPS> memoria(){
		ArrayList<SimuladorMIPS> m = new ArrayList<>();
		for(int i = 0; i < 1024; i++) {
			String x = i + "";
			m.add(new SimuladorMIPS(x,"0x00"));
		}
		return m;
	}
	
	//Fun�ao que transforma a entrada hexadecimal para binario
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
	
	//fun�ao que converte de binario para decimal
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
	
	//fun�ao que converte de binario para decimal, sem verificar se o numero � negativo
		private int binDecimalPos(String x) {		
			int numero = Integer.parseInt(x, 2);
			return numero;
		}
	
	//fun�ao que converte de decimal para binario
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
	
	//Fun��o que faz o complemento a 2 de n�meros negativos
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
	 *  opcode 6 bits / rs source or base 5 bits / rt destination or data 5 bits / operand ou endere�o offset 16 bits
	 * 
	 *  Instrucoes tipo J
	 *  opcode 6 bits / memory word adress 26 bits
	 */	 
	
	//Fun�ao que verifica o opcode de cada tipo de instru��o recebida na entrada: syscall, R, I ou J
	public String acharOp(String b, ArrayList<SimuladorMIPS> lista, ArrayList<SimuladorMIPS> memoria) {
		String res = "";
		
		if(b.equalsIgnoreCase("00000000000000000000000000001100")) {
			//tipo syscall
			res = syscall(res);
		} else if(b.substring(0,6).equalsIgnoreCase("000000")) {
			//tipo R
			res = tipoR(res,b, lista, memoria);
		} else if(b.substring(0,6).equalsIgnoreCase("000010") ||
				b.substring(0,6).equalsIgnoreCase("000011")) {
			//tipo J
			res = tipoJ(res,b, lista, memoria);
		} else {
			//tipo I
			res = tipoI(res,b, lista, memoria);
		}
		
		return res;
	}
	
	//Fun�ao criada para retornar a instru�ao syscall
	private String syscall(String res) {
		res = "syscall";
		return res;
	}
	
	//Fun�ao criada para as instru�oes do tipo R que tem como retorno 3 registradores
	private String registradorRTriplo(String res, String b) {
		if(res == "sllv" || res == "srlv" || res == "srav") {
			res = res + " ";
			res = res + "$" + binDecimalPos(b.substring(16, 21)) + ", ";
			res = res + "$" + binDecimalPos(b.substring(11, 16)) + ", ";
			res = res + "$" + binDecimalPos(b.substring(6, 11));
		} else {
			res = res + " ";		
			res = res + "$" + binDecimalPos(b.substring(16, 21)) + ", ";
			res = res + "$" + binDecimalPos(b.substring(6, 11)) + ", ";
			res = res + "$" + binDecimalPos(b.substring(11, 16));
		}
		return res;
	}
	
	//Fun�ao criada para as instru�oes do tipo R que tem como retorno 2 registradores
	private String registradorRDuplo(String res, String b) {
		if(res == "mult" ||	res == "multu" || res == "div" || res == "divu") {
			res = res + " ";
			res = res + "$" + binDecimalPos(b.substring(6, 11)) + ", ";
			res = res + "$" + binDecimalPos(b.substring(11, 16));
		} else {
			res = res + " ";
			res = res + "$" + binDecimalPos(b.substring(16, 21)) + ", ";
			res = res + "$" + binDecimalPos(b.substring(11, 16)) + ", ";
			res = res + binDecimalPos(b.substring(21, 26));
		}		
		return res;
	}
	
	//Fun�ao criada para as instru�oes do tipo R que tem como retorno 1 registrador
	private String registradorRUnico(String res, String b) {
		if(res == "jr") {
			res = res + " ";		
			res = res + "$" + binDecimalPos(b.substring(6, 11));
		} else {
			res = res + " ";		
			res = res + "$" + binDecimalPos(b.substring(16, 21));
		}		
		return res;
	}
	
	//Todas as intru��es do tipo R, sendo analisado pelo opcode extension. E sendo tratada em cada if as fun��es do tipos l�gicas e aritmeticas 
	private String tipoR(String res, String b, ArrayList<SimuladorMIPS> lista, ArrayList<SimuladorMIPS> memoria) {
		if(b.substring(26, 32).equalsIgnoreCase("100000")) {
			//fun��o add - soma dois registradores
			res = "add";
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int id3 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));		
			lista.get(id1).setValor(lista.get(id2).getValor() + lista.get(id3).getValor());
			
		} else if(b.substring(26, 32).equalsIgnoreCase("100010")) {
			//fun��o sub - diminui dois registradores
			res = "sub";
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int id3 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));	
			lista.get(id1).setValor(lista.get(id2).getValor() - lista.get(id3).getValor());
			
		} else if(b.substring(26, 32).equalsIgnoreCase("101010")) {
			//fun��o slt - compara se um registrador � maior que o outro
			res = "slt";
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int id3 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
			
			if(lista.get(id2).getValor() < lista.get(id3).getValor()) {
				lista.get(id1).setValor(1);
			} else lista.get(id1).setValor(0);
			
		} else if(b.substring(26, 32).equalsIgnoreCase("100100")) {
			//fun��o l�gica and entre dois registradores
			res = "and";
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int id3 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
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
			//fun��o l�gica or entre dois registradores
			res = "or";	
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int id3 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
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
			//fun��o l�gica xor entre dois registradores
			res = "xor";
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int id3 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
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
			//fun��o l�gica nor entre dois registradores
			res = "nor";
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int id3 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
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
			//fun��o para acessar o registrador hi
			res = "mfhi";
			res = registradorRUnico(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(16, 21)));
			int hi = lista.get(this.acharRegs(lista, "hi")).getValor();
			lista.get(id1).setValor(hi);
			
		} else if(b.substring(26, 32).equalsIgnoreCase("010010")) {
			//fun��o para acessar o registrador lo
			res = "mflo";
			res = registradorRUnico(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(16, 21)));
			int lo = lista.get(this.acharRegs(lista, "lo")).getValor();
			lista.get(id1).setValor(lo);
		} else if(b.substring(26, 32).equalsIgnoreCase("100001")) {
			//fun��o addu - soma dois registradores sem sinal
			res = "addu";	
			res = registradorRTriplo(res,b);
			//nao estamos verificando overflow ainda
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int id3 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));		
			lista.get(id1).setValor(lista.get(id2).getValor() + lista.get(id3).getValor());
			
		} else if(b.substring(26, 32).equalsIgnoreCase("100011")) {
			//fun��o subu - subtra��o de dois registradores sem sinal
			res = "subu";	
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int id3 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));	
			lista.get(id1).setValor(lista.get(id2).getValor() - lista.get(id3).getValor());
			
		} else if(b.substring(26, 32).equalsIgnoreCase("011000")) {
			//fun��o mult - multiplica��o de dois registradores
			res = "mult";	
			res = registradorRDuplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
			//int hi = this.acharRegs(lista, "hi");
			int lo = this.acharRegs(lista, "lo");
			
			lista.get(lo).setValor(lista.get(id1).getValor() * lista.get(id2).getValor());
			
		} else if(b.substring(26, 32).equalsIgnoreCase("011001")) {
			//fun��o multu - multiplica��o de dois registradores sem sinal
			res = "multu";	
			res = registradorRDuplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
			//int hi = this.acharRegs(lista, "hi");
			int lo = this.acharRegs(lista, "lo");
			
			lista.get(lo).setValor(lista.get(id1).getValor() * lista.get(id2).getValor());
			
		} else if(b.substring(26, 32).equalsIgnoreCase("011010")) {
			//fun��o div - divis�o de dois registradores
			res = "div";
			res = registradorRDuplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
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
			//fun��o divu - divis�o de dois registradores sem sinal
			res = "divu";
			res = registradorRDuplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
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
			//fun��o sll - anda o registrado para esquerda de acordo com o n�mero passado como parametro
			res = "sll";
			res = registradorRDuplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
			String rt = decimalBin(lista.get(id2).getValor()) + "";			
			int shift = binDecimalPos(b.substring(21, 26));
			
			for(int i = 0; i < shift; i++) {
				rt = rt + "0";
			}
			
			lista.get(id1).setValor(binDecimalPos(rt));			
			
		} else if(b.substring(26, 32).equalsIgnoreCase("000010")) {
			//fun��o srl - anda o registrado para direita de acordo com o n�mero passado como parametro
			res = "srl";
			res = registradorRDuplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
			String rt = decimalBin(lista.get(id2).getValor()) + "";			
			int shift = binDecimalPos(b.substring(21, 26));
			int cont = rt.length();
			String aux = "";
			
			for(int i = 0; i < shift; i++) {
				rt = "0" + rt;
			}
			
			for(int i = 0; i < cont; i++) {
				aux = aux + rt.substring(i, i+1);
			}
			lista.get(id1).setValor(binDecimalPos(aux));
			
		} else if(b.substring(26, 32).equalsIgnoreCase("000011")) {
			//fun��o sra - anda o registrado para direita preservando o bit de sinal de acordo com o n�mero passado como parametro
			res = "sra";
			res = registradorRDuplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
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
			//fun��o sllv - anda o registrado para esquerda de acordo com um registrador passado como parametro
			res = "sllv";	
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
			int id3 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			String rt = decimalBin(lista.get(id2).getValor()) + "";			
			int shift = lista.get(id3).getValor();			
			
			for(int i = 0; i < shift; i++) {
				rt = rt + "0";
			}
			
			lista.get(id1).setValor(binDecimal(rt));
			
		} else if(b.substring(26, 32).equalsIgnoreCase("000110")) {
			//fun��o srlv - anda o registrado para direita de acordo com um registrador passado como parametro
			res = "srlv";	
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
			int id3 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
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
			//fun��o sra - anda o registrado para direita preservando o bit de sinal de acordo com um registrador passado como parametro
			res = "srav";
			res = registradorRTriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(16, 21)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
			int id3 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
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
			//seta o pc para o endere�o que esta no registrador
			res = "jr";		
			res = registradorRUnico(res,b);
			
			int id = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			
			lista.get(this.acharRegs(lista, "pc")).setValor(lista.get(id).getValor());
			
		} 		
		return res;		
	}
	
	//Fun�ao criada para as instru�oes do tipo I que tem como retorno 3 registradores
	private String registradorITriplo(String res, String b) {
		if(res == "addi" || res == "slti" || res == "andi" || res == "ori" || res == "xori" || res == "addiu") {
			res = res + " ";
			res = res + "$" + binDecimalPos(b.substring(11, 16)) + ", ";
			res = res + "$" + binDecimalPos(b.substring(6, 11)) + ", ";
			res = res + binDecimalPos(b.substring(16,32));
		} else if(res == "beq" || res == "bne") {
			res = res + " ";
			res = res + "$" + binDecimalPos(b.substring(6, 11)) + ", ";
			res = res + "$" + binDecimalPos(b.substring(11, 16)) + ", ";
			//o start n�o esta sendo tratado ainda, apenas retornando o start
			res = res + "start";
		}
		return res;
	}
	
	//Fun�ao criada para as instru�oes do tipo I que tem como retorno 2 registradores
	private String registradorIDuplo(String res, String b) {
		if(res == "lui") {
			res = res + " ";
			res = res + "$" + binDecimalPos(b.substring(11, 16)) + ", ";
			res = res + binDecimalPos(b.substring(16,32));
		} else if(res == "lw" || res == "sw" || res == "lb" || res == "lbu" || res == "sb") {
			res = res + " ";
			res = res + "$" + binDecimalPos(b.substring(11, 16)) + ", ";
			res = res + binDecimalPos(b.substring(16,32)) + "(";
			res = res + "$" + binDecimalPos(b.substring(6, 11)) + ")";
		} else if(res == "bltz") {
			res = res + " ";
			res = res + "$" + binDecimalPos(b.substring(6, 11)) + ", ";
			if(b.substring(16,32).equalsIgnoreCase("1111111111101110")){
				res = res + "start";
			}
		}
		return res;
	}
	
	//Todas as intru��es do tipo I, sendo analisadas pelo opcode. E sendo tratada em cada if as fun��es do tipos l�gicas e aritmeticas 
	private String tipoI(String res, String b, ArrayList<SimuladorMIPS> lista, ArrayList<SimuladorMIPS> memoria) {
		if(b.substring(0,6).equalsIgnoreCase("001111")) {
			res = "lui";
			res = registradorIDuplo(res,b);
			
			int id = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
			int start = binDecimalPos(b.substring(16, 32));
			
			//se o numero for maior que 65535 (0xffff) daria overflow e n�o seria poss�vel armazenar em 16bits
			//se for menor, transforma ele em hexadecimal, adiciona os 16bits finais e transforma de volta para decimal e salva no registrador
			if(start <= 65535) {
				String hex = Integer.toHexString(start);				
				hex = "0x" + hex + "0000";
				start = Integer.decode(hex);
				lista.get(id).setValor(start);
			} else {
				//overflow - n�o salva nada
			}
			
		} else if(b.substring(0,6).equalsIgnoreCase("001000")) {
			//fun��o addi - soma o n�mero passado como parametro com o registrador
			res = "addi";
			res = registradorITriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));		
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int num = binDecimal(b.substring(16, 32));
			
			lista.get(id1).setValor(lista.get(id2).getValor() + num);
			
		} else if(b.substring(0,6).equalsIgnoreCase("001010")) {
			//fun��o slti - verifica se o registrador � menor que o numero passado como parametro
			res = "slti";	
			res = registradorITriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int num = binDecimal(b.substring(16, 32));
			
			if(lista.get(id2).getValor() < num) {
				lista.get(id1).setValor(1);
			} else lista.get(id1).setValor(0);
			
		} else if(b.substring(0,6).equalsIgnoreCase("001100")) {
			//fun��o l�gica andi entre o registrador e numero passado como parametro
			res = "andi";
			res = registradorITriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
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
			//fun��o l�gica ori entre o registrador e numero passado como parametro
			res = "ori";
			res = registradorITriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
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
			//fun��o l�gica xori entre o registrador e numero passado como parametro
			res = "xori";
			res = registradorITriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
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
			//loada da memoria o valor em hexadecimal na entrada solicitada e salva em decimal no registrador
			//o valor loadado ocupa 4 espa�os da memoria
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int num = binDecimal(b.substring(16, 32));			
			int mem = num + lista.get(id2).getValor();	
			
			String hex = "";
			for(int i = 0; i < 4; i++) {
				hex = memoria.get(mem+i).getValorHexa() + hex;
			}			
			hex = hex.replaceAll("0x", "");
			hex = "0x" + hex;

			int resultado = Integer.decode(hex);
			lista.get(id1).setValor(resultado);
			
		} else if(b.substring(0,6).equalsIgnoreCase("101011")) {
			res = "sw";	
			res = registradorIDuplo(res,b);
			//salva na memoria em hexadecimal o valor que esta no registrador em decimal
			//o valor salvo ocupa 4 espa�os da memoria			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int num = binDecimal(b.substring(16, 32));
			int mem = num + lista.get(id2).getValor();
			
			String resultado = Integer.toHexString(lista.get(id1).getValor());
			String aux = "";		
			
			aux = "0x" + resultado;
			if(aux.length() < 10) {	
				int cont = aux.length();
				for(int i = cont; i < 10; i++) {
					aux = aux.replace("0x", "0x0");
				}				
			}
			//salva os dois ultimos digitos do numero em hexadecimal e salva em hexadecimal na memoria
			//faz isso em 4 entradas da memoria (8 bits x 4 = 32bits)
			for(int i = 0; i < 4; i++) {
				String x = "0x" + aux.substring(8 - i*2, 10 - i*2);
				memoria.get(mem+i).setValorHexa(x);
			}			
			
		} else if(b.substring(0,6).equalsIgnoreCase("000001")) {
			res = "bltz";
			res = registradorIDuplo(res,b);
			
			int id = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int start = binDecimalPos(b.substring(16,32));
			//se o valor no registrador for menor que 0, insere o label "start" ao pc
			if(lista.get(id).getValor() < 0) {
				lista.get(this.acharRegs(lista, "pc")).setValor(start);
			}
			
		} else if(b.substring(0,6).equalsIgnoreCase("000100")) {
			res = "beq";	
			res = registradorITriplo(res,b);
			
			int id = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));			
			int start = binDecimalPos(b.substring(16,32));
			//se o valor em ambos os registradores forem iguais, insere o label "start" ao pc
			if(lista.get(id).getValor() == lista.get(id2).getValor()) {
				lista.get(this.acharRegs(lista, "pc")).setValor(start);
			}
			
		} else if(b.substring(0,6).equalsIgnoreCase("000101")) {
			res = "bne";
			res = registradorITriplo(res,b);
			
			int id = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));			
			int start = binDecimalPos(b.substring(16,32));
			//se o valor dos dois registradores forem diferentes, insere o label "start" ao pc
			if(lista.get(id).getValor() != lista.get(id2).getValor()) {
				lista.get(this.acharRegs(lista, "pc")).setValor(start);
			}
			
		} else if(b.substring(0,6).equalsIgnoreCase("001001")) {
			//fun��o addiu - soma o n�mero passado como parametro com o registrador sem sinal
			res = "addiu";
			res = registradorITriplo(res,b);
			
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));		
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int num = binDecimal(b.substring(16, 32));
			
			lista.get(id1).setValor(lista.get(id2).getValor() + num);
			
		} else if(b.substring(0,6).equalsIgnoreCase("100000")) {
			res = "lb";	
			res = registradorIDuplo(res,b);
			//loada da memoria o valor em hexadecimal na entrada solicitada e salva em decimal no registrador
			//o valor loadado ocupa 1 espa�o da memoria
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int num = binDecimal(b.substring(16, 32));			
			int mem = num + lista.get(id2).getValor();
			
			String hex = memoria.get(mem).getValorHexa();
			int resultado = Integer.decode(hex);
			
			lista.get(id1).setValor(resultado);
			
		} else if(b.substring(0,6).equalsIgnoreCase("100100")) {
			res = "lbu";
			res = registradorIDuplo(res,b);
			//loada da memoria o valor em hexadecimal na entrada solicitada e salva em decimal no registrador
			//o valor loadado ocupa 1 espa�o da memoria
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int num = binDecimal(b.substring(16, 32));			
			int mem = num + lista.get(id2).getValor();
			
			String hex = memoria.get(mem).getValorHexa();
			int resultado = Integer.decode(hex);
			
			lista.get(id1).setValor(resultado);
			
		} else if(b.substring(0,6).equalsIgnoreCase("101000")) {
			res = "sb";			
			res = registradorIDuplo(res,b);
			//salva na memoria em hexadecimal o valor que esta no registrador em decimal
			//ele salva apenas os 8 primeiros bits que est�o no registrador
			//o valor salvo ocupa 1 espa�o da memoria
			int id1 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(11, 16)));
			int id2 = this.acharRegs(lista, "$" + binDecimalPos(b.substring(6, 11)));
			int num = binDecimal(b.substring(16, 32));	
			int mem = num + lista.get(id2).getValor();
			
			String resultado = Integer.toHexString(lista.get(id1).getValor());
			String aux = "";
			int tam = resultado.length();
			if(tam > 1) {
				for(int i = tam; i > tam-2; i--) {
					aux = resultado.substring(i-1,i) + aux;
				}
			} else {
				aux = resultado;
			}
			
			aux = "0x" + aux;
			if(aux.length() < 4) {				
				aux = aux.replace("0x", "0x0");
			}
			//salva os dois ultimos digitos do numero em hexadecimal e salva em hexadecimal na memoria
			memoria.get(mem).setValorHexa(aux);
			
		}
		return res;
	}
	
	//achar a fun��o do tipo J comparando os 6 primeiros bits
	private String tipoJ(String res, String b, ArrayList<SimuladorMIPS> lista, ArrayList<SimuladorMIPS> memoria) {
		if(b.substring(0,6).equalsIgnoreCase("000010")) {
			res = "j";
			
			int start = binDecimalPos(b.substring(6, 32));		
			//salva no pc o label "start"
			lista.get(this.acharRegs(lista, "pc")).setValor(start);
			
		} else {
			res = "jal";
			
			int pc = this.acharRegs(lista, "pc");
			int start = binDecimalPos(b.substring(6, 32));	
			//salva no $ra (registrador aqui chamado de $31) o valor de pc
			lista.get(this.acharRegs(lista, "$31")).setValor(lista.get(pc).getValor());
			//salva no pc o label "start"
			lista.get(pc).setValor(start);
		}
		res = res + " ";
		//o start n�o esta sendo tratado ainda, apenas retornando o start
		res = res + "start";
		return res;
	}
	
	//Fun��o printar registrador	
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
		
	//Fun��o que acha qual � o resgistrador
	private int acharRegs(ArrayList<SimuladorMIPS> lista, String nome) {
		int indiceRegs = 0;
		for(int i = 0; i < lista.size(); i++) {
			if(lista.get(i).getNome().equalsIgnoreCase(nome)) {
				indiceRegs = i;
			}
		}
		
		return indiceRegs;
	}
	
	//Fun��o para printar a memoria, apenas entradas diferentes de 0x0
		private String printarMemoria(ArrayList<SimuladorMIPS> memoria) {
			int cont = 0;
			
			for(int i = 0; i < 1024; i++) {
				if(memoria.get(i).getValorHexa().equalsIgnoreCase("0x00") == false) {
					cont++;
				}
			}	
			
			String stringMem = "MEM[";
			
			for(int i = 0; i < 1024; i++) {
				if(memoria.get(i).getValorHexa().equalsIgnoreCase("0x00") == false) {
					stringMem = stringMem + memoria.get(i).getNome() + ":" + memoria.get(i).getValorHexa();
					cont--;
					if(cont > 0) {						
						stringMem = stringMem + ";";
					}					
				}									
			}
			
			stringMem = stringMem + "]";
			stringMem = stringMem.replaceAll("0x", "");
			
			return stringMem;
		}
	
	public static void main(String[] args) {
		SimuladorMIPS simulador = new SimuladorMIPS();
		ArrayList<SimuladorMIPS> lista = simulador.registradores();		
		ArrayList<SimuladorMIPS> memoria = simulador.memoria();
		
		String localDir = System.getProperty("user.dir");
		//Usamos os caminhos relativos
		String caminhoEntrada = localDir + "\\entrada.txt";
		String caminhoSaida = localDir + "\\saida.txt";
		ArrayList<String> entrada = new ArrayList<>();
		ArrayList<String> saida = new ArrayList<>();
		
		try {
		      FileReader arq = new FileReader(caminhoEntrada);
		      BufferedReader lerArq = new BufferedReader(arq);
		      String linha = lerArq.readLine(); // l� a primeira linha
		      
		      while (linha != null) {
		        entrada.add(linha);
		        linha = lerArq.readLine(); // l� da segunda at� a �ltima linha		        
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
				//se a linha de comando n�o � nula nem vazia, vai para a fun��o acharOp e retorna a string em comandos assembly
				String assembly = simulador.acharOp(simulador.hexToBin(entrada.get(i)),lista, memoria);
				saida.add(assembly);				
				//imprime o valor de cada registrador
				saida.add(simulador.toStringRegs(lista));
				//imrpime o valor da memoria que � diferente de 0, informando a entrada e o valor em hexadecimal
				saida.add(simulador.printarMemoria(memoria));
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
