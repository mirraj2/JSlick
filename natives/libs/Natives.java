package libs;

import jasonlib.IO;
import jasonlib.OS;
import java.io.File;
import org.apache.log4j.Logger;

public class Natives {

  private static final Logger logger = Logger.getLogger(Natives.class);
  
  public static void setupNativeLibs() {
    File dir = new File(OS.getTemporaryFolder(), "natives");
    dir.mkdirs();

    if (OS.type == OS.OS_Type.WINDOWS) {
      logger.debug("Extract Windows Natives");
      IO.from(Natives.class, "lwjgl64.dll").to(new File(dir, "lwjgl64.dll"));
      IO.from(Natives.class, "OpenAL64.dll").to(new File(dir, "OpenAL64.dll"));
    } else if (OS.type == OS.OS_Type.MAC) {
      logger.debug("Extract Mac Natives");
      IO.from(Natives.class, "liblwjgl.dylib").to(new File(dir, "liblwjgl.dylib"));
      IO.from(Natives.class, "openal.dylib").to(new File(dir, "openal.dylib"));
    } else {
      throw new RuntimeException("Don't support: " + OS.type);
    }

    System.setProperty("org.lwjgl.librarypath", dir.getPath());
  }

}
