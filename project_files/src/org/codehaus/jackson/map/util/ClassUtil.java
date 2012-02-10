/*     */ package org.codehaus.jackson.map.util;
/*     */ 
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public final class ClassUtil
/*     */ {
/*     */   public static List<Class<?>> findSuperTypes(Class<?> cls, Class<?> endBefore)
/*     */   {
/*  28 */     return findSuperTypes(cls, endBefore, new ArrayList());
/*     */   }
/*     */ 
/*     */   public static List<Class<?>> findSuperTypes(Class<?> cls, Class<?> endBefore, List<Class<?>> result)
/*     */   {
/*  33 */     _addSuperTypes(cls, endBefore, result, false);
/*  34 */     return result;
/*     */   }
/*     */ 
/*     */   private static void _addSuperTypes(Class<?> cls, Class<?> endBefore, Collection<Class<?>> result, boolean addClassItself)
/*     */   {
/*  39 */     if ((cls == endBefore) || (cls == null) || (cls == Object.class)) {
/*  40 */       return;
/*     */     }
/*  42 */     if (addClassItself) {
/*  43 */       if (result.contains(cls)) {
/*  44 */         return;
/*     */       }
/*  46 */       result.add(cls);
/*     */     }
/*  48 */     for (Class intCls : cls.getInterfaces()) {
/*  49 */       _addSuperTypes(intCls, endBefore, result, true);
/*     */     }
/*  51 */     _addSuperTypes(cls.getSuperclass(), endBefore, result, true);
/*     */   }
/*     */ 
/*     */   public static String canBeABeanType(Class<?> type)
/*     */   {
/*  67 */     if (type.isAnnotation()) {
/*  68 */       return "annotation";
/*     */     }
/*  70 */     if (type.isArray()) {
/*  71 */       return "array";
/*     */     }
/*  73 */     if (type.isEnum()) {
/*  74 */       return "enum";
/*     */     }
/*  76 */     if (type.isPrimitive()) {
/*  77 */       return "primitive";
/*     */     }
/*     */ 
/*  81 */     return null;
/*     */   }
/*     */ 
/*     */   public static String isLocalType(Class<?> type)
/*     */   {
/*     */     try
/*     */     {
/*  92 */       if (type.getEnclosingMethod() != null) {
/*  93 */         return "local/anonymous";
/*     */       }
/*     */ 
/* 100 */       if ((type.getEnclosingClass() != null) && 
/* 101 */         (!Modifier.isStatic(type.getModifiers())))
/* 102 */         return "non-static member class";
/*     */     }
/*     */     catch (SecurityException e) {
/*     */     }
/*     */     catch (NullPointerException e) {
/*     */     }
/* 108 */     return null;
/*     */   }
/*     */ 
/*     */   public static boolean isProxyType(Class<?> type)
/*     */   {
/* 119 */     if (Proxy.isProxyClass(type)) {
/* 120 */       return true;
/*     */     }
/* 122 */     String name = type.getName();
/*     */ 
/* 126 */     return (name.startsWith("net.sf.cglib.proxy.")) || (name.startsWith("org.hibernate.proxy."));
/*     */   }
/*     */ 
/*     */   public static boolean isConcrete(Class<?> type)
/*     */   {
/* 138 */     int mod = type.getModifiers();
/* 139 */     return (mod & 0x600) == 0;
/*     */   }
/*     */ 
/*     */   public static boolean isConcrete(Member member)
/*     */   {
/* 147 */     int mod = member.getModifiers();
/* 148 */     return (mod & 0x600) == 0;
/*     */   }
/*     */ 
/*     */   public static boolean isCollectionMapOrArray(Class<?> type)
/*     */   {
/* 153 */     if (type.isArray()) return true;
/* 154 */     if (Collection.class.isAssignableFrom(type)) return true;
/* 155 */     return Map.class.isAssignableFrom(type);
/*     */   }
/*     */ 
/*     */   public static String getClassDescription(Object classOrInstance)
/*     */   {
/* 172 */     if (classOrInstance == null) {
/* 173 */       return "unknown";
/*     */     }
/* 175 */     Class cls = (classOrInstance instanceof Class) ? (Class)classOrInstance : classOrInstance.getClass();
/*     */ 
/* 177 */     return cls.getName();
/*     */   }
/*     */ 
/*     */   public static boolean hasGetterSignature(Method m)
/*     */   {
/* 189 */     if (Modifier.isStatic(m.getModifiers())) {
/* 190 */       return false;
/*     */     }
/*     */ 
/* 193 */     Class[] pts = m.getParameterTypes();
/* 194 */     if ((pts != null) && (pts.length != 0)) {
/* 195 */       return false;
/*     */     }
/*     */ 
/* 199 */     return Void.TYPE != m.getReturnType();
/*     */   }
/*     */ 
/*     */   public static Throwable getRootCause(Throwable t)
/*     */   {
/* 217 */     while (t.getCause() != null) {
/* 218 */       t = t.getCause();
/*     */     }
/* 220 */     return t;
/*     */   }
/*     */ 
/*     */   public static void throwRootCause(Throwable t)
/*     */     throws Exception
/*     */   {
/* 233 */     t = getRootCause(t);
/* 234 */     if ((t instanceof Exception)) {
/* 235 */       throw ((Exception)t);
/*     */     }
/* 237 */     throw ((Error)t);
/*     */   }
/*     */ 
/*     */   public static void throwAsIAE(Throwable t)
/*     */   {
/* 246 */     throwAsIAE(t, t.getMessage());
/*     */   }
/*     */ 
/*     */   public static void throwAsIAE(Throwable t, String msg)
/*     */   {
/* 256 */     if ((t instanceof RuntimeException)) {
/* 257 */       throw ((RuntimeException)t);
/*     */     }
/* 259 */     if ((t instanceof Error)) {
/* 260 */       throw ((Error)t);
/*     */     }
/* 262 */     throw new IllegalArgumentException(msg, t);
/*     */   }
/*     */ 
/*     */   public static void unwrapAndThrowAsIAE(Throwable t)
/*     */   {
/* 272 */     throwAsIAE(getRootCause(t));
/*     */   }
/*     */ 
/*     */   public static void unwrapAndThrowAsIAE(Throwable t, String msg)
/*     */   {
/* 282 */     throwAsIAE(getRootCause(t), msg);
/*     */   }
/*     */ 
/*     */   public static <T> T createInstance(Class<T> cls, boolean canFixAccess)
/*     */     throws IllegalArgumentException
/*     */   {
/* 307 */     Constructor ctor = findConstructor(cls, canFixAccess);
/* 308 */     if (ctor == null)
/* 309 */       throw new IllegalArgumentException("Class " + cls.getName() + " has no default (no arg) constructor");
/*     */     try
/*     */     {
/* 312 */       return ctor.newInstance(new Object[0]);
/*     */     } catch (Exception e) {
/* 314 */       unwrapAndThrowAsIAE(e, "Failed to instantiate class " + cls.getName() + ", problem: " + e.getMessage());
/* 315 */     }return null;
/*     */   }
/*     */ 
/*     */   public static <T> Constructor<T> findConstructor(Class<T> cls, boolean canFixAccess)
/*     */     throws IllegalArgumentException
/*     */   {
/*     */     try
/*     */     {
/* 323 */       Constructor ctor = cls.getDeclaredConstructor(new Class[0]);
/* 324 */       if (canFixAccess) {
/* 325 */         checkAndFixAccess(ctor);
/*     */       }
/* 328 */       else if (!Modifier.isPublic(ctor.getModifiers())) {
/* 329 */         throw new IllegalArgumentException("Default constructor for " + cls.getName() + " is not accessible (non-public?): not allowed to try modify access via Reflection: can not instantiate type");
/*     */       }
/*     */ 
/* 332 */       return ctor;
/*     */     } catch (NoSuchMethodException e) {
/*     */     }
/*     */     catch (Exception e) {
/* 336 */       unwrapAndThrowAsIAE(e, "Failed to find default constructor of class " + cls.getName() + ", problem: " + e.getMessage());
/*     */     }
/* 338 */     return null;
/*     */   }
/*     */ 
/*     */   public static Object defaultValue(Class<?> cls)
/*     */   {
/* 355 */     if (cls == Integer.TYPE) {
/* 356 */       return Integer.valueOf(0);
/*     */     }
/* 358 */     if (cls == Long.TYPE) {
/* 359 */       return Long.valueOf(0L);
/*     */     }
/* 361 */     if (cls == Boolean.TYPE) {
/* 362 */       return Boolean.FALSE;
/*     */     }
/* 364 */     if (cls == Double.TYPE) {
/* 365 */       return Double.valueOf(0.0D);
/*     */     }
/* 367 */     if (cls == Float.TYPE) {
/* 368 */       return Float.valueOf(0.0F);
/*     */     }
/* 370 */     if (cls == Byte.TYPE) {
/* 371 */       return Byte.valueOf(0);
/*     */     }
/* 373 */     if (cls == Short.TYPE) {
/* 374 */       return Short.valueOf(0);
/*     */     }
/* 376 */     if (cls == Character.TYPE) {
/* 377 */       return Character.valueOf('\000');
/*     */     }
/* 379 */     throw new IllegalArgumentException("Class " + cls.getName() + " is not a primitive type");
/*     */   }
/*     */ 
/*     */   public static Class<?> wrapperType(Class<?> primitiveType)
/*     */   {
/* 390 */     if (primitiveType == Integer.TYPE) {
/* 391 */       return Integer.class;
/*     */     }
/* 393 */     if (primitiveType == Long.TYPE) {
/* 394 */       return Long.class;
/*     */     }
/* 396 */     if (primitiveType == Boolean.TYPE) {
/* 397 */       return Boolean.class;
/*     */     }
/* 399 */     if (primitiveType == Double.TYPE) {
/* 400 */       return Double.class;
/*     */     }
/* 402 */     if (primitiveType == Float.TYPE) {
/* 403 */       return Float.class;
/*     */     }
/* 405 */     if (primitiveType == Byte.TYPE) {
/* 406 */       return Byte.class;
/*     */     }
/* 408 */     if (primitiveType == Short.TYPE) {
/* 409 */       return Short.class;
/*     */     }
/* 411 */     if (primitiveType == Character.TYPE) {
/* 412 */       return Character.class;
/*     */     }
/* 414 */     throw new IllegalArgumentException("Class " + primitiveType.getName() + " is not a primitive type");
/*     */   }
/*     */ 
/*     */   public static void checkAndFixAccess(Member member)
/*     */   {
/* 432 */     AccessibleObject ao = (AccessibleObject)member;
/*     */     try
/*     */     {
/* 440 */       ao.setAccessible(true);
/*     */     }
/*     */     catch (SecurityException se)
/*     */     {
/* 446 */       if (!ao.isAccessible()) {
/* 447 */         Class declClass = member.getDeclaringClass();
/* 448 */         throw new IllegalArgumentException("Can not access " + member + " (from class " + declClass.getName() + "; failed to set access: " + se.getMessage());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Class<? extends Enum<?>> findEnumType(EnumSet<?> s)
/*     */   {
/* 471 */     if (!s.isEmpty()) {
/* 472 */       return findEnumType((Enum)s.iterator().next());
/*     */     }
/*     */ 
/* 475 */     return EnumTypeLocator.instance.enumTypeFor(s);
/*     */   }
/*     */ 
/*     */   public static Class<? extends Enum<?>> findEnumType(EnumMap<?, ?> m)
/*     */   {
/* 488 */     if (!m.isEmpty()) {
/* 489 */       return findEnumType((Enum)m.keySet().iterator().next());
/*     */     }
/*     */ 
/* 492 */     return EnumTypeLocator.instance.enumTypeFor(m);
/*     */   }
/*     */ 
/*     */   public static Class<? extends Enum<?>> findEnumType(Enum<?> en)
/*     */   {
/* 505 */     Class ec = en.getClass();
/* 506 */     if (ec.getSuperclass() != Enum.class) {
/* 507 */       ec = ec.getSuperclass();
/*     */     }
/* 509 */     return ec;
/*     */   }
/*     */ 
/*     */   public static Class<? extends Enum<?>> findEnumType(Class<?> cls)
/*     */   {
/* 522 */     if (cls.getSuperclass() != Enum.class) {
/* 523 */       cls = cls.getSuperclass();
/*     */     }
/* 525 */     return cls;
/*     */   }
/*     */ 
/*     */   private static class EnumTypeLocator
/*     */   {
/* 540 */     static final EnumTypeLocator instance = new EnumTypeLocator();
/*     */     private final Field enumSetTypeField;
/*     */     private final Field enumMapTypeField;
/*     */ 
/*     */     private EnumTypeLocator()
/*     */     {
/* 549 */       this.enumSetTypeField = locateField(EnumSet.class, "elementType", Class.class);
/* 550 */       this.enumMapTypeField = locateField(EnumMap.class, "elementType", Class.class);
/*     */     }
/*     */ 
/*     */     public Class<? extends Enum<?>> enumTypeFor(EnumSet<?> set)
/*     */     {
/* 556 */       if (this.enumSetTypeField != null) {
/* 557 */         return (Class)get(set, this.enumSetTypeField);
/*     */       }
/* 559 */       throw new IllegalStateException("Can not figure out type for EnumSet (odd JDK platform?)");
/*     */     }
/*     */ 
/*     */     public Class<? extends Enum<?>> enumTypeFor(EnumMap<?, ?> set)
/*     */     {
/* 565 */       if (this.enumMapTypeField != null) {
/* 566 */         return (Class)get(set, this.enumMapTypeField);
/*     */       }
/* 568 */       throw new IllegalStateException("Can not figure out type for EnumMap (odd JDK platform?)");
/*     */     }
/*     */ 
/*     */     private Object get(Object bean, Field field)
/*     */     {
/*     */       try
/*     */       {
/* 575 */         return field.get(bean); } catch (Exception e) {
/*     */       }
/* 577 */       throw new IllegalArgumentException(e);
/*     */     }
/*     */ 
/*     */     private static Field locateField(Class<?> fromClass, String expectedName, Class<?> type)
/*     */     {
/* 583 */       Field found = null;
/*     */ 
/* 585 */       Field[] fields = fromClass.getDeclaredFields();
/* 586 */       for (Field f : fields) {
/* 587 */         if ((expectedName.equals(f.getName())) && (f.getType() == type)) {
/* 588 */           found = f;
/* 589 */           break;
/*     */         }
/*     */       }
/*     */ 
/* 593 */       if (found == null) {
/* 594 */         for (Field f : fields) {
/* 595 */           if (f.getType() != type)
/*     */             continue;
/* 597 */           if (found != null) return null;
/* 598 */           found = f;
/*     */         }
/*     */       }
/*     */ 
/* 602 */       if (found != null)
/*     */         try {
/* 604 */           found.setAccessible(true);
/*     */         } catch (Throwable t) {
/*     */         }
/* 607 */       return found;
/*     */     }
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.util.ClassUtil
 * JD-Core Version:    0.6.0
 */