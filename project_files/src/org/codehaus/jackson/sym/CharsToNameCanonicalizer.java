/*     */ package org.codehaus.jackson.sym;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import org.codehaus.jackson.util.InternCache;
/*     */ 
/*     */ public final class CharsToNameCanonicalizer
/*     */ {
/*     */   protected static final int DEFAULT_TABLE_SIZE = 64;
/*     */   protected static final int MAX_TABLE_SIZE = 65536;
/*     */   static final int MAX_ENTRIES_FOR_REUSE = 12000;
/*  72 */   static final CharsToNameCanonicalizer sBootstrapSymbolTable = new CharsToNameCanonicalizer();
/*     */   protected CharsToNameCanonicalizer _parent;
/*     */   protected final boolean _intern;
/*     */   protected final boolean _canonicalize;
/*     */   protected String[] _symbols;
/*     */   protected Bucket[] _buckets;
/*     */   protected int _size;
/*     */   protected int _sizeThreshold;
/*     */   protected int _indexMask;
/*     */   protected boolean _dirty;
/*     */ 
/*     */   public static CharsToNameCanonicalizer createRoot()
/*     */   {
/* 169 */     return sBootstrapSymbolTable.makeOrphan();
/*     */   }
/*     */ 
/*     */   private CharsToNameCanonicalizer()
/*     */   {
/* 181 */     this._canonicalize = true;
/* 182 */     this._intern = true;
/*     */ 
/* 184 */     this._dirty = true;
/* 185 */     initTables(64);
/*     */   }
/*     */ 
/*     */   private void initTables(int initialSize)
/*     */   {
/* 190 */     this._symbols = new String[initialSize];
/* 191 */     this._buckets = new Bucket[initialSize >> 1];
/*     */ 
/* 193 */     this._indexMask = (initialSize - 1);
/* 194 */     this._size = 0;
/*     */ 
/* 196 */     this._sizeThreshold = (initialSize - (initialSize >> 2));
/*     */   }
/*     */ 
/*     */   private CharsToNameCanonicalizer(CharsToNameCanonicalizer parent, boolean canonicalize, boolean intern, String[] symbols, Bucket[] buckets, int size)
/*     */   {
/* 206 */     this._parent = parent;
/* 207 */     this._canonicalize = canonicalize;
/* 208 */     this._intern = intern;
/*     */ 
/* 210 */     this._symbols = symbols;
/* 211 */     this._buckets = buckets;
/* 212 */     this._size = size;
/*     */ 
/* 214 */     int arrayLen = symbols.length;
/* 215 */     this._sizeThreshold = (arrayLen - (arrayLen >> 2));
/* 216 */     this._indexMask = (arrayLen - 1);
/*     */ 
/* 219 */     this._dirty = false;
/*     */   }
/*     */ 
/*     */   public synchronized CharsToNameCanonicalizer makeChild(boolean canonicalize, boolean intern)
/*     */   {
/* 236 */     return new CharsToNameCanonicalizer(this, canonicalize, intern, this._symbols, this._buckets, this._size);
/*     */   }
/*     */ 
/*     */   private CharsToNameCanonicalizer makeOrphan()
/*     */   {
/* 241 */     return new CharsToNameCanonicalizer(null, true, true, this._symbols, this._buckets, this._size);
/*     */   }
/*     */ 
/*     */   private synchronized void mergeChild(CharsToNameCanonicalizer child)
/*     */   {
/* 259 */     if (child.size() > 12000)
/*     */     {
/* 265 */       initTables(64);
/*     */     }
/*     */     else
/*     */     {
/* 271 */       if (child.size() <= size()) {
/* 272 */         return;
/*     */       }
/*     */ 
/* 275 */       this._symbols = child._symbols;
/* 276 */       this._buckets = child._buckets;
/* 277 */       this._size = child._size;
/* 278 */       this._sizeThreshold = child._sizeThreshold;
/* 279 */       this._indexMask = child._indexMask;
/*     */     }
/*     */ 
/* 285 */     this._dirty = false;
/*     */   }
/*     */ 
/*     */   public void release()
/*     */   {
/* 291 */     if (!maybeDirty()) {
/* 292 */       return;
/*     */     }
/* 294 */     if (this._parent != null) {
/* 295 */       this._parent.mergeChild(this);
/*     */ 
/* 300 */       this._dirty = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 310 */     return this._size;
/*     */   }
/* 312 */   public boolean maybeDirty() { return this._dirty;
/*     */   }
/*     */ 
/*     */   public String findSymbol(char[] buffer, int start, int len, int hash)
/*     */   {
/* 322 */     if (len < 1) {
/* 323 */       return "";
/*     */     }
/* 325 */     if (!this._canonicalize) {
/* 326 */       return new String(buffer, start, len);
/*     */     }
/*     */ 
/* 329 */     hash &= this._indexMask;
/*     */ 
/* 331 */     String sym = this._symbols[hash];
/*     */ 
/* 334 */     if (sym != null)
/*     */     {
/* 336 */       if (sym.length() == len) {
/* 337 */         int i = 0;
/*     */         do {
/* 339 */           if (sym.charAt(i) != buffer[(start + i)]) {
/*     */             break;
/*     */           }
/* 342 */           i++; } while (i < len);
/*     */ 
/* 344 */         if (i == len) {
/* 345 */           return sym;
/*     */         }
/*     */       }
/*     */ 
/* 349 */       Bucket b = this._buckets[(hash >> 1)];
/* 350 */       if (b != null) {
/* 351 */         sym = b.find(buffer, start, len);
/* 352 */         if (sym != null) {
/* 353 */           return sym;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 358 */     if (!this._dirty) {
/* 359 */       copyArrays();
/* 360 */       this._dirty = true;
/* 361 */     } else if (this._size >= this._sizeThreshold) {
/* 362 */       rehash();
/*     */ 
/* 366 */       hash = calcHash(buffer, start, len) & this._indexMask;
/*     */     }
/* 368 */     this._size += 1;
/*     */ 
/* 370 */     String newSymbol = new String(buffer, start, len);
/* 371 */     if (this._intern) {
/* 372 */       newSymbol = InternCache.instance.intern(newSymbol);
/*     */     }
/*     */ 
/* 375 */     if (this._symbols[hash] == null) {
/* 376 */       this._symbols[hash] = newSymbol;
/*     */     } else {
/* 378 */       int bix = hash >> 1;
/* 379 */       this._buckets[bix] = new Bucket(newSymbol, this._buckets[bix]);
/*     */     }
/*     */ 
/* 382 */     return newSymbol;
/*     */   }
/*     */ 
/*     */   public static int calcHash(char[] buffer, int start, int len)
/*     */   {
/* 395 */     int hash = buffer[0];
/* 396 */     for (int i = 1; i < len; i++) {
/* 397 */       hash = hash * 31 + buffer[i];
/*     */     }
/* 399 */     return hash;
/*     */   }
/*     */ 
/*     */   public static int calcHash(String key) {
/* 403 */     int hash = key.charAt(0);
/* 404 */     int i = 1; for (int len = key.length(); i < len; i++) {
/* 405 */       hash = hash * 31 + key.charAt(i);
/*     */     }
/*     */ 
/* 408 */     return hash;
/*     */   }
/*     */ 
/*     */   private void copyArrays()
/*     */   {
/* 422 */     String[] oldSyms = this._symbols;
/* 423 */     int size = oldSyms.length;
/* 424 */     this._symbols = new String[size];
/* 425 */     System.arraycopy(oldSyms, 0, this._symbols, 0, size);
/* 426 */     Bucket[] oldBuckets = this._buckets;
/* 427 */     size = oldBuckets.length;
/* 428 */     this._buckets = new Bucket[size];
/* 429 */     System.arraycopy(oldBuckets, 0, this._buckets, 0, size);
/*     */   }
/*     */ 
/*     */   private void rehash()
/*     */   {
/* 441 */     int size = this._symbols.length;
/* 442 */     int newSize = size + size;
/*     */ 
/* 448 */     if (newSize > 65536)
/*     */     {
/* 453 */       this._size = 0;
/* 454 */       Arrays.fill(this._symbols, null);
/* 455 */       Arrays.fill(this._buckets, null);
/* 456 */       this._dirty = true;
/* 457 */       return;
/*     */     }
/*     */ 
/* 460 */     String[] oldSyms = this._symbols;
/* 461 */     Bucket[] oldBuckets = this._buckets;
/* 462 */     this._symbols = new String[newSize];
/* 463 */     this._buckets = new Bucket[newSize >> 1];
/*     */ 
/* 465 */     this._indexMask = (newSize - 1);
/* 466 */     this._sizeThreshold += this._sizeThreshold;
/*     */ 
/* 468 */     int count = 0;
/*     */ 
/* 473 */     for (int i = 0; i < size; i++) {
/* 474 */       String symbol = oldSyms[i];
/* 475 */       if (symbol != null) {
/* 476 */         count++;
/* 477 */         int index = calcHash(symbol) & this._indexMask;
/* 478 */         if (this._symbols[index] == null) {
/* 479 */           this._symbols[index] = symbol;
/*     */         } else {
/* 481 */           int bix = index >> 1;
/* 482 */           this._buckets[bix] = new Bucket(symbol, this._buckets[bix]);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 487 */     size >>= 1;
/* 488 */     for (int i = 0; i < size; i++) {
/* 489 */       Bucket b = oldBuckets[i];
/* 490 */       while (b != null) {
/* 491 */         count++;
/* 492 */         String symbol = b.getSymbol();
/* 493 */         int index = calcHash(symbol) & this._indexMask;
/* 494 */         if (this._symbols[index] == null) {
/* 495 */           this._symbols[index] = symbol;
/*     */         } else {
/* 497 */           int bix = index >> 1;
/* 498 */           this._buckets[bix] = new Bucket(symbol, this._buckets[bix]);
/*     */         }
/* 500 */         b = b.getNext();
/*     */       }
/*     */     }
/*     */ 
/* 504 */     if (count != this._size)
/* 505 */       throw new Error("Internal error on SymbolTable.rehash(): had " + this._size + " entries; now have " + count + ".");
/*     */   }
/*     */ 
/*     */   static final class Bucket
/*     */   {
/*     */     private final String _symbol;
/*     */     private final Bucket mNext;
/*     */ 
/*     */     public Bucket(String symbol, Bucket next)
/*     */     {
/* 524 */       this._symbol = symbol;
/* 525 */       this.mNext = next;
/*     */     }
/*     */     public String getSymbol() {
/* 528 */       return this._symbol; } 
/* 529 */     public Bucket getNext() { return this.mNext; }
/*     */ 
/*     */     public String find(char[] buf, int start, int len) {
/* 532 */       String sym = this._symbol;
/* 533 */       Bucket b = this.mNext;
/*     */       while (true)
/*     */       {
/* 536 */         if (sym.length() == len) {
/* 537 */           int i = 0;
/*     */           do {
/* 539 */             if (sym.charAt(i) != buf[(start + i)]) {
/*     */               break;
/*     */             }
/* 542 */             i++; } while (i < len);
/* 543 */           if (i == len) {
/* 544 */             return sym;
/*     */           }
/*     */         }
/* 547 */         if (b == null) {
/*     */           break;
/*     */         }
/* 550 */         sym = b.getSymbol();
/* 551 */         b = b.getNext();
/*     */       }
/* 553 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.sym.CharsToNameCanonicalizer
 * JD-Core Version:    0.6.0
 */