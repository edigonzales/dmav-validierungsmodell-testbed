package ch.interlis.testbed;

import java.util.Objects;

public class LogEvent {
    private LogEventType type;
    
    private String message;
    
    public LogEvent(LogEventType type, String message) {
        this.type = type;
        this.message = message;
    }
    
    public LogEventType getType() {
        return type;
    }

    public void setType(LogEventType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LogEvent [type=" + type + ", message=" + message + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LogEvent other = (LogEvent) obj;        
        return Objects.equals(message, other.message) && type == other.type;
    }
}
