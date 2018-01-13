package com.axemorgan.genconcatalogue.events;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.threeten.bp.ZonedDateTime;

@SuppressWarnings("unused")
@Entity
public class Event {

    @NonNull
    @PrimaryKey
    private String id;

    @ColumnInfo(name = "group")
    private String group;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "short_description")
    private String shortDescription;

    @ColumnInfo(name = "long description")
    private String longDescription;

    @ColumnInfo(name = "event_type")
    private String eventType;

    @ColumnInfo(name = "game_system")
    private String gameSystem;

    @ColumnInfo(name = "rules_edition")
    private String rulesEdition;

    @ColumnInfo(name = "min_players")
    private int minimumPlayers;

    @ColumnInfo(name = "max_players")
    private int maximumPlayers;

    @ColumnInfo(name = "age_required")
    private String ageRequired;

    @ColumnInfo(name = "experience_required")
    private String experienceRequired;

    @ColumnInfo(name = "materials_provided")
    private boolean materialsProvided;

    @ColumnInfo(name = "start_date")
    private ZonedDateTime startDate;

    @ColumnInfo(name = "duration")
    private double duration;

    @ColumnInfo(name = "end_date")
    private ZonedDateTime endDate;

    @ColumnInfo(name = "gm_names")
    private String gmNames;

    @ColumnInfo(name = "website")
    private String website;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "is_tournament")
    private boolean isTournament;

    @ColumnInfo(name = "round_number")
    private int roundNumber;

    @ColumnInfo(name = "total_rounds")
    private int totalRounds;

    @ColumnInfo(name = "cost")
    private int cost;

    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "room_name")
    private String roomName;

    @ColumnInfo(name = "table_number")
    private String tableNumber;

    @ColumnInfo(name = "available_tickets")
    private int availableTickets;

    @ColumnInfo(name = "last_modified")
    private String lastModified;


    public Event(String id, String group, String title, String shortDescription, String longDescription,
                 String eventType, String gameSystem, String rulesEdition, int minimumPlayers, int maximumPlayers,
                 String ageRequired, String experienceRequired, boolean materialsProvided, ZonedDateTime startDate,
                 double duration, ZonedDateTime endDate, String gmNames, String website, String email, boolean isTournament,
                 int roundNumber, int totalRounds, int cost, String location, String roomName, String tableNumber,
                 int availableTickets, String lastModified) {
        this.id = id;
        this.group = group;
        this.title = title;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.eventType = eventType;
        this.gameSystem = gameSystem;
        this.rulesEdition = rulesEdition;
        this.minimumPlayers = minimumPlayers;
        this.maximumPlayers = maximumPlayers;
        this.ageRequired = ageRequired;
        this.experienceRequired = experienceRequired;
        this.materialsProvided = materialsProvided;
        this.startDate = startDate;
        this.duration = duration;
        this.endDate = endDate;
        this.gmNames = gmNames;
        this.website = website;
        this.email = email;
        this.isTournament = isTournament;
        this.roundNumber = roundNumber;
        this.totalRounds = totalRounds;
        this.cost = cost;
        this.location = location;
        this.roomName = roomName;
        this.tableNumber = tableNumber;
        this.availableTickets = availableTickets;
        this.lastModified = lastModified;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    public String getGroup() {
        return group == null ? "" : group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getGameSystem() {
        return gameSystem;
    }

    public void setGameSystem(String gameSystem) {
        this.gameSystem = gameSystem;
    }

    public String getRulesEdition() {
        return rulesEdition;
    }

    public void setRulesEdition(String rulesEdition) {
        this.rulesEdition = rulesEdition;
    }

    public int getMinimumPlayers() {
        return minimumPlayers;
    }

    public void setMinimumPlayers(int minimumPlayers) {
        this.minimumPlayers = minimumPlayers;
    }

    public int getMaximumPlayers() {
        return maximumPlayers;
    }

    public void setMaximumPlayers(int maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
    }

    public String getAgeRequired() {
        return ageRequired;
    }

    public void setAgeRequired(String ageRequired) {
        this.ageRequired = ageRequired;
    }

    public String getExperienceRequired() {
        return experienceRequired;
    }

    public void setExperienceRequired(String experienceRequired) {
        this.experienceRequired = experienceRequired;
    }

    public boolean getMaterialsProvided() {
        return materialsProvided;
    }

    public void setMaterialsProvided(boolean materialsProvided) {
        this.materialsProvided = materialsProvided;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public String getGmNames() {
        return gmNames;
    }

    public void setGmNames(String gmNames) {
        this.gmNames = gmNames;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isTournament() {
        return isTournament;
    }

    public void setTournament(boolean tournament) {
        isTournament = tournament;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public void setTotalRounds(int totalRounds) {
        this.totalRounds = totalRounds;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRoomName() {
        return roomName == null ? "" : roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return title + " " + id;
    }
}
