/*    */ package org.codehaus.jackson.map.introspect;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.reflect.AnnotatedElement;
/*    */ import java.lang.reflect.Modifier;
/*    */ import java.lang.reflect.Type;
/*    */ import org.codehaus.jackson.map.type.TypeBindings;
/*    */ import org.codehaus.jackson.map.type.TypeFactory;
/*    */ import org.codehaus.jackson.type.JavaType;
/*    */ 
/*    */ public abstract class Annotated
/*    */ {
/*    */   public abstract <A extends Annotation> A getAnnotation(Class<A> paramClass);
/*    */ 
/*    */   public final <A extends Annotation> boolean hasAnnotation(Class<A> acls)
/*    */   {
/* 24 */     return getAnnotation(acls) != null;
/*    */   }
/*    */ 
/*    */   public abstract AnnotatedElement getAnnotated();
/*    */ 
/*    */   protected abstract int getModifiers();
/*    */ 
/*    */   public final boolean isPublic()
/*    */   {
/* 37 */     return Modifier.isPublic(getModifiers());
/*    */   }
/*    */ 
/*    */   public abstract String getName();
/*    */ 
/*    */   public JavaType getType(TypeBindings context)
/*    */   {
/* 47 */     return TypeFactory.type(getGenericType(), context);
/*    */   }
/*    */ 
/*    */   public abstract Type getGenericType();
/*    */ 
/*    */   public abstract Class<?> getRawType();
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.Annotated
 * JD-Core Version:    0.6.0
 */