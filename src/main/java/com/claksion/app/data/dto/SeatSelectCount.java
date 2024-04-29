package com.claksion.app.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatSelectCount {
    private String seatId;
    private int limit;

    private static final int END = 0;

    public SeatSelectCount(String seatId, int limit) {
        this.seatId = seatId;
        this.limit = limit;
    }

    public synchronized void decrease(){
        this.limit--;
    }

    public boolean end(){
        return this.limit == END;
    }
}