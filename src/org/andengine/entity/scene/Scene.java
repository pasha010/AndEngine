package org.andengine.entity.scene;

import android.util.SparseArray;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.runnable.RunnableHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.IBackground;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.shape.Shape;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.Constants;
import org.andengine.util.adt.color.Color;
import org.andengine.util.adt.list.SmartList;

import java.util.Collections;
import java.util.Comparator;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich
 * @since 12:47:39 - 08.03.2010
 */
public class Scene extends Entity {
    private static  final   int                                 TOUCH_AREAS_CAPACITY_DEFAULT = 16;
    private         final   RunnableHandler                     mRunnableHandler;
    private         final   SparseArray<IEntity>                mTouchAreaBindings;
    private         final   SparseArray<IOnSceneTouchListener>  mOnSceneTouchListenerBindings;
    protected       final   SmartList<IEntity>                  touchAreas;
    private                 float                               mSecondsElapsedTotal;
    private                 boolean                             mChildSceneModalDraw;
    private                 boolean                             mChildSceneModalUpdate;
	private                 boolean                             mChildSceneModalTouch;
	private                 IOnSceneTouchListener               mOnSceneTouchListener;
	private                 IOnAreaTouchListener                mOnAreaTouchListener;
    private                 IBackground                         mBackground;
	private                 boolean                             mBackgroundEnabled;
	private                 boolean                             mOnAreaTouchTraversalBackToFront;
    private                 boolean                             mTouchAreaBindingOnActionDownEnabled;
    private                 boolean                             mTouchAreaBindingOnActionMoveEnabled;
    private                 boolean                             mOnSceneTouchListenerBindingOnActionDownEnabled;
    private                 IEntity                             selectedEntity;
    protected               Scene                               mParentScene;
    protected               Scene                               mChildScene;

	public Scene() {
        touchAreas                      = new SmartList<>(TOUCH_AREAS_CAPACITY_DEFAULT);
        mOnSceneTouchListenerBindings   = new SparseArray<>();
        mTouchAreaBindings              = new SparseArray<>();
        mRunnableHandler                = new RunnableHandler();
        mBackground                     = new Background(Color.BLACK);
        mBackgroundEnabled              = true;
        mOnAreaTouchTraversalBackToFront = true;
        mTouchAreaBindingOnActionDownEnabled = false;
        mTouchAreaBindingOnActionMoveEnabled = false;
        mOnSceneTouchListenerBindingOnActionDownEnabled = false;
    }

	@Deprecated
	public Scene(final int pChildCount) {
		for (int i = 0; i < pChildCount; i++) {
			this.attachChild(new Entity());
		}
        touchAreas = new SmartList<>(Scene.TOUCH_AREAS_CAPACITY_DEFAULT);
        mOnSceneTouchListenerBindings = new SparseArray<>();
        mTouchAreaBindings = new SparseArray<>();
        mRunnableHandler = new RunnableHandler();
        mBackground = new Background(Color.BLACK);
        mBackgroundEnabled = true;
        mOnAreaTouchTraversalBackToFront = true;
        mTouchAreaBindingOnActionDownEnabled = false;
        mTouchAreaBindingOnActionMoveEnabled = false;
        mOnSceneTouchListenerBindingOnActionDownEnabled = false;
    }

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public float getSecondsElapsedTotal() {
		return this.mSecondsElapsedTotal;
	}

	public IBackground getBackground() {
		return this.mBackground;
	}

	public void setBackground(final IBackground pBackground) {
		this.mBackground = pBackground;
	}

	public boolean isBackgroundEnabled() {
		return this.mBackgroundEnabled;
	}

	public void setBackgroundEnabled(final boolean pEnabled) {
		this.mBackgroundEnabled = pEnabled;
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
		this.setChildScene(pChildScene, true, true, true);
	}

	public void setChildScene(final Scene pChildScene) {
		this.setChildScene(pChildScene, false, false, false);
	}

