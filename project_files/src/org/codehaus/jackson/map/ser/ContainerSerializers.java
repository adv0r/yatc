/*     */ package org.codehaus.jackson.map.ser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ import org.codehaus.jackson.map.ser.impl.PropertySerializerMap;
/*     */ import org.codehaus.jackson.map.ser.impl.PropertySerializerMap.SerializerAndMapResult;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.node.ObjectNode;
/*     */ import org.codehaus.jackson.schema.JsonSchema;
/*     */ import org.codehaus.jackson.schema.SchemaAware;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public final class ContainerSerializers
/*     */ {
/*     */   public static ContainerSerializerBase<?> indexedListSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property)
/*     */   {
/*  41 */     return new IndexedListSerializer(elemType, staticTyping, vts, property);
/*     */   }
/*     */ 
/*     */   public static ContainerSerializerBase<?> collectionSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property)
/*     */   {
/*  47 */     return new CollectionSerializer(elemType, staticTyping, vts, property);
/*     */   }
/*     */ 
/*     */   public static ContainerSerializerBase<?> iteratorSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property)
/*     */   {
/*  53 */     return new IteratorSerializer(elemType, staticTyping, vts, property);
/*     */   }
/*     */ 
/*     */   public static ContainerSerializerBase<?> iterableSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property)
/*     */   {
/*  59 */     return new IterableSerializer(elemType, staticTyping, vts, property);
/*     */   }
/*     */ 
/*     */   public static JsonSerializer<?> enumSetSerializer(JavaType enumType, BeanProperty property)
/*     */   {
/*  64 */     return new EnumSetSerializer(enumType, property);
/*     */   }
/*     */ 
/*     */   public static class EnumSetSerializer extends ContainerSerializers.AsArraySerializer<EnumSet<? extends Enum<?>>>
/*     */   {
/*     */     public EnumSetSerializer(JavaType elemType, BeanProperty property)
/*     */     {
/* 536 */       super(elemType, true, null, property);
/*     */     }
/*     */ 
/*     */     public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 542 */       return this;
/*     */     }
/*     */ 
/*     */     public void serializeContents(EnumSet<? extends Enum<?>> value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 549 */       JsonSerializer enumSer = this._elementSerializer;
/*     */ 
/* 554 */       for (Enum en : value) {
/* 555 */         if (enumSer == null)
/*     */         {
/* 559 */           enumSer = provider.findValueSerializer(en.getDeclaringClass(), this._property);
/*     */         }
/* 561 */         enumSer.serialize(en, jgen, provider);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class IterableSerializer extends ContainerSerializers.AsArraySerializer<Iterable<?>>
/*     */   {
/*     */     public IterableSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property)
/*     */     {
/* 487 */       super(elemType, staticTyping, vts, property);
/*     */     }
/*     */ 
/*     */     public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 492 */       return new IterableSerializer(this._elementType, this._staticTyping, vts, this._property);
/*     */     }
/*     */ 
/*     */     public void serializeContents(Iterable<?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 499 */       Iterator it = value.iterator();
/* 500 */       if (it.hasNext()) {
/* 501 */         TypeSerializer typeSer = this._valueTypeSerializer;
/* 502 */         JsonSerializer prevSerializer = null;
/* 503 */         Class prevClass = null;
/*     */         do
/*     */         {
/* 506 */           Object elem = it.next();
/* 507 */           if (elem == null) {
/* 508 */             provider.defaultSerializeNull(jgen);
/*     */           }
/*     */           else {
/* 511 */             Class cc = elem.getClass();
/*     */             JsonSerializer currSerializer;
/*     */             JsonSerializer currSerializer;
/* 513 */             if (cc == prevClass) {
/* 514 */               currSerializer = prevSerializer;
/*     */             } else {
/* 516 */               currSerializer = provider.findValueSerializer(cc, this._property);
/* 517 */               prevSerializer = currSerializer;
/* 518 */               prevClass = cc;
/*     */             }
/* 520 */             if (typeSer == null)
/* 521 */               currSerializer.serialize(elem, jgen, provider);
/*     */             else
/* 523 */               currSerializer.serializeWithType(elem, jgen, provider, typeSer);
/*     */           }
/*     */         }
/* 526 */         while (it.hasNext());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class IteratorSerializer extends ContainerSerializers.AsArraySerializer<Iterator<?>>
/*     */   {
/*     */     public IteratorSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property)
/*     */     {
/* 439 */       super(elemType, staticTyping, vts, property);
/*     */     }
/*     */ 
/*     */     public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 444 */       return new IteratorSerializer(this._elementType, this._staticTyping, vts, this._property);
/*     */     }
/*     */ 
/*     */     public void serializeContents(Iterator<?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 451 */       if (value.hasNext()) {
/* 452 */         TypeSerializer typeSer = this._valueTypeSerializer;
/* 453 */         JsonSerializer prevSerializer = null;
/* 454 */         Class prevClass = null;
/*     */         do {
/* 456 */           Object elem = value.next();
/* 457 */           if (elem == null) {
/* 458 */             provider.defaultSerializeNull(jgen);
/*     */           }
/*     */           else {
/* 461 */             Class cc = elem.getClass();
/*     */             JsonSerializer currSerializer;
/*     */             JsonSerializer currSerializer;
/* 463 */             if (cc == prevClass) {
/* 464 */               currSerializer = prevSerializer;
/*     */             } else {
/* 466 */               currSerializer = provider.findValueSerializer(cc, this._property);
/* 467 */               prevSerializer = currSerializer;
/* 468 */               prevClass = cc;
/*     */             }
/* 470 */             if (typeSer == null)
/* 471 */               currSerializer.serialize(elem, jgen, provider);
/*     */             else
/* 473 */               currSerializer.serializeWithType(elem, jgen, provider, typeSer);
/*     */           }
/*     */         }
/* 476 */         while (value.hasNext());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class CollectionSerializer extends ContainerSerializers.AsArraySerializer<Collection<?>>
/*     */   {
/*     */     public CollectionSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property)
/*     */     {
/* 353 */       super(elemType, staticTyping, vts, property);
/*     */     }
/*     */ 
/*     */     public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 358 */       return new CollectionSerializer(this._elementType, this._staticTyping, vts, this._property);
/*     */     }
/*     */ 
/*     */     public void serializeContents(Collection<?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 365 */       if (this._elementSerializer != null) {
/* 366 */         serializeContentsUsing(value, jgen, provider, this._elementSerializer);
/* 367 */         return;
/*     */       }
/* 369 */       Iterator it = value.iterator();
/* 370 */       if (!it.hasNext()) {
/* 371 */         return;
/*     */       }
/* 373 */       PropertySerializerMap serializers = this._dynamicSerializers;
/* 374 */       TypeSerializer typeSer = this._valueTypeSerializer;
/*     */ 
/* 376 */       int i = 0;
/*     */       try {
/*     */         do {
/* 379 */           Object elem = it.next();
/* 380 */           if (elem == null) {
/* 381 */             provider.defaultSerializeNull(jgen);
/*     */           } else {
/* 383 */             Class cc = elem.getClass();
/* 384 */             JsonSerializer serializer = serializers.serializerFor(cc);
/* 385 */             if (serializer == null) {
/* 386 */               serializer = _findAndAddDynamic(serializers, cc, provider);
/*     */             }
/* 388 */             if (typeSer == null)
/* 389 */               serializer.serialize(elem, jgen, provider);
/*     */             else {
/* 391 */               serializer.serializeWithType(elem, jgen, provider, typeSer);
/*     */             }
/*     */           }
/* 394 */           i++;
/* 395 */         }while (it.hasNext());
/*     */       }
/*     */       catch (Exception e) {
/* 398 */         wrapAndThrow(provider, e, value, i);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void serializeContentsUsing(Collection<?> value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 406 */       Iterator it = value.iterator();
/* 407 */       if (it.hasNext()) {
/* 408 */         TypeSerializer typeSer = this._valueTypeSerializer;
/* 409 */         int i = 0;
/*     */         do {
/* 411 */           Object elem = it.next();
/*     */           try {
/* 413 */             if (elem == null) {
/* 414 */               provider.defaultSerializeNull(jgen);
/*     */             }
/* 416 */             else if (typeSer == null)
/* 417 */               ser.serialize(elem, jgen, provider);
/*     */             else {
/* 419 */               ser.serializeWithType(elem, jgen, provider, typeSer);
/*     */             }
/*     */ 
/* 422 */             i++;
/*     */           }
/*     */           catch (Exception e) {
/* 425 */             wrapAndThrow(provider, e, value, i);
/*     */           }
/*     */         }
/* 427 */         while (it.hasNext());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class IndexedListSerializer extends ContainerSerializers.AsArraySerializer<List<?>>
/*     */   {
/*     */     public IndexedListSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property)
/*     */     {
/* 236 */       super(elemType, staticTyping, vts, property);
/*     */     }
/*     */ 
/*     */     public ContainerSerializerBase<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 241 */       return new IndexedListSerializer(this._elementType, this._staticTyping, vts, this._property);
/*     */     }
/*     */ 
/*     */     public void serializeContents(List<?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 248 */       if (this._elementSerializer != null) {
/* 249 */         serializeContentsUsing(value, jgen, provider, this._elementSerializer);
/* 250 */         return;
/*     */       }
/* 252 */       if (this._valueTypeSerializer != null) {
/* 253 */         serializeTypedContents(value, jgen, provider);
/* 254 */         return;
/*     */       }
/* 256 */       int len = value.size();
/* 257 */       if (len == 0) {
/* 258 */         return;
/*     */       }
/* 260 */       int i = 0;
/*     */       try {
/* 262 */         PropertySerializerMap serializers = this._dynamicSerializers;
/* 263 */         for (; i < len; i++) {
/* 264 */           Object elem = value.get(i);
/* 265 */           if (elem == null) {
/* 266 */             provider.defaultSerializeNull(jgen);
/*     */           } else {
/* 268 */             Class cc = elem.getClass();
/* 269 */             JsonSerializer serializer = serializers.serializerFor(cc);
/* 270 */             if (serializer == null) {
/* 271 */               serializer = _findAndAddDynamic(serializers, cc, provider);
/*     */             }
/* 273 */             serializer.serialize(elem, jgen, provider);
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception e) {
/* 278 */         wrapAndThrow(provider, e, value, i);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void serializeContentsUsing(List<?> value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 286 */       int len = value.size();
/* 287 */       if (len == 0) {
/* 288 */         return;
/*     */       }
/* 290 */       TypeSerializer typeSer = this._valueTypeSerializer;
/* 291 */       for (int i = 0; i < len; i++) {
/* 292 */         Object elem = value.get(i);
/*     */         try {
/* 294 */           if (elem == null)
/* 295 */             provider.defaultSerializeNull(jgen);
/* 296 */           else if (typeSer == null)
/* 297 */             ser.serialize(elem, jgen, provider);
/*     */           else
/* 299 */             ser.serializeWithType(elem, jgen, provider, typeSer);
/*     */         }
/*     */         catch (Exception e)
/*     */         {
/* 303 */           wrapAndThrow(provider, e, value, i);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public void serializeTypedContents(List<?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 311 */       int len = value.size();
/* 312 */       if (len == 0) {
/* 313 */         return;
/*     */       }
/* 315 */       int i = 0;
/*     */       try {
/* 317 */         TypeSerializer typeSer = this._valueTypeSerializer;
/* 318 */         PropertySerializerMap serializers = this._dynamicSerializers;
/* 319 */         for (; i < len; i++) {
/* 320 */           Object elem = value.get(i);
/* 321 */           if (elem == null) {
/* 322 */             provider.defaultSerializeNull(jgen);
/*     */           } else {
/* 324 */             Class cc = elem.getClass();
/* 325 */             JsonSerializer serializer = serializers.serializerFor(cc);
/* 326 */             if (serializer == null) {
/* 327 */               serializer = _findAndAddDynamic(serializers, cc, provider);
/*     */             }
/* 329 */             serializer.serializeWithType(elem, jgen, provider, typeSer);
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception e) {
/* 334 */         wrapAndThrow(provider, e, value, i);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract class AsArraySerializer<T> extends ContainerSerializerBase<T>
/*     */     implements ResolvableSerializer
/*     */   {
/*     */     protected final boolean _staticTyping;
/*     */     protected final JavaType _elementType;
/*     */     protected final TypeSerializer _valueTypeSerializer;
/*     */     protected JsonSerializer<Object> _elementSerializer;
/*     */     protected final BeanProperty _property;
/*     */     protected PropertySerializerMap _dynamicSerializers;
/*     */ 
/*     */     protected AsArraySerializer(Class<?> cls, JavaType et, boolean staticTyping, TypeSerializer vts, BeanProperty property)
/*     */     {
/* 116 */       super(false);
/* 117 */       this._elementType = et;
/*     */ 
/* 119 */       this._staticTyping = ((staticTyping) || ((et != null) && (et.isFinal())));
/* 120 */       this._valueTypeSerializer = vts;
/* 121 */       this._property = property;
/* 122 */       this._dynamicSerializers = PropertySerializerMap.emptyMap();
/*     */     }
/*     */ 
/*     */     public final void serialize(T value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 129 */       jgen.writeStartArray();
/* 130 */       serializeContents(value, jgen, provider);
/* 131 */       jgen.writeEndArray();
/*     */     }
/*     */ 
/*     */     public final void serializeWithType(T value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/* 139 */       typeSer.writeTypePrefixForArray(value, jgen);
/* 140 */       serializeContents(value, jgen, provider);
/* 141 */       typeSer.writeTypeSuffixForArray(value, jgen);
/*     */     }
/*     */ 
/*     */     protected abstract void serializeContents(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
/*     */       throws IOException, JsonGenerationException;
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 156 */       ObjectNode o = createSchemaNode("array", true);
/* 157 */       JavaType contentType = null;
/* 158 */       if (typeHint != null) {
/* 159 */         JavaType javaType = TypeFactory.type(typeHint);
/* 160 */         contentType = javaType.getContentType();
/* 161 */         if ((contentType == null) && 
/* 162 */           ((typeHint instanceof ParameterizedType))) {
/* 163 */           Type[] typeArgs = ((ParameterizedType)typeHint).getActualTypeArguments();
/* 164 */           if (typeArgs.length == 1) {
/* 165 */             contentType = TypeFactory.type(typeArgs[0]);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 170 */       if ((contentType == null) && (this._elementType != null)) {
/* 171 */         contentType = this._elementType;
/*     */       }
/* 173 */       if (contentType != null) {
/* 174 */         JsonNode schemaNode = null;
/*     */ 
/* 176 */         if (contentType.getRawClass() != Object.class) {
/* 177 */           JsonSerializer ser = provider.findValueSerializer(contentType, this._property);
/* 178 */           if ((ser instanceof SchemaAware)) {
/* 179 */             schemaNode = ((SchemaAware)ser).getSchema(provider, null);
/*     */           }
/*     */         }
/* 182 */         if (schemaNode == null) {
/* 183 */           schemaNode = JsonSchema.getDefaultSchemaNode();
/*     */         }
/* 185 */         o.put("items", schemaNode);
/*     */       }
/* 187 */       return o;
/*     */     }
/*     */ 
/*     */     public void resolve(SerializerProvider provider)
/*     */       throws JsonMappingException
/*     */     {
/* 198 */       if ((this._staticTyping) && (this._elementType != null))
/* 199 */         this._elementSerializer = provider.findValueSerializer(this._elementType, this._property);
/*     */     }
/*     */ 
/*     */     protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider)
/*     */       throws JsonMappingException
/*     */     {
/* 209 */       PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSerializer(type, provider, this._property);
/*     */ 
/* 211 */       if (map != result.map) {
/* 212 */         this._dynamicSerializers = result.map;
/*     */       }
/* 214 */       return result.serializer;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.ContainerSerializers
 * JD-Core Version:    0.6.0
 */