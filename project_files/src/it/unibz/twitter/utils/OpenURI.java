/*    */ package it.unibz.twitter.utils;
/*    */ 
/*    */ import java.awt.Desktop;
/*    */ import java.awt.Desktop.Action;
/*    */ import java.io.PrintStream;
/*    */ import java.net.URI;
/*    */ 
/*    */ public class OpenURI
/*    */ {
/*    */   public static void open(String URL)
/*    */   {
/*  7 */     if (!Desktop.isDesktopSupported())
/*    */     {
/*  9 */       System.err.println("Desktop is not supported (fatal)");
/* 10 */       System.exit(1);
/*    */     }
/*    */ 
/* 15 */     Desktop desktop = Desktop.getDesktop();
/*    */ 
/* 17 */     if (!desktop.isSupported(Desktop.Action.BROWSE))
/*    */     {
/* 19 */       System.err.println("Desktop doesn't support the browse action (fatal)");
/* 20 */       System.exit(1);
/*    */     }
/*    */ 
/*    */     try
/*    */     {
/* 25 */       URI uri = new URI(URL);
/* 26 */       desktop.browse(uri);
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 30 */       System.err.println(e.getMessage());
/*    */     }
/*    */   }
/*    */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     it.unibz.twitter.utils.OpenURI
 * JD-Core Version:    0.6.0
 */