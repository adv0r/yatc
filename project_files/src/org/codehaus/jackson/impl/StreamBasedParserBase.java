/*     */ package org.codehaus.jackson.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.codehaus.jackson.JsonParser.Feature;
/*     */ import org.codehaus.jackson.io.IOContext;
/*     */ 
/*     */ public abstract class StreamBasedParserBase extends JsonNumericParserBase
/*     */ {
/*     */   protected InputStream _inputStream;
/*     */   protected byte[] _inputBuffer;
/*     */   protected boolean _bufferRecyclable;
/*     */ 
/*     */   protected StreamBasedParserBase(IOContext ctxt, int features, InputStream in, byte[] inputBuffer, int start, int end, boolean bufferRecyclable)
/*     */   {
/*  66 */     super(ctxt, features);
/*  67 */     this._inputStream = in;
/*  68 */     this._inputBuffer = inputBuffer;
/*  69 */     this._inputPtr = start;
/*  70 */     this._inputEnd = end;
/*  71 */     this._bufferRecyclable = bufferRecyclable;
/*     */   }
/*     */ 
/*     */   public int releaseBuffered(OutputStream out)
/*     */     throws IOException
/*     */   {
/*  83 */     int count = this._inputEnd - this._inputPtr;
/*  84 */     if (count < 1) {
/*  85 */       return 0;
/*     */     }
/*     */ 
/*  88 */     int origPtr = this._inputPtr;
/*  89 */     out.write(this._inputBuffer, origPtr, count);
/*  90 */     return count;
/*     */   }
/*     */ 
/*     */   protected final boolean loadMore()
/*     */     throws IOException
/*     */   {
/* 103 */     this._currInputProcessed += this._inputEnd;
/* 104 */     this._currInputRowStart -= this._inputEnd;
/*     */ 
/* 106 */     if (this._inputStream != null) {
/* 107 */       int count = this._inputStream.read(this._inputBuffer, 0, this._inputBuffer.length);
/* 108 */       if (count > 0) {
/* 109 */         this._inputPtr = 0;
/* 110 */         this._inputEnd = count;
/* 111 */         return true;
/*     */       }
/*     */ 
/* 114 */       _closeInput();
/*     */ 
/* 116 */       if (count == 0) {
/* 117 */         throw new IOException("InputStream.read() returned 0 characters when trying to read " + this._inputBuffer.length + " bytes");
/*     */       }
/*     */     }
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */   protected final boolean _loadToHaveAtLeast(int minAvailable)
/*     */     throws IOException
/*     */   {
/* 133 */     if (this._inputStream == null) {
/* 134 */       return false;
/*     */     }
/*     */ 
/* 137 */     int amount = this._inputEnd - this._inputPtr;
/* 138 */     if ((amount > 0) && (this._inputPtr > 0)) {
/* 139 */       this._currInputProcessed += this._inputPtr;
/* 140 */       this._currInputRowStart -= this._inputPtr;
/* 141 */       System.arraycopy(this._inputBuffer, this._inputPtr, this._inputBuffer, 0, amount);
/* 142 */       this._inputEnd = amount;
/*     */     } else {
/* 144 */       this._inputEnd = 0;
/*     */     }
/* 146 */     this._inputPtr = 0;
/* 147 */     while (this._inputEnd < minAvailable) {
/* 148 */       int count = this._inputStream.read(this._inputBuffer, this._inputEnd, this._inputBuffer.length - this._inputEnd);
/* 149 */       if (count < 1)
/*     */       {
/* 151 */         _closeInput();
/*     */ 
/* 153 */         if (count == 0) {
/* 154 */           throw new IOException("InputStream.read() returned 0 characters when trying to read " + amount + " bytes");
/*     */         }
/* 156 */         return false;
/*     */       }
/* 158 */       this._inputEnd += count;
/*     */     }
/* 160 */     return true;
/*     */   }
/*     */ 
/*     */   protected void _closeInput()
/*     */     throws IOException
/*     */   {
/* 170 */     if (this._inputStream != null) {
/* 171 */       if ((this._ioContext.isResourceManaged()) || (isEnabled(JsonParser.Feature.AUTO_CLOSE_SOURCE))) {
/* 172 */         this._inputStream.close();
/*     */       }
/* 174 */       this._inputStream = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void _releaseBuffers()
/*     */     throws IOException
/*     */   {
/* 187 */     super._releaseBuffers();
/* 188 */     if (this._bufferRecyclable) {
/* 189 */       byte[] buf = this._inputBuffer;
/* 190 */       if (buf != null) {
/* 191 */         this._inputBuffer = null;
/* 192 */         this._ioContext.releaseReadIOBuffer(buf);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.impl.StreamBasedParserBase
 * JD-Core Version:    0.6.0
 */