	public void setChildScene(final Scene pChildScene, final boolean pModalDraw, final boolean pModalUpdate, final boolean pModalTouch) {
		pChildScene.setParentScene(this);
		this.mChildScene = pChildScene;
		this.mChildSceneModalDraw = pModalDraw;
		this.mChildSceneModalUpdate = pModalUpdate;
		this.mChildSceneModalTouch = pModalTouch;
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

	public boolean isTouchAreaBindingOnActionDownEnabled() {
		return this.mTouchAreaBindingOnActionDownEnabled;
	}

	public boolean isTouchAreaBindingOnActionMoveEnabled() {
		return this.mTouchAreaBindingOnActionMoveEnabled;
	}

	/**
	 * Enable or disable the binding of TouchAreas to PointerIDs (fingers).
	 * When enabled: TouchAreas get bound to a PointerID (finger) when returning true in
	 * {@link IShape#onAreaTouched(TouchEvent, float, float)} or
	 * {@link IOnAreaTouchListener#onAreaTouched(TouchEvent, IEntity, float, float)}
	 * with {@link TouchEvent#ACTION_DOWN}, they will receive all subsequent {@link TouchEvent}s
	 * that are made with the same PointerID (finger)
	 * <b>even if the {@link TouchEvent} is outside of the actual {@link IEntity}</b>!
	 *
	 * @param pTouchAreaBindingOnActionDownEnabled
	 */
	public void setTouchAreaBindingOnActionDownEnabled(final boolean pTouchAreaBindingOnActionDownEnabled) {
		if (this.mTouchAreaBindingOnActionDownEnabled && !pTouchAreaBindingOnActionDownEnabled) {
			this.mTouchAreaBindings.clear();
		}
		this.mTouchAreaBindingOnActionDownEnabled = pTouchAreaBindingOnActionDownEnabled;
	}

	/**
	 * Enable or disable the binding of TouchAreas to PointerIDs (fingers).
	 * When enabled: TouchAreas get bound to a PointerID (finger) when returning true in
	 * {@link IShape#onAreaTouched(TouchEvent, float, float)} or
	 * {@link IOnAreaTouchListener#onAreaTouched(TouchEvent, IEntity, float, float)}
	 * with {@link TouchEvent#ACTION_MOVE}, they will receive all subsequent {@link TouchEvent}s
	 * that are made with the same PointerID (finger)
	 * <b>even if the {@link TouchEvent} is outside of the actual {@link IEntity}</b>!
	 *
	 * @param pTouchAreaBindingOnActionMoveEnabled
	 */
	public void setTouchAreaBindingOnActionMoveEnabled(final boolean pTouchAreaBindingOnActionMoveEnabled) {
		if (this.mTouchAreaBindingOnActionMoveEnabled && !pTouchAreaBindingOnActionMoveEnabled) {
			this.mTouchAreaBindings.clear();
		}
		this.mTouchAreaBindingOnActionMoveEnabled = pTouchAreaBindingOnActionMoveEnabled;
	}

	public boolean isOnSceneTouchListenerBindingOnActionDownEnabled() {
		return this.mOnSceneTouchListenerBindingOnActionDownEnabled;
	}

	/**
	 * Enable or disable the binding of TouchAreas to PointerIDs (fingers).
	 * When enabled: The OnSceneTouchListener gets bound to a PointerID (finger) when returning true in
	 * {@link Shape#onAreaTouched(TouchEvent, float, float)} or
	 * {@link IOnAreaTouchListener#onAreaTouched(TouchEvent, IEntity, float, float)}
	 * with {@link TouchEvent#ACTION_DOWN}, it will receive all subsequent {@link TouchEvent}s
	 * that are made with the same PointerID (finger)
	 * <b>even if the {@link TouchEvent} is would belong to an overlaying {@link IEntity}</b>!
	 *
	 * @param pOnSceneTouchListenerBindingOnActionDownEnabled
	 */
	public void setOnSceneTouchListenerBindingOnActionDownEnabled(final boolean pOnSceneTouchListenerBindingOnActionDownEnabled) {
		if (this.mOnSceneTouchListenerBindingOnActionDownEnabled && !pOnSceneTouchListenerBindingOnActionDownEnabled) {
			this.mOnSceneTouchListenerBindings.clear();
		}
		this.mOnSceneTouchListenerBindingOnActionDownEnabled = pOnSceneTouchListenerBindingOnActionDownEnabled;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedDraw(final GLState pGLState, final Camera pCamera) {
		final Scene childScene = this.mChildScene;

		if (childScene == null || !this.mChildSceneModalDraw) {
			if (this.mBackgroundEnabled) {
				pGLState.pushProjectionGLMatrix();

				pCamera.onApplySceneBackgroundMatrix(pGLState);
				pGLState.loadModelViewGLMatrixIdentity();

				this.mBackground.onDraw(pGLState, pCamera);

				pGLState.popProjectionGLMatrix();
			}

			{
				pGLState.pushProjectionGLMatrix();

				this.onApplyMatrix(pGLState, pCamera);
				pGLState.loadModelViewGLMatrixIdentity();

				super.onManagedDraw(pGLState, pCamera);

				pGLState.popProjectionGLMatrix();
			}
		}

		if (childScene != null) {
			childScene.onDraw(pGLState, pCamera);
		}
	}

	protected void onApplyMatrix(final GLState pGLState, final Camera pCamera) {
		pCamera.onApplySceneMatrix(pGLState);
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		this.mSecondsElapsedTotal += pSecondsElapsed;

		this.mRunnableHandler.onUpdate(pSecondsElapsed);

		final Scene childScene = this.mChildScene;
		if (childScene == null || !this.mChildSceneModalUpdate) {
			this.mBackground.onUpdate(pSecondsElapsed);
			super.onManagedUpdate(pSecondsElapsed);
		}

		if (childScene != null) {
			childScene.onUpdate(pSecondsElapsed);
		}
	}

	public boolean onSceneTouchEvent(final TouchEvent pSceneTouchEvent) {
		final int action = pSceneTouchEvent.getAction();
		final boolean isActionDown = pSceneTouchEvent.isActionDown();
		final boolean isActionMove = pSceneTouchEvent.isActionMove();

        if (this.touchAreas.isEmpty()) {
            return true;
        }

		if (!isActionDown) {
			if (this.mOnSceneTouchListenerBindingOnActionDownEnabled) {
				final IOnSceneTouchListener boundOnSceneTouchListener = this.mOnSceneTouchListenerBindings.get(pSceneTouchEvent.getPointerID());
				if (boundOnSceneTouchListener != null) {
					/* Check if boundTouchArea needs to be removed. */
					switch (action) {
						case TouchEvent.ACTION_UP:
						case TouchEvent.ACTION_CANCEL:
							this.mOnSceneTouchListenerBindings.remove(pSceneTouchEvent.getPointerID());
					}
					final Boolean handled = this.mOnSceneTouchListener.onSceneTouchEvent(this, pSceneTouchEvent);
                    if (handled != null && handled) {
                        return true;
                    }
				}
			}
			if (this.mTouchAreaBindingOnActionDownEnabled) {
                final IEntity touchDownArea = this.mTouchAreaBindings.get(pSceneTouchEvent.getPointerID());
				/* In the case a ITouchArea has been bound to this PointerID,
				 * we'll pass this this TouchEvent to the same ITouchArea. */
				if (touchDownArea != null) {
					final float sceneTouchEventX = pSceneTouchEvent.getX();
					final float sceneTouchEventY = pSceneTouchEvent.getY();

					/* Check if boundTouchArea needs to be removed. */
					switch (action) {
						case TouchEvent.ACTION_UP:
                        case TouchEvent.ACTION_CANCEL:
                        case TouchEvent.ACTION_OUTSIDE:
							this.mTouchAreaBindings.remove(pSceneTouchEvent.getPointerID());
					}

                    if (!touchDownArea.contains(sceneTouchEventX, sceneTouchEventY)) {
                        pSceneTouchEvent.setAction(TouchEvent.ACTION_OUTSIDE);
                        this.onAreaTouchEvent(pSceneTouchEvent, sceneTouchEventX, sceneTouchEventY, touchDownArea);
                        selectedEntity = null;
                        return true;
                    } else {
                        if (selectedEntity != null) {
                            if (!selectedEntity.hasParent() || !selectedEntity.isVisible()) {
                                selectedEntity = null;
                            }
                        }
                        if (this.touchAreas != null) {
                            boolean containsTouchArea = false;
                            for (final IEntity touchArea : this.touchAreas) {
                                containsTouchArea = containsTouchArea || touchArea.contains(sceneTouchEventX, sceneTouchEventY);
                                if (touchArea.contains(sceneTouchEventX, sceneTouchEventY)) {
                                    if (pSceneTouchEvent.isActionUp() || pSceneTouchEvent.isActionMove()) {
                                        if (selectedEntity != null && !selectedEntity.equals(touchArea)) {
                                            pSceneTouchEvent.setAction(TouchEvent.ACTION_OUTSIDE);
                                            this.onAreaTouchEvent(pSceneTouchEvent, sceneTouchEventX, sceneTouchEventY, selectedEntity);
                                            selectedEntity = null;
                                            return true;
                                        }
                                        selectedEntity = null;
                                    }
                                }
                            }

                            if (!containsTouchArea) {
                                selectedEntity = null;
                                pSceneTouchEvent.setAction(TouchEvent.ACTION_OUTSIDE);
                            }
                        }

                        final Boolean handled = this.onAreaTouchEvent(pSceneTouchEvent, sceneTouchEventX, sceneTouchEventY, touchDownArea);
                        if (handled != null && handled) {
                            return true;
                        }
                    }
				}
			}
		}

		final Scene childScene = this.mChildScene;
		if (childScene != null) {
			final boolean handledByChild = this.onChildSceneTouchEvent(pSceneTouchEvent);
			if (handledByChild) {
				return true;
			} else if (this.mChildSceneModalTouch) {
				return false;
			}
		}
        final float sceneTouchEventX = pSceneTouchEvent.getX();
		final float sceneTouchEventY = pSceneTouchEvent.getY();

        synchronized (touchAreas) {
            if (touchAreas != null) {
                final int touchAreaCount = touchAreas.size();
                if (touchAreaCount > 0) {
                    if (this.mOnAreaTouchTraversalBackToFront) { /* Back to Front. */
                        for (final IEntity touchArea : touchAreas) {
                            if (touchArea.contains(sceneTouchEventX, sceneTouchEventY)) {
                                if (this.mTouchAreaBindingOnActionDownEnabled && isActionDown) {
                                    if (selectedEntity == null) {
                                        selectedEntity = touchArea;
                                    }
                                }

                                final Boolean handled = this.onAreaTouchEvent(pSceneTouchEvent, sceneTouchEventX, sceneTouchEventY, touchArea);
                                if (handled != null && handled) {
                                    /* If binding of ITouchAreas is enabled and this is an ACTION_DOWN event,
                                     * bind this ITouchArea to the PointerID. */
                                    if ( (this.mTouchAreaBindingOnActionDownEnabled && isActionDown)
                                      || (this.mTouchAreaBindingOnActionMoveEnabled && isActionMove)) {

                                        this.mTouchAreaBindings.put(pSceneTouchEvent.getPointerID(), touchArea);
                                    }
                                    return true;
                                }
                            }
                        }
                    } else { /* Front to back. */
                        for (int i = touchAreaCount - 1; i >= 0; i--) {
                            final IEntity touchArea = touchAreas.get(i);
                            if (touchArea.contains(sceneTouchEventX, sceneTouchEventY)) {
                                final Boolean handled = this.onAreaTouchEvent(pSceneTouchEvent, sceneTouchEventX, sceneTouchEventY, touchArea);
                                if (handled != null && handled) {
                                    /* If binding of ITouchAreas is enabled and this is an ACTION_DOWN event,
                                     * bind this ITouchArea to the PointerID. */
                                    if ((this.mTouchAreaBindingOnActionDownEnabled && isActionDown) || (this.mTouchAreaBindingOnActionMoveEnabled && isActionMove)) {
                                        this.mTouchAreaBindings.put(pSceneTouchEvent.getPointerID(), touchArea);
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
		/* If no area was touched, the Scene itself was touched as a fallback. */
		if (this.mOnSceneTouchListener != null) {
			final Boolean handled = this.mOnSceneTouchListener.onSceneTouchEvent(this, pSceneTouchEvent);
			if (handled != null && handled) {
				/* If binding of ITouchAreas is enabled and this is an ACTION_DOWN event,
				 * bind the active OnSceneTouchListener to the PointerID. */
				if (this.mOnSceneTouchListenerBindingOnActionDownEnabled && isActionDown) {
					this.mOnSceneTouchListenerBindings.put(pSceneTouchEvent.getPointerID(), this.mOnSceneTouchListener);
				}
                selectedEntity = null;
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	protected Boolean onAreaTouchEvent(final TouchEvent pSceneTouchEvent, final float sceneTouchEventX, final float sceneTouchEventY, final IEntity touchArea) {
		final float[] touchAreaLocalCoordinates = touchArea.convertSceneCoordinatesToLocalCoordinates(sceneTouchEventX, sceneTouchEventY);
		final float touchAreaLocalX = touchAreaLocalCoordinates[Constants.VERTEX_INDEX_X];
		final float touchAreaLocalY = touchAreaLocalCoordinates[Constants.VERTEX_INDEX_Y];

		final boolean handledSelf = touchArea.onAreaTouched(pSceneTouchEvent, touchAreaLocalX, touchAreaLocalY);
		if (handledSelf) {
			return Boolean.TRUE;
		} else if (this.mOnAreaTouchListener != null) {
			return this.mOnAreaTouchListener.onAreaTouched(pSceneTouchEvent, touchArea, touchAreaLocalX, touchAreaLocalY);
		} else {
			return null;
		}
	}

	protected boolean onChildSceneTouchEvent(final TouchEvent pSceneTouchEvent) {
		return this.mChildScene.onSceneTouchEvent(pSceneTouchEvent);
	}

	@Override
	public void reset() {
		super.reset();

		this.clearChildScene();
	}

	@Override
	public void setParent(final IEntity pEntity) {
//		super.setParent(pEntity);
	}

	public void postRunnable(final Runnable pRunnable) {
		this.mRunnableHandler.postRunnable(pRunnable);
	}

	public void registerTouchArea(final IEntity pTouchArea) {
        synchronized (touchAreas) {
            if (!this.touchAreas.contains(pTouchArea)) {
                this.touchAreas.add(pTouchArea);
                sortTouchAreas();
            }
        }
    }

    /**
     * sort touch areas by z-index
     * entity with biggest z-index - first in array
     * entity with smallest z-index - last in array
     */
    private void sortTouchAreas() {
        synchronized (touchAreas) {
            Collections.sort(touchAreas, new Comparator<IEntity>() {
                @Override
                public int compare(IEntity first, IEntity second) {
                    return second.getZIndex() - first.getZIndex();
                }
            });
        }
    }

    public boolean unregisterTouchArea(final IEntity pTouchArea) {
        synchronized (touchAreas) {
            return this.touchAreas.remove(pTouchArea);
        }
    }

	public void clearTouchAreas() {
        synchronized (this.touchAreas) {
            this.touchAreas.clear();
        }
    }

	public void back() {
		this.clearChildScene();

		if (this.mParentScene != null) {
			this.mParentScene.clearChildScene();
			this.mParentScene = null;
		}
	}
}
