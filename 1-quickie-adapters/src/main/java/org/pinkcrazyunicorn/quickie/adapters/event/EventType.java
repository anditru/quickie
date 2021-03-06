package org.pinkcrazyunicorn.quickie.adapters.event;

public final class EventType {
    private final String name;

    public EventType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventType eventType = (EventType) o;

        return name.equals(eventType.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
