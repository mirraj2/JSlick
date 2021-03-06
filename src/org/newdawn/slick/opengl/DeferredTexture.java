package org.newdawn.slick.opengl;

import java.io.IOException;
import java.io.InputStream;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import com.google.common.base.Throwables;

/**
 * A texture proxy that can be used to load a texture at a later date while still
 * allowing elements to reference it
 *
 * @author kevin
 */
public class DeferredTexture extends TextureImpl implements DeferredResource {
    /** The stream to read the texture from */
    private InputStream in;
    /** The name of the resource to load */
    private String resourceName;
    /** True if the image should be flipped */
    private boolean flipped;
    /** The filter to apply to the texture */
    private int filter;
    /** The texture we're proxying for */
    private TextureImpl target;
    /** The color to be transparent */
    private int[] trans;
    
    /**
     * Create a new deferred texture
     * 
     * @param in The input stream from which to read the texture
     * @param resourceName The name to give the resource
     * @param flipped True if the image should be flipped
     * @param filter The filter to apply
     * @param trans The colour to defined as transparent
     */
    public DeferredTexture(InputStream in, String resourceName, boolean flipped, int filter, int[] trans) {
        this.in = in;
        this.resourceName = resourceName;
        this.flipped = flipped;
        this.filter = filter;
        this.trans = trans;
        
        LoadingList.get().add(this);
    }

    /**
     * @see org.newdawn.slick.loading.DeferredResource#load()
     */
    @Override
  public void load() throws IOException {
    try {
      in.available();
    } catch (Exception e) {
      throw new RuntimeException("The stream was closed before the deferred loading could happen! resource = " + resourceName);
    }

        boolean before = InternalTextureLoader.get().isDeferredLoading();
        InternalTextureLoader.get().setDeferredLoading(false);
        try{
        target = InternalTextureLoader.get().getTexture(in, resourceName, flipped, filter, trans);
        }
        catch(Exception e){
      System.err.println("DefferedTexture: Problem loading texture: " + resourceName);
          throw Throwables.propagate(e);
        }
        InternalTextureLoader.get().setDeferredLoading(before);

    in.close();
    }
    
    /**
     * Check if the target has been obtained already
     */
    private void checkTarget() {
        if (target == null) {
            try {
                load();
                LoadingList.get().remove(this);
                return;
            } catch (IOException e) {
                throw new RuntimeException("Attempt to use deferred texture before loading and resource not found: "+resourceName);
            }
        }
    }
    
    /**
     * @see org.newdawn.slick.opengl.TextureImpl#bind()
     */
    @Override
  public void bind() {
        checkTarget();

        target.bind();
    }

    /**
     * @see org.newdawn.slick.opengl.TextureImpl#getHeight()
     */
    @Override
  public float getHeight() {
        checkTarget();

        return target.getHeight();
    }

    /**
     * @see org.newdawn.slick.opengl.TextureImpl#getImageHeight()
     */
    @Override
  public int getImageHeight() {
        checkTarget();
        return target.getImageHeight();
    }

    /**
     * @see org.newdawn.slick.opengl.TextureImpl#getImageWidth()
     */
    @Override
  public int getImageWidth() {
        checkTarget();
        return target.getImageWidth();
    }

    /**
     * @see org.newdawn.slick.opengl.TextureImpl#getTextureHeight()
     */
    @Override
  public int getTextureHeight() {
        checkTarget();
        return target.getTextureHeight();
    }

    /**
     * @see org.newdawn.slick.opengl.TextureImpl#getTextureID()
     */
    @Override
  public int getTextureID() {
        checkTarget();
        return target.getTextureID();
    }

    /**
     * @see org.newdawn.slick.opengl.TextureImpl#getTextureRef()
     */
    @Override
  public String getTextureRef() {
        checkTarget();
        return target.getTextureRef();
    }

    /**
     * @see org.newdawn.slick.opengl.TextureImpl#getTextureWidth()
     */
    @Override
  public int getTextureWidth() {
        checkTarget();
        return target.getTextureWidth();
    }

    /**
     * @see org.newdawn.slick.opengl.TextureImpl#getWidth()
     */
    @Override
  public float getWidth() {
        checkTarget();
        return target.getWidth();
    }

    /**
     * @see org.newdawn.slick.opengl.TextureImpl#release()
     */
    @Override
  public void release() {
        checkTarget();
        target.release();
    }

    /**
     * @see org.newdawn.slick.opengl.TextureImpl#setAlpha(boolean)
     */
    @Override
  public void setAlpha(boolean alpha) {
        checkTarget();
        target.setAlpha(alpha);
    }

    /**
     * @see org.newdawn.slick.opengl.TextureImpl#setHeight(int)
     */
    @Override
  public void setHeight(int height) {
        checkTarget();
        target.setHeight(height);
    }

    /**
     * @see org.newdawn.slick.opengl.TextureImpl#setTextureHeight(int)
     */
    @Override
  public void setTextureHeight(int texHeight) {
        checkTarget();
        target.setTextureHeight(texHeight);
    }

    /**
     * @see org.newdawn.slick.opengl.TextureImpl#setTextureID(int)
     */
    @Override
  public void setTextureID(int textureID) {
        checkTarget();
        target.setTextureID(textureID);
    }

    /**
     * @see org.newdawn.slick.opengl.TextureImpl#setTextureWidth(int)
     */
    @Override
  public void setTextureWidth(int texWidth) {
        checkTarget();
        target.setTextureWidth(texWidth);
    }

    /**
     * @see org.newdawn.slick.opengl.TextureImpl#setWidth(int)
     */
    @Override
  public void setWidth(int width) {
        checkTarget();
        target.setWidth(width);
    }
    
    /**
     * @see org.newdawn.slick.opengl.TextureImpl#getTextureData()
     */
    @Override
  public byte[] getTextureData() {
        checkTarget();
        return target.getTextureData();
    }

    /**
     * @see org.newdawn.slick.loading.DeferredResource#getDescription()
     */
    @Override
  public String getDescription() {
        return resourceName;
    }
    
    /**
     * @see org.newdawn.slick.opengl.Texture#hasAlpha()
     */
    @Override
    public boolean hasAlpha() {
        checkTarget();
        return target.hasAlpha();
    }
    
    /**
     * @see org.newdawn.slick.opengl.Texture#setTextureFilter(int)
     */
    @Override
  public void setTextureFilter(int textureFilter) {
        checkTarget();
        target.setTextureFilter(textureFilter);
    }
}
