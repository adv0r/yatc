/*     */ package org.codehaus.jackson.util;
/*     */ 
/*     */ import B;
/*     */ import C;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public final class CharTypes
/*     */ {
/*   7 */   private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
/*     */   private static final byte[] HEX_BYTES;
/*     */   static final int[] sInputCodes;
/*     */   static final int[] sInputCodesUtf8;
/*     */   static final int[] sInputCodesJsNames;
/*     */   static final int[] sInputCodesUtf8JsNames;
/*     */   static final int[] sInputCodesComment;
/*     */   static final int[] sOutputEscapes;
/*     */   static final int[] sHexValues;
/*     */ 
/*     */   public static final int[] getInputCodeLatin1()
/*     */   {
/* 170 */     return sInputCodes; } 
/* 171 */   public static final int[] getInputCodeUtf8() { return sInputCodesUtf8; } 
/*     */   public static final int[] getInputCodeLatin1JsNames() {
/* 173 */     return sInputCodesJsNames; } 
/* 174 */   public static final int[] getInputCodeUtf8JsNames() { return sInputCodesUtf8JsNames; } 
/*     */   public static final int[] getInputCodeComment() {
/* 176 */     return sInputCodesComment; } 
/* 177 */   public static final int[] getOutputEscapes() { return sOutputEscapes; }
/*     */ 
/*     */   public static int charToHex(int ch)
/*     */   {
/* 181 */     return ch > 127 ? -1 : sHexValues[ch];
/*     */   }
/*     */ 
/*     */   public static void appendQuoted(StringBuilder sb, String content)
/*     */   {
/* 186 */     int[] escCodes = sOutputEscapes;
/* 187 */     int escLen = escCodes.length;
/* 188 */     int i = 0; for (int len = content.length(); i < len; i++) {
/* 189 */       char c = content.charAt(i);
/* 190 */       if ((c >= escLen) || (escCodes[c] == 0)) {
/* 191 */         sb.append(c);
/*     */       }
/*     */       else {
/* 194 */         sb.append('\\');
/* 195 */         int escCode = escCodes[c];
/* 196 */         if (escCode < 0)
/*     */         {
/* 198 */           sb.append('u');
/* 199 */           sb.append('0');
/* 200 */           sb.append('0');
/* 201 */           int value = -(escCode + 1);
/* 202 */           sb.append(HEX_CHARS[(value >> 4)]);
/* 203 */           sb.append(HEX_CHARS[(value & 0xF)]);
/*     */         } else {
/* 205 */           sb.append((char)escCode);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static char[] copyHexChars()
/*     */   {
/* 215 */     return (char[])(char[])HEX_CHARS.clone();
/*     */   }
/*     */ 
/*     */   public static byte[] copyHexBytes()
/*     */   {
/* 223 */     return (byte[])(byte[])HEX_BYTES.clone();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  10 */     int len = HEX_CHARS.length;
/*  11 */     HEX_BYTES = new byte[len];
/*  12 */     for (int i = 0; i < len; i++) {
/*  13 */       HEX_BYTES[i] = (byte)HEX_CHARS[i];
/*     */     }
/*     */ 
/*  28 */     int[] table = new int[256];
/*     */ 
/*  30 */     for (int i = 0; i < 32; i++) {
/*  31 */       table[i] = -1;
/*     */     }
/*     */ 
/*  34 */     table[34] = 1;
/*  35 */     table[92] = 1;
/*  36 */     sInputCodes = table;
/*     */ 
/*  45 */     int[] table = new int[sInputCodes.length];
/*  46 */     System.arraycopy(sInputCodes, 0, table, 0, sInputCodes.length);
/*  47 */     for (int c = 128; c < 256; c++)
/*     */     {
/*     */       int code;
/*     */       int code;
/*  51 */       if ((c & 0xE0) == 192) {
/*  52 */         code = 2;
/*     */       }
/*     */       else
/*     */       {
/*     */         int code;
/*  53 */         if ((c & 0xF0) == 224) {
/*  54 */           code = 3;
/*     */         }
/*     */         else
/*     */         {
/*     */           int code;
/*  55 */           if ((c & 0xF8) == 240)
/*     */           {
/*  57 */             code = 4;
/*     */           }
/*     */           else
/*  60 */             code = -1; 
/*     */         }
/*     */       }
/*  62 */       table[c] = code;
/*     */     }
/*  64 */     sInputCodesUtf8 = table;
/*     */ 
/*  77 */     int[] table = new int[256];
/*     */ 
/*  79 */     Arrays.fill(table, -1);
/*     */ 
/*  81 */     for (int i = 33; i < 256; i++) {
/*  82 */       if (Character.isJavaIdentifierPart((char)i)) {
/*  83 */         table[i] = 0;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  89 */     table[64] = 0;
/*  90 */     table[35] = 0;
/*  91 */     table[42] = 0;
/*  92 */     table[45] = 0;
/*  93 */     table[43] = 0;
/*  94 */     sInputCodesJsNames = table;
/*     */ 
/* 104 */     int[] table = new int[256];
/*     */ 
/* 106 */     System.arraycopy(sInputCodesJsNames, 0, table, 0, sInputCodesJsNames.length);
/* 107 */     Arrays.fill(table, 128, 128, 0);
/* 108 */     sInputCodesUtf8JsNames = table;
/*     */ 
/* 115 */     sInputCodesComment = new int[256];
/*     */ 
/* 118 */     System.arraycopy(sInputCodesUtf8, 128, sInputCodesComment, 128, 128);
/*     */ 
/* 121 */     Arrays.fill(sInputCodesComment, 0, 32, -1);
/* 122 */     sInputCodesComment[9] = 0;
/* 123 */     sInputCodesComment[10] = 10;
/* 124 */     sInputCodesComment[13] = 13;
/* 125 */     sInputCodesComment[42] = 42;
/*     */ 
/* 134 */     int[] table = new int[256];
/*     */ 
/* 136 */     for (int i = 0; i < 32; i++) {
/* 137 */       table[i] = (-(i + 1));
/*     */     }
/*     */ 
/* 142 */     table[34] = 34;
/* 143 */     table[92] = 92;
/*     */ 
/* 145 */     table[8] = 98;
/* 146 */     table[9] = 116;
/* 147 */     table[12] = 102;
/* 148 */     table[10] = 110;
/* 149 */     table[13] = 114;
/* 150 */     sOutputEscapes = table;
/*     */ 
/* 158 */     sHexValues = new int[''];
/*     */ 
/* 160 */     Arrays.fill(sHexValues, -1);
/* 161 */     for (int i = 0; i < 10; i++) {
/* 162 */       sHexValues[(48 + i)] = i;
/*     */     }
/* 164 */     for (int i = 0; i < 6; i++) {
/* 165 */       sHexValues[(97 + i)] = (10 + i);
/* 166 */       sHexValues[(65 + i)] = (10 + i);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.util.CharTypes
 * JD-Core Version:    0.6.0
 */