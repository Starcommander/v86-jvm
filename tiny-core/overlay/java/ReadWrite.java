import java.io.FileWriter;
import java.io.FileReader;
//import java.io.FileInputStream;
//import java.io.InputStreamReader;
import java.io.IOException;

public class ReadWrite
{
  final static String SERIAL_DEV="/dev/ttyS0";
  final static String TAG_START="<execute>\n";
  final static String TAG_END="\n<end>\n";

  enum ReadState
  {
    None("<end>"),
    Execute("<execute>");

    private String name;
    ReadState(String name) { this.name = name; }
    public String getTagName() { return name; }
  };

  ReadState curState = ReadState.None;
  FileReader fr;

  public ReadWrite()
  {
    try
    {
      fr = new FileReader(SERIAL_DEV);
    }
    catch (Exception e)
    {
      System.out.println("Error reading SERIAL_DEV");
      e.printStackTrace();
    }
    Runtime.getRuntime().addShutdownHook(new Thread(() -> onExit()));
  }

  private void onExit()
  {
    try
    {
      if (fr != null) fr.close();
    }
    catch (Exception e)
    {
      System.out.println("Error closing SERIAL_DEV");
      e.printStackTrace();
    }
  }

  public void runJs(String script)
  {
    writeSerial(TAG_START + script + TAG_END);
  }

  private void writeSerial(String serData)
  {
    try(FileWriter fw = new FileWriter(SERIAL_DEV))
    {
      fw.write(serData);
    }
    catch (IOException e) { e.printStackTrace(); }
  }

  /** Loops until end of command. */
/*
  public void readJS_loop()
  {
    String line;
    while ((line = readSerialLine()) != null)
    {
      if (line.equals(ReadState.None.getTagName()))
      {
        curState = ReadState.None;
        break;
      }
      else if (curState == ReadState.Execute)
      {
        try
        {
          Runtime.getRuntime().exec(line);
        } catch (Exception e) { e.printStackTrace(); }
      }
      else if (line.equals(ReadState.Execute.getTagName()))
      {
        curState = ReadState.Execute;
      }
    }
  }
*/

  public String readSerialLine()
  {
    StringBuilder sb = new StringBuilder();
//    try(FileReader fr = new FileReader(SERIAL_DEV))
//    try (var fis = new FileInputStream(SERIAL_DEV); var fr = new InputStreamReader(fis))
    try
    {
      int c;
      while ((c = fr.read()) != -1)
      {
        if (c == '\n' || c == '\r') { break; }
        sb.append((char)c);
      }
    }
    catch (IOException e) { e.printStackTrace(); return null; }
    return sb.toString();
  }
}
