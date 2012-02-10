/*    */ package org.codehaus.jackson.map;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.LinkedHashMap;
/*    */ import org.codehaus.jackson.map.introspect.AnnotatedMethod;
/*    */ import org.codehaus.jackson.map.introspect.VisibilityChecker;
/*    */ import org.codehaus.jackson.map.type.TypeBindings;
/*    */ import org.codehaus.jackson.map.util.Annotations;
/*    */ import org.codehaus.jackson.type.JavaType;
/*    */ 
/*    */ public abstract class BeanDescription
/*    */ {
/*    */   protected final JavaType _type;
/*    */ 
/*    */   protected BeanDescription(JavaType type)
/*    */   {
/* 42 */     this._type = type;
/*    */   }
/*    */ 
/*    */   public JavaType getType()
/*    */   {
/* 55 */     return this._type;
/*    */   }
/* 57 */   public Class<?> getBeanClass() { return this._type.getRawClass();
/*    */   }
/*    */ 
/*    */   public abstract boolean hasKnownClassAnnotations();
/*    */ 
/*    */   public abstract TypeBindings bindingsForBeanType();
/*    */ 
/*    */   public abstract Annotations getClassAnnotations();
/*    */ 
/*    */   public abstract LinkedHashMap<String, AnnotatedMethod> findGetters(VisibilityChecker<?> paramVisibilityChecker, Collection<String> paramCollection);
/*    */ 
/*    */   public abstract LinkedHashMap<String, AnnotatedMethod> findSetters(VisibilityChecker<?> paramVisibilityChecker);
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.BeanDescription
 * JD-Core Version:    0.6.0
 */