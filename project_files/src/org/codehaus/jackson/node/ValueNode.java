/*    */ package org.codehaus.jackson.node;
/*    */ 
/*    */ import org.codehaus.jackson.JsonNode;
/*    */ import org.codehaus.jackson.JsonToken;
/*    */ 
/*    */ public abstract class ValueNode extends BaseJsonNode
/*    */ {
/*    */   public boolean isValueNode()
/*    */   {
/* 17 */     return true;
/*    */   }
/*    */ 
/*    */   public abstract JsonToken asToken();
/*    */ 
/*    */   public JsonNode path(String fieldName)
/*    */   {
/* 29 */     return MissingNode.getInstance();
/*    */   }
/*    */   public JsonNode path(int index) {
/* 32 */     return MissingNode.getInstance();
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 41 */     return getValueAsText();
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.node.ValueNode
 * JD-Core Version:    0.6.0
 */