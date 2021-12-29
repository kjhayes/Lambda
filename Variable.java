public class Variable extends LambdaExpression {
	
	public String name;

	public Variable(String n){
		name = n;
	}

	public LambdaExpression Substitute(String s, LambdaExpression l){
		if(s.equals(name)){
			return l.Clone();
		}
		else{
			return this;
		}
	}

	public LambdaExpression BetaReduction(BetaReducer b){
		return this;
	}

	public boolean IsFreeVariable(String s){
		return s.equals(name);
	}
	public boolean IsSubExpression(LambdaExpression l){
		return l == this;
	}

	public LambdaExpression Clone(){
		return this;
	}

	public String toString(){
		if(name.length() == 1){
			return name;
		}
		return "["+name+"]";
	}

	//@param i Must Be Positive
	public static String GenerateVariable(int i){
		if(i < 0){System.err.println("Error: Negative Value Passed To Variable.GenerateValue(int)");return "ERR";}
		else if(i < 26){
			return String.valueOf((char)(i+97));
		}
		else if(i < 52){
			return String.valueOf((char)(i+39));
		}
		else{
			System.err.println("Error: Number of Variables Exceeded");
			return "ERR";
		}
	}

	@Override
	public Variable IsVariable() {return this;}

	public boolean equals(LambdaExpression other){
		Variable v = other.IsVariable();
		return (v!=null && (v==this || v.name.equals(name)));
	}
}