/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.JsonParser;
/*     */ import org.codehaus.jackson.JsonProcessingException;
/*     */ import org.codehaus.jackson.map.AnnotationIntrospector;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.ContextualDeserializer;
/*     */ import org.codehaus.jackson.map.DeserializationConfig;
/*     */ import org.codehaus.jackson.map.DeserializationContext;
/*     */ import org.codehaus.jackson.map.DeserializerFactory;
/*     */ import org.codehaus.jackson.map.DeserializerProvider;
/*     */ import org.codehaus.jackson.map.Deserializers;
/*     */ import org.codehaus.jackson.map.JsonDeserializer;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.KeyDeserializer;
/*     */ import org.codehaus.jackson.map.ResolvableDeserializer;
/*     */ import org.codehaus.jackson.map.TypeDeserializer;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedClass;
/*     */ import org.codehaus.jackson.map.type.ArrayType;
/*     */ import org.codehaus.jackson.map.type.CollectionType;
/*     */ import org.codehaus.jackson.map.type.MapType;
/*     */ import org.codehaus.jackson.map.util.ClassUtil;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class StdDeserializerProvider extends DeserializerProvider
/*     */ {
/*  37 */   static final HashMap<JavaType, KeyDeserializer> _keyDeserializers = StdKeyDeserializers.constructAll();
/*     */ 
/*  49 */   protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _cachedDeserializers = new ConcurrentHashMap(64, 0.75F, 2);
/*     */ 
/*  57 */   protected final HashMap<JavaType, JsonDeserializer<Object>> _incompleteDeserializers = new HashMap(8);
/*     */   protected DeserializerFactory _factory;
/*     */ 
/*     */   public StdDeserializerProvider()
/*     */   {
/*  84 */     this(BeanDeserializerFactory.instance);
/*     */   }
/*     */   public StdDeserializerProvider(DeserializerFactory f) {
/*  87 */     this._factory = f;
/*     */   }
/*     */ 
/*     */   public DeserializerProvider withAdditionalDeserializers(Deserializers d)
/*     */   {
/*  92 */     this._factory = this._factory.withAdditionalDeserializers(d);
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */   public DeserializerProvider withDeserializerModifier(BeanDeserializerModifier modifier)
/*     */   {
/*  98 */     this._factory = this._factory.withDeserializerModifier(modifier);
/*  99 */     return this;
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<Object> findValueDeserializer(DeserializationConfig config, JavaType propertyType, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 114 */     JsonDeserializer deser = _findCachedDeserializer(propertyType);
/* 115 */     if (deser != null)
/*     */     {
/* 117 */       if ((deser instanceof ContextualDeserializer)) {
/* 118 */         JsonDeserializer d = ((ContextualDeserializer)deser).createContextual(config, property);
/* 119 */         deser = d;
/*     */       }
/* 121 */       return deser;
/*     */     }
/*     */ 
/* 124 */     deser = _createAndCacheValueDeserializer(config, propertyType, property);
/* 125 */     if (deser == null)
/*     */     {
/* 130 */       deser = _handleUnknownValueDeserializer(propertyType);
/*     */     }
/*     */ 
/* 133 */     if ((deser instanceof ContextualDeserializer)) {
/* 134 */       JsonDeserializer d = ((ContextualDeserializer)deser).createContextual(config, property);
/* 135 */       deser = d;
/*     */     }
/* 137 */     return deser;
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<Object> findTypedValueDeserializer(DeserializationConfig config, JavaType type, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 145 */     JsonDeserializer deser = findValueDeserializer(config, type, property);
/* 146 */     TypeDeserializer typeDeser = this._factory.findTypeDeserializer(config, type, property);
/* 147 */     if (typeDeser != null) {
/* 148 */       return new WrappedDeserializer(typeDeser, deser);
/*     */     }
/* 150 */     return deser;
/*     */   }
/*     */ 
/*     */   public KeyDeserializer findKeyDeserializer(DeserializationConfig config, JavaType type, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 160 */     Class raw = type.getRawClass();
/* 161 */     if ((raw == String.class) || (raw == Object.class)) {
/* 162 */       return null;
/*     */     }
/*     */ 
/* 165 */     KeyDeserializer kdes = (KeyDeserializer)_keyDeserializers.get(type);
/* 166 */     if (kdes != null) {
/* 167 */       return kdes;
/*     */     }
/*     */ 
/* 170 */     if (type.isEnumType()) {
/* 171 */       return StdKeyDeserializers.constructEnumKeyDeserializer(config, type);
/*     */     }
/*     */ 
/* 174 */     kdes = StdKeyDeserializers.findStringBasedKeyDeserializer(config, type);
/* 175 */     if (kdes != null) {
/* 176 */       return kdes;
/*     */     }
/*     */ 
/* 180 */     return _handleUnknownKeyDeserializer(type);
/*     */   }
/*     */ 
/*     */   public boolean hasValueDeserializerFor(DeserializationConfig config, JavaType type)
/*     */   {
/* 193 */     JsonDeserializer deser = _findCachedDeserializer(type);
/* 194 */     if (deser == null) {
/*     */       try {
/* 196 */         deser = _createAndCacheValueDeserializer(config, type, null);
/*     */       } catch (Exception e) {
/* 198 */         return false;
/*     */       }
/*     */     }
/* 201 */     return deser != null;
/*     */   }
/*     */ 
/*     */   public int cachedDeserializersCount()
/*     */   {
/* 206 */     return this._cachedDeserializers.size();
/*     */   }
/*     */ 
/*     */   public void flushCachedDeserializers()
/*     */   {
/* 220 */     this._cachedDeserializers.clear();
/*     */   }
/*     */ 
/*     */   protected JsonDeserializer<Object> _findCachedDeserializer(JavaType type)
/*     */   {
/* 231 */     return (JsonDeserializer)this._cachedDeserializers.get(type);
/*     */   }
/*     */ 
/*     */   protected JsonDeserializer<Object> _createAndCacheValueDeserializer(DeserializationConfig config, JavaType type, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 250 */     synchronized (this._incompleteDeserializers)
/*     */     {
/* 252 */       JsonDeserializer deser = _findCachedDeserializer(type);
/* 253 */       if (deser != null) {
/* 254 */         return deser;
/*     */       }
/* 256 */       int count = this._incompleteDeserializers.size();
/*     */ 
/* 258 */       if (count > 0) {
/* 259 */         deser = (JsonDeserializer)this._incompleteDeserializers.get(type);
/* 260 */         if (deser != null) {
/* 261 */           return deser;
/*     */         }
/*     */       }
/*     */       try
/*     */       {
/* 266 */         JsonDeserializer localJsonDeserializer1 = _createAndCache2(config, type, property);
/*     */ 
/* 269 */         if ((count == 0) && (this._incompleteDeserializers.size() > 0))
/* 270 */           this._incompleteDeserializers.clear(); return localJsonDeserializer1;
/*     */       }
/*     */       finally
/*     */       {
/* 269 */         if ((count == 0) && (this._incompleteDeserializers.size() > 0))
/* 270 */           this._incompleteDeserializers.clear();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected JsonDeserializer<Object> _createAndCache2(DeserializationConfig config, JavaType type, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*     */     JsonDeserializer deser;
/*     */     try
/*     */     {
/* 286 */       deser = _createDeserializer(config, type, property);
/*     */     }
/*     */     catch (IllegalArgumentException iae)
/*     */     {
/* 291 */       throw new JsonMappingException(iae.getMessage(), null, iae);
/*     */     }
/* 293 */     if (deser == null) {
/* 294 */       return null;
/*     */     }
/*     */ 
/* 300 */     boolean isResolvable = deser instanceof ResolvableDeserializer;
/* 301 */     boolean addToCache = deser.getClass() == BeanDeserializer.class;
/* 302 */     if (!addToCache) {
/* 303 */       AnnotationIntrospector aintr = config.getAnnotationIntrospector();
/*     */ 
/* 305 */       AnnotatedClass ac = AnnotatedClass.construct(deser.getClass(), aintr, null);
/* 306 */       Boolean cacheAnn = aintr.findCachability(ac);
/* 307 */       if (cacheAnn != null) {
/* 308 */         addToCache = cacheAnn.booleanValue();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 323 */     if (isResolvable) {
/* 324 */       this._incompleteDeserializers.put(type, deser);
/* 325 */       _resolveDeserializer(config, (ResolvableDeserializer)deser);
/* 326 */       this._incompleteDeserializers.remove(type);
/*     */     }
/* 328 */     if (addToCache) {
/* 329 */       this._cachedDeserializers.put(type, deser);
/*     */     }
/* 331 */     return deser;
/*     */   }
/*     */ 
/*     */   protected JsonDeserializer<Object> _createDeserializer(DeserializationConfig config, JavaType type, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 342 */     if (type.isEnumType()) {
/* 343 */       return this._factory.createEnumDeserializer(config, this, type, property);
/*     */     }
/* 345 */     if (type.isContainerType()) {
/* 346 */       if ((type instanceof ArrayType)) {
/* 347 */         return this._factory.createArrayDeserializer(config, this, (ArrayType)type, property);
/*     */       }
/*     */ 
/* 350 */       if ((type instanceof MapType)) {
/* 351 */         return this._factory.createMapDeserializer(config, this, (MapType)type, property);
/*     */       }
/*     */ 
/* 354 */       if ((type instanceof CollectionType)) {
/* 355 */         return this._factory.createCollectionDeserializer(config, this, (CollectionType)type, property);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 361 */     if (JsonNode.class.isAssignableFrom(type.getRawClass())) {
/* 362 */       return this._factory.createTreeDeserializer(config, this, type, property);
/*     */     }
/* 364 */     return this._factory.createBeanDeserializer(config, this, type, property);
/*     */   }
/*     */ 
/*     */   protected void _resolveDeserializer(DeserializationConfig config, ResolvableDeserializer ser)
/*     */     throws JsonMappingException
/*     */   {
/* 370 */     ser.resolve(config, this);
/*     */   }
/*     */ 
/*     */   protected JsonDeserializer<Object> _handleUnknownValueDeserializer(JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/* 385 */     Class rawClass = type.getRawClass();
/* 386 */     if (!ClassUtil.isConcrete(rawClass)) {
/* 387 */       throw new JsonMappingException("Can not find a Value deserializer for abstract type " + type);
/*     */     }
/* 389 */     throw new JsonMappingException("Can not find a Value deserializer for type " + type);
/*     */   }
/*     */ 
/*     */   protected KeyDeserializer _handleUnknownKeyDeserializer(JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/* 395 */     throw new JsonMappingException("Can not find a (Map) Key deserializer for type " + type);
/*     */   }
/*     */ 
/*     */   protected static final class WrappedDeserializer extends JsonDeserializer<Object>
/*     */   {
/*     */     final TypeDeserializer _typeDeserializer;
/*     */     final JsonDeserializer<Object> _deserializer;
/*     */ 
/*     */     public WrappedDeserializer(TypeDeserializer typeDeser, JsonDeserializer<Object> deser)
/*     */     {
/* 418 */       this._typeDeserializer = typeDeser;
/* 419 */       this._deserializer = deser;
/*     */     }
/*     */ 
/*     */     public Object deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 426 */       return this._deserializer.deserializeWithType(jp, ctxt, this._typeDeserializer);
/*     */     }
/*     */ 
/*     */     public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */       throws IOException, JsonProcessingException
/*     */     {
/* 435 */       throw new IllegalStateException("Type-wrapped deserializer's deserializeWithType should never get called");
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.StdDeserializerProvider
 * JD-Core Version:    0.6.0
 */