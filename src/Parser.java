import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.NotYetBoundException;
import java.util.ArrayList;



public class Parser {
	public static ArrayList<String> code = new ArrayList<String>();
	public static String token =""  ;
	public static int counter ;
	public static String output = new String();
	//recursivs 
	public static void  program(){
		stmt_seq();
		output += "Program found\n";
	}
	public static void stmt_seq(){
		statement();
		while(token.equals(";")){
			match(token);
			statement();
		}
		//output += "Statment Sequence found\n";
	}
	public static void statement(){
		if(token.equals("if")){
			if_stmt();
		}else if(token.equals("repeat")){
			repeat_stmt();
		}else if(token.equals("identifier")){
			assign_stmt();
		}else if(token.equals("read")){
			read_stmt();
		}else if(token.equals("write")){
			write_stmt();
		}else{
			error();
		}
	}
	public static void if_stmt(){
		match("if");
		exp();
		match("then");
		stmt_seq();
		if(token.equals("else")){
			match("else");
			stmt_seq();
		}
		match("end");
		output += "If Statment found\n";
		
	}
	public static void repeat_stmt(){
		match("repeat");
		stmt_seq();
		match("until");
		exp();
		output += "Repeat Statment found\n";
	}
	public static void assign_stmt(){
		match("identifier");
		match(":=");
		exp();
		output += "Assignment Statment found\n";
	}
	public static void read_stmt(){
		match("read");
		match("identifier");
		output += "Read Statment found\n";
	}
	public static void write_stmt(){
		match("write");
		exp();
		output += "Write Statment found\n";
	}
	public static void exp(){
		simple_exp();
		if(token.equals("<") || token.equals("=")){
			match(token);
			simple_exp();
			//output += "experssion found\n";
		}
	}
	public static void simple_exp(){
		term();
		while(token.equals("+") || token.equals("-")){
			match(token);
			term();
		}
		//output += "simple experssion found\n";
	}
	public static void term(){
		factor();
		while(token.equals("*")||token.equals("/")){
			match(token);
			factor();
		}
		//output += "term found\n";
	}
	public static void factor(){
		if(token.equals("(")){		
			match("(");
			exp();
			match(")");
		}else if(token.equals("number")){
			match(token);
		}else if(token.equals("identifier")){
			match(token);
		}else{
			error();
		}
		//output += "factor found\n";
	}
	//match and error 
	public static void match(String ExpexpectedToken){
		if(token.equals(ExpexpectedToken)){
			counter++;
			if(!code.get(counter).equals("$"))
				token = code.get(counter);
			else
				end();
		}
		else
			error();
	}
	private static void end() {
		System.out.println("END OF CODE");
		
	}
	public static void error(){
		System.out.println("Error");
		System.exit(0);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Input stream
		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream("scanner_output.txt");
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String strLine;
		try {
			while ((strLine = br.readLine()) != null) {
				
					String[] spl = strLine.split("\\s+");
					for(int j = 0 ; j < spl.length;j++)
						code.add(spl[j]);	
			}
			code.add("$");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//---------------------------
		counter =0;
		token = code.get(0);
		program();
		System.out.print(output);
		System.out.println(code.size()+ ","+counter);
		//---------------------------
		//Output stream
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("parser_output.txt"));
			writer.write(output);
		} catch (IOException e) {
		} finally

		{
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
			}
		}
	}

}
