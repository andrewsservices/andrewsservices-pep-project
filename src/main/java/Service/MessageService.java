package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Account;
import Model.Message;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }


    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public int[] getMessageIds(){
        List<Message> messages = messageDAO.getAllMessages();
        int[] messageIds = new int[messages.size()];
        
        for(int i = 0; i < messages.size(); i++){
            messageIds[i] = messages.get(i).getMessage_id();
        }
        return messageIds;
    }

    public Message addMessage(Message message){
        String messageText = message.getMessage_text();
        if(messageText.length() > 0 && messageText.length() < 225){
            return messageDAO.addMessage(message);
        }
        return null;

    }

    public Message getMessageById(String messageId){
        int messageIdInt = Integer.parseInt(messageId);
        Message retrievedMessage = messageDAO.getMessageById(messageIdInt);
        
        return retrievedMessage;

    }

}
