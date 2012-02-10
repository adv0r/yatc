/*     */ package org.codehaus.jackson.map.ser;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.Collection;
/*     */ import java.util.Currency;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.regex.Pattern;
/*     */ import org.codehaus.jackson.JsonGenerationException;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ import org.codehaus.jackson.map.util.Provider;
/*     */ 
/*     */ public class JdkSerializers
/*     */   implements Provider<Map.Entry<Class<?>, Object>>
/*     */ {
/*     */   public Collection<Map.Entry<Class<?>, Object>> provide()
/*     */   {
/*  28 */     HashMap sers = new HashMap();
/*     */ 
/*  31 */     ToStringSerializer sls = ToStringSerializer.instance;
/*     */ 
/*  33 */     sers.put(URL.class, sls);
/*  34 */     sers.put(URI.class, sls);
/*     */ 
/*  36 */     sers.put(Currency.class, sls);
/*  37 */     sers.put(UUID.class, sls);
/*  38 */     sers.put(Pattern.class, sls);
/*  39 */     sers.put(Locale.class, sls);
/*     */ 
/*  42 */     sers.put(Locale.class, sls);
/*     */ 
/*  45 */     sers.put(AtomicReference.class, AtomicReferenceSerializer.class);
/*  46 */     sers.put(AtomicBoolean.class, AtomicBooleanSerializer.class);
/*  47 */     sers.put(AtomicInteger.class, AtomicIntegerSerializer.class);
/*  48 */     sers.put(AtomicLong.class, AtomicLongSerializer.class);
/*     */ 
/*  51 */     sers.put(File.class, FileSerializer.class);
/*  52 */     sers.put(Class.class, ClassSerializer.class);
/*     */ 
/*  55 */     sers.put(Void.TYPE, NullSerializer.class);
/*     */ 
/*  57 */     return sers.entrySet();
/*     */   }
/*     */ 
/*     */   public static final class ClassSerializer extends ScalarSerializerBase<Class>
/*     */   {
/*     */     public ClassSerializer()
/*     */     {
/* 179 */       super();
/*     */     }
/*     */ 
/*     */     public void serialize(Class value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 185 */       jgen.writeString(value.getName());
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 192 */       return createSchemaNode("string", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class FileSerializer extends ScalarSerializerBase<File>
/*     */   {
/*     */     public FileSerializer()
/*     */     {
/* 155 */       super();
/*     */     }
/*     */ 
/*     */     public void serialize(File value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 161 */       jgen.writeString(value.getAbsolutePath());
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 167 */       return createSchemaNode("string", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class AtomicReferenceSerializer extends SerializerBase<AtomicReference<?>>
/*     */   {
/*     */     public AtomicReferenceSerializer()
/*     */     {
/* 126 */       super(false);
/*     */     }
/*     */ 
/*     */     public void serialize(AtomicReference<?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 132 */       provider.defaultSerializeValue(value.get(), jgen);
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 138 */       return createSchemaNode("any", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class AtomicLongSerializer extends ScalarSerializerBase<AtomicLong>
/*     */   {
/*     */     public AtomicLongSerializer()
/*     */     {
/* 107 */       super(false);
/*     */     }
/*     */ 
/*     */     public void serialize(AtomicLong value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 113 */       jgen.writeNumber(value.get());
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 119 */       return createSchemaNode("integer", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class AtomicIntegerSerializer extends ScalarSerializerBase<AtomicInteger>
/*     */   {
/*     */     public AtomicIntegerSerializer()
/*     */     {
/*  88 */       super(false);
/*     */     }
/*     */ 
/*     */     public void serialize(AtomicInteger value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/*  94 */       jgen.writeNumber(value.get());
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 100 */       return createSchemaNode("integer", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class AtomicBooleanSerializer extends ScalarSerializerBase<AtomicBoolean>
/*     */   {
/*     */     public AtomicBooleanSerializer()
/*     */     {
/*  69 */       super(false);
/*     */     }
/*     */ 
/*     */     public void serialize(AtomicBoolean value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/*  75 */       jgen.writeBoolean(value.get());
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/*  81 */       return createSchemaNode("boolean", true);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.JdkSerializers
 * JD-Core Version:    0.6.0
 */