package org.jason;

import java.awt.Rectangle;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.RoundedRectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.ShapeRenderer;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

public class SGraphics {

  private final Graphics g;

  private Rectangle clip = new Rectangle(0, 0, 0, 0);
  private Color imageFilter = Color.white;

  public SGraphics(Graphics g) {
    this.g = g;
    antialias(true);
  }

  public SGraphics color(Color c) {
    g.setColor(c);
    return this;
  }

  public SGraphics color(float r, float g, float b, float a) {
    return color(new Color(r, g, b, a));
  }

  public SGraphics imageFilter(Color imageFilterColor) {
    if (imageFilterColor == null) {
      imageFilterColor = Color.white;
    }
    this.imageFilter = imageFilterColor;
    return this;
  }

  public SGraphics draw(Shape shape) {
    g.draw(shape);
    return this;
  }

  public SGraphics draw(Rect r) {
    g.drawRect(r.x(), r.y(), r.w(), r.h());
    return this;
  }

  public SGraphics fill(Shape shape) {
    boolean antialias = g.isAntiAlias();
    antialias(false);
    g.fill(shape);
    antialias(antialias);
    return this;
  }

  public SGraphics fill(Rect r) {
    return fillRect(r.x, r.y, r.w, r.h);
  }

  public SGraphics draw(double x, double y, double w, double h) {
    if (clipMiss(x, y, w, h)) {
      return this;
    }
    g.drawRect((float) x, (float) y, (float) w, (float) h);
    return this;
  }

  public SGraphics fillRect(double x, double y, double w, double h) {
    if (clipMiss(x, y, w, h)) {
      return this;
    }
    boolean antialias = g.isAntiAlias();
    antialias(false);
    g.fillRect((float) x, (float) y, (float) w, (float) h);
    antialias(antialias);
    return this;
  }

  public SGraphics drawRoundRect(double x, double y, double w, double h, int cornerRadius) {
    if (clipMiss(x, y, w, h)) {
      return this;
    }
    boolean antialias = g.isAntiAlias();
    antialias(false);
    ShapeRenderer.draw(new RoundedRectangle((float) x, (float) y, (float) w, (float) h, cornerRadius));
    antialias(antialias);
    return this;
  }

  public SGraphics fillCircle(double x, double y, double radius) {
    return fillOval((float) (x - radius), (float) (y - radius), (float) (radius * 2),
        (float) (radius * 2));
  }

  public SGraphics fillOval(double x, double y, double w, double h) {
    if (clipMiss(x, y, w, h)) {
      return this;
    }
    g.fillOval((float) x, (float) y, (float) w, (float) h);
    return this;
  }

  public SGraphics translate(double x, double y) {
    g.translate((float) -x, (float) -y);
    this.clip.x += x;
    this.clip.y += y;
    return this;
  }

  public SGraphics rotate(double r, double x, double y) {
    g.rotate((float) x, (float) y, (float) Math.toDegrees(r));
    return this;
  }

  public SGraphics draw(Image image, double x, double y) {
    return draw(image, x, y, image.getWidth(), image.getHeight());
  }

  public SGraphics draw(Image image, Rect r) {
    return draw(image, r.x, r.y, r.w, r.h);
  }

  public SGraphics draw(Image image, double x, double y, double w, double h) {
    return draw(image, x, y, w, h, 0, 0, image.getWidth(), image.getHeight());
  }

  public SGraphics draw(Image image, double x, double y, double srcX, double srcY, double w, double h) {
    return draw(image, x, y, w, h, srcX, srcY, w, h);
  }

  public SGraphics draw(Image image, double x, double y, double w, double h, double srcX, double srcY, double srcW,
      double srcH) {
    if (clipMiss(x, y, w, h)) {
      return this;
    }
    boolean old = g.isAntiAlias();
    g.setAntiAlias(false);
    g.drawImage(image, (float) x, (float) y, (float) (x + w), (float) (y + h), (float) srcX,
        (float) srcY, (float) (srcX + srcW), (float) (srcY + srcH), imageFilter);
    g.setAntiAlias(old);
    return this;
  }

