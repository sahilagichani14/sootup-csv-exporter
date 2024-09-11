package sootupexport;

import java.io.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import sootup.core.inputlocation.*;
import sootup.core.model.*;
import sootup.core.types.*;
import sootup.java.bytecode.inputlocation.*;
import sootup.java.core.*;
import sootup.java.core.views.*;

public class Main {
  // private static ConcurrentSet<Type> types;

  public static void main(String[] args) throws IOException {
    //Path path = FileSystems.getDefault().getPath(".", "test.apk");
    Path path = FileSystems.getDefault().getPath(".");
    AnalysisInputLocation inputLocation =
        new JavaClassPathAnalysisInputLocation(path.toString(), SourceType.Application);
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
