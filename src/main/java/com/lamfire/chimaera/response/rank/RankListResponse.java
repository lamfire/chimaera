package com.lamfire.chimaera.response.rank;

import com.lamfire.chimaera.response.Response;
import com.lamfire.pandora.Item;

import java.util.List;

public class RankListResponse extends Response {

    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
