/*
 * João Pedro Giandoso - 2015.1.08.029
 * jpgiandoso@gmail.com  * 
 */
package sudoku;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import java.io.FileNotFoundException;

/**
 *
 * @author 2015.1.08.029
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException, IloException {
        Reader r = new Reader("src/sudoku/data.input");

        //dados do problema
        int tam_matriz = 9;
        int tam_quadrante = (int) Math.sqrt(tam_matriz);

        IloCplex model = new IloCplex();

        // variavel de decisão
        IloIntVar x[][][] = new IloIntVar[tam_matriz][tam_matriz][tam_matriz];
        for (int i = 0; i < tam_matriz; i++) {
            for (int j = 0; j < tam_matriz; j++) {
                for (int k = 0; k < tam_matriz; k++) {
                    x[i][j][k] = model.intVar(0, 1);
                }
            }
        }

        // função objetivo
        IloLinearNumExpr func_obj = model.linearNumExpr();
        for (int i = 0; i < tam_matriz; i++) {
            for (int j = 0; j < tam_matriz; j++) {
                for (int k = 0; k < tam_matriz; k++) {
                    func_obj.addTerm(x[i][j][k], 1.0);
                }
            }
        }
        model.addMaximize(func_obj);

        // restrição de linha
        for (int j = 0; j < tam_matriz; j++) {
            for (int k = 0; k < tam_matriz; k++) {
                IloLinearNumExpr curr_left = model.linearNumExpr();
                for (int i = 0; i < tam_matriz; i++) {
                    curr_left.addTerm(x[i][j][k], 1.0);
                }
                model.addEq(func_obj, 1.0);
            }
        }
        // restrição de coluna
        for (int i = 0; i < tam_matriz; i++) {
            for (int k = 0; k < tam_matriz; k++) {
                IloLinearNumExpr curr_left = model.linearNumExpr();
                for (int j = 0; j < tam_matriz; j++) {
                    curr_left.addTerm(x[i][j][k], 1.0);
                }
                model.addEq(func_obj, 1.0);
            }
        }
        
        // restrição de quadrante
        
        if (model.solve()) {
            System.out.println("Z: " + model.getObjValue()); //valor da funcao Z

        } else {
            System.err.println("DEU BOSTA");
        }

    }
}
