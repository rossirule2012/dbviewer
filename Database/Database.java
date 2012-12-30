package Database;
import java.sql.*;
import java.util.Vector;

public class Database {
   private String nomeDB;       
   private String nomeUtente;   

   private String pwdUtente;    
   private String errore;       
   private Connection db;       
   private boolean connesso;    

   public Database(String nomeDB) { this(nomeDB, "", ""); }

   public Database(String nomeDB, String nomeUtente, String pwdUtente) {
      this.nomeDB = nomeDB;
      this.nomeUtente = nomeUtente;
      this.pwdUtente = pwdUtente;
      connesso = false;
      errore = "";
   }

   public boolean connetti() {
      connesso = false;
      try {

         
         Class.forName("com.mysql.jdbc.Driver");

         
         if (!nomeDB.equals("")) {

           
            if (nomeUtente.equals("")) {

               
               db = DriverManager.getConnection("jdbc:mysql://localhost/" +nomeDB);
            } else {

                if (pwdUtente.equals("")) {

                 
                  db = DriverManager.getConnection("jdbc:mysql://localhost/" +nomeDB + "?user=" + nomeUtente);
               } else {

                  
                  db = DriverManager.getConnection("jdbc:mysql://localhost/" + nomeDB + "?user=" + nomeUtente + "&password=" + pwdUtente);
               }
            }

            
            connesso = true;
         } else {
            System.out.println("Manca il nome del database!!");
            System.out.println("Scrivere il nome del database da utilizzare all'interno del file \"config.xml\"");
            System.exit(0);
         }
      } catch (Exception e) { errore = e.getMessage(); e.printStackTrace(); }
      return connesso;
   }

   
   public Vector eseguiQuery(String query) {
      Vector v = null;
      String [] record;
      int colonne = 0;
      try {
         Statement stmt = db.createStatement();     
         ResultSet rs = stmt.executeQuery(query);  
         v = new Vector();
         ResultSetMetaData rsmd = rs.getMetaData();
         colonne = rsmd.getColumnCount();

         while(rs.next()) { 
            record = new String[colonne];
            for (int i=0; i<colonne; i++) record[i] = rs.getString(i+1);
            v.add( (String[]) record.clone() );
         }
         rs.close();     // Chiudo il ResultSet
         stmt.close();   // Chiudo lo Statement
      } catch (Exception e) { e.printStackTrace(); errore = e.getMessage(); }

      return v;
   }

    public boolean eseguiAggiornamento(String query) {
      int numero = 0;
      boolean risultato = false;
      try {
         Statement stmt = db.createStatement();
         numero = stmt.executeUpdate(query);
         risultato = true;
         stmt.close();
      } catch (Exception e) {
         e.printStackTrace();
         errore = e.getMessage();
         risultato = false;
      }
      return risultato;
   }

     public void disconnetti() {
      try {
         db.close();
         connesso = false;
      } catch (Exception e) { e.printStackTrace(); }
   }

   public boolean isConnesso() { return connesso; }   
   public String getErrore() { return errore; }       
}
