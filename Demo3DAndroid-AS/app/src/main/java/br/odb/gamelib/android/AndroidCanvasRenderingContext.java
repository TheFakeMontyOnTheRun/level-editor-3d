/**
 * 
 */
package br.odb.gamelib.android;

import java.util.HashMap;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Region;
import android.graphics.Shader;
import br.odb.gamerendering.rendering.RasterImage;
import br.odb.gamerendering.rendering.RenderingContext;
import br.odb.libsvg.ColoredPolygon;
import br.odb.libsvg.SVGParsingUtils.Gradient;
import br.odb.libsvg.SVGUtils;
import br.odb.utils.Color;
import br.odb.utils.Rect;
import br.odb.utils.math.Vec2;

/**
 * @author monty
 * 
 */
public class AndroidCanvasRenderingContext extends RenderingContext {

	private Canvas canvas;
	Paint paint;
	private HashMap<Gradient, LinearGradient> gradientsCache = new HashMap<Gradient, LinearGradient>();
	Path path = new Path();
	
	public void prepareWithCanvasAndPaint(Canvas canvas, Paint paint) {
		this.canvas = canvas;
		this.paint = paint;
		setReadyForRendering(true);
	}

	@Override
	public void fillRect(Color color, Rect rect) {

		android.graphics.Rect androidRect = AndroidUtils.toAndroidRect(rect);
		androidRect.top += currentOrigin.y;
		androidRect.bottom += currentOrigin.y;
		androidRect.left += currentOrigin.x;
		androidRect.right += currentOrigin.x;

		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setColor(color.getARGBColor());
		paint.setAlpha((int) (currentAlpha * 255));
		canvas.drawRect(androidRect, paint);
	}

	@Override
	public void prepareForRendering() {
		// TODO Auto-generated method stub

	}

	@Override
	public void endRendering() {
		this.paint = null;
		this.canvas = null;
		setReadyForRendering(false);
	}

	@Override
	public void drawColoredPolygon(ColoredPolygon pol, Rect bounds,
			String style, HashMap<String, Gradient> gradients) {

		LinearGradient lg = null;
		int currentColor = 0;

		Vec2 origin = bounds.p0.add(this.currentOrigin);

		if (currentAlpha < 1.0f) {
			paint.setAlpha((int) (currentAlpha * 255));
		}

		float diffX = 0;
		float diffY = 0;


		if (pol.xpoints == null || pol.ypoints == null || pol.npoints <= 0) {

			return;
		}

		path.reset();
		
		path.moveTo((origin.x + pol.xpoints[0]), origin.y + pol.ypoints[0]);

		for (int c = 0; c < pol.npoints; ++c) {

			if (((Vec2) pol.controlPoints.get(c)).isValid()) {

				Vec2 control = (Vec2) pol.controlPoints.get(c);

				path.cubicTo(origin.x + (pol.xpoints[c]) - diffX, origin.y
						+ pol.ypoints[c] - diffY, origin.x + control.x,
						origin.y + control.y - diffY, origin.x
								+ (pol.xpoints[c + 1]) - diffX, origin.y
								+ pol.ypoints[c + 1] - diffY);
				++c;
			} else {

				path.lineTo(origin.x + pol.xpoints[c], origin.y
						+ pol.ypoints[c]);
			}
		}

		path.close();

		if (currentAlpha < 1.0f) {
			paint.setAlpha(255);
		}

		if (pol.originalStyle != null) {

			if (pol.color == null) {
				pol.color = SVGUtils.parseColorFromStyle(pol.originalStyle);
			}

			if (pol.gradient == null) {

				pol.gradient = SVGUtils
						.parseGradientFromStyle(pol.originalStyle);
			}
		}

		if (pol.gradient != null) {


			Gradient g0 = gradients.get(pol.gradient);
			
			if ( gradientsCache .containsKey( g0 ) ) {
				lg = gradientsCache.get( g0 );
			} else {
				Color color1;
				Color color2;
				

				if (g0.stops[0].color == null) {

					g0.stops[0].color = SVGUtils.parseColorFromStyle(
							g0.stops[0].style, "stop-color", "stop-opacity");
				}

				if (g0.stops[1].color == null) {

					g0.stops[1].color = SVGUtils.parseColorFromStyle(
							g0.stops[1].style, "stop-color", "stop-opacity");
				}

				color1 = g0.stops[0].color;
				color2 = g0.stops[1].color;

				if (pol.color != null) {

					color1.a = ((int) ((color1.a / 255.0f) * (pol.color.a / 255.0f) * 255));
					color2.a = ((int) ((color2.a / 255.0f) * (pol.color.a / 255.0f) * 255));
				}

				lg = new LinearGradient(g0.x1, g0.y1, g0.x2, g0.y2,
						(int) color1.getARGBColor(), (int) color2.getARGBColor(),
						Shader.TileMode.CLAMP);
				
				gradientsCache.put( g0, lg );
			}			


			paint.setShader(lg);

		} else if (pol.color != null) {

			if (currentColor != pol.color.getARGBColor()) {
				currentColor = pol.color.getARGBColor();
				paint.setColor(currentColor);
				paint.setAlpha((int) (pol.color.a * currentAlpha));
			}
		} else {
			paint.setColor(0xFF000000);
		}
		

		canvas.drawPath(path, paint);

		if (lg != null) {
			paint.setShader(null);
		}
	}

