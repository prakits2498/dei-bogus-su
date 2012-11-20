package meta1;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;
import org.omg.CORBA_2_3.portable.OutputStream;

import testing.Main;

public class MainTest {

	@Test
	public void testMainMenu() {
		Main m = new Main();
		//Prepare to capture output
		PrintStream originalOut = System.out;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(os);
		
		//Perform tests
		m.mainMenu();
		assertNotSame("", os.toString());
		
		//Restore normal operation
		System.setOut(originalOut);
		
		fail("Deu bogus");
	}

	@Test
	public void testLoginPicasa() {
		Main m = new Main();
		m.loginPicasa();
		
		fail("Not yet implemented");
	}

	@Test
	public void testShowAlbums() {
		fail("Not yet implemented");
	}

	@Test
	public void testShowAlbumPhotos() {
		fail("Not yet implemented");
	}

	@Test
	public void testLoginFlickr() {
		Main m = new Main();
		
		fail("Not yet implemented");
	}

}
