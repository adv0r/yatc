/*     */ package org.codehaus.jackson.map;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.codehaus.jackson.Base64Variant;
/*     */ import org.codehaus.jackson.Base64Variants;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedClass;
/*     */ import org.codehaus.jackson.map.introspect.NopAnnotationIntrospector;
/*     */ import org.codehaus.jackson.map.introspect.VisibilityChecker;
/*     */ import org.codehaus.jackson.map.jsontype.SubtypeResolver;
/*     */ import org.codehaus.jackson.map.jsontype.TypeResolverBuilder;
/*     */ import org.codehaus.jackson.map.jsontype.impl.StdSubtypeResolver;
/*     */ import org.codehaus.jackson.map.type.ClassKey;
/*     */ import org.codehaus.jackson.map.util.LinkedNode;
/*     */ import org.codehaus.jackson.map.util.StdDateFormat;
/*     */ import org.codehaus.jackson.node.JsonNodeFactory;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class DeserializationConfig
/*     */   implements MapperConfig<DeserializationConfig>
/*     */ {
/* 322 */   protected static final int DEFAULT_FEATURE_FLAGS = Feature.collectDefaults();
/*     */ 
/* 324 */   protected static final DateFormat DEFAULT_DATE_FORMAT = StdDateFormat.instance;
/*     */   protected ClassIntrospector<? extends BeanDescription> _classIntrospector;
/*     */   protected AnnotationIntrospector _annotationIntrospector;
/* 347 */   protected int _featureFlags = DEFAULT_FEATURE_FLAGS;
/*     */   protected LinkedNode<DeserializationProblemHandler> _problemHandlers;
/* 363 */   protected DateFormat _dateFormat = DEFAULT_DATE_FORMAT;
/*     */   protected HashMap<ClassKey, Class<?>> _mixInAnnotations;
/*     */   protected boolean _mixInAnnotationsShared;
/*     */   protected final TypeResolverBuilder<?> _typer;
/*     */   protected VisibilityChecker<?> _visibilityChecker;
/*     */   protected SubtypeResolver _subtypeResolver;
/*     */   protected AbstractTypeResolver _abstractTypeResolver;
/*     */   protected JsonNodeFactory _nodeFactory;
/*     */ 
/*     */   public DeserializationConfig(ClassIntrospector<? extends BeanDescription> intr, AnnotationIntrospector annIntr, VisibilityChecker<?> vc, SubtypeResolver subtypeResolver)
/*     */   {
/* 444 */     this._classIntrospector = intr;
/* 445 */     this._annotationIntrospector = annIntr;
/* 446 */     this._typer = null;
/* 447 */     this._visibilityChecker = vc;
/* 448 */     this._subtypeResolver = subtypeResolver;
/* 449 */     this._nodeFactory = JsonNodeFactory.instance;
/*     */   }
/*     */ 
/*     */   protected DeserializationConfig(DeserializationConfig src, HashMap<ClassKey, Class<?>> mixins, TypeResolverBuilder<?> typer, VisibilityChecker<?> vc, SubtypeResolver subtypeResolver)
/*     */   {
/* 458 */     this._classIntrospector = src._classIntrospector;
/* 459 */     this._annotationIntrospector = src._annotationIntrospector;
/* 460 */     this._abstractTypeResolver = src._abstractTypeResolver;
/* 461 */     this._featureFlags = src._featureFlags;
/* 462 */     this._problemHandlers = src._problemHandlers;
/* 463 */     this._dateFormat = src._dateFormat;
/* 464 */     this._nodeFactory = src._nodeFactory;
/* 465 */     this._mixInAnnotations = mixins;
/* 466 */     this._typer = typer;
/* 467 */     this._visibilityChecker = vc;
/* 468 */     this._subtypeResolver = subtypeResolver;
/*     */   }
/*     */ 
/*     */   public void enable(Feature f)
/*     */   {
/* 481 */     this._featureFlags |= f.getMask();
/*     */   }
/*     */ 
/*     */   public void disable(Feature f)
/*     */   {
/* 488 */     this._featureFlags &= (f.getMask() ^ 0xFFFFFFFF);
/*     */   }
/*     */ 
/*     */   public void set(Feature f, boolean state)
/*     */   {
/* 496 */     if (state)
/* 497 */       enable(f);
/*     */     else
/* 499 */       disable(f);
/*     */   }
/*     */ 
/*     */   public final boolean isEnabled(Feature f)
/*     */   {
/* 507 */     return (this._featureFlags & f.getMask()) != 0;
/*     */   }
/*     */ 
/*     */   public void fromAnnotations(Class<?> cls)
/*     */   {
/* 548 */     AnnotatedClass ac = AnnotatedClass.construct(cls, this._annotationIntrospector, null);
/*     */ 
/* 550 */     this._visibilityChecker = this._annotationIntrospector.findAutoDetectVisibility(ac, this._visibilityChecker);
/*     */   }
/*     */ 
/*     */   public DeserializationConfig createUnshared(TypeResolverBuilder<?> typer, VisibilityChecker<?> vc, SubtypeResolver subtypeResolver)
/*     */   {
/* 565 */     HashMap mixins = this._mixInAnnotations;
/* 566 */     this._mixInAnnotationsShared = true;
/* 567 */     return new DeserializationConfig(this, mixins, typer, vc, subtypeResolver);
/*     */   }
/*     */ 
/*     */   public DeserializationConfig createUnshared(JsonNodeFactory nf)
/*     */   {
/* 578 */     DeserializationConfig config = createUnshared(this._typer, this._visibilityChecker, this._subtypeResolver);
/* 579 */     config.setNodeFactory(nf);
/* 580 */     return config;
/*     */   }
/*     */ 
/*     */   public void setIntrospector(ClassIntrospector<? extends BeanDescription> i)
/*     */   {
/* 585 */     this._classIntrospector = i;
/*     */   }
/*     */ 
/*     */   public AnnotationIntrospector getAnnotationIntrospector()
/*     */   {
/* 598 */     if (isEnabled(Feature.USE_ANNOTATIONS)) {
/* 599 */       return this._annotationIntrospector;
/*     */     }
/* 601 */     return NopAnnotationIntrospector.instance;
/*     */   }
/*     */ 
/*     */   public void setAnnotationIntrospector(AnnotationIntrospector introspector)
/*     */   {
/* 607 */     this._annotationIntrospector = introspector;
/*     */   }
/*     */ 
/*     */   public void insertAnnotationIntrospector(AnnotationIntrospector introspector)
/*     */   {
/* 613 */     this._annotationIntrospector = AnnotationIntrospector.Pair.create(introspector, this._annotationIntrospector);
/*     */   }
/*     */ 
/*     */   public void appendAnnotationIntrospector(AnnotationIntrospector introspector)
/*     */   {
/* 619 */     this._annotationIntrospector = AnnotationIntrospector.Pair.create(this._annotationIntrospector, introspector);
/*     */   }
/*     */ 
/*     */   public void setMixInAnnotations(Map<Class<?>, Class<?>> sourceMixins)
/*     */   {
/* 641 */     HashMap mixins = null;
/* 642 */     if ((sourceMixins != null) && (sourceMixins.size() > 0)) {
/* 643 */       mixins = new HashMap(sourceMixins.size());
/* 644 */       for (Map.Entry en : sourceMixins.entrySet()) {
/* 645 */         mixins.put(new ClassKey((Class)en.getKey()), en.getValue());
/*     */       }
/*     */     }
/* 648 */     this._mixInAnnotationsShared = false;
/* 649 */     this._mixInAnnotations = mixins;
/*     */   }
/*     */ 
/*     */   public void addMixInAnnotations(Class<?> target, Class<?> mixinSource)
/*     */   {
/* 655 */     if ((this._mixInAnnotations == null) || (this._mixInAnnotationsShared)) {
/* 656 */       this._mixInAnnotationsShared = false;
/* 657 */       this._mixInAnnotations = new HashMap();
/*     */     }
/* 659 */     this._mixInAnnotations.put(new ClassKey(target), mixinSource);
/*     */   }
/*     */ 
/*     */   public Class<?> findMixInClassFor(Class<?> cls)
/*     */   {
/* 667 */     return this._mixInAnnotations == null ? null : (Class)this._mixInAnnotations.get(new ClassKey(cls));
/*     */   }
/*     */ 
/*     */   public DateFormat getDateFormat() {
/* 671 */     return this._dateFormat;
/*     */   }
/*     */ 
/*     */   public void setDateFormat(DateFormat df)
/*     */   {
/* 680 */     this._dateFormat = (df == null ? StdDateFormat.instance : df);
/*     */   }
/*     */ 
/*     */   public VisibilityChecker<?> getDefaultVisibilityChecker()
/*     */   {
/* 685 */     return this._visibilityChecker;
/*     */   }
/*     */ 
/*     */   public TypeResolverBuilder<?> getDefaultTyper(JavaType baseType)
/*     */   {
/* 690 */     return this._typer;
/*     */   }
/*     */ 
/*     */   public SubtypeResolver getSubtypeResolver()
/*     */   {
/* 697 */     if (this._subtypeResolver == null) {
/* 698 */       this._subtypeResolver = new StdSubtypeResolver();
/*     */     }
/* 700 */     return this._subtypeResolver;
/*     */   }
/*     */ 
/*     */   public void setSubtypeResolver(SubtypeResolver r)
/*     */   {
/* 707 */     this._subtypeResolver = r;
/*     */   }
/*     */ 
/*     */   public <T extends BeanDescription> T introspectClassAnnotations(Class<?> cls)
/*     */   {
/* 718 */     return this._classIntrospector.forClassAnnotations(this, cls, this);
/*     */   }
/*     */ 
/*     */   public <T extends BeanDescription> T introspectDirectClassAnnotations(Class<?> cls)
/*     */   {
/* 730 */     return this._classIntrospector.forDirectClassAnnotations(this, cls, this);
/*     */   }
/*     */ 
/*     */   public LinkedNode<DeserializationProblemHandler> getProblemHandlers()
/*     */   {
/* 745 */     return this._problemHandlers;
/*     */   }
/*     */ 
/*     */   public void addHandler(DeserializationProblemHandler h)
/*     */   {
/* 757 */     if (!LinkedNode.contains(this._problemHandlers, h))
/* 758 */       this._problemHandlers = new LinkedNode(h, this._problemHandlers);
/*     */   }
/*     */ 
/*     */   public void clearHandlers()
/*     */   {
/* 770 */     this._problemHandlers = null;
/*     */   }
/*     */ 
/*     */   public <T extends BeanDescription> T introspect(JavaType type)
/*     */   {
/* 787 */     return this._classIntrospector.forDeserialization(this, type, this);
/*     */   }
/*     */ 
/*     */   public <T extends BeanDescription> T introspectForCreation(JavaType type)
/*     */   {
/* 796 */     return this._classIntrospector.forCreation(this, type, this);
/*     */   }
/*     */ 
/*     */   public AbstractTypeResolver getAbstractTypeResolver()
/*     */   {
/* 813 */     return this._abstractTypeResolver;
/*     */   }
/*     */ 
/*     */   public void setAbstractTypeResolver(AbstractTypeResolver atr)
/*     */   {
/* 820 */     this._abstractTypeResolver = atr;
/*     */   }
/*     */ 
/*     */   public Base64Variant getBase64Variant()
/*     */   {
/* 836 */     return Base64Variants.getDefaultVariant();
/*     */   }
/*     */ 
/*     */   public void setNodeFactory(JsonNodeFactory nf)
/*     */   {
/* 843 */     this._nodeFactory = nf;
/*     */   }
/*     */ 
/*     */   public final JsonNodeFactory getNodeFactory()
/*     */   {
/* 850 */     return this._nodeFactory;
/*     */   }
/*     */ 
/*     */   public static enum Feature
/*     */   {
/*  57 */     USE_ANNOTATIONS(true), 
/*     */ 
/*  73 */     AUTO_DETECT_SETTERS(true), 
/*     */ 
/*  89 */     AUTO_DETECT_CREATORS(true), 
/*     */ 
/* 106 */     AUTO_DETECT_FIELDS(true), 
/*     */ 
/* 124 */     USE_GETTERS_AS_SETTERS(true), 
/*     */ 
/* 134 */     CAN_OVERRIDE_ACCESS_MODIFIERS(true), 
/*     */ 
/* 156 */     USE_BIG_DECIMAL_FOR_FLOATS(false), 
/*     */ 
/* 174 */     USE_BIG_INTEGER_FOR_INTS(false), 
/*     */ 
/* 190 */     READ_ENUMS_USING_TO_STRING(false), 
/*     */ 
/* 215 */     FAIL_ON_UNKNOWN_PROPERTIES(true), 
/*     */ 
/* 230 */     FAIL_ON_NULL_FOR_PRIMITIVES(false), 
/*     */ 
/* 248 */     FAIL_ON_NUMBERS_FOR_ENUMS(false), 
/*     */ 
/* 268 */     WRAP_EXCEPTIONS(true), 
/*     */ 
/* 288 */     WRAP_ROOT_VALUE(false);
/*     */ 
/*     */     final boolean _defaultState;
/*     */ 
/*     */     public static int collectDefaults()
/*     */     {
/* 300 */       int flags = 0;
/* 301 */       for (Feature f : values()) {
/* 302 */         if (f.enabledByDefault()) {
/* 303 */           flags |= f.getMask();
/*     */         }
/*     */       }
/* 306 */       return flags;
/*     */     }
/*     */ 
/*     */     private Feature(boolean defaultState) {
/* 310 */       this._defaultState = defaultState;
/*     */     }
/*     */     public boolean enabledByDefault() {
/* 313 */       return this._defaultState;
/*     */     }
/* 315 */     public int getMask() { return 1 << ordinal();
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.DeserializationConfig
 * JD-Core Version:    0.6.0
 */