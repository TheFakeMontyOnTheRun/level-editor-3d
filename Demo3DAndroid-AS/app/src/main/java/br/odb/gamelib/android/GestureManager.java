package br.odb.gamelib.android;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import br.odb.utils.math.Vec2;

public class GestureManager implements OnTouchListener {
	/**
	 * 
	 */
	private long lastTapTime;
	/**
	 * 
	 */
	private Vec2 initialPosition;
	/**
	 * 
	 */
	private Vec2 lastMove;
	/**
	 * 
	 */
	private View parent;
	
	// ------------------------------------------------------------------------------------------------------------
		/**
		 * 
		 */
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN: {
				
				initialPosition = new Vec2(event.getX(), event.getY());
			}
			break;
			
			case MotionEvent.ACTION_UP: {
				
				initialPosition = null;
			}
			break;
			
			case MotionEvent.ACTION_MOVE: {

				lastTapTime = 0;
				lastMove.set(event.getX(), event.getY());
			}
			break;

		}

		return true;
	}
	

	public GestureManager( View view ) {
		
		parent = view;
		lastMove = new Vec2();
	}
	
	public void resetMovement() {
		
		initialPosition.set( lastMove );
	}

	public boolean notReady() {
		
		return initialPosition == null;
	}


	public boolean isTurningLeft() {

		Vec2 heading = lastMove.sub( initialPosition );
		return heading.x < -parent.getWidth() / 4;
	}

	public boolean isTurningRight() {
		
		Vec2 heading = lastMove.sub( initialPosition );
		return heading.x > parent.getWidth() / 4;
	}

	public boolean isMovingUp() {
		
		Vec2 heading = lastMove.sub( initialPosition );
		return heading.y < -parent.getHeight() / 10;
	}

	public boolean isMovingDown() {

		Vec2 heading = lastMove.sub( initialPosition );
		return heading.y > parent.getHeight() / 10;
	}

	public long getTimeSinceLastTouch() {

		return System.currentTimeMillis() - lastTapTime;
	}
}
