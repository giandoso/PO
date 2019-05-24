package po;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class xpto {

    
    /**
     * Max(z) = x1 + x2
     * Restrições:
     */
    
    // 2*x1 + 2*x2 <= 8
    // 5*x1 + 3*x2 <= 15
     
    public static void main(String[] args) {
        try {
            IloCplex model = new IloCplex();
            /*
                Para a função objetiva:
                Cria expressão linear, adiciona os termos da expressão(x1 e x2, também criados)
                Após isso, adiciona à funçao objetivo ao modelo
            */
            // z = x1 + x2
            IloLinearNumExpr z = model.linearNumExpr();
            IloNumVar x1 = model.numVar(0, 99999);
            IloNumVar x2 = model.numVar(0, 99999);
            z.addTerm(1, x1);
            z.addTerm(1, x2);
            model.addMaximize(z);
            
            
            // 2*x1 + 2*x2 <= 8
            IloLinearNumExpr r1 = model.linearNumExpr();
            r1.addTerm(x1, 2);
            r1.addTerm(x2, 2);            
            model.addLe(r1, 8);
            
            // 5*x1 + 3*x2 <= 15           
            IloLinearNumExpr r2 = model.linearNumExpr();
            r2.addTerm(x1, 5);
            r2.addTerm(x2, 3);            
            model.addLe(r2, 15);
            
            
            
            if(model.solve()){
                System.out.println(model);
                System.out.println(model.getValue(x1));
                System.out.println(model.getValue(x2));
            }
            
            
        }catch(IloException e){
            System.out.println(e);
        }
        
        
        

    }
}
