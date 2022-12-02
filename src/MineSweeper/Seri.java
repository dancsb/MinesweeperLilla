package MineSweeper;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Seri {

	protected static void load (String name) throws IOException, ClassNotFoundException {
		String file = System.getProperty("players.txt");
		File f = new File(file, name);
		
			if (f.exists()) {
				try {
					FileInputStream fs = new FileInputStream(f);
					ObjectInputStream in = new ObjectInputStream(fs);
					@SuppressWarnings("unchecked")
					int[] fields =  (int[])in.readObject();
					Minesweeper.field = fields;
					in.close();
				} catch(IOException ex) { }  catch(ClassNotFoundException ex) {}
				
			}
		}
	
	protected static void save (String name) throws IOException {
		String file = System.getProperty("players.txt");
		File f = new File(file, name);
		
			if (!f.exists()) f.createNewFile();
			if (f.exists()) {
				try {
					FileOutputStream fs = new FileOutputStream(f);
				ObjectOutputStream out = new ObjectOutputStream(fs);
				out.writeObject(Minesweeper.field);
				out.close();
				} catch(IOException ex) { }
				
			}
		}
}
