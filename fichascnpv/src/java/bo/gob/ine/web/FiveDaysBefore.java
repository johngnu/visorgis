/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.gob.ine.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author desktop
 */
public class FiveDaysBefore {

    public static void main(String[] args) throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -6);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        Calendar calx = Calendar.getInstance();
        calx.setTime(cal.getTime());
        for (int i = 1; i <= 6; i++) {
            System.out.println(df.format(calx.getTime()));
            calx.add(Calendar.DAY_OF_YEAR, 1);
        }
        DateFormat df1 = new SimpleDateFormat("dd/MM");
        Date d = new Date(1582584486073l);
        System.out.println(df1.format(d));
    }
}
