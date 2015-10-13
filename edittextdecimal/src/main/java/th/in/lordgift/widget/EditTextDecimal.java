package th.in.lordgift.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

import th.in.lordgift.edittextdecimal.R;

/**
 * custom EditText for using with scale of number.
 *
 * precision = all number included scale (12345.67 : p=7,s=2)
 *
 * Ex. 12345.67
 *
 * Created by Gift on 20-Jul-15.
 */
public class EditTextDecimal extends EditText implements InputFilter {

    private int precision, scale;

    public EditTextDecimal(Context context) {
        super(context);
    }

    public EditTextDecimal(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditTextDecimal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EditTextDecimal);
        precision = array.getInt(R.styleable.EditTextDecimal_precision, -1);
        scale = array.getInt(R.styleable.EditTextDecimal_scale, -1);

        if (precision < 0 || scale < 0 || scale > precision) {
            throw new IllegalArgumentException("Required attribute might not set, or invalid value. Please check res-auto attributes");
        }

        array.recycle();

        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        this.setFilters(new InputFilter[]{this});
    }

    /**
     * getText without symbol that added for format display
     *
     * @return realvalue in string without format symbol
     */
    public String getValue() {
        return getText().toString();
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        for (int i = start; i < end; i++) {
            //not match pattern or start with dot(.)
            if (!String.valueOf(source.charAt(i)).matches("\\d|\\.") || (dest.toString().length() == 0 && ".".equals(source.toString()))) {
                return "";
            } else {
                if (!TextUtils.isEmpty(dest)) {

                    if (".".equals(source.toString()) && dest.toString().contains("."))
                        return "";

                    if (".".equals(source.toString()))
                        if (dest.toString().contains(".") || dest.toString().substring(dstart).length() > scale)
                            return "";

                    //only precision without floating point
                    if (!".".equals(source.toString()) && dest.toString().matches("\\d{" + (precision - scale) + ",}"))
                        return "";

                    if (dest.toString().contains(".")) {
                        //scale limit (avoid move cursor when scale full by check only case dot before cursor)
                        if (dest.toString().matches("\\d*\\.\\d{" + scale + ",}"))
                            if (dest.toString().indexOf(".") < dstart)
                                return "";

                        //precision limit (avoided move cursor when precision full by check only case dot after cursor)
                        if (dest.toString().matches("\\d{" + (precision - scale) + ",}\\.\\d*"))
                            if (dest.toString().indexOf(".") >= dstart)
                                return "";
                    }
                }
            }
        }
        return null;
    }
}
