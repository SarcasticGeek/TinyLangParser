import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 */

/**
 * @author Mohamed Essam Fathalla Mohamed - Section 3
 * 
 */
public class Scanner {
	public static String[] codeA;
	public static ArrayList<String> code = new ArrayList<String>();
	private static String[] reservedWords = new String[] { "if", "then",
			"else", "end", "repeat", "until", "read", "write" };
	private static String[] specialSym = new String[] { ";", "+", "-", "*",
			"/", "=", "<", ">", "(", ")", ":" };
	private static String regExpDigit = "[0-9]";
	private static String regExpLetter = "[a-zA-Z]";

	// STATES
	final static int START = 0;
	final static int INCOMMENT = 1;
	final static int INNUM = 2;
	final static int INID = 3;
	final static int INASSIGN = 4;
	final static int DONE = 5;
	static char[] charOfCode;
	int state = START;
	int stop = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Reading from file
		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream("tiny_sample_code.txt");
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String strLine;
		try {
			while ((strLine = br.readLine()) != null) {
				code.add(strLine);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// start state
		int state = START;
		int stop = 0;
		String output = new String();
		String outputinLine = new String();
		String CodeLine;
		System.out.println("Scanning...");
		for (int j = 0; code.size() > j; j++) {
			state = START;
			stop = 0;
			CodeLine = "";
			CodeLine = code.get(j);
			CodeLine += " ";
			CodeLine = CodeLine.replace("{", " {");
			charOfCode = (CodeLine).toCharArray();
			System.out.print(charOfCode);
			String idnbuff = "";
			String Numbuff = "";
			String SymBuff ="";
			for (int i = stop; charOfCode.length > i; i++) {
				// System.out.println("now"+charOfCode[i] + " state now:" +
				// state);
				// System.out.println(idnbuff);

				if (state == DONE)
					state = START;

				switch (state) {
				case START:
					if (charOfCode[i] == '{') {
						state = INCOMMENT;
					} else if (Character.toString(charOfCode[i]) == ":") {
						state = INASSIGN;
					} else if (Character.toString(charOfCode[i]).matches(
							regExpLetter)) {
						idnbuff += charOfCode[i];
						state = INID;
					} else if (Character.toString(charOfCode[i]).matches(
							regExpDigit)) {
						Numbuff += charOfCode[i];
						state = INNUM;
					} else if (charOfCode[i] == ' ') {
						state = START;
					} else {
						state = DONE;
					}
					break;
				case INCOMMENT:
					if (charOfCode[i] == '}') {
						state = START;
					} else {
						state = INCOMMENT;
					}
					break;
				case INNUM:
					if (Character.toString(charOfCode[i]).matches(regExpDigit)) {
						Numbuff += charOfCode[i];
						state = INNUM;
					} else {
						if (Arrays.asList(specialSym).contains(
								Character.toString(charOfCode[i]))
								&& state != INCOMMENT) {
							if (charOfCode[i] == ':')
								SymBuff += charOfCode[i] + "=" ;
//								
							else
								SymBuff += charOfCode[i]  ;
								
						}
						output +=  "number ";
						outputinLine +="number ";
						if(SymBuff != ""){
							output += SymBuff
									+ " ";
						}
						Numbuff = "";
						SymBuff = "";
						state = DONE;
					}
					break;
				case INID:
					if (Character.toString(charOfCode[i]).matches(regExpLetter)) {
						state = INID;
						idnbuff += charOfCode[i];
					} else {
						if (Arrays.asList(specialSym).contains(
								Character.toString(charOfCode[i]))
								&& state != INCOMMENT) {
							if (charOfCode[i] == ':')
								SymBuff +=charOfCode[i] + "=" ;
							else
								SymBuff +=charOfCode[i]  ;
								
						}
						 if (Arrays.asList(reservedWords).contains(idnbuff)) {
							output += idnbuff+" ";
							outputinLine += idnbuff+" ";
							if(SymBuff != ""){
								output += SymBuff
										+ " ";
								outputinLine += SymBuff
										+ " ";
							}
							SymBuff= "";
							idnbuff = "";
						} else {
							output +=  "identifier ";
							outputinLine += "identifier ";
							if(SymBuff != ""){
								output += SymBuff
										+ " ";
								outputinLine += SymBuff
										+ " ";
							}
							SymBuff= "";
							idnbuff = "";
						}
						state = DONE;
					}
					break;
				case INASSIGN:
					if (charOfCode[i] == '=') {
						state = DONE;
					} else {
						state = DONE;
					}
					break;
				case DONE:
					stop = i;
					break;
				}
				if(i == charOfCode.length - 1 && !outputinLine.equals("")){
					output += "\n";
					outputinLine = "";
				}
			}
			
		}
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("scanner_output.txt"));
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
