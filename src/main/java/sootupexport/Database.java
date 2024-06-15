package sootupexport;

import java.io.*;
import java.util.*;

public class Database implements Closeable, Flushable {
  private static final char SEP = '\t';
  private static final char EOL = '\n';

  String directory;
  protected final Map<PredicateFile, Writer> _writers;
  protected boolean everCleared;

  public Database(String directory) throws IOException {
    this.directory = directory;
    _writers = new EnumMap<>(PredicateFile.class);
    for (PredicateFile predicateFile : EnumSet.allOf(PredicateFile.class)) {
      _writers.put(predicateFile, predicateFile.getWriter(new File(directory), ".facts", false));
    }
    everCleared = false;
  }

  @Override
  public void close() throws IOException {
    if (_writers != null) for (Writer w : _writers.values()) w.close();
  }

  @Override
  public void flush() throws IOException {
    if (_writers != null) for (Writer w : _writers.values()) w.flush();
  }

  public void writeFile(
      Collection<List<String>> contents, PredicateFile predicateFile, String directory)
      throws IOException {
    Writer w = predicateFile.getWriter(new File(directory), ".facts", everCleared);
    for (List<String> record : contents) {
      writeRecord(w, record);
    }
    w.flush();
    w.close();
  }

  // public void writeFacts(String directory) throws IOException {
  //  final int k = Runtime.getRuntime().availableProcessors() * 2 - 1;
  //  ExecutorService es = Executors.newFixedThreadPool(k);
  //  for (PredicateFile predicateFile : EnumSet.allOf(PredicateFile.class)) {
  //    es.execute(
  //        () -> {
  //          try {
  //            writeFile(this.contents.get(predicateFile), predicateFile, directory);
  //          } catch (Exception e) {
  //            // System.out.println(e);
  //            ghidra.util.Msg.info(this, e);
  //            for (java.lang.StackTraceElement st : e.getStackTrace()) {
  //              ghidra.util.Msg.info(this, st);
  //            }
  //          }
  //        });
  //  }

  //  es.shutdown();
  //  try {
  //    while (!es.awaitTermination(30, TimeUnit.SECONDS)) ;
  //  } catch (Exception e) {
  //    // System.out.println(e);
  //    ghidra.util.Msg.info(this, e);
  //    for (java.lang.StackTraceElement st : e.getStackTrace()) {
  //      ghidra.util.Msg.info(this, st);
  //    }
  //  }

  //  this.clear();
  // }

  private void writeRecord(Writer writer, List<String> record) throws IOException {
    boolean first = true;
    for (String col : record) {
      if (!first) {
        writer.write(SEP);
      }
      writeColumn(writer, col);
      first = false;
    }
    writer.write(EOL);
  }

  private void writeColumn(Writer writer, String column) throws IOException {
    // Quote some special characters.
    final char QUOTE = '\"';
    final char SLASH = '\\';
    final char TABCH = '\t';
    for (int i = 0; i < column.length(); i++) {
      char c = column.charAt(i);
      switch (c) {
        case QUOTE:
          writer.write("'");
          break;
        case SLASH:
          writer.write('\\');
          writer.write('\\');
          break;
        case EOL:
          writer.write('\\');
          writer.write('n');
          break;
        case TABCH:
          writer.write('\\');
          writer.write('t');
          break;
        default:
          writer.write(c);
      }
    }
  }

  private String addColumn(String column) {
    // Quote some special characters.
    final char SLASH = '\"';
    final char EOL = '\n';
    final char TAB = '\t';
    if ((column.indexOf(SLASH) >= 0) || (column.indexOf(EOL) >= 0) || (column.indexOf(TAB) >= 0)) {
      // Assume at most 5 special characters will be rewritten
      // before updating the capacity.
      StringBuilder sb = new StringBuilder(column.length() + 5);
      for (char c : column.toCharArray())
        switch (c) {
          case SLASH:
            sb.append("\\\\\"");
            break;
          case EOL:
            sb.append("\\\\n");
            break;
          case TAB:
            sb.append("\\\\t");
            break;
          default:
            sb.append(c);
        }
      return sb.toString();
    } else return column;
  }

  // public void add(PredicateFile predicateFile, String... args) {
  //   this.contents.get(predicateFile).add(Arrays.asList(args));
  // }

  public void add(PredicateFile predicateFile, String arg, String... args) {
    if (_writers == null) return;
    try {
      // Estimate StringBuilder capacity.
      int capacity = args.length + 1;
      for (String arg0 : args) capacity += arg0.length();

      StringBuilder line = new StringBuilder(capacity);
      line.append(addColumn(arg));
      for (String col : args) {
        line.append(SEP);
        line.append(addColumn(col));
      }
      line.append(EOL);
      String strLine = line.toString();
      Writer writer = _writers.get(predicateFile);
      synchronized (predicateFile) {
        writer.write(strLine);
      }
    } catch (IOException exc) {
      throw new RuntimeException(exc);
    }
  }

  // public void merge(Database other) {
  //   for (Map.Entry<PredicateFile, Collection<List<String>>> entry : other.contents.entrySet()) {
  //     this.contents.get(entry.getKey()).addAll(entry.getValue());
  //   }
  // }

  // public void clear() {
  //   for (PredicateFile predicateFile : EnumSet.allOf(PredicateFile.class)) {
  //     this.contents.get(predicateFile).clear();
  //   }
  //   everCleared = true;
  // }
}
