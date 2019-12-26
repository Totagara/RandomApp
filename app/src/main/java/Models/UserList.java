package Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserList implements Parcelable {
    Integer listId;
    String listName;
    Integer itemsCount;
    List<String> items;
    String lastModified;

    //Try expansion
    Boolean isExpanded = false;
    public Boolean getExpanded() {
        return isExpanded;
    }

    public void setExpanded(Boolean expanded) {
        isExpanded = expanded;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public Integer getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(Integer itemsCount) {
        this.itemsCount = itemsCount;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public Integer getListId() {
        return listId;
    }

    public void setListId(Integer userId) {
        this.listId = userId;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }



    public UserList(Integer listId, String listName, List<String> items, Integer itemsCount, String lastModified) {
        this.listName = listName;
        this.itemsCount = itemsCount;
        this.items = items;
        this.listId = listId;
        this.lastModified = lastModified;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
