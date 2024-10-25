package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Service.AccountService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;

    public SocialMediaController(){
        accountService = new AccountService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("/accounts", this::getAccountsHandler);
        app.post("/register", this::registerAccountHandler);
        app.post("/login", this::loginHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */

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


    private void getAccountsHandler(Context context) {
        context.json(accountService.getAllAccounts());
    }

    private void loginHandler(Context context)throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account accountToVerify = mapper.readValue(context.body(), Account.class);
        Account verifiedAccount = accountService.verifyAccount(accountToVerify);
        System.out.println("verified account: " + verifiedAccount);
        if(verifiedAccount != null){
            context.json(mapper.writeValueAsString(verifiedAccount));
        }else{
            context.status(401);
        }
    }


}