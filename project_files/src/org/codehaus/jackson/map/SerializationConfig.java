/*     */ package org.codehaus.jackson.map;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
/*     */ import org.codehaus.jackson.map.annotate.JsonSerialize.Typing;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedClass;
/*     */ import org.codehaus.jackson.map.introspect.VisibilityChecker;
/*     */ import org.codehaus.jackson.map.jsontype.SubtypeResolver;
/*     */ import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
/*     */ import org.codehaus.jackson.map.jsontype.impl.StdSubtypeResolver;
/*     */ import org.codehaus.jackson.map.ser.FilterProvider;
/*     */ import org.codehaus.jackson.map.type.ClassKey;
/*     */ import org.codehaus.jackson.map.util.StdDateFormat;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class SerializationConfig
/*     */   implements MapperConfig<SerializationConfig>
/*     */ {
/* 373 */   protected static final int DEFAULT_FEATURE_FLAGS = Feature.collectDefaults();
/*     */   protected ClassIntrospector<? extends BeanDescription> _classIntrospector;
/*     */   protected AnnotationIntrospector _annotationIntrospector;
/* 394 */   protected int _featureFlags = DEFAULT_FEATURE_FLAGS;
/*     */ 
/* 406 */   protected DateFormat _dateFormat = StdDateFormat.instance;
/*     */ 
/* 420 */   protected JsonSerialize.Inclusion _serializationInclusion = null;
/*     */   protected Class<?> _serializationView;
/*     */   protected HashMap<ClassKey, Class<?>> _mixInAnnotations;
/*     */   protected boolean _mixInAnnotationsShared;
/*     */   protected final TypeResolverBuilder<?> _typer;
/*     */   protected VisibilityChecker<?> _visibilityChecker;
/*     */   protected SubtypeResolver _subtypeResolver;
/*     */   protected FilterProvider _filterProvider;
/*     */ 
/*     */   public SerializationConfig(ClassIntrospector<? extends BeanDescription> intr, AnnotationIntrospector annIntr, VisibilityChecker<?> vc, SubtypeResolver subtypeResolver)
/*     */   {
/* 500 */     this._classIntrospector = intr;
/* 501 */     this._annotationIntrospector = annIntr;
/* 502 */     this._typer = null;
/* 503 */     this._visibilityChecker = vc;
/* 504 */     this._subtypeResolver = subtypeResolver;
/* 505 */     this._filterProvider = null;
/*     */   }
/*     */ 
/*     */   protected SerializationConfig(SerializationConfig src, HashMap<ClassKey, Class<?>> mixins, TypeResolverBuilder<?> typer, VisibilityChecker<?> vc, SubtypeResolver subtypeResolver, FilterProvider filterProvider)
/*     */   {
/* 518 */     this._classIntrospector = src._classIntrospector;
/* 519 */     this._annotationIntrospector = src._annotationIntrospector;
/* 520 */     this._featureFlags = src._featureFlags;
/* 521 */     this._dateFormat = src._dateFormat;
/* 522 */     this._serializationInclusion = src._serializationInclusion;
/* 523 */     this._serializationView = src._serializationView;
/* 524 */     this._mixInAnnotations = mixins;
/* 525 */     this._typer = typer;
/* 526 */     this._visibilityChecker = vc;
/* 527 */     this._subtypeResolver = subtypeResolver;
/* 528 */     this._filterProvider = filterProvider;
/*     */   }
/*     */ 
/*     */   protected SerializationConfig(SerializationConfig src, FilterProvider filterProvider)
/*     */   {
/* 539 */     this._classIntrospector = src._classIntrospector;
/* 540 */     this._annotationIntrospector = src._annotationIntrospector;
/* 541 */     this._featureFlags = src._featureFlags;
/* 542 */     this._dateFormat = src._dateFormat;
/* 543 */     this._serializationInclusion = src._serializationInclusion;
/* 544 */     this._serializationView = src._serializationView;
/* 545 */     this._mixInAnnotations = src._mixInAnnotations;
/* 546 */     this._typer = src._typer;
/* 547 */     this._visibilityChecker = src._visibilityChecker;
/* 548 */     this._subtypeResolver = src._subtypeResolver;
/* 549 */     this._filterProvider = filterProvider;
/*     */   }
/*     */ 
/*     */   public SerializationConfig withFilters(FilterProvider filterProvider) {
/* 553 */     return new SerializationConfig(this, filterProvider);
/*     */   }
/*     */ 
/*     */   public SerializationConfig createUnshared(TypeResolverBuilder<?> typer, VisibilityChecker<?> vc, SubtypeResolver subtypeResolver, FilterProvider filterProvider)
/*     */   {
/* 566 */     HashMap mixins = this._mixInAnnotations;
/* 567 */     this._mixInAnnotationsShared = true;
/* 568 */     return new SerializationConfig(this, mixins, typer, vc, subtypeResolver, filterProvider);
/*     */   }
/*     */ 
/*     */   public void fromAnnotations(Class<?> cls)
/*     */   {
/* 604 */     AnnotatedClass ac = AnnotatedClass.construct(cls, this._annotationIntrospector, null);
/* 605 */     this._visibilityChecker = this._annotationIntrospector.findAutoDetectVisibility(ac, this._visibilityChecker);
/*     */ 
/* 608 */     JsonSerialize.Inclusion incl = this._annotationIntrospector.findSerializationInclusion(ac, null);
/* 609 */     if (incl != this._serializationInclusion) {
/* 610 */       setSerializationInclusion(incl);
/*     */     }
/*     */ 
/* 613 */     JsonSerialize.Typing typing = this._annotationIntrospector.findSerializationTyping(ac);
/* 614 */     if (typing != null)
/* 615 */       set(Feature.USE_STATIC_TYPING, typing == JsonSerialize.Typing.STATIC);
/*     */   }
/*     */ 
/*     */   public SerializationConfig createUnshared(TypeResolverBuilder<?> typer, VisibilityChecker<?> vc, SubtypeResolver subtypeResolver)
/*     */   {
/* 631 */     HashMap mixins = this._mixInAnnotations;
/* 632 */     this._mixInAnnotationsShared = true;
/* 633 */     return new SerializationConfig(this, mixins, typer, vc, subtypeResolver, null);
/*     */   }
/*     */ 
/*     */   public void setIntrospector(ClassIntrospector<? extends BeanDescription> i)
/*     */   {
/* 638 */     this._classIntrospector = i;
/*     */   }
/*     */ 
/*     */   public AnnotationIntrospector getAnnotationIntrospector()
/*     */   {
/* 651 */     if (isEnabled(Feature.USE_ANNOTATIONS)) {
/* 652 */       return this._annotationIntrospector;
/*     */     }
/* 654 */     return AnnotationIntrospector.nopInstance();
/*     */   }
/*     */ 
/*     */   public void setAnnotationIntrospector(AnnotationIntrospector ai)
/*     */   {
/* 659 */     this._annotationIntrospector = ai;
/*     */   }
/*     */ 
/*     */   public void insertAnnotationIntrospector(AnnotationIntrospector introspector)
/*     */   {
/* 665 */     this._annotationIntrospector = AnnotationIntrospector.Pair.create(introspector, this._annotationIntrospector);
/*     */   }
/*     */ 
/*     */   public void appendAnnotationIntrospector(AnnotationIntrospector introspector)
/*     */   {
/* 671 */     this._annotationIntrospector = AnnotationIntrospector.Pair.create(this._annotationIntrospector, introspector);
/*     */   }
/*     */ 
/*     */   public void setMixInAnnotations(Map<Class<?>, Class<?>> sourceMixins)
/*     */   {
/* 693 */     HashMap mixins = null;
/* 694 */     if ((sourceMixins != null) && (sourceMixins.size() > 0)) {
/* 695 */       mixins = new HashMap(sourceMixins.size());
/* 696 */       for (Map.Entry en : sourceMixins.entrySet()) {
/* 697 */         mixins.put(new ClassKey((Class)en.getKey()), en.getValue());
/*     */       }
/*     */     }
/* 700 */     this._mixInAnnotationsShared = false;
/* 701 */     this._mixInAnnotations = mixins;
/*     */   }
/*     */ 
/*     */   public void addMixInAnnotations(Class<?> target, Class<?> mixinSource)
/*     */   {
/* 707 */     if ((this._mixInAnnotations == null) || (this._mixInAnnotationsShared)) {
/* 708 */       this._mixInAnnotationsShared = false;
/* 709 */       this._mixInAnnotations = new HashMap();
/*     */     }
/* 711 */     this._mixInAnnotations.put(new ClassKey(target), mixinSource);
/*     */   }
/*     */ 
/*     */   public Class<?> findMixInClassFor(Class<?> cls)
/*     */   {
/* 719 */     return this._mixInAnnotations == null ? null : (Class)this._mixInAnnotations.get(new ClassKey(cls));
/*     */   }
/*     */ 
/*     */   public DateFormat getDateFormat() {
/* 723 */     return this._dateFormat;
/*     */   }
/*     */ 
/*     */   public void setDateFormat(DateFormat df)
/*     */   {
/* 735 */     this._dateFormat = df;
/*     */ 
/* 737 */     set(Feature.WRITE_DATES_AS_TIMESTAMPS, df == null);
/*     */   }
/*     */ 
/*     */   public TypeResolverBuilder<?> getDefaultTyper(JavaType baseType)
/*     */   {
/* 742 */     return this._typer;
/*     */   }
/*     */ 
/*     */   public VisibilityChecker<?> getDefaultVisibilityChecker()
/*     */   {
/* 747 */     return this._visibilityChecker;
/*     */   }
/*     */ 
/*     */   public <T extends BeanDescription> T introspectClassAnnotations(Class<?> cls)
/*     */   {
/* 758 */     return this._classIntrospector.forClassAnnotations(this, cls, this);
/*     */   }
/*     */ 
/*     */   public <T extends BeanDescription> T introspectDirectClassAnnotations(Class<?> cls)
/*     */   {
/* 770 */     return this._classIntrospector.forDirectClassAnnotations(this, cls, this);
/*     */   }
/*     */ 
/*     */   public void enable(Feature f)
/*     */   {
/* 783 */     this._featureFlags |= f.getMask();
/*     */   }
/*     */ 
/*     */   public void disable(Feature f)
/*     */   {
/* 790 */     this._featureFlags &= (f.getMask() ^ 0xFFFFFFFF);
/*     */   }
/*     */ 
/*     */   public void set(Feature f, boolean state)
/*     */   {
/* 798 */     if (state)
/* 799 */       enable(f);
/*     */     else
/* 801 */       disable(f);
/*     */   }
/*     */ 
/*     */   public final boolean isEnabled(Feature f)
/*     */   {
/* 811 */     return (this._featureFlags & f.getMask()) != 0;
/*     */   }
/*     */ 
/*     */   public <T extends BeanDescription> T introspect(JavaType type)
/*     */   {
/* 826 */     return this._classIntrospector.forSerialization(this, type, this);
/*     */   }
/*     */ 
/*     */   public SubtypeResolver getSubtypeResolver()
/*     */   {
/* 839 */     if (this._subtypeResolver == null) {
/* 840 */       this._subtypeResolver = new StdSubtypeResolver();
/*     */     }
/* 842 */     return this._subtypeResolver;
/*     */   }
/*     */ 
/*     */   public void setSubtypeResolver(SubtypeResolver r)
/*     */   {
/* 849 */     this._subtypeResolver = r;
/*     */   }
/*     */ 
/*     */   public Class<?> getSerializationView()
/*     */   {
/* 864 */     return this._serializationView;
/*     */   }
/*     */ 
/*     */   public JsonSerialize.Inclusion getSerializationInclusion() {
/* 868 */     if (this._serializationInclusion != null) {
/* 869 */       return this._serializationInclusion;
/*     */     }
/* 871 */     return isEnabled(Feature.WRITE_NULL_PROPERTIES) ? JsonSerialize.Inclusion.ALWAYS : JsonSerialize.Inclusion.NON_NULL;
/*     */   }
/*     */ 
/*     */   public void setSerializationInclusion(JsonSerialize.Inclusion props)
/*     */   {
/* 885 */     this._serializationInclusion = props;
/*     */ 
/* 887 */     if (props == JsonSerialize.Inclusion.NON_NULL)
/* 888 */       disable(Feature.WRITE_NULL_PROPERTIES);
/*     */     else
/* 890 */       enable(Feature.WRITE_NULL_PROPERTIES);
/*     */   }
/*     */ 
/*     */   public void setSerializationView(Class<?> view)
/*     */   {
/* 902 */     this._serializationView = view;
/*     */   }
/*     */ 
/*     */   public FilterProvider getFilterProvider()
/*     */   {
/* 914 */     return this._filterProvider;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 925 */     return "[SerializationConfig: flags=0x" + Integer.toHexString(this._featureFlags) + "]";
/*     */   }
/*     */ 
/*     */   public static enum Feature
/*     */   {
/*  55 */     USE_ANNOTATIONS(true), 
/*     */ 
/*  74 */     AUTO_DETECT_GETTERS(true), 
/*     */ 
/*  90 */     AUTO_DETECT_IS_GETTERS(true), 
/*     */ 
/* 107 */     AUTO_DETECT_FIELDS(true), 
/*     */ 
/* 117 */     CAN_OVERRIDE_ACCESS_MODIFIERS(true), 
/*     */ 
/* 139 */     WRITE_NULL_PROPERTIES(true), 
/*     */ 
/* 152 */     USE_STATIC_TYPING(false), 
/*     */ 
/* 170 */     DEFAULT_VIEW_INCLUSION(true), 
/*     */ 
/* 185 */     WRAP_ROOT_VALUE(false), 
/*     */ 
/* 204 */     INDENT_OUTPUT(false), 
/*     */ 
/* 227 */     FAIL_ON_EMPTY_BEANS(true), 
/*     */ 
/* 247 */     WRAP_EXCEPTIONS(true), 
/*     */ 
/* 271 */     CLOSE_CLOSEABLE(false), 
/*     */ 
/* 285 */     FLUSH_AFTER_WRITE_VALUE(true), 
/*     */ 
/* 304 */     WRITE_DATES_AS_TIMESTAMPS(true), 
/*     */ 
/* 314 */     WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS(false), 
/*     */ 
/* 329 */     WRITE_ENUMS_USING_TO_STRING(false), 
/*     */ 
/* 339 */     WRITE_NULL_MAP_VALUES(true);
/*     */ 
/*     */     final boolean _defaultState;
/*     */ 
/*     */     public static int collectDefaults()
/*     */     {
/* 351 */       int flags = 0;
/* 352 */       for (Feature f : values()) {
/* 353 */         if (f.enabledByDefault()) {
/* 354 */           flags |= f.getMask();
/*     */         }
/*     */       }
/* 357 */       return flags;
/*     */     }
/*     */ 
/*     */     private Feature(boolean defaultState) {
/* 361 */       this._defaultState = defaultState;
/*     */     }
/*     */     public boolean enabledByDefault() {
/* 364 */       return this._defaultState;
/*     */     }
/* 366 */     public int getMask() { return 1 << ordinal();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.SerializationConfig
 * JD-Core Version:    0.6.0
 */