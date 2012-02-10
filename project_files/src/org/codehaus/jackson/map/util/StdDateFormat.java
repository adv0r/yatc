/*     */ package org.codehaus.jackson.map.util;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.ParseException;
/*     */ import java.text.ParsePosition;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.TimeZone;
/*     */ import org.codehaus.jackson.io.NumberInput;
/*     */ 
/*     */ public class StdDateFormat extends DateFormat
/*     */ {
/*     */   static final String DATE_FORMAT_STR_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
/*     */   static final String DATE_FORMAT_STR_ISO8601_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
/*     */   static final String DATE_FORMAT_STR_PLAIN = "yyyy-MM-dd";
/*     */   static final String DATE_FORMAT_STR_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
/*  56 */   static final String[] ALL_FORMATS = { "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "EEE, dd MMM yyyy HH:mm:ss zzz", "yyyy-MM-dd" };
/*     */   static final SimpleDateFormat DATE_FORMAT_RFC1123;
/*     */   static final SimpleDateFormat DATE_FORMAT_ISO8601;
/*     */   static final SimpleDateFormat DATE_FORMAT_ISO8601_Z;
/*     */   static final SimpleDateFormat DATE_FORMAT_PLAIN;
/*     */   public static final StdDateFormat instance;
/*     */   transient SimpleDateFormat _formatRFC1123;
/*     */   transient SimpleDateFormat _formatISO8601;
/*     */   transient SimpleDateFormat _formatISO8601_z;
/*     */   transient SimpleDateFormat _formatPlain;
/*     */ 
/*     */   public StdDateFormat clone()
/*     */   {
/* 113 */     return new StdDateFormat();
/*     */   }
/*     */ 
/*     */   public static DateFormat getBlueprintISO8601Format()
/*     */   {
/* 122 */     return DATE_FORMAT_ISO8601;
/*     */   }
/*     */ 
/*     */   public static DateFormat getISO8601Format(TimeZone tz)
/*     */   {
/* 131 */     DateFormat df = (SimpleDateFormat)DATE_FORMAT_ISO8601.clone();
/* 132 */     df.setTimeZone(tz);
/* 133 */     return df;
/*     */   }
/*     */ 
/*     */   public static DateFormat getBlueprintRFC1123Format()
/*     */   {
/* 142 */     return DATE_FORMAT_RFC1123;
/*     */   }
/*     */ 
/*     */   public static DateFormat getRFC1123Format(TimeZone tz)
/*     */   {
/* 153 */     DateFormat df = (SimpleDateFormat)DATE_FORMAT_RFC1123.clone();
/* 154 */     df.setTimeZone(tz);
/* 155 */     return df;
/*     */   }
/*     */ 
/*     */   public Date parse(String dateStr)
/*     */     throws ParseException
/*     */   {
/* 167 */     dateStr = dateStr.trim();
/* 168 */     ParsePosition pos = new ParsePosition(0);
/* 169 */     Date result = parse(dateStr, pos);
/* 170 */     if (result != null) {
/* 171 */       return result;
/*     */     }
/*     */ 
/* 174 */     StringBuilder sb = new StringBuilder();
/* 175 */     for (String f : ALL_FORMATS) {
/* 176 */       if (sb.length() > 0)
/* 177 */         sb.append("\", \"");
/*     */       else {
/* 179 */         sb.append('"');
/*     */       }
/* 181 */       sb.append(f);
/*     */     }
/* 183 */     sb.append('"');
/* 184 */     throw new ParseException(String.format("Can not parse date \"%s\": not compatible with any of standard forms (%s)", new Object[] { dateStr, sb.toString() }), pos.getErrorIndex());
/*     */   }
/*     */ 
/*     */   public Date parse(String dateStr, ParsePosition pos)
/*     */   {
/* 192 */     if (looksLikeISO8601(dateStr)) {
/* 193 */       return parseAsISO8601(dateStr, pos);
/*     */     }
/*     */ 
/* 198 */     int i = dateStr.length();
/*     */     while (true) { i--; if (i < 0) break;
/* 200 */       char ch = dateStr.charAt(i);
/* 201 */       if ((ch < '0') || (ch > '9'))
/*     */         break; }
/* 203 */     if ((i < 0) && 
/* 204 */       (NumberInput.inLongRange(dateStr, false))) {
/* 205 */       return new Date(Long.parseLong(dateStr));
/*     */     }
/*     */ 
/* 209 */     return parseAsRFC1123(dateStr, pos);
/*     */   }
/*     */ 
/*     */   public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition)
/*     */   {
/* 216 */     if (this._formatISO8601 == null) {
/* 217 */       this._formatISO8601 = ((SimpleDateFormat)DATE_FORMAT_ISO8601.clone());
/*     */     }
/* 219 */     return this._formatISO8601.format(date, toAppendTo, fieldPosition);
/*     */   }
/*     */ 
/*     */   protected boolean looksLikeISO8601(String dateStr)
/*     */   {
/* 239 */     return (dateStr.length() >= 5) && (Character.isDigit(dateStr.charAt(0))) && (Character.isDigit(dateStr.charAt(3))) && (dateStr.charAt(4) == '-');
/*     */   }
/*     */ 
/*     */   protected Date parseAsISO8601(String dateStr, ParsePosition pos)
/*     */   {
/* 254 */     int len = dateStr.length();
/* 255 */     char c = dateStr.charAt(len - 1);
/*     */     SimpleDateFormat df;
/* 259 */     if ((len <= 10) && (Character.isDigit(c))) {
/* 260 */       SimpleDateFormat df = this._formatPlain;
/* 261 */       if (df == null)
/* 262 */         df = this._formatPlain = (SimpleDateFormat)DATE_FORMAT_PLAIN.clone();
/*     */     }
/* 264 */     else if (c == 'Z') {
/* 265 */       SimpleDateFormat df = this._formatISO8601_z;
/* 266 */       if (df == null) {
/* 267 */         df = this._formatISO8601_z = (SimpleDateFormat)DATE_FORMAT_ISO8601_Z.clone();
/*     */       }
/*     */ 
/* 270 */       if (dateStr.charAt(len - 4) == ':') {
/* 271 */         StringBuilder sb = new StringBuilder(dateStr);
/* 272 */         sb.insert(len - 1, ".000");
/* 273 */         dateStr = sb.toString();
/*     */       }
/*     */ 
/*     */     }
/* 277 */     else if (hasTimeZone(dateStr)) {
/* 278 */       c = dateStr.charAt(len - 3);
/* 279 */       if (c == ':')
/*     */       {
/* 281 */         StringBuilder sb = new StringBuilder(dateStr);
/* 282 */         sb.delete(len - 3, len - 2);
/* 283 */         dateStr = sb.toString();
/* 284 */       } else if ((c == '+') || (c == '-'))
/*     */       {
/* 286 */         dateStr = dateStr + "00";
/*     */       }
/*     */ 
/* 289 */       len = dateStr.length();
/*     */ 
/* 291 */       c = dateStr.charAt(len - 9);
/* 292 */       if (Character.isDigit(c)) {
/* 293 */         StringBuilder sb = new StringBuilder(dateStr);
/* 294 */         sb.insert(len - 5, ".000");
/* 295 */         dateStr = sb.toString();
/*     */       }
/*     */ 
/* 298 */       SimpleDateFormat df = this._formatISO8601;
/* 299 */       if (this._formatISO8601 == null) {
/* 300 */         df = this._formatISO8601 = (SimpleDateFormat)DATE_FORMAT_ISO8601.clone();
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 308 */       StringBuilder sb = new StringBuilder(dateStr);
/*     */ 
/* 310 */       int timeLen = len - dateStr.lastIndexOf('T') - 1;
/* 311 */       if (timeLen <= 8) {
/* 312 */         sb.append(".000");
/*     */       }
/* 314 */       sb.append('Z');
/* 315 */       dateStr = sb.toString();
/* 316 */       df = this._formatISO8601_z;
/* 317 */       if (df == null) {
/* 318 */         df = this._formatISO8601_z = (SimpleDateFormat)DATE_FORMAT_ISO8601_Z.clone();
/*     */       }
/*     */     }
/*     */ 
/* 322 */     return df.parse(dateStr, pos);
/*     */   }
/*     */ 
/*     */   protected Date parseAsRFC1123(String dateStr, ParsePosition pos)
/*     */   {
/* 327 */     if (this._formatRFC1123 == null) {
/* 328 */       this._formatRFC1123 = ((SimpleDateFormat)DATE_FORMAT_RFC1123.clone());
/*     */     }
/* 330 */     return this._formatRFC1123.parse(dateStr, pos);
/*     */   }
/*     */ 
/*     */   private static final boolean hasTimeZone(String str)
/*     */   {
/* 336 */     int len = str.length();
/* 337 */     if (len >= 6) {
/* 338 */       char c = str.charAt(len - 6);
/* 339 */       if ((c == '+') || (c == '-')) return true;
/* 340 */       c = str.charAt(len - 5);
/* 341 */       if ((c == '+') || (c == '-')) return true;
/* 342 */       c = str.charAt(len - 3);
/* 343 */       if ((c == '+') || (c == '-')) return true;
/*     */     }
/* 345 */     return false;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  78 */     TimeZone gmt = TimeZone.getTimeZone("GMT");
/*  79 */     DATE_FORMAT_RFC1123 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
/*  80 */     DATE_FORMAT_RFC1123.setTimeZone(gmt);
/*  81 */     DATE_FORMAT_ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
/*  82 */     DATE_FORMAT_ISO8601.setTimeZone(gmt);
/*  83 */     DATE_FORMAT_ISO8601_Z = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
/*  84 */     DATE_FORMAT_ISO8601_Z.setTimeZone(gmt);
/*  85 */     DATE_FORMAT_PLAIN = new SimpleDateFormat("yyyy-MM-dd");
/*  86 */     DATE_FORMAT_PLAIN.setTimeZone(gmt);
/*     */ 
/*  92 */     instance = new StdDateFormat();
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.util.StdDateFormat
 * JD-Core Version:    0.6.0
 */