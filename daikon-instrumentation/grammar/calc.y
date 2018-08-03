/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright (C) 2001 Gerwin Klein <lsf@jflex.de>                          *
 * All rights reserved.                                                    *
 *                                                                         *
 * This is a modified version of the example from                          *
 *   http://www.lincom-asg.com/~rjamison/byacc/                            *
 *                                                                         *
 * Thanks to Larry Bell and Bob Jamison for suggestions and comments.      *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

%{

import daikon.parser.structure.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
%}

%token SEP /* a text */
%token <sval> TEXT /* a text */
%token <sval> ARRAYVAL /* a text */
%token <sval> STR /* a text */
%token <sval> MOD /* a text */
%token ENTER /* a text */
%token <sval> EXIT /* a text */
%token CLASS /* a text */
%token OBJECT /* a text */



%token SIZE /* a text */
%token ORIG /* a text */


/* OPs */

%token COMMA /* a text */
%token IF /* a text */
%token IFF /* a text */
%token EQUAL /* a text */
%token NEQUAL /* a text */
%token L /* a text */
%token LEQ /* a text */
%token GEQ /* a text */
%token G /* a text */
%token ONEOF /* a text */
%token ELEMS /* a text */

%left '-' '+'
%left '*' '/'

%type <obj> status
%type <obj> single
%type <obj> pars
%type <obj> arrays
%type <obj> op

%%

start : /* nothing */ {}
      | SEP first invs start

first: TEXT '(' pars ')' status { lastRule = "first-1st rule";
    MethodInvariants m = new MethodInvariants(isClass, isObj, isEnter, isExit, exitLine, $1, (List)$3);
    m.setFileLine(lexer.line);
    lm.add(m);
    this.lastInv = m;
}
     | TEXT status { lastRule = "first-2nd rule";
        MethodInvariants m = new MethodInvariants(isClass, isObj, isEnter, isExit, exitLine, $1, new ArrayList<>());
        m.setFileLine(lexer.line);
        lm.add(m);
        this.lastInv = m;
}

status: ENTER  {  isClass = false;
                  isObj   = false;
                  isEnter = true;
                  isExit  = false;
                  exitLine = 0;
               }
      | EXIT   {  isClass = false;
                  isObj   = false;
                  isEnter = false;
                  isExit  = true;
                  exitLine = 0;
                  String s = $1;
                  s = s.substring(7);
                  if(s.length()>0)
                    exitLine = Integer.parseInt(s);
              }
      | CLASS  {  isClass = true;
                  isObj   = false;
                  isEnter = false;
                  isExit  = false;
                  exitLine = 0;
              }
      | OBJECT {  isClass = false;
                  isObj   = true;
                  isEnter = false;
                  isExit  = false;
                  exitLine = 0;
              }

pars: /* nothing */     {       lastRule = "pars-empty";
                                $$ = new ArrayList<>();
                        }
    | ARRAYVAL          {
                                lastRule = "pars-array";
                                List<String> out = new ArrayList<>();
                                out.add($1);
                                $$ = out;
                        }
    | TEXT              {       lastRule = "pars-text";
                                List<String> out = new ArrayList<>();
                                out.add($1);
                                $$ = out;
                        }
    | STR              {        lastRule = "pars-str";
                                List<String> out = new ArrayList<>();
                                out.add($1);
                                $$ = out;
                        }
    | TEXT COMMA pars   {   lastRule = "pars-txt-comma";
                            List<String> out = new ArrayList<>();
                            out.add($1);
                            out.addAll((List)$3);
                            $$ = out;
                        }
    | STR COMMA pars   {    lastRule = "pars-str-comma";
                            List<String> out = new ArrayList<>();
                            out.add($1);
                            out.addAll((List)$3);
                            $$ = out;
                        }
    | ARRAYVAL COMMA pars { lastRule = "pars-array-comma";
                            List<String> out = new ArrayList<>();
                            out.add($1);
                            out.addAll((List)$3);
                            $$ = out;
                        }

invs: /* nothing */ { lastRule = "inv-empty"; }
    | invs single {
            lastRule = "inv-single";
            this.lastInv.add((Invariant) $2);
    }

