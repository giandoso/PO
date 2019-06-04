/*
 * João Pedro Giandoso - 2015.1.08.029
 * jpgiandoso@gmail.com  * 
 */
package container_v2;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author 2015.1.08.029
 */
public class Reader {

    File f;
    Scanner s;
    Double[] volume;
    Double[] weight;
    Double[] value;

    public Reader(String path) throws IOException {
        this.f = new File(path);
        this.s = new Scanner(f);
        
        int n = 200;
        
        volume = new Double[n];
        weight = new Double[n];
        value = new Double[n];
        
        this.s.next();
        int i = 0;
        while(s.hasNext()){
            String line = this.s.next();
            String[] s = line.split(",");
            volume[i] = Double.parseDouble(s[1]);
            weight[i] = Double.parseDouble(s[2]);
            value[i] = Double.parseDouble(s[3]);
            i++;
        }
    }
    
    Double[] getValue() {
        return value;
    }

    Double[] getWeight() {
        return weight;
    }

    Double[] getVolume() {
        return volume;
   }

}
