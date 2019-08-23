/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MontaHorarios;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import java.io.IOException;

/**
 *
 * @author glauc
 */
public class MontaHorarios {

    public static void main(String[] args) throws IOException, IloException {
        Leitura l = new Leitura();
        Informacoes infos = l.leituraSudoku("src/MontaHorarios/nilda36.txt");

        int qtdTurmas = infos.qtdTurmas;
        int qtdHorarios = infos.qtdHorarios;
        int qtdDias = infos.qtdDias;
        int qtdProf = infos.qtdProfessores;
        boolean[][][] inviabilidades = infos.matrizInviabilidade;
        int matrizAulas[][] = infos.matrizAulas;

        IloCplex model = new IloCplex(); //crio o modelo
        IloLinearNumExpr z = model.linearNumExpr();//criando a expressao z de maximizacao ou minimização
        IloNumVar[][][][] x = new IloNumVar[qtdDias][qtdHorarios][qtdTurmas][qtdProf];

        //criando as matrizes 3D para cada turma
        for (int t = 0; t < qtdTurmas; t++) {
            for (int h = 0; h < qtdHorarios; h++) {
                for (int d = 0; d < qtdDias; d++) {
                    for (int p = 0; p < qtdProf; p++) {
                        x[d][h][t][p] = model.intVar(0, 1);
                        z.addTerm(1, x[d][h][t][p]);
                    }
                }
            }
        }

        model.addMaximize(z);

        //Restrição das inviabilidades
        for (int h = 0; h < qtdHorarios; h++) {
            for (int d = 0; d < qtdDias; d++) {
                for (int p = 0; p < qtdProf; p++) {
                    if (inviabilidades[h][d][p]) {
                        for (int t = 0; t < qtdTurmas; t++) {
                            model.addEq(x[d][h][t][p], 0);
                        }
                    }
                }
            }
        }

        //Restrição da quantidade de aulas para cada professor em cada turma
        for (int p = 0; p < qtdProf; p++) {
            for (int t = 0; t < qtdTurmas; t++) {
                int qtdAulas = matrizAulas[p][t];
                IloLinearNumExpr somaAulas = model.linearNumExpr();
                for (int h = 0; h < qtdHorarios; h++) {
                    for (int d = 0; d < qtdDias; d++) {
                        somaAulas.addTerm(1, x[d][h][t][p]);
                    }
                }
                model.addEq(somaAulas, qtdAulas);
            }
        }

        //Restrição que deve haver um professor em cada horario de cada turma nos dias
        for (int t = 0; t < qtdTurmas; t++) {
            for (int h = 0; h < qtdHorarios; h++) {
                for (int d = 0; d < qtdDias; d++) {
                    IloLinearNumExpr somaProf = model.linearNumExpr();
                    for (int p = 0; p < qtdProf; p++) {
                        somaProf.addTerm(1, x[d][h][t][p]);
                    }
                    model.addEq(somaProf, 1);
                }
            }
        }

        //A soma de cada professor em cada horarios que ele esta deve ser menor ou igual a 1 para proibir que possivelmente
        //ele esteja em duas turmas no mesmo dia e no mesmo horario
        for (int h = 0; h < qtdHorarios; h++) {
            for (int d = 0; d < qtdDias; d++) {
                for (int p = 0; p < qtdProf; p++) {
                    IloLinearNumExpr somaProfTurmas = model.linearNumExpr();
                    for (int t = 0; t < qtdTurmas; t++) {
                        somaProfTurmas.addTerm(1, x[d][h][t][p]);
                    }
                    model.addLe(somaProfTurmas, 1);
                }
            }
        }

        if (model.solve()) {
            System.out.println("Horarios Montados\n");
            System.out.println("Valor funcao objetivo: " + model.getObjValue());
            System.out.println("STATUS: " + model.getStatus());

            for (int t = 0; t < qtdTurmas; t++) {
                System.out.println("Horarios turma : " + t);
                System.out.println("SEG  TER  QUA  QUI  SEX");
                for (int h = 0; h < qtdHorarios; h++) {
                    for (int d = 0; d < qtdDias; d++) {
                        for (int p = 0; p < qtdProf; p++) {
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

            for (int t = 0; t < qtdTurmas; t++) {
                for (int p = 0; p < qtdProf; p++) {
                    System.out.println("Horarios prof: " + p);
                    System.out.println("SEG  TER  QUA  QUI  SEX");
                        for (int h = 0; h < qtdHorarios; h++) {
                    for (int d = 0; d < qtdDias; d++) {
                            if (model.getValue(x[d][h][t][p]) == 1) {
                                System.out.print(t + " | ");
                            } else {
                                System.out.print("- | ");
                            }
                        }
                        System.out.println("");
                    }
                }
                System.out.println("\n\n\n");
            }

        }

    }
}
