
package SearchQuote;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;


/*
PROBLEMS:

1. Fix size of searchField TextField
2. Fix spacing of percent changes and "day" and "ytd"
*/

public class SearchQuoteGUI extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
       
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: #FFFFFF");
        pane.setPadding(new Insets (5, 20, 20, 20));
        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);
        GridPane innerGrid = new GridPane();
        innerGrid.setHgap(50);
        
        //main grid goes in the center of BP
        pane.setCenter(grid);
        
        //Create TF to search for stock and put it at bottom of grid pane
        TextField searchField = new TextField();
        //NEED TO FIX THE WIDTH
        grid.add(searchField, 0, 4);
        
        //Labels for each grid space
        final Text tickerText =  new Text();
        tickerText.setStyle("-fx-font: bold 35px Helvetica, Helvetica, sans-serif");
        grid.add(tickerText, 0, 0);
        GridPane.setHalignment(tickerText, HPos.CENTER);
        
        final Text companyText = new Text();
        companyText.setStyle("-fx-font: bold 28px Helvetica, Helvetica, sans-serif");
        grid.add(companyText, 0, 1);
        GridPane.setHalignment(companyText, HPos.CENTER);
        
        final Text priceText = new Text();
        priceText.setStyle("-fx-font: normal 45px Helvetica, Helvetica, sans-serif");
        grid.add(priceText, 0, 2);
        GridPane.setHalignment(priceText, HPos.CENTER);
        
        
        //Add innerGrid to main grid
        grid.add(innerGrid, 0, 3);
        GridPane.setHalignment(innerGrid, HPos.CENTER);
        
        //CHANGE % day and ytd
        Text dayChangeText = new Text();
        innerGrid.add(dayChangeText, 0, 0);
        GridPane.setHalignment(dayChangeText, HPos.RIGHT);
        final Text dayPercent = new Text();
        innerGrid.add(dayPercent, 0, 1);
        GridPane.setHalignment(dayPercent, HPos.RIGHT);
        
        Text ytdChangeText = new Text();
        innerGrid.add(ytdChangeText, 1, 0);
        GridPane.setHalignment(ytdChangeText, HPos.RIGHT);
        final Text YTDPercent = new Text();
        innerGrid.add(YTDPercent, 1, 1);
        GridPane.setHalignment(YTDPercent, HPos.RIGHT);
        
        //When ticker is entered into searchField
        searchField.setOnAction((ActionEvent) ->
        {
            tickerText.setText(searchField.getText().toUpperCase());
            
            try 
            {
                /*Scrape company name & price passing the searched ticker symbol
                and put in respective text objects*/
                companyText.setText(getCompanyName(searchField.getText()));
                priceText.setText(getPrice(searchField.getText()));
                
                /*Scrape day and ytd change and call setColor() to make green or
                red*/
                dayChangeText.setText(getDayChange(searchField.getText()));
                setColor(dayChangeText);
                ytdChangeText.setText(getYTDchange(searchField.getText()));
                setColor(ytdChangeText);
                
                //Add strings to text to differentiate between day and ytd
                dayPercent.setText("Day");
                YTDPercent.setText("YTD");
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(SearchQuoteGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //Clear searchField and focus curser in
            searchField.setText("");
            searchField.requestFocus(); //force curser to click into tf
        }); //eo lambda


        Scene scene = new Scene(pane, 380, 380);
        stage.setTitle("Enter a stock quote to search");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    
    
    public String getCompanyName(String quote) throws IOException
    {
        QuoteReader q = new QuoteReader(quote);
        return q.companyName();
    }


    public String getPrice(String quote) throws IOException
    {
        QuoteReader q = new QuoteReader(quote);
        
        return "$" + q.currentQuote();
    }
    
    public String getDayChange(String quote) throws IOException
    {
        QuoteReader q = new QuoteReader(quote);
        
        return q.currentDayChange();
    }

    public String getYTDchange(String quote) throws IOException
    {
        QuoteReader q = new QuoteReader(quote);
        
        return q.currentYTD() + "%";
    }

    public void setColor(Text t)
    {
        if(t.getText().startsWith("-"))
        {
            t.setFill(Color.RED);
        }
        else
        {
            t.setFill(Color.GREEN);
        }
        
        t.setStyle("-fx-font: bold 25px Helvetica, Helvetica, sans-serif");
    }
    
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
