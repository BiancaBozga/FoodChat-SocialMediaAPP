package com.example.demo2.business;



import com.example.demo2.Observable;
import com.example.demo2.Observer;
import com.example.demo2.domain.entities.User;
import com.example.demo2.domain.generaltypes.ObjectTransformer;
import com.example.demo2.exceptions.ValidationException;
import com.example.demo2.persistence.dbrepos.UserDBRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class UserService implements Observable {
    private List<Observer> ob=new ArrayList<>();
    private final UserDBRepository userRepository;

    public UserService(UserDBRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long addUser(String firstName, String lastName,String passwd) throws ValidationException {

        return userRepository.save(new User(firstName, lastName,passwd)).get().getId();
    }
    public void deleteUser(User u){
        userRepository.delete(u.getId());
    }
   public void updateUser(String firstName, String lastName,Long id,String passwd){
        User u=new User(firstName,lastName,passwd);
        u.setId(id);
        userRepository.update(u);
   }
   public boolean verifica(String p,Long id){
        return userRepository.verifica_parola(p,id);
   }
   public Optional<User> find(Long id){
        return userRepository.findOne(id);
   }
    public Collection<User> getAll() {
        return ObjectTransformer.iterableToCollection(userRepository.findAll());
    }
    public void update(User u){
        userRepository.update(u);
        notifyObservers();
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
