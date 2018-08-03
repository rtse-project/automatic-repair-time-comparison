package daikon.parser;

import java.nio.file.Path;
%%

%byaccj

%{
  private Parser yyparser;

  public int line = 1;
  public boolean debug = true;
  public Path filename;

  public Yylex(java.io.Reader r, Parser yyparser) {
    this(r);
    this.yyparser = yyparser;
  }

  public String getBuffer(){
      return new String(this.zzBuffer, this.zzCurrentPos, this.zzEndRead-this.zzStartRead);
  }
  public String getBuffer(int lookaheadLines){
      if(lookaheadLines < 1)
        lookaheadLines = 1;
      int start = zzCurrentPos;
      while(lookaheadLines != 0){
        if(this.zzBuffer[start] == '\n')
          lookaheadLines--;
        start++;
        if(this.zzBuffer.length <= start)
          lookaheadLines = 0;
      }
      return new String(this.zzBuffer, this.zzCurrentPos, start-this.zzCurrentPos);
    }
%}

NL  = \n | \r | \r\n
TEXT = \"?[aA-zZ0-9\-][aA-zZ0-9\.\-\$]*\"?
STR = \"([^\"\\]|\\.)*\"
ARR = \[([^\[]|\\.)+\]
MOD = [0-9]+\s*\(mod\s+[0-9]+\)
EXIT = :::EXIT([0-9]+)?
SEP = "==========================================================================="

%%

"{" |
"}" |
"[" |
"]" |
"(" |
")" { return (int) yycharat(0); }

"-" |
"+" |
"%" { return (int) yycharat(0); }

","    { return Parser.COMMA; }
"<==>" { return Parser.IFF; }
"==>"  { return Parser.IF; }
"=="   { return Parser.EQUAL; }
"!="   { return Parser.NEQUAL; }
">"    { return Parser.G; }
">="   { return Parser.GEQ; }
"<="   { return Parser.LEQ; }
"<"    { return Parser.L; }

":::ENTER"  {return Parser.ENTER; }
{EXIT}      {yyparser.yylval = new ParserVal(yytext());return Parser.EXIT; }
":::CLASS"  {return Parser.CLASS; }
":::OBJECT" {return Parser.OBJECT; }


"one of" { return Parser.ONEOF; }
"elements" { return Parser.ELEMS; }

"size" { return Parser.SIZE; }
"orig" { return Parser.ORIG; }


/* newline */
{NL}   { line++; }

{TEXT} {
        yyparser.yylval = new ParserVal(yytext());
        return Parser.TEXT;
}

{ARR}  {
            yyparser.yylval = new ParserVal(yytext());
            return Parser.ARRAYVAL;
        }

{STR} {
        yyparser.yylval = new ParserVal(yytext());
        return Parser.TEXT;
}

{MOD} {
        yyparser.yylval = new ParserVal(yytext());
        return Parser.MOD;
}

{SEP} {
        return Parser.SEP;
}

/* whitespace */
[ \t]+ { }

/* error fallback */
[^]    { throw new RuntimeException("[" + this.filename.toFile().getPath() + "]\n[" + line + "] Error: unexpected character '"+yytext()+"'");  }
