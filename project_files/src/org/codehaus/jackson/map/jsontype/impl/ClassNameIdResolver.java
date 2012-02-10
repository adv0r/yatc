/*    */ package org.codehaus.jackson.map.jsontype.impl;
/*    */ 
/*    */ import java.util.EnumMap;
/*    */ import java.util.EnumSet;
/*    */ import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
/*    */ import org.codehaus.jackson.map.type.TypeFactory;
/*    */ import org.codehaus.jackson.map.util.ClassUtil;
/*    */ import org.codehaus.jackson.type.JavaType;
/*    */ 
/*    */ public class ClassNameIdResolver extends TypeIdResolverBase
/*    */ {
/*    */   public ClassNameIdResolver(JavaType baseType)
/*    */   {
/* 14 */     super(baseType);
/*    */   }
/*    */   public JsonTypeInfo.Id getMechanism() {
/* 17 */     return JsonTypeInfo.Id.CLASS;
/*    */   }
/*    */ 
/*    */   public void registerSubtype(Class<?> type, String name)
/*    */   {
/*    */   }
/*    */ 
/*    */   public String idFromValue(Object value) {
/* 25 */     Class cls = value.getClass();
/*    */ 
/* 28 */     if ((Enum.class.isAssignableFrom(cls)) && 
/* 29 */       (!cls.isEnum())) {
/* 30 */       cls = cls.getSuperclass();
/*    */     }
/*    */ 
/* 33 */     String str = cls.getName();
/* 34 */     if (str.startsWith("java.util"))
/*    */     {
/* 43 */       if ((value instanceof EnumSet)) {
/* 44 */         Class enumClass = ClassUtil.findEnumType((EnumSet)value);
/* 45 */         str = TypeFactory.collectionType(EnumSet.class, enumClass).toCanonical();
/* 46 */       } else if ((value instanceof EnumMap)) {
/* 47 */         Class enumClass = ClassUtil.findEnumType((EnumMap)value);
/* 48 */         Class valueClass = Object.class;
/* 49 */         str = TypeFactory.mapType(EnumMap.class, enumClass, valueClass).toCanonical();
/*    */       } else {
/* 51 */         String end = str.substring(9);
/* 52 */         if (((end.startsWith(".Arrays$")) || (end.startsWith(".Collections$"))) && (str.indexOf("List") >= 0))
/*    */         {
/* 60 */           str = "java.util.ArrayList";
/*    */         }
/*    */       }
/*    */     }
/* 64 */     return str;
/*    */   }
/*    */ 
/*    */   public JavaType typeFromId(String id)
/*    */   {
/* 73 */     if (id.indexOf('<') > 0) {
/* 74 */       JavaType t = TypeFactory.fromCanonical(id);
/*    */ 
/* 76 */       return t;
/*    */     }
/*    */ 
/*    */     try
/*    */     {
/* 83 */       ClassLoader loader = Thread.currentThread().getContextClassLoader();
/* 84 */       Class cls = Class.forName(id, true, loader);
/* 85 */       return TypeFactory.specialize(this._baseType, cls);
/*    */     } catch (ClassNotFoundException e) {
/* 87 */       throw new IllegalArgumentException("Invalid type id '" + id + "' (for id type 'Id.class'): no such class found"); } catch (Exception e) {
/*    */     }
/* 89 */     throw new IllegalArgumentException("Invalid type id '" + id + "' (for id type 'Id.class'): " + e.getMessage(), e);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.jsontype.impl.ClassNameIdResolver
 * JD-Core Version:    0.6.0
 */