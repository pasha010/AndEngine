package org.andengine.util.texturepack;

import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 23:23:47 - 30.07.2011
 */
public class TexturePack {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ITexture mTexture;
	private final TexturePackTextureRegionLibrary mTexturePackTextureRegionLibrary;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TexturePack(final ITexture pTexture, final TexturePackTextureRegionLibrary pTexturePackTextureRegionLibrary) {
		this.mTexture = pTexture;
		this.mTexturePackTextureRegionLibrary = pTexturePackTextureRegionLibrary;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ITexture getTexture() {
		return this.mTexture;
	}

	public TexturePackTextureRegionLibrary getTexturePackTextureRegionLibrary() {
		return this.mTexturePackTextureRegionLibrary;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	public void loadTexture() {
        if (mTexture instanceof BuildableBitmapTextureAtlas) {
            try {
                ((BuildableBitmapTextureAtlas) mTexture).build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
            } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
                e.printStackTrace();
            }
        }
		this.mTexture.load();
	}

	public void unloadTexture() {
		this.mTexture.unload();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}