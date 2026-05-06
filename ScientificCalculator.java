package JavaProject;
import java.util.Scanner;
public class ScientificCalculator {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("=== Java Scientific Calculator ===");
		System.out.println(" Type mathematical question and press Enter.");
		System.out.println("Functions: log(base 10), ln(base e), sqrt");
		System.out.println("Trig functions: sin, cos, tan");
		System.out.println("Constants: pi, e");
		System.out.println("Type 'exit' to stop the expression");
		System.out.println("-----------------------------");
		
		while (true) {
			System.out.print("> ");
			String input = scanner.nextLine().trim();
			
			if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
				System.out.println("Exiting calculator. Goodbye!");
				break;
			}
			
			if (input.isEmpty()) {
				continue;
			}
			try {
				double result = evaluateExpression(input);
				System.out.println("Result: " + result);
			} catch(Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
		}
		scanner.close();
	}
	public static double evaluateExpression(final String str) {
		return new Object() {
			int pos = -1, ch;
			void nextChar() {
				ch = (++pos < str.length()) ? str.charAt(pos) : -1;
			}
			boolean eat(int charToEat) {
				while (ch == ' ') nextChar();
				if (ch == charToEat) {
					nextChar();
					return true;
				}
				return false;
			}
			double parse() {
				nextChar ();
				double x = parseExpression();
				if (pos< str.length()) throw new RuntimeException("Unexpected character: " + (char) ch);
				return x;
			}
			double parseExpression() {
				double x = parseTerm();
				for(;;) {
					if		(eat('+')) x += parseTerm();
					else if (eat('-')) x -= parseTerm();
					else return x;
				}
			}
			double parseTerm() {
				double x = parseFactor();
				for(;;) {
					if		(eat('*')) x *= parseFactor();
					else if (eat('/')) x /= parseFactor();
					else return x;
				}
			}
			double parseFactor() {
				if (eat('+')) return parseFactor();
				if (eat('-')) return -parseFactor();
				double x;
				int startPos = this.pos;
				
				if (eat('(')) {
					x = parseExpression();
					eat(')');
				} else if ((ch >= '0' && ch <= '9') || ch == '.') {
					while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
					x = Double.parseDouble(str.substring(startPos, this.pos));
				} else if (ch >= 'a' && ch <= 'z') {
					while (ch >= 'a' && ch <= 'z') nextChar();
					String func = str.substring(startPos, this.pos);
					
					if (func.equals("pi")) {
						x = Math.PI;
					} else if (func.equals("e")) {
						x = Math.E;
					} else {
						x = parseFactor(); 
						switch (func) {
							case "sqrt": x = Math.sqrt(x); break;
							case "sin":  x = Math.sin(x); break;   
							case "cos":  x = Math.cos(x); break;   
							case "tan":  x = Math.tan(x); break;   
							case "log":  x = Math.log10(x); break; 
							case "ln":   x = Math.log(x); break;   
							default: throw new RuntimeException("Unknown function: " + func);
						}
					}
				} else {
					throw new RuntimeException("Unexpected character: " + (char) ch);
				}
				
				if (eat('^')) x = Math.pow(x, parseFactor());
				
				return x;
			
			}
		}.parse();
	}

}
