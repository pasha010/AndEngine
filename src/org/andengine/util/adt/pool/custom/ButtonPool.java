package org.andengine.util.adt.pool.custom;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.texture.region.BaseTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.pool.EntityPool;

public class ButtonPool extends EntityPool<ButtonSprite, BaseTextureRegion> {
    private BaseTextureRegion texturePressed;

    public ButtonPool(VertexBufferObjectManager vertexBufferObjectManager) {
        super();
        this.vertexBufferObjectManager = vertexBufferObjectManager;
    }

    @Override
    protected ButtonSprite onAllocatePoolItem() {
        ButtonSprite sprite = new ButtonSprite(0, 0, texture, vertexBufferObjectManager);
        sprite.setUserData(this);
        return sprite;
    }

    public void setTextureNormal(BaseTextureRegion textureNormal) {
        this.texture = textureNormal;
        this.texturePressed = textureNormal;
    }

    public void setTexturePressed(BaseTextureRegion textureNormal) {
        this.texturePressed = textureNormal;
    }
}
