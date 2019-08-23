/*
 * João Pedro Giandoso - 2015.1.08.029
 * jpgiandoso@gmail.com  * 
 */
package MontaHorarios;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import java.io.IOException;

/**
 *
 * @author giandoso
 */
public class Main {

    public static void main(String[] args) throws IOException, IloException {
        Leitura l = new Leitura();
        Informacoes infos = l.leituraSudoku("src/MontaHorarios/nilda36.txt");

        int turmas = infos.qtdTurmas;
        int horarios = infos.qtdHorarios;
        int dias = infos.qtdDias;
        int profs = infos.qtdProfessores;
        boolean[][][] inviabilidades = infos.matrizInviabilidade;
        int matrizAulas[][] = infos.matrizAulas;

        IloCplex model = new IloCplex(); //crio o modelo
        IloNumVar[][][][] x = new IloNumVar[dias][horarios][turmas][profs];

        // X
        for (int d = 0; d < dias; d++) {
            for (int h = 0; h < horarios; h++) {
                for (int t = 0; t < turmas; t++) {
                    for (int p = 0; p < profs; p++) {
                        x[d][h][t][p] = model.intVar(0, 1);
                    }
                }
            }
        }

        // FO
        IloLinearNumExpr z = model.linearNumExpr();//criando a expressao z de maximizacao ou minimização
        for (int d = 0; d < dias; d++) {
            for (int h = 0; h < horarios; h++) {
                for (int t = 0; t < turmas; t++) {
                    for (int p = 0; p < profs; p++) {
                        z.addTerm(1, x[d][h][t][p]);
                    }
                }
            }
        }
        model.addMaximize(z);

        // inviabilidades
        for (int h = 0; h < horarios; h++) {
            for (int d = 0; d < dias; d++) {
                for (int p = 0; p < profs; p++) {
                    if (inviabilidades[h][d][p]);
                    for (int t = 0; t < turmas; t++) {
                        model.addEq(1, x[d][h][t][p]);
                    }
                }
            }
        }

        // Quantidade de aulas de prof em cada turma
        for (int t = 0; t < turmas; t++) {
            for (int p = 0; p < profs; p++) {
                int qtdAulas = matrizAulas[p][t];
                IloLinearNumExpr curr = model.linearNumExpr();
                for (int h = 0; h < horarios; h++) {
                    for (int d = 0; d < dias; d++) {
                        curr.addTerm(1, x[d][h][t][p]);
                    }
                }
                model.addEq(curr, qtdAulas);
            }
        }

        //A soma de cada professor em cada horarios que ele esta deve ser menor ou igual a 1 para proibir que possivelmente
        //ele esteja em duas turmas no mesmo dia e no mesmo horario
        for (int d = 0; d < dias; d++) {
            for (int h = 0; h < horarios; h++) {
                for (int p = 0; p < profs; p++) {
                    IloLinearNumExpr curr = model.linearNumExpr();
                    for (int t = 0; t < turmas; t++) {
                        curr.addTerm(1, x[d][h][t][p]);
                    }
                    model.addLe(1, curr);
                }
            }
        }

        // Um professor em cada horario em cada turma em cada dia
        for (int d = 0; d < dias; d++) {
            for (int h = 0; h < horarios; h++) {
                for (int t = 0; t < turmas; t++) {
                    IloLinearNumExpr curr = model.linearNumExpr();
                    for (int p = 0; p < profs; p++) {
                        curr.addTerm(1, x[d][h][t][p]);
                    }
                    model.addEq(1, curr);
                }
            }
        }

        if (model.solve()) {
            System.out.println("Horarios Montados\n");
            System.out.println("Valor funcao objetivo: " + model.getObjValue());
            System.out.println("STATUS: " + model.getStatus());

                        for (int t = 0; t < turmas; t++) {
                System.out.println("Horarios turma : " + t);
                System.out.println("SEG  TER  QUA  QUI  SEX");
                for (int h = 0; h < horarios; h++) {
                    for (int d = 0; d < dias; d++) {
                        for (int p = 0; p < profs; p++) {
                            if (model.getValue(x[d][h][t][p]) == 1) {
                                System.out.print(p + " | ");
                                break;
                            }
                        }
                    }
                    System.out.println("");

                }
                System.out.print("\n\n\n\n");
            }
        }
    }
}
