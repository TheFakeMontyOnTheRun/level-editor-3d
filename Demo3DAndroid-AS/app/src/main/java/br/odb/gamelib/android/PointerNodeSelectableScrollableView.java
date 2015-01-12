package br.odb.gamelib.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import br.odb.gamerendering.rendering.RenderingNode;
import br.odb.utils.math.Vec2;

public class PointerNodeSelectableScrollableView extends PointerScrollGameView {

	final Vec2 targetTranslate = new Vec2();
	protected RenderingNode selectedItem;
	public boolean shouldFollowTarget;
	

	public PointerNodeSelectableScrollableView(Context context) {
		super(context);	
		
	}

	public PointerNodeSelectableScrollableView(Context context,
			AttributeSet attrs) {
		super(context, attrs);
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		shouldFollowTarget = false;

		return super.onTouchEvent(event);
	}
	
	



	public void setSelectedItem(RenderingNode node) {
		selectedItem = node;
	}
	
	

	@Override
	public void update(  long ms ) {
		
		super.update( ms );
		
		if (shouldFollowTarget) {

			Vec2 inverted = new Vec2(accScroll);
			inverted.y *= -1.0f;
			inverted.x *= -1.0f;
			inverted.y += getHeight() / 2.0f;
			inverted.x += getWidth() / 2.0f;
			Vec2 translate = selectedItem.translate.sub(inverted);

			setAntiAliasing( true );
			
			if (translate.x > 16.0f) {
				targetTranslate.x = -4;
				setAntiAliasing( false );
			} else if (translate.x < -16.0f) {
				targetTranslate.x = 4;
				setAntiAliasing( false );
			} else {
				targetTranslate.x = 0;
			}

			if (translate.y > 16.0f) {
				targetTranslate.y = -4;
				setAntiAliasing( false );
			} else if (translate.y < -16.0f) {
				targetTranslate.y = 4;
				setAntiAliasing( false );
			} else {
				targetTranslate.y = 0;
			}

			super.accScroll.addTo(targetTranslate);
		}
	}
}
