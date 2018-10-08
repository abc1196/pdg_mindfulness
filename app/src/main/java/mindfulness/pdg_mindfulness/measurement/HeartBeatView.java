package mindfulness.pdg_mindfulness.measurement;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import mindfulness.pdg_mindfulness.R;
import mindfulness.pdg_mindfulness.measurement.HRVActivity;

public class HeartBeatView extends View {

    private static final Matrix matrix = new Matrix();
    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private static Bitmap greenBitmap = null;
    private static Bitmap redBitmap = null;

    private static int parentWidth = 0;
    private static int parentHeight = 0;

    public HeartBeatView(Context context, AttributeSet attr) {
        super(context, attr);

        greenBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back_primary_24dp);
        redBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_home);
    }

    public HeartBeatView(Context context) {
        super(context);

        greenBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back_primary_24dp);
        redBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_home);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(parentWidth, parentHeight);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas == null) {
            throw new NullPointerException();
        } else {
            Bitmap bitmap = null;
            if (HRVActivity.getCurrent() == HRVActivity.TYPE.GREEN) {
                bitmap =BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            } else {
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            }

                int bitmapX = bitmap.getWidth() / 2;
                int bitmapY = bitmap.getHeight() / 2;

                int parentX = parentWidth / 2;
                int parentY = parentHeight / 2;

                int centerX = parentX - bitmapX;
                int centerY = parentY - bitmapY;

                matrix.reset();
                matrix.postTranslate(centerX, centerY);
                canvas.drawBitmap(bitmap, matrix, paint);
            }
    }
}
