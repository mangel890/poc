package r4MS;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class MorningStarDriver
{
   private ChromeDriver driver_;
   private String baseUrl_;
   private static FundData fundData_;

   public void setUp(FundData fundData) throws Exception
   {
      System.setProperty("webdriver.chrome.driver", "ChromeDriver.exe");
      driver_ = new ChromeDriver();
      baseUrl_ = "http://www.morningstar.es/";
      driver_.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
      fundData_ = fundData;
   }

   public void prepareEnvironment() throws Exception
   {
      driver_.get(baseUrl_ + "/es/");

      driver_.findElement(By.cssSelector("#lnkLogin > span")).click();
      driver_.switchTo().frame("frameContainer");

      for (int second = 0;; second++)
      {
         if (second >= 60)
            throw new Exception("timeout");
         try
         {
            if (isElementPresent(By.id("txtUsername")))
               break;
         } catch (Exception e)
         {
            e.printStackTrace(System.out);
         }
         Thread.sleep(1000);
      }

      LoginDialog loginDlg = new LoginDialog(null).hideDni();
      loginDlg.setVisible(true);
      // if logon successfully
      if (loginDlg.isSucceeded())
      {
         // .setText("Hi " + loginDlg.getUsername() + "!");
      }

      driver_.findElement(By.id("txtUsername")).clear();
      driver_.findElement(By.id("txtUsername")).sendKeys(loginDlg.getUsername());
      driver_.findElement(By.id("txtPasswordText")).clear();
      driver_.findElement(By.id("txtRealPassword")).clear();
      driver_.findElement(By.id("txtRealPassword")).sendKeys(loginDlg.getPassword());

      driver_.findElement(By.cssSelector("a.jqTransformCheckbox")).click();
      driver_.findElement(By.id("btnLogin")).click();

      driver_.switchTo().window("Object");
      for (int second = 0;; second++)
      {
         if (second >= 60)
            throw new Exception("timeout");
         try
         {
            if (isElementPresent(By.linkText("Mi Cartera")))
               break;
         } catch (Exception e)
         {
            e.printStackTrace(System.out);
         }
         Thread.sleep(1000);
      }
      Thread.sleep(5000);
      driver_.findElement(By.linkText("Mi Cartera")).click();
      driver_.findElement(By.cssSelector("div.linkPortfolioNew")).click();

      driver_.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_PortfolioNameTextBox")).clear();
      driver_.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_PortfolioNameTextBox")).sendKeys("MyNewPortfolio5");
      driver_.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_TransactionRadio")).click();

      driver_.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_NextButton")).click();
   }

   enum OpType
   {
      Buy, Sell;
   }

   public void populateFunds(boolean testingMode)
   {
      int numberOfFunds = 0;
      int numberOfOperations = 0;
      try
      {
         POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("fondos.xls"));
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
                     System.out.println("ERROR. No Isin found!");
                     System.out.println("Aborting!");
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
   }

   private boolean isElementPresent(By by)
   {
      try
      {
         driver_.findElement(by);
         return true;
      } catch (NoSuchElementException e)
      {
         return false;
      }
   }



}
