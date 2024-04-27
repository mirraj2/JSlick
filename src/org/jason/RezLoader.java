package org.jason;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

import com.google.common.collect.Maps;

import ox.IO;

public class RezLoader {

  private static final Map<Class<?>, RezLoader> loaders = Maps.newHashMap();

  private final Map<String, BufferedImage> cache = new HashMap<>();
  private final Map<String, Image> cache2 = new HashMap<>();
  private final Map<String, Font> awtFontCache = Maps.newHashMap();
  private final Map<Font, TrueTypeFont> fontCache = Maps.newHashMap();

  private final Class<?> loader;

  private RezLoader(Class<?> loader) {
    this.loader = loader;
  }

  public TrueTypeFont getFont(String s, int style, float size) {
    Font font = getAwtFont(s);
    font = font.deriveFont(style, size);
    TrueTypeFont ret = fontCache.get(font);
    if (ret == null) {
      ret = new TrueTypeFont(font, true);
      fontCache.put(font, ret);
    }
    return ret;
  }

  private Font getAwtFont(String s) {
    Font ret = awtFontCache.get(s);
    if (ret == null) {
      ret = IO.from(loader, s).toFont();
      awtFontCache.put(s, ret);
    }
    return ret;
  }
  
  public BufferedImage getAWTImage(String s) {
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

  public Image getImage(String s) {
    Image ret = cache2.get(s);
    if (ret == null) {
      InputStream in = loader.getResourceAsStream(s);
      if (in == null) {
        throw new NullPointerException("Could not find image: " + s);
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

  public static RezLoader get(Class<?> loader) {
    RezLoader ret = loaders.get(loader);
    if (ret == null) {
      loaders.put(loader, ret = new RezLoader(loader));
    }
    return ret;
  }

}
