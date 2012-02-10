/*     */ package org.codehaus.jackson.map.ser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.text.DateFormat;
/*     */ import java.util.Date;
/*     */ import org.codehaus.jackson.JsonGenerationException;
/*     */ import org.codehaus.jackson.JsonGenerator;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.ContextualSerializer;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.JsonSerializer;
/*     */ import org.codehaus.jackson.map.ResolvableSerializer;
/*     */ import org.codehaus.jackson.map.SerializationConfig;
/*     */ import org.codehaus.jackson.map.SerializationConfig.Feature;
/*     */ import org.codehaus.jackson.map.SerializerFactory;
/*     */ import org.codehaus.jackson.map.SerializerProvider;
/*     */ import org.codehaus.jackson.map.TypeSerializer;
/*     */ import org.codehaus.jackson.map.ser.impl.ReadOnlyClassToSerializerMap;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.map.util.ClassUtil;
/*     */ import org.codehaus.jackson.map.util.RootNameLookup;
/*     */ import org.codehaus.jackson.node.ObjectNode;
/*     */ import org.codehaus.jackson.schema.JsonSchema;
/*     */ import org.codehaus.jackson.schema.SchemaAware;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class StdSerializerProvider extends SerializerProvider
/*     */ {
/*     */   static final boolean CACHE_UNKNOWN_MAPPINGS = false;
/*  50 */   public static final JsonSerializer<Object> DEFAULT_NULL_KEY_SERIALIZER = new FailingSerializer("Null key for a Map not allowed in Json (use a converting NullKeySerializer?)");
/*     */ 
/*  53 */   public static final JsonSerializer<Object> DEFAULT_KEY_SERIALIZER = new StdKeySerializer();
/*     */ 
/*  55 */   public static final JsonSerializer<Object> DEFAULT_UNKNOWN_SERIALIZER = new SerializerBase()
/*     */   {
/*     */     public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonMappingException
/*     */     {
/*  62 */       if (provider.isEnabled(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS)) {
/*  63 */         failForEmpty(value);
/*     */       }
/*     */ 
/*  66 */       jgen.writeStartObject();
/*  67 */       jgen.writeEndObject();
/*     */     }
/*     */ 
/*     */     public final void serializeWithType(Object value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */       throws IOException, JsonGenerationException
/*     */     {
/*  76 */       if (provider.isEnabled(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS)) {
/*  77 */         failForEmpty(value);
/*     */       }
/*  79 */       typeSer.writeTypePrefixForObject(value, jgen);
/*  80 */       typeSer.writeTypeSuffixForObject(value, jgen);
/*     */     }
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException
/*     */     {
/*  85 */       return null;
/*     */     }
/*     */ 
/*     */     protected void failForEmpty(Object value) throws JsonMappingException
/*     */     {
/*  90 */       throw new JsonMappingException("No serializer found for class " + value.getClass().getName() + " and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS) )");
/*     */     }
/*  55 */   };
/*     */   protected final SerializerFactory _serializerFactory;
/*     */   protected final SerializerCache _serializerCache;
/*     */   protected final RootNameLookup _rootNames;
/* 120 */   protected JsonSerializer<Object> _unknownTypeSerializer = DEFAULT_UNKNOWN_SERIALIZER;
/*     */ 
/* 126 */   protected JsonSerializer<Object> _keySerializer = DEFAULT_KEY_SERIALIZER;
/*     */ 
/* 132 */   protected JsonSerializer<Object> _nullValueSerializer = NullSerializer.instance;
/*     */ 
/* 141 */   protected JsonSerializer<Object> _nullKeySerializer = DEFAULT_NULL_KEY_SERIALIZER;
/*     */   protected final ReadOnlyClassToSerializerMap _knownSerializers;
/*     */   protected DateFormat _dateFormat;
/*     */ 
/*     */   public StdSerializerProvider()
/*     */   {
/* 175 */     super(null);
/* 176 */     this._serializerFactory = null;
/* 177 */     this._serializerCache = new SerializerCache();
/*     */ 
/* 179 */     this._knownSerializers = null;
/* 180 */     this._rootNames = new RootNameLookup();
/*     */   }
/*     */ 
/*     */   protected StdSerializerProvider(SerializationConfig config, StdSerializerProvider src, SerializerFactory f)
/*     */   {
/* 192 */     super(config);
/* 193 */     if (config == null) {
/* 194 */       throw new NullPointerException();
/*     */     }
/* 196 */     this._serializerFactory = f;
/*     */ 
/* 198 */     this._serializerCache = src._serializerCache;
/* 199 */     this._unknownTypeSerializer = src._unknownTypeSerializer;
/* 200 */     this._keySerializer = src._keySerializer;
/* 201 */     this._nullValueSerializer = src._nullValueSerializer;
/* 202 */     this._nullKeySerializer = src._nullKeySerializer;
/* 203 */     this._rootNames = src._rootNames;
/*     */ 
/* 208 */     this._knownSerializers = this._serializerCache.getReadOnlyLookupMap();
/*     */   }
/*     */ 
/*     */   protected StdSerializerProvider createInstance(SerializationConfig config, SerializerFactory jsf)
/*     */   {
/* 217 */     return new StdSerializerProvider(config, this, jsf);
/*     */   }
/*     */ 
/*     */   public final void serializeValue(SerializationConfig config, JsonGenerator jgen, Object value, SerializerFactory jsf)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 231 */     if (jsf == null) {
/* 232 */       throw new IllegalArgumentException("Can not pass null serializerFactory");
/*     */     }
/*     */ 
/* 239 */     StdSerializerProvider inst = createInstance(config, jsf);
/*     */ 
/* 241 */     if (inst.getClass() != getClass()) {
/* 242 */       throw new IllegalStateException("Broken serializer provider: createInstance returned instance of type " + inst.getClass() + "; blueprint of type " + getClass());
/*     */     }
/*     */ 
/* 245 */     inst._serializeValue(jgen, value);
/*     */   }
/*     */ 
/*     */   public final void serializeValue(SerializationConfig config, JsonGenerator jgen, Object value, JavaType rootType, SerializerFactory jsf)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 253 */     if (jsf == null) {
/* 254 */       throw new IllegalArgumentException("Can not pass null serializerFactory");
/*     */     }
/* 256 */     StdSerializerProvider inst = createInstance(config, jsf);
/* 257 */     if (inst.getClass() != getClass()) {
/* 258 */       throw new IllegalStateException("Broken serializer provider: createInstance returned instance of type " + inst.getClass() + "; blueprint of type " + getClass());
/*     */     }
/* 260 */     inst._serializeValue(jgen, value, rootType);
/*     */   }
/*     */ 
/*     */   public JsonSchema generateJsonSchema(Class<?> type, SerializationConfig config, SerializerFactory jsf)
/*     */     throws JsonMappingException
/*     */   {
/* 267 */     if (type == null) {
/* 268 */       throw new IllegalArgumentException("A class must be provided");
/*     */     }
/*     */ 
/* 275 */     StdSerializerProvider inst = createInstance(config, jsf);
/*     */ 
/* 277 */     if (inst.getClass() != getClass()) {
/* 278 */       throw new IllegalStateException("Broken serializer provider: createInstance returned instance of type " + inst.getClass() + "; blueprint of type " + getClass());
/*     */     }
/*     */ 
/* 283 */     JsonSerializer ser = inst.findValueSerializer(type, null);
/* 284 */     JsonNode schemaNode = (ser instanceof SchemaAware) ? ((SchemaAware)ser).getSchema(inst, null) : JsonSchema.getDefaultSchemaNode();
/*     */ 
/* 287 */     if (!(schemaNode instanceof ObjectNode)) {
/* 288 */       throw new IllegalArgumentException("Class " + type.getName() + " would not be serialized as a JSON object and therefore has no schema");
/*     */     }
/*     */ 
/* 292 */     return new JsonSchema((ObjectNode)schemaNode);
/*     */   }
/*     */ 
/*     */   public boolean hasSerializerFor(SerializationConfig config, Class<?> cls, SerializerFactory jsf)
/*     */   {
/* 299 */     return createInstance(config, jsf)._findExplicitUntypedSerializer(cls, null) != null;
/*     */   }
/*     */ 
/*     */   public void setKeySerializer(JsonSerializer<Object> ks)
/*     */   {
/* 310 */     if (ks == null) {
/* 311 */       throw new IllegalArgumentException("Can not pass null JsonSerializer");
/*     */     }
/* 313 */     this._keySerializer = ks;
/*     */   }
/*     */ 
/*     */   public void setNullValueSerializer(JsonSerializer<Object> nvs)
/*     */   {
/* 318 */     if (nvs == null) {
/* 319 */       throw new IllegalArgumentException("Can not pass null JsonSerializer");
/*     */     }
/* 321 */     this._nullValueSerializer = nvs;
/*     */   }
/*     */ 
/*     */   public void setNullKeySerializer(JsonSerializer<Object> nks)
/*     */   {
/* 326 */     if (nks == null) {
/* 327 */       throw new IllegalArgumentException("Can not pass null JsonSerializer");
/*     */     }
/* 329 */     this._nullKeySerializer = nks;
/*     */   }
/*     */ 
/*     */   public int cachedSerializersCount()
/*     */   {
/* 334 */     return this._serializerCache.size();
/*     */   }
/*     */ 
/*     */   public void flushCachedSerializers()
/*     */   {
/* 339 */     this._serializerCache.flush();
/*     */   }
/*     */ 
/*     */   public JsonSerializer<Object> findValueSerializer(Class<?> valueType, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 355 */     JsonSerializer ser = this._knownSerializers.untypedValueSerializer(valueType);
/* 356 */     if (ser == null)
/*     */     {
/* 358 */       ser = this._serializerCache.untypedValueSerializer(valueType);
/* 359 */       if (ser == null)
/*     */       {
/* 361 */         ser = this._serializerCache.untypedValueSerializer(TypeFactory.type(valueType));
/* 362 */         if (ser == null)
/*     */         {
/* 364 */           ser = _createAndCacheUntypedSerializer(valueType, property);
/*     */ 
/* 370 */           if (ser == null) {
/* 371 */             ser = getUnknownTypeSerializer(valueType);
/*     */ 
/* 376 */             return ser;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 381 */     if ((ser instanceof ContextualSerializer)) {
/* 382 */       return ((ContextualSerializer)ser).createContextual(this._config, property);
/*     */     }
/* 384 */     return ser;
/*     */   }
/*     */ 
/*     */   public JsonSerializer<Object> findValueSerializer(JavaType valueType, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 398 */     JsonSerializer ser = this._knownSerializers.untypedValueSerializer(valueType);
/* 399 */     if (ser == null)
/*     */     {
/* 401 */       ser = this._serializerCache.untypedValueSerializer(valueType);
/* 402 */       if (ser == null)
/*     */       {
/* 404 */         ser = _createAndCacheUntypedSerializer(valueType, property);
/*     */ 
/* 410 */         if (ser == null) {
/* 411 */           ser = getUnknownTypeSerializer(valueType.getRawClass());
/*     */ 
/* 416 */           return ser;
/*     */         }
/*     */       }
/*     */     }
/* 420 */     if ((ser instanceof ContextualSerializer)) {
/* 421 */       return ((ContextualSerializer)ser).createContextual(this._config, property);
/*     */     }
/* 423 */     return ser;
/*     */   }
/*     */ 
/*     */   public JsonSerializer<Object> findTypedValueSerializer(Class<?> valueType, boolean cache, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 436 */     JsonSerializer ser = this._knownSerializers.typedValueSerializer(valueType);
/* 437 */     if (ser != null) {
/* 438 */       return ser;
/*     */     }
/*     */ 
/* 441 */     ser = this._serializerCache.typedValueSerializer(valueType);
/* 442 */     if (ser != null) {
/* 443 */       return ser;
/*     */     }
/*     */ 
/* 447 */     ser = findValueSerializer(valueType, property);
/* 448 */     TypeSerializer typeSer = this._serializerFactory.createTypeSerializer(this._config, TypeFactory.type(valueType), property);
/*     */ 
/* 450 */     if (typeSer != null) {
/* 451 */       ser = new WrappedSerializer(typeSer, ser);
/*     */     }
/* 453 */     if (cache) {
/* 454 */       this._serializerCache.addTypedSerializer(valueType, ser);
/*     */     }
/* 456 */     return ser;
/*     */   }
/*     */ 
/*     */   public JsonSerializer<Object> findTypedValueSerializer(JavaType valueType, boolean cache, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 465 */     JsonSerializer ser = this._knownSerializers.typedValueSerializer(valueType);
/* 466 */     if (ser != null) {
/* 467 */       return ser;
/*     */     }
/*     */ 
/* 470 */     ser = this._serializerCache.typedValueSerializer(valueType);
/* 471 */     if (ser != null) {
/* 472 */       return ser;
/*     */     }
/*     */ 
/* 476 */     ser = findValueSerializer(valueType, property);
/* 477 */     TypeSerializer typeSer = this._serializerFactory.createTypeSerializer(this._config, valueType, property);
/* 478 */     if (typeSer != null) {
/* 479 */       ser = new WrappedSerializer(typeSer, ser);
/*     */     }
/* 481 */     if (cache) {
/* 482 */       this._serializerCache.addTypedSerializer(valueType, ser);
/*     */     }
/* 484 */     return ser;
/*     */   }
/*     */ 
/*     */   public JsonSerializer<Object> getKeySerializer(JavaType valueType, BeanProperty property)
/*     */   {
/* 496 */     return this._keySerializer;
/*     */   }
/*     */ 
/*     */   public JsonSerializer<Object> getNullKeySerializer()
/*     */   {
/* 501 */     return this._nullKeySerializer;
/*     */   }
/*     */ 
/*     */   public JsonSerializer<Object> getNullValueSerializer()
/*     */   {
/* 506 */     return this._nullValueSerializer;
/*     */   }
/*     */ 
/*     */   public JsonSerializer<Object> getUnknownTypeSerializer(Class<?> unknownType)
/*     */   {
/* 511 */     return this._unknownTypeSerializer;
/*     */   }
/*     */ 
/*     */   public final void defaultSerializeDateValue(long timestamp, JsonGenerator jgen)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 528 */     if (isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS)) {
/* 529 */       jgen.writeNumber(timestamp);
/*     */     } else {
/* 531 */       if (this._dateFormat == null)
/*     */       {
/* 533 */         this._dateFormat = ((DateFormat)this._config.getDateFormat().clone());
/*     */       }
/* 535 */       jgen.writeString(this._dateFormat.format(new Date(timestamp)));
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void defaultSerializeDateValue(Date date, JsonGenerator jgen)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 544 */     if (isEnabled(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS)) {
/* 545 */       jgen.writeNumber(date.getTime());
/*     */     } else {
/* 547 */       if (this._dateFormat == null) {
/* 548 */         DateFormat blueprint = this._config.getDateFormat();
/*     */ 
/* 550 */         this._dateFormat = ((DateFormat)blueprint.clone());
/*     */       }
/* 552 */       jgen.writeString(this._dateFormat.format(date));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void _serializeValue(JsonGenerator jgen, Object value)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*     */     boolean wrap;
/*     */     JsonSerializer ser;
/*     */     boolean wrap;
/* 572 */     if (value == null) {
/* 573 */       JsonSerializer ser = getNullValueSerializer();
/* 574 */       wrap = false;
/*     */     } else {
/* 576 */       Class cls = value.getClass();
/*     */ 
/* 578 */       ser = findTypedValueSerializer(cls, true, null);
/*     */ 
/* 580 */       wrap = this._config.isEnabled(SerializationConfig.Feature.WRAP_ROOT_VALUE);
/* 581 */       if (wrap) {
/* 582 */         jgen.writeStartObject();
/* 583 */         jgen.writeFieldName(this._rootNames.findRootName(value.getClass(), this._config));
/*     */       }
/*     */     }
/*     */     try {
/* 587 */       ser.serialize(value, jgen, this);
/* 588 */       if (wrap) {
/* 589 */         jgen.writeEndObject();
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 595 */       throw ioe;
/*     */     }
/*     */     catch (Exception e) {
/* 598 */       String msg = e.getMessage();
/* 599 */       if (msg == null) {
/* 600 */         msg = "[no message for " + e.getClass().getName() + "]";
/*     */       }
/* 602 */       throw new JsonMappingException(msg, e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void _serializeValue(JsonGenerator jgen, Object value, JavaType rootType)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*     */     boolean wrap;
/*     */     JsonSerializer ser;
/*     */     boolean wrap;
/* 618 */     if (value == null) {
/* 619 */       JsonSerializer ser = getNullValueSerializer();
/* 620 */       wrap = false;
/*     */     }
/*     */     else {
/* 623 */       if (!rootType.getRawClass().isAssignableFrom(value.getClass())) {
/* 624 */         _reportIncompatibleRootType(value, rootType);
/*     */       }
/*     */ 
/* 627 */       ser = findTypedValueSerializer(rootType, true, null);
/*     */ 
/* 629 */       wrap = this._config.isEnabled(SerializationConfig.Feature.WRAP_ROOT_VALUE);
/* 630 */       if (wrap) {
/* 631 */         jgen.writeStartObject();
/* 632 */         jgen.writeFieldName(this._rootNames.findRootName(rootType, this._config));
/*     */       }
/*     */     }
/*     */     try {
/* 636 */       ser.serialize(value, jgen, this);
/* 637 */       if (wrap)
/* 638 */         jgen.writeEndObject();
/*     */     }
/*     */     catch (IOException ioe) {
/* 641 */       throw ioe;
/*     */     } catch (Exception e) {
/* 643 */       String msg = e.getMessage();
/* 644 */       if (msg == null) {
/* 645 */         msg = "[no message for " + e.getClass().getName() + "]";
/*     */       }
/* 647 */       throw new JsonMappingException(msg, e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void _reportIncompatibleRootType(Object value, JavaType rootType)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 657 */     if (rootType.isPrimitive()) {
/* 658 */       Class wrapperType = ClassUtil.wrapperType(rootType.getRawClass());
/*     */ 
/* 660 */       if (wrapperType.isAssignableFrom(value.getClass())) {
/* 661 */         return;
/*     */       }
/*     */     }
/* 664 */     throw new JsonMappingException("Incompatible types: declared root type (" + rootType + ") vs " + value.getClass().getName());
/*     */   }
/*     */ 
/*     */   protected JsonSerializer<Object> _findExplicitUntypedSerializer(Class<?> runtimeType, BeanProperty property)
/*     */   {
/* 679 */     JsonSerializer ser = this._knownSerializers.untypedValueSerializer(runtimeType);
/* 680 */     if (ser != null) {
/* 681 */       return ser;
/*     */     }
/*     */ 
/* 684 */     ser = this._serializerCache.untypedValueSerializer(runtimeType);
/* 685 */     if (ser != null)
/* 686 */       return ser;
/*     */     try
/*     */     {
/* 689 */       return _createAndCacheUntypedSerializer(runtimeType, property); } catch (Exception e) {
/*     */     }
/* 691 */     return null;
/*     */   }
/*     */ 
/*     */   protected JsonSerializer<Object> _createAndCacheUntypedSerializer(Class<?> type, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*     */     JsonSerializer ser;
/*     */     try
/*     */     {
/* 705 */       ser = _createUntypedSerializer(TypeFactory.type(type), property);
/*     */     }
/*     */     catch (IllegalArgumentException iae)
/*     */     {
/* 710 */       throw new JsonMappingException(iae.getMessage(), null, iae);
/*     */     }
/*     */ 
/* 713 */     if (ser != null) {
/* 714 */       this._serializerCache.addNonTypedSerializer(type, ser);
/*     */ 
/* 718 */       if ((ser instanceof ResolvableSerializer)) {
/* 719 */         _resolveSerializer((ResolvableSerializer)ser);
/*     */       }
/*     */     }
/* 722 */     return ser;
/*     */   }
/*     */ 
/*     */   protected JsonSerializer<Object> _createAndCacheUntypedSerializer(JavaType type, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*     */     JsonSerializer ser;
/*     */     try
/*     */     {
/* 734 */       ser = _createUntypedSerializer(type, property);
/*     */     }
/*     */     catch (IllegalArgumentException iae)
/*     */     {
/* 739 */       throw new JsonMappingException(iae.getMessage(), null, iae);
/*     */     }
/*     */ 
/* 742 */     if (ser != null) {
/* 743 */       this._serializerCache.addNonTypedSerializer(type, ser);
/*     */ 
/* 747 */       if ((ser instanceof ResolvableSerializer)) {
/* 748 */         _resolveSerializer((ResolvableSerializer)ser);
/*     */       }
/*     */     }
/* 751 */     return ser;
/*     */   }
/*     */ 
/*     */   protected JsonSerializer<Object> _createUntypedSerializer(JavaType type, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 764 */     return this._serializerFactory.createSerializer(this._config, type, property);
/*     */   }
/*     */ 
/*     */   protected void _resolveSerializer(ResolvableSerializer ser)
/*     */     throws JsonMappingException
/*     */   {
/* 770 */     ser.resolve(this);
/*     */   }
/*     */ 
/*     */   private static final class WrappedSerializer extends JsonSerializer<Object>
/*     */   {
/*     */     protected final TypeSerializer _typeSerializer;
/*     */     protected final JsonSerializer<Object> _serializer;
/*     */ 
/*     */     public WrappedSerializer(TypeSerializer typeSer, JsonSerializer<Object> ser)
/*     */     {
/* 793 */       this._typeSerializer = typeSer;
/* 794 */       this._serializer = ser;
/*     */     }
/*     */ 
/*     */     public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 801 */       this._serializer.serializeWithType(value, jgen, provider, this._typeSerializer);
/*     */     }
/*     */ 
/*     */     public void serializeWithType(Object value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 812 */       this._serializer.serializeWithType(value, jgen, provider, typeSer);
/*     */     }
/*     */ 
/*     */     public Class<Object> handledType() {
/* 816 */       return Object.class;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.StdSerializerProvider
 * JD-Core Version:    0.6.0
 */