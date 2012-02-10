/*     */ package org.codehaus.jackson.io;
/*     */ 
/*     */ public final class NumberOutput
/*     */ {
/*     */   private static final char NULL_CHAR = '\000';
/*   7 */   private static int MILLION = 1000000;
/*   8 */   private static int BILLION = 1000000000;
/*   9 */   private static long TEN_BILLION_L = 10000000000L;
/*  10 */   private static long THOUSAND_L = 1000L;
/*     */ 
/*  12 */   private static long MIN_INT_AS_LONG = -2147483648L;
/*  13 */   private static long MAX_INT_AS_LONG = 2147483647L;
/*     */ 
/*  15 */   static final String SMALLEST_LONG = String.valueOf(-9223372036854775808L);
/*     */ 
/*  17 */   static final char[] LEADING_TRIPLETS = new char[4000];
/*  18 */   static final char[] FULL_TRIPLETS = new char[4000];
/*     */   static final byte[] FULL_TRIPLETS_B;
/*     */   static final String[] sSmallIntStrs;
/*     */   static final String[] sSmallIntStrs2;
/*     */ 
/*     */   public static int outputInt(int value, char[] buffer, int offset)
/*     */   {
/*  70 */     if (value < 0) {
/*  71 */       if (value == -2147483648)
/*     */       {
/*  75 */         return outputLong(value, buffer, offset);
/*     */       }
/*  77 */       buffer[(offset++)] = '-';
/*  78 */       value = -value;
/*     */     }
/*     */ 
/*  81 */     if (value < MILLION) {
/*  82 */       if (value < 1000) {
/*  83 */         if (value < 10)
/*  84 */           buffer[(offset++)] = (char)(48 + value);
/*     */         else
/*  86 */           offset = outputLeadingTriplet(value, buffer, offset);
/*     */       }
/*     */       else {
/*  89 */         int thousands = value / 1000;
/*  90 */         value -= thousands * 1000;
/*  91 */         offset = outputLeadingTriplet(thousands, buffer, offset);
/*  92 */         offset = outputFullTriplet(value, buffer, offset);
/*     */       }
/*  94 */       return offset;
/*     */     }
/*     */ 
/* 102 */     boolean hasBillions = value >= BILLION;
/* 103 */     if (hasBillions) {
/* 104 */       value -= BILLION;
/* 105 */       if (value >= BILLION) {
/* 106 */         value -= BILLION;
/* 107 */         buffer[(offset++)] = '2';
/*     */       } else {
/* 109 */         buffer[(offset++)] = '1';
/*     */       }
/*     */     }
/* 112 */     int newValue = value / 1000;
/* 113 */     int ones = value - newValue * 1000;
/* 114 */     value = newValue;
/* 115 */     newValue /= 1000;
/* 116 */     int thousands = value - newValue * 1000;
/*     */ 
/* 119 */     if (hasBillions)
/* 120 */       offset = outputFullTriplet(newValue, buffer, offset);
/*     */     else {
/* 122 */       offset = outputLeadingTriplet(newValue, buffer, offset);
/*     */     }
/* 124 */     offset = outputFullTriplet(thousands, buffer, offset);
/* 125 */     offset = outputFullTriplet(ones, buffer, offset);
/* 126 */     return offset;
/*     */   }
/*     */ 
/*     */   public static int outputInt(int value, byte[] buffer, int offset)
/*     */   {
/* 131 */     if (value < 0) {
/* 132 */       if (value == -2147483648) {
/* 133 */         return outputLong(value, buffer, offset);
/*     */       }
/* 135 */       buffer[(offset++)] = 45;
/* 136 */       value = -value;
/*     */     }
/*     */ 
/* 139 */     if (value < MILLION) {
/* 140 */       if (value < 1000) {
/* 141 */         if (value < 10)
/* 142 */           buffer[(offset++)] = (byte)(48 + value);
/*     */         else
/* 144 */           offset = outputLeadingTriplet(value, buffer, offset);
/*     */       }
/*     */       else {
/* 147 */         int thousands = value / 1000;
/* 148 */         value -= thousands * 1000;
/* 149 */         offset = outputLeadingTriplet(thousands, buffer, offset);
/* 150 */         offset = outputFullTriplet(value, buffer, offset);
/*     */       }
/* 152 */       return offset;
/*     */     }
/* 154 */     boolean hasBillions = value >= BILLION;
/* 155 */     if (hasBillions) {
/* 156 */       value -= BILLION;
/* 157 */       if (value >= BILLION) {
/* 158 */         value -= BILLION;
/* 159 */         buffer[(offset++)] = 50;
/*     */       } else {
/* 161 */         buffer[(offset++)] = 49;
/*     */       }
/*     */     }
/* 164 */     int newValue = value / 1000;
/* 165 */     int ones = value - newValue * 1000;
/* 166 */     value = newValue;
/* 167 */     newValue /= 1000;
/* 168 */     int thousands = value - newValue * 1000;
/*     */ 
/* 170 */     if (hasBillions)
/* 171 */       offset = outputFullTriplet(newValue, buffer, offset);
/*     */     else {
/* 173 */       offset = outputLeadingTriplet(newValue, buffer, offset);
/*     */     }
/* 175 */     offset = outputFullTriplet(thousands, buffer, offset);
/* 176 */     offset = outputFullTriplet(ones, buffer, offset);
/* 177 */     return offset;
/*     */   }
/*     */ 
/*     */   public static int outputLong(long value, char[] buffer, int offset)
/*     */   {
/* 186 */     if (value < 0L)
/*     */     {
/* 190 */       if (value > MIN_INT_AS_LONG) {
/* 191 */         return outputInt((int)value, buffer, offset);
/*     */       }
/* 193 */       if (value == -9223372036854775808L)
/*     */       {
/* 195 */         int len = SMALLEST_LONG.length();
/* 196 */         SMALLEST_LONG.getChars(0, len, buffer, offset);
/* 197 */         return offset + len;
/*     */       }
/* 199 */       buffer[(offset++)] = '-';
/* 200 */       value = -value;
/*     */     }
/* 202 */     else if (value <= MAX_INT_AS_LONG) {
/* 203 */       return outputInt((int)value, buffer, offset);
/*     */     }
/*     */ 
/* 210 */     int origOffset = offset;
/* 211 */     offset += calcLongStrLength(value);
/* 212 */     int ptr = offset;
/*     */ 
/* 215 */     while (value > MAX_INT_AS_LONG) {
/* 216 */       ptr -= 3;
/* 217 */       long newValue = value / THOUSAND_L;
/* 218 */       int triplet = (int)(value - newValue * THOUSAND_L);
/* 219 */       outputFullTriplet(triplet, buffer, ptr);
/* 220 */       value = newValue;
/*     */     }
/*     */ 
/* 223 */     int ivalue = (int)value;
/* 224 */     while (ivalue >= 1000) {
/* 225 */       ptr -= 3;
/* 226 */       int newValue = ivalue / 1000;
/* 227 */       int triplet = ivalue - newValue * 1000;
/* 228 */       outputFullTriplet(triplet, buffer, ptr);
/* 229 */       ivalue = newValue;
/*     */     }
/*     */ 
/* 232 */     outputLeadingTriplet(ivalue, buffer, origOffset);
/*     */ 
/* 234 */     return offset;
/*     */   }
/*     */ 
/*     */   public static int outputLong(long value, byte[] buffer, int offset)
/*     */   {
/* 239 */     if (value < 0L) {
/* 240 */       if (value > MIN_INT_AS_LONG) {
/* 241 */         return outputInt((int)value, buffer, offset);
/*     */       }
/* 243 */       if (value == -9223372036854775808L)
/*     */       {
/* 245 */         int len = SMALLEST_LONG.length();
/* 246 */         for (int i = 0; i < len; i++) {
/* 247 */           buffer[(offset++)] = (byte)SMALLEST_LONG.charAt(i);
/*     */         }
/* 249 */         return offset;
/*     */       }
/* 251 */       buffer[(offset++)] = 45;
/* 252 */       value = -value;
/*     */     }
/* 254 */     else if (value <= MAX_INT_AS_LONG) {
/* 255 */       return outputInt((int)value, buffer, offset);
/*     */     }
/*     */ 
/* 258 */     int origOffset = offset;
/* 259 */     offset += calcLongStrLength(value);
/* 260 */     int ptr = offset;
/*     */ 
/* 263 */     while (value > MAX_INT_AS_LONG) {
/* 264 */       ptr -= 3;
/* 265 */       long newValue = value / THOUSAND_L;
/* 266 */       int triplet = (int)(value - newValue * THOUSAND_L);
/* 267 */       outputFullTriplet(triplet, buffer, ptr);
/* 268 */       value = newValue;
/*     */     }
/*     */ 
/* 271 */     int ivalue = (int)value;
/* 272 */     while (ivalue >= 1000) {
/* 273 */       ptr -= 3;
/* 274 */       int newValue = ivalue / 1000;
/* 275 */       int triplet = ivalue - newValue * 1000;
/* 276 */       outputFullTriplet(triplet, buffer, ptr);
/* 277 */       ivalue = newValue;
/*     */     }
/* 279 */     outputLeadingTriplet(ivalue, buffer, origOffset);
/* 280 */     return offset;
/*     */   }
/*     */ 
/*     */   public static String toString(int value)
/*     */   {
/* 296 */     if (value < sSmallIntStrs.length) {
/* 297 */       if (value >= 0) {
/* 298 */         return sSmallIntStrs[value];
/*     */       }
/* 300 */       int v2 = -value - 1;
/* 301 */       if (v2 < sSmallIntStrs2.length) {
/* 302 */         return sSmallIntStrs2[v2];
/*     */       }
/*     */     }
/* 305 */     return Integer.toString(value);
/*     */   }
/*     */ 
/*     */   public static String toString(long value)
/*     */   {
/* 310 */     if ((value <= 2147483647L) && (value >= -2147483648L))
/*     */     {
/* 312 */       return toString((int)value);
/*     */     }
/* 314 */     return Long.toString(value);
/*     */   }
/*     */ 
/*     */   public static String toString(double value)
/*     */   {
/* 319 */     return Double.toString(value);
/*     */   }
/*     */ 
/*     */   private static int outputLeadingTriplet(int triplet, char[] buffer, int offset)
/*     */   {
/* 330 */     int digitOffset = triplet << 2;
/* 331 */     char c = LEADING_TRIPLETS[(digitOffset++)];
/* 332 */     if (c != 0) {
/* 333 */       buffer[(offset++)] = c;
/*     */     }
/* 335 */     c = LEADING_TRIPLETS[(digitOffset++)];
/* 336 */     if (c != 0) {
/* 337 */       buffer[(offset++)] = c;
/*     */     }
/*     */ 
/* 340 */     buffer[(offset++)] = LEADING_TRIPLETS[digitOffset];
/* 341 */     return offset;
/*     */   }
/*     */ 
/*     */   private static int outputLeadingTriplet(int triplet, byte[] buffer, int offset)
/*     */   {
/* 346 */     int digitOffset = triplet << 2;
/* 347 */     char c = LEADING_TRIPLETS[(digitOffset++)];
/* 348 */     if (c != 0) {
/* 349 */       buffer[(offset++)] = (byte)c;
/*     */     }
/* 351 */     c = LEADING_TRIPLETS[(digitOffset++)];
/* 352 */     if (c != 0) {
/* 353 */       buffer[(offset++)] = (byte)c;
/*     */     }
/*     */ 
/* 356 */     buffer[(offset++)] = (byte)LEADING_TRIPLETS[digitOffset];
/* 357 */     return offset;
/*     */   }
/*     */ 
/*     */   private static int outputFullTriplet(int triplet, char[] buffer, int offset)
/*     */   {
/* 362 */     int digitOffset = triplet << 2;
/* 363 */     buffer[(offset++)] = FULL_TRIPLETS[(digitOffset++)];
/* 364 */     buffer[(offset++)] = FULL_TRIPLETS[(digitOffset++)];
/* 365 */     buffer[(offset++)] = FULL_TRIPLETS[digitOffset];
/* 366 */     return offset;
/*     */   }
/*     */ 
/*     */   private static int outputFullTriplet(int triplet, byte[] buffer, int offset)
/*     */   {
/* 371 */     int digitOffset = triplet << 2;
/* 372 */     buffer[(offset++)] = FULL_TRIPLETS_B[(digitOffset++)];
/* 373 */     buffer[(offset++)] = FULL_TRIPLETS_B[(digitOffset++)];
/* 374 */     buffer[(offset++)] = FULL_TRIPLETS_B[digitOffset];
/* 375 */     return offset;
/*     */   }
/*     */ 
/*     */   private static int calcLongStrLength(long posValue)
/*     */   {
/* 385 */     int len = 10;
/* 386 */     long comp = TEN_BILLION_L;
/*     */ 
/* 389 */     while ((posValue >= comp) && 
/* 390 */       (len != 19))
/*     */     {
/* 393 */       len++;
/* 394 */       comp = (comp << 3) + (comp << 1);
/*     */     }
/* 396 */     return len;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  23 */     int ix = 0;
/*  24 */     for (int i1 = 0; i1 < 10; i1++) {
/*  25 */       char f1 = (char)(48 + i1);
/*  26 */       char l1 = i1 == 0 ? '\000' : f1;
/*  27 */       for (int i2 = 0; i2 < 10; i2++) {
/*  28 */         char f2 = (char)(48 + i2);
/*  29 */         char l2 = (i1 == 0) && (i2 == 0) ? '\000' : f2;
/*  30 */         for (int i3 = 0; i3 < 10; i3++)
/*     */         {
/*  32 */           char f3 = (char)(48 + i3);
/*  33 */           LEADING_TRIPLETS[ix] = l1;
/*  34 */           LEADING_TRIPLETS[(ix + 1)] = l2;
/*  35 */           LEADING_TRIPLETS[(ix + 2)] = f3;
/*  36 */           FULL_TRIPLETS[ix] = f1;
/*  37 */           FULL_TRIPLETS[(ix + 1)] = f2;
/*  38 */           FULL_TRIPLETS[(ix + 2)] = f3;
/*  39 */           ix += 4;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  45 */     FULL_TRIPLETS_B = new byte[4000];
/*     */ 
/*  47 */     for (int i = 0; i < 4000; i++) {
/*  48 */       FULL_TRIPLETS_B[i] = (byte)FULL_TRIPLETS[i];
/*     */     }
/*     */ 
/*  52 */     sSmallIntStrs = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
/*     */ 
/*  55 */     sSmallIntStrs2 = new String[] { "-1", "-2", "-3", "-4", "-5", "-6", "-7", "-8", "-9", "-10" };
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.io.NumberOutput
 * JD-Core Version:    0.6.0
 */