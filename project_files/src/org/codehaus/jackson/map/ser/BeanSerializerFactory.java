/*     */ package org.codehaus.jackson.map.ser;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
/*     */ import org.codehaus.jackson.map.AnnotationIntrospector;
/*     */ import org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty;
/*     */ import org.codehaus.jackson.map.BeanDescription;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.BeanProperty.Std;
/*     */ import org.codehaus.jackson.map.JsonSerializer;
/*     */ import org.codehaus.jackson.map.SerializationConfig;
/*     */ import org.codehaus.jackson.map.SerializationConfig.Feature;
/*     */ import org.codehaus.jackson.map.SerializerFactory;
/*     */ import org.codehaus.jackson.map.SerializerFactory.Config;
/*     */ import org.codehaus.jackson.map.Serializers;
/*     */ import org.codehaus.jackson.map.TypeSerializer;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedClass;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedMember;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedMethod;
/*     */ import org.codehaus.jackson.map.introspect.BasicBeanDescription;
/*     */ import org.codehaus.jackson.map.introspect.VisibilityChecker;
/*     */ import org.codehaus.jackson.map.jsontype.SubtypeResolver;
/*     */ import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
/*     */ import org.codehaus.jackson.map.type.TypeBindings;
/*     */ import org.codehaus.jackson.map.util.ArrayBuilders;
/*     */ import org.codehaus.jackson.map.util.ClassUtil;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class BeanSerializerFactory extends BasicSerializerFactory
/*     */ {
/*  56 */   public static final BeanSerializerFactory instance = new BeanSerializerFactory(null);
/*     */   protected final SerializerFactory.Config _factoryConfig;
/*     */ 
/*     */   @Deprecated
/*     */   protected BeanSerializerFactory()
/*     */   {
/* 157 */     this(null);
/*     */   }
/*     */ 
/*     */   protected BeanSerializerFactory(SerializerFactory.Config config)
/*     */   {
/* 164 */     if (config == null) {
/* 165 */       config = new ConfigImpl();
/*     */     }
/* 167 */     this._factoryConfig = config;
/*     */   }
/*     */   public SerializerFactory.Config getConfig() {
/* 170 */     return this._factoryConfig;
/*     */   }
/*     */ 
/*     */   public SerializerFactory withConfig(SerializerFactory.Config config)
/*     */   {
/* 183 */     if (this._factoryConfig == config) {
/* 184 */       return this;
/*     */     }
/*     */ 
/* 192 */     if (getClass() != BeanSerializerFactory.class) {
/* 193 */       throw new IllegalStateException("Subtype of BeanSerializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalSerializers': can not instantiate subtype with " + "additional serializer definitions");
/*     */     }
/*     */ 
/* 197 */     return new BeanSerializerFactory(config);
/*     */   }
/*     */ 
/*     */   public JsonSerializer<Object> createSerializer(SerializationConfig config, JavaType type, BeanProperty property)
/*     */   {
/* 224 */     BasicBeanDescription beanDesc = (BasicBeanDescription)config.introspect(type);
/* 225 */     JsonSerializer ser = findSerializerFromAnnotation(config, beanDesc.getClassInfo(), property);
/* 226 */     if (ser == null)
/*     */     {
/* 228 */       ser = _findFirstSerializer(this._factoryConfig.serializers(), config, type, beanDesc, property);
/* 229 */       if (ser == null)
/*     */       {
/* 231 */         ser = super.findSerializerByLookup(type, config, beanDesc, property);
/* 232 */         if (ser == null)
/*     */         {
/* 234 */           ser = super.findSerializerByPrimaryType(type, config, beanDesc, property);
/* 235 */           if (ser == null)
/*     */           {
/* 241 */             ser = findBeanSerializer(config, type, beanDesc, property);
/*     */ 
/* 245 */             if (ser == null) {
/* 246 */               ser = super.findSerializerByAddonType(config, type, beanDesc, property);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 252 */     return ser;
/*     */   }
/*     */ 
/*     */   private static JsonSerializer<?> _findFirstSerializer(Iterable<Serializers> sers, SerializationConfig config, JavaType type, BeanDescription beanDesc, BeanProperty property)
/*     */   {
/* 263 */     for (Serializers ser : sers) {
/* 264 */       JsonSerializer js = ser.findSerializer(config, type, beanDesc, property);
/* 265 */       if (js != null) {
/* 266 */         return js;
/*     */       }
/*     */     }
/* 269 */     return null;
/*     */   }
/*     */ 
/*     */   public JsonSerializer<Object> findBeanSerializer(SerializationConfig config, JavaType type, BasicBeanDescription beanDesc, BeanProperty property)
/*     */   {
/* 288 */     if (!isPotentialBeanType(type.getRawClass())) {
/* 289 */       return null;
/*     */     }
/* 291 */     JsonSerializer serializer = constructBeanSerializer(config, beanDesc, property);
/*     */ 
/* 293 */     if (this._factoryConfig.hasSerializerModifiers()) {
/* 294 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/* 295 */         serializer = mod.modifySerializer(config, beanDesc, serializer);
/*     */       }
/*     */     }
/* 298 */     return serializer;
/*     */   }
/*     */ 
/*     */   public TypeSerializer findPropertyTypeSerializer(JavaType baseType, SerializationConfig config, AnnotatedMember accessor, BeanProperty property)
/*     */   {
/* 316 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/* 317 */     TypeResolverBuilder b = ai.findPropertyTypeResolver(accessor, baseType);
/*     */ 
/* 319 */     if (b == null) {
/* 320 */       return createTypeSerializer(config, baseType, property);
/*     */     }
/* 322 */     Collection subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(accessor, config, ai);
/* 323 */     return b.buildTypeSerializer(baseType, subtypes, property);
/*     */   }
/*     */ 
/*     */   public TypeSerializer findPropertyContentTypeSerializer(JavaType containerType, SerializationConfig config, AnnotatedMember accessor, BeanProperty property)
/*     */   {
/* 341 */     JavaType contentType = containerType.getContentType();
/* 342 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/* 343 */     TypeResolverBuilder b = ai.findPropertyContentTypeResolver(accessor, containerType);
/*     */ 
/* 345 */     if (b == null) {
/* 346 */       return createTypeSerializer(config, contentType, property);
/*     */     }
/* 348 */     Collection subtypes = config.getSubtypeResolver().collectAndResolveSubtypes(accessor, config, ai);
/* 349 */     return b.buildTypeSerializer(contentType, subtypes, property);
/*     */   }
/*     */ 
/*     */   protected JsonSerializer<Object> constructBeanSerializer(SerializationConfig config, BasicBeanDescription beanDesc, BeanProperty property)
/*     */   {
/* 368 */     if (beanDesc.getBeanClass() == Object.class) {
/* 369 */       throw new IllegalArgumentException("Can not create bean serializer for Object.class");
/*     */     }
/*     */ 
/* 372 */     BeanSerializerBuilder builder = constructBeanSerializerBuilder(beanDesc);
/*     */ 
/* 375 */     List props = findBeanProperties(config, beanDesc);
/* 376 */     AnnotatedMethod anyGetter = beanDesc.findAnyGetter();
/*     */ 
/* 379 */     if (this._factoryConfig.hasSerializerModifiers()) {
/* 380 */       if (props == null) {
/* 381 */         props = new ArrayList();
/*     */       }
/* 383 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/* 384 */         props = mod.changeProperties(config, beanDesc, props);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 390 */     if ((props == null) || (props.size() == 0)) {
/* 391 */       if (anyGetter == null)
/*     */       {
/* 396 */         if (beanDesc.hasKnownClassAnnotations()) {
/* 397 */           return builder.createDummy();
/*     */         }
/* 399 */         return null;
/*     */       }
/* 401 */       props = Collections.emptyList();
/*     */     }
/*     */     else {
/* 404 */       props = filterBeanProperties(config, beanDesc, props);
/*     */ 
/* 406 */       props = sortBeanProperties(config, beanDesc, props);
/*     */     }
/*     */ 
/* 409 */     if (this._factoryConfig.hasSerializerModifiers()) {
/* 410 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/* 411 */         props = mod.orderProperties(config, beanDesc, props);
/*     */       }
/*     */     }
/* 414 */     builder.setProperties(props);
/* 415 */     builder.setFilterId(findFilterId(config, beanDesc));
/*     */ 
/* 417 */     if (anyGetter != null) {
/* 418 */       JavaType type = anyGetter.getType(beanDesc.bindingsForBeanType());
/*     */ 
/* 420 */       boolean staticTyping = config.isEnabled(SerializationConfig.Feature.USE_STATIC_TYPING);
/* 421 */       JavaType valueType = type.getContentType();
/* 422 */       TypeSerializer typeSer = createTypeSerializer(config, valueType, property);
/* 423 */       MapSerializer mapSer = MapSerializer.construct(null, type, staticTyping, typeSer, property);
/*     */ 
/* 425 */       builder.setAnyGetter(new AnyGetterWriter(anyGetter, mapSer));
/*     */     }
/*     */ 
/* 428 */     processViews(config, builder);
/*     */ 
/* 430 */     if (this._factoryConfig.hasSerializerModifiers()) {
/* 431 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/* 432 */         builder = mod.updateBuilder(config, beanDesc, builder);
/*     */       }
/*     */     }
/*     */ 
/* 436 */     return builder.build();
/*     */   }
/*     */ 
/*     */   protected BeanPropertyWriter constructFilteredBeanWriter(BeanPropertyWriter writer, Class<?>[] inViews)
/*     */   {
/* 446 */     return FilteredBeanPropertyWriter.constructViewBased(writer, inViews);
/*     */   }
/*     */ 
/*     */   protected PropertyBuilder constructPropertyBuilder(SerializationConfig config, BasicBeanDescription beanDesc)
/*     */   {
/* 452 */     return new PropertyBuilder(config, beanDesc);
/*     */   }
/*     */ 
/*     */   protected BeanSerializerBuilder constructBeanSerializerBuilder(BasicBeanDescription beanDesc) {
/* 456 */     return new BeanSerializerBuilder(beanDesc);
/*     */   }
/*     */ 
/*     */   protected Object findFilterId(SerializationConfig config, BasicBeanDescription beanDesc)
/*     */   {
/* 467 */     return config.getAnnotationIntrospector().findFilterId(beanDesc.getClassInfo());
/*     */   }
/*     */ 
/*     */   protected boolean isPotentialBeanType(Class<?> type)
/*     */   {
/* 486 */     return (ClassUtil.canBeABeanType(type) == null) && (!ClassUtil.isProxyType(type));
/*     */   }
/*     */ 
/*     */   protected List<BeanPropertyWriter> findBeanProperties(SerializationConfig config, BasicBeanDescription beanDesc)
/*     */   {
/* 496 */     VisibilityChecker vchecker = config.getDefaultVisibilityChecker();
/* 497 */     if (!config.isEnabled(SerializationConfig.Feature.AUTO_DETECT_GETTERS)) {
/* 498 */       vchecker = vchecker.withGetterVisibility(JsonAutoDetect.Visibility.NONE);
/*     */     }
/*     */ 
/* 501 */     if (!config.isEnabled(SerializationConfig.Feature.AUTO_DETECT_IS_GETTERS)) {
/* 502 */       vchecker = vchecker.withIsGetterVisibility(JsonAutoDetect.Visibility.NONE);
/*     */     }
/* 504 */     if (!config.isEnabled(SerializationConfig.Feature.AUTO_DETECT_FIELDS)) {
/* 505 */       vchecker = vchecker.withFieldVisibility(JsonAutoDetect.Visibility.NONE);
/*     */     }
/*     */ 
/* 508 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 509 */     vchecker = intr.findAutoDetectVisibility(beanDesc.getClassInfo(), vchecker);
/*     */ 
/* 511 */     LinkedHashMap methodsByProp = beanDesc.findGetters(vchecker, null);
/* 512 */     LinkedHashMap fieldsByProp = beanDesc.findSerializableFields(vchecker, methodsByProp.keySet());
/*     */ 
/* 515 */     removeIgnorableTypes(config, beanDesc, methodsByProp);
/* 516 */     removeIgnorableTypes(config, beanDesc, fieldsByProp);
/*     */ 
/* 519 */     if ((methodsByProp.isEmpty()) && (fieldsByProp.isEmpty())) {
/* 520 */       return null;
/*     */     }
/*     */ 
/* 524 */     boolean staticTyping = usesStaticTyping(config, beanDesc, null);
/* 525 */     PropertyBuilder pb = constructPropertyBuilder(config, beanDesc);
/*     */ 
/* 527 */     ArrayList props = new ArrayList(methodsByProp.size());
/* 528 */     TypeBindings typeBind = beanDesc.bindingsForBeanType();
/*     */ 
/* 530 */     for (Map.Entry en : fieldsByProp.entrySet())
/*     */     {
/* 532 */       AnnotationIntrospector.ReferenceProperty prop = intr.findReferenceType((AnnotatedMember)en.getValue());
/* 533 */       if ((prop != null) && (prop.isBackReference())) {
/*     */         continue;
/*     */       }
/* 536 */       props.add(_constructWriter(config, typeBind, pb, staticTyping, (String)en.getKey(), (AnnotatedMember)en.getValue()));
/*     */     }
/*     */ 
/* 539 */     for (Map.Entry en : methodsByProp.entrySet())
/*     */     {
/* 541 */       AnnotationIntrospector.ReferenceProperty prop = intr.findReferenceType((AnnotatedMember)en.getValue());
/* 542 */       if ((prop != null) && (prop.isBackReference())) {
/*     */         continue;
/*     */       }
/* 545 */       props.add(_constructWriter(config, typeBind, pb, staticTyping, (String)en.getKey(), (AnnotatedMember)en.getValue()));
/*     */     }
/* 547 */     return props;
/*     */   }
/*     */ 
/*     */   protected List<BeanPropertyWriter> filterBeanProperties(SerializationConfig config, BasicBeanDescription beanDesc, List<BeanPropertyWriter> props)
/*     */   {
/* 563 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 564 */     AnnotatedClass ac = beanDesc.getClassInfo();
/* 565 */     String[] ignored = intr.findPropertiesToIgnore(ac);
/* 566 */     if ((ignored != null) && (ignored.length > 0)) {
/* 567 */       HashSet ignoredSet = ArrayBuilders.arrayToSet(ignored);
/* 568 */       Iterator it = props.iterator();
/* 569 */       while (it.hasNext()) {
/* 570 */         if (ignoredSet.contains(((BeanPropertyWriter)it.next()).getName())) {
/* 571 */           it.remove();
/*     */         }
/*     */       }
/*     */     }
/* 575 */     return props;
/*     */   }
/*     */ 
/*     */   protected List<BeanPropertyWriter> sortBeanProperties(SerializationConfig config, BasicBeanDescription beanDesc, List<BeanPropertyWriter> props)
/*     */   {
/* 596 */     List creatorProps = beanDesc.findCreatorPropertyNames();
/*     */ 
/* 598 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 599 */     AnnotatedClass ac = beanDesc.getClassInfo();
/* 600 */     String[] propOrder = intr.findSerializationPropertyOrder(ac);
/* 601 */     Boolean alpha = intr.findSerializationSortAlphabetically(ac);
/* 602 */     boolean sort = (alpha != null) && (alpha.booleanValue());
/* 603 */     if ((sort) || (!creatorProps.isEmpty()) || (propOrder != null)) {
/* 604 */       props = _sortBeanProperties(props, creatorProps, propOrder, sort);
/*     */     }
/* 606 */     return props;
/*     */   }
/*     */ 
/*     */   protected void processViews(SerializationConfig config, BeanSerializerBuilder builder)
/*     */   {
/* 624 */     List props = builder.getProperties();
/* 625 */     boolean includeByDefault = config.isEnabled(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION);
/* 626 */     if (includeByDefault) {
/* 627 */       int propCount = props.size();
/* 628 */       BeanPropertyWriter[] filtered = null;
/*     */ 
/* 630 */       for (int i = 0; i < propCount; i++) {
/* 631 */         BeanPropertyWriter bpw = (BeanPropertyWriter)props.get(i);
/* 632 */         Class[] views = bpw.getViews();
/* 633 */         if (views != null) {
/* 634 */           if (filtered == null) {
/* 635 */             filtered = new BeanPropertyWriter[props.size()];
/*     */           }
/* 637 */           filtered[i] = constructFilteredBeanWriter(bpw, views);
/*     */         }
/*     */       }
/*     */ 
/* 641 */       if (filtered != null) {
/* 642 */         for (int i = 0; i < propCount; i++) {
/* 643 */           if (filtered[i] == null) {
/* 644 */             filtered[i] = ((BeanPropertyWriter)props.get(i));
/*     */           }
/*     */         }
/* 647 */         builder.setFilteredProperties(filtered);
/*     */       }
/*     */ 
/* 650 */       return;
/*     */     }
/*     */ 
/* 653 */     ArrayList explicit = new ArrayList(props.size());
/* 654 */     for (BeanPropertyWriter bpw : props) {
/* 655 */       Class[] views = bpw.getViews();
/* 656 */       if (views != null) {
/* 657 */         explicit.add(constructFilteredBeanWriter(bpw, views));
/*     */       }
/*     */     }
/* 660 */     builder.setFilteredProperties((BeanPropertyWriter[])explicit.toArray(new BeanPropertyWriter[explicit.size()]));
/*     */   }
/*     */ 
/*     */   protected <T extends AnnotatedMember> void removeIgnorableTypes(SerializationConfig config, BasicBeanDescription beanDesc, Map<String, T> props)
/*     */   {
/* 671 */     if (props.isEmpty()) {
/* 672 */       return;
/*     */     }
/* 674 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 675 */     Iterator it = props.entrySet().iterator();
/* 676 */     HashMap ignores = new HashMap();
/* 677 */     while (it.hasNext()) {
/* 678 */       Map.Entry entry = (Map.Entry)it.next();
/* 679 */       Class type = ((AnnotatedMember)entry.getValue()).getRawType();
/* 680 */       Boolean result = (Boolean)ignores.get(type);
/* 681 */       if (result == null) {
/* 682 */         BasicBeanDescription desc = (BasicBeanDescription)config.introspectClassAnnotations(type);
/* 683 */         AnnotatedClass ac = desc.getClassInfo();
/* 684 */         result = intr.isIgnorableType(ac);
/*     */ 
/* 686 */         if (result == null) {
/* 687 */           result = Boolean.FALSE;
/*     */         }
/* 689 */         ignores.put(type, result);
/*     */       }
/*     */ 
/* 692 */       if (result.booleanValue())
/* 693 */         it.remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected BeanPropertyWriter _constructWriter(SerializationConfig config, TypeBindings typeContext, PropertyBuilder pb, boolean staticTyping, String name, AnnotatedMember accessor)
/*     */   {
/* 711 */     if (config.isEnabled(SerializationConfig.Feature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
/* 712 */       accessor.fixAccess();
/*     */     }
/* 714 */     JavaType type = accessor.getType(typeContext);
/* 715 */     BeanProperty.Std property = new BeanProperty.Std(name, type, pb.getClassAnnotations(), accessor);
/*     */ 
/* 718 */     JsonSerializer annotatedSerializer = findSerializerFromAnnotation(config, accessor, property);
/*     */ 
/* 720 */     TypeSerializer contentTypeSer = null;
/* 721 */     if (ClassUtil.isCollectionMapOrArray(type.getRawClass())) {
/* 722 */       contentTypeSer = findPropertyContentTypeSerializer(type, config, accessor, property);
/*     */     }
/*     */ 
/* 726 */     TypeSerializer typeSer = findPropertyTypeSerializer(type, config, accessor, property);
/* 727 */     BeanPropertyWriter pbw = pb.buildWriter(name, type, annotatedSerializer, typeSer, contentTypeSer, accessor, staticTyping);
/*     */ 
/* 730 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 731 */     pbw.setViews(intr.findSerializationViews(accessor));
/* 732 */     return pbw;
/*     */   }
/*     */ 
/*     */   protected List<BeanPropertyWriter> _sortBeanProperties(List<BeanPropertyWriter> props, List<String> creatorProps, String[] propertyOrder, boolean sort)
/*     */   {
/* 742 */     int size = props.size();
/*     */     Map all;
/*     */     Map all;
/* 745 */     if (sort)
/* 746 */       all = new TreeMap();
/*     */     else {
/* 748 */       all = new LinkedHashMap(size * 2);
/*     */     }
/*     */ 
/* 751 */     for (BeanPropertyWriter w : props) {
/* 752 */       all.put(w.getName(), w);
/*     */     }
/* 754 */     Map ordered = new LinkedHashMap(size * 2);
/*     */ 
/* 756 */     if (propertyOrder != null) {
/* 757 */       for (String name : propertyOrder) {
/* 758 */         BeanPropertyWriter w = (BeanPropertyWriter)all.get(name);
/* 759 */         if (w != null) {
/* 760 */           ordered.put(name, w);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 765 */     for (String name : creatorProps) {
/* 766 */       BeanPropertyWriter w = (BeanPropertyWriter)all.get(name);
/* 767 */       if (w != null) {
/* 768 */         ordered.put(name, w);
/*     */       }
/*     */     }
/*     */ 
/* 772 */     ordered.putAll(all);
/* 773 */     return new ArrayList(ordered.values());
/*     */   }
/*     */ 
/*     */   public static class ConfigImpl extends SerializerFactory.Config
/*     */   {
/*  83 */     protected static final Serializers[] NO_SERIALIZERS = new Serializers[0];
/*     */ 
/*  85 */     protected static final BeanSerializerModifier[] NO_MODIFIERS = new BeanSerializerModifier[0];
/*     */     protected final Serializers[] _additionalSerializers;
/*     */     protected final BeanSerializerModifier[] _modifiers;
/*     */ 
/*     */     public ConfigImpl()
/*     */     {
/* 102 */       this(null, null);
/*     */     }
/*     */ 
/*     */     protected ConfigImpl(Serializers[] allAdditionalSerializers, BeanSerializerModifier[] modifiers)
/*     */     {
/* 108 */       this._additionalSerializers = (allAdditionalSerializers == null ? NO_SERIALIZERS : allAdditionalSerializers);
/*     */ 
/* 110 */       this._modifiers = (modifiers == null ? NO_MODIFIERS : modifiers);
/*     */     }
/*     */ 
/*     */     public SerializerFactory.Config withAdditionalSerializers(Serializers additional)
/*     */     {
/* 116 */       if (additional == null) {
/* 117 */         throw new IllegalArgumentException("Can not pass null Serializers");
/*     */       }
/* 119 */       Serializers[] all = (Serializers[])ArrayBuilders.insertInList(this._additionalSerializers, additional);
/* 120 */       return new ConfigImpl(all, this._modifiers);
/*     */     }
/*     */ 
/*     */     public SerializerFactory.Config withSerializerModifier(BeanSerializerModifier modifier)
/*     */     {
/* 126 */       if (modifier == null) {
/* 127 */         throw new IllegalArgumentException("Can not pass null modifier");
/*     */       }
/* 129 */       BeanSerializerModifier[] modifiers = (BeanSerializerModifier[])ArrayBuilders.insertInList(this._modifiers, modifier);
/* 130 */       return new ConfigImpl(this._additionalSerializers, modifiers);
/*     */     }
/*     */ 
/*     */     public boolean hasSerializers() {
/* 134 */       return this._additionalSerializers.length > 0;
/*     */     }
/*     */     public boolean hasSerializerModifiers() {
/* 137 */       return this._modifiers.length > 0;
/*     */     }
/*     */ 
/*     */     public Iterable<Serializers> serializers() {
/* 141 */       return ArrayBuilders.arrayAsIterable(this._additionalSerializers);
/*     */     }
/*     */ 
/*     */     public Iterable<BeanSerializerModifier> serializerModifiers()
/*     */     {
/* 146 */       return ArrayBuilders.arrayAsIterable(this._modifiers);
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.BeanSerializerFactory
 * JD-Core Version:    0.6.0
 */