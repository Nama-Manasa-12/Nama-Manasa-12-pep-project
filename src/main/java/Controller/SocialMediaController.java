package Controller;

import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */

public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/login", this::loginUser);
        app.post("/register", this::CreateHandler);
        app.post("/messages", this::CreateMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.get("/accounts/{account_Id}/messages",this::getMessagesByUser);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}",this::UpdateMessage);
        return app;
    }

    private void CreateHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        try {
            ObjectMapper om = new ObjectMapper();
            Account account = om.readValue(ctx.body(), Account.class);
            if (account.getUsername().isEmpty()) {
                ctx.status(400);
                ctx.result("");
                return;
            } else if (accountService.getAccountByUsername(account.getUsername()) != null) {
                ctx.status(400);
                ctx.result("");
                return;
            } else if (account.getPassword().length() < 4) {
                ctx.status(400);
                ctx.result("");
                return;
            }
            Account addAccount = accountService.registerUser(account);
            ctx.status(200);
            ctx.json(addAccount);

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500);
            return;
        }
    }

    private void loginUser(Context ctx) throws JsonMappingException, JsonProcessingException {
        Account loggedInAccount = null;
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);

        if (account.getUsername().isEmpty()) {
            ctx.status(400);
            return;
        }
        try {
            loggedInAccount = accountService.getAccountByUsername(account.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500);
            return;
        }
        if (loggedInAccount != null && loggedInAccount.getPassword().contains(account.getPassword())) {
            ctx.status(200);
            ctx.json(loggedInAccount);
        } else {
            ctx.status(401);
        }
    }

    // 1
    private void CreateMessage(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(ctx.body(), Message.class);
        try {
            // Message v = messageService.createMessage(message);
            Message v = messageService.CreateMessage(message);

            if (isValidMessage(message)) {
                ctx.status(200);
                ctx.json(v);
            } else {
                ctx.status(400);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean isValidMessage(Message message) throws SQLException {
        if (message == null) {
            System.out.println("1");
            return false;
        }
        if (message.getMessage_text().isEmpty()) {
            System.out.println("2");
            return false;
        }

        if (!messageService.doesUserExist(message.getPosted_by())) {
            System.out.println("3");
            return false;

        }
        if (message.getMessage_text().length() >= 255) {
            System.out.println("4");
            return false;
        }
        return true;
    }

    private void getAllMessages(Context ctx) throws JsonProcessingException, SQLException {
        
        List<Message> message = messageService.getAllMessages();
        ctx.json(message);

    }

    private void getMessageById(Context ctx) throws JsonMappingException, JsonProcessingException {
        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessageById(messageId);
            if (message != null) {
                ctx.json(message);

            } else {
                ctx.status(404);

            }
            ctx.status(200);
        } catch (NumberFormatException e) {
            ctx.status(400);
            return;
        }
    }

    private void deleteMessage(Context ctx) throws JsonProcessingException, SQLException {

        try {
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message msg = messageService.getMessageById(messageId);
            boolean success = messageService.deleteMessage(messageId);
            if (success) {
                ctx.json(msg);

            }
            ctx.status(200);
        } catch (NumberFormatException e) {
            ctx.status(400);
        }
    }

    // 6 update


    private void UpdateMessage(Context ctx) throws JsonMappingException, JsonProcessingException, SQLException{
        try{
            int messageId=Integer.parseInt(ctx.pathParam("message_id"));
            ObjectMapper ob=new ObjectMapper();
            Message message=ob.readValue(ctx.body(),Message.class);
            System.out.println(message);
            String newMessageText = message.getMessage_text();
        if (newMessageText == null || newMessageText.trim().isEmpty() || newMessageText.length() >= 255 || messageService.getMessageById(messageId)==null) {
            ctx.status(400);
            return;
        }

        Message existingMessage=messageService.UpdateMessage(message,messageId);
        System.out.println(existingMessage);
        ctx.status(200);
        ctx.json(existingMessage);
        }catch(NumberFormatException e){
            ctx.status(400).result("");
        } 
    }

    private void getMessagesByUser(Context ctx){

        
        int accountId= Integer.parseInt(ctx.pathParam("account_Id"));
        List<Message> messages = messageService.getAllMessagesUser(accountId);
        ctx.json(messages).status(200);
    }
    
}
