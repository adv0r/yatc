/*    */ package org.codehaus.jackson.map.jsontype.impl;
/*    */ 
/*    */ import org.codehaus.jackson.annotate.JsonTypeInfo.As;
/*    */ import org.codehaus.jackson.map.BeanProperty;
/*    */ import org.codehaus.jackson.map.TypeSerializer;
/*    */ import org.codehaus.jackson.map.jsontype.TypeIdResolver;
/*    */ 
/*    */ public abstract class TypeSerializerBase extends TypeSerializer
/*    */ {
/*    */   protected final TypeIdResolver _idResolver;
/*    */   protected final BeanProperty _property;
/*    */ 
/*    */   protected TypeSerializerBase(TypeIdResolver idRes, BeanProperty property)
/*    */   {
/* 20 */     this._idResolver = idRes;
/* 21 */     this._property = property;
/*    */   }
/*    */ 
/*    */   public abstract JsonTypeInfo.As getTypeInclusion();
/*    */ 
/*    */   public String getPropertyName() {
/* 28 */     return null;
/*    */   }
/*    */   public TypeIdResolver getTypeIdResolver() {
/* 31 */     return this._idResolver;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.jsontype.impl.TypeSerializerBase
 * JD-Core Version:    0.6.0
 */