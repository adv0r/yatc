/*      */ package it.unibz.twitter.utils;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FilterInputStream;
/*      */ import java.io.FilterOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.ObjectStreamClass;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.CharBuffer;
/*      */ import java.util.zip.GZIPInputStream;
/*      */ import java.util.zip.GZIPOutputStream;
/*      */ 
/*      */ public class Base64
/*      */ {
/*      */   public static final int NO_OPTIONS = 0;
/*      */   public static final int ENCODE = 1;
/*      */   public static final int DECODE = 0;
/*      */   public static final int GZIP = 2;
/*      */   public static final int DONT_GUNZIP = 4;
/*      */   public static final int DO_BREAK_LINES = 8;
/*      */   public static final int URL_SAFE = 16;
/*      */   public static final int ORDERED = 32;
/*      */   private static final int MAX_LINE_LENGTH = 76;
/*      */   private static final byte EQUALS_SIGN = 61;
/*      */   private static final byte NEW_LINE = 10;
/*      */   private static final String PREFERRED_ENCODING = "US-ASCII";
/*      */   private static final byte WHITE_SPACE_ENC = -5;
/*      */   private static final byte EQUALS_SIGN_ENC = -1;
/*      */   private static final byte[] _STANDARD_ALPHABET;
/*      */   private static final byte[] _STANDARD_DECODABET;
/*      */   private static final byte[] _URL_SAFE_ALPHABET;
/*      */   private static final byte[] _URL_SAFE_DECODABET;
/*      */   private static final byte[] _ORDERED_ALPHABET;
/*      */   private static final byte[] _ORDERED_DECODABET;
/*      */ 
/*      */   static
/*      */   {
/*  222 */     _STANDARD_ALPHABET = new byte[] { 
/*  223 */       65, 66, 67, 68, 69, 70, 71, 
/*  224 */       72, 73, 74, 75, 76, 77, 78, 
/*  225 */       79, 80, 81, 82, 83, 84, 85, 
/*  226 */       86, 87, 88, 89, 90, 
/*  227 */       97, 98, 99, 100, 101, 102, 103, 
/*  228 */       104, 105, 106, 107, 108, 109, 110, 
/*  229 */       111, 112, 113, 114, 115, 116, 117, 
/*  230 */       118, 119, 120, 121, 122, 
/*  231 */       48, 49, 50, 51, 52, 53, 
/*  232 */       54, 55, 56, 57, 43, 47 };
/*      */ 
/*  240 */     _STANDARD_DECODABET = new byte[] { 
/*  241 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  242 */       -5, -5, 
/*  243 */       -9, -9, 
/*  244 */       -5, 
/*  245 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  246 */       -9, -9, -9, -9, -9, 
/*  247 */       -5, 
/*  248 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  249 */       62, 
/*  250 */       -9, -9, -9, 
/*  251 */       63, 
/*  252 */       52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 
/*  253 */       -9, -9, -9, 
/*  254 */       -1, 
/*  255 */       -9, -9, -9, 
/*  256 */       0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 
/*  257 */       14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 
/*  258 */       -9, -9, -9, -9, -9, -9, 
/*  259 */       26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 
/*  260 */       39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 
/*  261 */       -9, -9, -9, -9, -9, 
/*  262 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  263 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  264 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  265 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  266 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  267 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  268 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  269 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  270 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  271 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9 };
/*      */ 
/*  282 */     _URL_SAFE_ALPHABET = new byte[] { 
/*  283 */       65, 66, 67, 68, 69, 70, 71, 
/*  284 */       72, 73, 74, 75, 76, 77, 78, 
/*  285 */       79, 80, 81, 82, 83, 84, 85, 
/*  286 */       86, 87, 88, 89, 90, 
/*  287 */       97, 98, 99, 100, 101, 102, 103, 
/*  288 */       104, 105, 106, 107, 108, 109, 110, 
/*  289 */       111, 112, 113, 114, 115, 116, 117, 
/*  290 */       118, 119, 120, 121, 122, 
/*  291 */       48, 49, 50, 51, 52, 53, 
/*  292 */       54, 55, 56, 57, 45, 95 };
/*      */ 
/*  298 */     _URL_SAFE_DECODABET = new byte[] { 
/*  299 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  300 */       -5, -5, 
/*  301 */       -9, -9, 
/*  302 */       -5, 
/*  303 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  304 */       -9, -9, -9, -9, -9, 
/*  305 */       -5, 
/*  306 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  307 */       -9, 
/*  308 */       -9, 
/*  309 */       62, 
/*  310 */       -9, 
/*  311 */       -9, 
/*  312 */       52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 
/*  313 */       -9, -9, -9, 
/*  314 */       -1, 
/*  315 */       -9, -9, -9, 
/*  316 */       0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 
/*  317 */       14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 
/*  318 */       -9, -9, -9, -9, 
/*  319 */       63, 
/*  320 */       -9, 
/*  321 */       26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 
/*  322 */       39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 
/*  323 */       -9, -9, -9, -9, -9, 
/*  324 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  325 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  326 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  327 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  328 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  329 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  330 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  331 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  332 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  333 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9 };
/*      */ 
/*  345 */     _ORDERED_ALPHABET = new byte[] { 
/*  346 */       45, 
/*  347 */       48, 49, 50, 51, 52, 
/*  348 */       53, 54, 55, 56, 57, 
/*  349 */       65, 66, 67, 68, 69, 70, 71, 
/*  350 */       72, 73, 74, 75, 76, 77, 78, 
/*  351 */       79, 80, 81, 82, 83, 84, 85, 
/*  352 */       86, 87, 88, 89, 90, 
/*  353 */       95, 
/*  354 */       97, 98, 99, 100, 101, 102, 103, 
/*  355 */       104, 105, 106, 107, 108, 109, 110, 
/*  356 */       111, 112, 113, 114, 115, 116, 117, 
/*  357 */       118, 119, 120, 121, 122 };
/*      */ 
/*  363 */     _ORDERED_DECODABET = new byte[] { 
/*  364 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  365 */       -5, -5, 
/*  366 */       -9, -9, 
/*  367 */       -5, 
/*  368 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  369 */       -9, -9, -9, -9, -9, 
/*  370 */       -5, 
/*  371 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  372 */       -9, 
/*  373 */       -9, 
/*  375 */       0, -9, 
/*  376 */       -9, 
/*  377 */       1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 
/*  378 */       -9, -9, -9, 
/*  379 */       -1, 
/*  380 */       -9, -9, -9, 
/*  381 */       11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 
/*  382 */       24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 
/*  383 */       -9, -9, -9, -9, 
/*  384 */       37, 
/*  385 */       -9, 
/*  386 */       38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 
/*  387 */       51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 
/*  388 */       -9, -9, -9, -9, -9, 
/*  389 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  390 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  391 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  392 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  393 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  394 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  395 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  396 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  397 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 
/*  398 */       -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9 };
/*      */   }
/*      */ 
/*      */   private static final byte[] getAlphabet(int options)
/*      */   {
/*  413 */     if ((options & 0x10) == 16)
/*  414 */       return _URL_SAFE_ALPHABET;
/*  415 */     if ((options & 0x20) == 32) {
/*  416 */       return _ORDERED_ALPHABET;
/*      */     }
/*  418 */     return _STANDARD_ALPHABET;
/*      */   }
/*      */ 
/*      */   private static final byte[] getDecodabet(int options)
/*      */   {
/*  431 */     if ((options & 0x10) == 16)
/*  432 */       return _URL_SAFE_DECODABET;
/*  433 */     if ((options & 0x20) == 32) {
/*  434 */       return _ORDERED_DECODABET;
/*      */     }
/*  436 */     return _STANDARD_DECODABET;
/*      */   }
/*      */ 
/*      */   private static byte[] encode3to4(byte[] b4, byte[] threeBytes, int numSigBytes, int options)
/*      */   {
/*  467 */     encode3to4(threeBytes, 0, numSigBytes, b4, 0, options);
/*  468 */     return b4;
/*      */   }
/*      */ 
/*      */   private static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset, int options)
/*      */   {
/*  499 */     byte[] ALPHABET = getAlphabet(options);
/*      */ 
/*  512 */     int inBuff = (numSigBytes > 0 ? source[srcOffset] << 24 >>> 8 : 0) | (
/*  513 */       numSigBytes > 1 ? source[(srcOffset + 1)] << 24 >>> 16 : 0) | (
/*  514 */       numSigBytes > 2 ? source[(srcOffset + 2)] << 24 >>> 24 : 0);
/*      */ 
/*  516 */     switch (numSigBytes)
/*      */     {
/*      */     case 3:
/*  519 */       destination[destOffset] = ALPHABET[(inBuff >>> 18)];
/*  520 */       destination[(destOffset + 1)] = ALPHABET[(inBuff >>> 12 & 0x3F)];
/*  521 */       destination[(destOffset + 2)] = ALPHABET[(inBuff >>> 6 & 0x3F)];
/*  522 */       destination[(destOffset + 3)] = ALPHABET[(inBuff & 0x3F)];
/*  523 */       return destination;
/*      */     case 2:
/*  526 */       destination[destOffset] = ALPHABET[(inBuff >>> 18)];
/*  527 */       destination[(destOffset + 1)] = ALPHABET[(inBuff >>> 12 & 0x3F)];
/*  528 */       destination[(destOffset + 2)] = ALPHABET[(inBuff >>> 6 & 0x3F)];
/*  529 */       destination[(destOffset + 3)] = 61;
/*  530 */       return destination;
/*      */     case 1:
/*  533 */       destination[destOffset] = ALPHABET[(inBuff >>> 18)];
/*  534 */       destination[(destOffset + 1)] = ALPHABET[(inBuff >>> 12 & 0x3F)];
/*  535 */       destination[(destOffset + 2)] = 61;
/*  536 */       destination[(destOffset + 3)] = 61;
/*  537 */       return destination;
/*      */     }
/*      */ 
/*  540 */     return destination;
/*      */   }
/*      */ 
/*      */   public static void encode(ByteBuffer raw, ByteBuffer encoded)
/*      */   {
/*  558 */     byte[] raw3 = new byte[3];
/*  559 */     byte[] enc4 = new byte[4];
/*      */ 
/*  561 */     while (raw.hasRemaining()) {
/*  562 */       int rem = Math.min(3, raw.remaining());
/*  563 */       raw.get(raw3, 0, rem);
/*  564 */       encode3to4(enc4, raw3, rem, 0);
/*  565 */       encoded.put(enc4);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void encode(ByteBuffer raw, CharBuffer encoded)
/*      */   {
/*  582 */     byte[] raw3 = new byte[3];
/*  583 */     byte[] enc4 = new byte[4];
/*      */ 
/*  585 */     while (raw.hasRemaining()) {
/*  586 */       int rem = Math.min(3, raw.remaining());
/*  587 */       raw.get(raw3, 0, rem);
/*  588 */       encode3to4(enc4, raw3, rem, 0);
/*  589 */       for (int i = 0; i < 4; i++)
/*  590 */         encoded.put((char)(enc4[i] & 0xFF));
/*      */     }
/*      */   }
/*      */ 
/*      */   public static String encodeObject(Serializable serializableObject)
/*      */     throws IOException
/*      */   {
/*  618 */     return encodeObject(serializableObject, 0);
/*      */   }
/*      */ 
/*      */   public static String encodeObject(Serializable serializableObject, int options)
/*      */     throws IOException
/*      */   {
/*  655 */     if (serializableObject == null) {
/*  656 */       throw new NullPointerException("Cannot serialize a null object.");
/*      */     }
/*      */ 
/*  660 */     ByteArrayOutputStream baos = null;
/*  661 */     OutputStream b64os = null;
/*  662 */     GZIPOutputStream gzos = null;
/*  663 */     ObjectOutputStream oos = null;
/*      */     try
/*      */     {
/*  668 */       baos = new ByteArrayOutputStream();
/*  669 */       b64os = new OutputStream(baos, 0x1 | options);
/*  670 */       if ((options & 0x2) != 0)
/*      */       {
/*  672 */         gzos = new GZIPOutputStream(b64os);
/*  673 */         oos = new ObjectOutputStream(gzos);
/*      */       }
/*      */       else {
/*  676 */         oos = new ObjectOutputStream(b64os);
/*      */       }
/*  678 */       oos.writeObject(serializableObject);
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/*  683 */       throw e;
/*      */     } finally {
/*      */       try {
/*  686 */         oos.close(); } catch (Exception localException) {
/*      */       }try { gzos.close(); } catch (Exception localException1) {
/*      */       }try { b64os.close(); } catch (Exception localException2) {
/*      */       }try { baos.close();
/*      */       } catch (Exception localException3) {
/*      */       }
/*      */     }
/*      */     try {
/*  694 */       return new String(baos.toByteArray(), "US-ASCII");
/*      */     }
/*      */     catch (UnsupportedEncodingException uue) {
/*      */     }
/*  698 */     return new String(baos.toByteArray());
/*      */   }
/*      */ 
/*      */   public static String encodeBytes(byte[] source)
/*      */   {
/*  718 */     String encoded = null;
/*      */     try {
/*  720 */       encoded = encodeBytes(source, 0, source.length, 0);
/*      */     } catch (IOException ex) {
/*  722 */       if (!$assertionsDisabled) throw new AssertionError(ex.getMessage());
/*      */     }
/*  724 */     assert (encoded != null);
/*  725 */     return encoded;
/*      */   }
/*      */ 
/*      */   public static String encodeBytes(byte[] source, int options)
/*      */     throws IOException
/*      */   {
/*  760 */     return encodeBytes(source, 0, source.length, options);
/*      */   }
/*      */ 
/*      */   public static String encodeBytes(byte[] source, int off, int len)
/*      */   {
/*  786 */     String encoded = null;
/*      */     try {
/*  788 */       encoded = encodeBytes(source, off, len, 0);
/*      */     } catch (IOException ex) {
/*  790 */       if (!$assertionsDisabled) throw new AssertionError(ex.getMessage());
/*      */     }
/*  792 */     assert (encoded != null);
/*  793 */     return encoded;
/*      */   }
/*      */ 
/*      */   public static String encodeBytes(byte[] source, int off, int len, int options)
/*      */     throws IOException
/*      */   {
/*  831 */     byte[] encoded = encodeBytesToBytes(source, off, len, options);
/*      */     try
/*      */     {
/*  835 */       return new String(encoded, "US-ASCII");
/*      */     } catch (UnsupportedEncodingException uue) {
/*      */     }
/*  838 */     return new String(encoded);
/*      */   }
/*      */ 
/*      */   public static byte[] encodeBytesToBytes(byte[] source)
/*      */   {
/*  858 */     byte[] encoded = (byte[])null;
/*      */     try {
/*  860 */       encoded = encodeBytesToBytes(source, 0, source.length, 0);
/*      */     } catch (IOException ex) {
/*  862 */       if (!$assertionsDisabled) throw new AssertionError("IOExceptions only come from GZipping, which is turned off: " + ex.getMessage());
/*      */     }
/*  864 */     return encoded;
/*      */   }
/*      */ 
/*      */   public static byte[] encodeBytesToBytes(byte[] source, int off, int len, int options)
/*      */     throws IOException
/*      */   {
/*  888 */     if (source == null) {
/*  889 */       throw new NullPointerException("Cannot serialize a null array.");
/*      */     }
/*      */ 
/*  892 */     if (off < 0) {
/*  893 */       throw new IllegalArgumentException("Cannot have negative offset: " + off);
/*      */     }
/*      */ 
/*  896 */     if (len < 0) {
/*  897 */       throw new IllegalArgumentException("Cannot have length offset: " + len);
/*      */     }
/*      */ 
/*  900 */     if (off + len > source.length) {
/*  901 */       throw new IllegalArgumentException(
/*  902 */         String.format("Cannot have offset of %d and length of %d with array of length %d", new Object[] { Integer.valueOf(off), Integer.valueOf(len), Integer.valueOf(source.length) }));
/*      */     }
/*      */ 
/*  908 */     if ((options & 0x2) != 0) {
/*  909 */       ByteArrayOutputStream baos = null;
/*  910 */       GZIPOutputStream gzos = null;
/*  911 */       OutputStream b64os = null;
/*      */       try
/*      */       {
/*  915 */         baos = new ByteArrayOutputStream();
/*  916 */         b64os = new OutputStream(baos, 0x1 | options);
/*  917 */         gzos = new GZIPOutputStream(b64os);
/*      */ 
/*  919 */         gzos.write(source, off, len);
/*  920 */         gzos.close();
/*      */       }
/*      */       catch (IOException e)
/*      */       {
/*  925 */         throw e;
/*      */       } finally {
/*      */         try {
/*  928 */           gzos.close(); } catch (Exception localException) {
/*      */         }try { b64os.close(); } catch (Exception localException1) {
/*      */         }try { baos.close(); } catch (Exception localException2) {
/*      */         }
/*      */       }
/*  933 */       return baos.toByteArray();
/*      */     }
/*      */ 
/*  938 */     boolean breakLines = (options & 0x8) != 0;
/*      */ 
/*  947 */     int encLen = len / 3 * 4 + (len % 3 > 0 ? 4 : 0);
/*  948 */     if (breakLines) {
/*  949 */       encLen += encLen / 76;
/*      */     }
/*  951 */     byte[] outBuff = new byte[encLen];
/*      */ 
/*  954 */     int d = 0;
/*  955 */     int e = 0;
/*  956 */     int len2 = len - 2;
/*  957 */     int lineLength = 0;
/*  958 */     for (; d < len2; e += 4) {
/*  959 */       encode3to4(source, d + off, 3, outBuff, e, options);
/*      */ 
/*  961 */       lineLength += 4;
/*  962 */       if ((breakLines) && (lineLength >= 76))
/*      */       {
/*  964 */         outBuff[(e + 4)] = 10;
/*  965 */         e++;
/*  966 */         lineLength = 0;
/*      */       }
/*  958 */       d += 3;
/*      */     }
/*      */ 
/*  970 */     if (d < len) {
/*  971 */       encode3to4(source, d + off, len - d, outBuff, e, options);
/*  972 */       e += 4;
/*      */     }
/*      */ 
/*  977 */     if (e <= outBuff.length - 1)
/*      */     {
/*  982 */       byte[] finalOut = new byte[e];
/*  983 */       System.arraycopy(outBuff, 0, finalOut, 0, e);
/*      */ 
/*  985 */       return finalOut;
/*      */     }
/*      */ 
/*  988 */     return outBuff;
/*      */   }
/*      */ 
/*      */   private static int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset, int options)
/*      */   {
/* 1035 */     if (source == null) {
/* 1036 */       throw new NullPointerException("Source array was null.");
/*      */     }
/* 1038 */     if (destination == null) {
/* 1039 */       throw new NullPointerException("Destination array was null.");
/*      */     }
/* 1041 */     if ((srcOffset < 0) || (srcOffset + 3 >= source.length)) {
/* 1042 */       throw new IllegalArgumentException(String.format(
/* 1043 */         "Source array with length %d cannot have offset of %d and still process four bytes.", new Object[] { Integer.valueOf(source.length), Integer.valueOf(srcOffset) }));
/*      */     }
/* 1045 */     if ((destOffset < 0) || (destOffset + 2 >= destination.length)) {
/* 1046 */       throw new IllegalArgumentException(String.format(
/* 1047 */         "Destination array with length %d cannot have offset of %d and still store three bytes.", new Object[] { Integer.valueOf(destination.length), Integer.valueOf(destOffset) }));
/*      */     }
/*      */ 
/* 1051 */     byte[] DECODABET = getDecodabet(options);
/*      */ 
/* 1054 */     if (source[(srcOffset + 2)] == 61)
/*      */     {
/* 1058 */       int outBuff = (DECODABET[source[srcOffset]] & 0xFF) << 18 | 
/* 1059 */         (DECODABET[source[(srcOffset + 1)]] & 0xFF) << 12;
/*      */ 
/* 1061 */       destination[destOffset] = (byte)(outBuff >>> 16);
/* 1062 */       return 1;
/*      */     }
/*      */ 
/* 1066 */     if (source[(srcOffset + 3)] == 61)
/*      */     {
/* 1071 */       int outBuff = (DECODABET[source[srcOffset]] & 0xFF) << 18 | 
/* 1072 */         (DECODABET[source[(srcOffset + 1)]] & 0xFF) << 12 | 
/* 1073 */         (DECODABET[source[(srcOffset + 2)]] & 0xFF) << 6;
/*      */ 
/* 1075 */       destination[destOffset] = (byte)(outBuff >>> 16);
/* 1076 */       destination[(destOffset + 1)] = (byte)(outBuff >>> 8);
/* 1077 */       return 2;
/*      */     }
/*      */ 
/* 1087 */     int outBuff = (DECODABET[source[srcOffset]] & 0xFF) << 18 | 
/* 1088 */       (DECODABET[source[(srcOffset + 1)]] & 0xFF) << 12 | 
/* 1089 */       (DECODABET[source[(srcOffset + 2)]] & 0xFF) << 6 | 
/* 1090 */       DECODABET[source[(srcOffset + 3)]] & 0xFF;
/*      */ 
/* 1093 */     destination[destOffset] = (byte)(outBuff >> 16);
/* 1094 */     destination[(destOffset + 1)] = (byte)(outBuff >> 8);
/* 1095 */     destination[(destOffset + 2)] = (byte)outBuff;
/*      */ 
/* 1097 */     return 3;
/*      */   }
/*      */ 
/*      */   public static byte[] decode(byte[] source)
/*      */     throws IOException
/*      */   {
/* 1120 */     byte[] decoded = (byte[])null;
/*      */ 
/* 1122 */     decoded = decode(source, 0, source.length, 0);
/*      */ 
/* 1126 */     return decoded;
/*      */   }
/*      */ 
/*      */   public static byte[] decode(byte[] source, int off, int len, int options)
/*      */     throws IOException
/*      */   {
/* 1152 */     if (source == null) {
/* 1153 */       throw new NullPointerException("Cannot decode null source array.");
/*      */     }
/* 1155 */     if ((off < 0) || (off + len > source.length)) {
/* 1156 */       throw new IllegalArgumentException(String.format(
/* 1157 */         "Source array with length %d cannot have offset of %d and process %d bytes.", new Object[] { Integer.valueOf(source.length), Integer.valueOf(off), Integer.valueOf(len) }));
/*      */     }
/*      */ 
/* 1160 */     if (len == 0)
/* 1161 */       return new byte[0];
/* 1162 */     if (len < 4) {
/* 1163 */       throw new IllegalArgumentException(
/* 1164 */         "Base64-encoded string must have at least four characters, but length specified was " + len);
/*      */     }
/*      */ 
/* 1167 */     byte[] DECODABET = getDecodabet(options);
/*      */ 
/* 1169 */     int len34 = len * 3 / 4;
/* 1170 */     byte[] outBuff = new byte[len34];
/* 1171 */     int outBuffPosn = 0;
/*      */ 
/* 1173 */     byte[] b4 = new byte[4];
/* 1174 */     int b4Posn = 0;
/* 1175 */     int i = 0;
/* 1176 */     byte sbiDecode = 0;
/*      */ 
/* 1178 */     for (i = off; i < off + len; i++)
/*      */     {
/* 1180 */       sbiDecode = DECODABET[(source[i] & 0xFF)];
/*      */ 
/* 1185 */       if (sbiDecode >= -5) {
/* 1186 */         if (sbiDecode >= -1) {
/* 1187 */           b4[(b4Posn++)] = source[i];
/* 1188 */           if (b4Posn > 3) {
/* 1189 */             outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, options);
/* 1190 */             b4Posn = 0;
/*      */ 
/* 1193 */             if (source[i] == 61) {
/* 1194 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1201 */         throw new IOException(String.format(
/* 1202 */           "Bad Base64 input character decimal %d in array position %d", new Object[] { Integer.valueOf(source[i] & 0xFF), Integer.valueOf(i) }));
/*      */       }
/*      */     }
/*      */ 
/* 1206 */     byte[] out = new byte[outBuffPosn];
/* 1207 */     System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
/* 1208 */     return out;
/*      */   }
/*      */ 
/*      */   public static byte[] decode(String s)
/*      */     throws IOException
/*      */   {
/* 1224 */     return decode(s, 0);
/*      */   }
/*      */ 
/*      */   public static byte[] decode(String s, int options)
/*      */     throws IOException
/*      */   {
/* 1242 */     if (s == null) {
/* 1243 */       throw new NullPointerException("Input string was null.");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1248 */       bytes = s.getBytes("US-ASCII");
/*      */     }
/*      */     catch (UnsupportedEncodingException uee)
/*      */     {
/*      */       byte[] bytes;
/* 1251 */       bytes = s.getBytes();
/*      */     }
/*      */ 
/* 1256 */     byte[] bytes = decode(bytes, 0, bytes.length, options);
/*      */ 
/* 1260 */     boolean dontGunzip = (options & 0x4) != 0;
/* 1261 */     if ((bytes != null) && (bytes.length >= 4) && (!dontGunzip))
/*      */     {
/* 1263 */       int head = bytes[0] & 0xFF | bytes[1] << 8 & 0xFF00;
/* 1264 */       if (35615 == head) {
/* 1265 */         ByteArrayInputStream bais = null;
/* 1266 */         GZIPInputStream gzis = null;
/* 1267 */         ByteArrayOutputStream baos = null;
/* 1268 */         byte[] buffer = new byte[2048];
/* 1269 */         int length = 0;
/*      */         try
/*      */         {
/* 1272 */           baos = new ByteArrayOutputStream();
/* 1273 */           bais = new ByteArrayInputStream(bytes);
/* 1274 */           gzis = new GZIPInputStream(bais);
/*      */ 
/* 1276 */           while ((length = gzis.read(buffer)) >= 0) {
/* 1277 */             baos.write(buffer, 0, length);
/*      */           }
/*      */ 
/* 1281 */           bytes = baos.toByteArray();
/*      */         }
/*      */         catch (IOException e)
/*      */         {
/* 1285 */           e.printStackTrace();
/*      */           try
/*      */           {
/* 1289 */             baos.close(); } catch (Exception localException) {
/*      */           }try { gzis.close(); } catch (Exception localException1) {
/*      */           }try { bais.close();
/*      */           }
/*      */           catch (Exception localException2)
/*      */           {
/*      */           }
/*      */         }
/*      */         finally
/*      */         {
/*      */           try
/*      */           {
/* 1289 */             baos.close(); } catch (Exception localException3) {
/*      */           }try { gzis.close(); } catch (Exception localException4) {
/*      */           }try { bais.close();
/*      */           } catch (Exception localException5) {
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1297 */     return bytes;
/*      */   }
/*      */ 
/*      */   public static Object decodeToObject(String encodedObject)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1316 */     return decodeToObject(encodedObject, 0, null);
/*      */   }
/*      */ 
/*      */   public static Object decodeToObject(String encodedObject, int options, ClassLoader loader)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1341 */     byte[] objBytes = decode(encodedObject, options);
/*      */ 
/* 1343 */     ByteArrayInputStream bais = null;
/* 1344 */     ObjectInputStream ois = null;
/* 1345 */     Object obj = null;
/*      */     try
/*      */     {
/* 1348 */       bais = new ByteArrayInputStream(objBytes);
/*      */ 
/* 1351 */       if (loader == null) {
/* 1352 */         ois = new ObjectInputStream(bais);
/*      */       }
/*      */       else
/*      */       {
/* 1358 */         ois = new ObjectInputStream(bais, loader)
/*      */         {
/*      */           public Class<?> resolveClass(ObjectStreamClass streamClass) throws IOException, ClassNotFoundException
/*      */           {
/* 1362 */             Class c = Class.forName(streamClass.getName(), false, this.val$loader);
/* 1363 */             if (c == null) {
/* 1364 */               return super.resolveClass(streamClass);
/*      */             }
/* 1366 */             return c;
/*      */           }
/*      */         };
/*      */       }
/*      */ 
/* 1372 */       obj = ois.readObject();
/*      */     }
/*      */     catch (IOException e) {
/* 1375 */       throw e;
/*      */     }
/*      */     catch (ClassNotFoundException e) {
/* 1378 */       throw e;
/*      */     } finally {
/*      */       try {
/* 1381 */         bais.close(); } catch (Exception localException) {
/*      */       }try { ois.close(); } catch (Exception localException1) {
/*      */       }
/*      */     }
/* 1385 */     return obj;
/*      */   }
/*      */ 
/*      */   public static void encodeToFile(byte[] dataToEncode, String filename)
/*      */     throws IOException
/*      */   {
/* 1407 */     if (dataToEncode == null) {
/* 1408 */       throw new NullPointerException("Data to encode was null.");
/*      */     }
/*      */ 
/* 1411 */     OutputStream bos = null;
/*      */     try {
/* 1413 */       bos = new OutputStream(
/* 1414 */         new FileOutputStream(filename), 1);
/* 1415 */       bos.write(dataToEncode);
/*      */     }
/*      */     catch (IOException e) {
/* 1418 */       throw e;
/*      */     } finally {
/*      */       try {
/* 1421 */         bos.close();
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void decodeToFile(String dataToDecode, String filename)
/*      */     throws IOException
/*      */   {
/* 1443 */     OutputStream bos = null;
/*      */     try {
/* 1445 */       bos = new OutputStream(
/* 1446 */         new FileOutputStream(filename), 0);
/* 1447 */       bos.write(dataToDecode.getBytes("US-ASCII"));
/*      */     }
/*      */     catch (IOException e) {
/* 1450 */       throw e;
/*      */     } finally {
/*      */       try {
/* 1453 */         bos.close();
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static byte[] decodeFromFile(String filename)
/*      */     throws IOException
/*      */   {
/* 1478 */     byte[] decodedData = (byte[])null;
/* 1479 */     InputStream bis = null;
/*      */     try
/*      */     {
/* 1483 */       File file = new File(filename);
/* 1484 */       byte[] buffer = (byte[])null;
/* 1485 */       int length = 0;
/* 1486 */       int numBytes = 0;
/*      */ 
/* 1489 */       if (file.length() > 2147483647L)
/*      */       {
/* 1491 */         throw new IOException("File is too big for this convenience method (" + file.length() + " bytes).");
/*      */       }
/* 1493 */       buffer = new byte[(int)file.length()];
/*      */ 
/* 1496 */       bis = new InputStream(
/* 1497 */         new BufferedInputStream(
/* 1498 */         new FileInputStream(file)), 0);
/*      */ 
/* 1501 */       while ((numBytes = bis.read(buffer, length, 4096)) >= 0) {
/* 1502 */         length += numBytes;
/*      */       }
/*      */ 
/* 1506 */       decodedData = new byte[length];
/* 1507 */       System.arraycopy(buffer, 0, decodedData, 0, length);
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/* 1511 */       throw e;
/*      */     } finally {
/*      */       try {
/* 1514 */         bis.close(); } catch (Exception localException) {
/*      */       }
/*      */     }
/* 1517 */     return decodedData;
/*      */   }
/*      */ 
/*      */   public static String encodeFromFile(String filename)
/*      */     throws IOException
/*      */   {
/* 1539 */     String encodedData = null;
/* 1540 */     InputStream bis = null;
/*      */     try
/*      */     {
/* 1544 */       File file = new File(filename);
/* 1545 */       byte[] buffer = new byte[Math.max((int)(file.length() * 1.4D + 1.0D), 40)];
/* 1546 */       int length = 0;
/* 1547 */       int numBytes = 0;
/*      */ 
/* 1550 */       bis = new InputStream(
/* 1551 */         new BufferedInputStream(
/* 1552 */         new FileInputStream(file)), 1);
/*      */ 
/* 1555 */       while ((numBytes = bis.read(buffer, length, 4096)) >= 0) {
/* 1556 */         length += numBytes;
/*      */       }
/*      */ 
/* 1560 */       encodedData = new String(buffer, 0, length, "US-ASCII");
/*      */     }
/*      */     catch (IOException e)
/*      */     {
/* 1564 */       throw e;
/*      */     } finally {
/*      */       try {
/* 1567 */         bis.close(); } catch (Exception localException) {
/*      */       }
/*      */     }
/* 1570 */     return encodedData;
/*      */   }
/*      */ 
/*      */   public static void encodeFileToFile(String infile, String outfile)
/*      */     throws IOException
/*      */   {
/* 1584 */     String encoded = encodeFromFile(infile);
/* 1585 */     OutputStream out = null;
/*      */     try {
/* 1587 */       out = new BufferedOutputStream(
/* 1588 */         new FileOutputStream(outfile));
/* 1589 */       out.write(encoded.getBytes("US-ASCII"));
/*      */     }
/*      */     catch (IOException e) {
/* 1592 */       throw e;
/*      */     } finally {
/*      */       try {
/* 1595 */         out.close();
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void decodeFileToFile(String infile, String outfile)
/*      */     throws IOException
/*      */   {
/* 1612 */     byte[] decoded = decodeFromFile(infile);
/* 1613 */     OutputStream out = null;
/*      */     try {
/* 1615 */       out = new BufferedOutputStream(
/* 1616 */         new FileOutputStream(outfile));
/* 1617 */       out.write(decoded);
/*      */     }
/*      */     catch (IOException e) {
/* 1620 */       throw e;
/*      */     } finally {
/*      */       try {
/* 1623 */         out.close();
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class InputStream extends FilterInputStream
/*      */   {
/*      */     private boolean encode;
/*      */     private int position;
/*      */     private byte[] buffer;
/*      */     private int bufferLength;
/*      */     private int numSigBytes;
/*      */     private int lineLength;
/*      */     private boolean breakLines;
/*      */     private int options;
/*      */     private byte[] decodabet;
/*      */ 
/*      */     public InputStream(InputStream in)
/*      */     {
/* 1661 */       this(in, 0);
/*      */     }
/*      */ 
/*      */     public InputStream(InputStream in, int options)
/*      */     {
/* 1687 */       super();
/* 1688 */       this.options = options;
/* 1689 */       this.breakLines = ((options & 0x8) > 0);
/* 1690 */       this.encode = ((options & 0x1) > 0);
/* 1691 */       this.bufferLength = (this.encode ? 4 : 3);
/* 1692 */       this.buffer = new byte[this.bufferLength];
/* 1693 */       this.position = -1;
/* 1694 */       this.lineLength = 0;
/* 1695 */       this.decodabet = Base64.access$0(options);
/*      */     }
/*      */ 
/*      */     public int read()
/*      */       throws IOException
/*      */     {
/* 1709 */       if (this.position < 0) {
/* 1710 */         if (this.encode) {
/* 1711 */           byte[] b3 = new byte[3];
/* 1712 */           int numBinaryBytes = 0;
/* 1713 */           for (int i = 0; i < 3; i++) {
/* 1714 */             int b = this.in.read();
/*      */ 
/* 1717 */             if (b < 0) break;
/* 1718 */             b3[i] = (byte)b;
/* 1719 */             numBinaryBytes++;
/*      */           }
/*      */ 
/* 1726 */           if (numBinaryBytes > 0) {
/* 1727 */             Base64.access$1(b3, 0, numBinaryBytes, this.buffer, 0, this.options);
/* 1728 */             this.position = 0;
/* 1729 */             this.numSigBytes = 4;
/*      */           }
/*      */           else {
/* 1732 */             return -1;
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1738 */           byte[] b4 = new byte[4];
/* 1739 */           int i = 0;
/* 1740 */           for (i = 0; i < 4; i++)
/*      */           {
/* 1742 */             int b = 0;
/*      */             do b = this.in.read();
/* 1744 */             while ((b >= 0) && (this.decodabet[(b & 0x7F)] <= -5));
/*      */ 
/* 1746 */             if (b < 0)
/*      */             {
/*      */               break;
/*      */             }
/* 1750 */             b4[i] = (byte)b;
/*      */           }
/*      */ 
/* 1753 */           if (i == 4) {
/* 1754 */             this.numSigBytes = Base64.access$2(b4, 0, this.buffer, 0, this.options);
/* 1755 */             this.position = 0;
/*      */           } else {
/* 1757 */             if (i == 0) {
/* 1758 */               return -1;
/*      */             }
/*      */ 
/* 1762 */             throw new IOException("Improperly padded Base64 input.");
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1769 */       if (this.position >= 0)
/*      */       {
/* 1771 */         if (this.position >= this.numSigBytes) {
/* 1772 */           return -1;
/*      */         }
/*      */ 
/* 1775 */         if ((this.encode) && (this.breakLines) && (this.lineLength >= 76)) {
/* 1776 */           this.lineLength = 0;
/* 1777 */           return 10;
/*      */         }
/*      */ 
/* 1780 */         this.lineLength += 1;
/*      */ 
/* 1784 */         int b = this.buffer[(this.position++)];
/*      */ 
/* 1786 */         if (this.position >= this.bufferLength) {
/* 1787 */           this.position = -1;
/*      */         }
/*      */ 
/* 1790 */         return b & 0xFF;
/*      */       }
/*      */ 
/* 1797 */       throw new IOException("Error in Base64 code reading stream.");
/*      */     }
/*      */ 
/*      */     public int read(byte[] dest, int off, int len)
/*      */       throws IOException
/*      */     {
/* 1819 */       for (int i = 0; i < len; i++) {
/* 1820 */         int b = read();
/*      */ 
/* 1822 */         if (b >= 0) {
/* 1823 */           dest[(off + i)] = (byte)b;
/*      */         } else {
/* 1825 */           if (i != 0) break;
/* 1826 */           return -1;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1832 */       return i;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class OutputStream extends FilterOutputStream
/*      */   {
/*      */     private boolean encode;
/*      */     private int position;
/*      */     private byte[] buffer;
/*      */     private int bufferLength;
/*      */     private int lineLength;
/*      */     private boolean breakLines;
/*      */     private byte[] b4;
/*      */     private boolean suspendEncoding;
/*      */     private int options;
/*      */     private byte[] decodabet;
/*      */ 
/*      */     public OutputStream(OutputStream out)
/*      */     {
/* 1874 */       this(out, 1);
/*      */     }
/*      */ 
/*      */     public OutputStream(OutputStream out, int options)
/*      */     {
/* 1898 */       super();
/* 1899 */       this.breakLines = ((options & 0x8) != 0);
/* 1900 */       this.encode = ((options & 0x1) != 0);
/* 1901 */       this.bufferLength = (this.encode ? 3 : 4);
/* 1902 */       this.buffer = new byte[this.bufferLength];
/* 1903 */       this.position = 0;
/* 1904 */       this.lineLength = 0;
/* 1905 */       this.suspendEncoding = false;
/* 1906 */       this.b4 = new byte[4];
/* 1907 */       this.options = options;
/* 1908 */       this.decodabet = Base64.access$0(options);
/*      */     }
/*      */ 
/*      */     public void write(int theByte)
/*      */       throws IOException
/*      */     {
/* 1928 */       if (this.suspendEncoding) {
/* 1929 */         this.out.write(theByte);
/* 1930 */         return;
/*      */       }
/*      */ 
/* 1934 */       if (this.encode) {
/* 1935 */         this.buffer[(this.position++)] = (byte)theByte;
/* 1936 */         if (this.position >= this.bufferLength)
/*      */         {
/* 1938 */           this.out.write(Base64.access$3(this.b4, this.buffer, this.bufferLength, this.options));
/*      */ 
/* 1940 */           this.lineLength += 4;
/* 1941 */           if ((this.breakLines) && (this.lineLength >= 76)) {
/* 1942 */             this.out.write(10);
/* 1943 */             this.lineLength = 0;
/*      */           }
/*      */ 
/* 1946 */           this.position = 0;
/*      */         }
/*      */ 
/*      */       }
/* 1953 */       else if (this.decodabet[(theByte & 0x7F)] > -5) {
/* 1954 */         this.buffer[(this.position++)] = (byte)theByte;
/* 1955 */         if (this.position >= this.bufferLength)
/*      */         {
/* 1957 */           int len = Base64.access$2(this.buffer, 0, this.b4, 0, this.options);
/* 1958 */           this.out.write(this.b4, 0, len);
/* 1959 */           this.position = 0;
/*      */         }
/*      */       }
/* 1962 */       else if (this.decodabet[(theByte & 0x7F)] != -5) {
/* 1963 */         throw new IOException("Invalid character in Base64 data.");
/*      */       }
/*      */     }
/*      */ 
/*      */     public void write(byte[] theBytes, int off, int len)
/*      */       throws IOException
/*      */     {
/* 1983 */       if (this.suspendEncoding) {
/* 1984 */         this.out.write(theBytes, off, len);
/* 1985 */         return;
/*      */       }
/*      */ 
/* 1988 */       for (int i = 0; i < len; i++)
/* 1989 */         write(theBytes[(off + i)]);
/*      */     }
/*      */ 
/*      */     public void flushBase64()
/*      */       throws IOException
/*      */     {
/* 2002 */       if (this.position > 0)
/* 2003 */         if (this.encode) {
/* 2004 */           this.out.write(Base64.access$3(this.b4, this.buffer, this.position, this.options));
/* 2005 */           this.position = 0;
/*      */         }
/*      */         else {
/* 2008 */           throw new IOException("Base64 input not properly padded.");
/*      */         }
/*      */     }
/*      */ 
/*      */     public void close()
/*      */       throws IOException
/*      */     {
/* 2023 */       flushBase64();
/*      */ 
/* 2027 */       super.close();
/*      */ 
/* 2029 */       this.buffer = null;
/* 2030 */       this.out = null;
/*      */     }
/*      */ 
/*      */     public void suspendEncoding()
/*      */       throws IOException
/*      */     {
/* 2044 */       flushBase64();
/* 2045 */       this.suspendEncoding = true;
/*      */     }
/*      */ 
/*      */     public void resumeEncoding()
/*      */     {
/* 2057 */       this.suspendEncoding = false;
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     it.unibz.twitter.utils.Base64
 * JD-Core Version:    0.6.0
 */