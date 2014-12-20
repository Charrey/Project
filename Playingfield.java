package Project;

public class Playingfield {
	int width;
	int height;
	int[][] layout;
	boolean won;
	int playerwon;

	public Playingfield(int width, int height) {
		this.width = width;
		this.height = height;
		layout = new int[width][height];
		for (int i = 0; i < width; i++) {
			for (int p = 0; p < height; p++) {
				layout[i][p] = 0;
			}
		}
		won=false;
		playerwon=0;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int play(int column, Player player, boolean test) {
		for (int i = 0; i < height; i++) {
			if (layout[column][i] == 0) {
				layout[column][i] = player.getMark().getNo();
				if (test == false && checkWin(column, i)) {
					won=true;
				}
				return (column * 100) + i;
			}
		}
		return -1;
	}

	public int getValue(int a, int b) {
		return layout[a][b];
	}

	public int checkDirection(int a, int b, int toright, int toabove) {
		int checking = getValue(a, b);
		int testa = a;
		int testb = b;
		int stones = 0;
		while (testa <= getWidth() - 1 && testa >= 0
				&& testb <= getHeight() - 1 && testb >= 0
				&& layout[testa][testb] == checking) {
			stones++;
			testa += toright;
			testb += toabove;
		}
		return stones;

	}

	public boolean checkWin(int a, int b) {
		if (checkDirection(a, b, 1, 0) + checkDirection(a, b, -1, 0) - 1 >= 4) {
			return true;
		} else if (checkDirection(a, b, 0, 1) + checkDirection(a, b, 0, -1) - 1 >= 4) {
			return true;
		} else if (checkDirection(a, b, 1, 1) + checkDirection(a, b, -1, -1)
				- 1 >= 4) {
			return true;
		} else if (checkDirection(a, b, -1, 1) + checkDirection(a, b, 1, -1)
				- 1 >= 4) {
			return true;
		} else {
			return false;
		}
	}

	public void stringPrint() {
		for (int p = height - 1; p >= 0; p--) {
			String line = "";
			for (int i = 0; i < width; i++) {
				line += " " + layout[i][p];
			}
			System.out.println(line);
		}
		System.out.println(" ");
	}

	public String playerString(int player) {
		if (player == 0) {
			return " ";
		} else if (player == 1) {
			return "O";
		} else if (player == 2) {
			return "X";
		} else {
			System.err.println("ERROR: non-existant player");
			return null;
		}
	}
	
	public boolean getWon() {
		return won;
	}
	
	public int playerWon() {
		return playerwon;
	}
	
	public boolean isFull() {
		for (int i=0; i<getWidth();i++) {
			if (getValue(i, getHeight()-1)==0) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean validInput(String input) {
		if (input.length()!=1 || !Character.isDigit(input.charAt(0))) {
			return false;
			
		}
		return true;
	}
	

}
