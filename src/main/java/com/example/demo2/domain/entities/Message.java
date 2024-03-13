package com.example.demo2.domain.entities;



import com.example.demo2.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long>{

    private String message;
    private Long from;
    private List<Long> to;
   private LocalDateTime date;
    private Long reply_id;



    public String getMessage() {
        return message;
    }

    public Long getFrom() {
        return from;
    }

    public List<Long> getTo() {
        return to;
    }

    public LocalDateTime getDate() {
        return date;
    }


    public Long getReply_id() {
        return reply_id;
    }



    public Message(Long aLong, String message, Long from, List<Long> to,LocalDateTime date) {
        super(aLong);
        this.from= from;
        this.to = to;
        this.message = message;
        this.date =  date;
        this.reply_id=null;
    }
    public Message(Long aLong,String message,Long from,List<Long> to,LocalDateTime date,Long reply) {
        super(aLong);
        this.from= from;
        this.to = to;
        this.message = message;
        this.date = date;

        this.reply_id=reply;
    }
    public static Message create(Long fromUser, List<Long> toUser, String message,Long id_reply) throws ValidationException {
        String error = "";
        if (fromUser == null) {
            error += "The user who sends the message mustn't be null!\n";
        }
        if (toUser == null) {
            error += "The user who receives the message mustn't be null!\n";
        }
        if (message == null || message.equals("")) {
            error += "The message mustn't be null!\n";
        }
        if (message != null && message.length() > 200) {
            error += "The message limit is 200 characters!\n";
        }
        if (!error.equals("")) {
            throw new ValidationException(error);
        }

        return new Message(0L, message, fromUser, toUser, LocalDateTime.now(),id_reply);
    }
//    @Override
//    public String toString() {
//        return "ID : " + id + " | " +
//                "From : '" + from + "' | " +
//                "Message : '" + message + "' | " +
//                "Friends list : [ " +
//                to.stream()
//                        .map(user -> user.getId().toString())
//                        .collect(Collectors.joining(" , ")) + " ].";
//    }
}
