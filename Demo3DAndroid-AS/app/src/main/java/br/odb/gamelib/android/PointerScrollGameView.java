package br.odb.gamelib.android;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import br.odb.utils.math.Vec2;

public class PointerScrollGameView extends GameView {
	private Vec2 cameraScroll;
	private Vec2 lastTouchPosition;
	protected Vec2 accScroll;

	public PointerScrollGameView(Context context) {
		super(context);
		
		initPointerScrollGameView();
	}
	
	private void initPointerScrollGameView() {
		accScroll = new Vec2();
		new Vec2();
		cameraScroll = new Vec2();
		lastTouchPosition = new Vec2();
	}
	
	public PointerScrollGameView(Context context, AttributeSet attrs) {
		super( context, attrs );
		
		initPointerScrollGameView();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		super.setAntiAliasing( false );
		
		if (event.getAction() == MotionEvent.ACTION_MOVE) {

			cameraScroll.x += (event.getX() - lastTouchPosition.x);
			cameraScroll.y += (event.getY() - lastTouchPosition.y);

			accScroll.x += (event.getX() - lastTouchPosition.x);
			accScroll.y += (event.getY() - lastTouchPosition.y);

			lastTouchPosition.x = (int) event.getX();
			lastTouchPosition.y = (int) event.getY();

			cameraScroll.x = 0;
			cameraScroll.y = 0;

		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
			lastTouchPosition.x = (int) event.getX();
			lastTouchPosition.y = (int) event.getY();
		} else {

			lastTouchPosition.x = (int) event.getX();
			lastTouchPosition.y = (int) event.getY();
			super.setAntiAliasing( true );
		}		

		postInvalidate();

		return true;

	}
	
	@Override
	public void doDraw(Canvas canvas) {
		
		if ( this.getRenderingContent() != null ) {
			
			this.getRenderingContent().translate.set( accScroll );
		}
		
		super.doDraw(canvas);
	}
}
