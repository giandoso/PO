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
        for (int[] p : r.l) {
            model.addEq(x[p[0]][p[1]][p[2]], 1);
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

        // restriçao deprofundidade
        for (int i = 0; i < tam_matriz; i++) {
            for (int j = 0; j < tam_matriz; j++) {
                IloLinearNumExpr curr_left = model.linearNumExpr();
                for (int k = 0; k < tam_matriz; k++) {
                    curr_left.addTerm(x[i][j][k], 1);
                }
                model.addEq(curr_left, 1);
            }
        }

        // restrição de linha
        for (int k = 0; k < tam_matriz; k++) {
            for (int i = 0; i < tam_matriz; i++) {
                IloLinearNumExpr curr_left = model.linearNumExpr();
                for (int j = 0; j < tam_matriz; j++) {
                    curr_left.addTerm(x[i][j][k], 1.0);
                }
                model.addEq(curr_left, 1.0);
            }
        }
        // restrição de coluna
        for (int k = 0; k < tam_matriz; k++) {
            for (int j = 0; j < tam_matriz; j++) {
                IloLinearNumExpr curr_left = model.linearNumExpr();
                for (int i = 0; i < tam_matriz; i++) {
                    curr_left.addTerm(x[i][j][k], 1.0);
                }
                model.addEq(curr_left, 1.0);
            }
        }

        // restrição de quadrante
        for (int k = 0; k < tam_matriz; k++) {

            for (int qAux1 = tam_quadrante; qAux1 <= tam_matriz; qAux1 += tam_quadrante) {
                for (int qAux2 = tam_quadrante; qAux2 <= tam_matriz; qAux2 += tam_quadrante) {
                    IloLinearNumExpr current = model.linearNumExpr();

                    for (int i = 0; i < tam_quadrante; i++) {
                        for (int j = 0; j < tam_quadrante; j++) {
                            int pos_i = (((qAux1 - tam_quadrante) + i));
                            int pos_j = ((qAux2 - tam_quadrante) + j);
                            current.addTerm(1, x[pos_i][pos_j][k]);
                        }
                    }
                    model.addEq(current, 1);//somatorio de cada quadrante em cada profundidade tem que ser igual a 1
                }
            }
        }

        if (model.solve()) {
            for (int i = 0; i < tam_matriz; i++) {
                for (int j = 0; j < tam_matriz; j++) {
                    for (int k = 0; k < tam_matriz; k++) {
                        if (model.getValue(x[i][j][k]) == 1) {
                            System.out.print(k + 1 + "|");
                            break;
                        }
                    }
                }
                System.out.println("");
            }
            System.out.println("Z: " + model.getObjValue()); //valor da funcao Z
        } else {
            System.err.println("DEU BOSTA");
        }

    }
}
