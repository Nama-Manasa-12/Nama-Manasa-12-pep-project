package Service;


import java.sql.SQLException;
import java.util.List;

import DAO.MessageDAO;
import Model.Message;


public class MessageService {
    MessageDAO messageDAO;
    public MessageService(){
       // return messageDAO.save(messageDAO);
        messageDAO= new MessageDAO();
    }
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    public Message CreateMessage(Message message){
        return messageDAO.CreateMessage(message);
    }
    
    public boolean doesUserExist(int id) throws SQLException{
        return messageDAO.doesUserExist(id);
    }
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }
    
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }
    

    public boolean deleteMessage(int messageId){
        return messageDAO.deleteMessage(messageId);
    }
    // public Message updateMessage(Message message){
    //     return messageDAO.updateMessage(message);
    // }

    public Message UpdateMessage(Message message,int id) throws SQLException {
        return messageDAO.updateMessage(message,id);
    }
    public List<Message> getAllMessagesUser(int messageId) {
        return messageDAO.getMessagesByUser(messageId);
    }
}

    