/*     */ package org.codehaus.jackson.map.introspect;
/*     */ 
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.codehaus.jackson.map.AnnotationIntrospector;
/*     */ import org.codehaus.jackson.map.AnnotationIntrospector.ReferenceProperty;
/*     */ import org.codehaus.jackson.map.BeanDescription;
/*     */ import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
/*     */ import org.codehaus.jackson.map.type.TypeBindings;
/*     */ import org.codehaus.jackson.map.util.Annotations;
/*     */ import org.codehaus.jackson.map.util.ClassUtil;
/*     */ import org.codehaus.jackson.type.JavaType;
/*     */ 
/*     */ public class BasicBeanDescription extends BeanDescription
/*     */ {
/*     */   protected final AnnotatedClass _classInfo;
/*     */   protected final AnnotationIntrospector _annotationIntrospector;
/*     */   protected TypeBindings _bindings;
/*     */ 
/*     */   public BasicBeanDescription(JavaType type, AnnotatedClass ac, AnnotationIntrospector ai)
/*     */   {
/*  53 */     super(type);
/*  54 */     this._classInfo = ac;
/*  55 */     this._annotationIntrospector = ai;
/*     */   }
/*     */ 
/*     */   public boolean hasKnownClassAnnotations()
/*     */   {
/*  70 */     return this._classInfo.hasAnnotations();
/*     */   }
/*     */ 
/*     */   public Annotations getClassAnnotations()
/*     */   {
/*  75 */     return this._classInfo.getAnnotations();
/*     */   }
/*     */ 
/*     */   public TypeBindings bindingsForBeanType()
/*     */   {
/*  81 */     if (this._bindings == null) {
/*  82 */       this._bindings = new TypeBindings(this._type);
/*     */     }
/*  84 */     return this._bindings;
/*     */   }
/*     */ 
/*     */   public AnnotatedClass getClassInfo()
/*     */   {
/*  93 */     return this._classInfo;
/*     */   }
/*     */ 
/*     */   public AnnotatedMethod findMethod(String name, Class<?>[] paramTypes) {
/*  97 */     return this._classInfo.findMethod(name, paramTypes);
/*     */   }
/*     */ 
/*     */   public Object instantiateBean(boolean fixAccess)
/*     */   {
/* 110 */     AnnotatedConstructor ac = this._classInfo.getDefaultConstructor();
/* 111 */     if (ac == null) {
/* 112 */       return null;
/*     */     }
/* 114 */     if (fixAccess)
/* 115 */       ac.fixAccess(); Throwable t;
/*     */     try {
/* 118 */       return ac.getAnnotated().newInstance(new Object[0]);
/*     */     } catch (Exception e) {
/* 120 */       t = e;
/* 121 */       while (t.getCause() != null) {
/* 122 */         t = t.getCause();
/*     */       }
/* 124 */       if ((t instanceof Error)) throw ((Error)t);
/* 125 */       if ((t instanceof RuntimeException)) throw ((RuntimeException)t); 
/*     */     }
/* 126 */     throw new IllegalArgumentException("Failed to instantiate bean of type " + this._classInfo.getAnnotated().getName() + ": (" + t.getClass().getName() + ") " + t.getMessage(), t);
/*     */   }
/*     */ 
/*     */   public LinkedHashMap<String, AnnotatedMethod> findGetters(VisibilityChecker<?> visibilityChecker, Collection<String> ignoredProperties)
/*     */   {
/* 146 */     LinkedHashMap results = new LinkedHashMap();
/* 147 */     for (AnnotatedMethod am : this._classInfo.memberMethods())
/*     */     {
/* 152 */       if (am.getParameterCount() != 0)
/*     */       {
/*     */         continue;
/*     */       }
/*     */ 
/* 159 */       String propName = this._annotationIntrospector.findGettablePropertyName(am);
/* 160 */       if (propName != null)
/*     */       {
/* 165 */         if (propName.length() == 0) {
/* 166 */           propName = okNameForAnyGetter(am, am.getName());
/* 167 */           if (propName == null)
/* 168 */             propName = am.getName();
/*     */         }
/*     */       }
/*     */       else {
/* 172 */         propName = am.getName();
/*     */ 
/* 174 */         if (propName.startsWith("get")) {
/* 175 */           if (!visibilityChecker.isGetterVisible(am)) {
/*     */             continue;
/*     */           }
/* 178 */           propName = okNameForGetter(am, propName);
/*     */         } else {
/* 180 */           if (!visibilityChecker.isIsGetterVisible(am)) {
/*     */             continue;
/*     */           }
/* 183 */           propName = okNameForIsGetter(am, propName);
/*     */         }
/*     */ 
/* 186 */         if ((propName == null) || 
/* 188 */           (this._annotationIntrospector.hasAnyGetterAnnotation(am)))
/*     */           continue;
/*     */       }
/* 191 */       if ((ignoredProperties != null) && 
/* 192 */         (ignoredProperties.contains(propName)))
/*     */       {
/*     */         continue;
/*     */       }
/*     */ 
/* 200 */       AnnotatedMethod old = (AnnotatedMethod)results.put(propName, am);
/* 201 */       if (old != null) {
/* 202 */         String oldDesc = old.getFullName();
/* 203 */         String newDesc = am.getFullName();
/* 204 */         throw new IllegalArgumentException("Conflicting getter definitions for property \"" + propName + "\": " + oldDesc + " vs " + newDesc);
/*     */       }
/*     */     }
/* 207 */     return results;
/*     */   }
/*     */ 
/*     */   public AnnotatedMethod findJsonValueMethod()
/*     */   {
/* 218 */     AnnotatedMethod found = null;
/* 219 */     for (AnnotatedMethod am : this._classInfo.memberMethods())
/*     */     {
/* 221 */       if (!this._annotationIntrospector.hasAsValueAnnotation(am)) {
/*     */         continue;
/*     */       }
/* 224 */       if (found != null) {
/* 225 */         throw new IllegalArgumentException("Multiple methods with active 'as-value' annotation (" + found.getName() + "(), " + am.getName() + ")");
/*     */       }
/*     */ 
/* 232 */       if (!ClassUtil.hasGetterSignature(am.getAnnotated())) {
/* 233 */         throw new IllegalArgumentException("Method " + am.getName() + "() marked with an 'as-value' annotation, but does not have valid getter signature (non-static, takes no args, returns a value)");
/*     */       }
/* 235 */       found = am;
/*     */     }
/* 237 */     return found;
/*     */   }
/*     */ 
/*     */   public Constructor<?> findDefaultConstructor()
/*     */   {
/* 254 */     AnnotatedConstructor ac = this._classInfo.getDefaultConstructor();
/* 255 */     if (ac == null) {
/* 256 */       return null;
/*     */     }
/* 258 */     return ac.getAnnotated();
/*     */   }
/*     */ 
/*     */   public List<AnnotatedConstructor> getConstructors()
/*     */   {
/* 263 */     return this._classInfo.getConstructors();
/*     */   }
/*     */ 
/*     */   public List<AnnotatedMethod> getFactoryMethods()
/*     */   {
/* 269 */     List candidates = this._classInfo.getStaticMethods();
/* 270 */     if (candidates.isEmpty()) {
/* 271 */       return candidates;
/*     */     }
/* 273 */     ArrayList result = new ArrayList();
/* 274 */     for (AnnotatedMethod am : candidates) {
/* 275 */       if (isFactoryMethod(am)) {
/* 276 */         result.add(am);
/*     */       }
/*     */     }
/* 279 */     return result;
/*     */   }
/*     */ 
/*     */   public Constructor<?> findSingleArgConstructor(Class<?>[] argTypes)
/*     */   {
/* 290 */     for (AnnotatedConstructor ac : this._classInfo.getConstructors())
/*     */     {
/* 295 */       if (ac.getParameterCount() == 1) {
/* 296 */         Class actArg = ac.getParameterClass(0);
/* 297 */         for (Class expArg : argTypes) {
/* 298 */           if (expArg == actArg) {
/* 299 */             return ac.getAnnotated();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 304 */     return null;
/*     */   }
/*     */ 
/*     */   public Method findFactoryMethod(Class<?>[] expArgTypes)
/*     */   {
/* 319 */     for (AnnotatedMethod am : this._classInfo.getStaticMethods()) {
/* 320 */       if (isFactoryMethod(am))
/*     */       {
/* 322 */         Class actualArgType = am.getParameterClass(0);
/* 323 */         for (Class expArgType : expArgTypes)
/*     */         {
/* 325 */           if (actualArgType.isAssignableFrom(expArgType)) {
/* 326 */             return am.getAnnotated();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 331 */     return null;
/*     */   }
/*     */ 
/*     */   protected boolean isFactoryMethod(AnnotatedMethod am)
/*     */   {
/* 340 */     Class rt = am.getRawType();
/* 341 */     if (!getBeanClass().isAssignableFrom(rt)) {
/* 342 */       return false;
/*     */     }
/*     */ 
/* 349 */     if (this._annotationIntrospector.hasCreatorAnnotation(am)) {
/* 350 */       return true;
/*     */     }
/*     */ 
/* 353 */     return "valueOf".equals(am.getName());
/*     */   }
/*     */ 
/*     */   public List<String> findCreatorPropertyNames()
/*     */   {
/* 369 */     List names = null;
/*     */ 
/* 371 */     for (int i = 0; i < 2; i++) {
/* 372 */       List l = i == 0 ? getConstructors() : getFactoryMethods();
/*     */ 
/* 374 */       for (AnnotatedWithParams creator : l) {
/* 375 */         int argCount = creator.getParameterCount();
/* 376 */         if (argCount >= 1) {
/* 377 */           String name = this._annotationIntrospector.findPropertyNameForParam(creator.getParameter(0));
/* 378 */           if (name != null) {
/* 379 */             if (names == null) {
/* 380 */               names = new ArrayList();
/*     */             }
/* 382 */             names.add(name);
/* 383 */             for (int p = 1; p < argCount; p++)
/* 384 */               names.add(this._annotationIntrospector.findPropertyNameForParam(creator.getParameter(p))); 
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 388 */     if (names == null) {
/* 389 */       return Collections.emptyList();
/*     */     }
/* 391 */     return names;
/*     */   }
/*     */ 
/*     */   public LinkedHashMap<String, AnnotatedField> findSerializableFields(VisibilityChecker<?> vchecker, Collection<String> ignoredProperties)
/*     */   {
/* 403 */     return _findPropertyFields(vchecker, ignoredProperties, true);
/*     */   }
/*     */ 
/*     */   public JsonSerialize.Inclusion findSerializationInclusion(JsonSerialize.Inclusion defValue)
/*     */   {
/* 420 */     return this._annotationIntrospector.findSerializationInclusion(this._classInfo, defValue);
/*     */   }
/*     */ 
/*     */   public LinkedHashMap<String, AnnotatedMethod> findSetters(VisibilityChecker<?> vchecker)
/*     */   {
/* 432 */     LinkedHashMap results = new LinkedHashMap();
/* 433 */     for (AnnotatedMethod am : this._classInfo.memberMethods())
/*     */     {
/* 437 */       if (am.getParameterCount() != 1)
/*     */       {
/*     */         continue;
/*     */       }
/*     */ 
/* 446 */       String propName = this._annotationIntrospector.findSettablePropertyName(am);
/* 447 */       if (propName != null)
/*     */       {
/* 451 */         if (propName.length() == 0) {
/* 452 */           propName = okNameForSetter(am);
/*     */ 
/* 454 */           if (propName == null)
/* 455 */             propName = am.getName();
/*     */         }
/*     */       }
/*     */       else {
/* 459 */         if (!vchecker.isSetterVisible(am)) {
/*     */           continue;
/*     */         }
/* 462 */         propName = okNameForSetter(am);
/* 463 */         if (propName == null)
/*     */         {
/*     */           continue;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 471 */       AnnotatedMethod old = (AnnotatedMethod)results.put(propName, am);
/* 472 */       if (old != null)
/*     */       {
/* 478 */         if (old.getDeclaringClass() == am.getDeclaringClass()) {
/* 479 */           String oldDesc = old.getFullName();
/* 480 */           String newDesc = am.getFullName();
/* 481 */           throw new IllegalArgumentException("Conflicting setter definitions for property \"" + propName + "\": " + oldDesc + " vs " + newDesc);
/*     */         }
/*     */ 
/* 484 */         results.put(propName, old);
/*     */       }
/*     */     }
/*     */ 
/* 488 */     return results;
/*     */   }
/*     */ 
/*     */   public AnnotatedMethod findAnySetter()
/*     */     throws IllegalArgumentException
/*     */   {
/* 504 */     AnnotatedMethod found = null;
/* 505 */     for (AnnotatedMethod am : this._classInfo.memberMethods()) {
/* 506 */       if (!this._annotationIntrospector.hasAnySetterAnnotation(am)) {
/*     */         continue;
/*     */       }
/* 509 */       if (found != null) {
/* 510 */         throw new IllegalArgumentException("Multiple methods with 'any-setter' annotation (" + found.getName() + "(), " + am.getName() + ")");
/*     */       }
/* 512 */       int pcount = am.getParameterCount();
/* 513 */       if (pcount != 2) {
/* 514 */         throw new IllegalArgumentException("Invalid 'any-setter' annotation on method " + am.getName() + "(): takes " + pcount + " parameters, should take 2");
/*     */       }
/*     */ 
/* 524 */       Class type = am.getParameterClass(0);
/* 525 */       if ((type != String.class) && (type != Object.class)) {
/* 526 */         throw new IllegalArgumentException("Invalid 'any-setter' annotation on method " + am.getName() + "(): first argument not of type String or Object, but " + type.getName());
/*     */       }
/* 528 */       found = am;
/*     */     }
/* 530 */     return found;
/*     */   }
/*     */ 
/*     */   public AnnotatedMethod findAnyGetter()
/*     */     throws IllegalArgumentException
/*     */   {
/* 543 */     AnnotatedMethod found = null;
/* 544 */     for (AnnotatedMethod am : this._classInfo.memberMethods()) {
/* 545 */       if (!this._annotationIntrospector.hasAnyGetterAnnotation(am)) {
/*     */         continue;
/*     */       }
/* 548 */       if (found != null) {
/* 549 */         throw new IllegalArgumentException("Multiple methods with 'any-getter' annotation (" + found.getName() + "(), " + am.getName() + ")");
/*     */       }
/*     */ 
/* 554 */       Class type = am.getRawType();
/* 555 */       if (!Map.class.isAssignableFrom(type)) {
/* 556 */         throw new IllegalArgumentException("Invalid 'any-getter' annotation on method " + am.getName() + "(): return type is not instance of java.util.Map");
/*     */       }
/* 558 */       found = am;
/*     */     }
/* 560 */     return found;
/*     */   }
/*     */ 
/*     */   public Map<String, AnnotatedMember> findBackReferenceProperties()
/*     */   {
/* 570 */     HashMap result = null;
/*     */ 
/* 572 */     for (AnnotatedMethod am : this._classInfo.memberMethods()) {
/* 573 */       if (am.getParameterCount() == 1) {
/* 574 */         AnnotationIntrospector.ReferenceProperty prop = this._annotationIntrospector.findReferenceType(am);
/* 575 */         if ((prop != null) && (prop.isBackReference())) {
/* 576 */           if (result == null) {
/* 577 */             result = new HashMap();
/*     */           }
/* 579 */           if (result.put(prop.getName(), am) != null) {
/* 580 */             throw new IllegalArgumentException("Multiple back-reference properties with name '" + prop.getName() + "'");
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 586 */     for (AnnotatedField af : this._classInfo.fields()) {
/* 587 */       AnnotationIntrospector.ReferenceProperty prop = this._annotationIntrospector.findReferenceType(af);
/* 588 */       if ((prop != null) && (prop.isBackReference())) {
/* 589 */         if (result == null) {
/* 590 */           result = new HashMap();
/*     */         }
/* 592 */         if (result.put(prop.getName(), af) != null) {
/* 593 */           throw new IllegalArgumentException("Multiple back-reference properties with name '" + prop.getName() + "'");
/*     */         }
/*     */       }
/*     */     }
/* 597 */     return result;
/*     */   }
/*     */ 
/*     */   public LinkedHashMap<String, AnnotatedField> findDeserializableFields(VisibilityChecker<?> vchecker, Collection<String> ignoredProperties)
/*     */   {
/* 609 */     return _findPropertyFields(vchecker, ignoredProperties, false);
/*     */   }
/*     */ 
/*     */   public String okNameForAnyGetter(AnnotatedMethod am, String name)
/*     */   {
/* 620 */     String str = okNameForIsGetter(am, name);
/* 621 */     if (str == null) {
/* 622 */       str = okNameForGetter(am, name);
/*     */     }
/* 624 */     return str;
/*     */   }
/*     */ 
/*     */   public String okNameForGetter(AnnotatedMethod am, String name)
/*     */   {
/* 629 */     if (name.startsWith("get"))
/*     */     {
/* 637 */       if ("getCallbacks".equals(name)) {
/* 638 */         if (isCglibGetCallbacks(am))
/* 639 */           return null;
/*     */       }
/* 641 */       else if ("getMetaClass".equals(name))
/*     */       {
/* 645 */         if (isGroovyMetaClassGetter(am)) {
/* 646 */           return null;
/*     */         }
/*     */       }
/* 649 */       return mangleGetterName(am, name.substring(3));
/*     */     }
/* 651 */     return null;
/*     */   }
/*     */ 
/*     */   public String okNameForIsGetter(AnnotatedMethod am, String name)
/*     */   {
/* 656 */     if (name.startsWith("is"))
/*     */     {
/* 658 */       Class rt = am.getRawType();
/* 659 */       if ((rt != Boolean.class) && (rt != Boolean.TYPE)) {
/* 660 */         return null;
/*     */       }
/* 662 */       return mangleGetterName(am, name.substring(2));
/*     */     }
/*     */ 
/* 665 */     return null;
/*     */   }
/*     */ 
/*     */   protected String mangleGetterName(Annotated a, String basename)
/*     */   {
/* 674 */     return manglePropertyName(basename);
/*     */   }
/*     */ 
/*     */   protected boolean isCglibGetCallbacks(AnnotatedMethod am)
/*     */   {
/* 690 */     Class rt = am.getRawType();
/*     */ 
/* 692 */     if ((rt == null) || (!rt.isArray())) {
/* 693 */       return false;
/*     */     }
/*     */ 
/* 699 */     Class compType = rt.getComponentType();
/*     */ 
/* 701 */     Package pkg = compType.getPackage();
/* 702 */     if (pkg != null) {
/* 703 */       String pname = pkg.getName();
/* 704 */       if ((pname.startsWith("net.sf.cglib")) || (pname.startsWith("org.hibernate.repackage.cglib")))
/*     */       {
/* 707 */         return true;
/*     */       }
/*     */     }
/* 710 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean isGroovyMetaClassSetter(AnnotatedMethod am)
/*     */   {
/* 719 */     Class argType = am.getParameterClass(0);
/* 720 */     Package pkg = argType.getPackage();
/*     */ 
/* 722 */     return (pkg != null) && (pkg.getName().startsWith("groovy.lang"));
/*     */   }
/*     */ 
/*     */   protected boolean isGroovyMetaClassGetter(AnnotatedMethod am)
/*     */   {
/* 732 */     Class rt = am.getRawType();
/* 733 */     if ((rt == null) || (rt.isArray())) {
/* 734 */       return false;
/*     */     }
/* 736 */     Package pkg = rt.getPackage();
/*     */ 
/* 738 */     return (pkg != null) && (pkg.getName().startsWith("groovy.lang"));
/*     */   }
/*     */ 
/*     */   public String okNameForSetter(AnnotatedMethod am)
/*     */   {
/* 751 */     String name = am.getName();
/*     */ 
/* 757 */     if (name.startsWith("set")) {
/* 758 */       name = mangleSetterName(am, name.substring(3));
/* 759 */       if (name == null) {
/* 760 */         return null;
/*     */       }
/* 762 */       if ("metaClass".equals(name))
/*     */       {
/* 764 */         if (isGroovyMetaClassSetter(am)) {
/* 765 */           return null;
/*     */         }
/*     */       }
/* 768 */       return name;
/*     */     }
/* 770 */     return null;
/*     */   }
/*     */ 
/*     */   protected String mangleSetterName(Annotated a, String basename)
/*     */   {
/* 779 */     return manglePropertyName(basename);
/*     */   }
/*     */ 
/*     */   public LinkedHashMap<String, AnnotatedField> _findPropertyFields(VisibilityChecker<?> vchecker, Collection<String> ignoredProperties, boolean forSerialization)
/*     */   {
/* 805 */     LinkedHashMap results = new LinkedHashMap();
/* 806 */     for (AnnotatedField af : this._classInfo.fields())
/*     */     {
/* 817 */       String propName = forSerialization ? this._annotationIntrospector.findSerializablePropertyName(af) : this._annotationIntrospector.findDeserializablePropertyName(af);
/*     */ 
/* 821 */       if (propName != null) {
/* 822 */         if (propName.length() == 0)
/* 823 */           propName = af.getName();
/*     */       }
/*     */       else {
/* 826 */         if (!vchecker.isFieldVisible(af)) {
/*     */           continue;
/*     */         }
/* 829 */         propName = af.getName();
/*     */       }
/*     */ 
/* 832 */       if ((ignoredProperties != null) && 
/* 833 */         (ignoredProperties.contains(propName)))
/*     */       {
/*     */         continue;
/*     */       }
/*     */ 
/* 842 */       AnnotatedField old = (AnnotatedField)results.put(propName, af);
/* 843 */       if (old != null)
/*     */       {
/* 853 */         if (old.getDeclaringClass() == af.getDeclaringClass()) {
/* 854 */           String oldDesc = old.getFullName();
/* 855 */           String newDesc = af.getFullName();
/* 856 */           throw new IllegalArgumentException("Multiple fields representing property \"" + propName + "\": " + oldDesc + " vs " + newDesc);
/*     */         }
/*     */       }
/*     */     }
/* 860 */     return results;
/*     */   }
/*     */ 
/*     */   public static String manglePropertyName(String basename)
/*     */   {
/* 878 */     int len = basename.length();
/*     */ 
/* 881 */     if (len == 0) {
/* 882 */       return null;
/*     */     }
/*     */ 
/* 885 */     StringBuilder sb = null;
/* 886 */     for (int i = 0; i < len; i++) {
/* 887 */       char upper = basename.charAt(i);
/* 888 */       char lower = Character.toLowerCase(upper);
/* 889 */       if (upper == lower) {
/*     */         break;
/*     */       }
/* 892 */       if (sb == null) {
/* 893 */         sb = new StringBuilder(basename);
/*     */       }
/* 895 */       sb.setCharAt(i, lower);
/*     */     }
/* 897 */     return sb == null ? basename : sb.toString();
/*     */   }
/*     */ 
/*     */   public static String descFor(AnnotatedElement elem)
/*     */   {
/* 906 */     if ((elem instanceof Class)) {
/* 907 */       return "class " + ((Class)elem).getName();
/*     */     }
/* 909 */     if ((elem instanceof Method)) {
/* 910 */       Method m = (Method)elem;
/* 911 */       return "method " + m.getName() + " (from class " + m.getDeclaringClass().getName() + ")";
/*     */     }
/* 913 */     if ((elem instanceof Constructor)) {
/* 914 */       Constructor ctor = (Constructor)elem;
/*     */ 
/* 916 */       return "constructor() (from class " + ctor.getDeclaringClass().getName() + ")";
/*     */     }
/*     */ 
/* 919 */     return "unknown type [" + elem.getClass() + "]";
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.BasicBeanDescription
 * JD-Core Version:    0.6.0
 */