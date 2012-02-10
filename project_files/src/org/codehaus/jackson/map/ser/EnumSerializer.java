/*    */ package org.codehaus.jackson.map.ser;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ import org.codehaus.jackson.JsonGenerationException;
/*    */ import org.codehaus.jackson.JsonGenerator;
/*    */ import org.codehaus.jackson.JsonNode;
/*    */ import org.codehaus.jackson.io.SerializedString;
/*    */ import org.codehaus.jackson.map.AnnotationIntrospector;
/*    */ import org.codehaus.jackson.map.JsonMappingException;
/*    */ import org.codehaus.jackson.map.SerializationConfig;
/*    */ import org.codehaus.jackson.map.SerializationConfig.Feature;
/*    */ import org.codehaus.jackson.map.SerializerProvider;
/*    */ import org.codehaus.jackson.map.annotate.JacksonStdImpl;
/*    */ import org.codehaus.jackson.map.introspect.BasicBeanDescription;
/*    */ import org.codehaus.jackson.map.type.TypeFactory;
/*    */ import org.codehaus.jackson.map.util.EnumValues;
/*    */ import org.codehaus.jackson.node.ArrayNode;
/*    */ import org.codehaus.jackson.node.ObjectNode;
/*    */ import org.codehaus.jackson.type.JavaType;
/*    */ 
/*    */ @JacksonStdImpl
/*    */ public class EnumSerializer extends ScalarSerializerBase<Enum<?>>
/*    */ {
/*    */   protected final EnumValues _values;
/*    */ 
/*    */   public EnumSerializer(EnumValues v)
/*    */   {
/* 37 */     super(Enum.class, false);
/* 38 */     this._values = v;
/*    */   }
/*    */ 
/*    */   public static EnumSerializer construct(Class<Enum<?>> enumClass, SerializationConfig config, BasicBeanDescription beanDesc)
/*    */   {
/* 45 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 46 */     EnumValues v = config.isEnabled(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING) ? EnumValues.constructFromToString(enumClass, intr) : EnumValues.constructFromName(enumClass, intr);
/*    */ 
/* 48 */     return new EnumSerializer(v);
/*    */   }
/*    */ 
/*    */   public final void serialize(Enum<?> en, JsonGenerator jgen, SerializerProvider provider)
/*    */     throws IOException, JsonGenerationException
/*    */   {
/* 55 */     jgen.writeString(this._values.serializedValueFor(en));
/*    */   }
/*    */ 
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*    */     throws JsonMappingException
/*    */   {
/* 62 */     ObjectNode objectNode = createSchemaNode("string", true);
/*    */     ArrayNode enumNode;
/* 63 */     if (typeHint != null) {
/* 64 */       JavaType type = TypeFactory.type(typeHint);
/* 65 */       if (type.isEnumType()) {
/* 66 */         enumNode = objectNode.putArray("enum");
/* 67 */         for (SerializedString value : this._values.values()) {
/* 68 */           enumNode.add(value.getValue());
/*    */         }
/*    */       }
/*    */     }
/* 72 */     return objectNode;
/*    */   }
/*    */   public EnumValues getEnumValues() {
/* 75 */     return this._values;
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     org.codehaus.jackson.map.ser.EnumSerializer
 * JD-Core Version:    0.6.0
 */