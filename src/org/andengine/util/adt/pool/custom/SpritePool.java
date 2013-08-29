package org.andengine.util.adt.pool.custom;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.BaseTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.EntityPool;

public class SpritePool extends EntityPool<Sprite, BaseTextureRegion> {

    public SpritePool(VertexBufferObjectManager vertexBufferObjectManager) {
        super();
        this.vertexBufferObjectManager = vertexBufferObjectManager;
    }

    @Override
    protected Sprite onAllocatePoolItem() {
        return new Sprite(0, 0, texture, vertexBufferObjectManager);
    }

    public void setTexture(BaseTextureRegion texture) {
        if (texture == null) {
            throw new IllegalArgumentException(String.format( "trying to set non existing texture: %s"
                                                            , texture.getTextureName()));
        }
        this.texture = texture;
    }
}
