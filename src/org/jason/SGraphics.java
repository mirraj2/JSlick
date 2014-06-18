package org.jason;

import java.awt.Rectangle;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;

public class SGraphics {

  private final Graphics g;

  private Rectangle clip = new Rectangle(0, 0, 0, 0);
  private Color imageFilter = Color.white;

  public SGraphics(Graphics g) {
    this.g = g;
    g.setAntiAlias(true);
  }

  public SGraphics color(Color c) {
    g.setColor(c);
    return this;
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

  public SGraphics fill(Shape shape) {
    g.fill(shape);
    return this;
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
    g.fillRect((float) x, (float) y, (float) w, (float) h);
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

  public SGraphics draw(Image image, double x, double y, double w, double h) {
    return draw(image, x, y, w, h, 0, 0, image.getWidth(), image.getHeight());
  }

  public SGraphics draw(Image image, double x, double y, double srcX, double srcY, double w,
      double h) {
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
    g.drawString(text, (float) x, (float) y);
    return this;
  }

  public SGraphics textCentered(String text, double w, double h) {
    return textCentered(text, 0, 0, w, h);
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

  public SGraphics destroy() {
    g.destroy();
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
