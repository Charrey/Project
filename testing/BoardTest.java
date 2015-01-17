package Project.testing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Project.logic.Board;
import Project.logic.Mark;

public class BoardTest {

	Mark m = Mark.X;
	Board emptyBoard = new Board();
	Board hasFourInRow = new Board();
	Board hasFourInColumn = new Board();
	Board hasFourInDiagnol = new Board();
	Board invalidPut = new Board();
	Board fullBoard = new Board();
	int heightVar;
	
	@Before
	public void setUp() throws Exception {
	
	
	
	
	hasFourInRow.putMark(0, m);
	hasFourInRow.putMark(1, m);
	hasFourInRow.putMark(2, m);
	hasFourInRow.putMark(3, m);
	

	hasFourInColumn.putMark(0, m);
	hasFourInColumn.putMark(0, m);
	hasFourInColumn.putMark(0, m);
	hasFourInColumn.putMark(0, m);
	
	
	hasFourInDiagnol.putMark(0, 0, m);
	hasFourInDiagnol.putMark(1, 1, m);
	hasFourInDiagnol.putMark(2, 2, m);
	hasFourInDiagnol.putMark(3, 3, m);
	
	
	invalidPut.putMark(0, m);
	invalidPut.putMark(0, m);
	invalidPut.putMark(0, m);
	invalidPut.putMark(0, m);
	invalidPut.putMark(0, m);
	invalidPut.putMark(0, m);
	invalidPut.putMark(0, m);
	heightVar = invalidPut.putMark(0, m);
	
	
	for(int x=0; x<fullBoard.getWidth(); x++){
		for(int y=0; y<fullBoard.getHeight(); y++){
			fullBoard.putMark(x, y, m);
		}
	}
	
	
	
	}

	@Test
	public void test() {
		emptyBoardTest();
		hasFourInRowTest();
		hasFourInColumnTest();
		hasFourInDiagnolTest();
		fullBoardTest();
		InvalidPutTest();
		
		
		
		
		System.out.println("Test Completed");
	}
	
	private void InvalidPutTest() {
		ifExpected("is put in -1", -1,heightVar);
		ifExpected("Is validInput?", -1,fullBoard.putMark(fullBoard.getWidth(), m));
		
	}

	private void fullBoardTest() {
		ifExpected("Is Full (fullBoard)", true,fullBoard.isFull());	
		ifExpected("Is GameOver (fullBoard)", true, fullBoard.gameOver());
		fullBoard.reset();
		ifExpected("Proper Reset (fullBoard)", fullBoard.getHeight(), fullBoard.emptySlotCount(0));
	}

	private void hasFourInDiagnolTest() {
		ifExpected("Has win (diagnol)", true, hasFourInDiagnol.isWin());
		ifExpected("Is GameOver (diagnlo)", true, hasFourInDiagnol.gameOver());
		
	}

	private void hasFourInColumnTest() {
		ifExpected("Has win (hasFourInColumn)", true, hasFourInColumn.isWin());
		ifExpected("Is GameOver (hasFourInColumn)", true, hasFourInColumn.gameOver());
		
	}

	private void hasFourInRowTest() {
		ifExpected("Has win (row)", true, hasFourInRow.isWin());
		ifExpected("Is GameOver (hasFourInRow)", true, hasFourInRow.gameOver());
		
	}

	public void emptyBoardTest(){
		ifExpected("Is board full (emptyBoard)", false, emptyBoard.isFull());
		ifExpected("Has four in row? (emptyBoard)", false, emptyBoard.isWin());
		ifExpected("EmptySlotCount (emptyBoard)", emptyBoard.getHeight() ,emptyBoard.emptySlotCount(0));
		ifExpected("Is GameOver (emptyBoard)", false, emptyBoard.gameOver());
		
		
	}
	
	public void ifExpected(String description, Object expected, Object result){
		if(!expected.equals(result)){
			System.out.println(description + "Expected: "+ expected.toString()+ "Result: " + result.toString());
		}
	}

}
