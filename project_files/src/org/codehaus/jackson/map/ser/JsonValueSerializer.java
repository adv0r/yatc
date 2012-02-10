/*     */ package org.codehaus.jackson.map.ser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Type;
/*     */ import org.codehaus.jackson.JsonGenerationException;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.JsonSerializer;
/*     */ import org.codehaus.jackson.map.ResolvableSerializer;
/*     */ import org.codehaus.jackson.map.SerializationConfig.Feature;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ import org.codehaus.jackson.map.TypeSerializer;
/*     */ import org.codehaus.jackson.map.annotate.JacksonStdImpl;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.schema.JsonSchema;
/*     */ import org.codehaus.jackson.schema.SchemaAware;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public final class JsonValueSerializer extends SerializerBase<Object>
/*     */   implements ResolvableSerializer, SchemaAware
/*     */ {
/*     */   protected final Method _accessorMethod;
/*     */   protected JsonSerializer<Object> _valueSerializer;
/*     */   protected final BeanProperty _property;
/*     */   protected boolean _forceTypeInformation;
/*     */ 
/*     */   public JsonValueSerializer(Method valueMethod, JsonSerializer<Object> ser, BeanProperty property)
/*     */   {
/*  61 */     super(Object.class);
/*  62 */     this._accessorMethod = valueMethod;
/*  63 */     this._valueSerializer = ser;
/*  64 */     this._property = property;
/*     */   }
/*     */ 
/*     */   public void serialize(Object bean, JsonGenerator jgen, SerializerProvider prov)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*     */     try
/*     */     {
/*  72 */       Object value = this._accessorMethod.invoke(bean, new Object[0]);
/*     */ 
/*  74 */       if (value == null) {
/*  75 */         prov.defaultSerializeNull(jgen);
/*  76 */         return;
/*     */       }
/*  78 */       JsonSerializer ser = this._valueSerializer;
/*  79 */       if (ser == null) {
/*  80 */         Class c = value.getClass();
/*     */ 
/*  86 */         ser = prov.findTypedValueSerializer(c, true, this._property);
/*     */       }
/*  88 */       ser.serialize(value, jgen, prov);
/*     */     } catch (IOException ioe) {
/*  90 */       throw ioe;
/*     */     } catch (Exception e) {
/*  92 */       Throwable t = e;
/*     */ 
/*  94 */       while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/*  95 */         t = t.getCause();
/*     */       }
/*     */ 
/*  98 */       if ((t instanceof Error)) {
/*  99 */         throw ((Error)t);
/*     */       }
/*     */ 
/* 102 */       throw JsonMappingException.wrapWithPath(t, bean, this._accessorMethod.getName() + "()");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void serializeWithType(Object bean, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 112 */     Object value = null;
/*     */     try {
/* 114 */       value = this._accessorMethod.invoke(bean, new Object[0]);
/*     */ 
/* 117 */       if (value == null) {
/* 118 */         provider.defaultSerializeNull(jgen);
/* 119 */         return;
/*     */       }
/* 121 */       JsonSerializer ser = this._valueSerializer;
/* 122 */       if (ser != null)
/*     */       {
/* 126 */         if (this._forceTypeInformation) {
/* 127 */           typeSer.writeTypePrefixForScalar(bean, jgen);
/*     */         }
/* 129 */         ser.serializeWithType(value, jgen, provider, typeSer);
/* 130 */         if (this._forceTypeInformation) {
/* 131 */           typeSer.writeTypeSuffixForScalar(bean, jgen);
/*     */         }
/* 133 */         return;
/*     */       }
/*     */ 
/* 136 */       Class c = value.getClass();
/* 137 */       ser = provider.findTypedValueSerializer(c, true, this._property);
/*     */ 
/* 139 */       ser.serialize(value, jgen, provider);
/*     */     } catch (IOException ioe) {
/* 141 */       throw ioe;
/*     */     } catch (Exception e) {
/* 143 */       Throwable t = e;
/*     */ 
/* 145 */       while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 146 */         t = t.getCause();
/*     */       }
/*     */ 
/* 149 */       if ((t instanceof Error)) {
/* 150 */         throw ((Error)t);
/*     */       }
/*     */ 
/* 153 */       throw JsonMappingException.wrapWithPath(t, bean, this._accessorMethod.getName() + "()");
/*     */     }
/*     */   }
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 161 */     return (this._valueSerializer instanceof SchemaAware) ? ((SchemaAware)this._valueSerializer).getSchema(provider, null) : JsonSchema.getDefaultSchemaNode();
/*     */   }
/*     */ 
/*     */   public void resolve(SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 179 */     if (this._valueSerializer == null)
/*     */     {
/* 185 */       if ((provider.isEnabled(SerializationConfig.Feature.USE_STATIC_TYPING)) || (Modifier.isFinal(this._accessorMethod.getReturnType().getModifiers())))
/*     */       {
/* 187 */         JavaType t = TypeFactory.type(this._accessorMethod.getGenericReturnType());
/*     */ 
/* 193 */         this._valueSerializer = provider.findTypedValueSerializer(t, false, this._property);
/*     */ 
/* 198 */         this._forceTypeInformation = isNaturalTypeWithStdHandling(t, this._valueSerializer);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean isNaturalTypeWithStdHandling(JavaType type, JsonSerializer<?> ser)
/*     */   {
/* 205 */     Class cls = type.getRawClass();
/*     */ 
/* 207 */     if (type.isPrimitive()) {
/* 208 */       if ((cls != Integer.TYPE) && (cls != Boolean.TYPE) && (cls != Double.TYPE)) {
/* 209 */         return false;
/*     */       }
/*     */     }
/* 212 */     else if ((cls != String.class) && (cls != Integer.class) && (cls != Boolean.class) && (cls != Double.class))
/*     */     {
/* 214 */       return false;
/*     */     }
/*     */ 
/* 218 */     return ser.getClass().getAnnotation(JacksonStdImpl.class) != null;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 230 */     return "(@JsonValue serializer for method " + this._accessorMethod.getDeclaringClass() + "#" + this._accessorMethod.getName() + ")";
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.JsonValueSerializer
 * JD-Core Version:    0.6.0
 */