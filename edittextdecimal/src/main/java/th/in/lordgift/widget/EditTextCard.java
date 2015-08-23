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
 * custom EditText for add hyphen for display.
 * Ex. 1-1212-12121-12-1
 *
 * Created by Gift on 21-Jul-15.
 */
public class EditTextCard extends EditText implements TextWatcher, InputFilter {

    private boolean isFired = false;
    private int cardType;

    public static final int CARDTYPE_OTHER = 0;
    public static final int CARDTYPE_IDCARD = 1;


    public EditTextCard(Context context) {
        super(context);
    }

    public EditTextCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditTextCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EditTextCard);
        cardType = array.getInt(R.styleable.EditTextCard_cardType, -1);

        if (cardType < 0) {
            throw new IllegalArgumentException("Required attribute might not set, or invalid value. Please check res-auto attributes");
        }

        array.recycle();

        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        this.setFilters(new InputFilter[]{this});
    }

    /**
     * getText without symbol that added for format display
     *
     * @return realvalue in string without format symbol
     */
    public String getValue() {
        if (!TextUtils.isEmpty(getText()) && getText().toString().contains("-")) {
            return getText().toString().replaceAll("-", "");
        }
        return getText().toString();
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (!isFired) {
            if (!String.valueOf(source).matches("(\\d|\\-)*")) {
                return "";
            } else {
                if (!TextUtils.isEmpty(dest)) {

                    //remove hyphen if exist
                    String destVal = dest.toString();
                    if (destVal.contains("-")) {
//                        dstart -= (destVal.split(",").length -1);
                        destVal = destVal.replaceAll("-", "");
                    }

                    switch (cardType) {
                        case CARDTYPE_IDCARD: //idCard
                            if (destVal.matches("\\d{13,}"))
                                return "";

                            //TODO add cardType here
                    }
                }
            }
        }
        return null;
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!isFired) {
            isFired = true;

            switch (cardType) {
                case CARDTYPE_IDCARD:
                    String removedHyphen = getText().toString().replaceAll("-", "");
                    if (count == 1 && removedHyphen.length() == 13 && !Utils.checkPID(removedHyphen)) {
                        this.setTextColor(getResources().getColor(android.R.color.holo_red_light));
//                        Toast.makeText(getContext(), "Invalid Id-Card", Toast.LENGTH_LONG).show();
                    } else {
                        this.setTextColor(getResources().getColor(android.R.color.black));
                    }
                    setText(Utils.insertIdCardHyphen(removedHyphen));
                    break;

                //TODO add cardType (enum) here
            }
        } else {
            isFired = false;
            setSelection(getText().length());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }
}
