/*     */ package org.codehaus.jackson.impl;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.CharConversionException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import org.codehaus.jackson.JsonEncoding;
/*     */ import org.codehaus.jackson.JsonParseException;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonParser.Feature;
/*     */ import org.codehaus.jackson.ObjectCodec;
/*     */ import org.codehaus.jackson.io.IOContext;
/*     */ import org.codehaus.jackson.io.MergedStream;
/*     */ import org.codehaus.jackson.io.UTF32Reader;
/*     */ import org.codehaus.jackson.sym.BytesToNameCanonicalizer;
/*     */ import org.codehaus.jackson.sym.CharsToNameCanonicalizer;
/*     */ 
/*     */ public final class ByteSourceBootstrapper
/*     */ {
/*     */   final IOContext _context;
/*     */   final InputStream _in;
/*     */   final byte[] _inputBuffer;
/*     */   private int _inputPtr;
/*     */   private int _inputEnd;
/*     */   private final boolean _bufferRecyclable;
/*     */   protected int _inputProcessed;
/*  68 */   boolean _bigEndian = true;
/*  69 */   int _bytesPerChar = 0;
/*     */ 
/*     */   public ByteSourceBootstrapper(IOContext ctxt, InputStream in)
/*     */   {
/*  79 */     this._context = ctxt;
/*  80 */     this._in = in;
/*  81 */     this._inputBuffer = ctxt.allocReadIOBuffer();
/*  82 */     this._inputEnd = (this._inputPtr = 0);
/*  83 */     this._inputProcessed = 0;
/*  84 */     this._bufferRecyclable = true;
/*     */   }
/*     */ 
/*     */   public ByteSourceBootstrapper(IOContext ctxt, byte[] inputBuffer, int inputStart, int inputLen)
/*     */   {
/*  89 */     this._context = ctxt;
/*  90 */     this._in = null;
/*  91 */     this._inputBuffer = inputBuffer;
/*  92 */     this._inputPtr = inputStart;
/*  93 */     this._inputEnd = (inputStart + inputLen);
/*     */ 
/*  95 */     this._inputProcessed = (-inputStart);
/*  96 */     this._bufferRecyclable = false;
/*     */   }
/*     */ 
/*     */   public JsonEncoding detectEncoding()
/*     */     throws IOException, JsonParseException
/*     */   {
/* 107 */     boolean foundEncoding = false;
/*     */ 
/* 116 */     if (ensureLoaded(4)) {
/* 117 */       int quad = this._inputBuffer[this._inputPtr] << 24 | (this._inputBuffer[(this._inputPtr + 1)] & 0xFF) << 16 | (this._inputBuffer[(this._inputPtr + 2)] & 0xFF) << 8 | this._inputBuffer[(this._inputPtr + 3)] & 0xFF;
/*     */ 
/* 122 */       if (handleBOM(quad)) {
/* 123 */         foundEncoding = true;
/*     */       }
/* 131 */       else if (checkUTF32(quad))
/* 132 */         foundEncoding = true;
/* 133 */       else if (checkUTF16(quad >>> 16)) {
/* 134 */         foundEncoding = true;
/*     */       }
/*     */     }
/* 137 */     else if (ensureLoaded(2)) {
/* 138 */       int i16 = (this._inputBuffer[this._inputPtr] & 0xFF) << 8 | this._inputBuffer[(this._inputPtr + 1)] & 0xFF;
/*     */ 
/* 140 */       if (checkUTF16(i16))
/* 141 */         foundEncoding = true;
/*     */     }
/*     */     JsonEncoding enc;
/* 148 */     if (!foundEncoding) {
/* 149 */       enc = JsonEncoding.UTF8;
/*     */     }
/*     */     else
/*     */     {
/*     */       JsonEncoding enc;
/* 150 */       if (this._bytesPerChar == 2) {
/* 151 */         enc = this._bigEndian ? JsonEncoding.UTF16_BE : JsonEncoding.UTF16_LE;
/*     */       }
/*     */       else
/*     */       {
/*     */         JsonEncoding enc;
/* 152 */         if (this._bytesPerChar == 4)
/* 153 */           enc = this._bigEndian ? JsonEncoding.UTF32_BE : JsonEncoding.UTF32_LE;
/*     */         else
/* 155 */           throw new RuntimeException("Internal error");
/*     */       }
/*     */     }
/*     */     JsonEncoding enc;
/* 157 */     this._context.setEncoding(enc);
/* 158 */     return enc;
/*     */   }
/*     */ 
/*     */   public Reader constructReader()
/*     */     throws IOException
/*     */   {
/* 164 */     JsonEncoding enc = this._context.getEncoding();
/* 165 */     switch (1.$SwitchMap$org$codehaus$jackson$JsonEncoding[enc.ordinal()]) {
/*     */     case 1:
/*     */     case 2:
/* 168 */       return new UTF32Reader(this._context, this._in, this._inputBuffer, this._inputPtr, this._inputEnd, this._context.getEncoding().isBigEndian());
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/* 176 */       InputStream in = this._in;
/*     */ 
/* 178 */       if (in == null) {
/* 179 */         in = new ByteArrayInputStream(this._inputBuffer, this._inputPtr, this._inputEnd);
/*     */       }
/* 184 */       else if (this._inputPtr < this._inputEnd) {
/* 185 */         in = new MergedStream(this._context, in, this._inputBuffer, this._inputPtr, this._inputEnd);
/*     */       }
/*     */ 
/* 188 */       return new InputStreamReader(in, enc.getJavaName());
/*     */     }
/*     */ 
/* 191 */     throw new RuntimeException("Internal error");
/*     */   }
/*     */ 
/*     */   public JsonParser constructParser(int features, ObjectCodec codec, BytesToNameCanonicalizer rootByteSymbols, CharsToNameCanonicalizer rootCharSymbols)
/*     */     throws IOException, JsonParseException
/*     */   {
/* 197 */     JsonEncoding enc = detectEncoding();
/*     */ 
/* 200 */     boolean canonicalize = JsonParser.Feature.CANONICALIZE_FIELD_NAMES.enabledIn(features);
/* 201 */     boolean intern = JsonParser.Feature.INTERN_FIELD_NAMES.enabledIn(features);
/* 202 */     if (enc == JsonEncoding.UTF8)
/*     */     {
/* 206 */       if (canonicalize) {
/* 207 */         BytesToNameCanonicalizer can = rootByteSymbols.makeChild(canonicalize, intern);
/* 208 */         return new Utf8StreamParser(this._context, features, this._in, codec, can, this._inputBuffer, this._inputPtr, this._inputEnd, this._bufferRecyclable);
/*     */       }
/*     */     }
/* 211 */     return new ReaderBasedParser(this._context, features, constructReader(), codec, rootCharSymbols.makeChild(canonicalize, intern));
/*     */   }
/*     */ 
/*     */   private boolean handleBOM(int quad)
/*     */     throws IOException
/*     */   {
/* 230 */     switch (quad) {
/*     */     case 65279:
/* 232 */       this._bigEndian = true;
/* 233 */       this._inputPtr += 4;
/* 234 */       this._bytesPerChar = 4;
/* 235 */       return true;
/*     */     case -131072:
/* 237 */       this._inputPtr += 4;
/* 238 */       this._bytesPerChar = 4;
/* 239 */       this._bigEndian = false;
/* 240 */       return true;
/*     */     case 65534:
/* 242 */       reportWeirdUCS4("2143");
/*     */     case -16842752:
/* 244 */       reportWeirdUCS4("3412");
/*     */     }
/*     */ 
/* 247 */     int msw = quad >>> 16;
/* 248 */     if (msw == 65279) {
/* 249 */       this._inputPtr += 2;
/* 250 */       this._bytesPerChar = 2;
/* 251 */       this._bigEndian = true;
/* 252 */       return true;
/*     */     }
/* 254 */     if (msw == 65534) {
/* 255 */       this._inputPtr += 2;
/* 256 */       this._bytesPerChar = 2;
/* 257 */       this._bigEndian = false;
/* 258 */       return true;
/*     */     }
/*     */ 
/* 261 */     if (quad >>> 8 == 15711167) {
/* 262 */       this._inputPtr += 3;
/* 263 */       this._bytesPerChar = 1;
/* 264 */       this._bigEndian = true;
/* 265 */       return true;
/*     */     }
/* 267 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean checkUTF32(int quad)
/*     */     throws IOException
/*     */   {
/* 276 */     if (quad >> 8 == 0)
/* 277 */       this._bigEndian = true;
/* 278 */     else if ((quad & 0xFFFFFF) == 0)
/* 279 */       this._bigEndian = false;
/* 280 */     else if ((quad & 0xFF00FFFF) == 0)
/* 281 */       reportWeirdUCS4("3412");
/* 282 */     else if ((quad & 0xFFFF00FF) == 0) {
/* 283 */       reportWeirdUCS4("2143");
/*     */     }
/*     */     else {
/* 286 */       return false;
/*     */     }
/*     */ 
/* 290 */     this._bytesPerChar = 4;
/* 291 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean checkUTF16(int i16)
/*     */   {
/* 296 */     if ((i16 & 0xFF00) == 0)
/* 297 */       this._bigEndian = true;
/* 298 */     else if ((i16 & 0xFF) == 0)
/* 299 */       this._bigEndian = false;
/*     */     else {
/* 301 */       return false;
/*     */     }
/*     */ 
/* 305 */     this._bytesPerChar = 2;
/* 306 */     return true;
/*     */   }
/*     */ 
/*     */   private void reportWeirdUCS4(String type)
/*     */     throws IOException
/*     */   {
/* 318 */     throw new CharConversionException("Unsupported UCS-4 endianness (" + type + ") detected");
/*     */   }
/*     */ 
/*     */   protected boolean ensureLoaded(int minimum)
/*     */     throws IOException
/*     */   {
/* 333 */     int gotten = this._inputEnd - this._inputPtr;
/* 334 */     while (gotten < minimum)
/*     */     {
/*     */       int count;
/*     */       int count;
/* 337 */       if (this._in == null)
/* 338 */         count = -1;
/*     */       else {
/* 340 */         count = this._in.read(this._inputBuffer, this._inputEnd, this._inputBuffer.length - this._inputEnd);
/*     */       }
/* 342 */       if (count < 1) {
/* 343 */         return false;
/*     */       }
/* 345 */       this._inputEnd += count;
/* 346 */       gotten += count;
/*     */     }
/* 348 */     return true;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.impl.ByteSourceBootstrapper
 * JD-Core Version:    0.6.0
 */