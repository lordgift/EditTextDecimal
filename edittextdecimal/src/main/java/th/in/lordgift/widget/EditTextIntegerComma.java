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
import android.widget.EditText;

import th.in.lordgift.edittextdecimal.R;
import th.in.lordgift.widget.utils.Utils;

/**
 *
 * custom EditText for add comma for display.
 * Ex. 1,234,567
 *
 * Created by Gift on 21-Jul-15.
 */
public class EditTextIntegerComma extends EditText implements TextWatcher, InputFilter {

    private boolean isFired = false;
    private int numberLength;

    public EditTextIntegerComma(Context context) {
        super(context);
    }

    public EditTextIntegerComma(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditTextIntegerComma(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EditTextIntegerComma);
        numberLength = array.getInt(R.styleable.EditTextIntegerComma_numberLength, -1);

        if(numberLength < 0) {
            numberLength = 1000;
//            throw new IllegalArgumentException("Required attribute might not set, or invalid value. Please check res-auto attributes");
        }

        array.recycle();

        setInputType(InputType.TYPE_CLASS_NUMBER);

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
        if(!isFired) {
            if (!String.valueOf(source).matches("(\\d|,\\.|,)*")) {
                return "";
            } else {
                if (!TextUtils.isEmpty(dest)) {

                    //remove comma if exist
                    String destVal = dest.toString();
                    if(destVal.contains(",")) {
//                        dstart -= (destVal.split(",").length -1);
                        destVal = destVal.replaceAll(",","");
                    }

                    if (destVal.matches("\\d{" + numberLength + ",}"))
                        return "";
                }
            }
        }
        return null;
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!isFired) {
            isFired = true;
            setText( Utils.insertNumberComma(getText().toString()));
        } else {
            isFired = false;
            setSelection(getText().length());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {}




}
