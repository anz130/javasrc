import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.JOptionPane;

/**
 * Read a file of reminders, sleep until each is due, beep.
 * @author Ian F. Darwin, ian@darwinsys.com
 * @version $Id$
 */
public class ReminderService {

	/** The Timer object */
	Timer timer = new Timer();

	class Item extends TimerTask {
		String message;
		Item(String m) {
			message = m;
		}
		public void run() {
			message(message);
		}
	}

	public static void main(String[] argv) throws IOException {
		new ReminderService().load();
	}

	protected void load() throws IOException {

		BufferedReader is = new BufferedReader(
			new FileReader("ReminderService.txt"));
		SimpleDateFormat formatter =
			new SimpleDateFormat ("yyyy MM dd hh mm");
		String aLine;
		while ((aLine = is.readLine()) != null) {
			ParsePosition pp = new ParsePosition(0);
			Date date = formatter.parse(aLine, pp);
			if (date == null) {
				message("Invalid date in " + aLine);
				continue;
			}
			String mesg = aLine.substring(pp.getIndex());
			timer.schedule(new Item(mesg), date);
		}
	}

	void message(String message) {
		System.out.println("\007" + message);
		JOptionPane.showMessageDialog(null,
			message, 
			"Timer Alert",				// titlebar
			JOptionPane.INFORMATION_MESSAGE);	// icon
	}
}
