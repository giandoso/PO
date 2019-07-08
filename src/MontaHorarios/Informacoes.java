/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MontaHorarios;

/**
 *
 * @author glauc
 */
public class Informacoes {
    int qtdTurmas;
    int qtdHorarios;
    int qtdDias;
    int qtdProfessores;
    boolean matrizInviabilidade[][][];
    int matrizAulas[][];

    public Informacoes(int qtdTurmas, int qtdHorarios, int qtdDias, int qtdProfessores, boolean[][][] matrizInviabilidade, int[][] matrizAulas) {
        this.qtdTurmas = qtdTurmas;
        this.qtdHorarios = qtdHorarios;
        this.qtdDias = qtdDias;
        this.qtdProfessores = qtdProfessores;
        this.matrizInviabilidade = matrizInviabilidade;
        this.matrizAulas = matrizAulas;
    }

   
    
    
    
    
}
