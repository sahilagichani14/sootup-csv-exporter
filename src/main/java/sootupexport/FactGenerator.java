package sootupexport;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import sootup.core.inputlocation.*;
import sootup.core.model.*;
import sootup.core.types.*;
import sootup.java.bytecode.inputlocation.*;
import sootup.java.core.*;
import sootup.java.core.views.*;

public class FactGenerator {
  protected FactWriter _writer;
  protected Collection<? extends JavaSootClass> _classes;
  public static final AtomicInteger methodsWithoutActiveBodies = new AtomicInteger(0);

  public FactGenerator(FactWriter writer, Collection<? extends JavaSootClass> classes) {
    this._writer = writer;
    this._classes = classes;
  }

  public void run() {
    _classes.stream().forEach(this::generate);
  }

  private void generate(JavaSootClass cls) {
    _writer.writeClassOrInterfaceType(cls);
    cls.getFields().stream().forEach(this::generate);
    for (JavaSootMethod m : cls.getMethods()) {
      SessionCounter session = new SessionCounter();
      generate(m, session);
    }
  }

  private void generate(JavaSootMethod m, SessionCounter session) {
    String methodId = _writer.writeMethod(m);

    if (!m.isStatic()) {
      _writer.writeThisVar(methodId, m.getDeclaringClassType());
    }

    for (int i = 0; i < m.getParameterCount(); i++)
      _writer.writeFormalParam(methodId, m.getParameterType(i), i);

    if (!(m.isAbstract() || m.isNative())) {
      // if (!m.hasBody()) {
      //   m.getBody();
      //   methodsWithoutActiveBodies.incrementAndGet();
      // }

      Body b0 = m.getBody();
      // try {
      //   if (b0 != null) {
      //     Body b = b0;
      //     // if (_ssa) {
      //     //   b = Shimple.v().newBody(b);
      //     //   m.setActiveBody(b);
      //     // }
      //     // DoopRenamer.transform(b);
      //     generate(m, b, session);
      //     // If the Shimple body is not needed anymore, put
      //     // back original body. This saves some memory.
      //     // if (sootParameters._lowMem && !sootParameters._generateJimple) m.setActiveBody(b0);
      //   }
      // } catch (RuntimeException ex) {
      //   System.err.println("Fact generation failed for method " + m.getSignature() + ".");
      //   ex.printStackTrace();
      //   throw ex;
      // }
    }
  }

  private void generate(JavaSootMethod m, Body body, SessionCounter session) {}

  private void generate(JavaSootField fld) {
    System.out.println(fld.toString());
  }
}
