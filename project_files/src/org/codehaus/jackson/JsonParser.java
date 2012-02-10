/*      */ package org.codehaus.jackson;
/*      */ 
/*      */ import java.io.Closeable;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Writer;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import org.codehaus.jackson.type.TypeReference;
/*      */ 
/*      */ public abstract class JsonParser
/*      */   implements Closeable, Versioned
/*      */ {
/*      */   private static final int MIN_BYTE_I = -128;
/*      */   private static final int MAX_BYTE_I = 127;
/*      */   private static final int MIN_SHORT_I = -32768;
/*      */   private static final int MAX_SHORT_I = 32767;
/*      */   protected int _features;
/*      */   protected JsonToken _currToken;
/*      */   protected JsonToken _lastClearedToken;
/*      */ 
/*      */   protected JsonParser()
/*      */   {
/*      */   }
/*      */ 
/*      */   protected JsonParser(int features)
/*      */   {
/*  265 */     this._features = features;
/*      */   }
/*      */ 
/*      */   public abstract ObjectCodec getCodec();
/*      */ 
/*      */   public abstract void setCodec(ObjectCodec paramObjectCodec);
/*      */ 
/*      */   public Version version()
/*      */   {
/*  290 */     return Version.unknownVersion();
/*      */   }
/*      */ 
/*      */   public abstract void close()
/*      */     throws IOException;
/*      */ 
/*      */   public int releaseBuffered(OutputStream out)
/*      */     throws IOException
/*      */   {
/*  340 */     return -1;
/*      */   }
/*      */ 
/*      */   public int releaseBuffered(Writer w)
/*      */     throws IOException
/*      */   {
/*  362 */     return -1;
/*      */   }
/*      */ 
/*      */   public JsonParser enable(Feature f)
/*      */   {
/*  379 */     this._features |= f.getMask();
/*  380 */     return this;
/*      */   }
/*      */ 
/*      */   public JsonParser disable(Feature f)
/*      */   {
/*  391 */     this._features &= (f.getMask() ^ 0xFFFFFFFF);
/*  392 */     return this;
/*      */   }
/*      */ 
/*      */   public JsonParser configure(Feature f, boolean state)
/*      */   {
/*  403 */     if (state)
/*  404 */       enableFeature(f);
/*      */     else {
/*  406 */       disableFeature(f);
/*      */     }
/*  408 */     return this;
/*      */   }
/*      */ 
/*      */   public boolean isEnabled(Feature f)
/*      */   {
/*  418 */     return (this._features & f.getMask()) != 0;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public void setFeature(Feature f, boolean state) {
/*  424 */     configure(f, state);
/*      */   }
/*      */   /** @deprecated */
/*      */   public void enableFeature(Feature f) {
/*  429 */     enable(f);
/*      */   }
/*      */   /** @deprecated */
/*      */   public void disableFeature(Feature f) {
/*  434 */     disable(f);
/*      */   }
/*      */   /** @deprecated */
/*      */   public final boolean isFeatureEnabled(Feature f) {
/*  439 */     return isEnabled(f);
/*      */   }
/*      */ 
/*      */   public abstract JsonToken nextToken()
/*      */     throws IOException, JsonParseException;
/*      */ 
/*      */   public JsonToken nextValue()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  486 */     JsonToken t = nextToken();
/*  487 */     if (t == JsonToken.FIELD_NAME) {
/*  488 */       t = nextToken();
/*      */     }
/*  490 */     return t;
/*      */   }
/*      */ 
/*      */   public abstract JsonParser skipChildren()
/*      */     throws IOException, JsonParseException;
/*      */ 
/*      */   public abstract boolean isClosed();
/*      */ 
/*      */   public JsonToken getCurrentToken()
/*      */   {
/*  538 */     return this._currToken;
/*      */   }
/*      */ 
/*      */   public boolean hasCurrentToken()
/*      */   {
/*  553 */     return this._currToken != null;
/*      */   }
/*      */ 
/*      */   public void clearCurrentToken()
/*      */   {
/*  570 */     if (this._currToken != null) {
/*  571 */       this._lastClearedToken = this._currToken;
/*  572 */       this._currToken = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public abstract String getCurrentName()
/*      */     throws IOException, JsonParseException;
/*      */ 
/*      */   public abstract JsonStreamContext getParsingContext();
/*      */ 
/*      */   public abstract JsonLocation getTokenLocation();
/*      */ 
/*      */   public abstract JsonLocation getCurrentLocation();
/*      */ 
/*      */   public JsonToken getLastClearedToken()
/*      */   {
/*  619 */     return this._lastClearedToken;
/*      */   }
/*      */ 
/*      */   public boolean isExpectedStartArrayToken()
/*      */   {
/*  642 */     return getCurrentToken() == JsonToken.START_ARRAY;
/*      */   }
/*      */ 
/*      */   public abstract String getText()
/*      */     throws IOException, JsonParseException;
/*      */ 
/*      */   public abstract char[] getTextCharacters()
/*      */     throws IOException, JsonParseException;
/*      */ 
/*      */   public abstract int getTextLength()
/*      */     throws IOException, JsonParseException;
/*      */ 
/*      */   public abstract int getTextOffset()
/*      */     throws IOException, JsonParseException;
/*      */ 
/*      */   public boolean hasTextCharacters()
/*      */   {
/*  730 */     return false;
/*      */   }
/*      */ 
/*      */   public abstract Number getNumberValue()
/*      */     throws IOException, JsonParseException;
/*      */ 
/*      */   public abstract NumberType getNumberType()
/*      */     throws IOException, JsonParseException;
/*      */ 
/*      */   public byte getByteValue()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  773 */     int value = getIntValue();
/*      */ 
/*  775 */     if ((value < -128) || (value > 127)) {
/*  776 */       throw _constructError("Numeric value (" + getText() + ") out of range of Java byte");
/*      */     }
/*  778 */     return (byte)value;
/*      */   }
/*      */ 
/*      */   public short getShortValue()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  797 */     int value = getIntValue();
/*  798 */     if ((value < -32768) || (value > 32767)) {
/*  799 */       throw _constructError("Numeric value (" + getText() + ") out of range of Java short");
/*      */     }
/*  801 */     return (short)value;
/*      */   }
/*      */ 
/*      */   public abstract int getIntValue()
/*      */     throws IOException, JsonParseException;
/*      */ 
/*      */   public abstract long getLongValue()
/*      */     throws IOException, JsonParseException;
/*      */ 
/*      */   public abstract BigInteger getBigIntegerValue()
/*      */     throws IOException, JsonParseException;
/*      */ 
/*      */   public abstract float getFloatValue()
/*      */     throws IOException, JsonParseException;
/*      */ 
/*      */   public abstract double getDoubleValue()
/*      */     throws IOException, JsonParseException;
/*      */ 
/*      */   public abstract BigDecimal getDecimalValue()
/*      */     throws IOException, JsonParseException;
/*      */ 
/*      */   public boolean getBooleanValue()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  910 */     if (this._currToken == JsonToken.VALUE_TRUE) return true;
/*  911 */     if (this._currToken == JsonToken.VALUE_FALSE) return false;
/*  912 */     throw new JsonParseException("Current token (" + this._currToken + ") not of boolean type", getCurrentLocation());
/*      */   }
/*      */ 
/*      */   public Object getEmbeddedObject()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  930 */     return null;
/*      */   }
/*      */ 
/*      */   public abstract byte[] getBinaryValue(Base64Variant paramBase64Variant)
/*      */     throws IOException, JsonParseException;
/*      */ 
/*      */   public byte[] getBinaryValue()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  970 */     return getBinaryValue(Base64Variants.getDefaultVariant());
/*      */   }
/*      */ 
/*      */   public int getValueAsInt()
/*      */     throws IOException, JsonParseException
/*      */   {
/*  993 */     return getValueAsInt(0);
/*      */   }
/*      */ 
/*      */   public int getValueAsInt(int defaultValue)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1010 */     return defaultValue;
/*      */   }
/*      */ 
/*      */   public long getValueAsLong()
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1027 */     return getValueAsInt(0);
/*      */   }
/*      */ 
/*      */   public long getValueAsLong(long defaultValue)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1044 */     return defaultValue;
/*      */   }
/*      */ 
/*      */   public double getValueAsDouble()
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1061 */     return getValueAsDouble(0.0D);
/*      */   }
/*      */ 
/*      */   public double getValueAsDouble(double defaultValue)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1078 */     return defaultValue;
/*      */   }
/*      */ 
/*      */   public boolean getValueAsBoolean()
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1095 */     return getValueAsBoolean(false);
/*      */   }
/*      */ 
/*      */   public boolean getValueAsBoolean(boolean defaultValue)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1112 */     return defaultValue;
/*      */   }
/*      */ 
/*      */   public <T> T readValueAs(Class<T> valueType)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1145 */     ObjectCodec codec = getCodec();
/* 1146 */     if (codec == null) {
/* 1147 */       throw new IllegalStateException("No ObjectCodec defined for the parser, can not deserialize JSON into Java objects");
/*      */     }
/* 1149 */     return codec.readValue(this, valueType);
/*      */   }
/*      */ 
/*      */   public <T> T readValueAs(TypeReference<?> valueTypeRef)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1174 */     ObjectCodec codec = getCodec();
/* 1175 */     if (codec == null) {
/* 1176 */       throw new IllegalStateException("No ObjectCodec defined for the parser, can not deserialize JSON into Java objects");
/*      */     }
/*      */ 
/* 1181 */     return codec.readValue(this, valueTypeRef);
/*      */   }
/*      */ 
/*      */   public JsonNode readValueAsTree()
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1194 */     ObjectCodec codec = getCodec();
/* 1195 */     if (codec == null) {
/* 1196 */       throw new IllegalStateException("No ObjectCodec defined for the parser, can not deserialize JSON into JsonNode tree");
/*      */     }
/* 1198 */     return codec.readTree(this);
/*      */   }
/*      */ 
/*      */   protected JsonParseException _constructError(String msg)
/*      */   {
/* 1213 */     return new JsonParseException(msg, getCurrentLocation());
/*      */   }
/*      */ 
/*      */   public static enum Feature
/*      */   {
/*   64 */     AUTO_CLOSE_SOURCE(true), 
/*      */ 
/*   80 */     ALLOW_COMMENTS(false), 
/*      */ 
/*   96 */     ALLOW_UNQUOTED_FIELD_NAMES(false), 
/*      */ 
/*  114 */     ALLOW_SINGLE_QUOTES(false), 
/*      */ 
/*  131 */     ALLOW_UNQUOTED_CONTROL_CHARS(false), 
/*      */ 
/*  146 */     ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER(false), 
/*      */ 
/*  163 */     INTERN_FIELD_NAMES(true), 
/*      */ 
/*  173 */     CANONICALIZE_FIELD_NAMES(true);
/*      */ 
/*      */     final boolean _defaultState;
/*      */ 
/*      */     public static int collectDefaults()
/*      */     {
/*  204 */       int flags = 0;
/*  205 */       for (Feature f : values()) {
/*  206 */         if (f.enabledByDefault()) {
/*  207 */           flags |= f.getMask();
/*      */         }
/*      */       }
/*  210 */       return flags;
/*      */     }
/*      */ 
/*      */     private Feature(boolean defaultState) {
/*  214 */       this._defaultState = defaultState;
/*      */     }
/*      */     public boolean enabledByDefault() {
/*  217 */       return this._defaultState;
/*      */     }
/*  219 */     public boolean enabledIn(int flags) { return (flags & getMask()) != 0; } 
/*      */     public int getMask() {
/*  221 */       return 1 << ordinal();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static enum NumberType
/*      */   {
/*   45 */     INT, LONG, BIG_INTEGER, FLOAT, DOUBLE, BIG_DECIMAL;
/*      */   }
/*      */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.JsonParser
 * JD-Core Version:    0.6.0
 */