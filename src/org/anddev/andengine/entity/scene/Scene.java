package org.anddev.andengine.entity.scene;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.UpdateHandlerList;
import org.anddev.andengine.entity.BaseEntity;
import org.anddev.andengine.entity.layer.DynamicCapacityLayer;
import org.anddev.andengine.entity.layer.FixedCapacityLayer;
import org.anddev.andengine.entity.layer.ILayer;

import android.view.MotionEvent;

/**
 * @author Nicolas Gramlich
 * @since 12:47:39 - 08.03.2010
 */
public class Scene extends BaseEntity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private Scene mParentScene;

	private float mSecondsElapsedTotal;

	protected Scene mChildScene;
	private boolean mChildSceneModalDraw;
	private boolean mChildSceneModalUpdate;

	private final ILayer[] mLayers;

	private final ArrayList<ITouchArea> mTouchAreas = new ArrayList<ITouchArea>();

	private float mRed = 0.0f;
	private float mGreen = 0.0f;
	private float mBlue = 0.0f;
	private float mAlpha = 1.0f;

	private final UpdateHandlerList mPreFrameHandlers = new UpdateHandlerList();
	private final UpdateHandlerList mPostFrameHandlers = new UpdateHandlerList();

	private IOnSceneTouchListener mOnSceneTouchListener;

	private IOnAreaTouchListener mOnAreaTouchListener;

	private boolean mBackgroundEnabled = true;

	private boolean mOnAreaTouchTraversalBackToFront = true;

	// ===========================================================
	// Constructors
	// ===========================================================

	public Scene(final int pLayerCount) {
		this.mLayers = new ILayer[pLayerCount];
		this.createLayers();
	}

	public Scene(final int pLayerCount, final boolean pFixedCapacityLayers, final int ... pLayerCapacities) {
		if(pLayerCount != pLayerCapacities.length) {
			throw new IllegalArgumentException("pLayerCount must be the same as the length of pLayerCapacities.");
		}
		this.mLayers = new ILayer[pLayerCount];
		this.createLayers(pFixedCapacityLayers, pLayerCapacities);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getSecondsElapsedTotal() {
		return this.mSecondsElapsedTotal;
	}

	public ILayer getLayer(final int pLayerIndex) throws ArrayIndexOutOfBoundsException {
		return this.mLayers[pLayerIndex];
	}

	public int getLayerCount() {
		return this.mLayers.length;
	}

	public ILayer getBottomLayer() {
		return this.mLayers[0];
	}

	public ILayer getTopLayer() {
		return this.mLayers[this.mLayers.length - 1];
	}

	public boolean isBackgroundEnabled() {
		return this.mBackgroundEnabled;
	}

	public void setBackgroundEnabled(final boolean pEnabled) {
		this.mBackgroundEnabled  = pEnabled;
	}

	public void setBackgroundColor(final float pRed, final float pGreen, final float pBlue) {
		this.mRed = pRed;
		this.mGreen = pGreen;
		this.mBlue = pBlue;
	}

	public void setBackgroundColor(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mRed = pRed;
		this.mGreen = pGreen;
		this.mBlue = pBlue;
		this.mAlpha = pAlpha;
	}

	public void clearTouchAreas() {
		this.mTouchAreas.clear();
	}

	public void clearPreFrameHandlers() {
		this.mPreFrameHandlers.clear();
	}

	public void clearPostFrameHandlers() {
		this.mPostFrameHandlers.clear();
	}

	public void registerTouchArea(final ITouchArea pTouchArea) {
		this.mTouchAreas.add(pTouchArea);
	}

	public void registerPreFrameHandler(final IUpdateHandler pUpdateHandler) {
		this.mPreFrameHandlers.add(pUpdateHandler);
	}

	public void registerPostFrameHandler(final IUpdateHandler pUpdateHandler) {
		this.mPostFrameHandlers.add(pUpdateHandler);
	}

	public void unregisterTouchArea(final ITouchArea pTouchArea) {
		this.mTouchAreas.remove(pTouchArea);
	}

	public void unregisterPreFrameHandler(final IUpdateHandler pUpdateHandler) {
		this.mPreFrameHandlers.remove(pUpdateHandler);
	}

	public void unregisterPostFrameHandler(final IUpdateHandler pUpdateHandler) {
		this.mPostFrameHandlers.remove(pUpdateHandler);
	}

	public void setOnSceneTouchListener(final IOnSceneTouchListener pOnSceneTouchListener) {
		this.mOnSceneTouchListener = pOnSceneTouchListener;
	}

	public IOnSceneTouchListener getOnSceneTouchListener() {
		return this.mOnSceneTouchListener;
	}

	public boolean hasOnSceneTouchListener() {
		return this.mOnSceneTouchListener != null;
	}

	public void setOnAreaTouchListener(final IOnAreaTouchListener pOnAreaTouchListener) {
		this.mOnAreaTouchListener = pOnAreaTouchListener;
	}

	public IOnAreaTouchListener getOnAreaTouchListener() {
		return this.mOnAreaTouchListener;
	}

	public boolean hasOnAreaTouchListener() {
		return this.mOnAreaTouchListener != null;
	}

	private void setParentScene(final Scene pParentScene) {
		this.mParentScene = pParentScene;
	}

	public boolean hasChildScene() {
		return this.mChildScene != null;
	}

	public Scene getChildScene() {
		return this.mChildScene;
	}

	public void setChildSceneModal(final Scene pChildScene) {
		this.setChildScene(pChildScene, true, true);
	}

	public void setChildScene(final Scene pChildScene, final boolean pModalDraw, final boolean pModalUpdate) {
		pChildScene.setParentScene(this);
		this.mChildScene = pChildScene;
		this.mChildSceneModalDraw = pModalDraw;
		this.mChildSceneModalUpdate = pModalUpdate;
	}

	public void clearChildScene() {
		this.mChildScene = null;
	}

	public void setOnAreaTouchTraversalBackToFront() {
		this.mOnAreaTouchTraversalBackToFront = true;
	}

	public void setOnAreaTouchTraversalFrontToBack() {
		this.mOnAreaTouchTraversalBackToFront = false;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedDraw(final GL10 pGL) {
		final Scene childScene = this.mChildScene;
		if(childScene == null || !this.mChildSceneModalDraw) {
			this.drawBackground(pGL);
			this.drawLayers(pGL);
		}
		if(childScene != null) {
			childScene.onDraw(pGL);
		}
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		this.mSecondsElapsedTotal += pSecondsElapsed;

		final Scene childScene = this.mChildScene;
		if(childScene == null || !this.mChildSceneModalUpdate) {
			this.updateLayers(pSecondsElapsed);
		}

		if(childScene != null) {
			childScene.onUpdate(pSecondsElapsed);
		}
	}

	public boolean onSceneTouchEvent(final MotionEvent pSceneMotionEvent) {
		final Scene childScene = this.mChildScene;
		if(childScene == null) {
			final ArrayList<ITouchArea> touchAreas = this.mTouchAreas;
			final int touchAreaCount = touchAreas.size();
			if(touchAreaCount > 0) {
				final float sceneMotionEventX = pSceneMotionEvent.getX();
				final float sceneMotionEventY = pSceneMotionEvent.getY();
				if(this.mOnAreaTouchTraversalBackToFront) {
					for(int i = 0; i < touchAreaCount; i++) {
						final ITouchArea touchArea = touchAreas.get(i);
						if(touchArea.contains(sceneMotionEventX, sceneMotionEventY)) {
							final boolean handledSelf = touchArea.onAreaTouched(pSceneMotionEvent);
							if(handledSelf) {
								return true;
							} else if(this.mOnAreaTouchListener != null) {
								return this.mOnAreaTouchListener.onAreaTouched(touchArea, pSceneMotionEvent);
							} else {
								return false;
							}
						}
					}
				} else { /* Front to back. */
					for(int i = touchAreaCount - 1; i >= 0; i--) {
						final ITouchArea touchArea = touchAreas.get(i);
						if(touchArea.contains(sceneMotionEventX, sceneMotionEventY)) {
							final boolean handled = touchArea.onAreaTouched(pSceneMotionEvent);
							if(handled) {
								return true;
							} else if(this.mOnAreaTouchListener != null) {
								return this.mOnAreaTouchListener.onAreaTouched(touchArea, pSceneMotionEvent);
							} else {
								return false;
							}
						}
					}
				}
			}
			/* If no area was touched, the Scene itself was touched as a fallback. */
			if(this.mOnSceneTouchListener != null){
				return this.mOnSceneTouchListener.onSceneTouchEvent(this, pSceneMotionEvent);
			} else {
				return false;
			}
		} else {
			return this.onChildSceneTouchEvent(pSceneMotionEvent);
		}
	}

	protected boolean onChildSceneTouchEvent(final MotionEvent pSceneMotionEvent) {
		return this.mChildScene.onSceneTouchEvent(pSceneMotionEvent);
	}

	@Override
	public void reset() {
		super.reset();

		this.clearChildScene();

		final ILayer[] layers = this.mLayers;
		for(int i = layers.length - 1; i >= 0; i--) {
			layers[i].reset();
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void back() {
		this.clearChildScene();

		if(this.mParentScene != null) {
			this.mParentScene.clearChildScene();
			this.mParentScene = null;
		}
	}
	
	private void createLayers() {
		final ILayer[] layers = this.mLayers;
		for(int i = layers.length - 1; i >= 0; i--) {
			layers[i] = new DynamicCapacityLayer();
		}		
	}

	private void createLayers(final boolean pFixedCapacityLayers, final int[] pLayerCapacities) {
		final ILayer[] layers = this.mLayers;
		if(pFixedCapacityLayers) {
			for(int i = layers.length - 1; i >= 0; i--) {
				layers[i] = new FixedCapacityLayer(pLayerCapacities[i]);
			}
		} else {
			for(int i = layers.length - 1; i >= 0; i--) {
				layers[i] = new DynamicCapacityLayer(pLayerCapacities[i]);
			}		
		}
	}

	private void updateLayers(final float pSecondsElapsed) {
		final ILayer[] layers = this.mLayers;
		final int layerCount = layers.length;
		for(int i = 0; i < layerCount; i++) {
			layers[i].onUpdate(pSecondsElapsed);
		}
	}

	protected void drawBackground(final GL10 pGL) {
		if(this.mBackgroundEnabled) {
			pGL.glClearColor(this.mRed, this.mGreen, this.mBlue, this.mAlpha);
			pGL.glClear(GL10.GL_COLOR_BUFFER_BIT);
		}
	}

	private void drawLayers(final GL10 pGL) {
		final ILayer[] layers = this.mLayers;
		final int layerCount = layers.length;
		for(int i = 0; i < layerCount; i++) {
			layers[i].onDraw(pGL);
		}
	}

	public void updatePreFrameHandlers(final float pSecondsElapsed) {
		if(this.mChildScene == null || !this.mChildSceneModalUpdate) {
			this.mPreFrameHandlers.onUpdate(pSecondsElapsed);
		}

		if (this.mChildScene != null) {
			this.mChildScene.updatePreFrameHandlers(pSecondsElapsed);
		}
	}

	public void updatePostFrameHandlers(final float pSecondsElapsed) {
		if(this.mChildScene == null || !this.mChildSceneModalUpdate) {
			this.mPostFrameHandlers.onUpdate(pSecondsElapsed);
		}

		if (this.mChildScene != null) {
			this.mChildScene.updatePostFrameHandlers(pSecondsElapsed);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface ITouchArea {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public boolean contains(final float pX, final float pY);

		/**
		 * This method only fires if this {@link ITouchArea} is registered to the {@link Scene} via {@link Scene#registerTouchArea(ITouchArea)}.
		 * @param pSceneMotionEvent
		 * @return <code>true</code> if the event was handled (that means {@link IOnAreaTouchListener} of the {@link Scene} will not be fired!), otherwise <code>false</code>.
		 */
		public boolean onAreaTouched(final MotionEvent pSceneMotionEvent);
	}

	public static interface IOnAreaTouchListener {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public boolean onAreaTouched(final ITouchArea pTouchArea, final MotionEvent pSceneMotionEvent);
	}

	public static interface IOnSceneTouchListener {
		// ===========================================================
		// Final Fields
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public boolean onSceneTouchEvent(final Scene pScene, final MotionEvent pSceneMotionEvent);
	}
}
