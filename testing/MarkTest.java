package Project.testing;

import org.junit.Before;
import org.junit.Test;

import Project.logic.Mark;

public class MarkTest {

	Mark mO;
	Mark mX;
	Mark mE;
	
	@Before
	public void setUp() throws Exception {
		mO = Mark.O;
		mX = Mark.X;
		mE = Mark.EMPTY;
	}

	@Test
	public void test() {
		ifExpected("Other O", Mark.X, mO.other());
		ifExpected("Other X", Mark.O, mX.other());
		ifExpected("Other EMPTY", Mark.EMPTY, mE.other());
		
		ifExpected("To String O", "O", mO.toString());
		ifExpected("To String X", "X", mX.toString());
		ifExpected("To String EMPTY", " ", mE.toString());
		
		ifExpected("From int to mark (0)", mE, Mark.fromInt(0));
		ifExpected("From int to mark (1)", mX, Mark.fromInt(1));
		ifExpected("From int to mark (2)", mO, Mark.fromInt(2));
		
		System.out.println("Test finished");
		
		
	}
	
	public void ifExpected(String description, Object expected, Object result){
		if(!expected.equals(result)){
			System.out.println(description + "Expected: "+ expected.toString()+ "Result: " + result.toString());
		}
	}

}
