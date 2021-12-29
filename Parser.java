import java.util.*;

public class Parser {

	final static String main_function_name = "main";

	public LambdaExpression main_function = null;

	Hashtable<String, LambdaExpression> symbol_table = new Hashtable<>();

	public LambdaExpression Parse(String s){		
		s = s.replaceAll("[ \n\t]*", "");
		
		if(s.equals("")){return null;}

		int eq_sign = s.indexOf(":=");
		String alias = "";
		if(eq_sign > 0){
			alias = s.substring(0,eq_sign);
		}else{
			eq_sign = -2;
		}

		int p_sum = 0;
		for(int i = eq_sign+2; i < s.length(); i++){
			if(s.charAt(i) == '('){
				p_sum++;
			}
			else if(s.charAt(i) == ')'){
				p_sum--;
			}
		}
		if(p_sum != 0){
			System.err.println("Error: Ill Formed Statement Fed To Parser.Parse(String) (p_sum = "+p_sum+")");
		}

		ArrayList<LambdaExpression> expressions = new ArrayList<LambdaExpression>();

		for(int i = eq_sign+2; i<s.length(); i++){
			if(s.charAt(i) == '/' || s.charAt(i) == '\u03bb' || s.charAt(i) == 'L'){
				expressions.add(ParseAbstraction(s.substring(i)));
				break;
			}
			else if(s.charAt(i) == '('){
				int end_p = i+1;
				p_sum = 1;
				while(p_sum != 0){
					if(s.charAt(end_p) == '('){p_sum++;}
					else if(s.charAt(end_p) == ')'){p_sum--;}
					end_p++;
				}
				expressions.add(Parse(s.substring(i+1, end_p-1)));
				i = end_p-1;
			}
			else if(s.charAt(i) == '{'){
				int end_cb = i+2;
				while(!(s.charAt(end_cb) == '}')){
					end_cb++;
				}
				String sym = s.substring(i+1,end_cb);
				if(symbol_table.keySet().contains(sym)){
					expressions.add(symbol_table.get(sym).Clone());
				}else{
					System.err.println("Cannot Find Symbol {"+sym+"}!");
				}
				i = end_cb;
			}
			else{
				IVPair p = ParseVariable(s.substring(i));
				expressions.add(p.v);
				i += p.i-1;
			}
		}
		
		int front = 1;
		while(front < expressions.size()){
			expressions.set(front, new Application(expressions.get(front-1),expressions.get(front)));
			front++;
		}

		LambdaExpression ret = expressions.get(expressions.size()-1);

		if(alias.equals(main_function_name)){
			main_function = ret.Clone();
		}
		else if(!alias.equals("")){
			ret.SetAlias(alias);
			symbol_table.put(alias, ret.Clone());
		}

		return ret;
	}

	private IVPair ParseVariable(String s){
		if(s.charAt(0) == '['){
			int end_b = 2;
			while(s.charAt(end_b) != ']'){
				end_b++;
			}
			return new IVPair(
				end_b+1,
				new Variable(s.substring(1,end_b))
			);
		}
		else{
			return new IVPair(1, new Variable(s.substring(0,1)));
		}
	}

	private Abstraction ParseAbstraction(String s){
		Variable v = ParseVariable(s.substring(1)).v;
		int period = s.indexOf(".");
		if(period<0){System.out.println("Error: Missing Abstraction Period"); return null;}
		Abstraction a = new Abstraction(
			v.name,
			Parse(s.substring(period+1))
		);
		return a;
	}
}