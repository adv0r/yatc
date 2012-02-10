/*     */ package org.codehaus.jackson.map.ext;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.JsonToken;
/*     */ import org.codehaus.jackson.map.DeserializationContext;
/*     */ import org.codehaus.jackson.map.deser.StdDeserializer;
/*     */ import org.codehaus.jackson.map.deser.StdScalarDeserializer;
/*     */ import org.codehaus.jackson.map.util.Provider;
/*     */ import org.joda.time.DateMidnight;
/*     */ import org.joda.time.DateTime;
/*     */ import org.joda.time.DateTimeZone;
/*     */ import org.joda.time.LocalDate;
/*     */ import org.joda.time.LocalDateTime;
/*     */ import org.joda.time.ReadableDateTime;
/*     */ import org.joda.time.ReadableInstant;
/*     */ import org.joda.time.format.DateTimeFormatter;
/*     */ import org.joda.time.format.ISODateTimeFormat;
/*     */ 
/*     */ public class JodaDeserializers
/*     */   implements Provider<StdDeserializer<?>>
/*     */ {
/*     */   public Collection<StdDeserializer<?>> provide()
/*     */   {
/*  32 */     return Arrays.asList(new StdDeserializer[] { new DateTimeDeserializer(DateTime.class), new DateTimeDeserializer(ReadableDateTime.class), new DateTimeDeserializer(ReadableInstant.class), new LocalDateDeserializer(), new LocalDateTimeDeserializer(), new DateMidnightDeserializer() });
/*     */   }
/*     */ 
/*     */   public static class DateMidnightDeserializer extends JodaDeserializers.JodaDeserializer<DateMidnight>
/*     */   {
/*     */     public DateMidnightDeserializer()
/*     */     {
/* 203 */       super();
/*     */     }
/*     */ 
/*     */     public DateMidnight deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 210 */       if (jp.isExpectedStartArrayToken()) {
/* 211 */         jp.nextToken();
/* 212 */         int year = jp.getIntValue();
/* 213 */         jp.nextToken();
/* 214 */         int month = jp.getIntValue();
/* 215 */         jp.nextToken();
/* 216 */         int day = jp.getIntValue();
/* 217 */         if (jp.nextToken() != JsonToken.END_ARRAY) {
/* 218 */           ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "after DateMidnight ints");
/*     */         }
/* 220 */         return new DateMidnight(year, month, day);
/*     */       }
/* 222 */       switch (JodaDeserializers.1.$SwitchMap$org$codehaus$jackson$JsonToken[jp.getCurrentToken().ordinal()]) {
/*     */       case 1:
/* 224 */         return new DateMidnight(jp.getLongValue());
/*     */       case 2:
/* 226 */         DateTime local = parseLocal(jp);
/* 227 */         if (local == null) {
/* 228 */           return null;
/*     */         }
/* 230 */         return local.toDateMidnight();
/*     */       }
/* 232 */       ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "expected JSON Array, Number or String");
/* 233 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class LocalDateTimeDeserializer extends JodaDeserializers.JodaDeserializer<LocalDateTime>
/*     */   {
/*     */     public LocalDateTimeDeserializer()
/*     */     {
/* 150 */       super();
/*     */     }
/*     */ 
/*     */     public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 157 */       if (jp.isExpectedStartArrayToken()) {
/* 158 */         jp.nextToken();
/* 159 */         int year = jp.getIntValue();
/* 160 */         jp.nextToken();
/* 161 */         int month = jp.getIntValue();
/* 162 */         jp.nextToken();
/* 163 */         int day = jp.getIntValue();
/* 164 */         jp.nextToken();
/* 165 */         int hour = jp.getIntValue();
/* 166 */         jp.nextToken();
/* 167 */         int minute = jp.getIntValue();
/* 168 */         jp.nextToken();
/* 169 */         int second = jp.getIntValue();
/*     */ 
/* 171 */         int millisecond = 0;
/* 172 */         if (jp.nextToken() != JsonToken.END_ARRAY) {
/* 173 */           millisecond = jp.getIntValue();
/* 174 */           jp.nextToken();
/*     */         }
/* 176 */         if (jp.getCurrentToken() != JsonToken.END_ARRAY) {
/* 177 */           ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "after LocalDateTime ints");
/*     */         }
/* 179 */         return new LocalDateTime(year, month, day, hour, minute, second, millisecond);
/*     */       }
/*     */ 
/* 182 */       switch (JodaDeserializers.1.$SwitchMap$org$codehaus$jackson$JsonToken[jp.getCurrentToken().ordinal()]) {
/*     */       case 1:
/* 184 */         return new LocalDateTime(jp.getLongValue());
/*     */       case 2:
/* 186 */         DateTime local = parseLocal(jp);
/* 187 */         if (local == null) {
/* 188 */           return null;
/*     */         }
/* 190 */         return local.toLocalDateTime();
/*     */       }
/* 192 */       ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "expected JSON Array or Number");
/* 193 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class LocalDateDeserializer extends JodaDeserializers.JodaDeserializer<LocalDate>
/*     */   {
/*     */     public LocalDateDeserializer()
/*     */     {
/* 110 */       super();
/*     */     }
/*     */ 
/*     */     public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 117 */       if (jp.isExpectedStartArrayToken()) {
/* 118 */         jp.nextToken();
/* 119 */         int year = jp.getIntValue();
/* 120 */         jp.nextToken();
/* 121 */         int month = jp.getIntValue();
/* 122 */         jp.nextToken();
/* 123 */         int day = jp.getIntValue();
/* 124 */         if (jp.nextToken() != JsonToken.END_ARRAY) {
/* 125 */           ctxt.wrongTokenException(jp, JsonToken.END_ARRAY, "after LocalDate ints");
/*     */         }
/* 127 */         return new LocalDate(year, month, day);
/*     */       }
/* 129 */       switch (JodaDeserializers.1.$SwitchMap$org$codehaus$jackson$JsonToken[jp.getCurrentToken().ordinal()]) {
/*     */       case 1:
/* 131 */         return new LocalDate(jp.getLongValue());
/*     */       case 2:
/* 133 */         DateTime local = parseLocal(jp);
/* 134 */         if (local == null) {
/* 135 */           return null;
/*     */         }
/* 137 */         return local.toLocalDate();
/*     */       }
/* 139 */       ctxt.wrongTokenException(jp, JsonToken.START_ARRAY, "expected JSON Array, String or Number");
/* 140 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class DateTimeDeserializer<T extends ReadableInstant> extends JodaDeserializers.JodaDeserializer<T>
/*     */   {
/*     */     public DateTimeDeserializer(Class<T> cls)
/*     */     {
/*  82 */       super();
/*     */     }
/*     */ 
/*     */     public T deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/*  89 */       JsonToken t = jp.getCurrentToken();
/*  90 */       if (t == JsonToken.VALUE_NUMBER_INT) {
/*  91 */         return new DateTime(jp.getLongValue(), DateTimeZone.UTC);
/*     */       }
/*  93 */       if (t == JsonToken.VALUE_STRING) {
/*  94 */         String str = jp.getText().trim();
/*  95 */         if (str.length() == 0) {
/*  96 */           return null;
/*     */         }
/*  98 */         return new DateTime(str, DateTimeZone.UTC);
/*     */       }
/* 100 */       throw ctxt.mappingException(getValueClass());
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract class JodaDeserializer<T> extends StdScalarDeserializer<T>
/*     */   {
/*  50 */     static final DateTimeFormatter _localDateTimeFormat = ISODateTimeFormat.localDateOptionalTimeParser();
/*     */ 
/*  52 */     protected JodaDeserializer(Class<T> cls) { super(); }
/*     */ 
/*     */     protected DateTime parseLocal(JsonParser jp)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/*  57 */       String str = jp.getText().trim();
/*  58 */       if (str.length() == 0) {
/*  59 */         return null;
/*     */       }
/*  61 */       return _localDateTimeFormat.parseDateTime(str);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ext.JodaDeserializers
 * JD-Core Version:    0.6.0
 */