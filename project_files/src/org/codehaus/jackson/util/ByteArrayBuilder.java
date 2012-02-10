/*     */ package org.codehaus.jackson.util;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.util.LinkedList;
/*     */ 
/*     */ public final class ByteArrayBuilder extends OutputStream
/*     */ {
/*  37 */   private static final byte[] NO_BYTES = new byte[0];
/*     */   private static final int INITIAL_BLOCK_SIZE = 500;
/*     */   private static final int MAX_BLOCK_SIZE = 262144;
/*     */   static final int DEFAULT_BLOCK_ARRAY_SIZE = 40;
/*     */   private final BufferRecycler _bufferRecycler;
/*  60 */   private final LinkedList<byte[]> _pastBlocks = new LinkedList();
/*     */   private int _pastLen;
/*     */   private byte[] _currBlock;
/*     */   private int _currBlockPtr;
/*     */ 
/*     */   public ByteArrayBuilder()
/*     */   {
/*  71 */     this(null);
/*     */   }
/*  73 */   public ByteArrayBuilder(BufferRecycler br) { this(br, 500); } 
/*     */   public ByteArrayBuilder(int firstBlockSize) {
/*  75 */     this(null, firstBlockSize);
/*     */   }
/*     */ 
/*     */   public ByteArrayBuilder(BufferRecycler br, int firstBlockSize) {
/*  79 */     this._bufferRecycler = br;
/*  80 */     if (br == null)
/*  81 */       this._currBlock = new byte[firstBlockSize];
/*     */     else
/*  83 */       this._currBlock = br.allocByteBuffer(BufferRecycler.ByteBufferType.WRITE_CONCAT_BUFFER);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/*  89 */     this._pastLen = 0;
/*  90 */     this._currBlockPtr = 0;
/*     */ 
/*  92 */     if (!this._pastBlocks.isEmpty())
/*  93 */       this._pastBlocks.clear();
/*     */   }
/*     */ 
/*     */   public void release()
/*     */   {
/* 103 */     reset();
/* 104 */     if ((this._bufferRecycler != null) && (this._currBlock != null)) {
/* 105 */       this._bufferRecycler.releaseByteBuffer(BufferRecycler.ByteBufferType.WRITE_CONCAT_BUFFER, this._currBlock);
/* 106 */       this._currBlock = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void append(int i)
/*     */   {
/* 112 */     if (this._currBlockPtr >= this._currBlock.length) {
/* 113 */       _allocMore();
/*     */     }
/* 115 */     this._currBlock[(this._currBlockPtr++)] = (byte)i;
/*     */   }
/*     */ 
/*     */   public void appendTwoBytes(int b16)
/*     */   {
/* 120 */     if (this._currBlockPtr + 1 < this._currBlock.length) {
/* 121 */       this._currBlock[(this._currBlockPtr++)] = (byte)(b16 >> 8);
/* 122 */       this._currBlock[(this._currBlockPtr++)] = (byte)b16;
/*     */     } else {
/* 124 */       append(b16 >> 8);
/* 125 */       append(b16);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void appendThreeBytes(int b24)
/*     */   {
/* 131 */     if (this._currBlockPtr + 2 < this._currBlock.length) {
/* 132 */       this._currBlock[(this._currBlockPtr++)] = (byte)(b24 >> 16);
/* 133 */       this._currBlock[(this._currBlockPtr++)] = (byte)(b24 >> 8);
/* 134 */       this._currBlock[(this._currBlockPtr++)] = (byte)b24;
/*     */     } else {
/* 136 */       append(b24 >> 16);
/* 137 */       append(b24 >> 8);
/* 138 */       append(b24);
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] toByteArray()
/*     */   {
/* 148 */     int totalLen = this._pastLen + this._currBlockPtr;
/*     */ 
/* 150 */     if (totalLen == 0) {
/* 151 */       return NO_BYTES;
/*     */     }
/*     */ 
/* 154 */     byte[] result = new byte[totalLen];
/* 155 */     int offset = 0;
/*     */ 
/* 157 */     for (byte[] block : this._pastBlocks) {
/* 158 */       int len = block.length;
/* 159 */       System.arraycopy(block, 0, result, offset, len);
/* 160 */       offset += len;
/*     */     }
/* 162 */     System.arraycopy(this._currBlock, 0, result, offset, this._currBlockPtr);
/* 163 */     offset += this._currBlockPtr;
/* 164 */     if (offset != totalLen) {
/* 165 */       throw new RuntimeException("Internal error: total len assumed to be " + totalLen + ", copied " + offset + " bytes");
/*     */     }
/*     */ 
/* 168 */     if (!this._pastBlocks.isEmpty()) {
/* 169 */       reset();
/*     */     }
/* 171 */     return result;
/*     */   }
/*     */ 
/*     */   public byte[] resetAndGetFirstSegment()
/*     */   {
/* 187 */     reset();
/* 188 */     return this._currBlock;
/*     */   }
/*     */ 
/*     */   public byte[] finishCurrentSegment()
/*     */   {
/* 199 */     _allocMore();
/* 200 */     return this._currBlock;
/*     */   }
/*     */ 
/*     */   public byte[] completeAndCoalesce(int lastBlockLength)
/*     */   {
/* 214 */     this._currBlockPtr = lastBlockLength;
/* 215 */     return toByteArray();
/*     */   }
/*     */ 
/*     */   public byte[] getCurrentSegment() {
/* 219 */     return this._currBlock;
/*     */   }
/*     */ 
/*     */   public void setCurrentSegmentLength(int len) {
/* 223 */     this._currBlockPtr = len;
/*     */   }
/*     */ 
/*     */   public int getCurrentSegmentLength() {
/* 227 */     return this._currBlockPtr;
/*     */   }
/*     */ 
/*     */   public void write(byte[] b)
/*     */   {
/* 238 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   public void write(byte[] b, int off, int len)
/*     */   {
/*     */     while (true)
/*     */     {
/* 245 */       int max = this._currBlock.length - this._currBlockPtr;
/* 246 */       int toCopy = Math.min(max, len);
/* 247 */       if (toCopy > 0) {
/* 248 */         System.arraycopy(b, off, this._currBlock, this._currBlockPtr, toCopy);
/* 249 */         off += toCopy;
/* 250 */         this._currBlockPtr += toCopy;
/* 251 */         len -= toCopy;
/*     */       }
/* 253 */       if (len <= 0) break;
/* 254 */       _allocMore();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(int b)
/*     */   {
/* 260 */     append(b);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */   {
/*     */   }
/*     */ 
/*     */   private void _allocMore()
/*     */   {
/* 275 */     this._pastLen += this._currBlock.length;
/*     */ 
/* 283 */     int newSize = Math.max(this._pastLen >> 1, 1000);
/*     */ 
/* 285 */     if (newSize > 262144) {
/* 286 */       newSize = 262144;
/*     */     }
/* 288 */     this._pastBlocks.add(this._currBlock);
/* 289 */     this._currBlock = new byte[newSize];
/* 290 */     this._currBlockPtr = 0;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.util.ByteArrayBuilder
 * JD-Core Version:    0.6.0
 */