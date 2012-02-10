/*     */ package org.codehaus.jackson.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.codehaus.jackson.JsonParseException;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ 
/*     */ public class JsonParserSequence extends JsonParserDelegate
/*     */ {
/*     */   protected final JsonParser[] _parsers;
/*     */   protected int _nextParser;
/*     */ 
/*     */   protected JsonParserSequence(JsonParser[] parsers)
/*     */   {
/*  40 */     super(parsers[0]);
/*  41 */     this._parsers = parsers;
/*  42 */     this._nextParser = 1;
/*     */   }
/*     */ 
/*     */   public static JsonParserSequence createFlattened(JsonParser first, JsonParser second)
/*     */   {
/*  56 */     if ((!(first instanceof JsonParserSequence)) && (!(second instanceof JsonParserSequence)))
/*     */     {
/*  58 */       return new JsonParserSequence(new JsonParser[] { first, second });
/*     */     }
/*  60 */     ArrayList p = new ArrayList();
/*  61 */     if ((first instanceof JsonParserSequence))
/*  62 */       ((JsonParserSequence)first).addFlattenedActiveParsers(p);
/*     */     else {
/*  64 */       p.add(first);
/*     */     }
/*  66 */     if ((second instanceof JsonParserSequence))
/*  67 */       ((JsonParserSequence)second).addFlattenedActiveParsers(p);
/*     */     else {
/*  69 */       p.add(second);
/*     */     }
/*  71 */     return new JsonParserSequence((JsonParser[])p.toArray(new JsonParser[p.size()]));
/*     */   }
/*     */ 
/*     */   protected void addFlattenedActiveParsers(List<JsonParser> result)
/*     */   {
/*  76 */     int i = this._nextParser - 1; for (int len = this._parsers.length; i < len; i++) {
/*  77 */       JsonParser p = this._parsers[i];
/*  78 */       if ((p instanceof JsonParserSequence))
/*  79 */         ((JsonParserSequence)p).addFlattenedActiveParsers(result);
/*     */       else
/*  81 */         result.add(p);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*     */     do
/*  97 */       this.delegate.close();
/*  98 */     while (switchToNext());
/*     */   }
/*     */ 
/*     */   public JsonToken nextToken()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 104 */     JsonToken t = this.delegate.nextToken();
/* 105 */     if (t != null) return t;
/* 106 */     while (switchToNext()) {
/* 107 */       t = this.delegate.nextToken();
/* 108 */       if (t != null) return t;
/*     */     }
/* 110 */     return null;
/*     */   }
/*     */ 
/*     */   public int containedParsersCount()
/*     */   {
/* 125 */     return this._parsers.length;
/*     */   }
/*     */ 
/*     */   protected boolean switchToNext()
/*     */   {
/* 144 */     if (this._nextParser >= this._parsers.length) {
/* 145 */       return false;
/*     */     }
/* 147 */     this.delegate = this._parsers[(this._nextParser++)];
/* 148 */     return true;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.util.JsonParserSequence
 * JD-Core Version:    0.6.0
 */