package r4MS;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;


public class orchestrator
{
  orchestrator()
  {};

   public static void main(String[] args)
   {
      
      R4Driver r4Driver = new R4Driver();
      try
      {
         //r4Driver.setUp();
         //r4Driver.prepareEnvironment();
         String pageSource = "";
         for (String line: Files.readAllLines(Paths.get("C:\\Repo\\poc\\test.html"), Charset.forName("ISO-8859-1")))
            pageSource += line;
         r4Driver.extractFundData(pageSource);
         
      }
      catch (Exception e)
      {
         e.printStackTrace(); 
      }
      
      /*
      MorningStarDriver mSDriver = new MorningStarDriver();
      try
      {
         JFileChooser chooser = new JFileChooser();
         FileNameExtensionFilter filter = new FileNameExtensionFilter("Name to ISIN maps", "txt");
         chooser.setFileFilter(filter);
         chooser.setCurrentDirectory(new File("."));
         int returnVal = chooser.showOpenDialog(null);
         if (returnVal == JFileChooser.APPROVE_OPTION)
         {

            FundData fundData = new FundData(chooser.getSelectedFile().getAbsoluteFile().toString());
            mSDriver.setUp(fundData);
            mSDriver.populateFunds(true); //testing round
            mSDriver.prepareEnvironment();
            mSDriver.populateFunds(false); //final round
         }
      } catch (Exception e)
      {
         e.printStackTrace();
      }
*/
   }
}
