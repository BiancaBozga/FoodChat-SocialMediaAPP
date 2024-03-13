package com.example.demo2.persistence.dbrepos;



import com.example.demo2.domain.entities.Message;
import com.example.demo2.persistence.Repository;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;


public class MessageDBRepository implements Repository<Long, Message> {
    private final String url;
    private final String username;
    private final String password;

    public MessageDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }
    private List<Long> find_to_list(Long aLong){
        ArrayList<Long> l=new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from messagesusers " +
                    "where id_message = ?");

        ) {

            statement.setInt(1, Math.toIntExact(aLong));
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {

                Long to=resultSet.getLong("id_to_user");
                l.add(to);


            }


            return l;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //private final Validator<User> validator;
    @Override
    public Optional<Message> findOne(Long aLong) {
        if (aLong == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from messages " +
                    "where id = ?");

        ) {
            statement.setInt(1, Math.toIntExact(aLong));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                 String message=resultSet.getString("message");
                 Long from=resultSet.getLong("from_id");
                Date date = resultSet.getDate("date");
                Time time=resultSet.getTime("time");
                Long reply=resultSet.getLong("id_reply");
               List<Long> to=find_to_list(aLong);

                Message m=new Message(0L, message, from, to,LocalDateTime.of(date.toLocalDate(), time.toLocalTime()));

                return Optional.of(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Iterable<Message> findAll() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from messages");
             ResultSet resultSet = statement.executeQuery()
        ) {
            Set<Message> msg = new HashSet<>();

            while (resultSet.next())
            {
                Long id=resultSet.getLong("id");
                String message=resultSet.getString("message");
                Long from=resultSet.getLong("from_id");
                java.sql.Date date = resultSet.getDate("data");
                Time time=resultSet.getTime("time");
                Long reply=resultSet.getLong("id_reply");
                List<Long> to=find_to_list(id);
               // to.add(52L);
                //System.out.println(to);
                Message m=new Message(0L, message, from, to,LocalDateTime.of(date.toLocalDate(), time.toLocalTime()));

                m.setId(id);
                msg.add(m);
            }
//            msg.add(new Message(15L, "aaaa", 49L,List.of(50L,51L), LocalDateTime.now(),52L));
            return msg;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer getSize() {
        return null;
    }
    private void save_messages_users(Message entity,Long l){
        String insertSQL = "insert into messagesusers (id_message,id_to_user) values(?,?)";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement=connection.prepareStatement(insertSQL))
        {
            statement.setLong(1,entity.getId());
            statement.setLong(2, l);

            int response = statement.executeUpdate();
           // return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Optional<Message> save(Message entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Message cannot be null.");
        }
      //  validator.validate(entity);

        String insertSQL = "insert into messages (message,from_id,data,time,id_reply) values(?,?,?,?,?) returning id";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement=connection.prepareStatement(insertSQL))
        {
            statement.setString(1,entity.getMessage());
            statement.setLong(2,entity.getFrom());
            java.sql.Date date =  java.sql.Date.valueOf(entity.getDate().toLocalDate());
            LocalTime time = entity.getDate().toLocalTime();
            Time sqlTime = Time.valueOf(time);
            statement.setDate(3,date);
            statement.setTime(4, Time.valueOf(time));
            statement.setLong(5,entity.getReply_id());
            ResultSet response = statement.executeQuery();
            if(response.next()){
                Long id=response.getLong("id");
                entity.setId(id);
                entity.getTo().forEach(o->save_messages_users(entity,o));

            System.out.println(entity.getId());
            return Optional.of(entity) ;
            }
            else return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Message> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Message> update(Message entity) {
        return Optional.empty();
    }
}
