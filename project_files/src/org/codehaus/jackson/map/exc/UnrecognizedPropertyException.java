/*    */ package org.codehaus.jackson.map.exc;
/*    */ 
/*    */ import org.codehaus.jackson.JsonLocation;
/*    */ import org.codehaus.jackson.JsonParser;
/*    */ import org.codehaus.jackson.map.JsonMappingException;
/*    */ 
/*    */ public class UnrecognizedPropertyException extends JsonMappingException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected final Class<?> _referringClass;
/*    */   protected final String _unrecognizedPropertyName;
/*    */ 
/*    */   public UnrecognizedPropertyException(String msg, JsonLocation loc, Class<?> referringClass, String propName)
/*    */   {
/* 36 */     super(msg, loc);
/* 37 */     this._referringClass = referringClass;
/* 38 */     this._unrecognizedPropertyName = propName;
/*    */   }
/*    */ 
/*    */   public static UnrecognizedPropertyException from(JsonParser jp, Object fromObjectOrClass, String propertyName)
/*    */   {
/* 43 */     if (fromObjectOrClass == null)
/* 44 */       throw new IllegalArgumentException();
/*    */     Class ref;
/*    */     Class ref;
/* 47 */     if ((fromObjectOrClass instanceof Class))
/* 48 */       ref = (Class)fromObjectOrClass;
/*    */     else {
/* 50 */       ref = fromObjectOrClass.getClass();
/*    */     }
/* 52 */     String msg = "Unrecognized field \"" + propertyName + "\" (Class " + ref.getName() + "), not marked as ignorable";
/* 53 */     UnrecognizedPropertyException e = new UnrecognizedPropertyException(msg, jp.getCurrentLocation(), ref, propertyName);
/*    */ 
/* 55 */     e.prependPath(fromObjectOrClass, propertyName);
/* 56 */     return e;
/*    */   }
/*    */ 
/*    */   public Class<?> getReferringClass()
/*    */   {
/* 64 */     return this._referringClass;
/*    */   }
/*    */ 
/*    */   public String getUnrecognizedPropertyName()
/*    */   {
/* 73 */     return this._unrecognizedPropertyName;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.exc.UnrecognizedPropertyException
 * JD-Core Version:    0.6.0
 */