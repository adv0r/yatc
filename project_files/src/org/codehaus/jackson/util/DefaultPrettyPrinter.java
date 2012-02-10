/*     */ package org.codehaus.jackson.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import org.codehaus.jackson.JsonGenerationException;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.PrettyPrinter;
/*     */ import org.codehaus.jackson.impl.Indenter;
/*     */ 
/*     */ public class DefaultPrettyPrinter
/*     */   implements PrettyPrinter
/*     */ {
/*  24 */   protected Indenter _arrayIndenter = new FixedSpaceIndenter();
/*     */ 
/*  32 */   protected Indenter _objectIndenter = new Lf2SpacesIndenter();
/*     */ 
/*  41 */   protected boolean _spacesInObjectEntries = true;
/*     */ 
/*  49 */   protected int _nesting = 0;
/*     */ 
/*     */   public void indentArraysWith(Indenter i)
/*     */   {
/*  61 */     this._arrayIndenter = (i == null ? new NopIndenter() : i);
/*     */   }
/*     */ 
/*     */   public void indentObjectsWith(Indenter i)
/*     */   {
/*  66 */     this._objectIndenter = (i == null ? new NopIndenter() : i);
/*     */   }
/*     */   public void spacesInObjectEntries(boolean b) {
/*  69 */     this._spacesInObjectEntries = b;
/*     */   }
/*     */ 
/*     */   public void writeRootValueSeparator(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*  80 */     jg.writeRaw(' ');
/*     */   }
/*     */ 
/*     */   public void writeStartObject(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*  86 */     jg.writeRaw('{');
/*  87 */     if (!this._objectIndenter.isInline())
/*  88 */       this._nesting += 1;
/*     */   }
/*     */ 
/*     */   public void beforeObjectEntries(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*  95 */     this._objectIndenter.writeIndentation(jg, this._nesting);
/*     */   }
/*     */ 
/*     */   public void writeObjectFieldValueSeparator(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 110 */     if (this._spacesInObjectEntries)
/* 111 */       jg.writeRaw(" : ");
/*     */     else
/* 113 */       jg.writeRaw(':');
/*     */   }
/*     */ 
/*     */   public void writeObjectEntrySeparator(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 129 */     jg.writeRaw(',');
/* 130 */     this._objectIndenter.writeIndentation(jg, this._nesting);
/*     */   }
/*     */ 
/*     */   public void writeEndObject(JsonGenerator jg, int nrOfEntries)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 136 */     if (!this._objectIndenter.isInline()) {
/* 137 */       this._nesting -= 1;
/*     */     }
/* 139 */     if (nrOfEntries > 0)
/* 140 */       this._objectIndenter.writeIndentation(jg, this._nesting);
/*     */     else {
/* 142 */       jg.writeRaw(' ');
/*     */     }
/* 144 */     jg.writeRaw('}');
/*     */   }
/*     */ 
/*     */   public void writeStartArray(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 150 */     if (!this._arrayIndenter.isInline()) {
/* 151 */       this._nesting += 1;
/*     */     }
/* 153 */     jg.writeRaw('[');
/*     */   }
/*     */ 
/*     */   public void beforeArrayValues(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 159 */     this._arrayIndenter.writeIndentation(jg, this._nesting);
/*     */   }
/*     */ 
/*     */   public void writeArrayValueSeparator(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 174 */     jg.writeRaw(',');
/* 175 */     this._arrayIndenter.writeIndentation(jg, this._nesting);
/*     */   }
/*     */ 
/*     */   public void writeEndArray(JsonGenerator jg, int nrOfValues)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 181 */     if (!this._arrayIndenter.isInline()) {
/* 182 */       this._nesting -= 1;
/*     */     }
/* 184 */     if (nrOfValues > 0)
/* 185 */       this._arrayIndenter.writeIndentation(jg, this._nesting);
/*     */     else {
/* 187 */       jg.writeRaw(' ');
/*     */     }
/* 189 */     jg.writeRaw(']');
/*     */   }
/*     */ 
/*     */   public static class Lf2SpacesIndenter
/*     */     implements Indenter
/*     */   {
/*     */     static final String SYSTEM_LINE_SEPARATOR;
/*     */     static final int SPACE_COUNT = 64;
/*     */     static final char[] SPACES;
/*     */ 
/*     */     public boolean isInline()
/*     */     {
/* 252 */       return false;
/*     */     }
/*     */ 
/*     */     public void writeIndentation(JsonGenerator jg, int level) throws IOException, JsonGenerationException
/*     */     {
/* 257 */       jg.writeRaw(SYSTEM_LINE_SEPARATOR);
/* 258 */       level += level;
/* 259 */       while (level > 64) {
/* 260 */         jg.writeRaw(SPACES, 0, 64);
/* 261 */         level -= SPACES.length;
/*     */       }
/* 263 */       jg.writeRaw(SPACES, 0, level);
/*     */     }
/*     */ 
/*     */     static
/*     */     {
/* 237 */       String lf = null;
/*     */       try {
/* 239 */         lf = System.getProperty("line.separator"); } catch (Throwable t) {
/*     */       }
/* 241 */       SYSTEM_LINE_SEPARATOR = lf == null ? "\n" : lf;
/*     */ 
/* 245 */       SPACES = new char[64];
/*     */ 
/* 247 */       Arrays.fill(SPACES, ' ');
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class FixedSpaceIndenter
/*     */     implements Indenter
/*     */   {
/*     */     public void writeIndentation(JsonGenerator jg, int level)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 222 */       jg.writeRaw(' ');
/*     */     }
/*     */     public boolean isInline() {
/* 225 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class NopIndenter
/*     */     implements Indenter
/*     */   {
/*     */     public void writeIndentation(JsonGenerator jg, int level)
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean isInline()
/*     */     {
/* 206 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.util.DefaultPrettyPrinter
 * JD-Core Version:    0.6.0
 */