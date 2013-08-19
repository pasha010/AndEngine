package org.andengine.util.texturepack;

import android.util.SparseArray;

import java.util.HashMap;

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

	private final SparseArray<TexturePackTextureContainer> mIDMapping;
	private final HashMap<String, TexturePackTextureContainer> mSourceMapping;

	// ===========================================================
	// Constructors
	// ===========================================================

	public TexturePackTextureRegionLibrary(final int pInitialCapacity) {
		this.mIDMapping = new SparseArray<TexturePackTextureContainer>(pInitialCapacity);
		this.mSourceMapping = new HashMap<String, TexturePackTextureContainer>(pInitialCapacity);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public SparseArray<TexturePackTextureContainer> getIDMapping() {
		return this.mIDMapping;
	}

	public HashMap<String, TexturePackTextureContainer> getSourceMapping() {
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

		this.mIDMapping.put(pTexturePackTextureRegion.getTextureRegion().getID(), pTexturePackTextureRegion);
		this.mSourceMapping.put(pTexturePackTextureRegion.getTextureRegion().getSource(), pTexturePackTextureRegion);
	}

	public void remove(final int pID) {
		this.mIDMapping.remove(pID);
	}

	public TexturePackTextureRegion get(final int pID) {
        TexturePackTextureContainer container = this.mIDMapping.get(pID);
        return container == null ? null : container.getTextureRegion();
	}

	public TexturePackTextureRegion get(final String pSource) {
        TexturePackTextureContainer container = this.mSourceMapping.get(pSource);
        return container == null ? null : container.getTextureRegion();
	}

	public TexturePackTextureRegion get(final String pSource, final boolean pStripExtension) {
		if (pStripExtension) {
			final int indexOfExtension = pSource.lastIndexOf('.');
			if (indexOfExtension == -1) {
				return this.get(pSource);
			} else {
				final String stripped = pSource.substring(0, indexOfExtension);
				return this.mSourceMapping.get(stripped).getTextureRegion();
			}
		} else {
			return this.get(pSource);
		}
	}

	private void throwOnCollision(final TexturePackTextureContainer pTexturePackTextureRegion) throws IllegalArgumentException {
		if (this.mIDMapping.get(pTexturePackTextureRegion.getTextureRegion().getID()) != null) {
			throw new IllegalArgumentException("Collision with ID: '" + pTexturePackTextureRegion.getTextureRegion().getID() + "'.");
		} else if (this.mSourceMapping.get(pTexturePackTextureRegion.getTextureRegion().getSource()) != null) {
			throw new IllegalArgumentException("Collision with Source: '" + pTexturePackTextureRegion.getTextureRegion().getSource() + "'.");
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
