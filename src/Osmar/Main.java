package Osmar;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class Main {     
    public static void main(String[] args) {
        try {
            IloCplex model = new IloCplex();
            
            // FO
            IloLinearNumExpr z = model.linearNumExpr();
            IloNumVar x = model.numVar(0, 999);
            IloNumVar y = model.numVar(0, 999);
            z.addTerm(300, x);
            z.addTerm(280, y);
            model.addMaximize(z);
            
            
            // Custo :=  70x + 50y <= 350
            IloLinearNumExpr custo = model.linearNumExpr();
            custo.addTerm(70, x);
            custo.addTerm(50, y);            
            model.addLe(custo, 350);
            
            // Carga := 50x + 80y <= 400
            IloLinearNumExpr carga = model.linearNumExpr();
            carga.addTerm(x, 50);
            carga.addTerm(y, 80);            
            model.addLe(carga, 400);
            
            
            
            if(model.solve()){
                System.out.println(model.getCplexStatus());
                System.out.println("Soja: " + model.getValue(x));
                System.out.println("Milho: " + model.getValue(y));
            }
            
        }catch(IloException e){
            System.out.println(e);
        }
        
        
        

    }
}
