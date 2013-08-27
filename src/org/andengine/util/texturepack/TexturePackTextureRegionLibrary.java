package org.andengine.util.texturepack;

import java.util.concurrent.ConcurrentHashMap;

/**
 * (c) 2011 Zynga Inc.
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:34:23 - 15.08.2011
 */
public class TexturePackTextureRegionLibrary {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final ConcurrentHashMap<String, TexturePackTextureContainer> mSourceMapping;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TexturePackTextureRegionLibrary(final int pInitialCapacity) {
		this.mSourceMapping = new ConcurrentHashMap<>(pInitialCapacity);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public ConcurrentHashMap<String, TexturePackTextureContainer> getSourceMapping() {
		return this.mSourceMapping;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	public void put(final TexturePackTextureContainer pTexturePackTextureRegion) {
		this.throwOnCollision(pTexturePackTextureRegion);

		this.mSourceMapping.put(pTexturePackTextureRegion.getProperties().getSource(), pTexturePackTextureRegion);
	}

	public TexturePackTextureRegion get(final String pSource) {
        TexturePackTextureContainer container = this.mSourceMapping.get(pSource);
        return    container == null
                ? null
                : container.getTextureRegion();
	}

	private void throwOnCollision(final TexturePackTextureContainer pTexturePackTextureRegion) throws IllegalArgumentException {
        String source = pTexturePackTextureRegion.getProperties().getSource();
        if (this.mSourceMapping.get(source) != null) {
			throw new IllegalArgumentException("Collision with Source: '" + source + "'.");
		}
	}

    public void unload() {
        this.mSourceMapping.clear();
    }


    // ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
