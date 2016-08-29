package byteshaft.com.nationalpropertyassist.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.RadioButton;

import byteshaft.com.nationalpropertyassist.AppGlobals;

public class StyledRadioButton extends RadioButton {

    public StyledRadioButton() {
        super(AppGlobals.getContext());
        init();
    }

    public StyledRadioButton(Context context) {
        super(context);
        init();
    }

    private void init(){
        if(!isInEditMode()){
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/italic.ttf");
            setTypeface(tf);
        }
    }
}
