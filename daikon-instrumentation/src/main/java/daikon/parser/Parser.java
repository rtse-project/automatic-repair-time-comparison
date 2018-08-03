//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";



package daikon.parser;



//#line 15 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"

import daikon.parser.structure.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
//#line 27 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 10000;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short SEP=257;
public final static short TEXT=258;
public final static short ARRAYVAL=259;
public final static short STR=260;
public final static short MOD=261;
public final static short ENTER=262;
public final static short EXIT=263;
public final static short CLASS=264;
public final static short OBJECT=265;
public final static short SIZE=266;
public final static short ORIG=267;
public final static short COMMA=268;
public final static short IF=269;
public final static short IFF=270;
public final static short EQUAL=271;
public final static short NEQUAL=272;
public final static short L=273;
public final static short LEQ=274;
public final static short GEQ=275;
public final static short G=276;
public final static short ONEOF=277;
public final static short ELEMS=278;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    6,    6,    1,    1,    1,    1,    3,    3,
    3,    3,    3,    3,    3,    7,    7,    2,    2,    2,
    2,    2,    2,    2,    2,    2,    2,    2,    2,    2,
    2,    2,    2,    2,    2,    2,    2,    2,    4,    5,
    5,    5,    5,    5,    5,
};
final static short yylen[] = {                            2,
    0,    4,    5,    2,    1,    1,    1,    1,    0,    1,
    1,    1,    3,    3,    3,    0,    2,    3,    3,    6,
    6,    6,    3,    7,    7,    6,    4,    6,    4,    6,
    7,   10,   10,    5,    8,    8,    8,    3,    3,    1,
    1,    1,    1,    1,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,   16,    5,    6,    7,    8,    0,    4,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    2,
   17,    0,    0,    0,    0,    0,    0,   45,   40,   41,
   42,   43,    0,    0,    0,    0,    0,    0,    0,    0,
   13,   15,   14,    3,    0,   38,   23,    0,   19,   44,
    0,    0,    0,    0,    0,   18,    0,    0,    0,    0,
    0,    0,   29,   27,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   39,    0,   34,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   22,
   28,    0,   26,   30,    0,   20,   21,    0,    0,   31,
    0,    0,    0,    0,    0,   24,   25,    0,    0,   36,
   37,   35,    0,    0,   32,   33,
};
final static short yydgoto[] = {                          2,
   10,   21,   15,   49,   37,    4,   11,
};
final static short yysindex[] = {                      -215,
 -240,    0,   -8,    0,    0,    0,    0,    0, -231,    0,
   -7, -253, -211, -207,   25,  -37,   27,   28,   -5,    0,
    0, -231, -231, -231, -243, -189, -225,    0,    0,    0,
    0,    0,  -53, -227, -196, -244, -242, -186, -183,   35,
    0,    0,    0,    0, -181,    0,    0, -231,    0,    0,
  -53, -178,   41, -220,   42,    0,   43,   44,   47, -239,
 -174,  -34,    0,    0, -166, -165, -164, -163,  -44,  -28,
   56,   57, -220,    0,   58,    0,   59,   60,  -53, -160,
 -156,  -53, -155, -195,   -5,   -5, -154,   15, -220,    0,
    0, -220,    0,    0, -220,    0,    0,   64,   65,    0,
 -151, -150, -149, -148, -147,    0,    0, -220, -220,    0,
    0,    0, -146, -145,    0,    0,
};
final static short yyrindex[] = {                       114,
    0,    0,    0,    0,    0,    0,    0,    0,   74,    0,
  114,  -39,  -38,  -36,    0,    0,    0,    0,    0,    0,
    0,  -35,  -35,  -35,    0,    0, -241,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   -9,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                       106,
   93,  -12,  -11,  -41,  -30,    0,    0,
};
final static int YYTABLESIZE=262;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         36,
   80,   11,   10,   52,   12,    9,   40,   35,   83,   63,
   41,   42,   43,   54,   22,   56,   44,    3,    5,    6,
    7,    8,   55,   66,   57,   44,   12,   13,   14,   71,
   72,    9,   19,   46,   19,   47,   62,   91,   81,   84,
   94,    1,   87,   50,   28,   29,   30,   31,   32,   51,
   50,   28,   29,   30,   31,   32,   23,  102,  103,  101,
   24,  104,   96,   97,  105,   25,   38,   39,   45,   48,
   53,   58,   98,   99,   59,   60,   61,  113,  114,   64,
   65,   67,   68,   73,   69,   11,   10,   70,   12,    9,
   74,   75,   76,   77,   78,   85,   86,   92,   88,   89,
   90,   93,   95,  100,  106,  107,  108,  109,  110,  111,
  112,  115,  116,    1,    9,    9,   20,   44,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   26,    0,    0,    0,    0,    0,   50,   28,   29,   30,
   31,   32,   79,   27,   28,   29,   30,   31,   32,   33,
   34,    0,   50,   28,   29,   30,   31,   32,   82,    1,
   16,    0,   16,    5,    6,    7,    8,    0,   17,   18,
   17,   18,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         37,
   45,   41,   41,   34,   41,   41,   19,   45,   37,   51,
   22,   23,   24,  258,  268,  258,  258,  258,  262,  263,
  264,  265,  267,   54,  267,  267,  258,  259,  260,  269,
  270,   40,   40,  259,   40,  261,   48,   79,   69,   70,
   82,  257,   73,  271,  272,  273,  274,  275,  276,  277,
  271,  272,  273,  274,  275,  276,  268,   43,   89,   45,
  268,   92,  258,  259,   95,   41,   40,   40,  258,  123,
  267,  258,   85,   86,  258,   41,  258,  108,  109,  258,
   40,   40,   40,  258,   41,  125,  125,   41,  125,  125,
  125,  258,  258,  258,  258,   40,   40,  258,   41,   41,
   41,  258,  258,  258,   41,   41,  258,  258,  258,  258,
  258,  258,  258,    0,   41,  125,   11,   25,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  258,   -1,   -1,   -1,   -1,   -1,  271,  272,  273,  274,
  275,  276,  277,  271,  272,  273,  274,  275,  276,  277,
  278,   -1,  271,  272,  273,  274,  275,  276,  277,  257,
  258,   -1,  258,  262,  263,  264,  265,   -1,  266,  267,
  266,  267,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=278;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"'%'",null,null,"'('","')'","'*'","'+'",null,
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"SEP","TEXT","ARRAYVAL","STR","MOD","ENTER",
"EXIT","CLASS","OBJECT","SIZE","ORIG","COMMA","IF","IFF","EQUAL","NEQUAL","L",
"LEQ","GEQ","G","ONEOF","ELEMS",
};
final static String yyrule[] = {
"$accept : start",
"start :",
"start : SEP first invs start",
"first : TEXT '(' pars ')' status",
"first : TEXT status",
"status : ENTER",
"status : EXIT",
"status : CLASS",
"status : OBJECT",
"pars :",
"pars : ARRAYVAL",
"pars : TEXT",
"pars : STR",
"pars : TEXT COMMA pars",
"pars : STR COMMA pars",
"pars : ARRAYVAL COMMA pars",
"invs :",
"invs : invs single",
"single : TEXT op TEXT",
"single : TEXT ONEOF arrays",
"single : ORIG '(' TEXT ')' op TEXT",
"single : ORIG '(' TEXT ')' op ARRAYVAL",
"single : TEXT op ORIG '(' TEXT ')'",
"single : TEXT EQUAL MOD",
"single : '(' single ')' IF '(' single ')'",
"single : '(' single ')' IFF '(' single ')'",
"single : SIZE '(' TEXT ')' op TEXT",
"single : TEXT ELEMS op TEXT",
"single : SIZE '(' TEXT ')' ONEOF arrays",
"single : TEXT ELEMS ONEOF arrays",
"single : ORIG '(' TEXT ')' ONEOF arrays",
"single : TEXT TEXT TEXT TEXT TEXT op TEXT",
"single : TEXT '-' ORIG '(' TEXT ')' '-' TEXT op TEXT",
"single : TEXT '-' ORIG '(' TEXT ')' '+' TEXT op TEXT",
"single : TEXT '%' TEXT op TEXT",
"single : ORIG '(' TEXT ')' '%' TEXT op TEXT",
"single : TEXT '%' ORIG '(' TEXT ')' op TEXT",
"single : SIZE '(' TEXT ')' '-' TEXT op TEXT",
"single : TEXT EQUAL ARRAYVAL",
"arrays : '{' pars '}'",
"op : L",
"op : LEQ",
"op : GEQ",
"op : G",
"op : EQUAL",
"op : NEQUAL",
};

