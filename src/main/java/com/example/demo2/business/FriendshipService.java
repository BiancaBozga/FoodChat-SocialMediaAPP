package com.example.demo2.business;



import com.example.demo2.Observable;
import com.example.demo2.Observer;
import com.example.demo2.domain.entities.FriendRequest;
import com.example.demo2.domain.entities.Friendship;
import com.example.demo2.domain.entities.Tuple;
import com.example.demo2.domain.entities.User;
import com.example.demo2.domain.generaltypes.ObjectTransformer;
import com.example.demo2.persistence.Repository;
import com.example.demo2.persistence.dbrepos.FriendshipDBRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


public class FriendshipService implements Observable {
    private List<Observer> ob=new ArrayList<>();
    FriendshipDBRepository repo;
    UserService serv;
    Repository repoUtilizatori;

    private void loadFriends() {
        for (Friendship friendship : getAll()) {
            Long leftUserId = friendship.getId().getLeft();
            Long rightUserId = friendship.getId().getRight();

            // Retrieve the users from the repositories as Optionals
            Optional<User> optionalUser1 = repoUtilizatori.findOne(leftUserId);
            Optional<User> optionalUser2 = repoUtilizatori.findOne(rightUserId);

            // If both users exist, add them as friends
            optionalUser1.ifPresent(user1 -> {
                optionalUser2.ifPresent(user2 -> {
                    user1.addFriend(user2);
                    user2.addFriend(user1);
                });
            });
        }
    }



    public FriendshipService(FriendshipDBRepository repo, Repository repoUtilizatori,UserService s) {
        this.repo = repo;
        this.repoUtilizatori = repoUtilizatori;
        this.serv=s;
        loadFriends();
    }


    public Optional<Friendship> add(Friendship entity) {
        Long id1 = entity.getId().getLeft();
        Long id2 = entity.getId().getRight();
        Optional<User> user1 = repoUtilizatori.findOne(id1);
        if (user1.isEmpty()) {
            throw new IllegalArgumentException("Nu exista user cu id-ul " + id1);
        }
        Optional<User> user2 = repoUtilizatori.findOne(id2);
        if (user2.isEmpty()) {
            throw new IllegalArgumentException("Nu exista user cu id-ul " + id2);
        }
        Optional<Friendship> f = repo.save(entity);


        user1.ifPresent(u1 -> {
            user2.ifPresent(u2 -> {
                u1.addFriend(u2);
                u2.addFriend(u1);
            });
        });

        notifyObservers();
        user1.ifPresent(serv::update);
        user2.ifPresent(serv::update);
//        notifyObservers();

        return f;
    }


    public Optional<Friendship> delete(Tuple<Long, Long> longLongTuple) {
        Long id1 = longLongTuple.getLeft();
        Long id2 = longLongTuple.getRight();
        Optional<User> u1 = repoUtilizatori.findOne(id1);
        Optional<User> u2 = repoUtilizatori.findOne(id2);

        if (u1.isEmpty() || u2.isEmpty()) {
            throw new IllegalArgumentException("User inexistent!");
        }

        Optional<Friendship> deleted = repo.delete(longLongTuple);
        if (deleted.isPresent()) {
            // Extract the User objects from Optional and call removeFriend
            u1.ifPresent(user1 -> {
                u2.ifPresent(user2 -> {
                    user1.removeFriend(user2);
                    user2.removeFriend(user1);
                });
            });
        }

        notifyObservers();
        return deleted;
    }


    public Optional<Friendship> getEntityById(Tuple<Long, Long> longLongTuple) {

        return repo.findOne(longLongTuple);
    }

    public Collection<FriendRequest> find_all_req(Long u){
        return ObjectTransformer.iterableToCollection(repo.findAll_requests(u));
    }
   public void update_fr_req(Long id,Long id1,Long id2,String status){
       FriendRequest fr=new FriendRequest(id1,id2,status);
       fr.setId(id);
       repo.update_fr_req(fr);
       notifyObservers();
   }

    public Collection<Friendship> getAll() {
        return (Collection<Friendship>) repo.findAll();
    }


    @Override
    public void addObserver(Observer o) {
        ob.add(o);
    }

    @Override
    public void removeObsrver(Observer o) {
        ob.remove(o);
    }

    @Override
    public void notifyObservers() {
        ob.forEach(Observer::update);
    }
}
