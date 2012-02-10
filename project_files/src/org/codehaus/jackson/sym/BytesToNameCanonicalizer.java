/*     */ package org.codehaus.jackson.sym;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import org.codehaus.jackson.util.InternCache;
/*     */ 
/*     */ public final class BytesToNameCanonicalizer
/*     */ {
/*     */   protected static final int DEFAULT_TABLE_SIZE = 64;
/*     */   protected static final int MAX_TABLE_SIZE = 65536;
/*     */   static final int MAX_ENTRIES_FOR_REUSE = 6000;
/*     */   static final int MIN_HASH_SIZE = 16;
/*     */   static final int INITIAL_COLLISION_LEN = 32;
/*     */   static final int LAST_VALID_BUCKET = 254;
/*     */   final BytesToNameCanonicalizer _parent;
/*     */   final boolean _intern;
/*     */   private int _count;
/*     */   private int _mainHashMask;
/*     */   private int[] _mainHash;
/*     */   private Name[] _mainNames;
/*     */   private Bucket[] _collList;
/*     */   private int _collCount;
/*     */   private int _collEnd;
/*     */   private transient boolean _needRehash;
/*     */   private boolean _mainHashShared;
/*     */   private boolean _mainNamesShared;
/*     */   private boolean _collListShared;
/*     */ 
/*     */   public static BytesToNameCanonicalizer createRoot()
/*     */   {
/* 167 */     return new BytesToNameCanonicalizer(64, true);
/*     */   }
/*     */ 
/*     */   public synchronized BytesToNameCanonicalizer makeChild(boolean canonicalize, boolean intern)
/*     */   {
/* 177 */     return new BytesToNameCanonicalizer(this, intern);
/*     */   }
/*     */ 
/*     */   public void release()
/*     */   {
/* 189 */     if ((maybeDirty()) && (this._parent != null)) {
/* 190 */       this._parent.mergeChild(this);
/*     */ 
/* 195 */       markAsShared();
/*     */     }
/*     */   }
/*     */ 
/*     */   private BytesToNameCanonicalizer(int hashSize, boolean intern)
/*     */   {
/* 201 */     this._parent = null;
/* 202 */     this._intern = intern;
/*     */ 
/* 206 */     if (hashSize < 16) {
/* 207 */       hashSize = 16;
/*     */     }
/* 212 */     else if ((hashSize & hashSize - 1) != 0) {
/* 213 */       int curr = 16;
/* 214 */       while (curr < hashSize) {
/* 215 */         curr += curr;
/*     */       }
/* 217 */       hashSize = curr;
/*     */     }
/*     */ 
/* 220 */     initTables(hashSize);
/*     */   }
/*     */ 
/*     */   private BytesToNameCanonicalizer(BytesToNameCanonicalizer parent, boolean intern)
/*     */   {
/* 228 */     this._parent = parent;
/* 229 */     this._intern = intern;
/*     */ 
/* 232 */     this._count = parent._count;
/* 233 */     this._mainHashMask = parent._mainHashMask;
/* 234 */     this._mainHash = parent._mainHash;
/* 235 */     this._mainNames = parent._mainNames;
/* 236 */     this._collList = parent._collList;
/* 237 */     this._collCount = parent._collCount;
/* 238 */     this._collEnd = parent._collEnd;
/* 239 */     this._needRehash = false;
/*     */ 
/* 241 */     this._mainHashShared = true;
/* 242 */     this._mainNamesShared = true;
/* 243 */     this._collListShared = true;
/*     */   }
/*     */ 
/*     */   private void initTables(int hashSize)
/*     */   {
/* 248 */     this._count = 0;
/* 249 */     this._mainHash = new int[hashSize];
/* 250 */     this._mainNames = new Name[hashSize];
/* 251 */     this._mainHashShared = false;
/* 252 */     this._mainNamesShared = false;
/* 253 */     this._mainHashMask = (hashSize - 1);
/*     */ 
/* 255 */     this._collListShared = true;
/* 256 */     this._collList = null;
/* 257 */     this._collEnd = 0;
/*     */ 
/* 259 */     this._needRehash = false;
/*     */   }
/*     */ 
/*     */   private synchronized void mergeChild(BytesToNameCanonicalizer child)
/*     */   {
/* 265 */     int childCount = child._count;
/* 266 */     if (childCount <= this._count) {
/* 267 */       return;
/*     */     }
/*     */ 
/* 276 */     if (child.size() > 6000)
/*     */     {
/* 282 */       initTables(64);
/*     */     } else {
/* 284 */       this._count = child._count;
/* 285 */       this._mainHash = child._mainHash;
/* 286 */       this._mainNames = child._mainNames;
/* 287 */       this._mainHashShared = true;
/* 288 */       this._mainNamesShared = true;
/* 289 */       this._mainHashMask = child._mainHashMask;
/* 290 */       this._collList = child._collList;
/* 291 */       this._collCount = child._collCount;
/* 292 */       this._collEnd = child._collEnd;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void markAsShared()
/*     */   {
/* 298 */     this._mainHashShared = true;
/* 299 */     this._mainNamesShared = true;
/* 300 */     this._collListShared = true;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 309 */     return this._count;
/*     */   }
/*     */ 
/*     */   public boolean maybeDirty()
/*     */   {
/* 318 */     return !this._mainHashShared;
/*     */   }
/*     */ 
/*     */   public static Name getEmptyName()
/*     */   {
/* 323 */     return Name1.getEmptyName();
/*     */   }
/*     */ 
/*     */   public Name findName(int firstQuad)
/*     */   {
/* 343 */     int hash = calcHash(firstQuad);
/* 344 */     int ix = hash & this._mainHashMask;
/* 345 */     int val = this._mainHash[ix];
/*     */ 
/* 350 */     if ((val >> 8 ^ hash) << 8 == 0)
/*     */     {
/* 352 */       Name name = this._mainNames[ix];
/* 353 */       if (name == null) {
/* 354 */         return null;
/*     */       }
/* 356 */       if (name.equals(firstQuad))
/* 357 */         return name;
/*     */     }
/* 359 */     else if (val == 0) {
/* 360 */       return null;
/*     */     }
/*     */ 
/* 363 */     val &= 255;
/* 364 */     if (val > 0) {
/* 365 */       val--;
/* 366 */       Bucket bucket = this._collList[val];
/* 367 */       if (bucket != null) {
/* 368 */         return bucket.find(hash, firstQuad, 0);
/*     */       }
/*     */     }
/*     */ 
/* 372 */     return null;
/*     */   }
/*     */ 
/*     */   public Name findName(int firstQuad, int secondQuad)
/*     */   {
/* 393 */     int hash = calcHash(firstQuad, secondQuad);
/* 394 */     int ix = hash & this._mainHashMask;
/* 395 */     int val = this._mainHash[ix];
/*     */ 
/* 400 */     if ((val >> 8 ^ hash) << 8 == 0)
/*     */     {
/* 402 */       Name name = this._mainNames[ix];
/* 403 */       if (name == null) {
/* 404 */         return null;
/*     */       }
/* 406 */       if (name.equals(firstQuad, secondQuad))
/* 407 */         return name;
/*     */     }
/* 409 */     else if (val == 0) {
/* 410 */       return null;
/*     */     }
/*     */ 
/* 413 */     val &= 255;
/* 414 */     if (val > 0) {
/* 415 */       val--;
/* 416 */       Bucket bucket = this._collList[val];
/* 417 */       if (bucket != null) {
/* 418 */         return bucket.find(hash, firstQuad, secondQuad);
/*     */       }
/*     */     }
/*     */ 
/* 422 */     return null;
/*     */   }
/*     */ 
/*     */   public Name findName(int[] quads, int qlen)
/*     */   {
/* 450 */     int hash = calcHash(quads, qlen);
/*     */ 
/* 452 */     int ix = hash & this._mainHashMask;
/* 453 */     int val = this._mainHash[ix];
/* 454 */     if ((val >> 8 ^ hash) << 8 == 0) {
/* 455 */       Name name = this._mainNames[ix];
/* 456 */       if ((name == null) || (name.equals(quads, qlen)))
/*     */       {
/* 458 */         return name;
/*     */       }
/* 460 */     } else if (val == 0) {
/* 461 */       return null;
/*     */     }
/* 463 */     val &= 255;
/* 464 */     if (val > 0) {
/* 465 */       val--;
/* 466 */       Bucket bucket = this._collList[val];
/* 467 */       if (bucket != null) {
/* 468 */         return bucket.find(hash, quads, qlen);
/*     */       }
/*     */     }
/* 471 */     return null;
/*     */   }
/*     */ 
/*     */   public Name addName(String symbolStr, int q1, int q2)
/*     */   {
/* 485 */     if (this._intern) {
/* 486 */       symbolStr = InternCache.instance.intern(symbolStr);
/*     */     }
/* 488 */     int hash = q2 == 0 ? calcHash(q1) : calcHash(q1, q2);
/* 489 */     Name symbol = constructName(hash, symbolStr, q1, q2);
/* 490 */     _addSymbol(hash, symbol);
/* 491 */     return symbol;
/*     */   }
/*     */ 
/*     */   public Name addName(String symbolStr, int[] quads, int qlen)
/*     */   {
/* 496 */     if (this._intern) {
/* 497 */       symbolStr = InternCache.instance.intern(symbolStr);
/*     */     }
/* 499 */     int hash = calcHash(quads, qlen);
/* 500 */     Name symbol = constructName(hash, symbolStr, quads, qlen);
/* 501 */     _addSymbol(hash, symbol);
/* 502 */     return symbol;
/*     */   }
/*     */ 
/*     */   public static final int calcHash(int firstQuad)
/*     */   {
/* 513 */     int hash = firstQuad;
/* 514 */     hash ^= hash >>> 16;
/* 515 */     hash ^= hash >>> 8;
/* 516 */     return hash;
/*     */   }
/*     */ 
/*     */   public static final int calcHash(int firstQuad, int secondQuad)
/*     */   {
/* 521 */     int hash = firstQuad * 31 + secondQuad;
/*     */ 
/* 526 */     hash ^= hash >>> 16;
/* 527 */     hash ^= hash >>> 8;
/* 528 */     return hash;
/*     */   }
/*     */ 
/*     */   public static final int calcHash(int[] quads, int qlen)
/*     */   {
/* 534 */     int hash = quads[0];
/* 535 */     for (int i = 1; i < qlen; i++) {
/* 536 */       hash = hash * 31 + quads[i];
/*     */     }
/*     */ 
/* 539 */     hash ^= hash >>> 16;
/* 540 */     hash ^= hash >>> 8;
/*     */ 
/* 542 */     return hash;
/*     */   }
/*     */ 
/*     */   private void _addSymbol(int hash, Name symbol)
/*     */   {
/* 624 */     if (this._mainHashShared) {
/* 625 */       unshareMain();
/*     */     }
/*     */ 
/* 628 */     if (this._needRehash) {
/* 629 */       rehash();
/*     */     }
/*     */ 
/* 632 */     this._count += 1;
/*     */ 
/* 637 */     int ix = hash & this._mainHashMask;
/* 638 */     if (this._mainNames[ix] == null) {
/* 639 */       this._mainHash[ix] = (hash << 8);
/* 640 */       if (this._mainNamesShared) {
/* 641 */         unshareNames();
/*     */       }
/* 643 */       this._mainNames[ix] = symbol;
/*     */     }
/*     */     else
/*     */     {
/* 648 */       if (this._collListShared) {
/* 649 */         unshareCollision();
/*     */       }
/*     */ 
/* 652 */       this._collCount += 1;
/* 653 */       int entryValue = this._mainHash[ix];
/* 654 */       int bucket = entryValue & 0xFF;
/* 655 */       if (bucket == 0) {
/* 656 */         if (this._collEnd <= 254) {
/* 657 */           bucket = this._collEnd;
/* 658 */           this._collEnd += 1;
/*     */ 
/* 660 */           if (bucket >= this._collList.length)
/* 661 */             expandCollision();
/*     */         }
/*     */         else {
/* 664 */           bucket = findBestBucket();
/*     */         }
/*     */ 
/* 667 */         this._mainHash[ix] = (entryValue & 0xFFFFFF00 | bucket + 1);
/*     */       } else {
/* 669 */         bucket--;
/*     */       }
/*     */ 
/* 673 */       this._collList[bucket] = new Bucket(symbol, this._collList[bucket]);
/*     */     }
/*     */ 
/* 680 */     int hashSize = this._mainHash.length;
/* 681 */     if (this._count > hashSize >> 1) {
/* 682 */       int hashQuarter = hashSize >> 2;
/*     */ 
/* 686 */       if (this._count > hashSize - hashQuarter)
/* 687 */         this._needRehash = true;
/* 688 */       else if (this._collCount >= hashQuarter)
/* 689 */         this._needRehash = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void rehash()
/*     */   {
/* 697 */     this._needRehash = false;
/*     */ 
/* 699 */     this._mainNamesShared = false;
/*     */ 
/* 705 */     int[] oldMainHash = this._mainHash;
/* 706 */     int len = oldMainHash.length;
/* 707 */     int newLen = len + len;
/*     */ 
/* 712 */     if (newLen > 65536) {
/* 713 */       nukeSymbols();
/* 714 */       return;
/*     */     }
/*     */ 
/* 717 */     this._mainHash = new int[newLen];
/* 718 */     this._mainHashMask = (newLen - 1);
/* 719 */     Name[] oldNames = this._mainNames;
/* 720 */     this._mainNames = new Name[newLen];
/* 721 */     int symbolsSeen = 0;
/* 722 */     for (int i = 0; i < len; i++) {
/* 723 */       Name symbol = oldNames[i];
/* 724 */       if (symbol != null) {
/* 725 */         symbolsSeen++;
/* 726 */         int hash = symbol.hashCode();
/* 727 */         int ix = hash & this._mainHashMask;
/* 728 */         this._mainNames[ix] = symbol;
/* 729 */         this._mainHash[ix] = (hash << 8);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 737 */     int oldEnd = this._collEnd;
/* 738 */     if (oldEnd == 0) {
/* 739 */       return;
/*     */     }
/*     */ 
/* 742 */     this._collCount = 0;
/* 743 */     this._collEnd = 0;
/* 744 */     this._collListShared = false;
/*     */ 
/* 746 */     Bucket[] oldBuckets = this._collList;
/* 747 */     this._collList = new Bucket[oldBuckets.length];
/* 748 */     for (int i = 0; i < oldEnd; i++) {
/* 749 */       for (Bucket curr = oldBuckets[i]; curr != null; curr = curr._next) {
/* 750 */         symbolsSeen++;
/* 751 */         Name symbol = curr._name;
/* 752 */         int hash = symbol.hashCode();
/* 753 */         int ix = hash & this._mainHashMask;
/* 754 */         int val = this._mainHash[ix];
/* 755 */         if (this._mainNames[ix] == null) {
/* 756 */           this._mainHash[ix] = (hash << 8);
/* 757 */           this._mainNames[ix] = symbol;
/*     */         } else {
/* 759 */           this._collCount += 1;
/* 760 */           int bucket = val & 0xFF;
/* 761 */           if (bucket == 0) {
/* 762 */             if (this._collEnd <= 254) {
/* 763 */               bucket = this._collEnd;
/* 764 */               this._collEnd += 1;
/*     */ 
/* 766 */               if (bucket >= this._collList.length)
/* 767 */                 expandCollision();
/*     */             }
/*     */             else {
/* 770 */               bucket = findBestBucket();
/*     */             }
/*     */ 
/* 773 */             this._mainHash[ix] = (val & 0xFFFFFF00 | bucket + 1);
/*     */           } else {
/* 775 */             bucket--;
/*     */           }
/*     */ 
/* 778 */           this._collList[bucket] = new Bucket(symbol, this._collList[bucket]);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 783 */     if (symbolsSeen != this._count)
/* 784 */       throw new RuntimeException("Internal error: count after rehash " + symbolsSeen + "; should be " + this._count);
/*     */   }
/*     */ 
/*     */   private void nukeSymbols()
/*     */   {
/* 794 */     this._count = 0;
/* 795 */     Arrays.fill(this._mainHash, 0);
/* 796 */     Arrays.fill(this._mainNames, null);
/* 797 */     Arrays.fill(this._collList, null);
/* 798 */     this._collCount = 0;
/* 799 */     this._collEnd = 0;
/*     */   }
/*     */ 
/*     */   private int findBestBucket()
/*     */   {
/* 809 */     Bucket[] buckets = this._collList;
/* 810 */     int bestCount = 2147483647;
/* 811 */     int bestIx = -1;
/*     */ 
/* 813 */     int i = 0; for (int len = this._collEnd; i < len; i++) {
/* 814 */       int count = buckets[i].length();
/* 815 */       if (count < bestCount) {
/* 816 */         if (count == 1) {
/* 817 */           return i;
/*     */         }
/* 819 */         bestCount = count;
/* 820 */         bestIx = i;
/*     */       }
/*     */     }
/* 823 */     return bestIx;
/*     */   }
/*     */ 
/*     */   private void unshareMain()
/*     */   {
/* 834 */     int[] old = this._mainHash;
/* 835 */     int len = this._mainHash.length;
/*     */ 
/* 837 */     this._mainHash = new int[len];
/* 838 */     System.arraycopy(old, 0, this._mainHash, 0, len);
/* 839 */     this._mainHashShared = false;
/*     */   }
/*     */ 
/*     */   private void unshareCollision()
/*     */   {
/* 844 */     Bucket[] old = this._collList;
/* 845 */     if (old == null) {
/* 846 */       this._collList = new Bucket[32];
/*     */     } else {
/* 848 */       int len = old.length;
/* 849 */       this._collList = new Bucket[len];
/* 850 */       System.arraycopy(old, 0, this._collList, 0, len);
/*     */     }
/* 852 */     this._collListShared = false;
/*     */   }
/*     */ 
/*     */   private void unshareNames()
/*     */   {
/* 857 */     Name[] old = this._mainNames;
/* 858 */     int len = old.length;
/* 859 */     this._mainNames = new Name[len];
/* 860 */     System.arraycopy(old, 0, this._mainNames, 0, len);
/* 861 */     this._mainNamesShared = false;
/*     */   }
/*     */ 
/*     */   private void expandCollision()
/*     */   {
/* 866 */     Bucket[] old = this._collList;
/* 867 */     int len = old.length;
/* 868 */     this._collList = new Bucket[len + len];
/* 869 */     System.arraycopy(old, 0, this._collList, 0, len);
/*     */   }
/*     */ 
/*     */   private static Name constructName(int hash, String name, int q1, int q2)
/*     */   {
/* 881 */     if (q2 == 0) {
/* 882 */       return new Name1(name, hash, q1);
/*     */     }
/* 884 */     return new Name2(name, hash, q1, q2);
/*     */   }
/*     */ 
/*     */   private static Name constructName(int hash, String name, int[] quads, int qlen)
/*     */   {
/* 889 */     if (qlen < 4) {
/* 890 */       switch (qlen) {
/*     */       case 1:
/* 892 */         return new Name1(name, hash, quads[0]);
/*     */       case 2:
/* 894 */         return new Name2(name, hash, quads[0], quads[1]);
/*     */       case 3:
/* 896 */         return new Name3(name, hash, quads[0], quads[1], quads[2]);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 901 */     int[] buf = new int[qlen];
/* 902 */     for (int i = 0; i < qlen; i++) {
/* 903 */       buf[i] = quads[i];
/*     */     }
/* 905 */     return new NameN(name, hash, buf, qlen);
/*     */   }
/*     */ 
/*     */   static final class Bucket
/*     */   {
/*     */     protected final Name _name;
/*     */     protected final Bucket _next;
/*     */ 
/*     */     Bucket(Name name, Bucket next)
/*     */     {
/* 921 */       this._name = name;
/* 922 */       this._next = next;
/*     */     }
/*     */ 
/*     */     public int length()
/*     */     {
/* 927 */       int len = 1;
/* 928 */       for (Bucket curr = this._next; curr != null; curr = curr._next) {
/* 929 */         len++;
/*     */       }
/* 931 */       return len;
/*     */     }
/*     */ 
/*     */     public Name find(int hash, int firstQuad, int secondQuad)
/*     */     {
/* 936 */       if ((this._name.hashCode() == hash) && 
/* 937 */         (this._name.equals(firstQuad, secondQuad))) {
/* 938 */         return this._name;
/*     */       }
/*     */ 
/* 941 */       for (Bucket curr = this._next; curr != null; curr = curr._next) {
/* 942 */         Name currName = curr._name;
/* 943 */         if ((currName.hashCode() == hash) && 
/* 944 */           (currName.equals(firstQuad, secondQuad))) {
/* 945 */           return currName;
/*     */         }
/*     */       }
/*     */ 
/* 949 */       return null;
/*     */     }
/*     */ 
/*     */     public Name find(int hash, int[] quads, int qlen)
/*     */     {
/* 954 */       if ((this._name.hashCode() == hash) && 
/* 955 */         (this._name.equals(quads, qlen))) {
/* 956 */         return this._name;
/*     */       }
/*     */ 
/* 959 */       for (Bucket curr = this._next; curr != null; curr = curr._next) {
/* 960 */         Name currName = curr._name;
/* 961 */         if ((currName.hashCode() == hash) && 
/* 962 */           (currName.equals(quads, qlen))) {
/* 963 */           return currName;
/*     */         }
/*     */       }
/*     */ 
/* 967 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.sym.BytesToNameCanonicalizer
 * JD-Core Version:    0.6.0
 */