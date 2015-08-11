package th.in.lordgift.edittextdecimaldemo.widget;

import android.text.TextUtils;

public class Utils {

    /**
     * insert comma into normalNumber <BR />
     * Ex. input : 123456, output : 123,456
     * <p/>
     * <BR /><BR />
     * by reverse input text, count for 3-digit then add comma and reverse again for return correct value.
     *
     * @param normalNumber unformat number
     * @return formatted number
     */
    public static String insertNumberComma(String normalNumber) {
        if (TextUtils.isEmpty(normalNumber)) return "";

        if (normalNumber.length() > 3) {
            StringBuilder numberReverse = new StringBuilder();
            normalNumber = normalNumber.replaceAll(",", "");
            for (int i = normalNumber.length() - 1, count = 1; i >= 0; --i, ++count) {
                numberReverse.append(normalNumber.charAt(i));
                if (count == 3 && i != 0) {
                    numberReverse.append(",");
                    count = 0;
                }
            }
            return numberReverse.reverse().toString();
        } else {
            return normalNumber;
        }
    }

    /**
     * insert hyphen into normal text like this 1-1212-12121-12-1
     *
     * @param normalNumber unformat number
     * @return formatted number
     */
    public static String insertIdCardHyphen(String normalNumber) {
        if (TextUtils.isEmpty(normalNumber)) return "";

        StringBuilder formatted = new StringBuilder();
        normalNumber = normalNumber.replaceAll("-", "");
        for (int i = 0; i < normalNumber.length(); i++) {
            if (i == 1 || i == 5 || i == 10 || i == 12) formatted.append('-');
            formatted.append(normalNumber.charAt(i));
        }

        return formatted.toString();
    }

    /**
     * validate Thai people id
     *
     * @param id
     * @return true if the id is valid
     */
    public static boolean checkPID(String id) {
        if (id.length() != 13) return false;

        int sum = 0;

         /* x13, x12, x11, ... */
        for (int i = 0; i < 12; i++) {
            sum += Integer.parseInt(String.valueOf(id.charAt(i))) * (13 - i);
        }

         /* complements(12, sum mod 11) */
        return id.charAt(12) - '0' == ((11 - (sum % 11)) % 10);
    }

}
