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
public class Demo {
    public static void main(String args[])
    {
        String Main = "[sdsd, sdsd, sdsd]";
        String replaced = Main.replaceAll("\\[|\\]", "");
        System.out.println(replaced);
    }
            
}
