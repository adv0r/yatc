/*     */ package org.codehaus.jackson.map.ser.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Type;
/*     */ import org.codehaus.jackson.JsonGenerationException;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.JsonSerializer;
/*     */ import org.codehaus.jackson.map.ResolvableSerializer;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ import org.codehaus.jackson.map.TypeSerializer;
/*     */ import org.codehaus.jackson.map.annotate.JacksonStdImpl;
/*     */ import org.codehaus.jackson.map.ser.ArraySerializers.AsArraySerializer;
/*     */ import org.codehaus.jackson.map.ser.ContainerSerializerBase;
/*     */ import org.codehaus.jackson.map.type.ArrayType;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.node.ObjectNode;
/*     */ import org.codehaus.jackson.schema.JsonSchema;
/*     */ import org.codehaus.jackson.schema.SchemaAware;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class ObjectArraySerializer extends ArraySerializers.AsArraySerializer<Object[]>
/*     */   implements ResolvableSerializer
/*     */ {
/*     */   protected final boolean _staticTyping;
/*     */   protected final JavaType _elementType;
/*     */   protected JsonSerializer<Object> _elementSerializer;
/*     */   protected PropertySerializerMap _dynamicSerializers;
/*     */ 
/*     */   public ObjectArraySerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property)
/*     */   {
/*  63 */     super([Ljava.lang.Object.class, vts, property);
/*  64 */     this._elementType = elemType;
/*  65 */     this._staticTyping = staticTyping;
/*  66 */     this._dynamicSerializers = PropertySerializerMap.emptyMap();
/*     */   }
/*     */ 
/*     */   public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */   {
/*  72 */     return new ObjectArraySerializer(this._elementType, this._staticTyping, vts, this._property);
/*     */   }
/*     */ 
/*     */   public void serializeContents(Object[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*  79 */     int len = value.length;
/*  80 */     if (len == 0) {
/*  81 */       return;
/*     */     }
/*  83 */     if (this._elementSerializer != null) {
/*  84 */       serializeContentsUsing(value, jgen, provider, this._elementSerializer);
/*  85 */       return;
/*     */     }
/*  87 */     if (this._valueTypeSerializer != null) {
/*  88 */       serializeTypedContents(value, jgen, provider);
/*  89 */       return;
/*     */     }
/*  91 */     int i = 0;
/*  92 */     Object elem = null;
/*     */     try {
/*  94 */       PropertySerializerMap serializers = this._dynamicSerializers;
/*  95 */       for (; i < len; i++) {
/*  96 */         elem = value[i];
/*  97 */         if (elem == null) {
/*  98 */           provider.defaultSerializeNull(jgen);
/*     */         }
/*     */         else {
/* 101 */           Class cc = elem.getClass();
/* 102 */           JsonSerializer serializer = serializers.serializerFor(cc);
/* 103 */           if (serializer == null) {
/* 104 */             serializer = _findAndAddDynamic(serializers, cc, provider);
/*     */           }
/* 106 */           serializer.serialize(elem, jgen, provider);
/*     */         }
/*     */       }
/*     */     } catch (IOException ioe) {
/* 109 */       throw ioe;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 116 */       Throwable t = e;
/* 117 */       while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 118 */         t = t.getCause();
/*     */       }
/* 120 */       if ((t instanceof Error)) {
/* 121 */         throw ((Error)t);
/*     */       }
/* 123 */       throw JsonMappingException.wrapWithPath(t, elem, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void serializeContentsUsing(Object[] value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 131 */     int len = value.length;
/* 132 */     TypeSerializer typeSer = this._valueTypeSerializer;
/*     */ 
/* 134 */     int i = 0;
/* 135 */     Object elem = null;
/*     */     try {
/* 137 */       for (; i < len; i++) {
/* 138 */         elem = value[i];
/* 139 */         if (elem == null) {
/* 140 */           provider.defaultSerializeNull(jgen);
/*     */         }
/* 143 */         else if (typeSer == null)
/* 144 */           ser.serialize(elem, jgen, provider);
/*     */         else
/* 146 */           ser.serializeWithType(elem, jgen, provider, typeSer);
/*     */       }
/*     */     }
/*     */     catch (IOException ioe) {
/* 150 */       throw ioe;
/*     */     } catch (Exception e) {
/* 152 */       Throwable t = e;
/* 153 */       while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 154 */         t = t.getCause();
/*     */       }
/* 156 */       if ((t instanceof Error)) {
/* 157 */         throw ((Error)t);
/*     */       }
/* 159 */       throw JsonMappingException.wrapWithPath(t, elem, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void serializeTypedContents(Object[] value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 166 */     int len = value.length;
/* 167 */     TypeSerializer typeSer = this._valueTypeSerializer;
/* 168 */     int i = 0;
/* 169 */     Object elem = null;
/*     */     try {
/* 171 */       PropertySerializerMap serializers = this._dynamicSerializers;
/* 172 */       for (; i < len; i++) {
/* 173 */         elem = value[i];
/* 174 */         if (elem == null) {
/* 175 */           provider.defaultSerializeNull(jgen);
/*     */         }
/*     */         else {
/* 178 */           Class cc = elem.getClass();
/* 179 */           JsonSerializer serializer = serializers.serializerFor(cc);
/* 180 */           if (serializer == null) {
/* 181 */             serializer = _findAndAddDynamic(serializers, cc, provider);
/*     */           }
/* 183 */           serializer.serializeWithType(elem, jgen, provider, typeSer);
/*     */         }
/*     */       }
/*     */     } catch (IOException ioe) {
/* 186 */       throw ioe;
/*     */     } catch (Exception e) {
/* 188 */       Throwable t = e;
/* 189 */       while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 190 */         t = t.getCause();
/*     */       }
/* 192 */       if ((t instanceof Error)) {
/* 193 */         throw ((Error)t);
/*     */       }
/* 195 */       throw JsonMappingException.wrapWithPath(t, elem, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 203 */     ObjectNode o = createSchemaNode("array", true);
/* 204 */     if (typeHint != null) {
/* 205 */       JavaType javaType = TypeFactory.type(typeHint);
/* 206 */       if (javaType.isArrayType()) {
/* 207 */         Class componentType = ((ArrayType)javaType).getContentType().getRawClass();
/*     */ 
/* 209 */         if (componentType == Object.class) {
/* 210 */           o.put("items", JsonSchema.getDefaultSchemaNode());
/*     */         } else {
/* 212 */           JsonSerializer ser = provider.findValueSerializer(componentType, this._property);
/* 213 */           JsonNode schemaNode = (ser instanceof SchemaAware) ? ((SchemaAware)ser).getSchema(provider, null) : JsonSchema.getDefaultSchemaNode();
/*     */ 
/* 216 */           o.put("items", schemaNode);
/*     */         }
/*     */       }
/*     */     }
/* 220 */     return o;
/*     */   }
/*     */ 
/*     */   public void resolve(SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 230 */     if (this._staticTyping)
/* 231 */       this._elementSerializer = provider.findValueSerializer(this._elementType, this._property);
/*     */   }
/*     */ 
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 241 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSerializer(type, provider, this._property);
/*     */ 
/* 243 */     if (map != result.map) {
/* 244 */       this._dynamicSerializers = result.map;
/*     */     }
/* 246 */     return result.serializer;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.impl.ObjectArraySerializer
 * JD-Core Version:    0.6.0
 */