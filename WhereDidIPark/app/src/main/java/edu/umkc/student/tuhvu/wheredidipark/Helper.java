package edu.umkc.student.tuhvu.wheredidipark;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Sil3nz3r on 12/10/2015.
 */
public class Helper {
    public static String formatDate(long datetime) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format.format(datetime);
    }
}
