package org.andengine.util.texturepack;

import org.andengine.opengl.texture.ITexture;

class TexturePackTextureContainer {
    private final ITexture atlas;
    private final TexturePackTextureRegion.TexturePackTextureProperties properties;
    private TexturePackTextureRegion textureRegion;

    TexturePackTextureContainer(ITexture atlas, TexturePackTextureRegion.TexturePackTextureProperties properties) {
        this.atlas = atlas;
        this.properties = properties;
    }

    ITexture getAtlas() {
        return atlas;
    }

    TexturePackTextureRegion.TexturePackTextureProperties getProperties() {
        return properties;
    }

    TexturePackTextureRegion getTextureRegion() {
        if (textureRegion == null) {
            textureRegion = new TexturePackTextureRegion(this);
        }
        return textureRegion;
    }
}
