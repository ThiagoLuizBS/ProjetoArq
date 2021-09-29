package mips;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Parte1 {
	public ArrayList<String> registradores(){
		ArrayList<String> r = new ArrayList<>();
		r.add("$zero");
		r.add("$at");
		r.add("$v0");
		r.add("$v1");
		r.add("$a0");
		r.add("$a1");
		r.add("$a2");
		r.add("$a3");
		r.add("$t0");
		r.add("$t1");
		r.add("$t2");
		r.add("$t3");
		r.add("$t4");
		r.add("$t5");
		r.add("$t6");
		r.add("$t7");
		r.add("$s0");
		r.add("$s1");
		r.add("$s2");
		r.add("$s3");
		r.add("$s4");
		r.add("$s5");
		r.add("$s6");
		r.add("$s7");
		r.add("$t8");
		r.add("$t9");
		r.add("$k0");
		r.add("$k1");
		r.add("$gp");
		r.add("$sp");
		r.add("$fp");
		r.add("$ra");
		
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
	    
	/* Instrucoes tipo R 
	 * opcode 6 bits / rs e rt (operando) 5 bits cada/ rd (resultante) 5 bits / sh 5 bits / opcode funcao 6 bits
	 * 
	 *  Instrucoes tipo I 
	 *  opcode 6 bits / rs source or base 5 bits / rt destination or data 5 bits / operand ou endereço offset 16 bits
	 * 
	 *  Instrucoes tipo J
	 *  opcode 6 bits / memory word adress 26 bits
	 */	  
	}
	
	private int binDecimal(String x) {
		int numero = Integer.parseInt(x, 2);
		return numero;
	}
	
	public String acharOp(String b) {
		String res = "";
		
		if(b.equalsIgnoreCase("00000000000000000000000000001100")) {
			//tipo syscall
			res = syscall(res);
		} else if(b.substring(0,6).equalsIgnoreCase("000000")) {
			//tipo R
			res = tipoR(res,b);
		} else if(b.substring(0,6).equalsIgnoreCase("000010") ||
				b.substring(0,6).equalsIgnoreCase("000011")) {
			//tipo J
			res = tipoJ(res,b);
		} else {
			//tipo I
			res = tipoI(res,b);
		}
		
		return res;
	}
	
	private String syscall(String res) {
		res = "syscall";
		return res;
	}
	
	private String registradorTriplo(String res, String b) {
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
	
	private String registradorDuplo(String res, String b) {
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
	
	private String registradorUnico(String res, String b) {
		if(res == "jr") {
			res = res + " ";		
			res = res + "$" + binDecimal(b.substring(6, 11));
		} else {
			res = res + " ";		
			res = res + "$" + binDecimal(b.substring(16, 21));
		}		
		return res;
	}
	
	private String tipoR(String res, String b) {
		if(b.substring(26, 32).equalsIgnoreCase("100000")) {
			res = "add";
			res = registradorTriplo(res,b);
		} else if(b.substring(26, 32).equalsIgnoreCase("100010")) {
			res = "sub";
			res = registradorTriplo(res,b);
		} else if(b.substring(26, 32).equalsIgnoreCase("101010")) {
			res = "slt";
			res = registradorTriplo(res,b);
		} else if(b.substring(26, 32).equalsIgnoreCase("100100")) {
			res = "and";
			res = registradorTriplo(res,b);
		} else if(b.substring(26, 32).equalsIgnoreCase("100101")) {
			res = "or";	
			res = registradorTriplo(res,b);
		} else if(b.substring(26, 32).equalsIgnoreCase("100110")) {
			res = "xor";
			res = registradorTriplo(res,b);
		} else if(b.substring(26, 32).equalsIgnoreCase("100111")) {
			res = "nor";
			res = registradorTriplo(res,b);
		} else if(b.substring(26, 32).equalsIgnoreCase("010000")) {
			res = "mfhi";
			res = registradorUnico(res,b);
		} else if(b.substring(26, 32).equalsIgnoreCase("010010")) {
			res = "mflo";
			res = registradorUnico(res,b);
		} else if(b.substring(26, 32).equalsIgnoreCase("100001")) {
			res = "addu";	
			res = registradorTriplo(res,b);
		} else if(b.substring(26, 32).equalsIgnoreCase("100011")) {
			res = "subu";	
			res = registradorTriplo(res,b);
		} else if(b.substring(26, 32).equalsIgnoreCase("011000")) {
			res = "mult";	
			res = registradorDuplo(res,b);
		} else if(b.substring(26, 32).equalsIgnoreCase("011001")) {
			res = "multu";	
			res = registradorDuplo(res,b);
		} else if(b.substring(26, 32).equalsIgnoreCase("011010")) {
			res = "div";
			res = registradorDuplo(res,b);
		} else if(b.substring(26, 32).equalsIgnoreCase("011011")) {
			res = "divu";
			res = registradorDuplo(res,b);
		} else if(b.substring(26, 32).equalsIgnoreCase("000000")) {
			res = "sll";
			res = registradorDuplo(res,b);
		} else if(b.substring(26, 32).equalsIgnoreCase("000010")) {
			res = "srl";
			res = registradorDuplo(res,b);
		} else if(b.substring(26, 32).equalsIgnoreCase("000011")) {
			res = "sra";
			res = registradorDuplo(res,b);
		} else if(b.substring(26, 32).equalsIgnoreCase("000100")) {
			res = "sllv";	
			res = registradorTriplo(res,b);
		} else if(b.substring(26, 32).equalsIgnoreCase("000110")) {
			res = "srlv";	
			res = registradorTriplo(res,b);
		} else if(b.substring(26, 32).equalsIgnoreCase("000111")) {
			res = "srav";
			res = registradorTriplo(res,b);
		} else if(b.substring(26, 32).equalsIgnoreCase("001000")) {
			res = "jr";		
			res = registradorUnico(res,b);
		} 
		
		return res;		
	}
	
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
			if(b.substring(16,32).equalsIgnoreCase("1111111111101100") ||
					b.substring(16,32).equalsIgnoreCase("1111111111101101")) {
				res = res + "start";
			}
		}
		return res;
	}
	
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
	
	private String tipoI(String res, String b) {
		if(b.substring(0,6).equalsIgnoreCase("001111")) {
			res = "lui";
			res = registradorIDuplo(res,b);
		} else if(b.substring(0,6).equalsIgnoreCase("001000")) {
			res = "addi";
			res = registradorITriplo(res,b);
		} else if(b.substring(0,6).equalsIgnoreCase("001010")) {
			res = "slti";	
			res = registradorITriplo(res,b);
		} else if(b.substring(0,6).equalsIgnoreCase("001100")) {
			res = "andi";
			res = registradorITriplo(res,b);
		} else if(b.substring(0,6).equalsIgnoreCase("001101")) {
			res = "ori";
			res = registradorITriplo(res,b);
		} else if(b.substring(0,6).equalsIgnoreCase("001110")) {
			res = "xori";
			res = registradorITriplo(res,b);
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

	private String tipoJ(String res, String b) {
		if(b.substring(0,6).equalsIgnoreCase("000010")) {
			res = "j";
		} else {
			res = "jal";
		}
		res = res + " ";
		if(b.substring(6,32).equalsIgnoreCase("00000100000000000000000000")) {
			res = res + "start";
		}
		return res;
	}
	
	public static void main(String[] args) {
		Parte1 p = new Parte1();
				
		String caminho = "C:\\Users\\Thiago\\Downloads\\entrada.txt";
		ArrayList<String> entrada = new ArrayList<>();
		ArrayList<String> saida = new ArrayList<>();
		
		try {
		      FileReader arq = new FileReader(caminho);
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
			arq2 = new FileWriter("C:\\Users\\Thiago\\Downloads\\saida.txt");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PrintWriter gravarArq = new PrintWriter(arq2);
		
		for(int i = 0; i < entrada.size(); i++) {
			String assembly = p.acharOp(p.hexToBin(entrada.get(i)));
			System.out.println(assembly);
			saida.add(assembly);
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