	@Override
	public void saveClipRect() {
		canvas.save();
	}

	@Override
	public void restoreClipRect() {
		canvas.restore();
	}

	@Override
	public void drawBitmap(RasterImage image, Vec2 p0) {
		AndroidRasterImage ari = (AndroidRasterImage) image;
		canvas.drawBitmap(ari.bitmap, p0.x, p0.y, paint);
	}

	@Override
	public void drawBitmap(RasterImage image, Rect bounds) {

		AndroidRasterImage ari = (AndroidRasterImage) image;
		android.graphics.Rect src = new android.graphics.Rect();
		src.top = 0;
		src.left = 0;
		src.right = (int) bounds.getDX();
		src.bottom = (int) bounds.getDY();
		android.graphics.Rect dst = AndroidUtils.toAndroidRect(bounds);
		dst.left += currentOrigin.x;
		dst.top += currentOrigin.y;
		dst.bottom += currentOrigin.y;
		dst.right += currentOrigin.x;
		canvas.drawBitmap(ari.bitmap, src, dst, paint);
	}

	public void setClipRect(Rect bounds) {
		android.graphics.Rect rect = AndroidUtils.toAndroidRect(bounds);
		canvas.clipRect(rect, Region.Op.REPLACE);
	}

	@Override
	public void setCurrentAlpha(float f) {
		super.setCurrentAlpha(f);

		paint.setAlpha((int) (currentAlpha * 255));
	}

	@Override
	public void drawBitmap(RasterImage image, Vec2 p0, Vec2 scale, int rotation) {
		AndroidRasterImage ari = (AndroidRasterImage) image;

		Matrix rotator = new Matrix();

		rotator.postRotate(rotation, image.getWidth() / 2,
				image.getHeight() / 2);
		rotator.postScale(scale.x, scale.y);
		rotator.postTranslate(p0.x, p0.y);

		canvas.drawBitmap(ari.bitmap, rotator, paint);

	}

	public void setAntiAlias(boolean b) {
		if (paint != null) {
			paint.setAntiAlias(b);
		}
	}

	@Override
	public void drawLine(Vec2 p0, Vec2 p1) {
		canvas.drawLine( p0.x, p0.y, p1.x, p1.y, paint);		
	}

	@Override
	public void drawText(Vec2 p0, String text, Color c, int fontSize ) {
		paint.setColor( c.getARGBColor() );
		paint.setTextSize( fontSize );
		canvas.drawText( text, currentOrigin.x,  currentOrigin.y, paint);
	}
}
