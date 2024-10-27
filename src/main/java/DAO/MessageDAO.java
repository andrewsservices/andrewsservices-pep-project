package DAO;
import Util.ConnectionUtil;
import java.util.List;

import Model.Message;
import java.sql.*;
import java.util.ArrayList;

public class MessageDAO {

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> Messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM Message";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message newMessage = new Message(rs.getInt("message_id"),
                rs.getInt("posted_by"),rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
                Messages.add(newMessage);
            }
        } catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return Messages;
    }

    public Message addMessage(Message message){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "insert into Message (posted_by, message_text, time_posted_epoch) values (?,?,?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,message.getPosted_by());
            preparedStatement.setString(2,message.getMessage_text());
            preparedStatement.setLong(3,message.getTime_posted_epoch());
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_Message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_Message_id, message.getPosted_by()
                ,message.getMessage_text(),message.getTime_posted_epoch());
            }
        } catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public Message getMessageById(int messageId){
        Connection connection = ConnectionUtil.getConnection();
        Message retrievedMessage = new Message();
        try{
            String sql = "SELECT * FROM message WHERE message_id = (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,messageId);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                
                retrievedMessage.setMessage_id(rs.getInt("message_id"));
                retrievedMessage.setPosted_by(rs.getInt("posted_by"));
                retrievedMessage.setMessage_text(rs.getString("message_text"));
                retrievedMessage.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
            }
        } catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return retrievedMessage;
    }
}
