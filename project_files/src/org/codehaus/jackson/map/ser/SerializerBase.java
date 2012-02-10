/*     */ package org.codehaus.jackson.map.ser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Type;
/*     */ import org.codehaus.jackson.JsonGenerationException;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.JsonSerializer;
/*     */ import org.codehaus.jackson.map.SerializationConfig.Feature;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ import org.codehaus.jackson.map.annotate.JacksonStdImpl;
/*     */ import org.codehaus.jackson.node.JsonNodeFactory;
/*     */ import org.codehaus.jackson.node.ObjectNode;
/*     */ import org.codehaus.jackson.schema.SchemaAware;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public abstract class SerializerBase<T> extends JsonSerializer<T>
/*     */   implements SchemaAware
/*     */ {
/*     */   protected final Class<T> _handledType;
/*     */ 
/*     */   protected SerializerBase(Class<T> t)
/*     */   {
/*  28 */     this._handledType = t;
/*     */   }
/*     */ 
/*     */   protected SerializerBase(JavaType type)
/*     */   {
/*  36 */     this._handledType = type.getRawClass();
/*     */   }
/*     */ 
/*     */   protected SerializerBase(Class<?> t, boolean dummy)
/*     */   {
/*  45 */     this._handledType = t;
/*     */   }
/*     */ 
/*     */   public final Class<T> handledType() {
/*  49 */     return this._handledType;
/*     */   }
/*     */ 
/*     */   public abstract void serialize(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider) throws IOException, JsonGenerationException;
/*     */ 
/*     */   public abstract JsonNode getSchema(SerializerProvider paramSerializerProvider, Type paramType) throws JsonMappingException;
/*     */ 
/*     */   protected ObjectNode createObjectNode() {
/*  59 */     return JsonNodeFactory.instance.objectNode();
/*     */   }
/*     */ 
/*     */   protected ObjectNode createSchemaNode(String type)
/*     */   {
/*  64 */     ObjectNode schema = createObjectNode();
/*  65 */     schema.put("type", type);
/*  66 */     return schema;
/*     */   }
/*     */ 
/*     */   protected ObjectNode createSchemaNode(String type, boolean isOptional)
/*     */   {
/*  71 */     ObjectNode schema = createSchemaNode(type);
/*  72 */     schema.put("optional", isOptional);
/*  73 */     return schema;
/*     */   }
/*     */ 
/*     */   protected boolean isDefaultSerializer(JsonSerializer<?> serializer)
/*     */   {
/*  86 */     return (serializer != null) && (serializer.getClass().getAnnotation(JacksonStdImpl.class) != null);
/*     */   }
/*     */ 
/*     */   public void wrapAndThrow(SerializerProvider provider, Throwable t, Object bean, String fieldName)
/*     */     throws IOException
/*     */   {
/* 109 */     while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 110 */       t = t.getCause();
/*     */     }
/*     */ 
/* 113 */     if ((t instanceof Error)) {
/* 114 */       throw ((Error)t);
/*     */     }
/*     */ 
/* 117 */     boolean wrap = (provider == null) || (provider.isEnabled(SerializationConfig.Feature.WRAP_EXCEPTIONS));
/* 118 */     if ((t instanceof IOException)) {
/* 119 */       if ((!wrap) || (!(t instanceof JsonMappingException)))
/* 120 */         throw ((IOException)t);
/*     */     }
/* 122 */     else if ((!wrap) && 
/* 123 */       ((t instanceof RuntimeException))) {
/* 124 */       throw ((RuntimeException)t);
/*     */     }
/*     */ 
/* 128 */     throw JsonMappingException.wrapWithPath(t, bean, fieldName);
/*     */   }
/*     */ 
/*     */   public void wrapAndThrow(SerializerProvider provider, Throwable t, Object bean, int index)
/*     */     throws IOException
/*     */   {
/* 135 */     while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 136 */       t = t.getCause();
/*     */     }
/*     */ 
/* 139 */     if ((t instanceof Error)) {
/* 140 */       throw ((Error)t);
/*     */     }
/*     */ 
/* 143 */     boolean wrap = (provider == null) || (provider.isEnabled(SerializationConfig.Feature.WRAP_EXCEPTIONS));
/* 144 */     if ((t instanceof IOException)) {
/* 145 */       if ((!wrap) || (!(t instanceof JsonMappingException)))
/* 146 */         throw ((IOException)t);
/*     */     }
/* 148 */     else if ((!wrap) && 
/* 149 */       ((t instanceof RuntimeException))) {
/* 150 */       throw ((RuntimeException)t);
/*     */     }
/*     */ 
/* 154 */     throw JsonMappingException.wrapWithPath(t, bean, index);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void wrapAndThrow(Throwable t, Object bean, String fieldName)
/*     */     throws IOException
/*     */   {
/* 162 */     wrapAndThrow(null, t, bean, fieldName);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void wrapAndThrow(Throwable t, Object bean, int index)
/*     */     throws IOException
/*     */   {
/* 170 */     wrapAndThrow(null, t, bean, index);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.SerializerBase
 * JD-Core Version:    0.6.0
 */