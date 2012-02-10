/*     */ package org.codehaus.jackson.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public final class MergedStream extends InputStream
/*     */ {
/*     */   protected final IOContext _context;
/*     */   final InputStream _in;
/*     */   byte[] _buffer;
/*     */   int _ptr;
/*     */   final int _end;
/*     */ 
/*     */   public MergedStream(IOContext context, InputStream in, byte[] buf, int start, int end)
/*     */   {
/*  29 */     this._context = context;
/*  30 */     this._in = in;
/*  31 */     this._buffer = buf;
/*  32 */     this._ptr = start;
/*  33 */     this._end = end;
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/*  39 */     if (this._buffer != null) {
/*  40 */       return this._end - this._ptr;
/*     */     }
/*  42 */     return this._in.available();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*  48 */     freeMergedBuffer();
/*  49 */     this._in.close();
/*     */   }
/*     */ 
/*     */   public void mark(int readlimit)
/*     */   {
/*  55 */     if (this._buffer == null)
/*  56 */       this._in.mark(readlimit);
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/*  64 */     return (this._buffer == null) && (this._in.markSupported());
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/*  70 */     if (this._buffer != null) {
/*  71 */       int c = this._buffer[(this._ptr++)] & 0xFF;
/*  72 */       if (this._ptr >= this._end) {
/*  73 */         freeMergedBuffer();
/*     */       }
/*  75 */       return c;
/*     */     }
/*  77 */     return this._in.read();
/*     */   }
/*     */ 
/*     */   public int read(byte[] b)
/*     */     throws IOException
/*     */   {
/*  83 */     return read(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   public int read(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/*  89 */     if (this._buffer != null) {
/*  90 */       int avail = this._end - this._ptr;
/*  91 */       if (len > avail) {
/*  92 */         len = avail;
/*     */       }
/*  94 */       System.arraycopy(this._buffer, this._ptr, b, off, len);
/*  95 */       this._ptr += len;
/*  96 */       if (this._ptr >= this._end) {
/*  97 */         freeMergedBuffer();
/*     */       }
/*  99 */       return len;
/*     */     }
/* 101 */     return this._in.read(b, off, len);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 107 */     if (this._buffer == null)
/* 108 */       this._in.reset();
/*     */   }
/*     */ 
/*     */   public long skip(long n)
/*     */     throws IOException
/*     */   {
/* 115 */     long count = 0L;
/*     */ 
/* 117 */     if (this._buffer != null) {
/* 118 */       int amount = this._end - this._ptr;
/*     */ 
/* 120 */       if (amount > n) {
/* 121 */         this._ptr += (int)n;
/* 122 */         return n;
/*     */       }
/* 124 */       freeMergedBuffer();
/* 125 */       count += amount;
/* 126 */       n -= amount;
/*     */     }
/*     */ 
/* 129 */     if (n > 0L) {
/* 130 */       count += this._in.skip(n);
/*     */     }
/* 132 */     return count;
/*     */   }
/*     */ 
/*     */   private void freeMergedBuffer()
/*     */   {
/* 137 */     byte[] buf = this._buffer;
/* 138 */     if (buf != null) {
/* 139 */       this._buffer = null;
/* 140 */       this._context.releaseReadIOBuffer(buf);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.io.MergedStream
 * JD-Core Version:    0.6.0
 */