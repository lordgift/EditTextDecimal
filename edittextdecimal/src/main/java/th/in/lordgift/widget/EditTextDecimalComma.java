package th.in.lordgift.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import th.in.lordgift.edittextdecimal.R;

/**
 *
 * custom EditText for using with scale of number and add comma for precision.
 *
 * precision = all number included scale (12345.67 : p=7,s=2)
 *
 * Ex. 1,234.56
 *
 * Created by Gift on 20-Jul-15.
 */
public class EditTextDecimalComma extends EditText implements InputFilter, TextWatcher {

    private int precision, scale;
    private boolean isFired = false;

    public EditTextDecimalComma(Context context) {
        super(context);
    }

    public EditTextDecimalComma(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditTextDecimalComma(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EditTextDecimalComma);
        precision = array.getInt(R.styleable.EditTextDecimalComma_precision, -1);
        scale = array.getInt(R.styleable.EditTextDecimalComma_scale, -1);

        if(precision < 0 || scale < 0 || scale > precision) {
            throw new IllegalArgumentException("Required attribute might not set, or invalid value. Please check res-auto attributes");
        }

        array.recycle();

        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        this.setFilters(new InputFilter[]{this});
    }

    /**
     * getText without symbol that added for format display
     * @return realvalue in string without format symbol
     */
    public String getValue() {
        if(!TextUtils.isEmpty(getText()) && getText().toString().contains(",")) {
            return getText().toString().replaceAll(",","");
        }
        return  getText().toString();
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Log.d("", "filter");
        if(!isFired) {
            for (int i = start; i < end; i++) {
                //not match pattern or start with dot(.)
                if (!String.valueOf(source.charAt(i)).matches("(\\d|\\.|,)*") || (dest.toString().length() == 0 && ".".equals(source.toString()))) {
                    return "";
                } else {
                    if (!TextUtils.isEmpty(dest)) {

                        //remove comma if exist
                        String destVal = dest.toString();
                        if(destVal.contains(",")) {
                            dstart -= (destVal.split(",").length -1);
                            destVal = destVal.replaceAll(",","");
                        }

                        if (".".equals(source.toString()) && destVal.contains("."))
                            return "";

                        if (".".equals(source.toString()))
                            if (destVal.contains(".") || dstart < 0 || destVal.substring(dstart).length() > scale)
                                return "";

                        //only precision without floating point
                        if (!".".equals(source.toString()) && destVal.matches("\\d{" + (precision-scale) + ",}"))
                            return "";

                        if (destVal.contains(".")) {
                            //scale limit (avoid move cursor when scale full by check only case dot before cursor)
                            if (destVal.matches("\\d*\\.\\d{" + scale + ",}"))
                                if (destVal.indexOf(".") < dstart)
                                    return "";

                            //precision limit (avoided move cursor when precision full by check only case dot after cursor)
                            if (destVal.matches("\\d{" + (precision-scale) + ",}\\.\\d*"))
                                if (destVal.indexOf(".") >= dstart)
                                    return "";
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.d("", "beforeTextChanged");
    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d("", "onTextChanged");
        if(!isFired) {
            isFired = true;

            //remove comma if exist
            String text = getText().toString();
            if(text.contains(",")) text = text.replaceAll(",","");

            if(text.matches("\\d+\\.\\d+")) {
                setText( Utils.insertNumberComma(text.split("\\.")[0])+"."+text.split("\\.")[1] );
            } else if(text.contains(".")) {
                setText(Utils.insertNumberComma(text.replace(".",""))+".");
            } else {
                setText(Utils.insertNumberComma(text));
            }
        } else {
            isFired = false;
            setSelection(getText().length());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d("", "afterTextChanged");
    }


}
