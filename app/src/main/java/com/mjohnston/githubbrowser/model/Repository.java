package com.mjohnston.githubbrowser.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mattjohnston on 4/14/17.
 */

public class Repository implements Serializable {



    protected String name;

    protected Owner owner;
    protected String description;
    protected Integer size;

    @SerializedName("updated_at")
    protected Date updatedAt;


    @SerializedName("forks_count")
    protected Integer numberForks;


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getOwnerName() {
        if (owner != null) {
            return owner.login;
        }
        else {
            return null;
        }
    }

    public String getOwnerAvatarUrl() {
        if (owner != null) {
            return owner.avatarUrl;
        }
        else {
            return null;
        }
    }

    public String getDescription() {
        return description;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getNumberForks() {
        return numberForks;
    }

    public Integer getNumberIssues() {
        return numberIssues;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @SerializedName("open_issues")
    protected Integer numberIssues;

    @SerializedName("html_url")
    protected String link;


    @SerializedName("stargazers_count")
    protected Integer stars;

    public Integer getStars() {
        return stars;
    }


    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getFormattedDate() {

        SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy");
        if (updatedAt != null) {
            return format.format(updatedAt);
        }
        else {
            return null;
        }
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getFormattedSize() {
        if (size != null) {

            if (size < 1000) {
                return size +" KB";
            }
            else if (size < 1000000) {
                return size/1000 + " MB";
            }
            else if (size < 1000000000) {
                return size/1000000 + " GB";
            }
            else {
                return size + "";
            }

        }
        else {
            return "0";
        }
    }

    protected class Owner implements Serializable {

        protected String login;

        @SerializedName("avatar_url")
        protected String avatarUrl;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Owner owner = (Owner) o;

            return login != null ? login.equals(owner.login) : owner.login == null;

        }

        @Override
        public int hashCode() {
            return login != null ? login.hashCode() : 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Repository that = (Repository) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (owner != null ? !owner.equals(that.owner) : that.owner != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (size != null ? !size.equals(that.size) : that.size != null) return false;
        if (numberForks != null ? !numberForks.equals(that.numberForks) : that.numberForks != null)
            return false;
        if (numberIssues != null ? !numberIssues.equals(that.numberIssues) : that.numberIssues != null)
            return false;
        return link != null ? link.equals(that.link) : that.link == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (numberForks != null ? numberForks.hashCode() : 0);
        result = 31 * result + (numberIssues != null ? numberIssues.hashCode() : 0);
        result = 31 * result + (link != null ? link.hashCode() : 0);
        return result;
    }
}
