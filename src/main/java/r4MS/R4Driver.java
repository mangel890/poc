package r4MS;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

public class R4Driver
{
   private ChromeDriver driver_;

   public void setUp() throws Exception
   {
      System.setProperty("webdriver.chrome.driver", "ChromeDriver.exe");
      driver_ = new ChromeDriver();
      driver_.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
   }

   public String prepareEnvironment() throws Exception
   {
      driver_.get("https://www.fondotop.com/");
      Thread.sleep(1000);
      while (driver_.findElement(By.id("USUARIO")) == null );
      
      Thread.sleep(1000);
      LoginDialog loginDlg = new LoginDialog(null);
      loginDlg.setVisible(true);
      // if logon successfully
      if (loginDlg.isSucceeded())
      {
         // .setText("Hi " + loginDlg.getUsername() + "!");
      }

      driver_.findElement(By.id("USUARIO")).clear();
      driver_.findElement(By.id("USUARIO")).sendKeys(loginDlg.getUsername());
      driver_.findElement(By.id("PASSWORD")).clear();
      driver_.findElement(By.id("PASSWORD")).sendKeys(loginDlg.getPassword());
      driver_.findElement(By.id("EF_DNI")).clear();
      driver_.findElement(By.id("EF_DNI")).sendKeys(loginDlg.getDni());

      driver_.findElement(By.cssSelector("b")).click();
      
      driver_.get("https://www.fondotop.com/fondotop?TX=login&OPC=7");

      return driver_.getPageSource();

   }

   enum OpType
   {
      Buy, Sell;
   }

   public void extractFundData(String pageSource)
   {

      String fundPattern = "<tr><td><a href=(.*?)";
      
      Pattern r = Pattern.compile(fundPattern);
      Matcher m = r.matcher(pageSource);
      if (m.find( )) 
      {
         System.out.println(m.group(0));
         System.out.println(m.group(1));
         System.out.println(m.group(2));
         System.out.println(m.group(3));
         System.out.println(m.group(4));
      }
      else
      {
         System.out.println("No match");
      }
      ////       tr><td><a href=(.*?)<\/td><\/tr>
      
/*      <tr><td><a href='fondotop?TX=buscador_fnd&OPC=15&ISIN=LU0090865873&DIVI=EUR&MOSTRARCNT=1&BSQ=1'>
      ABERDEEN LIQUIDITY EUR "A2" (EUR) ACC / LU0090865873</a></td><td>
      <div align='center'>EUR</div></td><td><div align='right'>0,448000</div></td>
      <td><div align='right'>446,214022</div></td><td><div align='right'>199,90</div></td>
      <td><div align='right'>0,12</div></td><td><div align='right'>0,11</div></td><td>
      <div align='right'>-0,10</div></td><td><div align='right'>-0,05%</div></td></tr>
  */
      
      /*
      int numberOfFunds = 0;
      int numberOfOperations = 0;
      try
      {
         POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("C:\\RepoMisc\\fondos.xls"));
         HSSFWorkbook wb = new HSSFWorkbook(fs);
         HSSFSheet sheet = wb.getSheetAt(0);

         Iterator<Row> myRowIter = sheet.rowIterator();
         HSSFRow currentRow;
         do
         {
            try
            {
               currentRow = (HSSFRow) myRowIter.next();

               int textColor = currentRow.getCell(0).getCellStyle().getFont(wb).getColor();

               if (textColor != 32767)
               {
                  // This is a new fund
                  numberOfFunds++;

                  // Fund name
                  String fundName = currentRow.getCell(0).getStringCellValue();
                  System.out.print("Fund name: " + fundName);

                  // Isin
                  String isin = fundData_.getIsin(fundName);
                  System.out.println("   ISIN: " + isin);
                  if (isin == null && testingMode)
                  {
                     System.out.println("**********************");
                     System.out.println("**********************");
                     System.out.println("**********************");
                     System.out.println("**********************");
                     System.out.println("**********************");
                     System.out.println("**********************");
                     System.out.println("!ERROR. No Isin found");
                     System.out.println("**********************");
                     System.out.println("**********************");
                     System.out.println("**********************");
                     System.out.println("**********************");
                     System.out.println("**********************");
                     System.out.println("**********************");
                     System.out.println("**********************");
                     System.exit(-1);
                  }

                  // Skip one line and read while cell not empty
                  currentRow = (HSSFRow) myRowIter.next();
                  currentRow = (HSSFRow) myRowIter.next();

                  while (!currentRow.getCell(0).getStringCellValue().equals(""))
                  {

                     numberOfOperations++;
                     // Date
                     String date = currentRow.getCell(0).getStringCellValue();

                     System.out.print("            Date: " + date);

                     String buyOrSell = currentRow.getCell(1).getStringCellValue();

                     // Buy Or Sell
                     boolean buyOp = buyOrSell.contains("ENTRADA") || buyOrSell.contains("SUSCRIPCI�N");

                     if (buyOp)
                     {
                        System.out.print("  Op: BUY");

                     } else
                     {
                        System.out.print("  Op: SELL");
                     }

                     // Shares
                     double shares = currentRow.getCell(2).getNumericCellValue();

                     System.out.print(" Shares:" + shares);

                     // Comission
                     double comission = currentRow.getCell(6).getNumericCellValue();

                     System.out.print(" Comission:" + comission);

                     // Witholding
                     double witholding = currentRow.getCell(7).getNumericCellValue();

                     System.out.print(" Witholding:" + witholding);

                     System.out.println();
                     currentRow = (HSSFRow) myRowIter.next();

                     if (!testingMode)
                     {

                        Thread.sleep(4000);
                        driver_.manage().timeouts().pageLoadTimeout(180, TimeUnit.SECONDS);
                        driver_.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_tbSecName")).click();
                        driver_.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_tbSecName")).clear();
                        driver_.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_tbSecName")).sendKeys(isin);
                        Thread.sleep(1000);
                        driver_.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_tbSecName")).sendKeys(" ");

                        Thread.sleep(2500);

                        driver_.findElement(By.xpath("//body/div[2]/ul/li[2]")).click();

                        if (buyOp)
                           new Select(driver_.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_ddlTransTypeExtended"))).selectByVisibleText("Comprar");
                        else
                           new Select(driver_.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_ddlTransTypeExtended"))).selectByVisibleText("Vender");

                        driver_.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_tbShares")).clear();
                        driver_.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_tbShares")).sendKeys(String.valueOf(shares).replace(".", ","));
                        driver_.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_tbDate")).clear();
                        driver_.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_tbDate")).sendKeys(date);

                        if (comission != 0)
                        {
                           driver_.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_tbCommission")).clear();
                           driver_.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_tbCommission")).sendKeys(String.valueOf(comission + witholding).replace(".", ","));
                        }

                        driver_.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_AddHolding")).click();
                     }
                  }
               }
            } catch (Exception e)
            {
               e.printStackTrace(System.out);
            }
            System.out.println(" Number of funds: " + numberOfFunds + " Number of Operations: " + numberOfOperations);
         } while (myRowIter.hasNext());

      } catch (Exception ioe)
      {
         ioe.printStackTrace();
      }
      */
   }
 
}
