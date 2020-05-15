package com.example.sdp1;

import android.net.Uri;

public class Gadget {
    private Uri gadgetUri;

    Gadget(Uri gadgetUri){
        this.setGadgetUri(gadgetUri);
    }
    private void setGadgetUri(Uri gadgetUri){
        this.gadgetUri = gadgetUri;
    }
    public Uri getGadgetUri(){
        return this.gadgetUri;
    }
}
