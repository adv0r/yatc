/*     */ package org.codehaus.jackson.io;
/*     */ 
/*     */ public final class NumberInput
/*     */ {
/*     */   static final long L_BILLION = 1000000000L;
/*  10 */   static final String MIN_LONG_STR_NO_SIGN = String.valueOf(-9223372036854775808L).substring(1);
/*  11 */   static final String MAX_LONG_STR = String.valueOf(9223372036854775807L);
/*     */ 
/*     */   public static final int parseInt(char[] digitChars, int offset, int len)
/*     */   {
/*  22 */     int num = digitChars[offset] - '0';
/*  23 */     len += offset;
/*     */ 
/*  25 */     offset++; if (offset < len) {
/*  26 */       num = num * 10 + (digitChars[offset] - '0');
/*  27 */       offset++; if (offset < len) {
/*  28 */         num = num * 10 + (digitChars[offset] - '0');
/*  29 */         offset++; if (offset < len) {
/*  30 */           num = num * 10 + (digitChars[offset] - '0');
/*  31 */           offset++; if (offset < len) {
/*  32 */             num = num * 10 + (digitChars[offset] - '0');
/*  33 */             offset++; if (offset < len) {
/*  34 */               num = num * 10 + (digitChars[offset] - '0');
/*  35 */               offset++; if (offset < len) {
/*  36 */                 num = num * 10 + (digitChars[offset] - '0');
/*  37 */                 offset++; if (offset < len) {
/*  38 */                   num = num * 10 + (digitChars[offset] - '0');
/*  39 */                   offset++; if (offset < len) {
/*  40 */                     num = num * 10 + (digitChars[offset] - '0');
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  49 */     return num;
/*     */   }
/*     */ 
/*     */   public static final int parseInt(String str)
/*     */   {
/*  64 */     char c = str.charAt(0);
/*  65 */     int length = str.length();
/*  66 */     boolean negative = c == '-';
/*  67 */     int offset = 1;
/*     */ 
/*  70 */     if (negative) {
/*  71 */       if ((length == 1) || (length > 10)) {
/*  72 */         return Integer.parseInt(str);
/*     */       }
/*  74 */       c = str.charAt(offset++);
/*     */     }
/*  76 */     else if (length > 9) {
/*  77 */       return Integer.parseInt(str);
/*     */     }
/*     */ 
/*  80 */     if ((c > '9') || (c < '0')) {
/*  81 */       return Integer.parseInt(str);
/*     */     }
/*  83 */     int num = c - '0';
/*  84 */     if (offset < length) {
/*  85 */       c = str.charAt(offset++);
/*  86 */       if ((c > '9') || (c < '0')) {
/*  87 */         return Integer.parseInt(str);
/*     */       }
/*  89 */       num = num * 10 + (c - '0');
/*  90 */       if (offset < length) {
/*  91 */         c = str.charAt(offset++);
/*  92 */         if ((c > '9') || (c < '0')) {
/*  93 */           return Integer.parseInt(str);
/*     */         }
/*  95 */         num = num * 10 + (c - '0');
/*     */ 
/*  97 */         if (offset < length) {
/*     */           do {
/*  99 */             c = str.charAt(offset++);
/* 100 */             if ((c > '9') || (c < '0')) {
/* 101 */               return Integer.parseInt(str);
/*     */             }
/* 103 */             num = num * 10 + (c - '0');
/* 104 */           }while (offset < length);
/*     */         }
/*     */       }
/*     */     }
/* 108 */     return negative ? -num : num;
/*     */   }
/*     */ 
/*     */   public static final long parseLong(char[] digitChars, int offset, int len)
/*     */   {
/* 114 */     int len1 = len - 9;
/* 115 */     long val = parseInt(digitChars, offset, len1) * 1000000000L;
/* 116 */     return val + parseInt(digitChars, offset + len1, 9);
/*     */   }
/*     */ 
/*     */   public static final long parseLong(String str)
/*     */   {
/* 124 */     int length = str.length();
/* 125 */     if (length <= 9) {
/* 126 */       return parseInt(str);
/*     */     }
/*     */ 
/* 129 */     return Long.parseLong(str);
/*     */   }
/*     */ 
/*     */   public static final boolean inLongRange(char[] digitChars, int offset, int len, boolean negative)
/*     */   {
/* 144 */     String cmpStr = negative ? MIN_LONG_STR_NO_SIGN : MAX_LONG_STR;
/* 145 */     int cmpLen = cmpStr.length();
/* 146 */     if (len < cmpLen) return true;
/* 147 */     if (len > cmpLen) return false;
/*     */ 
/* 149 */     for (int i = 0; i < cmpLen; i++) {
/* 150 */       int diff = digitChars[(offset + i)] - cmpStr.charAt(i);
/* 151 */       if (diff != 0) {
/* 152 */         return diff < 0;
/*     */       }
/*     */     }
/* 155 */     return true;
/*     */   }
/*     */ 
/*     */   public static final boolean inLongRange(String numberStr, boolean negative)
/*     */   {
/* 169 */     String cmpStr = negative ? MIN_LONG_STR_NO_SIGN : MAX_LONG_STR;
/* 170 */     int cmpLen = cmpStr.length();
/* 171 */     int actualLen = numberStr.length();
/* 172 */     if (actualLen < cmpLen) return true;
/* 173 */     if (actualLen > cmpLen) return false;
/*     */ 
/* 176 */     for (int i = 0; i < cmpLen; i++) {
/* 177 */       int diff = numberStr.charAt(i) - cmpStr.charAt(i);
/* 178 */       if (diff != 0) {
/* 179 */         return diff < 0;
/*     */       }
/*     */     }
/* 182 */     return true;
/*     */   }
/*     */ 
/*     */   public static int parseAsInt(String input, int defaultValue)
/*     */   {
/* 190 */     if (input == null) {
/* 191 */       return defaultValue;
/*     */     }
/* 193 */     input = input.trim();
/* 194 */     int len = input.length();
/* 195 */     if (len == 0) {
/* 196 */       return defaultValue;
/*     */     }
/*     */ 
/* 199 */     int i = 0;
/* 200 */     if (i < len) {
/* 201 */       char c = input.charAt(0);
/* 202 */       if (c == '+') {
/* 203 */         input = input.substring(1);
/* 204 */         len = input.length();
/* 205 */       } else if (c == '-') {
/* 206 */         i++;
/*     */       }
/*     */     }
/* 209 */     for (; i < len; i++) {
/* 210 */       char c = input.charAt(i);
/*     */ 
/* 212 */       if ((c <= '9') && (c >= '0')) continue;
/*     */       try {
/* 214 */         double d = Double.parseDouble(input);
/* 215 */         return (int)d;
/*     */       } catch (NumberFormatException e) {
/*     */       }
/*     */     }
/*     */     try {
/* 220 */       return Integer.parseInt(input); } catch (NumberFormatException e) {
/*     */     }
/* 222 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   public static long parseAsLong(String input, long defaultValue)
/*     */   {
/* 230 */     if (input == null) {
/* 231 */       return defaultValue;
/*     */     }
/* 233 */     input = input.trim();
/* 234 */     int len = input.length();
/* 235 */     if (len == 0) {
/* 236 */       return defaultValue;
/*     */     }
/*     */ 
/* 239 */     int i = 0;
/* 240 */     if (i < len) {
/* 241 */       char c = input.charAt(0);
/* 242 */       if (c == '+') {
/* 243 */         input = input.substring(1);
/* 244 */         len = input.length();
/* 245 */       } else if (c == '-') {
/* 246 */         i++;
/*     */       }
/*     */     }
/* 249 */     for (; i < len; i++) {
/* 250 */       char c = input.charAt(i);
/*     */ 
/* 252 */       if ((c <= '9') && (c >= '0')) continue;
/*     */       try {
/* 254 */         double d = Double.parseDouble(input);
/* 255 */         return ()d;
/*     */       } catch (NumberFormatException e) {
/*     */       }
/*     */     }
/*     */     try {
/* 260 */       return Long.parseLong(input); } catch (NumberFormatException e) {
/*     */     }
/* 262 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   public static double parseAsDouble(String input, double defaultValue)
/*     */   {
/* 270 */     if (input == null) {
/* 271 */       return defaultValue;
/*     */     }
/* 273 */     input = input.trim();
/* 274 */     int len = input.length();
/* 275 */     if (len == 0)
/* 276 */       return defaultValue;
/*     */     try
/*     */     {
/* 279 */       return Double.parseDouble(input); } catch (NumberFormatException e) {
/*     */     }
/* 281 */     return defaultValue;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.io.NumberInput
 * JD-Core Version:    0.6.0
 */