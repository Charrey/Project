package Project;

public enum Mark {
	
	O, X, EMPTY;
	
	public Mark other(){
		if(this==O){
			return X;
		}else if(this==X){
			return O;
		}else{
			return EMPTY;
		}
	}
	
	public String toString(){
		if(this==O){
			return "O";
		}else if(this==X){
			return "X";
		}else{
			return " ";
		}
	}

}