single: TEXT op TEXT                  { $$ = new Check($1, $3, (Invariant.OP)$2); }
      | TEXT ONEOF arrays             { $$ = new OneOf($1, (List) $3); }
      | ORIG '(' TEXT ')' op TEXT     { $$ = new LOrig($3, (Invariant.OP)$5, $6); }
      | ORIG '(' TEXT ')' op ARRAYVAL { $$ = new LOrig($3, (Invariant.OP)$5, $6); }
      | TEXT op ORIG '(' TEXT ')'   { $$ = new ROrig($1, (Invariant.OP)$2, $5); }
      | TEXT EQUAL MOD              { $$ = new Mod($1, $3); }
      | '(' single ')' IF '(' single ')'    { $$ = new Implication((Invariant)$2, (Invariant)$6); }
      | '(' single ')' IFF '(' single ')'    { $$ = new Implication((Invariant)$2, (Invariant)$6); }
      | SIZE '(' TEXT ')' op TEXT           { $$ = new Size($3, $6, (Invariant.OP)$5); }
      | TEXT ELEMS op TEXT                  { /* ignore */ }
      | SIZE '(' TEXT ')' ONEOF arrays      { /* ignore */ }
      | TEXT ELEMS ONEOF arrays             { $$ = new ArrayVals($1,(List) $4); }
      | ORIG '(' TEXT ')' ONEOF arrays      { $$ = new LOrigArrayVals($3, (List) $6); }
      /* ignore list of invs */
      | TEXT TEXT TEXT TEXT TEXT op TEXT { /* ignore corner invs */}
      | TEXT '-' ORIG '(' TEXT ')' '-' TEXT op TEXT { /* ignore corner invs */}
      | TEXT '-' ORIG '(' TEXT ')' '+' TEXT op TEXT { /* ignore corner invs */}
      | TEXT '%' TEXT op TEXT { /* ignore corner invs */}
      | ORIG '(' TEXT ')' '%' TEXT op TEXT { /* ignore corner invs */}
      | TEXT '%' ORIG '(' TEXT ')' op TEXT { /* ignore corner invs */}
      | SIZE '(' TEXT ')' '-' TEXT op TEXT { /* ignore corner invs */}
      | TEXT EQUAL ARRAYVAL          {   }

arrays: '{' pars '}' { $$ = $2; }

op: L       { $$ = Invariant.OP.L; }
  | LEQ     { $$ = Invariant.OP.LE; }
  | GEQ     { $$ = Invariant.OP.GE; }
  | G       { $$ = Invariant.OP.G; }
  | EQUAL   { $$ = Invariant.OP.EQ; }
  | NEQUAL  { $$ = Invariant.OP.NEQ; }


%%



  private boolean isClass = false;
  private boolean isObj   = false;
  private boolean isEnter = false;
  private boolean isExit  = false;
  private int exitLine = 0;
  private MethodInvariants lastInv;

  private List<MethodInvariants> lm = new ArrayList<>();

  //easy debug
  private String lastRule = "";


  private Yylex lexer;
  private Path filename;

  private int yylex () {
    int yyl_return = -1;
    try {
      yylval = new ParserVal(0);
      yyl_return = lexer.yylex();
    }
    catch (IOException e) {
      System.err.println("IO error :"+e);
    }
    return yyl_return;
  }

  public void yyerror (String error) {
    String buffer = lexer.getBuffer(5);
    String msg = String.format("[%s]\n[%d] Error: %s\nBuffer:\n%s", this.filename.toFile().getPath(), lexer.line, error, buffer);
    throw new RuntimeException(msg);
  }


  public Parser(Reader r) {
      lexer = new Yylex(r, this);
  }

  public Parser(Reader r, boolean debugMe) {
        yydebug=debugMe;
        lexer = new Yylex(r, this);
  }

  public List<MethodInvariants> calculate(String input){
    InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
    lexer = new Yylex(new InputStreamReader(stream), this);
    lexer.filename = this.filename;
    lm.clear();
    this.run();
    return lm;
  }

  public List<MethodInvariants> calculate(Path file) throws IOException {
      this.filename = file;
      List<String> lines = Files.readAllLines(file);
      StringBuilder cnt = new StringBuilder();
      for(String line : lines) {
          if(line.contains("()") && !line.contains("():::"))
              cnt.append("\n");
          else if(line.contains(":::EXIT") && line.contains(";condition="))
              cnt.append(line.substring(0, line.indexOf(";"))).append("\n");
          else if(line.contains(":::EXIT") || line.contains(":::ENTER"))
              cnt.append(line).append("\n");
          else if(line.contains("has only one value"))
              cnt.append("\n");
          else if(line.contains("is the reverse of"))
              cnt.append("\n");
          else if(line.contains("$hidden$"))
              cnt.append("\n");
          else if(line.contains("elementwise"))
              cnt.append("\n");
          else if(line.contains(" elements "))
              cnt.append("\n");
          else if(line.contains("sorted by"))
              cnt.append("\n");
          else if(line.contains("size"))
              cnt.append("\n");
          else if(line.contains("(lexically)"))
              cnt.append("\n");
          else
              cnt.append(line).append("\n");
      }
      return this.calculate(cnt.toString());
  }