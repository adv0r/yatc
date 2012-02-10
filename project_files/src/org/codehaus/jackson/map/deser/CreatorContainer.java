/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.HashMap;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedConstructor;
/*     */ import org.codehaus.jackson.map.introspect.AnnotatedMethod;
/*     */ import org.codehaus.jackson.map.util.ClassUtil;
/*     */ 
/*     */ public class CreatorContainer
/*     */ {
/*     */   final Class<?> _beanClass;
/*     */   final boolean _canFixAccess;
/*     */   protected Constructor<?> _defaultConstructor;
/*     */   AnnotatedMethod _strFactory;
/*     */   AnnotatedMethod _intFactory;
/*     */   AnnotatedMethod _longFactory;
/*     */   AnnotatedMethod _delegatingFactory;
/*     */   AnnotatedMethod _propertyBasedFactory;
/*  24 */   SettableBeanProperty[] _propertyBasedFactoryProperties = null;
/*     */   AnnotatedConstructor _strConstructor;
/*     */   AnnotatedConstructor _intConstructor;
/*     */   AnnotatedConstructor _longConstructor;
/*     */   AnnotatedConstructor _delegatingConstructor;
/*     */   AnnotatedConstructor _propertyBasedConstructor;
/*  29 */   SettableBeanProperty[] _propertyBasedConstructorProperties = null;
/*     */ 
/*     */   public CreatorContainer(Class<?> beanClass, boolean canFixAccess) {
/*  32 */     this._canFixAccess = canFixAccess;
/*  33 */     this._beanClass = beanClass;
/*     */   }
/*     */ 
/*     */   public void setDefaultConstructor(Constructor<?> ctor)
/*     */   {
/*  43 */     this._defaultConstructor = ctor;
/*     */   }
/*     */ 
/*     */   public void addStringConstructor(AnnotatedConstructor ctor) {
/*  47 */     this._strConstructor = verifyNonDup(ctor, this._strConstructor, "String");
/*     */   }
/*     */   public void addIntConstructor(AnnotatedConstructor ctor) {
/*  50 */     this._intConstructor = verifyNonDup(ctor, this._intConstructor, "int");
/*     */   }
/*     */   public void addLongConstructor(AnnotatedConstructor ctor) {
/*  53 */     this._longConstructor = verifyNonDup(ctor, this._longConstructor, "long");
/*     */   }
/*     */ 
/*     */   public void addDelegatingConstructor(AnnotatedConstructor ctor) {
/*  57 */     this._delegatingConstructor = verifyNonDup(ctor, this._delegatingConstructor, "long");
/*     */   }
/*     */ 
/*     */   public void addPropertyConstructor(AnnotatedConstructor ctor, SettableBeanProperty[] properties)
/*     */   {
/*  62 */     this._propertyBasedConstructor = verifyNonDup(ctor, this._propertyBasedConstructor, "property-based");
/*     */ 
/*  64 */     if (properties.length > 1) {
/*  65 */       HashMap names = new HashMap();
/*  66 */       int i = 0; for (int len = properties.length; i < len; i++) {
/*  67 */         String name = properties[i].getName();
/*  68 */         Integer old = (Integer)names.put(name, Integer.valueOf(i));
/*  69 */         if (old != null) {
/*  70 */           throw new IllegalArgumentException("Duplicate creator property \"" + name + "\" (index " + old + " vs " + i + ")");
/*     */         }
/*     */       }
/*     */     }
/*  74 */     this._propertyBasedConstructorProperties = properties;
/*     */   }
/*     */ 
/*     */   public void addStringFactory(AnnotatedMethod factory) {
/*  78 */     this._strFactory = verifyNonDup(factory, this._strFactory, "String");
/*     */   }
/*     */   public void addIntFactory(AnnotatedMethod factory) {
/*  81 */     this._intFactory = verifyNonDup(factory, this._intFactory, "int");
/*     */   }
/*     */   public void addLongFactory(AnnotatedMethod factory) {
/*  84 */     this._longFactory = verifyNonDup(factory, this._longFactory, "long");
/*     */   }
/*     */ 
/*     */   public void addDelegatingFactory(AnnotatedMethod factory) {
/*  88 */     this._delegatingFactory = verifyNonDup(factory, this._delegatingFactory, "long");
/*     */   }
/*     */ 
/*     */   public void addPropertyFactory(AnnotatedMethod factory, SettableBeanProperty[] properties)
/*     */   {
/*  93 */     this._propertyBasedFactory = verifyNonDup(factory, this._propertyBasedFactory, "property-based");
/*  94 */     this._propertyBasedFactoryProperties = properties;
/*     */   }
/*     */ 
/*     */   public Constructor<?> getDefaultConstructor()
/*     */   {
/* 103 */     return this._defaultConstructor;
/*     */   }
/*     */ 
/*     */   public Creator.StringBased stringCreator() {
/* 107 */     if ((this._strConstructor == null) && (this._strFactory == null)) {
/* 108 */       return null;
/*     */     }
/* 110 */     return new Creator.StringBased(this._beanClass, this._strConstructor, this._strFactory);
/*     */   }
/*     */ 
/*     */   public Creator.NumberBased numberCreator()
/*     */   {
/* 115 */     if ((this._intConstructor == null) && (this._intFactory == null) && (this._longConstructor == null) && (this._longFactory == null))
/*     */     {
/* 117 */       return null;
/*     */     }
/* 119 */     return new Creator.NumberBased(this._beanClass, this._intConstructor, this._intFactory, this._longConstructor, this._longFactory);
/*     */   }
/*     */ 
/*     */   public Creator.Delegating delegatingCreator()
/*     */   {
/* 125 */     if ((this._delegatingConstructor == null) && (this._delegatingFactory == null)) {
/* 126 */       return null;
/*     */     }
/* 128 */     return new Creator.Delegating(this._delegatingConstructor, this._delegatingFactory);
/*     */   }
/*     */ 
/*     */   public Creator.PropertyBased propertyBasedCreator()
/*     */   {
/* 133 */     if ((this._propertyBasedConstructor == null) && (this._propertyBasedFactory == null)) {
/* 134 */       return null;
/*     */     }
/* 136 */     return new Creator.PropertyBased(this._propertyBasedConstructor, this._propertyBasedConstructorProperties, this._propertyBasedFactory, this._propertyBasedFactoryProperties);
/*     */   }
/*     */ 
/*     */   protected AnnotatedConstructor verifyNonDup(AnnotatedConstructor newOne, AnnotatedConstructor oldOne, String type)
/*     */   {
/* 149 */     if (oldOne != null) {
/* 150 */       throw new IllegalArgumentException("Conflicting " + type + " constructors: already had " + oldOne + ", encountered " + newOne);
/*     */     }
/* 152 */     if (this._canFixAccess) {
/* 153 */       ClassUtil.checkAndFixAccess(newOne.getAnnotated());
/*     */     }
/* 155 */     return newOne;
/*     */   }
/*     */ 
/*     */   protected AnnotatedMethod verifyNonDup(AnnotatedMethod newOne, AnnotatedMethod oldOne, String type)
/*     */   {
/* 161 */     if (oldOne != null) {
/* 162 */       throw new IllegalArgumentException("Conflicting " + type + " factory methods: already had " + oldOne + ", encountered " + newOne);
/*     */     }
/* 164 */     if (this._canFixAccess) {
/* 165 */       ClassUtil.checkAndFixAccess(newOne.getAnnotated());
/*     */     }
/* 167 */     return newOne;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.CreatorContainer
 * JD-Core Version:    0.6.0
 */