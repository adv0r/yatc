/*     */ package org.codehaus.jackson.map.introspect;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.codehaus.jackson.annotate.JacksonAnnotation;
/*     */ import org.codehaus.jackson.annotate.JsonAnyGetter;
/*     */ import org.codehaus.jackson.annotate.JsonAnySetter;
/*     */ import org.codehaus.jackson.annotate.JsonAutoDetect;
/*     */ import org.codehaus.jackson.annotate.JsonBackReference;
/*     */ import org.codehaus.jackson.annotate.JsonClass;
/*     */ import org.codehaus.jackson.annotate.JsonContentClass;
/*     */ import org.codehaus.jackson.annotate.JsonCreator;
/*     */ import org.codehaus.jackson.annotate.JsonGetter;
/*     */ import org.codehaus.jackson.annotate.JsonIgnore;
/*     */ import org.codehaus.jackson.annotate.JsonIgnoreProperties;
/*     */ import org.codehaus.jackson.annotate.JsonIgnoreType;
/*     */ import org.codehaus.jackson.annotate.JsonKeyClass;
/*     */ import org.codehaus.jackson.annotate.JsonManagedReference;
/*     */ import org.codehaus.jackson.annotate.JsonProperty;
/*     */ import org.codehaus.jackson.annotate.JsonPropertyOrder;
/*     */ import org.codehaus.jackson.annotate.JsonRawValue;
/*     */ import org.codehaus.jackson.annotate.JsonSetter;
/*     */ import org.codehaus.jackson.annotate.JsonSubTypes;
/*     */ import org.codehaus.jackson.annotate.JsonSubTypes.Type;
/*     */ import org.codehaus.jackson.annotate.JsonTypeInfo;
/*     */ import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
/*     */ import org.codehaus.jackson.annotate.JsonTypeName;
/*     */ import org.codehaus.jackson.annotate.JsonValue;
/*     */ import org.codehaus.jackson.annotate.JsonWriteNullProperties;
/*     */ import org.codehaus.jackson.map.AnnotationIntrospector;
/*     */ import org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty;
/*     */ import org.codehaus.jackson.map.BeanProperty;
/*     */ import org.codehaus.jackson.map.JsonDeserializer;
/*     */ import org.codehaus.jackson.map.JsonDeserializer.None;
/*     */ import org.codehaus.jackson.map.JsonSerializer.None;
/*     */ import org.codehaus.jackson.map.KeyDeserializer;
/*     */ import org.codehaus.jackson.map.KeyDeserializer.None;
/*     */ import org.codehaus.jackson.map.annotate.JsonCachable;
/*     */ import org.codehaus.jackson.map.annotate.JsonDeserialize;
/*     */ import org.codehaus.jackson.map.annotate.JsonFilter;
/*     */ import org.codehaus.jackson.map.annotate.JsonSerialize;
/*     */ import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
/*     */ import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
/*     */ import org.codehaus.jackson.map.annotate.JsonTypeIdResolver;
/*     */ import org.codehaus.jackson.map.annotate.JsonTypeResolver;
/*     */ import org.codehaus.jackson.map.annotate.JsonView;
/*     */ import org.codehaus.jackson.map.annotate.NoClass;
/*     */ import org.codehaus.jackson.map.jsontype.NamedType;
/*     */ import org.codehaus.jackson.map.jsontype.TypeIdResolver;
/*     */ import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
/*     */ import org.codehaus.jackson.map.jsontype.impl.StdTypeResolverBuilder;
/*     */ import org.codehaus.jackson.map.ser.impl.RawSerializer;
/*     */ import org.codehaus.jackson.map.util.ClassUtil;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class JacksonAnnotationIntrospector extends AnnotationIntrospector
/*     */ {
/*     */   public boolean isHandled(Annotation ann)
/*     */   {
/*  47 */     Class acls = ann.annotationType();
/*     */ 
/*  57 */     return acls.getAnnotation(JacksonAnnotation.class) != null;
/*     */   }
/*     */ 
/*     */   public String findEnumValue(Enum<?> value)
/*     */   {
/*  69 */     return value.name();
/*     */   }
/*     */ 
/*     */   public Boolean findCachability(AnnotatedClass ac)
/*     */   {
/*  81 */     JsonCachable ann = (JsonCachable)ac.getAnnotation(JsonCachable.class);
/*  82 */     if (ann == null) {
/*  83 */       return null;
/*     */     }
/*  85 */     return ann.value() ? Boolean.TRUE : Boolean.FALSE;
/*     */   }
/*     */ 
/*     */   public String findRootName(AnnotatedClass ac)
/*     */   {
/*  92 */     return null;
/*     */   }
/*     */ 
/*     */   public String[] findPropertiesToIgnore(AnnotatedClass ac)
/*     */   {
/*  97 */     JsonIgnoreProperties ignore = (JsonIgnoreProperties)ac.getAnnotation(JsonIgnoreProperties.class);
/*  98 */     return ignore == null ? null : ignore.value();
/*     */   }
/*     */ 
/*     */   public Boolean findIgnoreUnknownProperties(AnnotatedClass ac)
/*     */   {
/* 103 */     JsonIgnoreProperties ignore = (JsonIgnoreProperties)ac.getAnnotation(JsonIgnoreProperties.class);
/* 104 */     return ignore == null ? null : Boolean.valueOf(ignore.ignoreUnknown());
/*     */   }
/*     */ 
/*     */   public Boolean isIgnorableType(AnnotatedClass ac)
/*     */   {
/* 109 */     JsonIgnoreType ignore = (JsonIgnoreType)ac.getAnnotation(JsonIgnoreType.class);
/* 110 */     return ignore == null ? null : Boolean.valueOf(ignore.value());
/*     */   }
/*     */ 
/*     */   public Object findFilterId(AnnotatedClass ac)
/*     */   {
/* 116 */     JsonFilter ann = (JsonFilter)ac.getAnnotation(JsonFilter.class);
/* 117 */     if (ann != null) {
/* 118 */       String id = ann.value();
/*     */ 
/* 120 */       if (id.length() > 0) {
/* 121 */         return id;
/*     */       }
/*     */     }
/* 124 */     return null;
/*     */   }
/*     */ 
/*     */   public VisibilityChecker<?> findAutoDetectVisibility(AnnotatedClass ac, VisibilityChecker<?> checker)
/*     */   {
/* 137 */     JsonAutoDetect ann = (JsonAutoDetect)ac.getAnnotation(JsonAutoDetect.class);
/* 138 */     return ann == null ? checker : checker.with(ann);
/*     */   }
/*     */ 
/*     */   public AnnotationIntrospector.ReferenceProperty findReferenceType(AnnotatedMember member)
/*     */   {
/* 151 */     JsonManagedReference ref1 = (JsonManagedReference)member.getAnnotation(JsonManagedReference.class);
/* 152 */     if (ref1 != null) {
/* 153 */       return AnnotationIntrospector.ReferenceProperty.managed(ref1.value());
/*     */     }
/* 155 */     JsonBackReference ref2 = (JsonBackReference)member.getAnnotation(JsonBackReference.class);
/* 156 */     if (ref2 != null) {
/* 157 */       return AnnotationIntrospector.ReferenceProperty.back(ref2.value());
/*     */     }
/* 159 */     return null;
/*     */   }
/*     */ 
/*     */   public TypeResolverBuilder<?> findTypeResolver(AnnotatedClass ac, JavaType baseType)
/*     */   {
/* 171 */     return _findTypeResolver(ac, baseType);
/*     */   }
/*     */ 
/*     */   public TypeResolverBuilder<?> findPropertyTypeResolver(AnnotatedMember am, JavaType baseType)
/*     */   {
/* 183 */     if (baseType.isContainerType()) return null;
/*     */ 
/* 185 */     return _findTypeResolver(am, baseType);
/*     */   }
/*     */ 
/*     */   public TypeResolverBuilder<?> findPropertyContentTypeResolver(AnnotatedMember am, JavaType containerType)
/*     */   {
/* 197 */     if (!containerType.isContainerType()) {
/* 198 */       throw new IllegalArgumentException("Must call method with a container type (got " + containerType + ")");
/*     */     }
/* 200 */     return _findTypeResolver(am, containerType);
/*     */   }
/*     */ 
/*     */   public List<NamedType> findSubtypes(Annotated a)
/*     */   {
/* 206 */     JsonSubTypes t = (JsonSubTypes)a.getAnnotation(JsonSubTypes.class);
/* 207 */     if (t == null) return null;
/* 208 */     JsonSubTypes.Type[] types = t.value();
/* 209 */     ArrayList result = new ArrayList(types.length);
/* 210 */     for (JsonSubTypes.Type type : types) {
/* 211 */       result.add(new NamedType(type.value(), type.name()));
/*     */     }
/* 213 */     return result;
/*     */   }
/*     */ 
/*     */   public String findTypeName(AnnotatedClass ac)
/*     */   {
/* 219 */     JsonTypeName tn = (JsonTypeName)ac.getAnnotation(JsonTypeName.class);
/* 220 */     return tn == null ? null : tn.value();
/*     */   }
/*     */ 
/*     */   public boolean isIgnorableMethod(AnnotatedMethod m)
/*     */   {
/* 231 */     return _isIgnorable(m);
/*     */   }
/*     */ 
/*     */   public boolean isIgnorableConstructor(AnnotatedConstructor c)
/*     */   {
/* 236 */     return _isIgnorable(c);
/*     */   }
/*     */ 
/*     */   public boolean isIgnorableField(AnnotatedField f)
/*     */   {
/* 247 */     return _isIgnorable(f);
/*     */   }
/*     */ 
/*     */   public Object findSerializer(Annotated a, BeanProperty property)
/*     */   {
/* 262 */     JsonSerialize ann = (JsonSerialize)a.getAnnotation(JsonSerialize.class);
/* 263 */     if (ann != null) {
/* 264 */       Class serClass = ann.using();
/* 265 */       if (serClass != JsonSerializer.None.class) {
/* 266 */         return serClass;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 274 */     JsonRawValue annRaw = (JsonRawValue)a.getAnnotation(JsonRawValue.class);
/* 275 */     if ((annRaw != null) && (annRaw.value()))
/*     */     {
/* 277 */       Class cls = a.getRawType();
/* 278 */       return new RawSerializer(cls);
/*     */     }
/* 280 */     return null;
/*     */   }
/*     */ 
/*     */   public JsonSerialize.Inclusion findSerializationInclusion(Annotated a, JsonSerialize.Inclusion defValue)
/*     */   {
/* 287 */     JsonSerialize ann = (JsonSerialize)a.getAnnotation(JsonSerialize.class);
/* 288 */     if (ann != null) {
/* 289 */       return ann.include();
/*     */     }
/*     */ 
/* 294 */     JsonWriteNullProperties oldAnn = (JsonWriteNullProperties)a.getAnnotation(JsonWriteNullProperties.class);
/* 295 */     if (oldAnn != null) {
/* 296 */       boolean writeNulls = oldAnn.value();
/* 297 */       return writeNulls ? JsonSerialize.Inclusion.ALWAYS : JsonSerialize.Inclusion.NON_NULL;
/*     */     }
/* 299 */     return defValue;
/*     */   }
/*     */ 
/*     */   public Class<?> findSerializationType(Annotated am)
/*     */   {
/* 306 */     JsonSerialize ann = (JsonSerialize)am.getAnnotation(JsonSerialize.class);
/* 307 */     if (ann != null) {
/* 308 */       Class cls = ann.as();
/* 309 */       if (cls != NoClass.class) {
/* 310 */         return cls;
/*     */       }
/*     */     }
/* 313 */     return null;
/*     */   }
/*     */ 
/*     */   public JsonSerialize.Typing findSerializationTyping(Annotated a)
/*     */   {
/* 319 */     JsonSerialize ann = (JsonSerialize)a.getAnnotation(JsonSerialize.class);
/* 320 */     return ann == null ? null : ann.typing();
/*     */   }
/*     */ 
/*     */   public Class<?>[] findSerializationViews(Annotated a)
/*     */   {
/* 326 */     JsonView ann = (JsonView)a.getAnnotation(JsonView.class);
/* 327 */     return ann == null ? null : ann.value();
/*     */   }
/*     */ 
/*     */   public String[] findSerializationPropertyOrder(AnnotatedClass ac)
/*     */   {
/* 338 */     JsonPropertyOrder order = (JsonPropertyOrder)ac.getAnnotation(JsonPropertyOrder.class);
/* 339 */     return order == null ? null : order.value();
/*     */   }
/*     */ 
/*     */   public Boolean findSerializationSortAlphabetically(AnnotatedClass ac)
/*     */   {
/* 344 */     JsonPropertyOrder order = (JsonPropertyOrder)ac.getAnnotation(JsonPropertyOrder.class);
/* 345 */     return order == null ? null : Boolean.valueOf(order.alphabetic());
/*     */   }
/*     */ 
/*     */   public String findGettablePropertyName(AnnotatedMethod am)
/*     */   {
/* 361 */     JsonProperty pann = (JsonProperty)am.getAnnotation(JsonProperty.class);
/* 362 */     if (pann != null) {
/* 363 */       return pann.value();
/*     */     }
/*     */ 
/* 368 */     JsonGetter ann = (JsonGetter)am.getAnnotation(JsonGetter.class);
/* 369 */     if (ann != null) {
/* 370 */       return ann.value();
/*     */     }
/*     */ 
/* 376 */     if ((am.hasAnnotation(JsonSerialize.class)) || (am.hasAnnotation(JsonView.class))) {
/* 377 */       return "";
/*     */     }
/* 379 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean hasAsValueAnnotation(AnnotatedMethod am)
/*     */   {
/* 385 */     JsonValue ann = (JsonValue)am.getAnnotation(JsonValue.class);
/*     */ 
/* 387 */     return (ann != null) && (ann.value());
/*     */   }
/*     */ 
/*     */   public String findSerializablePropertyName(AnnotatedField af)
/*     */   {
/* 399 */     JsonProperty pann = (JsonProperty)af.getAnnotation(JsonProperty.class);
/* 400 */     if (pann != null) {
/* 401 */       return pann.value();
/*     */     }
/*     */ 
/* 405 */     if ((af.hasAnnotation(JsonSerialize.class)) || (af.hasAnnotation(JsonView.class))) {
/* 406 */       return "";
/*     */     }
/* 408 */     return null;
/*     */   }
/*     */ 
/*     */   public Class<? extends JsonDeserializer<?>> findDeserializer(Annotated a, BeanProperty property)
/*     */   {
/* 423 */     JsonDeserialize ann = (JsonDeserialize)a.getAnnotation(JsonDeserialize.class);
/* 424 */     if (ann != null) {
/* 425 */       Class deserClass = ann.using();
/* 426 */       if (deserClass != JsonDeserializer.None.class) {
/* 427 */         return deserClass;
/*     */       }
/*     */     }
/*     */ 
/* 431 */     return null;
/*     */   }
/*     */ 
/*     */   public Class<? extends KeyDeserializer> findKeyDeserializer(Annotated a)
/*     */   {
/* 437 */     JsonDeserialize ann = (JsonDeserialize)a.getAnnotation(JsonDeserialize.class);
/* 438 */     if (ann != null) {
/* 439 */       Class deserClass = ann.keyUsing();
/* 440 */       if (deserClass != KeyDeserializer.None.class) {
/* 441 */         return deserClass;
/*     */       }
/*     */     }
/* 444 */     return null;
/*     */   }
/*     */ 
/*     */   public Class<? extends JsonDeserializer<?>> findContentDeserializer(Annotated a)
/*     */   {
/* 450 */     JsonDeserialize ann = (JsonDeserialize)a.getAnnotation(JsonDeserialize.class);
/* 451 */     if (ann != null) {
/* 452 */       Class deserClass = ann.contentUsing();
/* 453 */       if (deserClass != JsonDeserializer.None.class) {
/* 454 */         return deserClass;
/*     */       }
/*     */     }
/* 457 */     return null;
/*     */   }
/*     */ 
/*     */   public Class<?> findDeserializationType(Annotated am, JavaType baseType, String propName)
/*     */   {
/* 465 */     JsonDeserialize ann = (JsonDeserialize)am.getAnnotation(JsonDeserialize.class);
/* 466 */     if (ann != null) {
/* 467 */       Class cls = ann.as();
/* 468 */       if (cls != NoClass.class) {
/* 469 */         return cls;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 477 */     JsonClass oldAnn = (JsonClass)am.getAnnotation(JsonClass.class);
/* 478 */     if (oldAnn != null)
/*     */     {
/* 480 */       Class cls = oldAnn.value();
/* 481 */       if (cls != NoClass.class) {
/* 482 */         return cls;
/*     */       }
/*     */     }
/* 485 */     return null;
/*     */   }
/*     */ 
/*     */   public Class<?> findDeserializationKeyType(Annotated am, JavaType baseKeyType, String propName)
/*     */   {
/* 493 */     JsonDeserialize ann = (JsonDeserialize)am.getAnnotation(JsonDeserialize.class);
/* 494 */     if (ann != null) {
/* 495 */       Class cls = ann.keyAs();
/* 496 */       if (cls != NoClass.class) {
/* 497 */         return cls;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 505 */     JsonKeyClass oldAnn = (JsonKeyClass)am.getAnnotation(JsonKeyClass.class);
/* 506 */     if (oldAnn != null)
/*     */     {
/* 508 */       Class cls = oldAnn.value();
/* 509 */       if (cls != NoClass.class) {
/* 510 */         return cls;
/*     */       }
/*     */     }
/* 513 */     return null;
/*     */   }
/*     */ 
/*     */   public Class<?> findDeserializationContentType(Annotated am, JavaType baseContentType, String propName)
/*     */   {
/* 522 */     JsonDeserialize ann = (JsonDeserialize)am.getAnnotation(JsonDeserialize.class);
/* 523 */     if (ann != null) {
/* 524 */       Class cls = ann.contentAs();
/* 525 */       if (cls != NoClass.class) {
/* 526 */         return cls;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 533 */     JsonContentClass oldAnn = (JsonContentClass)am.getAnnotation(JsonContentClass.class);
/* 534 */     if (oldAnn != null) {
/* 535 */       Class cls = oldAnn.value();
/* 536 */       if (cls != NoClass.class) {
/* 537 */         return cls;
/*     */       }
/*     */     }
/* 540 */     return null;
/*     */   }
/*     */ 
/*     */   public String findSettablePropertyName(AnnotatedMethod am)
/*     */   {
/* 558 */     JsonProperty pann = (JsonProperty)am.getAnnotation(JsonProperty.class);
/* 559 */     if (pann != null) {
/* 560 */       return pann.value();
/*     */     }
/* 562 */     JsonSetter ann = (JsonSetter)am.getAnnotation(JsonSetter.class);
/* 563 */     if (ann != null) {
/* 564 */       return ann.value();
/*     */     }
/*     */ 
/* 570 */     if ((am.hasAnnotation(JsonDeserialize.class)) || (am.hasAnnotation(JsonView.class))) {
/* 571 */       return "";
/*     */     }
/* 573 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean hasAnySetterAnnotation(AnnotatedMethod am)
/*     */   {
/* 583 */     return am.hasAnnotation(JsonAnySetter.class);
/*     */   }
/*     */ 
/*     */   public boolean hasAnyGetterAnnotation(AnnotatedMethod am)
/*     */   {
/* 592 */     return am.hasAnnotation(JsonAnyGetter.class);
/*     */   }
/*     */ 
/*     */   public boolean hasCreatorAnnotation(Annotated a)
/*     */   {
/* 602 */     return a.hasAnnotation(JsonCreator.class);
/*     */   }
/*     */ 
/*     */   public String findDeserializablePropertyName(AnnotatedField af)
/*     */   {
/* 614 */     JsonProperty pann = (JsonProperty)af.getAnnotation(JsonProperty.class);
/* 615 */     if (pann != null) {
/* 616 */       return pann.value();
/*     */     }
/*     */ 
/* 620 */     if ((af.hasAnnotation(JsonDeserialize.class)) || (af.hasAnnotation(JsonView.class))) {
/* 621 */       return "";
/*     */     }
/* 623 */     return null;
/*     */   }
/*     */ 
/*     */   public String findPropertyNameForParam(AnnotatedParameter param)
/*     */   {
/* 635 */     if (param != null) {
/* 636 */       JsonProperty pann = (JsonProperty)param.getAnnotation(JsonProperty.class);
/* 637 */       if (pann != null) {
/* 638 */         return pann.value();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 645 */     return null;
/*     */   }
/*     */ 
/*     */   protected boolean _isIgnorable(Annotated a)
/*     */   {
/* 656 */     JsonIgnore ann = (JsonIgnore)a.getAnnotation(JsonIgnore.class);
/* 657 */     return (ann != null) && (ann.value());
/*     */   }
/*     */ 
/*     */   protected TypeResolverBuilder<?> _findTypeResolver(Annotated ann, JavaType baseType)
/*     */   {
/* 668 */     JsonTypeInfo info = (JsonTypeInfo)ann.getAnnotation(JsonTypeInfo.class);
/* 669 */     JsonTypeResolver resAnn = (JsonTypeResolver)ann.getAnnotation(JsonTypeResolver.class);
/*     */     TypeResolverBuilder b;
/* 670 */     if (resAnn != null)
/*     */     {
/* 674 */       if (info == null) {
/* 675 */         return null;
/*     */       }
/*     */ 
/* 681 */       b = (TypeResolverBuilder)ClassUtil.createInstance(resAnn.value(), false);
/*     */     } else {
/* 683 */       if ((info == null) || (info.use() == JsonTypeInfo.Id.NONE)) {
/* 684 */         return null;
/*     */       }
/* 686 */       b = _constructStdTypeResolverBuilder();
/*     */     }
/*     */ 
/* 689 */     JsonTypeIdResolver idResInfo = (JsonTypeIdResolver)ann.getAnnotation(JsonTypeIdResolver.class);
/* 690 */     TypeIdResolver idRes = idResInfo == null ? null : (TypeIdResolver)ClassUtil.createInstance(idResInfo.value(), true);
/*     */ 
/* 692 */     if (idRes != null) {
/* 693 */       idRes.init(baseType);
/*     */     }
/* 695 */     TypeResolverBuilder b = b.init(info.use(), idRes);
/* 696 */     b = b.inclusion(info.include());
/* 697 */     b = b.typeProperty(info.property());
/* 698 */     return b;
/*     */   }
/*     */ 
/*     */   protected StdTypeResolverBuilder _constructStdTypeResolverBuilder()
/*     */   {
/* 709 */     return new StdTypeResolverBuilder();
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector
 * JD-Core Version:    0.6.0
 */