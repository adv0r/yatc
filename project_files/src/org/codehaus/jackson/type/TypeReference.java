/*    */ package org.codehaus.jackson.type;
/*    */ 
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ public abstract class TypeReference<T>
/*    */   implements Comparable<TypeReference<T>>
/*    */ {
/*    */   final Type _type;
/*    */ 
/*    */   protected TypeReference()
/*    */   {
/* 33 */     Type superClass = getClass().getGenericSuperclass();
/* 34 */     if ((superClass instanceof Class)) {
/* 35 */       throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
/*    */     }
/*    */ 
/* 44 */     this._type = ((java.lang.reflect.ParameterizedType)superClass).getActualTypeArguments()[0];
/*    */   }
/*    */   public Type getType() {
/* 47 */     return this._type;
/*    */   }
/*    */ 
/*    */   public int compareTo(TypeReference<T> o)
/*    */   {
/* 56 */     return 0;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.type.TypeReference
 * JD-Core Version:    0.6.0
 */