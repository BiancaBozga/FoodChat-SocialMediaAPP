package com.example.demo2.persistence.dbrepos;




import com.example.demo2.domain.entities.FriendRequest;
import com.example.demo2.domain.entities.Friendship;
import com.example.demo2.domain.entities.Tuple;
import com.example.demo2.persistence.Repository;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendshipDBRepository implements Repository<Tuple<Long,Long>, Friendship> {
    private String url;
    private String username;
    private String password;

    public FriendshipDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }



    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> fr = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from friendships");
             ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                Date date = resultSet.getDate("date");
                Time time = resultSet.getTime("time");

                Friendship fri=new Friendship(LocalDateTime.of(date.toLocalDate(), time.toLocalTime()));
                fri.setId(new Tuple<>(id1,id2));

                fr.add(fri);

            }
            return fr;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer getSize() {
        return null;
    }

    @Override
    public Optional<Friendship> save(Friendship entity) throws IllegalArgumentException {
        String insertSQL="insert into friendships (id1,id2,date,time) values(?,?,?,?)";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement=connection.prepareStatement(insertSQL);)
        {
            statement.setLong(1,entity.getId().getLeft());
            statement.setLong(2,entity.getId().getRight());
            Date date = Date.valueOf(entity.getDate().toLocalDate());
            LocalTime time = entity.getDate().toLocalTime();
            statement.setDate(3,date);
            statement.setTime(4, Time.valueOf(time));
            int response=statement.executeUpdate();
            return response==0 ? Optional.of(entity) : Optional.empty();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Friendship> findOne(Tuple<Long, Long> longLongTuple) throws IllegalArgumentException {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from friendships " +
                    "where id1 = ? and id2=? ");

        ) {
            statement.setInt(1, Math.toIntExact(longLongTuple.getLeft()));
            statement.setInt(2,Math.toIntExact(longLongTuple.getRight()));
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                Long firstid = resultSet.getLong("id1");
                Long secondid = resultSet.getLong("id2");
                Date date = resultSet.getDate("date");
                Time time = resultSet.getTime("time");
                Friendship fr=new Friendship(LocalDateTime.of(date.toLocalDate(), time.toLocalTime()));
                fr.setId(new Tuple<>(firstid,secondid));

                return Optional.ofNullable(fr);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Friendship> delete(Tuple<Long, Long> longLongTuple) throws IllegalArgumentException {
        if (longLongTuple.getLeft() == null || longLongTuple.getRight()==null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        String deleteSQL="delete from friendships f where f.id1=? and f.id2=?";

        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(deleteSQL);

        ) {
            statement.setLong(1, longLongTuple.getLeft());
            statement.setLong(2, longLongTuple.getRight());


            Optional<Friendship> foundUser = findOne(longLongTuple);

            int response = 0;
            if (foundUser.isPresent()) {
                response = statement.executeUpdate();
            }

            return response == 0 ? Optional.empty() : foundUser;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Friendship> update(Friendship entity) throws IllegalArgumentException {
        return Optional.empty();
    }
    public void save_friend_request(Long u1,Long u2) throws IllegalArgumentException{
        String insertSQL="insert into friendrequests (id_user_from,id_user_to) values(?,?)";
        try (var connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement=connection.prepareStatement(insertSQL);)
        {
            statement.setLong(1,u1);
            statement.setLong(2,u2);


            int response=statement.executeUpdate();

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Iterable<FriendRequest> findAll_requests(Long idd) {
        Set<FriendRequest> fr = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from friendrequests f where f.id_user_to=?");

        ) {
            statement.setInt(1, Math.toIntExact(idd));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                Long id = resultSet.getLong("fr_id");
                Long id1 = resultSet.getLong("id_user_from");
                Long id2 = resultSet.getLong("id_user_to");
                String status=resultSet.getString("status");
                FriendRequest fri=new FriendRequest(id1,id2,status);
                fri.setId(id);



                fr.add(fri);

            }
            return fr;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void update_fr_req(FriendRequest entity){
        if (entity == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
//        validator.validate(entity);

        String updateSQL = "update friendrequests set status=? where fr_id=?";
        try(var connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement=connection.prepareStatement(updateSQL)
        ) {
           statement.setString(1,entity.getStatus());
           statement.setLong(2,entity.getId());

            int response = statement.executeUpdate();
//            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }


}
