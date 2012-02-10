/*    */ package org.codehaus.jackson.map.util;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.EnumMap;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.codehaus.jackson.io.SerializedString;
/*    */ import org.codehaus.jackson.map.AnnotationIntrospector;
/*    */ 
/*    */ public final class EnumValues
/*    */ {
/*    */   private final EnumMap<?, SerializedString> _values;
/*    */ 
/*    */   private EnumValues(Map<Enum<?>, SerializedString> v)
/*    */   {
/* 22 */     this._values = new EnumMap(v);
/*    */   }
/*    */ 
/*    */   public static EnumValues construct(Class<Enum<?>> enumClass, AnnotationIntrospector intr)
/*    */   {
/* 27 */     return constructFromName(enumClass, intr);
/*    */   }
/*    */ 
/*    */   public static EnumValues constructFromName(Class<Enum<?>> enumClass, AnnotationIntrospector intr)
/*    */   {
/* 35 */     Class cls = ClassUtil.findEnumType(enumClass);
/* 36 */     Enum[] values = (Enum[])cls.getEnumConstants();
/* 37 */     if (values != null)
/*    */     {
/* 39 */       Map map = new HashMap();
/* 40 */       for (Enum en : values) {
/* 41 */         String value = intr.findEnumValue(en);
/* 42 */         map.put(en, new SerializedString(value));
/*    */       }
/* 44 */       return new EnumValues(map);
/*    */     }
/* 46 */     throw new IllegalArgumentException("Can not determine enum constants for Class " + enumClass.getName());
/*    */   }
/*    */ 
/*    */   public static EnumValues constructFromToString(Class<Enum<?>> enumClass, AnnotationIntrospector intr)
/*    */   {
/* 51 */     Class cls = ClassUtil.findEnumType(enumClass);
/* 52 */     Enum[] values = (Enum[])cls.getEnumConstants();
/* 53 */     if (values != null)
/*    */     {
/* 55 */       Map map = new HashMap();
/* 56 */       for (Enum en : values) {
/* 57 */         map.put(en, new SerializedString(en.toString()));
/*    */       }
/* 59 */       return new EnumValues(map);
/*    */     }
/* 61 */     throw new IllegalArgumentException("Can not determine enum constants for Class " + enumClass.getName());
/*    */   }
/*    */ 
/*    */   @Deprecated
/*    */   public String valueFor(Enum<?> key)
/*    */   {
/* 70 */     SerializedString sstr = (SerializedString)this._values.get(key);
/* 71 */     return sstr == null ? null : sstr.getValue();
/*    */   }
/*    */ 
/*    */   public SerializedString serializedValueFor(Enum<?> key)
/*    */   {
/* 76 */     return (SerializedString)this._values.get(key);
/*    */   }
/*    */ 
/*    */   public Collection<SerializedString> values() {
/* 80 */     return this._values.values();
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.util.EnumValues
 * JD-Core Version:    0.6.0
 */