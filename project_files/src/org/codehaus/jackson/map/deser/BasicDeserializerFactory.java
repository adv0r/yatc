/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.codehaus.jackson.JsonNode;
/*     */ import org.codehaus.jackson.map.AnnotationIntrospector;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.BeanProperty.Std;
/*     */ import org.codehaus.jackson.map.DeserializationConfig;
/*     */ import org.codehaus.jackson.map.DeserializationConfig.Feature;
/*     */ import org.codehaus.jackson.map.DeserializerFactory;
/*     */ import org.codehaus.jackson.map.DeserializerFactory.Config;
/*     */ import org.codehaus.jackson.map.DeserializerProvider;
/*     */ import org.codehaus.jackson.map.JsonDeserializer;
/*     */ import org.codehaus.jackson.map.JsonDeserializer.None;
/*     */ import org.codehaus.jackson.map.JsonMappingException;
/*     */ import org.codehaus.jackson.map.KeyDeserializer;
/*     */ import org.codehaus.jackson.map.KeyDeserializer.None;
/*     */ import org.codehaus.jackson.map.TypeDeserializer;
/*     */ import org.codehaus.jackson.map.deser.impl.StringCollectionDeserializer;
/*     */ import org.codehaus.jackson.map.ext.OptionalHandlerFactory;
/*     */ import org.codehaus.jackson.map.introspect.Annotated;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedClass;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedMember;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedMethod;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedParameter;
/*     */ import org.codehaus.jackson.map.introspect.BasicBeanDescription;
/*     */ import org.codehaus.jackson.map.jsontype.SubtypeResolver;
/*     */ import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
/*     */ import org.codehaus.jackson.map.type.ArrayType;
/*     */ import org.codehaus.jackson.map.type.CollectionType;
/*     */ import org.codehaus.jackson.map.type.MapType;
/*     */ import org.codehaus.jackson.map.type.TypeFactory;
/*     */ import org.codehaus.jackson.map.util.ClassUtil;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public abstract class BasicDeserializerFactory extends DeserializerFactory
/*     */ {
/*  34 */   static final JavaType TYPE_STRING = TypeFactory.type(String.class);
/*     */ 
/*  41 */   static final HashMap<JavaType, JsonDeserializer<Object>> _simpleDeserializers = StdDeserializers.constructAll();
/*     */ 
/*  48 */   static final HashMap<String, Class<? extends Map>> _mapFallbacks = new HashMap();
/*     */   static final HashMap<String, Class<? extends Collection>> _collectionFallbacks;
/*     */   protected static final HashMap<JavaType, JsonDeserializer<Object>> _arrayDeserializers;
/* 104 */   protected OptionalHandlerFactory optionalHandlers = OptionalHandlerFactory.instance;
/*     */ 
/*     */   public abstract DeserializerFactory withConfig(DeserializerFactory.Config paramConfig);
/*     */ 
/*     */   protected abstract JsonDeserializer<?> _findCustomArrayDeserializer(ArrayType paramArrayType, DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, BeanProperty paramBeanProperty, TypeDeserializer paramTypeDeserializer, JsonDeserializer<?> paramJsonDeserializer)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   protected abstract JsonDeserializer<?> _findCustomCollectionDeserializer(CollectionType paramCollectionType, DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty, TypeDeserializer paramTypeDeserializer, JsonDeserializer<?> paramJsonDeserializer)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   protected abstract JsonDeserializer<?> _findCustomEnumDeserializer(Class<?> paramClass, DeserializationConfig paramDeserializationConfig, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   protected abstract JsonDeserializer<?> _findCustomMapDeserializer(MapType paramMapType, DeserializationConfig paramDeserializationConfig, DeserializerProvider paramDeserializerProvider, BasicBeanDescription paramBasicBeanDescription, BeanProperty paramBeanProperty, KeyDeserializer paramKeyDeserializer, TypeDeserializer paramTypeDeserializer, JsonDeserializer<?> paramJsonDeserializer)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   protected abstract JsonDeserializer<?> _findCustomTreeNodeDeserializer(Class<? extends JsonNode> paramClass, DeserializationConfig paramDeserializationConfig, BeanProperty paramBeanProperty)
/*     */     throws JsonMappingException;
/*     */ 
/*     */   public JsonDeserializer<?> createArrayDeserializer(DeserializationConfig config, DeserializerProvider p, ArrayType type, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 160 */     JavaType elemType = type.getContentType();
/*     */ 
/* 163 */     JsonDeserializer contentDeser = (JsonDeserializer)elemType.getValueHandler();
/* 164 */     if (contentDeser == null)
/*     */     {
/* 166 */       JsonDeserializer deser = (JsonDeserializer)_arrayDeserializers.get(elemType);
/* 167 */       if (deser != null)
/*     */       {
/* 172 */         JsonDeserializer custom = _findCustomArrayDeserializer(type, config, p, property, null, null);
/* 173 */         if (custom != null) {
/* 174 */           return custom;
/*     */         }
/* 176 */         return deser;
/*     */       }
/*     */ 
/* 179 */       if (elemType.isPrimitive()) {
/* 180 */         throw new IllegalArgumentException("Internal error: primitive type (" + type + ") passed, no array deserializer found");
/*     */       }
/*     */     }
/*     */ 
/* 184 */     TypeDeserializer elemTypeDeser = (TypeDeserializer)elemType.getTypeHandler();
/*     */ 
/* 186 */     if (elemTypeDeser == null) {
/* 187 */       elemTypeDeser = findTypeDeserializer(config, elemType, property);
/*     */     }
/*     */ 
/* 190 */     JsonDeserializer custom = _findCustomArrayDeserializer(type, config, p, property, elemTypeDeser, contentDeser);
/* 191 */     if (custom != null) {
/* 192 */       return custom;
/*     */     }
/*     */ 
/* 195 */     if (contentDeser == null)
/*     */     {
/* 197 */       contentDeser = p.findValueDeserializer(config, elemType, property);
/*     */     }
/* 199 */     return new ArrayDeserializer(type, contentDeser, elemTypeDeser);
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<?> createCollectionDeserializer(DeserializationConfig config, DeserializerProvider p, CollectionType type, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 207 */     Class collectionClass = type.getRawClass();
/* 208 */     BasicBeanDescription beanDesc = (BasicBeanDescription)config.introspectClassAnnotations(collectionClass);
/*     */ 
/* 210 */     JsonDeserializer deser = findDeserializerFromAnnotation(config, beanDesc.getClassInfo(), property);
/* 211 */     if (deser != null) {
/* 212 */       return deser;
/*     */     }
/*     */ 
/* 215 */     type = (CollectionType)modifyTypeByAnnotation(config, beanDesc.getClassInfo(), type, null);
/*     */ 
/* 217 */     JavaType contentType = type.getContentType();
/*     */ 
/* 219 */     JsonDeserializer contentDeser = (JsonDeserializer)contentType.getValueHandler();
/*     */ 
/* 222 */     TypeDeserializer contentTypeDeser = (TypeDeserializer)contentType.getTypeHandler();
/*     */ 
/* 224 */     if (contentTypeDeser == null) {
/* 225 */       contentTypeDeser = findTypeDeserializer(config, contentType, property);
/*     */     }
/*     */ 
/* 229 */     JsonDeserializer custom = _findCustomCollectionDeserializer(type, config, p, beanDesc, property, contentTypeDeser, contentDeser);
/*     */ 
/* 231 */     if (custom != null) {
/* 232 */       return custom;
/*     */     }
/*     */ 
/* 235 */     if (contentDeser == null)
/*     */     {
/* 237 */       if (EnumSet.class.isAssignableFrom(collectionClass)) {
/* 238 */         return new EnumSetDeserializer(constructEnumResolver(contentType.getRawClass(), config));
/*     */       }
/*     */ 
/* 242 */       contentDeser = p.findValueDeserializer(config, contentType, property);
/*     */     }
/*     */ 
/* 254 */     if ((type.isInterface()) || (type.isAbstract()))
/*     */     {
/* 256 */       Class fallback = (Class)_collectionFallbacks.get(collectionClass.getName());
/* 257 */       if (fallback == null) {
/* 258 */         throw new IllegalArgumentException("Can not find a deserializer for non-concrete Collection type " + type);
/*     */       }
/* 260 */       collectionClass = fallback;
/*     */     }
/*     */ 
/* 263 */     boolean fixAccess = config.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
/*     */ 
/* 265 */     Constructor ctor = ClassUtil.findConstructor(collectionClass, fixAccess);
/*     */ 
/* 267 */     if (contentType.getRawClass() == String.class)
/*     */     {
/* 269 */       return new StringCollectionDeserializer(type, contentDeser, ctor);
/*     */     }
/* 271 */     return new CollectionDeserializer(type, contentDeser, contentTypeDeser, ctor);
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<?> createMapDeserializer(DeserializationConfig config, DeserializerProvider p, MapType type, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 279 */     Class mapClass = type.getRawClass();
/*     */ 
/* 281 */     BasicBeanDescription beanDesc = (BasicBeanDescription)config.introspectForCreation(type);
/*     */ 
/* 283 */     JsonDeserializer deser = findDeserializerFromAnnotation(config, beanDesc.getClassInfo(), property);
/* 284 */     if (deser != null) {
/* 285 */       return deser;
/*     */     }
/*     */ 
/* 288 */     type = (MapType)modifyTypeByAnnotation(config, beanDesc.getClassInfo(), type, null);
/*     */ 
/* 290 */     JavaType keyType = type.getKeyType();
/* 291 */     JavaType contentType = type.getContentType();
/*     */ 
/* 295 */     JsonDeserializer contentDeser = (JsonDeserializer)contentType.getValueHandler();
/*     */ 
/* 298 */     KeyDeserializer keyDes = (KeyDeserializer)keyType.getValueHandler();
/* 299 */     if (keyDes == null) {
/* 300 */       keyDes = TYPE_STRING.equals(keyType) ? null : p.findKeyDeserializer(config, keyType, property);
/*     */     }
/*     */ 
/* 303 */     TypeDeserializer contentTypeDeser = (TypeDeserializer)contentType.getTypeHandler();
/*     */ 
/* 305 */     if (contentTypeDeser == null) {
/* 306 */       contentTypeDeser = findTypeDeserializer(config, contentType, property);
/*     */     }
/*     */ 
/* 310 */     JsonDeserializer custom = _findCustomMapDeserializer(type, config, p, beanDesc, property, keyDes, contentTypeDeser, contentDeser);
/*     */ 
/* 312 */     if (custom != null) {
/* 313 */       return custom;
/*     */     }
/*     */ 
/* 316 */     if (contentDeser == null)
/*     */     {
/* 318 */       contentDeser = p.findValueDeserializer(config, contentType, property);
/*     */     }
/*     */ 
/* 323 */     if (EnumMap.class.isAssignableFrom(mapClass)) {
/* 324 */       Class kt = keyType.getRawClass();
/* 325 */       if ((kt == null) || (!kt.isEnum())) {
/* 326 */         throw new IllegalArgumentException("Can not construct EnumMap; generic (key) type not available");
/*     */       }
/* 328 */       return new EnumMapDeserializer(constructEnumResolver(kt, config), contentDeser);
/*     */     }
/*     */ 
/* 342 */     if ((type.isInterface()) || (type.isAbstract()))
/*     */     {
/* 344 */       Class fallback = (Class)_mapFallbacks.get(mapClass.getName());
/* 345 */       if (fallback == null) {
/* 346 */         throw new IllegalArgumentException("Can not find a deserializer for non-concrete Map type " + type);
/*     */       }
/* 348 */       mapClass = fallback;
/* 349 */       type = (MapType)type.forcedNarrowBy(mapClass);
/*     */ 
/* 351 */       beanDesc = (BasicBeanDescription)config.introspectForCreation(type);
/*     */     }
/*     */ 
/* 355 */     boolean fixAccess = config.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
/*     */ 
/* 358 */     Constructor defaultCtor = beanDesc.findDefaultConstructor();
/* 359 */     if ((defaultCtor != null) && 
/* 360 */       (fixAccess)) {
/* 361 */       ClassUtil.checkAndFixAccess(defaultCtor);
/*     */     }
/*     */ 
/* 364 */     MapDeserializer md = new MapDeserializer(type, defaultCtor, keyDes, contentDeser, contentTypeDeser);
/* 365 */     md.setIgnorableProperties(config.getAnnotationIntrospector().findPropertiesToIgnore(beanDesc.getClassInfo()));
/* 366 */     md.setCreators(findMapCreators(config, beanDesc));
/* 367 */     return md;
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<?> createEnumDeserializer(DeserializationConfig config, DeserializerProvider p, JavaType type, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 381 */     BasicBeanDescription beanDesc = (BasicBeanDescription)config.introspectForCreation(type);
/* 382 */     JsonDeserializer des = findDeserializerFromAnnotation(config, beanDesc.getClassInfo(), property);
/* 383 */     if (des != null) {
/* 384 */       return des;
/*     */     }
/* 386 */     Class enumClass = type.getRawClass();
/*     */ 
/* 388 */     JsonDeserializer custom = _findCustomEnumDeserializer(enumClass, config, beanDesc, property);
/* 389 */     if (custom != null) {
/* 390 */       return custom;
/*     */     }
/*     */ 
/* 394 */     for (AnnotatedMethod factory : beanDesc.getFactoryMethods()) {
/* 395 */       if (config.getAnnotationIntrospector().hasCreatorAnnotation(factory)) {
/* 396 */         int argCount = factory.getParameterCount();
/* 397 */         if (argCount == 1) {
/* 398 */           Class returnType = factory.getRawType();
/*     */ 
/* 400 */           if (returnType.isAssignableFrom(enumClass)) {
/* 401 */             return EnumDeserializer.deserializerForCreator(config, enumClass, factory);
/*     */           }
/*     */         }
/* 404 */         throw new IllegalArgumentException("Unsuitable method (" + factory + ") decorated with @JsonCreator (for Enum type " + enumClass.getName() + ")");
/*     */       }
/*     */     }
/*     */ 
/* 408 */     return new EnumDeserializer(constructEnumResolver(enumClass, config));
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<?> createTreeDeserializer(DeserializationConfig config, DeserializerProvider p, JavaType nodeType, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 417 */     Class nodeClass = nodeType.getRawClass();
/*     */ 
/* 419 */     JsonDeserializer custom = _findCustomTreeNodeDeserializer(nodeClass, config, property);
/* 420 */     if (custom != null) {
/* 421 */       return custom;
/*     */     }
/* 423 */     return JsonNodeDeserializer.getDeserializer(nodeClass);
/*     */   }
/*     */ 
/*     */   public JsonDeserializer<Object> createBeanDeserializer(DeserializationConfig config, DeserializerProvider p, JavaType type, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 434 */     JsonDeserializer deser = (JsonDeserializer)_simpleDeserializers.get(type);
/* 435 */     if (deser != null) {
/* 436 */       return deser;
/*     */     }
/*     */ 
/* 439 */     Class cls = type.getRawClass();
/* 440 */     if (AtomicReference.class.isAssignableFrom(cls)) {
/* 441 */       JsonDeserializer d2 = new StdDeserializer.AtomicReferenceDeserializer(type, property);
/* 442 */       return d2;
/*     */     }
/*     */ 
/* 445 */     JsonDeserializer d = this.optionalHandlers.findDeserializer(type, config, p);
/* 446 */     if (d != null) {
/* 447 */       return d;
/*     */     }
/* 449 */     return null;
/*     */   }
/*     */ 
/*     */   public TypeDeserializer findTypeDeserializer(DeserializationConfig config, JavaType baseType, BeanProperty property)
/*     */   {
/* 456 */     Class cls = baseType.getRawClass();
/* 457 */     BasicBeanDescription bean = (BasicBeanDescription)config.introspectClassAnnotations(cls);
/* 458 */     AnnotatedClass ac = bean.getClassInfo();
/* 459 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/* 460 */     TypeResolverBuilder b = ai.findTypeResolver(ac, baseType);
/*     */ 
/* 465 */     Collection subtypes = null;
/* 466 */     if (b == null) {
/* 467 */       b = config.getDefaultTyper(baseType);
/* 468 */       if (b == null)
/* 469 */         return null;
/*     */     }
/*     */     else {
/* 472 */       subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(ac, config, ai);
/*     */     }
/* 474 */     return b.buildTypeDeserializer(baseType, subtypes, property);
/*     */   }
/*     */ 
/*     */   public TypeDeserializer findPropertyTypeDeserializer(DeserializationConfig config, JavaType baseType, AnnotatedMember annotated, BeanProperty property)
/*     */   {
/* 501 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/* 502 */     TypeResolverBuilder b = ai.findPropertyTypeResolver(annotated, baseType);
/*     */ 
/* 505 */     if (b == null) {
/* 506 */       return findTypeDeserializer(config, baseType, property);
/*     */     }
/*     */ 
/* 509 */     Collection subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(annotated, config, ai);
/* 510 */     return b.buildTypeDeserializer(baseType, subtypes, property);
/*     */   }
/*     */ 
/*     */   public TypeDeserializer findPropertyContentTypeDeserializer(DeserializationConfig config, JavaType containerType, AnnotatedMember propertyEntity, BeanProperty property)
/*     */   {
/* 529 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/* 530 */     TypeResolverBuilder b = ai.findPropertyContentTypeResolver(propertyEntity, containerType);
/* 531 */     JavaType contentType = containerType.getContentType();
/*     */ 
/* 533 */     if (b == null) {
/* 534 */       return findTypeDeserializer(config, contentType, property);
/*     */     }
/*     */ 
/* 537 */     Collection subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(propertyEntity, config, ai);
/* 538 */     return b.buildTypeDeserializer(contentType, subtypes, property);
/*     */   }
/*     */ 
/*     */   protected JsonDeserializer<Object> findDeserializerFromAnnotation(DeserializationConfig config, Annotated a, BeanProperty property)
/*     */   {
/* 555 */     Object deserDef = config.getAnnotationIntrospector().findDeserializer(a, property);
/* 556 */     if (deserDef != null) {
/* 557 */       return _constructDeserializer(config, deserDef);
/*     */     }
/* 559 */     return null;
/*     */   }
/*     */ 
/*     */   JsonDeserializer<Object> _constructDeserializer(DeserializationConfig config, Object deserDef)
/*     */   {
/* 565 */     if ((deserDef instanceof JsonDeserializer)) {
/* 566 */       return (JsonDeserializer)deserDef;
/*     */     }
/*     */ 
/* 571 */     if (!(deserDef instanceof Class)) {
/* 572 */       throw new IllegalStateException("AnnotationIntrospector returned deserializer definition of type " + deserDef.getClass().getName() + "; expected type JsonDeserializer or Class<JsonDeserializer> instead");
/*     */     }
/* 574 */     Class cls = (Class)deserDef;
/* 575 */     if (!JsonDeserializer.class.isAssignableFrom(cls)) {
/* 576 */       throw new IllegalStateException("AnnotationIntrospector returned Class " + cls.getName() + "; expected Class<JsonDeserializer>");
/*     */     }
/* 578 */     return (JsonDeserializer)ClassUtil.createInstance(cls, config.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS));
/*     */   }
/*     */ 
/*     */   protected <T extends JavaType> T modifyTypeByAnnotation(DeserializationConfig config, Annotated a, T type, String propName)
/*     */     throws JsonMappingException
/*     */   {
/* 606 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 607 */     Class subclass = intr.findDeserializationType(a, type, propName);
/* 608 */     if (subclass != null) {
/*     */       try {
/* 610 */         type = type.narrowBy(subclass);
/*     */       } catch (IllegalArgumentException iae) {
/* 612 */         throw new JsonMappingException("Failed to narrow type " + type + " with concrete-type annotation (value " + subclass.getName() + "), method '" + a.getName() + "': " + iae.getMessage(), null, iae);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 617 */     if (type.isContainerType()) {
/* 618 */       Class keyClass = intr.findDeserializationKeyType(a, type.getKeyType(), propName);
/* 619 */       if (keyClass != null)
/*     */       {
/* 621 */         if (!(type instanceof MapType))
/* 622 */           throw new JsonMappingException("Illegal key-type annotation: type " + type + " is not a Map type");
/*     */         try
/*     */         {
/* 625 */           type = ((MapType)type).narrowKey(keyClass);
/*     */         } catch (IllegalArgumentException iae) {
/* 627 */           throw new JsonMappingException("Failed to narrow key type " + type + " with key-type annotation (" + keyClass.getName() + "): " + iae.getMessage(), null, iae);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 632 */       Class cc = intr.findDeserializationContentType(a, type.getContentType(), propName);
/* 633 */       if (cc != null) {
/*     */         try {
/* 635 */           type = type.narrowContentsBy(cc);
/*     */         } catch (IllegalArgumentException iae) {
/* 637 */           throw new JsonMappingException("Failed to narrow content type " + type + " with content-type annotation (" + cc.getName() + "): " + iae.getMessage(), null, iae);
/*     */         }
/*     */       }
/*     */     }
/* 641 */     return type;
/*     */   }
/*     */ 
/*     */   protected JavaType resolveType(DeserializationConfig config, BasicBeanDescription beanDesc, JavaType type, AnnotatedMember member, BeanProperty property)
/*     */   {
/* 659 */     if (type.isContainerType()) {
/* 660 */       AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 661 */       boolean canForceAccess = config.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
/* 662 */       JavaType keyType = type.getKeyType();
/* 663 */       if (keyType != null) {
/* 664 */         Class kdClass = intr.findKeyDeserializer(member);
/* 665 */         if ((kdClass != null) && (kdClass != KeyDeserializer.None.class)) {
/* 666 */           KeyDeserializer kd = (KeyDeserializer)ClassUtil.createInstance(kdClass, canForceAccess);
/* 667 */           keyType.setValueHandler(kd);
/*     */         }
/*     */       }
/*     */ 
/* 671 */       Class cdClass = intr.findContentDeserializer(member);
/* 672 */       if ((cdClass != null) && (cdClass != JsonDeserializer.None.class)) {
/* 673 */         JsonDeserializer cd = (JsonDeserializer)ClassUtil.createInstance(cdClass, canForceAccess);
/* 674 */         type.getContentType().setValueHandler(cd);
/*     */       }
/*     */ 
/* 681 */       if ((member instanceof AnnotatedMember)) {
/* 682 */         TypeDeserializer contentTypeDeser = findPropertyContentTypeDeserializer(config, type, member, property);
/*     */ 
/* 684 */         if (contentTypeDeser != null)
/* 685 */           type = type.withContentTypeHandler(contentTypeDeser);
/*     */       }
/*     */     }
/*     */     TypeDeserializer valueTypeDeser;
/*     */     TypeDeserializer valueTypeDeser;
/* 691 */     if ((member instanceof AnnotatedMember)) {
/* 692 */       valueTypeDeser = findPropertyTypeDeserializer(config, type, member, property);
/*     */     }
/*     */     else {
/* 695 */       valueTypeDeser = findTypeDeserializer(config, type, null);
/*     */     }
/* 697 */     if (valueTypeDeser != null) {
/* 698 */       type = type.withTypeHandler(valueTypeDeser);
/*     */     }
/* 700 */     return type;
/*     */   }
/*     */ 
/*     */   protected EnumResolver<?> constructEnumResolver(Class<?> enumClass, DeserializationConfig config)
/*     */   {
/* 706 */     if (config.isEnabled(DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING)) {
/* 707 */       return EnumResolver.constructUnsafeUsingToString(enumClass);
/*     */     }
/* 709 */     return EnumResolver.constructUnsafe(enumClass, config.getAnnotationIntrospector());
/*     */   }
/*     */ 
/*     */   protected CreatorContainer findMapCreators(DeserializationConfig config, BasicBeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 725 */     Class mapClass = beanDesc.getBeanClass();
/* 726 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 727 */     boolean fixAccess = config.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
/* 728 */     CreatorContainer creators = new CreatorContainer(mapClass, fixAccess);
/*     */ 
/* 730 */     for (AnnotatedConstructor ctor : beanDesc.getConstructors()) {
/* 731 */       int argCount = ctor.getParameterCount();
/* 732 */       if ((argCount < 1) || (!intr.hasCreatorAnnotation(ctor)))
/*     */       {
/*     */         continue;
/*     */       }
/* 736 */       SettableBeanProperty[] properties = new SettableBeanProperty[argCount];
/* 737 */       int nameCount = 0;
/* 738 */       for (int i = 0; i < argCount; i++) {
/* 739 */         AnnotatedParameter param = ctor.getParameter(i);
/* 740 */         String name = param == null ? null : intr.findPropertyNameForParam(param);
/*     */ 
/* 742 */         if ((name == null) || (name.length() == 0)) {
/* 743 */           throw new IllegalArgumentException("Parameter #" + i + " of constructor " + ctor + " has no property name annotation: must have for @JsonCreator for a Map type");
/*     */         }
/* 745 */         nameCount++;
/* 746 */         properties[i] = constructCreatorProperty(config, beanDesc, name, i, param);
/*     */       }
/* 748 */       creators.addPropertyConstructor(ctor, properties);
/*     */     }
/*     */ 
/* 752 */     for (AnnotatedMethod factory : beanDesc.getFactoryMethods()) {
/* 753 */       int argCount = factory.getParameterCount();
/* 754 */       if ((argCount < 1) || (!intr.hasCreatorAnnotation(factory)))
/*     */       {
/*     */         continue;
/*     */       }
/* 758 */       SettableBeanProperty[] properties = new SettableBeanProperty[argCount];
/* 759 */       int nameCount = 0;
/* 760 */       for (int i = 0; i < argCount; i++) {
/* 761 */         AnnotatedParameter param = factory.getParameter(i);
/* 762 */         String name = param == null ? null : intr.findPropertyNameForParam(param);
/*     */ 
/* 764 */         if ((name == null) || (name.length() == 0)) {
/* 765 */           throw new IllegalArgumentException("Parameter #" + i + " of factory method " + factory + " has no property name annotation: must have for @JsonCreator for a Map type");
/*     */         }
/* 767 */         nameCount++;
/* 768 */         properties[i] = constructCreatorProperty(config, beanDesc, name, i, param);
/*     */       }
/* 770 */       creators.addPropertyFactory(factory, properties);
/*     */     }
/* 772 */     return creators;
/*     */   }
/*     */ 
/*     */   protected SettableBeanProperty constructCreatorProperty(DeserializationConfig config, BasicBeanDescription beanDesc, String name, int index, AnnotatedParameter param)
/*     */     throws JsonMappingException
/*     */   {
/* 785 */     JavaType t0 = TypeFactory.type(param.getParameterType(), beanDesc.bindingsForBeanType());
/* 786 */     BeanProperty.Std property = new BeanProperty.Std(name, t0, beanDesc.getClassAnnotations(), param);
/* 787 */     JavaType type = resolveType(config, beanDesc, t0, param, property);
/* 788 */     if (type != t0) {
/* 789 */       property = property.withType(type);
/*     */     }
/*     */ 
/* 792 */     JsonDeserializer deser = findDeserializerFromAnnotation(config, param, property);
/*     */ 
/* 794 */     type = modifyTypeByAnnotation(config, param, type, name);
/* 795 */     TypeDeserializer typeDeser = findTypeDeserializer(config, type, property);
/* 796 */     SettableBeanProperty prop = new SettableBeanProperty.CreatorProperty(name, type, typeDeser, beanDesc.getClassAnnotations(), param, index);
/*     */ 
/* 798 */     if (deser != null) {
/* 799 */       prop.setValueDeserializer(deser);
/*     */     }
/* 801 */     return prop;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  51 */     _mapFallbacks.put(Map.class.getName(), LinkedHashMap.class);
/*  52 */     _mapFallbacks.put(ConcurrentMap.class.getName(), ConcurrentHashMap.class);
/*  53 */     _mapFallbacks.put(SortedMap.class.getName(), TreeMap.class);
/*     */ 
/*  60 */     _mapFallbacks.put("java.util.NavigableMap", TreeMap.class);
/*     */     try {
/*  62 */       Class key = Class.forName("java.util.ConcurrentNavigableMap");
/*  63 */       Class value = Class.forName("java.util.ConcurrentSkipListMap");
/*     */ 
/*  65 */       Class mapValue = value;
/*  66 */       _mapFallbacks.put(key.getName(), mapValue);
/*     */     }
/*     */     catch (ClassNotFoundException cnfe)
/*     */     {
/*     */     }
/*     */ 
/*  76 */     _collectionFallbacks = new HashMap();
/*     */ 
/*  79 */     _collectionFallbacks.put(Collection.class.getName(), ArrayList.class);
/*  80 */     _collectionFallbacks.put(List.class.getName(), ArrayList.class);
/*  81 */     _collectionFallbacks.put(Set.class.getName(), HashSet.class);
/*  82 */     _collectionFallbacks.put(SortedSet.class.getName(), TreeSet.class);
/*  83 */     _collectionFallbacks.put(Queue.class.getName(), LinkedList.class);
/*     */ 
/*  90 */     _collectionFallbacks.put("java.util.Deque", LinkedList.class);
/*  91 */     _collectionFallbacks.put("java.util.NavigableSet", TreeSet.class);
/*     */ 
/*  98 */     _arrayDeserializers = ArrayDeserializers.getAll();
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.BasicDeserializerFactory
 * JD-Core Version:    0.6.0
 */