package libs;

import java.io.File;

import ox.IO;
import ox.Log;
import ox.OS;

public class Natives {

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
        Log.debug("Extracting " + n + " to " + target);
        IO.from(Natives.class, n).to(target);
      }
    }

    System.setProperty("org.lwjgl.librarypath", dir.getPath());
  }

}
