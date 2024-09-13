package sootupexport;

import sootup.apk.frontend.ApkAnalysisInputLocation;
import sootup.interceptors.BytecodeBodyInterceptors;
import sootup.java.core.JavaSootClass;
import sootup.java.core.views.JavaView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

public class Main {
  // private static ConcurrentSet<Type> types;

  public static void main(String[] args) throws IOException {
    // Path path = FileSystems.getDefault().getPath(".", "test.apk");
    Path path = Paths.get("backflash.apk");
    ApkAnalysisInputLocation inputLocation =
        new ApkAnalysisInputLocation(path, "", BytecodeBodyInterceptors.Default.getBodyInterceptors());
    JavaView view = new JavaView(inputLocation);
    Collection<JavaSootClass> viewClasses = view.getClasses().toList();

    File dir = new File("facts");
    dir.mkdirs();
    Database db = new Database("facts");
    Representation rep = new Representation();
    FactWriter writer = new FactWriter(db, rep);
    FactGenerator factgen = new FactGenerator(writer, viewClasses);
    factgen.run();
    db.flush();
    db.close();
  }

  // private static void addType(Type ty) {
  //   if (types.add(ty)) {
  //     System.out.println(ty);
  //   }
  // }
}
