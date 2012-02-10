/*     */ package org.codehaus.jackson.util;
/*     */ 
/*     */ public class BufferRecycler
/*     */ {
/*     */   public static final int DEFAULT_WRITE_CONCAT_BUFFER_LEN = 2000;
/*  47 */   protected final byte[][] _byteBuffers = new byte[ByteBufferType.values().length][];
/*  48 */   protected final char[][] _charBuffers = new char[CharBufferType.values().length][];
/*     */ 
/*     */   public final byte[] allocByteBuffer(ByteBufferType type)
/*     */   {
/*  54 */     int ix = type.ordinal();
/*  55 */     byte[] buffer = this._byteBuffers[ix];
/*  56 */     if (buffer == null)
/*  57 */       buffer = balloc(type.size);
/*     */     else {
/*  59 */       this._byteBuffers[ix] = null;
/*     */     }
/*  61 */     return buffer;
/*     */   }
/*     */ 
/*     */   public final void releaseByteBuffer(ByteBufferType type, byte[] buffer)
/*     */   {
/*  66 */     this._byteBuffers[type.ordinal()] = buffer;
/*     */   }
/*     */ 
/*     */   public final char[] allocCharBuffer(CharBufferType type)
/*     */   {
/*  71 */     return allocCharBuffer(type, 0);
/*     */   }
/*     */ 
/*     */   public final char[] allocCharBuffer(CharBufferType type, int minSize)
/*     */   {
/*  76 */     if (type.size > minSize) {
/*  77 */       minSize = type.size;
/*     */     }
/*  79 */     int ix = type.ordinal();
/*  80 */     char[] buffer = this._charBuffers[ix];
/*  81 */     if ((buffer == null) || (buffer.length < minSize))
/*  82 */       buffer = calloc(minSize);
/*     */     else {
/*  84 */       this._charBuffers[ix] = null;
/*     */     }
/*  86 */     return buffer;
/*     */   }
/*     */ 
/*     */   public final void releaseCharBuffer(CharBufferType type, char[] buffer)
/*     */   {
/*  91 */     this._charBuffers[type.ordinal()] = buffer;
/*     */   }
/*     */ 
/*     */   private final byte[] balloc(int size)
/*     */   {
/* 102 */     return new byte[size];
/*     */   }
/*     */ 
/*     */   private final char[] calloc(int size)
/*     */   {
/* 107 */     return new char[size];
/*     */   }
/*     */ 
/*     */   public static enum CharBufferType
/*     */   {
/*  36 */     TOKEN_BUFFER(2000), 
/*  37 */     CONCAT_BUFFER(2000), 
/*  38 */     TEXT_BUFFER(200), 
/*  39 */     NAME_COPY_BUFFER(200);
/*     */ 
/*     */     private final int size;
/*     */ 
/*  44 */     private CharBufferType(int size) { this.size = size;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum ByteBufferType
/*     */   {
/*  16 */     READ_IO_BUFFER(4000), 
/*     */ 
/*  21 */     WRITE_ENCODING_BUFFER(4000), 
/*     */ 
/*  27 */     WRITE_CONCAT_BUFFER(2000);
/*     */ 
/*     */     private final int size;
/*     */ 
/*  32 */     private ByteBufferType(int size) { this.size = size;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.util.BufferRecycler
 * JD-Core Version:    0.6.0
 */