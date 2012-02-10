/*     */ package org.codehaus.jackson;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import org.codehaus.jackson.io.SerializedString;
/*     */ 
/*     */ public abstract class JsonGenerator
/*     */   implements Closeable, Versioned
/*     */ {
/*     */   protected PrettyPrinter _cfgPrettyPrinter;
/*     */ 
/*     */   public Version version()
/*     */   {
/* 168 */     return Version.unknownVersion();
/*     */   }
/*     */ 
/*     */   public abstract JsonGenerator enable(Feature paramFeature);
/*     */ 
/*     */   public abstract JsonGenerator disable(Feature paramFeature);
/*     */ 
/*     */   public JsonGenerator configure(Feature f, boolean state)
/*     */   {
/* 207 */     if (state)
/* 208 */       enable(f);
/*     */     else {
/* 210 */       disable(f);
/*     */     }
/* 212 */     return this;
/*     */   }
/*     */ 
/*     */   public abstract boolean isEnabled(Feature paramFeature);
/*     */ 
/*     */   public abstract JsonGenerator setCodec(ObjectCodec paramObjectCodec);
/*     */ 
/*     */   public abstract ObjectCodec getCodec();
/*     */ 
/*     */   /** @deprecated */
/*     */   public void enableFeature(Feature f)
/*     */   {
/* 244 */     enable(f);
/*     */   }
/*     */   /** @deprecated */
/*     */   public void disableFeature(Feature f) {
/* 249 */     disable(f);
/*     */   }
/*     */   /** @deprecated */
/*     */   public void setFeature(Feature f, boolean state) {
/* 254 */     configure(f, state);
/*     */   }
/*     */   /** @deprecated */
/*     */   public boolean isFeatureEnabled(Feature f) {
/* 259 */     return isEnabled(f);
/*     */   }
/*     */ 
/*     */   public JsonGenerator setPrettyPrinter(PrettyPrinter pp)
/*     */   {
/* 279 */     this._cfgPrettyPrinter = pp;
/* 280 */     return this;
/*     */   }
/*     */ 
/*     */   public abstract JsonGenerator useDefaultPrettyPrinter();
/*     */ 
/*     */   public abstract void writeStartArray()
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeEndArray()
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeStartObject()
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeEndObject()
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeFieldName(String paramString)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public void writeFieldName(SerializedString name)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 374 */     writeFieldName(name.getValue());
/*     */   }
/*     */ 
/*     */   public void writeFieldName(SerializableString name)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 393 */     writeFieldName(name.getValue());
/*     */   }
/*     */ 
/*     */   public abstract void writeString(String paramString)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeString(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public void writeString(SerializableString text)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 437 */     writeString(text.getValue());
/*     */   }
/*     */ 
/*     */   public abstract void writeRawUTF8String(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeUTF8String(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeRaw(String paramString)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeRaw(String paramString, int paramInt1, int paramInt2)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeRaw(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeRaw(char paramChar)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeRawValue(String paramString)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeRawValue(String paramString, int paramInt1, int paramInt2)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeRawValue(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeBinary(Base64Variant paramBase64Variant, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public void writeBinary(byte[] data, int offset, int len)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 598 */     writeBinary(Base64Variants.getDefaultVariant(), data, offset, len);
/*     */   }
/*     */ 
/*     */   public void writeBinary(byte[] data)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 610 */     writeBinary(Base64Variants.getDefaultVariant(), data, 0, data.length);
/*     */   }
/*     */ 
/*     */   public abstract void writeNumber(int paramInt)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeNumber(long paramLong)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeNumber(BigInteger paramBigInteger)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeNumber(double paramDouble)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeNumber(float paramFloat)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeNumber(BigDecimal paramBigDecimal)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeNumber(String paramString)
/*     */     throws IOException, JsonGenerationException, UnsupportedOperationException;
/*     */ 
/*     */   public abstract void writeBoolean(boolean paramBoolean)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeNull()
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void writeObject(Object paramObject)
/*     */     throws IOException, JsonProcessingException;
/*     */ 
/*     */   public abstract void writeTree(JsonNode paramJsonNode)
/*     */     throws IOException, JsonProcessingException;
/*     */ 
/*     */   public void writeStringField(String fieldName, String value)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 770 */     writeFieldName(fieldName);
/* 771 */     writeString(value);
/*     */   }
/*     */ 
/*     */   public final void writeBooleanField(String fieldName, boolean value)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 785 */     writeFieldName(fieldName);
/* 786 */     writeBoolean(value);
/*     */   }
/*     */ 
/*     */   public final void writeNullField(String fieldName)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 800 */     writeFieldName(fieldName);
/* 801 */     writeNull();
/*     */   }
/*     */ 
/*     */   public final void writeNumberField(String fieldName, int value)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 814 */     writeFieldName(fieldName);
/* 815 */     writeNumber(value);
/*     */   }
/*     */ 
/*     */   public final void writeNumberField(String fieldName, long value)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 829 */     writeFieldName(fieldName);
/* 830 */     writeNumber(value);
/*     */   }
/*     */ 
/*     */   public final void writeNumberField(String fieldName, double value)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 844 */     writeFieldName(fieldName);
/* 845 */     writeNumber(value);
/*     */   }
/*     */ 
/*     */   public final void writeNumberField(String fieldName, float value)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 859 */     writeFieldName(fieldName);
/* 860 */     writeNumber(value);
/*     */   }
/*     */ 
/*     */   public final void writeNumberField(String fieldName, BigDecimal value)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 875 */     writeFieldName(fieldName);
/* 876 */     writeNumber(value);
/*     */   }
/*     */ 
/*     */   public final void writeBinaryField(String fieldName, byte[] data)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 891 */     writeFieldName(fieldName);
/* 892 */     writeBinary(data);
/*     */   }
/*     */ 
/*     */   public final void writeArrayFieldStart(String fieldName)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 911 */     writeFieldName(fieldName);
/* 912 */     writeStartArray();
/*     */   }
/*     */ 
/*     */   public final void writeObjectFieldStart(String fieldName)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 931 */     writeFieldName(fieldName);
/* 932 */     writeStartObject();
/*     */   }
/*     */ 
/*     */   public final void writeObjectField(String fieldName, Object pojo)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 947 */     writeFieldName(fieldName);
/* 948 */     writeObject(pojo);
/*     */   }
/*     */ 
/*     */   public abstract void copyCurrentEvent(JsonParser paramJsonParser)
/*     */     throws IOException, JsonProcessingException;
/*     */ 
/*     */   public abstract void copyCurrentStructure(JsonParser paramJsonParser)
/*     */     throws IOException, JsonProcessingException;
/*     */ 
/*     */   public abstract JsonStreamContext getOutputContext();
/*     */ 
/*     */   public abstract void flush()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract boolean isClosed();
/*     */ 
/*     */   public abstract void close()
/*     */     throws IOException;
/*     */ 
/*     */   public static enum Feature
/*     */   {
/*  50 */     AUTO_CLOSE_TARGET(true), 
/*     */ 
/*  62 */     AUTO_CLOSE_JSON_CONTENT(true), 
/*     */ 
/*  71 */     QUOTE_FIELD_NAMES(true), 
/*     */ 
/*  85 */     QUOTE_NON_NUMERIC_NUMBERS(true), 
/*     */ 
/* 104 */     WRITE_NUMBERS_AS_STRINGS(false), 
/*     */ 
/* 119 */     FLUSH_PASSED_TO_STREAM(true);
/*     */ 
/*     */     final boolean _defaultState;
/*     */     final int _mask;
/*     */ 
/*     */     public static int collectDefaults()
/*     */     {
/* 133 */       int flags = 0;
/* 134 */       for (Feature f : values()) {
/* 135 */         if (f.enabledByDefault()) {
/* 136 */           flags |= f.getMask();
/*     */         }
/*     */       }
/* 139 */       return flags;
/*     */     }
/*     */ 
/*     */     private Feature(boolean defaultState) {
/* 143 */       this._defaultState = defaultState;
/* 144 */       this._mask = (1 << ordinal());
/*     */     }
/*     */     public boolean enabledByDefault() {
/* 147 */       return this._defaultState;
/*     */     }
/* 149 */     public int getMask() { return this._mask;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.JsonGenerator
 * JD-Core Version:    0.6.0
 */