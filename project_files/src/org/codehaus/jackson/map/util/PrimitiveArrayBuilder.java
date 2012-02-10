/*     */ package org.codehaus.jackson.map.util;
/*     */ 
/*     */ public abstract class PrimitiveArrayBuilder<T>
/*     */ {
/*     */   static final int INITIAL_CHUNK_SIZE = 12;
/*     */   static final int SMALL_CHUNK_SIZE = 16384;
/*     */   static final int MAX_CHUNK_SIZE = 262144;
/*     */   T _freeBuffer;
/*     */   Node<T> _bufferHead;
/*     */   Node<T> _bufferTail;
/*     */   int _bufferedEntryCount;
/*     */ 
/*     */   public T resetAndStart()
/*     */   {
/*  55 */     _reset();
/*  56 */     return this._freeBuffer == null ? _constructArray(12) : this._freeBuffer;
/*     */   }
/*     */ 
/*     */   public final T appendCompletedChunk(T fullChunk, int fullChunkLength)
/*     */   {
/*  65 */     Node next = new Node(fullChunk, fullChunkLength);
/*  66 */     if (this._bufferHead == null) {
/*  67 */       this._bufferHead = (this._bufferTail = next);
/*     */     } else {
/*  69 */       this._bufferTail.linkNext(next);
/*  70 */       this._bufferTail = next;
/*     */     }
/*  72 */     this._bufferedEntryCount += fullChunkLength;
/*  73 */     int nextLen = fullChunkLength;
/*     */ 
/*  75 */     if (nextLen < 16384)
/*  76 */       nextLen += nextLen;
/*     */     else {
/*  78 */       nextLen += (nextLen >> 2);
/*     */     }
/*  80 */     return _constructArray(nextLen);
/*     */   }
/*     */ 
/*     */   public T completeAndClearBuffer(T lastChunk, int lastChunkEntries)
/*     */   {
/*  85 */     int totalSize = lastChunkEntries + this._bufferedEntryCount;
/*  86 */     Object resultArray = _constructArray(totalSize);
/*     */ 
/*  88 */     int ptr = 0;
/*     */ 
/*  90 */     for (Node n = this._bufferHead; n != null; n = n.next()) {
/*  91 */       ptr = n.copyData(resultArray, ptr);
/*     */     }
/*  93 */     System.arraycopy(lastChunk, 0, resultArray, ptr, lastChunkEntries);
/*  94 */     ptr += lastChunkEntries;
/*     */ 
/*  97 */     if (ptr != totalSize) {
/*  98 */       throw new IllegalStateException("Should have gotten " + totalSize + " entries, got " + ptr);
/*     */     }
/* 100 */     return resultArray;
/*     */   }
/*     */ 
/*     */   protected abstract T _constructArray(int paramInt);
/*     */ 
/*     */   protected void _reset()
/*     */   {
/* 120 */     if (this._bufferTail != null) {
/* 121 */       this._freeBuffer = this._bufferTail.getData();
/*     */     }
/*     */ 
/* 124 */     this._bufferHead = (this._bufferTail = null);
/* 125 */     this._bufferedEntryCount = 0;
/*     */   }
/*     */ 
/*     */   static final class Node<T>
/*     */   {
/*     */     final T _data;
/*     */     final int _dataLength;
/*     */     Node<T> _next;
/*     */ 
/*     */     public Node(T data, int dataLen)
/*     */     {
/* 157 */       this._data = data;
/* 158 */       this._dataLength = dataLen;
/*     */     }
/*     */     public T getData() {
/* 161 */       return this._data;
/*     */     }
/*     */ 
/*     */     public int copyData(T dst, int ptr) {
/* 165 */       System.arraycopy(this._data, 0, dst, ptr, this._dataLength);
/* 166 */       ptr += this._dataLength;
/* 167 */       return ptr;
/*     */     }
/*     */     public Node<T> next() {
/* 170 */       return this._next;
/*     */     }
/*     */ 
/*     */     public void linkNext(Node<T> next) {
/* 174 */       if (this._next != null) {
/* 175 */         throw new IllegalStateException();
/*     */       }
/* 177 */       this._next = next;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.util.PrimitiveArrayBuilder
 * JD-Core Version:    0.6.0
 */