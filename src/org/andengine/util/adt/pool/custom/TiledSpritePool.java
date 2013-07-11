package org.andengine.util.adt.pool.custom;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.EntityPool;

public class TiledSpritePool extends EntityPool<TiledSprite, TiledTextureRegion> {
    private VertexBufferObjectManager   vertexBufferObjectManager;

    public TiledSpritePool(VertexBufferObjectManager vertexBufferObjectManager) {
        super();
        this.vertexBufferObjectManager = vertexBufferObjectManager;
    }

    @Override
    protected void onHandleRecycleItem(TiledSprite pItem) {
        // do nothing (from mheater)
    }

    @Override
    protected void onHandleObtainItem(TiledSprite pItem) {
        super.onHandleObtainItem(pItem);
        pItem.setCurrentTileIndex(0);
    }

    @Override
    protected TiledSprite onAllocatePoolItem() {
        return new TiledSprite(0, 0, texture, vertexBufferObjectManager);
    }

    public void setTexture(TiledTextureRegion texture) {
        this.texture = texture;
    }

}