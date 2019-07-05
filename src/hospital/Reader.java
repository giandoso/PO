package hospital;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Reader {

    private String path;
    public int numPessoas;
    public int numGrupos;
    public int array_tamanho_grupo[];
    public int[][] values;

    public Reader() throws FileNotFoundException, IOException {
        path = "src/hospital/instanciaHospital.txt";

        Scanner s = new Scanner(new FileReader(path));

        String linha = s.nextLine().replaceAll("[^0-9]", "");
        numPessoas = Integer.parseInt(linha);

        linha = s.nextLine().replaceAll("[^0-9]", "");
        numGrupos = Integer.parseInt(linha);

        array_tamanho_grupo = new int[numGrupos];
        for (int i = 0; i < numGrupos; i++) {
            array_tamanho_grupo[i] = Integer.parseInt(s.nextLine().replaceAll("G0[0-9]*\t", ""));
        }

        values = new int[numPessoas][numPessoas];
        for (int i = 0; i < numPessoas; i++) {
            for (int j = 0; j < numPessoas; j++) {
                values[i][j] = s.nextInt();
            }
        }
    }
}
