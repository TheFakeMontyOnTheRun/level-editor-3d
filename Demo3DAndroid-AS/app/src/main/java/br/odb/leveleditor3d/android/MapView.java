package br.odb.leveleditor3d.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import br.odb.libscene.GroupSector;
import br.odb.utils.math.Vec3;

/**
 * Created by monty on 6/17/15.
 */
public class MapView extends View {

    Paint paint = new Paint();

    public final Vec3 position = new Vec3();
    public final ArrayList<GroupSector> sectors = new ArrayList<GroupSector>();

    public MapView(Context context) {
        super(context);
        init();
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while( true ) {
                    try {
                        Thread.sleep( 250 );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    MapView.this.postInvalidate();
                }
            }
        }).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int xOff = (int) (( - position.x ) + ( getWidth() / 2.0f ));
        int yOff = (int) (( - position.z ) + ( getHeight()  /2.0f ));
        paint.setStyle(Paint.Style.FILL);

        Vec3 pos;
        synchronized ( sectors ) {
            for ( GroupSector gs : sectors ) {
                paint.setColor( gs.material.mainColor.getARGBColor() );
                pos = gs.getAbsolutePosition();
                canvas.drawRect( xOff + pos.x, yOff + pos.z, xOff + pos.x + gs.size.x, yOff + pos.z + gs.size.z, paint );
            }

            paint.setColor(Color.BLACK );
            canvas.drawRect( getWidth() / 2.0f, ( getHeight() / 2.0f ), ( getWidth() / 2.0f ) + 5, ( getHeight() / 2.0f ) + 5, paint );
        }
    }
}
