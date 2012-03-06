package metric;

import java.io.IOException;
import java.io.StringWriter;

public class ARFFWriter {
	
	private StringWriter fw = null;

	public void init(int attNum)
	{ 
		String relation = "history";
		try {
			fw = new StringWriter();

			fw.write("@RELATION " + relation + '\n');

			for(int i = 0; i < attNum; i++)
				fw.write("@ATTRIBUTE " + Integer.toString(i) + " numeric" + '\n');

			fw.write("@DATA" + '\n');
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public StringWriter getSW() { return fw; }
}
