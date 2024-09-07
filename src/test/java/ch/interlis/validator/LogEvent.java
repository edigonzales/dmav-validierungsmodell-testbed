package ch.interlis.validator;

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
}
