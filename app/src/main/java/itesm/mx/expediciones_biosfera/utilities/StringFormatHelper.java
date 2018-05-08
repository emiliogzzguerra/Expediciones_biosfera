package itesm.mx.expediciones_biosfera.utilities;

import android.content.res.Resources;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import itesm.mx.expediciones_biosfera.R;

public class StringFormatHelper {

    public static String getPriceFormat(double price, Resources res) {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        String formattedPrice = formatter.format(price);

        if( price == 0 )
           formattedPrice = "0.00";

        return res.getString(R.string.price_text, formattedPrice);

    }

    public static String getDateAsString(Date date, Boolean capitalized) {
        String myFormat = "EEEE d 'de' MMMM 'de' yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, new Locale("es", "MX"));

        String sDate = dateFormat.format(date);

        if (capitalized)
            return sDate.substring(0, 1).toUpperCase() + sDate.substring(1);

        return sDate;

    }


}
