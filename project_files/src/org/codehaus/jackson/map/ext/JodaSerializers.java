/*     */ package org.codehaus.jackson.map.ext;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map.Entry;
/*     */ import org.codehaus.jackson.JsonGenerationException;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.map.JsonSerializer;
/*     */ import org.codehaus.jackson.map.SerializationConfig.Feature;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ import org.codehaus.jackson.map.ser.SerializerBase;
/*     */ import org.codehaus.jackson.map.util.Provider;
/*     */ import org.joda.time.DateMidnight;
/*     */ import org.joda.time.DateMidnight.Property;
/*     */ import org.joda.time.DateTime;
/*     */ import org.joda.time.LocalDate;
/*     */ import org.joda.time.LocalDate.Property;
/*     */ import org.joda.time.LocalDateTime;
/*     */ import org.joda.time.LocalDateTime.Property;
/*     */ import org.joda.time.ReadableInstant;
/*     */ import org.joda.time.ReadablePartial;
/*     */ import org.joda.time.format.DateTimeFormatter;
/*     */ import org.joda.time.format.ISODateTimeFormat;
/*     */ 
/*     */ public class JodaSerializers
/*     */   implements Provider<Map.Entry<Class<?>, JsonSerializer<?>>>
/*     */ {
/*  28 */   static final HashMap<Class<?>, JsonSerializer<?>> _serializers = new HashMap();
/*     */ 
/*     */   public Collection<Map.Entry<Class<?>, JsonSerializer<?>>> provide()
/*     */   {
/*  39 */     return _serializers.entrySet();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  30 */     _serializers.put(DateTime.class, new DateTimeSerializer());
/*  31 */     _serializers.put(LocalDateTime.class, new LocalDateTimeSerializer());
/*  32 */     _serializers.put(LocalDate.class, new LocalDateSerializer());
/*  33 */     _serializers.put(DateMidnight.class, new DateMidnightSerializer());
/*     */   }
/*     */ 
/*     */   public static final class DateMidnightSerializer extends JodaSerializers.JodaSerializer<DateMidnight>
/*     */   {
/*     */     public DateMidnightSerializer()
/*     */     {
/* 173 */       super();
/*     */     }
/*     */ 
/*     */     public void serialize(DateMidnight dt, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 179 */       if (provider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS))
/*     */       {
/* 181 */         jgen.writeStartArray();
/* 182 */         jgen.writeNumber(dt.year().get());
/* 183 */         jgen.writeNumber(dt.monthOfYear().get());
/* 184 */         jgen.writeNumber(dt.dayOfMonth().get());
/* 185 */         jgen.writeEndArray();
/*     */       } else {
/* 187 */         jgen.writeString(printLocalDate(dt));
/*     */       }
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 194 */       return createSchemaNode(provider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS) ? "array" : "string", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class LocalDateSerializer extends JodaSerializers.JodaSerializer<LocalDate>
/*     */   {
/*     */     public LocalDateSerializer()
/*     */     {
/* 144 */       super();
/*     */     }
/*     */ 
/*     */     public void serialize(LocalDate dt, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 150 */       if (provider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS))
/*     */       {
/* 152 */         jgen.writeStartArray();
/* 153 */         jgen.writeNumber(dt.year().get());
/* 154 */         jgen.writeNumber(dt.monthOfYear().get());
/* 155 */         jgen.writeNumber(dt.dayOfMonth().get());
/* 156 */         jgen.writeEndArray();
/*     */       } else {
/* 158 */         jgen.writeString(printLocalDate(dt));
/*     */       }
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 165 */       return createSchemaNode(provider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS) ? "array" : "string", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class LocalDateTimeSerializer extends JodaSerializers.JodaSerializer<LocalDateTime>
/*     */   {
/*     */     public LocalDateTimeSerializer()
/*     */     {
/* 111 */       super();
/*     */     }
/*     */ 
/*     */     public void serialize(LocalDateTime dt, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 117 */       if (provider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS))
/*     */       {
/* 119 */         jgen.writeStartArray();
/* 120 */         jgen.writeNumber(dt.year().get());
/* 121 */         jgen.writeNumber(dt.monthOfYear().get());
/* 122 */         jgen.writeNumber(dt.dayOfMonth().get());
/* 123 */         jgen.writeNumber(dt.hourOfDay().get());
/* 124 */         jgen.writeNumber(dt.minuteOfHour().get());
/* 125 */         jgen.writeNumber(dt.secondOfMinute().get());
/* 126 */         jgen.writeNumber(dt.millisOfSecond().get());
/* 127 */         jgen.writeEndArray();
/*     */       } else {
/* 129 */         jgen.writeString(printLocalDateTime(dt));
/*     */       }
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 136 */       return createSchemaNode(provider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS) ? "array" : "string", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class DateTimeSerializer extends JodaSerializers.JodaSerializer<DateTime>
/*     */   {
/*     */     public DateTimeSerializer()
/*     */     {
/*  83 */       super();
/*     */     }
/*     */ 
/*     */     public void serialize(DateTime value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/*  89 */       if (provider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS))
/*  90 */         jgen.writeNumber(value.getMillis());
/*     */       else
/*  92 */         jgen.writeString(value.toString());
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/*  99 */       return createSchemaNode(provider.isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS) ? "number" : "string", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static abstract class JodaSerializer<T> extends SerializerBase<T>
/*     */   {
/*  50 */     static final DateTimeFormatter _localDateTimeFormat = ISODateTimeFormat.dateTime();
/*  51 */     static final DateTimeFormatter _localDateFormat = ISODateTimeFormat.date();
/*     */ 
/*  53 */     protected JodaSerializer(Class<T> cls) { super(); }
/*     */ 
/*     */     protected String printLocalDateTime(ReadablePartial dateValue)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/*  58 */       return _localDateTimeFormat.print(dateValue);
/*     */     }
/*     */ 
/*     */     protected String printLocalDate(ReadablePartial dateValue)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/*  64 */       return _localDateFormat.print(dateValue);
/*     */     }
/*     */ 
/*     */     protected String printLocalDate(ReadableInstant dateValue)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/*  70 */       return _localDateFormat.print(dateValue);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ext.JodaSerializers
 * JD-Core Version:    0.6.0
 */