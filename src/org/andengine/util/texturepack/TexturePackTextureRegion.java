package org.andengine.util.texturepack;

import org.andengine.opengl.texture.region.TextureRegion;

/**
 *
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:28:48 - 15.08.2011
 */
public class TexturePackTextureRegion extends TextureRegion {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

    private final TexturePackTextureProperties texturePackTextureProperties;


    // ===========================================================
	// Constructors
	// ===========================================================

    protected TexturePackTextureRegion(TexturePackTextureContainer container) {
        super(container.getAtlas(), container.getProperties().getX(), container.getProperties().getY(), container.getProperties().getWidth(), container.getProperties().getHeight(), container.getProperties().isRotated());
        this.texturePackTextureProperties = container.getProperties();
        this.setTextureName(texturePackTextureProperties.getSource());
    }

    // ===========================================================
	// Getter & Setter
	// ===========================================================

	public int getID() {
        return texturePackTextureProperties.getID();
    }

	public String getSource() {
        return texturePackTextureProperties.getSource();
    }

	public boolean isTrimmed() {
        return texturePackTextureProperties.isTrimmed();
    }

	public int getSourceX() {
        return texturePackTextureProperties.getSourceX();
    }

	public int getSourceY() {
        return texturePackTextureProperties.getSourceY();
    }

	public int getSourceWidth() {
        return texturePackTextureProperties.getSourceWidth();
    }

	public int getSourceHeight() {
        return texturePackTextureProperties.getSourceHeight();
    }

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

    static class TexturePackTextureProperties {
        final int x;
        final int y;
        final int width;
        final int height;
        final int mID;
        final String mSource;
        final boolean isRotated;
        final boolean mTrimmed;
        final int mSourceX;
        final int mSourceY;
        final int mSourceWidth;
        final int mSourceHeight;

        public TexturePackTextureProperties(int x, int y, int width, int height, int mID, String mSource, boolean isRotated, boolean mTrimmed, int mSourceX, int mSourceY, int mSourceWidth, int mSourceHeight) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.mID = mID;
            this.mSource = mSource;
            this.isRotated = isRotated;
            this.mTrimmed = mTrimmed;
            this.mSourceX = mSourceX;
            this.mSourceY = mSourceY;
            this.mSourceWidth = mSourceWidth;
            this.mSourceHeight = mSourceHeight;
        }

        int getX() {
            return x;
        }

        int getY() {
            return y;
        }

        int getWidth() {
            return width;
        }

        int getHeight() {
            return height;
        }

        boolean ismTrimmed() {
            return mTrimmed;
        }

        public int getID() {
            return this.mID;
        }

        public String getSource() {
            return this.mSource;
        }

        public boolean isTrimmed() {
            return this.mTrimmed;
        }

        public int getSourceX() {
            return this.mSourceX;
        }

        public int getSourceY() {
            return this.mSourceY;
        }

        public int getSourceWidth() {
            return this.mSourceWidth;
        }

        public int getSourceHeight() {
            return this.mSourceHeight;
        }

        public boolean isRotated() {
            return isRotated;
        }
    }
}
