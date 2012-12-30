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
	Button con = new Button("Connetti");
	Button dis = new Button("Disconnetti");
	Button sho = new Button("Esegui");
	Button exi = new Button("Esci");
	Button upd = new Button("Update");
	Panel  but = new Panel();
	Panel  txt = new Panel();
	Panel  mod = new Panel();
	Panel  shw = new Panel();
	Panel query = new Panel();
	BorderLayout br = new BorderLayout();
	GridLayout far = new GridLayout(2,2);
	TextField que = new TextField("Inserisci qua la query",30);
	TextField dat = new TextField("Inserisci il nome del DB da usare",10);
	TextField user = new TextField("Nome utente",10);
	TextField pass = new TextField("Password",10);
	TextArea lab = new TextArea("Qui verrà visualizzato il contenuto del database");
	Database db= new Database("libri","root","cyanogenmod");
	
	
	public dbviewer()
	{
		super("DBViewer");
		this.setLocationRelativeTo(null);
		setSize(600,600);
		setLayout(br);		
		txt.setLayout(far);
		mod.setLayout(far);
		but.add(con);
		but.add(dis);
		txt.add(que);
		but.add(exi);
		mod.add(sho);
		mod.add(upd);
		shw.add(lab);
		query.add(txt);
		query.add(mod);
		add(query,br.CENTER);
		add(but,br.NORTH);
		add(shw,br.SOUTH);
		pack();
		show();
		events();
	}
	
	void events()
	{
		con.addActionListener(new ActionListener()
		{
				public void actionPerformed(ActionEvent e)
					{
						if(!db.connetti())
						{lab.setText("Errore di connessione al database");
						lab.setText(db.getErrore());}
						else {lab.setText("Connesso");}
					}
		});
	
		dis.addActionListener(new ActionListener()
		{
				public void actionPerformed(ActionEvent e)
					{
						if (db.isConnesso()) {db.disconnetti(); lab.setText("Disconnesso");}
						else {lab.setText("Non sei connesso");};
					}
		});
		sho.addActionListener(new ActionListener()
		{
				public void actionPerformed(ActionEvent e)
					{
						ArrayList<String[]> v = db.eseguiQuery(que.getText());
						String[] a = new String[v.size()];
						lab.setText(null);
						for (int i=0;i<v.size();i++)
						{
							 a= v.get(i);
							 for (int l=0;l<a.length;l++)
							 {
								 lab.append(a[l]+"\n");
							 }
							
						}
						
						
					}
												

						
				
		});
		
		
			
						
		exi.addActionListener(new ActionListener()
		{
				public void actionPerformed(ActionEvent e)
					{
						if(escisino()=="Si")
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
				if (db.eseguiAggiornamento(que.getText()))
				
					 {JOptionPane.showMessageDialog(new Frame(),"Aggiornamento riuscito");}
				else
				{
					System.out.print(db.getErrore());
				}
			}
		});
	}
		
public Object escisino()
		{
			JOptionPane pan = new JOptionPane("Sicuro di uscire?");
			Object[] opt = new String[] {"Si","No"};
			pan.setOptions(opt);
			JDialog esci = 	pan.createDialog(new JFrame(), "Esci?");
			esci.show();
			Object obj = pan.getValue(); 
			return obj;
		} 
				
public static void main(String[] a)
{
	new dbviewer();
}
}
