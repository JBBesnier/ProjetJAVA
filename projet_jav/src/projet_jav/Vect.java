package projet_jav;

import static java.lang.Math.*;

public class Vect {
	/*	Cette classe est une classe g�n�rique pour cr�er des Vecteurs.
	 * Elle a �t� choisi au d�triment de la classe Vector de Java.util 
	 * car on peut facilement la remodeler � notre convenance.
	 * La classe Vector de Java.util nous posait notament probl�me pour 
	 * r�cup�rer des �l�ments dans les Vectors
	 */
	
	// Param�tres :
    double x, y;
    
    public Vect(double x, double y) {
    	// Constructeur
        this.x = x;
        this.y = y;
    }
    
    public double[] toArray() {
    	// Conversion Vect -> Array
    	return new double[] {this.x,this.y};
    }

    public void add(Vect v) {
    	// Addition 
        x += v.x;
        y += v.y;
    }
    
    public void sub(Vect v) {
    	// Soustraction
        x -= v.x;
        y -= v.y;
    }
    
    public void div(double val) {
    	// Division
        x /= val;
        y /= val;
    }
    
    public void mult(double val) {
    	// Multiplication 
        x *= val;
        y *= val;
    }
    
    public double norme() {
    	// Norme Euclidienne
        return sqrt(pow(x, 2) + pow(y, 2));
    }
    
    public double produit_sca(Vect v) {
    	// Produit scalaire canonique
        return x * v.x + y * v.y;
    }
    
    public void normalisation() {
    	// Normalisation du vecteur
        double norme = norme();
        if (norme != 0) {
            x /= norme;
            y /= norme;
        }
    }
    public void limit(double lim) {
    	// Facteur d'�chelle d'un vecteur
        double norme = norme();
        if (norme != 0 && norme > lim) {
            x *= lim / norme;
            y *= lim / norme;
        }
    }
    
    public double heading() {
    	// atan2() est utilis� pour r�cup�rer la composante th�ta dans les coordonn�es polaires
        return atan2(y, x);
    }
    
    public static Vect sub(Vect v, Vect v2) {
    	// Soustraction
        return new Vect(v.x - v2.x, v.y - v2.y);
    }
    
    public static double dist(Vect v, Vect v2) {
    	// Distance Euclidienne 
        return sqrt(pow(v.x - v2.x, 2) + pow(v.y - v2.y, 2));
    }
}