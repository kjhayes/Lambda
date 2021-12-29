public abstract class LambdaExpression {

	protected String alias = "";

	public abstract LambdaExpression BetaReduction(BetaReducer b);
	public abstract LambdaExpression Substitute(String s, LambdaExpression l);

	public Abstraction IsAbstraction(){return null;}
	public Application IsApplication(){return null;}
	public Variable IsVariable(){return null;}

	public abstract boolean IsFreeVariable(String s);
	public abstract boolean IsSubExpression(LambdaExpression l);

	public void SetAlias(String alias){
		this.alias = alias;
	}

	public abstract LambdaExpression Clone();

	public abstract String toString();

	public abstract boolean equals(LambdaExpression other);
}