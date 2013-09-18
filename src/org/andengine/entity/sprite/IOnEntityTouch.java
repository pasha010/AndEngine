package org.andengine.entity.sprite;

import org.andengine.entity.IEntity;

/**
 * for touch events
 */
public interface IOnEntityTouch<E extends IEntity> {
    public boolean onTouchUp(E e);
    public boolean onTouchDown(E e);
    public boolean onTouchEnded(E e);
}
