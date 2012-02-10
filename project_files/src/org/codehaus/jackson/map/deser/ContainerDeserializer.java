/*    */ package org.codehaus.jackson.map.deser;
/*    */ 
/*    */ import org.codehaus.jackson.map.JsonDeserializer;
/*    */ import org.codehaus.jackson.type.JavaType;
/*    */ 
/*    */ public abstract class ContainerDeserializer<T> extends StdDeserializer<T>
/*    */ {
/*    */   protected ContainerDeserializer(Class<?> selfType)
/*    */   {
/* 18 */     super(selfType);
/*    */   }
/*    */ 
/*    */   public abstract JavaType getContentType();
/*    */ 
/*    */   public abstract JsonDeserializer<Object> getContentDeserializer();
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.deser.ContainerDeserializer
 * JD-Core Version:    0.6.0
 */