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

  private static final Map<String, BufferedImage> cache = new HashMap<>();
  private static final Map<String, Image> cache2 = new HashMap<>();

  private static Class<?> loader;

  public static void setLoader(Class<?> loader) {
    ImageLoader.loader = loader;
  }

  public static BufferedImage getImage(String s) {
    BufferedImage ret = cache.get(s);
    if (ret == null) {
      try {
        ret = ImageIO.read(loader.getResource("rez/" + s));
        cache.put(s, ret);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return ret;
  }

  public static Image getSlickImage(String s) {
    Image ret = cache2.get(s);
    if (ret == null) {
      InputStream in = loader.getResourceAsStream("rez/" + s);
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
