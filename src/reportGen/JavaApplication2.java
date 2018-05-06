/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reportGen;

import java.util.Arrays;

/**
 *
 * @author ViduraDan
 */
public class JavaApplication2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String[] petStrings = {"Item 1", "Item 2","Item 3","Item 4","Item 5","Item 6","Item 7","Item 8"};
        String[] subString= Arrays.copyOfRange(petStrings, 0, 5);
        
        for (int i = 0; i < subString.length; i++){
        System.out.println(subString[i]);
        }
    
        
        
        // TODO code application logic here
    }
    
}
