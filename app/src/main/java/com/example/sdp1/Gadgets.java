package com.example.sdp1;

import java.util.ArrayList;

public class Gadgets {
    public ArrayList<Gadget> gadgets = new ArrayList();

    public void setGadget(Gadget gadget) {
        this.gadgets.add(gadget);
    }
    public ArrayList<Gadget> getGadgets(){
        return this.gadgets;
    }
}
