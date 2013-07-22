package org.andengine.util.adt.pool;

import org.andengine.entity.Entity;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public abstract class EntityPool<E extends Entity, T extends ITextureRegion> extends GenericPool<E> {
    protected T                         texture;
    protected VertexBufferObjectManager vertexBufferObjectManager;

    @Override
    protected void onHandleRecycleItem(final E pItem) {
        pItem.detachChildren();
    }

    @Override
    public synchronized E obtainPoolItem() {
        E item = super.obtainPoolItem();
        item.resetEntityProperties();
        return item;
    }
}
