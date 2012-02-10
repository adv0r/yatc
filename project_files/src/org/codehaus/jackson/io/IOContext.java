/*     */ package org.codehaus.jackson.io;
/*     */ 
/*     */ import org.codehaus.jackson.JsonEncoding;
/*     */ import org.codehaus.jackson.util.BufferRecycler;
/*     */ import org.codehaus.jackson.util.BufferRecycler.ByteBufferType;
/*     */ import org.codehaus.jackson.util.BufferRecycler.CharBufferType;
/*     */ import org.codehaus.jackson.util.TextBuffer;
/*     */ 
/*     */ public final class IOContext
/*     */ {
/*     */   protected final Object _sourceRef;
/*     */   protected JsonEncoding _encoding;
/*     */   protected final boolean _managedResource;
/*     */   protected final BufferRecycler _bufferRecycler;
/*  57 */   protected byte[] _readIOBuffer = null;
/*     */ 
/*  63 */   protected byte[] _writeEncodingBuffer = null;
/*     */ 
/*  70 */   protected char[] _tokenCBuffer = null;
/*     */ 
/*  77 */   protected char[] _concatCBuffer = null;
/*     */ 
/*  85 */   protected char[] _nameCopyBuffer = null;
/*     */ 
/*     */   public IOContext(BufferRecycler br, Object sourceRef, boolean managedResource)
/*     */   {
/*  95 */     this._bufferRecycler = br;
/*  96 */     this._sourceRef = sourceRef;
/*  97 */     this._managedResource = managedResource;
/*     */   }
/*     */ 
/*     */   public void setEncoding(JsonEncoding enc)
/*     */   {
/* 102 */     this._encoding = enc;
/*     */   }
/*     */ 
/*     */   public final Object getSourceReference()
/*     */   {
/* 111 */     return this._sourceRef; } 
/* 112 */   public final JsonEncoding getEncoding() { return this._encoding; } 
/* 113 */   public final boolean isResourceManaged() { return this._managedResource;
/*     */   }
/*     */ 
/*     */   public final TextBuffer constructTextBuffer()
/*     */   {
/* 122 */     return new TextBuffer(this._bufferRecycler);
/*     */   }
/*     */ 
/*     */   public final byte[] allocReadIOBuffer()
/*     */   {
/* 132 */     if (this._readIOBuffer != null) {
/* 133 */       throw new IllegalStateException("Trying to call allocReadIOBuffer() second time");
/*     */     }
/* 135 */     this._readIOBuffer = this._bufferRecycler.allocByteBuffer(BufferRecycler.ByteBufferType.READ_IO_BUFFER);
/* 136 */     return this._readIOBuffer;
/*     */   }
/*     */ 
/*     */   public final byte[] allocWriteEncodingBuffer()
/*     */   {
/* 141 */     if (this._writeEncodingBuffer != null) {
/* 142 */       throw new IllegalStateException("Trying to call allocWriteEncodingBuffer() second time");
/*     */     }
/* 144 */     this._writeEncodingBuffer = this._bufferRecycler.allocByteBuffer(BufferRecycler.ByteBufferType.WRITE_ENCODING_BUFFER);
/* 145 */     return this._writeEncodingBuffer;
/*     */   }
/*     */ 
/*     */   public final char[] allocTokenBuffer()
/*     */   {
/* 150 */     if (this._tokenCBuffer != null) {
/* 151 */       throw new IllegalStateException("Trying to call allocTokenBuffer() second time");
/*     */     }
/* 153 */     this._tokenCBuffer = this._bufferRecycler.allocCharBuffer(BufferRecycler.CharBufferType.TOKEN_BUFFER);
/* 154 */     return this._tokenCBuffer;
/*     */   }
/*     */ 
/*     */   public final char[] allocConcatBuffer()
/*     */   {
/* 159 */     if (this._concatCBuffer != null) {
/* 160 */       throw new IllegalStateException("Trying to call allocConcatBuffer() second time");
/*     */     }
/* 162 */     this._concatCBuffer = this._bufferRecycler.allocCharBuffer(BufferRecycler.CharBufferType.CONCAT_BUFFER);
/* 163 */     return this._concatCBuffer;
/*     */   }
/*     */ 
/*     */   public final char[] allocNameCopyBuffer(int minSize)
/*     */   {
/* 168 */     if (this._nameCopyBuffer != null) {
/* 169 */       throw new IllegalStateException("Trying to call allocNameCopyBuffer() second time");
/*     */     }
/* 171 */     this._nameCopyBuffer = this._bufferRecycler.allocCharBuffer(BufferRecycler.CharBufferType.NAME_COPY_BUFFER, minSize);
/* 172 */     return this._nameCopyBuffer;
/*     */   }
/*     */ 
/*     */   public final void releaseReadIOBuffer(byte[] buf)
/*     */   {
/* 181 */     if (buf != null)
/*     */     {
/* 185 */       if (buf != this._readIOBuffer) {
/* 186 */         throw new IllegalArgumentException("Trying to release buffer not owned by the context");
/*     */       }
/* 188 */       this._readIOBuffer = null;
/* 189 */       this._bufferRecycler.releaseByteBuffer(BufferRecycler.ByteBufferType.READ_IO_BUFFER, buf);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void releaseWriteEncodingBuffer(byte[] buf)
/*     */   {
/* 195 */     if (buf != null)
/*     */     {
/* 199 */       if (buf != this._writeEncodingBuffer) {
/* 200 */         throw new IllegalArgumentException("Trying to release buffer not owned by the context");
/*     */       }
/* 202 */       this._writeEncodingBuffer = null;
/* 203 */       this._bufferRecycler.releaseByteBuffer(BufferRecycler.ByteBufferType.WRITE_ENCODING_BUFFER, buf);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void releaseTokenBuffer(char[] buf)
/*     */   {
/* 209 */     if (buf != null) {
/* 210 */       if (buf != this._tokenCBuffer) {
/* 211 */         throw new IllegalArgumentException("Trying to release buffer not owned by the context");
/*     */       }
/* 213 */       this._tokenCBuffer = null;
/* 214 */       this._bufferRecycler.releaseCharBuffer(BufferRecycler.CharBufferType.TOKEN_BUFFER, buf);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void releaseConcatBuffer(char[] buf)
/*     */   {
/* 220 */     if (buf != null) {
/* 221 */       if (buf != this._concatCBuffer) {
/* 222 */         throw new IllegalArgumentException("Trying to release buffer not owned by the context");
/*     */       }
/* 224 */       this._concatCBuffer = null;
/* 225 */       this._bufferRecycler.releaseCharBuffer(BufferRecycler.CharBufferType.CONCAT_BUFFER, buf);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void releaseNameCopyBuffer(char[] buf)
/*     */   {
/* 231 */     if (buf != null) {
/* 232 */       if (buf != this._nameCopyBuffer) {
/* 233 */         throw new IllegalArgumentException("Trying to release buffer not owned by the context");
/*     */       }
/* 235 */       this._nameCopyBuffer = null;
/* 236 */       this._bufferRecycler.releaseCharBuffer(BufferRecycler.CharBufferType.NAME_COPY_BUFFER, buf);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.io.IOContext
 * JD-Core Version:    0.6.0
 */