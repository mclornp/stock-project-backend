package com.example;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRegistryActor extends AbstractActor {

  LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  public static class InitUser{
    private final String name;

    public InitUser() {
      this.name = "Player";
    }

    public InitUser(String name){
      this.name = name;
    }

    public String getName(){
      return this.name;
    }
  }

  //#user-case-classes
  public static class User {
    private final int balance;
    private final int id;
    private final String name;

    public User() {
      this.name = "Player";
      this.id = 1;
      this.balance = 100;
    }

    public User(String name, int id,int balance) {
      this.name = name;
      this.id = id;
      this.balance = balance;
    }

    public String getName() {
      return name;
    }

    public int getId(){
      return id;
    }

    public int getBalance(){
      return balance;
    }
  }

  public static class Users{
    private final List<User> users;

    public Users() {
      this.users = new ArrayList<>();
    }

    public Users(List<User> users) {
      this.users = users;
    }

    public List<User> getUsers() {
      return users;
    }
  }
//#user-case-classes

  static Props props() {
    return Props.create(UserRegistryActor.class);
  }

  private final List<User> users = new ArrayList<>();

  @Override
  public Receive createReceive(){
    return receiveBuilder()
            .match(UserRegistryMessages.GetUsers.class, getUsers -> getSender().tell(new Users(users),getSelf()))
            .match(UserRegistryMessages.CreateUser.class, createUser -> {

              InitUser recvdUser = createUser.getUser();

              // checking user with name already exists
              boolean exists = false;
              int id = 1;
              for(User u:users){
                if(u.getName().equals(recvdUser.getName())){
                 getSender().tell(new UserRegistryMessages.ActionPerformed(
                      String.format("User with name %s already exists.", recvdUser.getName())),getSelf()); 
                  exists = true;
                }
                id++;
              }

              User newUser = new User(recvdUser.getName(),id,100);

              if(!exists){
                users.add(newUser);
                getSender().tell(new UserRegistryMessages.ActionPerformed(
                        String.format("User %s created.", recvdUser.getName())),getSelf());
              }
            })
            .match(UserRegistryMessages.GetUser.class, getUser -> {
              getSender().tell(users.stream()
                      .filter(user -> user.getName().equals(getUser.getName()))
                      .findFirst(), getSelf());
            })
            .match(UserRegistryMessages.DeleteUser.class, deleteUser -> {
              users.removeIf(user -> user.getName().equals(deleteUser.getName()));
              getSender().tell(new UserRegistryMessages.ActionPerformed(String.format("User %s deleted.", deleteUser.getName())),
                      getSelf());

            }).matchAny(o -> log.info("received unknown message"))
            .build();
  }
}
