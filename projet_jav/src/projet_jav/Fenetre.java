package projet_jav;

import javax.swing.*;  		// biblioth�que pour la gestion des fen�tres
import java.awt.*; 			// �l�ments pour interfaces graphiques
import java.awt.event.*; 	// �l�ments permettant l'�coute et l'action sur l'interface graphique

/**Classe servant � la cr�ation d'une fen�tre pour r�cup�rer les param�tres 
 * utilisateurs et lancer la simulation.
 * @author Jean-Baptiste
 */

public class Fenetre extends JFrame implements Runnable{
	
	private static final long serialVersionUID = 1L;
	// Utile uniquement lors de s�rialisation : pas besoin ici mais laiss� par acquis de conscience
	
	// Param�tres :
	protected Thread affichage = new Thread(this);
		// Objets
	protected Poisson[] poissons;
	protected Zones_dangereuses[] zones;
		// Param�tres
	protected int L; // Longueur fen�tre
	protected int l; // Largeur fen�tre
	protected int nb_objets;
	protected int nb_predateurs;
	protected int nb_poissons;
		// Champs 
	protected JTextField nb_pois = new JTextField("100");
	protected JTextField nb_pred = new JTextField("5");
	protected JTextField nb_obj = new JTextField("10");
	protected JTextField taille_min = new JTextField("0");
	protected JTextField taille_max = new JTextField("60");
	protected JTextField vitesse_pred = new JTextField("1.25");
	protected JTextField vitesse_rep = new JTextField("1000");
	
	public Fenetre(int L, int l) {
		// Constructeur de la fen�tre de simulation
		
			// Cr�ation de la fen�tre
		this.setTitle("---- Simulation d'un banc de poisson ---- Camille B - Jean-Baptiste B - Corentin P ----");
		this.L = L;
		this.l = l;
		this.setSize(L+315, l+15); // +285 et +15 pour g�rer le bandeau et les boutons
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
			// Cr�ation du panneau de la fen�tre
		JPanel panneau = creerPanel();
		panneau.setPreferredSize(new Dimension(300,l));
		this.getContentPane().add(panneau , BorderLayout.EAST);
		
		this.setVisible(true);
	}
	
	public Fenetre(int L , int l ,String texte) {
		// Constructeur de la fen�tre de message d'erreur
		
			// Cr�ation de la fen�tre
		this.setTitle("Une erreur est survenue :");
		this.setSize(L,l);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
			// Cr�ation du panneau de la fen�tre
		JPanel panneau = new JPanel();
		this.setContentPane(panneau);
		
			// Cr�ation et gestion du bouton
		JLabel erreur = new JLabel(texte);
		panneau.add(erreur);
		JButton bouton = new JButton("OK");
			// D�but du bloc r�curant pour r�cuperer le click d'un bouton 
		ActionListener fermer = new ActionListener() {
			@Override 
			public void actionPerformed (ActionEvent click) {
				fermer();
			}
		};
		bouton.addActionListener(fermer);
		panneau.add(bouton);
		
		this.setVisible(true);
	}

