package sootupexport;

import java.util.regex.Pattern;
import sootup.core.jimple.visitor.*;
import sootup.core.types.ClassType;

public class TypeNameVisitor extends AbstractTypeVisitor {
  private static final Pattern dotPat = Pattern.compile("\\.");
  protected StringBuilder result = null;

  TypeNameVisitor() {
    this.result = new StringBuilder();
  }

  @Override
  public void caseBooleanType() {
    result.append("Z");
  }

  public void caseArrayType() {
    result.append("[");
  }

  public void caseByteType() {
    result.append("B");
  }

  public void caseCharType() {
    result.append("C");
  }

  public void caseDoubleType() {
    result.append("D");
  }

  public void caseFloatType() {
    result.append("F");
  }

  public void caseIntType() {
    result.append("I");
  }

  public void caseLongType() {
    result.append("J");
  }

  public void caseNullType() {
    result.append("null");
  }

  public void caseShortType() {
    result.append("S");
  }

  public void caseVoidType() {
    result.append("V");
  }

  public void caseClassType(ClassType t) {
    result.append("L" + dotPat.matcher(t.toString()).replaceAll("/") + ";");
  }

  public String getResult() {
    return result.toString();
  }
}
