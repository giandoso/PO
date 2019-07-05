/*
 * João Pedro Giandoso - 2015.1.08.029
 * jpgiandoso@gmail.com  * 
 */
package Caixeiro;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author giandoso
 */
public class Leitura {

    public LinkedList<Coordenada> lerPontos(String instancia) throws FileNotFoundException, IOException {

        BufferedReader conteudoCSV = null; //BufferedReader lê o arquivo inteiro
        String linha = ""; //nossa linha por linha
        String separador = ",";
        String[] linhassplitada;
        
        LinkedList<Coordenada> pontos = new LinkedList();

        conteudoCSV = new BufferedReader(new FileReader(instancia));

        linha = conteudoCSV.readLine(); //Primeira linha sendo ignorada com o nome dos parametros
        int i = 0;
        while ((linha = conteudoCSV.readLine()) != null && !linha.contentEquals("")) {
            linhassplitada = linha.split(separador);
            pontos.add(new Coordenada(Double.parseDouble(linhassplitada[0]), Double.parseDouble(linhassplitada[1])));        
        }
        return pontos;
    }
}
