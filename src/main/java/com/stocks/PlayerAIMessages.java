package com.stocks;

import com.stocks.UserRegistryActor.User;

import java.io.Serializable;
import java.util.*;  
import com.fasterxml.jackson.databind.ObjectMapper;
import akka.actor.ActorRef;

public interface PlayerAIMessages {

    class ActionPerformed implements Serializable {
        private final String description;

        public ActionPerformed(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }


    class LookPlayerCompletion implements Serializable {

    }
    
    class SetActors implements Serializable{
        private final ActorRef clockActor;
        private final ActorRef brokerActor;
        private final ActorRef userRegistryActor;

        public SetActors(ActorRef clockActor,ActorRef brokerActor,ActorRef userRegistryActor){
            this.clockActor = clockActor;
            this.brokerActor = brokerActor;
            this.userRegistryActor = userRegistryActor;
        }

        public ActorRef getClockActor(){
            return clockActor;
        }

        public ActorRef getBrokerActor(){
            return brokerActor;
        }

        public ActorRef getUserRegistryActor(){
            return userRegistryActor;
        }

    }
}