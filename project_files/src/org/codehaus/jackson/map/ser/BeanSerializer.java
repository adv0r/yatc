/*     */ package org.codehaus.jackson.map.ser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collection;
/*     */ import org.codehaus.jackson.JsonGenerationException;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.JsonMappingException.Reference;
/*     */ import org.codehaus.jackson.map.JsonSerializer;
/*     */ import org.codehaus.jackson.map.ResolvableSerializer;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ import org.codehaus.jackson.map.TypeSerializer;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.node.ObjectNode;
/*     */ import org.codehaus.jackson.schema.JsonSchema;
/*     */ import org.codehaus.jackson.schema.SchemaAware;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class BeanSerializer extends SerializerBase<Object>
/*     */   implements ResolvableSerializer, SchemaAware
/*     */ {
/*  32 */   protected static final BeanPropertyWriter[] NO_PROPS = new BeanPropertyWriter[0];
/*     */   protected final BeanPropertyWriter[] _props;
/*     */   protected final BeanPropertyWriter[] _filteredProps;
/*     */   protected final AnyGetterWriter _anyGetterWriter;
/*     */   protected final Object _propertyFilterId;
/*     */ 
/*     */   public BeanSerializer(JavaType type, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties, AnyGetterWriter anyGetterWriter, Object filterId)
/*     */   {
/*  79 */     super(type);
/*  80 */     this._props = properties;
/*  81 */     this._filteredProps = filteredProperties;
/*  82 */     this._anyGetterWriter = anyGetterWriter;
/*  83 */     this._propertyFilterId = filterId;
/*     */   }
/*     */ 
/*     */   public BeanSerializer(Class<?> rawType, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties, AnyGetterWriter anyGetterWriter, Object filterId)
/*     */   {
/*  92 */     super(rawType);
/*  93 */     this._props = properties;
/*  94 */     this._filteredProps = filteredProperties;
/*  95 */     this._anyGetterWriter = anyGetterWriter;
/*  96 */     this._propertyFilterId = filterId;
/*     */   }
/*     */ 
/*     */   protected BeanSerializer(BeanSerializer src)
/*     */   {
/* 106 */     this(src._handledType, src._props, src._filteredProps, src._anyGetterWriter, src._propertyFilterId);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public BeanSerializer(Class<?> type, BeanPropertyWriter[] properties, Object filterId)
/*     */   {
/* 117 */     super(type);
/* 118 */     this._props = properties;
/* 119 */     this._filteredProps = null;
/* 120 */     this._anyGetterWriter = null;
/* 121 */     this._propertyFilterId = filterId;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public BeanSerializer(Class<?> type, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties)
/*     */   {
/* 132 */     super(type);
/* 133 */     this._props = properties;
/* 134 */     this._filteredProps = filteredProperties;
/* 135 */     this._anyGetterWriter = null;
/* 136 */     this._propertyFilterId = null;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public BeanSerializer(Class<?> type, Collection<BeanPropertyWriter> props)
/*     */   {
/* 145 */     this(type, (BeanPropertyWriter[])props.toArray(new BeanPropertyWriter[props.size()]), null, null, null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public BeanSerializer(Class<?> type, BeanPropertyWriter[] writers)
/*     */   {
/* 154 */     this(type, writers, null, null, null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public BeanSerializer withFiltered(BeanPropertyWriter[] filtered)
/*     */   {
/* 174 */     if (getClass() != BeanSerializer.class) {
/* 175 */       throw new IllegalStateException("BeanSerializer.withFiltered() called on base class: sub-classes MUST override method");
/*     */     }
/*     */ 
/* 178 */     if ((filtered == null) && (this._filteredProps == null)) {
/* 179 */       return this;
/*     */     }
/* 181 */     return new BeanSerializer(handledType(), this._props, filtered, this._anyGetterWriter, this._propertyFilterId);
/*     */   }
/*     */ 
/*     */   public static BeanSerializer createDummy(Class<?> forType)
/*     */   {
/* 190 */     return new BeanSerializer(forType, NO_PROPS, null, null, null);
/*     */   }
/*     */ 
/*     */   public final void serialize(Object bean, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 208 */     jgen.writeStartObject();
/* 209 */     if (this._propertyFilterId != null)
/* 210 */       serializeFieldsFiltered(bean, jgen, provider);
/*     */     else {
/* 212 */       serializeFields(bean, jgen, provider);
/*     */     }
/* 214 */     jgen.writeEndObject();
/*     */   }
/*     */ 
/*     */   public void serializeWithType(Object bean, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 222 */     typeSer.writeTypePrefixForObject(bean, jgen);
/* 223 */     if (this._propertyFilterId != null)
/* 224 */       serializeFieldsFiltered(bean, jgen, provider);
/*     */     else {
/* 226 */       serializeFields(bean, jgen, provider);
/*     */     }
/* 228 */     typeSer.writeTypeSuffixForObject(bean, jgen);
/*     */   }
/*     */ 
/*     */   protected void serializeFields(Object bean, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*     */     BeanPropertyWriter[] props;
/*     */     BeanPropertyWriter[] props;
/* 235 */     if ((this._filteredProps != null) && (provider.getSerializationView() != null))
/* 236 */       props = this._filteredProps;
/*     */     else {
/* 238 */       props = this._props;
/*     */     }
/* 240 */     int i = 0;
/*     */     try {
/* 242 */       for (int len = props.length; i < len; i++) {
/* 243 */         BeanPropertyWriter prop = props[i];
/* 244 */         if (prop != null) {
/* 245 */           prop.serializeAsField(bean, jgen, provider);
/*     */         }
/*     */       }
/* 248 */       if (this._anyGetterWriter != null)
/* 249 */         this._anyGetterWriter.getAndSerialize(bean, jgen, provider);
/*     */     }
/*     */     catch (Exception e) {
/* 252 */       String name = i == props.length ? "[anySetter]" : props[i].getName();
/* 253 */       wrapAndThrow(provider, e, bean, name);
/*     */     }
/*     */     catch (StackOverflowError e)
/*     */     {
/* 259 */       JsonMappingException mapE = new JsonMappingException("Infinite recursion (StackOverflowError)");
/* 260 */       String name = i == props.length ? "[anySetter]" : props[i].getName();
/* 261 */       mapE.prependPath(new JsonMappingException.Reference(bean, name));
/* 262 */       throw mapE;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void serializeFieldsFiltered(Object bean, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/*     */     BeanPropertyWriter[] props;
/*     */     BeanPropertyWriter[] props;
/* 281 */     if ((this._filteredProps != null) && (provider.getSerializationView() != null))
/* 282 */       props = this._filteredProps;
/*     */     else {
/* 284 */       props = this._props;
/*     */     }
/* 286 */     BeanPropertyFilter filter = findFilter(provider);
/* 287 */     int i = 0;
/*     */     try {
/* 289 */       for (int len = props.length; i < len; i++) {
/* 290 */         BeanPropertyWriter prop = props[i];
/* 291 */         if (prop != null) {
/* 292 */           filter.serializeAsField(bean, jgen, provider, prop);
/*     */         }
/*     */       }
/* 295 */       if (this._anyGetterWriter != null)
/* 296 */         this._anyGetterWriter.getAndSerialize(bean, jgen, provider);
/*     */     }
/*     */     catch (Exception e) {
/* 299 */       String name = i == props.length ? "[anySetter]" : props[i].getName();
/* 300 */       wrapAndThrow(provider, e, bean, name);
/*     */     } catch (StackOverflowError e) {
/* 302 */       JsonMappingException mapE = new JsonMappingException("Infinite recursion (StackOverflowError)");
/* 303 */       String name = i == props.length ? "[anySetter]" : props[i].getName();
/* 304 */       mapE.prependPath(new JsonMappingException.Reference(bean, name));
/* 305 */       throw mapE;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected BeanPropertyFilter findFilter(SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 318 */     Object filterId = this._propertyFilterId;
/* 319 */     FilterProvider filters = provider.getFilterProvider();
/*     */ 
/* 321 */     if (filters == null) {
/* 322 */       throw new JsonMappingException("Can not resolve BeanPropertyFilter with id '" + filterId + "'; no FilterProvider configured");
/*     */     }
/* 324 */     BeanPropertyFilter filter = filters.findFilter(filterId);
/*     */ 
/* 326 */     if (filter == null) {
/* 327 */       throw new JsonMappingException("No filter configured with id '" + filterId + "' (type " + filterId.getClass().getName() + ")");
/*     */     }
/*     */ 
/* 330 */     return filter;
/*     */   }
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 337 */     ObjectNode o = createSchemaNode("object", true);
/*     */ 
/* 340 */     ObjectNode propertiesNode = o.objectNode();
/* 341 */     for (int i = 0; i < this._props.length; i++) {
/* 342 */       BeanPropertyWriter prop = this._props[i];
/* 343 */       JavaType propType = prop.getSerializationType();
/*     */ 
/* 345 */       Type hint = propType == null ? prop.getGenericPropertyType() : propType.getRawClass();
/*     */ 
/* 347 */       JsonSerializer ser = prop.getSerializer();
/* 348 */       if (ser == null) {
/* 349 */         Class serType = prop.getRawSerializationType();
/* 350 */         if (serType == null) {
/* 351 */           serType = prop.getPropertyType();
/*     */         }
/* 353 */         ser = provider.findValueSerializer(serType, prop);
/*     */       }
/* 355 */       JsonNode schemaNode = (ser instanceof SchemaAware) ? ((SchemaAware)ser).getSchema(provider, hint) : JsonSchema.getDefaultSchemaNode();
/*     */ 
/* 358 */       propertiesNode.put(prop.getName(), schemaNode);
/*     */     }
/* 360 */     o.put("properties", propertiesNode);
/* 361 */     return o;
/*     */   }
/*     */ 
/*     */   public void resolve(SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 374 */     int filteredCount = this._filteredProps == null ? 0 : this._filteredProps.length;
/* 375 */     int i = 0; for (int len = this._props.length; i < len; i++) {
/* 376 */       BeanPropertyWriter prop = this._props[i];
/* 377 */       if (prop.hasSerializer())
/*     */       {
/*     */         continue;
/*     */       }
/* 381 */       JavaType type = prop.getSerializationType();
/*     */ 
/* 387 */       if (type == null) {
/* 388 */         type = TypeFactory.type(prop.getGenericPropertyType());
/* 389 */         if (!type.isFinal())
/*     */         {
/* 395 */           if ((!type.isContainerType()) && (type.containedTypeCount() <= 0)) continue;
/* 396 */           prop.setNonTrivialBaseType(type); continue;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 401 */       JsonSerializer ser = provider.findValueSerializer(type, prop);
/*     */ 
/* 405 */       if (type.isContainerType()) {
/* 406 */         TypeSerializer typeSer = (TypeSerializer)type.getContentType().getTypeHandler();
/* 407 */         if (typeSer != null)
/*     */         {
/* 409 */           if ((ser instanceof ContainerSerializerBase))
/*     */           {
/* 412 */             JsonSerializer ser2 = ((ContainerSerializerBase)ser).withValueTypeSerializer(typeSer);
/* 413 */             ser = ser2;
/*     */           }
/*     */         }
/*     */       }
/* 417 */       prop = prop.withSerializer(ser);
/* 418 */       this._props[i] = prop;
/*     */ 
/* 420 */       if (i < filteredCount) {
/* 421 */         BeanPropertyWriter w2 = this._filteredProps[i];
/* 422 */         if (w2 != null) {
/* 423 */           this._filteredProps[i] = w2.withSerializer(ser);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 429 */     if (this._anyGetterWriter != null)
/* 430 */       this._anyGetterWriter.resolve(provider);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 441 */     return "BeanSerializer for " + handledType().getName();
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.BeanSerializer
 * JD-Core Version:    0.6.0
 */