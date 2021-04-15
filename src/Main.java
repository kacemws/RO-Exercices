import ilog.concert.*;
import ilog.cplex.*;
public class Main {
	public static void main(String[] args) {
		try {
			
			// Creation du modèle
			IloCplex simplexe = new IloCplex ();
			
			// n repr�sente le nombre de machines et de personnes, 6 dans notre exemple
			int n = 6;
			
			// p est la matrice de productivit� de chaque persone i vis � vis d'une machine j
			int[][] p = {
					{13,24,31,19,40,29},
					{18,25,30,15,43,22},
					{20,20,27,25,34,33},
					{23,26,28,18,37,30},
					{28,33,34,17,38,20},
					{19,36,25,27,45,24}
			};
			
			// d�claration des Variables de d�cision de type boolean x[i][j]
			IloNumVar x [][] = new IloNumVar [n][n];
			for (int i=0;i<n;i++){
				for (int j=0; j <n; j++) {
					x[i][j] = simplexe.boolVar();
				}
			}
			
			
			// declaration de la fonction objectif
			IloLinearNumExpr objectif = simplexe.linearNumExpr();
			
			// d�finition des coefficients de la fonction objectif
			for(int i = 0; i <n; i++) {
				for(int j =0; j < n; j++) {
					objectif.addTerm(p[i][j], x[i][j]);
				}
			}
			
			// D�finir le type d'optimisation de la fonction (max)
			simplexe.addMaximize(objectif);
			
			
			//contrainte N�1: pour chaque i, la somme des x[i][j] (o� j = 1..n) est = 1
			for (int i = 0; i < n; i++) {
				IloLinearNumExpr contrainte = simplexe.linearNumExpr();
				for(int j = 0; j < n; j++) {
					contrainte.addTerm(1, x[i][j]);
				}
				simplexe.addEq(1,contrainte);
			}
			
			
			//contrainte N�2: pour chaque j, la somme des x[i][j] (o� i = 1..n) est = 1
			for (int j = 0; j < n; j++) {
				IloLinearNumExpr contrainte = simplexe.linearNumExpr();
				for(int i = 0; i < n; i++) {
					contrainte.addTerm(1, x[i][j]);
				}
				simplexe.addEq(1,contrainte);
			}
			
			simplexe.solve(); // lancer la resolution
			
			
			// Afficher des r�sultat
			System.out.println("Voici la valeur de la fonction objectif "+ simplexe.getObjValue());
			System.out.println(" Voici les valeurs des variables de d�cision: ") ;
			for (int i=0;i<n;i++)
				for(int j =0; j<n; j++)
					System.out.println( "X"+(i+1)+(j+1)+" = "+ simplexe.getValue(x[i][j]));
			
		} catch (IloException e){
			System.out.print("Exception lev�e " + e); 
		}	
	}
}