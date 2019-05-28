/*
 * João Pedro Giandoso - 2015.1.08.029
 * jpgiandoso@gmail.com  * 
 */
package container;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import java.io.IOException;

/**
 *
 * @author 2015.1.08.029
 */
public class Container {

    public static void main(String[] args) throws IOException, IloException {
        try {
            Reader r = new Reader("src/container/products_01.csv");
            
            
            // dados da instancia
            int n = 200;
            int k = 15;
            int cc = 100;
            int cv = 100;
            int b = 4;
            Double[] volume = r.getVolume();
            Double[] weight = r.getWeight();
            Double[] value = r.getValue();
            
            
            IloCplex model = new IloCplex();

            //variaveis
            // Ao trocar IloNumvar Por IloInt var trocamos o dominio do problema( ponto flutuante para inteiro )
            IloIntVar x[][] = new IloIntVar[n][k]; //n itens por k containers

            //criando todas variaveis de decisao
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < k; j++) {
                    x[i][j] = model.intVar(0, 4);
                }
            }

            //criando a funcao objetivo
            IloLinearNumExpr funcObj = model.linearNumExpr();
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < k; j++) {
                    funcObj.addTerm(value[i], x[i][j]);
                }
            }
            model.addMaximize(funcObj);

            /*
            adicionando todas as restricao
             */
            //restricoes de carga
            for (int j = 0; j < k; j++) { //Somatorio w[i] * x[i][j] <= cc para todo j = 1 ... k
                IloLinearNumExpr currentLeft = model.linearNumExpr();
                //.............
                for (int i = 0; i < n; i++) {
                    currentLeft.addTerm(weight[i], x[i][j]);
                }
                model.addLe(currentLeft, cc);
            }

            //restricoes de volume
            for (int j = 0; j < k; j++) { //Somatorio v[i]*x[i][j] <= cv para todo j = 1 ... k
                IloLinearNumExpr currentLeft = model.linearNumExpr();
                for (int i = 0; i < n; i++) {
                    currentLeft.addTerm(volume[i], x[i][j]);
                }
                model.addLe(currentLeft, cv);

            }

            //restricoes de quantidade
            for (int i = 0; i < n; i++) { //Somatorio x[i][j] <= b para todo i = 1 ... n
                IloLinearNumExpr currentLeft = model.linearNumExpr();
                for (int j = 0; j < k; j++) {
                    currentLeft.addTerm(1, x[i][j]);
                }
                model.addLe(currentLeft, b);

            }
            //cada variavel eh menor ou igual b
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < k; j++) {
                    model.addLe(x[i][j], b);
                }
            }
            //cada variavel eh maior ou igual b
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < k; j++) {
                    model.addGe(x[i][j], 0);
                }
            }
            if (model.solve()) {

                //imprimir quanto de cada item foi levado
                for (int j = 0; j < k; j++) {
                    System.out.println("Container: \t" + j);
                    for (int i = 0; i < n; i++) {
                        double v = model.getValue(x[i][j]);
                        if (v > 0.000000001) {
                            System.out.println("\tItem " + i + ":\t" + v);
                        }
                    }
                    System.out.println("");
                }
                //imprimir Funcao Objetivo
                System.out.println("Z: " + model.getObjValue()); //valor da funcao Z
                //imprimir status
                System.out.println("Status: " + model.getStatus());
            } else {
                System.err.println("DEU BOSTA");
            }
        } catch (IloException e) {
            System.out.println(e);
        }

    }
}
