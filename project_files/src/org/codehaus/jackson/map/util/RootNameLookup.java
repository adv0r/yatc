/*    */ package org.codehaus.jackson.map.util;
/*    */ 
/*    */ import org.codehaus.jackson.io.SerializedString;
/*    */ import org.codehaus.jackson.map.AnnotationIntrospector;
/*    */ import org.codehaus.jackson.map.MapperConfig;
/*    */ import org.codehaus.jackson.map.introspect.AnnotatedClass;
/*    */ import org.codehaus.jackson.map.introspect.BasicBeanDescription;
/*    */ import org.codehaus.jackson.map.type.ClassKey;
/*    */ import org.codehaus.jackson.type.JavaType;
/*    */ 
/*    */ public class RootNameLookup
/*    */ {
/*    */   protected LRUMap<ClassKey, SerializedString> _rootNames;
/*    */ 
/*    */   public SerializedString findRootName(JavaType rootType, MapperConfig<?> config)
/*    */   {
/* 26 */     return findRootName(rootType.getRawClass(), config);
/*    */   }
/*    */ 
/*    */   public synchronized SerializedString findRootName(Class<?> rootType, MapperConfig<?> config)
/*    */   {
/* 31 */     ClassKey key = new ClassKey(rootType);
/*    */ 
/* 33 */     if (this._rootNames == null) {
/* 34 */       this._rootNames = new LRUMap(20, 200);
/*    */     } else {
/* 36 */       SerializedString name = (SerializedString)this._rootNames.get(key);
/* 37 */       if (name != null) {
/* 38 */         return name;
/*    */       }
/*    */     }
/* 41 */     BasicBeanDescription beanDesc = (BasicBeanDescription)config.introspectClassAnnotations(rootType);
/* 42 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 43 */     AnnotatedClass ac = beanDesc.getClassInfo();
/* 44 */     String nameStr = intr.findRootName(ac);
/*    */ 
/* 46 */     if (nameStr == null)
/*    */     {
/* 48 */       nameStr = rootType.getSimpleName();
/*    */     }
/* 50 */     SerializedString name = new SerializedString(nameStr);
/* 51 */     this._rootNames.put(key, name);
/* 52 */     return name;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.util.RootNameLookup
 * JD-Core Version:    0.6.0
 */