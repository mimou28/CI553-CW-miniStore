package clients.customer;

import catalogue.Basket;
import catalogue.Product;
import debug.DEBUG;
import middle.MiddleFactory;
import middle.OrderProcessing;
import middle.StockException;
import middle.StockReader;

import javax.swing.*;
import java.util.Observable;

/**
 * Implements the Model of the customer client
 */
public class CustomerModel extends Observable
{
  private Product     theProduct = null;          // Current product
  private Basket      theBasket  = null;          // Bought items

  private String      pn = "";                    // Product being processed

  private StockReader     theStock     = null;
  private OrderProcessing theOrder     = null;
  private ImageIcon       thePic       = null;

  /*
   * Construct the model of the Customer
   * @param mf The factory to create the connection objects
   */
  public CustomerModel(MiddleFactory mf)
  {
    try                                          // 
    {  
      theStock = mf.makeStockReader();           // Database access
    } catch ( Exception e )
    {
      DEBUG.error("CustomerModel.constructor\n" +
                  "Database not created?\n%s\n", e.getMessage() );
    }
    theBasket = makeBasket();                    // Initial Basket
  }
  
  /**
   * return the Basket of products
   * @return the basket of products
   */
  public Basket getBasket()
  {
    return theBasket;
  }

  /**
   * Check if the product is in Stock
   * @param productNum The product number
   */
  public void doCheck(String productNum) {
	    theBasket.clear();                          // Clear s. list
	    String theAction = "";
	    pn  = productNum.trim();                    // Product no.
	    int    amount  = 1;                         // & quantity
	    try {
	        if (theStock.exists(pn)) { // Check if the product exists
	            Product pr = theStock.getDetails(pn); // Get product details
	            if (pr.getQuantity() >= amount) { // Check if it's in stock
	                theAction = String.format(
	                    "Only %d %s left in the stock", // Corrected format
	                    pr.getQuantity(),  // Quantity left
	                    pr.getDescription() // Product description
	                );
	                pr.setQuantity(amount); // Require 1
	                theBasket.add(pr); // Add to basket
	                thePic = theStock.getImage(pn); // Get product image
	            } else {
	                theAction = pr.getDescription() + " not in stock";
	            }
	        } else {
	            theAction = "Unknown product number " + pn;
	        }
	    } catch (StockException e) {
	        DEBUG.error("CustomerModel.doCheck()\n%s", e.getMessage());
	    }
	    setChanged();
	    notifyObservers(theAction); // Notify the view
	}


  /**
   * Clear the products from the basket
   */
  public void doClear()
  {
    String theAction = "";
    theBasket.clear();                        // Clear s. list
    theAction = "Enter Product Number";       // Set display
    thePic = null;                            // No picture
    setChanged(); notifyObservers(theAction);
  }
  
  /**
   * Return a picture of the product
   * @return An instance of an ImageIcon
   */ 
  public ImageIcon getPicture()
  {
    return thePic;
  }
  
  /**
   * ask for update of view callled at start
   */
  private void askForUpdate()
  {
    setChanged(); notifyObservers("START only"); // Notify
  }

  /**
   * Make a new Basket
   * @return an instance of a new Basket
   */
  protected Basket makeBasket()
  {
    return new Basket();
  }
}

