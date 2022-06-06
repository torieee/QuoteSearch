
package SearchQuote;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class QuoteReader {
    
    private String open;
    private String current;
    private String ytd;
    private String dayChange;
    private String name;
    String cnbcURL;
    
    
    public static void main(String[] args) throws IOException {
        
        QuoteReader sp = new QuoteReader("aapl");
        //System.out.println("Open: " + sp.currentOpen());
//        System.out.println("Current price: " + sp.currentQuote());
        System.out.println(sp.getName());
        //System.out.println(sp.getOpen());
    }
    
    public QuoteReader(String index) throws IOException 
    {
        cnbcURL = "https://www.cnbc.com/quotes/" + index;
        
        try
        {
            this.open = getOpen();
            this.current = getCurrent();
            this.ytd = getYTD();
            this.dayChange = getDayChange();
            this.name = getName();
            
            //System.out.println(index);
        }
        catch (FileNotFoundException ex)
        {
            System.err.println("Market is closed!");
        }
    }
    
    //Gets daily OPENING quote (updated on CNBC daily)
    private String getOpen() throws IOException
    {
        String open = "";
        Document doc = Jsoup.connect(cnbcURL).get();
        
        Elements divs = doc.select("li[class=Summary-stat Summary-prevClose]");
        
        for (Element e : divs) 
        {
            open = e.getElementsByClass("Summary-value").text();
            break;
        }
        
        return open;
    } 
    
    public String currentOpen()
    {
        return this.open;
    }
    
    public String toStringOpen()
    {
        return open + "";
    }
    
    
    //Gets CURRENT quote (updated on CNBC every few seconds)
    private String getCurrent() throws IOException 
    {
        String current = "";
        Document doc = Jsoup.connect(cnbcURL).get();
        
        Elements divs = doc.select("div[class=QuoteStrip-lastPriceStripContainer]");
        
        for (Element e : divs) 
        {
            current = e.getElementsByClass("QuoteStrip-lastPrice").text();
        }
        
        return current;
    }
    
    public String currentQuote()
    {
        return this.current;
    }
    
    public String toStringCurrent()
    {
        return current + "";
    }
    
    //Gets the current daily PERCENT CHANGE (updated every few seconds on CNBC)
    private String getDayChange() throws IOException
    {
        String currentVar = getCurrent().replace(",", "");
        String openVar = getOpen().replace(",", "");
        
        //System.out.println("Current " + currentVar);
        //System.out.println("Open " + openVar);
        
        String dayChange = "";
        String change = "";
        Document doc = Jsoup.connect(cnbcURL).get();
        
        Elements divs = doc.select("div[class=QuoteStrip-lastPriceStripContainer]");
        
        for (Element e : divs) 
        {
            if(Double.parseDouble(currentVar) > Double.parseDouble(openVar))
            {
                dayChange = e.getElementsByClass("QuoteStrip-changeUp").text();
                change = dayChange.substring(dayChange.indexOf("(") + 1, 
                        dayChange.indexOf(")") - 1);
            }
            else
            {
                dayChange = e.getElementsByClass("QuoteStrip-changeDown").text();
                change = dayChange.substring(dayChange.indexOf("(") + 1, 
                        dayChange.indexOf(")") - 1);
            }
            
            break;
        }
        
        return change + "%";
        
    }
    
    public String currentDayChange()
    {
        return this.dayChange;
    }
    
    public String toStringDayChange()
    {
        return dayChange + "";
    }
    
    
    private String getYTD() throws IOException
    {
        String janOpen = "";
        Document doc = Jsoup.connect(cnbcURL).get();
        
        Elements divs = doc.select("li[class=Summary-stat Summary-ytdPercChange]");
        
        for (Element e : divs) 
        {
            janOpen = e.getElementsByClass("Summary-value").text();
            break;
        }
        
        return janOpen;
    } //eo getYTD()
    
    public String currentYTD()
    {
        return this.ytd;
    }
    
    public String toStringYTD()
    {
        return "" +  ytd + "%";
    }
    
    //Gets the name of the company/index
    private String getName() throws IOException
    {
        String name = "";
        Document doc = Jsoup.connect(cnbcURL).get();
        
        Elements divs = doc.select("div[class=QuoteStrip-companyName]");
        
        for (Element e : divs) 
        {
            name = e.getElementsByClass("QuoteStrip-name").text();
            break;
        }
        
        return name;
    } //eo getYTD()
    
    public String companyName()
    {
        return this.name;
    }
    
    public String toStringName()
    {
        return name;
    }
}
