/*    */ package org.codehaus.jackson.map.jsontype.impl;
/*    */ 
/*    */ import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
/*    */ import org.codehaus.jackson.type.JavaType;
/*    */ 
/*    */ public class MinimalClassNameIdResolver extends ClassNameIdResolver
/*    */ {
/*    */   protected final String _basePackageName;
/*    */   protected final String _basePackagePrefix;
/*    */ 
/*    */   protected MinimalClassNameIdResolver(JavaType baseType)
/*    */   {
/* 23 */     super(baseType);
/* 24 */     String base = baseType.getRawClass().getName();
/* 25 */     int ix = base.lastIndexOf('.');
/* 26 */     if (ix < 0) {
/* 27 */       this._basePackageName = "";
/* 28 */       this._basePackagePrefix = ".";
/*    */     } else {
/* 30 */       this._basePackagePrefix = base.substring(0, ix + 1);
/* 31 */       this._basePackageName = base.substring(0, ix);
/*    */     }
/*    */   }
/*    */ 
/*    */   public JsonTypeInfo.Id getMechanism() {
/* 36 */     return JsonTypeInfo.Id.MINIMAL_CLASS;
/*    */   }
/*    */ 
/*    */   public String idFromValue(Object value)
/*    */   {
/* 41 */     String n = value.getClass().getName();
/* 42 */     if (n.startsWith(this._basePackagePrefix))
/*    */     {
/* 44 */       return n.substring(this._basePackagePrefix.length() - 1);
/*    */     }
/* 46 */     return n;
/*    */   }
/*    */ 
/*    */   public JavaType typeFromId(String id)
/*    */   {
/* 52 */     if (id.startsWith(".")) {
/* 53 */       StringBuilder sb = new StringBuilder(id.length() + this._basePackageName.length());
/* 54 */       if (this._basePackageName.length() == 0)
/*    */       {
/* 56 */         sb.append(id.substring(1));
/*    */       }
/*    */       else {
/* 59 */         sb.append(this._basePackageName).append(id);
/*    */       }
/* 61 */       id = sb.toString();
/*    */     }
/* 63 */     return super.typeFromId(id);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.jsontype.impl.MinimalClassNameIdResolver
 * JD-Core Version:    0.6.0
 */