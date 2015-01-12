package br.odb.gamelib.android;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import br.odb.gamerendering.rendering.DisplayList;
import br.odb.gamerendering.rendering.GameRenderer;
import br.odb.gamerendering.rendering.RenderingNode;
import br.odb.gamerendering.rendering.SolidSquareRenderingNode;
import br.odb.utils.Color;
import br.odb.utils.Rect;
import br.odb.utils.Updatable;
import br.odb.utils.math.Vec2;

public class GameView extends View implements Updatable {

	Paint paint = new Paint();

	public class Updater implements Runnable {

		GameView view;
		boolean stillRunning;

		public long latency = 100;

		public Updater(GameView view) {
			this.stillRunning = false;
			this.view = view;
		}

		public void setRunning(boolean running) {
			stillRunning = running;
		}

		@Override
		public void run() {
			while (stillRunning) {
				try {
					Thread.sleep(latency);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				update(latency);
				postInvalidate();

			}
		}
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			return performClick();
		}
		return true;
	}

	private GameRenderer gameRenderer;
	private AndroidCanvasRenderingContext renderingContext;
	volatile private DisplayList renderingNode;
	private long renderingBudget;
	private RenderingNode defaultRenderingNode;

	private Thread updateThread;
	public Updater updater;

	public void update(long ms) {
		if (renderingNode != null) {

			renderingNode.update(ms);
		}
	}

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init(context);
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context);
	}

	public GameView(Context context) {
		super(context);

		init(context);
	}

	public void init(Context context) {
		this.requestFocus();
		this.setFocusableInTouchMode(true);

		// if (!isInEditMode()) {
		// setLayerType(View.LAYER_TYPE_HARDWARE, null);
		// }

		paint.setAntiAlias(true);
		renderingBudget = 100;
		renderingContext = new AndroidCanvasRenderingContext();
		gameRenderer = new GameRenderer();
		gameRenderer.setCurrentRenderingContext(renderingContext);
		
		DisplayList dl = new DisplayList( "dl" );
		dl.setItems( new RenderingNode[] { new SolidSquareRenderingNode(new Rect(10, 10, 100, 100), new Color(255, 0, 0)),
                new SolidSquareRenderingNode(new Rect(110, 10, 100, 100), new Color(0, 255, 0)),
                new SolidSquareRenderingNode(new Rect(210, 10, 100, 100), new Color(0, 0, 255))} );
        defaultRenderingNode = dl;


		updater = new Updater(this);
		updater.setRunning(true);
		updateThread = new Thread(updater);
		updateThread.start();

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		doDraw(canvas);
	}

	public RenderingNode getRenderingContent() {
		return renderingNode;
	}

	public void doDraw(Canvas canvas) {

		long t0 = System.currentTimeMillis();
		long tn = t0;

		renderingContext.prepareWithCanvasAndPaint(canvas, paint);

		if (renderingNode == null) {
			renderDefaultEmptyScreen();
			return;
		}

		renderingContext.currentOrigin.addTo(renderingNode.translate);
		gameRenderer.startRendering(renderingNode);

		while (gameRenderer.hasJobs() && ((tn - t0) < this.renderingBudget)) {
			gameRenderer.renderNext();
			tn = System.currentTimeMillis();
		}

		gameRenderer.resetRenderingContext();
		renderingContext.currentOrigin.addTo(renderingNode.translate.negated());
	}

	private void renderDefaultEmptyScreen() {
		if (defaultRenderingNode == null) {

			defaultRenderingNode = new SolidSquareRenderingNode(new Rect(100,
					100, 200, 200), new Color(255, 255, 0));
		}
		gameRenderer.renderNode(defaultRenderingNode);

	}

	public void setRenderingContent(DisplayList displayList) {
		this.renderingNode = displayList;
		this.renderingContext.currentOrigin.set(0.0f, 0.0f);
	}

	public void setRenderingContent(List<RenderingNode> nodes) {
		RenderingNode[] contents;
		DisplayList dl = new DisplayList("dl");
		contents = new RenderingNode[nodes.size()];
		contents = nodes.toArray(contents);
		dl.setItems(contents);
		setRenderingContent(dl);
	}

	public void setRenderingBudget(long milisseconds) {
		this.renderingBudget = milisseconds;
	}

	public RenderingNode getElementAt(Vec2 pointer) {
		for (RenderingNode node : renderingNode.getItems()) {
			if (node.isInside(pointer)) {
				return node;
			}
		}
		return null;
	}

	public void setAntiAliasing(boolean b) {

		boolean previous;

		if (renderingContext.paint != null) {

			previous = renderingContext.paint.isAntiAlias();

			if (b != previous) {
				renderingContext.setAntiAlias(b);
				postInvalidate();
			}
		}
	}
}
