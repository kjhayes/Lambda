import java.nio.file.Path;
import java.util.Scanner;
import java.nio.file.Files;
import java.util.List;

class Main {
	  

  public static void main(String[] args) {
	
	Parser p = new Parser();

	try{
		Path path = Path.of("main.lambda");
		List<String> lines = Files.readAllLines(path);
		for(int i = 0; i < lines.size(); i++){
			p.Parse(lines.get(i));
		}
	}catch(Exception e){System.err.println(e);}

	if(p.main_function == null){
		System.err.println("Error: Function \"main\" Not Found");
		return;
	}

	BetaReducer b = new BetaReducer();
	b.single_reduction_per_run = true;

	LambdaExpression l_last = p.main_function;
	
	String s_last = l_last.toString();
	//String s_curr = "";
	LambdaExpression l_curr;
	System.out.println(s_last);

	Scanner scan = new Scanner(System.in);

	boolean stepping = false;
	String line = scan.nextLine();
	if(line.equals("s")){stepping = true;}
	long startTime = 0;
	long endTime = 0;

	startTime = System.nanoTime();
	
	/*
	while(!line.equals("q")){
		b.InitForReduction();
		l_curr = l_last.BetaReduction(b);
		s_curr = l_curr.toString();
		if(s_curr.equals(s_last)){break;}
		else{
			l_last = l_curr;
			s_last = s_curr;
		}
		if(stepping){
			System.out.println(s_last);
			line = scan.nextLine();
			if(line.equals("r")){
				stepping = false;
			}
		}
	}
	*/
	while(!line.equals("q")){
		b.InitForReduction();
		l_curr = l_last.BetaReduction(b);
		if(l_curr.equals(l_last)){break;}
		else{
			l_last = l_curr;
		}
		if(stepping){
			System.out.println(l_last.toString());
			line = scan.nextLine();
			if(line.equals("r")){
				stepping = false;
			}
		}
	}
	endTime = System.nanoTime();

	System.out.println(l_last.toString());

	System.out.println("Total Time: "+((endTime-startTime)/1000000)+" ms");

	scan.close();
  }
}