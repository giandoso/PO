/*
 * João Pedro Giandoso - 2015.1.08.029
 * jpgiandoso@gmail.com  * 
 */
package sudoku;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author 2015.1.08.029
 */
public class Reader {

    File f;
    Scanner s;
    ArrayList<String[]> l = new ArrayList<>();

    public Reader(String path) throws FileNotFoundException {
        this.f = new File(path);
        this.s = new Scanner(f);
        
        
        while(s.hasNext()){
            String line = this.s.next();
            String[] split_line = line.split(",");
            l.add(split_line);
        }
        
        
        for(String[] arr : l){
            System.out.println(Arrays.toString(arr));
        }
    }

}
