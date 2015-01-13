package Project.logic;

public class TUI {

	private Board playingfield;
	
	public TUI(Board board){
		playingfield = board;
	}
	

	public String showBoard() {
		String result = "Board layout: \n\n";
		int width = playingfield.getWidth();
		int height = playingfield.getHeight();
		String lineresult = "";
		for (int r = 0; r < width; r++) {
			lineresult += "----";
		}
		result += lineresult + "\n";
		for (int i = height - 1; i >= 0; i--) {
			String resultline = "|";
			for (int p = 0; p < width; p++) {
				resultline += " "
						+ playingfield.getPlace(p, i).toString()
						+ " |";
			}
			result += resultline + "\n";
			result += lineresult + "\n";
		}
		String guidance = "  ";
			for (int i =0; i<width; i++) {
				guidance += i+"   ";
			}
		result += guidance + "\n\n";
		return result;
	}
}
