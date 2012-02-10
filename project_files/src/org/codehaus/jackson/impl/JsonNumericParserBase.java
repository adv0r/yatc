/*     */ package org.codehaus.jackson.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import org.codehaus.jackson.JsonParseException;
/*     */ import org.codehaus.jackson.JsonParser.NumberType;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.io.IOContext;
/*     */ import org.codehaus.jackson.io.NumberInput;
/*     */ import org.codehaus.jackson.util.TextBuffer;
/*     */ 
/*     */ public abstract class JsonNumericParserBase extends JsonParserBase
/*     */ {
/*     */   protected static final int NR_UNKNOWN = 0;
/*     */   protected static final int NR_INT = 1;
/*     */   protected static final int NR_LONG = 2;
/*     */   protected static final int NR_BIGINT = 4;
/*     */   protected static final int NR_DOUBLE = 8;
/*     */   protected static final int NR_BIGDECIMAL = 16;
/*  42 */   static final BigDecimal BD_MIN_LONG = new BigDecimal(-9223372036854775808L);
/*  43 */   static final BigDecimal BD_MAX_LONG = new BigDecimal(9223372036854775807L);
/*     */ 
/*  45 */   static final BigDecimal BD_MIN_INT = new BigDecimal(-9223372036854775808L);
/*  46 */   static final BigDecimal BD_MAX_INT = new BigDecimal(9223372036854775807L);
/*     */   static final long MIN_INT_L = -2147483648L;
/*     */   static final long MAX_INT_L = 2147483647L;
/*     */   static final double MIN_LONG_D = -9.223372036854776E+18D;
/*     */   static final double MAX_LONG_D = 9.223372036854776E+18D;
/*     */   static final double MIN_INT_D = -2147483648.0D;
/*     */   static final double MAX_INT_D = 2147483647.0D;
/*     */   protected static final int INT_0 = 48;
/*     */   protected static final int INT_1 = 49;
/*     */   protected static final int INT_2 = 50;
/*     */   protected static final int INT_3 = 51;
/*     */   protected static final int INT_4 = 52;
/*     */   protected static final int INT_5 = 53;
/*     */   protected static final int INT_6 = 54;
/*     */   protected static final int INT_7 = 55;
/*     */   protected static final int INT_8 = 56;
/*     */   protected static final int INT_9 = 57;
/*     */   protected static final int INT_MINUS = 45;
/*     */   protected static final int INT_PLUS = 43;
/*     */   protected static final int INT_DECIMAL_POINT = 46;
/*     */   protected static final int INT_e = 101;
/*     */   protected static final int INT_E = 69;
/*     */   protected static final char CHAR_NULL = '\000';
/*  92 */   protected int _numTypesValid = 0;
/*     */   protected int _numberInt;
/*     */   protected long _numberLong;
/*     */   protected double _numberDouble;
/*     */   protected BigInteger _numberBigInt;
/*     */   protected BigDecimal _numberBigDecimal;
/*     */   protected boolean _numberNegative;
/*     */   protected int _intLength;
/*     */   protected int _fractLength;
/*     */   protected int _expLength;
/*     */ 
/*     */   protected JsonNumericParserBase(IOContext ctxt, int features)
/*     */   {
/* 144 */     super(ctxt, features);
/*     */   }
/*     */ 
/*     */   protected final JsonToken reset(boolean negative, int intLen, int fractLen, int expLen)
/*     */   {
/* 149 */     if ((fractLen < 1) && (expLen < 1)) {
/* 150 */       return resetInt(negative, intLen);
/*     */     }
/* 152 */     return resetFloat(negative, intLen, fractLen, expLen);
/*     */   }
/*     */ 
/*     */   protected final JsonToken resetInt(boolean negative, int intLen)
/*     */   {
/* 157 */     this._numberNegative = negative;
/* 158 */     this._intLength = intLen;
/* 159 */     this._fractLength = 0;
/* 160 */     this._expLength = 0;
/* 161 */     this._numTypesValid = 0;
/* 162 */     return JsonToken.VALUE_NUMBER_INT;
/*     */   }
/*     */ 
/*     */   protected final JsonToken resetFloat(boolean negative, int intLen, int fractLen, int expLen)
/*     */   {
/* 167 */     this._numberNegative = negative;
/* 168 */     this._intLength = intLen;
/* 169 */     this._fractLength = fractLen;
/* 170 */     this._expLength = expLen;
/* 171 */     this._numTypesValid = 0;
/* 172 */     return JsonToken.VALUE_NUMBER_FLOAT;
/*     */   }
/*     */ 
/*     */   public Number getNumberValue()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 184 */     if (this._numTypesValid == 0) {
/* 185 */       _parseNumericValue(0);
/*     */     }
/*     */ 
/* 188 */     if (this._currToken == JsonToken.VALUE_NUMBER_INT) {
/* 189 */       if ((this._numTypesValid & 0x1) != 0) {
/* 190 */         return Integer.valueOf(this._numberInt);
/*     */       }
/* 192 */       if ((this._numTypesValid & 0x2) != 0) {
/* 193 */         return Long.valueOf(this._numberLong);
/*     */       }
/* 195 */       if ((this._numTypesValid & 0x4) != 0) {
/* 196 */         return this._numberBigInt;
/*     */       }
/*     */ 
/* 199 */       return this._numberBigDecimal;
/*     */     }
/*     */ 
/* 205 */     if ((this._numTypesValid & 0x10) != 0) {
/* 206 */       return this._numberBigDecimal;
/*     */     }
/* 208 */     if ((this._numTypesValid & 0x8) == 0) {
/* 209 */       _throwInternal();
/*     */     }
/* 211 */     return Double.valueOf(this._numberDouble);
/*     */   }
/*     */ 
/*     */   public JsonParser.NumberType getNumberType()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 217 */     if (this._numTypesValid == 0) {
/* 218 */       _parseNumericValue(0);
/*     */     }
/* 220 */     if (this._currToken == JsonToken.VALUE_NUMBER_INT) {
/* 221 */       if ((this._numTypesValid & 0x1) != 0) {
/* 222 */         return JsonParser.NumberType.INT;
/*     */       }
/* 224 */       if ((this._numTypesValid & 0x2) != 0) {
/* 225 */         return JsonParser.NumberType.LONG;
/*     */       }
/* 227 */       return JsonParser.NumberType.BIG_INTEGER;
/*     */     }
/*     */ 
/* 236 */     if ((this._numTypesValid & 0x10) != 0) {
/* 237 */       return JsonParser.NumberType.BIG_DECIMAL;
/*     */     }
/* 239 */     return JsonParser.NumberType.DOUBLE;
/*     */   }
/*     */ 
/*     */   public int getIntValue()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 245 */     if ((this._numTypesValid & 0x1) == 0) {
/* 246 */       if (this._numTypesValid == 0) {
/* 247 */         _parseNumericValue(1);
/*     */       }
/* 249 */       if ((this._numTypesValid & 0x1) == 0) {
/* 250 */         convertNumberToInt();
/*     */       }
/*     */     }
/* 253 */     return this._numberInt;
/*     */   }
/*     */ 
/*     */   public long getLongValue()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 259 */     if ((this._numTypesValid & 0x2) == 0) {
/* 260 */       if (this._numTypesValid == 0) {
/* 261 */         _parseNumericValue(2);
/*     */       }
/* 263 */       if ((this._numTypesValid & 0x2) == 0) {
/* 264 */         convertNumberToLong();
/*     */       }
/*     */     }
/* 267 */     return this._numberLong;
/*     */   }
/*     */ 
/*     */   public BigInteger getBigIntegerValue()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 273 */     if ((this._numTypesValid & 0x4) == 0) {
/* 274 */       if (this._numTypesValid == 0) {
/* 275 */         _parseNumericValue(4);
/*     */       }
/* 277 */       if ((this._numTypesValid & 0x4) == 0) {
/* 278 */         convertNumberToBigInteger();
/*     */       }
/*     */     }
/* 281 */     return this._numberBigInt;
/*     */   }
/*     */ 
/*     */   public float getFloatValue()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 287 */     double value = getDoubleValue();
/*     */ 
/* 296 */     return (float)value;
/*     */   }
/*     */ 
/*     */   public double getDoubleValue()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 302 */     if ((this._numTypesValid & 0x8) == 0) {
/* 303 */       if (this._numTypesValid == 0) {
/* 304 */         _parseNumericValue(8);
/*     */       }
/* 306 */       if ((this._numTypesValid & 0x8) == 0) {
/* 307 */         convertNumberToDouble();
/*     */       }
/*     */     }
/* 310 */     return this._numberDouble;
/*     */   }
/*     */ 
/*     */   public BigDecimal getDecimalValue()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 316 */     if ((this._numTypesValid & 0x10) == 0) {
/* 317 */       if (this._numTypesValid == 0) {
/* 318 */         _parseNumericValue(16);
/*     */       }
/* 320 */       if ((this._numTypesValid & 0x10) == 0) {
/* 321 */         convertNumberToBigDecimal();
/*     */       }
/*     */     }
/* 324 */     return this._numberBigDecimal;
/*     */   }
/*     */ 
/*     */   protected void _parseNumericValue(int expType)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 347 */     if (this._currToken == JsonToken.VALUE_NUMBER_INT) {
/* 348 */       char[] buf = this._textBuffer.getTextBuffer();
/* 349 */       int offset = this._textBuffer.getTextOffset();
/* 350 */       int len = this._intLength;
/* 351 */       if (this._numberNegative) {
/* 352 */         offset++;
/*     */       }
/* 354 */       if (len <= 9) {
/* 355 */         int i = NumberInput.parseInt(buf, offset, len);
/* 356 */         this._numberInt = (this._numberNegative ? -i : i);
/* 357 */         this._numTypesValid = 1;
/* 358 */         return;
/*     */       }
/* 360 */       if (len <= 18) {
/* 361 */         long l = NumberInput.parseLong(buf, offset, len);
/* 362 */         if (this._numberNegative) {
/* 363 */           l = -l;
/*     */         }
/*     */ 
/* 366 */         if (len == 10) {
/* 367 */           if (this._numberNegative) {
/* 368 */             if (l >= -2147483648L) {
/* 369 */               this._numberInt = (int)l;
/* 370 */               this._numTypesValid = 1;
/* 371 */               return;
/*     */             }
/*     */           }
/* 374 */           else if (l <= 2147483647L) {
/* 375 */             this._numberInt = (int)l;
/* 376 */             this._numTypesValid = 1;
/* 377 */             return;
/*     */           }
/*     */         }
/*     */ 
/* 381 */         this._numberLong = l;
/* 382 */         this._numTypesValid = 2;
/* 383 */         return;
/*     */       }
/* 385 */       _parseSlowIntValue(expType, buf, offset, len);
/* 386 */       return;
/*     */     }
/* 388 */     if (this._currToken == JsonToken.VALUE_NUMBER_FLOAT) {
/* 389 */       _parseSlowFloatValue(expType);
/* 390 */       return;
/*     */     }
/* 392 */     _reportError("Current token (" + this._currToken + ") not numeric, can not use numeric value accessors");
/*     */   }
/*     */ 
/*     */   private final void _parseSlowFloatValue(int expType)
/*     */     throws IOException, JsonParseException
/*     */   {
/*     */     try
/*     */     {
/* 406 */       if (expType == 16) {
/* 407 */         this._numberBigDecimal = this._textBuffer.contentsAsDecimal();
/* 408 */         this._numTypesValid = 16;
/*     */       }
/*     */       else {
/* 411 */         this._numberDouble = this._textBuffer.contentsAsDouble();
/* 412 */         this._numTypesValid = 8;
/*     */       }
/*     */     }
/*     */     catch (NumberFormatException nex) {
/* 416 */       _wrapError("Malformed numeric value '" + this._textBuffer.contentsAsString() + "'", nex);
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void _parseSlowIntValue(int expType, char[] buf, int offset, int len)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 423 */     String numStr = this._textBuffer.contentsAsString();
/*     */     try
/*     */     {
/* 426 */       if (NumberInput.inLongRange(buf, offset, len, this._numberNegative))
/*     */       {
/* 428 */         this._numberLong = Long.parseLong(numStr);
/* 429 */         this._numTypesValid = 2;
/*     */       }
/*     */       else {
/* 432 */         this._numberBigInt = new BigInteger(numStr);
/* 433 */         this._numTypesValid = 4;
/*     */       }
/*     */     }
/*     */     catch (NumberFormatException nex) {
/* 437 */       _wrapError("Malformed numeric value '" + numStr + "'", nex);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void convertNumberToInt()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 451 */     if ((this._numTypesValid & 0x2) != 0)
/*     */     {
/* 453 */       int result = (int)this._numberLong;
/* 454 */       if (result != this._numberLong) {
/* 455 */         _reportError("Numeric value (" + getText() + ") out of range of int");
/*     */       }
/* 457 */       this._numberInt = result;
/* 458 */     } else if ((this._numTypesValid & 0x4) != 0)
/*     */     {
/* 460 */       this._numberInt = this._numberBigInt.intValue();
/* 461 */     } else if ((this._numTypesValid & 0x8) != 0)
/*     */     {
/* 463 */       if ((this._numberDouble < -2147483648.0D) || (this._numberDouble > 2147483647.0D)) {
/* 464 */         reportOverflowInt();
/*     */       }
/* 466 */       this._numberInt = (int)this._numberDouble;
/* 467 */     } else if ((this._numTypesValid & 0x10) != 0) {
/* 468 */       if ((BD_MIN_INT.compareTo(this._numberBigDecimal) > 0) || (BD_MAX_INT.compareTo(this._numberBigDecimal) < 0))
/*     */       {
/* 470 */         reportOverflowInt();
/*     */       }
/* 472 */       this._numberInt = this._numberBigDecimal.intValue();
/*     */     } else {
/* 474 */       _throwInternal();
/*     */     }
/*     */ 
/* 477 */     this._numTypesValid |= 1;
/*     */   }
/*     */ 
/*     */   protected void convertNumberToLong()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 483 */     if ((this._numTypesValid & 0x1) != 0) {
/* 484 */       this._numberLong = this._numberInt;
/* 485 */     } else if ((this._numTypesValid & 0x4) != 0)
/*     */     {
/* 487 */       this._numberLong = this._numberBigInt.longValue();
/* 488 */     } else if ((this._numTypesValid & 0x8) != 0)
/*     */     {
/* 490 */       if ((this._numberDouble < -9.223372036854776E+18D) || (this._numberDouble > 9.223372036854776E+18D)) {
/* 491 */         reportOverflowLong();
/*     */       }
/* 493 */       this._numberLong = ()this._numberDouble;
/* 494 */     } else if ((this._numTypesValid & 0x10) != 0) {
/* 495 */       if ((BD_MIN_LONG.compareTo(this._numberBigDecimal) > 0) || (BD_MAX_LONG.compareTo(this._numberBigDecimal) < 0))
/*     */       {
/* 497 */         reportOverflowLong();
/*     */       }
/* 499 */       this._numberLong = this._numberBigDecimal.longValue();
/*     */     } else {
/* 501 */       _throwInternal();
/*     */     }
/*     */ 
/* 504 */     this._numTypesValid |= 2;
/*     */   }
/*     */ 
/*     */   protected void convertNumberToBigInteger()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 510 */     if ((this._numTypesValid & 0x10) != 0)
/*     */     {
/* 512 */       this._numberBigInt = this._numberBigDecimal.toBigInteger();
/* 513 */     } else if ((this._numTypesValid & 0x2) != 0)
/* 514 */       this._numberBigInt = BigInteger.valueOf(this._numberLong);
/* 515 */     else if ((this._numTypesValid & 0x1) != 0)
/* 516 */       this._numberBigInt = BigInteger.valueOf(this._numberInt);
/* 517 */     else if ((this._numTypesValid & 0x8) != 0)
/* 518 */       this._numberBigInt = BigDecimal.valueOf(this._numberDouble).toBigInteger();
/*     */     else {
/* 520 */       _throwInternal();
/*     */     }
/* 522 */     this._numTypesValid |= 4;
/*     */   }
/*     */ 
/*     */   protected void convertNumberToDouble()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 534 */     if ((this._numTypesValid & 0x10) != 0)
/* 535 */       this._numberDouble = this._numberBigDecimal.doubleValue();
/* 536 */     else if ((this._numTypesValid & 0x4) != 0)
/* 537 */       this._numberDouble = this._numberBigInt.doubleValue();
/* 538 */     else if ((this._numTypesValid & 0x2) != 0)
/* 539 */       this._numberDouble = this._numberLong;
/* 540 */     else if ((this._numTypesValid & 0x1) != 0)
/* 541 */       this._numberDouble = this._numberInt;
/*     */     else {
/* 543 */       _throwInternal();
/*     */     }
/*     */ 
/* 546 */     this._numTypesValid |= 8;
/*     */   }
/*     */ 
/*     */   protected void convertNumberToBigDecimal()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 558 */     if ((this._numTypesValid & 0x8) != 0)
/*     */     {
/* 563 */       this._numberBigDecimal = new BigDecimal(getText());
/* 564 */     } else if ((this._numTypesValid & 0x4) != 0)
/* 565 */       this._numberBigDecimal = new BigDecimal(this._numberBigInt);
/* 566 */     else if ((this._numTypesValid & 0x2) != 0)
/* 567 */       this._numberBigDecimal = BigDecimal.valueOf(this._numberLong);
/* 568 */     else if ((this._numTypesValid & 0x1) != 0)
/* 569 */       this._numberBigDecimal = BigDecimal.valueOf(this._numberInt);
/*     */     else {
/* 571 */       _throwInternal();
/*     */     }
/* 573 */     this._numTypesValid |= 16;
/*     */   }
/*     */ 
/*     */   protected void reportUnexpectedNumberChar(int ch, String comment)
/*     */     throws JsonParseException
/*     */   {
/* 585 */     String msg = "Unexpected character (" + _getCharDesc(ch) + ") in numeric value";
/* 586 */     if (comment != null) {
/* 587 */       msg = msg + ": " + comment;
/*     */     }
/* 589 */     _reportError(msg);
/*     */   }
/*     */ 
/*     */   protected void reportInvalidNumber(String msg)
/*     */     throws JsonParseException
/*     */   {
/* 595 */     _reportError("Invalid numeric value: " + msg);
/*     */   }
/*     */ 
/*     */   protected void reportOverflowInt()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 602 */     _reportError("Numeric value (" + getText() + ") out of range of int (" + -2147483648 + " - " + 2147483647 + ")");
/*     */   }
/*     */ 
/*     */   protected void reportOverflowLong()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 608 */     _reportError("Numeric value (" + getText() + ") out of range of long (" + -9223372036854775808L + " - " + 9223372036854775807L + ")");
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.impl.JsonNumericParserBase
 * JD-Core Version:    0.6.0
 */