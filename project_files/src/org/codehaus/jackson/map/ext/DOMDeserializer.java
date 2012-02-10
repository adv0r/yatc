/*    */ package org.codehaus.jackson.map.ext;
/*    */ 
/*    */ import java.io.StringReader;
/*    */ import javax.xml.parsers.DocumentBuilder;
/*    */ import javax.xml.parsers.DocumentBuilderFactory;
/*    */ import org.codehaus.jackson.map.DeserializationContext;
/*    */ import org.codehaus.jackson.map.deser.FromStringDeserializer;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Node;
/*    */ import org.xml.sax.InputSource;
/*    */ 
/*    */ public abstract class DOMDeserializer<T> extends FromStringDeserializer<T>
/*    */ {
/* 22 */   static final DocumentBuilderFactory _parserFactory = DocumentBuilderFactory.newInstance();
/*    */ 
/*    */   protected DOMDeserializer(Class<T> cls)
/*    */   {
/* 27 */     super(cls);
/*    */   }
/*    */ 
/*    */   public abstract T _deserialize(String paramString, DeserializationContext paramDeserializationContext);
/*    */ 
/*    */   protected final Document parse(String value) throws IllegalArgumentException {
/*    */     try {
/* 35 */       return _parserFactory.newDocumentBuilder().parse(new InputSource(new StringReader(value))); } catch (Exception e) {
/*    */     }
/* 37 */     throw new IllegalArgumentException("Failed to parse JSON String as XML: " + e.getMessage(), e);
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 24 */     _parserFactory.setNamespaceAware(true);
/*    */   }
/*    */ 
/*    */   public static class DocumentDeserializer extends DOMDeserializer<Document>
/*    */   {
/*    */     public DocumentDeserializer()
/*    */     {
/* 58 */       super();
/*    */     }
/*    */     public Document _deserialize(String value, DeserializationContext ctxt) throws IllegalArgumentException {
/* 61 */       return parse(value);
/*    */     }
/*    */   }
/*    */ 
/*    */   public static class NodeDeserializer extends DOMDeserializer<Node>
/*    */   {
/*    */     public NodeDeserializer()
/*    */     {
/* 49 */       super();
/*    */     }
/*    */     public Node _deserialize(String value, DeserializationContext ctxt) throws IllegalArgumentException {
/* 52 */       return parse(value);
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ext.DOMDeserializer
 * JD-Core Version:    0.6.0
 */