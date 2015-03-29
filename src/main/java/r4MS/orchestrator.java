package r4MS;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;


public class orchestrator
{
  orchestrator()
  {};

   public static void main(String[] args)
   {
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

   }
}
