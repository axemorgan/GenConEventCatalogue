package com.axemorgan.genconcatalogue.events;

import org.threeten.bp.ZonedDateTime;

public class EventBuilder {
    private String id;
    private String group;
    private String title;
    private String shortDescription;
    private String longDescription;
    private String eventType;
    private String gameSystem;
    private String rulesEdition;
    private int minimumPlayers;
    private int maximumPlayers;
    private String ageRequired;
    private String experienceRequired;
    private boolean materialsProvided;
    private ZonedDateTime startDate;
    private double duration;
    private ZonedDateTime endDate;
    private String gmNames;
    private String website;
    private String email;
    private boolean isTournament;
    private int roundNumber;
    private int totalRounds;
    private int cost;
    private String location;
    private String roomName;
    private String tableNumber;
    private int availableTickets;
    private String lastModified;

    public EventBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public EventBuilder setGroup(String group) {
        this.group = group;
        return this;
    }

    public EventBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public EventBuilder setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public EventBuilder setLongDescription(String longDescription) {
        this.longDescription = longDescription;
        return this;
    }

    public EventBuilder setEventType(String eventType) {
        this.eventType = eventType;
        return this;
    }

    public EventBuilder setGameSystem(String gameSystem) {
        this.gameSystem = gameSystem;
        return this;
    }

    public EventBuilder setRulesEdition(String rulesEdition) {
        this.rulesEdition = rulesEdition;
        return this;
    }

    public EventBuilder setMinimumPlayers(int minimumPlayers) {
        this.minimumPlayers = minimumPlayers;
        return this;
    }

    public EventBuilder setMaximumPlayers(int maximumPlayers) {
        this.maximumPlayers = maximumPlayers;
        return this;
    }

    public EventBuilder setAgeRequired(String ageRequired) {
        this.ageRequired = ageRequired;
        return this;
    }

    public EventBuilder setExperienceRequired(String experienceRequired) {
        this.experienceRequired = experienceRequired;
        return this;
    }

    public EventBuilder setMaterialsProvided(boolean materialsProvided) {
        this.materialsProvided = materialsProvided;
        return this;
    }

    public EventBuilder setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public EventBuilder setDuration(double duration) {
        this.duration = duration;
        return this;
    }

    public EventBuilder setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public EventBuilder setGmNames(String gmNames) {
        this.gmNames = gmNames;
        return this;
    }

    public EventBuilder setWebsite(String website) {
        this.website = website;
        return this;
    }

    public EventBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public EventBuilder setIsTournament(boolean isTournament) {
        this.isTournament = isTournament;
        return this;
    }

    public EventBuilder setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
        return this;
    }

    public EventBuilder setTotalRounds(int totalRounds) {
        this.totalRounds = totalRounds;
        return this;
    }

    public EventBuilder setCost(int cost) {
        this.cost = cost;
        return this;
    }

    public EventBuilder setLocation(String location) {
        this.location = location;
        return this;
    }

    public EventBuilder setRoomName(String roomName) {
        this.roomName = roomName;
        return this;
    }

    public EventBuilder setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
        return this;
    }

    public EventBuilder setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
        return this;
    }

    public EventBuilder setLastModified(String lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public Event create() {
        return new Event(id, group, title, shortDescription, longDescription, eventType, gameSystem,
                rulesEdition, minimumPlayers, maximumPlayers, ageRequired, experienceRequired,
                materialsProvided, startDate, duration, endDate, gmNames, website, email,
                isTournament, roundNumber, totalRounds, cost, location, roomName, tableNumber,
                availableTickets, lastModified);
    }
}