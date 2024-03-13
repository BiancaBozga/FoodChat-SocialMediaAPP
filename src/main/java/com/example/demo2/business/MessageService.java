package com.example.demo2.business;



import com.example.demo2.Observer;
import com.example.demo2.domain.entities.Message;
import com.example.demo2.domain.entities.User;
import com.example.demo2.domain.generaltypes.ObjectTransformer;
import com.example.demo2.persistence.Repository;
import com.example.demo2.persistence.dbrepos.FriendshipDBRepository;

import java.util.*;

public class MessageService implements com.example.demo2.Observable {
    private List<com.example.demo2.Observer> ob=new ArrayList<>();
    private final Repository<Long, Message> messageRepository;
    private final Repository<Long, User> userRepository;
    private final FriendshipDBRepository fr_repo;

    public MessageService(Repository<Long, Message> messageRepository, Repository<Long, User> userRepository, FriendshipDBRepository fr_repo) {
        this.messageRepository = messageRepository;
        this.userRepository=userRepository;
        this.fr_repo=fr_repo;
    }
    public void save(Long u1,List<Long> u2,String msg,Long id_reply){
//        List<Long> l=new ArrayList<>();
//        l.add(u2);
        messageRepository.save(Message.create(u1,u2,msg,id_reply));
        notifyObservers();
    }
    public Optional<Message> getOne(Long id) {
        return messageRepository.findOne(id);
    }
    public Collection<Message> getAll() {
        return ObjectTransformer.iterableToCollection(messageRepository.findAll());
    }
    public Collection<Message> get_conversation(Long id_user1,Long id_user2){
        Collection<Message> l=  getAll();
        Collection<Message> l1 = l.stream()
                .filter(message -> (message.getFrom().equals(id_user1) && message.getTo().contains(id_user2)) ||
                        (message.getFrom().equals(id_user2) && message.getTo().contains(id_user1))
                ).toList();

        //l1.forEach(m-> System.out.println(m.getId()));
        List<Message> l2=l1.stream().toList();
        l2=l2.stream().sorted(Comparator.comparing(Message::getDate)).toList();
        return new ArrayList<>(l2);
    }
    public Collection<Message> get_conversation_2(Long id_user1,List<Long> id_user2){
        Collection<Message> l=  getAll();
        Collection<Message> l1 = new ArrayList<>();

        for (Message message : l) {
            System.out.println(message);
              int ok=1,ok2=1;
               for(Long id:id_user2)
                   if(!message.getTo().contains(id))
                       ok=0;
               if(!Objects.equals(message.getFrom(), id_user1))
                   ok=0;

               if(!message.getFrom().equals(id_user2.get(0)))
                   ok2=0;
               if(!message.getTo().contains(id_user1))
                   ok2=0;
               if(ok!=0 || ok2!=0)
                   l1.add(message);
            }

        //l1.forEach(m-> System.out.println(m.getId()));
//        List<Message> l2=l1.stream().toList();
//        l2=l2.stream().sorted(Comparator.comparing(Message::getDate)).toList();
        System.out.println(l1);
        return new ArrayList<>(l1);
    }
    public Collection<Message> get_conversation_from_one(Long id_user1){
        Collection<Message> l=  getAll();
        Collection<Message> l1 = l.stream()
                .filter(message -> (message.getFrom().equals(id_user1) ||
                         message.getTo().contains(id_user1)))
                .toList();

        //l1.forEach(m-> System.out.println(m.getId()));
        List<Message> l2=l1.stream().toList();
        l2=l2.stream().sorted(Comparator.comparing(Message::getDate)).toList();
        return new ArrayList<>(l2);
    }
    public void send_friend_request(Long u1,Long u2){
        fr_repo.save_friend_request(u1,u2);
        notifyObservers();
    }



    @Override
    public void addObserver(com.example.demo2.Observer o) {
        ob.add(o);
    }

    @Override
    public void removeObsrver(com.example.demo2.Observer o) {
        ob.remove(o);
    }

    @Override
    public void notifyObservers() {
        ob.forEach(Observer::update);
    }
}