	public JPanel creerPanel() {
		// Constructeur du menu de la fen�tre
			//Cr�ation et �coute des boutons
		ActionListener commencer = new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent action) {
				commencer();
			}
		};
		JPanel panneau = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton bouton = new JButton("Commencer");
		bouton.setPreferredSize(new Dimension(120, 20));
		bouton.addActionListener(commencer);
		panneau.add(bouton);
		
		ActionListener fermer = new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent action) {
				fermer();
			}
		};
		JButton bouton2 = new JButton("Fermer");
		bouton2.setPreferredSize(new Dimension(120, 20));
		bouton2.addActionListener(fermer);
		panneau.add(bouton2);
		
			//Champs d'entr�es
		JLabel texte = new JLabel ("Nombre de poissons = ");
		panneau.add(texte);
		nb_pois.setPreferredSize(new Dimension (50 , 20));
		panneau.add(nb_pois);
		
		JLabel texte2 = new JLabel ("Nombre d'it�rations avant reproduction = ");
		panneau.add(texte2);
		vitesse_rep.setPreferredSize(new Dimension (50 , 20));
		panneau.add(vitesse_rep);
		
		JLabel texte3 = new JLabel ("Nombre de pr�dateurs = ");
		panneau.add(texte3);
		nb_pred.setPreferredSize(new Dimension (50 , 20));
		panneau.add(nb_pred);
		
		JLabel texte4 = new JLabel ("Vitesse des pr�dateurs = ");
		panneau.add(texte4);
		vitesse_pred.setPreferredSize(new Dimension (50 , 20));
		panneau.add(vitesse_pred);
		
		JLabel texte5 = new JLabel ("Nombre d'objets physiques = ");
		panneau.add(texte5);
		nb_obj.setPreferredSize(new Dimension (50 , 20));
		panneau.add(nb_obj);
		
		JLabel texte6 = new JLabel ("Taille minimale des obstacles = ");
		panneau.add(texte6);
		taille_min.setPreferredSize(new Dimension (50 , 20));
		panneau.add(taille_min);
		
		JLabel texte7 = new JLabel ("Taille maximale des obstacles = ");
		panneau.add(texte7);
		taille_max.setPreferredSize(new Dimension (50 , 20));
		panneau.add(taille_max);
		
		return panneau;
	}
	
	public void commencer() {
		// On s'assure de ne pas avoir d'erreurs avant de lancer la simulation
		if (this.affichage.isAlive()) {
			this.affichage.interrupt();
			this.affichage = new Thread(this);
		}
		try {
			get_elements();
			} catch (ExceptionRayonMinimum e) {
				new Fenetre (330 , 100 ,"On doit avoir : rayon minimum < rayon maximum");
				return;
			} catch (NumberFormatException e) {
				new Fenetre (380 , 100 ,"Vous n'avez pas rentr� un nombre entier dans un champ.");
				return;
			} catch (NegativeArraySizeException e) {
				new Fenetre (350 , 100 ,"Vous avez rentr� un nombre n�gatif dans un champ.");
				return;
		}
		
		this.affichage.start(); // si tout va bien : on lance
	}
	
	public void fermer() {
		// ferme la fen�tre de message d'erreur
		if (this.affichage.isAlive()) {
			this.affichage.interrupt();
		}
		this.dispose();
	}
	
	public void get_elements () throws ExceptionRayonMinimum {
		// On r�cup�re tout les �l�ments du menu de la fen�tre pour la simulation
		// et g�re une exception cr��e
		this.nb_poissons = Integer.parseInt(this.nb_pois.getText());
		this.nb_objets = Integer.parseInt(this.nb_obj.getText());
		this.nb_predateurs = Integer.parseInt(this.nb_pred.getText());
		Objet_physique.R_max = Integer.parseInt(this.taille_max.getText());
		Objet_physique.R_min = Integer.parseInt(this.taille_min.getText());
		Predateurs.vitesse_predateurs = Double.parseDouble(this.vitesse_pred.getText());
		Poisson.Vitesse_repro = Integer.parseInt(this.vitesse_rep.getText());
		if (Objet_physique.R_min >= Objet_physique.R_max) {
			throw new ExceptionRayonMinimum();
		} else {
		this.zones = Zones_dangereuses.creation_zones(this.nb_objets, this.nb_predateurs, this.L, this.l);
		this.poissons = Poisson.creation_poisson(this.nb_poissons, this.zones, this.L, this.l);
		}
	}

	public void run() {
		// Coeur du programme permmettant tout les calculs
		Canvas canvas = new Canvas(this.poissons, this.zones ,this.L,this.l);
		this.setContentPane(canvas);
		this.setLayout(new BorderLayout());
		JPanel panneau = creerPanel();
		panneau.setPreferredSize(new Dimension(300,l));
		this.getContentPane().add(panneau , BorderLayout.EAST);
		this.validate();
		Thread.currentThread();
		while(!Thread.interrupted()) {
			canvas.repaint();
		}	
	}
}