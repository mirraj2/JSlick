package org.jason;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ImageLoader {

  private final Map<String, BufferedImage> cache = new HashMap<>();
  private final Map<String, Image> cache2 = new HashMap<>();

  private final Class<?> loader;

  public ImageLoader(Class<?> loader) {
    this.loader = loader;
  }

  public BufferedImage getImage(String s) {
    BufferedImage ret = cache.get(s);
    if (ret == null) {
      try {
        ret = ImageIO.read(loader.getResource(s));
        cache.put(s, ret);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return ret;
  }

  public Image getSlickImage(String s) {
    Image ret = cache2.get(s);
    if (ret == null) {
      InputStream in = loader.getResourceAsStream(s);
      if (in == null) {
        throw new NullPointerException("Could not find image: rez/" + s);
      }
      try {
        ret = new Image(in, s, false);
        cache2.put(s, ret);
      } catch (SlickException e) {
        throw new RuntimeException(e);
      }
    }
    return ret;
  }

}
