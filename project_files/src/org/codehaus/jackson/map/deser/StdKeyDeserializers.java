/*    */ package org.codehaus.jackson.map.deser;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.HashMap;
/*    */ import org.codehaus.jackson.map.DeserializationConfig;
/*    */ import org.codehaus.jackson.map.KeyDeserializer;
/*    */ import org.codehaus.jackson.map.introspect.BasicBeanDescription;
/*    */ import org.codehaus.jackson.map.type.TypeFactory;
/*    */ import org.codehaus.jackson.type.JavaType;
/*    */ 
/*    */ class StdKeyDeserializers
/*    */ {
/* 27 */   final HashMap<JavaType, KeyDeserializer> _keyDeserializers = new HashMap();
/*    */ 
/*    */   private StdKeyDeserializers()
/*    */   {
/* 31 */     add(new StdKeyDeserializer.BoolKD());
/* 32 */     add(new StdKeyDeserializer.ByteKD());
/* 33 */     add(new StdKeyDeserializer.CharKD());
/* 34 */     add(new StdKeyDeserializer.ShortKD());
/* 35 */     add(new StdKeyDeserializer.IntKD());
/* 36 */     add(new StdKeyDeserializer.LongKD());
/* 37 */     add(new StdKeyDeserializer.FloatKD());
/* 38 */     add(new StdKeyDeserializer.DoubleKD());
/*    */   }
/*    */ 
/*    */   private void add(StdKeyDeserializer kdeser)
/*    */   {
/* 43 */     Class keyClass = kdeser.getKeyClass();
/* 44 */     this._keyDeserializers.put(TypeFactory.type(keyClass), kdeser);
/*    */   }
/*    */ 
/*    */   public static HashMap<JavaType, KeyDeserializer> constructAll()
/*    */   {
/* 49 */     return new StdKeyDeserializers()._keyDeserializers;
/*    */   }
/*    */ 
/*    */   public static KeyDeserializer constructEnumKeyDeserializer(DeserializationConfig config, JavaType type)
/*    */   {
/* 60 */     EnumResolver er = EnumResolver.constructUnsafe(type.getRawClass(), config.getAnnotationIntrospector());
/* 61 */     return new StdKeyDeserializer.EnumKD(er);
/*    */   }
/*    */ 
/*    */   public static KeyDeserializer findStringBasedKeyDeserializer(DeserializationConfig config, JavaType type)
/*    */   {
/* 69 */     BasicBeanDescription beanDesc = (BasicBeanDescription)config.introspect(type);
/*    */ 
/* 71 */     Constructor ctor = beanDesc.findSingleArgConstructor(new Class[] { String.class });
/* 72 */     if (ctor != null) {
/* 73 */       return new StdKeyDeserializer.StringCtorKeyDeserializer(ctor);
/*    */     }
/*    */ 
/* 78 */     Method m = beanDesc.findFactoryMethod(new Class[] { String.class });
/* 79 */     if (m != null) {
/* 80 */       return new StdKeyDeserializer.StringFactoryKeyDeserializer(m);
/*    */     }
/*    */ 
/* 83 */     return null;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.StdKeyDeserializers
 * JD-Core Version:    0.6.0
 */