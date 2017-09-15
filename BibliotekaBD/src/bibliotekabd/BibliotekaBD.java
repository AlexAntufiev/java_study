/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bibliotekabd;
import java.sql.*;
import java.util.*;
/**
 *
 * @author bvc
 */
public class BibliotekaBD {

    /**
     * @param args the command line arguments
     */
    
    public Connection dbConnect(String username, String password)
    {
        Connection conn = null;
        try{
           
        Locale.setDefault(Locale.ENGLISH);
        String driverName = "oracle.jdbc.driver.OracleDriver";
        Class.forName(driverName);
        String serverName = "Asus";
        String serverPort = "1521";
        String sid = "XE";
        String url = "jdbc:oracle:thin:@"+serverName+":"+serverPort+":"+ sid;
        //String username = "Aventador";
        //String password = "12345";
        conn = DriverManager.getConnection(url, username, password);
        System.out.println("Successfull!");
        } catch(ClassNotFoundException e){
            System.out.println("Could not find the database driver"+e.getMessage());
            conn= null;
        } catch(SQLException e){
            System.out.println("Could not connect to database" + e.getMessage());
            conn= null;
        }
        return conn;
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        /*Connection conn = null;
        try{
           
            Locale.setDefault(Locale.ENGLISH);
        String driverName = "oracle.jdbc.driver.OracleDriver";
        Class.forName(driverName);
        String serverName = "Asus";
        String serverPort = "1521";
        String sid = "XE";
        String url = "jdbc:oracle:thin:@"+serverName+":"+serverPort+":"+ sid;
        String username = "Aventador";
        String password = "12345";
        conn = DriverManager.getConnection(url, username, password);
        System.out.println("Successfull!");
        } catch(ClassNotFoundException e){
            System.out.println("Could not find the database driver"+e.getMessage());
        } catch(SQLException e){
            System.out.println("Could not connect to database" + e.getMessage());
        }
        
        try{
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select * from Journal");
        
        ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>(); 
        
        int i=0;
        int c = rs.getMetaData().getColumnCount();
        String[] headers = new String[c];
        for( int m =0;m<c;m++){
        headers[m]=rs.getMetaData().getColumnName(m+1);
        }
        
         while (rs.next()) {
             array.add(new ArrayList<String>());
             for(int j =0 ; j< c; j++ )
             {
             array.get(i).add(rs.getString(j+1));
             }
             i++;
         }
         Object[][] data = new String[i][c];
         for( int m =0; m<i;m++)
         {
         for(int n=0; n<c;n++)
         {
            // data[m][n]=array.get(m).get(n);
             System.out.print(array.get(m).get(n) + " ");
             //System.out.println(array.get(m).get(n));
         }
         System.out.println();
         }
        
        }
        catch(Exception e){
            
        }*/
        
    }
    
}
