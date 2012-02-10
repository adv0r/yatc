/*     */ package org.codehaus.jackson.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import org.codehaus.jackson.Base64Variant;
/*     */ import org.codehaus.jackson.JsonGenerationException;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonGenerator.Feature;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonStreamContext;
/*     */ import org.codehaus.jackson.ObjectCodec;
/*     */ import org.codehaus.jackson.SerializableString;
/*     */ import org.codehaus.jackson.io.SerializedString;
/*     */ 
/*     */ public class JsonGeneratorDelegate extends JsonGenerator
/*     */ {
/*     */   protected JsonGenerator delegate;
/*     */ 
/*     */   public JsonGeneratorDelegate(JsonGenerator d)
/*     */   {
/*  18 */     this.delegate = d;
/*     */   }
/*     */ 
/*     */   public void close() throws IOException
/*     */   {
/*  23 */     this.delegate.close();
/*     */   }
/*     */ 
/*     */   public void copyCurrentEvent(JsonParser jp) throws IOException, JsonProcessingException
/*     */   {
/*  28 */     this.delegate.copyCurrentEvent(jp);
/*     */   }
/*     */ 
/*     */   public void copyCurrentStructure(JsonParser jp) throws IOException, JsonProcessingException
/*     */   {
/*  33 */     this.delegate.copyCurrentStructure(jp);
/*     */   }
/*     */ 
/*     */   public JsonGenerator disable(JsonGenerator.Feature f)
/*     */   {
/*  38 */     return this.delegate.disable(f);
/*     */   }
/*     */ 
/*     */   public JsonGenerator enable(JsonGenerator.Feature f)
/*     */   {
/*  43 */     return this.delegate.enable(f);
/*     */   }
/*     */ 
/*     */   public void flush() throws IOException
/*     */   {
/*  48 */     this.delegate.flush();
/*     */   }
/*     */ 
/*     */   public ObjectCodec getCodec()
/*     */   {
/*  53 */     return this.delegate.getCodec();
/*     */   }
/*     */ 
/*     */   public JsonStreamContext getOutputContext()
/*     */   {
/*  58 */     return this.delegate.getOutputContext();
/*     */   }
/*     */ 
/*     */   public boolean isClosed()
/*     */   {
/*  63 */     return this.delegate.isClosed();
/*     */   }
/*     */ 
/*     */   public boolean isEnabled(JsonGenerator.Feature f)
/*     */   {
/*  68 */     return this.delegate.isEnabled(f);
/*     */   }
/*     */ 
/*     */   public JsonGenerator setCodec(ObjectCodec oc)
/*     */   {
/*  73 */     this.delegate.setCodec(oc);
/*  74 */     return this;
/*     */   }
/*     */ 
/*     */   public JsonGenerator useDefaultPrettyPrinter()
/*     */   {
/*  79 */     this.delegate.useDefaultPrettyPrinter();
/*  80 */     return this;
/*     */   }
/*     */ 
/*     */   public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*  87 */     this.delegate.writeBinary(b64variant, data, offset, len);
/*     */   }
/*     */ 
/*     */   public void writeBoolean(boolean state) throws IOException, JsonGenerationException
/*     */   {
/*  92 */     this.delegate.writeBoolean(state);
/*     */   }
/*     */ 
/*     */   public void writeEndArray() throws IOException, JsonGenerationException
/*     */   {
/*  97 */     this.delegate.writeEndArray();
/*     */   }
/*     */ 
/*     */   public void writeEndObject() throws IOException, JsonGenerationException
/*     */   {
/* 102 */     this.delegate.writeEndObject();
/*     */   }
/*     */ 
/*     */   public void writeFieldName(String name)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 109 */     this.delegate.writeFieldName(name);
/*     */   }
/*     */ 
/*     */   public void writeFieldName(SerializedString name)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 116 */     this.delegate.writeFieldName(name);
/*     */   }
/*     */ 
/*     */   public void writeFieldName(SerializableString name)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 123 */     this.delegate.writeFieldName(name);
/*     */   }
/*     */ 
/*     */   public void writeNull() throws IOException, JsonGenerationException
/*     */   {
/* 128 */     this.delegate.writeNull();
/*     */   }
/*     */ 
/*     */   public void writeNumber(int v) throws IOException, JsonGenerationException
/*     */   {
/* 133 */     this.delegate.writeNumber(v);
/*     */   }
/*     */ 
/*     */   public void writeNumber(long v) throws IOException, JsonGenerationException
/*     */   {
/* 138 */     this.delegate.writeNumber(v);
/*     */   }
/*     */ 
/*     */   public void writeNumber(BigInteger v)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 144 */     this.delegate.writeNumber(v);
/*     */   }
/*     */ 
/*     */   public void writeNumber(double v)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 150 */     this.delegate.writeNumber(v);
/*     */   }
/*     */ 
/*     */   public void writeNumber(float v)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 156 */     this.delegate.writeNumber(v);
/*     */   }
/*     */ 
/*     */   public void writeNumber(BigDecimal v)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 162 */     this.delegate.writeNumber(v);
/*     */   }
/*     */ 
/*     */   public void writeNumber(String encodedValue) throws IOException, JsonGenerationException, UnsupportedOperationException
/*     */   {
/* 167 */     this.delegate.writeNumber(encodedValue);
/*     */   }
/*     */ 
/*     */   public void writeObject(Object pojo) throws IOException, JsonProcessingException
/*     */   {
/* 172 */     this.delegate.writeObject(pojo);
/*     */   }
/*     */ 
/*     */   public void writeRaw(String text) throws IOException, JsonGenerationException
/*     */   {
/* 177 */     this.delegate.writeRaw(text);
/*     */   }
/*     */ 
/*     */   public void writeRaw(String text, int offset, int len) throws IOException, JsonGenerationException
/*     */   {
/* 182 */     this.delegate.writeRaw(text, offset, len);
/*     */   }
/*     */ 
/*     */   public void writeRaw(char[] text, int offset, int len) throws IOException, JsonGenerationException
/*     */   {
/* 187 */     this.delegate.writeRaw(text, offset, len);
/*     */   }
/*     */ 
/*     */   public void writeRaw(char c) throws IOException, JsonGenerationException
/*     */   {
/* 192 */     this.delegate.writeRaw(c);
/*     */   }
/*     */ 
/*     */   public void writeRawValue(String text) throws IOException, JsonGenerationException
/*     */   {
/* 197 */     this.delegate.writeRawValue(text);
/*     */   }
/*     */ 
/*     */   public void writeRawValue(String text, int offset, int len) throws IOException, JsonGenerationException
/*     */   {
/* 202 */     this.delegate.writeRawValue(text, offset, len);
/*     */   }
/*     */ 
/*     */   public void writeRawValue(char[] text, int offset, int len) throws IOException, JsonGenerationException
/*     */   {
/* 207 */     this.delegate.writeRawValue(text, offset, len);
/*     */   }
/*     */ 
/*     */   public void writeStartArray() throws IOException, JsonGenerationException
/*     */   {
/* 212 */     this.delegate.writeStartArray();
/*     */   }
/*     */ 
/*     */   public void writeStartObject() throws IOException, JsonGenerationException
/*     */   {
/* 217 */     this.delegate.writeStartObject();
/*     */   }
/*     */ 
/*     */   public void writeString(String text) throws IOException, JsonGenerationException
/*     */   {
/* 222 */     this.delegate.writeString(text);
/*     */   }
/*     */ 
/*     */   public void writeString(char[] text, int offset, int len) throws IOException, JsonGenerationException
/*     */   {
/* 227 */     this.delegate.writeString(text, offset, len);
/*     */   }
/*     */ 
/*     */   public void writeString(SerializableString text) throws IOException, JsonGenerationException
/*     */   {
/* 232 */     this.delegate.writeString(text);
/*     */   }
/*     */ 
/*     */   public void writeRawUTF8String(byte[] text, int offset, int length)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 239 */     this.delegate.writeRawUTF8String(text, offset, length);
/*     */   }
/*     */ 
/*     */   public void writeUTF8String(byte[] text, int offset, int length)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 246 */     this.delegate.writeUTF8String(text, offset, length);
/*     */   }
/*     */ 
/*     */   public void writeTree(JsonNode rootNode) throws IOException, JsonProcessingException
/*     */   {
/* 251 */     this.delegate.writeTree(rootNode);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.util.JsonGeneratorDelegate
 * JD-Core Version:    0.6.0
 */