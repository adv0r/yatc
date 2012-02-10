/*    */ package org.codehaus.jackson.map;
/*    */ 
/*    */ import org.codehaus.jackson.JsonFactory;
/*    */ 
/*    */ public class MappingJsonFactory extends JsonFactory
/*    */ {
/*    */   public MappingJsonFactory()
/*    */   {
/* 32 */     this(null);
/*    */   }
/*    */ 
/*    */   public MappingJsonFactory(ObjectMapper mapper)
/*    */   {
/* 37 */     super(mapper);
/* 38 */     if (mapper == null)
/* 39 */       setCodec(new ObjectMapper(this));
/*    */   }
/*    */ 
/*    */   public final ObjectMapper getCodec()
/*    */   {
/* 48 */     return (ObjectMapper)this._objectCodec;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.MappingJsonFactory
 * JD-Core Version:    0.6.0
 */