  public SGraphics line(double lineWidth, double x, double y, double xx, double yy) {
    if (clipMiss(Math.min(x, xx), Math.min(y, yy), Math.abs(x - xx), Math.abs(y - yy))) {
      return this;
    }

    float oldWidth = g.getLineWidth();
    g.setLineWidth((float) lineWidth);
    g.drawLine((float) x, (float) y, (float) xx, (float) yy);
    g.setLineWidth(oldWidth);
    return this;
  }

  public SGraphics lineWidth(double lineWidth) {
    g.setLineWidth((float) lineWidth);
    return this;
  }

  public SGraphics zoom(double zoom) {
    g.scale((float) zoom, (float) zoom);
    return this;
  }

  public SGraphics clip(double w, double h) {
    this.clip.width = (int) w;
    this.clip.height = (int) h;
    return this;
  }

  public SGraphics text(String text, double x, double y) {
    boolean oldAlias = g.isAntiAlias();
    antialias(false);
    g.drawString(text, (float) x, (float) y);
    antialias(oldAlias);
    return this;
  }

  public SGraphics textCentered(String text, double w, double h) {
    return textCentered(text, 0, 0, w, h);
  }

  public SGraphics textCentered(String text, Rect r) {
    return textCentered(text, r.x, r.y, r.w, r.h);
  }

  public SGraphics textCentered(String text, double x, double y, double w, double h) {
    Font font = g.getFont();
    int sw = font.getWidth(text);
    int sh = font.getHeight(text);

    return text(text, x + w / 2 - sw / 2, y + h / 2 - sh / 2);
  }

  public SGraphics font(Font font) {
    g.setFont(font);
    return this;
  }

  public SGraphics resetFont() {
    g.resetFont();
    return this;
  }

  public SGraphics push() {
    g.pushTransform();
    return this;
  }

  public SGraphics pop() {
    g.popTransform();
    clip.x = 0;
    clip.y = 0;
    clip.width = 0;
    clip.height = 0;
    return this;
  }

  public boolean clipMiss(double x, double y, double w, double h) {
    if (clip.width == 0) {
      return false;
    }
    if (x > clip.getMaxX() || y > clip.getMaxY()) {
      return true;
    }
    if (x + w < clip.x || y + h < clip.y) {
      return true;
    }
    return false;
  }

  public SGraphics centerCameraOn(double x, double y, int screenWidth, int screenHeight) {
    translate(x - screenWidth / 2, y - screenHeight / 2);
    return this;
  }

  public SGraphics addMode() {
    return drawMode(Graphics.MODE_ADD);
  }

  public SGraphics screenMode() {
    return drawMode(Graphics.MODE_SCREEN);
  }

  public SGraphics normalMode() {
    return drawMode(Graphics.MODE_NORMAL);
  }

  public SGraphics drawMode(int drawMode) {
    g.setDrawMode(drawMode);
    return this;
  }

  public int textWrapped(String text, Rect r, double margin) {
    return textWrapped(text, r, margin, true);
  }

  public int getNumRows(String text, double width, double margin) {
    return textWrapped(text, new Rect(0, 0, width, 0), margin, false);
  }

  public Color getImageFilter() {
    return imageFilter;
  }

  /**
   * Returns the number of rows used.
   */
  private int textWrapped(String text, Rect r, double margin, boolean render) {
    org.newdawn.slick.Font font = g.getFont();

    double x = r.x + margin;
    double y = r.y + margin * .6;
    int textHeight = font.getLineHeight();
    int spaceWidth = font.getWidth("_");
    spaceWidth--;

    List<String> words = ImmutableList.copyOf(Splitter.on(' ').split(text));
    int i = 0;
    int numRows = 1;

    while (i < words.size()) {
      String word = words.get(i++);
      int w = font.getWidth(word);
      if (x + w > r.maxX() - margin) {
        numRows++;
        x = r.x + margin;
        y += textHeight;
      }
      if (render) {
        this.text(word, x, y);
      }
      x += w + spaceWidth;
    }

    return numRows;
  }

  public SGraphics destroy() {
    g.destroy();
    return this;
  }

  public Font getFont() {
    return g.getFont();
  }

  public SGraphics antialias(boolean b) {
    g.setAntiAlias(b);
    return this;
  }

  public static SGraphics create(Graphics g) {
    return new SGraphics(g);
  }

  public static SGraphics create(Image im) {
    try {
      return new SGraphics(im.getGraphics());
    } catch (SlickException e) {
      throw new RuntimeException(e);
    }
  }

}