//#line 190 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"



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
//#line 409 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 67 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{}
break;
case 3:
//#line 70 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ lastRule = "first-1st rule";
    MethodInvariants m = new MethodInvariants(isClass, isObj, isEnter, isExit, exitLine, val_peek(4).sval, (List)val_peek(2).obj);
    m.setFileLine(lexer.line);
    lm.add(m);
    this.lastInv = m;
}
break;
case 4:
//#line 76 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ lastRule = "first-2nd rule";
        MethodInvariants m = new MethodInvariants(isClass, isObj, isEnter, isExit, exitLine, val_peek(1).sval, new ArrayList<>());
        m.setFileLine(lexer.line);
        lm.add(m);
        this.lastInv = m;
}
break;
case 5:
//#line 83 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{  isClass = false;
                  isObj   = false;
                  isEnter = true;
                  isExit  = false;
                  exitLine = 0;
               }
break;
case 6:
//#line 89 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{  isClass = false;
                  isObj   = false;
                  isEnter = false;
                  isExit  = true;
                  exitLine = 0;
                  String s = val_peek(0).sval;
                  s = s.substring(7);
                  if(s.length()>0)
                    exitLine = Integer.parseInt(s);
              }
break;
case 7:
//#line 99 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{  isClass = true;
                  isObj   = false;
                  isEnter = false;
                  isExit  = false;
                  exitLine = 0;
              }
