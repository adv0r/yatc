/*    */ package org.codehaus.jackson.map.jsontype.impl;
/*    */ 
/*    */ import org.codehaus.jackson.map.jsontype.TypeIdResolver;
/*    */ import org.codehaus.jackson.type.JavaType;
/*    */ 
/*    */ public abstract class TypeIdResolverBase
/*    */   implements TypeIdResolver
/*    */ {
/*    */   protected final JavaType _baseType;
/*    */ 
/*    */   protected TypeIdResolverBase(JavaType baseType)
/*    */   {
/* 13 */     this._baseType = baseType;
/*    */   }
/*    */ 
/*    */   public void init(JavaType bt)
/*    */   {
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.jsontype.impl.TypeIdResolverBase
 * JD-Core Version:    0.6.0
 */