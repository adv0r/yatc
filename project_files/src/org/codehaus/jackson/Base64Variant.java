/*     */ package org.codehaus.jackson;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public final class Base64Variant
/*     */ {
/*     */   static final char PADDING_CHAR_NONE = '\000';
/*     */   public static final int BASE64_VALUE_INVALID = -1;
/*     */   public static final int BASE64_VALUE_PADDING = -2;
/*  59 */   private final int[] _asciiToBase64 = new int[''];
/*     */ 
/*  65 */   private final char[] _base64ToAsciiC = new char[64];
/*     */ 
/*  71 */   private final byte[] _base64ToAsciiB = new byte[64];
/*     */   final String _name;
/*     */   final boolean _usesPadding;
/*     */   final char _paddingChar;
/*     */   final int _maxLineLength;
/*     */ 
/*     */   public Base64Variant(String name, String base64Alphabet, boolean usesPadding, char paddingChar, int maxLineLength)
/*     */   {
/* 112 */     this._name = name;
/* 113 */     this._usesPadding = usesPadding;
/* 114 */     this._paddingChar = paddingChar;
/* 115 */     this._maxLineLength = maxLineLength;
/*     */ 
/* 120 */     int alphaLen = base64Alphabet.length();
/* 121 */     if (alphaLen != 64) {
/* 122 */       throw new IllegalArgumentException("Base64Alphabet length must be exactly 64 (was " + alphaLen + ")");
/*     */     }
/*     */ 
/* 126 */     base64Alphabet.getChars(0, alphaLen, this._base64ToAsciiC, 0);
/* 127 */     Arrays.fill(this._asciiToBase64, -1);
/* 128 */     for (int i = 0; i < alphaLen; i++) {
/* 129 */       char alpha = this._base64ToAsciiC[i];
/* 130 */       this._base64ToAsciiB[i] = (byte)alpha;
/* 131 */       this._asciiToBase64[alpha] = i;
/*     */     }
/*     */ 
/* 135 */     if (usesPadding)
/* 136 */       this._asciiToBase64[paddingChar] = -2;
/*     */   }
/*     */ 
/*     */   public Base64Variant(Base64Variant base, String name, int maxLineLength)
/*     */   {
/* 147 */     this(base, name, base._usesPadding, base._paddingChar, maxLineLength);
/*     */   }
/*     */ 
/*     */   public Base64Variant(Base64Variant base, String name, boolean usesPadding, char paddingChar, int maxLineLength)
/*     */   {
/* 157 */     this._name = name;
/* 158 */     byte[] srcB = base._base64ToAsciiB;
/* 159 */     System.arraycopy(srcB, 0, this._base64ToAsciiB, 0, srcB.length);
/* 160 */     char[] srcC = base._base64ToAsciiC;
/* 161 */     System.arraycopy(srcC, 0, this._base64ToAsciiC, 0, srcC.length);
/* 162 */     int[] srcV = base._asciiToBase64;
/* 163 */     System.arraycopy(srcV, 0, this._asciiToBase64, 0, srcV.length);
/*     */ 
/* 165 */     this._usesPadding = usesPadding;
/* 166 */     this._paddingChar = paddingChar;
/* 167 */     this._maxLineLength = maxLineLength;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 176 */     return this._name;
/*     */   }
/* 178 */   public boolean usesPadding() { return this._usesPadding; } 
/* 179 */   public boolean usesPaddingChar(char c) { return c == this._paddingChar; } 
/* 180 */   public boolean usesPaddingChar(int ch) { return ch == this._paddingChar; } 
/* 181 */   public char getPaddingChar() { return this._paddingChar; } 
/* 182 */   public byte getPaddingByte() { return (byte)this._paddingChar; } 
/*     */   public int getMaxLineLength() {
/* 184 */     return this._maxLineLength;
/*     */   }
/*     */ 
/*     */   public int decodeBase64Char(char c)
/*     */   {
/* 197 */     int ch = c;
/* 198 */     return ch <= 127 ? this._asciiToBase64[ch] : -1;
/*     */   }
/*     */ 
/*     */   public int decodeBase64Char(int ch)
/*     */   {
/* 203 */     return ch <= 127 ? this._asciiToBase64[ch] : -1;
/*     */   }
/*     */ 
/*     */   public int decodeBase64Byte(byte b)
/*     */   {
/* 208 */     int ch = b;
/* 209 */     return ch <= 127 ? this._asciiToBase64[ch] : -1;
/*     */   }
/*     */ 
/*     */   public char encodeBase64BitsAsChar(int value)
/*     */   {
/* 223 */     return this._base64ToAsciiC[value];
/*     */   }
/*     */ 
/*     */   public int encodeBase64Chunk(int b24, char[] buffer, int ptr)
/*     */   {
/* 232 */     buffer[(ptr++)] = this._base64ToAsciiC[(b24 >> 18 & 0x3F)];
/* 233 */     buffer[(ptr++)] = this._base64ToAsciiC[(b24 >> 12 & 0x3F)];
/* 234 */     buffer[(ptr++)] = this._base64ToAsciiC[(b24 >> 6 & 0x3F)];
/* 235 */     buffer[(ptr++)] = this._base64ToAsciiC[(b24 & 0x3F)];
/* 236 */     return ptr;
/*     */   }
/*     */ 
/*     */   public void encodeBase64Chunk(StringBuilder sb, int b24)
/*     */   {
/* 241 */     sb.append(this._base64ToAsciiC[(b24 >> 18 & 0x3F)]);
/* 242 */     sb.append(this._base64ToAsciiC[(b24 >> 12 & 0x3F)]);
/* 243 */     sb.append(this._base64ToAsciiC[(b24 >> 6 & 0x3F)]);
/* 244 */     sb.append(this._base64ToAsciiC[(b24 & 0x3F)]);
/*     */   }
/*     */ 
/*     */   public int encodeBase64Partial(int bits, int outputBytes, char[] buffer, int outPtr)
/*     */   {
/* 257 */     buffer[(outPtr++)] = this._base64ToAsciiC[(bits >> 18 & 0x3F)];
/* 258 */     buffer[(outPtr++)] = this._base64ToAsciiC[(bits >> 12 & 0x3F)];
/* 259 */     if (this._usesPadding) {
/* 260 */       buffer[(outPtr++)] = (outputBytes == 2 ? this._base64ToAsciiC[(bits >> 6 & 0x3F)] : this._paddingChar);
/*     */ 
/* 262 */       buffer[(outPtr++)] = this._paddingChar;
/*     */     }
/* 264 */     else if (outputBytes == 2) {
/* 265 */       buffer[(outPtr++)] = this._base64ToAsciiC[(bits >> 6 & 0x3F)];
/*     */     }
/*     */ 
/* 268 */     return outPtr;
/*     */   }
/*     */ 
/*     */   public void encodeBase64Partial(StringBuilder sb, int bits, int outputBytes)
/*     */   {
/* 273 */     sb.append(this._base64ToAsciiC[(bits >> 18 & 0x3F)]);
/* 274 */     sb.append(this._base64ToAsciiC[(bits >> 12 & 0x3F)]);
/* 275 */     if (this._usesPadding) {
/* 276 */       sb.append(outputBytes == 2 ? this._base64ToAsciiC[(bits >> 6 & 0x3F)] : this._paddingChar);
/*     */ 
/* 278 */       sb.append(this._paddingChar);
/*     */     }
/* 280 */     else if (outputBytes == 2) {
/* 281 */       sb.append(this._base64ToAsciiC[(bits >> 6 & 0x3F)]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte encodeBase64BitsAsByte(int value)
/*     */   {
/* 289 */     return this._base64ToAsciiB[value];
/*     */   }
/*     */ 
/*     */   public int encodeBase64Chunk(int b24, byte[] buffer, int ptr)
/*     */   {
/* 298 */     buffer[(ptr++)] = this._base64ToAsciiB[(b24 >> 18 & 0x3F)];
/* 299 */     buffer[(ptr++)] = this._base64ToAsciiB[(b24 >> 12 & 0x3F)];
/* 300 */     buffer[(ptr++)] = this._base64ToAsciiB[(b24 >> 6 & 0x3F)];
/* 301 */     buffer[(ptr++)] = this._base64ToAsciiB[(b24 & 0x3F)];
/* 302 */     return ptr;
/*     */   }
/*     */ 
/*     */   public int encodeBase64Partial(int bits, int outputBytes, byte[] buffer, int outPtr)
/*     */   {
/* 315 */     buffer[(outPtr++)] = this._base64ToAsciiB[(bits >> 18 & 0x3F)];
/* 316 */     buffer[(outPtr++)] = this._base64ToAsciiB[(bits >> 12 & 0x3F)];
/* 317 */     if (this._usesPadding) {
/* 318 */       byte pb = (byte)this._paddingChar;
/* 319 */       buffer[(outPtr++)] = (outputBytes == 2 ? this._base64ToAsciiB[(bits >> 6 & 0x3F)] : pb);
/*     */ 
/* 321 */       buffer[(outPtr++)] = pb;
/*     */     }
/* 323 */     else if (outputBytes == 2) {
/* 324 */       buffer[(outPtr++)] = this._base64ToAsciiB[(bits >> 6 & 0x3F)];
/*     */     }
/*     */ 
/* 327 */     return outPtr;
/*     */   }
/*     */ 
/*     */   public String encode(byte[] input)
/*     */   {
/* 341 */     return encode(input, false);
/*     */   }
/*     */ 
/*     */   public String encode(byte[] input, boolean addQuotes)
/*     */   {
/* 357 */     int inputEnd = input.length;
/*     */ 
/* 361 */     int outputLen = inputEnd + (inputEnd >> 2) + (inputEnd >> 3);
/* 362 */     StringBuilder sb = new StringBuilder(outputLen);
/*     */ 
/* 364 */     if (addQuotes) {
/* 365 */       sb.append('"');
/*     */     }
/*     */ 
/* 368 */     int chunksBeforeLF = getMaxLineLength() >> 2;
/*     */ 
/* 371 */     int inputPtr = 0;
/* 372 */     int safeInputEnd = inputEnd - 3;
/*     */ 
/* 374 */     while (inputPtr <= safeInputEnd)
/*     */     {
/* 376 */       int b24 = input[(inputPtr++)] << 8;
/* 377 */       b24 |= input[(inputPtr++)] & 0xFF;
/* 378 */       b24 = b24 << 8 | input[(inputPtr++)] & 0xFF;
/* 379 */       encodeBase64Chunk(sb, b24);
/* 380 */       chunksBeforeLF--; if (chunksBeforeLF <= 0)
/*     */       {
/* 382 */         sb.append('\\');
/* 383 */         sb.append('n');
/* 384 */         chunksBeforeLF = getMaxLineLength() >> 2;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 389 */     int inputLeft = inputEnd - inputPtr;
/* 390 */     if (inputLeft > 0) {
/* 391 */       int b24 = input[(inputPtr++)] << 16;
/* 392 */       if (inputLeft == 2) {
/* 393 */         b24 |= (input[(inputPtr++)] & 0xFF) << 8;
/*     */       }
/* 395 */       encodeBase64Partial(sb, b24, inputLeft);
/*     */     }
/*     */ 
/* 398 */     if (addQuotes) {
/* 399 */       sb.append('"');
/*     */     }
/* 401 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 411 */     return this._name;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.Base64Variant
 * JD-Core Version:    0.6.0
 */