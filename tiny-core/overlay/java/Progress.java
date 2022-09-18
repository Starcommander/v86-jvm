import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URL;
import java.net.URI;
import java.io.FileOutputStream;
import java.io.File;
import javax.swing.JWindow;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Progress
{
        public static final String HOME_DIR = "/home/tc/";
//        public static final String LOG_DEV = "/dev/ttyS0";
	static final int STATE_INIT = 30; // Wait for communication
	static final int STATE_UPDATE = 60; // Load jar
	static final int STATE_RUNNING = 90; // Run jar

	// create a frame
//	static JFrame f;
	JWindow f;

	JProgressBar b;

	public static void main(String args[])
	{
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            new Progress();
        }
    });
	}

public Progress()
{
Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
int width = (int)screenSize.getWidth();
int height = (int)screenSize.getHeight();


		// create a frame
		f = new JWindow();
//("ProgressBar demo");

		// create a panel
//		JPanel p = new JPanel();

		// create a progressbar
		b = new JProgressBar();

		// set initial value
		b.setValue(0);

		b.setStringPainted(true);

		// add progressbar
//		p.add(b);

		// add panel
//		f.add(p);
b.setSize(width, height/4);
f.setLayout(new BorderLayout());
f.add(b, BorderLayout.NORTH);
		// set the size of the frame
		f.setSize(width, height);
		f.setVisible(true);

new Thread(() -> getInput()).start();
	}

private void showMsg(String msg)
{
  SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(f, msg));
}

private void showProgress(String txt, int percent)
{
    SwingUtilities.invokeLater(() -> { b.setString(txt); b.setValue(percent); });
}

	// Function running in Thread
	private void getInput()
	{
// Working example
/*
ReadWrite rw = new ReadWrite();
b.setString("Wait for jar input");
b.setValue(10);
rw.runJs("console.log('Hallo from java')");
*/
ReadWrite rw = new ReadWrite();
showProgress("Initialisation", STATE_INIT);
while (true)
{
  String line = rw.readSerialLine();
  System.out.println("Reading: " + line);
  if (line==null) { showProgress("Init error[null]", STATE_INIT); }

  else if (line.equals("<log>true"))
  {
    f.setSize(f.getWidth(), f.getHeight()/3);
  }
  else if (line.startsWith("<jar>"))
  {
//showMsg("Just as info: got jar line: '" + line + "' equals:" + line.equals("<jar>"));
    System.out.println("Info: got jar line!");
    if (line.equals("<jar>"))
    {
      showProgress("Update jar", STATE_UPDATE);
      SwingUtilities.invokeLater(() -> addOpenButton(true));
    }
    else // startsWith("<jar>")
    {
      String jar = line.substring(5);
      if (jar.startsWith("http:"))
      {
        showProgress("Update jar", STATE_UPDATE);
        jar = loadHttp(jar);
        if (jar == null)
        {
          showMsg("Error on getting jar.");
          break;
        }
      }
      else if (jar.startsWith("/")) {}
      else
      {
        showMsg("Error, unknown jar protocol (http or '/'): " + jar);
        break;
      }

showProgress("Running JAR", STATE_RUNNING);

//      if (doLog)
//      {
//        try
//        {
//          System.setOut(new java.io.PrintStream(new FileOutputStream(LOG_DEV)));
//          System.setErr(new java.io.PrintStream(new FileOutputStream(LOG_DEV)));
//        } catch (Exception e) { showMsg("Error redirecting log-output"); }
//      }

      URL url = null;
      try
      {
        url = new File(jar).toURI().toURL();
      }
      catch (Exception e)
      {
        showMsg("Invalid url: " + jar);
        break;
      }
      var classL = new JarClassLoader(url);
      try
      {
        classL.invokeMainClass(new String[0]);
      }
      catch (Exception e)
      {
        showMsg("Uncatched exception thrown by main class: " + e);
        break;
      }
//      else if (jar.equals("wait")) { if (!doSleep()) break; }
    }
  }
  else
  {
showProgress("Unknown input: " + line, STATE_INIT);
  }
}

// Lese von JS wegen jar
// Download jar, oder Button wenn null
// Run jar


/*
		int i = 0;
		try {
			while (i <= 100) {
				// set text according to the level to which the bar is filled
				if (i > 30 && i < 70)
					b.setString("wait for sometime");
				else if (i > 70)
					b.setString("almost finished loading");
				else
					b.setString("loading started");

				// fill the menu bar
				b.setValue(i + 10);

				// delay the thread
				Thread.sleep(3000);
				i += 20;
			}
//f.setVisible(false);
//f.dispose();
		}
		catch (Exception e) {
		}
*/
	}

	private String loadHttp(String jar)
	{
		try
		{
//String doLogS = "";
//if (doLog) { doLogS = "-o " + LOG_DEV; }
Runtime.getRuntime().exec("sh ../sh/getHttp.sh " + jar + " " + HOME_DIR + "App.jar").waitFor();
//			Runtime.getRuntime().exec("wget -O " + HOME_DIR + "App.jar " + doLogS + " " + jar).waitFor();
			return HOME_DIR + "App.jar";
		}
		catch (Exception ex)
		{
			showMsg("Error downloading jar: " + ex);
		}
		return null;
	}

	private void addOpenButton(boolean clearProgressBar)
	{
		JButton openB = new JButton("Open JAR");
                openB.addActionListener(e -> openJarAction());
		f.add(b, BorderLayout.NORTH);
		if (clearProgressBar) { f.remove(b); }
	}

	private boolean doSleep()
	{
		try
		{
			Thread.currentThread().sleep(1000);
			return true;
		}
		catch (InterruptedException ex)
		{
			showMsg("Error, sleep was interrupted.");
		}
		return false;
	}

	public void openJarAction()
	{
		f.dispose();
	}
}
