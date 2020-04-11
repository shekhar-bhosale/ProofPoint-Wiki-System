package com.proofpoint.wikisystem.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder @Getter @Setter
public class Attachment extends Component {

    private String filename;

  /*  public User getOwner(){
        return this.owner;
    }

    public void setOwner(User owner){
        this.owner = owner;
    }*/

    public void create(){
        System.out.println("Creating Attachment");
    }

    public void delete(){
        System.out.println("Deleting Attachment");
    }

    public void update(){
        System.out.println("Updating Attachment");
    }
}