/*    */ package org.codehaus.jackson.map.type;
/*    */ 
/*    */ import java.lang.reflect.ParameterizedType;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ public class HierarchicType
/*    */ {
/*    */   protected final Type _actualType;
/*    */   protected final Class<?> _rawClass;
/*    */   protected final ParameterizedType _genericType;
/*    */   protected HierarchicType _superType;
/*    */   protected HierarchicType _subType;
/*    */ 
/*    */   public HierarchicType(Type type)
/*    */   {
/* 32 */     this._actualType = type;
/* 33 */     if ((type instanceof Class)) {
/* 34 */       this._rawClass = ((Class)type);
/* 35 */       this._genericType = null;
/* 36 */     } else if ((type instanceof ParameterizedType)) {
/* 37 */       this._genericType = ((ParameterizedType)type);
/* 38 */       this._rawClass = ((Class)this._genericType.getRawType());
/*    */     } else {
/* 40 */       throw new IllegalArgumentException("Type " + type.getClass().getName() + " can not be used to construct HierarchicType");
/*    */     }
/*    */   }
/*    */ 
/*    */   public void setSuperType(HierarchicType sup) {
/* 44 */     this._superType = sup; } 
/* 45 */   public HierarchicType getSuperType() { return this._superType; } 
/* 46 */   public void setSubType(HierarchicType sub) { this._subType = sub; } 
/* 47 */   public HierarchicType getSubType() { return this._subType; } 
/*    */   public boolean isGeneric() {
/* 49 */     return this._genericType != null; } 
/* 50 */   public ParameterizedType asGeneric() { return this._genericType; } 
/*    */   public Class<?> getRawClass() {
/* 52 */     return this._rawClass;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 56 */     if (this._genericType != null) {
/* 57 */       return this._genericType.toString();
/*    */     }
/* 59 */     return this._rawClass.getName();
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.type.HierarchicType
 * JD-Core Version:    0.6.0
 */