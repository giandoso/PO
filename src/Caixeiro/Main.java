/*
 * João Pedro Giandoso - 2015.1.08.029
 * jpgiandoso@gmail.com  * 
 */
package Caixeiro;

import ilog.concert.IloException;
import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author giandoso
 */
public class Main {

    public static void main(String[] args) throws IOException, IloException {
        CaixeiroPanel caixeiro = new CaixeiroPanel();
        caixeiro.ExecutaRotaCaixeiro("src/Caixeiro/v100.csv");
    }

}
