/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.gob.ine.web;

/**
 *
 * @author desktop
 */
public class NewClass {

    public static void main(String[] args) {
        int[] monedas = {15, 8, 10, 23};
        int bs = 0; // bolivianos
        int ctv = 0; // centavos
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0: // monedas de 10
                    ctv = ctv + (monedas[i] * 10);
                    break;
                case 1: // monedas de 20
                    ctv = ctv + (monedas[i] * 20);
                    break;
                case 2: // monedas de 50
                    ctv = ctv + (monedas[i] * 50);
                    break;
                case 3: // monedas de 1 Bs
                    ctv = ctv + (monedas[i] * 100);
                    break;
            }            
        }
        bs = ctv / 100;
        ctv = ctv % 100;
        System.out.println("Se tiene: " + bs + " Bs, con " + ctv + " ctvs.");        
    }
}
