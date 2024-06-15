package sootupexport;

import java.util.Map;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import sootup.core.inputlocation.*;
import sootup.core.model.*;
import sootup.core.signatures.*;
import sootup.core.types.*;
import sootup.java.bytecode.inputlocation.*;
import sootup.java.core.*;
import sootup.java.core.views.*;

public class Representation {

  private static final Pattern qPat = Pattern.compile("'");
  private static final Pattern dotPat = Pattern.compile("\\.");

  private final Map<JavaSootMethod, String> _methodSigRepr = new ConcurrentHashMap<>();

  String signature(JavaSootMethod m) {
    String result = _methodSigRepr.get(m);
    if (result == null) {
      MethodSignature sig = m.getSignature();
      String raw = name(sig.getDeclClassType()) + "." + simpleName(m) + ":" + descriptor(m);
      result = stripQuotes(raw);
      _methodSigRepr.put(m, result);
    }

    return result;
  }

  public String simpleName(JavaSootMethod m) {
    return m.getSignature().getName();
  }

  public String descriptor(JavaSootMethod m) {
    MethodSignature sig = m.getSignature();
    return "("
        + (sig.getParameterTypes().stream().map(this::name).collect(Collectors.joining()))
        + ")"
        + name(sig.getType());
  }

  public String thisVar(String methodId) {
    return methodId + "/@this";
  }

  public String param(String methodId, int i) {
    return methodId + "/@parameter" + i;
  }

  public String name(Type t) {
    TypeNameVisitor v = new TypeNameVisitor();
    String result = new String();
    while (t instanceof ArrayType) {
      t.accept(v);
      result += v.getResult();
      t = ((ArrayType) t).getBaseType();
    }

    t.accept(v);
    result += v.getResult();
    // String raw = t.toString();
    // if (Type.isObject(t)) {
    //   return "L" + dotPat.matcher(raw).replaceAll("/") + ";";
    // } else {
    //   return raw;
    // }
    return result;
  }

  public static String stripQuotes(CharSequence s) {
    return qPat.matcher(s).replaceAll("");
  }
}
