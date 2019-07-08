/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MontaHorarios;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author glauc
 */
public class Leitura {

    public Informacoes leituraSudoku(String instancia) throws FileNotFoundException, IOException {

        BufferedReader conteudoCSV = null; //BufferedReader lê o arquivo inteiro
        String linha = ""; //nossa linha por linha
        String separador = "	";
        String[] linhassplitada;

        conteudoCSV = new BufferedReader(new FileReader(instancia));

        linha = conteudoCSV.readLine(); //Primeira linha contendo a quantidade de professores

        linhassplitada = linha.split(separador);

        int QtdProfessores = Integer.parseInt(linhassplitada[0]);

        linha = conteudoCSV.readLine(); //Segunda linha contendo a quantidade de turmas

        linhassplitada = linha.split(separador);

        int QtdTurmas = Integer.parseInt(linhassplitada[0]);

        linha = conteudoCSV.readLine(); //Terceira linha contendo a quantidade de Dias e Horarios

        linhassplitada = linha.split(separador);

        int qtdDias = Integer.parseInt(linhassplitada[0]);

        int qtdHorarios = Integer.parseInt(linhassplitada[1]);

        boolean[][][] matrizInviabilidade = new boolean[qtdHorarios][qtdDias][QtdProfessores];

        int[][] matrizAulas = new int[QtdProfessores][QtdTurmas];

        while ((linha = conteudoCSV.readLine()) != null && !linha.contentEquals("")) {

            linhassplitada = linha.split(separador);

            if (linhassplitada[0].equals("i")) {//inviabilidade
                matrizInviabilidade[Integer.parseInt(linhassplitada[2])][Integer.parseInt(linhassplitada[1])][Integer.parseInt(linhassplitada[3])] = true;
            } else {//quantidade de aulas
                matrizAulas[Integer.parseInt(linhassplitada[1])][Integer.parseInt(linhassplitada[2])] = Integer.parseInt(linhassplitada[3]);
            }
        }


        return new Informacoes(QtdTurmas, qtdHorarios, qtdDias, QtdProfessores, matrizInviabilidade, matrizAulas);

    }
}
