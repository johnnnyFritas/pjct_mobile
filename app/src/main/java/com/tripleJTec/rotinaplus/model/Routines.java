package com.tripleJTec.rotinaplus.model;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Routines {
    private String id;
    private String name;
    private String descricao;
    private String[] daysOfWeek;
    private String hour;
    private Boolean repeatable;
    private Boolean finished;
    private String user_id;

    public Routines(String id, String name, String descricao, String[] daysOfWeek, String hour, Boolean repeatable, Boolean finished, String user_id) {
        this.id = id;
        this.name = name;
        this.descricao = descricao;
        this.daysOfWeek = daysOfWeek;
        this.hour = hour;
        this.repeatable = repeatable;
        this.finished = finished;
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDaysOfWeek() {
        return String.join(", ", daysOfWeek);
    }

    public void setDaysOfWeek(String[] daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Boolean getRepeatable() {
        return repeatable;
    }

    public void setRepeatable(Boolean repeatable) {
        this.repeatable = repeatable;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
