package org.andengine.util.adt.pool.custom;

import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.opengl.texture.region.BaseTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.EntityPool;

public class MenuItemPool extends EntityPool<SpriteMenuItem, BaseTextureRegion> {
    private int id;

    public MenuItemPool(VertexBufferObjectManager vertexBufferObjectManager) {
        super();
        this.vertexBufferObjectManager = vertexBufferObjectManager;
    }

    @Override
    protected void onHandleRecycleItem(SpriteMenuItem pItem) {
        super.onHandleRecycleItem(pItem);
        pItem.detachSelf();
        pItem.detachChildren();
    }

    @Override
    protected SpriteMenuItem onAllocatePoolItem() {
        return new SpriteMenuItem(id, texture, vertexBufferObjectManager);
    }

    public void setTexture(BaseTextureRegion texture) {
        this.texture = texture;
    }

    public void setId(int id) {
        this.id = id;
    }
}
