/*     */ package org.codehaus.jackson.map.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.List;
/*     */ 
/*     */ public final class ObjectBuffer
/*     */ {
/*     */   static final int INITIAL_CHUNK_SIZE = 12;
/*     */   static final int SMALL_CHUNK_SIZE = 16384;
/*     */   static final int MAX_CHUNK_SIZE = 262144;
/*     */   private Node _bufferHead;
/*     */   private Node _bufferTail;
/*     */   private int _bufferedEntryCount;
/*     */   private Object[] _freeBuffer;
/*     */ 
/*     */   public Object[] resetAndStart()
/*     */   {
/*  73 */     _reset();
/*  74 */     if (this._freeBuffer == null) {
/*  75 */       return new Object[12];
/*     */     }
/*  77 */     return this._freeBuffer;
/*     */   }
/*     */ 
/*     */   public Object[] appendCompletedChunk(Object[] fullChunk)
/*     */   {
/*  96 */     Node next = new Node(fullChunk);
/*  97 */     if (this._bufferHead == null) {
/*  98 */       this._bufferHead = (this._bufferTail = next);
/*     */     } else {
/* 100 */       this._bufferTail.linkNext(next);
/* 101 */       this._bufferTail = next;
/*     */     }
/* 103 */     int len = fullChunk.length;
/* 104 */     this._bufferedEntryCount += len;
/*     */ 
/* 106 */     if (len < 16384)
/* 107 */       len += len;
/*     */     else {
/* 109 */       len += (len >> 2);
/*     */     }
/* 111 */     return new Object[len];
/*     */   }
/*     */ 
/*     */   public Object[] completeAndClearBuffer(Object[] lastChunk, int lastChunkEntries)
/*     */   {
/* 126 */     int totalSize = lastChunkEntries + this._bufferedEntryCount;
/* 127 */     Object[] result = new Object[totalSize];
/* 128 */     _copyTo(result, totalSize, lastChunk, lastChunkEntries);
/* 129 */     return result;
/*     */   }
/*     */ 
/*     */   public <T> T[] completeAndClearBuffer(Object[] lastChunk, int lastChunkEntries, Class<T> componentType)
/*     */   {
/* 142 */     int totalSize = lastChunkEntries + this._bufferedEntryCount;
/*     */ 
/* 144 */     Object[] result = (Object[])(Object[])Array.newInstance(componentType, totalSize);
/* 145 */     _copyTo(result, totalSize, lastChunk, lastChunkEntries);
/* 146 */     _reset();
/* 147 */     return result;
/*     */   }
/*     */ 
/*     */   public void completeAndClearBuffer(Object[] lastChunk, int lastChunkEntries, List<Object> resultList)
/*     */   {
/* 157 */     for (Node n = this._bufferHead; n != null; n = n.next()) {
/* 158 */       Object[] curr = n.getData();
/* 159 */       int i = 0; for (int len = curr.length; i < len; i++) {
/* 160 */         resultList.add(curr[i]);
/*     */       }
/*     */     }
/*     */ 
/* 164 */     for (int i = 0; i < lastChunkEntries; i++)
/* 165 */       resultList.add(lastChunk[i]);
/*     */   }
/*     */ 
/*     */   public int initialCapacity()
/*     */   {
/* 177 */     return this._freeBuffer == null ? 0 : this._freeBuffer.length;
/*     */   }
/*     */ 
/*     */   public int bufferedSize()
/*     */   {
/* 184 */     return this._bufferedEntryCount;
/*     */   }
/*     */ 
/*     */   protected void _reset()
/*     */   {
/* 195 */     if (this._bufferTail != null) {
/* 196 */       this._freeBuffer = this._bufferTail.getData();
/*     */     }
/*     */ 
/* 199 */     this._bufferHead = (this._bufferTail = null);
/* 200 */     this._bufferedEntryCount = 0;
/*     */   }
/*     */ 
/*     */   protected final void _copyTo(Object resultArray, int totalSize, Object[] lastChunk, int lastChunkEntries)
/*     */   {
/* 206 */     int ptr = 0;
/*     */ 
/* 208 */     for (Node n = this._bufferHead; n != null; n = n.next()) {
/* 209 */       Object[] curr = n.getData();
/* 210 */       int len = curr.length;
/* 211 */       System.arraycopy(curr, 0, resultArray, ptr, len);
/* 212 */       ptr += len;
/*     */     }
/* 214 */     System.arraycopy(lastChunk, 0, resultArray, ptr, lastChunkEntries);
/* 215 */     ptr += lastChunkEntries;
/*     */ 
/* 218 */     if (ptr != totalSize)
/* 219 */       throw new IllegalStateException("Should have gotten " + totalSize + " entries, got " + ptr);
/*     */   }
/*     */ 
/*     */   static final class Node
/*     */   {
/*     */     final Object[] _data;
/*     */     Node _next;
/*     */ 
/*     */     public Node(Object[] data)
/*     */     {
/* 242 */       this._data = data;
/*     */     }
/*     */     public Object[] getData() {
/* 245 */       return this._data;
/*     */     }
/* 247 */     public Node next() { return this._next; }
/*     */ 
/*     */     public void linkNext(Node next)
/*     */     {
/* 251 */       if (this._next != null) {
/* 252 */         throw new IllegalStateException();
/*     */       }
/* 254 */       this._next = next;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.util.ObjectBuffer
 * JD-Core Version:    0.6.0
 */