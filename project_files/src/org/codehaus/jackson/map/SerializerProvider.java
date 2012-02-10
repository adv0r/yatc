/*     */ package org.codehaus.jackson.map;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Date;
/*     */ import org.codehaus.jackson.JsonGenerationException;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.map.ser.FilterProvider;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.schema.JsonSchema;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public abstract class SerializerProvider
/*     */ {
/*  24 */   protected static final JavaType TYPE_OBJECT = TypeFactory.type(Object.class);
/*     */   protected final SerializationConfig _config;
/*     */   protected final Class<?> _serializationView;
/*     */ 
/*     */   protected SerializerProvider(SerializationConfig config)
/*     */   {
/*  38 */     this._config = config;
/*  39 */     this._serializationView = (config == null ? null : this._config.getSerializationView());
/*     */   }
/*     */ 
/*     */   public abstract void serializeValue(SerializationConfig paramSerializationConfig, JsonGenerator paramJsonGenerator, Object paramObject, SerializerFactory paramSerializerFactory)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract void serializeValue(SerializationConfig paramSerializationConfig, JsonGenerator paramJsonGenerator, Object paramObject, JavaType paramJavaType, SerializerFactory paramSerializerFactory)
/*     */     throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract JsonSchema generateJsonSchema(Class<?> paramClass, SerializationConfig paramSerializationConfig, SerializerFactory paramSerializerFactory)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   public abstract boolean hasSerializerFor(SerializationConfig paramSerializationConfig, Class<?> paramClass, SerializerFactory paramSerializerFactory);
/*     */ 
/*     */   public final SerializationConfig getConfig()
/*     */   {
/* 103 */     return this._config;
/*     */   }
/*     */ 
/*     */   public final boolean isEnabled(SerializationConfig.Feature feature)
/*     */   {
/* 114 */     return this._config.isEnabled(feature);
/*     */   }
/*     */ 
/*     */   public final Class<?> getSerializationView()
/*     */   {
/* 125 */     return this._serializationView;
/*     */   }
/*     */ 
/*     */   public final FilterProvider getFilterProvider()
/*     */   {
/* 137 */     return this._config.getFilterProvider();
/*     */   }
/*     */ 
/*     */   public abstract JsonSerializer<Object> findValueSerializer(Class<?> paramClass, BeanProperty paramBeanProperty)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   public abstract JsonSerializer<Object> findValueSerializer(JavaType paramJavaType, BeanProperty paramBeanProperty)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   public abstract JsonSerializer<Object> findTypedValueSerializer(Class<?> paramClass, boolean paramBoolean, BeanProperty paramBeanProperty)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   public abstract JsonSerializer<Object> findTypedValueSerializer(JavaType paramJavaType, boolean paramBoolean, BeanProperty paramBeanProperty)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   public abstract JsonSerializer<Object> getKeySerializer(JavaType paramJavaType, BeanProperty paramBeanProperty)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   @Deprecated
/*     */   public final JsonSerializer<Object> findValueSerializer(Class<?> runtimeType)
/*     */     throws JsonMappingException
/*     */   {
/* 250 */     return findValueSerializer(runtimeType, null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final JsonSerializer<Object> findValueSerializer(JavaType serializationType)
/*     */     throws JsonMappingException
/*     */   {
/* 265 */     return findValueSerializer(serializationType, null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final JsonSerializer<Object> findTypedValueSerializer(Class<?> valueType, boolean cache)
/*     */     throws JsonMappingException
/*     */   {
/* 281 */     return findTypedValueSerializer(valueType, cache, null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final JsonSerializer<Object> findTypedValueSerializer(JavaType valueType, boolean cache)
/*     */     throws JsonMappingException
/*     */   {
/* 297 */     return findTypedValueSerializer(valueType, cache, null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final JsonSerializer<Object> getKeySerializer()
/*     */     throws JsonMappingException
/*     */   {
/* 312 */     return getKeySerializer(TYPE_OBJECT, null);
/*     */   }
/*     */ 
/*     */   public abstract JsonSerializer<Object> getNullKeySerializer();
/*     */ 
/*     */   public abstract JsonSerializer<Object> getNullValueSerializer();
/*     */ 
/*     */   public abstract JsonSerializer<Object> getUnknownTypeSerializer(Class<?> paramClass);
/*     */ 
/*     */   public final void defaultSerializeValue(Object value, JsonGenerator jgen)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 373 */     if (value == null) {
/* 374 */       getNullValueSerializer().serialize(null, jgen, this);
/*     */     } else {
/* 376 */       Class cls = value.getClass();
/* 377 */       findTypedValueSerializer(cls, true).serialize(value, jgen, this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void defaultSerializeField(String fieldName, Object value, JsonGenerator jgen)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 389 */     jgen.writeFieldName(fieldName);
/* 390 */     if (value == null)
/*     */     {
/* 394 */       getNullValueSerializer().serialize(null, jgen, this);
/*     */     } else {
/* 396 */       Class cls = value.getClass();
/* 397 */       findTypedValueSerializer(cls, true).serialize(value, jgen, this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract void defaultSerializeDateValue(long paramLong, JsonGenerator paramJsonGenerator)
/*     */     throws IOException, JsonProcessingException;
/*     */ 
/*     */   public abstract void defaultSerializeDateValue(Date paramDate, JsonGenerator paramJsonGenerator)
/*     */     throws IOException, JsonProcessingException;
/*     */ 
/*     */   public final void defaultSerializeNull(JsonGenerator jgen)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 428 */     getNullValueSerializer().serialize(null, jgen, this);
/*     */   }
/*     */ 
/*     */   public abstract int cachedSerializersCount();
/*     */ 
/*     */   public abstract void flushCachedSerializers();
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.SerializerProvider
 * JD-Core Version:    0.6.0
 */