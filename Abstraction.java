public class Abstraction extends LambdaExpression {
	
	private String bound;
	private LambdaExpression expr;

	Abstraction(String b, LambdaExpression l){
		bound = b;
		expr = l;
	}

	public LambdaExpression Substitute(String s, LambdaExpression l){
		if(s.equals("")){
			return expr.Substitute(bound, l);
		}
		else if(!bound.equals(s)){
			int i = 0;
			if(l.IsFreeVariable(bound)){
				AlphaReduce(Variable.GenerateVariable(i));
				i++;
			}
			Abstraction a = new Abstraction(bound, expr.Substitute(s, l));
			if(a.toString().equals(toString())){
				a.SetAlias(alias);
			}
			return a;
		}
		return Clone();
	}

	public void AlphaReduce(String s){
		expr = expr.Substitute(bound, new Variable(s));
		bound = s;
	}

	public LambdaExpression BetaReduction(BetaReducer b){
		if(b.ShouldContinue()){
			expr = expr.BetaReduction(b);
			return this;
		}
		return Clone();
	}

	public boolean IsFreeVariable(String s){
		return !bound.equals(s) && expr.IsFreeVariable(s);
	}
	public boolean IsSubExpression(LambdaExpression l){
		return l == this || expr.IsSubExpression(l);
	}

	public LambdaExpression Clone(){
		Abstraction a = new Abstraction(bound, expr.Clone());
		a.SetAlias(alias);
		return a;
	}

	public String toString(){
		/*
		if(expr.IsSubExpression(this)){
			System.err.println("RECURSIVE ABSTRACTION ERROR");
			return "ERROR";
		}
		*/
		if(alias.equals("")){
			return "/"+bound+"."+expr.toString();
		}
		else{
			return "{"+alias+"}";
		}
	}

	@Override
	public Abstraction IsAbstraction(){return this;}

	public boolean equals(LambdaExpression other){
		Abstraction a = other.IsAbstraction();
		if(a == null){return false;}
		if(!a.bound.equals(bound)){
			a.AlphaReduce(bound);
		}
		return expr.equals(a.expr);
	}
}