package sootupexport;

import java.util.regex.Pattern;
import sootup.core.jimple.visitor.*;
import sootup.core.types.ClassType;

public class TypeNameVisitor extends AbstractTypeVisitor<String> {
  private static final Pattern dotPat = Pattern.compile("\\.");

  public void caseBooleanType() {
    setResult("Z");
  }

  public void caseArrayType() {
    setResult("[");
  }

  public void caseByteType() {
    setResult("B");
  }

  public void caseCharType() {
    setResult("C");
  }

  public void caseDoubleType() {
    setResult("D");
  }

  public void caseFloatType() {
    setResult("F");
  }

  public void caseIntType() {
    setResult("I");
  }

  public void caseLongType() {
    setResult("J");
  }

  public void caseNullType() {
    setResult("null");
  }

  public void caseShortType() {
    setResult("S");
  }

  public void caseVoidType() {
    setResult("V");
  }

  public void caseClassType(ClassType t) {
    setResult("L" + dotPat.matcher(t.toString()).replaceAll("/") + ";");
  }
}
