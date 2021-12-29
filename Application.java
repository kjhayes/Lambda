public class Application extends LambdaExpression {
	
	final private LambdaExpression left;
	final private LambdaExpression right;

	public Application(LambdaExpression left, LambdaExpression right){
		this.left = left;
		this.right = right;
	}

	public LambdaExpression Substitute(String s, LambdaExpression l){
		return new Application(
			left.Substitute(s, l),
			right.Substitute(s, l));
	}

	public LambdaExpression BetaReduction(BetaReducer b) {
		if(b.ShouldContinue()){
			if(left.IsAbstraction() != null){
				LambdaExpression l;
				if(b.single_reduction_per_run){
					l = left.Substitute("", right.Clone());
				}else{
					l = left.BetaReduction(b).Substitute("", right.BetaReduction(b));
				}
				l.SetAlias(alias);
				b.reduction_has_occurred = true;
				return l;
			}
			else{
				Application a = new Application(left.BetaReduction(b), right.BetaReduction(b));
				a.SetAlias(alias);
				return a;
			}
		}
		else{
			return Clone();
		}
	}

	@Override
	public Application IsApplication(){return this;}

	public boolean IsFreeVariable(String s){
		return left.IsFreeVariable(s) || right.IsFreeVariable(s);
	}
	public boolean IsSubExpression(LambdaExpression l){
		return l == this || left.IsSubExpression(l) || right.IsSubExpression(l);
	}

	public LambdaExpression Clone(){
		Application a = new Application(left.Clone(), right.Clone());
		a.SetAlias(alias);
		return a;
	}

	public String toString(){
		if(!alias.equals("")){
			return "{"+alias+"}";
		}
		else if(left.IsSubExpression(this) || right.IsSubExpression(this)){
			System.err.println("RECURSIVE APPLICATION ERROR");
			return "ERROR";
		}
		
		String l_s = left.toString();
		String r_s = right.toString();

		if(left.IsAbstraction() != null && left.alias.equals("")){
			l_s = "("+l_s+")";
		}
		if((right.IsApplication() != null || right.IsAbstraction() != null) && right.alias.equals("")){
			r_s = "("+r_s+")";
		}

		return l_s+r_s;
	}

	public boolean equals(LambdaExpression other){
		Application a = other.IsApplication();
		return a!=null && left.equals(a.left) && right.equals(a.right);
	}
}