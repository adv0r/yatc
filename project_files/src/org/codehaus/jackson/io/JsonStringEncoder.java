/*     */ package org.codehaus.jackson.io;
/*     */ 
/*     */ import java.lang.ref.SoftReference;
/*     */ import org.codehaus.jackson.util.ByteArrayBuilder;
/*     */ import org.codehaus.jackson.util.CharTypes;
/*     */ import org.codehaus.jackson.util.TextBuffer;
/*     */ 
/*     */ public final class JsonStringEncoder
/*     */ {
/*  22 */   private static final char[] HEX_CHARS = CharTypes.copyHexChars();
/*     */ 
/*  24 */   private static final byte[] HEX_BYTES = CharTypes.copyHexBytes();
/*     */   private static final int SURR1_FIRST = 55296;
/*     */   private static final int SURR1_LAST = 56319;
/*     */   private static final int SURR2_FIRST = 56320;
/*     */   private static final int SURR2_LAST = 57343;
/*     */   private static final int INT_BACKSLASH = 92;
/*     */   private static final int INT_U = 117;
/*     */   private static final int INT_0 = 48;
/*  40 */   protected static final ThreadLocal<SoftReference<JsonStringEncoder>> _threadEncoder = new ThreadLocal();
/*     */   protected TextBuffer _textBuffer;
/*     */   protected ByteArrayBuilder _byteBuilder;
/*     */   protected final char[] _quoteBuffer;
/*     */ 
/*     */   public JsonStringEncoder()
/*     */   {
/*  68 */     this._quoteBuffer = new char[6];
/*  69 */     this._quoteBuffer[0] = '\\';
/*  70 */     this._quoteBuffer[2] = '0';
/*  71 */     this._quoteBuffer[3] = '0';
/*     */   }
/*     */ 
/*     */   public static JsonStringEncoder getInstance()
/*     */   {
/*  80 */     SoftReference ref = (SoftReference)_threadEncoder.get();
/*  81 */     JsonStringEncoder enc = ref == null ? null : (JsonStringEncoder)ref.get();
/*     */ 
/*  83 */     if (enc == null) {
/*  84 */       enc = new JsonStringEncoder();
/*  85 */       _threadEncoder.set(new SoftReference(enc));
/*     */     }
/*  87 */     return enc;
/*     */   }
/*     */ 
/*     */   public char[] quoteAsString(String input)
/*     */   {
/* 102 */     TextBuffer textBuffer = this._textBuffer;
/* 103 */     if (textBuffer == null)
/*     */     {
/* 105 */       this._textBuffer = (textBuffer = new TextBuffer(null));
/*     */     }
/* 107 */     char[] outputBuffer = textBuffer.emptyAndGetCurrentSegment();
/* 108 */     int[] escCodes = CharTypes.getOutputEscapes();
/* 109 */     int escCodeCount = escCodes.length;
/* 110 */     int inPtr = 0;
/* 111 */     int inputLen = input.length();
/* 112 */     int outPtr = 0;
/*     */ 
/* 115 */     while (inPtr < inputLen)
/*     */     {
/*     */       while (true) {
/* 118 */         char c = input.charAt(inPtr);
/* 119 */         if ((c < escCodeCount) && (escCodes[c] != 0)) {
/*     */           break;
/*     */         }
/* 122 */         if (outPtr >= outputBuffer.length) {
/* 123 */           outputBuffer = textBuffer.finishCurrentSegment();
/* 124 */           outPtr = 0;
/*     */         }
/* 126 */         outputBuffer[(outPtr++)] = c;
/* 127 */         inPtr++; if (inPtr >= inputLen) {
/*     */           break label243;
/*     */         }
/*     */       }
/* 132 */       int escCode = escCodes[input.charAt(inPtr++)];
/* 133 */       int length = _appendSingleEscape(escCode, this._quoteBuffer);
/* 134 */       if (outPtr + length > outputBuffer.length) {
/* 135 */         int first = outputBuffer.length - outPtr;
/* 136 */         if (first > 0) {
/* 137 */           System.arraycopy(this._quoteBuffer, 0, outputBuffer, outPtr, first);
/*     */         }
/* 139 */         outputBuffer = textBuffer.finishCurrentSegment();
/* 140 */         int second = length - first;
/* 141 */         System.arraycopy(this._quoteBuffer, first, outputBuffer, outPtr, second);
/* 142 */         outPtr += second;
/*     */       } else {
/* 144 */         System.arraycopy(this._quoteBuffer, 0, outputBuffer, outPtr, length);
/* 145 */         outPtr += length;
/*     */       }
/*     */     }
/*     */ 
/* 149 */     label243: textBuffer.setCurrentLength(outPtr);
/* 150 */     return textBuffer.contentsAsArray();
/*     */   }
/*     */ 
/*     */   public byte[] quoteAsUTF8(String text)
/*     */   {
/* 159 */     ByteArrayBuilder byteBuilder = this._byteBuilder;
/* 160 */     if (byteBuilder == null)
/*     */     {
/* 162 */       this._byteBuilder = (byteBuilder = new ByteArrayBuilder(null));
/*     */     }
/* 164 */     int inputPtr = 0;
/* 165 */     int inputEnd = text.length();
/* 166 */     int outputPtr = 0;
/* 167 */     byte[] outputBuffer = byteBuilder.resetAndGetFirstSegment();
/*     */ 
/* 170 */     while (inputPtr < inputEnd) {
/* 171 */       int[] escCodes = CharTypes.getOutputEscapes();
/*     */       while (true)
/*     */       {
/* 175 */         int ch = text.charAt(inputPtr);
/* 176 */         if ((ch > 127) || (escCodes[ch] != 0)) {
/*     */           break;
/*     */         }
/* 179 */         if (outputPtr >= outputBuffer.length) {
/* 180 */           outputBuffer = byteBuilder.finishCurrentSegment();
/* 181 */           outputPtr = 0;
/*     */         }
/* 183 */         outputBuffer[(outputPtr++)] = (byte)ch;
/* 184 */         inputPtr++; if (inputPtr >= inputEnd)
/*     */           break label494;
/*     */       }
/* 188 */       if (outputPtr >= outputBuffer.length) {
/* 189 */         outputBuffer = byteBuilder.finishCurrentSegment();
/* 190 */         outputPtr = 0;
/*     */       }
/*     */ 
/* 193 */       int ch = text.charAt(inputPtr++);
/* 194 */       if (ch <= 127) {
/* 195 */         int escape = escCodes[ch];
/*     */ 
/* 197 */         outputPtr = _appendByteEscape(escape, byteBuilder, outputPtr);
/* 198 */         outputBuffer = byteBuilder.getCurrentSegment();
/* 199 */         continue;
/* 200 */       }if (ch <= 2047) {
/* 201 */         outputBuffer[(outputPtr++)] = (byte)(0xC0 | ch >> 6);
/* 202 */         ch = 0x80 | ch & 0x3F;
/*     */       }
/* 205 */       else if ((ch < 55296) || (ch > 57343)) {
/* 206 */         outputBuffer[(outputPtr++)] = (byte)(0xE0 | ch >> 12);
/* 207 */         if (outputPtr >= outputBuffer.length) {
/* 208 */           outputBuffer = byteBuilder.finishCurrentSegment();
/* 209 */           outputPtr = 0;
/*     */         }
/* 211 */         outputBuffer[(outputPtr++)] = (byte)(0x80 | ch >> 6 & 0x3F);
/* 212 */         ch = 0x80 | ch & 0x3F;
/*     */       } else {
/* 214 */         if (ch > 56319) {
/* 215 */           _throwIllegalSurrogate(ch);
/*     */         }
/*     */ 
/* 218 */         if (inputPtr >= inputEnd) {
/* 219 */           _throwIllegalSurrogate(ch);
/*     */         }
/* 221 */         ch = _convertSurrogate(ch, text.charAt(inputPtr++));
/* 222 */         if (ch > 1114111) {
/* 223 */           _throwIllegalSurrogate(ch);
/*     */         }
/* 225 */         outputBuffer[(outputPtr++)] = (byte)(0xF0 | ch >> 18);
/* 226 */         if (outputPtr >= outputBuffer.length) {
/* 227 */           outputBuffer = byteBuilder.finishCurrentSegment();
/* 228 */           outputPtr = 0;
/*     */         }
/* 230 */         outputBuffer[(outputPtr++)] = (byte)(0x80 | ch >> 12 & 0x3F);
/* 231 */         if (outputPtr >= outputBuffer.length) {
/* 232 */           outputBuffer = byteBuilder.finishCurrentSegment();
/* 233 */           outputPtr = 0;
/*     */         }
/* 235 */         outputBuffer[(outputPtr++)] = (byte)(0x80 | ch >> 6 & 0x3F);
/* 236 */         ch = 0x80 | ch & 0x3F;
/*     */       }
/*     */ 
/* 239 */       if (outputPtr >= outputBuffer.length) {
/* 240 */         outputBuffer = byteBuilder.finishCurrentSegment();
/* 241 */         outputPtr = 0;
/*     */       }
/* 243 */       outputBuffer[(outputPtr++)] = (byte)ch;
/*     */     }
/* 245 */     label494: return this._byteBuilder.completeAndCoalesce(outputPtr);
/*     */   }
/*     */ 
/*     */   public byte[] encodeAsUTF8(String text)
/*     */   {
/* 254 */     ByteArrayBuilder byteBuilder = this._byteBuilder;
/* 255 */     if (byteBuilder == null)
/*     */     {
/* 257 */       this._byteBuilder = (byteBuilder = new ByteArrayBuilder(null));
/*     */     }
/* 259 */     int inputPtr = 0;
/* 260 */     int inputEnd = text.length();
/* 261 */     int outputPtr = 0;
/* 262 */     byte[] outputBuffer = byteBuilder.resetAndGetFirstSegment();
/* 263 */     int outputEnd = outputBuffer.length;
/*     */ 
/* 266 */     while (inputPtr < inputEnd) {
/* 267 */       int c = text.charAt(inputPtr++);
/*     */ 
/* 270 */       while (c <= 127) {
/* 271 */         if (outputPtr >= outputEnd) {
/* 272 */           outputBuffer = byteBuilder.finishCurrentSegment();
/* 273 */           outputEnd = outputBuffer.length;
/* 274 */           outputPtr = 0;
/*     */         }
/* 276 */         outputBuffer[(outputPtr++)] = (byte)c;
/* 277 */         if (inputPtr >= inputEnd)
/*     */           break label447;
/* 280 */         c = text.charAt(inputPtr++);
/*     */       }
/*     */ 
/* 284 */       if (outputPtr >= outputEnd) {
/* 285 */         outputBuffer = byteBuilder.finishCurrentSegment();
/* 286 */         outputEnd = outputBuffer.length;
/* 287 */         outputPtr = 0;
/*     */       }
/* 289 */       if (c < 2048) {
/* 290 */         outputBuffer[(outputPtr++)] = (byte)(0xC0 | c >> 6);
/*     */       }
/* 293 */       else if ((c < 55296) || (c > 57343)) {
/* 294 */         outputBuffer[(outputPtr++)] = (byte)(0xE0 | c >> 12);
/* 295 */         if (outputPtr >= outputEnd) {
/* 296 */           outputBuffer = byteBuilder.finishCurrentSegment();
/* 297 */           outputEnd = outputBuffer.length;
/* 298 */           outputPtr = 0;
/*     */         }
/* 300 */         outputBuffer[(outputPtr++)] = (byte)(0x80 | c >> 6 & 0x3F);
/*     */       } else {
/* 302 */         if (c > 56319) {
/* 303 */           _throwIllegalSurrogate(c);
/*     */         }
/*     */ 
/* 306 */         if (inputPtr >= inputEnd) {
/* 307 */           _throwIllegalSurrogate(c);
/*     */         }
/* 309 */         c = _convertSurrogate(c, text.charAt(inputPtr++));
/* 310 */         if (c > 1114111) {
/* 311 */           _throwIllegalSurrogate(c);
/*     */         }
/* 313 */         outputBuffer[(outputPtr++)] = (byte)(0xF0 | c >> 18);
/* 314 */         if (outputPtr >= outputEnd) {
/* 315 */           outputBuffer = byteBuilder.finishCurrentSegment();
/* 316 */           outputEnd = outputBuffer.length;
/* 317 */           outputPtr = 0;
/*     */         }
/* 319 */         outputBuffer[(outputPtr++)] = (byte)(0x80 | c >> 12 & 0x3F);
/* 320 */         if (outputPtr >= outputEnd) {
/* 321 */           outputBuffer = byteBuilder.finishCurrentSegment();
/* 322 */           outputEnd = outputBuffer.length;
/* 323 */           outputPtr = 0;
/*     */         }
/* 325 */         outputBuffer[(outputPtr++)] = (byte)(0x80 | c >> 6 & 0x3F);
/*     */       }
/*     */ 
/* 328 */       if (outputPtr >= outputEnd) {
/* 329 */         outputBuffer = byteBuilder.finishCurrentSegment();
/* 330 */         outputEnd = outputBuffer.length;
/* 331 */         outputPtr = 0;
/*     */       }
/* 333 */       outputBuffer[(outputPtr++)] = (byte)(0x80 | c & 0x3F);
/*     */     }
/* 335 */     label447: return this._byteBuilder.completeAndCoalesce(outputPtr);
/*     */   }
/*     */ 
/*     */   private int _appendSingleEscape(int escCode, char[] quoteBuffer)
/*     */   {
/* 346 */     if (escCode < 0) {
/* 347 */       int value = -(escCode + 1);
/* 348 */       quoteBuffer[1] = 'u';
/*     */ 
/* 350 */       quoteBuffer[4] = HEX_CHARS[(value >> 4)];
/* 351 */       quoteBuffer[5] = HEX_CHARS[(value & 0xF)];
/* 352 */       return 6;
/*     */     }
/* 354 */     quoteBuffer[1] = (char)escCode;
/* 355 */     return 2;
/*     */   }
/*     */ 
/*     */   private int _appendByteEscape(int escCode, ByteArrayBuilder byteBuilder, int ptr)
/*     */   {
/* 360 */     byteBuilder.setCurrentSegmentLength(ptr);
/* 361 */     byteBuilder.append(92);
/* 362 */     if (escCode < 0) {
/* 363 */       int value = -(escCode + 1);
/* 364 */       byteBuilder.append(117);
/* 365 */       byteBuilder.append(48);
/* 366 */       byteBuilder.append(48);
/*     */ 
/* 368 */       byteBuilder.append(HEX_BYTES[(value >> 4)]);
/* 369 */       byteBuilder.append(HEX_BYTES[(value & 0xF)]);
/*     */     } else {
/* 371 */       byteBuilder.append((byte)escCode);
/*     */     }
/* 373 */     return byteBuilder.getCurrentSegmentLength();
/*     */   }
/*     */ 
/*     */   private int _convertSurrogate(int firstPart, int secondPart)
/*     */   {
/* 382 */     if ((secondPart < 56320) || (secondPart > 57343)) {
/* 383 */       throw new IllegalArgumentException("Broken surrogate pair: first char 0x" + Integer.toHexString(firstPart) + ", second 0x" + Integer.toHexString(secondPart) + "; illegal combination");
/*     */     }
/* 385 */     return 65536 + (firstPart - 55296 << 10) + (secondPart - 56320);
/*     */   }
/*     */ 
/*     */   private void _throwIllegalSurrogate(int code)
/*     */   {
/* 390 */     if (code > 1114111) {
/* 391 */       throw new IllegalArgumentException("Illegal character point (0x" + Integer.toHexString(code) + ") to output; max is 0x10FFFF as per RFC 4627");
/*     */     }
/* 393 */     if (code >= 55296) {
/* 394 */       if (code <= 56319) {
/* 395 */         throw new IllegalArgumentException("Unmatched first part of surrogate pair (0x" + Integer.toHexString(code) + ")");
/*     */       }
/* 397 */       throw new IllegalArgumentException("Unmatched second part of surrogate pair (0x" + Integer.toHexString(code) + ")");
/*     */     }
/*     */ 
/* 400 */     throw new IllegalArgumentException("Illegal character point (0x" + Integer.toHexString(code) + ") to output");
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.io.JsonStringEncoder
 * JD-Core Version:    0.6.0
 */