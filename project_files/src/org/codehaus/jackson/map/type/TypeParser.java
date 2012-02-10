/*     */ package org.codehaus.jackson.map.type;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class TypeParser
/*     */ {
/*     */   final TypeFactory _factory;
/*     */ 
/*     */   public TypeParser(TypeFactory f)
/*     */   {
/*  19 */     this._factory = f;
/*     */   }
/*     */ 
/*     */   public JavaType parse(String canonical)
/*     */     throws IllegalArgumentException
/*     */   {
/*  25 */     canonical = canonical.trim();
/*  26 */     MyTokenizer tokens = new MyTokenizer(canonical);
/*  27 */     JavaType type = parseType(tokens);
/*     */ 
/*  29 */     if (tokens.hasMoreTokens()) {
/*  30 */       throw _problem(tokens, "Unexpected tokens after complete type");
/*     */     }
/*  32 */     return type;
/*     */   }
/*     */ 
/*     */   protected JavaType parseType(MyTokenizer tokens)
/*     */     throws IllegalArgumentException
/*     */   {
/*  38 */     if (!tokens.hasMoreTokens()) {
/*  39 */       throw _problem(tokens, "Unexpected end-of-string");
/*     */     }
/*  41 */     Class base = findClass(tokens.nextToken(), tokens);
/*     */ 
/*  43 */     if (tokens.hasMoreTokens()) {
/*  44 */       String token = tokens.nextToken();
/*  45 */       if ("<".equals(token)) {
/*  46 */         return this._factory._fromParameterizedClass(base, parseTypes(tokens));
/*     */       }
/*     */ 
/*  49 */       tokens.pushBack(token);
/*     */     }
/*  51 */     return this._factory._fromClass(base, null);
/*     */   }
/*     */ 
/*     */   protected List<JavaType> parseTypes(MyTokenizer tokens)
/*     */     throws IllegalArgumentException
/*     */   {
/*  57 */     ArrayList types = new ArrayList();
/*  58 */     while (tokens.hasMoreTokens()) {
/*  59 */       types.add(parseType(tokens));
/*  60 */       if (!tokens.hasMoreTokens()) break;
/*  61 */       String token = tokens.nextToken();
/*  62 */       if (">".equals(token)) return types;
/*  63 */       if (!",".equals(token)) {
/*  64 */         throw _problem(tokens, "Unexpected token '" + token + "', expected ',' or '>')");
/*     */       }
/*     */     }
/*  67 */     throw _problem(tokens, "Unexpected end-of-string");
/*     */   }
/*     */ 
/*     */   protected Class<?> findClass(String className, MyTokenizer tokens)
/*     */   {
/*     */     try
/*     */     {
/*  77 */       ClassLoader loader = Thread.currentThread().getContextClassLoader();
/*  78 */       return Class.forName(className, true, loader);
/*     */     } catch (Exception e) {
/*  80 */       if ((e instanceof RuntimeException))
/*  81 */         throw ((RuntimeException)e);
/*     */     }
/*  83 */     throw _problem(tokens, "Can not locate class '" + className + "', problem: " + e.getMessage());
/*     */   }
/*     */ 
/*     */   protected IllegalArgumentException _problem(MyTokenizer tokens, String msg)
/*     */   {
/*  89 */     return new IllegalArgumentException("Failed to parse type '" + tokens.getAllInput() + "' (remaining: '" + tokens.getRemainingInput() + "'): " + msg);
/*     */   }
/*     */ 
/*     */   static final class MyTokenizer extends StringTokenizer
/*     */   {
/*     */     protected final String _input;
/*     */     protected int _index;
/*     */     protected String _pushbackToken;
/*     */ 
/*     */     public MyTokenizer(String str)
/*     */     {
/* 103 */       super("<,>", true);
/* 104 */       this._input = str;
/*     */     }
/*     */ 
/*     */     public boolean hasMoreTokens()
/*     */     {
/* 109 */       return (this._pushbackToken != null) || (super.hasMoreTokens());
/*     */     }
/*     */ 
/*     */     public String nextToken()
/*     */     {
/*     */       String token;
/* 115 */       if (this._pushbackToken != null) {
/* 116 */         String token = this._pushbackToken;
/* 117 */         this._pushbackToken = null;
/*     */       } else {
/* 119 */         token = super.nextToken();
/*     */       }
/* 121 */       this._index += token.length();
/* 122 */       return token;
/*     */     }
/*     */ 
/*     */     public void pushBack(String token) {
/* 126 */       this._pushbackToken = token;
/* 127 */       this._index -= token.length();
/*     */     }
/*     */     public String getAllInput() {
/* 130 */       return this._input; } 
/* 131 */     public String getUsedInput() { return this._input.substring(0, this._index); } 
/* 132 */     public String getRemainingInput() { return this._input.substring(this._index);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.type.TypeParser
 * JD-Core Version:    0.6.0
 */