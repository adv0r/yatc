/*     */ package org.codehaus.jackson.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.codehaus.jackson.Base64Variant;
/*     */ import org.codehaus.jackson.JsonParseException;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonParser.Feature;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonStreamContext;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.io.NumberInput;
/*     */ 
/*     */ public abstract class JsonParserMinimalBase extends JsonParser
/*     */ {
/*     */   protected static final int INT_TAB = 9;
/*     */   protected static final int INT_LF = 10;
/*     */   protected static final int INT_CR = 13;
/*     */   protected static final int INT_SPACE = 32;
/*     */   protected static final int INT_LBRACKET = 91;
/*     */   protected static final int INT_RBRACKET = 93;
/*     */   protected static final int INT_LCURLY = 123;
/*     */   protected static final int INT_RCURLY = 125;
/*     */   protected static final int INT_QUOTE = 34;
/*     */   protected static final int INT_BACKSLASH = 92;
/*     */   protected static final int INT_SLASH = 47;
/*     */   protected static final int INT_COLON = 58;
/*     */   protected static final int INT_COMMA = 44;
/*     */   protected static final int INT_ASTERISK = 42;
/*     */   protected static final int INT_APOSTROPHE = 39;
/*     */   protected static final int INT_b = 98;
/*     */   protected static final int INT_f = 102;
/*     */   protected static final int INT_n = 110;
/*     */   protected static final int INT_r = 114;
/*     */   protected static final int INT_t = 116;
/*     */   protected static final int INT_u = 117;
/*     */ 
/*     */   protected JsonParserMinimalBase()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected JsonParserMinimalBase(int features)
/*     */   {
/*  56 */     super(features);
/*     */   }
/*     */ 
/*     */   public abstract JsonToken nextToken()
/*     */     throws IOException, JsonParseException;
/*     */ 
/*     */   public JsonParser skipChildren()
/*     */     throws IOException, JsonParseException
/*     */   {
/*  86 */     if ((this._currToken != JsonToken.START_OBJECT) && (this._currToken != JsonToken.START_ARRAY))
/*     */     {
/*  88 */       return this;
/*     */     }
/*  90 */     int open = 1;
/*     */     while (true)
/*     */     {
/*  96 */       JsonToken t = nextToken();
/*  97 */       if (t == null) {
/*  98 */         _handleEOF();
/*     */ 
/* 103 */         return this;
/*     */       }
/* 105 */       switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[t.ordinal()]) {
/*     */       case 1:
/*     */       case 2:
/* 108 */         open++;
/* 109 */         break;
/*     */       case 3:
/*     */       case 4:
/* 112 */         open--; if (open != 0) break;
/* 113 */         return this;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract void _handleEOF()
/*     */     throws JsonParseException;
/*     */ 
/*     */   public abstract String getCurrentName()
/*     */     throws IOException, JsonParseException;
/*     */ 
/*     */   public abstract void close()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract boolean isClosed();
/*     */ 
/*     */   public abstract JsonStreamContext getParsingContext();
/*     */ 
/*     */   public abstract String getText()
/*     */     throws IOException, JsonParseException;
/*     */ 
/*     */   public abstract char[] getTextCharacters()
/*     */     throws IOException, JsonParseException;
/*     */ 
/*     */   public abstract boolean hasTextCharacters();
/*     */ 
/*     */   public abstract int getTextLength()
/*     */     throws IOException, JsonParseException;
/*     */ 
/*     */   public abstract int getTextOffset()
/*     */     throws IOException, JsonParseException;
/*     */ 
/*     */   public abstract byte[] getBinaryValue(Base64Variant paramBase64Variant)
/*     */     throws IOException, JsonParseException;
/*     */ 
/*     */   public boolean getValueAsBoolean(boolean defaultValue)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 185 */     if (this._currToken != null) {
/* 186 */       switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
/*     */       case 5:
/* 188 */         return getIntValue() != 0;
/*     */       case 6:
/* 190 */         return true;
/*     */       case 7:
/*     */       case 8:
/* 193 */         return false;
/*     */       case 9:
/* 196 */         Object value = getEmbeddedObject();
/* 197 */         if (!(value instanceof Boolean)) break;
/* 198 */         return ((Boolean)value).booleanValue();
/*     */       case 10:
/* 202 */         String str = getText().trim();
/* 203 */         if ("true".equals(str)) {
/* 204 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 209 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   public int getValueAsInt(int defaultValue)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 215 */     if (this._currToken != null) {
/* 216 */       switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
/*     */       case 5:
/*     */       case 11:
/* 219 */         return getIntValue();
/*     */       case 6:
/* 221 */         return 1;
/*     */       case 7:
/*     */       case 8:
/* 224 */         return 0;
/*     */       case 10:
/* 226 */         return NumberInput.parseAsInt(getText(), defaultValue);
/*     */       case 9:
/* 229 */         Object value = getEmbeddedObject();
/* 230 */         if (!(value instanceof Number)) break;
/* 231 */         return ((Number)value).intValue();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 236 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   public long getValueAsLong(long defaultValue)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 242 */     if (this._currToken != null) {
/* 243 */       switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
/*     */       case 5:
/*     */       case 11:
/* 246 */         return getLongValue();
/*     */       case 6:
/* 248 */         return 1L;
/*     */       case 7:
/*     */       case 8:
/* 251 */         return 0L;
/*     */       case 10:
/* 253 */         return NumberInput.parseAsLong(getText(), defaultValue);
/*     */       case 9:
/* 256 */         Object value = getEmbeddedObject();
/* 257 */         if (!(value instanceof Number)) break;
/* 258 */         return ((Number)value).longValue();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 263 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   public double getValueAsDouble(double defaultValue)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 269 */     if (this._currToken != null) {
/* 270 */       switch (1.$SwitchMap$org$codehaus$jackson$JsonToken[this._currToken.ordinal()]) {
/*     */       case 5:
/*     */       case 11:
/* 273 */         return getDoubleValue();
/*     */       case 6:
/* 275 */         return 1.0D;
/*     */       case 7:
/*     */       case 8:
/* 278 */         return 0.0D;
/*     */       case 10:
/* 280 */         return NumberInput.parseAsDouble(getText(), defaultValue);
/*     */       case 9:
/* 283 */         Object value = getEmbeddedObject();
/* 284 */         if (!(value instanceof Number)) break;
/* 285 */         return ((Number)value).doubleValue();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 290 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   protected void _reportUnexpectedChar(int ch, String comment)
/*     */     throws JsonParseException
/*     */   {
/* 302 */     String msg = "Unexpected character (" + _getCharDesc(ch) + ")";
/* 303 */     if (comment != null) {
/* 304 */       msg = msg + ": " + comment;
/*     */     }
/* 306 */     _reportError(msg);
/*     */   }
/*     */ 
/*     */   protected void _reportInvalidEOF()
/*     */     throws JsonParseException
/*     */   {
/* 312 */     _reportInvalidEOF(" in " + this._currToken);
/*     */   }
/*     */ 
/*     */   protected void _reportInvalidEOF(String msg)
/*     */     throws JsonParseException
/*     */   {
/* 318 */     _reportError("Unexpected end-of-input" + msg);
/*     */   }
/*     */ 
/*     */   protected void _throwInvalidSpace(int i)
/*     */     throws JsonParseException
/*     */   {
/* 324 */     char c = (char)i;
/* 325 */     String msg = "Illegal character (" + _getCharDesc(c) + "): only regular white space (\\r, \\n, \\t) is allowed between tokens";
/* 326 */     _reportError(msg);
/*     */   }
/*     */ 
/*     */   protected void _throwUnquotedSpace(int i, String ctxtDesc)
/*     */     throws JsonParseException
/*     */   {
/* 338 */     if ((!isEnabled(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS)) || (i >= 32)) {
/* 339 */       char c = (char)i;
/* 340 */       String msg = "Illegal unquoted character (" + _getCharDesc(c) + "): has to be escaped using backslash to be included in " + ctxtDesc;
/* 341 */       _reportError(msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected char _handleUnrecognizedCharacterEscape(char ch)
/*     */     throws JsonProcessingException
/*     */   {
/* 348 */     if (!isEnabled(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)) {
/* 349 */       _reportError("Unrecognized character escape " + _getCharDesc(ch));
/*     */     }
/* 351 */     return ch;
/*     */   }
/*     */ 
/*     */   protected static final String _getCharDesc(int ch)
/*     */   {
/* 362 */     char c = (char)ch;
/* 363 */     if (Character.isISOControl(c)) {
/* 364 */       return "(CTRL-CHAR, code " + ch + ")";
/*     */     }
/* 366 */     if (ch > 255) {
/* 367 */       return "'" + c + "' (code " + ch + " / 0x" + Integer.toHexString(ch) + ")";
/*     */     }
/* 369 */     return "'" + c + "' (code " + ch + ")";
/*     */   }
/*     */ 
/*     */   protected final void _reportError(String msg)
/*     */     throws JsonParseException
/*     */   {
/* 375 */     throw _constructError(msg);
/*     */   }
/*     */ 
/*     */   protected final void _wrapError(String msg, Throwable t)
/*     */     throws JsonParseException
/*     */   {
/* 381 */     throw _constructError(msg, t);
/*     */   }
/*     */ 
/*     */   protected final void _throwInternal()
/*     */   {
/* 386 */     throw new RuntimeException("Internal error: this code path should never get executed");
/*     */   }
/*     */ 
/*     */   protected final JsonParseException _constructError(String msg, Throwable t)
/*     */   {
/* 391 */     return new JsonParseException(msg, getCurrentLocation(), t);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.impl.JsonParserMinimalBase
 * JD-Core Version:    0.6.0
 */