/*     */ package org.codehaus.jackson.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import org.codehaus.jackson.JsonParseException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.io.IOContext;
/*     */ import org.codehaus.jackson.util.TextBuffer;
/*     */ 
/*     */ public abstract class ReaderBasedNumericParser extends ReaderBasedParserBase
/*     */ {
/*     */   public ReaderBasedNumericParser(IOContext pc, int features, Reader r)
/*     */   {
/*  28 */     super(pc, features, r);
/*     */   }
/*     */ 
/*     */   protected final JsonToken parseNumberText(int ch)
/*     */     throws IOException, JsonParseException
/*     */   {
/*  60 */     boolean negative = ch == 45;
/*  61 */     int ptr = this._inputPtr;
/*  62 */     int startPtr = ptr - 1;
/*  63 */     int inputLen = this._inputEnd;
/*     */ 
/*  67 */     if (negative) {
/*  68 */       if (ptr >= this._inputEnd)
/*     */         break label358;
/*  71 */       ch = this._inputBuffer[(ptr++)];
/*     */ 
/*  73 */       if ((ch > 57) || (ch < 48)) {
/*  74 */         reportUnexpectedNumberChar(ch, "expected digit (0-9) to follow minus sign, for valid numeric value");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  88 */     int intLen = 1;
/*     */ 
/*  94 */     while (ptr < this._inputEnd)
/*     */     {
/*  97 */       ch = this._inputBuffer[(ptr++)];
/*  98 */       if ((ch >= 48) && (ch <= 57))
/*     */       {
/* 102 */         intLen++; if ((intLen != 2) || 
/* 103 */           (this._inputBuffer[(ptr - 2)] != '0')) continue;
/* 104 */         reportInvalidNumber("Leading zeroes not allowed"); continue;
/*     */       }
/*     */ 
/* 109 */       int fractLen = 0;
/*     */ 
/* 112 */       if (ch == 46)
/*     */       {
/*     */         while (true) {
/* 115 */           if (ptr >= inputLen)
/*     */             break label358;
/* 118 */           ch = this._inputBuffer[(ptr++)];
/* 119 */           if ((ch < 48) || (ch > 57)) {
/*     */             break;
/*     */           }
/* 122 */           fractLen++;
/*     */         }
/*     */ 
/* 125 */         if (fractLen == 0) {
/* 126 */           reportUnexpectedNumberChar(ch, "Decimal point not followed by a digit");
/*     */         }
/*     */       }
/*     */ 
/* 130 */       int expLen = 0;
/* 131 */       if ((ch == 101) || (ch == 69)) {
/* 132 */         if (ptr >= inputLen)
/*     */         {
/*     */           break;
/*     */         }
/* 136 */         ch = this._inputBuffer[(ptr++)];
/* 137 */         if ((ch == 45) || (ch == 43)) {
/* 138 */           if (ptr >= inputLen) {
/*     */             break;
/*     */           }
/* 141 */           ch = this._inputBuffer[(ptr++)];
/*     */         }
/*     */       }
/*     */       while (true) if ((ch <= 57) && (ch >= 48)) {
/* 144 */           expLen++;
/* 145 */           if (ptr >= inputLen) {
/*     */             break;
/*     */           }
/* 148 */           ch = this._inputBuffer[(ptr++)]; continue;
/*     */         }
/*     */         else {
/* 151 */           if (expLen == 0) {
/* 152 */             reportUnexpectedNumberChar(ch, "Exponent indicator not followed by a digit");
/*     */           }
/*     */ 
/* 157 */           ptr--;
/* 158 */           this._inputPtr = ptr;
/* 159 */           int len = ptr - startPtr;
/* 160 */           this._textBuffer.resetWithShared(this._inputBuffer, startPtr, len);
/* 161 */           return reset(negative, intLen, fractLen, expLen);
/*     */         }
/*     */     }
/* 164 */     label358: this._inputPtr = (negative ? startPtr + 1 : startPtr);
/* 165 */     return parseNumberText2(negative);
/*     */   }
/*     */ 
/*     */   private final JsonToken parseNumberText2(boolean negative)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 178 */     char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
/* 179 */     int outPtr = 0;
/*     */ 
/* 182 */     if (negative) {
/* 183 */       outBuf[(outPtr++)] = '-';
/*     */     }
/*     */ 
/* 187 */     int intLen = 0;
/* 188 */     boolean eof = false;
/*     */     char c;
/*     */     while (true) {
/* 193 */       if ((this._inputPtr >= this._inputEnd) && (!loadMore()))
/*     */       {
/* 195 */         char c = '\000';
/* 196 */         eof = true;
/* 197 */         break;
/*     */       }
/* 199 */       c = this._inputBuffer[(this._inputPtr++)];
/* 200 */       if ((c < '0') || (c > '9')) {
/*     */         break;
/*     */       }
/* 203 */       intLen++;
/*     */ 
/* 205 */       if ((intLen == 2) && 
/* 206 */         (outBuf[(outPtr - 1)] == '0')) {
/* 207 */         reportInvalidNumber("Leading zeroes not allowed");
/*     */       }
/*     */ 
/* 210 */       if (outPtr >= outBuf.length) {
/* 211 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 212 */         outPtr = 0;
/*     */       }
/* 214 */       outBuf[(outPtr++)] = c;
/*     */     }
/*     */ 
/* 217 */     if (intLen == 0) {
/* 218 */       reportInvalidNumber("Missing integer part (next char " + _getCharDesc(c) + ")");
/*     */     }
/*     */ 
/* 221 */     int fractLen = 0;
/*     */ 
/* 223 */     if (c == '.') {
/* 224 */       outBuf[(outPtr++)] = c;
/*     */       while (true)
/*     */       {
/* 228 */         if ((this._inputPtr >= this._inputEnd) && (!loadMore())) {
/* 229 */           eof = true;
/* 230 */           break;
/*     */         }
/* 232 */         c = this._inputBuffer[(this._inputPtr++)];
/* 233 */         if ((c < '0') || (c > '9')) {
/*     */           break;
/*     */         }
/* 236 */         fractLen++;
/* 237 */         if (outPtr >= outBuf.length) {
/* 238 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 239 */           outPtr = 0;
/*     */         }
/* 241 */         outBuf[(outPtr++)] = c;
/*     */       }
/*     */ 
/* 244 */       if (fractLen == 0) {
/* 245 */         reportUnexpectedNumberChar(c, "Decimal point not followed by a digit");
/*     */       }
/*     */     }
/*     */ 
/* 249 */     int expLen = 0;
/* 250 */     if ((c == 'e') || (c == 'E')) {
/* 251 */       if (outPtr >= outBuf.length) {
/* 252 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 253 */         outPtr = 0;
/*     */       }
/* 255 */       outBuf[(outPtr++)] = c;
/*     */ 
/* 257 */       c = this._inputPtr < this._inputEnd ? this._inputBuffer[(this._inputPtr++)] : getNextChar("expected a digit for number exponent");
/*     */ 
/* 260 */       if ((c == '-') || (c == '+')) {
/* 261 */         if (outPtr >= outBuf.length) {
/* 262 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 263 */           outPtr = 0;
/*     */         }
/* 265 */         outBuf[(outPtr++)] = c;
/*     */ 
/* 267 */         c = this._inputPtr < this._inputEnd ? this._inputBuffer[(this._inputPtr++)] : getNextChar("expected a digit for number exponent");
/*     */       }
/*     */ 
/* 272 */       while ((c <= '9') && (c >= '0')) {
/* 273 */         expLen++;
/* 274 */         if (outPtr >= outBuf.length) {
/* 275 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 276 */           outPtr = 0;
/*     */         }
/* 278 */         outBuf[(outPtr++)] = c;
/* 279 */         if ((this._inputPtr >= this._inputEnd) && (!loadMore())) {
/* 280 */           eof = true;
/* 281 */           break;
/*     */         }
/* 283 */         c = this._inputBuffer[(this._inputPtr++)];
/*     */       }
/*     */ 
/* 286 */       if (expLen == 0) {
/* 287 */         reportUnexpectedNumberChar(c, "Exponent indicator not followed by a digit");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 292 */     if (!eof) {
/* 293 */       this._inputPtr -= 1;
/*     */     }
/* 295 */     this._textBuffer.setCurrentLength(outPtr);
/*     */ 
/* 298 */     return reset(negative, intLen, fractLen, expLen);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.impl.ReaderBasedNumericParser
 * JD-Core Version:    0.6.0
 */