break;
case 8:
//#line 105 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{  isClass = false;
                  isObj   = true;
                  isEnter = false;
                  isExit  = false;
                  exitLine = 0;
              }
break;
case 9:
//#line 112 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{       lastRule = "pars-empty";
                                yyval.obj = new ArrayList<>();
                        }
break;
case 10:
//#line 115 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{
                                lastRule = "pars-array";
                                List<String> out = new ArrayList<>();
                                out.add(val_peek(0).sval);
                                yyval.obj = out;
                        }
break;
case 11:
//#line 121 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{       lastRule = "pars-text";
                                List<String> out = new ArrayList<>();
                                out.add(val_peek(0).sval);
                                yyval.obj = out;
                        }
break;
case 12:
//#line 126 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{        lastRule = "pars-str";
                                List<String> out = new ArrayList<>();
                                out.add(val_peek(0).sval);
                                yyval.obj = out;
                        }
break;
case 13:
//#line 131 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{   lastRule = "pars-txt-comma";
                            List<String> out = new ArrayList<>();
                            out.add(val_peek(2).sval);
                            out.addAll((List)val_peek(0).obj);
                            yyval.obj = out;
                        }
break;
case 14:
//#line 137 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{    lastRule = "pars-str-comma";
                            List<String> out = new ArrayList<>();
                            out.add(val_peek(2).sval);
                            out.addAll((List)val_peek(0).obj);
                            yyval.obj = out;
                        }
break;
case 15:
//#line 143 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ lastRule = "pars-array-comma";
                            List<String> out = new ArrayList<>();
                            out.add(val_peek(2).sval);
                            out.addAll((List)val_peek(0).obj);
                            yyval.obj = out;
                        }
break;
case 16:
//#line 150 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ lastRule = "inv-empty"; }
break;
case 17:
//#line 151 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{
            lastRule = "inv-single";
            this.lastInv.add((Invariant) val_peek(0).obj);
    }
break;
case 18:
//#line 156 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ yyval.obj = new Check(val_peek(2).sval, val_peek(0).sval, (Invariant.OP)val_peek(1).obj); }
break;
case 19:
//#line 157 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ yyval.obj = new OneOf(val_peek(2).sval, (List) val_peek(0).obj); }
break;
case 20:
//#line 158 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ yyval.obj = new LOrig(val_peek(3).sval, (Invariant.OP)val_peek(1).obj, val_peek(0).sval); }
break;
case 21:
//#line 159 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ yyval.obj = new LOrig(val_peek(3).sval, (Invariant.OP)val_peek(1).obj, val_peek(0).sval); }
break;
case 22:
//#line 160 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ yyval.obj = new ROrig(val_peek(5).sval, (Invariant.OP)val_peek(4).obj, val_peek(1).sval); }
break;
case 23:
//#line 161 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ yyval.obj = new Mod(val_peek(2).sval, val_peek(0).sval); }
break;
case 24:
//#line 162 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ yyval.obj = new Implication((Invariant)val_peek(5).obj, (Invariant)val_peek(1).obj); }
break;
case 25:
//#line 163 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ yyval.obj = new Implication((Invariant)val_peek(5).obj, (Invariant)val_peek(1).obj); }
break;
case 26:
//#line 164 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ yyval.obj = new Size(val_peek(3).sval, val_peek(0).sval, (Invariant.OP)val_peek(1).obj); }
break;
case 27:
//#line 165 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ /* ignore */ }
break;
case 28:
//#line 166 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ /* ignore */ }
break;
case 29:
//#line 167 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ yyval.obj = new ArrayVals(val_peek(3).sval,(List) val_peek(0).obj); }
break;
case 30:
//#line 168 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ yyval.obj = new LOrigArrayVals(val_peek(3).sval, (List) val_peek(0).obj); }
break;
case 31:
//#line 170 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ /* ignore corner invs */}
break;
case 32:
//#line 171 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ /* ignore corner invs */}
break;
case 33:
//#line 172 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ /* ignore corner invs */}
break;
case 34:
//#line 173 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ /* ignore corner invs */}
break;
case 35:
//#line 174 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ /* ignore corner invs */}
break;
case 36:
//#line 175 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ /* ignore corner invs */}
break;
case 37:
//#line 176 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ /* ignore corner invs */}
break;
case 38:
//#line 177 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{   }
break;
case 39:
//#line 179 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 40:
//#line 181 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ yyval.obj = Invariant.OP.L; }
break;
case 41:
//#line 182 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ yyval.obj = Invariant.OP.LE; }
break;
case 42:
//#line 183 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ yyval.obj = Invariant.OP.GE; }
break;
case 43:
//#line 184 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ yyval.obj = Invariant.OP.G; }
break;
case 44:
//#line 185 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ yyval.obj = Invariant.OP.EQ; }
break;
case 45:
//#line 186 "/Users/giovanni/repository/automatic-repair-time-comparison/daikon-instrumentation/grammar/calc.y"
{ yyval.obj = Invariant.OP.NEQ; }
break;
//#line 801 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
