/*     */ package org.codehaus.jackson.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.codehaus.jackson.JsonGenerationException;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.PrettyPrinter;
/*     */ 
/*     */ public class MinimalPrettyPrinter
/*     */   implements PrettyPrinter
/*     */ {
/*     */   public static final String DEFAULT_ROOT_VALUE_SEPARATOR = " ";
/*  32 */   protected String _rootValueSeparator = " ";
/*     */ 
/*     */   public void setRootValueSeparator(String sep)
/*     */   {
/*  43 */     this._rootValueSeparator = sep;
/*     */   }
/*     */ 
/*     */   public void writeRootValueSeparator(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*  55 */     if (this._rootValueSeparator != null)
/*  56 */       jg.writeRaw(this._rootValueSeparator);
/*     */   }
/*     */ 
/*     */   public void writeStartObject(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*  63 */     jg.writeRaw('{');
/*     */   }
/*     */ 
/*     */   public void beforeObjectEntries(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void writeObjectFieldValueSeparator(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*  82 */     jg.writeRaw(':');
/*     */   }
/*     */ 
/*     */   public void writeObjectEntrySeparator(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*  95 */     jg.writeRaw(',');
/*     */   }
/*     */ 
/*     */   public void writeEndObject(JsonGenerator jg, int nrOfEntries)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 101 */     jg.writeRaw('}');
/*     */   }
/*     */ 
/*     */   public void writeStartArray(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 107 */     jg.writeRaw('[');
/*     */   }
/*     */ 
/*     */   public void beforeArrayValues(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void writeArrayValueSeparator(JsonGenerator jg)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 126 */     jg.writeRaw(',');
/*     */   }
/*     */ 
/*     */   public void writeEndArray(JsonGenerator jg, int nrOfValues)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 132 */     jg.writeRaw(']');
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.util.MinimalPrettyPrinter
 * JD-Core Version:    0.6.0
 */