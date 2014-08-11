package libs;

import jasonlib.IO;
import jasonlib.OS;
import java.io.File;
import org.apache.log4j.Logger;

public class Natives {

  private static final Logger logger = Logger.getLogger(Natives.class);

  public static void setupNativeLibs() {
    if (OS.type == OS.OS_Type.WINDOWS) {
      extract("lwjgl64.dll", "OpenAL64.dll");
    } else if (OS.type == OS.OS_Type.MAC) {
      extract("liblwjgl.dylib", "openal.dylib");
    } else {
      throw new RuntimeException("Don't support: " + OS.type);
    }

  }

  private static void extract(String... natives) {
    File dir = new File(OS.getTemporaryFolder(), "natives");
    dir.mkdirs();

    for (String n : natives) {
      File target = new File(dir, n);
      if (!target.exists()) {
        logger.debug("Extracting " + n + " to " + target);
        IO.from(Natives.class, n).to(target);
      }
    }

    System.setProperty("org.lwjgl.librarypath", dir.getPath());
  }

}
