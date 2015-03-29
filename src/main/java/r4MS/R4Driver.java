package r4MS;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class R4Driver
{
   private ChromeDriver driver;
   private String baseUrl;

   public void setUp() throws Exception
   {
      System.setProperty("webdriver.chrome.driver", "C:\\RepoMisc\\libs\\ChromeDriver.exe");
      driver = new ChromeDriver();
      baseUrl = "http://www.morningstar.es/";
      driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
   }

   public void prepareEnvironment() throws Exception
   {
      driver.get(baseUrl + "/es/");

      driver.findElement(By.cssSelector("#lnkLogin > span")).click();
      driver.switchTo().frame("frameContainer");

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

      LoginDialog loginDlg = new LoginDialog(null);
      loginDlg.setVisible(true);
      // if logon successfully
      if (loginDlg.isSucceeded())
      {
         // .setText("Hi " + loginDlg.getUsername() + "!");
      }

      driver.findElement(By.id("txtUsername")).clear();
      driver.findElement(By.id("txtUsername")).sendKeys(loginDlg.getUsername());
      driver.findElement(By.id("txtPasswordText")).clear();
      driver.findElement(By.id("txtRealPassword")).clear();
      driver.findElement(By.id("txtRealPassword")).sendKeys(loginDlg.getPassword());

      driver.findElement(By.cssSelector("a.jqTransformCheckbox")).click();
      driver.findElement(By.id("btnLogin")).click();

      driver.switchTo().window("Object");
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
      driver.findElement(By.linkText("Mi Cartera")).click();
      driver.findElement(By.cssSelector("div.linkPortfolioNew")).click();

      driver.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_PortfolioNameTextBox")).clear();
      driver.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_PortfolioNameTextBox")).sendKeys("MyNewPortfolio5");
      driver.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_TransactionRadio")).click();

      driver.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_NextButton")).click();
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
                        driver.manage().timeouts().pageLoadTimeout(180, TimeUnit.SECONDS);
                        driver.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_tbSecName")).click();
                        driver.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_tbSecName")).clear();
                        driver.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_tbSecName")).sendKeys(isin);
                        Thread.sleep(1000);
                        driver.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_tbSecName")).sendKeys(" ");

                        Thread.sleep(2500);

                        driver.findElement(By.xpath("//body/div[2]/ul/li[2]")).click();

                        if (buyOp)
                           new Select(driver.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_ddlTransTypeExtended"))).selectByVisibleText("Comprar");
                        else
                           new Select(driver.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_ddlTransTypeExtended"))).selectByVisibleText("Vender");

                        driver.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_tbShares")).clear();
                        driver.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_tbShares")).sendKeys(String.valueOf(shares).replace(".", ","));
                        driver.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_tbDate")).clear();
                        driver.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_tbDate")).sendKeys(date);

                        if (comission != 0)
                        {
                           driver.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_tbCommission")).clear();
                           driver.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_tbCommission")).sendKeys(String.valueOf(comission + witholding).replace(".", ","));
                        }

                        driver.findElement(By.id("ctl00_ctl00_MainContent_PM_MainContent_AddHolding")).click();
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
         driver.findElement(by);
         return true;
      } catch (NoSuchElementException e)
      {
         return false;
      }
   }

   private static FundData fundData_;

   public static void main(String[] args)
   {
      R4Driver twd = new R4Driver();
      try
      {
         JFileChooser chooser = new JFileChooser();
         FileNameExtensionFilter filter = new FileNameExtensionFilter("Name to ISIN maps", "txt");
         chooser.setFileFilter(filter);
         int returnVal = chooser.showOpenDialog(null);
         if (returnVal == JFileChooser.APPROVE_OPTION)
         {

            fundData_ = new FundData(chooser.getSelectedFile().getAbsoluteFile().toString());
            twd.setUp();
            twd.populateFunds(true);
            twd.prepareEnvironment();
            twd.populateFunds(false);
         }
      } catch (Exception e)
      {
         e.printStackTrace();
      }

   }
}
