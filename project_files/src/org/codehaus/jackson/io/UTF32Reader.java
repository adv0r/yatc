/*     */ package org.codehaus.jackson.io;
/*     */ 
/*     */ import java.io.CharConversionException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public final class UTF32Reader extends BaseReader
/*     */ {
/*     */   final boolean mBigEndian;
/*  20 */   char mSurrogate = '\000';
/*     */ 
/*  25 */   int mCharCount = 0;
/*     */ 
/*  30 */   int mByteCount = 0;
/*     */ 
/*     */   public UTF32Reader(IOContext ctxt, InputStream in, byte[] buf, int ptr, int len, boolean isBigEndian)
/*     */   {
/*  42 */     super(ctxt, in, buf, ptr, len);
/*  43 */     this.mBigEndian = isBigEndian;
/*     */   }
/*     */ 
/*     */   public int read(char[] cbuf, int start, int len)
/*     */     throws IOException
/*     */   {
/*  57 */     if (this._buffer == null) {
/*  58 */       return -1;
/*     */     }
/*  60 */     if (len < 1) {
/*  61 */       return len;
/*     */     }
/*     */ 
/*  64 */     if ((start < 0) || (start + len > cbuf.length)) {
/*  65 */       reportBounds(cbuf, start, len);
/*     */     }
/*     */ 
/*  68 */     len += start;
/*  69 */     int outPtr = start;
/*     */ 
/*  72 */     if (this.mSurrogate != 0) {
/*  73 */       cbuf[(outPtr++)] = this.mSurrogate;
/*  74 */       this.mSurrogate = '\000';
/*     */     }
/*     */     else
/*     */     {
/*  80 */       int left = this._length - this._ptr;
/*  81 */       if ((left < 4) && 
/*  82 */         (!loadMore(left))) {
/*  83 */         return -1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  89 */     while (outPtr < len) {
/*  90 */       int ptr = this._ptr;
/*     */       int ch;
/*     */       int ch;
/*  93 */       if (this.mBigEndian) {
/*  94 */         ch = this._buffer[ptr] << 24 | (this._buffer[(ptr + 1)] & 0xFF) << 16 | (this._buffer[(ptr + 2)] & 0xFF) << 8 | this._buffer[(ptr + 3)] & 0xFF;
/*     */       }
/*     */       else {
/*  97 */         ch = this._buffer[ptr] & 0xFF | (this._buffer[(ptr + 1)] & 0xFF) << 8 | (this._buffer[(ptr + 2)] & 0xFF) << 16 | this._buffer[(ptr + 3)] << 24;
/*     */       }
/*     */ 
/* 100 */       this._ptr += 4;
/*     */ 
/* 104 */       if (ch > 65535) {
/* 105 */         if (ch > 1114111) {
/* 106 */           reportInvalid(ch, outPtr - start, "(above " + Integer.toHexString(1114111) + ") ");
/*     */         }
/*     */ 
/* 109 */         ch -= 65536;
/* 110 */         cbuf[(outPtr++)] = (char)(55296 + (ch >> 10));
/*     */ 
/* 112 */         ch = 0xDC00 | ch & 0x3FF;
/*     */ 
/* 114 */         if (outPtr >= len) {
/* 115 */           this.mSurrogate = (char)ch;
/* 116 */           break;
/*     */         }
/*     */       }
/* 119 */       cbuf[(outPtr++)] = (char)ch;
/* 120 */       if (this._ptr >= this._length)
/*     */       {
/*     */         break;
/*     */       }
/*     */     }
/* 125 */     len = outPtr - start;
/* 126 */     this.mCharCount += len;
/* 127 */     return len;
/*     */   }
/*     */ 
/*     */   private void reportUnexpectedEOF(int gotBytes, int needed)
/*     */     throws IOException
/*     */   {
/* 139 */     int bytePos = this.mByteCount + gotBytes;
/* 140 */     int charPos = this.mCharCount;
/*     */ 
/* 142 */     throw new CharConversionException("Unexpected EOF in the middle of a 4-byte UTF-32 char: got " + gotBytes + ", needed " + needed + ", at char #" + charPos + ", byte #" + bytePos + ")");
/*     */   }
/*     */ 
/*     */   private void reportInvalid(int value, int offset, String msg)
/*     */     throws IOException
/*     */   {
/* 150 */     int bytePos = this.mByteCount + this._ptr - 1;
/* 151 */     int charPos = this.mCharCount + offset;
/*     */ 
/* 153 */     throw new CharConversionException("Invalid UTF-32 character 0x" + Integer.toHexString(value) + msg + " at char #" + charPos + ", byte #" + bytePos + ")");
/*     */   }
/*     */ 
/*     */   private boolean loadMore(int available)
/*     */     throws IOException
/*     */   {
/* 167 */     this.mByteCount += this._length - available;
/*     */ 
/* 170 */     if (available > 0) {
/* 171 */       if (this._ptr > 0) {
/* 172 */         for (int i = 0; i < available; i++) {
/* 173 */           this._buffer[i] = this._buffer[(this._ptr + i)];
/*     */         }
/* 175 */         this._ptr = 0;
/*     */       }
/* 177 */       this._length = available;
/*     */     }
/*     */     else
/*     */     {
/* 182 */       this._ptr = 0;
/* 183 */       int count = this._in.read(this._buffer);
/* 184 */       if (count < 1) {
/* 185 */         this._length = 0;
/* 186 */         if (count < 0) {
/* 187 */           freeBuffers();
/* 188 */           return false;
/*     */         }
/*     */ 
/* 191 */         reportStrangeStream();
/*     */       }
/* 193 */       this._length = count;
/*     */     }
/*     */ 
/* 199 */     while (this._length < 4) {
/* 200 */       int count = this._in.read(this._buffer, this._length, this._buffer.length - this._length);
/* 201 */       if (count < 1) {
/* 202 */         if (count < 0) {
/* 203 */           freeBuffers();
/* 204 */           reportUnexpectedEOF(this._length, 4);
/*     */         }
/*     */ 
/* 207 */         reportStrangeStream();
/*     */       }
/* 209 */       this._length += count;
/*     */     }
/* 211 */     return true;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.io.UTF32Reader
 * JD-Core Version:    0.6.0
 */