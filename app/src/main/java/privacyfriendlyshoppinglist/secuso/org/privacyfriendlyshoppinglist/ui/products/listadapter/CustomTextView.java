package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.products.listadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.R;

@SuppressLint("AppCompatCustomView")
public class CustomTextView extends TextView {
    private int mColor;
    private Paint paint;

    public CustomTextView (Context context) {
        super(context);
        init(context);
    }

    public CustomTextView (Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTextView (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        Resources resources = context.getResources();
        //Color
        mColor = resources.getColor(R.color.colorPrimary);

        paint = new Paint();
        paint.setColor(mColor);
        //Width
        paint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, 110, getWidth()+10, 110, paint);
    }
}