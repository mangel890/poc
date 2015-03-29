package r4MS;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class FundData
{

   public FundData(String fileName)
   {
      fundNameToIsin_ = new HashMap<String, String>();
      loadData(fileName);

   };

   String getIsin(String fundName)
   {
      return fundNameToIsin_.get(fundName.trim());
   };

   public boolean addIsin(String fundName, String isin)
   {
      fundNameToIsin_.put(fundName, isin);
      return true;
   };
   
   private boolean loadData(String fileName)
   {
      try
      {

         BufferedReader br = new BufferedReader(new FileReader(fileName));
         String line;
         while ((line = br.readLine()) != null)
         {
            String fundName = line.substring(0, line.indexOf('/')).trim();
            String isin = line.substring(line.indexOf('/')+1).trim();
            fundNameToIsin_.put(fundName, isin);

         }
         br.close();
      }

      catch (Exception e)
      {
         e.printStackTrace(System.out);
      }
      return true;
   }

   private HashMap<String, String> fundNameToIsin_;

}
