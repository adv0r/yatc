/*     */ package it.unibz.twitter.client;
/*     */ 
/*     */ import it.unibz.twitter.model.Token;
/*     */ import it.unibz.twitter.model.Tweet;
/*     */ import it.unibz.twitter.model.User;
/*     */ import it.unibz.twitter.net.Memory;
/*     */ import it.unibz.twitter.net.TweetService;
/*     */ import it.unibz.twitter.utils.OpenURI;
/*     */ import it.unibz.twitter.utils.Utils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.security.SignatureException;
/*     */ import java.util.List;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ 
/*     */ public class App
/*     */ {
/*     */   private Utils u;
/*     */   private final Memory memory;
/*     */   private final TweetService ts;
/*     */   private boolean isUserAuth;
/*  25 */   private String userScreenName = "you";
/*     */   private Timer timer;
/*     */   private TimerTask task;
/*  28 */   private int defaultTweetNumber = 20;
/*  29 */   private int defaultRefreshInterval = 0;
/*     */ 
/*     */   public App()
/*     */   {
/*  35 */     boolean debugMode = false;
/*     */ 
/*  37 */     this.u = new Utils(debugMode);
/*  38 */     this.memory = new Memory();
/*  39 */     this.ts = new TweetService();
/*     */   }
/*     */ 
/*     */   public static void main(String[] debug)
/*     */   {
/*  49 */     App app = new App();
/*  50 */     app.start();
/*     */   }
/*     */ 
/*     */   private void start()
/*     */   {
/*  56 */     this.timer = new Timer();
/*  57 */     this.task = new TimerTask() {
/*     */       public void run() {
/*  59 */         App.this.refreshTimeline(App.this.defaultTweetNumber);
/*  60 */         App.this.u.print("Hint: Type !autoRefresh 0 to turn it off , !help to display the main menu");
/*  61 */         App.this.printBash();
/*     */       }
/*     */     };
/*  64 */     this.u.debug("App Started ");
/*  65 */     this.u.print("Hello dude, welcome to Y(et) A(nother) T(witter) C(lient)\n");
/*  66 */     this.u.print("Lets see if we know each other already ... \n");
/*     */ 
/*  68 */     Token userToken = this.memory.getUserToken();
/*     */ 
/*  70 */     if (userToken != null)
/*     */     {
/*  72 */       this.isUserAuth = true;
/*  73 */       this.u.debug("User is already Authenticated!");
/*  74 */       this.userScreenName = this.ts.getUserScreenName();
/*  75 */       this.u.print("Oh, yes we do. Welcome back " + this.userScreenName + " !");
/*  76 */       displayMenu(false);
/*     */     }
/*     */     else
/*     */     {
/*  80 */       this.isUserAuth = false;
/*  81 */       while (!this.isUserAuth)
/*     */         try {
/*  83 */           this.u.debug("Tring to Authenticate the User");
/*  84 */           authUser();
/*  85 */           this.isUserAuth = true;
/*  86 */           this.userScreenName = this.ts.getUserScreenName();
/*  87 */           this.u.print("Welcome  " + this.userScreenName + " !");
/*  88 */           this.u.debug("User " + this.userScreenName + " is Authenticated!");
/*  89 */           displayMenu(false);
/*     */         } catch (Exception e) {
/*  91 */           System.err.println("3rr0r: I don't know who you are. I can't let you go");
/*  92 */           e.printStackTrace();
/*  93 */           this.isUserAuth = false;
/*  94 */           displayMenu(false);
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void displayMenu(boolean cleanConsole)
/*     */   {
/* 104 */     this.u.debug("User info: " + this.ts.getUserScreenName());
/*     */ 
/* 106 */     this.u.debug("displayMenu called");
/*     */ 
/* 108 */     if (cleanConsole) {
/* 109 */       this.u.cleanConsole();
/*     */     }
/* 111 */     this.u.print("");
/* 112 */     this.u.print("#-----------------------*** Main Menu ***--------------------------------#");
/* 113 */     this.u.print("Type !help to show this menu");
/* 114 */     this.u.print("#-----------------------*Twitter .commands*------------------------------#");
/* 115 */     this.u.print("!tweet <#something>");
/* 116 */     this.u.print("!follow <@someone>");
/* 117 */     this.u.print("!unfollow <@the-bad-guy>");
/* 118 */     this.u.print("#-----------------------*Timeline .commands*-----------------------------#");
/* 119 */     this.u.print("Refresh your last !timeline <n> tweets");
/* 120 */     this.u.print("Use !defaultTweets <number> , to change default tweets displayed [now " + this.defaultTweetNumber + "]");
/* 121 */     this.u.print("Use !autorefresh <seconds> , to change the refresh interval [now " + this.defaultRefreshInterval + ", 0 = off]");
/* 122 */     this.u.print("#-----------------------*Other .commands*--------------------------------#");
/* 123 */     this.u.print("!clean the screen");
/* 124 */     this.u.print("!change current user");
/* 125 */     this.u.print("!quit this .app");
/* 126 */     this.u.print("#------------------------------------------------------------------------#\n");
/* 127 */     this.u.print("Input your !command and hit Enter : \n");
/* 128 */     printBash();
/* 129 */     System.out.flush();
/*     */ 
/* 131 */     InputStreamReader converter = new InputStreamReader(System.in);
/* 132 */     BufferedReader in = new BufferedReader(converter);
/* 133 */     String cmd = "";
/*     */     try {
/* 135 */       while ((cmd = in.readLine()) != null)
/* 136 */         if (cmd.startsWith("!tweet")) {
/* 137 */           String tweetTxt = cmd.substring("!tweet".length());
/* 138 */           if (tweetTxt.length() > 0) {
/* 139 */             tweet(tweetTxt.substring(1));
/*     */           }
/*     */           else
/* 142 */             this.u.print("3rr0r: you forget to tell me what do you want me to tweet. Use !tweet <some text here> ");
/* 143 */           printBash();
/*     */         }
/* 145 */         else if (cmd.startsWith("!follow ")) {
/* 146 */           String username = cmd.substring("!follow".length());
/* 147 */           if (username.length() > 0)
/* 148 */             follow(username.replace("@", "").substring(1));
/*     */           else
/* 150 */             this.u.print("3rr0r: you forget to write the username of the person you want to follow. Use !follow <@username here> ");
/* 151 */           printBash();
/*     */         }
/* 154 */         else if (cmd.startsWith("!unfollow ")) {
/* 155 */           String username = cmd.substring("!unfollow".length());
/* 156 */           if (username.length() > 0)
/* 157 */             unfollow(username.replace("@", "").substring(1));
/*     */           else
/* 159 */             this.u.print("3rr0r: you forget to write the username of the bad guy. Use !unfollow <@username here> ");
/* 160 */           printBash();
/*     */         }
/* 163 */         else if (cmd.startsWith("!defaultTweets "))
/*     */         {
/*     */           try {
/* 166 */             int how_many = Integer.parseInt(cmd.substring("!defaultTweets ".length()));
/* 167 */             if ((how_many > 200) || (how_many < 0))
/*     */             {
/* 169 */               throw new Exception();
/*     */             }
/*     */ 
/* 172 */             this.defaultTweetNumber = how_many;
/* 173 */             this.u.print("Default number of tweet changed succesfully! Wait or refresh manually ( !timeline )");
/* 174 */             printBash();
/*     */           }
/*     */           catch (Exception e)
/*     */           {
/* 180 */             this.u.print("3rr0r : you should specify an integer positive number [<200]");
/* 181 */             printBash();
/*     */           }
/*     */ 
/*     */         }
/* 187 */         else if (cmd.startsWith("!autoRefresh "))
/*     */         {
/*     */           try {
/* 190 */             int sec = Integer.parseInt(cmd.substring("!autoRefresh ".length()));
/*     */ 
/* 192 */             if (sec == 0)
/*     */             {
/* 194 */               this.defaultRefreshInterval = 0;
/* 195 */               this.timer.cancel();
/* 196 */               this.timer.purge();
/* 197 */               this.u.print("You just switched off the auto refresh");
/* 198 */               printBash(); continue;
/*     */             }
/*     */ 
/* 202 */             if (sec > 20) {
/* 203 */               this.defaultRefreshInterval = sec;
/* 204 */               this.timer.scheduleAtFixedRate(this.task, this.defaultRefreshInterval * 1000, this.defaultRefreshInterval * 1000);
/* 205 */               this.u.print("The timeline will be now autorefreshed every " + this.defaultRefreshInterval + " seconds . Type !autoRefresh 0 to turn it off!");
/* 206 */               printBash(); continue;
/*     */             }
/*     */ 
/* 209 */             throw new Exception();
/*     */           }
/*     */           catch (Exception e)
/*     */           {
/* 215 */             this.u.print("3rr0r : you should specify an integer number of seconds [min 20 sec] ");
/* 216 */             printBash();
/*     */           }
/*     */ 
/*     */         }
/* 222 */         else if (cmd.equals("!timeline"))
/*     */         {
/* 224 */           refreshTimeline(this.defaultTweetNumber);
/* 225 */           printBash();
/*     */         }
/* 228 */         else if ((cmd.startsWith("!timeline ")) && (!cmd.equals("!timeline")))
/*     */         {
/*     */           try {
/* 231 */             int how_many = Integer.parseInt(cmd.substring("!timeline ".length()));
/* 232 */             if ((how_many > 200) || (how_many < 0))
/*     */             {
/* 234 */               throw new Exception();
/*     */             }
/*     */ 
/* 237 */             refreshTimeline(how_many);
/* 238 */             printBash();
/*     */           }
/*     */           catch (Exception e)
/*     */           {
/* 242 */             this.u.print("3rr0r : you should specify an integer number < 200");
/* 243 */             printBash();
/*     */           }
/*     */ 
/*     */         }
/* 247 */         else if (cmd.equals("!help")) {
/* 248 */           displayMenu(true);
/*     */         }
/* 250 */         else if (cmd.equals("!quit")) {
/* 251 */           quit();
/*     */         }
/* 253 */         else if (cmd.equals("!change")) {
/* 254 */           this.u.debug("User requested a change");
/* 255 */           authUser();
/* 256 */           this.u.debug("New User authenticated a logout");
/* 257 */           displayMenu(false);
/*     */         }
/* 259 */         else if (cmd.equals("!clean")) {
/* 260 */           this.u.debug("User requested a cleanscreen");
/* 261 */           for (int i = 0; i < 100; i++)
/* 262 */             System.out.println("");
/* 263 */           printBash();
/*     */         }
/*     */         else
/*     */         {
/* 267 */           this.u.cleanConsole();
/* 268 */           this.u.print("\n ####(" + cmd + ") ? Say whaat?\n");
/* 269 */           displayMenu(false);
/*     */         }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 274 */       this.u.debug(e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void printBash()
/*     */   {
/* 282 */     System.out.print("YATC:~ " + this.userScreenName + "$ >");
/*     */   }
/*     */ 
/*     */   private void tweet(String tweetTxt)
/*     */   {
/* 288 */     this.ts.tweet(tweetTxt);
/* 289 */     this.u.debug("Request to tweet :" + tweetTxt);
/* 290 */     this.u.print("Tweet submittet correctly!");
/*     */   }
/*     */ 
/*     */   private void unfollow(String username)
/*     */   {
/* 295 */     this.u.debug("Request to !unfollow :" + username);
/* 296 */     username = username.replace("@", "");
/* 297 */     this.ts.unfollow(username);
/* 298 */     this.u.print("You will not read the annoying posts of @" + username);
/*     */   }
/*     */ 
/*     */   private void follow(String username) {
/* 302 */     this.u.debug("Request to !follow :" + username);
/* 303 */     username = username.replace("@", "");
/* 304 */     this.ts.follow(username);
/* 305 */     this.u.print("You will now be bothered by  @" + username + "'s tweets");
/*     */   }
/*     */ 
/*     */   private void refreshTimeline(int how_many) {
/* 309 */     this.u.debug("Request to refresh timeline ( howmany = " + how_many + " )");
/*     */     try
/*     */     {
/* 312 */       List tweets = null;
/* 313 */       if (how_many == 0)
/*     */       {
/* 315 */         tweets = this.ts.getTweetsSinceX(this.memory.getLastViewedTweet());
/*     */       }
/*     */       else {
/* 318 */         tweets = this.ts.getTimeline(how_many);
/*     */       }
/* 320 */       this.u.print("\n ------------------------------------------ .timeLine [begin] ------------------------------------------ \n");
/*     */ 
/* 322 */       this.u.print(" ");
/* 323 */       if (tweets.size() > 0) {
/* 324 */         Tweet last = (Tweet)tweets.get(0);
/* 325 */         this.memory.setLastViewedTweet(last.getId());
/* 326 */         for (Tweet twit : tweets)
/* 327 */           this.u.print("@" + twit.getUser().getScreen_name() + " .says :  " + twit.getText());
/*     */       }
/*     */       else {
/* 330 */         this.u.print("Go buy new friends. (or try !timeline 10 )");
/*     */       }
/* 332 */       this.u.print(" ");
/* 333 */       this.u.print("\n ------------------------------------------ .timeLine [end] ------------------------------------------ \n");
/*     */ 
/* 335 */       System.out.flush();
/*     */     } catch (Exception e) {
/* 337 */       this.u.print("Request didn't worked out, tut mir leid");
/* 338 */       this.u.print(e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void quit()
/*     */   {
/* 345 */     this.u.debug("User quitted");
/* 346 */     this.u.cleanConsole();
/* 347 */     this.u.print("c u s00n,dude");
/* 348 */     System.exit(0);
/*     */   }
/*     */ 
/*     */   private void authUser()
/*     */     throws SignatureException, IOException
/*     */   {
/* 354 */     Token requestToken = this.ts.getRequestToken();
/* 355 */     String authURL = this.ts.getAuthorizationURL(requestToken);
/* 356 */     OpenURI.open(authURL);
/* 357 */     this.u.print("Great. \nBefore we start getting along, there is something you have to do for me:");
/* 358 */     this.u.print("1. Authorize the application to access your data ( via the browser )");
/* 359 */     this.u.print("2. Type the s3cr3t PIN code you received and press enter");
/* 360 */     printBash();
/*     */ 
/* 362 */     InputStreamReader converter = new InputStreamReader(System.in);
/* 363 */     BufferedReader in = new BufferedReader(converter);
/* 364 */     String pin = in.readLine().trim();
/*     */ 
/* 366 */     Token accessToken = this.ts.getAccessToken(requestToken, pin);
/* 367 */     this.memory.setUserToken(accessToken);
/*     */   }
/*     */ }

/* Location:           /Users/Advanced/Desktop/EMSE/Year1/Semester2/Computer Networks/Assignment1_twitter/yatc/project_files/yatc.jar
 * Qualified Name:     it.unibz.twitter.client.App
 * JD-Core Version:    0.6.0
 */