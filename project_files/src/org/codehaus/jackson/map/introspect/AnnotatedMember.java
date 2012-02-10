/*    */ package org.codehaus.jackson.map.introspect;
/*    */ 
/*    */ import java.lang.reflect.Member;
/*    */ import org.codehaus.jackson.map.util.ClassUtil;
/*    */ 
/*    */ public abstract class AnnotatedMember extends Annotated
/*    */ {
/*    */   public abstract Class<?> getDeclaringClass();
/*    */ 
/*    */   public abstract Member getMember();
/*    */ 
/*    */   public final void fixAccess()
/*    */   {
/* 30 */     ClassUtil.checkAndFixAccess(getMember());
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.introspect.AnnotatedMember
 * JD-Core Version:    0.6.0
 */