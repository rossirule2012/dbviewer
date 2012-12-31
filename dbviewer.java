import java.awt.*;
import javax.swing.*;
import javax.swing.JOptionPane.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.ArrayList;
import Database.*;
public class dbviewer extends JFrame
{
	String user;
	String pass;
	String name;
	String stmnt="";
	
	JButton con = new JButton("Connetti");
	JButton dis = new JButton("Disconnetti");
	JButton sho = new JButton("Esegui");
	JButton exi = new JButton("Esci");
	JButton upd = new JButton("Update");
	JPanel  but = new JPanel();
	JPanel  txt = new JPanel();
	JPanel  mod = new JPanel();
	JPanel  shw = new JPanel();
	JPanel query = new JPanel();
	BorderLayout br = new BorderLayout();
	GridLayout far = new GridLayout(2,2);
	FlowLayout sta = new FlowLayout();
	JLabel tus = new JLabel("Disconnesso");
	JTextField que = new JTextField("Inserisci qua la query",30);
	JTextField dat = new JTextField("Inserisci il nome del DB da usare",10);
	JTextArea lab = new JTextArea();
	Database db= new Database(name,user,pass);
		
	
	public dbviewer() //definisco l'interfaccia
	{
		super("DBViewer");
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		Dimension dim = getToolkit().getScreenSize();
		setLocation(dim.width/2 - this.getWidth()/2,dim.height/2-this.getHeight()/2);
		setLayout(br);		
		txt.setLayout(far);
		mod.setLayout(far);
		shw.setLayout(sta);
		query.setLayout(far);
		lab.setEditable(false);
		lab.setSize(900,900);
		tus.setSize(20,20);
		but.add(con);
		but.add(dis);
		txt.add(que);
		but.add(exi);
		mod.add(sho);
		mod.add(upd);
		mod.add(tus);
		shw.add(lab);
		query.add(txt);
		query.add(mod);
		add(query,br.SOUTH);
		add(but,br.NORTH);
		add(shw,br.CENTER);
		pack();
		show();
		events();
		Actionlisteners();
		
	}
	
	void events() //actionlisteners per i pulsanti
	{
		con.addActionListener(new ActionListener() //pulsante di connessione, il metodo db.connetti restituisce true se
		{                                         //la connessione è avvenuta con successo
				public void actionPerformed(ActionEvent e)
					{
						Process p;
						try{p = Runtime.getRuntime().exec("start mysql");}
						catch(Exception err){}
						access();
						if(!db.connetti(user,name,pass))
						{lab.setText("Errore di connessione al database");
						lab.setText(db.getErrore());}
						else {tus.setText("Connesso");}
					}
		});
		dis.addActionListener(new ActionListener()
		{
				public void actionPerformed(ActionEvent e)
					{
						if (db.isConnesso()) {db.disconnetti(); tus.setText("Disconnesso");} //disconnette l'utente dal database se connesso
					}
		});
		sho.addActionListener(new ActionListener()
		{
				public void actionPerformed(ActionEvent e) //eseguo una query in input dall'utente nella casella di testo "que"
					{
						ArrayList<String[]> v = db.eseguiQuery(que.getText()); //ho modificato la classe Database per restituire un 
						String[] a = new String[v.size()];                     //ArrayList piuttosto che un vector
						lab.setText(null);
						for (int i=0;i<v.size();i++)                           //prendo in esame ognuno degli array di stringhe contenuti
						{													   //nell'arraylist
							 a= v.get(i);
							 for (int l=0;l<a.length;l++)
							 {
								 lab.append(a[l]+"\n");                     //prendo in esame le singole stringe e le scrivo nella textarea
							 }
						}				
					}										
		});				
		exi.addActionListener(new ActionListener()
		{
				public void actionPerformed(ActionEvent e)
					{
						if(escisino()=="Si")         //esco richiamando il metodo escisino() definito sotto
						{
							if(db.isConnesso()){db.disconnetti();}						
						    System.exit(0);
					    }
					  }				    		    
					     
		});
		upd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (db.eseguiAggiornamento(que.getText()))      //query di update, restituisce true se non ci sono errori
				
					 {JOptionPane.showMessageDialog(new Frame(),"Aggiornamento riuscito");}  //creo una dialog
				else
				{
					System.out.print(db.getErrore());
				}
			}
		});
	}
		
public Object escisino()  //metodo che definisce una dialog si/no restuisce un Oggetto[] stringa {si , no} a seconda della scelta
		{
			JOptionPane pan = new JOptionPane("Sicuro di uscire?");
			Object[] opt = new String[] {"Si","No"};  //oggetto stringa
			pan.setOptions(opt);
			JDialog esci = 	pan.createDialog(new JFrame(), "Esci?");  //dialog
			esci.show();
			Object obj = pan.getValue(); 
			return obj;
		} 

void access() //creo la dialog per l'accesso (Commenta questo metodo e inserisci i dati nell'oggetto Database, per un accesso rapido ma poco sicuro
{
	JFrame access = new JFrame("Dati d'accesso");
	JTextField usr = new JTextField(10);
	JPasswordField pss = new JPasswordField(10);
	JTextField nme = new JTextField(10);
	JPanel acc = new JPanel();
	acc.add(new JLabel("Nome db"));
	acc.add(nme);
	acc.add(new JLabel("User e Pass"));
	acc.add(usr);
	acc.add(pss);
	int result = JOptionPane.showConfirmDialog(null,acc,"Inserisci dati di accesso",JOptionPane.OK_CANCEL_OPTION);
	if (result == JOptionPane.OK_OPTION)
	{
		user=usr.getText();
		pass= String.valueOf(pss.getPassword());
		name=nme.getText();
	}
}
	
void Actionlisteners() //itemlisteners per la finestra
{
	
			
	this.addWindowListener( new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent we) {
			System.exit(0);
		}
	});
}

public static void main(String[] a)
{
	new dbviewer();
}
}
