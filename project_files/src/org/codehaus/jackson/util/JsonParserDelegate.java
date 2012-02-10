/*     */ package org.codehaus.jackson.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import org.codehaus.jackson.Base64Variant;
/*     */ import org.codehaus.jackson.JsonLocation;
/*     */ import org.codehaus.jackson.JsonParseException;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonParser.Feature;
/*     */ import org.codehaus.jackson.JsonParser.NumberType;
/*     */ import org.codehaus.jackson.JsonStreamContext;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.ObjectCodec;
/*     */ 
/*     */ public class JsonParserDelegate extends JsonParser
/*     */ {
/*     */   protected JsonParser delegate;
/*     */ 
/*     */   public JsonParserDelegate(JsonParser d)
/*     */   {
/*  26 */     this.delegate = d;
/*     */   }
/*     */ 
/*     */   public void setCodec(ObjectCodec c)
/*     */   {
/*  37 */     this.delegate.setCodec(c);
/*     */   }
/*     */ 
/*     */   public ObjectCodec getCodec()
/*     */   {
/*  42 */     return this.delegate.getCodec();
/*     */   }
/*     */ 
/*     */   public JsonParser enable(JsonParser.Feature f)
/*     */   {
/*  47 */     this.delegate.enable(f);
/*  48 */     return this;
/*     */   }
/*     */ 
/*     */   public JsonParser disable(JsonParser.Feature f)
/*     */   {
/*  53 */     this.delegate.disable(f);
/*  54 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean isEnabled(JsonParser.Feature f)
/*     */   {
/*  59 */     return this.delegate.isEnabled(f);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*  70 */     this.delegate.close();
/*     */   }
/*     */ 
/*     */   public boolean isClosed()
/*     */   {
/*  75 */     return this.delegate.isClosed();
/*     */   }
/*     */ 
/*     */   public JsonToken getCurrentToken()
/*     */   {
/*  86 */     return this.delegate.getCurrentToken();
/*     */   }
/*     */ 
/*     */   public boolean hasCurrentToken()
/*     */   {
/*  91 */     return this.delegate.hasCurrentToken();
/*     */   }
/*     */ 
/*     */   public void clearCurrentToken()
/*     */   {
/*  96 */     this.delegate.clearCurrentToken();
/*     */   }
/*     */ 
/*     */   public String getCurrentName() throws IOException, JsonParseException
/*     */   {
/* 101 */     return this.delegate.getCurrentName();
/*     */   }
/*     */ 
/*     */   public JsonLocation getCurrentLocation()
/*     */   {
/* 106 */     return this.delegate.getCurrentLocation();
/*     */   }
/*     */ 
/*     */   public JsonToken getLastClearedToken()
/*     */   {
/* 111 */     return this.delegate.getLastClearedToken();
/*     */   }
/*     */ 
/*     */   public JsonStreamContext getParsingContext()
/*     */   {
/* 116 */     return this.delegate.getParsingContext();
/*     */   }
/*     */ 
/*     */   public String getText()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 127 */     return this.delegate.getText();
/*     */   }
/*     */ 
/*     */   public char[] getTextCharacters() throws IOException, JsonParseException
/*     */   {
/* 132 */     return this.delegate.getTextCharacters();
/*     */   }
/*     */ 
/*     */   public int getTextLength() throws IOException, JsonParseException
/*     */   {
/* 137 */     return this.delegate.getTextLength();
/*     */   }
/*     */ 
/*     */   public int getTextOffset() throws IOException, JsonParseException
/*     */   {
/* 142 */     return this.delegate.getTextOffset();
/*     */   }
/*     */ 
/*     */   public BigInteger getBigIntegerValue()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 154 */     return this.delegate.getBigIntegerValue();
/*     */   }
/*     */ 
/*     */   public byte getByteValue() throws IOException, JsonParseException
/*     */   {
/* 159 */     return this.delegate.getByteValue();
/*     */   }
/*     */ 
/*     */   public short getShortValue() throws IOException, JsonParseException
/*     */   {
/* 164 */     return this.delegate.getShortValue();
/*     */   }
/*     */ 
/*     */   public BigDecimal getDecimalValue() throws IOException, JsonParseException
/*     */   {
/* 169 */     return this.delegate.getDecimalValue();
/*     */   }
/*     */ 
/*     */   public double getDoubleValue() throws IOException, JsonParseException
/*     */   {
/* 174 */     return this.delegate.getDoubleValue();
/*     */   }
/*     */ 
/*     */   public float getFloatValue() throws IOException, JsonParseException
/*     */   {
/* 179 */     return this.delegate.getFloatValue();
/*     */   }
/*     */ 
/*     */   public int getIntValue() throws IOException, JsonParseException
/*     */   {
/* 184 */     return this.delegate.getIntValue();
/*     */   }
/*     */ 
/*     */   public long getLongValue() throws IOException, JsonParseException
/*     */   {
/* 189 */     return this.delegate.getLongValue();
/*     */   }
/*     */ 
/*     */   public JsonParser.NumberType getNumberType() throws IOException, JsonParseException
/*     */   {
/* 194 */     return this.delegate.getNumberType();
/*     */   }
/*     */ 
/*     */   public Number getNumberValue() throws IOException, JsonParseException
/*     */   {
/* 199 */     return this.delegate.getNumberValue();
/*     */   }
/*     */ 
/*     */   public byte[] getBinaryValue(Base64Variant b64variant) throws IOException, JsonParseException
/*     */   {
/* 204 */     return this.delegate.getBinaryValue(b64variant);
/*     */   }
/*     */ 
/*     */   public JsonLocation getTokenLocation()
/*     */   {
/* 209 */     return this.delegate.getTokenLocation();
/*     */   }
/*     */ 
/*     */   public JsonToken nextToken() throws IOException, JsonParseException
/*     */   {
/* 214 */     return this.delegate.nextToken();
/*     */   }
/*     */ 
/*     */   public JsonParser skipChildren() throws IOException, JsonParseException
/*     */   {
/* 219 */     this.delegate.skipChildren();
/*     */ 
/* 221 */     return this;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.util.JsonParserDelegate
 * JD-Core Version:    0.6.0
 */