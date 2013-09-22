package org.andengine.entity.scene;

import org.andengine.entity.IEntity;
import org.andengine.input.touch.TouchEvent;

/**
 * An interface for a callback to be invoked when a {@link TouchEvent} is
 * dispatched to an {@link IEntity} area. The callback will be invoked
 * before the {@link TouchEvent} is passed to the {@link IEntity}.
 *
 * (c) 2012 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 15:01:48 PM - 27.03.2012
 */
public interface IOnAreaTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * Called when a {@link TouchEvent} is dispatched to an {@link IEntity}. This allows
	 * listeners to get a chance to respond before the target {@link IEntity#onAreaTouched(TouchEvent, float, float)} is called.
	 *
	 * @param pTouchArea The {@link IEntity} that the {@link TouchEvent} has been dispatched to.
	 * @param pSceneTouchEvent The {@link TouchEvent} object containing full information about the event.
	 * @param pTouchAreaLocalX the x coordinate within the area touched.
	 * @param pTouchAreaLocalY the y coordinate within the area touched.
	 *
	 * @return <code>true</code> if this {@link IOnAreaTouchListener} has consumed the {@link TouchEvent}, <code>false</code> otherwise.
	 */
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final IEntity pTouchArea, final float pTouchAreaLocalX, final float pTouchAreaLocalY);
}