/**
 * 
 */
package br.odb.gamelib.android;

import br.odb.gamerendering.rendering.AssetManager;
import br.odb.gamerendering.rendering.DisplayList;
import br.odb.gamerendering.rendering.RenderingNode;
import br.odb.gamerendering.rendering.SVGRenderingNode;
import br.odb.libsvg.SVGGraphic;
import br.odb.utils.Rect;
import br.odb.utils.math.Vec2;



/**
 * @author monty
 *
 */
public class AndroidUtils {

	public static android.graphics.Rect toAndroidRect(Rect rect) {
		
		android.graphics.Rect androidRect = new android.graphics.Rect();
		
		androidRect.left = (int) rect.p0.x;
		androidRect.right = (int) rect.p1.x;
		androidRect.top = (int) rect.p0.y;
		androidRect.bottom = (int) rect.p1.y;
		
		return androidRect;
	}
	
	public static void initImage(GameView gv, String graphicPath, AssetManager resManager ) {

		DisplayList dl = new DisplayList("dl");

		SVGGraphic graphic = resManager.getGraphics(graphicPath);

		float scale = 1;
		Vec2 trans = new Vec2();

			if ( gv.getWidth() > 0 && gv.getHeight() > 0) {
				
				Rect bound = graphic.makeBounds();
				
				// não me interessa a parte acima da "página".
				float newWidth = bound.p1.x;
				float newHeight = bound.p1.y;
				
				if (newWidth > newHeight) {
					scale = gv.getWidth() / newWidth;
					trans.y = (gv.getHeight() - (bound.p1.y * scale)) / 2.0f;
				} else {
					scale = gv.getHeight() / newHeight;
					trans.x = (gv.getWidth() - (bound.p1.x * scale)) / 2.0f;
				}
			}

		graphic = graphic.scale( scale, scale );
		
		
		
		SVGRenderingNode node = new SVGRenderingNode(graphic, "graphic_"
				+ graphicPath);

		dl.setItems(new RenderingNode[] { node });
		gv.setRenderingContent(dl);
		gv.updater.setRunning( false );
		gv.postInvalidate();
	}
	

	public static Rect toGameRect(
			android.graphics.Rect rect) {
		
		Rect gameRect = new Rect();
		
		gameRect.p0.x = rect.left;
		gameRect.p0.y = rect.top;
		gameRect.p1.x = rect.right;
		gameRect.p1.y = rect.bottom;
		
		return gameRect;
	}

	public static void initImage(GameView gv, String graphicPath,
			AssetManager resManager, int width, int height, String prefix) {
		DisplayList dl = new DisplayList("dl");

		SVGGraphic graphic = resManager.getGraphics(graphicPath);

		graphic = graphic.scaleTo( width, height );
		
		SVGRenderingNode node = new SVGRenderingNode(graphic, prefix
				+ graphicPath);

		dl.setItems(new RenderingNode[] { node });
		gv.setRenderingContent(dl);
		gv.updater.setRunning( false );
		gv.postInvalidate();
		
	}

	public static void initImage(GameView gv, SVGGraphic graphic, String id ) {
		DisplayList dl = new DisplayList("dl");

		SVGRenderingNode node = new SVGRenderingNode(graphic, id );

		dl.setItems(new RenderingNode[] { node });
		gv.setRenderingContent(dl);
		gv.updater.setRunning( false );
		gv.postInvalidate();		
	}
}
