/*     */ package org.codehaus.jackson.map.deser;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import org.codehaus.jackson.map.AnnotationIntrospector;
/*     */ 
/*     */ public final class EnumResolver<T extends Enum<T>>
/*     */ {
/*     */   protected final Class<T> _enumClass;
/*     */   protected final T[] _enums;
/*     */   protected final HashMap<String, T> _enumsById;
/*     */ 
/*     */   private EnumResolver(Class<T> enumClass, T[] enums, HashMap<String, T> map)
/*     */   {
/*  21 */     this._enumClass = enumClass;
/*  22 */     this._enums = enums;
/*  23 */     this._enumsById = map;
/*     */   }
/*     */ 
/*     */   public static <ET extends Enum<ET>> EnumResolver<ET> constructFor(Class<ET> enumCls, AnnotationIntrospector ai)
/*     */   {
/*  32 */     Enum[] enumValues = (Enum[])enumCls.getEnumConstants();
/*  33 */     if (enumValues == null) {
/*  34 */       throw new IllegalArgumentException("No enum constants for class " + enumCls.getName());
/*     */     }
/*  36 */     HashMap map = new HashMap();
/*  37 */     for (Enum e : enumValues) {
/*  38 */       map.put(ai.findEnumValue(e), e);
/*     */     }
/*  40 */     return new EnumResolver(enumCls, enumValues, map);
/*     */   }
/*     */ 
/*     */   public static <ET extends Enum<ET>> EnumResolver<ET> constructUsingToString(Class<ET> enumCls)
/*     */   {
/*  51 */     Enum[] enumValues = (Enum[])enumCls.getEnumConstants();
/*  52 */     HashMap map = new HashMap();
/*     */ 
/*  54 */     int i = enumValues.length;
/*     */     while (true) { i--; if (i < 0) break;
/*  55 */       Enum e = enumValues[i];
/*  56 */       map.put(e.toString(), e);
/*     */     }
/*  58 */     return new EnumResolver(enumCls, enumValues, map);
/*     */   }
/*     */ 
/*     */   public static EnumResolver<?> constructUnsafe(Class<?> rawEnumCls, AnnotationIntrospector ai)
/*     */   {
/*  71 */     Class enumCls = rawEnumCls;
/*  72 */     return constructFor(enumCls, ai);
/*     */   }
/*     */ 
/*     */   public static EnumResolver<?> constructUnsafeUsingToString(Class<?> rawEnumCls)
/*     */   {
/*  85 */     Class enumCls = rawEnumCls;
/*  86 */     return constructUsingToString(enumCls);
/*     */   }
/*     */ 
/*     */   public T findEnum(String key)
/*     */   {
/*  91 */     return (Enum)this._enumsById.get(key);
/*     */   }
/*     */ 
/*     */   public T getEnum(int index)
/*     */   {
/*  96 */     if ((index < 0) || (index >= this._enums.length)) {
/*  97 */       return null;
/*     */     }
/*  99 */     return this._enums[index];
/*     */   }
/*     */   public Class<T> getEnumClass() {
/* 102 */     return this._enumClass;
/*     */   }
/* 104 */   public int lastValidIndex() { return this._enums.length - 1;
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.EnumResolver
 * JD-Core Version:    0.6.0
 */