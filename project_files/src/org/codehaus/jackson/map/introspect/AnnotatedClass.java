/*     */ package org.codehaus.jackson.map.introspect;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.codehaus.jackson.map.AnnotationIntrospector;
/*     */ import org.codehaus.jackson.map.ClassIntrospector.MixInResolver;
/*     */ import org.codehaus.jackson.map.util.Annotations;
/*     */ import org.codehaus.jackson.map.util.ArrayBuilders;
/*     */ import org.codehaus.jackson.map.util.ClassUtil;
/*     */ 
/*     */ public final class AnnotatedClass extends Annotated
/*     */ {
/*     */   protected final Class<?> _class;
/*     */   protected final Collection<Class<?>> _superTypes;
/*     */   protected final AnnotationIntrospector _annotationIntrospector;
/*     */   protected final ClassIntrospector.MixInResolver _mixInResolver;
/*     */   protected final Class<?> _primaryMixIn;
/*     */   protected AnnotationMap _classAnnotations;
/*     */   protected AnnotatedConstructor _defaultConstructor;
/*     */   protected List<AnnotatedConstructor> _constructors;
/*     */   protected List<AnnotatedMethod> _creatorMethods;
/*     */   protected AnnotatedMethodMap _memberMethods;
/*     */   protected List<AnnotatedField> _fields;
/*     */   protected List<AnnotatedMethod> _ignoredMethods;
/*     */   protected List<AnnotatedField> _ignoredFields;
/*     */ 
/*     */   private AnnotatedClass(Class<?> cls, List<Class<?>> superTypes, AnnotationIntrospector aintr, ClassIntrospector.MixInResolver mir)
/*     */   {
/* 121 */     this._class = cls;
/* 122 */     this._superTypes = superTypes;
/* 123 */     this._annotationIntrospector = aintr;
/* 124 */     this._mixInResolver = mir;
/* 125 */     this._primaryMixIn = (this._mixInResolver == null ? null : this._mixInResolver.findMixInClassFor(this._class));
/*     */   }
/*     */ 
/*     */   public static AnnotatedClass construct(Class<?> cls, AnnotationIntrospector aintr, ClassIntrospector.MixInResolver mir)
/*     */   {
/* 137 */     List st = ClassUtil.findSuperTypes(cls, null);
/* 138 */     AnnotatedClass ac = new AnnotatedClass(cls, st, aintr, mir);
/* 139 */     ac.resolveClassAnnotations();
/* 140 */     return ac;
/*     */   }
/*     */ 
/*     */   public static AnnotatedClass constructWithoutSuperTypes(Class<?> cls, AnnotationIntrospector aintr, ClassIntrospector.MixInResolver mir)
/*     */   {
/* 151 */     List empty = Collections.emptyList();
/* 152 */     AnnotatedClass ac = new AnnotatedClass(cls, empty, aintr, mir);
/* 153 */     ac.resolveClassAnnotations();
/* 154 */     return ac;
/*     */   }
/*     */ 
/*     */   public Class<?> getAnnotated()
/*     */   {
/* 164 */     return this._class;
/*     */   }
/*     */   public int getModifiers() {
/* 167 */     return this._class.getModifiers();
/*     */   }
/*     */   public String getName() {
/* 170 */     return this._class.getName();
/*     */   }
/*     */ 
/*     */   public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */   {
/* 175 */     if (this._classAnnotations == null) {
/* 176 */       return null;
/*     */     }
/* 178 */     return this._classAnnotations.get(acls);
/*     */   }
/*     */ 
/*     */   public Type getGenericType()
/*     */   {
/* 183 */     return this._class;
/*     */   }
/*     */ 
/*     */   public Class<?> getRawType()
/*     */   {
/* 188 */     return this._class;
/*     */   }
/*     */ 
/*     */   public Annotations getAnnotations()
/*     */   {
/* 200 */     return this._classAnnotations;
/*     */   }
/* 202 */   public boolean hasAnnotations() { return this._classAnnotations.size() > 0; } 
/*     */   public AnnotatedConstructor getDefaultConstructor() {
/* 204 */     return this._defaultConstructor;
/*     */   }
/*     */ 
/*     */   public List<AnnotatedConstructor> getConstructors() {
/* 208 */     if (this._constructors == null) {
/* 209 */       return Collections.emptyList();
/*     */     }
/* 211 */     return this._constructors;
/*     */   }
/*     */ 
/*     */   public List<AnnotatedMethod> getStaticMethods()
/*     */   {
/* 216 */     if (this._creatorMethods == null) {
/* 217 */       return Collections.emptyList();
/*     */     }
/* 219 */     return this._creatorMethods;
/*     */   }
/*     */ 
/*     */   public Iterable<AnnotatedMethod> memberMethods()
/*     */   {
/* 224 */     return this._memberMethods;
/*     */   }
/*     */ 
/*     */   public Iterable<AnnotatedMethod> ignoredMemberMethods()
/*     */   {
/* 229 */     if (this._ignoredMethods == null) {
/* 230 */       List l = Collections.emptyList();
/* 231 */       return l;
/*     */     }
/* 233 */     return this._ignoredMethods;
/*     */   }
/*     */ 
/*     */   public int getMemberMethodCount()
/*     */   {
/* 238 */     return this._memberMethods.size();
/*     */   }
/*     */ 
/*     */   public AnnotatedMethod findMethod(String name, Class<?>[] paramTypes)
/*     */   {
/* 243 */     return this._memberMethods.find(name, paramTypes);
/*     */   }
/*     */ 
/*     */   public int getFieldCount() {
/* 247 */     return this._fields == null ? 0 : this._fields.size();
/*     */   }
/*     */ 
/*     */   public Iterable<AnnotatedField> fields()
/*     */   {
/* 252 */     if (this._fields == null) {
/* 253 */       List l = Collections.emptyList();
/* 254 */       return l;
/*     */     }
/* 256 */     return this._fields;
/*     */   }
/*     */ 
/*     */   public Iterable<AnnotatedField> ignoredFields()
/*     */   {
/* 261 */     if (this._ignoredFields == null) {
/* 262 */       List l = Collections.emptyList();
/* 263 */       return l;
/*     */     }
/* 265 */     return this._ignoredFields;
/*     */   }
/*     */ 
/*     */   protected void resolveClassAnnotations()
/*     */   {
/* 286 */     this._classAnnotations = new AnnotationMap();
/*     */ 
/* 288 */     if (this._primaryMixIn != null) {
/* 289 */       _addClassMixIns(this._classAnnotations, this._class, this._primaryMixIn);
/*     */     }
/*     */ 
/* 292 */     for (Annotation a : this._class.getDeclaredAnnotations()) {
/* 293 */       if (this._annotationIntrospector.isHandled(a)) {
/* 294 */         this._classAnnotations.addIfNotPresent(a);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 299 */     for (Class cls : this._superTypes)
/*     */     {
/* 301 */       _addClassMixIns(this._classAnnotations, cls);
/* 302 */       for (Annotation a : cls.getDeclaredAnnotations()) {
/* 303 */         if (this._annotationIntrospector.isHandled(a)) {
/* 304 */           this._classAnnotations.addIfNotPresent(a);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 317 */     _addClassMixIns(this._classAnnotations, Object.class);
/*     */   }
/*     */ 
/*     */   protected void _addClassMixIns(AnnotationMap annotations, Class<?> toMask)
/*     */   {
/* 326 */     if (this._mixInResolver != null)
/* 327 */       _addClassMixIns(annotations, toMask, this._mixInResolver.findMixInClassFor(toMask));
/*     */   }
/*     */ 
/*     */   protected void _addClassMixIns(AnnotationMap annotations, Class<?> toMask, Class<?> mixin)
/*     */   {
/* 334 */     if (mixin == null) {
/* 335 */       return;
/*     */     }
/*     */ 
/* 338 */     for (Annotation a : mixin.getDeclaredAnnotations()) {
/* 339 */       if (this._annotationIntrospector.isHandled(a)) {
/* 340 */         annotations.addIfNotPresent(a);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 350 */     for (Class parent : ClassUtil.findSuperTypes(mixin, toMask))
/* 351 */       for (Annotation a : parent.getDeclaredAnnotations())
/* 352 */         if (this._annotationIntrospector.isHandled(a))
/* 353 */           annotations.addIfNotPresent(a);
/*     */   }
/*     */ 
/*     */   public void resolveCreators(boolean includeAll)
/*     */   {
/* 378 */     this._constructors = null;
/* 379 */     for (Constructor ctor : this._class.getDeclaredConstructors()) {
/* 380 */       switch (ctor.getParameterTypes().length) {
/*     */       case 0:
/* 382 */         this._defaultConstructor = _constructConstructor(ctor, true);
/* 383 */         break;
/*     */       default:
/* 385 */         if (!includeAll) continue;
/* 386 */         if (this._constructors == null) {
/* 387 */           this._constructors = new ArrayList();
/*     */         }
/* 389 */         this._constructors.add(_constructConstructor(ctor, false));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 394 */     if ((this._primaryMixIn != null) && (
/* 395 */       (this._defaultConstructor != null) || (this._constructors != null))) {
/* 396 */       _addConstructorMixIns(this._primaryMixIn);
/*     */     }
/*     */ 
/* 405 */     if ((this._defaultConstructor != null) && 
/* 406 */       (this._annotationIntrospector.isIgnorableConstructor(this._defaultConstructor))) {
/* 407 */       this._defaultConstructor = null;
/*     */     }
/*     */ 
/* 410 */     if (this._constructors != null)
/*     */     {
/* 412 */       int i = this._constructors.size();
/*     */       while (true) { i--; if (i < 0) break;
/* 413 */         if (this._annotationIntrospector.isIgnorableConstructor((AnnotatedConstructor)this._constructors.get(i))) {
/* 414 */           this._constructors.remove(i);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 419 */     this._creatorMethods = null;
/*     */ 
/* 421 */     if (includeAll)
/*     */     {
/* 425 */       for (Method m : this._class.getDeclaredMethods()) {
/* 426 */         if (!Modifier.isStatic(m.getModifiers())) {
/*     */           continue;
/*     */         }
/* 429 */         int argCount = m.getParameterTypes().length;
/*     */ 
/* 431 */         if (argCount < 1) {
/*     */           continue;
/*     */         }
/* 434 */         if (this._creatorMethods == null) {
/* 435 */           this._creatorMethods = new ArrayList();
/*     */         }
/* 437 */         this._creatorMethods.add(_constructCreatorMethod(m));
/*     */       }
/*     */ 
/* 440 */       if ((this._primaryMixIn != null) && (this._creatorMethods != null)) {
/* 441 */         _addFactoryMixIns(this._primaryMixIn);
/*     */       }
/*     */ 
/* 444 */       if (this._creatorMethods != null)
/*     */       {
/* 446 */         int i = this._creatorMethods.size();
/*     */         while (true) { i--; if (i < 0) break;
/* 447 */           if (this._annotationIntrospector.isIgnorableMethod((AnnotatedMethod)this._creatorMethods.get(i)))
/* 448 */             this._creatorMethods.remove(i);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void _addConstructorMixIns(Class<?> mixin)
/*     */   {
/* 457 */     MemberKey[] ctorKeys = null;
/* 458 */     int ctorCount = this._constructors == null ? 0 : this._constructors.size();
/* 459 */     for (Constructor ctor : mixin.getDeclaredConstructors())
/* 460 */       switch (ctor.getParameterTypes().length) {
/*     */       case 0:
/* 462 */         if (this._defaultConstructor == null) continue;
/* 463 */         _addMixOvers(ctor, this._defaultConstructor, false); break;
/*     */       default:
/* 467 */         if (ctorKeys == null) {
/* 468 */           ctorKeys = new MemberKey[ctorCount];
/* 469 */           for (int i = 0; i < ctorCount; i++) {
/* 470 */             ctorKeys[i] = new MemberKey(((AnnotatedConstructor)this._constructors.get(i)).getAnnotated());
/*     */           }
/*     */         }
/* 473 */         MemberKey key = new MemberKey(ctor);
/*     */ 
/* 475 */         for (int i = 0; i < ctorCount; i++) {
/* 476 */           if (!key.equals(ctorKeys[i])) {
/*     */             continue;
/*     */           }
/* 479 */           _addMixOvers(ctor, (AnnotatedConstructor)this._constructors.get(i), true);
/* 480 */           break;
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void _addFactoryMixIns(Class<?> mixin)
/*     */   {
/* 488 */     MemberKey[] methodKeys = null;
/* 489 */     int methodCount = this._creatorMethods.size();
/*     */ 
/* 491 */     for (Method m : mixin.getDeclaredMethods()) {
/* 492 */       if (!Modifier.isStatic(m.getModifiers())) {
/*     */         continue;
/*     */       }
/* 495 */       if (m.getParameterTypes().length == 0) {
/*     */         continue;
/*     */       }
/* 498 */       if (methodKeys == null) {
/* 499 */         methodKeys = new MemberKey[methodCount];
/* 500 */         for (int i = 0; i < methodCount; i++) {
/* 501 */           methodKeys[i] = new MemberKey(((AnnotatedMethod)this._creatorMethods.get(i)).getAnnotated());
/*     */         }
/*     */       }
/* 504 */       MemberKey key = new MemberKey(m);
/* 505 */       for (int i = 0; i < methodCount; i++) {
/* 506 */         if (!key.equals(methodKeys[i])) {
/*     */           continue;
/*     */         }
/* 509 */         _addMixOvers(m, (AnnotatedMethod)this._creatorMethods.get(i), true);
/* 510 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void resolveMemberMethods(MethodFilter methodFilter, boolean collectIgnored)
/*     */   {
/* 529 */     this._memberMethods = new AnnotatedMethodMap();
/* 530 */     AnnotatedMethodMap mixins = new AnnotatedMethodMap();
/*     */ 
/* 532 */     _addMemberMethods(this._class, methodFilter, this._memberMethods, this._primaryMixIn, mixins);
/*     */ 
/* 535 */     for (Class cls : this._superTypes) {
/* 536 */       Class mixin = this._mixInResolver == null ? null : this._mixInResolver.findMixInClassFor(cls);
/* 537 */       _addMemberMethods(cls, methodFilter, this._memberMethods, mixin, mixins);
/*     */     }
/*     */ 
/* 540 */     if (this._mixInResolver != null) {
/* 541 */       Class mixin = this._mixInResolver.findMixInClassFor(Object.class);
/* 542 */       if (mixin != null) {
/* 543 */         _addMethodMixIns(methodFilter, this._memberMethods, mixin, mixins);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 552 */     if (!mixins.isEmpty()) {
/* 553 */       Iterator it = mixins.iterator();
/* 554 */       while (it.hasNext()) {
/* 555 */         AnnotatedMethod mixIn = (AnnotatedMethod)it.next();
/*     */         try {
/* 557 */           Method m = Object.class.getDeclaredMethod(mixIn.getName(), mixIn.getParameterClasses());
/* 558 */           if (m != null) {
/* 559 */             AnnotatedMethod am = _constructMethod(m);
/* 560 */             _addMixOvers(mixIn.getAnnotated(), am, false);
/* 561 */             this._memberMethods.add(am);
/*     */           }
/*     */         }
/*     */         catch (Exception e)
/*     */         {
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 571 */     Iterator it = this._memberMethods.iterator();
/* 572 */     while (it.hasNext()) {
/* 573 */       AnnotatedMethod am = (AnnotatedMethod)it.next();
/* 574 */       if (this._annotationIntrospector.isIgnorableMethod(am)) {
/* 575 */         it.remove();
/* 576 */         if (collectIgnored)
/* 577 */           this._ignoredMethods = ArrayBuilders.addToList(this._ignoredMethods, am);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void _addMemberMethods(Class<?> cls, MethodFilter methodFilter, AnnotatedMethodMap methods, Class<?> mixInCls, AnnotatedMethodMap mixIns)
/*     */   {
/* 588 */     if (mixInCls != null) {
/* 589 */       _addMethodMixIns(methodFilter, methods, mixInCls, mixIns);
/*     */     }
/*     */ 
/* 592 */     if (cls == null) {
/* 593 */       return;
/*     */     }
/*     */ 
/* 596 */     for (Method m : cls.getDeclaredMethods()) {
/* 597 */       if (!_isIncludableMethod(m, methodFilter)) {
/*     */         continue;
/*     */       }
/* 600 */       AnnotatedMethod old = methods.find(m);
/* 601 */       if (old == null) {
/* 602 */         AnnotatedMethod newM = _constructMethod(m);
/* 603 */         methods.add(newM);
/*     */ 
/* 605 */         old = mixIns.remove(m);
/* 606 */         if (old != null) {
/* 607 */           _addMixOvers(old.getAnnotated(), newM, false);
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 613 */         _addMixUnders(m, old);
/*     */ 
/* 622 */         if ((old.getDeclaringClass().isInterface()) && (!m.getDeclaringClass().isInterface()))
/* 623 */           methods.add(old.withMethod(m));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void _addMethodMixIns(MethodFilter methodFilter, AnnotatedMethodMap methods, Class<?> mixInCls, AnnotatedMethodMap mixIns)
/*     */   {
/* 632 */     for (Method m : mixInCls.getDeclaredMethods()) {
/* 633 */       if (!_isIncludableMethod(m, methodFilter)) {
/*     */         continue;
/*     */       }
/* 636 */       AnnotatedMethod am = methods.find(m);
/*     */ 
/* 641 */       if (am != null) {
/* 642 */         _addMixUnders(m, am);
/*     */       }
/*     */       else
/*     */       {
/* 648 */         mixIns.add(_constructMethod(m));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void resolveFields(boolean collectIgnored)
/*     */   {
/* 668 */     LinkedHashMap foundFields = new LinkedHashMap();
/* 669 */     _addFields(foundFields, this._class);
/*     */ 
/* 675 */     Iterator it = foundFields.entrySet().iterator();
/* 676 */     while (it.hasNext()) {
/* 677 */       AnnotatedField f = (AnnotatedField)((Map.Entry)it.next()).getValue();
/* 678 */       if (this._annotationIntrospector.isIgnorableField(f)) {
/* 679 */         it.remove();
/* 680 */         if (collectIgnored) {
/* 681 */           this._ignoredFields = ArrayBuilders.addToList(this._ignoredFields, f);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 687 */     if (foundFields.isEmpty()) {
/* 688 */       this._fields = Collections.emptyList();
/*     */     } else {
/* 690 */       this._fields = new ArrayList(foundFields.size());
/* 691 */       this._fields.addAll(foundFields.values());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void _addFields(Map<String, AnnotatedField> fields, Class<?> c)
/*     */   {
/* 702 */     Class parent = c.getSuperclass();
/* 703 */     if (parent != null)
/*     */     {
/* 708 */       _addFields(fields, parent);
/* 709 */       for (Field f : c.getDeclaredFields())
/*     */       {
/* 711 */         if (!_isIncludableField(f))
/*     */         {
/*     */           continue;
/*     */         }
/*     */ 
/* 719 */         fields.put(f.getName(), _constructField(f));
/*     */       }
/*     */ 
/* 722 */       if (this._mixInResolver != null) {
/* 723 */         Class mixin = this._mixInResolver.findMixInClassFor(c);
/* 724 */         if (mixin != null)
/* 725 */           _addFieldMixIns(mixin, fields);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void _addFieldMixIns(Class<?> mixin, Map<String, AnnotatedField> fields)
/*     */   {
/* 738 */     for (Field mixinField : mixin.getDeclaredFields())
/*     */     {
/* 742 */       if (!_isIncludableField(mixinField)) {
/*     */         continue;
/*     */       }
/* 745 */       String name = mixinField.getName();
/*     */ 
/* 747 */       AnnotatedField maskedField = (AnnotatedField)fields.get(name);
/* 748 */       if (maskedField != null)
/* 749 */         for (Annotation a : mixinField.getDeclaredAnnotations())
/* 750 */           if (this._annotationIntrospector.isHandled(a))
/* 751 */             maskedField.addOrOverride(a);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected AnnotatedMethod _constructMethod(Method m)
/*     */   {
/* 769 */     return new AnnotatedMethod(m, _collectRelevantAnnotations(m.getDeclaredAnnotations()), null);
/*     */   }
/*     */ 
/*     */   protected AnnotatedConstructor _constructConstructor(Constructor<?> ctor, boolean defaultCtor)
/*     */   {
/* 774 */     return new AnnotatedConstructor(ctor, _collectRelevantAnnotations(ctor.getDeclaredAnnotations()), defaultCtor ? null : _collectRelevantAnnotations(ctor.getParameterAnnotations()));
/*     */   }
/*     */ 
/*     */   protected AnnotatedMethod _constructCreatorMethod(Method m)
/*     */   {
/* 780 */     return new AnnotatedMethod(m, _collectRelevantAnnotations(m.getDeclaredAnnotations()), _collectRelevantAnnotations(m.getParameterAnnotations()));
/*     */   }
/*     */ 
/*     */   protected AnnotatedField _constructField(Field f)
/*     */   {
/* 786 */     return new AnnotatedField(f, _collectRelevantAnnotations(f.getDeclaredAnnotations()));
/*     */   }
/*     */ 
/*     */   protected AnnotationMap[] _collectRelevantAnnotations(Annotation[][] anns)
/*     */   {
/* 791 */     int len = anns.length;
/* 792 */     AnnotationMap[] result = new AnnotationMap[len];
/* 793 */     for (int i = 0; i < len; i++) {
/* 794 */       result[i] = _collectRelevantAnnotations(anns[i]);
/*     */     }
/* 796 */     return result;
/*     */   }
/*     */ 
/*     */   protected AnnotationMap _collectRelevantAnnotations(Annotation[] anns)
/*     */   {
/* 801 */     AnnotationMap annMap = new AnnotationMap();
/* 802 */     if (anns != null) {
/* 803 */       for (Annotation a : anns) {
/* 804 */         if (this._annotationIntrospector.isHandled(a)) {
/* 805 */           annMap.add(a);
/*     */         }
/*     */       }
/*     */     }
/* 809 */     return annMap;
/*     */   }
/*     */ 
/*     */   protected boolean _isIncludableMethod(Method m, MethodFilter filter)
/*     */   {
/* 820 */     if ((filter != null) && (!filter.includeMethod(m))) {
/* 821 */       return false;
/*     */     }
/*     */ 
/* 828 */     return (!m.isSynthetic()) && (!m.isBridge());
/*     */   }
/*     */ 
/*     */   private boolean _isIncludableField(Field f)
/*     */   {
/* 838 */     if (f.isSynthetic()) {
/* 839 */       return false;
/*     */     }
/*     */ 
/* 842 */     int mods = f.getModifiers();
/*     */ 
/* 844 */     return (!Modifier.isStatic(mods)) && (!Modifier.isTransient(mods));
/*     */   }
/*     */ 
/*     */   protected void _addMixOvers(Constructor<?> mixin, AnnotatedConstructor target, boolean addParamAnnotations)
/*     */   {
/* 862 */     for (Annotation a : mixin.getDeclaredAnnotations()) {
/* 863 */       if (this._annotationIntrospector.isHandled(a)) {
/* 864 */         target.addOrOverride(a);
/*     */       }
/*     */     }
/* 867 */     if (addParamAnnotations) {
/* 868 */       Annotation[][] pa = mixin.getParameterAnnotations();
/* 869 */       int i = 0; for (int len = pa.length; i < len; i++)
/* 870 */         for (Annotation a : pa[i])
/* 871 */           target.addOrOverrideParam(i, a);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void _addMixOvers(Method mixin, AnnotatedMethod target, boolean addParamAnnotations)
/*     */   {
/* 884 */     for (Annotation a : mixin.getDeclaredAnnotations()) {
/* 885 */       if (this._annotationIntrospector.isHandled(a)) {
/* 886 */         target.addOrOverride(a);
/*     */       }
/*     */     }
/* 889 */     if (addParamAnnotations) {
/* 890 */       Annotation[][] pa = mixin.getParameterAnnotations();
/* 891 */       int i = 0; for (int len = pa.length; i < len; i++)
/* 892 */         for (Annotation a : pa[i])
/* 893 */           target.addOrOverrideParam(i, a);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void _addMixUnders(Method src, AnnotatedMethod target)
/*     */   {
/* 905 */     for (Annotation a : src.getDeclaredAnnotations())
/* 906 */       if (this._annotationIntrospector.isHandled(a))
/* 907 */         target.addIfNotPresent(a);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 921 */     return "[AnnotedClass " + this._class.getName() + "]";
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.AnnotatedClass
 * JD-Core Version:    0.6.0
 */