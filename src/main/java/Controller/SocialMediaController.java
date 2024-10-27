package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("/accounts", this::getAccountsHandler);
        app.get("/messages", this::getMessagesHandler);
        app.post("/register", this::registerAccountHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessageById);
        app.get("accounts/{account_id}/messages", this::getMessagesByAccountHanlder);


        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */

     private void getAccountsHandler(Context context) {
        context.json(accountService.getAllAccounts());
    }

    private void getMessagesHandler(Context context) {
        context.json(messageService.getAllMessages());
    }

     private void registerAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount==null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(addedAccount));
        }
    }

    private void loginHandler(Context context)throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account accountToVerify = mapper.readValue(context.body(), Account.class);
        Account verifiedAccount = accountService.verifyAccount(accountToVerify);
        if(verifiedAccount != null){
            context.json(mapper.writeValueAsString(verifiedAccount));
        }else{
            context.status(401);
        }
    }

    private void postMessageHandler(Context context) throws JsonProcessingException {
        int[] accountIds = accountService.getAccountIds();
        ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(context.body(), Message.class);
            Message addedMessage = messageService.addMessage(message);
            if(addedMessage==null){
                context.status(400);
            }else{
                for(int id: accountIds){
                    if(addedMessage.getPosted_by() == id){
                        context.json(mapper.writeValueAsString(addedMessage));
                    } else {
                        context.status(400);
                    }
                }
                
            }
    }

    private void getMessageById(Context context){
        Message retrievedMessage = messageService.getMessageById(context.pathParam("message_id"));
        if(retrievedMessage.getMessage_text() == null){
            System.out.println("empty");
        } else {
            context.json(retrievedMessage);
        }
        
    }

    private void deleteMessageById(Context context){
        Message retrievedMessage = messageService.getMessageById(context.pathParam("message_id"));
        List<Message> messages = messageService.getAllMessages();
        if(retrievedMessage.getMessage_text() == null){
            System.out.println("empty");
        } else {
            Message deletedMessage = messages.remove(retrievedMessage.getMessage_id() - 1);
            context.json(deletedMessage);
        }
        
    }

    public void updateMessageById(Context context)throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        String messageText = message.getMessage_text();

        Message retrievedMessage = messageService.getMessageById(context.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(retrievedMessage,messageText);

        int[] messageIds = messageService.getMessageIds();

        for(int id: messageIds){
            if(retrievedMessage.getMessage_id() == id){
                
                if(updatedMessage == null){
                    context.status(400);
                } else {
                    context.json(updatedMessage);
                }
            } else {
                context.status(400);
            }
        }
    }

    public void getMessagesByAccountHanlder(Context context){
        
        Account selectedAccount = accountService.getAccountById(context.pathParam("account_id"));
        int accountId = selectedAccount.getAccount_id();

        List<Message> allMessages = messageService.getAllMessages();
        List<Message> allMessagesByUser = new ArrayList<>();

        for(Message message : allMessages){
            int posted_by = message.getPosted_by();

            if(posted_by == accountId){
                allMessagesByUser.add(message);
            }
        }
        context.json(allMessagesByUser);
    }
}