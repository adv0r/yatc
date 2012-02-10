/*    */ package org.codehaus.jackson.schema;
/*    */ 
/*    */ import org.codehaus.jackson.JsonNode;
/*    */ import org.codehaus.jackson.annotate.JsonCreator;
/*    */ import org.codehaus.jackson.annotate.JsonValue;
/*    */ import org.codehaus.jackson.node.JsonNodeFactory;
/*    */ import org.codehaus.jackson.node.ObjectNode;
/*    */ 
/*    */ public class JsonSchema
/*    */ {
/*    */   private final ObjectNode schema;
/*    */ 
/*    */   @JsonCreator
/*    */   public JsonSchema(ObjectNode schema)
/*    */   {
/* 30 */     this.schema = schema;
/*    */   }
/*    */ 
/*    */   @JsonValue
/*    */   public ObjectNode getSchemaNode()
/*    */   {
/* 45 */     return this.schema;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 51 */     return this.schema.toString();
/*    */   }
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 57 */     if (o == this) return true;
/* 58 */     if (o == null) return false;
/* 59 */     if (!(o instanceof JsonSchema)) return false;
/*    */ 
/* 61 */     JsonSchema other = (JsonSchema)o;
/* 62 */     if (this.schema == null) {
/* 63 */       return other.schema == null;
/*    */     }
/* 65 */     return this.schema.equals(other.schema);
/*    */   }
/*    */ 
/*    */   public static JsonNode getDefaultSchemaNode()
/*    */   {
/* 75 */     ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
/* 76 */     objectNode.put("type", "any");
/* 77 */     objectNode.put("optional", true);
/* 78 */     return objectNode;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.schema.JsonSchema
 * JD-Core Version:    0.6.0
 */