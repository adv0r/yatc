/*    */ package org.codehaus.jackson.map.ext;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ import org.codehaus.jackson.JsonGenerationException;
/*    */ import org.codehaus.jackson.JsonGenerator;
/*    */ import org.codehaus.jackson.JsonNode;
/*    */ import org.codehaus.jackson.map.SerializerProvider;
/*    */ import org.codehaus.jackson.map.ser.SerializerBase;
/*    */ import org.w3c.dom.Node;
/*    */ import org.w3c.dom.bootstrap.DOMImplementationRegistry;
/*    */ import org.w3c.dom.ls.DOMImplementationLS;
/*    */ import org.w3c.dom.ls.LSSerializer;
/*    */ 
/*    */ public class DOMSerializer extends SerializerBase<Node>
/*    */ {
/*    */   protected final DOMImplementationLS _domImpl;
/*    */ 
/*    */   public DOMSerializer()
/*    */   {
/* 22 */     super(Node.class);
/*    */     DOMImplementationRegistry registry;
/*    */     try
/*    */     {
/* 25 */       registry = DOMImplementationRegistry.newInstance();
/*    */     } catch (Exception e) {
/* 27 */       throw new IllegalStateException("Could not instantiate DOMImplementationRegistry: " + e.getMessage(), e);
/*    */     }
/* 29 */     this._domImpl = ((DOMImplementationLS)registry.getDOMImplementation("LS"));
/*    */   }
/*    */ 
/*    */   public void serialize(Node value, JsonGenerator jgen, SerializerProvider provider)
/*    */     throws IOException, JsonGenerationException
/*    */   {
/* 36 */     if (this._domImpl == null) throw new IllegalStateException("Could not find DOM LS");
/* 37 */     LSSerializer writer = this._domImpl.createLSSerializer();
/* 38 */     jgen.writeString(writer.writeToString(value));
/*    */   }
/*    */ 
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*    */   {
/* 45 */     return createSchemaNode("string", true);
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ext.DOMSerializer
 * JD-Core Version:    0.6.0
 */