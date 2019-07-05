/*
 * João Pedro Giandoso - 2015.1.08.029
 * jpgiandoso@gmail.com  * 
 */
package hospital;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.cplex.IloCplex;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, IloException {
        //leitura
        Reader r = new Reader();
        
        int grupos = r.numGrupos;
        int pessoas = r.numPessoas;

        //Modelo
        IloCplex model = new IloCplex();

        //variável de decisão
        IloIntVar x[][] = new IloIntVar[grupos][pessoas];
        for (int i = 0; i < grupos; i++) {
            for (int j = 0; j < pessoas; j++) {
                x[i][j] = model.boolVar();
            }
        }

        //função objetivo: maximizar a qualidade das relações entre as 
        //pessoas em seus grupos
        IloLinearNumExpr z = model.linearNumExpr();
        for (int i = 0; i < grupos; i++) {
            for (int j = 0; j < pessoas; j++) {
                z.addTerm(r.values[i][j], x[i][j]);
            }
        }
        model.addMaximize(z);

        //Restrições
        //Número de pessoas em cada grupo:
        for (int i = 0; i < grupos; i++) {
            IloLinearNumExpr pessoasPorGrupo = model.linearNumExpr();
            for (int j = 0; j < pessoas; j++) {
                pessoasPorGrupo.addTerm(1, x[i][j]);
            }
            model.addEq(pessoasPorGrupo, r.array_tamanho_grupo[i]);
        }

        //Cada pessoa deve estar apenas em um grupo (somatório de cada coluna de X = 1)
        for (int j = 0; j < pessoas; j++) {
            IloLinearNumExpr restricaoPessoa = model.linearNumExpr();
            for (int i = 0; i < grupos; i++) {
                restricaoPessoa.addTerm(1, x[i][j]);
            }
            model.addEq(restricaoPessoa, 1);
        }

        //Resultados
        if (model.solve()) {
            System.out.println("\n\n");
            for (int i = 0; i < grupos; i++) {
                System.out.print("Grupo " + (i + 1) + ":  ");
                for (int j = 0; j < pessoas; j++) {
                    if (model.getValue(x[i][j]) == 1) {
                        System.out.print((j + 1) + " ");
                    }
                }
                System.out.println("");
            }
            System.out.println("Nível de satisfação: " + model.getObjValue());
        } else {
            System.out.println("Erro no modelo.solve()");
        }
    }
}
