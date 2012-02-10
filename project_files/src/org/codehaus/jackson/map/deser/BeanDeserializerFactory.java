/*      */ package org.codehaus.jackson.map.deser;
/*      */ 
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import org.codehaus.jackson.JsonNode;
/*      */ import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/*      */ import org.codehaus.jackson.map.AbstractTypeResolver;
/*      */ import org.codehaus.jackson.map.AnnotationIntrospector;
/*      */ import org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty;
/*      */ import org.codehaus.jackson.map.BeanProperty;
/*      */ import org.codehaus.jackson.map.BeanProperty.Std;
/*      */ import org.codehaus.jackson.map.DeserializationConfig;
/*      */ import org.codehaus.jackson.map.DeserializationConfig.Feature;
/*      */ import org.codehaus.jackson.map.DeserializerFactory;
/*      */ import org.codehaus.jackson.map.DeserializerFactory.Config;
/*      */ import org.codehaus.jackson.map.DeserializerProvider;
/*      */ import org.codehaus.jackson.map.Deserializers;
/*      */ import org.codehaus.jackson.map.JsonDeserializer;
/*      */ import org.codehaus.jackson.map.JsonMappingException;
/*      */ import org.codehaus.jackson.map.KeyDeserializer;
/*      */ import org.codehaus.jackson.map.TypeDeserializer;
/*      */ import org.codehaus.jackson.map.introspect.AnnotatedClass;
/*      */ import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
/*      */ import org.codehaus.jackson.map.introspect.AnnotatedField;
/*      */ import org.codehaus.jackson.map.introspect.AnnotatedMember;
/*      */ import org.codehaus.jackson.map.introspect.AnnotatedMethod;
/*      */ import org.codehaus.jackson.map.introspect.AnnotatedParameter;
/*      */ import org.codehaus.jackson.map.introspect.BasicBeanDescription;
/*      */ import org.codehaus.jackson.map.introspect.VisibilityChecker;
/*      */ import org.codehaus.jackson.map.type.ArrayType;
/*      */ import org.codehaus.jackson.map.type.CollectionType;
/*      */ import org.codehaus.jackson.map.type.MapType;
/*      */ import org.codehaus.jackson.map.type.TypeFactory;
/*      */ import org.codehaus.jackson.map.util.ArrayBuilders;
/*      */ import org.codehaus.jackson.map.util.ClassUtil;
/*      */ import org.codehaus.jackson.type.JavaType;
/*      */ 
/*      */ public class BeanDeserializerFactory extends BasicDeserializerFactory
/*      */ {
/*   29 */   private static final Class<?>[] INIT_CAUSE_PARAMS = { Throwable.class };
/*      */ 
/*  127 */   public static final BeanDeserializerFactory instance = new BeanDeserializerFactory(null);
/*      */   protected final DeserializerFactory.Config _factoryConfig;
/*      */ 
/*      */   @Deprecated
/*      */   public BeanDeserializerFactory()
/*      */   {
/*  139 */     this(null);
/*      */   }
/*      */ 
/*      */   public BeanDeserializerFactory(DeserializerFactory.Config config)
/*      */   {
/*  146 */     if (config == null) {
/*  147 */       config = new ConfigImpl();
/*      */     }
/*  149 */     this._factoryConfig = config;
/*      */   }
/*      */ 
/*      */   public final DeserializerFactory.Config getConfig()
/*      */   {
/*  154 */     return this._factoryConfig;
/*      */   }
/*      */ 
/*      */   public DeserializerFactory withConfig(DeserializerFactory.Config config)
/*      */   {
/*  167 */     if (this._factoryConfig == config) {
/*  168 */       return this;
/*      */     }
/*      */ 
/*  177 */     if (getClass() != BeanDeserializerFactory.class) {
/*  178 */       throw new IllegalStateException("Subtype of BeanDeserializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalDeserializers': can not instantiate subtype with " + "additional deserializer definitions");
/*      */     }
/*      */ 
/*  182 */     return new BeanDeserializerFactory(config);
/*      */   }
/*      */ 
/*      */   protected JsonDeserializer<?> _findCustomArrayDeserializer(ArrayType type, DeserializationConfig config, DeserializerProvider provider, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
/*      */     throws JsonMappingException
/*      */   {
/*  199 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/*  200 */       JsonDeserializer deser = d.findArrayDeserializer(type, config, provider, property, elementTypeDeserializer, elementDeserializer);
/*      */ 
/*  202 */       if (deser != null) {
/*  203 */         return deser;
/*      */       }
/*      */     }
/*  206 */     return null;
/*      */   }
/*      */ 
/*      */   protected JsonDeserializer<?> _findCustomCollectionDeserializer(CollectionType type, DeserializationConfig config, DeserializerProvider provider, BasicBeanDescription beanDesc, BeanProperty property, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
/*      */     throws JsonMappingException
/*      */   {
/*  216 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/*  217 */       JsonDeserializer deser = d.findCollectionDeserializer(type, config, provider, beanDesc, property, elementTypeDeserializer, elementDeserializer);
/*      */ 
/*  219 */       if (deser != null) {
/*  220 */         return deser;
/*      */       }
/*      */     }
/*  223 */     return null;
/*      */   }
/*      */ 
/*      */   protected JsonDeserializer<?> _findCustomEnumDeserializer(Class<?> type, DeserializationConfig config, BasicBeanDescription beanDesc, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  231 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/*  232 */       JsonDeserializer deser = d.findEnumDeserializer(type, config, beanDesc, property);
/*  233 */       if (deser != null) {
/*  234 */         return deser;
/*      */       }
/*      */     }
/*  237 */     return null;
/*      */   }
/*      */ 
/*      */   protected JsonDeserializer<?> _findCustomMapDeserializer(MapType type, DeserializationConfig config, DeserializerProvider provider, BasicBeanDescription beanDesc, BeanProperty property, KeyDeserializer keyDeserializer, TypeDeserializer elementTypeDeserializer, JsonDeserializer<?> elementDeserializer)
/*      */     throws JsonMappingException
/*      */   {
/*  247 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/*  248 */       JsonDeserializer deser = d.findMapDeserializer(type, config, provider, beanDesc, property, keyDeserializer, elementTypeDeserializer, elementDeserializer);
/*      */ 
/*  250 */       if (deser != null) {
/*  251 */         return deser;
/*      */       }
/*      */     }
/*  254 */     return null;
/*      */   }
/*      */ 
/*      */   protected JsonDeserializer<?> _findCustomTreeNodeDeserializer(Class<? extends JsonNode> type, DeserializationConfig config, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  262 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/*  263 */       JsonDeserializer deser = d.findTreeNodeDeserializer(type, config, property);
/*  264 */       if (deser != null) {
/*  265 */         return deser;
/*      */       }
/*      */     }
/*  268 */     return null;
/*      */   }
/*      */ 
/*      */   protected JsonDeserializer<Object> _findCustomBeanDeserializer(JavaType type, DeserializationConfig config, DeserializerProvider provider, BasicBeanDescription beanDesc, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  277 */     for (Deserializers d : this._factoryConfig.deserializers()) {
/*  278 */       JsonDeserializer deser = d.findBeanDeserializer(type, config, provider, beanDesc, property);
/*  279 */       if (deser != null) {
/*  280 */         return deser;
/*      */       }
/*      */     }
/*  283 */     return null;
/*      */   }
/*      */ 
/*      */   public JsonDeserializer<Object> createBeanDeserializer(DeserializationConfig config, DeserializerProvider p, JavaType type, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  303 */     BasicBeanDescription beanDesc = (BasicBeanDescription)config.introspect(type);
/*  304 */     JsonDeserializer ad = findDeserializerFromAnnotation(config, beanDesc.getClassInfo(), property);
/*  305 */     if (ad != null) {
/*  306 */       return ad;
/*      */     }
/*      */ 
/*  309 */     JavaType newType = modifyTypeByAnnotation(config, beanDesc.getClassInfo(), type, null);
/*  310 */     if (newType.getRawClass() != type.getRawClass()) {
/*  311 */       type = newType;
/*  312 */       beanDesc = (BasicBeanDescription)config.introspect(type);
/*      */     }
/*      */ 
/*  315 */     JsonDeserializer custom = _findCustomBeanDeserializer(type, config, p, beanDesc, property);
/*  316 */     if (custom != null) {
/*  317 */       return custom;
/*      */     }
/*      */ 
/*  323 */     JsonDeserializer deser = super.createBeanDeserializer(config, p, type, property);
/*  324 */     if (deser != null) {
/*  325 */       return deser;
/*      */     }
/*      */ 
/*  328 */     if (!isPotentialBeanType(type.getRawClass())) {
/*  329 */       return null;
/*      */     }
/*      */ 
/*  336 */     if (type.isThrowable()) {
/*  337 */       return buildThrowableDeserializer(config, type, beanDesc, property);
/*      */     }
/*      */ 
/*  344 */     if (type.isAbstract())
/*      */     {
/*  349 */       AbstractTypeResolver res = config.getAbstractTypeResolver();
/*  350 */       if (res != null) {
/*  351 */         AnnotationIntrospector intr = config.getAnnotationIntrospector();
/*  352 */         if (intr.findTypeResolver(beanDesc.getClassInfo(), type) == null) {
/*  353 */           JavaType concrete = res.resolveAbstractType(config, type);
/*  354 */           if (concrete != null)
/*      */           {
/*  358 */             beanDesc = (BasicBeanDescription)config.introspect(concrete);
/*  359 */             return buildBeanDeserializer(config, concrete, beanDesc, property);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  364 */       return new AbstractDeserializer(type);
/*      */     }
/*      */ 
/*  370 */     return buildBeanDeserializer(config, type, beanDesc, property);
/*      */   }
/*      */ 
/*      */   public JsonDeserializer<Object> buildBeanDeserializer(DeserializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  392 */     BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(beanDesc);
/*  393 */     builder.setCreators(findDeserializerCreators(config, beanDesc));
/*      */ 
/*  395 */     addBeanProps(config, beanDesc, builder);
/*      */ 
/*  397 */     addReferenceProperties(config, beanDesc, builder);
/*      */ 
/*  400 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/*  401 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/*  402 */         builder = mod.updateBuilder(config, beanDesc, builder);
/*      */       }
/*      */     }
/*  405 */     JsonDeserializer deserializer = builder.build(property);
/*      */ 
/*  408 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/*  409 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/*  410 */         deserializer = mod.modifyDeserializer(config, beanDesc, deserializer);
/*      */       }
/*      */     }
/*  413 */     return deserializer;
/*      */   }
/*      */ 
/*      */   public JsonDeserializer<Object> buildThrowableDeserializer(DeserializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  423 */     BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(beanDesc);
/*  424 */     builder.setCreators(findDeserializerCreators(config, beanDesc));
/*      */ 
/*  426 */     addBeanProps(config, beanDesc, builder);
/*      */ 
/*  433 */     AnnotatedMethod am = beanDesc.findMethod("initCause", INIT_CAUSE_PARAMS);
/*  434 */     if (am != null) {
/*  435 */       SettableBeanProperty prop = constructSettableProperty(config, beanDesc, "cause", am);
/*  436 */       if (prop != null) {
/*  437 */         builder.addProperty(prop);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  442 */     builder.addIgnorable("localizedMessage");
/*      */ 
/*  446 */     builder.addIgnorable("message");
/*      */ 
/*  449 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/*  450 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/*  451 */         builder = mod.updateBuilder(config, beanDesc, builder);
/*      */       }
/*      */     }
/*  454 */     JsonDeserializer deserializer = builder.build(property);
/*      */ 
/*  459 */     if ((deserializer instanceof BeanDeserializer)) {
/*  460 */       deserializer = new ThrowableDeserializer((BeanDeserializer)deserializer);
/*      */     }
/*      */ 
/*  464 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/*  465 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/*  466 */         deserializer = mod.modifyDeserializer(config, beanDesc, deserializer);
/*      */       }
/*      */     }
/*  469 */     return deserializer;
/*      */   }
/*      */ 
/*      */   protected BeanDeserializerBuilder constructBeanDeserializerBuilder(BasicBeanDescription beanDesc)
/*      */   {
/*  487 */     return new BeanDeserializerBuilder(beanDesc);
/*      */   }
/*      */ 
/*      */   protected CreatorContainer findDeserializerCreators(DeserializationConfig config, BasicBeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/*  500 */     boolean fixAccess = config.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS);
/*  501 */     CreatorContainer creators = new CreatorContainer(beanDesc.getBeanClass(), fixAccess);
/*  502 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/*      */ 
/*  506 */     if (beanDesc.getType().isConcrete()) {
/*  507 */       Constructor defaultCtor = beanDesc.findDefaultConstructor();
/*  508 */       if (defaultCtor != null) {
/*  509 */         if (fixAccess) {
/*  510 */           ClassUtil.checkAndFixAccess(defaultCtor);
/*      */         }
/*      */ 
/*  513 */         creators.setDefaultConstructor(defaultCtor);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  518 */     VisibilityChecker vchecker = config.getDefaultVisibilityChecker();
/*  519 */     if (!config.isEnabled(DeserializationConfig.Feature.AUTO_DETECT_CREATORS)) {
/*  520 */       vchecker = vchecker.withCreatorVisibility(JsonAutoDetect.Visibility.NONE);
/*      */     }
/*  522 */     vchecker = config.getAnnotationIntrospector().findAutoDetectVisibility(beanDesc.getClassInfo(), vchecker);
/*      */ 
/*  524 */     _addDeserializerConstructors(config, beanDesc, vchecker, intr, creators);
/*  525 */     _addDeserializerFactoryMethods(config, beanDesc, vchecker, intr, creators);
/*  526 */     return creators;
/*      */   }
/*      */ 
/*      */   protected void _addDeserializerConstructors(DeserializationConfig config, BasicBeanDescription beanDesc, VisibilityChecker<?> vchecker, AnnotationIntrospector intr, CreatorContainer creators)
/*      */     throws JsonMappingException
/*      */   {
/*  534 */     for (AnnotatedConstructor ctor : beanDesc.getConstructors()) {
/*  535 */       int argCount = ctor.getParameterCount();
/*  536 */       if (argCount < 1) {
/*      */         continue;
/*      */       }
/*  539 */       boolean isCreator = intr.hasCreatorAnnotation(ctor);
/*      */ 
/*  541 */       if (argCount == 1)
/*      */       {
/*  545 */         String name = intr.findPropertyNameForParam(ctor.getParameter(0));
/*  546 */         if ((name == null) || (name.length() == 0)) {
/*  547 */           Class type = ctor.getParameterClass(0);
/*  548 */           if (type == String.class) {
/*  549 */             if ((isCreator) || (vchecker.isCreatorVisible(ctor))) {
/*  550 */               creators.addStringConstructor(ctor); continue;
/*      */             }
/*      */           }
/*      */ 
/*  554 */           if ((type == Integer.TYPE) || (type == Integer.class)) {
/*  555 */             if ((isCreator) || (vchecker.isCreatorVisible(ctor))) {
/*  556 */               creators.addIntConstructor(ctor); continue;
/*      */             }
/*      */           }
/*      */ 
/*  560 */           if ((type == Long.TYPE) || (type == Long.class)) {
/*  561 */             if ((isCreator) || (vchecker.isCreatorVisible(ctor))) {
/*  562 */               creators.addLongConstructor(ctor); continue;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  567 */           if (intr.hasCreatorAnnotation(ctor)) {
/*  568 */             creators.addDelegatingConstructor(ctor); continue;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  576 */         if (!intr.hasCreatorAnnotation(ctor))
/*      */         {
/*      */           continue;
/*      */         }
/*      */       }
/*      */ 
/*  582 */       SettableBeanProperty[] properties = new SettableBeanProperty[argCount];
/*  583 */       for (int i = 0; i < argCount; i++) {
/*  584 */         AnnotatedParameter param = ctor.getParameter(i);
/*  585 */         String name = param == null ? null : intr.findPropertyNameForParam(param);
/*      */ 
/*  587 */         if ((name == null) || (name.length() == 0)) {
/*  588 */           throw new IllegalArgumentException("Argument #" + i + " of constructor " + ctor + " has no property name annotation; must have name when multiple-paramater constructor annotated as Creator");
/*      */         }
/*  590 */         properties[i] = constructCreatorProperty(config, beanDesc, name, i, param);
/*      */       }
/*  592 */       creators.addPropertyConstructor(ctor, properties);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void _addDeserializerFactoryMethods(DeserializationConfig config, BasicBeanDescription beanDesc, VisibilityChecker<?> vchecker, AnnotationIntrospector intr, CreatorContainer creators)
/*      */     throws JsonMappingException
/*      */   {
/*  602 */     for (AnnotatedMethod factory : beanDesc.getFactoryMethods()) {
/*  603 */       int argCount = factory.getParameterCount();
/*  604 */       if (argCount < 1) {
/*      */         continue;
/*      */       }
/*  607 */       boolean isCreator = intr.hasCreatorAnnotation(factory);
/*      */ 
/*  609 */       if (argCount == 1)
/*      */       {
/*  613 */         String name = intr.findPropertyNameForParam(factory.getParameter(0));
/*  614 */         if ((name == null) || (name.length() == 0)) {
/*  615 */           Class type = factory.getParameterClass(0);
/*  616 */           if (type == String.class) {
/*  617 */             if ((isCreator) || (vchecker.isCreatorVisible(factory))) {
/*  618 */               creators.addStringFactory(factory); continue;
/*      */             }
/*      */           }
/*      */ 
/*  622 */           if ((type == Integer.TYPE) || (type == Integer.class)) {
/*  623 */             if ((isCreator) || (vchecker.isCreatorVisible(factory))) {
/*  624 */               creators.addIntFactory(factory); continue;
/*      */             }
/*      */           }
/*      */ 
/*  628 */           if ((type == Long.TYPE) || (type == Long.class)) {
/*  629 */             if ((isCreator) || (vchecker.isCreatorVisible(factory))) {
/*  630 */               creators.addLongFactory(factory); continue;
/*      */             }
/*      */           }
/*      */ 
/*  634 */           if (intr.hasCreatorAnnotation(factory)) {
/*  635 */             creators.addDelegatingFactory(factory); continue;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  643 */         if (!intr.hasCreatorAnnotation(factory))
/*      */         {
/*      */           continue;
/*      */         }
/*      */       }
/*  648 */       SettableBeanProperty[] properties = new SettableBeanProperty[argCount];
/*  649 */       for (int i = 0; i < argCount; i++) {
/*  650 */         AnnotatedParameter param = factory.getParameter(i);
/*  651 */         String name = intr.findPropertyNameForParam(param);
/*      */ 
/*  653 */         if ((name == null) || (name.length() == 0)) {
/*  654 */           throw new IllegalArgumentException("Argument #" + i + " of factory method " + factory + " has no property name annotation; must have when multiple-paramater static method annotated as Creator");
/*      */         }
/*  656 */         properties[i] = constructCreatorProperty(config, beanDesc, name, i, param);
/*      */       }
/*  658 */       creators.addPropertyFactory(factory, properties);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void addBeanProps(DeserializationConfig config, BasicBeanDescription beanDesc, BeanDeserializerBuilder builder)
/*      */     throws JsonMappingException
/*      */   {
/*  674 */     VisibilityChecker vchecker = config.getDefaultVisibilityChecker();
/*      */ 
/*  676 */     if (!config.isEnabled(DeserializationConfig.Feature.AUTO_DETECT_SETTERS)) {
/*  677 */       vchecker = vchecker.withSetterVisibility(JsonAutoDetect.Visibility.NONE);
/*      */     }
/*  679 */     if (!config.isEnabled(DeserializationConfig.Feature.AUTO_DETECT_FIELDS)) {
/*  680 */       vchecker = vchecker.withFieldVisibility(JsonAutoDetect.Visibility.NONE);
/*      */     }
/*      */ 
/*  683 */     vchecker = config.getAnnotationIntrospector().findAutoDetectVisibility(beanDesc.getClassInfo(), vchecker);
/*      */ 
/*  685 */     Map setters = beanDesc.findSetters(vchecker);
/*      */ 
/*  687 */     AnnotatedMethod anySetter = beanDesc.findAnySetter();
/*      */ 
/*  690 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/*  691 */     boolean ignoreAny = false;
/*      */ 
/*  693 */     Boolean B = intr.findIgnoreUnknownProperties(beanDesc.getClassInfo());
/*  694 */     if (B != null) {
/*  695 */       ignoreAny = B.booleanValue();
/*  696 */       builder.setIgnoreUnknownProperties(ignoreAny);
/*      */     }
/*      */ 
/*  700 */     HashSet ignored = ArrayBuilders.arrayToSet(intr.findPropertiesToIgnore(beanDesc.getClassInfo()));
/*      */ 
/*  707 */     for (String propName : ignored) {
/*  708 */       builder.addIgnorable(propName);
/*      */     }
/*      */ 
/*  711 */     AnnotatedClass ac = beanDesc.getClassInfo();
/*  712 */     for (AnnotatedMethod am : ac.ignoredMemberMethods()) {
/*  713 */       String name = beanDesc.okNameForSetter(am);
/*  714 */       if (name != null) {
/*  715 */         builder.addIgnorable(name);
/*      */       }
/*      */     }
/*  718 */     for (AnnotatedField af : ac.ignoredFields()) {
/*  719 */       builder.addIgnorable(af.getName());
/*      */     }
/*      */ 
/*  723 */     HashMap ignoredTypes = new HashMap();
/*      */ 
/*  726 */     for (Map.Entry en : setters.entrySet()) {
/*  727 */       String name = (String)en.getKey();
/*  728 */       if (!ignored.contains(name)) {
/*  729 */         AnnotatedMethod setter = (AnnotatedMethod)en.getValue();
/*      */ 
/*  731 */         Class type = setter.getParameterClass(0);
/*  732 */         if (isIgnorableType(config, beanDesc, type, ignoredTypes))
/*      */         {
/*  734 */           builder.addIgnorable(name);
/*  735 */           continue;
/*      */         }
/*  737 */         SettableBeanProperty prop = constructSettableProperty(config, beanDesc, name, setter);
/*  738 */         if (prop != null) {
/*  739 */           builder.addProperty(prop);
/*      */         }
/*      */       }
/*      */     }
/*  743 */     if (anySetter != null) {
/*  744 */       builder.setAnySetter(constructAnySetter(config, beanDesc, anySetter));
/*      */     }
/*      */ 
/*  747 */     HashSet addedProps = new HashSet(setters.keySet());
/*      */ 
/*  752 */     LinkedHashMap fieldsByProp = beanDesc.findDeserializableFields(vchecker, addedProps);
/*  753 */     for (Map.Entry en : fieldsByProp.entrySet()) {
/*  754 */       String name = (String)en.getKey();
/*  755 */       if ((!ignored.contains(name)) && (!builder.hasProperty(name))) {
/*  756 */         AnnotatedField field = (AnnotatedField)en.getValue();
/*      */ 
/*  758 */         Class type = field.getRawType();
/*  759 */         if (isIgnorableType(config, beanDesc, type, ignoredTypes))
/*      */         {
/*  761 */           builder.addIgnorable(name);
/*  762 */           continue;
/*      */         }
/*  764 */         SettableBeanProperty prop = constructSettableProperty(config, beanDesc, name, field);
/*  765 */         if (prop != null) {
/*  766 */           builder.addProperty(prop);
/*  767 */           addedProps.add(name);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  778 */     if (config.isEnabled(DeserializationConfig.Feature.USE_GETTERS_AS_SETTERS))
/*      */     {
/*  784 */       Map getters = beanDesc.findGetters(vchecker, addedProps);
/*      */ 
/*  786 */       for (Map.Entry en : getters.entrySet()) {
/*  787 */         AnnotatedMethod getter = (AnnotatedMethod)en.getValue();
/*      */ 
/*  789 */         Class rt = getter.getRawType();
/*  790 */         if ((!Collection.class.isAssignableFrom(rt)) && (!Map.class.isAssignableFrom(rt))) {
/*      */           continue;
/*      */         }
/*  793 */         String name = (String)en.getKey();
/*  794 */         if ((!ignored.contains(name)) && (!builder.hasProperty(name))) {
/*  795 */           builder.addProperty(constructSetterlessProperty(config, beanDesc, name, getter));
/*  796 */           addedProps.add(name);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void addReferenceProperties(DeserializationConfig config, BasicBeanDescription beanDesc, BeanDeserializerBuilder builder)
/*      */     throws JsonMappingException
/*      */   {
/*  814 */     Map refs = beanDesc.findBackReferenceProperties();
/*  815 */     if (refs != null)
/*  816 */       for (Map.Entry en : refs.entrySet()) {
/*  817 */         String name = (String)en.getKey();
/*  818 */         AnnotatedMember m = (AnnotatedMember)en.getValue();
/*  819 */         if ((m instanceof AnnotatedMethod)) {
/*  820 */           builder.addBackReferenceProperty(name, constructSettableProperty(config, beanDesc, m.getName(), (AnnotatedMethod)m));
/*      */         }
/*      */         else
/*  823 */           builder.addBackReferenceProperty(name, constructSettableProperty(config, beanDesc, m.getName(), (AnnotatedField)m));
/*      */       }
/*      */   }
/*      */ 
/*      */   protected SettableAnyProperty constructAnySetter(DeserializationConfig config, BasicBeanDescription beanDesc, AnnotatedMethod setter)
/*      */     throws JsonMappingException
/*      */   {
/*  839 */     if (config.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
/*  840 */       setter.fixAccess();
/*      */     }
/*      */ 
/*  843 */     JavaType type = TypeFactory.type(setter.getParameterType(1), beanDesc.bindingsForBeanType());
/*  844 */     BeanProperty.Std property = new BeanProperty.Std(setter.getName(), type, beanDesc.getClassAnnotations(), setter);
/*  845 */     type = resolveType(config, beanDesc, type, setter, property);
/*      */ 
/*  852 */     JsonDeserializer deser = findDeserializerFromAnnotation(config, setter, property);
/*  853 */     if (deser != null) {
/*  854 */       SettableAnyProperty prop = new SettableAnyProperty(property, setter, type);
/*  855 */       prop.setValueDeserializer(deser);
/*  856 */       return prop;
/*      */     }
/*      */ 
/*  861 */     type = modifyTypeByAnnotation(config, setter, type, property.getName());
/*  862 */     return new SettableAnyProperty(property, setter, type);
/*      */   }
/*      */ 
/*      */   protected SettableBeanProperty constructSettableProperty(DeserializationConfig config, BasicBeanDescription beanDesc, String name, AnnotatedMethod setter)
/*      */     throws JsonMappingException
/*      */   {
/*  881 */     if (config.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
/*  882 */       setter.fixAccess();
/*      */     }
/*      */ 
/*  886 */     JavaType t0 = TypeFactory.type(setter.getParameterType(0), beanDesc.bindingsForBeanType());
/*  887 */     BeanProperty.Std property = new BeanProperty.Std(name, t0, beanDesc.getClassAnnotations(), setter);
/*  888 */     JavaType type = resolveType(config, beanDesc, t0, setter, property);
/*      */ 
/*  890 */     if (type != t0) {
/*  891 */       property = property.withType(type);
/*      */     }
/*      */ 
/*  897 */     JsonDeserializer propDeser = findDeserializerFromAnnotation(config, setter, property);
/*  898 */     type = modifyTypeByAnnotation(config, setter, type, name);
/*  899 */     TypeDeserializer typeDeser = (TypeDeserializer)type.getTypeHandler();
/*  900 */     SettableBeanProperty prop = new SettableBeanProperty.MethodProperty(name, type, typeDeser, beanDesc.getClassAnnotations(), setter);
/*      */ 
/*  902 */     if (propDeser != null) {
/*  903 */       prop.setValueDeserializer(propDeser);
/*      */     }
/*      */ 
/*  906 */     AnnotationIntrospector.ReferenceProperty ref = config.getAnnotationIntrospector().findReferenceType(setter);
/*  907 */     if ((ref != null) && (ref.isManagedReference())) {
/*  908 */       prop.setManagedReferenceName(ref.getName());
/*      */     }
/*  910 */     return prop;
/*      */   }
/*      */ 
/*      */   protected SettableBeanProperty constructSettableProperty(DeserializationConfig config, BasicBeanDescription beanDesc, String name, AnnotatedField field)
/*      */     throws JsonMappingException
/*      */   {
/*  918 */     if (config.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
/*  919 */       field.fixAccess();
/*      */     }
/*  921 */     JavaType t0 = TypeFactory.type(field.getGenericType(), beanDesc.bindingsForBeanType());
/*  922 */     BeanProperty.Std property = new BeanProperty.Std(name, t0, beanDesc.getClassAnnotations(), field);
/*  923 */     JavaType type = resolveType(config, beanDesc, t0, field, property);
/*      */ 
/*  925 */     if (type != t0) {
/*  926 */       property = property.withType(type);
/*      */     }
/*      */ 
/*  931 */     JsonDeserializer propDeser = findDeserializerFromAnnotation(config, field, property);
/*  932 */     type = modifyTypeByAnnotation(config, field, type, name);
/*  933 */     TypeDeserializer typeDeser = (TypeDeserializer)type.getTypeHandler();
/*  934 */     SettableBeanProperty prop = new SettableBeanProperty.FieldProperty(name, type, typeDeser, beanDesc.getClassAnnotations(), field);
/*      */ 
/*  936 */     if (propDeser != null) {
/*  937 */       prop.setValueDeserializer(propDeser);
/*      */     }
/*      */ 
/*  940 */     AnnotationIntrospector.ReferenceProperty ref = config.getAnnotationIntrospector().findReferenceType(field);
/*  941 */     if ((ref != null) && (ref.isManagedReference())) {
/*  942 */       prop.setManagedReferenceName(ref.getName());
/*      */     }
/*  944 */     return prop;
/*      */   }
/*      */ 
/*      */   protected SettableBeanProperty constructSetterlessProperty(DeserializationConfig config, BasicBeanDescription beanDesc, String name, AnnotatedMethod getter)
/*      */     throws JsonMappingException
/*      */   {
/*  959 */     if (config.isEnabled(DeserializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
/*  960 */       getter.fixAccess();
/*      */     }
/*      */ 
/*  963 */     JavaType type = getter.getType(beanDesc.bindingsForBeanType());
/*      */ 
/*  967 */     BeanProperty.Std property = new BeanProperty.Std(name, type, beanDesc.getClassAnnotations(), getter);
/*      */ 
/*  969 */     JsonDeserializer propDeser = findDeserializerFromAnnotation(config, getter, property);
/*  970 */     type = modifyTypeByAnnotation(config, getter, type, name);
/*  971 */     TypeDeserializer typeDeser = (TypeDeserializer)type.getTypeHandler();
/*  972 */     SettableBeanProperty prop = new SettableBeanProperty.SetterlessProperty(name, type, typeDeser, beanDesc.getClassAnnotations(), getter);
/*      */ 
/*  974 */     if (propDeser != null) {
/*  975 */       prop.setValueDeserializer(propDeser);
/*      */     }
/*  977 */     return prop;
/*      */   }
/*      */ 
/*      */   protected boolean isPotentialBeanType(Class<?> type)
/*      */   {
/*  996 */     String typeStr = ClassUtil.canBeABeanType(type);
/*  997 */     if (typeStr != null) {
/*  998 */       throw new IllegalArgumentException("Can not deserialize Class " + type.getName() + " (of type " + typeStr + ") as a Bean");
/*      */     }
/* 1000 */     if (ClassUtil.isProxyType(type)) {
/* 1001 */       throw new IllegalArgumentException("Can not deserialize Proxy class " + type.getName() + " as a Bean");
/*      */     }
/*      */ 
/* 1004 */     typeStr = ClassUtil.isLocalType(type);
/* 1005 */     if (typeStr != null) {
/* 1006 */       throw new IllegalArgumentException("Can not deserialize Class " + type.getName() + " (of type " + typeStr + ") as a Bean");
/*      */     }
/* 1008 */     return true;
/*      */   }
/*      */ 
/*      */   protected boolean isIgnorableType(DeserializationConfig config, BasicBeanDescription beanDesc, Class<?> type, Map<Class<?>, Boolean> ignoredTypes)
/*      */   {
/* 1018 */     Boolean status = (Boolean)ignoredTypes.get(type);
/* 1019 */     if (status == null) {
/* 1020 */       BasicBeanDescription desc = (BasicBeanDescription)config.introspectClassAnnotations(type);
/* 1021 */       status = config.getAnnotationIntrospector().isIgnorableType(desc.getClassInfo());
/*      */ 
/* 1023 */       if (status == null) {
/* 1024 */         status = Boolean.FALSE;
/*      */       }
/*      */     }
/* 1027 */     return status.booleanValue();
/*      */   }
/*      */ 
/*      */   public static class ConfigImpl extends DeserializerFactory.Config
/*      */   {
/*   44 */     protected static final BeanDeserializerModifier[] NO_MODIFIERS = new BeanDeserializerModifier[0];
/*      */     protected final Deserializers[] _additionalDeserializers;
/*      */     protected final BeanDeserializerModifier[] _modifiers;
/*      */ 
/*      */     public ConfigImpl()
/*      */     {
/*   65 */       this(null, null);
/*      */     }
/*      */ 
/*      */     protected ConfigImpl(Deserializers[] allAdditionalDeserializers, BeanDeserializerModifier[] modifiers)
/*      */     {
/*   75 */       this._additionalDeserializers = (allAdditionalDeserializers == null ? BeanDeserializerFactory.NO_DESERIALIZERS : allAdditionalDeserializers);
/*      */ 
/*   77 */       this._modifiers = (modifiers == null ? NO_MODIFIERS : modifiers);
/*      */     }
/*      */ 
/*      */     public DeserializerFactory.Config withAdditionalDeserializers(Deserializers additional)
/*      */     {
/*   83 */       if (additional == null) {
/*   84 */         throw new IllegalArgumentException("Can not pass null Deserializers");
/*      */       }
/*   86 */       Deserializers[] all = (Deserializers[])ArrayBuilders.insertInList(this._additionalDeserializers, additional);
/*   87 */       return new ConfigImpl(all, this._modifiers);
/*      */     }
/*      */ 
/*      */     public DeserializerFactory.Config withDeserializerModifier(BeanDeserializerModifier modifier)
/*      */     {
/*   93 */       if (modifier == null) {
/*   94 */         throw new IllegalArgumentException("Can not pass null modifier");
/*      */       }
/*   96 */       BeanDeserializerModifier[] all = (BeanDeserializerModifier[])ArrayBuilders.insertInList(this._modifiers, modifier);
/*   97 */       return new ConfigImpl(this._additionalDeserializers, all);
/*      */     }
/*      */ 
/*      */     public boolean hasDeserializers() {
/*  101 */       return this._additionalDeserializers.length > 0;
/*      */     }
/*      */     public boolean hasDeserializerModifiers() {
/*  104 */       return this._modifiers.length > 0;
/*      */     }
/*      */ 
/*      */     public Iterable<Deserializers> deserializers() {
/*  108 */       return ArrayBuilders.arrayAsIterable(this._additionalDeserializers);
/*      */     }
/*      */ 
/*      */     public Iterable<BeanDeserializerModifier> deserializerModifiers()
/*      */     {
/*  113 */       return ArrayBuilders.arrayAsIterable(this._modifiers);
/*      */     }
/*      */   }
/*      */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.BeanDeserializerFactory
 * JD-Core Version:    0.6.0
 */