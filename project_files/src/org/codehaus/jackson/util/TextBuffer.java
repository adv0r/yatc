/*     */ package org.codehaus.jackson.util;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ public final class TextBuffer
/*     */ {
/*  26 */   static final char[] NO_CHARS = new char[0];
/*     */   static final int MIN_SEGMENT_LEN = 1000;
/*     */   static final int MAX_SEGMENT_LEN = 262144;
/*     */   private final BufferRecycler _allocator;
/*     */   private char[] _inputBuffer;
/*     */   private int _inputStart;
/*     */   private int _inputLen;
/*     */   private ArrayList<char[]> _segments;
/*  82 */   private boolean _hasSegments = false;
/*     */   private int _segmentSize;
/*     */   private char[] _currentSegment;
/*     */   private int _currentSize;
/*     */   private String _resultString;
/*     */   private char[] _resultArray;
/*     */ 
/*     */   public TextBuffer(BufferRecycler allocator)
/*     */   {
/* 120 */     this._allocator = allocator;
/*     */   }
/*     */ 
/*     */   public void releaseBuffers()
/*     */   {
/* 134 */     if (this._allocator == null) {
/* 135 */       resetWithEmpty();
/*     */     }
/* 137 */     else if (this._currentSegment != null)
/*     */     {
/* 139 */       resetWithEmpty();
/*     */ 
/* 141 */       char[] buf = this._currentSegment;
/* 142 */       this._currentSegment = null;
/* 143 */       this._allocator.releaseCharBuffer(BufferRecycler.CharBufferType.TEXT_BUFFER, buf);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void resetWithEmpty()
/*     */   {
/* 154 */     this._inputStart = -1;
/* 155 */     this._currentSize = 0;
/* 156 */     this._inputLen = 0;
/*     */ 
/* 158 */     this._inputBuffer = null;
/* 159 */     this._resultString = null;
/* 160 */     this._resultArray = null;
/*     */ 
/* 163 */     if (this._hasSegments)
/* 164 */       clearSegments();
/*     */   }
/*     */ 
/*     */   public void resetWithShared(char[] buf, int start, int len)
/*     */   {
/* 177 */     this._resultString = null;
/* 178 */     this._resultArray = null;
/*     */ 
/* 181 */     this._inputBuffer = buf;
/* 182 */     this._inputStart = start;
/* 183 */     this._inputLen = len;
/*     */ 
/* 186 */     if (this._hasSegments)
/* 187 */       clearSegments();
/*     */   }
/*     */ 
/*     */   public void resetWithCopy(char[] buf, int start, int len)
/*     */   {
/* 193 */     this._inputBuffer = null;
/* 194 */     this._inputStart = -1;
/* 195 */     this._inputLen = 0;
/*     */ 
/* 197 */     this._resultString = null;
/* 198 */     this._resultArray = null;
/*     */ 
/* 201 */     if (this._hasSegments)
/* 202 */       clearSegments();
/* 203 */     else if (this._currentSegment == null) {
/* 204 */       this._currentSegment = findBuffer(len);
/*     */     }
/* 206 */     this._currentSize = (this._segmentSize = 0);
/* 207 */     append(buf, start, len);
/*     */   }
/*     */ 
/*     */   public void resetWithString(String value)
/*     */   {
/* 212 */     this._inputBuffer = null;
/* 213 */     this._inputStart = -1;
/* 214 */     this._inputLen = 0;
/*     */ 
/* 216 */     this._resultString = value;
/* 217 */     this._resultArray = null;
/*     */ 
/* 219 */     if (this._hasSegments) {
/* 220 */       clearSegments();
/*     */     }
/* 222 */     this._currentSize = 0;
/*     */   }
/*     */ 
/*     */   private final char[] findBuffer(int needed)
/*     */   {
/* 232 */     if (this._allocator != null) {
/* 233 */       return this._allocator.allocCharBuffer(BufferRecycler.CharBufferType.TEXT_BUFFER, needed);
/*     */     }
/* 235 */     return new char[Math.max(needed, 1000)];
/*     */   }
/*     */ 
/*     */   private final void clearSegments()
/*     */   {
/* 240 */     this._hasSegments = false;
/*     */ 
/* 248 */     this._segments.clear();
/* 249 */     this._currentSize = (this._segmentSize = 0);
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 262 */     if (this._inputStart >= 0) {
/* 263 */       return this._inputLen;
/*     */     }
/*     */ 
/* 266 */     return this._segmentSize + this._currentSize;
/*     */   }
/*     */ 
/*     */   public int getTextOffset()
/*     */   {
/* 275 */     return this._inputStart >= 0 ? this._inputStart : 0;
/*     */   }
/*     */ 
/*     */   public char[] getTextBuffer()
/*     */   {
/* 281 */     if (this._inputStart >= 0) {
/* 282 */       return this._inputBuffer;
/*     */     }
/*     */ 
/* 285 */     if (!this._hasSegments) {
/* 286 */       return this._currentSegment;
/*     */     }
/*     */ 
/* 289 */     return contentsAsArray();
/*     */   }
/*     */ 
/*     */   public String contentsAsString()
/*     */   {
/* 300 */     if (this._resultString == null)
/*     */     {
/* 302 */       if (this._resultArray != null) {
/* 303 */         this._resultString = new String(this._resultArray);
/*     */       }
/* 306 */       else if (this._inputStart >= 0) {
/* 307 */         if (this._inputLen < 1) {
/* 308 */           return this._resultString = "";
/*     */         }
/* 310 */         this._resultString = new String(this._inputBuffer, this._inputStart, this._inputLen);
/*     */       }
/*     */       else {
/* 313 */         int segLen = this._segmentSize;
/* 314 */         int currLen = this._currentSize;
/*     */ 
/* 316 */         if (segLen == 0) {
/* 317 */           this._resultString = (currLen == 0 ? "" : new String(this._currentSegment, 0, currLen));
/*     */         } else {
/* 319 */           StringBuilder sb = new StringBuilder(segLen + currLen);
/*     */ 
/* 321 */           if (this._segments != null) {
/* 322 */             int i = 0; for (int len = this._segments.size(); i < len; i++) {
/* 323 */               char[] curr = (char[])this._segments.get(i);
/* 324 */               sb.append(curr, 0, curr.length);
/*     */             }
/*     */           }
/*     */ 
/* 328 */           sb.append(this._currentSegment, 0, this._currentSize);
/* 329 */           this._resultString = sb.toString();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 334 */     return this._resultString;
/*     */   }
/*     */ 
/*     */   public char[] contentsAsArray()
/*     */   {
/* 339 */     char[] result = this._resultArray;
/* 340 */     if (result == null) {
/* 341 */       this._resultArray = (result = buildResultArray());
/*     */     }
/* 343 */     return result;
/*     */   }
/*     */ 
/*     */   public BigDecimal contentsAsDecimal()
/*     */     throws NumberFormatException
/*     */   {
/* 354 */     if (this._resultArray != null) {
/* 355 */       return new BigDecimal(this._resultArray);
/*     */     }
/*     */ 
/* 358 */     if (this._inputStart >= 0) {
/* 359 */       return new BigDecimal(this._inputBuffer, this._inputStart, this._inputLen);
/*     */     }
/*     */ 
/* 362 */     if (this._segmentSize == 0) {
/* 363 */       return new BigDecimal(this._currentSegment, 0, this._currentSize);
/*     */     }
/*     */ 
/* 366 */     return new BigDecimal(contentsAsArray());
/*     */   }
/*     */ 
/*     */   public double contentsAsDouble()
/*     */     throws NumberFormatException
/*     */   {
/* 376 */     return Double.parseDouble(contentsAsString());
/*     */   }
/*     */ 
/*     */   public void ensureNotShared()
/*     */   {
/* 390 */     if (this._inputStart >= 0)
/* 391 */       unshare(16);
/*     */   }
/*     */ 
/*     */   public void append(char c)
/*     */   {
/* 397 */     if (this._inputStart >= 0) {
/* 398 */       unshare(16);
/*     */     }
/* 400 */     this._resultString = null;
/* 401 */     this._resultArray = null;
/*     */ 
/* 403 */     char[] curr = this._currentSegment;
/* 404 */     if (this._currentSize >= curr.length) {
/* 405 */       expand(1);
/* 406 */       curr = this._currentSegment;
/*     */     }
/* 408 */     curr[(this._currentSize++)] = c;
/*     */   }
/*     */ 
/*     */   public void append(char[] c, int start, int len)
/*     */   {
/* 414 */     if (this._inputStart >= 0) {
/* 415 */       unshare(len);
/*     */     }
/* 417 */     this._resultString = null;
/* 418 */     this._resultArray = null;
/*     */ 
/* 421 */     char[] curr = this._currentSegment;
/* 422 */     int max = curr.length - this._currentSize;
/*     */ 
/* 424 */     if (max >= len) {
/* 425 */       System.arraycopy(c, start, curr, this._currentSize, len);
/* 426 */       this._currentSize += len;
/*     */     }
/*     */     else {
/* 429 */       if (max > 0) {
/* 430 */         System.arraycopy(c, start, curr, this._currentSize, max);
/* 431 */         start += max;
/* 432 */         len -= max;
/*     */       }
/*     */ 
/* 436 */       expand(len);
/* 437 */       System.arraycopy(c, start, this._currentSegment, 0, len);
/* 438 */       this._currentSize = len;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void append(String str, int offset, int len)
/*     */   {
/* 445 */     if (this._inputStart >= 0) {
/* 446 */       unshare(len);
/*     */     }
/* 448 */     this._resultString = null;
/* 449 */     this._resultArray = null;
/*     */ 
/* 452 */     char[] curr = this._currentSegment;
/* 453 */     int max = curr.length - this._currentSize;
/* 454 */     if (max >= len) {
/* 455 */       str.getChars(offset, offset + len, curr, this._currentSize);
/* 456 */       this._currentSize += len;
/*     */     }
/*     */     else {
/* 459 */       if (max > 0) {
/* 460 */         str.getChars(offset, offset + max, curr, this._currentSize);
/* 461 */         len -= max;
/* 462 */         offset += max;
/*     */       }
/*     */ 
/* 467 */       expand(len);
/* 468 */       str.getChars(offset, offset + len, this._currentSegment, 0);
/* 469 */       this._currentSize = len;
/*     */     }
/*     */   }
/*     */ 
/*     */   public char[] getCurrentSegment()
/*     */   {
/* 485 */     if (this._inputStart >= 0) {
/* 486 */       unshare(1);
/*     */     } else {
/* 488 */       char[] curr = this._currentSegment;
/* 489 */       if (curr == null)
/* 490 */         this._currentSegment = findBuffer(0);
/* 491 */       else if (this._currentSize >= curr.length)
/*     */       {
/* 493 */         expand(1);
/*     */       }
/*     */     }
/* 496 */     return this._currentSegment;
/*     */   }
/*     */ 
/*     */   public final char[] emptyAndGetCurrentSegment()
/*     */   {
/* 502 */     this._inputStart = -1;
/* 503 */     this._currentSize = 0;
/* 504 */     this._inputLen = 0;
/*     */ 
/* 506 */     this._inputBuffer = null;
/* 507 */     this._resultString = null;
/* 508 */     this._resultArray = null;
/*     */ 
/* 511 */     if (this._hasSegments) {
/* 512 */       clearSegments();
/*     */     }
/* 514 */     char[] curr = this._currentSegment;
/* 515 */     if (curr == null) {
/* 516 */       this._currentSegment = (curr = findBuffer(0));
/*     */     }
/* 518 */     return curr;
/*     */   }
/*     */ 
/*     */   public int getCurrentSegmentSize() {
/* 522 */     return this._currentSize;
/*     */   }
/*     */ 
/*     */   public void setCurrentLength(int len) {
/* 526 */     this._currentSize = len;
/*     */   }
/*     */ 
/*     */   public char[] finishCurrentSegment()
/*     */   {
/* 531 */     if (this._segments == null) {
/* 532 */       this._segments = new ArrayList();
/*     */     }
/* 534 */     this._hasSegments = true;
/* 535 */     this._segments.add(this._currentSegment);
/* 536 */     int oldLen = this._currentSegment.length;
/* 537 */     this._segmentSize += oldLen;
/*     */ 
/* 539 */     int newLen = Math.min(oldLen + (oldLen >> 1), 262144);
/* 540 */     char[] curr = _charArray(newLen);
/* 541 */     this._currentSize = 0;
/* 542 */     this._currentSegment = curr;
/* 543 */     return curr;
/*     */   }
/*     */ 
/*     */   public char[] expandCurrentSegment()
/*     */   {
/* 553 */     char[] curr = this._currentSegment;
/*     */ 
/* 555 */     int len = curr.length;
/*     */ 
/* 557 */     int newLen = len == 262144 ? 262145 : Math.min(262144, len + (len >> 1));
/*     */ 
/* 559 */     this._currentSegment = _charArray(newLen);
/* 560 */     System.arraycopy(curr, 0, this._currentSegment, 0, len);
/* 561 */     return this._currentSegment;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 577 */     return contentsAsString();
/*     */   }
/*     */ 
/*     */   private void unshare(int needExtra)
/*     */   {
/* 592 */     int sharedLen = this._inputLen;
/* 593 */     this._inputLen = 0;
/* 594 */     char[] inputBuf = this._inputBuffer;
/* 595 */     this._inputBuffer = null;
/* 596 */     int start = this._inputStart;
/* 597 */     this._inputStart = -1;
/*     */ 
/* 600 */     int needed = sharedLen + needExtra;
/* 601 */     if ((this._currentSegment == null) || (needed > this._currentSegment.length)) {
/* 602 */       this._currentSegment = findBuffer(needed);
/*     */     }
/* 604 */     if (sharedLen > 0) {
/* 605 */       System.arraycopy(inputBuf, start, this._currentSegment, 0, sharedLen);
/*     */     }
/* 607 */     this._segmentSize = 0;
/* 608 */     this._currentSize = sharedLen;
/*     */   }
/*     */ 
/*     */   private void expand(int minNewSegmentSize)
/*     */   {
/* 618 */     if (this._segments == null) {
/* 619 */       this._segments = new ArrayList();
/*     */     }
/* 621 */     char[] curr = this._currentSegment;
/* 622 */     this._hasSegments = true;
/* 623 */     this._segments.add(curr);
/* 624 */     this._segmentSize += curr.length;
/* 625 */     int oldLen = curr.length;
/*     */ 
/* 627 */     int sizeAddition = oldLen >> 1;
/* 628 */     if (sizeAddition < minNewSegmentSize) {
/* 629 */       sizeAddition = minNewSegmentSize;
/*     */     }
/* 631 */     curr = _charArray(Math.min(262144, oldLen + sizeAddition));
/* 632 */     this._currentSize = 0;
/* 633 */     this._currentSegment = curr;
/*     */   }
/*     */ 
/*     */   private char[] buildResultArray()
/*     */   {
/* 638 */     if (this._resultString != null)
/* 639 */       return this._resultString.toCharArray();
/*     */     char[] result;
/* 644 */     if (this._inputStart >= 0) {
/* 645 */       if (this._inputLen < 1) {
/* 646 */         return NO_CHARS;
/*     */       }
/* 648 */       char[] result = _charArray(this._inputLen);
/* 649 */       System.arraycopy(this._inputBuffer, this._inputStart, result, 0, this._inputLen);
/*     */     }
/*     */     else {
/* 652 */       int size = size();
/* 653 */       if (size < 1) {
/* 654 */         return NO_CHARS;
/*     */       }
/* 656 */       int offset = 0;
/* 657 */       result = _charArray(size);
/* 658 */       if (this._segments != null) {
/* 659 */         int i = 0; for (int len = this._segments.size(); i < len; i++) {
/* 660 */           char[] curr = (char[])(char[])this._segments.get(i);
/* 661 */           int currLen = curr.length;
/* 662 */           System.arraycopy(curr, 0, result, offset, currLen);
/* 663 */           offset += currLen;
/*     */         }
/*     */       }
/* 666 */       System.arraycopy(this._currentSegment, 0, result, offset, this._currentSize);
/*     */     }
/* 668 */     return result;
/*     */   }
/*     */ 
/*     */   private final char[] _charArray(int len) {
/* 672 */     return new char[len];
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.util.TextBuffer
 * JD-Core Version:    0.6.0
 */