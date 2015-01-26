package Project.logic;

public enum Mark {

	O, X, EMPTY;

	public Mark other() {
		if (this == O) {
			return X;
		} else if (this == X) {
			return O;
		} else {
			return EMPTY;
		}
	}

	public static Mark fromInt(int a) {
		if (a == 1) {
			return X;
		} else if (a == 2) {
			return O;
		} else {
			return EMPTY;
		}
	}

	public String toString() {
		if (this == O) {
			return "O";
		} else if (this == X) {
			return "X";
		} else {
			return " ";
		}
	}

}
